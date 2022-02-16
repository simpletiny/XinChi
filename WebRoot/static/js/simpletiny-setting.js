var SimpletinyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.fixReceivable = function() {

		var team_numbers = $("#txt-team-number").val().trim();
		var data = "team_numbers=" + team_numbers;

		$.ajax({
			type : "POST",
			url : self.apiurl + 'simpletiny/fixReportReceivable',
			data : data
		}).success(function(str) {
			if (str == "success") {
				success_msg("修正成功！");
			} else {
				fail_msg(str);
			}
		});
	}
};

var ctx = new SimpletinyContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
});
