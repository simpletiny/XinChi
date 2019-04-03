var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.view = ko.observable({});

	self.labels = ko.observableArray([]);
	self.searchLabels = function() {
		$.getJSON(self.apiurl + 'culture/searchAllLabels', {}, function(data) {
			self.labels(data.labels);

		});
	}

	self.publish = function() {
		editor.sync();
		if (!$("form").valid()) {
			return;
		}

		$
				.ajax({
					type : "POST",
					url : self.apiurl + 'culture/createProductResearchView',
					data : $("form").serialize()
				})
				.success(
						function(str) {
							if (str == "OK") {
								window.location.href = self.apiurl
										+ "templates/culture/product-research-view.jsp";
							}
						});
	};
};

var ctx = new ViewContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.searchLabels();
});
