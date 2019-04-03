var passengerCheckLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.supplierEmployees = ko.observable({});

	self.team_number = $("#key").val();
	self.order = ko.observable({});

	// 产品包含的供应商信息
	self.productSuppliers = ko.observableArray([]);

	$.getJSON(self.apiurl + 'product/searchProductDataForOrder', {
		team_number : self.team_number
	}, function(data) {
		self.order(data.order);
		self.productSuppliers(data.productSuppliers);
	});

	self.isD = function(t) {

		if (t == '0')
			return true
		else
			return false;
	}

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

	// 保存地接维护信息
	self.createOrderOperation = function() {

		if (!self.checkForm()) {
			return;
		}
		var json = self.getJson();

		var data = "json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/createOrderOperation',
			data : data
		}).success(
				function(str) {
					endLoadingIndicator();
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/product/product-upkeep.jsp";
					} else {
						fail_msg(str);
					}
				});

	}

	// 获取页面json
	self.getJson = function() {
		var json = '{';

		var order_pk = self.order().pk;
		json += '"order_pk":"' + order_pk + '","json":[';

		var max_div = $("#div-supplier");

		var all_divs = $(max_div).children();

		var one_json = '';
		for (var i = 0; i < all_divs.length; i++) {
			var current_div = all_divs[i];

			var supplier_pk = $(current_div).find(':input[st="supplier-pk"]')
					.val();

			var supplier_product_name = $(current_div).find(
					':input[st="supplier-product-name"]').val();

			var supplier_product_days = $(current_div).find(
					':input[st="supplier-product-days"]').val();

			var supplier_cost = $(current_div).find(
					':input[st="supplier-cost"]').val();
			// var child_cost = $(current_div).find(':input[st="child-cost"]')
			// .val();

			var tourist_info = '';
			$(current_div).find('input[name="chk_tourist"]:checked').each(
					function(i) {
						tourist_info += $(this).val() + ";";
					});

			var confirm_file_templet = $(current_div).find(
					'input[st="confirm-file-templet"]').val();

			one_json += '{"supplier_index":"' + i + '",' + '"supplier_pk":"'
					+ supplier_pk + '",' + '"supplier_product_name":"'
					+ supplier_product_name + '",'
					+ '"supplier_product_days":"' + supplier_product_days
					+ '",' + '"supplier_cost":"' + supplier_cost + '",'
					+ '"tourist_info":"' + tourist_info + '",'
					+ '"confirm_file_templet":"' + confirm_file_templet + '",';

			one_json += '"info_json":';

			var tbody = $(current_div).find('tbody');
			var trs = $(tbody).children();
			var two_json = '[';
			for (j = 0; j < trs.length - 1;) {

				if ((j + 1) % 3 == 0) {
					j++;
					continue;
				}

				var tr_min = trs[j];
				var tr_add = trs[j + 1];

				var info_index = j / 3;

				var pick_type = $(tr_min).find(
						':radio[name^="radio-jie"]:checked').val();
				var pick_leg, pick_other;

				if (pick_type == "0") {
					pick_leg = $(tr_min).find(':input[st="txt-jie-type-0"]')
							.val();
					pick_other = "";
				} else {
					pick_other = $(tr_min).find(':input[st="txt-jie-type-1"]')
							.val();
					pick_leg = "";
				}

				var pick_day = $(tr_min).find(':input[st="day"]').val();
				var pick_traffic = $(tr_min).find(':input[st="traffic-tool"]')
						.val();
				var pick_time = $(tr_min).find(':input[st="time"]').val();
				var pick_city = $(tr_min).find(':input[st="city"]').val();
				var pick_place = $(tr_min).find(':input[st="place"]').val();

				var send_type = $(tr_add).find(
						':radio[name^="radio-song"]:checked').val();
				var send_leg, send_other;

				if (send_type == "0") {
					send_leg = $(tr_add).find(':input[st="txt-song-type-0"]')
							.val();
					send_other = "";
				} else {
					send_other = $(tr_add).find(':input[st="txt-song-type-1"]')
							.val();
					send_leg = "";
				}

				var send_day = $(tr_add).find(':input[st="day"]').val();
				var send_traffic = $(tr_add).find(':input[st="traffic-tool"]')
						.val();
				var send_time = $(tr_add).find(':input[st="time"]').val();
				var send_city = $(tr_add).find(':input[st="city"]').val();
				var send_place = $(tr_add).find(':input[st="place"]').val();

				two_json += '{';
				two_json += '"info_index":"' + info_index + '",'
						+ '"pick_type":"' + pick_type + '",' + '"pick_leg":"'
						+ pick_leg + '",' + '"pick_other":"' + pick_other
						+ '",' + '"pick_day":"' + pick_day + '",'
						+ '"pick_traffic":"' + pick_traffic + '",'
						+ '"pick_time":"' + pick_time + '",' + '"pick_city":"'
						+ pick_city + '",' + '"pick_place":"' + pick_place
						+ '",' + '"send_type":"' + send_type + '",'
						+ '"send_leg":"' + send_leg + '",' + '"send_other":"'
						+ send_other + '",' + '"send_day":"' + send_day + '",'
						+ '"send_traffic":"' + send_traffic + '",'
						+ '"send_time":"' + send_time + '",' + '"send_city":"'
						+ send_city + '",' + '"send_place":"' + send_place
						+ '"';

				if (j == trs.length - 2) {
					two_json += '}';
				} else {
					two_json += '},';
				}

				j = j + 2;
			}

			two_json += ']';
			one_json += two_json;

			if (i == all_divs.length - 1) {
				one_json += '}';
			} else {
				one_json += '},';
			}
		}

		json += one_json + ']}';

		return json;
	}
	// 查验表单
	self.checkForm = function() {
		var all = $("input.required");
		var result = true;
		for (var i = 0; i < all.length; i++) {
			var current = all[i];
			if ($(current).val().trim() == "") {
				$(current).css("background", "red");
				result = false;
			}
		}
		if (!result) {
			setTimeout(function() {
				$(all).removeAttr("style");
				fail_msg("请填写不能为空的项目！");
			}, 500);
		}

		return result;
	}

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
function addRow(btn) {
	var tr_current = $(btn).parent().parent();
	var timestamp = (new Date()).getTime();

	var tr_min = $('<tr>'
			+ '<td><input type="button" value="-" onclick="deleteRow(this)" /></td>'
			+ '<td class="r">接：</td>'
			+ '<td><input name="radio-jie-'
			+ timestamp
			+ '" checked="checked" type="radio" st="radio-jie-0" value="0" onclick="changeJieSongType(this)"/>航段</td>'
			+ '<td><input class="required" type="text" maxlength="10" st="txt-jie-type-0"/></td>'
			+ '<td><input name="radio-jie-'
			+ timestamp
			+ '" type="radio" st="radio-jie-1" value="1" onclick="changeJieSongType(this)"/>其他</td>'
			+ '<td><input type="text"  maxlength="10" st="txt-jie-type-1" disabled="disabled"/></td>'
			+ '<td><input class="required" type="number" st="day"/></td>'
			+ '<td><input class="required" type="text" maxlength="10" st="traffic-tool"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="time"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="city"/></td>'
			+ '<td><input class="required" type="text" maxlength="30" st="place"/></td>'
			+ '</tr>');
	var tr_add = $('<tr>'
			+ '<td><input type="button" value="+" onclick="addRow(this)" /></td>'
			+ '<td class="r">送：</td>'
			+ '<td><input name="radio-song-'
			+ timestamp
			+ '" checked="checked" st="radio-song-0" type="radio" value="0" onclick="changeJieSongType(this)"/>航段</td>'
			+ '<td><input class="required" type="text" maxlength="10" st="txt-song-type-0"/></td>'
			+ '<td><input name="radio-song-'
			+ timestamp
			+ '" type="radio" value="1" st="radio-song-0" onclick="changeJieSongType(this)"/>其他</td>'
			+ '<td><input type="text"  maxlength="10" st="txt-song-type-1" disabled="disabled"/></td>'
			+ '<td><input class="required" type="number" st="day"/></td>'
			+ '<td><input class="required" type="text" maxlength="10" st="traffic-tool"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="time"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="city"/></td>'
			+ '<td><input class="required" type="text" maxlength="30" st="place"/></td>'
			+ '</tr>');

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
	var timestamp = (new Date()).getTime();
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
			+ '<input type="text" class="ip-" st="supplier-product-name"/>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-2 required">'
			+ '<label class="l" style="width: 70px !important">天数</label>'
			+ '<div class="ip" style="width: 50% !important">'
			+ '<input type="number" class="ip-" st="supplier-product-days"/>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-2 required">'
			+ '<label class="l" style="width: 70px !important">结算价格</label>'
			+ '<div class="ip" style="width: 50% !important">'
			+ '<input type="number" class="ip-" st="supplier-cost"/>'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div style="margin-top: 20px; padding-left: 70px">'
			+ '<table style="width: 90%" class="table-supplier">'
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
			+ '<tbody st="t-body">'
			+ '<tr>'
			+ '<td><input type="button" value="-" onclick="deleteRow(this)" /></td>'
			+ '<td class="r">接：</td>' + '<td><input name="radio-jie-'
			+ timestamp
			+ '" checked="checked" type="radio" st="radio-jie-0" value="0" onclick="changeJieSongType(this)"/>航段</td>'
			+ '<td><input class="required" type="text" maxlength="10" st="txt-jie-type-0"/></td>'
			+ '<td><input name="radio-jie-'
			+ timestamp
			+ '" type="radio" st="radio-jie-1" value="1" onclick="changeJieSongType(this)"/>其他</td>'
			+ '<td><input type="text"  maxlength="10" st="txt-jie-type-1" disabled="disabled"/></td>'
			+ '<td><input class="required" type="number" st="day"/></td>'
			+ '<td><input class="required" type="text" maxlength="10" st="traffic-tool"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="time"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="city"/></td>'
			+ '<td><input class="required" type="text" maxlength="30" st="place"/></td>'
			+ '</tr>'
			+ '<tr>'
			+ '<td><input type="button" value="+" onclick="addRow(this)" /></td>'
			+ '<td class="r">送：</td>'
			+ '<td><input name="radio-song-'
			+ timestamp
			+ '" checked="checked" st="radio-song-0" type="radio" value="0" onclick="changeJieSongType(this)"/>航段</td>'
			+ '<td><input class="required" type="text" maxlength="10" st="txt-song-type-0"/></td>'
			+ '<td><input name="radio-song-'
			+ timestamp
			+ '" type="radio" value="1" st="radio-song-0" onclick="changeJieSongType(this)"/>其他</td>'
			+ '<td><input type="text"  maxlength="10" st="txt-song-type-1" disabled="disabled"/></td>'
			+ '<td><input class="required" type="number" st="day"/></td>'
			+ '<td><input class="required" type="text" maxlength="10" st="traffic-tool"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="time"/></td>'
			+ '<td><input class="required" type="text" maxlength="15" st="city"/></td>'
			+ '<td><input class="required" type="text" maxlength="30" st="place"/></td>'
			+ '</tr>'
			+ '</tbody>'
			+ '</table>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-6">'
			+ '<label class="l" style="width: 70px !important">游客信息：</label>'
			+ '<div class="ip">'
			+ '<div style="padding-top: 4px;">'
			+ '<em class="small-box"> <input type="checkbox" checked="checked" name="chk_tourist" value="name"/><label>姓名</label> <input '
			+ 'type="checkbox" checked="checked" name="chk_tourist" value="sex"/><label>性别</label> <input type="checkbox" '
			+ 'name="chk_tourist" value="age"/><label>年龄</label> <input type="checkbox" name="chk_tourist" value="id"/><label>身份证号码</label> <input '
			+ 'type="checkbox" checked="checked" name="chk_tourist" value="tel"/><label>联系方式</label> <input type="checkbox" '
			+ 'name="chk_tourist" value="room_group"/><label>分房组</label>'
			+ '</em>'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3">'
			+ '<div class="ip">'
			+ '<a href="javascript:;" class="a-upload">上传确认件<input type="file" onchange="changeFile(this)"/></a> <input type="hidden" st="confirm-file-templet"/>'
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
function changeJieSongType(ra) {
	var v = $(ra).val();
	var tr = $(ra).parent().parent();
	var txt0 = $(tr).find(':input[st^="txt-"][st$="type-0"]');
	var txt1 = $(tr).find(':input[st^="txt-"][st$="type-1"]');
	if (v == "0") {
		$(txt0).attr("disabled", false);
		$(txt0).addClass("required");
		$(txt1).attr("disabled", true);
		$(txt1).removeClass("required");
	} else {
		$(txt1).attr("disabled", false);
		$(txt1).addClass("required");
		$(txt0).attr("disabled", true);
		$(txt0).removeClass("required");
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
function changeFile(thisx) {
	var file = thisx.files[0];
	var inputName = thisx.name;
	name_check = file.name;
	size = file.size;
	type = file.type;

	if (type.indexOf("msword") < 0) {
		fail_msg("请上传word文档");
		return;
	}
	if (size > 1048576) {
		fail_msg("文件大于1MB");
		return;
	}

	var fileNameInput = $(thisx).parent().next();
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/fileUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.responseType = "blob";

	xhr.onload = function() {
		if (this.status == 200) {
			var fileName = this.getResponseHeader("Content-Disposition").split(
					";")[1].split("=")[1];
			var blob = this.response;
			fileNameInput.val(fileName);
			success_msg("上传成功！请点击保存按钮以保存。");
		} else {
			fail_msg("上传失败，请重试或联系管理员！");
		}
	};
	xhr.send(formData);
}