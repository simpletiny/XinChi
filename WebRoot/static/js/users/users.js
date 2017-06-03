var idCheckLayer;
var UsersContext = function() {
	var self = this;
	self.basePath = $("#hidden_apiurl").val();
	self.users = ko.observable({
		total : 0,
		items : []
	});
	self.chosenUserRoles = ko.observableArray([]);

	self.allRoles = [ 'ADMIN', 'MANAGER', 'SALES', 'PRODUCT', 'FINANCE' ];
	self.sexMapping = {
		'F' : '女',
		'M' : '男'
	};
	
	self.roleMapping = {
		'MANAGER' : '经理',
		'ADMIN' : '管理员',
		'SALES' : '销售人员',
		'PRODUCT' : '产品',
		'FINANCE' : '财务'
	};

	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.basePath + 'user/searchUsersByPage', param, function(data) {
			var users = data.users;
			$(users).each(function(idx, user) {
				var user_roles = user.user_roles;
				var arr = user_roles.split(",");
				var roles_name = "";
				for ( var i = 0; i < arr.length; i++) {
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

		$("#img-pic").attr("src", self.basePath + 'file/getFileStream?fileFileName=' + fileName + "&fileType=USER_ID");
	};
	// 新标签页显示大图片
	$("#img-pic").on('click', function() {
		window.open(self.basePath + "templates/common/check-picture-big.jsp?src=" + encodeURIComponent($(this).attr("src")));
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
		for ( var i = startPage; i <= endPage; i++) {
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
