function confirm() {
	var password1 = $("#password1").val()
	var password2 = $("#password2").val()
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
		url : window['basePath'] + "/user/checkLoginName",
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

})