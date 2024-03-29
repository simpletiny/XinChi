var fillLayer;
var viewDetailLayer;
var ProductBoxContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.user_roles = $("#user_roles").val();
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
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'SALE'
	}, function(data) {
		self.sales(data.users);
	});
	// 获取产品经理信息
	self.product_managers = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.product_managers(data.users);
	});

	self.confirm_month = ko.observable();
	var x = new Date();
	x = new Date(x.setMonth(x.getMonth() - 1));
	self.confirm_month(x.Format("yyyy-MM"));

	self.refresh = function() {
		startLoadingIndicator("加载中...");

		var param = $("form").serialize();

		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'order/searchOrderReportByPage', param, function(data) {
			self.reports(data.reports);

			$("#main-table").tableSum({
				title : '汇总',
				title_index : 5,
				except : [1, 2, 3, 4, 18, 19, 20]
			})

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$(".rmb").formatCurrency();

			endLoadingIndicator();
		});
	};

	// 确认单团核算单
	self.confirmReport = function(report) {
		// 检测是否可以审核
		if (report.order_type != 'F') {
			fail_msg("销售订单未决算，不能审核！");
			return;
		}
		if (report.independent_flg != 'A') {
			if (report.product_final_flg == null) {
				fail_msg("产品未操作，不能审核！");
				return;
			}

			if (report.product_final_flg.indexOf("N") >= 0) {
				fail_msg("地接款未决算，不能审核！");
				return;
			}
		}

		if (report.air_ticket_cost == null) {
			fail_msg("请等待票务填报机票款！");
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
		$.layer({
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
	var current_report;
	var current_td;
	// 填报机票款
	self.fillAirTicketCost = function(report, event) {
		current_report = report;
		current_td = $(event.target).parent();
		fillLayer = $.layer({
			type : 1,
			title : ['机票款', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '250px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#fill-cost'
			},
			end : function() {
			}
		});
	}
	// 确认决算
	self.doFill = function() {

		var cost = $("#air-ticket-cost").val();
		if (cost.trim() == "") {
			fail_msg("请填写机票款！");
			return;
		}

		var data = "team_number=" + current_report.team_number + "&air_ticket_cost=" + cost;
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认填报机票款吗？',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("保存中...");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'order/fillAirTicketCost',
						data : data
					}).success(function(str) {
						layer.close(fillLayer);
						endLoadingIndicator();
						if (str == "success") {
							$(current_td).text(cost);
						} else {
							fail_msg(str);
						}
					});
				}
			}
		});
	};

	self.order = ko.observable({});
	self.ticket_infos = ko.observable({});
	self.viewTeamDetail = function(team_number) {
		var param = "team_number=" + team_number;
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'order/selectOrderByTeamNumber', param, function(data) {

			self.order(data.option);
			self.ticket_infos(data.ticketInfos);
			endLoadingIndicator();

			viewDetailLayer = $.layer({
				type : 1,
				title : ['摘要详情', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['700px', 'auto'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#team-detail'
				},
				end : function() {
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
		$("input[st='st-month']").attr("disabled", true);
	} else {
		$("input[st='st-date-1']").attr("disabled", true);
		$("input[st='st-date-2']").attr("disabled", false);
		$("input[st='st-date-1']").val("");
		$("input[st='st-month']").attr("disabled", false);
	}
};