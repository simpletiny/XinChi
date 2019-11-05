var employeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.orders = ko.observable();
	self.test = ko.observableArray([]);

	for (var i = 0; i < 10; i++) {
		var s = new Object;
		s.index = i;
		s.name = "吗" + i;
		s.id = "13012319870322271" + i;
		self.test.push(s);
	}

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'order/searchConfirmingOrders', {}, function(data) {
		if (data.orders) {
			self.orders(data.orders);
		} else {
			fail_msg("加载失败，联系管理员！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.confirmNameList = function(team_number) {
		var btn = event.target;
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认名单无误？',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("确认中...");
					var data = "team_number=" + team_number;
					$.ajax({
						type : "POST",
						url : self.apiurl + 'order/confirmNameList',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							$(btn).parent().parent().remove();
						} else {
							fail_msg(str);
						}
					});
				}
			}
		});

	}
};

var ctx = new employeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
