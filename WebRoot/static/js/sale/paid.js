var viewDetailLayer;
var viewCommentLayer;
var viewPaidLayer;
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

	self.allStatus = ['I', 'N', 'Y', 'P'];

	self.statusMapping = {
		'I' : '待审批',
		'N' : '被驳回',
		'Y' : '已同意',
		'P' : '已支付'
	};
	self.paidTypes = [{
		name : '支付',
		key : 'PAID'
	}, {
		name : '返款',
		key : 'BACK'
	}, {
		name : '冲账',
		key : 'STRIKE'
	}, {
		name : '扣款',
		key : 'DEDUCT'
	}];
	self.typeMapping = {
		'BACK' : '返款',
		'PAID' : '支付',
		'STRIKE' : '冲账',
		'DEDUCT' : '扣款',
		'STRIKEOUT' : '冲账/出',
		'STRIKEIN' : '冲账/入'
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
		startLoadingSimpleIndicator("加载中...");
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'sale/searchPaidByPage', param, function(data) {
			self.paids(data.paids);
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
	self.reimbursement = function() {
		window.location.href = self.apiurl + "templates/accounting/reimbursement-creation.jsp";
	};
	self.rollBack = function() {
		if (self.chosenPaids().length < 1) {
			fail_msg("请选择");
			return;
		} else if (self.chosenPaids().length > 1) {
			fail_msg("只能选择一个");
			return;
		}

		var sourceData = self.chosenPaids()[0];

		if (sourceData.status == 'Y' || sourceData.status == 'P') {
			fail_msg("请选择没有同意的申请");
			return;
		}

		var data = "related_pk=" + sourceData.related_pk;

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认要打回重报吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					startLoadingSimpleIndicator("操作中");
					layer.close(index);
					$.ajax({
						type : "POST",
						url : self.apiurl + 'sale/rollBackPayApply',
						data : data,
						success : function(str) {
							if (str != "success") {
								fail_msg("回滚失败，请联系管理员");
							}
							self.refresh();
							self.chosenPaids = ko.observableArray([]);
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
		if (detail.type == "STRIKE") {
			msg(detail.comment);
		} else {
			var param = "related_pk=" + detail.related_pk;
			startLoadingSimpleIndicator("加载中");
			$.getJSON(self.apiurl + 'sale/searchPaidByRelatedPk', param, function(data) {
				self.order(data.order);
				self.comment(detail.comment);
				endLoadingIndicator();
				viewCommentLayer = $.layer({
					type : 1,
					title : ['摘要详情', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['700px', 'auto'],
					offset : ['150px', ''],
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
	self.sumDetail = ko.observable({});
	self.viewDetail = function(data) {
		var param = "related_pk=" + data.related_pk;
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'sale/searchPaidDetailsByRelatedPk', param, function(result) {
			self.sumDetails(result.paids);
			self.sumDetail(data);
			viewDetailLayer = $.layer({
				type : 1,
				title : ['合账详情', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', 'auto'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#sum_detail'
				},
				end : function() {
					console.log("Done");
				}
			});
			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	};

	self.strikeouts = ko.observableArray([]);
	self.strikeins = ko.observableArray([]);
	self.viewStrikeDetail = function(data) {
		self.strikeouts.removeAll();
		self.strikeins.removeAll();
		var param = "related_pk=" + data.related_pk;
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'sale/searchPaidDetailsByRelatedPk', param, function(result) {
			$(result.paids).each(function(idx, paid) {
				if (paid.type == "STRIKEOUT") {
					self.strikeouts.push(paid);
				} else if (paid.type == "STRIKEIN") {
					self.strikeins.push(paid);
				}
			});
			viewDetailLayer = $.layer({
				type : 1,
				title : ['冲账详情', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', 'auto'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#strike_detail'
				},
				end : function() {
					console.log("Done");
				}
			});
			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	};

	self.detail = ko.observable([]);
	self.imgs = ko.observableArray();
	self.viewPaidInfo = function(related_pk) {
		var param = "related_pk=" + related_pk
		$.getJSON(self.apiurl + 'sale/searchPaidInfo', param, function(data) {

			self.detail(data.detail);

			console.log(data);
			self.imgs(data.detail.voucher_file_name.split(";"));
			self.loadFiles();

			endLoadingIndicator();
			viewPaidLayer = $.layer({
				type : 1,
				title : ['支付详情', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', 'auto'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#div_view_detail'
				},
				end : function() {
					console.log("Done");
				}
			});
		});
	}

	self.viewRejectReason = function(back_pk) {
		var data = "back_pk=" + back_pk;

		$.ajax({
			type : "POST",
			url : self.apiurl + 'accounting/searchRejectReason',
			data : data
		}).success(function(str) {
			success_msg(str);
		});
	}
	// 加载头像
	self.loadFiles = function() {
		var imgs = $("#voucher-img").find("input[st='voucher-file-name']");

		for (var i = 0; i < imgs.length; i++) {
			var img = imgs[i];
			var container = $(img).next();

			var fileName = $(img).val();
			self.downFile(fileName, container);
		}
	};

	self.downFile = function(fileName, imgContainer) {

		files = fileName.split(",");

		if (files.length < 2)
			return;

		var formData = new FormData();
		formData.append("fileFileName", files[1]);
		formData.append("subFolder", files[0]);
		formData.append("fileType", "VOUCHER");

		var url = ctx.apiurl + 'file/getFileStream';
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.responseType = "blob";
		xhr.onload = function() {
			if (this.status == 200) {
				var blob = this.response;
				imgContainer.attr("src", window.URL.createObjectURL(blob));
			}
		};
		xhr.send(formData);
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

var ctx = new PaidContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
