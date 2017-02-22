var ClientContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.relations = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchRelationsByPage', param, function(data) {
			self.relations(data.relations);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			$(".rmb").formatCurrency();
		});
	};
	
	self.clientSummary = ko.observable({});
	self.searchClientSummary = function() {
		var param = $("form").serialize();
		$.getJSON(self.apiurl + 'client/searchClientSummary', param, function(data) {
			self.clientSummary(data.clientSummary);
			$(".rmb").formatCurrency();
		});
	};

	self.createVisit = function() {
		window.location.href = self.apiurl + "templates/client/visit-creation.jsp";

	};

	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	self.sales_name = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
		$(self.sales()).each(function(idx, data) {
			self.sales_name.push(data.user_name);
		});
	});

	// 获取摘要信息
	self.fetchSummary = function() {
		self.refresh();
		self.searchClientSummary();
	};

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

var ctx = new ClientContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	ctx.searchClientSummary();
});