var idCheckLayer;
var userRoleLayer;
var passwordLayer;
var UsersContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.users = ko.observable({
		total : 0,
		items : []
	});
	self.chosenUserRoles = ko.observableArray();
	self.chosenUsers = ko.observableArray([]);

	self.allRoles = ['ADMIN', 'MANAGER', 'SALES', 'PRODUCT', 'ACCOUNTING', 'CASHIER', 'TICKET'];
	self.sexMapping = {
		'F' : '女',
		'M' : '男'
	};

	self.roleMapping = {
		'MANAGER' : '经理',
		'ADMIN' : '管理员',
		'SALES' : '销售人员',
		'PRODUCT' : '产品',
		'ACCOUNTING' : '会计',
		'CASHIER' : '出纳',
		'TICKET' : '票务'
	};

	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'user/searchUsersByPage', param, function(data) {
			var users = data.users;
			$(users).each(function(idx, user) {
				var user_roles = user.user_roles;
				var arr;
				if (user_roles == null) {
					arr = [];
				} else {
					arr = user_roles.split(",");
				}

				var roles_name = "";
				for (var i = 0; i < arr.length; i++) {
					roles_name += self.roleMapping[arr[i]] + ",";
				}

				roles_name = roles_name.substring(0, roles_name.length - 1);
				user.user_roles = ko.observable();
				user.user_roles(roles_name);
			});

			self.users(users);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	// 修改用户角色
	self.editUserRole = function() {
		if (self.chosenUsers().length < 1) {
			fail_msg("请选择用户");
			return;
		} else if (self.chosenUsers().length > 1) {
			fail_msg("只能选择一个用户");
			return;
		} else {

			$.getJSON(self.apiurl + 'user/searchUserByPk', "user_pk=" + self.chosenUsers()[0], function(data) {
				self.chosenUserRoles.removeAll();
				var user = data.ucb;
				var roles;
				if (user.user_roles == null) {
					roles = [];
				} else {
					roles = user.user_roles.split(",");
				}

				for (var i = 0; i < roles.length; i++) {
					if (roles[i] == '')
						continue;
					self.chosenUserRoles.push(roles[i]);
				}

				userRoleLayer = $.layer({
					type : 1,
					title : ['修改用户角色', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['600px', '200px'],
					offset : ['200px', ''],
					scrollbar : true,
					page : {
						dom : '#edit-role'
					},
					end : function() {

					}
				});
			});

		}
	};
	self.doSave = function() {
		startLoadingSimpleIndicator("保存中");
		var data = 'user_pk=' + self.chosenUsers()[0] + "&user_roles=" + self.chosenUserRoles();
		$.ajax({
			type : "POST",
			url : self.apiurl + 'user/updateUserRoles',
			data : data
		}).success(function(str) {
			if (str == "success") {
				self.refresh();
				endLoadingIndicator();
				layer.close(userRoleLayer);
			}
		});
	};

	self.doCancel = function() {
		layer.close(userRoleLayer);
	};

	// 停用用户
	self.stop = function() {
		if (self.chosenUsers().length < 1) {
			fail_msg("请选择用户");
			return;
		} else if (self.chosenUsers().length > 1) {
			fail_msg("只能选择一个用户");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要停用该用户吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("停用中");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'user/stopUser',
							data : 'user_pk=' + self.chosenUsers()[0]
						}).success(function(str) {
							if (str == "success") {
								self.chosenUsers.removeAll();
								self.refresh();
								endLoadingIndicator();
							}
						});
					}
				}
			});
		}
	};
	// 启用员工
	self.reuse = function() {
		if (self.chosenUsers().length < 1) {
			fail_msg("请选择用户");
			return;
		} else if (self.chosenUsers().length > 1) {
			fail_msg("只能选择一个用户");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要启用该用户吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("启用中");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'user/reuseUser',
							data : 'user_pk=' + self.chosenUsers()[0]
						}).success(function(str) {
							if (str == "success") {
								self.chosenUsers.removeAll();
								self.refresh();
								endLoadingIndicator();
							}
						});
					}
				}
			});
		}
	};

	// 修改员工密码
	self.changePassword = function() {
		if (self.chosenUsers().length < 1) {
			fail_msg("请选择用户");
			return;
		} else if (self.chosenUsers().length > 1) {
			fail_msg("只能选择一个用户");
			return;
		} else {
			passwordLayer = $.layer({
				type : 1,
				title : ['设置新密码', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['600px', '250px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#password-new'
				},
				end : function() {
					console.log("Done");
				}
			});
		}
	};
	self.doChangePassword = function() {
		var newPassword = $("#password_new").val();

		if (newPassword.trim() == "") {
			fail_msg("密码不能为空!");
			return;
		} else {
			var data = "ucb.pk=" + self.chosenUsers()[0] + "&ucb.password=" + newPassword;
			$.ajax({
				type : "POST",
				url : self.apiurl + 'user/changePassword',
				data : data
			}).success(function(str) {
				layer.close(passwordLayer);
				if (str == "success") {
					success_msg("修改成功，下次登录请使用新密码！");
				} else {
					fail_msg("修改失败，请联系管理员！");
				}
			});
		}
	};

	// 查看身份证图片
	self.checkIdPic = function(fileName) {
		$("#img-pic").attr("src", "");
		idCheckLayer = $.layer({
			type : 1,
			title : ['查看身份证图片', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});

		$("#img-pic").attr("src", self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=USER_ID");
	};
	// 新标签页显示大图片
	$("#img-pic").on(
			'click',
			function() {
				window.open(self.apiurl + "templates/common/check-picture-big.jsp?src="
						+ encodeURIComponent($(this).attr("src")));
			});

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 20;
	self.pageNums = ko.observableArray();
	self.totalCount = ko.observable(1);
	self.startIndex = ko.computed(function() {
		return (self.currentPage() - 1) * self.perPage;
	});

	self.resetPage = function() {
		self.currentPage(1);
	};

	self.previousPage = function() {
		if (self.currentPage() > 1) {
			self.currentPage(self.currentPage() - 1);
			self.refreshPage();
		}
	};

	self.nextPage = function() {
		if (self.currentPage() < self.pageNums().length) {
			self.currentPage(self.currentPage() + 1);
			self.refreshPage();
		}
	};

	self.turnPage = function(pageIndex) {
		self.currentPage(pageIndex);
		self.refreshPage();
	};

	self.setPageNums = function(curPage) {
		var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self.totalCount();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.refresh();
	};
	// end pagination
};

var ctx = new UsersContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
