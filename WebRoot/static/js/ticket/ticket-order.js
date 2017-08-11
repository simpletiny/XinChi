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

	// 锁定操作
	self.lockOrder = function() {
		if (self.chosenOrders().length < 1) {
			fail_msg("请选择订单！");
			return;
		} else {
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '锁定操作后销售不能修改名单。',
					btns : 2,
					type : 4,
					btn : [ '知道了', '再等等' ],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("锁定中...");
						var data = "airTicketOrderPks=" + self.chosenOrders();
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

	self.airTickets = ko.observableArray([]);
	// 查看航段信息
	self.checkTicketPart = function(product_pk, first_ticket_date) {
		var x = new Date(first_ticket_date);
		self.airTickets.removeAll();
		$.getJSON(self.apiurl + 'product/searchProductAirTicketInfoByProductPk', {
			product_pk : product_pk
		}, function(data) {
			$(data.air_tickets).each(function(idx, ticket) {
				var ticket_start_day = ticket.start_day;
				var ticket_end_day = ticket.end_day;
				ticket.off_date = (x.addDate(ticket_start_day - 1)).Format("yyyy-MM-dd");
				ticket.land_date = (x.addDate(ticket_end_day - 1)).Format("yyyy-MM-dd");
				self.airTickets.push(ticket);
			});

			airTicketCheckLayer = $.layer({
				type : 1,
				title : [ '航段信息', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '800px', '500px' ],
				offset : [ '', '' ],
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
	self.checkPassengers = function(sale_order_pk, standard_flg) {
		self.passengers.removeAll();
		var url = "";
		if (standard_flg == "Y") {
			url = "order/searchTbcBsOrderByPk";
		} else {
			url = "order/searchTbcBnsOrderByPk";
		}
		$.getJSON(self.apiurl + url, {
			order_pk : sale_order_pk
		}, function(data) {
			var order;
			if (standard_flg == "Y") {
				order = data.bsOrder;
			} else {
				order = data.bnsOrder;
			}

			var name_list = order.name_list;
			if (null != name_list) {
				var names = name_list.split(";");
				for ( var i = 0; i < names.length; i++) {
					var d = names[i].split(":");
					if (d.length < 2)
						continue;

					var passenger = new Object();
					passenger.index = i + 1;
					passenger.name = d[0];
					passenger.id = d[1];
					self.passengers.push(passenger);
				}
			}
			passengerCheckLayer = $.layer({
				type : 1,
				title : [ '乘客信息', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '800px', '500px' ],
				offset : [ '', '' ],
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

	self.refresh = function() {
		var param = "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'ticket/searchAirTicketOrderByPage', param, function(data) {
			self.orders(data.airTicketOrders);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
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
