var CardContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.card = ko.observable({});
	self.cardType = [ '借记卡', '信用卡', '公帐', '微信', '支付宝' ];

	self.checkAccount = function() {
		var account = $("#account").val();
		if ($.trim(account) == "")
			return;
		$.ajax({
			url : self.apiurl + "finance/checkAccount",
			type : "post",
			data : "account=" + account,
			success : function(data) {
				if (data == "exist") {
					layer.msg("账户已经存在！", 2, 8);
					$("#submit").attr("disabled", true);
				} else {
					$("#submit").attr("disabled", false);
				}
			},
			error : function(data) {
				console.log(eval(data));
			}
		});
	};

	self.createCompany = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'finance/createCard',
			data : $("form").serialize()
		}).success(
				function(str) {
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/finance/card.jsp";
					}
				});
	};
};

var ctx = new CardContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
