var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.product = ko.observable({
		first_air_start : "哈尔滨"
	});
	self.supplierEmployees = ko.observable({});

	self.locations = ["云南", "华东", "桂林", "张家界", "四川", "其他"];
	self.createProduct = function() {
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
		data += "&json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/createProduct',
			data : data
		}).success(
				function(str) {
					endLoadingIndicator();
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/product/product.jsp";
					} else if (str == "exists") {
						fail_msg("产品库中存在同名产品！");
						endLoadingIndicator();
					}
				});

	};
	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$
				.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param,
						function(data) {
							self.supplierEmployees(data.employees);

							self.totalCount(Math.round(data.page.total
									/ self.perPage));
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
function addRow(btn) {
	var tr_current = $(btn).parent().parent();
	var tr_min = $('<tr>'
			+ '<td><input type="button" value="-" onclick="deleteRow(this)" /></td>'
			+ '<td class="r">接：</td>'
			+ '<td><input name="se" type="radio" />航段</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input name="se" type="radio" />其他</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="number" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>' + '</tr>');
	var tr_add = $('<tr>'
			+ '<td><input type="button" value="+" onclick="addRow(this)" /></td>'
			+ '<td class="r">送：</td>'
			+ '<td><input name="se" type="radio" />航段</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input name="se" type="radio" />其他</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="number"/></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>' + '</tr>');

	var tr_line = $('<tr>'
			+ '<td colspan="11"><hr style="width: 100%; text-align: center; vertical-align: middle" /></td>'
			+ '</tr>');

	tr_current.after(tr_line);
	tr_line.after(tr_min);
	tr_min.after(tr_add);
}

function deleteRow(btn) {
	var tr_min = $(btn).parent().parent();
	var tr_add = tr_min.next();
	var tr_line = tr_min.prev();
	var tbody = tr_min.parent();
	var index = $(tbody).children().length;
	if (index > 2) {
		tr_min.remove();
		tr_add.remove();
		tr_line.remove();
	}
}

function addSupplier() {
	var div_supplier = $('<div><div class="input-row clearfloat">'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">地接社</label>'
			+ '<div class="fix-width1">'
			+ '<input type="text" class="ip-" st="supplier-name" onclick="choseSupplierEmployee(event)" /> <input type="text"'
			+ 'class="need" st="supplier-pk" style="display: none" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">产品名称</label>'
			+ '<div class="fix-width1">'
			+ '<input type="text" class="ip-" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-2 required">'
			+ '<label class="l" style="width: 70px !important">天数</label>'
			+ '<div class="ip" style="width: 50% !important">'
			+ '<input type="number" class="ip-" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-2 required">'
			+ '<label class="l" style="width: 70px !important">成人</label>'
			+ '<div class="ip" style="width: 50% !important">'
			+ '<input type="number" class="ip-" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-2 required">'
			+ '<label class="l" style="width: 70px !important">儿童</label>'
			+ '<div class="ip" style="width: 50% !important">'
			+ '<input type="number" class="ip-" />'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div style="margin-top: 20px; padding-left: 70px">'
			+ '<table style="width: 90%" id="table-supplier">'
			+ '<thead>'
			+ '<tr class="required">'
			+ '<th style="width: 5%"></th>'
			+ '<th style="width: 5%"></th>'
			+ '<th style="width: 5%"></th>'
			+ '<th style="width: 10%"></th>'
			+ '<th style="width: 5%"></th>'
			+ '<th style="width: 10%"></th>'
			+ '<th class="r" style="width: 10%">天次</th>'
			+ '<th class="r" style="width: 10%">交通工具</th>'
			+ '<th class="r" style="width: 10%">抵离时间</th>'
			+ '<th class="r" style="width: 10%">抵离城市</th>'
			+ '<th class="r" style="width: 10%">抵离地点</th>'
			+ '</tr>'
			+ '</thead>'
			+ '<tbody>'
			+ '<tr>'
			+ '<td><input type="button" value="-" onclick="deleteRow(this)" /></td>'
			+ '<td class="r">接：</td>'
			+ '<td><input name="se" type="radio" />航段</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input name="se" type="radio" />其他</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="number" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '</tr>'
			+ '<tr>'
			+ '<td><input type="button" value="+" onclick="addRow(this)" /></td>'
			+ '<td class="r">送：</td>'
			+ '<td><input name="se" type="radio" />航段</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input name="se" type="radio" />其他</td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="number" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '<td><input type="text" /></td>'
			+ '</tr>'
			+ '</tbody>'
			+ '</table>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-6">'
			+ '<label class="l" style="width: 70px !important">游客信息：</label>'
			+ '<div class="ip">'
			+ '<div style="padding-top: 4px;">'
			+ '<em class="small-box"> <input type="checkbox" checked="checked" /><label>姓名</label> <input'
			+ 'type="checkbox" checked="checked" /><label>性别</label> <input type="checkbox" /><label>年龄</label> <input'
			+ 'type="checkbox" /><label>身份证号码</label> <input type="checkbox" checked="checked" /><label>联系方式</label> <input'
			+ 'type="checkbox" /><label>分房组</label>'
			+ '</em>'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3">'
			+ '<div class="ip">'
			+ '<a type="submit" class="btn btn-r" data-bind="click: createProduct">上传确认件</a>'
			+ '</div></div></div><hr /></div>');
	$('#div-supplier').append(div_supplier);
}
function deleteSupplier() {
	var div_suppliers = $("#div-supplier");
	var children = div_suppliers.children();
	var index = children.length;
	if (index > 1) {
		children[index - 1].remove();
	}
}

function addLocation() {
	var div_location = $('<div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-3">'
			+ '<div class="ip">'
			+ '<em class="small-box"> <input type="checkbox" checked="checked" /><label>机场衔接</label>'
			+ '</em>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-2">'
			+ '<div class="ip">'
			+ '<em class="small-box"> <input type="checkbox" /><label>接送机场</label>'
			+ '</em>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">费用：</label>'
			+ '<div class="ip">'
			+ '<input class="ip- " type="number" />'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 80px !important"><input type="checkbox" />供应商</label>'
			+ '<div class="fix-width1">'
			+ '<input class="ip- " type="text" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">服务名称</label>'
			+ '<div class="ip">'
			+ '<input class="ip- " type="text" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">人均成人</label>'
			+ '<div class="ip">'
			+ '<input class="ip- " type="number" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">人均儿童</label>'
			+ '<div class="ip">'
			+ '<input class="ip- " type="number" />'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-12">'
			+ '<label class="l">服务要求：</label>'
			+ '<div class="ip">'
			+ '<textarea type="text" class="ip-default" rows="3" maxlength="200" data-bind="value: product().comment"'
			+ 'name="product.comment" placeholder="服务要求"></textarea>'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-6">'
			+ '<label class="l" style="width: 70px !important">游客信息：</label>'
			+ '<div class="ip">'
			+ '<div style="padding-top: 4px;">'
			+ '<em class="small-box"> <input type="checkbox" checked="checked" /><label>姓名</label> <input'
			+ 'type="checkbox" checked="checked" /><label>性别</label> <input type="checkbox" /><label>年龄</label> <input'
			+ 'type="checkbox" /><label>身份证号码</label> <input type="checkbox" checked="checked" /><label>联系方式</label> <input'
			+ 'type="checkbox" /><label>分房组</label>' + '</em>' + '</div>'
			+ '</div></div></div><hr /></div>');

	$("#div-location").append(div_location);
}

function deleteLocation() {
	var div_locations = $("#div-location");
	var children = div_locations.children();
	var index = children.length;
	if (index > 1) {
		children[index - 1].remove();
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