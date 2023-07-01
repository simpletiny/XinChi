
function parseID(id){
	let id_type  = /^[A-Za-z]/.test(id)?'P':'I';
	
	if(id.length!=18)
		return {id_type};
	let d = new Date($("#hidden-server-date").val());
	let year_now = d.getFullYear();
	let birthYear = id.substring(6, 10);

	let age = year_now - birthYear;

	let lastSecond = id.charAt(16);
	let sex = lastSecond % 2 == 0 ? "F" : "M";
	let birthday = id.substring(6, 14);
	let is_child = isChild(birthday);
	return {id_type,age,sex,is_child};
}

function formatNameList(option) {
	let content_id = option.content_id;
	let table_id = option.table_id;
	let append_html = option.append_html;

	let name_list = $(`#${content_id}`).val().trim();

	if (name_list == "")
		return;
	
	const idPattern = /\d{17}[\dXx]|[A-Z,a-z]{1,2}\d+/g;
	const namePattern = /[\u4e00-\u9fa5]+/g;
	let ids = name_list.match(idPattern);
	let names = name_list.match(namePattern);
	if (null == ids) {
		fail_msg("请填写正确的名单！");
		return;
	}
	if(ids.length!=names.length){
		fail_msg("姓名和证件号码个数不匹配！")
		return;
	}
	
	let people = new Array();
	let newNameList = "";
	for (let i = 0; i < ids.length; i++) {
		const id = ids[i];
		const name = names[i];
		newNameList +=name+":"+id+";\n";
		if (isRepeatId(table_id,id))
			continue;
		let person ={id,name};
		people.push(person);
	}
	
	writeName(people,table_id,append_html);
	$(`#${content_id}`).val(newNameList);
	return people;
}


var writeName = function(people,table_id,append_html) {
	const tbody = $(`#${table_id}`).find("tbody");
	const trs = $(tbody).children();
	let index = trs.length;
	let can_write_trs = new Array();
	for (let i = 0; i < trs.length; i++) {
		const tr = $(trs[i]);
		if (tr.find("[st='name']").val().trim() == "" && tr.find("[st='id']").val().trim() == "") {
			can_write_trs.push(tr);
		}
	}
	const more_len = people.length-can_write_trs.length;
	let more_tr = more_len>0?Array.from({ length: more_len }, () => {
		  return $(append_html);
		}):[];
	
	
	more_tr.forEach(function(tr){
		let name_index = tr.find("[st='name-index']");
		$(name_index).text(++index);
	
		tbody.append(tr);
	})
	
	const all_tr = [...can_write_trs,...more_tr];
	
	all_tr.forEach(function(tr,index){
		let name = $(tr).find("[st='name']");
		let id = $(tr).find("[st='id']");
		let price = $(tr).find("[st='price']");
		
		let person = people[index];
		$(name).val(person.name);
		$(id).val(person.id);
	})
}

// 判断是否已经存在重复的id乘客
var isRepeatId = function(table_id,id) {
	const tbody = $(`#${table_id}`).find("tbody");

	const trs = $(tbody).children();

	for (let i = 0; i < trs.length; i++) {
		const tr = trs[i];
		const td_id = $(tr).find("[st='id']");
		const existId = $(td_id).val();
		if (id == existId)
			return true;
	}
	return false;
}