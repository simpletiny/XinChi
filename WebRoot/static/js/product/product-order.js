var operateLayer;
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
		'Y' : '已操作'
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
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductOrderByPage', param, function(data) {
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
			if (operate_flg != 'N') {
				fail_msg("请选择未操作的订单！");
				return;
			}

			var product_pk = data[1];
			var data = "team_number=" + team_number + "&product_pk=" + product_pk;

			$.getJSON(self.apiurl + 'product/searchProductSuppliersByPk', data, function(data) {
				self.productSuppliers(data.productSuppliers);
				operateLayer = $.layer({
					type : 1,
					title : [ '供应商信息', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '1400px', '500px' ],
					offset : [ '', '' ],
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

	self.doOperate = function() {
		var allNeeds = $('.need');
		for ( var i = 0; i < allNeeds.length; i++) {
			var current = allNeeds[i];
			if ($(current).val().trim() == "") {
				fail_msg("请填写必填项目！");
				return;
			}
		}

		var data = self.chosenOrders()[0].split(";");
		var team_number = data[2];
		
		layer.close(operateLayer);
		
		startLoadingIndicator("生成中...");
		// json化供应商信息
		var json = '[';
		var tbody = $("#table-supplier tbody");
		var trs = $(tbody).children();
		for ( var i = 0; i < trs.length; i++) {
			var tr = trs[i];
			var index = i + 1;
			var supplierEmployeePk = $(tr).find("[st='supplier-pk']").val();

			if (supplierEmployeePk == '')
				continue;

			var supplierProductName = $(tr).find("[st='supplier-product-name']").val();
			var supplierCost = $(tr).find("[st='supplier-cost']").val();

			var landDay = $(tr).find("[st='land-day']").val();
			var pickType = $(tr).find("[st='pick-type']").val();
			var picker = $(tr).find("[st='picker']").val();
			var pickerCellphone = $(tr).find("[st='picker-cellphone']").val();
			var offDay = $(tr).find("[st='off-day']").val();
			var sendType = $(tr).find("[st='send-type']").val();

			var current = '{"supplier_index":"' + index + '","supplier_employee_pk":"' + supplierEmployeePk + '","supplier_product_name":"' + supplierProductName + '","supplier_cost":"'
					+ supplierCost + '","land_day":"' + landDay + '","pick_type":"' + pickType + '","picker":"' + picker + '","picker_cellphone":"' + pickerCellphone + '","off_day":"' + offDay
					+ '","send_type":"' + sendType + '"}';
			if (i == trs.length - 1) {
				json += current + ']';
			} else {
				json += current + ',';
			}
		}

		var data = "json=" + json;
		data += "&team_number=" + team_number;

		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/createOrderOperation',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
				self.chosenOrders.removeAll();
			} else {
				fail_msg(str);
			}
		});

	};

	self.cancelOperate = function() {
		layer.close(operateLayer);
	};

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
		for ( var i = startPage1; i <= endPage1; i++) {
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
	var tbody = $("#table-supplier tbody");
	var index = tbody.children().length + 1;
	var tr = $('<tr><td st="index">1</td><td><input type="text" st="supplier-name" onclick="choseSupplierEmployee(event)" /><input type="text" class="need" st="supplier-pk" style="display: none" /></td><td><input class="need" st="supplier-product-name" maxlength="10" type="text" /></td><td><input st="supplier-cost" class="need" type="number" /></td><td><input st="land-day" class="need" type="number" /></td><td><input st="pick-type" maxlength="50" type="text" /></td><td><input st="picker" maxlength="10" type="text" /></td>	<td><input st="picker-cellphone" maxlength="15" type="number" /></td><td><input st="off-day" class="need" type="number" /></td><td><input st="send-type" maxlength="50" type="text" /></td><td><input type="button" value="-" onclick="deleteRow(this)" /></td></tr>');
	$(tr).find("td[st='index']").html(index);
	tbody.append(tr);
}

function deleteRow(btn) {
	var tbody = $("#table-supplier tbody");
	var index = tbody.children().length - 1;
	if (index > 0) {
		$(btn).parent().parent().remove();
		refreshIndex();
	}
}

function refreshIndex() {
	var tbody = $("#table-supplier tbody");
	var trs = $(tbody).children();
	for ( var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		$(tr).find("td[st='index']").html(i + 1);
	}
}