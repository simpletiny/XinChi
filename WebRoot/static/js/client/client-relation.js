var todoLayer;
var levelLayer;
var ClientContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.relations = ko.observable({
		total : 0,
		items : []
	});
	// 维度
	self.level = [ '关系度', '回款誉', '市场力' ];
	// 关系度
	self.relationLevel = [ '朋友级', '商务级', '市场级', '绝缘级', '排斥级' ];
	// 回款誉
	self.marketLevel = [ '主导级', '引领级', '普通级', '跟随级', '玩闹级' ];
	// 市场力
	self.backLevel = [ '提前', '及时', '定期', '拖拉', '费劲', '垃圾' ];
	self.chosenLevel = ko.observableArray([]);

	self.chosenRelationLevel = ko.observableArray([]);
	self.chosenBackLevel = ko.observableArray([]);
	self.chosenMarketLevel = ko.observableArray([]);

	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchRelationsByPage', param, function(data) {
			self.relations(data.relations);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			$(".rmb").formatCurrency();
		});
	};

	self.clientSummary = ko.observable({
		total : 0,
		items : []
	});
	self.clientEmployeeCount = ko.observable();
	self.monthOrderCount = ko.observable();
	self.searchClientSummary = function() {
		var param = $("form").serialize();
		$.getJSON(self.apiurl + 'client/searchClientSummary', param, function(data) {
			self.clientSummary(data.clientSummary);
			self.clientEmployeeCount(data.employeeCount);
			self.monthOrderCount(data.monthOrderCount);
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
			window.location.href = self.apiurl + "templates/client/visit-creation.jsp?key=" + self.chosenEmployee();
		}
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
		}else{
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
	self.sales_name = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
		$(self.sales()).each(function(idx, data) {
			self.sales_name.push(data.user_name);
		});
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

var ctx = new ClientContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	ctx.searchClientSummary();
});