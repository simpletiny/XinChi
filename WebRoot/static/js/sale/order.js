var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenOrders = ko.observableArray([]);
	self.createOrder = function() {
		window.location.href = self.apiurl
				+ "templates/sale/order-creation.jsp";
	};

	self.orders = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		$.getJSON(self.apiurl + 'sale/searchOrder', {}, function(data) {
			self.orders(data.orders);
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editOrder = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenOrders().length == 1) {
			window.location.href = self.apiurl +"templates/client/company-edit.jsp?key="+self.chosenOrders()[0];
		}
	};

};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
