var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.client = ko.observable({});
	self.genders = [ '男', '女' ];
	self.clientArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆' ];
	self.clientType = [ '注册', '挂靠', '独立旅游人', '夫妻店', '其他' ];

	self.createEmployee = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/createCompany',
			data : $("form").serialize()
		}).success(function(str) {
			if(str=="success"){
				window.location.href=self.apiurl+"templates/client/company.jsp";
			}
		});
	};
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
