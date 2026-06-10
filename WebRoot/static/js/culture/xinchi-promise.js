var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenViews = ko.observableArray([]);

	self.createView = function() {
		window.location.href = self.apiurl + "templates/culture/xinchi-promise-creation.jsp";
	};

	self.views = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中……");
		var param = "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'culture/searchXinChiPromiseByPage', param, function(data) {
			self.views(data.views);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			endLoadingIndicator();
		});
	};

	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.deleteView = function() {
		if (self.chosenViews().length == 0) {
			fail_msg("请选择要删除的文章");
			return;
		} else if (self.chosenViews().length > 1) {
			fail_msg("删除每次只能选中一个");
			return;
		} else if (self.chosenViews().length == 1) {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要删除这篇文章吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						$.ajax({
							type : "POST",
							url : self.apiurl + 'culture/deleteXinChiPromise',
							data : "view_pk=" + self.chosenViews()
						}).success(function(str) {
							if (str == "OK") {
								window.location.href = self.apiurl + "templates/culture/xinchi-promise.jsp";
							}
						});
					}
				}
			});
		}
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

var ctx = new ViewContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
