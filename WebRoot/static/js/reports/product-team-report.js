var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.report = ko.observable([]);

	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("Y");
	self.statuses = ['N', 'Y'];
	self.approvedMapping = ({
		'Y' : '已审核',
		'N' : '未审核'
	});

	self.month = ko.observable();
	var x = new Date();
	x = new Date(x.setMonth((new Date().getMonth() - 1)));
	self.month(x.Format("yyyy-MM"));

	self.refresh = function() {
		var param = $("form").serialize();
		startLoadingSimpleIndicator("加载中...");
		$.getJSON(self.apiurl + 'order/searchSumReport', param, function(data) {
			self.report(data.report);
			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	};

	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.sales(data.users);
	});

};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();

});
