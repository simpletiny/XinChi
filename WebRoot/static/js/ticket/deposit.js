var createLayer;
var receiveLayer;
var DepositContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.deposits = ko.observable({
		total : 0,
		items : []
	});
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
	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("N");

	self.status = ['N', 'Y'];
	self.statusMapping = {
		'N' : '在押',
		'Y' : '已清'
	};

	self.wayMapping = {
		'C' : '冲账',
		'T' : '退还',
		'C,T' : '冲账+退还',
		'T,C' : '冲账+退还'
	};

	self.refresh = function() {
		self.chosenDeposits.removeAll();
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage + "&deposit.deposit_type=A";

		$.getJSON(self.apiurl + 'supplier/searchDepositByPage', param,
				function(data) {
					self.deposits(data.deposits);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());

					$(".rmb").formatCurrency();
				});
	};
	/**
	 * 新建押金账
	 */
	self.create = function() {
		createLayer = $.layer({
			type : 1,
			title : ['新建押金账', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['1200px', '800px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#div-create'
			},
			end : function() {
				contentClear('div-create');
			}
		});
	}

	self.doCreate = function() {
		if (!$("#form-create").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中...");
		var data = $("#form-create").serialize();

		$.ajax({
			type : "POST",
			url : self.apiurl + 'supplier/createSupplierDeposit',
			data : data,
			success : function(str) {
				if (str == "success") {
					self.refresh();
					contentClear('div-create');
					layer.close(createLayer);
				} else {
					fail_msg(str);
				}
				endLoadingIndicator();
			}
		});
	}
	self.cancelCreate = function() {
		layer.close(createLayer);
	}

	/**
	 * 押金退回
	 */
	self.receive = function() {
		if (self.chosenDeposits().length == 0) {
			fail_msg("请选择押金账");
			return;
		} else if (self.chosenDeposits().length >= 1) {

			var supplier_pk = self.chosenDeposits()[0].supplier_pk;

			// 判断是否还有剩余
			for (var i = 0; i < self.chosenDeposits().length; i++) {
				var data = self.chosenDeposits()[i];
				if (data.balance - 0 <= 0) {
					fail_msg("所选押金账已清！");
					return;
				}

				if (supplier_pk != data.supplier_pk) {
					fail_msg("所选押金账不属于同一供应商！");
					return;
				}
			}

			receiveLayer = $.layer({
				type : 1,
				title : ['押金退还', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['1200px', '800px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#div-receive'
				},
				end : function() {
				}
			});

		}
	}

	self.doReceive = function() {
		if (!$("#form-receive").valid())
			return;

		var data = $("#form-receive").serialize();
		var divs = $("#div-allot>div");
		var all_money = $("#sum-money").val() - 0;
		var sum_money = 0;
		var json = '[';
		for (var i = 0; i < divs.length; i++) {
			var current = divs[i];
			var deposit_pk = $(current).find('input[st="deposit-pk"]').val();
			var money = $(current).find('input[st="money"]').val();
			var balance = $(current).find('input[st="deposit-balance"]').val();

			if ((money - 0) > balance) {
				fail_msg("分配金额不能大于剩余押金！");
				return;
			}

			json += '{"deposit_pk":"' + deposit_pk + '","money":"' + money
					+ '"}';
			if (i != divs.length - 1) {
				json += ',';
			}

			sum_money += (money - 0)
		}
		json += ']';

		data += "&json=" + json;

		if (all_money != sum_money) {
			fail_msg("分配金额总额与收入金额不符！");
			return;
		}
		startLoadingSimpleIndicator("保存中...");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'supplier/receiveSupplierDeposit',
			data : data,
			success : function(str) {
				if (str == "success") {
					self.refresh();
					layer.close(receiveLayer);
				} else {
					fail_msg(str);
				}

				endLoadingIndicator();
			}
		});

	}
	self.cancelReceive = function() {
		layer.close(receiveLayer);
	}
	/**
	 * 删除押金账
	 */
	self.delete_deposit = function() {
		if (self.chosenDeposits().length == 0) {
			fail_msg("请选择押金账");
			return;
		} else if (self.chosenDeposits().length > 1) {
			fail_msg("只能选择一个");
			return;
		} else if (self.chosenDeposits().length == 1) {
			var data = self.chosenDeposits()[0];
			if (data.received - 0 != 0) {
				fail_msg("已存在‘已退’，不允许删除！");
				return;
			}

			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要删除此押金账吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						startLoadingSimpleIndicator("删除中...");
						layer.close(index);
						$.ajax({
							type : "POST",
							url : self.apiurl
									+ 'supplier/deleteSupplierDeposit',
							data : "deposit_pk=" + data.pk,
							success : function(str) {
								if (str == "success") {
									self.refresh();
									layer.close(index);
								} else {
									fail_msg(str);
								}

								endLoadingIndicator();
							}
						});
					}
				}
			});
		}
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

	self.suppliers = ko.observable({
		total : 0,
		items : []
	});

	self.choseFinancial = function() {
		financialLayer = $.layer({
			type : 1,
			title : ['选择财务主体', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#financial-pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.refresh1 = function() {
		var param = "supplier.type=A&supplier.supplier_short_name="
				+ $("#supplier_name").val();
		param += "&page.start=" + self.startIndex1() + "&page.count="
				+ self.perPage1;
		$.getJSON(self.apiurl + 'supplier/searchSupplierByPage', param,
				function(data) {
					self.suppliers(data.suppliers);

					self
							.totalCount1(Math.ceil(data.page.total
									/ self.perPage1));
					self.setPageNums1(self.currentPage1());
				});
	};
	self.searchFinancial = function() {
		self.refresh1();

	};

	self.pickFinancial = function(name, pk) {
		$("#financial-body-name").val(name);
		$("#financial-body-pk").val(pk);
		layer.close(financialLayer);
	};

	// start supplier pick pagination
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
		var endPage = curPage + 4 <= self.totalCount1() ? curPage + 4 : self
				.totalCount1();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums1(pageNums);
	};

	self.refreshPage1 = function() {
		self.searchFinancial();

	};
	// end pagination
};

var ctx = new DepositContext();

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
var contentClear = function(id) {
	$("#" + id + " input,textarea").val('');
	$("#" + id + " img").remove();
}