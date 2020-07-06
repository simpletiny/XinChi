var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.viewPk = $("#view_key").val();
	self.view = ko.observable({
		sales : []
	});
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'culture/searchOneTeamEvolution', {
		view_pk : self.viewPk
	}, function(data) {
		if (data.view) {
			self.view(data.view);
			editor.html(self.view().content);
		} else {
			fail_msg("文章不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.publish = function() {
		editor.sync();
		if (!$("form").valid()) {
			return;
		}

		$.ajax({
			type : "POST",
			url : self.apiurl + 'culture/updateTeamEvolution',
			data : $("form").serialize()
		}).success(
				function(str) {
					if (str == "OK") {
						window.location.href = self.apiurl
								+ "templates/culture/team-evolution.jsp";
					}
				});
	};
};

var ctx = new ViewContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
