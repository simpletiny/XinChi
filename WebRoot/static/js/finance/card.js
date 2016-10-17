var CardContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCompanies = ko.observableArray([]);
	self.createCompany = function() {
		window.location.href = self.apiurl
				+ "templates/finance/card-creation.jsp";
	};

	self.clients = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		$.getJSON(self.apiurl + 'client/searchCompany', {}, function(data) {
			self.clients(data.clients);
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editCompany = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择公司");
			return;
		} else if (self.chosenCompanies().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenCompanies().length == 1) {
			window.location.href = self.apiurl +"templates/client/company-edit.jsp?key="+self.chosenCompanies()[0];
		}
	};

};

var ctx = new CardContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
