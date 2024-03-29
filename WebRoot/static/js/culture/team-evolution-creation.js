var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.view = ko.observable({});

	self.publish = function() {
		editor.sync();
		if (!$("form").valid()) {
			return;
		}

		$.ajax({
			type : "POST",
			url : self.apiurl + 'culture/createTeamEvolution',
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
