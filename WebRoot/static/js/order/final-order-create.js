var clientEmployeeLayer;
var passengerBatLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	self.order_pk = $("#key").val();
	self.employee = ko.observable({});

	self.types = ko.observableArray([{
		name : "无变化",
		value : "1"
	}, {
		name : "有变更",
		value : "2"
	}, {
		name : "有投诉",
		value : "3"
	}]);

	self.chosenType = ko.observable();
	self.chosenType("1");

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
			url : self.apiurl + 'order/finalOrder',
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
var final_type = "0";
var changeType = function(rad) {
	final_type = $(rad).val();
	switch ($(rad).val() - 0) {
		case 1 :
			$("#div-2").hide();
			break;
		case 2 :
			$("#div-2").show();
			$(".type-3").hide();
			break;
		case 3 :
			$("#div-2").show();
			$(".type-3").show();
			break;
	}
	caculateFinalReceivable();
};

var inputRaise = function(txt) {
	var money = $(txt).val() - 0;
	if (money > 0) {
		if (!$("#l-raise").hasClass("required")) {
			$("#l-raise").addClass("required");
			$("#txt-raise").attr("required", "required");
		}
	} else {
		if ($("#l-raise").hasClass("required")) {
			$("#l-raise").removeClass("required");
			$("#txt-raise").attr("required", false);
			$("#txt-raise-error").remove();
		}
	}
	caculateFinalReceivable();
};
var inputReduce = function(txt) {
	var money = $(txt).val() - 0;
	if (money > 0) {
		if (!$("#l-reduce").hasClass("required")) {
			$("#l-reduce").addClass("required");
			$("#txt-reduce").attr("required", "required");
		}
	} else {
		if ($("#l-reduce").hasClass("required")) {
			$("#l-reduce").removeClass("required");
			$("#txt-reduce").attr("required", false);
			$("#txt-reduce-error").remove();
		}
	}
	caculateFinalReceivable();
};
var inputComplain = function(txt) {
	var money = $(txt).val() - 0;
	if (money > 0) {
		if (!$("#l-complain-reason").hasClass("required")) {
			$("#l-complain-reason").addClass("required");
			$("#txt-complain-reason").attr("required", "required");
		}
		if (!$("#l-complain-solution").hasClass("required")) {
			$("#l-complain-solution").addClass("required");
			$("#txt-complain-solution").attr("required", "required");
		}
	} else {
		if ($("#l-complain-reason").hasClass("required")) {
			$("#l-complain-reason").removeClass("required");
			$("#txt-complain-reason").attr("required", false);
			$("#txt-complain-reason-error").remove();
		}
		if ($("#l-complain-solution").hasClass("required")) {
			$("#l-complain-solution").removeClass("required");
			$("#txt-complain-solution").attr("required", false);
			$("#txt-complain-solution-error").remove();
		}
	}
	caculateFinalReceivable();
};
var caculateFinalReceivable = function() {
	var budget_receivable = ctx.order().receivable;
	var final_receivable = budget_receivable;
	var raise_money = $("#txt-raise-money").val() - 0;
	var reduce_money = $("#txt-reduce-money").val() - 0;
	var complain_money = $("#txt-complain-money").val() - 0;
	if (final_type != "1") {
		final_receivable += raise_money;
		final_receivable -= reduce_money;
	}
	if (final_type == "3") {
		final_receivable -= complain_money;
	}

	final_receivable = final_receivable.toFixed(2);
	$("#p-final-receivable").text(final_receivable);
	$("#txt-final-receivable").val(final_receivable);
};