var LoginContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.login = function() {
		var username = $("#username").val().trim();
		var password = $("#password").val();
		if (username == "" || password == "") {
			fail_msg("请填写用户名或密码！");
			return;
		}

		var data = $("form").serialize();
		$.ajax({
			type : "POST",
			url : self.apiurl + 'user/login',
			data : data
		}).success(function(str) {
			// 保存cookie
			if (str == "success" || str == "sale") {
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
			}
			if (str == "none") {
				fail_msg("用户不存在！");
			} else if (str == "input") {
				fail_msg("密码不正确！");
			} else if (str == "stop") {
				fail_msg("用户已停用，请联系负责人！");
			} else if (str == "noright") {
				fail_msg("请等待负责人审批！");
			} else if (str == "success") {
				window.location.href = self.apiurl + "/templates/home.jsp";
			} else if (str == "sale") {
				window.location.href = self.apiurl + "/templates/client/client-relation.jsp";
			}
		});
	}

};

var ctx = new LoginContext();

function addFixedPositionedText(text, horizontalSpacing, verticalSpacing) {
	const mask = document.getElementById('mask');
	const maskWidth = mask.offsetWidth;
	const maskHeight = mask.offsetHeight;

	for (let x = 0; x < maskWidth; x += horizontalSpacing) {
		for (let y = 0; y < maskHeight; y += verticalSpacing) {
			const textElement = document.createElement('div');
			textElement.classList.add('mask-text');
			textElement.textContent = text;

			textElement.style.left = x + 'px';
			textElement.style.top = y + 'px';

			mask.appendChild(textElement);
		}
	}
}

$(document).ready(function() {
	 $("body").append(`<div id="mask">
	 </div>`)
		

	addFixedPositionedText('欣驰国际', 400, 200);
	ko.applyBindings(ctx);
	if ($.cookie("auto-login") == "true") {
		$("#auto-login").prop("checked", true);
		$("#username").val($.cookie("username"));
		$("#password").val($.cookie("password"));
	}
	$(document).keydown(function(event) {
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if (e && e.keyCode == 13) { // enter 键
			$("#btn-login").click();
		}
	});
});
