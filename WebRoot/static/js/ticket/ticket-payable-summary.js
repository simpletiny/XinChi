var PayableContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.users(data.users);
	});

	self.service_fees = ko.observableArray([]);
	self.deposit_deducts = ko.observableArray([]);
	self.none_business_payments = ko.observableArray([]);
	self.refresh = function() {

		startLoadingSimpleIndicator("加载中……");
		var param = $("#form-search").serialize();
		$.getJSON(self.apiurl + 'payable/searchAirPayableSummary', param, function(data) {

			self.service_fees(data.service_fees);
			self.deposit_deducts(data.deposit_deducts);
			self.none_business_payments(data.none_business_payments);

			$("#main-table-1").tableSum({
				title_index : 4
			})
			$("#main-table-2").tableSum({
				title_index : 2
			})

			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	}
};

var ctx = new PayableContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
