var deductLayer;
var strikeLayer;
var receiveLayer;
var payLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);
	self.teamStatus = ['未出团', '已出团', '已回团'];
	self.types = ['预算', '决算'];
	self.chosenTypes = ko.observableArray([]);
	self.paysum = ko.observable({});
	self.sortTypes = ['倒序', '正序'];

	// 获取摘要信息
	self.fetchSummary = function() {
		$.getJSON(self.apiurl + 'sale/searchPayableSummary', {
			create_user : self.chosenSales()
		}, function(data) {
			self.paysum(data.summary);
			$(".rmb").formatCurrency();
		});
	};

	self.payables = ko.observable({
		total : 0,
		items : []
	});

	self.store = ko.observableArray([]);
	self.chosenPayables = ko.observableArray([]);

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

	// 表头月份数组
	self.titleMonth = ko.observableArray([]);
	var x = new Date();
	self.titleMonth.push("");
	self.titleMonth.push("全部");
	self.titleMonth.push("本月");
	for (var i = 0; i < 3; i++) {
		x.setMonth(x.getMonth() - 1);
		self.titleMonth.push(x.Format("yyyy年MM月"));
	}
	self.titleMonth.push("更早");

	self.tailMoney = ko.observable();
	self.team_number = ko.observable();
	self.client_employee_name = ko.observable();

	self.supplier_name = ko.observable();
	self.totalBack = ko.observable();
	// 返款收入
	self.receive = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else {
			var supplier_employee_pks = new Array();
			var totalBack = 0;
			var check_result = true;
			$(self.chosenOrders()).each(function(idx, data) {
				supplier_employee_pks.push(data.supplier_employee_pk);

				if (data.final_flg == "Y") {
					if (data.final_balance >= 0) {
						fail_msg(data.team_number + "尾款为正，不存在返款");
						check_result = false;
					} else {
						totalBack += (data.final_balance - 0);
					}
				} else {
					if (data.budget_balance >= 0) {
						fail_msg(data.team_number + "尾款为正，不存在返款");
						check_result = false;
					} else {
						totalBack += (data.budget_balance - 0);
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
						self.supplier_name(data.supplier.supplier_short_name);
						self.totalBack(totalBack * -1);
						receiveLayer = $.layer({
							type : 1,
							title : ['收入', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['1000px', '650px'],
							offset : ['150px', ''],
							scrollbar : true,
							page : {
								dom : '#receive'
							},
							end : function() {
								console.log("Done");
							}
						});
						$("#receive").attr("overflow", "yes");
					}
					endLoadingIndicator();
				}
			});
		}
	};
	// 执行返款收入
	self.applyReceive = function() {
		if (!$("#form-receive").valid())
			return;
		var sumAllot = 0;
		$("[st='back_receive']").each(function(idx, data) {
			sumAllot += $(data).val() - 0;
		});
		if (sumAllot != $("[st='sum_received']").val() - 0) {
			fail_msg("分配金额合计和总金额不匹配");
			return;
		}

		var data = $("#form-receive").serialize();
		var allot_json = '[';
		var allot = $("[st='back_allot']");
		for (var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var payable_pk = $(current).find("[st='back-pk']").val();
			var n = $(current).find("[st='team_number']").val();
			var r = $(current).find("[st='back_receive']").val();
			var p = $(current).find("[st='supplier_employee_pk']").val();
			var m = $(current).find("[st='supplier_employee_name']").val();
			allot_json += '{"team_number":"' + n + '",' + '"payable_pk":"' + payable_pk + '","received":"' + r + '",'
					+ '"supplier_employee_name":"' + m + '",' + '"supplier_employee_pk":"' + p;
			if (i == allot.length - 1) {
				allot_json += '"}';
			} else {
				allot_json += '"},';
			}
		}
		allot_json += ']';
		layer.close(receiveLayer);
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyBackRecive',
			data : data + "&allot_json=" + allot_json,
			success : function(str) {
				if (str != "success") {
					fail_msg("申请失败，请联系管理员");
				}

				self.search();
				endLoadingIndicator();
			},
			fail : function(str) {
				console.log(str);
			}
		});
	};

	self.totalPay = ko.observable();
	self.today = ko.observable();
	var today_date = new Date();
	self.today(today_date.Format("yyyy-MM-dd"));
	self.tomorrow = ko.observable();
	var dow = today_date.getDay();
	if (dow == 5 || dow == 6) {
		self.tomorrow(today_date.addDate(8 - dow).Format("yyyy-MM-dd 18:00"))
	} else {
		self.tomorrow(today_date.addDate(1).Format("yyyy-MM-dd 18:00"))
	}

	// 支付申请
	self.pay = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else {
			var supplier_employee_pks = new Array();
			var totalPay = 0;
			var check_result = true;
			$(self.chosenOrders()).each(function(idx, data) {
				supplier_employee_pks.push(data.supplier_employee_pk);
				if (data.final_flg == "Y") {
					if (data.final_balance == 0) {
						fail_msg(data.team_number + "尾款已清!");
						check_result = false;
					} else if (data.final_balance < 0) {
						fail_msg(data.team_number + "尾款为负!请选择收入或冲账处理。");
						check_result = false;
					} else {
						totalPay += (data.final_balance - 0);
					}

				} else {
					if (data.budget_balance == 0) {
						fail_msg(data.team_number + "尾款已清!");
						check_result = false;
					} else if (data.budget_balance < 0) {
						fail_msg(data.team_number + "尾款为负!请选择收入或冲账处理。");
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
						self.supplier_name(data.supplier.supplier_short_name);
						self.totalPay(totalPay);
						payLayer = $.layer({
							type : 1,
							title : ['支付申请', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['1000px', '800px'],
							offset : ['150px', ''],
							scrollbar : true,
							page : {
								dom : '#pay'
							},
							end : function() {
								console.log("Done");
							}
						});
					}
					endLoadingIndicator();
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

		// 验证时间的合法性
		var limit_time = $(".st-limit-time").val();
		var date_limit = new Date(limit_time);
		if (!isLegalLimitDate(date_limit))
			return;

		var data = $("#form-pay").serialize();
		var allot_json = '[';
		var allot = $("[st='pay_allot']");
		for (var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var payable_pk = $(current).find("[st='pay-pk']").val();
			var n = $(current).find("[st='team_number']").val();
			var r = $(current).find("[st='paid']").val();
			var p = $(current).find("[st='supplier_employee_pk']").val();
			var m = $(current).find("[st='supplier_employee_name']").val();
			allot_json += '{"team_number":"' + n + '",' + '"paid":"' + r + '","payable_pk":"' + payable_pk + '",'
					+ '"supplier_employee_name":"' + m + '",' + '"supplier_employee_pk":"' + p;
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
			url : self.apiurl + 'sale/applyPay',
			data : data + "&allot_json=" + allot_json,
			success : function(str) {
				if (str != "success") {
					fail_msg("申请失败，请联系管理员");
				}

				self.search();
				endLoadingIndicator();
			},
			fail : function(str) {
				console.log(str);
			}
		});
	};

	self.chosenPayable = ko.observable({});
	self.positivePayables = ko.observableArray([]);
	self.negativePayables = ko.observableArray([]);
	// self.supplier_employee_name = ko.observable();
	// self.max_strike_money = ko.observable();
	// self.supplier_employee_pk = ko.observable();
	// 冲账申请
	self.strike = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			fail_msg("请选择两个及以上订单");
			return;
		} else if (self.chosenOrders().length > 1) {

			self.positivePayables.removeAll();
			self.negativePayables.removeAll();
			var supplier_employee_pks = new Array();

			var check_result = true;
			var negative_cnt = 0;
			var positive_cnt = 0;

			$(self.chosenOrders()).each(function(idx, data) {
				supplier_employee_pks.push(data.supplier_employee_pk);
				if (data.final_flg == "Y") {
					if (data.final_balance == 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					} else if (data.final_balance < 0) {
						negative_cnt++;
						self.negativePayables.push(data);
					} else {
						positive_cnt++;
						self.positivePayables.push(data);
					}
				} else {
					if (data.budget_balance == 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					} else if (data.budget_balance < 0) {
						fail_msg("请选择已经决算的负尾款订单！");
						check_result = false;
					} else {
						positive_cnt++;
						self.positivePayables.push(data);
					}
				}
			});

			if (!check_result)
				return;

			if (negative_cnt == 0) {
				fail_msg("请选择至少一单尾款为负的订单（即需要冲账的多付款订单！）");
				return;
			}

			if (positive_cnt == 0) {
				fail_msg("请选择至少一单尾款为正的订单！");
				return;
			}

			startLoadingSimpleIndicator("检测中");
			$(".rmb").formatCurrency();
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/isSameFinancialBody2',
				data : "supplier_employee_pks=" + supplier_employee_pks,
				success : function(data) {
					if (data.isSame == "NOT") {
						fail_msg("供应商不属于同一财务主体");
					} else {
						strikeLayer = $.layer({
							type : 1,
							title : ['冲账申请', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['1020px', '780px'],
							offset : ['', ''],
							scrollbar : true,
							page : {
								dom : '#strike'
							},
							end : function() {
								console.log("Done");
							}
						});
					}
					endLoadingIndicator();
				}
			});
		}
	};

	self.applyStrike = function() {
		if (!$("#form-strike").valid())
			return;

		var sumIn = 0;
		$("[st='in-money']").each(function(idx, data) {
			sumIn += $(data).val() - 0;
		});

		var sumOut = 0;
		$("[st='out-money']").each(function(idx, data) {
			sumOut += $(data).val() - 0;
		});

		if (sumIn != sumOut) {
			fail_msg("冲出总额与冲入总额不符！");
			return;
		}

		layer.close(strikeLayer);
		startLoadingSimpleIndicator("保存中");

		var out_json = '[';
		var strike_out = $("[st='strike-out']");
		for (var i = 0; i < strike_out.length; i++) {
			var current = strike_out[i];
			var payable_pk = $(current).find("[st='out-pk']").val();
			var n = $(current).find("[st='team-number']").val();
			var r = $(current).find("[st='out-money']").val();
			var supplier_employee_pk = $(current).find("[st='supplier-employee-pk']").val();

			out_json += '{"team_number":"' + n + '",' + '"out":"' + r + '","payable_pk":"' + payable_pk
					+ '","supplier_employee_pk":"' + supplier_employee_pk;
			if (i == strike_out.length - 1) {
				out_json += '"}';
			} else {
				out_json += '"},';
			}
		}
		out_json += ']';

		var in_json = '[';
		var strike_in = $("[st='strike-in']");
		for (var i = 0; i < strike_in.length; i++) {
			var current = strike_in[i];
			var payable_pk = $(current).find("[st='in-pk']").val();
			var n = $(current).find("[st='team-number']").val();
			var r = $(current).find("[st='in-money']").val();
			var supplier_employee_pk = $(current).find("[st='supplier-employee-pk']").val();

			in_json += '{"team_number":"' + n + '",' + '"in":"' + r + '","payable_pk":"' + payable_pk
					+ '","supplier_employee_pk":"' + supplier_employee_pk;
			if (i == strike_in.length - 1) {
				in_json += '"}';
			} else {
				in_json += '"},';
			}
		}
		in_json += ']';

		var comment = $("#strike-comment").val();
		data = 'json={"comment":"' + comment + '",out_json:' + out_json + ',in_json:' + in_json + '}';

		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyStrike',
			data : data,
			success : function(str) {
				endLoadingIndicator();
				if (str == "success") {
					self.search();
				} else {
					fail_msg("申请失败，请联系管理员");
				}
			}
		});
	};

	// 扣款申请
	self.deduct = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else {
			var supplier_employee_pks = new Array();
			var totalPay = 0;
			var check_result = true;
			$(self.chosenOrders()).each(function(idx, data) {
				supplier_employee_pks.push(data.supplier_employee_pk);
				if (data.final_flg != "Y") {
					fail_msg(data.team_number + "还未决算");
					check_result = false;
				} else {
					totalPay += (data.final_balance - 0);
				}
			});

			if (!check_result)
				return;

			self.totalPay(totalPay);
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
						self.supplier_name(data.supplier.supplier_short_name);
						deductLayer = $.layer({
							type : 1,
							title : ['扣款申请', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['1000px', '800px'],
							offset : ['150px', ''],
							scrollbar : true,
							page : {
								dom : '#deduct'
							},
							end : function() {
								console.log("Done");
							}
						});
					}
					endLoadingIndicator();
				}
			});
		}
	};
	// 执行扣款申请
	self.applyDeduct = function() {
		if (!$("#form-deduct").valid())
			return;
		var sumAllot = 0;
		$("[st='deduct']").each(function(idx, data) {
			sumAllot += $(data).val() - 0;
		});
		if (sumAllot != $("[st='sum_deduct']").val() - 0) {
			fail_msg("分配金额合计和总金额不匹配");
			return;
		}

		var data = $("#form-deduct").serialize();
		var allot_json = '[';
		var allot = $("[st='deduct_allot']");
		for (var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var payable_pk = $(current).find("[st='deduct-pk']").val();
			var n = $(current).find("[st='team_number']").val();
			var r = $(current).find("[st='deduct']").val();
			var p = $(current).find("[st='supplier_employee_pk']").val();
			var m = $(current).find("[st='supplier_employee_name']").val();
			allot_json += '{"team_number":"' + n + '",' + '"deduct":"' + r + '","payable_pk":"' + payable_pk + '",'
					+ '"supplier_employee_name":"' + m + '",' + '"supplier_employee_pk":"' + p;
			if (i == allot.length - 1) {
				allot_json += '"}';
			} else {
				allot_json += '"},';
			}
		}
		allot_json += ']';
		layer.close(deductLayer);
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyDeduct',
			data : data + "&allot_json=" + allot_json,
			success : function(str) {
				if (str != "success") {
					fail_msg("申请失败，请联系管理员");
				}

				self.search();
				endLoadingIndicator();
			},
			fail : function(str) {
				console.log(str);
			}
		});
	};

	self.chosenAll = function(obj) {
	};
	self.zeroBalance = function() {
		self.refresh();
		return true;
	};
	// 计算合计
	self.totalPeople = ko.observable(0);

	self.totalBudgetPayable = ko.observable(0);
	self.totalPayable = ko.observable(0);
	self.totalFinalPayable = ko.observable(0);

	self.totalPaid = ko.observable(0);

	self.totalBudgetBalance = ko.observable(0);
	self.totalBalance = ko.observable(0);
	self.totalFinalBalance = ko.observable(0);

	var pages = new Array();
	self.refresh = function() {
		var totalPeople = 0;
		var totalBudgetPayable = 0;
		var totalPayable = 0;
		var totalFinalPayable = 0;
		var totalPaid = 0;
		var totalBudgetBalance = 0;
		var totalBalance = 0;
		var totalFinalBalance = 0;
		startLoadingSimpleIndicator("加载中...");
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'sale/searchPayableByPage', param, function(data) {
			self.payables(data.payables);
			if (!pages.contains(self.currentPage())) {
				self.store(self.store().concat(self.payables()));
				pages.push(self.currentPage());
			}
			// 计算合计
			$(self.payables()).each(function(idx, data) {
				totalPeople += data.people_count;
				totalBudgetPayable += data.budget_payable;
				totalBudgetBalance += data.budget_balance;
				if (data.final_flg == "Y") {
					totalFinalPayable += data.final_payable;
					totalFinalBalance += data.final_balance;
					totalPayable += data.final_payable;
					totalBalance += data.final_balance;
				} else {
					totalPayable += data.budget_payable;
					totalBalance += data.budget_balance;
				}
				totalPaid += data.paid;

			});

			self.totalPeople(totalPeople);

			self.totalBudgetPayable(totalBudgetPayable);
			self.totalPayable(totalPayable);
			self.totalFinalPayable(totalFinalPayable);
			self.totalPaid(totalPaid);

			self.totalBudgetBalance(totalBudgetBalance);
			self.totalBalance(totalBalance);
			self.totalFinalBalance(totalFinalBalance);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$(".rmb").formatCurrency();
			self.changeType();
			endLoadingIndicator();
		});
	};

	self.changeType = function() {
		if (self.chosenTypes().length == 0 || self.chosenTypes().length == 2) {
			$("[st='all']").show();
			$("[st='budget']").hide();
			$("[st='final']").hide();
		} else if (self.chosenTypes()[0] == "预算") {
			$("[st='all']").hide();
			$("[st='budget']").show();
			$("[st='final']").hide();
		} else {
			$("[st='all']").hide();
			$("[st='budget']").hide();
			$("[st='final']").show();
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
		self.chosenOrders.removeAll();
		self.refresh();
	};

	self.editOrder = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenOrders().length == 1) {
			window.location.href = self.apiurl + "templates/sale/order-edit.jsp?key=" + self.chosenOrders()[0];
		}
	};

	// 结团
	self.closeTeam = function(pk) {
		window.location.href = self.apiurl + "templates/sale/final-order-creation.jsp?key=" + pk;
	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 50;
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
	ctx.fetchSummary();
	$('.month-picker-st').MonthPicker({
		Button : false,
		MonthFormat : 'yy-mm'
	});

	$(':file').change(function() {
		changeFile({
			input : this,
			size : 400,
			width : 400,
			required : "yes"
		});
	});
});
function checkAll(chk) {
	if ($(chk).is(":checked")) {
		for (var i = 0; i < ctx.payables().length; i++) {
			var payable = ctx.payables()[i];
			ctx.chosenOrders.push(payable);
		}
	} else {
		for (var i = 0; i < ctx.payables().length; i++) {
			var payable = ctx.payables()[i];
			ctx.chosenOrders.remove(payable);
		}
	}
}

function isLegalLimitDate(date) {
	if (date.getDay() == 6 || date.getDay() == 0) {
		fail_msg("不能选择周末的时间!");
		return false;
	}

	var minDate;

	var x = new Date();
	x.setHours(0);
	x.setMinutes(0);
	x.setSeconds(0);
	var dow = x.getDay();
	if (dow == 5 || dow == 6) {
		minDate = x.addDate(8 - dow);
		minDate.setHours(18);
	} else {
		minDate = x.addDate(1);
		minDate.setHours(18);
	}

	if (!date.after(minDate, 'yyyy-MM-dd hh:mm') && !date.equal(minDate, 'yyyy-MM-dd hh:mm')) {
		fail_msg("最早时间为下一个工作日18:00!");
		return false;
	}

	return true;
}