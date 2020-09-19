var airTicketCheckLayer;
var passengerCheckLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);
	self.statusMapping = {
		'0' : '未锁定',
		'1' : '已锁定'
	};

	// 生成代操作名单
	self.lockOrder = function() {
		if (self.chosenOrders().length < 1) {
			fail_msg("请选择订单！");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确定要生成操作名单 吗？',
					btns : 2,
					type : 4,
					btn : ['知道了', '再等等'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("生成中...");
						var data = "";
						for (var i = 0; i < self.chosenOrders().length; i++) {
							data += "airTicketOrderPks="
									+ self.chosenOrders()[i] + "&";
						}
						$.ajax({
							type : "POST",
							url : self.apiurl + 'ticket/lockTicketOrder',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.chosenOrders.removeAll();
								self.refresh();
							} else {
								fail_msg(str);
								self.chosenOrders.removeAll();
							}
						});
					}
				}
			});
		}
	};

	// 打回订单
	self.rollBack = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确定要打回订单到票务需求状态吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("打回中...");
						var data = "order_pk=" + self.chosenOrders();

						$.ajax({
							type : "POST",
							url : self.apiurl + 'ticket/rollBackTicketOrder',
							data : data
						}).success(function(str) {
							endLoadingIndicator();

							if (str == "success") {
								self.refresh();
								self.chosenOrders.removeAll();
							} else if (str == "namelock") {
								fail_msg("该订单下已有出票名单，请先处理之后再进行打回！")
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	}

	self.airTickets = ko.observable();
	// 查看航段信息
	self.checkTicketPart = function(ticket_order_pk) {
		$.getJSON(self.apiurl + 'ticket/searchAirTicketOrderLegByOrderPk', {
			order_pk : ticket_order_pk
		}, function(data) {
			self.airTickets(data.air_tickets);
			airTicketCheckLayer = $.layer({
				type : 1,
				title : ['航段信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#air-ticket-check'
				},
				end : function() {
				}
			});
		});
	};

	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(order_number) {
		self.passengers.removeAll();

		startLoadingIndicator("加载中...");
		var url = "ticket/searchAirTicketNameListByOrderNumber";

		$.getJSON(self.apiurl + url, {
			order_number : order_number
		}, function(data) {

			self.passengers(data.name_list);
			endLoadingIndicator();
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['乘客信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#passengers-check'
				},
				end : function() {
				}
			});
		});
	};
	self.orders = ko.observable({
		total : 0,
		items : []
	});
	self.totalPeople = ko.observable();
	self.refresh = function() {
		startLoadingIndicator("加载中...");

		var totalPeople = 0;
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage + "&airTicketOrder.status=I";
		$.getJSON(self.apiurl + 'ticket/searchAirTicketOrderByPage', param,
				function(data) {
					self.orders(data.airTicketOrders);

					$(self.orders()).each(function(idx, data) {
						totalPeople += data.people_count - 0;
					});
					self.totalPeople(totalPeople);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
					endLoadingIndicator();
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

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
