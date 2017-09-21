var oldLayer;
var newLayer;
var employeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.userPk = $("#user_pk").val();
	self.user = ko.observable({
		sales : []
	});

	self.sexMapping = {
		"M" : "男",
		"F" : "女"
	};
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'user/searchUserByPk', {
		user_pk : self.userPk
	}, function(data) {
		if (data.ucb) {
			self.user(data.ucb);
		} else {
			fail_msg("员工不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	// 修改密码
	self.changePassword = function() {
		oldLayer = $.layer({
			type : 1,
			title : [ '输入旧密码', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '200px' ],
			offset : [ '', '' ],
			scrollbar : true,
			page : {
				dom : '#password-old'
			},
			end : function() {
				console.log("Done");
			}
		});
	};
	// 修改密码
	self.newPassword = function() {
		var oldPassword = $("#password_old").val();
		var data = "ucb.pk=" + self.userPk + "&ucb.password=" + oldPassword;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'user/checkPassword',
			data : data
		}).success(function(str) {
			$("#password_old").val('');
			if (str == "success") {
				layer.close(oldLayer);
				newLayer = $.layer({
					type : 1,
					title : [ '设置新密码', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '600px', '250px' ],
					offset : [ '', '' ],
					scrollbar : true,
					page : {
						dom : '#password-new'
					},
					end : function() {
						console.log("Done");
					}
				});
			} else {
				fail_msg("旧密码错误，不能修改！");
			}
		});
	};

	self.doChangePassword = function() {
		var newPassword = $("#password_new").val();
		var confirmPassword = $("#password_confirm").val();

		if (newPassword != confirmPassword) {
			fail_msg("前后密码不一致！");
			return;
		} else {
			var data = "ucb.pk=" + self.userPk + "&ucb.password=" + newPassword;
			$.ajax({
				type : "POST",
				url : self.apiurl + 'user/changePassword',
				data : data
			}).success(function(str) {
				$("#password_new").val('');
				$("#password_confirm").val('');
				layer.close(newLayer);
				if (str == "success") {
					success_msg("修改成功，下次登录请使用新密码！");
				} else {
					fail_msg("修改失败，请联系管理员！");
				}
			});
		}
	};
};

var ctx = new employeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
