var clientEmployeeLayer;
var ClientContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.visit = ko.observable({});
	self.reibursement = ko.observable({
		date: '',
		month: ''
	});
	self.items = ko.observableArray(['X', 'H', 'J', 'T', 'A', 'B', 'E', 'K', 'G', 'C', 'S', 'I', 'Q']);
	var x = new Date();
	self.reibursement().date = ko.observable();
	self.reibursement().month = ko.observable();
	self.reibursement().date(x.Format("yyyy-MM-dd"));
	self.reibursement().month(x.Format("yyyy-MM"));

	self.changeItem = function(data, event) {
		const current_item = event.target.value;
		//如果选择的是销售费用，必须选择客户
		if (current_item === 'X') {
			$("#div-client").show();
			$("#txt-client-employee-name").prop("disabled", false);
			$("#txt-client-employee-pk").prop("disabled", false);
		} else {
			$("#div-client").hide();
			$("#txt-client-employee-name").prop("disabled", true);
			$("#txt-client-employee-pk").prop("disabled", true);
		}
	}

	self.save = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		const data = $("form").serialize();
		$.ajax({
			type: "POST",
			url: self.apiurl + 'accounting/saveReimbursement',
			data: data
		}).success(function(str) {
			if (str == "success") {
				window.history.go(-1);
			} else {
				fail_msg(str);
				endLoadingIndicator();
			}
		});

	};

	self.clientEmployees = ko.observable({});
	self.refreshClient = function() {
		startLoadingSimpleIndicator("加载中……");
		var param = "employee.name=" + $("#client_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(data) {
			self.clientEmployees(data.employees);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			endLoadingIndicator();
		});
	};

	self.searchClientEmployee = function() {
		self.refreshClient();
	};

	self.choseClientEmployee = function() {
		$("#txt-client-employee-name").blur();
		clientEmployeeLayer = $.layer({
			type: 1,
			title: ['选择客户操作', ''],
			maxmin: false,
			closeBtn: [1, true],
			shadeClose: false,
			area: ['600px', '650px'],
			offset: ['50px', ''],
			scrollbar: true,
			page: {
				dom: '#client-pick'
			},
			end: function() {
				console.log("Done");
			}
		});
	};

	self.pickClientEmployee = function(data) {
		$("#txt-client-employee-name").val(data.name);
		$("#txt-client-employee-pk").val(data.pk);
		$("#txt-financial-body-name").text(data.financial_body_name);
		layer.close(clientEmployeeLayer);
	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 10;
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
		self.searchClientEmployee();
	};
};

var ctx = new ClientContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});

