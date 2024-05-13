var createLayer;
var receiveLayer;
var uploadLayer;
var uploadConfirmLayer;
var deductLayer;
var returnDetailLayer;

var DepositContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.deposits = ko.observable({
		total : 0,
		items : []
	});


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
		var param = $("#form-search").serialize();
		param+="&request_from=PRODUCT";
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
					inner.return_way_cn = new_return.RTrim("\\+");
				}
			});

			self.deposits(data.deposits);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$("#table-main").tableSum({
				accept : [5, 6, 7],
				title_index : 4
			})

			$(".rmb").formatCurrency();
		});
	};

	

	self.deposit_receiveds = ko.observableArray([]);
	self.deposit_deducts = ko.observableArray([]);
	self.deposit_strikes = ko.observableArray([]);
	self.detail_return_way = ko.observable("");
	self.sum_receiveds = ko.observable(0);
	self.sum_deducts = ko.observable(0);
	self.sum_strikes = ko.observable(0);
	self.viewDetail = function(data) {
		startLoadingSimpleIndicator("查询中……");
		self.detail_return_way(data.return_way);
		$.ajax({
			type : "POST",
			url : self.apiurl + 'supplier/searchDepositReturnDetails',
			data : "deposit_pk=" + data.pk,
			success : function(data) {
				if (data.deposit_strikes) {
					let sum = 0;
					$(data.deposit_deduct_relations).each(function(idx, inner) {
						sum += +inner.money;
					});
					self.sum_strikes(sum);
					self.deposit_strikes(data.deposit_strikes);
				}

				if (data.deposit_receiveds) {
					let sum = 0;
					$(data.deposit_receiveds).each(function(idx, inner) {
						sum += +inner.received;
					});
					self.sum_receiveds(sum);
					self.deposit_receiveds(data.deposit_receiveds);
				}
				if (data.deposit_deducts) {
					let sum = 0;
					$(data.deposit_deducts).each(function(idx, inner) {
						sum += +inner.money;
					});
					self.sum_deducts(sum);
					self.deposit_deducts(data.deposit_deducts);
				}
				endLoadingIndicator();
				returnDetailLayer = $.layer({
					type : 1,
					title : ['退还详情', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['1000px', '650px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#div-return-detail'
					},
					end : function() {
					}
				});
			}
		});

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

};

var ctx = new DepositContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
