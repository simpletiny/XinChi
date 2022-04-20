var viewDetailLayer;
var deductLayer;
var depositLayer;
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

	self.statusMapping = {
		'I' : '待审批',
		'N' : '被驳回',
		'Y' : '已同意',
		'P' : '已入账'
	};
	self.types = ['BACK', 'PAID', 'STRIKE', 'DEDUCT', 'STRIKEOUT', 'STRIKEIN', 'DSTRIKEIN', 'RECEIVE', 'PAY'];

	self.typeMapping = {
		'BACK' : '返款',
		'PAID' : '支付',
		'STRIKE' : '冲账',
		'DEDUCT' : '扣款',
		'STRIKEOUT' : '冲账/出',
		'STRIKEIN' : '冲账/入',
		'DSTRIKEIN' : '押金冲账/入',
		'RECEIVE' : '无业务收入',
		'PAY' : '无业务支出'
	};

	self.refresh = function() {
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'payable/searchAirTicketPaidDetailByPage', param, function(data) {
			self.paids(data.paids);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$(".rmb").formatCurrency();
		});
	};

	self.createDetail = function(type) {
		window.location.href = self.apiurl + "templates/ticket/ticket-detail-creation.jsp?key=" + type;
	}
	/**
	 * 无业务押金退款
	 */
	self.createDeduct = function() {
		deductLayer = $.layer({
			type : 1,
			title : ['押金扣款', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['1000px', '650px'],
			offset : ['150px', ''],
			scrollbar : true,
			page : {
				dom : '#div-deduct'
			},
			end : function() {
				console.log("Done");
			}
		});
	}

	self.doCreateDeduct = function() {
		if (!$("#form-deduct").valid()) {
			return;
		}
		if (self.usedDeposits().length == 0) {
			fail_msg("请选择押金账！");
			return;
		}

		var deduct_money = $("#deduct-money").val().trim();
		var deposit_pk = $("#deposit-pk").val().trim();
		var deposit_balance = $("#deposit-balance").val().trim();
		var comment = $("#comment").val().trim();
		var date = $("#deduct-date").val().trim();

		if (deduct_money == "") {
			fail_msg("请填写扣款金额！");
			return;
		}
		if ((deduct_money - 0) > (deposit_balance - 0)) {
			fail_msg("扣款金额不能大于可用余额！");
			return;
		}

		if (date == "") {
			fail_msg("请填写扣款日期！");
			return;
		}
		if (comment == "") {
			fail_msg("扣款必须填写备注!");
			return;
		}

		var json = '{"deduct_money":"' + deduct_money + '","deposit_pk":"' + deposit_pk + '","time":"' + date
				+ '","comment":"' + comment + '"}';

		var data = "json=" + json;
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认要提交押金扣款吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					startLoadingSimpleIndicator("操作中");
					layer.close(index);
					$.ajax({
						type : "POST",
						url : self.apiurl + 'payable/createDeduct',
						data : data,
						success : function(str) {
							if (str != "success") {
								fail_msg("提交失败");
							}
							self.refresh();
							self.usedDeposits.removeAll();
							layer.close(deductLayer);
							endLoadingIndicator();
						}
					});
				}
			}
		});
	}

	self.cancelDeduct = function() {
		layer.close(deductLayer);
		self.usedDeposits.removeAll();
	}
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

		var param = self.chosenPaids()[0].split(";");

		var related_pk = param[0];
		var type = param[1];
		var status = param[2];

		if (type == "RECEIVE" || type == "BACK") {
			if (status == "P") {
				fail_msg("已入账，不能打回！");
				return;
			}
		}

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
								fail_msg(str);
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
		$.getJSON(self.apiurl + 'payable/searchPaidDetailByRelatedPk', param, function(data) {

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
				self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=VOUCHER&subFolder="
						+ accountId);
	};
	// 新标签页显示大图片
	$("#img-pic").on(
			'click',
			function() {
				window.open(self.apiurl + "templates/common/check-picture-big.jsp?src="
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

	// 获取票务账户
	self.ticketAccounts = ko.observableArray([]);
	$.getJSON(self.apiurl + 'finance/searchCardsByPurpose', {
		purpose : 'TICKET'
	}, function(data) {
		if (data.cards) {
			self.ticketAccounts(data.cards);
		} else {
			fail_msg("不存在票务账户，无法支付！");
			setTimeout(function() {
				window.history.go(-1);
			}, 2000);
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.deposits = ko.observable();
	self.usedDeposits = ko.observableArray([]);
	self.chosenDeposits = ko.observableArray([]);
	self.chooseDeposit = function() {
		self.searchDeposit();
		self.chosenDeposits.removeAll();
		depositLayer = $.layer({
			type : 1,
			title : ['选择航司押金', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['900px', '650px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#deposit-pick'
			},
			end : function() {
				return true;
			}
		});
	};

	self.refresh1 = function() {
		var param = $("#form-search-deposit").serialize();
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1 + "&deposit.deposit_type=A";

		$.getJSON(self.apiurl + 'supplier/searchDepositByPage', param, function(data) {
			self.deposits(data.deposits);
			self.totalCount1(Math.ceil(data.page.total / self.perPage1));
			self.setPageNums1(self.currentPage1());
			$(".rmb").formatCurrency();
		});

	};

	self.searchDeposit = function() {
		self.refresh1();
	};

	self.finishChoose = function() {
		if (self.chosenDeposits().length <= 0) {
			fail_msg("请选择押金账！");
			return;
		} else if (self.chosenDeposits().length > 1) {
			fail_msg("只能选择一笔押金账！");
			return;
		};
		self.usedDeposits(self.chosenDeposits());
		layer.close(depositLayer);
	}

	// start deposit pick pagination
	self.currentPage1 = ko.observable(1);
	self.perPage1 = 10;
	self.pageNums1 = ko.observableArray();
	self.totalCount1 = ko.observable(1);
	self.startIndex1 = ko.computed(function() {
		return (self.currentPage1() - 1) * self.perPage1;
	});

	self.resetPage1 = function() {
		self.currentPage1(1);
	};

	self.previousPage1 = function() {
		if (self.currentPage1() > 1) {
			self.currentPage1(self.currentPage1() - 1);
			self.refreshPage1();
		}
	};

	self.nextPage1 = function() {
		if (self.currentPage1() < self.pageNums1().length) {
			self.currentPage1(self.currentPage1() + 1);
			self.refreshPage1();
		}
	};

	self.turnPage1 = function(pageIndex) {
		self.currentPage1(pageIndex);
		self.refreshPage1();
	};

	self.setPageNums1 = function(curPage) {
		var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage = curPage + 4 <= self.totalCount1() ? curPage + 4 : self.totalCount1();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums1(pageNums);
	};

	self.refreshPage1 = function() {
		self.searchDeposit();

	};
};

var ctx = new PaidContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
