var orderCheckLayer;
var passengerCheckLayer;
var innerPassengerCheckLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.allCharges = ["PRODUCT", "SALE", "NONE"];
	self.chosenCharge = ko.observable();

	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});

	self.status = ['N', 'Y'];
	self.statusMapping = {
		'N' : "未操作",
		'Y' : "已操作"
	};
	self.singleMapping = {
		'N' : "合",
		'Y' : "单"
	};

	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("N");

	self.orders = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductOrderByPage', param,
				function(data) {
					self.orders(data.orders);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
				});
	};

	self.chosenOrders = ko.observableArray([]);

	// 订单操作
	self.createOperate = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个产品订单！");
			return;
		} else {
			var data = self.chosenOrders()[0].split(";");
			var order_number = data[0];
			var standard_flg = data[1];
			var product_pk = data[2];
			var status = data[3];
			if (status == 'Y') {
				fail_msg("请选择未操作的订单！");
				return;
			}
			if (standard_flg == "N") {

				window.location.href = self.apiurl
						+ 'templates/product/order-operate-creation.jsp?key='
						+ order_number;

			} else {

				$
						.getJSON(
								self.apiurl + 'product/searchProductByPk',
								{
									product_pk : product_pk
								},
								function(data) {
									if (data
											&& data.product.supplier_upkeep_flg == 'Y') {
										window.location.href = self.apiurl
												+ 'templates/product/order-operate-creation.jsp?product_pk='
												+ product_pk + '&order_number='
												+ order_number;
									} else {
										fail_msg("产品未添加地接维护！不能操作");
									}

								});

			}

		}
	};

	self.sale_orders = ko.observableArray([]);
	self.checkOrders = function(order_number) {

		startLoadingSimpleIndicator("加载中...");

		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/searchSaleOrderByProductOrderNumber',
			data : "order_number=" + order_number
		}).success(function(data) {
			self.sale_orders(data.sale_orders);
			orderCheckLayer = $.layer({
				type : 1,
				title : ['合单信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#div-check-order'
				},
				end : function() {
				}
			});

			endLoadingIndicator();
		});
	}

	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(data, event) {
		self.passengers.removeAll();
		var order_number = data.order_number;
		var url = "product/searchSaleOrderNameListByProductOrderNumber";

		$.getJSON(self.apiurl + url, {
			order_number : order_number
		}, function(data) {
			self.passengers(data.passengers);
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['游客信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '700px'],
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
	// 订单详情查看乘客信息
	self.innerCheckPassengers = function(data, event) {
		self.passengers.removeAll();
		var team_number = data.team_number;
		var url = "order/selectSaleOrderNameListByTeamNumber";

		$.getJSON(self.apiurl + url, {
			team_number : team_number
		}, function(data) {
			self.passengers(data.passengers);
			innerPassengerCheckLayer = $.layer({
				type : 1,
				title : ['游客信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['600px', '600px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#passengers-check-inner'
				},
				end : function() {
				}
			});
		});
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

var ctx = new ProductContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});