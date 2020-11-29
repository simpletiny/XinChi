var payLayer;
var passengerCheckLayer;
var PayableContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenPayables = ko.observableArray([]);
	self.types = ['预算', '决算'];
	self.chosenTypes = ko.observableArray([]);

	self.payables = ko.observable({
		total : 0,
		items : []
	});

	self.store = ko.observableArray([]);
	self.chosenPayables = ko.observableArray([]);

	self.team_number = ko.observable();
	self.supplier_name = ko.observable();
	self.totalPay = ko.observable();
	self.today = ko.observable();
	self.today((new Date()).Format("yyyy-MM-dd"));

	// 支付申请
	self.pay = function() {
		if (self.chosenPayables().length == 0) {
			fail_msg("请选择订单");
			return;
		} else {
			var supplier_employee_pks = new Array();
			var totalPay = 0;
			var check_result = true;
			var payable_pks = new Array();
			$(self.chosenPayables()).each(function(idx, data) {
				supplier_employee_pks.push(data.supplier_employee_pk);
				payable_pks.push(data.pk);

				if (data.final_flg == "Y") {
					if (data.final_balance == 0) {
						fail_msg(data.team_number + "尾款已清");
						check_result = false;
					} else {
						totalPay += (data.final_balance - 0);
					}

				} else {
					if (data.budget_balance == 0) {
						fail_msg(data.team_number + "尾款已清");
						check_result = false;
					} else {
						totalPay += (data.budget_balance - 0);
					}
				}
			});
			if (!check_result)
				return;
			$(".rmb").formatCurrency();
			startLoadingSimpleIndicator("检测中");
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/isSameFinancialBody2',
				data : "supplier_employee_pks=" + supplier_employee_pks,
				success : function(data) {
					if (data.isSame == "NOT") {
						fail_msg("供应商不属于同一财务主体");
					} else {
						window.location.href = self.apiurl
								+ "templates/ticket/air-ticket-paid.jsp?key="
								+ payable_pks;

					}
				}
			});
		}
	};
	// 执行支付申请
	self.applyPay = function() {
		if (!$("#form-pay").valid())
			return;
		var sumAllot = 0;
		$("[st='paid']").each(function(idx, data) {
			sumAllot += $(data).val() - 0;
		});
		if (sumAllot != $("[st='sum_paid']").val() - 0) {
			fail_msg("分配金额合计和总金额不匹配");
			return;
		}

		var data = $("#form-pay").serialize();
		var allot_json = '[';
		var allot = $("[st='pay_allot']");
		for (var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var r = $(current).find("[st='paid']").val();
			var p = $(current).find("[st='supplier_employee_pk']").val();
			var base_pk = $(current).find("[st='base-pk']").val();
			var PNR = $(current).find("[st='PNR']").val();
			allot_json += '{"paid":"' + r + '",' + '"base_pk":"' + base_pk
					+ '","PNR":"' + PNR + '",' + '"supplier_employee_pk":"' + p;
			if (i == allot.length - 1) {
				allot_json += '"}';
			} else {
				allot_json += '"},';
			}
		}
		allot_json += ']';
		layer.close(payLayer);
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'payable/applyAirTicketPay',
			data : data + "&json=" + allot_json,
			success : function(str) {
				endLoadingIndicator();
				if (str == "success") {
					self.search();
				} else {
					fail_msg("申请失败，请联系管理员");
				}
			},
			fail : function(str) {
				console.log(str);
			}
		});
	};

	self.chosenAll = function(obj) {
		console.log(obj);
	};

	// 计算合计
	self.totalBudgetPayable = ko.observable(0);
	self.totalPaid = ko.observable(0);
	self.totalBudgetBalance = ko.observable(0);

	var pages = new Array();
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var totalBudgetPayable = 0;
		var totalPaid = 0;
		var totalBudgetBalance = 0;

		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;

		$.getJSON(self.apiurl + 'payable/searchAirTicketPayableByPage', param,
				function(data) {
					self.payables(data.payables);
					if (!pages.contains(self.currentPage())) {
						self.store(self.store().concat(self.payables()));
						pages.push(self.currentPage());
					}
					// 计算合计
					$(self.payables()).each(function(idx, data) {
						totalBudgetPayable += data.budget_payable;
						totalBudgetBalance += data.budget_balance;
						totalPaid += data.paid;

					});

					self.totalBudgetPayable(totalBudgetPayable);
					self.totalPaid(totalPaid);

					self.totalBudgetBalance(totalBudgetBalance);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());

					$(".rmb").formatCurrency();
					endLoadingIndicator();
				});
	};

	self.zeroBalance = function() {
		self.refresh();
		return true;
	};

	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(data, event) {
		self.passengers.removeAll();
		var pk = data.pk;
		var url = "payable/searchPayablePassengersByPayablePk";
		$.getJSON(self.apiurl + url, {
			payable_pk : pk
		}, function(data) {
			self.passengers(data.passengers);
			console.log(data.passengers);
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['名单信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#passengers-check'
				},
				end : function() {
				}
			});
		});
	};
	self.infos = ko.observableArray([]);
	// 航班信息
	self.checkTicketInfo = function(data, event) {
		self.passengers.removeAll();
		var pk = data.pk;
		var url = "payable/searchTicketInfoByPayablePk";

		$.getJSON(self.apiurl + url, {
			payable_pk : pk
		}, function(data) {
			self.infos(data.ptInfos);
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['航班信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#infos-check'
				},
				end : function() {
				}
			});
		});
	};
	// self.changeType = function() {
	// if (self.chosenTypes().length == 0 || self.chosenTypes().length == 2) {
	// $("[st='all']").show();
	// $("[st='budget']").hide();
	// $("[st='final']").hide();
	// } else if (self.chosenTypes()[0] == "预算") {
	// $("[st='all']").hide();
	// $("[st='budget']").show();
	// $("[st='final']").hide();
	// } else {
	// $("[st='all']").hide();
	// $("[st='budget']").hide();
	// $("[st='final']").show();
	// }
	//
	// return true;
	// };

	self.search = function() {
		self.refresh();
		console.log("DDWWO")
	};

	self.resetPage = function() {

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

var ctx = new PayableContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
