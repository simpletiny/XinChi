function confirm() {
	var password1 = $("#password1").val();
	var password2 = $("#password2").val();
	if (password1 == "" || password2 == "")
		return;
	if (password1 != password2) {
		layer.msg("前后密码不一致！", 2, 8);
		$("#submit").attr("disabled", true);
	} else {
		$("#submit").attr("disabled", false);
	}
}
function check_name() {
	var login_name = $("#login_name").val();
	if ($.trim(login_name) == "")
		return;
	$.ajax({
		url : window['basePath'] + "user/checkLoginName",
		type : "post",
		data : "login_name=" + login_name,
		success : function(data) {
			if (data == "exist") {
				layer.msg("用户名已经存在！", 2, 8);
				$("#submit").attr("disabled", true);
			} else {
				$("#submit").attr("disabled", false);
			}
		},
		error : function(data) {
			console.log(eval(data));
		}
	});
}

$(document).ready(function() {
	var login_result = $("#login_result").val();
	if (login_result == "none") {
		layer.msg("用户不存在！", 2, 8);
		$("#login_result").val("");
	} else if (login_result == "input") {
		layer.msg("密码不正确！", 2, 8);
		$("#login_result").val("");
	}
	$("form").submit(function(e) {
		if ($("#auto-login").prop("checked")) {
			var str_username = $("#username").val();
			var str_password = $("#password").val();
			$.cookie("auto-login", "true", {
				expires : 7
			});
			$.cookie("username", str_username, {
				expires : 7
			});
			$.cookie("password", str_password, {
				expires : 7
			});
		} else {
			$.cookie("auto-login", "false", {
				expire : -1
			});
			$.cookie("username", "", {
				expires : -1
			});
			$.cookie("password", "", {
				expires : -1
			});
		}
	});
	if ($.cookie("auto-login") == "true") {
		$("#auto-login").prop("checked", true);
		$("#username").val($.cookie("username"));
		$("#password").val($.cookie("password"));
	}
});