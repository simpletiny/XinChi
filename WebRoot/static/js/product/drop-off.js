var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.drop_offs = ko.observableArray([]);
	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});
	self.first_ticket_date = ko.observable();
	self.from_city = ko.observable("哈尔滨");
	var x = new Date();
	self.first_ticket_date(x.Format("yyyy-MM-dd"));

	self.refresh = function() {
		self.drop_offs.removeAll();
		var param = $("form").serialize();
		$.getJSON(self.apiurl + 'product/searchDropOff', param, function(data) {
			var ticket_number = "";
			var names = "";
			var infos = "";
			var client_name = "";
			for (var i = 0; i < data.drop_offs.length; i++) {
				var info = data.drop_offs[i];
				if (i == 0) {
					ticket_number = info.ticket_number;

					names += info.name + ":" + info.id + ";\n";
					infos += "航班号：" + info.ticket_number + "\n"
							+ info.first_start_city + info.from_airport + "--"
							+ info.first_end_city + info.to_airport + "      "
							+ info.from_to_time;
					client_name = info.client_name;
				} else {
					if (ticket_number == info.ticket_number) {
						names += info.name + ":" + info.id + ";\n";
						if (i == data.drop_offs.length - 1) {
							var obj = new Object();
							obj.names = names;
							obj.infos = infos;
							obj.client_name = client_name;
							self.drop_offs.push(obj);
						}
					} else {
						if (i == data.drop_offs.length - 1) {
							names = "";
							infos = "";
							client_name = "";
							names += info.name + ":" + info.id + ";\n";
							infos += "航班号：" + info.ticket_number + "\n"
									+ info.first_start_city + info.from_airport
									+ "--" + info.first_end_city
									+ info.to_airport + "      "
									+ info.from_to_time;
							client_name = info.client_name;

							var obj = new Object();
							obj.names = names;
							obj.infos = infos;
							obj.client_name = client_name;
							self.drop_offs.push(obj);
						} else {
							var obj = new Object();
							obj.names = names;
							obj.infos = infos;
							obj.client_name = client_name;
							self.drop_offs.push(obj);

							names = "";
							infos = "";
							client_name = "";

							ticket_number = info.ticket_number;
							names += info.name + ":" + info.id + ";\n";
							infos += "航班号：" + info.ticket_number + "\n"
									+ info.first_start_city + info.from_airport
									+ "--" + info.first_end_city
									+ info.to_airport + "      "
									+ info.from_to_time;
							client_name = info.client_name;
						}

					}

				}

			}

		});
	};

};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});