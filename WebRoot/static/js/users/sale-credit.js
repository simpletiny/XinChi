var creditLayer;
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
		param += "ucb.delete_flg=N&user_roles=SALES&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

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
			$(".rmb").formatCurrency();
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	self.chosenUserNames = ko.observableArray();
	self.chosenUserPks = ko.observableArray();
	// 修改销售信用额度
	self.editCreditLimit = function() {
		if (self.chosenUsers().length < 1) {
			fail_msg("请选择销售！");
			return;
		} else {
			self.chosenUserNames.removeAll();
			self.chosenUserPks.removeAll();

			$("#credit-limit").val("");
			for (var i = 0; i < self.chosenUsers().length; i++) {
				var users = self.chosenUsers()[i].split(",");
				self.chosenUserPks.push(users[0]);
				self.chosenUserNames.push(users[1]);
			}

			creditLayer = $.layer({
				type : 1,
				title : ['修改信用额度', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['750px', '400px'],
				offset : ['200px', ''],
				scrollbar : true,
				page : {
					dom : '#edit-credit'
				},
				end : function() {

				}
			});
		}
	}

	// 确认修改
	self.doSaveCreditLimit = function() {

		var credit_limit = $("#credit-limit").val();
		if (credit_limit.trim() == "") {
			fail_msg("额度不能为空！");
			return;
		}
		startLoadingIndicator("保存中……");
		var data = "credit_limit=" + credit_limit;
		var user_pks = "";

		for (var i = 0; i < self.chosenUserPks().length; i++) {
			user_pks += self.chosenUserPks()[i] + ",";
		}
		user_pks = user_pks.RTrim(",");

		data += "&user_pks=" + user_pks;

		$.ajax({
			type : "POST",
			url : self.apiurl + 'user/updateCreditLimit',
			data : data
		}).success(function(str) {
			if (str == "success") {
				self.refresh();
				layer.close(creditLayer);
				self.chosenUsers.removeAll();
				endLoadingIndicator();
			}
		});
	}

	// 取消修改
	self.cancelSaveCreditLimit = function() {
		layer.close(creditLayer);
	}

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

function checkAll(chk) {
	if ($(chk).is(":checked")) {
		for (var i = 0; i < ctx.users().length; i++) {
			var user = ctx.users()[i];
			ctx.chosenUsers.push(user.pk + "," + user.user_name);
		}
	} else {
		for (var i = 0; i < ctx.users().length; i++) {
			var user = ctx.users()[i];
			ctx.chosenUsers.remove(user.pk + "," + user.user_name);
		}
	}
}
