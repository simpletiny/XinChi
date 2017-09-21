var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.product = ko.observable({});
	self.supplierEmployees = ko.observable({});

	self.locations = [ "云南", "华东", "桂林", "张家界", "四川", "其他" ];
	self.createProduct = function() {
		if (!$("form").valid()) {
			return;
		}

		var allNeeds = $('.need');
		for ( var i = 0; i < allNeeds.length; i++) {
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

		;
		
		var data = $("form").serialize()+"&product.gross_profit_rate="+gross_profit_rate;
		data += "&json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/createProduct',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				window.location.href = self.apiurl + "templates/product/product.jsp";
			} else if (str == "exists") {
				fail_msg("产品库中存在同名产品！");
				endLoadingIndicator();
			}
		});

	};
	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param, function(data) {
			self.supplierEmployees(data.employees);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
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
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self.totalCount();
		var pageNums = [];
		for ( var i = startPage; i <= endPage; i++) {
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
	$("#other-cost").disabled();
	$("#gross-profit").disabled();
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
	var tr = $('<tr><td st="index">1</td><td><input type="text" st="supplier-name" onclick="choseSupplierEmployee(event)" /><input class="need" type="text" st="supplier-pk" style="display: none" /></td><td><input st="supplier-product-name" class="need" maxlength="10" type="text" /></td><td><input onkeyup="caculateOtherCost()" st="supplier-cost" class="need" type="number" /></td><td><input st="land-day" class="need" type="number" /></td><td><input st="pick-type" maxlength="50" type="text" /></td><td><input st="picker" maxlength="10" type="text" /></td>	<td><input st="picker-cellphone" maxlength="15" type="number" /></td><td><input st="off-day" class="need" type="number" /></td><td><input st="send-type" maxlength="50" type="text" /></td><td><input type="button" value="-" onclick="deleteRow(this)" /></td></tr>');
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
	for ( var i = 0; i < trs.length; i++) {
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
	for ( var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var supplierCost = $(tr).find("[st='supplier-cost']").val();
		sum += supplierCost - 0;
	}

	$("#other-cost").val(sum);
	caculateGrossProfit();
}
var gross_profit_rate = 0;
function caculateGrossProfit() {
	var business_price = $("#business-price").val();
	var air_ticket_cost = $("#air-ticket-cost").val();
	var other_cost = $("#other-cost").val();
	var max_profit_substract = $("#max-profit-substract").val();

	var gross_profit = 0;
	if (business_price != "" && air_ticket_cost != "" && other_cost != "" && max_profit_substract != "") {
		gross_profit = (business_price - 0) - (air_ticket_cost - 0) - (other_cost - 0) - (max_profit_substract - 0);
		$("#gross-profit").val(gross_profit);

		var denominator = (air_ticket_cost - 0) - (other_cost - 0);
		var numerator = gross_profit;

		if (denominator != 0) {
			gross_profit_rate = (100 * Math.floor(numerator) / Math.floor(denominator)).toFixed(0);
		} else {
			gross_profit_rate = 0;
		}

		$("#gross-profit-rate").text(gross_profit_rate + "%");
	} else {
		$("#gross-profit").val("");
		$("#gross-profit-rate").text("");
	}
}