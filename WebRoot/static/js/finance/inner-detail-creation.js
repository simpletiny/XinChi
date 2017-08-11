var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.detail = ko.observable({});
	self.accounts = ko.observableArray([]);

	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.createDetail = function() {
		if (!$("form").valid()) {
			return;
		}
		var from_account = $("#from_account").val();
		var to_account = $("#to_account").val();
		if (from_account == to_account) {
			fail_msg("转出账户和转入账户不能相同！");
			return;
		}
		
		console.log($("form").serialize());
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '提交后无法修改，是否确认提交?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					$.ajax({
						type : "POST",
						url : self.apiurl + 'finance/createInnerDetail',
						data : $("form").serialize()
					}).success(function(str) {
						if (str == "success") {
							window.location.href = self.apiurl + "templates/finance/detail.jsp";
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

function checkExchange() {
	if ($("#check-exchange").is(":checked")) {
		$("#radio-out").attr("disabled", false);
		$("#radio-in").attr("disabled", false);
		$("#exchange-money").attr("disabled", false);
	} else {
		$("#radio-out").attr("disabled", true);
		$("#radio-in").attr("disabled", true);
		$("#exchange-money").attr("disabled", true);
		$("#exchange-money").val("");
		$("#exchange-money-error").remove();
	}
}

function setInMoney(txt) {
	$("#in-money").val($(txt).val());
}
