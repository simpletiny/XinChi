var passengerBatLayer;
function cancelBat() {
	layer.close(passengerBatLayer);
}
function addName() {
	var tbody = $("#name-table").find("tbody");
	var count = $(tbody).children().length;
	var html = '<tr>'
			+ '<td><input type="radio" name="team_chairman" /></td>'
			+ '<td st="name-index">'
			+ (count + 1)
			+ '</td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" st="name" /></td>'
			+ '<td><select class="form-control" style="height: 34px" st="sex">'
			+ '<option value="">选择</option>'
			+ '<option value="M">男</option>'
			+ '<option value="F">女</option>'
			+ '</select></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" st="age" /></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" st="cellphone_A" /></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" st="cellphone_B" /></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" maxlength="18" oninput="autoPrice();autoCaculate()" st="id" /></td>'
			+ '<td><input type="text" class="ip-" style="width: 90%" oninput="fixAllPrice(event);autoPrice()" value="'
			+ (ctx.product().adult_price - ctx.product().business_profit_substract) + '" st="price" /></td>'
			+ '<td><input type="button" style="width: 60%" onclick= "removeName(this)" alt="删除名单" value="—" /></td>'
			+ '</tr>';
	tbody.append(html);
};

function autoPrice(event) {
	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();

	var sumMoney = 0;
	var otherCost = $("#other-cost").val().trim();
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		var td_price = $(tr).find("[st='price']");
		var id = $(td_id).val();

		if (id.length < 18)
			continue;

		var price = $(td_price).val().trim();
		sumMoney += +price;
	}
	sumMoney += +otherCost;
	$("#txt-auto-sum-money").text(sumMoney);
	$("#auto-sum-money").val(sumMoney);

}

function autoCaculate() {
	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	var adultCnt = 0;
	var childrenCnt = 0;

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		var td_price = $(tr).find("[st='price']");
		var id = $(td_id).val();
		var td_sex = $(tr).find("[st='sex']");
		var td_age = $(tr).find("[st='age']");
		var d = new Date();
		var year_now = d.getFullYear();
		var birthYear = id.substring(6, 10);

		if (id.length < 18)
			continue;

		$(td_age).val(year_now - birthYear);
		var lastSecond = id.charAt(16);
		$(td_sex).val(lastSecond % 2 == 0 ? "F" : "M");
		var birthday = id.substring(6, 14);
		if (isChild(birthday)) {
			if ($(td_price).val() - 0 == ctx.product().adult_price - ctx.product().business_profit_substract
					|| $(td_price).val() == "") {
				$(td_price).val(ctx.product().child_price - ctx.product().business_profit_substract);
			}
			childrenCnt++;

		} else {
			if ($(td_price).val() - 0 == ctx.product().child_price - ctx.product().business_profit_substract
					|| $(td_price).val() == "") {
				$(td_price).val(ctx.product().adult_price - ctx.product().business_profit_substract);
			}
			adultCnt++;
		}
	}
	if (adultCnt != 0) {

		$("#txt-auto-adult-count").text(adultCnt);
		$("#auto-adult-count").val(adultCnt);
	}

	if (childrenCnt != 0) {
		$("#txt-auto-children-count").text(childrenCnt);
		$("#auto-children-count").val(childrenCnt);
	}
}

function fixAllPrice(e) {
	if ($("#chk-bind").is(":checked")) {
		var current = e.target;
		var tbody = $("#name-table").find("tbody");
		var prices = tbody.find("[st='price']");
		$(prices).val($(current).val());
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
	autoPrice();
	autoCaculate();
}

var writeName = function(nameObj) {

	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	var done = false;
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];

		if ($(tr).find("[st='name']").val().trim() == "" && $(tr).find("[st='id']").val().trim() == "") {

			var name = $(tr).find("[st='name']");
			var id = $(tr).find("[st='id']");

			$(name).val(nameObj.name);
			$(id).val(nameObj.id);

			done = true;
		}
	}

	if (!done) {
		addName();
		writeName(nameObj);
	}

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

	autoPrice();
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