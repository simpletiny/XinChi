var ReimbursementContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.items = ko.observableArray(['X', 'H', 'J', 'T', 'A', 'B', 'E', 'K', 'G', 'C', 'Q']);

	self.statusMapping = {
		'I' : '待审批',
		'Y' : '待支付',
		'N' : '已驳回',
		'P' : '已入账'
	};
	// 申请人信息
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});

	self.totalMoney = ko.observable();
	self.reimbursements = ko.observableArray([]);
	self.refresh = function() {
		var total = 0;
		var param = $("form").serialize();

		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		startLoadingIndicator("加载中...");
		$.getJSON(self.apiurl + 'accounting/searchReimbursementByPage', param, function(data) {
			self.reimbursements(data.reimbursements);

			// 计算合计
			$(self.reimbursements()).each(function(idx, data) {
				if (data.status == "P") {
					total += data.money;
				}
			});

			self.totalMoney(total);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$(".rmb").formatCurrency();

			endLoadingIndicator();
		});
	};

	self.chosenReimbursements = ko.observableArray([]);
	// 删除已驳回数据
	self.delete_reimbursement = function() {
		if (self.chosenReimbursements().length == 0) {
			fail_msg("请选择要删除的数据！");
		} else {
			var data = "";
			for (var i = 0; i < self.chosenReimbursements().length; i++) {
				var rei = self.chosenReimbursements()[i];
				if (rei.status != 'N') {
					fail_msg("只能删除已驳回的数据！");
					return;
				}
				data += "reimbursement_pks=" + rei.pk + "&";
			}
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要删除这些费用数据吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("删除中！");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'accounting/deleteReibursement',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenReimbursements.removeAll();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}

	}

	self.reimbursement = function() {
		window.location.href = self.apiurl + "templates/accounting/reimbursement-creation.jsp";
	};

	self.viewRejectReason = function(back_pk) {
		var data = "back_pk=" + back_pk;

		$.ajax({
			type : "POST",
			url : self.apiurl + 'accounting/searchRejectReason',
			data : data
		}).success(function(str) {
			success_msg(str);
		});
	}
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 50;
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

var ctx = new ReimbursementContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
