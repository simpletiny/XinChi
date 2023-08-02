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

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var param = $('form').serialize();
		$.getJSON(self.apiurl + 'product/searchProductProfit', param, function(data) {
			self.reports(data.productProfits);
			$(".table").tableSum({
				title : '汇总',
				title_index : 3,
				except : [1, 2, 3]
			});
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
