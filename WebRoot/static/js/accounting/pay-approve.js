var reasonLayer;
var employeeLayer;
var PaidContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenPaids = ko.observableArray([]);

	self.paids = ko.observable({
		total : 0,
		items : []
	});
	self.items = ko.observableArray(['D', 'X', 'H', 'J', 'T', 'P', 'B', 'E', 'K', 'G', 'C', 'Q', 'M', 'F'])
	self.itemMapping = {
		'D' : '地接款',
		'X' : '销售费用',
		'H' : '亲情费用',
		'J' : '产品费用',
		'T' : '唯品费',
		'P' : '票务费用',
		'B' : '办公费用',
		'E' : '招待费',
		'K' : '差旅费用',
		'G' : '个人工资',
		'C' : '分红分润',
		'Q' : '其它支出',
		'M' : '多付返款',
		'F' : 'FLY'
	};

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

	self.chosenStatus = ko.observableArray([]);
	self.allStatus = ['I', 'N', 'Y'];
	self.chosenStatus.push('I');

	self.statusMapping = {
		'I' : '待审批',
		'Y' : '已同意',
		'N' : '已驳回',
		'P' : '已入账'
	};
	// 计算合计
	self.totalPeople = ko.observable(0);
	self.totalReceivable = ko.observable(0);
	self.totalPayable = ko.observable(0);
	self.totalProfit = ko.observable(0);
	self.totalPerProfit = ko.observable(0);

	self.sumBalance = ko.observable();
	self.sumCardBalance = ko.observable();
	self.sum_waiting_for_paid = ko.observable();
	self.refresh = function() {

		startLoadingIndicator("加载中……");
		var totalPeople = 0;
		var totalReceivable = 0;
		var totalPayable = 0;
		var totalProfit = 0;
		var totalPerProfit = 0;

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'accounting/searchPaidApplyByPage', param, function(data) {
			self.paids(data.payApprovals);
			self.sumBalance(data.sum_balance);
			self.sumCardBalance(data.sum_card_balance);
			self.sum_waiting_for_paid(data.sum_waiting_for_paid);
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
			endLoadingIndicator();
		});
	};

	self.agree = function(paid) {
		var data = "item=" + paid.item + "&pk=" + paid.pk;

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认同意此申请吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("提交中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'accounting/agreePayApply',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
						} else if (str == "NOFINANCIAL") {
							fail_msg("客户不存在财务主体！");
						}
					});
				}
			}
		});
	};

	var current_data;
	self.reject = function(paid) {
		current_data = paid;
		$("#txt-comment").val("");
		reasonLayer = $.layer({
			type : 1,
			title : ['驳回理由', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '300px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#comment'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.doReject = function() {
		var data = "item=" + current_data.item + "&pk=" + current_data.pk;

		var comment = $("#txt-comment").val().trim();
		data += "&reject_reason=" + comment;
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认拒绝此申请吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("提交中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'accounting/rejectPayApply',
						data : data
					}).success(function(str) {

						if (str == "success") {
							layer.close(reasonLayer);
							self.refresh();
							endLoadingIndicator();
						}
					});
				}
			}
		});
	}
	self.cancel = function() {
		layer.close(reasonLayer);
	}
	self.employee = ko.observable({});
	self.checkEmployee = function(data) {
		var param = "related_pk=" + data.related_pk;

		$.getJSON(self.apiurl + 'accounting/searchMoreBackClientEmployee', param, function(data) {
			self.employee(data.client_employee);
			console.log(data.client_employee);
			employeeLayer = $.layer({
				type : 1,
				title : ['多付返款客户', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['600px', '300px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#employee'
				},
				end : function() {
					console.log("Done");
				}
			});
		});

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

var ctx = new PaidContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
