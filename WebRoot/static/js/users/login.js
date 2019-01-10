


$(document).ready(function() {
	var login_result = $("#login_result").val();
	if (login_result == "none") {
		layer.msg("用户不存在！", 2, 8);
		$("#login_result").val("");
	} else if (login_result == "input") {
		layer.msg("密码不正确！", 2, 8);
		$("#login_result").val("");
	}else if(login_result=="stop"){
		layer.msg("用户已停用，请联系管理员！", 2, 8);
		$("#login_result").val("");
	}else if(login_result=="noright"){
		layer.msg("请等待管理员审批",2,8);
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