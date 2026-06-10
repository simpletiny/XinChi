var passengerCheckLayer;
var ticketInfoCheckLayer;
var ChangeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenChanges = ko.observableArray([]);

	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(data) {
		startLoadingIndicator("加载中...");
		const url = "ticket/searchPassengersByChangePk";

		$.getJSON(self.apiurl + url, {
			ticket_change_pk : data.pk
		}, function(data) {

			self.passengers(data.airTicketNameList);
			endLoadingIndicator();
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['乘客信息', ''],
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
		const url = "ticket/searchTicketInfoByChangePk";
		const param = "ticket_change_pk=" + data.pk;

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

	self.changes = ko.observable({
		total : 0,
		items : []
	});
	self.totalPeople = ko.observable();
	self.refresh = function() {
		startLoadingIndicator("加载中...");
		var totalPeople = 0;
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'ticket/searchTicketChangeByPage', param, function(data) {

			self.changes(data.changes);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			endLoadingIndicator();
		});
	};

	self.rollBackChange = function() {
		if (self.chosenChanges().length < 1) {
			fail_msg("请选择航变！");
			return;
		} else if (self.chosenChanges().length > 1) {
			fail_msg("取消只能选择一个！");
			return;
		} else {
			let param = "change_pk=" + self.chosenChanges()[0];
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要取消此航变吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						$.ajax({
							type : "POST",
							url : self.apiurl + 'ticket/rollBackTicketChange',
							async : false,
							data : param
						}).success(function(str) {
							if (str == "success") {
								success_msg("取消成功！")
								self.refresh();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	}

	self.search = function() {

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

};

var ctx = new ChangeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

var currentAirLeg;
var airLegLayer;
function choseAirLeg(event) {
	ctx.searchAirLeg();
	airLegLayer = $.layer({
		type : 1,
		title : ['选择票务航段', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['600px', '650px'],
		offset : ['', ''],
		scrollbar : true,
		page : {
			dom : '#air-leg-pick'
		},
		end : function() {
			console.log("Done");
		}
	});

	currentAirLeg = event.target;
	$(currentAirLeg).blur();
}
