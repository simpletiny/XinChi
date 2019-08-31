var operateLayer;
var passengerCheckLayer;
var airLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	self.chosenOrders = ko.observableArray([]);

	self.status = [ 'N', 'I', 'Y' ];

	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("N");

	self.statusMapping = {
		'N' : '未操作',
		'I' : '操作中',
		'Y' : '已操作',
		'A' : '票务'
	};

	// 获取用户信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});

	self.orders = ko.observable({
		total : 0,
		items : []
	});

	self.totalAdult = ko.observable();
	self.totalSpecial = ko.observable();
	self.refresh = function() {
		var totalAdult = 0;
		var totalSpecial = 0;
		var param = $('form').serialize();
		param += "&order_option.operate_flg=N";
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductOrderByPage', param,
				function(data) {
					self.orders(data.productOrders);

					$(self.orders()).each(function(idx, data) {
						totalAdult += data.adult_count - 0;
						if (data.special_count != null) {
							totalSpecial += data.special_count - 0;
						}
					});

					self.totalAdult(totalAdult);
					self.totalSpecial(totalSpecial);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
				});
	};

	self.productSuppliers = ko.observableArray([]);

	self.createOperate = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else if (self.chosenOrders().length == 1) {
			var data = self.chosenOrders()[0].split(";");
			var team_number = data[2];
			if (team_number == "undefined") {
				fail_msg("订单未确认！");
				return;
			}
			var operate_flg = data[3];
			if (operate_flg != 'A') {
				fail_msg("请先处理票务！");
				return;
			}

			var product_pk = data[1];
			if (product_pk == "undefined") {
				window.location.href = self.apiurl
						+ 'templates/product/order-operate-creation.jsp?key='
						+ team_number;
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
												+ 'templates/product/order-operate-creation.jsp?key='
												+ team_number;
									} else {
										fail_msg("产品未添加地接维护！不能操作");
									}

								});
			}

		}
	};

	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(data, event) {
		self.passengers.removeAll();

		var team_number = data.team_number;
		var url = "order/selectSaleOrderNameListByTeamNumber";

		$.getJSON(self.apiurl + url, {
			team_number : team_number
		}, function(data) {
			self.passengers(data.passengers);
			passengerCheckLayer = $.layer({
				type : 1,
				title : [ '游客信息', '' ],
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
	self.cancelOperate = function() {
		layer.close(operateLayer);
	};

	self.flight = ko.observable({});
	self.operateAir = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else if (self.chosenOrders().length == 1) {
			var data = self.chosenOrders()[0].split(";");
			var product_pk = data[1];
			var operate_flg = data[3];
			if (operate_flg == "A") {
				fail_msg("请选择未操作订单！");
				return;
			}

			if (product_pk == "undefined") {
				airLayer = $.layer({
					type : 1,
					title : [ '票务信息', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '800px', '550px' ],
					offset : [ '', '' ],
					page : {
						dom : '#air-ticket-edit'
					},
					end : function() {
					}
				});
			} else {
				$.getJSON(self.apiurl
						+ 'product/searchProductFlightByProductPk', {
					product_pk : product_pk
				}, function(data) {
					self.flight(data.flight);
					airLayer = $.layer({
						type : 1,
						title : [ '票务信息', '' ],
						maxmin : false,
						closeBtn : [ 1, true ],
						shadeClose : false,
						area : [ '800px', '550px' ],
						offset : [ '', '' ],
						page : {
							dom : '#air-ticket-edit'
						},
						end : function() {
						}
					});
				});
			}

		}
	}
	self.doSendAir = function() {
		if (!$("#form-air").valid()) {
			return;
		}

		var msg = "";
		var tbody = $("#table-ticket tbody");
		var index = tbody.children().length;
		if (index == 0) {
			msg = "没有机票信息，确定要提交吗?";
		} else {
			msg = "提交后不能修改，确定提交给票务吗?";
		}
		$
				.layer({
					area : [ 'auto', 'auto' ],
					dialog : {
						msg : msg,
						btns : 2,
						type : 4,
						btn : [ '确认', '取消' ],
						yes : function(index) {
							layer.close(index);
							startLoadingIndicator("保存中...");

							var temp = self.chosenOrders()[0].split(";");
							var team_number = temp[2];

							var data = $("#form-air").serialize();
							data += "&air_base.team_number=" + team_number;

							var json = '[';
							var allTrs = tbody.children();
							for (var i = 0; i < allTrs.length; i++) {
								var current = allTrs[i];

								var flight_index = i + 1;

								var flight_leg = $(current).find(
										"[st='flight-leg']").val();
								var start_day = $(current).find(
										"[st='start-day']").val();
								var start_city = $(current).find(
										"[st='start-city']").val();
								var end_day = $(current).find("[st='end-day']")
										.val();
								var end_city = $(current).find(
										"[st='end-city']").val();
								var flight_number = $(current).find(
										"[st='flight-number']").val();

								json += '{"flight_index":"' + flight_index
										+ '","flight_leg":"' + flight_leg
										+ '","start_day":"' + start_day
										+ '","start_city":"' + start_city
										+ '","end_day":"' + end_day
										+ '","end_city":"' + end_city
										+ '","flight_number":"' + flight_number
										+ '"';

								if (i == allTrs.length - 1) {
									json += '}';
								} else {
									json += '},';
								}
							}

							json += ']';
							data += "&json=" + json;
							console.log(data);
							$
									.ajax(
											{
												type : "POST",
												url : self.apiurl
														+ 'product/operateOrderAirTicket',
												data : data
											}).success(function(str) {
										endLoadingIndicator();
										if (str == "success") {
											layer.close(airLayer);
											self.refresh();
										} else {
											fail_msg("提交失败，请联系管理员！");
										}
									});
						}
					}
				});

	}

	self.cancelSendAir = function() {
		layer.close(airLayer);
	}

	self.productSuppliers = ko.observableArray([]);

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

	self.supplierEmployees = ko.observable({});
	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex1() + "&page.count="
				+ self.perPage1;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param,
				function(data) {
					self.supplierEmployees(data.employees);

					self
							.totalCount1(Math.ceil(data.page.total
									/ self.perPage1));
					self.setPageNums1(self.currentPage1());
				});
	};

	self.searchSupplierEmployee = function() {
		self.refreshSupplier();
	};
	self.pickSupplierEmployee = function(name, pk) {
		$(currentSupplier).val(name);
		$(currentSupplier).next().val(pk);
		layer.close(supplierEmployeeLayer);
	};

	// start pagination
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

	self.turnPage1 = function(pageIndex) {
		self.currentPage1(pageIndex);
		self.refreshPage1();
	};

	self.setPageNums1 = function(curPage) {
		var startPage1 = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage1 = curPage + 4 <= self.totalCount1() ? curPage + 4 : self
				.totalCount1();
		var pageNums1 = [];
		for (var i = startPage1; i <= endPage1; i++) {
			pageNums1.push(i);
		}
		self.pageNums1(pageNums1);
	};

	self.refreshPage1 = function() {
		self.searchSupplierEmployee();
	};
	// end pagination
};

var ctx = new OrderContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
var currentSupplier;
var supplierEmployeeLayer;
function choseSupplierEmployee(event) {
	supplierEmployeeLayer = $.layer({
		type : 1,
		title : [ '选择供应商操作', '' ],
		maxmin : false,
		closeBtn : [ 1, true ],
		shadeClose : false,
		area : [ '600px', '650px' ],
		offset : [ '50px', '' ],
		scrollbar : true,
		page : {
			dom : '#supplier-pick'
		},
		end : function() {
			console.log("Done");
		}
	});

	currentSupplier = event.toElement;
	$(currentSupplier).blur();
}

function addRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;
	var tr = $('<tr><input type="hidden" st="flight-index" value="1" /><td ><input st="flight-leg" type="text"  maxlength="10"/></td><td><input st="start-day" type="number" min="1" maxlength="2"/></td><td><input st="start-city" type="text" maxlength="10"/></td><td><input st="end-day" type="number" min="1" maxlength="2"/></td><td><input st="end-city" type="text" maxlength="10"/></td><td><input st="flight-number" type="text" maxlength="10"/></td><td><input type="button" value="-" onclick="deleteRow(this)"></input></td></tr>');
	$(tr).find("input[st='flight-leg']").val(CHARACTER_ARRAY[index]);

	$(tr).find("input[st='flight-index']").val(index + 1);

	tbody.append(tr);
}

function deleteRow(txt) {
	$(txt).parent().parent().remove();
}

function designated() {
	var tbody = $("#table-ticket tbody");
	if ($("#chk-designated").is(":checked")) {
		tbody.find("input[st='flight-number']").attr("disabled", false);
	} else {
		tbody.find("input[st='flight-number']").attr("disabled", true);
	}
}