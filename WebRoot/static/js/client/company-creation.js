var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.client = ko.observable({});
	self.genders = [ '男', '女' ];
	self.clientArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化', '呼伦贝尔',
			'伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭' ];
	self.clientType = [ '独立注册', '分公司', '营业部', '包桌', '经纪人', '其他' ];
	
	self.sales = ko.observableArray([]);
	self.choosenSales = ko.observableArray([]);
	self.publicFlg = ko.observable("N");
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});
	
	self.publicClient = function() {
		if ($("#check-public").is(":checked")) {
			$("[st='sales']").attr("checked", false);
			$("[st='sales']").attr("disabled", true);
			self.publicFlg("Y");
			self.choosenSales = ko.observableArray([]);
		} else {
			$("[st='sales']").attr("disabled", false);
			self.publicFlg("N");
		}
		return true;
	};
	
	self.createCompany = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/createCompany',
			data : $("form").serialize() + "&client.sales="
			+ self.choosenSales()+"&client.public_flg="+self.publicFlg()
		}).success(
				function(str) {
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/client/company.jsp";
					}
				});
	};
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
