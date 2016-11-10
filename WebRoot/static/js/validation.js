jQuery.validator.addMethod("isPhone", function(value,element) {
    var mobile =  /^1[3|4|5|6|7|8|9][0-9]{1}[0-9]{8}$/;
    var tel = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
    return this.optional(element) || (tel.test(value) || mobile.test(value));
}, "请正确填写您的联系电话");


jQuery.validator.addMethod("isCellphone", function(value,element) {
    var length = value.length;
    var mobile =  /^1[3|4|5|6|7|8|9][0-9]{1}[0-9]{8}$/;
    if(length!=11) return this.optional(element) || false;
    return this.optional(element) ||  mobile.test(value);
}, "请正确填写手机号码");

jQuery.validator.addMethod("date_year", function(value,element) {
    var length = value.length;
    var mobile =  /^[1|2]{1}[0-9]{3}$/;
    if(length!=4) return this.optional(element) || false;
    return this.optional(element) ||  mobile.test(value);
}, "请正确填写出生年");

//jQuery.validator.addMethod("isDifferent", function(value,element) {
//	var allNeed = $(".different");
//	if(allNeed.length == 1){
//		return true;
//	}else if(allNeed.length ==2){
//		return this.optional(element) || $(allNeed[0]).val()!=$(allNeed[1]).val();
//	}
//}, "不能相同");

jQuery.validator.addMethod("checkurl", function(value, element) {
        return /^(http:\/\/)?(https:\/\/)?(www\.)?[A-Za-z0-9_-]+\.+[A-Za-z0-9.\/%&=\?_:;-]+$/.test(value);
    }, "请输入有效的网址"
);

jQuery.validator.addClassRules({
    checkurl : { checkurl : true }
});

$("form").validate({
    highlight: function(element) {
        $(element).closest('.ip').removeClass('has-success').addClass('has-error');
    },
    unhighlight: function(element) {
        $(element).closest('.ip').removeClass('has-error').addClass('has-success');
    },
    rules: {
    },
    errorElement: 'span',
    errorClass: 'help-block',
    errorPlacement: function(error, element) {
        if(element.closest(".ip").length) {
            element.closest(".ip").append(error);
        } else {
            error.insertAfter(element);
        }
    },
    messages: {
        c_password: {
            equalTo: '两次密码输入不一致'
        }
    }
});

$(".phone").each(function(){
    $(this).rules("add", { isPhone: true});
});

$(".cellphone").each(function(){
    $(this).rules("add", { isCellphone: true});
});

$(".date_year").each(function(){
	$(this).rules("add", { date_year: true});
});
//$(".different").each(function(){
//	$(this).rules("add", { isDifferent: true});
//});
$("#c_password").each(function(){
    $(this).rules("add", { required: true, equalTo: "#password"});
});
