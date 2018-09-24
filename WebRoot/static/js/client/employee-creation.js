var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.client = ko.observable({});
	self.genders = [ '男', '女' ];
	self.employeeType = [ '未知', '员工', '老板', '包桌' ];
	self.employee = ko.observable({});

	self.choosenSales = ko.observableArray([]);
	self.publicFlg = ko.observable("N");

	self.createEmployee = function() {
		if (!$("form").valid()) {
			return;
		}

		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/createEmployee',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/client/client-employee.jsp";
			} else if (str = "exist") {
				fail_msg("同一财务主体下存在同名客户！");
			}
		});
	};
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
