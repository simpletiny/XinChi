var ReimbursementContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.items = ko.observableArray(['X', 'B', 'P', 'J', 'G', 'Q', 'T']);
	self.itemMapping = {
		'D' : '地接款',
		'X' : '销售费用',
		'B' : '办公费用',
		'P' : '票务费用',
		'J' : '产品费用',
		'G' : '工资费用',
		'Q' : '其他支出',
		'T' : '投诉赔偿',
		'M' : '多付返款',
		'F' : 'FLY'
	};

	self.statusMapping = {
		'I' : '待审批',
		'Y' : '待支付',
		'N' : '已驳回',
		'P' : '已入账'
	};
	// 申请人信息
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});

	self.totalMoney = ko.observable();
	self.reimbursements = ko.observableArray([]);
	self.refresh = function() {
		var total = 0;
		var param = $("form").serialize();

		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;

		$.getJSON(self.apiurl + 'accounting/searchReimbursementByPage', param,
				function(data) {
					self.reimbursements(data.reimbursements);

					// 计算合计
					$(self.reimbursements()).each(function(idx, data) {
						if (data.status == "P") {
							total += data.money;
						}
					});

					self.totalMoney(total);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());

					$(".rmb").formatCurrency();
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

var ctx = new ReimbursementContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
