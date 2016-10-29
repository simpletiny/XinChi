var SupplierContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.supplierPk = $("#supplier_key").val();
	self.supplier = ko.observable({
		sales : []
	});

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
};

var ctx = new SupplierContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
