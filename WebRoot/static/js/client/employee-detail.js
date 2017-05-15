var employeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.employeePk = $("#employee_key").val();
	self.employee = ko.observable({
		sales : []
	});
	self.recovery = function() {
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认恢复该客户吗?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("恢复中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'client/recoveryClientEmployee',
						data : "employee_pks=" + self.employeePk
					}).success(function(str) {
						if (str == "success") {
							location.reload();
							endLoadingIndicator();
						} else {
							fail_msg("恢复失败，请联系管理员！");
						}
					});
				}
			}
		});

	};

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'client/searchOneEmployee', {
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
