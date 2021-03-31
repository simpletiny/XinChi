var ProductBoxContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.reports = ko.observable({});
	self.orderTypeMapping = ({
		'Y' : '预',
		'F' : '决'
	});
	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("N");
	self.statuses = ['N', 'Y'];
	self.approvedMapping = ({
		'Y' : '已审核',
		'N' : '未审核'
	});

	// 销售信息
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});
	self.refresh = function() {
		startLoadingIndicator("加载中...");

		var param = $("form").serialize();

		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'order/searchOrderReportByPage', param,
				function(data) {
					self.reports(data.reports);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());

					$(".rmb").formatCurrency();

					endLoadingIndicator();
				});
	};

	// 确认单团核算单
	self.confirmReport = function(report) {
		console.log(report)
		// 检测是否可以审核
		if (report.order_type != 'F') {
			fail_msg("销售订单未决算，不能审核！");
			return;
		}

		if (report.product_final_flg == null) {
			fail_msg("产品未操作，不能审核！");
			return;
		}

		if (report.product_final_flg.indexOf("N") >= 0) {
			fail_msg("地接款未决算，不能审核！");
			return;
		}

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : "&nbsp;&nbsp;&nbsp;&nbsp;确认无误？&nbsp;&nbsp;&nbsp;&nbsp;",
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("确认中...");
					var data = "team_number=" + report.team_number;

					$.ajax({
						type : "POST",
						url : self.apiurl + 'order/approveTeamReport',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
						} else {
							fail_msg("请联系管理员！");
						}
					});
				}
			}
		});
	}
	// 打回已审核的单团核算单
	self.rollBackReport = function(report) {
		$
				.layer({
					area : ['auto', 'auto'],
					dialog : {
						msg : "&nbsp;&nbsp;&nbsp;&nbsp;确认打回重审吗？&nbsp;&nbsp;&nbsp;&nbsp;",
						btns : 2,
						type : 4,
						btn : ['确认', '取消'],
						yes : function(index) {
							layer.close(index);
							startLoadingIndicator("打回中...");
							var data = "team_number=" + report.team_number;

							$.ajax({
								type : "POST",
								url : self.apiurl + 'order/rollBackTeamReport',
								data : data
							}).success(function(str) {
								endLoadingIndicator();
								if (str == "success") {
									self.refresh();
								} else {
									fail_msg("请联系管理员！");
								}
							});
						}
					}
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

var ctx = new ProductBoxContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
var check = function(radio) {
	var r = $(radio).val();
	if (r == "1") {
		$("input[st='st-date-1']").attr("disabled", false);
		$("input[st='st-date-2']").val("");
		$("input[st='st-date-2']").attr("disabled", true);
	} else {
		$("input[st='st-date-1']").attr("disabled", true);
		$("input[st='st-date-2']").attr("disabled", false);
		$("input[st='st-date-1']").val("");
	}
};