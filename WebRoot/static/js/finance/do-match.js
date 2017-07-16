var viewDetailLayer;
var viewCommentLayer;
var ReceivedContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenReceiveds = ko.observableArray([]);

	self.receiveds = ko.observable({
		total : 0,
		items : []
	});
	self.dateFrom = ko.observable();
	self.dateTo = ko.observable();
	self.detailId = $("#detail_key").val();
	self.detail = ko.observable({});
	// 获取收入信息
	$.getJSON(self.apiurl + 'finance/searchDetailByPk', "detailId=" + self.detailId, function(data) {
		if (data.detail) {
			self.detail(data.detail);
			var x = new Date(data.detail.time);
			self.dateTo(x.Format("yyyy-MM-dd"));
			self.dateFrom(x.Format("yyyy-MM-dd"));
			self.refresh();
		} else {
			fail_msg("不存在的收入明细！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});




	self.typeMapping = {
		'TAIL' : '抹零',
		'SUM' : '合账',
		'STRIKE' : '冲账',
		'RECEIVED' : '收入'
	};

	self.refresh = function() {
		var param = "detail.date_from=" + self.dateFrom() + "&detail.date_to=" + self.dateTo() + "&detail.status=I";
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'sale/searchReceivedByPage', param, function(data) {
			self.receiveds(data.receiveds);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$(".rmb").formatCurrency();
		});
	};

	self.sumDetails = ko.observable({
		total : 0,
		items : []
	});
	self.order = ko.observable({
		team_number : "",
		client_employee_name : "",
		product : "",
		people_count : "",
		departure_date : ""

	});
	self.comment = ko.observable();
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
	};
	self.sumDetail = ko.observable({
		card_account : "",
		sum_received : "",
		client_employee_name : "",
		allot_received : ""

	});
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
	};
	self.match = function() {
		var checks = new Array();
		for ( var i = 0; i < self.receiveds().length; i++) {
			var sou = self.receiveds()[i];
			for ( var j = 0; j < self.chosenReceiveds().length; j++) {
				var des = self.chosenReceiveds()[j];
				var x = des.split(";");
				if (x[1] == sou.related_pk) {
					checks.push(sou);
				}

			}
		}
		var sum = 0;
		for ( var i = 0; i < checks.length; i++) {
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
		var json = '{"detailId":"' + self.detailId + '","arr":[';
		for ( var i = 0; i < checks.length; i++) {
			json += '{"related_pk":"' + checks[i].related_pk + '","type":"' + checks[i].type + '"';
			if (i == checks.length - 1) {
				json += '}]}';
			} else {
				json += '},';
			}
		}
		console.log(json);
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '是否确认匹配?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					startLoadingSimpleIndicator("匹配中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'finance/matchReceived',
						data : "json="+json
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

var ctx = new ReceivedContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
