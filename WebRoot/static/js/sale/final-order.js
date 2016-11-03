var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.orders = ko.observable({
		total : 0,
		items : []
	});
	
	self.refresh = function() {
		$.getJSON(self.apiurl + 'sale/searchFinalOrders', {}, function(data) {
			self.orders(data.orders);
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};
};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
