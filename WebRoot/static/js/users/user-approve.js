var idCheckLayer;
var userAgreeLayer;
var UsersContext = function() {
	var self = this;
	self.basePath = $("#hidden_apiurl").val();
	self.users = ko.observable({
		total : 0,
		items : []
	});
	self.chosenUserRoles = ko.observableArray([]);

	self.allRoles = [ 'ADMIN', 'MANAGER', 'SALES', 'PRODUCT', 'FINANCE',
			'TICKET' ];

	self.roleMapping = {
		'MANAGER' : '经理',
		'ADMIN' : '管理员',
		'SALES' : '销售人员',
		'PRODUCT' : '产品',
		'FINANCE' : '财务',
		'TICKET' : '票务'
	};

	self.refresh = function() {
		$.getJSON(self.basePath + 'user/searchNewUsers', {}, function(data) {
			self.users(data.users);
		});
	};

	self.checkIdPic = function(fileName) {
		idCheckLayer = $.layer({
			type : 1,
			title : [ '查看身份证图片', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});

		$("#img-pic").attr(
				"src",
				self.basePath + 'file/getFileStream?fileFileName=' + fileName
						+ "&fileType=USER_ID");
	};

	self.agreeUser = function(pk) {
		$("#current-pk").val(pk);
		userAgreeLayer = $.layer({
			type : 1,
			title : [ '选择用户角色', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '200px' ],
			offset : [ '200px', '' ],
			scrollbar : true,
			page : {
				dom : '#agree-panel'
			},
			end : function() {
				$("#current-pk").val("");
				$(":checkbox").attr("checked", false);
				self.chosenUserRoles = ko.observableArray([]);
			},
			close : function() {
				$("#current-pk").val("");
				$(":checkbox").attr("checked", false);
				self.chosenUserRoles = ko.observableArray([]);
			}
		});
	};

	self.doAgree = function() {
		var pk = $("#current-pk").val();
		if (self.chosenUserRoles().length == 0) {
			fail_msg("请选择角色");
			return;
		}
		$.ajax({
			url : self.basePath + "user/approveUser",
			type : "post",
			data : "user_pk=" + pk + "&" + "user_roles="
					+ self.chosenUserRoles(),
			success : function(data) {
				if (data == "success") {
					success_msg("审批成功");
					layer.close(userAgreeLayer);
					self.refresh();
				} else {
					fail_msg(data);
				}
			},
			error : function(data) {
				console.log(eval(data));
			}
		});

	};

	self.doCancel = function() {
		layer.close(userAgreeLayer);
	};

	self.rejectUser = function(pk) {
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确定要拒绝此用户申请吗？',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					$.ajax({
						url : self.basePath + "user/rejectUser",
						type : "post",
						data : "user_pk=" + pk,
						success : function(data) {
							if (data == "success") {
								success_msg("操作成功");
								self.refresh();
							} else {
								fail_msg(data);
							}
						},
						error : function(data) {
							console.log(eval(data));
						}
					});
				}
			}
		});

	};
	// 新标签页显示大图片
	$("#img-pic").on(
			'click',
			function() {
				window.open(self.basePath
						+ "templates/common/check-picture-big.jsp?src="
						+ encodeURIComponent($(this).attr("src")));
			});
};

var ctx = new UsersContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
