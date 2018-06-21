var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.product = ko.observable({});
	self.locations = [ "云南", "华东", "桂林", "张家界", "四川", "其他" ];
	self.product_pk = $("#key").val();
	self.productSuppliers = ko.observableArray([]);

	$.getJSON(self.apiurl + 'product/searchProductByPk', {
		product_pk : self.product_pk
	}, function(data) {
		self.product(data.product);
		self.productSuppliers(data.productSuppliers);
		if (self.product().strict_price_flg == "Y")
			$("#chk-strict").attr("checked", true);

		if (self.product().cash_flow_air_flg == "Y")
			$("#chk-air-ticket").attr("checked", true);

		if (self.product().cash_flow_local_flg == "Y")
			$("#chk-local").attr("checked", true);

		if (self.product().cash_flow_other_flg == "Y")
			$("#chk-other").attr("checked", true);

		caculateOtherCost();
	});

	self.updateProductConfirm = function() {
		var sale_flg = self.product().sale_flg;
		if (sale_flg == 'Y') {
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '架上产品更新，价格信息将于次日凌晨生效！',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						layer.close(index);
						self.updateProduct();
					}
				}
			});
		} else {
			self.updateProduct();
		}
	}

	self.updateProduct = function() {
		if (!$("form").valid()) {
			return;
		}
		var allNeeds = $('.need');
		for (var i = 0; i < allNeeds.length; i++) {
			var current = allNeeds[i];
			if ($(current).val().trim() == "") {
				fail_msg("请填写必填项目！");
				return;
			}
		}
		startLoadingIndicator("保存中");
		// json化供应商信息
		var json = '[';
		var tbody = $("#table-supplier tbody");
		var trs = $(tbody).children();
		for (var i = 0; i < trs.length; i++) {
			var tr = trs[i];
			var index = i + 1;
			var supplierEmployeePk = $(tr).find("[st='supplier-pk']").val();

			if (supplierEmployeePk == '')
				continue;

			var supplierProductName = $(tr)
					.find("[st='supplier-product-name']").val();
			var supplierCost = $(tr).find("[st='supplier-cost']").val();

			var landDay = $(tr).find("[st='land-day']").val();
			var pickType = $(tr).find("[st='pick-type']").val();
			var picker = $(tr).find("[st='picker']").val();
			var pickerCellphone = $(tr).find("[st='picker-cellphone']").val();
			var offDay = $(tr).find("[st='off-day']").val();
			var sendType = $(tr).find("[st='send-type']").val();

			var current = '{"supplier_index":"' + index
					+ '","supplier_employee_pk":"' + supplierEmployeePk
					+ '","supplier_product_name":"' + supplierProductName
					+ '","supplier_cost":"' + supplierCost + '","land_day":"'
					+ landDay + '","pick_type":"' + pickType + '","picker":"'
					+ picker + '","picker_cellphone":"' + pickerCellphone
					+ '","off_day":"' + offDay + '","send_type":"' + sendType
					+ '"}';
			if (i == trs.length - 1) {
				json += current + ']';
			} else {
				json += current + ',';
			}
		}

		var data = $("form").serialize();
		if (!$("#chk-strict").is(":checked"))
			data += "&product.strict_price_flg=N";

		if (!$("#chk-air-ticket").is(":checked"))
			data += "&product.cash_flow_air_flg=N";

		if (!$("#chk-local").is(":checked"))
			data += "&product.cash_flow_local_flg=N";

		if (!$("#chk-other").is(":checked"))
			data += "&product.cash_flow_other_flg=N";

		data += "&json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/updateProduct',
			data : data
		}).success(
				function(str) {
					endLoadingIndicator();
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/product/product.jsp";
					} else if (str == "exists") {
						fail_msg("产品库中存在同名产品！");
					} else if (str == "more_update") {
						fail_msg("架上产品一天最多更新三次！");
					}
				});

	};
	self.supplierEmployees = ko.observable({});
	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param,
				function(data) {
					self.supplierEmployees(data.employees);

					self.totalCount(Math.round(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
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
	self.currentPage = ko.observable(1);
	self.perPage = 10;
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
		self.searchSupplierEmployee();
	};
	// end pagination
};

var ctx = new ProductContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$("#local-adult-cost").disabled();
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
	var tr = $('<tr><td st="index">1</td><td><input type="text" st="supplier-name" onclick="choseSupplierEmployee(event)" /><input class="need" type="text" st="supplier-pk" style="display: none" /></td><td><input st="supplier-product-name" class="need" maxlength="10" type="text" /></td><td><input st="supplier-cost" class="need"  onkeyup="caculateOtherCost()" type="number" /></td><td><input st="land-day" class="need" type="number" /></td><td><input st="pick-type" maxlength="50" type="text" /></td><td><input st="picker" maxlength="10" type="text" /></td>	<td><input st="picker-cellphone" maxlength="15" type="number" /></td><td><input st="off-day" class="need" type="number" /></td><td><input st="send-type" maxlength="50" type="text" /></td><td><input type="button" value="-" onclick="deleteRow(this)" /></td></tr>');
	$(tr).find("td[st='index']").html(index);
	tbody.append(tr);
}

function deleteRow(btn) {
	var tbody = $("#table-supplier tbody");
	var index = tbody.children().length - 1;
	if (index > 0) {
		$(btn).parent().parent().remove();
		refreshIndex();
		caculateOtherCost();
	}
}

function refreshIndex() {
	var tbody = $("#table-supplier tbody");
	var trs = $(tbody).children();
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		$(tr).find("td[st='index']").html(i + 1);
	}
}

/**
 * 计算供应商成本合计
 */
function caculateOtherCost() {

	var tbody = $("#table-supplier tbody");
	var trs = $(tbody).children();
	var sum = 0;
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var supplierCost = $(tr).find("[st='supplier-cost']").val();
		sum += supplierCost - 0;
	}

	$("#local-adult-cost").val(sum);
	caculateGrossProfit();
}

function caculateGrossProfit() {
	var adult_price = $("#adult-price").val() - 0;
	var child_price = $("#child-price").val() - 0;
	var business_profit_substract = $("#business-profit-substract").val() - 0;
	var max_profit_substract = $("#max-profit-substract").val() - 0;
	// 产品价格
	var product_price = adult_price - business_profit_substract
			- max_profit_substract;

	var product_child_price = child_price - business_profit_substract
			- max_profit_substract;

	var local_adult_cost = $("#local-adult-cost").val() - 0;
	var air_ticket_cost = $("#air-ticket-cost").val() - 0;
	var other_cost = $("#other-cost").val() - 0;

	var local_child_cost = $("#local-child-cost").val() - 0;
	var air_ticket_child_cost = $("#air-ticket-child-cost").val() - 0;
	var other_child_cost = $("#other-child-cost").val() - 0;

	// 毛利
	var gross_profit = product_price - local_adult_cost - air_ticket_cost
			- other_cost;
	var gross_child_profit = product_child_price - local_child_cost
			- air_ticket_child_cost - other_child_cost;

	$("#gross-profit").text(gross_profit);
	$("#txt-gross-profit").val(gross_profit);

	$("#gross-child-profit").text(gross_child_profit);
	$("#txt-gross-child-profit").val(gross_child_profit);

	// 毛利率
	var gross_profit_rate = 0;
	if (adult_price - business_profit_substract != 0) {
		gross_profit_rate = parseFloat((gross_profit / (adult_price - business_profit_substract))
				.toFixed(2));
		gross_profit_rate = Math.round(gross_profit_rate * 100);
	}

	var gross_child_profit_rate = 0;
	if (child_price - business_profit_substract != 0) {
		gross_child_profit_rate = parseFloat((gross_child_profit / (child_price - business_profit_substract))
				.toFixed(2));
		gross_child_profit_rate = Math.round(gross_child_profit_rate * 100);
	}

	$("#gross-profit-rate").text(gross_profit_rate + "%");
	$("#txt-gross-profit-rate").val(gross_profit_rate);

	$("#gross-child-profit-rate").text(gross_child_profit_rate + "%");
	$("#txt-gross-child-profit-rate").val(gross_child_profit_rate);

	// 现付资金
	var spot_cash = 0;
	var spot_child_cash = 0;
	if ($("#chk-air-ticket").is(":checked")) {
		spot_cash += air_ticket_cost;
		spot_child_cash += air_ticket_child_cost;
	}

	if ($("#chk-local").is(":checked")) {
		spot_cash += local_adult_cost;
		spot_child_cash += local_child_cost;
	}

	if ($("#chk-other").is(":checked")) {
		spot_cash += other_cost;
		spot_child_cash += other_child_cost;
	}

	$("#spot-cash").text(spot_cash)
	$("#txt-spot-cash").val(spot_cash);

	$("#spot-child-cash").text(spot_child_cash)
	$("#txt-spot-child-cash").val(spot_child_cash);

	// 现金流
	var cash_flow = product_price - spot_cash;
	var cash_child_flow = product_child_price - spot_child_cash;

	$("#cash-flow").text(cash_flow);
	$("#txt-cash-flow").val(cash_flow);

	$("#cash-child-flow").text(cash_child_flow);
	$("#txt-cash-child-flow").val(cash_child_flow);

}