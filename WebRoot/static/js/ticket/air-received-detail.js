var AirReceivedDetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.details = ko.observableArray();

	self.statuses = ['I', 'E'];
	self.statusMapping = {
		'I' : "待确认",
		'E' : "已入账"
	};

	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push('I');

	// 获取所有账户
	self.accounts = ko.observableArray([]);
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {

	}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.chosenReceiveds = ko.observableArray();

	self.typeMapping = {
		SUM : '合账',
		RECEIVED : '收入'
	}

	self.refresh = function() {
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'receivable/searchAirDetailsByPage', param, function(data) {

			self.details(data.details);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$("td:contains('已入账')").css("color", "green");
			$(".rmb").formatCurrency();
		});
	};

	self.rollBack = function() {
		let len = self.chosenReceiveds().length;
		if (len == 0) {
			fail_msg("请选择记录！");
			return;
		} else if (len > 1) {
			fail_msg("只能选择一条记录！");
			return;
		} else {
			let detail = self.chosenReceiveds()[0];
			if (detail.status != 'I') {
				fail_msg("请选择会计未确认的记录！");
				return;
			}

			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "确认要打回押金退还记录吗？",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("打回中……");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'receivable/rollBackReceivedDetail',
							data : "related_pk=" + detail.related_pk
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.chosenReceiveds.removeAll();
								self.refresh();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	}

	// 查看身份证图片
	self.checkIdPic = function(received_time, fileName) {
		$("#img-pic").attr("src", "");
		idCheckLayer = $.layer({
			type : 1,
			title : ['查看身份证图片', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			}
		});

		const split_str = received_time.split("-");
		const sub_folder = split_str[0] + "/" + split_str[1];

		$("#img-pic").attr(
				"src",
				self.apiurl + 'file/getFileStream?fileFileName=' + fileName
						+ "&fileType=SUPPLIER_RECEIVED_VOUCHER&subFolder=" + sub_folder);
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
};

var ctx = new AirReceivedDetailContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh()
});
