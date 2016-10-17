var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.card = ko.observable({});
	self.cardType = [ '借记卡', '信用卡', '公帐', '微信', '支付宝' ];
	
	self.createCompany = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'finance/createCard',
			data : $("form").serialize()
		}).success(function(str) {
			if(str=="success"){
				window.location.href=self.apiurl+"templates/finance/card.jsp";
			}
		});
	};
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
