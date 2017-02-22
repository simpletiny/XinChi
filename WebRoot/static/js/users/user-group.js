var GroupContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	// 新建用户组
	self.create = function() {
		window.location.href = self.apiurl + "templates/users/group-creation.jsp";
	};
	self.groups = ko.observable({
		total : 0,
		items : []
	});
	$.getJSON(self.apiurl + 'user/searchAllGroups', {}, function(data) {
		self.groups(data.groups);
	});

	self.getUsers = function(group_pk) {

		return "暂未处理";
	};
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 10;
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
		if (currentType == "supplier") {
			self.searchSupplierEmployee();
		} else {
			self.searchClientEmployee();
		}

	};
	// end pagination
};

var ctx = new GroupContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
});
