var SummaryContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.summary = ko.observable({});

	startLoadingSimpleIndicator("加载中...");
	$.getJSON(self.apiurl + 'data/fetchFinanceSummary', {}, function(data) {
		self.summary(data.dfsd);

		$(".rmb").formatCurrency();
		endLoadingIndicator();
	});

};

var ctx = new SummaryContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
