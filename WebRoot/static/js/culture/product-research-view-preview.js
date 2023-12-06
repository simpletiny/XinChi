var viewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.viewPk = $("#view_key").val();
	self.view = ko.observable({
		sales : []
	});
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'culture/searchOneProductResearchView', {
		view_pk : self.viewPk
	}, function(data) {
		let user_number = $("#user-number").val();
		let user_roles = $("#user-roles").val();
		if (user_roles.indexOf("ADMIN") >= 0 || user_roles.indexOf("MANAGER") >= 0) {
			$(".hasright").show();
		} else {
			if (data.view.create_user == user_number) {
				$(".hasright").show();
			}
		}
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
		window.location.href = self.apiurl + "templates/culture/product-research-view-edit.jsp?key=" + self.viewPk;
	};
};

var ctx = new viewContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
