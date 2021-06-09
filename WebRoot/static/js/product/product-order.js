var orderCheckLayer;
var passengerCheckLayer;
var innerPassengerCheckLayer;
var ticketInfoCheckLayer;
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

	self.lockMapping = {
		'N' : "未锁定",
		'Y' : "锁定"
	}

	self.chosenStatuses = ko.observable();
	self.chosenStatuses("N");

	self.orders = ko.observable({
		total : 0,
		items : []
	});
	self.adult_cnt = ko.observable();
	self.special_cnt = ko.observable();

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...")

		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductOrderByPage', param,
				function(data) {
					self.orders(data.orders);
					var total_adult = 0;
					var total_special = 0;
					// 计算合计
					$(self.orders()).each(function(idx, data) {
						total_adult += data.adult_count;
						total_special += data.special_count;
					});

					self.adult_cnt(total_adult);
					self.special_cnt(total_special);
					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());

					endLoadingIndicator();
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

			startLoadingIndicator("校验中...");
			var data = self.chosenOrders()[0].split(";");
			var order_number = data[0];
			var standard_flg = data[1];
			var product_pk = data[2];
			var status = data[3];
			if (status == 'Y') {
				fail_msg("请选择未操作的订单！");
				return;
			}

			$
					.ajax({
						type : "POST",
						url : self.apiurl + 'product/isAllOrdersLocked',
						data : "order_number=" + order_number
					})
					.success(
							function(str) {
								endLoadingIndicator()
								if (str == "yes") {
									if (standard_flg == "N") {

										window.location.href = self.apiurl
												+ 'templates/product/order-operate-creation.jsp?standard_flg=N&order_number='
												+ order_number;

									} else {

										$
												.getJSON(
														self.apiurl
																+ 'product/searchProductByPk',
														{
															product_pk : product_pk
														},
														function(data) {
															if (data
																	&& data.product.supplier_upkeep_flg == 'Y') {
																window.location.href = self.apiurl
																		+ 'templates/product/order-operate-creation.jsp?standard_flg=Y&product_pk='
																		+ product_pk
																		+ '&order_number='
																		+ order_number;
															} else {
																fail_msg("产品未添加地接维护！不能操作");
															}

														});

									}
								} else if (str == "no") {
									fail_msg("请锁定所有销售订单后继续操作！")
								} else {
									fail_msg(str);
								}
							});

		}
	};

	self.rollBack = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个产品订单！");
			return;
		} else {
			var data = self.chosenOrders()[0].split(";");
			var order_number = data[0];
			var status = data[3];
			if (status == 'Y') {
				fail_msg("请选择未操作的订单！");
				return;
			}

			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "确认要将订单打回吗？",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("打回中！");

						var param = "order_number=" + order_number;

						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/rollBackProductOrder',
							data : param
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenOrders.removeAll();
							} else if (str == "airlock") {
								fail_msg("票务已经操作，不能打回！请联系票务！")
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});

		}
	}

	self.sale_orders = ko.observableArray([]);
	var current_order_number = "";
	self.checkOrders = function(order_number) {

		startLoadingSimpleIndicator("加载中...");
		current_order_number = order_number;
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
				area : ['1000px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#div-check-order'
				},
				zIndex : -1,
				end : function() {
				}
			});

			endLoadingIndicator();
		});
	}
	self.refreshSaleOrders = function() {

		startLoadingSimpleIndicator("刷新中...");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/searchSaleOrderByProductOrderNumber',
			data : "order_number=" + current_order_number
		}).success(function(data) {
			self.sale_orders(data.sale_orders);
			endLoadingIndicator();
		});
	}

	self.lockOrder = function(team_number, lock_flg) {
		var param = "team_number=" + team_number + "&lock_flg=" + lock_flg;

		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/changeOrderLock',
			data : param
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refreshSaleOrders();
			} else {
				fail_msg(str);
			}
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
				area : ['800px', '600px'],
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

	self.ticketInfos = ko.observableArray();

	// 查看出票信息
	self.checkTicketInfos = function(data, event) {
		self.ticketInfos.removeAll();
		var order_number = data.order_number;
		var url = "product/searchTicketInfoByOrderNumber";

		$.getJSON(self.apiurl + url, {
			order_number : order_number
		}, function(data) {
			self.ticketInfos(data.ticket_infos);
			ticketInfoCheckLayer = $.layer({
				type : 1,
				title : ['出票信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['1000px', '700px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#ticket-check'
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