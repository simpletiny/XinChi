var operateLayer;
var passengerCheckLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	self.chosenOperations = ko.observableArray([]);

	self.status = ['N', 'I', 'Y'];

	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("N");

	self.statusMapping = {
		'N' : '未操作',
		'I' : '操作中',
		'Y' : '已操作'
	};
	self.singleMapping = {
		'N' : "合",
		'Y' : "单"
	};
	self.lockMapping = {
		'N' : "未锁定",
		'Y' : "锁定"
	}

	self.orderStatusMapping = {
		'N' : "正常",
		'Y' : "已取消"
	};
	// 获取用户信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});

	self.operations = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var param = $('form').serialize();
		param += "&operate_option.status=I";
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductOrderOperationByPage', param, function(data) {
			self.operations(data.operations);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$("#main-table").tableSum({
				title : '汇总',
				title_index : 5,
				accept : [6, 8]
			})
			endLoadingIndicator();
		});
	};

	self.productSuppliers = ko.observableArray([]);

	self.confirmOperation = function() {
		if (self.chosenOperations().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOperations().length > 0) {

			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '是否要确认此订单！',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("校验中...");

						var operate_pks = "";
						var order_numbers = "";
						for (var i = 0; i < self.chosenOperations().length; i++) {
							var current = self.chosenOperations()[i].split(";");
							operate_pks += current[0] + ",";
							order_numbers = current[1] + ",";
						}
						operate_pks = operate_pks.RTrim(",");
						order_numbers = order_numbers.RTrim(",");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/isAllOrdersLocked',
							data : "order_number=" + order_numbers
						}).success(function(result) {
							var str = result.split(",");
							endLoadingIndicator()
							if (str[0] == "yes") {
								startLoadingIndicator("确认中...");
								var data = "operate_pks=" + operate_pks;
								$.ajax({
									type : "POST",
									url : self.apiurl + 'product/confirmOperation',
									data : data
								}).success(function(str) {
									endLoadingIndicator();
									if (str == "success") {
										self.refresh();
										self.chosenOperations.removeAll();
									} else {
										fail_msg(str);
									}
								});
							} else if (str[0] == "no") {
								fail_msg("请锁定" + str[1] + "所有销售订单后继续操作！")
							} else {
								fail_msg(str);
							}
						});

					}
				}
			});

		}
	};
	var current_order_number = "";
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
			if (str == "success") {
				self.refreshSaleOrders();
			} else {
				fail_msg(str);
			}
		});
	}
	// 打回操作中订单
	self.deleteOperation = function() {
		if (self.chosenOperations().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOperations().length > 0) {
			var team_numbers = "";
			for (var i = 0; i < self.chosenOperations().length; i++) {
				var current = self.chosenOperations()[i].split(";");
				team_numbers += current[1] + ",";
			}

			team_numbers = team_numbers.substr(0, team_numbers.length - 1);

			var data = "team_numbers=" + team_numbers;
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '打回会将关联的操作订单一并打回，并将产品订单设置为未操作状态！',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("打回中...");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/deleteOperation',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenOperations.removeAll();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	};
	// 产品包含的供应商信息
	self.operation = ko.observable({});
	self.orders = ko.observableArray([]);
	self.editOperation = function() {
		if (self.chosenOperations().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOperations().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else if (self.chosenOperations().length == 1) {

			startLoadingSimpleIndicator("加载中……");
			$("#supplier-info input").val('');
			var current = self.chosenOperations()[0].split(";");
			var operate_pk = current[0];
			$.getJSON(self.apiurl + 'product/searchOpeartionDataByPk', {
				operate_pk : operate_pk
			}, function(data) {
				self.operation(data.operate_option);
				self.orders(data.orders);

				endLoadingIndicator();
				operateLayer = $.layer({
					type : 1,
					title : ['供应商信息', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['800px', '500px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#supplier-info'
					},
					end : function() {
					}
				});

			});
		}
	};

	self.doEdit = function() {
		var allNeeds = $("#supplier-info input");
		for (var i = 0; i < allNeeds.length; i++) {
			var current = allNeeds[i];
			if ($(current).val().trim() == "") {
				fail_msg("请填写必填项目！");
				return;
			}
		}
		// 地接结算费用和所有费用合计是否匹配
		var costs = 0;

		$(".supplier-cost").each(function() {
			costs += +$(this).val().trim();
		});

		var new_cost = +$(".new-cost").val().trim();

		if (costs != new_cost) {
			fail_msg("分配费用合计" + costs + "和新费用" + new_cost + "不符！");
			return;
		}
		layer.close(operateLayer);
		startLoadingIndicator("更新中...");

		// json化供应商信息
		var current = self.chosenOperations()[0].split(";");
		var operate_pk = current[0];
		var supplier_employee_pk = $(".supplier-employee-pk").val().trim();
		var json = '{"operate_pk":"' + operate_pk + '","supplier_employee_pk":"' + supplier_employee_pk
				+ '","new_cost":"' + new_cost + '","teams":[';
		$(".team-number").each(function() {
			var team_number = $(this).text().trim();
			var supplier_cost = +$(this).parent().find(".supplier-cost").val().trim();

			json += '{"team_number":"' + team_number + '","supplier_cost":"' + supplier_cost + '"},'

		});

		json = json.RTrim(',');
		json += ']}';

		var data = "json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/updateOrderOperation',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
				self.chosenOperations.removeAll();
			} else {
				fail_msg(str);
			}
		});

	};

	self.cancelEdit = function() {
		layer.close(operateLayer);
	};
	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(data, event) {
		self.passengers.removeAll();

		var team_number = data.team_number;
		var url = "product/searchSaleOrderNameListByProductOrderNumber";

		$.getJSON(self.apiurl + url, {
			order_number : team_number
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

	self.downloadSc = function(team_number, supplier_employee_pk) {
		window.location.href = self.apiurl + "file/downloadProductFile?team_number=" + team_number
				+ "&supplier_employee_pk=" + supplier_employee_pk + "&fileType=C";
	}

	// 查看订单详情
	self.sale_orders = ko.observableArray([]);
	self.checkOrders = function(order_number) {
		if (order_number.startsWith("N")) {
			success_msg("老数据所见即订单详情！")
			return;
		}
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
				area : ['1000px', '600px'],
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
		for (var i = 0; i < ctx.operations().length; i++) {
			var operation = ctx.operations()[i];
			ctx.chosenOperations.push(operation.pk + ';' + operation.team_number + ';' + operation.supplier_cost);
		}
	} else {
		for (var i = 0; i < ctx.operations().length; i++) {
			var operation = ctx.operations()[i];
			ctx.chosenOperations.remove(operation.pk + ';' + operation.team_number + ';' + operation.supplier_cost);
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
// function addRow() {
// var tbody = $("#table-supplier tbody");
// var index = tbody.children().length + 1;
// var tr = $('<tr><td st="index">1</td><td><input type="text"
// st="supplier-name" onclick="choseSupplierEmployee(event)" /><input
// type="text" class="need" st="supplier-pk" style="display: none"
// /></td><td><input class="need" st="supplier-product-name" maxlength="10"
// type="text" /></td><td><input st="supplier-cost" class="need" type="number"
// /></td><td><input st="land-day" class="need" type="number" /></td><td><input
// st="pick-type" maxlength="50" type="text" /></td><td><input st="picker"
// maxlength="10" type="text" /></td> <td><input st="picker-cellphone"
// maxlength="15" type="number" /></td><td><input st="off-day" class="need"
// type="number" /></td><td><input st="send-type" maxlength="50" type="text"
// /></td><td><input type="button" value="-" onclick="deleteRow(this)"
// /></td></tr>');
// $(tr).find("td[st='index']").html(index);
// tbody.append(tr);
// }
//
// function deleteRow(btn) {
// var tbody = $("#table-supplier tbody");
// var index = tbody.children().length - 1;
// if (index > 0) {
// $(btn).parent().parent().remove();
// refreshIndex();
// }
// }
//
// function refreshIndex() {
// var tbody = $("#table-supplier tbody");
// var trs = $(tbody).children();
// for (var i = 0; i < trs.length; i++) {
// var tr = trs[i];
// $(tr).find("td[st='index']").html(i + 1);
// }
// }
