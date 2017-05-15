var tailLayer;
var sumLayer;
var strikeLayer;
var receiveLayer;

var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);
	self.teamStatus = [ '未出团', '已出团', '已回团' ];
	self.types = [ '预算', '决算' ];
	self.chosenTypes = ko.observableArray([]);
	self.recsum = ko.observable({});
	self.chosenSales = ko.observable();
	self.sortTypes = [ '倒序', '正序' ];
	// 获取摘要信息
	self.fetchSummary = function() {
		$.getJSON(self.apiurl + 'sale/searchReceivableSummary', {
			sales_name : self.chosenSales()
		}, function(data) {
			self.recsum(data.summary);
			$(".rmb").formatCurrency();
		});
	};

	self.receivables = ko.observable({
		total : 0,
		items : []
	});

	self.store = ko.observableArray([]);

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

	// 执行方法
	// 抹零申请
	self.tailMoney = ko.observable();
	self.team_number = ko.observable();
	self.client_employee_name = ko.observable();

	self.ridTail = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("抹零申请只能选中一个订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			var re = null;
			$(self.receivables()).each(function(idx, data) {
				if (data.pk == self.chosenOrders()[0]) {
					re = data;
					return false;
				}
			});
			if (re.final_flg == "N") {
				fail_msg("订单未决算，不能抹零申请");
				return;
			}
			if (re.final_balance <= 0) {
				fail_msg("尾款已结清");
				return;
			}
			if (re.final_balance >= 100) {
				fail_msg("尾款多余100元，不能抹零申请");
				return;
			}
			self.tailMoney(re.final_balance);
			self.team_number(re.team_number);
			self.client_employee_name(re.client_employee_name);

			$(".rmb").formatCurrency();
			tailLayer = $.layer({
				type : 1,
				title : [ '抹零申请', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '1120px', '300px' ],
				offset : [ '150px', '' ],
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
				if (str != "OK") {
					fail_msg("申请失败，请联系管理员");
				}
				layer.close(tailLayer);
				self.search();
				endLoadingIndicator();
			}
		});
	};
	self.chosenReceivables = ko.observableArray([]);

	// 合账申请
	self.sumOrder = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			fail_msg("合账申请请选择多个订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			self.chosenReceivables.removeAll();
			var client_employee_pks = new Array();
			$(self.receivables()).each(function(idx, data1) {
				$(self.chosenOrders()).each(function(idx, data2) {
					if (data1.pk == data2) {
						self.chosenReceivables.push(data1);
						return false;
					}
				});
			});
			var check_result = true;
			$(self.chosenReceivables()).each(function(idx, data) {
				client_employee_pks.push(data.client_employee_pk);
				if (data.final_flg == "Y") {
					if (data.final_balance <= 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					}
				} else {
					if (data.budget_balance <= 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					}
				}
			});
			if (!check_result)
				return;
			$(".rmb").formatCurrency();
			startLoadingSimpleIndicator("检测中");
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/isSameFinancialBody',
				data : "client_employee_pks=" + client_employee_pks,
				success : function(str) {
					if (str == "NOT") {
						fail_msg("客户不属于同一财务主体");
					} else {
						sumLayer = $.layer({
							type : 1,
							title : [ '合账申请', '' ],
							maxmin : false,
							closeBtn : [ 1, true ],
							shadeClose : false,
							area : [ '820px', '700px' ],
							offset : [ '150px', '' ],
							scrollbar : true,
							page : {
								dom : '#sum_submit'
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
	// 执行合账申请
	self.applySum = function() {
		if (!$("#form-sum").valid())
			return;
		var sumAllot = 0;
		$("[st='received']").each(function(idx, data) {
			sumAllot += $(data).val() - 0;
		});
		if (sumAllot != $(".amountRangeEnd").val() - 0) {
			fail_msg("分配金额合计和我组金额不匹配");
			return;
		}

		var data = $("#form-sum").serialize();

		var allot_json = '[';
		var allot = $("[st='allot']");
		for ( var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var n = $(current).find("[st='team_number']").val();
			var r = $(current).find("[st='received']").val();
			allot_json += '{"team_number":"' + n + '",' + '"received":"' + r;
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
				if (str != "OK") {
					fail_msg("申请失败，请联系管理员");
				}
				layer.close(sumLayer);
				self.search();
				endLoadingIndicator();
			}
		});

	};

	// 冲账申请
	self.strike = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length >= 1) {
			self.chosenReceivables.removeAll();
			var client_employee_pks = new Array();
			$(self.receivables()).each(function(idx, data1) {
				$(self.chosenOrders()).each(function(idx, data2) {
					if (data1.pk == data2) {
						self.chosenReceivables.push(data1);
						return false;
					}
				});
			});
			var check_result = true;
			$(self.chosenReceivables()).each(function(idx, data) {
				client_employee_pks.push(data.client_employee_pk);
				if (data.final_flg == "Y") {
					if (data.final_balance <= 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					}
				} else {
					if (data.budget_balance <= 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					}
				}
			});

			if (!check_result)
				return;
			$(".rmb").formatCurrency();
			startLoadingSimpleIndicator("检测中");
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/isSameFinancialBody',
				data : "client_employee_pks=" + client_employee_pks,
				success : function(str) {
					if (str == "NOT") {
						fail_msg("客户不属于同一财务主体");
					} else {
						strikeLayer = $.layer({
							type : 1,
							title : [ '冲账申请', '' ],
							maxmin : false,
							closeBtn : [ 1, true ],
							shadeClose : false,
							area : [ '820px', '780px' ],
							offset : [ '150px', '' ],
							scrollbar : true,
							page : {
								dom : '#strike_submit'
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
		var sumAllot = 0;
		$("[st='strike-received']").each(function(idx, data) {
			sumAllot += $(data).val() - 0;
		});
		if (sumAllot != $("#strike-money").val() - 0) {
			fail_msg("分配金额合计和冲账金额不匹配");
			return;
		}

		var data = $("#form-strike").serialize();
		var allot_json = '[';
		var allot = $("[st='strike-allot']");
		for ( var i = 0; i < allot.length; i++) {
			var current = allot[i];
			var n = $(current).find("[st='strike-team_number']").val();
			var r = $(current).find("[st='strike-received']").val();
			allot_json += '{"team_number":"' + n + '",' + '"received":"' + r;
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
			url : self.apiurl + 'sale/applyReceiveStrike',
			data : data + "&allot_json=" + allot_json,
			success : function(str) {
				if (str != "OK") {
					fail_msg("申请失败，请联系管理员");
				}
				layer.close(strikeLayer);
				self.search();
				endLoadingIndicator();
			}
		});
	};
	// 收入
	self.receive = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length == 1) {
			var re = null;
			$(self.receivables()).each(function(idx, data) {
				if (data.pk == self.chosenOrders()[0]) {
					re = data;
					return false;
				}
			});
			if (re.final_flg == "Y") {
				if (re.final_balance <= 0) {
					fail_msg("尾款已结清");
					return;
				}
			} else {
				if (re.budget_balance <= 0) {
					fail_msg("尾款已结清");
					return;
				}
			}

			self.team_number(re.team_number);
			self.client_employee_name(re.client_employee_name);

			receiveLayer = $.layer({
				type : 1,
				title : [ '收入', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '820px', '400px' ],
				offset : [ '150px', '' ],
				scrollbar : true,
				page : {
					dom : '#receive_submit'
				},
				end : function() {
					console.log("Done");
				}
			});
		} else {
			self.chosenReceivables.removeAll();
			var client_employee_pks = new Array();
			$(self.store()).each(function(idx, data1) {
				$(self.chosenOrders()).each(function(idx, data2) {
					if (data1.pk == data2) {
						self.chosenReceivables.push(data1);
						return false;
					}
				});
			});
			var check_result = true;
			$(self.chosenReceivables()).each(function(idx, data) {
				client_employee_pks.push(data.client_employee_pk);
				if (data.final_flg == "Y") {
					if (data.final_balance <= 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					}
				} else {
					if (data.budget_balance <= 0) {
						fail_msg(data.team_number + "尾款已结清");
						check_result = false;
					}
				}
			});
			if (!check_result)
				return;
			$(".rmb").formatCurrency();
			startLoadingSimpleIndicator("检测中");
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/isSameFinancialBody',
				data : "client_employee_pks=" + client_employee_pks,
				success : function(str) {
					if (str == "NOT") {
						fail_msg("客户不属于同一财务主体");
					} else {
						receiveLayer = $.layer({
							type : 1,
							title : [ '收入', '' ],
							maxmin : false,
							closeBtn : [ 1, true ],
							shadeClose : false,
							area : [ '820px', '800px' ],
							offset : [ '150px', '' ],
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
					endLoadingIndicator();
				}
			});
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
					if (str != "OK") {
						fail_msg("申请失败，请联系管理员");
					}
					
					self.search();
					endLoadingIndicator();
				}
			});
		} else {
			if (!$("#form-receive-sum").valid())
				return;
			var sumAllot = 0;
			$("[st='receive_received']").each(function(idx, data) {
				sumAllot += $(data).val() - 0;
			});
			console.log(sumAllot);
			console.log($(".amountRangeStart1").val() - 0);
			if (sumAllot != $(".amountRangeStart1").val() - 0) {
				fail_msg("分配金额合计和总金额不匹配");
				return;
			}

			var data = $("#form-receive-sum").serialize();
			data += "&detail.allot_received=" + $(".amountRangeStart1").val();
			var allot_json = '[';
			var allot = $("[st='receive_allot']");
			for ( var i = 0; i < allot.length; i++) {
				var current = allot[i];
				var n = $(current).find("[st='team_number']").val();
				console.log(n);
				var r = $(current).find("[st='receive_received']").val();
				allot_json += '{"team_number":"' + n + '",' + '"received":"' + r;
				if (i == allot.length - 1) {
					allot_json += '"}';
				} else {
					allot_json += '"},';
				}
			}
			allot_json += ']';
			startLoadingSimpleIndicator("保存中");
			console.log(data);
			$.ajax({
				type : "POST",
				url : self.apiurl + 'sale/applySum',
				data : data + "&allot_json=" + allot_json,
				success : function(str) {
					if (str != "OK") {
						fail_msg("申请失败，请联系管理员");
					}
					layer.close(receiveLayer);
					self.search();
					endLoadingIndicator();
				}
			});
		}

	};
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

		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'sale/searchReceivableByPage', param, function(data) {
			self.receivables(data.receivables);
			self.store(self.store().concat(self.receivables()));
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
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	self.sales_name = ko.observableArray([]);
	self.sales_number = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
		$(self.sales()).each(function(idx, data) {
			self.sales_name.push(data.user_name);
			self.sales_number.push(data.user_number);
		});

	});
	self.search = function() {
		self.chosenOrders.removeAll();
		self.store.removeAll();
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
		for ( var i = startPage; i <= endPage; i++) {
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

});
