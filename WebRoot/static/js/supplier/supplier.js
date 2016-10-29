var SupplierContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCompanies = ko.observableArray([]);
	self.createSupplier = function() {
		window.location.href = self.apiurl
				+ "templates/supplier/supplier-creation.jsp";
	};

	self.suppliers = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		$.getJSON(self.apiurl + 'supplier/searchSupplier', {}, function(data) {
			self.suppliers(data.suppliers);
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editSupplier = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择公司");
			return;
		} else if (self.chosenCompanies().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenCompanies().length == 1) {
			window.location.href = self.apiurl +"templates/supplier/supplier-edit.jsp?key="+self.chosenCompanies()[0];
		}
	};

};

var ctx = new SupplierContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
