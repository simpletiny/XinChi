var AgencyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.finish = function() {

		startLoadingSimpleIndicator("保存中");
		var all = $(".every-count");
		var json = '[';
		for ( var i = 0; i < all.length; i++) {
			var current = all[i];

			var ticket_source = $(current).find("input[st^='ticket-source']").val();
			var ticket_source_pk = $(current).find("input[st^='ticket-source-pk']").val();

			var ticket_cost = $(current).find("input[st^='ticket-cost']").val();
			var ticket_PNR = $(current).find("input[st^='ticket-PNR']").val();

			if (ticket_source == "" || ticket_cost == "") {
				fail_msg("请填写必填项！");
				endLoadingIndicator();
				return;
			}

			json += '{"ticket_source":+"' + ticket_source + '","ticket_source_pk":"' + ticket_source_pk + '","ticket_cost":"' + ticket_cost + '","ticket_PNR":"' + ticket_PNR + '","ticket_info":[';
			var trs = $(current).find(".table-ticket tbody tr");
			for ( var j = 0; j < trs.length; j++) {
				var tr = trs[j];
				var ticket_index = j+1;
				var ticket_date = $(tr).find("input[st^='ticket-date']").val();
				var ticket_number = $(tr).find("input[st^='ticket-number']").val();
				var from_to_time = $(tr).find("input[st^='from-to-time']").val();
				var from_to_city = $(tr).find("input[st^='from-to-city']").val();
				var from_airport = $(tr).find("input[st^='from-airport']").val();
				var to_airport = $(tr).find("input[st^='to-airport']").val();
				var terminal = $(tr).find("input[st^='terminal']").val();
				json += '{"ticket_index":+"' + ticket_index + '","ticket_date":"' + ticket_date + '","ticket_number":"' + ticket_number + '","from_to_time":"' + from_to_time + '","from_to_city":"'
						+ from_to_city + '","from_airport":"' + from_airport + '","to_airport":"' + to_airport + '","terminal":"' + terminal + '"}';
				if (j != trs.length - 1) {
					json += ",";
				}
			}
			json += ']';
			var passengers = $(current).find("em");
			var pks = "";
			for ( var k = 0; k < passengers.length; k++) {
				var passenger = passengers[k];
				pks += $(passenger).find("input[st^='passenger-pk']").val() + ",";
			}
			pks = pks.substr(0, pks.length - 1);
			json += ',"passenger_pks":"' + pks + '"}';
			if (i != all.length - 1) {
				json += ",";
			}
		}
		json += ']';
		$.ajax({
			type : "POST",
			url : self.apiurl + 'ticket/allotTicket',
			data : "json=" + json
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/ticket/name-list.jsp";
			} else {
				fail_msg(str);
			}
			endLoadingIndicator();
		});
	};

};

var ctx = new AgencyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});

function deleteRow(input) {
	var count = $(input).parent().parent().parent().children().length;
	if (count > 1) {
		$(input).parent().parent().remove();
	} else {
		// do nothing
	}
}
///**
// * 添加row
// * 
// * @param btn
// */
//function addRow(btn) {
//	var template = '<tr>' + '<td><input type="button" value="-" onclick="deleteRow(this)"></input></td>' + '<td><input st="ticket-index" type="number" /></td>'
//			+ '<td><input class="date-picker" st="ticket-date" type="text" /></td>' + '<td><input st="ticket-number" maxlength="10" type="text" /></td>'
//			+ '<td><input st="from-to-time" maxlength="20" type="text" /></td>' + '<td><input st="from-to-city"  maxlength="50" type="text" /></td>'
//			+ '<td><input st="from-airport"  maxlength="10" type="text" /></td>' + '<td><input st="to-airport"  maxlength="10" type="text" /></td>'
//			+ '<td><input st="terminal"  maxlength="10" type="text" /></td>' + '</tr>';
//	$(btn).parent().parent().find("tbody").append(template);
//	reloadDatePicker();
//}
