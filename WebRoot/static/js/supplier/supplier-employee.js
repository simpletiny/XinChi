var SupplierContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenEmployees = ko.observableArray([]);

	self.createSupplierEmployee = function() {
		window.location.href = self.apiurl
				+ "templates/supplier/employee-creation.jsp";
	};

	self.employees = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		$.getJSON(self.apiurl + 'supplier/searchEmployee', {}, function(data) {
			self.employees(data.employees);
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenEmployees().length == 1) {
			window.location.href = self.apiurl +"templates/supplier/employee-edit.jsp?key="+self.chosenEmployees()[0];
		}
	};

};

var ctx = new SupplierContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
