var ReportContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.statuses = ['I', 'Y', 'N'];
	self.statusMapping = {
		'I' : '待确认',
		'N' : "被驳回",
		'Y' : "已确认"
	}

	self.chosenReconciliations = ko.observableArray([]);

	// 获取用户信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.users(data.users);
	});

	self.reconciliations = ko.observableArray([]);

	self.refresh = function() {
		startLoadingIndicator("加载中...");
		var param = $('form').serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchReconciliationByPage', param, function(data) {
			self.reconciliations(data.reconciliations);

			$(".rmb").formatCurrency();

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			endLoadingIndicator();
		});
	};

	self.delete_reconciliation = function() {
		if (self.chosenReconciliations().length > 1) {
			fail_msg("只能选择一个!");
			return;
		} else if (self.chosenReconciliations().length == 0) {
			fail_msg("请选择!");
			return;
		} else if (self.chosenReconciliations().length == 1) {
			const data = self.chosenReconciliations()[0];
			const status = data.status;

			if (status == 'Y') {
				fail_msg("不能删除已确认明细！");
				return;
			}

			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "确认要删除此条明细吗？",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("删除中");
						const pk = data.pk;
						const param = "reconciliation_pk=" + pk;
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/deleteReconciliation',
							data : param
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenReconciliations.removeAll();
							} else {
								fail_msg("删除失败！");
							}
						});

					}
				}
			});

		}
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

var ctx = new ReportContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
