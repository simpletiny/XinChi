var tailLayer;
var sumLayer;
var strikeLayer;
var receiveLayer;

var payLayer;
var sumPayLayer;
var flyLayer;
var tail98Layer;

var OrderContext = function() {
	var today = new Date();
	var tomorrow = today.addDate(1);

	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);
	self.teamStatus = ['未出团', '已出团', '已回团'];
	self.types = ['预算', '决算'];
	self.chosenTypes = ko.observableArray([]);
	self.recsum = ko.observable({});
	self.chosenSales = ko.observable();
	self.sortTypes = ['正序', '倒序'];
	// 获取摘要信息
	self.fetchSummary = function() {
		$.getJSON(self.apiurl + 'sale/searchReceivableSummary', {
			user_number : self.chosenSales()
		}, function(data) {
			self.recsum(data.summary);
			$(".rmb").formatCurrency();
		});
	};

	self.receivables = ko.observable({
		total : 0,
		items : []
	});

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

	// 抹零申请
	self.tailMoney = ko.observable();
	self.team_number = ko.observable();
	self.client_employee_name = ko.observable();
	self.financial_body_name = ko.observable();
	self.client_employee_pk = ko.observable();

	self.ridTail = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("抹零申请只能选中一个订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			var current = self.chosenOrders()[0];
			if (current.final_flg == "N") {
				fail_msg("订单未决算，不能抹零申请！");
				return;
			}
			if (current.final_balance <= 0) {
				fail_msg("尾款必须为正！");
				return;
			}
			if (current.final_balance >= 100) {
				fail_msg("尾款多余100元，不能抹零申请！");
				return;
			}

			self.tailMoney(current.final_balance);
			self.team_number(current.team_number);
			self.client_employee_name(current.client_employee_name);
			self.client_employee_pk(current.client_employee_pk);

			$(".rmb").formatCurrency();
			tailLayer = $.layer({
				type : 1,
				title : ['抹零申请', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['1120px', '300px'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#tail_submit'
				},
				end : function() {
					console.log("Done");
				}
			});
		}
	};

	self.applyRidTail = function() {
		if (!$("#form-tail").valid())
			return;
		startLoadingSimpleIndicator("保存中");
		var data = $("#form-tail").serialize();
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyRidTail',
			data : data,
			success : function(str) {
				if (str != "success") {
					fail_msg("申请失败，请联系管理员");
				}
				layer.close(tailLayer);
				self.search();
				endLoadingIndicator();
			}
		});
	};

	// 代收
	self.collect = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("代收申请只能选中一个订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			var current = self.chosenOrders()[0];
			var check_result = true;
			if (current.final_flg == "Y") {
				if (current.final_balance <= 0) {
					fail_msg(current.team_number + "尾款必须为正");
					check_result = false;
				} else {
					self.tailMoney(current.final_balance);
				}
			} else {
				if (current.budget_balance == 0) {
					fail_msg(current.team_number + "尾款必须为正");
					check_result = false;
				} else {
					self.tailMoney(current.budget_balance);
				}
			}

			if (!check_result)
				return;

			self.team_number(current.team_number);
			self.client_employee_name(current.client_employee_name);
			self.financial_body_name(current.financial_body_name);
			self.client_employee_pk(current.client_employee_pk);
			$(".rmb").formatCurrency();
			tailLayer = $.layer({
				type : 1,
				title : ['代收申请', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['1120px', '600px'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#collect-submit'
				},
				end : function() {
					console.log("Done");
				}
			});
		}
	};

	self.applyCollect = function() {
		if (!$("#form-collect").valid())
			return;
		// if ($("#collect-money").val() > self.tailMoney()) {
		// fail_msg("代收金额大于尾款！");
		// return;
		// }
		startLoadingSimpleIndicator("保存中");
		var data = $("#form-collect").serialize();
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyCollect',
			data : data,
			success : function(str) {
				endLoadingIndicator();
				layer.close(tailLayer);
				if (str == "success") {
					self.search();
				} else {
					fail_msg("申请失败，请联系管理员");
				}
			}
		});
	};

	// 98清尾
	self.tail98 = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选中一个订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			var current = self.chosenOrders()[0];
			var check_result = true;
			var receivable = 0;
			if (current.final_flg == "Y") {
				if (current.final_balance <= 0) {
					fail_msg(current.team_number + "尾款必须为正");
					check_result = false;
				} else {
					receivable = current.final_receivable;
					self.tailMoney(current.final_balance);
				}

			} else {
				if (current.budget_balance == 0) {
					fail_msg(current.team_number + "尾款必须为正");
					check_result = false;
				} else {
					receivable = current.budget_receivable;
					self.tailMoney(current.budget_balance);
				}
			}

			if (Math.ceil(receivable * 0.02) < self.tailMoney()) {
				check_result = false;
				fail_msg("尾款太多！");
			}

			if (!check_result)
				return;
			// 2021-09-01之后要检查是否符合立款规则（即是否在两天内收齐了立款）
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/checkIs98',
				data : "team_number=" + current.team_number,
				success : function(str) {
					endLoadingIndicator();
					layer.close(tailLayer);
					if (str == "success") {
						self.team_number(current.team_number);
						self.client_employee_name(current.client_employee_name);
						self.financial_body_name(current.financial_body_name);
						self.client_employee_pk(current.client_employee_pk);
						$(".rmb").formatCurrency();
						tail98Layer = $.layer({
							type : 1,
							title : ['立款98', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['1120px', '450px'],
							offset : ['150px', ''],
							scrollbar : true,
							page : {
								dom : '#tail98-clear'
							},
							end : function() {
								console.log("Done");
							}
						});
					} else if (str == "bad") {
						fail_msg("不符合立款规则，不是在确认后两天内收取的款项不算做立款，不享受打折！")
						// fail_msg("不符合立款规则，不是在确认当天内收取的款项不算做立款，不享受打折！")
					} else if (str == "noconfirm") {
						fail_msg("收款会计未确认，请稍后再试！");
					} else {
						fail_msg("申请失败，请联系管理员");
					}
				}
			});

		}
	};

	self.applyTail98 = function() {
		startLoadingSimpleIndicator("保存中");
		var data = $("#form-tail98").serialize();
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyTail98',
			data : data,
			success : function(str) {
				endLoadingIndicator();
				layer.close(tail98Layer);
				if (str == "success") {
					self.search();
					self.chosenOrders.removeAll();
				} else {
					fail_msg("申请失败，请联系管理员");
				}
			}
		});
	};

	self.chosenReceivables = ko.observableArray([]);
	self.more_money = ko.observable();
	self.nextDay = ko.observable();
	// 退反申请
	self.pay = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else {
			var check_result = true;
			$(self.chosenOrders()).each(function(idx, data) {
				if (data.final_flg == "Y") {
					if (data.final_balance >= 0) {
						fail_msg(data.team_number + "尾款必须为负");
						check_result = false;
					}
				} else {
					if (data.budget_balance >= 0) {
						fail_msg(data.team_number + "尾款必须为负");
						check_result = false;
					}
				}
			});
			if (!check_result)
				return;
			self.nextDay(tomorrow.Format("yyyy-MM-dd") + " 23:59");
			$(".rmb").formatCurrency();
			caculateSumBack();
			payLayer = $.layer({
				type : 1,
				title : ['退反申请', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['920px', '600px'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#pay-sumbit'
				},
				end : function() {
					console.log("Done");
				}
			});
		}
	};
	// 执行退反申请
	self.applyPay = function() {
		if (!$("#form-pay").valid())
			return;

		var data = $("#form-pay").serialize();

		var allot_json = '[';
		var allot = $("[st='more-back-allot']");

		for (var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var n = $(current).find("[st='team-number']").val();
			var r = $(current).find("[st='more-back-money']").val();
			var client_employee_pk = $(current).find("[st='client-employee-pk']").val();
			allot_json += '{"team_number":"' + n + '",' + '"received":"' + r + '","client_employee_pk":"'
					+ client_employee_pk;
			if (i == allot.length - 1) {
				allot_json += '"}';
			} else {
				allot_json += '"},';
			}
		}
		allot_json += ']';

		data += "&allot_json=" + allot_json;

		startLoadingSimpleIndicator("申请中...");
		layer.close(payLayer);
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyIfMorePay',
			data : data,
			success : function(str) {
				endLoadingIndicator();
				if (str == "success") {
					self.chosenOrders.removeAll();
					self.search();
				} else {
					fail_msg("申请失败，请联系管理员");
				}
			}
		});
	};
	self.positiveReceivables = ko.observableArray([]);
	self.negativeReceivables = ko.observableArray([]);

	self.max_strike_money = ko.observable();
	// 冲账申请
	self.strike = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			fail_msg("请选择两个及以上订单");
			return;
		} else if (self.chosenOrders().length >= 1) {
			self.positiveReceivables.removeAll();
			self.negativeReceivables.removeAll();

			var check_result = true;
			var negative_cnt = 0;
			var positive_cnt = 0;

			$(self.chosenOrders()).each(function(idx, data) {
				if (data.final_flg == "Y") {
					if (data.final_balance == 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					} else if (data.final_balance < 0) {
						negative_cnt++;
						self.negativeReceivables.push(data);
					} else {
						positive_cnt++;
						self.positiveReceivables.push(data);
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
						self.positiveReceivables.push(data);
					}
				}
			});

			if (!check_result)
				return;

			if (negative_cnt == 0) {
				fail_msg("请选择一单尾款为负的订单（即需要冲账的多付款订单！）");
				return;
			}

			if (positive_cnt == 0) {
				fail_msg("请选择至少一单尾款为正的订单！");
				return;
			}

			$(".rmb").formatCurrency();

			strikeLayer = $.layer({
				type : 1,
				title : ['冲账申请', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['900px', '780px'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#strike-submit'
				},
				end : function() {
					console.log("Done");
				}
			});
		}

	};
	// 执行冲账申请
	self.applyStrike = function() {
		if (!$("#form-strike").valid())
			return;
		var strike_outs = $("#strike-submit").find("[st='strike-out-money']");
		var strike_ins = $("#strike-submit").find("[st='strike-in-money']");
		var strike_out = 0;
		var strike_in = 0;
		for (var i = 0; i < strike_outs.length; i++) {
			strike_out += $(strike_outs[i]).val() - 0;
		}
		for (var i = 0; i < strike_ins.length; i++) {
			strike_in += $(strike_ins[i]).val() - 0;
		}

		if (strike_out != strike_in) {
			fail_msg("冲出冲入金额不符！");
			return;
		}

		var data = $("#form-strike").serialize();
		var strike_out_json = '[';
		var strike_out = $("[st='strike-out']");
		for (var i = 0; i < strike_out.length; i++) {
			var current = strike_out[i];
			var n = $(current).find("[st='strike-team-number']").val();
			var r = $(current).find("[st='strike-out-money']").val();

			var client_employee_pk = $(current).find("[st='client-employee-pk']").val();

			strike_out_json += '{"team_number":"' + n + '",' + '"received":"' + r + '","client_employee_pk":"'
					+ client_employee_pk;

			if (i == strike_out.length - 1) {
				strike_out_json += '"}';
			} else {
				strike_out_json += '"},';
			}
		}
		strike_out_json += ']';

		var strike_in_json = '[';
		var strike_in = $("[st='strike-in']");
		for (var i = 0; i < strike_in.length; i++) {
			var current = strike_in[i];
			var n = $(current).find("[st='strike-team-number']").val();
			var r = $(current).find("[st='strike-in-money']").val();
			var client_employee_pk = $(current).find("[st='client-employee-pk']").val();
			strike_in_json += '{"team_number":"' + n + '",' + '"received":"' + r + '","client_employee_pk":"'
					+ client_employee_pk;
			if (i == strike_in.length - 1) {
				strike_in_json += '"}';
			} else {
				strike_in_json += '"},';
			}
		}
		strike_in_json += ']';

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyReceiveStrike',
			data : data + "&strike_out_json=" + strike_out_json + "&strike_in_json=" + strike_in_json,
			success : function(str) {
				endLoadingIndicator();
				if (str == "success") {
					layer.close(strikeLayer);
					self.search();
				} else {
					fail_msg("申请失败，请联系管理员");
				}
			}
		});
	};

	// 收入
	self.receive = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			var current = self.chosenOrders()[0];

			if (current.final_flg == "Y") {
				if (current.final_balance <= 0) {
					fail_msg("尾款必须为正！");
					return;
				}
				self.tailMoney(current.final_balance);
			} else {
				if (current.budget_balance == 0) {
					fail_msg("尾款必须为正！");
					return;
				}
				self.tailMoney(current.budget_balance);
			}

			self.team_number(current.team_number);
			self.client_employee_name(current.client_employee_name);
			self.financial_body_name(current.financial_body_name);
			self.client_employee_pk(current.client_employee_pk);

			receiveLayer = $.layer({
				type : 1,
				title : ['收入', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['1000px', '600px'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#receive_submit'
				},
				end : function() {
					console.log("Done");
				}
			});
		} else {
			var check_result = true;
			var money = 0;
			$(self.chosenOrders()).each(function(idx, data) {
				if (data.final_flg == "Y") {
					if (data.final_balance <= 0) {
						fail_msg(data.team_number + "尾款必须为正");
						check_result = false;
					}
					money += data.final_balance;
				} else {
					if (data.budget_balance <= 0) {
						fail_msg(data.team_number + "尾款必须为正");
						check_result = false;
					}
					money += data.budget_balance;
				}
			});
			if (!check_result)
				return;
			self.tailMoney(money);
			$(".rmb").formatCurrency();

			receiveLayer = $.layer({
				type : 1,
				title : ['收入', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['1200px', '600px'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#receive_sum_submit'
				},
				end : function() {
					console.log("Done");
				}
			});
			$("receive_sum_submit").attr("overflow", "yes");
		}
	};

	self.applyReceive = function() {

		if (self.chosenOrders().length == 1) {
			if (!$("#form-receive").valid()) {
				return;
			}
			var data = $("#form-receive").serialize();
			layer.close(receiveLayer);
			startLoadingSimpleIndicator("保存中");
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/applyReceive',
				data : data,
				success : function(str) {
					endLoadingIndicator();
					if (str != "success") {
						fail_msg("申请失败，请联系管理员");
					} else {
						self.chosenOrders.removeAll();
						self.search();
					}

				}
			});
		} else {
			if (!$("#form-receive-sum").valid())
				return;
			var sumAllot = 0;
			$("[st='receive_received']").each(function(idx, data) {
				sumAllot += $(data).val() - 0;
			});
			if (sumAllot != $(".amountRangeStart1").val() - 0) {
				fail_msg("分配金额合计和总金额不匹配");
				return;
			}

			var data = $("#form-receive-sum").serialize();
			data += "&detail.allot_received=" + $(".amountRangeStart1").val();
			var allot_json = '[';
			var allot = $("[st='receive_allot']");
			for (var i = 0; i < allot.length; i++) {
				var current = allot[i];
				var n = $(current).find("[st='team_number']").val();
				var r = $(current).find("[st='receive_received']").val();
				var client_employee_pk = $(current).find("[st='client-employee-pk']").val();
				allot_json += '{"team_number":"' + n + '",' + '"received":"' + r + '","client_employee_pk":"'
						+ client_employee_pk;
				if (i == allot.length - 1) {
					allot_json += '"}';
				} else {
					allot_json += '"},';
				}
			}
			allot_json += ']';

			startLoadingSimpleIndicator("保存中");
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/applySum',
				data : data + "&allot_json=" + allot_json,
				success : function(str) {
					endLoadingIndicator();
					layer.close(receiveLayer);
					if (str != "success") {
						fail_msg("申请失败，请联系管理员");
					} else {
						self.search();
						self.chosenOrders.removeAll();
					}
				}
			});
		}
	};
	self.order = ko.observable();
	self.flyMoney = ko.observable();
	// fly申请
	self.fly = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个");
			return;
		} else {
			var current = self.chosenOrders()[0];
			var team_number = current.team_number;
			var data = "team_number=" + team_number;
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/canApplyFly',
				data : data
			}).success(function(str) {
				if (str == "noexist") {
					fail_msg("不存在fly信息，不能申请！	");
					return;
				} else if (str == "already") {
					fail_msg("已经申请fly！");
					return;
				} else {
					self.nextDay(tomorrow.Format("yyyy-MM-dd") + " 23:59");
					self.team_number(current.team_number);
					self.client_employee_name(current.client_employee_name);
					self.financial_body_name(current.financial_body_name);
					self.flyMoney(str);
					$(".rmb").formatCurrency();
					flyLayer = $.layer({
						type : 1,
						title : ['fly申请', ''],
						maxmin : false,
						closeBtn : [1, true],
						shadeClose : false,
						area : ['920px', '400px'],
						offset : ['', ''],
						scrollbar : true,
						page : {
							dom : '#fly-submit'
						},
						end : function() {
							console.log("Done");
						}
					});
				}
			});
		}
	};

	self.applyFly = function() {

		if (!$("#form-fly").valid()) {
			return;
		}
		var data = $("#form-fly").serialize();
		layer.close(flyLayer);
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/applyFly',
			data : data,
			success : function(str) {
				endLoadingIndicator();
				if (str == "nomatch") {
					fail_msg("申请金额于订单填报不符！");
				} else if (str == "success") {
					self.chosenOrders.removeAll();
					self.search();
				} else {
					fail_msg("申请失败，请联系管理员");
				}
			}
		});
	}

	// 计算合计
	self.totalPeople = ko.observable(0);

	self.totalBudgetReceivable = ko.observable(0);
	self.totalReceivable = ko.observable(0);
	self.totalFinalReceivable = ko.observable(0);

	self.totalReceived = ko.observable(0);

	self.totalBudgetBalance = ko.observable(0);
	self.totalBalance = ko.observable(0);
	self.totalFinalBalance = ko.observable(0);

	self.refresh = function() {
		var totalPeople = 0;
		var totalBudgetReceivable = 0;
		var totalReceivable = 0;
		var totalFinalReceivable = 0;
		var totalReceived = 0;
		var totalBudgetBalance = 0;
		var totalBalance = 0;
		var totalFinalBalance = 0;
		startLoadingSimpleIndicator("加载中...");
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'sale/searchReceivableByPage', param, function(data) {
			self.receivables(data.receivables);

			// 计算合计
			$(self.receivables()).each(function(idx, data) {
				totalPeople += data.people_count;
				totalBudgetReceivable += data.budget_receivable;
				totalBudgetBalance += data.budget_balance;
				if (data.final_flg == "Y") {
					totalFinalReceivable += data.final_receivable;
					totalFinalBalance += data.final_balance;
					totalReceivable += data.final_receivable;
					totalBalance += data.final_balance;
				} else {
					totalReceivable += data.budget_receivable;
					totalBalance += data.budget_balance;
				}
				totalReceived += data.received;

			});

			self.totalPeople(totalPeople);

			self.totalBudgetReceivable(totalBudgetReceivable);
			self.totalReceivable(totalReceivable);
			self.totalFinalReceivable(totalFinalReceivable);
			self.totalReceived(totalReceived);

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

	self.zeroBalance = function() {
		self.refresh();
		return true;
	};

	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});
	self.search = function() {
		self.refresh();
	};

	self.resetPage = function() {

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

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.search();
	ctx.fetchSummary();
	$("#sum-more-back").disabled();
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
		for (var i = 0; i < ctx.receivables().length; i++) {
			var receivable = ctx.receivables()[i];
			ctx.chosenOrders.push(receivable);
		}
	} else {
		for (var i = 0; i < ctx.receivables().length; i++) {
			var receivable = ctx.receivables()[i];
			ctx.chosenOrders.remove(receivable);
		}
	}
}
var caculateSumBack = function() {
	var allot = $("[st='more-back-allot']");
	var sum = 0;
	for (var i = 0; i < allot.length; i++) {
		var current = allot[i];
		var money = $(current).find("[st='more-back-money']").val() - 0;
		sum += money;
	}
	$("#sum-more-back").val(sum);
};
// 计算冲出冲入合计
var caculateStrike = function() {
	var strike_outs = $("#strike-submit").find("[st='strike-out-money']");
	var strike_ins = $("#strike-submit").find("[st='strike-in-money']");
	var strike_out = 0;
	var strike_in = 0;
	for (var i = 0; i < strike_outs.length; i++) {
		strike_out += $(strike_outs[i]).val() - 0;
	}
	for (var i = 0; i < strike_ins.length; i++) {
		strike_in += $(strike_ins[i]).val() - 0;
	}
	$("#p-strike-out").text(strike_out);
	$("#p-strike-in").text(strike_in);
	$("#p-strike-out").formatCurrency();
	$("#p-strike-in").formatCurrency();
};