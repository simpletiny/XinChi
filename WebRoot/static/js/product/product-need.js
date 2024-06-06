var operateLayer;
var passengerCheckLayer;
var createLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);

	self.status = ['N', 'I', 'Y'];

	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("N");

	self.statusMapping = {
		'N' : '未操作',
		'I' : '操作中',
		'Y' : '已操作',
		'A' : '票务'
	};
	self.standardMapping = {
		"Y" : "标",
		"N" : "非标"
	}

	self.lockMapping = {
		"Y" : "是",
		"N" : "否"
	}
	self.nameMapping = {
		"1" : "未确认",
		"2" : "待确认",
		"3" : "已确认",
		"4" : "待确认",
		"5" : "已确认"
	}

	// 获取用户信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});

	self.orders = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var totalAdult = 0;
		var totalSpecial = 0;
		var param = $('#form-search').serialize();
		param += "&order_option.operate_flgs=N,N&order_option.operate_flgs=N,Y";
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductNeedByPage', param, function(data) {
			self.orders(data.productOrders);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$("#main-table").tableSum({
				title : '汇总',
				title_index : 6,
				accept : [7, 8]
			})

			endLoadingIndicator();
		});
	};

	self.productSuppliers = ko.observableArray([]);

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
	self.cancelOperate = function() {
		layer.close(operateLayer);
	};

	self.unlockOrder = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "解锁后销售能对名单数量进行修改，确认要解锁订单吗？",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("解锁中...");
						var param = "";
						for (var i = 0; i < self.chosenOrders().length; i++) {
							var inner_data = self.chosenOrders()[i].split(";");
							var team_number = inner_data[2];
							param += "team_numbers=" + team_number + "&";
						}
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/unlockOrders',
							data : param
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenOrders.removeAll();
							} else {
								fail_msg("解锁失败，请联系管理员！");
							}
						});

					}
				}
			});

		}
	}
	/**
	 * 提示销售确认名单
	 */
	self.tipSales = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else {

			startLoadingIndicator("操作中...");

			var param = "";
			for (var i = 0; i < self.chosenOrders().length; i++) {
				var inner_data = self.chosenOrders()[i].split(";");
				var team_number = inner_data[2];
				param += "team_numbers=" + team_number + "&";
			}
			$.ajax({
				type : "POST",
				url : self.apiurl + 'product/tipSalesConfirmName',
				data : param
			}).success(function(str) {
				endLoadingIndicator();
				if (str == "success") {
					$("input[type='checkbox']:checked").each(function() {
						if ($(this).is(":checked")) {
							$(this).parents("tr").find(".status-no").removeAttr("style").text("待确认");
						}
					})

					self.chosenOrders.removeAll();
					success_msg("提示成功，请等待销售确认后操作。")
				} else {
					fail_msg("操作失败，请联系管理员！");
				}
			});
		}
	}

	self.flight = ko.observableArray([]);
	self.sale_orders = ko.observableArray([]);
	// 生成订单
	self.createOrder = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else {

			// 判断是否是相同产品
			var data = self.chosenOrders()[0].split(";");
			var product_pk = data[1];
			var departure_date = data[6];
			var team_numbers = new Array();
			for (var i = 0; i < self.chosenOrders().length; i++) {
				var inner_data = self.chosenOrders()[i].split(";");
				var inner_product_pk = inner_data[1];
				var inner_operate_flg = inner_data[3];
				var inner_name_confirm_status = inner_data[4];
				var inner_departure_date = inner_data[6];
				var inner_team_number = inner_data[2];
				team_numbers.push(inner_team_number);

				if (product_pk != inner_product_pk) {
					fail_msg("只能选择产品相同的订单！");
					return;
				}

				if (departure_date != inner_departure_date) {
					fail_msg("只能选择团期相同的订单！");
					return;
				}

				if (inner_operate_flg == "A") {
					fail_msg("请选择未操作订单！");
					return;
				}

				if (inner_name_confirm_status < 3) {
					fail_msg("销售未确认名单，不能操作！请提示销售进行名单确认！");
					return;
				}
			}
			startLoadingSimpleIndicator("检测中……");
			var product_name = data[7];
			var product_model = data[8];

			var param = 'product_name=' + product_name + '&product_model=' + product_model + '&departure_date='
					+ departure_date;

			$.ajax({
				type : "POST",
				url : self.apiurl + 'product/checkSameNeedOrder',
				data : param
			}).success(function(data) {
				const orders = data.sale_orders;
				let no_orders = new Array();
				let result = new Array();
				for (let i = 0; i < orders.length; i++) {
					const order = orders[i];
					if (!team_numbers.includes(order.team_number)) {
						result.push(order.team_number);
					} else {
						no_orders.push(order);
					}
				}
				endLoadingIndicator();
				if (result.length != 0) {
					var msg = "系统检测到" + result + "也是相同的产品和团期，是否添加一起进行操作？"
					$.layer({
						area : ['auto', 'auto'],
						dialog : {
							msg : msg,
							btns : 2,
							type : 4,
							btn : ['一并操作', '忽略'],
							yes : function(index) {
								layer.close(index);
								popCreateLayer(orders);
							},
							no : function(index) {
								layer.close(index);
								popCreateLayer(no_orders);
							}
						}
					});
				} else {
					popCreateLayer(orders);
				}
			});
		}
	}
	self.product_orders = ko.observableArray([]);
	function popCreateLayer(orders) {
		self.sale_orders(orders);
		let url = self.apiurl + "product/searchSameProductOrderByTeamNumber";
		let team_number = orders[0].team_number;
		startLoadingSimpleIndicator("加载中");
		// 检测是否已存在同质未操作产品订单
		$.getJSON(url, {
			team_number : team_number
		}, function(data) {
			self.product_orders(data.orders);
			endLoadingIndicator();
			createLayer = $.layer({
				type : 1,
				title : ['销售单信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['900px', '550px'],
				offset : ['', ''],
				page : {
					dom : '#div-order-create'
				},
				end : function() {
				}
			});
		});
	}

	self.doCreateOrder = function(isplus) {

		if ($("#chk-combine").is(":checked") && $("#sel-product-order").val() == "") {
			fail_msg("请选择要合并到的订单！");
			return;
		}

		// 单地接信息
		let trs = $("#table-sale-order tbody tr");
		let order_prop = {};
		let sale_order_status = new Array();
		for (let i = 0; i < trs.length; i++) {
			let tr = $(trs[i]);
			let team_number = tr.find("[st='team-number']").val();
			let is_only_dijie = tr.find("[st='is-only-dijie']").is(":checked") ? "Y" : "N";

			let obj = {};
			obj.team_number = team_number;
			obj.is_only_dijie = is_only_dijie;
			sale_order_status.push(obj);
		}
		order_prop.sale_order_status = sale_order_status;

		// 合并订单信息
		let is_combine = $("#chk-combine").is(":checked") ? "Y" : "N";
		order_prop.is_combine = is_combine;
		if (is_combine == "Y") {
			let product_order_number = $("#sel-product-order").val();
			order_prop.product_order_number = product_order_number;
		}

		// 备注
		let comment = $(".comment").val().replace(/\n/g, ";");
		order_prop.comment = comment;

		const msg = "确认要生成产品订单吗？";
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : msg,
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(createLayer);
					layer.close(index);
					startLoadingIndicator("保存中");

					var param = "json=" + JSON.stringify(order_prop);
					$.ajax({
						type : "POST",
						url : self.apiurl + 'product/createProductOrder',
						data : param
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
							self.chosenOrders.removeAll();
						} else {
							fail_msg("提交失败，请联系管理员！");
						}
					});
				}
			}
		});
	}

	self.cancelCreateOrder = function() {
		layer.close(createLayer);
	}

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

	self.supplierEmployees = ko.observable({});
	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param, function(data) {
			self.supplierEmployees(data.employees);

			self.totalCount1(Math.ceil(data.page.total / self.perPage1));
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
		var endPage1 = curPage + 4 <= self.totalCount1() ? curPage + 4 : self.totalCount1();
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

function checkAll(chk) {
	if ($(chk).is(":checked")) {
		for (var i = 0; i < ctx.orders().length; i++) {
			var order = ctx.orders()[i];
			ctx.chosenOrders.push(order.pk + ";" + order.product_pk + ";" + order.team_number + ';' + order.operate_flg
					+ ';' + order.name_confirm_status + ';' + order.standard_flg + ';' + order.departure_date + ';'
					+ order.product_name + ';' + order.product_model);
		}
	} else {
		for (var i = 0; i < ctx.orders().length; i++) {
			var order = ctx.orders()[i];
			ctx.chosenOrders.remove(order.pk + ";" + order.product_pk + ";" + order.team_number + ';'
					+ order.operate_flg + ';' + order.name_confirm_status + ';' + order.standard_flg + ';'
					+ order.departure_date + ';' + order.product_name + ';' + order.product_model);
		}
	}
}
var currentSupplier;
var supplierEmployeeLayer;
function choseSupplierEmployee(event) {
	supplierEmployeeLayer = $.layer({
		type : 1,
		title : ['选择供应商操作', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['600px', '650px'],
		offset : ['50px', ''],
		scrollbar : true,
		page : {
			dom : '#supplier-pick'
		},
		end : function() {
			console.log("Done");
		}
	});

	currentSupplier = event.target;
	$(currentSupplier).blur();
}

function addRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;
	if (index == 10)
		return;
	var tr = $('<tr><input type="hidden" st="flight-index" value="1" /><td st="index"></td><td><input st="start-day" type="number" min="1" maxlength="2" /></td><td><input st="flight-number" type="text"/></td><td><input st="start-city" type="text" maxlength="10" /></td><td><input st="end-city" type="text" maxlength="10"/></td><td><input type="button" value="-" onclick="deleteRow(this)"></input></td></tr>');

	$(tr).find("input[st='flight-index']").val(index + 1);
	$(tr).find("td[st='index']").text(alphabetMap[index + 1]);

	tbody.append(tr);
}

function deleteRow(txt) {
	$(txt).parent().parent().remove();
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;
	var ins = $(tbody).find("td[st='index']");
	let hid_ins = $(tbody).find("input[st='flight-index']");
	for (var i = 0; i < ins.length; i++) {
		$(ins[i]).text(alphabetMap[i + 1]);
		$(hid_ins[i]).val(i + 1);
	}
}
/* 切换是否有机票信息 */
function hasTicket(chk) {
	if ($(chk).is(":checked")) {
		$("#air-ticket").hide();
		$("#air-comment").hide();
	} else {
		$("#air-ticket").show();
		$("#air-comment").show();
	}

}

function checkCombine() {
	if ($("#sel-product-order option").length < 2) {
		fail_msg("不存在同质产品订单！");
		$("#chk-combine").prop("checked", false);
	} else {
		if ($("#chk-combine").is(":checked")) {
			$("#sel-product-order").prop("disabled", false);
		} else {
			$("#sel-product-order").prop("disabled", true);
		}
	};
}