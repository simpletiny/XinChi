function addRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;

	if (index == 10)
		return;

	var tr = $('<tr><input type="hidden" st="flight-index"/><td st="index"></td><td><input st="date" class="ip- date-picker" type="text" maxlength="10" /></td><td><input st="from-city" class="ip- " type="text" maxlength="10" /></td><td><input st="to-city" class="ip- " type="text" maxlength="10"/></td><td><input type="button" value="-" onclick="deleteRow(this)"></input></td></tr>');

	$(tr).find("td[st='index']").text(index + 1);
	$(tr).find("input[st='flight-index']").val(index + 1);
	tbody.append(tr);

	reloadDatePicker();
}

function deleteRow(txt) {

	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;
	if (index == 1)
		return;

	$(txt).parent().parent().remove();
	var ins = $(tbody).find("td[st='index']");
	var hid_ins = $(tbody).find("input[st='flight-index']");

	for (var i = 0; i < ins.length; i++) {
		$(ins[i]).text(i + 1);
		$(hid_ins[i]).val(i + 1);
	}
}