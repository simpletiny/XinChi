var viewDetailLayer;
var viewCommentLayer;
var ReceivedContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenReceiveds = ko.observableArray([]);

	// 销售信息
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});

	self.receiveds = ko.observable({
		total : 0,
		items : []
	});

	// 账户信息
	self.accounts = ko.observableArray([]);
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
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

	var getWeekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek
			+ 1);
	// 获得本周的结束日期
	var getWeekEndDate = new Date(nowYear, nowMonth, nowDay
			+ (7 - nowDayOfWeek));
	self.dateTo(getWeekEndDate.Format("yyyy-MM-dd"));

	self.dateFrom(getWeekStartDate.Format("yyyy-MM-dd"));

	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("I");
	self.allStatus = [ 'I', 'N', 'E' ];

	self.statusMapping = {
		'I' : '待确认',
		'N' : '被驳回',
		'E' : '已入账'
	};

	self.typeMapping = {
		'TAIL' : '抹零',
		'SUM' : '合账',
		'STRIKE' : '冲账',
		'RECEIVED' : '收入',
		'PAY' : '支出',
		'STRIKEOUT' : '冲账/出',
		'STRIKEIN' : '冲账/入',
		'COLLECT' : '代收'
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

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;

		$.getJSON(self.apiurl + 'sale/searchReceivedByPage', param, function(
				data) {
			self.receiveds(data.receiveds);
			// 计算合计
			$(self.receiveds()).each(function(idx, data) {
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

	self.rollBack = function() {
		if (self.chosenReceiveds().length == 0) {
			fail_msg("请选择");
			return;
		}
		var pks = new Array();
		var check = true;
		$(self.chosenReceiveds()).each(function(idx, data) {
			if (data.split(";")[1] == 'Y') {
				check = false;
				return false;
			} else {
				pks.push(data.split(";")[0]);
			}
		});
		if (!check) {
			fail_msg("请选择未入账的收入");
			return;
		}
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认要打回重报吗?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					startLoadingSimpleIndicator("操作中");
					layer.close(index);
					$.ajax({
						type : "POST",
						url : self.apiurl + 'sale/rollBackReceived',
						data : "received_pks=" + pks,
						success : function(str) {
							if (str != "OK") {
								fail_msg("回滚失败，请联系管理员");
							}
							self.refresh();
							self.chosenReceiveds = ko.observableArray([]);
							endLoadingIndicator();
						}
					});
				}
			}
		});
	};
	self.sumDetails = ko.observable({
		total : 0,
		items : []
	});
	self.order = ko.observable({
		team_number : "",
		client_employee_name : "",
		product : "",
		people_count : "",
		departure_date : ""

	});
	self.comment = ko.observable();
	self.viewComment = function(detail) {
		if (detail.type == "SUM") {
			msg(detail.comment);
		} else {
			var param = "team_number=" + detail.team_number;
			startLoadingSimpleIndicator("加载中");
			$.getJSON(self.apiurl + 'sale/searchOrderByTeamNumber', param,
					function(data) {
						self.order(data.order);
						self.comment(detail.comment);
						endLoadingIndicator();
						viewCommentLayer = $.layer({
							type : 1,
							title : [ '摘要详情', '' ],
							maxmin : false,
							closeBtn : [ 1, true ],
							shadeClose : false,
							area : [ '700px', 'auto' ],
							offset : [ '150px', '' ],
							scrollbar : true,
							page : {
								dom : '#comment'
							},
							end : function() {
								console.log("Done");
							}
						});
					});
		}
	};
	self.sumDetail = ko.observable({
		card_account : "",
		sum_received : "",
		client_employee_name : "",
		allot_received : ""
	});
	self.viewDetail = function(related_pk) {
		var param = "related_pks=" + related_pk;
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'sale/searchByRelatedPks', param,
				function(data) {

					self.sumDetails(data.receiveds);
					self.sumDetail(self.sumDetails()[0]);
					$(".rmb").formatCurrency();
					endLoadingIndicator();

					viewDetailLayer = $.layer({
						type : 1,
						title : [ '合账详情', '' ],
						maxmin : false,
						closeBtn : [ 1, true ],
						shadeClose : false,
						area : [ '800px', 'auto' ],
						offset : [ '150px', '' ],
						scrollbar : true,
						page : {
							dom : '#sum_detail'
						},
						end : function() {
							console.log("Done");
						}
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

var ctx = new ReceivedContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

function baseMonth(txt) {
	if ($(txt).val().trim() != '') {
		$('.date-picker').val("");
	}
}
