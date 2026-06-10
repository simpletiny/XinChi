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
	self.service_deduct_summary = ko.observableArray([]);
	self.none_business_payments = ko.observableArray([]);
	self.deposits = ko.observableArray([]);
	self.deposit = ko.observable({});
	self.service_fee_summary = ko.observableArray([]);
	self.refresh = function() {

		startLoadingSimpleIndicator("加载中……");
		var param = $("#form-search").serialize();
		$.getJSON(self.apiurl + 'payable/searchAirPayableSummary', param, function(data) {

			self.service_fees(data.service_fees);
			self.deposit_deducts(data.deposit_deducts);
			self.none_business_payments(data.none_business_payments);
			self.deposits(data.deposits);
			self.deposit(data.deposit);
			self.service_fee_summary(data.service_fee_summary);
			self.service_deduct_summary(data.service_deduct_summary);

			$("#main-table-1").tableSum({
				title_index : 4
			})
			$("#main-table-2").tableSum({
				title_index : 2
			})
			$("#main-table-3").tableSum({
				title_index : 1,
				accept : [2, 3, 4]
			})

			$("#main-table-4").tableSum({
				title_index : 1,
				except : [1, 6, 7]
			})
			$("#main-table-5").tableSum({
				title_index : 1,
				accept : [2]
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
