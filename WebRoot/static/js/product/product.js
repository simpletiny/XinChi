var airTicketLayer;
var airTickeChecktLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.allCharges = ["PRODUCT", "SALE", "NONE"];
	self.chosenCharge = ko.observable();
	self.locations = ko.observableArray();

	$.getJSON(self.apiurl + 'system/searchByType', {
		type : "LINE"
	}, function(data) {
		self.locations(data.datas);
	});
	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});
	self.chargeMapping = {
		'PRODUCT' : '产品包票',
		'SALE' : '销售包票',
		'NONE' : '无机票'
	};
	self.status = ['N', 'Y', 'D'];
	self.saleMapping = {
		'N' : "架下",
		'Y' : "架上",
		'D' : "废弃"
	};
	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("Y");

	// 新建产品
	self.create = function() {
		window.location.href = self.apiurl
				+ "templates/product/product-creation.jsp";
	};
	self.chosenProducts = ko.observableArray([]);
	// 编辑产品
	self.edit = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("维护只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			window.location.href = self.apiurl
					+ "templates/product/product-edit.jsp?key="
					+ self.chosenProducts();
		}
	};

	// 克隆产品
	self.clone = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("维护只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			window.location.href = self.apiurl
					+ "templates/product/product-clone.jsp?key="
					+ self.chosenProducts();
		}
	};
	self.onSale = function(sale_flg) {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 0) {
			var msg = "确认要";
			if (sale_flg == "Y") {
				msg += "上架";
			} else {
				msg += "下架";
			}

			if (self.chosenProducts().length == 1) {
				msg += "此";

			} else {
				msg += "这些";
			}
			msg += "产品吗？";

			if (sale_flg == "N") {
				msg += "下架当日不能重新上架！";
			}
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("保存中！");
						var data = "sale_flg=" + sale_flg + "&product_pks="
								+ self.chosenProducts();
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/onSaleProduct',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenProducts.removeAll();
							} else if (str.split("&&")[0] == "second") {
								self.secondOff(str.split("&&")[1], data);
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	};
	self.secondOff = function(msg, data) {
		msg += "强制下架会将待确认订单一并删除！！是否要强制下架？";
		data += "&force_flg=Y"
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : msg,
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("保存中！");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'product/onSaleProduct',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
							self.chosenProducts.removeAll();
						} else {
							fail_msg(str);
						}
					});
				}
			}
		});
	}

	/**
	 * 废弃产品
	 */
	self.abandon = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			$.getJSON(self.apiurl
					+ 'product/searchProductAirTicketInfoByProductPk', {
				product_pk : self.chosenProducts()[0]
			}, function(data) {
				if (data.product.sale_flg == "Y") {
					fail_msg("请选择未上架产品！");
					return;
				}
				$.layer({
					area : ['auto', 'auto'],
					dialog : {
						msg : '确认要废弃此产品吗？',
						btns : 2,
						type : 4,
						btn : ['确认', '取消'],
						yes : function(index) {
							layer.close(index);
							startLoadingIndicator("保存中！");
							var data = "sale_flg=D" + "&product_pks="
									+ self.chosenProducts();
							$.ajax({
								type : "POST",
								url : self.apiurl + 'product/onSaleProduct',
								data : data
							}).success(function(str) {
								endLoadingIndicator();
								if (str == "success") {
									self.refresh();
									self.chosenProducts.removeAll();
								} else {
									fail_msg(str);
								}
							});
						}
					}
				});
			});

		}
	};
	self.product = ko.observable({});
	self.airTickets = ko.observableArray([]);
	// 绑定机票
	self.bindingTicket = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {

			$.getJSON(self.apiurl
					+ 'product/searchProductAirTicketInfoByProductPk', {
				product_pk : self.chosenProducts()[0]
			}, function(data) {
				self.product(data.product);
				if (self.product().sale_flg == "Y") {
					fail_msg("请选择未上架产品！");
					return;
				}
				var ticket_charge = self.product().air_ticket_charge;

				if (ticket_charge == "NO") {
					self.chosenCharge("PRODUCT");
				} else {
					self.chosenCharge(ticket_charge);
				}

				changeCharge(self.chosenCharge());

				self.airTickets(data.air_tickets);
				airTicketLayer = $.layer({
					type : 1,
					title : ['添加机票信息', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['800px', '500px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#air-ticket'
					},
					end : function() {
					}
				});
			});
		}
	};

	self.checkAirTicket = function(product_pk) {

		$.getJSON(
				self.apiurl + 'product/searchProductAirTicketInfoByProductPk',
				{
					product_pk : product_pk
				}, function(data) {
					self.product(data.product);

					self.airTickets(data.air_tickets);
					airTicketCheckLayer = $.layer({
						type : 1,
						title : ['机票信息', ''],
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

	self.products = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductsByPage', param,
				function(data) {
					self.products(data.products);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
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

function addRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length + 1;
	var tr = $('<tr><td st="index">1</td><td><input st="start-day" type="text" /></td><td><input st="start-city" type="text" /></td><td><input st="end-day" type="text" /></td><td><input st="end-city" type="text" /></td><td><input st="ticket-number" type="text" /></td></tr>');
	$(tr).find("td[st='index']").html(index);
	tbody.append(tr);
}

function deleteRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length - 1;
	if (index > 0) {
		$(tbody.children()[index]).remove();
	}
}
var noneChecked = false;
function changeCharge(type) {
	if (type == "NONE") {
		$("#div-ticket").hide();
		noneChecked = true;
	} else {
		$("#div-ticket").show();
		noneChecked = false;
	}
}

function cancelTicket() {
	layer.close(airTicketLayer);
}

function saveTicket() {
	var data = $("#form-ticket").serialize();
	if (!noneChecked) {
		var ticketJson = '[';
		var tbody = $("#table-ticket tbody");
		var allTickets = tbody.children();
		for (var i = 0; i < allTickets.length; i++) {
			var current = allTickets[i];
			var index = i + 1;
			var start_day = $(current).find("[st='start-day']").val();
			var start_city = $(current).find("[st='start-city']").val();
			var end_day = $(current).find("[st='end-day']").val();
			var end_city = $(current).find("[st='end-city']").val();
			var ticket_number = $(current).find("[st='ticket-number']").val();

			if (start_day.trim() == "" || start_city.trim() == ""
					|| end_day.trim() == "" || end_city.trim() == "") {
				fail_msg("请填写第" + index + "行非空项目！");
				return;
			}

			ticketJson += '{"index":"' + index + '","start_day":"' + start_day
					+ '","start_city":"' + start_city + '","end_day":"'
					+ end_day + '","end_city":"' + end_city
					+ '","ticket_number":"' + ticket_number + '"';

			if (i == allTickets.length - 1) {
				ticketJson += '}';
			} else {
				ticketJson += '},';
			}
		}

		ticketJson += ']';
		data += "&ticket_json=" + ticketJson;
	}
	layer.close(airTicketLayer);
	startLoadingIndicator("保存中！");
	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'product/saveAirTicket',
		data : data
	}).success(function(str) {
		endLoadingIndicator();
		if (str == "success") {
			ctx.refresh();
			ctx.chosenProducts.removeAll();
		} else {
			fail_msg(str);
		}
	});
}

function sameEnd(txt) {
	var tr = $(txt).parent().parent();
	var end = $(tr).find("input[st='end-day']");
	var start_day = $(txt).val();
	var end_day = $(end).val();
	if (start_day.substring(0, start_day.length - 1) == end_day
			|| end_day.trim() == "") {
		$(end).val(start_day);
	}
}