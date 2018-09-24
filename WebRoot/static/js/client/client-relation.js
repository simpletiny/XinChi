var todoLayer;
var levelLayer;
var quitLayer;
var connectInfoLayer;
var ClientContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.relations = ko.observable({
		total : 0,
		items : []
	});
	// 维度
	self.level = [ '新增级', '忽略级', '尝试级', '市场级', '朋友级', '主力级', '核心级' ];
	// 关系度
	self.relationLevel = [ '核心级', '主力级', '老铁级', '市场级', '尝试级', '新增级' ];

	// 市场力
	self.marketLevel = [ '未知', '主导级', '引领级', '普通级', '跟随级', '玩闹级' ];
	// 回款誉
	self.backLevel = [ '未知', '提前', '及时', '定期', '拖拉', '费劲', '垃圾' ];
	self.chosenLevel = ko.observableArray([ '关系度' ]);

	self.connectTypeMapping = {
		"ORDER" : "订单",
		"VISIT" : "拜访",
		"TOUCH" : "电联",
		"INCOMING" : "咨询"
	};

	self.chosenRelationLevel = ko.observableArray([]);
	self.chosenBackLevel = ko.observableArray([]);
	self.chosenMarketLevel = ko.observableArray([]);

	self.sortTypes = [ '1', '2', '3' ];
	self.sortTypeMapping = {
		'1' : '账期倒序',
		'2' : '交流日期',
		'3' : '订单排序'
	};

	self.chosenSortType = ko.observable('1');

	self.changeSortType = function() {
		self.refresh();
		return true;
	};

	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		startLoadingSimpleIndicator("加载中...");
		$.getJSON(self.apiurl + 'client/searchRelationsByPage', param,
				function(data) {
					self.relations(data.relations);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
					$(".rmb").formatCurrency();
					$(".detail").showDetail();
					endLoadingIndicator();
				});
	};

	self.saleScore = ko.observable();
	self.todayPoint = ko.observable();

	self.potential = ko.observable({});
	self.meter = ko.observable({
		receivable : 0,
		warning : 0,
		score : 0,
		month : 0
	});
	self.workOrder = ko.observable({});
	self.accurateSale = ko.observable({});

	self.searchClientSummary = function() {
		var param = $("form").serialize();
		$.getJSON(self.apiurl + 'client/searchClientSummary', param, function(
				data) {
			self.potential(data.potential);
			self.meter(data.meter);
			self.workOrder(data.workOrder);
			self.accurateSale(data.accurateSale);

			self.saleScore(data.sale_score);
			self.todayPoint(data.today_point);
			$(".rmb").formatCurrency();
		});
	};
	self.chosenEmployee = ko.observableArray([]);
	self.createVisit = function() {
		if (self.chosenEmployee().length == 0) {
			fail_msg("请选择客户");
			return;
		} else if (self.chosenEmployee().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenEmployee().length == 1) {
			window.location.href = self.apiurl
					+ "templates/client/visit-creation.jsp?key="
					+ self.chosenEmployee();
		}
	};
	self.createEmployee = function() {
		window.location.href = self.apiurl
				+ "templates/client/employee-creation.jsp"
	}
	// 新增精推
	self.createAccurateSale = function() {
		if (self.chosenEmployee().length == 0) {
			fail_msg("请选择客户");
			return;
		} else if (self.chosenEmployee().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenEmployee().length == 1) {
			window.location.href = self.apiurl
					+ "templates/client/accurate-sale-creation.jsp?key="
					+ self.chosenEmployee();
		}
	};
	// 新增电联
	self.createMobileTouch = function() {
		if (self.chosenEmployee().length == 0) {
			fail_msg("请选择客户");
			return;
		} else if (self.chosenEmployee().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenEmployee().length == 1) {
			window.location.href = self.apiurl
					+ "templates/client/give-me-a-call.jsp?key="
					+ self.chosenEmployee();
		}
	};
	// 新增被动咨询
	self.createIncomingCall = function() {
		if (self.chosenEmployee().length == 0) {
			fail_msg("请选择客户");
			return;
		} else if (self.chosenEmployee().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenEmployee().length == 1) {
			window.location.href = self.apiurl
					+ "templates/client/incoming-call.jsp?key="
					+ self.chosenEmployee();
		}
	};

	self.reimbursement = function() {
		window.location.href = self.apiurl
				+ "templates/accounting/reimbursement-creation.jsp";
	};
	self.employee = ko.observable({
		sales : []
	});
	self.setClientLevel = function() {

		if (self.chosenEmployee().length == 0) {
			fail_msg("请选择客户");
			return;
		} else if (self.chosenEmployee().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenEmployee().length == 1) {
			$.getJSON(self.apiurl + 'client/searchOneEmployee', {
				employee_pk : self.chosenEmployee()[0].split(";")[0]
			}, function(data) {
				if (data.employee) {
					self.employee(data.employee);
					self.chosenRelationLevel(self.employee().relation_level);
					self.chosenBackLevel(self.employee().back_level);
					self.chosenMarketLevel(self.employee().market_level);
				} else {
					fail_msg("员工不存在！");
				}
			}).fail(function(reason) {
				fail_msg(reason.responseText);
			});

			levelLayer = $.layer({
				type : 1,
				title : [ '客户评级', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '800px', '150px' ],
				offset : [ '', '' ],
				scrollbar : true,
				page : {
					dom : '#client-level'
				},
				end : function() {

				}
			});
		}
	};

	self.doSetClientLevel = function() {
		param = "employee.pk=" + self.chosenEmployee()[0].split(";")[0];
		if (self.chosenRelationLevel() != null) {
			param += "&employee.relation_level=" + self.chosenRelationLevel();
		} else {
			param += "&employee.relation_level=";
		}

		if (self.chosenBackLevel() != null) {
			param += "&employee.back_level=" + self.chosenBackLevel();
		} else {
			param += "&employee.back_level=";
		}

		if (self.chosenMarketLevel() != null) {
			param += "&employee.market_level=" + self.chosenMarketLevel();
		} else {
			param += "&employee.market_level=";
		}

		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/setClientEmployeeLevel',
			data : param
		}).success(function(str) {
			if (str == "success") {
				self.fetchSummary();
				layer.close(levelLayer);
			}
		});
	};
	self.cancelSetClientLevel = function() {
		layer.close(levelLayer);
	};
	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});

	// 获取摘要信息
	self.fetchSummary = function() {
		self.refresh();
		self.searchClientSummary();
	};

	self.createToDo = function(pk) {
		$("#todo_content").val("");
		$("#client_employee_pk").val(pk);
		todoLayer = $.layer({
			type : 1,
			title : [ '新增待办', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '400px', '200px' ],
			offset : [ '', '' ],
			scrollbar : true,
			page : {
				dom : '#todo-create'
			},
			end : function() {

			}
		});

	};
	self.doCreateToDo = function() {
		if ($("#todo_content").val().trim() == "") {
			fail_msg("内容不能为空");
			return;
		}
		param = "todo.content=" + $("#todo_content").val();
		param += "&todo.ext1=" + $("#client_employee_pk").val();
		param += "&todo.type=CLIENT";

		$.ajax({
			type : "POST",
			url : self.apiurl + 'todo/createToDo',
			data : param
		}).success(function(str) {
			if (str == "OK") {
				self.refresh();
				layer.close(todoLayer);
			}
		});
	};

	self.cancelCreateToDo = function() {
		layer.close(todoLayer);
	};

	self.viewToDo = function(pk) {

	};

	self.quit = ko.observable({});

	self.reasons = [ '效果不佳', '精力不够', '客户离职' ];
	/**
	 * 放弃维护
	 */
	self.quitConnect = function() {

		if (self.chosenEmployee().length == 0) {
			fail_msg("请选择客户");
			return;
		} else if (self.chosenEmployee().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenEmployee().length == 1) {
			var param = "client_employee_pk="
					+ self.chosenEmployee()[0].split(";")[0];
			// 判断尾款是否为0
			$
					.getJSON(self.apiurl + 'sale/fetchEmployeeBalance', param,
							function(data) {
								var balance = data.balance;
								if (0 == balance) {
									var quit_in = new Object();
									quit_in.client_employee_pk = self
											.chosenEmployee()[0].split(";")[0];
									quit_in.client_employee_name = self
											.chosenEmployee()[0].split(";")[1];
									self.quit(quit_in);

									quitLayer = $.layer({
										type : 1,
										title : [ '放弃维护', '' ],
										maxmin : false,
										closeBtn : [ 1, true ],
										shadeClose : false,
										area : [ '400px', '200px' ],
										offset : [ '', '' ],
										scrollbar : true,
										page : {
											dom : '#quit-connect'
										},
										end : function() {
										}
									});
								} else {
									fail_msg("客户尾款不为0，不能放弃维护！");
								}
							});
		}
	};
	/**
	 * 执行放弃维护操作
	 */
	self.doQuit = function() {
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认要放弃维护吗？',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					var data = $("#form-quit").serialize();

					startLoadingSimpleIndicator("保存中...");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'client/quitConnectEmployee',
						traditional : true,
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(quitLayer);
							self.refresh();
							self.chosenEmployee.removeAll();
						} else {
							fail_msg("放弃失败，请联系管理员！");
						}
					});
				}
			}
		});
	};
	/**
	 * 取消放弃维护
	 */
	self.cancelQuit = function() {
		layer.close(quitLayer);
	};

	self.upToFriend = function(pk, client_employee_pk) {
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认将此客户升级为朋友级吗？',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					var data = "clientRelation.pk=" + pk
							+ "&clientRelation.client_employee_pk="
							+ client_employee_pk
							+ "&clientRelation.relation_level=朋友级";

					startLoadingSimpleIndicator("保存中...");
					$.ajax(
							{
								type : "POST",
								url : self.apiurl
										+ 'client/updateEmployeeRelationLevel',
								traditional : true,
								data : data
							}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
						} else {
							fail_msg("升级失败，请联系管理员！");
						}
					});
				}
			}
		});
	};

	self.downToMarket = function(pk, client_employee_pk) {
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认将此客户降级为市场级吗？',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					var data = "clientRelation.pk=" + pk
							+ "&clientRelation.client_employee_pk="
							+ client_employee_pk
							+ "&clientRelation.relation_level=市场级";

					startLoadingSimpleIndicator("保存中...");
					$.ajax(
							{
								type : "POST",
								url : self.apiurl
										+ 'client/updateEmployeeRelationLevel',
								traditional : true,
								data : data
							}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
						} else {
							fail_msg("降级失败，请联系管理员！");
						}
					});
				}
			}
		});
	};

	self.connects = ko.observableArray([]);
	var current_client_employee_pk;
	self.checkConnectInfo = function(client_employee_pk) {
		current_client_employee_pk = client_employee_pk;
		self.searchConnects();

		connectInfoLayer = $.layer({
			type : 1,
			title : [ '交流信息', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '800px', '700px' ],
			offset : [ '', '' ],
			scrollbar : true,
			page : {
				dom : '#connect-info'
			},
			end : function() {
			}
		});
	};

	self.searchConnects = function(client_employee_pk) {
		var param = "client_employee_pk=" + current_client_employee_pk;
		param += "&page.start=" + self.startIndex1() + "&page.count="
				+ self.perPage1;
		startLoadingSimpleIndicator("加载中...");
		$.getJSON(self.apiurl + 'client/searchConnectsByPage', param, function(
				data) {
			self.connects(data.connects);

			self.totalCount1(Math.ceil(data.page.total / self.perPage1));
			self.setPageNums1(self.currentPage1());
			endLoadingIndicator();
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

	// start pagination one
	self.currentPage1 = ko.observable(1);
	self.perPage1 = 10;
	self.pageNums1 = ko.observableArray();
	self.totalCount1 = ko.observable(1);
	self.startIndex1 = ko.computed(function() {
		return (self.currentPage1() - 1) * self.perPage1;
	});

	self.resetPage1 = function() {
		self.currentPage1(1);
	};

	self.previousPage1 = function() {
		if (self.currentPage1() > 1) {
			self.currentPage1(self.currentPage1() - 1);
			self.refreshPage1();
		}
	};

	self.nextPage1 = function() {
		if (self.currentPage1() < self.pageNums1().length) {
			self.currentPage1(self.currentPage1() + 1);
			self.refreshPage1();
		}
	};

	self.turnPage1 = function(pageIndex1) {
		self.currentPage1(pageIndex1);
		self.refreshPage1();
	};

	self.setPageNums1 = function(curPage1) {
		var startPage1 = curPage1 - 4 > 0 ? curPage1 - 4 : 1;
		var endPage1 = curPage1 + 4 <= self.totalCount1() ? curPage1 + 4 : self
				.totalCount1();
		var pageNums1 = [];
		for (var i = startPage1; i <= endPage1; i++) {
			pageNums1.push(i);
		}
		self.pageNums1(pageNums1);
	};

	self.refreshPage1 = function() {
		self.searchConnects();
	};
	// end pagination one
};

var ctx = new ClientContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	ctx.searchClientSummary();
});