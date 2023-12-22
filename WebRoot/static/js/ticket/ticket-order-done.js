var airTicketCheckLayer;
var passengerCheckLayer;
var finalDetailLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);
	self.statusMapping = {
		'0' : '未锁定',
		'1' : '已锁定'
	};
	
	

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
	self.totalCost = ko.observable();
	self.refresh = function() {
		startLoadingIndicator("加载中...");

		var totalPeople = 0;
		var totalCost = 0;

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage + "&airTicketOrder.status=Y";
		$.getJSON(self.apiurl + 'ticket/searchAirTicketOrderByPage', param, function(data) {
			self.orders(data.airTicketOrders);

			$(self.orders()).each(function(idx, data) {
				totalPeople += data.people_count - 0;
				totalCost += data.ticket_cost - 0;
			});
			self.totalPeople(totalPeople);
			self.totalCost(totalCost.toFixed(2));

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			endLoadingIndicator();
		});
	};

	self.order = ko.observable({});
	// 决算订单
	self.statusMapping = {
			'Y' : '正常出票',
			'C' : '航变'
		}
	self.finalOrder = function() {
		if (self.chosenOrders().length < 1) {
			fail_msg("请选择订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else {
			let order = self.chosenOrders()[0];
			self.order(order);
			startLoadingIndicator("加载中...");
			Promise.all([$.getJSON(self.apiurl + 'ticket/searchAirTicketOrderLegByOrderPk', {order_pk : order.pk}),
				$.getJSON(self.apiurl + "ticket/searchPassengerTicketCostInfo", "passenger.order_number="+order.order_number)]).then((results)=>{
				self.airTickets(results[0].air_tickets);
				self.passengers(results[1].airTicketNameList);
				endLoadingIndicator();
				finalDetailLayer = $.layer({
					type : 1,
					title : ['决算详情', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['1000px', '750px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#final-detail'
					},
					end : function() {
					}
				});
				
				$("td:contains('航变')").css("color", "red");
				$("td:contains('正常出票')").css("color", "green");
			}).catch((error) => {
				console.log("test");
			});
		}
	};
	self.cancelFinal = function(){
		console.log("tt");
		layer.close(finalDetailLayer);
	}
	self.doFinal = function(){
		let order = self.chosenOrders()[0];
		
		const order_number = order.order_number;
		let param = "order_number=" + order_number;
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '订单决算后，不能在进行任何操作！确认要决算此订单吗？',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("决算中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'ticket/finalTicketOrder',
						data : param
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(finalDetailLayer);
							success_msg("决算成功！")
							self.refresh();
						} else {
							fail_msg(str);
						}
					});
				}
			}
		});
		
	}

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
