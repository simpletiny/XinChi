var payableDetailLayer;
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

	self.payable_details = ko.observable({});

	self.view_detail = function(provice) {
		$.getJSON(self.apiurl + 'data/fetchPayableByArea', "provice=" + provice, function(data) {
			self.payable_details(data.payables);

			payableDetailLayer = $.layer({
				type : 1,
				title : ['查看明细', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['610px', '610px'],
				offset : ['50px', ''],
				scrollbar : true,
				page : {
					dom : '#payable-detail'
				},
				end : function() {
					console.log("Done");
				}
			});

			console.log(self.payable_details());
			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	}

};

var ctx = new SummaryContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
