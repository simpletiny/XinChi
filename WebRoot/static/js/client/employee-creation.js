var financialLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.client = ko.observable({});
	self.genders = [ '男', '女' ];
	self.employeeArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆' ];
	// self.clientType = [ '注册', '挂靠', '独立旅游人', '夫妻店', '其他' ];
	self.sales = ko.observableArray([]);
	self.employee = ko.observable({});
	self.clients = ko.observable({
		total : 0,
		items : []
	});

	self.choosenSales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});
	self.choseFinancial = function() {
		financialLayer = $.layer({
			type : 1,
			title : [ '选择财务主体', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#financial_pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.refresh = function() {
		$.getJSON(self.apiurl + 'client/searchCompany', {}, function(data) {
			self.clients(data.clients);
		});
	};
	self.searchFinancial = function() {
		self.refresh();

	};

	self.pickFinancial = function(name, pk) {
		$("#financial_body_name").val(name);
		$("#financial_body_pk").val(pk);
		layer.close(financialLayer);
	};

	self.createEmployee = function() {

		if (!$("form").valid()) {
			return;
		}

		$.ajax(
				{
					type : "POST",
					url : self.apiurl + 'client/createEmployee',
					data : $("form").serialize() + "&employee.sales="
							+ self.choosenSales()
				}).success(
				function(str) {
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/client/client-employee.jsp";
					}
				});
	};
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});