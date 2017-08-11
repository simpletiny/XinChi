var airTicketCheckLayer;
var passengerCheckLayer;
var createLayer;
var NeedContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenNeeds = ko.observableArray([]);
	self.status = ["Y","N"];
	self.statusMapping = {
		'Y' : '已确认',
		'N' : '待确认'
	};

	self.createOrder = function() {
		if (self.chosenNeeds().length == 0) {
			fail_msg("请选择");
			return;
		} else if (self.chosenNeeds().length > 1) {
			fail_msg("只能选择一个");
			return;
		} else if (self.chosenNeeds().length == 1) {
			createLayer = $.layer({
				type : 1,
				title : [ '生成订单', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '800px', '200px' ],
				offset : [ '', '' ],
				scrollbar : true,
				page : {
					dom : '#order-create'
				},
				end : function() {
				}
			});
		}
	};
	// 确认生成订单
	self.doCreateOrder = function() {
		var cost = $("#air_ticket_cost").val();
		if (cost.trim() == "") {
			fail_msg("请填写机票款！");
			return
		}
		var param = self.chosenNeeds()[0].split(";");
		var sale_order_pk = param[0];
		var standard_flg = param[1];
		var data = "air_ticket_cost=" + cost + "&sale_order_pk=" + sale_order_pk + "&standard_flg=" + standard_flg;
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认要生成订单吗?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("保存中...");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'ticket/createTicketOrder',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						layer.close(createLayer);
						if (str == "success") {
							self.chosenNeeds.removeAll();
							self.refresh();
						} else {
							fail_msg(str);
							self.chosenNeeds.removeAll();
						}
					});
				}
			}
		});
	};
	self.onlyTicket = function() {

	};
	self.airTickets = ko.observableArray([]);
	// 查看航段信息
	self.checkTicketPart = function(product_pk,first_ticket_date) {
		var x = new Date(first_ticket_date);
		self.airTickets.removeAll();
		$.getJSON(self.apiurl + 'product/searchProductAirTicketInfoByProductPk', {
			product_pk : product_pk
		}, function(data) {
			$(data.air_tickets).each(function(idx, ticket) {
				var ticket_start_day=ticket.start_day;
				var ticket_end_day =ticket.end_day;
				ticket.off_date=(x.addDate(ticket_start_day-1)).Format("yyyy-MM-dd");
				ticket.land_date=(x.addDate(ticket_end_day-1)).Format("yyyy-MM-dd");
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
	self.checkPassengers = function(sale_order_pk,standard_flg) {
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
				order= data.bnsOrder;
			}
			console.log(order);
		 var name_list = order.name_list;
		 if(null!=name_list){
			 var names = name_list.split(";");
			 for(var i=0;i<names.length;i++){
				 var d = names[i].split(":");
				 if(d.length<2)
					continue;
				 
				 var passenger = new Object();
				 passenger.index = i+1;
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
	self.needs = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		var param =$("form").serialize();
		 param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'ticket/searchAirTicketNeedByPage', param, function(data) {
			self.needs(data.airTicketNeeds);

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

var ctx = new NeedContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
