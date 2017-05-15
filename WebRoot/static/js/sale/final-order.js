var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);

	self.orders = ko.observable({
		total : 0,
		items : []
	});

	self.dateFrom = ko.observable();
	self.dateTo = ko.observable();
	var x = new Date();
	self.dateTo(x.Format("yyyy-MM-") + x.getDaysInMonth());
	self.dateFrom(x.Format("yyyy-MM-") + "01");

	// 计算合计
	self.totalPeople = ko.observable(0);
	self.totalReceivable = ko.observable(0);
	self.totalPayable = ko.observable(0);
	self.totalProfit = ko.observable(0);
	self.totalPerProfit = ko.observable(0);

	self.refresh = function() {
		var totalPeople = 0;
		var totalReceivable = 0;
		var totalPayable = 0;
		var totalProfit = 0;
		var totalPerProfit = 0;

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'sale/searchFinalOrdersByPage', param, function(data) {
			self.orders(data.orders);

			// 计算合计
			$(self.orders()).each(function(idx, data) {
				totalPeople += data.people_count;
				totalReceivable += data.receivable;
				totalPayable += data.payable;
				totalProfit += data.profit;
			});

			self.totalPeople(totalPeople);
			self.totalReceivable(totalReceivable);
			self.totalPayable(totalPayable);
			self.totalProfit(totalProfit);
			self.totalPerProfit((totalProfit / totalPeople).toFixed(2));

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			$(".rmb").formatCurrency();
		});
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

	self.rollBack = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择决算单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个决算单");
			return;
		}

		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认要打回重报吗?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					startLoadingSimpleIndicator("操作中");
					layer.close(index);
					$.ajax({
						type : "POST",
						url : self.apiurl + 'sale/rollBackFinalOrder',
						data : "order_pk=" + self.chosenOrders(),
						success : function(str) {
							if (str != "success") {
								fail_msg("回滚失败，请联系管理员");
							}
							self.refresh();
							self.chosenReceiveds = ko.observableArray([]);
							endLoadingIndicator();
						}
					});
				}
			}
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

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

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
