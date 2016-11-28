var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.detail = ko.observable({});
	self.accounts = ko.observableArray([]);
	self.balance = '0';
	self.initBalance = "0";
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.changeAccount = function() {
		if (self.detail().account == "") {
			self.balance = 0;
			self.initBalance = 0;
			return;
		}

		$.getJSON(self.apiurl + 'finance/getAccountBalance', {
			account : self.detail().account
		}, function(data) {
			self.balance = data;
			self.initBalance = data;
			self.calculateBalance();
		}).fail(function(reason) {
			fail_msg(reason.responseText);
		});

	};
	// 计算余额
	self.calculateBalance = function() {
		self.balance = Number((self.initBalance - 0) - ($("#txt-money").val() - 0)).toFixed(2);
		$("#p-balance").text(self.balance);
		$(".rmb").formatCurrency();
	};

	self.createDetail = function() {
		if (!$("form").valid()) {
			return;
		}

		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '提交后无法修改，是否确认提交?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					$.ajax(
							{
								type : "POST",
								url : self.apiurl + 'finance/createDetail',
								data : $("form").serialize()
										+ "&detail.balance=" + self.balance+"&detail.type=支出"
							}).success(
							function(str) {
								if (str == "success") {
									window.location.href = self.apiurl
											+ "templates/finance/detail.jsp";
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
