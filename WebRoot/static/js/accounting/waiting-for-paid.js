var viewDetailLayer;
var viewCommentLayer;
var PaidContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenPaids = ko.observableArray([]);

	self.paids = ko.observable({
		total : 0,
		items : []
	});

	self.dateFrom = ko.observable();
	self.dateTo = ko.observable();
	// var x = new Date();

	var now = new Date(); // 当前日期
	var nowDayOfWeek = now.getDay(); // 今天本周的第几天
	var nowDay = now.getDate(); // 当前日
	var nowMonth = now.getMonth(); // 当前月
	var nowYear = now.getFullYear();
	if (nowDayOfWeek == 0) {
		nowDayOfWeek = 7;
	}

	var getWeekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek + 1);
	// 获得本周的结束日期
	var getWeekEndDate = new Date(nowYear, nowMonth, nowDay + (7 - nowDayOfWeek));
	self.dateTo(getWeekEndDate.Format("yyyy-MM-dd"));

	self.dateFrom(getWeekStartDate.Format("yyyy-MM-dd"));

	self.chosenStatus = ko.observableArray(['I']);
	self.allStatus = [ 'I', 'N', 'Y' ];
	self.chosenStatus.push('I');

	self.itemMapping = {
		'D' : '地接款',
		'X' : '销售费用',
		'B' : '办公费用',
		'C' : '产品费用',
		'P' : '票务费用',
		'J' : '交通垫付',
		'G' : '工资费用',
		'Q' : '其他支出'
	};

	self.statusMapping = {
		'I' : '待支付',
		'Y' : '已支付',
		'N' : '已驳回'
	};
	// 计算合计
	self.totalPeople = ko.observable(0);
	self.totalReceivable = ko.observable(0);
	self.totalPayable = ko.observable(0);
	self.totalProfit = ko.observable(0);
	self.totalPerProfit = ko.observable(0);

	self.refresh = function() {
		var totalPeople = 0;
		var totalReceivable = 0;
		var totalPayable = 0;
		var totalProfit = 0;
		var totalPerProfit = 0;

		var param = $("form").serialize() + "&wfp.status=" + self.chosenStatus();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'accounting/searchWaitingForPaidByPage', param, function(data) {
			self.paids(data.wfps);
			// 计算合计
			$(self.paids()).each(function(idx, data) {
				totalPeople += data.people_count;
				totalReceivable += data.receivable;
				totalPayable += data.payable;
				totalProfit += data.gross_profit;
			});

			self.totalPeople(totalPeople);
			self.totalReceivable(totalReceivable);
			self.totalPayable(totalPayable);
			self.totalProfit(totalProfit);
			self.totalPerProfit((totalProfit / totalPeople).toFixed(2));

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$(".rmb").formatCurrency();
		});
	};

	self.pay = function() {
		if (self.chosenPaids().length == 0) {
			fail_msg("请选择");
			return;
		} else if (self.chosenPaids().length > 1) {
			fail_msg("只能选中一个");
			return;
		} else if (self.chosenPaids().length == 1) {
			window.location.href = self.apiurl + "templates/accounting/paid.jsp?key="+self.chosenPaids()[0];
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

var ctx = new PaidContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
