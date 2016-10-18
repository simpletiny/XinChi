var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCards = ko.observableArray([]);
	self.createDetail = function() {
		window.location.href = self.apiurl
				+ "templates/finance/detail-creation.jsp";
	};

	self.details = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		$.getJSON(self.apiurl + 'finance/searchDetail', {}, function(data) {
			self.details(data.details);
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

};

var ctx = new DetailContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
