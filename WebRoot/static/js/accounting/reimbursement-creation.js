var clientEmployeeLayer;
var ClientContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.visit = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.reibursement = ko.observable({
		date:'',
		month:''
	});
	
	self.item = [ 'X', 'J', 'Q' ];
	self.itemMapping = ko.observableArray([ {
		"key" : "X",
		"value" : "销售费用"
	}, {
		"key" : "J",
		"value" : "交通垫付"
	}, {
		"key" : "Q",
		"value" : "其他支出"
	} ]);
	var x = new Date();
	self.reibursement().date = ko.observable();
	self.reibursement().month = ko.observable();
	self.reibursement().date(x.Format("yyyy-MM-dd"));
	self.reibursement().month(x.Format("yyyy-MM"));
	
	self.save = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'accounting/saveReimbursement',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.history.go(-1);
			}else{
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