var clientEmployeeLayer;
var ClientContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.accurate = ko.observable({});

	var key = $("#client_key").val();
	var arr = key.split(";");
	var client_employee_pk = arr[0];
	var client_employee_name = arr[1];
	self.accurate().client_employee_pk = ko.observable(client_employee_pk);
	self.accurate().client_employee_name = ko.observable(client_employee_name);
	self.type=['电话沟通','微信沟通'];
	var x = new Date();
	self.accurate().date = ko.observable();
	self.accurate().date(x.Format("yyyy-MM-dd"));

	self.saveVisit = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/createAccurateSale',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/client/client-relation.jsp";
			} else {
				endLoadingIndicator();
				fail_msg(str);
			}
		});

	};
};

var ctx = new ClientContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});