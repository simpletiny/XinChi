var matchDetailLayer;
var viewDetailLayer;
var viewCommentLayer;
var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.allStatus = ['N', 'Y'];
	self.statusMapping = {
		'N' : '未匹配',
		'Y' : '已匹配',
		'O' : '其他收入'
	};
	self.chosenStatus = ko.observable('N');
	self.chosenDetails = ko.observableArray([]);
	self.accounts = ko.observableArray([]);
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
		} else {
			fail_msg("不存在账户");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.details = ko.observable({
		total : 0,
		items : []
	});
	self.today = ko.observable();
	var x = new Date();
	self.today(x.Format("yyyy-MM-dd"));

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var param = $("form").serialize() + "&detail.type=收入&detail.inner_flg=N";
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'finance/searchDetailByPage', param, function(data) {
			self.details(data.details);
			$(".rmb").formatCurrency();
			endLoadingIndicator();
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

		});
	};

	// 匹配其他收入
	self.matchOther = function() {
		if (self.chosenDetails() == null || self.chosenDetails() == "") {
			fail_msg("请选择收入");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认标记为其他收入吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						startLoadingSimpleIndicator("保存中...");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'finance/matchOtherReceived',
							data : "detailId=" + self.chosenDetails()
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.search();
							} else {
								fail_msg(str);
							}
							self.chosenDetails(null);
						});
						layer.close(index);
					}
				}
			});
		}
	};
	self.received = ko.observable({
		user_name : "",
		create_time : ""
	});
	self.sumDetails = ko.observableArray([]);
	self.sumDetail = ko.observable({
		card_account : "",
		sum_received : "",
		client_employee_name : "",
		allot_received : ""
	});

	self.order = ko.observable({
		team_number : "",
		client_employee_name : "",
		product : "",
		people_count : "",
		departure_date : ""
	});

	self.orders = ko.observableArray([]);
	self.comment = ko.observable();

	self.showDetails = function(data, event) {
		var detail_pk = data.pk;
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'finance/searchReceivedDetailByPaymentDetailPk', {
			detailId : detail_pk
		}, function(data) {
			self.received(data.received_detail);

			if (self.received().type == "SUM") {
				var param = "related_pks=" + self.received().related_pk;

				$.getJSON(self.apiurl + 'sale/searchByRelatedPks', param, function(data) {
					self.sumDetails(data.receiveds);
					self.sumDetail(self.sumDetails()[0]);
					$(".rmb").formatCurrency();
					endLoadingIndicator();
					matchDetailLayer = $.layer({
						type : 1,
						title : ['合账详情', ''],
						maxmin : false,
						closeBtn : [1, true],
						shadeClose : false,
						area : ['800px', 'auto'],
						offset : ['150px', ''],
						scrollbar : true,
						page : {
							dom : '#sum_detail'
						},
						end : function() {
							console.log("Done");
						}
					});
				});
			} else {
				var param = "team_number=" + self.received().team_number;
				$.getJSON(self.apiurl + 'sale/searchOrderByTeamNumber', param, function(data) {
					self.order(data.order);
					self.comment(self.received().comment);
					endLoadingIndicator();
					matchDetailLayer = $.layer({
						type : 1,
						title : ['摘要详情', ''],
						maxmin : false,
						closeBtn : [1, true],
						shadeClose : false,
						area : ['700px', 'auto'],
						offset : ['150px', ''],
						scrollbar : true,
						page : {
							dom : '#comment'
						},
						end : function() {
							console.log("Done");
						}
					});
				});
			}

		});
	};

	// 取消匹配
	self.cancelMatch = function() {
		if (self.chosenDetails().length == 0) {
			fail_msg("请选择收入");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要取消匹配吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						startLoadingSimpleIndicator("匹配中");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'finance/cancelMatchReceived',
							data : "detailId=" + self.chosenDetails()
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.search();
							}
						});
						layer.close(index);
					}
				}
			});
		}
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
	// right side info
	self.typeMapping = {
		'CSUM' : '合账',
		'CRECEIVED' : '收入',
		'ABACK' : '票务退返',
		'DBACK' : '地接退返'
	};
	self.receiveds = ko.observableArray([]);
	self.detail = ko.observable({});
	self.date = ko.observable();
	self.checkReceived = function() {
		startLoadingSimpleIndicator("加载中...");
		var detailId = self.chosenDetails();
		// 获取收入信息
		$.getJSON(self.apiurl + 'finance/searchDetailByPk', "detailId=" + detailId, function(data) {
			if (data.detail) {
				self.detail(data.detail);
				self.searchReceiveApply(data);
			} else {
				fail_msg("不存在的收入明细！");
			}
		}).fail(function(reason) {
			fail_msg(reason.responseText);
		});
		return true;
	}
	self.account = ko.observable();
	self.money = ko.observable();
	self.current_param = ko.observable('');

	self.refreshRight = function() {
		startLoadingSimpleIndicator("加载中...");
		var param = self.current_param();
		if (param == null || param == '') {
			param = '';
		} else {
			param += '&';
		}

		param += "page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;

		$.getJSON(self.apiurl + 'accounting/searchReceivedByPage', param, function(data) {
			self.receiveds(data.receiveds);

			self.totalCount1(Math.ceil(data.page.total / self.perPage));
			self.setPageNums1(self.currentPage1());

			self.chosenReceiveds.removeAll();
			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	}

	self.searchReceiveApply = function(data) {
		var x = new Date(data.detail.time);
		self.date(x.Format("yyyy-MM-dd"));
		self.account(data.detail.account);
		self.money(data.detail.money);

		var param = "";

		if ($("#chk-data").is(":checked")) {
			param += "&detail.date=" + self.date();
		}

		if ($("#chk-account").is(":checked")) {
			param += "&detail.card_account=" + self.account();
		}

		if ($("#chk-money").is(":checked")) {
			param += "&detail.received=" + self.money();
		}
		self.current_param(param);
		self.refreshRight();
	};

	self.showAll = function() {
		self.chosenDetails(null);
		self.current_param('');
		self.refreshRight();
	};

	self.chosenReceiveds = ko.observableArray([]);
	// 匹配主营业务收入
	self.match = function() {
		// var checks = new Array();
		// for (var i = 0; i < self.receiveds().length; i++) {
		// var sou = self.receiveds()[i];
		// for (var j = 0; j < self.chosenReceiveds().length; j++) {
		// var des = self.chosenReceiveds()[j];
		// var x = des.split(";");
		// if (x[1] == sou.related_pk) {
		// checks.push(sou);
		// }
		// }
		// }

		if (self.detail().match_flg == "Y") {
			fail_msg("请选择未匹配的明细！");
			return;
		}
		if (self.chosenReceiveds().length < 1) {
			fail_msg("请选择要匹配的收入申请！");
			return;
		}
		var sum = 0;
		for (var i = 0; i < self.chosenReceiveds().length; i++) {
			if (self.detail().account != self.chosenReceiveds()[i].card_account) {
				fail_msg("账户不同！不能匹配");
				return;
			}
			sum += self.chosenReceiveds()[i].received;
		}
		if (self.detail().money != sum) {
			fail_msg("匹配金额不同！不能匹配");
			return;
		}
		var json = '{"detailId":"' + self.chosenDetails() + '","arr":[';
		for (var i = 0; i < self.chosenReceiveds().length; i++) {
			json += '{"related_pk":"' + self.chosenReceiveds()[i].related_pk + '","from_where":"'
					+ self.chosenReceiveds()[i].from_where + '"';
			if (i == self.chosenReceiveds().length - 1) {
				json += '}]}';
			} else {
				json += '},';
			}
		}
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '是否确认匹配?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					startLoadingSimpleIndicator("匹配中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'finance/matchReceived',
						data : "json=" + json
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refreshRight();
							self.search();
							self.chosenDetails(null);
						}
					});
					layer.close(index);
				}
			}
		});
	};

	// 驳回收入申请
	self.reject = function() {
		if (self.chosenReceiveds().length < 1) {
			fail_msg("请选择要驳回的收入申请！");
			return;
		}
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认将此收入申请驳回吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					startLoadingSimpleIndicator("驳回中");

					var related_pks = '';
					for (var i = 0; i < self.chosenReceiveds().length; i++) {
						related_pks += self.chosenReceiveds()[i].related_pk
						if (i != self.chosenReceiveds().length - 1) {
							related_pks += ',';
						}
					}

					$.ajax({
						type : "POST",
						url : self.apiurl + 'sale/rejectReceived',
						data : "related_pks=" + related_pks
					}).success(function(str) {
						if (str == "success") {
							self.refreshRight();
							self.search();
							self.chosenDetails(null);
						}
						endLoadingIndicator();
					});
					layer.close(index);
				}
			}
		});
	}
	self.viewComment = function(detail) {

		if (detail.type == "SUM") {
			msg(detail.comment);
		} else {
			var param = "related_pk=" + detail.related_pk;
			startLoadingSimpleIndicator("加载中");
			$.getJSON(self.apiurl + 'order/searchOrderByRelatedPk', param, function(data) {
				console.log(data.orders);
				self.orders(data.orders);
				endLoadingIndicator();
				viewCommentLayer = $.layer({
					type : 1,
					title : ['摘要详情', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['700px', 'auto'],
					offset : ['150px', ''],
					scrollbar : true,
					page : {
						dom : '#comment1'
					},
					end : function() {
						console.log("Done");
					}
				});
			});
		}
	};
	self.viewDetail = function(related_pk) {
		var param = "related_pks=" + related_pk;
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'sale/searchByRelatedPks', param, function(data) {

			self.sumDetails(data.receiveds);
			self.sumDetail(self.sumDetails()[0]);
			$(".rmb").formatCurrency();
			endLoadingIndicator();

			viewDetailLayer = $.layer({
				type : 1,
				title : ['合账详情', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', 'auto'],
				offset : ['150px', ''],
				scrollbar : true,
				page : {
					dom : '#sum_detail1'
				},
				end : function() {
					console.log("Done");
				}
			});
		});
	};
	// 查看收入凭证
	self.checkVoucherPic = function(fileName, received_time) {
		$("#img-pic").attr("src", "");
		budgetConfirmCheckLayer = $.layer({
			type : 1,
			title : ['查看确认件', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});
		console.log(received_time)
		var subFolder = received_time.substring(0, 4) + "/" + received_time.substring(5, 7);

		$("#img-pic").attr(
				"src",
				self.apiurl + 'file/getFileStream?fileFileName=' + fileName
						+ "&fileType=CLIENT_RECEIVED_VOUCHER&subFolder=" + subFolder);
	};
	// 新标签页显示大图片
	$("#img-pic").on(
			'click',
			function() {
				window.open(self.apiurl + "templates/common/check-picture-big.jsp?src="
						+ encodeURIComponent($(this).attr("src")));
			});
	// start pagination
	self.currentPage1 = ko.observable(1);
	self.perPage1 = 20;
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
		self.refreshRight();
	};
	// end pagination
};

var ctx = new DetailContext();

$(document).ready(function() {
	// $('.month-picker-st').AlertSelf();
	ko.applyBindings(ctx);
	ctx.refresh();
	$('.month-picker-st').MonthPicker({
		Button : false,
		MonthFormat : 'yy-mm'
	});
	$(':file').change(function() {
		changeFile(this);

	});
});

function changeFile(thisx) {
	var file = thisx.files[0];
	name = file.name;
	size = file.size;
	type = file.type;

	if (type.indexOf("excel") < 0) {
		fail_msg("请上传Excel");
		return;
	}
	if (size > 4194304) {
		fail_msg("文件大于4MB");
		return;
	}
	startLoadingSimpleIndicator("导入中");
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/detailExcelUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.onload = function() {
		if (this.status == 200) {

			var blob = this.response;
			if (blob == "OK") {
				success_msg("导入成功");
			} else if (blob == "BEFORE") {
				fail_msg("导入数据的交易时间不能早于系统已存在明细的时间");
			}
			endLoadingIndicator();
		}
	};
	xhr.send(formData);
}
changeStatus = function(obj) {
	ctx.refresh();
	if ($(obj).val() == 'Y') {
		$("#btn-cancel").show();
	} else {
		$("#btn-cancel").hide();
	}
};
// (function($){
// $.fn.AlertSelf = function(){
// this.click(function(){alert($(this).val())});
// }
// })(jQuery)
