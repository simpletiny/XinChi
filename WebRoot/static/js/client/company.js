var salesLayer;
var levelLayer;
var commentLayer;
var employeeLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCompanies = ko.observableArray([]);
	self.createCompany = function() {
		window.location.href = self.apiurl + "templates/client/company-creation.jsp";
	};
	self.client = ko.observable({});
	self.clients = ko.observable({
		total: 0,
		items: []
	});
	self.storeTypes = ['未知', '门店', '写字间', '其它 '];
	self.mainBusinesses = ['其它', '组团', '地接', '同业', '综合'];
	self.backLevels = ['其它', '立即', '及时', '拖拉', '费劲', '定期', '垃圾', '布莱'];
	self.marketLevels = ['未知', '主导级', '引领级', '普通级', '跟随级', '玩闹级'];
	self.status = ['N', 'Y'];
	self.talkLevels = ['核心', '主力', '市场', '排斥'];
	self.chosenTalkLevels = ko.observableArray([]);
	self.chosenTalkLevels.push("核心");
	self.chosenTalkLevels.push("主力");
	self.chosenTalkLevels.push("市场");
	self.chosenTalkLevels.push("排斥");
	self.statusMapping = {
		'N': '正常',
		'Y': '已停用'
	};

	// self.chosenMainBusinesses = ko.observableArray([]);
	// self.chosenMainBusinesses.push("综合");
	// self.chosenMainBusinesses.push("组团");

	self.chosenStatus = ko.observable('N');

	self.relates = ['N', 'Y'];
	self.relatesMapping = {
		'N': '未关联',
		'Y': '已关联'
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

	self.clientCount = ko.observable({
		oneYearorderCnt: 0,
		moreYearorderCnt: 0
	});
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");
		var param = $("#form-search").serialize();

		if ($(":radio[name='client.main_businesses']:checked").val() == "全部") {
			param += "&client.main_businesses=组团&client.main_businesses=综合&client.main_businesses=地接&client.main_businesses=同业&client.main_businesses=其它";
		}
		if ($("input[name='client.public_flgs']:checked").val() == "A") {
			param += "&client.public_flgs=Y&client.public_flgs=N";
		}

		$.getJSON(self.apiurl + 'client/searchClinetCount', param, function(data) {
			self.clientCount(data.clientCount);
		});

		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'client/searchCompanyByPage', param, function(data) {
			self.clients(data.clients);
			$(".rmb").formatCurrency();

			self.totalCompanies(data.page.total);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			endLoadingIndicator();
		});
	};

	self.chkMainBusinessRad = function(data, event) {
		$(":checkbox[name='client.main_businesses']").attr("checked", false);
		self.refresh();
		return true;
	}
	self.chkMainBusinessChk = function(data, event) {
		$(":radio[name='client.main_businesses']").attr("checked", false);
		self.refresh();
		return true;
	}
	self.changeStatus = function() {
		self.refresh();
		return true;
	};

	self.changeRelate = function() {
		self.refresh();
		return true;
	};

	// 客户评级
	self.setClientLevel = function() {

		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择客户财务主体");
			return;
		} else if (self.chosenCompanies().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenCompanies().length == 1) {
			$.getJSON(self.apiurl + 'client/searchOneCompany', {
				client_pk: self.chosenCompanies()[0]
			}, function(data) {
				if (data.client) {
					self.client(data.client);
				} else {
					fail_msg("财务主体不存在！");
				}
			}).fail(function(reason) {
				fail_msg(reason.responseText);
			});

			levelLayer = $.layer({
				type: 1,
				title: ['客户评级', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['900px', '150px'],
				offset: ['', ''],
				scrollbar: true,
				page: {
					dom: '#client-level'
				},
				end: function() {

				}
			});
		}
	};

	self.doSetClientLevel = function() {
		var param = $("#form-level").serialize();
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type: "POST",
			url: self.apiurl + 'client/pureUpdateCompany',
			data: param
		}).success(function(str) {
			layer.close(levelLayer);
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
			} else {
				fail_msg(str);
			}
		});
	};
	self.cancelSetClientLevel = function() {
		layer.close(levelLayer);
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
				area: ['auto', 'auto'],
				dialog: {
					msg: '确认停用该财务主体吗?',
					btns: 2,
					type: 4,
					btn: ['确认', '取消'],
					yes: function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("停用中");
						$.ajax({
							type: "POST",
							url: self.apiurl + 'client/deleteCompany',
							data: "company_pks=" + self.chosenCompanies()
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
				area: ['auto', 'auto'],
				dialog: {
					msg: '注意：删除财务主体，会将此财务主体下的客户一并删除。如果你知道自己在做什么，请点确认?',
					btns: 2,
					type: 7,
					btn: ['确认', '不了'],
					yes: function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("删除中...");
						$.ajax({
							type: "POST",
							url: self.apiurl + 'client/deleteCompanyReally',
							data: "client_pk=" + self.chosenCompanies()
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
	self.chosenUser = ko.observableArray([]);

	/**
	 * 调整财务主体所属销售
	 */
	self.changeSales = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择公司");
			return;
		} else {
			salesLayer = $.layer({
				type: 1,
				title: ['修改财务主体销售', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['600px', '400px'],
				offset: ['200px', ''],
				scrollbar: true,
				page: {
					dom: '#edit-sale'
				},
				end: function() {

				}
			});
		}
	};
	self.checkSale = function() {
		if (self.chosenUser().contains("public")) {
			self.chosenUser.removeAll();
			self.chosenUser.push("public");
		};
		return true;
	}
	self.doChangeSale = function() {
		if (self.chosenUser().length == 0) {
			fail_msg("请选择销售！");
			return;
		}

		$.layer({
			area: ['auto', 'auto'],
			dialog: {
				msg: '确认将选中的财务主体移至新销售名下吗?',
				btns: 2,
				type: 4,
				btn: ['确认', '取消'],
				yes: function(index) {
					layer.close(index);
					var data = {
						company_pks: self.chosenCompanies(),
						sale_pks: self.chosenUser()
					};
					startLoadingSimpleIndicator("转移中...");
					$.ajax({
						type: "POST",
						url: self.apiurl + 'client/changeClientSales',
						traditional: true,
						data: data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(salesLayer);
							self.refresh();
							self.chosenCompanies.removeAll();
							self.chosenUser.removeAll();
						} else {
							fail_msg("转移失败，请联系管理员！");
						}
					});
				}
			}
		});
	};

	self.keepMySide = function() {
		let user_pk = $("#user-pk").val();
		$.layer({
			area: ['auto', 'auto'],
			dialog: {
				msg: '确认将选中的财务主体移至自己名下吗?',
				btns: 2,
				type: 4,
				btn: ['确认', '取消'],
				yes: function(index) {
					layer.close(index);
					var data = {
						company_pks: self.chosenCompanies(),
						sale_pks: user_pk
					};
					startLoadingSimpleIndicator("转移中...");
					$.ajax({
						type: "POST",
						url: self.apiurl + 'client/changeClientSales',
						traditional: true,
						data: data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(salesLayer);
							self.refresh();
							self.chosenCompanies.removeAll();
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

	self.employees = ko.observableArray([]);
	self.checkEmployee = function(data) {

		$.getJSON(self.apiurl + 'client/searchEmployeeByFinancialBodyPk', {
			financial_body_pk: data.pk
		}, function(data) {
			if (data.employees) {
				self.employees(data.employees);
			} else {
				fail_msg("不存在客户员工！");
			}
			employeeLayer = $.layer({
				type: 1,
				title: ['客户员工详情', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['500px', '450px'],
				offset: ['200px', ''],
				scrollbar: true,
				page: {
					dom: '#employee-detail'
				},
				end: function() {

				}
			});

		}).fail(function(reason) {
			fail_msg(reason.responseText);
		});

	}

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
	self.company = ko.observable({});
	// 添加/修改备注
	self.editComment = function(client_pk) {
		$.getJSON(self.apiurl + 'client/searchOneCompany', {
			client_pk: client_pk
		}, function(data) {
			self.company(data.client);
			commentLayer = $.layer({
				type: 1,
				title: ['备注', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['500px', '300px'],
				offset: ['', ''],
				scrollbar: true,
				page: {
					dom: '#comment-edit'
				},
				end: function() {
					console.log("Done");
				}
			});

		});
	};

	self.cancelEditComment = function() {
		layer.close(commentLayer);
		$("#txt-comment").val('');
	};

	self.updateComment = function() {
		var client_pk = self.company().pk;
		var comment = $("#txt-comment").val();
		var data = "client.pk=" + client_pk + "&" + "client.comment=" + comment;

		startLoadingIndicator("保存中");
		$.ajax({
			type: "POST",
			url: self.apiurl + "client/pureUpdateCompany",
			data: data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
				layer.close(commentLayer);
				$("#txt-comment").val('');
			}
		});
	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 100;
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
