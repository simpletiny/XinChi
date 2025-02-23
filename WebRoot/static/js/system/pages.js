var titleLayer;
let initialState;
let updateOrder;
let updateTitle;
let updateChildFatherPk;
var DataContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.pages = ko.observableArray([]);

	self.refresh = function() {
		$.getJSON(self.apiurl + 'system/searchSortPages', {}, function(data) {
			self.pages(data.pages);
			// 记录页面初始化信息
			initialState = new Map();
			updateOrder = new Map();
			updateTitle = new Map();
			updateChildFatherPk = new Map();
			$(data.pages).each(function(father_index, father) {
				let father_pk = father.pk;
				let father_father_pk = father.father_pk || "0";
				let father_order_index = father.order_index;
				let father_page_title = father.page_title
				initialState.set(father_pk, {
					order_index : father_order_index,
					father_pk : father_father_pk,
					page_title : father_page_title
				});
				$(father.child_pages).each(function(child_index, child) {
					let child_pk = child.pk;
					let child_father_pk = child.father_pk || "0";
					let child_order_index = child.order_index;
					let child_page_title = child.page_title
					initialState.set(child_pk, {
						order_index : child_order_index,
						father_pk : child_father_pk,
						page_title : child_page_title
					});
				});

			});
		}).then(function() {
			let father_sort = new Sortable($(".page-container")[0], {
				group : 'menu',
				animation : 150,
				handle : 'h2',
				ghostClass : 'blue-background-class',
				fallbackOnBody : true,
				onEnd : function(e) {
					dragFather(e);
				}
			});
			let child_sort = $(".children").each(function() {
				new Sortable(this, {
					group : {
						name : 'page',
						pull : true,
						put : ['page']
					},
					animation : 150,
					draggable : '.child',
					ghostClass : 'blue-background-class',
					onStart:function(e){
						if($(e.from).children().length<2){
							fail_msg("必须保留一个！");
							return;
						}
					},
					onEnd : function(e) {
						dragChild(e);
					}
				});
			});
		});
	}
};

var ctx = new DataContext();
let isDataUnsaved = false;

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	window.addEventListener("beforeunload", function (event) {
		if(isDataUnsaved){
		      event.preventDefault();
		      event.returnValue = "";
		}
	});

});
let save = function(){
	$.layer({
		area : ['auto', 'auto'],
		dialog : {
			msg : '保存之后，重新登录生效。确定要保存修改吗？',
			btns : 2,
			type : 4,
			btn : ['确认', '取消'],
			yes : function(index) {
				
				const order = Array.from(updateOrder, ([pk, order_index]) => ({ pk, order_index }));
				const title = Array.from(updateTitle, ([pk, page_title]) => ({ pk, page_title }));
				const father_pk = Array.from(updateChildFatherPk, ([pk, father_pk]) => ({ pk, father_pk }));

				const json = {
				    order,
				    title,
				    father_pk
				};
				const data = "json="+JSON.stringify(json);
				console.log(data);
				 $.ajax({
					 type : "POST",
					 url : ctx.apiurl + 'system/updatePages',
					 data : data
				 }).success(function(str) {
					 if (str == "success") {
						layer.close(index);
						ctx.refresh();
						isDataUnsaved = false;
						showButton();
					 }
				 });
			}
		}
	});
}
let revoke = function(){
	$.layer({
		area : ['auto', 'auto'],
		dialog : {
			msg : '确认要撤回到初始状态吗？',
			btns : 2,
			type : 4,
			btn : ['确认', '取消'],
			yes : function(index) {
				layer.close(index);
				ctx.refresh();
				isDataUnsaved = false;
				showButton();
			}
		}
	});
}
let editFatherTitle = function(alink){
	const old_title = $(alink).prev().prev().text();
	const pk = $(alink).parent().attr("data-pk");
	$("#title-edit").find("input.title").val(old_title);
	$("#title-edit").find("input.pk").val(pk);
	titleLayer = $.layer({
		type : 1,
		title : ['备注', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['500px', '200px'],
		offset : ['', ''],
		scrollbar : true,
		page : {
			dom : '#title-edit'
		},
		end : function() {
			console.log("Done");
		}
	});
}
let confirmEditTitle = function(){
	const new_title = $("#title-edit").find("input.title").val().trim();
	if(new_title===""){
		fail_msg("名称不能为空!");
		return;
	}
	if(!isChinese(new_title)){
		fail_msg("名称只能是汉字！")
		return;
	}
	const pk = $("#title-edit").find("input.pk").val();
	let current_edit = $("h2,h3").filter('[data-pk="'+pk+'"]');
	current_edit.find("span.title").text(new_title);
	
	if(initialState.get(pk).page_title!==new_title){
		updateTitle.set(pk,new_title);
	}else{
		updateTitle.delete(pk);
	}
	layer.close(titleLayer);
	isDataUnsaved=checkUnsaved();
	showButton();
}
let editChildTitle = function(alink){
	const old_title = $(alink).prev().text();
	const pk = $(alink).parent().attr("data-pk");
	$("#title-edit").find("input.title").val(old_title);
	$("#title-edit").find("input.pk").val(pk);
	titleLayer = $.layer({
		type : 1,
		title : ['备注', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['500px', '200px'],
		offset : ['', ''],
		scrollbar : true,
		page : {
			dom : '#title-edit'
		},
		end : function() {
			console.log("Done");
		}
	});
}
let cancelEditTitle=function(){
	layer.close(titleLayer);
}

// 拖拽父标签
let dragFather = function(e) {
	let container = $(e.from);
	let from = Math.min(e.oldIndex, e.newIndex);
	let to = Math.max(e.oldIndex, e.newIndex);
	let children = container.children();
	for (let i = from; i <= to; i++) {
		let child = children[i];
		let new_index = i + 1;
		let pk = $(child).find("h2[data-pk]").attr("data-pk");
		$(child).find(".sort-num").text(new_index);
		if (initialState.get(pk).order_index !== new_index) {
			updateOrder.set(pk, new_index);
		} else {
			updateOrder.delete(pk);
		}
	}
	isDataUnsaved=checkUnsaved();
	showButton();
}
// 拖拽子标签
let dragChild = function(e){
	// 如果只是排序
	if(e.from===e.to){
		let from = Math.min(e.oldIndex, e.newIndex);
		let to = Math.max(e.oldIndex, e.newIndex);
		let container = $(e.from);
		let children = container.children();
		for (let i = from; i <= to; i++) {
			let child = children[i];
			let new_index = i + 1;
			let pk = $(child).find("h3[data-pk]").attr("data-pk");
			if (initialState.get(pk).order_index !== new_index) {
				updateOrder.set(pk, new_index);
			} else {
				updateOrder.delete(pk);
			}
		}
		isDataUnsaved = checkUnsaved();
		showButton();
	}
	// 如果拖拽到其他容器
	else{
		let item = $(e.item);
		let from_container = $(e.from);
		let to_container = $(e.to);
		let from_children = from_container.children();
		let to_children = to_container.children();
		for (let i = e.oldIndex; i < from_children.length; i++) {
			let child = from_children[i];
			let new_index = i + 1;
			let pk = $(child).find("h3[data-pk]").attr("data-pk");
			if (initialState.get(pk).order_index !== new_index) {
				updateOrder.set(pk, new_index);
			} else {
				updateOrder.delete(pk);
			}
		}
		
		for (let i = e.newIndex; i < to_children.length; i++) {
			let child = to_children[i];
			let new_index = i + 1;
			let pk = $(child).find("h3[data-pk]").attr("data-pk");
			if (initialState.get(pk).order_index !== new_index) {
				updateOrder.set(pk, new_index);
			} else {
				updateOrder.delete(pk);
			}
		}
		const new_father_pk = to_container.attr("data-pk");
		const item_pk = item.find("h3[data-pk]").attr("data-pk");
		if(initialState.get(item_pk).father_pk!==new_father_pk){
			updateChildFatherPk.set(item_pk,new_father_pk);
		}else{
			updateChildFatherPk.delete(item_pk);
		}
		
		isDataUnsaved = checkUnsaved();
		showButton();
	}
}
let checkUnsaved = function(){
	return updateOrder.size>0||updateTitle.size>0||updateChildFatherPk.size>0;
}

let showButton = function(){
	if(isDataUnsaved){
		$(".fixed").show();
	}else{
		$(".fixed").hide();
	}
}
