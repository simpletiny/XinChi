var legLayer;
var legEditLayer;
var AirLegContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.bases = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");

		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'ticket/searchSeasonTicketByPage', param,
				function(data) {
					self.bases(data.bases);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
					endLoadingIndicator();
				});
	};
	self.createSeasonTicket = function() {
		window.location.href = self.apiurl
				+ "templates/ticket/season-ticket-creation.jsp"
	}
	self.chosenTickets = ko.observableArray([]);

	self.deleteSeasonTicket = function() {
		if (self.chosenTickets().length < 1) {
			fail_msg("请选择！");
		} else if (self.chosenTickets().length > 1) {
			fail_msg("删除只能选择一个！");
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "确认要删除套票吗！",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("删除中");
						var data = "base_pk=" + self.chosenTickets()[0];
						$.ajax({
							type : "POST",
							url : self.apiurl + 'ticket/deleteSeasonTicket',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.chosenTickets.removeAll();
								self.refresh();
							} else {
								fail_msg("保存失败，联系管理员！");
							}
						});
					}
				}
			});

		}
	}

	self.editSeasonTicket = function() {
		if (self.chosenTickets().length < 1) {
			fail_msg("请选择！");
		} else if (self.chosenTickets().length > 1) {
			fail_msg("编辑只能选择一个！");
		} else {
			window.location.href = self.apiurl
					+ "templates/ticket/season-ticket-edit.jsp?key="
					+ self.chosenTickets()[0];
		}
	}

	self.search = function() {

	};

	self.resetPage = function() {

	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 50;
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
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self
				.totalCount();
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

var ctx = new AirLegContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
