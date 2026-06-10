var operateContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.air_ticket_charge_config = ko.observable({});
	self.refreshAirTicketCharge = function() {
		var param = "type=AIRCHARGE";
		$.getJSON(self.apiurl + 'system/searchByType', param, function(data) {
			self.air_ticket_charge_config(data.datas[0]);
		});
	};
	self.finish = function() {
		startLoadingSimpleIndicator("保存中");
		var all = $(".every-count");
		let infos = new Array();
		for (var i = 0; i < all.length; i++) {
			var current = all[i];

			var ticket_source = $(current).find("input[st^='ticket-source']").val();
			var ticket_source_pk = $(current).find("input[st^='ticket-source-pk']").val();
			var ticket_PNR = $(current).find("input[st^='ticket-PNR']").val().trim();

			if (ticket_source == "") {
				endLoadingIndicator();
				fail_msg("请填写票源！");
				return;
			}

			let info = {};
			info.ticket_source = ticket_source;
			info.ticket_source_pk = ticket_source_pk;
			info.ticket_PNR = ticket_PNR;

			let ticket_infos = new Array();

			var trs = $(current).find(".table-ticket tbody tr");
			for (var j = 0; j < trs.length; j++) {
				let ticket_info = {};
				var tr = trs[j];
				var ticket_index = j + 1;
				var ticket_date = $(tr).find("input[st^='ticket-date']").val();
				var ticket_number = $(tr).find("input[st^='ticket-number']").val().trim();

				if (ticket_number == "") {
					endLoadingIndicator();
					fail_msg("请填写航班号！");
					return;
				}

				var from_to_time = $(tr).find("input[st^='from-to-time']").val().trim();

				if (from_to_time == "") {
					endLoadingIndicator();
					fail_msg("请填写起降时刻！");
					return;
				}
				let add_day_flg = $(tr).find("input[st^='add-day-flg']").is(":checked") ? "Y" : "N";
				var from_to_city = $(tr).find("input[st^='from-to-city']").val();
				var start_place = $(tr).find("input[st^='start-place']").val();
				var end_place = $(tr).find("input[st^='end-place']").val();
				ticket_info.ticket_index = ticket_index;
				ticket_info.ticket_date = ticket_date;
				ticket_info.ticket_number = ticket_number;
				ticket_info.from_to_time = from_to_time;
				ticket_info.add_day_flg = add_day_flg;
				ticket_info.from_to_city = from_to_city;
				ticket_info.from_airport = start_place;
				ticket_info.to_airport = end_place;

				ticket_infos.push(ticket_info);
			}

			info.ticket_infos = ticket_infos;

			// 游客价格信息
			let name_table_trs = $(current).find("table[st='name-table'] tbody tr");
			let name_infos = new Array();
			let name_check = 0;
			name_table_trs.each((index, tr) => {
				const passenger_pk = $(tr).find("input[st='passenger-pk']").val();
				const ticket_cost = $(tr).find("input[st='ticket-cost']").val().trim() - 0;
				const taxation = $(tr).find("input[st='taxation']").val().trim() - 0;
				const other_cost = $(tr).find("input[st='other-cost']").val().trim() - 0;
				if (ticket_cost == "") {
					name_check = 1;
					return false;
				}

				let name = {};
				name.passenger_pk = passenger_pk;
				name.ticket_cost = ticket_cost;
				name.taxation = taxation;
				name.other_cost = other_cost;
				name_infos.push(name);
			});

			if (name_check == 1) {
				endLoadingIndicator();
				fail_msg("请填写票款！");
				return;
			}

			info.name_infos = name_infos;

			infos.push(info);
		}

		let json = JSON.stringify(infos);
		$.ajax({
			type: "POST",
			url: self.apiurl + 'ticket/allotTicket',
			data: "json=" + json
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

var ctx = new operateContext();

$(document).ready(function() {
	$("input[st='ticket-source']").disabled();
	ko.applyBindings(ctx);
	ctx.refreshAirTicketCharge();
});

function deleteRow(input) {
	var count = $(input).parent().parent().parent().children().length;
	if (count > 1) {
		$(input).parent().parent().remove();
	} else {
		// do nothing
	}
}
function fixBind() {
	let txt = event.target;
	let value = txt.value;
	const st_name = $(txt).attr("st");
	let tr = $(txt).parent().parent();
	let tbody = tr.parent();
	let table = tbody.parent();
	let thead = $(tbody).prev();
	let chk = thead.find("input[st='" + st_name + "']");
	if (chk.is(":checked")) {
		tbody.find("input[st='" + st_name + "']").val(value);
	}
	if (st_name !== 'sum')
		autoCaculate(table);
}

function autoCaculate(table) {
	let trs = $(table).find("tbody tr");
	let sum_other = 0;
	let sum = 0;
	trs.each((index, tr) => {
		let ticket_cost = $(tr).find("input[st='ticket-cost']").val().trim() - 0;
		let taxation = $(tr).find("input[st='taxation']").val().trim() - 0;
		let other_cost = $(tr).find("input[st='other-cost']").val().trim() - 0;
		let current_sum = ticket_cost + taxation + other_cost;
		sum_other += other_cost;
		sum += current_sum;
		$(tr).find("td[st='sum']").text(current_sum);

	});
	let div = table.parent().parent().parent();
	let txt_sum_other = div.find("p[st='sum-other']");
	let txt_sum = div.find("p[st='sum-cost']");

	txt_sum_other.text(sum_other);
	txt_sum.text(sum);
}