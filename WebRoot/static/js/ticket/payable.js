var payLayer;
var passengerCheckLayer;
var ticketInfoCheckLayer;
var receiveLayer;
var businessStrikeLayer;
var depositStrikeLayer;
var depositLayer;
var PayableContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenPayables = ko.observableArray([]);
	self.types = ['预算', '决算'];
	self.chosenTypes = ko.observableArray([]);

	self.cards = ko.observableArray([]);
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {

	}, function(data) {
		if (data.accounts) {
			self.cards(data.accounts);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.payables = ko.observable({
		total : 0,
		items : []
	});

	self.chosenTypes = ko.observableArray();

	self.payableTypes = ['A', 'B', 'C'];
	self.payableTypesMapping = {
		"A" : "机票款",
		"B" : "手续费",
		"C" : "航变"
	};
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
			fail_msg("请选择应付款！");
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
						fail_msg("第" + (idx + 1) + "个的" + "尾款已清");
						check_result = false;
					} else {
						totalPay += (data.final_balance - 0);
					}

				} else {
					if (data.budget_balance == 0) {
						fail_msg("第" + (idx + 1) + "个的" + "尾款已清");
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
					endLoadingIndicator();
					if (data.isSame == "NOT") {
						fail_msg("供应商不属于同一财务主体");
					} else {
						window.location.href = self.apiurl + "templates/ticket/air-ticket-paid.jsp?key=" + payable_pks;

					}
				}
			});
		}
	};

	// 返款收入
	self.totalBack = ko.observable();
	self.receive = function() {
		if (self.chosenPayables().length == 0) {
			fail_msg("请选择应付款！");
			return;
		} else {
			var supplier_employee_pks = new Array();
			var totalBack = 0;
			var check_result = true;
			$(self.chosenPayables()).each(function(idx, data) {
				supplier_employee_pks.push(data.supplier_employee_pk);

				if (data.final_flg == "Y") {
					if (data.final_balance >= 0) {
						fail_msg("第" + (idx + 1) + "个的" + "尾款为正，不存在返款");
						check_result = false;
					} else {
						totalBack += (data.final_balance - 0);
					}
				} else {
					if (data.budget_balance >= 0) {
						fail_msg("第" + (idx + 1) + "个的" + "尾款为正，不存在返款");
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

						self.totalBack(totalBack.toFixed(2) * -1);
						receiveLayer = $.layer({
							type : 1,
							title : ['收入', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['1000px', '700px'],
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
		sumAllot = sumAllot.toFixed(2);
		if (sumAllot != $("[st='sum_received']").val() - 0) {
			fail_msg("分配金额合计和总金额不匹配");
			return;
		}

		var data = $("#form-receive").serialize();
		var allot_json = '[';
		var allot = $("[st='back_allot']");
		for (var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var base_pk = $(current).find("[st='base-pk']").val();
			var r = $(current).find("[st='back_receive']").val();
			var p = $(current).find("[st='supplier_employee_pk']").val();
			allot_json += '{"base_pk":"' + base_pk + '",' + '"received":"' + r + '",' + '"supplier_employee_pk":"' + p;
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
			url : self.apiurl + 'payable/backRecive',
			data : data + "&json=" + allot_json,
			success : function(str) {
				if (str == "success") {
					self.chosenPayables.removeAll();
					self.search();
				} else if (str = "time") {
					fail_msg("此账户在同一时间存在收支！");
				} else {
					fail_msg("申请失败，请联系管理员");
				}
				endLoadingIndicator();
			},
			fail : function(str) {
				console.log(str);
			}
		});
	};

	self.positivePayables = ko.observableArray([]);
	self.negativePayables = ko.observableArray([]);

	// 业务冲抵
	self.businessStrike = function() {
		if (self.chosenPayables().length == 0) {
			fail_msg("请选择应付款！");
			return;
		} else if (self.chosenPayables().length == 1) {
			fail_msg("业务冲抵，需要选择两个及以上应付款！")
			return;
		} else {
			var positives = new Array();
			var negatives = new Array();

			for (var i = 0; i < self.chosenPayables().length; i++) {
				var current = self.chosenPayables()[i];
				if (current.budget_balance == 0) {
					fail_msg("不能选择尾款为0的应付款！");
					return;
				} else if (current.budget_balance < 0) {
					negatives.push(current);
				} else {
					positives.push(current);
				}
			}

			if (positives.length == 0) {
				fail_msg("请添加选择尾款为正的应付款（即冲入应付款）！");
				return;
			} else if (negatives.length == 0) {
				fail_msg("请添加选择尾款为负的应付款（即冲出应付款）！");
				return;
			}

			self.positivePayables(positives);
			self.negativePayables(negatives);
			businessStrikeLayer = $.layer({
				type : 1,
				title : ['业务冲账', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['1000px', '700px'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#business-strike'
				},
				end : function() {
					$("#business-strike input").val("");
				}
			});
		}
	}
	// 执行业务冲账
	self.doBusinessStrike = function() {
		if (!$("#form-business-strike").valid())
			return;
		var outs = $("#div-strike-out>div");
		var ins = $("#div-strike-in>div");
		var sum_out = 0;
		var sum_in = 0;
		var outJson = '[';
		for (var i = 0; i < outs.length; i++) {
			var current = outs[i];
			var payable_pk = $(current).find('input[st="payable-pk"]').val();
			var money = $(current).find('input[st="strike-out-money"]').val();
			var budget_balance = $(current).find('input[st="budget-balance"]').val();

			if ((money - 0) > (budget_balance - 0) * -1) {
				fail_msg("第" + (i + 1) + "个的冲出金额大于可用冲出！");
				return;
			}
			outJson += '{"payable_pk":"' + payable_pk + '","money":"' + money + '"}';

			if (i < outs.length - 1) {
				outJson += ',';
			}

			sum_out += (money - 0);
		}

		outJson += ']';
		var inJson = '[';
		for (var i = 0; i < ins.length; i++) {
			var current = ins[i];
			var payable_pk = $(current).find('input[st="payable-pk"]').val();
			var money = $(current).find('input[st="strike-in-money"]').val();
			var budget_balance = $(current).find('input[st="budget-balance"]').val();

			if ((money - 0) > (budget_balance - 0)) {
				fail_msg("第" + (i + 1) + "个的冲入金额大于最大冲入金额！");
				return;
			}

			inJson += '{"payable_pk":"' + payable_pk + '","money":"' + money + '"}';

			if (i < ins.length - 1) {
				inJson += ',';
			}

			sum_in += (money - 0);
		}

		inJson += ']';

		if (sum_out != sum_in) {
			fail_msg("冲出金额于冲入金额合计不符！");
			return;
		}

		var json = '{"out":' + outJson + ',"in":' + inJson + ',"allot_money":"' + sum_out + '"}';
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认提交此次冲账吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("操作中...");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'payable/businessStrike',
						data : "json=" + json,
						success : function(str) {
							if (str == "success") {
								self.chosenPayables.removeAll();
								layer.close(businessStrikeLayer);
								self.search();
							} else {
								fail_msg("提交失败，请联系管理员");
							}
							endLoadingIndicator();
						},
						fail : function(str) {
							console.log(str);
						}
					});
				}
			}
		});

	}
	// 取消业务冲账
	self.cancelBusinessStrike = function() {
		layer.close(businessStrikeLayer);
	}
	self.usedDeposits = ko.observableArray([]);
	// 押金冲抵
	self.depositStrike = function() {
		if (self.chosenPayables().length == 0) {
			fail_msg("请选择应付款！");
			return;
		} else {
			var supplier_employee_pks = new Array();
			var check_result = true;
			$(self.chosenPayables()).each(function(idx, data) {
				supplier_employee_pks.push(data.supplier_employee_pk);

				if (data.final_flg == "Y") {
					if (data.final_balance <= 0) {
						fail_msg("第" + (idx + 1) + "个不存在应付款！");
						check_result = false;
					}
				} else {
					if (data.budget_balance <= 0) {
						fail_msg("第" + (idx + 1) + "个不存在应付款！");
						check_result = false;
					}
				}
			});
			if (!check_result)
				return;

			startLoadingSimpleIndicator("检测中");
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/isSameFinancialBody2',
				data : "supplier_employee_pks=" + supplier_employee_pks,
				success : function(data) {
					if (data.isSame == "NOT") {
						fail_msg("供应商不属于同一财务主体");
					} else {
						depositStrikeLayer = $.layer({
							type : 1,
							title : ['押金冲抵', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['1000px', '700px'],
							offset : ['150px', ''],
							scrollbar : true,
							page : {
								dom : '#deposit-strike'
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
	}
	/**
	 * 执行押金冲账
	 */
	self.doDepositStrike = function() {

		if (!$("#form-deposit-strike").valid())
			return;

		var outs = $("#table-deposit-out>tr");
		var ins = $("#div-deposit-in>div");

		if (outs.length < 1) {
			fail_msg("请选择冲出押金！");
			return;
		}

		var sum_out = 0;
		var sum_in = 0;

		var outJson = '[';
		var inJson = '[';

		for (var i = 0; i < outs.length; i++) {
			var current = outs[i];

			var deposit_pk = $(current).find('input[st="deposit-pk"]').val();
			var money = $(current).find('input[st="deposit-out-money"]').val();
			var balance = $(current).find('input[st="deposit-balance"]').val();

			if ((money - 0) > (balance - 0)) {
				fail_msg("第" + (i + 1) + "个的冲出金额大于最大冲出金额！");
				return;
			}

			sum_out += (money - 0);

			outJson += '{"deposit_pk":"' + deposit_pk + '","money":"' + money + '"}';

			if (i < outs.length - 1) {
				outJson += ',';
			}
		}
		outJson += ']';

		for (var i = 0; i < ins.length; i++) {
			var current = ins[i];

			var payable_pk = $(current).find('input[st="payable-pk"]').val();
			var money = $(current).find('input[st="deposit-in-money"]').val();
			var balance = $(current).find('input[st="budget-balance"]').val();

			if ((money - 0) > (balance - 0)) {
				fail_msg("第" + (i + 1) + "个的冲入金额大于最大冲入金额！");
				return;
			}

			sum_in += (money - 0);

			inJson += '{"payable_pk":"' + payable_pk + '","money":"' + money + '"}';

			if (i < ins.length - 1) {
				inJson += ',';
			}
		}
		inJson += ']';

		var json = '{"out":' + outJson + ',"in":' + inJson + ',"allot_money":"' + sum_out + '"}';

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认提交此次冲账吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("操作中...");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'payable/depositStrike',
						data : "json=" + json,
						success : function(str) {
							if (str == "success") {
								self.chosenPayables.removeAll();
								layer.close(depositStrikeLayer);
								self.search();
							} else {
								fail_msg("提交失败，请联系管理员");
							}
							endLoadingIndicator();
						},
						fail : function(str) {
							console.log(str);
						}
					});
				}
			}
		});
	}

	/**
	 * 取消押金冲账
	 */
	self.cancelDepositStrike = function() {
		layer.close(depositStrikeLayer);
	}

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
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'payable/searchAirTicketPayableByPage', param, function(data) {
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
	self.checkPassengers = function(data) {
		startLoadingIndicator("加载中...");
		var url = "";
		var param = "";
		if (data.payable_type == "A") {
			url = "payable/searchPayablePassengersByPayablePk";
			param = "payable_pk=" + data.pk;
		} else {
			url = "payable/searchPayablePassengersByRelatedPk";
			param = "related_pk=" + data.related_pk;
		}

		$.getJSON(self.apiurl + url, param, function(data) {
			self.passengers(data.passengers);
			endLoadingIndicator();
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
	self.checkTicketInfo = function(data) {
		startLoadingIndicator("加载中...");
		var url = "";
		var param = "";
		if (data.payable_type == "A") {
			url = "payable/searchTicketInfoByPayablePk";
			param = "payable_pk=" + data.pk;
		} else {
			url = "payable/searchTicketInfoByRelatedPk";
			param = "related_pk=" + data.related_pk;
		}

		$.getJSON(self.apiurl + url, param, function(data) {
			self.infos(data.ptInfos);
			endLoadingIndicator();

			ticketInfoCheckLayer = $.layer({
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

	self.search = function() {
		self.refresh();
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
	self.deposits = ko.observable({
		total : 0,
		items : []
	});
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
			area : ['1220px', '650px'],
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
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1
				+ "&deposit.deposit_type=A&deposit.statuses=N";

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
		};
		self.usedDeposits(self.chosenDeposits());
		layer.close(depositLayer);
	}

	// start deposits pick pagination
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
	// end pagination
};

var ctx = new PayableContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	$(':file').change(function() {
		changeFile({
			input : this,
			size : 400,
			width : 400,
			required : "yes"
		});
	});
});
