var createLayer;
var receiveLayer;
var uploadLayer;
var uploadConfirmLayer;
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
		'K' : '扣款'
	};

	self.refresh = function() {
		self.chosenDeposits.removeAll();
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage + "&deposit.deposit_type=A";

		$.getJSON(self.apiurl + 'supplier/searchDepositByPage', param, function(data) {
			$(data.deposits).each(function(idx, inner) {

				if (inner.return_way != null) {
					var return_way = inner.return_way.split(",");
					var new_return = "";
					for (var i = 0; i < return_way.length; i++) {
						if (return_way != "") {
							new_return += self.wayMapping[return_way[i]] + "+";
						}
					}
					inner.return_way = new_return.RTrim("\\+");

				}
			});

			self.deposits(data.deposits);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$(".rmb").formatCurrency();
		});
	};

	self.batDeposits = ko.observableArray([]);
	// 批量上传
	self.upload = function() {
		$(".file-path").val("");
		$("#file-upload").val("");
		uploadLayer = $.layer({
			type : 1,
			title : ['选择文件', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['550px', '250px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#div-upload'
			},
			end : function() {
				contentClear('div-upload');
			}
		});
	}
	// 对上传的文件进行操作
	doUpload = function() {
		if ($("#file-upload").val() == "") {
			fail_msg("请选择要上传的文件！");
			return;
		}
		var data = "deposit_type=TICKET&file_name=" + $("#office-file").val();
		layer.close(uploadLayer);
		startLoadingSimpleIndicator("处理中...");

		$.ajax({
			type : "POST",
			url : self.apiurl + 'supplier/batUploadDeposit',
			data : data,
			success : function(data) {
				self.batDeposits(data.deposits);
				endLoadingIndicator();

				uploadConfirmLayer = $.layer({
					type : 1,
					title : ['上传确认', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['1200px', '750px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#div-upload-confirm'
					},
					end : function() {
						contentClear('div-create');
					}
				});
			}
		});
	}

	// 取消上传
	cancelUpload = function() {
		layer.close(uploadLayer);
	}

	confirmUpload = function(data, event) {
		var link = event.target;
		if (data.isConfirmed && data.isConfirmed != "0") {
			$(link).parent().parent().removeClass("confirmed");
			$(link).parent().parent().css("color", "black");
			$(link).html("确认")
			data.isConfirmed = "0";
		} else {
			$(link).parent().parent().addClass("confirmed");
			$(link).parent().parent().css("color", "green");
			$(link).html("取消")
			data.isConfirmed = "1";
		}
	}

	doSaveBat = function() {
		var confirmed = new Array();
		$(self.batDeposits()).each(function(idx, data) {
			if (data.isConfirmed == "1") {
				confirmed.push(data);
			}
		});

		if (confirmed.length == 0) {
			fail_msg("没有确认的上传，请直接点取消按钮！");
			return;
		}

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : "确认要新增这些航司押金吗？",
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("保存中...");
					var data = "json=" + JSON.stringify(confirmed, replacer);
					$.ajax({
						type : "POST",
						url : self.apiurl + 'supplier/batSaveDeposit',
						data : data,
						success : function(str) {

							endLoadingIndicator();

							if (str == "success") {
								layer.close(uploadConfirmLayer);
								self.refresh();
								success_msg("保存成功！");
							} else {
								fail_msg(str);
							}
						}
					});
				}
			}
		});

	}

	cancelSaveBat = function() {
		layer.close(uploadConfirmLayer);
		self.batDeposits.removeAll();
	}
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

	self.account = ko.observable();
	self.sum_money = ko.observable();
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
			var account = self.chosenDeposits()[0].account;
			self.account(account);
			let sum = 0;
			$(self.chosenDeposits()).each(function(index, data) {
				sum += +data.balance;
			});

			self.sum_money(sum);

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
		let json_arr = new Array();
		for (var i = 0; i < divs.length; i++) {
			var current = divs[i];
			var deposit_pk = $(current).find('input[st="deposit-pk"]').val();
			var money = $(current).find('input[st="money"]').val();
			var balance = $(current).find('input[st="deposit-balance"]').val();

			if ((money - 0) > balance) {
				fail_msg("分配金额不能大于剩余押金！");
				return;
			}

			let comment = $(current).find('textarea[st="comment"]').val().trim();

			let obj = {};
			obj.deposit_pk = deposit_pk;
			obj.money = money;
			obj.comment = comment;
			json_arr.push(obj);

			sum_money += (money - 0)
		}
		const json = encodeURIComponent(JSON.stringify(json_arr));

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
					contentClear("div-receive")
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

	self.linkDeposits = ko.observableArray();
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

			// 检测关联押金
			$.ajax({
				type : "POST",
				url : self.apiurl + 'supplier/searchDepositByVoucherNumber',
				data : "voucher_number=" + data.voucher_number,
				success : function(result) {
					self.linkDeposits(result.deposits);
					var checkResult = true;
					var com = "";
					for (var i = 0; i < self.linkDeposits().length; i++) {
						if (self.linkDeposits()[i].received - 0 != 0) {
							checkResult = false;
							com = self.linkDeposits()[i].comment;
							break;
						}
					}

					if (!checkResult) {
						fail_msg("系统检测到与此相关的“" + com + "”这笔押金存在‘已退’，不允许删除！")
						return;
					}

					var msg = "";
					if (self.linkDeposits().length > 1) {
						msg = "系统检测到与此笔押金相关的一共" + self.linkDeposits().length + "笔押金，删除会将所有相关押金一并删除！确定删除吗？";
					} else {
						msg = "确定删除这笔押金吗？";
					}

					$.layer({
						area : ['auto', 'auto'],
						dialog : {
							msg : msg,
							btns : 2,
							type : 4,
							btn : ['确认', '取消'],
							yes : function(index) {
								startLoadingSimpleIndicator("删除中...");
								layer.close(index);
								$.ajax({
									type : "POST",
									url : self.apiurl + 'supplier/deleteSupplierDeposit',
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
		var param = "supplier.type=A&supplier.supplier_short_name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;
		$.getJSON(self.apiurl + 'supplier/searchSupplierByPage', param, function(data) {
			self.suppliers(data.suppliers);

			self.totalCount1(Math.ceil(data.page.total / self.perPage1));
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
		var endPage = curPage + 4 <= self.totalCount1() ? curPage + 4 : self.totalCount1();
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
	$('.file-img').change(function() {
		changeFile({
			input : this,
			size : 400,
			width : 400,
			required : "yes"
		});
	});
	$('.file-office').change(function() {
		uploadOffice({
			input : this,
			type : 'xlsx',
			required : "yes"
		});
	});
});
var contentClear = function(id) {
	$("#" + id + " input,textarea").val('');
	$("#" + id + " img").remove();
}

var replacer = function(key, value) {
	if (typeof value === "number") {
		return value + "";
	}

	// if (typeof value === "string") {
	// if (value.indexOf(":") > -1) {
	// return value.replaceAll(":", "\\:");
	// }
	// }

	return value;
}