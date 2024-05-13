var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.report = ko.observable([]);

	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("Y");
	self.statuses = ['N', 'Y'];
	self.approvedMapping = ({
		'Y': '已审核',
		'N': '未审核'
	});

	self.month = ko.observable();
	var x = new Date();
	x = new Date(x.setMonth((new Date().getMonth() - 1)));
	self.month(x.Format("yyyy-MM"));
	self.reiSummary = ko.observable(new Map());

	self.items = ko.observableArray(['X', 'H', 'J', 'T', 'A', 'B', 'E', 'K', 'G', 'C', 'S', 'I', 'SUM', 'Q']);


	self.refresh = function() {
		var param = $("form").serialize();
		$.getJSON(self.apiurl + 'order/searchSumReport', param, function(data) {
			self.report(data.report);
			$(".rmb").formatCurrency();
		});
		const date_from = $('input[name="option.date_from"]').val();
		const date_to = $('input[name="option.date_to"]').val();
		$.getJSON(self.apiurl + 'accounting/searchSumReimbursement', { "option.date_from": date_from, "option.date_to": date_to }, function(data) {
			let results = new Map(self.items().map(item => [item, 0]));
			let sum_money = 0;
			for (let [key, value] of results) {
				if (data.summarise[key]) {
					results.set(key, data.summarise[key]);

					if (key !== "Q") { sum_money += data.summarise[key]; }
				}
			}
			results.set("SUM",sum_money);
			self.reiSummary(results);
			$(".rmb").formatCurrency();
		});
	};

	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role: 'PRODUCT'
	}, function(data) {
		self.sales(data.users);
	});




};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();


});
