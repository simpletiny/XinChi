var matchDetailLayer;
var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.allStatus = [ 'N', 'Y' ];
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
		var param = $("form").serialize() + "&detail.type=收入&detail.inner_flg=N";
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'finance/searchDetailByPage', param, function(data) {
			self.details(data.details);
			$(".rmb").formatCurrency();

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	// 匹配主营业务收入
	self.match = function() {
		if (self.chosenDetails().length == 0) {
			fail_msg("请选择收入");
			return;
		} else if (self.chosenDetails().length > 1) {
			fail_msg("只能选择一笔收入");
			return;
		} else if (self.chosenDetails().length == 1) {
			var key = self.chosenDetails()[0];
			self.chosenDetails.removeAll();
			window.location=self.apiurl + "templates/finance/do-match.jsp?key=" + key;
		}
	};
	// 匹配其他收入
	self.matchOther = function() {
		if (self.chosenDetails().length == 0) {
			fail_msg("请选择收入");
			return;
		} else if (self.chosenDetails().length > 1) {
			fail_msg("只能选择一笔收入");
			return;
		} else if (self.chosenDetails().length == 1) {

			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '确认标记为其他收入吗?',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
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
							self.chosenDetails.removeAll();
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
						title : [ '合账详情', '' ],
						maxmin : false,
						closeBtn : [ 1, true ],
						shadeClose : false,
						area : [ '800px', 'auto' ],
						offset : [ '150px', '' ],
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
						title : [ '摘要详情', '' ],
						maxmin : false,
						closeBtn : [ 1, true ],
						shadeClose : false,
						area : [ '700px', 'auto' ],
						offset : [ '150px', '' ],
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
		} else if (self.chosenDetails().length > 1) {
			fail_msg("只能选择一笔收入");
			return;
		} else if (self.chosenDetails().length == 1) {
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '确认要取消匹配吗?',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						startLoadingSimpleIndicator("匹配中");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'finance/cancelMatchReceived',
							data : "detailId=" + self.chosenDetails()
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								window.location.href = self.apiurl + "templates/finance/received-match.jsp";
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
