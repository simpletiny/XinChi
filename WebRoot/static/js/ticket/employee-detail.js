var employeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.employeePk = $("#employee_key").val();
	self.employee = ko.observable({
		sales : []
	});

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'supplier/searchOneEmployee', {
		employee_pk : self.employeePk
	}, function(data) {
		if (data.employee) {
			self.employee(data.employee);
		} else {
			fail_msg("员工不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
};

var ctx = new employeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});

