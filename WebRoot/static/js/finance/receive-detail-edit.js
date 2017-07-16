var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.detail = ko.observable({});
	self.accounts = ko.observableArray([]);
	self.detailId = $("#detail_key").val();
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	$.getJSON(self.apiurl + 'finance/searchDetailByPk', "detailId=" + self.detailId, function(data) {
		if (data.detail) {
			self.detail(data.detail);
		} else {
			fail_msg("不存在的收入明细！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.updateDetail = function() {
		if (!$("form").valid()) {
			return;
		}

		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '是否确认提交?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					startLoadingSimpleIndicator("保存中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'finance/updateDetail',
						data : $("form").serialize() + "&detail.type=收入"
					}).success(function(str) {
						if (str == "success") {
							window.location.href = self.apiurl + "templates/finance/detail.jsp";
						} else if (str == "time") {
							fail_msg("同一账户下的明细账，时间不能相同，请调整时间。");
							endLoadingIndicator();
						}
					});
					layer.close(index);
				}
			}
		});
	};
};

var ctx = new DetailContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
