var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.scores = ko.observableArray([]);
	self.confirm_month = ko.observable();
	var x = new Date();
	self.confirm_month(x.Format("yyyy-MM"));
	self.refresh = function() {

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'order/searchSaleScoreByPage', param, function(data) {
			self.scores(data.scores);
			$(".table").tableSum({
				except : [1, 10]
			});
		});
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

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

var switchType = function(rad) {
	var v = $(rad).val();
	if (v == "month") {
		$(".date-picker").attr("disabled", true);
		$(".month-picker-st").attr("disabled", false);
	} else {
		$(".date-picker").attr("disabled", false);
		$(".month-picker-st").attr("disabled", true);
	}
}
var st_page = "s";
var st_page_map = {
	"s" : "sale-score.jsp",
	"b" : "back-money-score.jsp"
}
function changePage(rad) {
	var v = $(rad).val();
	if (v == st_page)
		return;
	else
		window.location.href = ctx.apiurl + 'templates/sale/' + st_page_map[v];
}