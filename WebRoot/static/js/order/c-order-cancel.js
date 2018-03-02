var clientEmployeeLayer;
var passengerBatLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	self.order_pk = $("#key").val();
	self.employee = ko.observable({});
	
	$.getJSON(self.apiurl + 'order/searchCOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.order);
		$.getJSON(self.apiurl + 'client/searchOneEmployee', {
			employee_pk : self.order().client_employee_pk
		}, function(data) {
			if (data.employee) {
				self.employee(data.employee);
			} else {
				fail_msg("员工不存在！");
			}
		}).fail(function(reason) {
			fail_msg(reason.responseText);
		});

	});

	self.finalOrder = function() {
		if (!$("form").valid()) {
			return;
		}
		var data = $("form").serialize();
		startLoadingSimpleIndicator("申请中...");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/cancelCOrder',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				window.location.href = self.apiurl + "templates/order/c-order.jsp";
			} else {
				fail_msg(str);
			}
		});

	};

};

var ctx = new OrderContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		changeFile(this);
	});
});
