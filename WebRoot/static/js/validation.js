jQuery.validator.addMethod("isPhone", function(value, element) {
	var mobile = /^1[3|4|5|6|7|8|9][0-9]{1}[0-9]{8}$/;
	var tel = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
	return this.optional(element) || (tel.test(value) || mobile.test(value));
}, "请正确填写您的联系电话");

jQuery.validator.addMethod("isCellphone", function(value, element) {
	var length = value.length;
	var mobile = /^1[3|4|5|6|7|8|9][0-9]{1}[0-9]{8}$/;
	if (length != 11)
		return this.optional(element) || false;
	return this.optional(element) || mobile.test(value);
}, "请正确填写手机号码");

jQuery.validator.addMethod("date_year", function(value, element) {
	var length = value.length;
	var mobile = /^[1|2]{1}[0-9]{3}$/;
	if (length != 4)
		return this.optional(element) || false;
	return this.optional(element) || mobile.test(value);
}, "请正确填写出生年");

jQuery.validator.addMethod("isTime", function(value, element) {
	var length = value.length;
	var time = /^(([0-2][0-3])|([0-1][0-9])):[0-5][0-9]$/;
	if (length != 5)
		return this.optional(element) || false;
	return this.optional(element) || time.test(value);
}, "请正确填写时间");

jQuery.validator.addMethod("isStep", function(value, element) {
	var s = $(element).attr("step");
	if (s == null || isNaN(s))
		return this.optional(element) || true
	var v = value - 0;
	var x = s - 0;
	if (v % x != 0)
		return this.optional(element) || false;
	return this.optional(element) || true;
}, "请填写符合要求的数字");

// jQuery.validator.addMethod("isDifferent", function(value,element) {
// var allNeed = $(".different");
// if(allNeed.length == 1){
// return true;
// }else if(allNeed.length ==2){
// return this.optional(element) || $(allNeed[0]).val()!=$(allNeed[1]).val();
// }
// }, "不能相同");

jQuery.validator
		.addMethod(
				"checkurl",
				function(value, element) {
					return /^(http:\/\/)?(https:\/\/)?(www\.)?[A-Za-z0-9_-]+\.+[A-Za-z0-9.\/%&=\?_:;-]+$/
							.test(value);
				}, "请输入有效的网址");

jQuery.validator.addMethod("amountLimit", function(value, element) {
	var returnVal = false;
	var amountStart = $(".amountRangeStart").val();
	var amountEnd = $(".amountRangeEnd").val();
	if (parseFloat(amountEnd) <= parseFloat(amountStart)) {
		returnVal = true;
	}
	return this.optional(element) || returnVal;
}, "大于总金额");

jQuery.validator.addClassRules({
	checkurl : {
		checkurl : true
	}
});

$("form").each(
		function() {
			$(this).validate(
					{

						highlight : function(element) {
							$(element).closest('.ip')
									.removeClass('has-success').addClass(
											'has-error');
						},
						unhighlight : function(element) {
							$(element).closest('.ip').removeClass('has-error')
									.addClass('has-success');
						},
						rules : {},
						errorElement : 'span',
						errorClass : 'help-block',
						errorPlacement : function(error, element) {

							if (element.closest(".ip").length) {
								element.closest(".ip").append(error);
							} else {
								error.insertAfter(element);
							}
						},
						messages : {
							c_password : {
								equalTo : '两次密码输入不一致'
							}
						}
					});
		});

$(".phone").each(function() {
	$(this).rules("add", {
		isPhone : true
	});
});

$(".cellphone").each(function() {
	$(this).rules("add", {
		isCellphone : true
	});
});

$(".date_year").each(function() {
	$(this).rules("add", {
		date_year : true
	});
});
$(".amountRangeEnd").each(function() {
	$(this).rules("add", {
		amountLimit : true
	});
});

$(".time").each(function() {
	$(this).rules("add", {
		isTime : true
	});
});
$("#c_password").each(function() {
	$(this).rules("add", {
		required : true,
		equalTo : "#password"
	});
});

$("input[type='number']").each(function() {
	$(this).rules("add", {
		isStep : true
	});
});
