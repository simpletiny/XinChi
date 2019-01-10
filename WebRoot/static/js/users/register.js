var RegisterContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.confirm = function() {
		var password1 = $("#password1").val();
		var password2 = $("#password2").val();
		if (password1 == "" || password2 == "")
			return;
		if (password1 != password2) {
			fail_msg("前后密码不一致！");
			$("#submit").attr("disabled", true);
		} else {
			$("#submit").attr("disabled", false);
		}
	}

	self.register = function() {
		var login_name = $("#login_name").val();
		var pat = new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]", "i");
		if (pat.test(login_name) == true) {
			fail_msg("账户含有非法字符！");
			return;
		}
		if (login_name.length < 5) {
			fail_msg("登录账户必须为5位以上！");
			return;
		}
		var pat2 = new RegExp("[\u4e00-\u9fa5]", "i");

		var password1 = $("#password1").val();
		var password2 = $("#password2").val();
		if (password1 == "") {
			fail_msg("密码不能为空！");
			return;
		}

		if (pat2.test(password1) == true) {
			fail_msg("密码不能包含中文！");
			return;
		}
		if (password1.length < 6) {
			fail_msg("密码太短！");
			return;
		}

		if (password1 != password2) {
			fail_msg("前后密码不一致！");
			return;
		}

		if (!$("form").valid()) {
			return;
		}
		var data = $("form").serialize();

		$.ajax({
			type : "POST",
			url : self.apiurl + 'user/register',
			data : data
		}).success(function(str) {
			if (str == "success") {
				success_msg("注册成功，跳转至登录页面...");
				setTimeout(function() {
					window.location.href = self.apiurl;
				}, 2000);
			} else if (str == "same_id") {
				fail_msg("已存在相同的身份证号！");
			} else if (str == "same_login_name") {
				fail_msg("已存在相同用户名！");
			}
		});
	}
};
var ctx = new RegisterContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		var param = {
			size : 516,
			width : 600,
			type : "image",
			required : "no",
			input : this,
			basePath : ctx.apiurl,
			superKey : "b18425e2dbaf23c96b7ddcb1d28e6d9c"
		}
		changeFile(param);
	});
});
