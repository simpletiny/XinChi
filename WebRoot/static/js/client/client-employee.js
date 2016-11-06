var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenEmployees = ko.observableArray([]);
	self.employeeArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化', '呼伦贝尔',
	        			'伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭' ];
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 20;
	self.pageNums = ko.observableArray();
	self.totalCount = ko.observable(1);
	self.startIndex = ko.computed(function() {
		return (self.currentPage() - 1) * self.perPage;
	});
	// end pagination

	self.createClientEmployee = function() {
		window.location.href = self.apiurl
				+ "templates/client/employee-creation.jsp";
	};

	self.employees = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(
				data) {
			self.employees(data.employees);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenEmployees().length == 1) {
			window.location.href = self.apiurl
					+ "templates/client/employee-edit.jsp?key="
					+ self.chosenEmployees()[0];
		}
	};

	// start pagination
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
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self
				.totalCount();
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

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
