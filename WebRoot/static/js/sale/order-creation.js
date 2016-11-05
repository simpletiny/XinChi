var nameListLayer;
var clientEmployeeLayer;
var supplierEmployeeLayer;

var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	self.clientEmployees = ko.observable({});
	self.supplierEmployees = ko.observable({});

	self.refreshClient = function() {
		$.getJSON(self.apiurl + 'client/searchEmployee', {}, function(data) {
			self.clientEmployees(data.employees);
		});
	};

	self.searchClientEmployee = function() {
		self.refreshClient();
	};

	self.refreshSupplier = function() {
		$.getJSON(self.apiurl + 'supplier/searchEmployee', {}, function(data) {
			self.supplierEmployees(data.employees);
		});
	};

	self.searchSupplierEmployee = function() {
		self.refreshSupplier();
	};

	self.addSupplier = function(data, event) {
		$(event.toElement)
				.parent()
				.parent()
				.prev()
				.after(
						' <div class="input-row clearfloat" st="supplier">'
								+ '<div class="col-md-6">'
								+ '<label class="l">供应商</label>'
								+ '<div class="ip"><input type="text" class="ip-" onclick="choseSupplierEmployee(this,event)" placeholder="供应商" st="supplierEmployeeName"/></div>'
								+ '<input type="text" class="ip-" st="supplierEmployeePk" style="display:none" />'
								+ '</div>'
								+ '<div class="col-md-6">'
								+ '<label class="l">应付款</label>'
								+ '<div class="ip"><input type="number" min="0" st="payable" class="ip-" placeholder="应付款" /></div>'
								+ '</div>' + '</div>');
	};

	self.recordNameList = function() {
		nameListLayer = $.layer({
			type : 1,
			title : [ '名单录入', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#name-list'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.choseClientEmployee = function() {
		clientEmployeeLayer = $.layer({
			type : 1,
			title : [ '选择客户操作', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#client-pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.pickClientEmployee = function(name, pk) {
		$("#txt-client-employee-name").val(name);
		$("#txt-client-employee-pk").val(pk);
		layer.close(clientEmployeeLayer);
	};

	self.pickSupplierEmployee = function(name, pk) {
		$(currentSupplier).val(name);
		$(currentSupplier).parent().next().val(pk);
		layer.close(supplierEmployeeLayer);
	};

	self.saveNameList = function() {
		layer.close(nameListLayer);

	};

	self.createOrder = function() {
		if (!$("form").valid()) {
			return;
		}

		var nameList = $("#txt-name-list").val();
		nameList = $.trim(nameList.replace(new RegExp("；", "gm"), ";").replace(
				new RegExp("：", "gm"), ":"));

		var allSupplierEmployees = $("[st='supplier']");
		var supplierJson = '[';
		for ( var i = 0; i < allSupplierEmployees.length; i++) {
			var current = allSupplierEmployees[i];
			var supplierEmployeeName = $(current).find(
					"[st='supplierEmployeeName']").val();
			var supplierEmployeePk = $(current).find(
					"[st='supplierEmployeePk']").val();
			var payable = $(current).find("[st='payable']").val();

			if (supplierEmployeePk == "" || supplierEmployeeName == "")
				continue;
			supplierJson += '{"supplierEmployeeName":"' + supplierEmployeeName
					+ '",' + '"supplierEmployeePk":"' + supplierEmployeePk
					+ '",' + '"payable":"' + payable;
			if (i == allSupplierEmployees.length - 1) {
				supplierJson += '"}';
			} else {
				supplierJson += '"},';
			}
		}
		supplierJson += ']';

		var data = $("form").serialize() + "&nameList=" + nameList
				+ "&supplierJson=" + supplierJson;
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '提交后无法修改，是否确认提交?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					$.ajax({
						type : "POST",
						url : self.apiurl + 'sale/createOrder',
						data : data
					}).success(
							function(str) {
								if (str == "OK") {
									window.location.href = self.apiurl
											+ "templates/sale/order.jsp";
								}
							});
				}
			}
		});
	};
};

var ctx = new OrderContext();
var currentSupplier;
function choseSupplierEmployee(data, event) {
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
}
$(document).ready(function() {
	ko.applyBindings(ctx);
});
