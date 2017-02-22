var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.view = ko.observable({});

	self.publish = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'culture/createWorldView',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "OK") {
				window.location.href = self.apiurl + "templates/culture/world-view.jsp";
			}
		});
	};
};

var ctx = new ViewContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
