var SupplierContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.supplierPk = $("#supplier_key").val();
	self.supplier = ko.observable({});
	self.genders = [ '男', '女' ];
	self.supplierArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆' ];
	self.supplierType = [ '注册', '挂靠', '独立旅游人', '夫妻店', '其他' ];
	
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'supplier/searchOneSupplier', {
		supplier_pk : self.supplierPk
	}, function(data) {
		if (data.supplier) {
			self.supplier(data.supplier);
		} else {
			fail_msg("公司不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
	
	self.saveSupplier = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'supplier/updateSupplier',
			data : $("form").serialize()
		}).success(function(str) {
			if(str=="success"){
				window.location.href=self.apiurl+"templates/supplier/supplier.jsp";
			}
		});
	};
};

var ctx = new SupplierContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
