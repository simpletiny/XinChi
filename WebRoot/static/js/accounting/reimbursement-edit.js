var clientEmployeeLayer;
var ClientContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.reimbursement_pk = $("#key").val();
	self.reimbursement = ko.observable({});
	self.items = ko.observableArray(['X', 'H', 'J', 'T', 'A', 'B', 'E', 'K', 'G', 'C', 'Q']);

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'accounting/searchReimbursementByPk', {
		reimbursement_pk : self.reimbursement_pk
	}, function(data) {
		if (data.reimbursement) {
			self.reimbursement(data.reimbursement);
		} else {
			fail_msg("不存在的申请！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.update = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'accounting/reApplyReimbursement',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.history.go(-1);
			} else {
				fail_msg(str);
				endLoadingIndicator();
			}
		});

	};
};

var ctx = new ClientContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});