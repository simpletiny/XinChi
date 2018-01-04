var salesLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCompanies = ko.observableArray([]);
	self.createCompany = function() {
		window.location.href = self.apiurl
				+ "templates/client/company-creation.jsp";
	};
	self.clientArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化', '呼伦贝尔',
			'伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭' ];
	self.clients = ko.observable({
		total : 0,
		items : []
	});
	self.storeTypes = [ '未知', '门店', '写字间', '其它 ' ];
	self.mainBusinesses = [ '未知', '组团', '地接', '同业', '综合' ];
	self.backLevels = [ '未知', '立即', '及时', '拖拉', '费劲', '定期', '垃圾', '布莱' ];
	self.status = [ 'N', 'Y' ];
	self.statusMapping = {
		'N' : '正常',
		'Y' : '已停用'
	};

	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("N");

	self.relates = [ 'N', 'Y' ];
	self.relatesMapping = {
		'N' : '未关联',
		'Y' : '已关联'
	};

	self.chosenRelates = ko.observableArray([]);
	self.totalCompanies = ko.observable();

	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
		var pub = new Object();
		pub.user_name = "公开";
		pub.pk = "public";
		self.sales.push(pub);
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");
		var param = $("form").serialize();
		if (!$("#txt-public-flg").is(':checked'))
			param += "&client.public_flg=N"
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;

		$.getJSON(self.apiurl + 'client/searchCompanyByPage', param, function(
				data) {
			self.clients(data.clients);
			$(".rmb").formatCurrency();

			self.totalCompanies(data.page.total);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			endLoadingIndicator();
		});
	};

	self.changeStatus = function() {
		self.refresh();
		return true;
	};

	self.changeRelate = function() {
		self.refresh();
		return true;
	};

	self.stopCompany = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择财务主体");
			return;
		} else if (self.chosenCompanies().length > 1) {
			fail_msg("只能选择一个财务主体");
			return;
		} else {
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '确认停用该财务主体吗?',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("停用中");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'client/deleteCompany',
							data : "company_pks=" + self.chosenCompanies()
						}).success(function(str) {
							if (str == "success") {
								self.refresh();
								self.chosenCompanies.removeAll();
								endLoadingIndicator();
							} else {
								fail_msg("停用失败，请联系管理员！");
							}
						});
					}
				}
			});
		}
	};
	self.deleteCompany = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择财务主体");
			return;
		} else if (self.chosenCompanies().length > 1) {
			fail_msg("只能选择一个财务主体");
			return;
		} else {
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '注意：删除财务主体，会将此财务主体下的客户一并删除。如果你知道自己在做什么，请点确认?',
					btns : 2,
					type : 7,
					btn : [ '确认', '不了' ],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("删除中...");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'client/deleteCompanyReally',
							data : "client_pk=" + self.chosenCompanies()
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenCompanies.removeAll();
							} else if (str == "has_order") {
								fail_msg("此财务主体下存在订单，不能删除，请选择停用。");
							} else {
								fail_msg("删除失败，请联系管理员");
							}
						});
					}
				}
			});
		}
	};
	self.chosenUser = ko.observable("");

	/**
	 * 调整财务主体所属销售
	 */
	self.changeSales = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择公司");
			return;
		} else {
			salesLayer = $.layer({
				type : 1,
				title : [ '修改财务主体销售', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '600px', '400px' ],
				offset : [ '200px', '' ],
				scrollbar : true,
				page : {
					dom : '#edit-sale'
				},
				end : function() {

				}
			});
		}
	};

	self.doChangeSale = function() {
		if (self.chosenUser() == "") {
			fail_msg("请选择销售！");
			return;
		}

		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认将选中的财务主体移至新销售名下吗?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					var data = {
						company_pks : self.chosenCompanies(),
						sale_pk : self.chosenUser()
					};

					startLoadingSimpleIndicator("转移中...");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'client/changeClientSales',
						traditional : true,
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(salesLayer);
							self.refresh();
							self.chosenCompanies.removeAll();
							self.chosenUser("");
						} else {
							fail_msg("转移失败，请联系管理员！");
						}
					});
				}
			}
		});
	};

	self.doCancelChangeSale = function() {
		layer.close(salesLayer);
	};

	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editCompany = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择公司");
			return;
		} else if (self.chosenCompanies().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenCompanies().length == 1) {
			window.location.href = self.apiurl
					+ "templates/client/company-edit.jsp?key="
					+ self.chosenCompanies()[0];
		}
	};
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 20;
	self.pageNums = ko.observableArray();
	self.totalCount = ko.observable(1);
	self.startIndex = ko.computed(function() {
		return (self.currentPage() - 1) * self.perPage;
	});

	self.resetPage = function() {
		self.currentPage(1);
	};

	self.previousPage = function() {
		if (self.currentPage() > 1) {
			self.currentPage(self.currentPage() - 1);
			self.refreshPage();
		}
	};

	self.nextPage = function() {
		if (self.currentPage() < self.pageNums().length) {
			self.currentPage(self.currentPage() + 1);
			self.refreshPage();
		}
	};

	self.turnPage = function(pageIndex) {
		self.currentPage(pageIndex);
		self.refreshPage();
	};

	self.setPageNums = function(curPage) {
		var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self
				.totalCount();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.refresh();
	};
	// end pagination
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
