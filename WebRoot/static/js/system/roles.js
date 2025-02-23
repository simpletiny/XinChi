let firstPageLayer;
let initialState;
let updateOrder;
let initialPages;
let addedPages;
let removedPages;
let initialFirstPages;
let firstPages;
var DataContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.user_roles = ko.observableArray([]);
	self.refreshUserRoles = function() {
		initialState = new Map();
		updateOrder = new Map();
		var param = "type=ROLE";
		$.getJSON(self.apiurl + 'system/searchByType', param, function(data) {
			self.user_roles(data.datas);
			$(data.datas).each(function(index, role) {
				initialState.set(role.code, role.order_index);
				self.refreshRolePages();
			});
		});
	};
	self.pages = ko.observableArray([]);
	self.refreshPages = function() {
		$.getJSON(self.apiurl + 'system/searchSortPages', {}, function(data) {
			self.pages(data.pages);
		});
	}
	self.role_pages = ko.observableArray([]);
	self.refreshRolePages = function(){
		initialPages = new Set();
		addedPages = new Set();
		removedPages = new Set();
		initialFirstPages = new Map();
		firstPages = new Map();
		
		let role = $("div.light-blue").attr("role-code");
		
		$.getJSON(self.apiurl + 'system/searchRolePages', {role:role}, function(data) {
			self.role_pages(data.role_pages);


			$(data.role_pages).each(function(index, role_page) {
				if(role_page.is_father==="Y"){
					let first_page = data.role_pages.filter(page => page.is_father!=="Y" && page.page_url === role_page.page_url);
					if(first_page.length>0){
						initialFirstPages.set(role_page.page_pk.toString(),first_page[0].page_pk.toString());
					}
				}else{
					initialPages.add(role_page.page_pk.toString());
				}
				
			});
			
			init_page();
		});
	}
};

var ctx = new DataContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$(document).on('change', '.child input[type="checkbox"]', function () {
	   selectPages(this);
	});
	new Sortable(roleGrid, {
		animation : 150,
		ghostClass : 'blue-background-class',
		onEnd : function(e) {
			sortUserRoles(e);
		}
	});
	ctx.refreshUserRoles();
	ctx.refreshPages();
});

let init_page = function(){
	$("h3 input[type='checkbox']").prop('checked', false);
	$("h2 small.first-page").empty();
	initialFirstPages.forEach((value, key) => {
		$("h2[data-pk='"+key+"']").find("small.first-page").text($("h3 span[data-pk='"+value+"']").text());
	});
	for(let page_pk of initialPages){
		$("h3 input[type='checkbox'][data-pk='"+page_pk+"']").prop('checked', true);
	}
	showButton();
}

let selectPages = function(chk){
	let pk = $(chk).attr("data-pk");
	const parent2 = $(chk).parent().parent().parent();
	const father_pk = $(chk).attr("father-pk");
	
	if($(chk).is(":checked")){
		if(initialPages.has(pk)){
			removedPages.delete(pk);
		}else{
			addedPages.add(pk);
		}
		removedPages.delete(father_pk);
	}else{
		if(initialPages.has(pk)){
			removedPages.add(pk);
		}else{
			addedPages.delete(pk);
		}
		
		if(!parent2.find("input[type='checkbox']").is(":checked")){
			const first_page = parent2.prev().find("small.first-page");
			first_page.text("");
			firstPages.delete(father_pk);
			if(initialFirstPages.has(father_pk)){
				removedPages.add(father_pk);
			}
		}
	}
	showButton();
}
let sortUserRoles = function(e) {
	let [from_index, to_index] = e.oldIndex < e.newIndex ? [e.oldIndex, e.newIndex] : [e.newIndex, e.oldIndex];
	var allDivs = $(e.from).children();
	for (var i = from_index; i <= to_index; i++) {
		var div = allDivs[i];
		const role_code = $(div).attr("role-code");
		const order_index = i+1;
		if(initialState.get(role_code)!==order_index){
			updateOrder.set(role_code,order_index);
		}else{
			updateOrder.delete(role_code);
		}
	}
	showButton();
}

let changeRoles = function(div) {
	if ($(div).hasClass("light-blue")) {
		return;
	} else {
		if(checkUnsaved()){
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '有未保存的数据，确认要更改角色吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						$(".light-blue").removeClass("light-blue");
						$(div).addClass("light-blue");
						ctx.refreshRolePages();
						showButton();
					}
				}
			});
		}else{
			$(".light-blue").removeClass("light-blue");
			$(div).addClass("light-blue");
			ctx.refreshRolePages();
			showButton();
		}
	}
}

let revoke = function() {
	$.layer({
		area : ['auto', 'auto'],
		dialog : {
			msg : '确认要撤销所作修改吗？',
			btns : 2,
			type : 4,
			btn : ['确认', '取消'],
			yes : function(index) {
				layer.close(index);
				ctx.refreshUserRoles();
				ctx.refreshPages();
				showButton();
			}
		}
	});
	
}

let save = function() {
	$.layer({
		area : ['auto', 'auto'],
		dialog : {
			msg : '权限更改，重新登录后生效。确认保存修改吗？',
			btns : 2,
			type : 4,
			btn : ['确认', '取消'],
			yes : function(index) {
				layer.close(index);
				if(!validateBeforeSave())
					return;
				startLoadingSimpleIndicator("保存中");
				let role = $("div.light-blue").attr("role-code");
				const role_order = Array.from(updateOrder, ([pk, order_index]) => ({ pk, order_index }));
				const first_pages =Array.from(firstPages, ([pk, child_pk]) => ({ pk, child_pk }));
				const json = {
					role :role,
					role_order:role_order,
					added_pages:[...addedPages],
					removed_pages:[...removedPages],
					first_pages:first_pages
				}
				const data = "json="+JSON.stringify(json);
				$.ajax({
					type : "POST",
					url : ctx.apiurl + 'system/updateRolePages',
					data : data

				}).success(function(str) {
					endLoadingIndicator();
					if (str == "success") {
						init_map();
						endLoadingIndicator();
						showButton();
					} else {
						fail_msg("排序失败，请联系管理员！");
					}
				});
			}
		}
	});
}

let init_map = function(){
	initialPages = new Set();
	addedPages = new Set();
	removedPages = new Set();
	initialFirstPages = new Map();
	firstPages = new Map();
	initialState = new Map();
	updateOrder = new Map();
}
let validateBeforeSave = function(){
	let emptyElements = $("h2 small").filter(function() {
	    return $(this).text().trim() === "";
	});
	if (emptyElements.length > 0) {
		let need_check = "";
		$(emptyElements).each(function(index,sma){
			if($(sma).parent().next().find("input[type='checkbox']").is(":checked")){
				need_check +=$(sma).prev().text()+",";
			}
		});
		if(need_check!==""){
			const msg = "请选择["+need_check.RTrim(",")+"]的首页指向页面！";
			fail_msg(msg);
			return false;
		}
	}
	
	return true;
}
let editFirstPage = function(alink){
	const page_box = $(alink).parent().parent();
	const father_pk = $(alink).parent().attr("data-pk");
	$("#first-page-edit").find("input.pk").val(father_pk);
	$("#sel-first-page").empty();
	if(!page_box.find("input[type='checkbox']").is(":checked")){
		fail_msg("至少选择一个子页面！")
		return;
	}
	const selected_page_pk = firstPages.get(father_pk)?firstPages.get(father_pk):initialFirstPages.get(father_pk);
	page_box.find("input[type='checkbox']").each(function(index,chk){
		if($(chk).is(":checked")){
			let child_pk = $(chk).attr("data-pk");
			let page_title = $(chk).attr("page-title");
			if(child_pk===selected_page_pk){
				$('#sel-first-page').append(new Option(page_title, child_pk,false,true));
			}else{
				$('#sel-first-page').append(new Option(page_title, child_pk));
			}
		}
	});
	
	firstPageLayer = $.layer({
		type : 1,
		title : ['备注', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['500px', '200px'],
		offset : ['', ''],
		scrollbar : true,
		page : {
			dom : '#first-page-edit'
		},
		end : function() {
			console.log("Done");
		}
	});
}
let confirmFirstPage = function(){
	const choosed_first_page_name = $("#sel-first-page option:selected").text();
	const choosed_first_page_pk =$("#sel-first-page").val();
	const father_pk = $("#first-page-edit").find("input.pk").val();
	const title_h2 = $(".box h2[data-pk='"+father_pk+"']");
	title_h2.find("small.first-page").text(choosed_first_page_name);
	
	if(initialFirstPages.get(father_pk)!==choosed_first_page_pk){
		firstPages.set(father_pk,choosed_first_page_pk);
	}else{
		firstPages.delete(father_pk);
	}
	layer.close(firstPageLayer);
	showButton();
}

let cancelEditFirstPage = function(){
	layer.close(firstPageLayer);
}
let checkUnsaved = function(){
	return updateOrder.size>0||addedPages.size>0||removedPages.size>0||firstPages.size>0;
}

let showButton = function(){
	checkUnsaved()?$(".fixed").show():$(".fixed").hide();
}