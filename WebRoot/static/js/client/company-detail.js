var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.companyPk = $("#client_key").val();
	self.company = ko.observable({
		sales : []
	});

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'client/searchOneCompany', {
		client_pk : self.companyPk
	}, function(data) {
		if (data.client) {
			self.company(data.client);
		} else {
			fail_msg("公司不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
	self.recovery = function() {
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认恢复该财务主体吗?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("恢复中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'client/recoveryCompany',
						data : "company_pks=" + self.companyPk
					}).success(function(str) {
						if (str == "success") {
							location.reload();
							endLoadingIndicator();
						} else {
							fail_msg("恢复失败，请联系管理员！");
						}
					});
				}
			}
		});

	};
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
