var viewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.viewPk = $("#view_key").val();
	self.view = ko.observable({
		sales : []
	});
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'culture/searchOneXinChiPromise', {
		view_pk : self.viewPk
	}, function(data) {
		if (data.view) {
			self.view(data.view);
		} else {
			fail_msg("文章不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.editView = function() {
		window.location.href = self.apiurl + "templates/culture/xinchi-promise-edit.jsp?key=" + self.viewPk;
	};
};

var ctx = new viewContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
