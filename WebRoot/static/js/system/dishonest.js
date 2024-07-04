var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	// 批量导入
	self.batName = function() {
		passengerBatLayer = $.layer({
			type : 1,
			title : ['批量导入名单', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '300px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#bat-passenger'
			},
			end : function() {
				console.log("Done");
			}
		});
	};
	self.check=function(){
		
		let valid_result = true;
		$("#div-name-list").find("input[name='name'], input[name='id']").each(function(index,txt){
			if($(txt).val().trim()==""){
				fail_msg("请填写非空项！")
				valid_result = false;
				return false; 
			}
		});
		
		if(!valid_result)
			return;
		$("#div-result").show();
		const tbody = $("#table-result").find("tbody");
		$(tbody).empty();
		let name_divs=	$("#div-name-list").children();
		startLoadingSimpleIndicator("查询中...");
		let count = 0;
		for(let i=0;i<name_divs.length;i++){
			let name_div = $(name_divs[i]);
			let name = name_div.find("input[name='name']").val().trim();
			let id = name_div.find("input[name='id']").val().trim();
			let img = name_div.find("img");
			const param = "person.name="+name+"&person.id="+id;
			$.ajax({
				type : "POST",
				url : self.apiurl + "system/checkIsDishonest",
				data : param
			}).success(function(data) {
				count++;
				let person_result = data.person_result;
				let tr_len = $(tbody).children().length;
				if(person_result.code==200){
					if(person_result.dishonest_flg=="N"){
						$(img).attr("src", self.apiurl + "static/img/dui.png");
						const status = "成功";
						let tr = $(`<tr>
									<td>${tr_len+1}</td>
									<td>${name}</td>
									<td style="color:green">成功</td>
									<td style="color:green">否</td>
									<td>没有找到 ${id}${name}相关的结果</td>
									<td>---</td>
								</tr>`);
						$(tbody).append(tr);
					}else if(person_result.dishonest_flg=="Y"){
						$(img).attr("src", self.apiurl + "static/img/cuo.png");
						let person_cases = data.cases;
						if(person_cases){
							for(let i=0;i<person_cases.length;i++){
								const person_case = person_cases[i];
								const result_msg = "立案时间："+person_case.reg_date+"；案号："+person_case.case_code;
								const sign = person_case.sign_flg=="Y"?"是":"否";
								let tr = $(`<tr>
											<td>${tr_len+1}</td>
											<td>${name}</td>
											<td style="color:green">成功</td>
											<<td style="color:red">失信人</td>
											<td>${result_msg}</td>
											<td>${sign}</td>
										</tr>`);
								$(tbody).append(tr);
								tr_len++;
							}
						}
					}
				}else{
					$(img).attr("src", self.apiurl + "static/img/cuo.png");
					const result_msg = person_result.msg;
					let tr = $(`<tr>
								<td>${tr_len+1}</td>
								<td>${name}</td>
								<td style="color:red">失败</td>
								<td>---</td>
								<td>${result_msg}</td>
								<td>---</td>
							</tr>`);
					$(tbody).append(tr);
				}
				if(count == name_divs.length){
					endLoadingIndicator();
				}
			});
		}
	}
};

var ctx = new OrderContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		changeFile(this);
	});
	// changeAutoType("Y");
});

var passengerBatLayer;
function cancelBat() {
	layer.close(passengerBatLayer);
}
function doBat() {
	nameModule.nameFormat();
	layer.close(passengerBatLayer);
}
function addName() {
	nameModule.addTr();
}
var removeName = function(alink) {
	let name_divs=	$("#div-name-list").children();
	if(name_divs.length>1){
		$(alink).parent().parent().remove();
	}
}
// 判断是否已经存在重复的id乘客
var isRepeatId = function(div_id,id) {
	const trs = $(`#${div_id}`).children();
	for (let i = 0; i < trs.length; i++) {
		const tr = trs[i];
		const td_id = $(tr).find("[name='id']");
		const existId = $(td_id).val().trim();
		if (id == existId)
			return true;
	}
	return false;
}
function formatNameList(option) {
	let content_id = option.content_id;
	let div_id = option.div_id;
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
		if (isRepeatId(div_id,id))
			continue;
		let person ={id,name};
		people.push(person);
	}
	
	writeName(people,div_id,append_html);
	$(`#${content_id}`).val(newNameList);
	return people;
}
const nameModule = (function(){
	let html = `
		<div class="input-row clearfloat">
			<div class="col-md-3 required">
				<label class="l" style="width: 50px !important">姓名:</label>
				<div class="ip fix-width">
					<input type="text" name='name' required class="ip- date-picker" maxlength="10" placeholder="姓名" />
				</div>
			</div>
			<div class="col-md-4 required">
				<label class="l" style="width: 75px !important">身份证号:</label>
				<div class="ip">
					<input type="text" name='id' required class="ip- date-picker" maxlength="18" placeholder="身份证号" />
				</div>
			</div>
			<div class="col-md-1">
				<img src="" alt="" />
			</div>
			<div class="col-md-1">
				<a href="javascript:void(0)" onclick="removeName(this)" style="line-height: 40px">删除</a>
			</div>
		</div>
	`;
	function nameFormat(){
		return formatNameList({
			content_id:"txt-name-list",
			div_id:"div-name-list",
			append_html:html
		});
	}
	
	function addTr(){
		let tr = $(html);
		$("#div-name-list").append(tr);
	}
	return {nameFormat,addTr};
})();

var writeName = function(people,div_id,append_html) {
	const div = $(`#${div_id}`);
	const trs = div.children();
	let index = trs.length;
	let can_write_trs = new Array();
	for (let i = 0; i < trs.length; i++) {
		const tr = $(trs[i]);
		if (tr.find("[name='name']").val().trim() == "" && tr.find("[name='id']").val().trim() == "") {
			can_write_trs.push(tr);
		}
	}
	const more_len = people.length-can_write_trs.length;
	let more_tr = more_len>0?Array.from({ length: more_len }, () => {
		  return $(append_html);
		}):[];
	
	more_tr.forEach(function(tr){
		div.append(tr);
	});
	
	const all_tr = [...can_write_trs,...more_tr];
	
	all_tr.forEach(function(tr,index){
		let name = $(tr).find("[name='name']");
		let id = $(tr).find("[name='id']");
		let person = people[index];
		$(name).val(person.name);
		$(id).val(person.id);
	})
}
