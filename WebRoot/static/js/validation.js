jQuery.validator.addMethod("isPhone", function(value,element) {
    var length = value.length;
    var mobile =  /^1[3|4|5|6|7|8|9][0-9]{1}[0-9]{8}$/;
    var tel = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
    return this.optional(element) || (tel.test(value) || mobile.test(value));
}, "请正确填写您的联系电话");

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
    $(this).rules("add", { isPhone: true})
});

$("#c_password").each(function(){
    $(this).rules("add", { required: true, equalTo: "#password"})
});
