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
		'TAIL' : '抹零',
		'SUM' : '合账',
		'STRIKE' : '冲账',
		'RECEIVED' : '收入'
	};
	self.receiveds = ko.observableArray([]);
	self.detail = ko.observable({});
	self.dateTo = ko.observable();
	self.dateFrom = ko.observable();
	self.checkReceived = function() {
		startLoadingSimpleIndicator("加载中...");
		var detailId = self.chosenDetails();
		// 获取收入信息
		$.getJSON(self.apiurl + 'finance/searchDetailByPk', "detailId=" + detailId, function(data) {
			if (data.detail) {
				self.detail(data.detail);
				self.refreshRight(data);
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

	self.refreshRight = function(data) {
		var x = new Date(data.detail.time);
		self.dateTo(x.Format("yyyy-MM-dd"));
		self.dateFrom(x.Format("yyyy-MM-dd"));
		self.account(data.detail.account);
		self.money(data.detail.money);
		// todo
		self.searchReceiveApply();
	}

	self.searchReceiveApply = function() {

		if (self.chosenDetails().length == 0) {
			endLoadingIndicator();
			return;
		}

		var param = "detail.statuses=I";

		if ($("#chk-data").is(":checked")) {
			param += "&detail.date_from=" + self.dateFrom() + "&detail.date_to=" + self.dateTo();
		}

		if ($("#chk-account").is(":checked")) {
			param += "&detail.card_account=" + self.account();
		}

		if ($("#chk-money").is(":checked")) {
			param += "&detail.money=" + self.money();
		}

		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;

		$.getJSON(self.apiurl + 'sale/searchReceivedByPage', param, function(data) {
			self.receiveds(data.receiveds);

			self.totalCount1(Math.ceil(data.page.total / self.perPage));
			self.setPageNums1(self.currentPage1());

			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	};

	self.showAll = function() {
		startLoadingSimpleIndicator("加载中...");
		var param = "detail.statuses=I";
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;
		$.getJSON(self.apiurl + 'sale/searchReceivedByPage', param, function(data) {
			self.receiveds(data.receiveds);

			self.totalCount1(Math.ceil(data.page.total / self.perPage));
			self.setPageNums1(self.currentPage1());

			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	};

	self.chosenReceiveds = ko.observableArray([]);
	// 匹配主营业务收入
	self.match = function() {
		var checks = new Array();
		for (var i = 0; i < self.receiveds().length; i++) {
			var sou = self.receiveds()[i];
			for (var j = 0; j < self.chosenReceiveds().length; j++) {
				var des = self.chosenReceiveds()[j];
				var x = des.split(";");
				if (x[1] == sou.related_pk) {
					checks.push(sou);
				}

			}
		}
		var sum = 0;
		for (var i = 0; i < checks.length; i++) {
			if (self.detail().account != checks[i].card_account) {
				fail_msg("账户不同！不能匹配");
				return;
			}

			if (checks[i].type == "SUM") {
				sum += checks[i].allot_received;
			} else {
				sum += checks[i].received;
			}
		}
		if (self.detail().money != sum) {
			fail_msg("匹配金额不同！不能匹配");
			return;
		}
		var json = '{"detailId":"' + self.chosenDetails() + '","arr":[';
		for (var i = 0; i < checks.length; i++) {
			json += '{"related_pk":"' + checks[i].related_pk + '","type":"' + checks[i].type + '"';
			if (i == checks.length - 1) {
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
							self.searchReceiveApply();
							self.search();
							self.chosenDetails(null);
						}
					});
					layer.close(index);
				}
			}
		});
	};
	self.viewComment = function(detail) {
		if (detail.type == "SUM") {
			msg(detail.comment);
		} else {
			var param = "team_number=" + detail.team_number;
			startLoadingSimpleIndicator("加载中");
			$.getJSON(self.apiurl + 'sale/searchOrderByTeamNumber', param, function(data) {
				self.order(data.order);
				self.comment(detail.comment);
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
		self.searchReceiveApply();
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
