var passengerCheckLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.supplierEmployees = ko.observable({});
	self.order_number = $("#txt-order-number").val();
	self.supplier_employee_pk = $("#txt-supplier-employee-pk").val();
	self.operation_pk =$("#txt-operation-pk").val();
	self.order = ko.observable({});

	self.adult_count = ko.observable();
	self.special_count = ko.observable();
	
	let ticketStatusMap = {
			"A" : "完全出票",
			"B" : "部分出票",
			"C" : "有未发送",
			"D" : "未出票",
			"NO" : "状态查询失败"
		}
	
	// 产品包含的供应商信息
	self.ticket_infos = ko.observableArray([]);
	self.ticket_status = ko.observable();
	self.airTickets = ko.observableArray([]);
	self.sale_orders = ko.observableArray([]);
	self.product_order = ko.observable({});
	
	
	self.order_supplier = ko.observable({infos: []});
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中")
		var param = "product_order_number=" + self.order_number+"&supplier_employee_pk="+self.supplier_employee_pk;
		const supplierPromise = $.ajax({
			type : "GET",
			url : self.apiurl + 'product/searchOneOrderSupplier',
			data : param
		}).success(function(data) {
			self.order_supplier(data.order_supplier);
		});
		
		const dataPromise=$.ajax({
			type : "GET",
			url : self.apiurl + 'product/searchProductDataForOrderByOrderNumber',
			data : "product_order_number=" + self.order_number
		}).success(function(data) {
			self.product_order(data.product_order);
			self.adult_count(data.adult_count);
			self.special_count(data.special_count);
			self.ticket_infos(data.ticket_infos);
		});
			
		
		const statusPromise= $.ajax({
			type : "GET",
			url : self.apiurl + 'product/searchProductOrderTicketStatusByOrderNumber',
			data : "product_order_number=" + self.order_number
		}).success(function(str) {
			self.ticket_status(ticketStatusMap[str]);
		});
		
		const orderPromise= $.ajax({
			type : "GET",
			url : self.apiurl + 'product/searchSaleOrderWithNames',
			data : "product_order_number=" + self.order_number
		}).success(function(data) {
			let new_orders = new Array();
			data.orders.forEach(function(data,index){
				let new_order = data;
				new_order.contact_way = "";
				data.name_list.forEach(function(name,name_index){
					if(name.cellphone_A!=""||name.cellphone_B!=""){
						new_order.contact_way+= name.name+":"+name.cellphone_A+";"+name.cellphone_B+"\n";
					}
				});
				new_orders.push(new_order);
			})
			self.sale_orders(new_orders);
		});
		
		const ticketPromise=$.getJSON(self.apiurl + 'product/searchProductAirTicketInfoByProductOrderNumber', {
			product_order_number : self.order_number
		}, function(data) {
			self.airTickets(data.air_tickets);
		})
		
		Promise.all([supplierPromise,dataPromise, statusPromise, orderPromise,ticketPromise])
        .then(function() {
        	adjustTextareaHeight();
            endLoadingIndicator();
        }).catch(function(error) {
            console.error('一个或多个请求失败', error);
            endLoadingIndicator();
        });
	}
	// 保存地接维护信息
	self.finalOrderOperation = function() {
		 if (!self.checkForm()) {
			 return;
		 }
		startLoadingIndicator("保存中...");
		var json = self.getJson();
		var data = "json=" + json;
		 $.ajax({
			 type : "POST",
			 url : self.apiurl + 'product/finalOperation',
			 data : data
			 }).success(function(str) {
				 endLoadingIndicator();
				 if (str == "success") {
					 window.location.href = self.apiurl + "templates/product/product-order-operated.jsp";
				 } else {
					 fail_msg(str);
				 }
		 });
	}
	// 获取页面json
	self.getJson = function() {
		let jsonObj = {};
		jsonObj.operation_pk = self.operation_pk;
		jsonObj.final_supplier_cost = $("#final-cost").val().trim();
		let order_trs = $("#table-name").find("tbody").children();
		let order_infos = {};
		order_trs.each(function(index,tr){
			let order_name_pk = $(tr).find("input[st='name-pk']").val();
			let final_price = $(tr).find("input[st='price']").val();
			let	team_number = $(tr).find("input[st='team-number']").val();
			let	order_supplier_sale_order_pk = $(tr).find("input[st='sale-order-pk']").val();
			let txt_final_payable_comment = $(tr).find("textarea[st='final-payable-comment']");
			if(txt_final_payable_comment.length>0){
				final_payable_comment =  encodeURIComponent($(tr).find("textarea[st='final-payable-comment']").val().trim());
				final_comment =  encodeURIComponent($(tr).find("textarea[st='final-comment']").val().trim());
			}
			if (!order_infos[team_number]) {
				order_infos[team_number] = {
					order_supplier_sale_order_pk:order_supplier_sale_order_pk,
		            team_number: team_number,
		            final_payable_comment:final_payable_comment,
		            final_comment:final_comment,
		            names: [] 
		        };
			   }
			order_infos[team_number].names.push({
					order_name_pk,
			 		final_price
			    });
		});
		
		Object.entries(order_infos).forEach(([key, value]) => {
				let team_payable = 0;
			  	value.names.forEach(function(item, index) {
			  	  	team_payable += item.final_price-0;
			  	});
			  	value.team_payable = team_payable;
			});
		jsonObj.orders = Object.values(order_infos);
		
		return JSON.stringify(jsonObj);
	}
	// 查验表单
	self.checkForm = function() {
		var all = $("#div-supplier").find("input.required");
		var result = true;
		for (var i = 0; i < all.length; i++) {
			var current = all[i];
			if ($(current).val().trim() == "") {
				$(current).css("background", "red");
				result = false;
			}
		}
		if (!result) {
			setTimeout(function() {
				$(all).removeAttr("style");
				fail_msg("请填写不能为空的项目！");
			}, 500);
			return result;
		}
		// 检查金额是否合账
		let total = caculateCost();
		let final_cost = $("#final-cost").val().trim()-0;
		if(total!==final_cost){
			fail_msg("决算团款总计：￥" + final_cost + "与决算价格合计：￥" + total + "不符！");
			return false;
		}
		return result;
	}
};

var ctx = new ProductContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

/**
 * 计算供应商成本合计
 */
function caculateCost() {
	const table = $("#table-name");
	let all_prices = table.find("tbody").find("input[st='price']");
	let total = all_prices.toArray().reduce((sum, el) => sum + (+el.value || 0), 0);
	$("#final-cost").val(total);
	return total;
}

function adjustTextareaHeight() {
    // 获取 textarea 和父 td
    const textareas = $(".td-textarea");
    textareas.each(function(index,area){
    	let td = $(area).parent();
    	$(area).height(td.height()-9);
    })
}

function editAll(txt){
	let table = $("#table-name");
	let val_price = $(txt).val().trim();
	 if (!/^-?\d*$/.test(val_price)) {
         val_price = val_price.slice(0, -1);
         $(txt).val(val_price);
     }
	let all_flg = table.find("thead").find("input[st='chk-edit-all']").is(":checked");
	if(all_flg){
		let all_prices = table.find("tbody").find("input[st='price']");
		all_prices.each(function(index,now_txt){
			$(now_txt).val(val_price);
		});
	}
	caculateCost();
}