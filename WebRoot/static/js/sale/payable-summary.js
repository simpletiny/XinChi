var deductLayer;
var strikeLayer;
var receiveLayer;
var payLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.types = ['预算', '决算'];
	self.chosenType = ko.observable();
	self.chosenType('决算');
	self.paysum = ko.observable({});

	self.payableSummarys = ko.observable({
		total : 0,
		items : []
	});

	self.current_month = ko.observable();
	self.last_month = ko.observable();
	self.before_last_month = ko.observable();

	var x = new Date();
	self.current_month(x.Format("yyyy年MM月"))
	self.last_month(x.addMonth(-1).Format("yyyy年MM月"));
	self.before_last_month(x.addMonth(-2).Format("yyyy年MM月"));

	// 计算合计
	self.all_budget_balance = ko.observable(0);

	self.all_final_balance = ko.observable(0);
	self.all_expected_balance = ko.observable(0);

	self.current_month_budget_balance = ko.observable(0);
	self.current_month_final_balance = ko.observable(0);
	self.current_month_budget_payable = ko.observable(0);
	self.current_month_final_payable = ko.observable(0);

	self.one_month_budget_balance = ko.observable(0);
	self.one_month_final_balance = ko.observable(0);
	self.one_month_budget_payable = ko.observable(0);
	self.one_month_final_payable = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);
	self.current_month_paid = ko.observable(0);

	// <td data-bind="text: $data.one_month_budget_balance" class="rmb border
	// budget"></td>
	// <td data-bind="text: $data.one_month_final_balance" class="rmb border
	// final"></td>
	// <td data-bind="text: $data.one_month_budget_payable" class="rmb
	// budget"></td>
	// <td data-bind="text: $data.one_month_final_payable" class="rmb
	// final"></td>
	// <td data-bind="text: $data.one_month_paid" class="rmb"></td>
	//	
	// <td data-bind="text: $data.two_month_budget_balance" class="rmb border
	// budget"></td>
	// <td data-bind="text: $data.two_month_final_balance" class="rmb border
	// final"></td>
	// <td data-bind="text: $data.two_month_budget_payable" class="rmb
	// budget"></td>
	// <td data-bind="text: $data.two_month_final_payable" class="rmb
	// final"></td>
	// <td data-bind="text: $data.two_month_paid" class="rmb"></td>
	//	
	// <td data-bind="text: $data.earlier_month_budget_balance" class="rmb
	// border budget"></td>
	// <td data-bind="text: $data.earlier_month_final_balance" class="rmb border
	// final"></td>
	// <td data-bind="text: $data.earlier_month_budget_payable" class="rmb
	// budget"></td>
	// <td data-bind="text: $data.earlier_month_final_payable" class="rmb
	// final"></td>
	// <td data-bind="text: $data.earlier_month_paid" class="rmb"></td>

	self.refresh = function() {

		startLoadingIndicator("加载中……")
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'payable/searchPayableSummaryByPage', param, function(data) {

			self.payableSummarys(data.payableSummarys);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			caculate_sum();
			$(".rmb").formatCurrency();

			endLoadingIndicator();
		});
	};

	self.changeType = function() {
		if (self.chosenType() == "预算") {
			$(".budget").show();
			$(".final").hide();
		} else if (self.chosenType() == "决算") {
			$(".budget").hide();
			$(".final").show();
		}
		return true;
	};

	// 销售信息
	self.chosenSales = ko.observableArray([]);
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : "PRODUCT"
	}, function(data) {
		self.sales(data.users);
	});
	self.search = function() {
		self.refresh();
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
	ctx.search();

});

function caculate_sum() {
	var len = +$("#head th:not(:first-child)").length;
	var sum = new Array(23).fill(0);

	$("#tbody-data tr").each(function() {
		$(this).children('td:not(:first-child)').each(function() {
			var index = $(this).index() - 1;

			sum[index] += +$(this).text();
		});
	});

	$('#total-row td:not(:first-child)').each(function() {
		var index = $(this).index() - 1;
		$(this).text(sum[index]);
	});
}
