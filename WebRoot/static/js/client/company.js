var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCompanies = ko.observableArray([]);
	self.createCompany = function() {
		window.location.href = self.apiurl + "templates/client/company-creation.jsp";
	};
	self.clientArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化', '呼伦贝尔', '伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭' ];
	self.clients = ko.observable({
		total : 0,
		items : []
	});
	self.status = [ '正常', '已停用' ];
	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("正常");
	
	
	self.relates = [ '未关联', '已关联' ];
	self.chosenRelates = ko.observableArray([]);

	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	self.sales_name = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
		self.sales_name.push("公开");
		$(self.sales()).each(function(idx, data) {
			self.sales_name.push(data.user_name);
		});
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");
		var param = $("form").serialize() + "&company_status=" + self.chosenStatus()+"&relate_status="+self.chosenRelates();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'client/searchCompanyByPage', param, function(data) {
			self.clients(data.clients);

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
			window.location.href = self.apiurl + "templates/client/company-edit.jsp?key=" + self.chosenCompanies()[0];
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
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self.totalCount();
		var pageNums = [];
		for ( var i = startPage; i <= endPage; i++) {
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
