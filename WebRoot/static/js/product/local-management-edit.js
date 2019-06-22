var SupplierContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.productPk = $("#product-pk").val();
	self.product = ko.observable({});

	$.getJSON(self.apiurl + 'product/searchProductByPk', {
		product_pk : self.productPk
	}, function(data) {
		self.product(data.product);
	});

	self.locals = ko.observableArray([]);
	$.getJSON(self.apiurl + 'product/searchProductLocalByProductPk', {
		product_pk : self.productPk
	}, function(data) {
		self.locals(data.productLocals);
		console.log(self.locals());
	});
	// 更新本地维护信息
	self.updateProductLocal = function() {
		if (!self.checkForm()) {
			return;
		}
		var json = self.getJson();

		var data = "json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/updateProductLocal',
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

	self.getJson = function() {
		var json = '{';
		var product_pk = self.productPk;

		json += '"product_pk":"' + product_pk + '","json":';

		var all = $("#div-location").children();
		var json_info = '[';
		for (var i = 0; i < all.length; i++) {

			var current = all[i];

			var service_type = $(current).find(
					'input[name^="service_type_"]:checked').val();
			var cost = $(current).find('input[st="cost"]').val();
			var supplier_pk = $(current).find('input[st="supplier-pk"]').val();
			var service_name = $(current).find('input[st="service-name"]')
					.val();
			var adult_cost = $(current).find('input[st="adult-cost"]').val();
			var child_cost = $(current).find('input[st="child-cost"]').val();
			var service_comment = $(current).find(
					'textarea[st="service-comment"]').val();

			var tourist_info = '';
			$(current).find('input[name="chk_tourist"]:checked').each(function(i) {
				tourist_info += $(this).val() + ";";
			});

			json_info += '{"service_type":"' + service_type + '",' + '"cost":"'
					+ cost + '",' + '"supplier_pk":"' + supplier_pk + '",'
					+ '"service_name":"' + service_name + '",'
					+ '"adult_cost":"' + adult_cost + '",' + '"child_cost":"'
					+ child_cost + '",' + '"service_comment":"'
					+ service_comment + '",' + '"tourist_info":"'
					+ tourist_info + '"}';

			if (i != all.length - 1) {
				json_info += ','
			}
		}

		json_info += ']';
		json += json_info + '}';
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

	self.supplierEmployees = ko.observable({});

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

var ctx = new SupplierContext();

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

function addLocation() {
	var timestamp = (new Date()).getTime();
	var div_location = $('<div>' + '<div class="input-row clearfloat">'
			+ '<div class="col-md-3">' + '<div class="ip">'
			+ '<em class="small-box"> <input type="radio" name="service_type_'
			+ timestamp
			+ '" checked="checked" value="0" st="chk-0" /><label>机场衔接</label>'
			+ '</em>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-1">'
			+ '<div class="ip">'
			+ '<em class="small-box"> <input type="radio" name="service_type_'
			+ timestamp
			+ '" value="1" st="chk-1" /><label>接送机场</label>'
			+ '</em>'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">费用：</label>'
			+ '<div class="ip">'
			+ '<input class="ip- required" type="number" st="cost" />'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-3">'
			+ '<label class="l" style="width: 80px !important">供应商</label>'
			+ '<div class="fix-width1">'
			+ '<input type="text" class="ip-" st="supplier-name" onclick="choseSupplierEmployee(event)" /> <input type="text"'
			+ 'class="need" st="supplier-pk" style="display: none" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">服务名称</label>'
			+ '<div class="ip">'
			+ '<input class="ip- required" type="text" st="service-name" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3 required">'
			+ '<label class="l" style="width: 70px !important">人均成人</label>'
			+ '<div class="ip">'
			+ '<input class="ip- required" type="number" st="adult-cost" />'
			+ '</div>'
			+ '</div>'
			+ '<div class="col-md-3">'
			+ '<label class="l" style="width: 70px !important">人均儿童</label>'
			+ '<div class="ip">'
			+ '<input class="ip- " type="number" st="child-cost" />'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-12">'
			+ '<label class="l">服务要求：</label>'
			+ '<div class="ip">'
			+ '<textarea type="text" class="ip-default" rows="3" maxlength="200" st="service-comment" placeholder="服务要求"></textarea>'
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="input-row clearfloat">'
			+ '<div class="col-md-6">'
			+ '<label class="l" style="width: 70px !important">游客信息：</label>'
			+ '<div class="ip">'
			+ '<div style="padding-top: 4px;">'
			+ '<em class="small-box"> <input type="checkbox" name="chk_info" checked="checked" value="name" /><label>姓名</label>'
			+ '<input type="checkbox" checked="checked" name="chk_info" value="sex" /><label>性别</label> <input'
			+ 'type="checkbox" name="chk_info" value="age" /><label>年龄</label> <input type="checkbox" name="chk_info"'
			+ 'value="id" /><label>身份证号码</label> <input type="checkbox" checked="checked" name="chk_info" value="tel" /><label>联系方式</label>'
			+ '<input type="checkbox" name="chk_info" value="room_group" /><label>分房组</label>'
			+ '</em></div></div></div></div><hr /></div>');

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