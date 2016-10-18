var CardContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCards = ko.observableArray([]);
	self.createCard = function() {
		window.location.href = self.apiurl
				+ "templates/finance/card-creation.jsp";
	};

	self.cards = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		$.getJSON(self.apiurl + 'finance/searchCard', {}, function(data) {
			self.cards(data.cards);
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editCard = function() {
		if (self.chosenCards().length == 0) {
			fail_msg("请选择账户");
			return;
		} else if (self.chosenCards().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenCards().length == 1) {
			window.location.href = self.apiurl +"templates/client/Card-edit.jsp?key="+self.chosenCards()[0];
		}
	};

};

var ctx = new CardContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
