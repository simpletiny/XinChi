var viewDetailLayer;
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

	var getWeekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek
			+ 1);
	// 获得本周的结束日期
	var getWeekEndDate = new Date(nowYear, nowMonth, nowDay
			+ (7 - nowDayOfWeek));
	self.dateTo(getWeekEndDate.Format("yyyy-MM-dd"));
	self.dateFrom(getWeekStartDate.Format("yyyy-MM-dd"));

	self.statusMapping = {
		'I' : '待审批',
		'N' : '被驳回',
		'Y' : '已同意',
		'P' : '已支付'
	};

	self.typeMapping = {
		'BACK' : '返款',
		'PAID' : '支付',
		'STRIKE' : '冲账',
		'DEDUCT' : '扣款',
		'STRIKEOUT' : '冲账/出',
		'STRIKEIN' : '冲账/入',
		'DSTRIKEIN' : '押金冲账/入'
	};

	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;

		$.getJSON(self.apiurl + 'payable/searchAirTicketPaidDetailByPage',
				param, function(data) {
					self.paids(data.paids);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());

					$(".rmb").formatCurrency();
				});
	};

	/**
	 * 打回重报
	 */
	self.rollBack = function() {
		if (self.chosenPaids().length < 1) {
			fail_msg("请选择");
			return;
		} else if (self.chosenPaids().length > 1) {
			fail_msg("只能选择一个");
			return;
		}

		var related_pk = self.chosenPaids()[0];

		var data = {
			related_pk : related_pk
		}
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
						url : self.apiurl + 'payable/rollBackTicketPayApply',
						data : data,
						success : function(str) {
							if (str != "success") {
								fail_msg("回滚失败，请联系管理员");
							}
							self.refresh();
							self.chosenPaids.removeAll();
							endLoadingIndicator();
						}
					});
				}
			}
		});
	};
	self.details = ko.observableArray([]);

	self.paymentDetails = ko.observableArray([]);

	self.viewDetail = function(data, event) {
		var param = "related_pk=" + data.related_pk;
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'payable/searchPaidDetailByRelatedPk', param,
				function(data) {

					self.details(data.details);
					self.paymentDetails(data.payment_details);

					console.log(self.details());
					console.log(self.paymentDetails());
					$(".rmb").formatCurrency();
					endLoadingIndicator();
					viewDetailLayer = $.layer({
						type : 1,
						title : ['支付详情', ''],
						maxmin : false,
						closeBtn : [1, true],
						shadeClose : false,
						area : ['1200px', '600px'],
						offset : ['', ''],
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

	self.checkVoucherPic = function(fileName, accountId) {
		$("#img-pic").attr("src", "");
		voucherCheckLayer = $.layer({
			type : 1,
			title : ['查看凭证', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});

		$("#img-pic").attr(
				"src",
				self.apiurl + 'file/getFileStream?fileFileName=' + fileName
						+ "&fileType=VOUCHER&subFolder=" + accountId);
	};
	// 新标签页显示大图片
	$("#img-pic").on(
			'click',
			function() {
				window.open(self.apiurl
						+ "templates/common/check-picture-big.jsp?src="
						+ encodeURIComponent($(this).attr("src")));
			});

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

var ctx = new PaidContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
