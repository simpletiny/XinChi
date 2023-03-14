var ReportContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	self.chosenReports = ko.observableArray([]);

	// 获取用户信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', 'role=PRODUCT', function(data) {
		self.users(data.users);
	});

	self.years = ko.observableArray();

	var x = new Date();
	var current_year = x.getFullYear();
	for (var i = current_year; i > 2017; i--) {
		var o = new Object();
		o.name = i + "年";
		o.key = i;
		self.years().push(o);
	}

	self.statusMapping = {
		'N' : '待审核',
		'Y' : '待终审'
	};

	self.reports = ko.observable({
		total : 0,
		items : []
	});

	self.peopleCount = ko.observable();
	self.grossProfit = ko.observable();
	self.productCost = ko.observable();
	self.keepCost = ko.observable();
	self.refresh = function() {
		startLoadingIndicator("加载中...");
		var peopleCount = 0;
		var grossProfit = 0;
		var productCost = 0;
		var keepCost = 0;
		var param = $('form').serialize();
		$.getJSON(self.apiurl + 'product/searchProductProfit', param, function(data) {
			self.reports(data.productProfits);

			$(self.reports()).each(function(idx, data) {
				peopleCount += data.people_count - 0;
				grossProfit += data.gross_profit == null ? 0 : data.gross_profit - 0;
				productCost += data.product_cost == null ? 0 : data.product_cost - 0;
				keepCost += data.keep_cost == null ? 0 : data.keep_cost - 0;
			});

			self.peopleCount(peopleCount);
			self.grossProfit(grossProfit);
			self.productCost(productCost);
			self.keepCost(keepCost);

			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	};

};

var ctx = new ReportContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
