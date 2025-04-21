var passengerBatLayer;

function cancelBat() {
	layer.close(passengerBatLayer);
}

function autoPrice() {
	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();

	var sumMoney = 0;
	var otherCost = $("#other-cost").val().trim();
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		var td_price = $(tr).find("[st='price']");
		var td_type = $(tr).find("[st='type']");
		var id = $(td_id).val().trim();
		var type = $(td_type).val();

		if ((type == 'I' && id.length < 18) || (type == 'P' && id == ""))
			continue;

		var price = $(td_price).val().trim();
		sumMoney += +price;
	}
	sumMoney += +otherCost;
	$("#txt-auto-sum-money").text(sumMoney);
	$("#auto-sum-money").val(sumMoney);

}
// var td = debounce(test, 1500);
function inputId(txt) {
	autoPrice();
	autoPersonInfo();
	whenChangeId(txt);
}

function inputName(txt){
	whenChangeName(txt);
}

function whenChangeContent(txt){
	
}

function inputPrice() {
	fixAllPrice(event);
	autoPrice();
}
function inputAge(){
	autoPersonInfo();
}
function checkAdult(){
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
		var td_price = $(tr).find("[st='price']");
		var id = $(td_id).val().trim().toUpperCase();
		var td_sex = $(tr).find("[st='sex']");
		var td_age = $(tr).find("[st='age']");
		var td_type = $(tr).find("[st='type']");
		let as_adult = $(tr).find("[st='as-adult']");
		
		let person = parseID(id);
		$(td_type).val(person.id_type);

		if (person.id_type == 'I') {
			if (id.length < 18)
				continue;

			$(td_age).val(person.age);
			$(td_sex).val(person.sex);
			if (person.is_child) {
				if ($(td_price).val() == "") {
					$(td_price).val(ctx.product().child_price - ctx.product().business_profit_substract);
				}
				
				if(as_adult){
					$(as_adult).show();
					if($(as_adult).is(":checked")){
						adultCnt++;
					}else{
						childrenCnt++;
					}
				}else{
					childrenCnt++;
				}
			} else {
				if ($(td_price).val() == "") {
					$(td_price).val(ctx.product().adult_price - ctx.product().business_profit_substract);
				}
				adultCnt++;
				if(as_adult)
					$(as_adult).hide();
			}
		}else{
			if ($(td_price).val() == "") {
				$(td_price).val(ctx.product().adult_price - ctx.product().business_profit_substract);
			}
			let age =$(td_age).val().trim();
			if(age==""||age>=12||age<=0){
				adultCnt++;
				if(as_adult)
					$(as_adult).hide();
			}else{
				if(as_adult){
					$(as_adult).show();
					if($(as_adult).is(":checked")){
						adultCnt++;
					}else{
						childrenCnt++;
					}
				}else{
					childrenCnt++;
				}
			}
		}

		$("#txt-auto-adult-count").text(adultCnt);
		$("#auto-adult-count").val(adultCnt);
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

function doBat() {
	nameModule.nameFormat();
	layer.close(passengerBatLayer);
	autoPersonInfo();
	autoPrice();
}
function addName() {
	console.log("test");
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

	autoPrice();
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
	let html = `
		<tr>
			<input type="hidden" st="name-pk" />
			<input type="hidden" st="name-lock" />
			<input type="hidden" data-bind="value:$data.id" st="ok_id"/>
			<input type="hidden" data-bind="value:$data.name" st="ok_name"/>
			<input type="hidden" st="is_ok"/>
			<td><input type="radio" name="team_chairman" /></td>
			<td st="name-index"></td>
			<td><input type="text" class="ip-" style="width: 90%" st="name" oninput="inputName(this)"/></td>
			<td>
				<select class="form-control" style="height: 34px" st="sex">
					<option value="">选择</option>
					<option value="M">男</option>
					<option value="F">女</option>
				</select>
			</td>
			<td><input type="number" class="ip-" oninput="inputAge()" style="width: 90%" st="age" /></td>
			<td><input type="text" class="ip-" style="width: 90%" st="cellphone_A" /></td>
			<td><input type="text" class="ip-" style="width: 90%" st="cellphone_B" /></td>
			<td><select class="form-control" style="height: 34px" st="type">
					<option value="I">身份证</option>
					<option value="P">护照</option>
			</select>
			</td>
			<td><input type="text" class="ip-" style="width: 90%" maxlength="18" oninput="inputId(this)" st="id" />
			</td>
			<td st='td-as-adult'><input type="checkbox" style="display:none" value='Y' onchange='checkAdult()' st='as-adult' /></td>
			<td>
				<input type="text" class="ip-" style="width: 90%" oninput="inputPrice()"  st="price" />
			</td>
			<td><img src="" alt="" /></td> 
			<td><input type="button" style="width: 60%" onclick="removeName(this)" alt="删除名单" value="—" /></td>
		</tr>
	`;
	
	function nameFormat(){
		if(ctx.order().as_adult_flg=='N'){
			html = html.replace(`<td st='td-as-adult'><input type="checkbox" style="display:none" value='Y' onchange='checkAdult()' st='as-adult' /></td>`,'');
		}
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
		tr.find("[st='price']").val(ctx.product().adult_price - ctx.product().business_profit_substract);
		let td_as_adult = tr.find("[st='td-as-adult']");
		if(ctx.order().as_adult_flg=='N'){
			$(td_as_adult).remove();
		}
		tbody.append(tr);
	}
	return {nameFormat,addTr};
})();
let names;
function writeNameFromSession(){
	names = JSON.parse(sessionStorage.getItem('ok_names'));
	if(names!=null){
		let content = "";
		names.forEach(function(name){
			content+=name.name+name.id;
		})
		$("#txt-name-list").val(content);
		doBat();
		
		let trs = $("#name-table tbody").children();
		trs.each(function(index,tr){
			let txt_id = $(tr).find("input[st='id']");
			let txt_ok_id = $(tr).find("input[st='ok_id']");
			let txt_name = $(tr).find("input[st='name']");
			let txt_ok_name = $(tr).find("input[st='ok_name']");
			let txt_is_ok = $(tr).find("input[st='is_ok']");
			let img = $(tr).find("img");
			txt_ok_id.val(txt_id.val());
			txt_ok_name.val(txt_name.val());
			txt_is_ok.val("Y");
			$(img).attr("src", ctx.apiurl + "static/img/dui.png");
		});
	}
}

function whenChangeId(txt){
	let txt_id = $(txt);
	let tr = txt_id.parent().parent();
	let txt_ok_id =  tr.find("input[st='ok_id']");
	let txt_is_ok = tr.find("input[st='is_ok']");
	let img = tr.find("img");
	if(txt_ok_id.val()!=""||txt_is_ok.val()==="Y"){
		if(txt_id.val()!=txt_ok_id.val()){
			$(img).attr("src", ctx.apiurl + "static/img/cuo.png");
			txt_is_ok.val("N");
			
		}else{
			$(img).attr("src", ctx.apiurl + "static/img/dui.png");
			txt_is_ok.val("Y");
		
		}
	}
}
function whenChangeName(txt){
	let txt_name = $(txt);
	let tr = txt_name.parent().parent();
	let txt_ok_name = tr.find("input[st='ok_name']");
	let txt_is_ok = tr.find("input[st='is_ok']");
	let img = tr.find("img");
	if(txt_ok_name.val()!=""||txt_is_ok.val()==="Y"){
		if(txt_name.val()!=txt_ok_name.val()){
			$(img).attr("src", ctx.apiurl + "static/img/cuo.png");
			txt_is_ok.val("N");
		}else{
			$(img).attr("src", ctx.apiurl + "static/img/dui.png");
			txt_is_ok.val("Y");
		}
	}
}

function checkName(){
	let trs = $("#name-table tbody").children();
	const tbody = $("#table-result").find("tbody");
	$(tbody).empty();
	let validate_result = true;
	trs.each(function(index,tr){
		let txt_id = $(tr).find("input[st='id']");
		let txt_ok_id = $(tr).find("input[st='ok_id']");
		let txt_name = $(tr).find("input[st='name']");
		let txt_ok_name = $(tr).find("input[st='ok_name']");
		let txt_is_ok = $(tr).find("input[st='is_ok']");
		let img = $(tr).find("img");
		let name = txt_name.val();
		let id= txt_id.val();
		
		if(name.trim()===""||id.trim()===""){
			fail_msg("请填姓名或身份证号！");
			return;
		}
		let sel_type=$(tr).find("select[st='type']");
		let type= sel_type.val();
		if(type=="P"){
			$(img).attr("src", ctx.apiurl + "static/img/dui.png");
			$(txt_is_ok).val('Y');
			$(txt_ok_id).val(id);
			$(txt_ok_name).val(name);
			return true;
		}
		
		startLoadingSimpleIndicator("检验中");
		let person_result = validateName(name,id);
		if(person_result.code==200){
			if(person_result.dishonest_flg=="N"){
				$(img).attr("src", ctx.apiurl + "static/img/dui.png");
				$(txt_is_ok).val('Y');
				$(txt_ok_id).val(id);
				$(txt_ok_name).val(name);
			
				let person_cases = person_result.cases;
				if(person_cases && person_cases.length>0){
					let span = person_cases.length;
					for(let j=0;j<span;j++){
						const person_case = person_cases[j];
						const result_msg = "立案时间："+person_case.reg_date+"；案号："+person_case.case_code;
						const sign = person_case.sign_flg=="Y"?"是":"否";
						const sign_color =  person_case.sign_flg=="Y"?"green":"red";
						if(j==0){
							let tr = $(`<tr>
									<td rowspan="${span}">${index+1}</td>
									<td rowspan="${span}">${name}</td>
									<td style="color:green" rowspan="${span}">成功</td>
									<td style="color:green" rowspan="${span}">否</td>
									<td>${result_msg}</td>
									<td style="color:${sign_color}">${sign}</td>
									</tr>`);
							$(tbody).append(tr);
						}else{
							let tr = $(`<tr>
									<td>${result_msg}</td>
									<td style="color:${sign_color}">${sign}</td>
									</tr>`);
						$(tbody).append(tr);
						}
					}
					validate_result = false;
				}else{
					let tr = $(`<tr>
							<td>${index+1}</td>
							<td>${name}</td>
							<td style="color:green">成功</td>
							<td style="color:green">否</td>
							<td>没有找到 ${id}${name}相关的结果</td>
							<td>---</td>
						</tr>`);
					$(tbody).append(tr);
				}
			}else if(person_result.dishonest_flg=="Y" || person_result.dishonest_flg=="H"){
				$(img).attr("src", ctx.apiurl + "static/img/cuo.png");
				$(txt_is_ok).val('N');
				let person_cases = person_result.cases;
				if(person_cases){
					let span = person_cases.length;
					for(let j=0;j<span;j++){
						const person_case = person_cases[j];
						const result_msg = "立案时间："+person_case.reg_date+"；案号："+person_case.case_code;
						const sign = person_case.sign_flg=="Y"?"是":"否";
						const sign_color =  person_case.sign_flg=="Y"?"green":"red";
						const dis_type = person_result.dishonest_flg=="H"? "限高":"失信人";
						if(j==0){
							let tr = $(`<tr>
									<td rowspan="${span}">${index+1}</td>
									<td rowspan="${span}">${name}</td>
									<td style="color:green" rowspan="${span}">成功</td>
									<td style="color:red" rowspan="${span}">${dis_type}</td>
									<td>${result_msg}</td>
									<td style="color:${sign_color}">${sign}</td>
									</tr>`);
							$(tbody).append(tr);
						}else{
							let tr = $(`<tr>
									<td>${result_msg}</td>
									<td style="color:${sign_color}">${sign}</td>
									</tr>`);
						$(tbody).append(tr);
						}
					}
				}
				validate_result = false;
			}
		}else{
			$(img).attr("src", ctx.apiurl + "static/img/cuo.png");
			$(txt_is_ok).val('N');
			const result_msg = person_result.msg;
			let tr = $(`<tr>
						<td>${index+1}</td>
						<td>${name}</td>
						<td style="color:red">失败</td>
						<td>---</td>
						<td>${result_msg}</td>
						<td>---</td>
					</tr>`);
			$(tbody).append(tr);
			validate_result = false;
		}
	});
	
	if(!validate_result){
		showCheckMsg();
	}else{
		$("#span-msg").hide();
	}
	endLoadingIndicator();
}

function showCheckMsg(){
	if($("#span-msg").length){
		$("#span-msg").show();
	}else{
		let a_span = $(`<span id="span-msg"
		style="color:green;margin-right:10px;vertical-align:bottom;"><a href="javascript:void(0)" onclick="checkValidateResult()">点击查看校验结果</a></span>`);
		$("#div-btn-area").prepend(a_span);
	}
}
let validateResultLayer;
function checkValidateResult(){
	validateResultLayer = $.layer({
		type : 1,
		title : ['名单校验结果', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['1000px', '450px'],
		offset : ['50px', ''],
		scrollbar : true,
		page : {
			dom : '#div-result'
		},
		end : function() {
			console.log("Done");
		}
	});
}