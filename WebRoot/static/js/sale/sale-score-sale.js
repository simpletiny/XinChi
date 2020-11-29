var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.scores = ko.observableArray([]);

	self.refresh = function() {
		$.getJSON(self.apiurl + 'order/search3MonthSaleScore', {}, function(
				data) {
			self.scores(data.scores);
			console.log(data.scores);
		});
	};

};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});