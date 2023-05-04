var passengerBatLayer;

function cancelBat() {
	layer.close(passengerBatLayer);
}
var addName = function() {
	var tbody = $("#name-table").find("tbody");
	var count = $(tbody).children().length;
	var html = '<tr>'
			+ '<input type="hidden" st="name-pk" />'
			+ '<input type="hidden" data-bind="value:$data.lock_flg" st="name-lock"/>'
			+ '<td><input type="radio" name="team_chairman" /></td>'
			+ '<td st="name-index">'
			+ (count + 1)
			+ '</td>'
			+ '<td><input type="text" style="width: 90%" class="ip-" st="name" /></td>'
			+ '<td><select class="form-control" style="height: 34px" st="sex">'
			+ '<option value="">选择</option>'
			+ '<option value="M">男</option>'
			+ '<option value="F">女</option>'
			+ '</select></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" st="age" /></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" st="cellphone_A" /></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" st="cellphone_B" /></td>'
			+ '<td><input type="text" class="ip-" maxlength="18" oninput="autoCaculate();" style="width: 90%" st="id" /></td>'
			+ '<td><input type="button" style="width: 60%" onclick= "removeName(this)" alt="删除名单" value="—" /></td>'
			+ '</tr>';
	tbody.append(html);
};
function autoCaculate() {
	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	var adultCnt = 0;
	var childrenCnt = 0;

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		var td_price = $(tr).find("[st='price']");
		var td_sex = $(tr).find("[st='sex']");
		var id = $(td_id).val();

		if (id.length < 18)
			continue;

		var td_age = $(tr).find("[st='age']");
		var d = new Date();
		var year_now = d.getFullYear();
		var birthYear = id.substring(6, 10);
		$(td_age).val(year_now - birthYear);

		var lastSecond = id.charAt(16);
		$(td_sex).val(lastSecond % 2 == 0 ? "F" : "M");

		var birthday = id.substring(6, 14);
		if (isChild(birthday)) {
			childrenCnt++;
		} else {
			adultCnt++;
		}
	}
	if (adultCnt != 0) {
		$("#people-count").val(adultCnt);
	}

	if (childrenCnt != 0) {
		$("#special-count").val(childrenCnt);
	}
}

function formatNameList() {
	nameList = $("#txt-name-list").val();
	if (nameList.trim() == "")
		return;
	var illegal = /[^\u4e00-\u9fa5,0-9,X,x]|[\n\r]/gm;

	nameList = nameList.replace(illegal, "");
	var idPattern = /[0-9,X,x]+/gm;
	var ids = nameList.match(idPattern);

	var namePattern = /[\u4e00-\u9fa5]+/gm;
	var names = nameList.match(namePattern);

	if (null == ids) {
		fail_msg("请填写正确的名单！");
		return;
	}

	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
		nameList = nameList.replace(id, ":" + id + ";");
	}

	var nameObjs = new Array();
	var names = nameList.split(";");
	var newNameList = "";

	for (var i = 0; i < names.length; i++) {
		if (names[i] == "")
			continue;

		if (i % 2 == 1) {
			newNameList += names[i] + ";\n";
		} else {
			newNameList += names[i] + ";";
		}
		var nameObj = new Object();
		var passenger = names[i].split(":");
		if (passenger.length < 2)
			continue;
		nameObj.name = passenger[0];
		nameObj.id = passenger[1];
		nameObjs.push(nameObj);
	}

	for (var i = 0; i < nameObjs.length; i++) {
		var nameObj = nameObjs[i];
		if (isRepeatId(nameObj.id))
			continue;
		writeName(nameObj);
	}

	$("#txt-name-list").val(newNameList);
	layer.close(passengerBatLayer);
	autoCaculate();
}

var writeName = function(nameObj) {

	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];

		if ($(tr).find("[st='name']").val().trim() == "" && $(tr).find("[st='id']").val().trim() == "") {

			var name = $(tr).find("[st='name']");
			var id = $(tr).find("[st='id']");

			$(name).val(nameObj.name);
			$(id).val(nameObj.id);

			return;
		}
	}

	addName();
	writeName(nameObj);

}

// 判断是否已经存在重复的id乘客
var isRepeatId = function(id) {
	var tbody = $("#name-table").find("tbody");

	var trs = $(tbody).children();

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		var existId = $(td_id).val();
		if (id == existId)
			return true;
	}
	return false;
}
var removeName = function(btn) {
	var tbody = $("#name-table").find("tbody");
	var count = $(tbody).children().length;
	var target = $(btn).parent().parent();
	if (count > 1) {
		var td_radio = $(target).find("[name='team_chairman']");
		$(target).remove();
		refreshNameIndex();

		if ($(td_radio).is(":checked")) {
			var next_chair = $("input[name='team_chairman']:eq(0)");
			$(next_chair).prop("checked", true);
		}
	}
	autoCaculate();

};
var refreshNameIndex = function() {
	var tbody = $("#name-table").find("tbody");

	var trs = $(tbody).children();

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_index = $(tr).find("[st='name-index']");
		$(td_index).html(i + 1);
	}
}