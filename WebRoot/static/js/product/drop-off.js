var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.colors = ['#FFFFE0', '#FFF0F5', '#FFFACD', '#FFEBCD'];
	self.drop_offs = ko.observableArray([]);
	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});
	self.first_ticket_date = ko.observable();
	self.from_city = ko.observable("");
	var x = new Date();
	x = new Date(x.setDate(x.getDate() + 1));
	self.first_ticket_date(x.Format("yyyy-MM-dd"));

	self.refresh = function() {
		self.drop_offs.removeAll();
		var param = $("form").serialize();
		startLoadingIndicator("加载中...");
		$.getJSON(self.apiurl + 'product/searchDropOff', param, function(data) {
			// self.drop_offs(data.drop_offs);
			for (var i = 0; i < data.drop_offs.length; i++) {
				var obj = new Object();
				var info = data.drop_offs[i];
				obj.product_order_number = info.product_order_number;
				obj.infos = "航班号：" + info.ticket_number + "\n" + info.first_start_city + info.from_airport + "--"
						+ info.first_end_city + info.to_airport + " " + info.from_to_time;
				obj.client_name = info.client_name;
				obj.team_number = info.team_number;

				var names = info.name_list.split(",");
				var name_list = "";
				for (var j = 0; j < names.length; j++) {
					if (j % 2 == 0) {
						name_list += names[j];
					} else {
						name_list += names[j] + "\n";
					}
				}
				obj.name_list = name_list;
				var phones = "";
				if (info.phones != null) {
					var arr_phone = info.phones.split(',');

					var arr_new = new Array();
					var j = 0;

					for (var k = 0; k < arr_phone.length; k++) {
						var phone = arr_phone[k];
						if (phone.length > 11) {
							arr_new[0] = phone;
						} else {
							arr_new[j + 1] = phone;
							j++;
						}
					}
					var h = 0;

					for (var k = 0; k < arr_new.length; k++) {
						if (arr_new[k] != null) {
							phones += arr_new[k] + ',';
							h++;
							if (h % 2 == 0) {
								phones += "\n";
							}
						}

					}
					phones = phones.RTrim(',');
				}
				obj.phones = phones;
				self.drop_offs.push(obj);
			}

			var trs = $("#div-table").find("tbody").find("tr");

			var current_no = "";
			var current_index = 0;
			for (var i = 0; i < trs.length; i++) {
				var tr = $(trs[i]);
				if (i == 0) {
					current_no = $(tr).find("td[st='order-number']").text();
				}
				var order_number = $(tr).find("td[st='order-number']").text();

				if (order_number != current_no) {
					current_no = order_number;
					current_index += 1;
				}
				tr.find("td").css("cssText", "background:" + self.colors[current_index % 4] + " !important");

			}

			// var product_order_number = "";
			// var obj = new Object();
			//
			// for (var i = 0; i < data.drop_offs.length; i++) {
			// var info = data.drop_offs[i];
			// if (info.product_order_number == product_order_number) {
			// var infos = new Object();
			// infos.team_number = info.team_number;
			// infos.name_list = info.name_list;
			// obj.name_list.push(infos);
			//
			// } else {
			// if (i != 0) {
			// self.drop_offs.push(obj);
			// obj = new Object();
			// }
			//
			// product_order_number = info.product_order_number;
			// obj.product_order_number = info.product_order_number;
			// obj.infos += "航班号：" + info.ticket_number + "\n"
			// + info.first_start_city + info.from_airport + "--"
			// + info.first_end_city + info.to_airport + " "
			// + info.from_to_time;
			// obj.client_name = info.client_name;
			//
			// var infos = new Object();
			// infos.team_number = info.team_number;
			// infos.name_list = info.name_list;
			// obj.name_list = new Array();
			//
			// obj.name_list.push(infos);
			// }
			//
			// if (i == data.drop_offs.length - 1) {
			// self.drop_offs.push(obj);
			// }
			//
			// }
			endLoadingIndicator();
		});
	};

};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});