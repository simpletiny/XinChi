var passengerBatLayer;

function cancelBat() {
	layer.close(passengerBatLayer);
}
function doBat(){
	nameModule.nameFormat();
	layer.close(passengerBatLayer);
	autoPersonInfo();
}
function inputId(){
	autoPersonInfo();
}

function inputAge(){
	autoPersonInfo();
}
function autoPersonInfo() {
	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	var adultCnt = 0;
	var childrenCnt = 0;

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		let td_age = $(tr).find("[st='age']");
		var td_price = $(tr).find("[st='price']");
		var td_sex = $(tr).find("[st='sex']");
		let td_type = $(tr).find("[st='type']")
		var id = $(td_id).val().trim();
		let person = parseID(id);
		$(td_type).val(person.id_type);
		
		if(person.id_type=="I"){
			if (id.length < 18)
				continue;
			$(td_age).val(person.age);
			$(td_sex).val(person.sex);
			if (person.is_child) {
				childrenCnt++;
			} else {
				adultCnt++;
			}
		}else{
			const age = $(td_age).val().trim();
			if(age==""||age>=12||age<=0){
				adultCnt++;
			}else{
				childrenCnt++;
			}
		}
		
		
	}
	$("#people-count").val(adultCnt);
	$("#special-count").val(childrenCnt);
}
function addName() {
	nameModule.addTr();
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
	autoPersonInfo();

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

const nameModule = (function(){
	var html  = `
		<tr>
	    <input type="hidden" st="name-pk" />
	    <input type="hidden" data-bind="value:$data.lock_flg" st="name-lock"/>
	    <td><input type="radio" name="team_chairman" /></td>
	    <td st="name-index"></td>
	    <td><input type="text" style="width: 90%" class="ip-" st="name" /></td>
	    <td>
	        <select class="form-control" style="height: 34px" st="sex">
	            <option value="">选择</option>
	            <option value="M">男</option>
	            <option value="F">女</option>
	        </select>
	    </td>
	    <td><input type="number" class="ip-" style="width: 90%" oninput="inputAge()" st="age" /></td>
	    <td><input type="text" class="ip-" style="width: 90%" st="cellphone_A" /></td>
	    <td><input type="text" class="ip-" style="width: 90%" st="cellphone_B" /></td>
	    <td><select class="form-control" style="height: 34px" st="type">
				<option value="I">身份证</option>
				<option value="P">护照</option>
		</select></td>
	    <td><input type="text" class="ip-" maxlength="18" oninput="inputId();" style="width: 90%" st="id" /></td>
	    <td><input type="button" style="width: 60%" onclick="removeName(this)" alt="删除名单" value="—" /></td>
	</tr>
	`;
	
	function nameFormat(){
		return formatNameList({
			content_id:"txt-name-list",
			table_id:"name-table",
			append_html:html
		});
	}
	
	function addTr(){
		const tbody = $("#name-table").find("tbody");
		const count = $(tbody).children().length;
		let tr = $(html);
		tr.find("[st='name-index']").text(count+1);
		tbody.append(tr);
	}
	return {nameFormat,addTr};
})();