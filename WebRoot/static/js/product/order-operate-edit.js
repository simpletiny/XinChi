var passengerCheckLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.supplierEmployees = ko.observable({});
	self.order_number = $("#order_number").val();
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
	
	
	self.order_suppliers = ko.observableArray([]);
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中")
		var param = "product_order_number=" + self.order_number;
		const supplierPromise = $.ajax({
			type : "GET",
			url : self.apiurl + 'product/searchOrderSuppliersByOrderNumber',
			data : "product_order_number=" + self.order_number
		}).success(function(data) {
			self.order_suppliers(data.order_suppliers);
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
        	initializeDatePicker();
            endLoadingIndicator();
        }).catch(function(error) {
            console.error('一个或多个请求失败', error);
            endLoadingIndicator();
        });
	}

	self.isD = function(t) {
		if (t == '0')
			return true
		else
			return false;
	}

	// 保存地接维护信息
	self.updateOrderOperation = function() {
		 if (!self.checkForm()) {
			 return;
		 }
		startLoadingIndicator("保存中...");
		var json = self.getJson();
		var data = "json=" + json;
		 $.ajax({
			 type : "POST",
			 url : self.apiurl + 'product/udpateOrderOperation',
			 data : data
			 }).success(function(str) {
				 endLoadingIndicator();
				 if (str == "success") {
					 window.location.href = self.apiurl + "templates/product/product-order-operating.jsp";
				 } else {
					 fail_msg(str);
				 }
		 });
	}
	// 获取页面json
	self.getJson = function() {
		
		let order_number = self.order_number;

		var max_div = $("#div-supplier");
		var all_divs = $(max_div).children();
		let supplier_infos = new Array();
		for (var i = 0; i < all_divs.length; i++) {
			const current_div = all_divs[i];
			const supplier_pk = $(current_div).find('input[st="supplier-pk"]').val();
			const supplier_product_name = $(current_div).find('input[st="supplier-product-name"]').val();
			const supplier_product_days = $(current_div).find('input[st="supplier-product-days"]').val().trim();
			const pick_date = $(current_div).find("input[st='pick-date']").val().trim();
			const supplier_cost = $(current_div).find('input[st="supplier-cost"]').val().trim();
			const treat_comment= $(current_div).find('textarea[st="treat-comment"]').val().trim();
			const payable_comment = $(current_div).find('textarea[st="payable-comment"]').val().trim();

			let tourist_info = '';
			$(current_div).find('input[name="chk_tourist"]checked').each(function(i) {
				tourist_info += $(this).val() + ";";
			});

			var confirm_file_templet = $(current_div).find('input[st="confirm-file-templet"]').val();
			const supplier_index = i;
		
			// 航段信息
			var tbody = $(current_div).find('.table-supplier').find("tbody");
			var trs = $(tbody).children();
			let leg_infos = new Array();
			for (j = 0; j < trs.length - 1;) {
				if ((j + 1) % 3 == 0) {
					j++;
					continue;
				}
				var tr_min = trs[j];
				var tr_add = trs[j + 1];
				var info_index = j / 3;
				var pick_type = $(tr_min).find(':radio[name^="radio-jie"]:checked').val();
				var pick_leg, pick_other;
				if (pick_type == "0") {
					pick_leg = $(tr_min).find('input[st="txt-jie-type-0"]').val();
					pick_other = "";
				} else {
					pick_other = $(tr_min).find('input[st="txt-jie-type-1"]').val();
					pick_leg = "";
				}
				var pick_day = $(tr_min).find('input[st="day"]').val();
				var pick_traffic = $(tr_min).find('input[st="traffic-tool"]').val();
				var pick_time = $(tr_min).find('input[st="time"]').val();
				var pick_city = $(tr_min).find('input[st="city"]').val();
				var pick_place = $(tr_min).find('input[st="place"]').val();

				var send_type = $(tr_add).find(':radio[name^="radio-song"]:checked').val();
				var send_leg, send_other;
				if (send_type == "0") {
					send_leg = $(tr_add).find('input[st="txt-song-type-0"]').val();
					send_other = "";
				} else {
					send_other = $(tr_add).find('input[st="txt-song-type-1"]').val();
					send_leg = "";
				}
				var send_day = $(tr_add).find('input[st="day"]').val();
				var send_traffic = $(tr_add).find('input[st="traffic-tool"]').val();
				var send_time = $(tr_add).find('input[st="time"]').val();
				var send_city = $(tr_add).find('input[st="city"]').val();
				var send_place = $(tr_add).find('input[st="place"]').val();
				let leg_info = {
					    info_index,
					    pick_type,
					    pick_leg,
					    pick_other,
					    pick_day,
					    pick_traffic,
					    pick_time,
					    pick_city,
					    pick_place,
					    send_type,
					    send_leg,
					    send_other,
					    send_day,
					    send_traffic,
					    send_time,
					    send_city,
					    send_place
					};
				leg_infos.push(leg_info);
				j = j + 2;
			}
			// 名单信息
			var order_trs = $(current_div).find('.table-order').find("tbody").children();
			let	contact_way,sale_receivable_comment,hotel_comment,sale_treat_comment;
			let order_info ={};
			order_trs.each(function(index,tr){
				let name_pk = $(tr).find("input[st='name-pk']").val();
				let price = $(tr).find("input[st='price']").val();
				let	team_number = $(tr).find("input[st='team-number']").val();
				let txt_contact_way = $(tr).find("textarea[st='contact-way']");
				if(txt_contact_way.length>0){
					contact_way =  $(tr).find("textarea[st='contact-way']").val().trim();
					sale_receivable_comment =  $(tr).find("textarea[st='receivable-comment']").val().trim();
					hotel_comment =  $(tr).find("textarea[st='hotel-comment']").val().trim();
					sale_treat_comment =  $(tr).find("textarea[st='treat-comment']").val().trim();
				}
				 if (!order_info[team_number]) {
					 order_info[team_number] = { 
				            team_number: team_number,
				            contact_way:contact_way,
				            sale_receivable_comment:sale_receivable_comment,
				            hotel_comment:hotel_comment,
				            sale_treat_comment:sale_treat_comment,
				            name_infos: [] 
				        };
				    }
				 order_info[team_number].name_infos.push({
				 		name_pk,
				 		price
				    });
			});
			let order_infos = Object.values(order_info);
			let supplier_info = {supplier_index,supplier_pk,supplier_product_name,supplier_product_days,pick_date,supplier_cost,tourist_info,confirm_file_templet,treat_comment,payable_comment,leg_infos,order_infos};
			supplier_infos.push(supplier_info);
		}
		let result = {order_number,supplier_infos};
		
		return JSON.stringify(result);
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

		// 查验是否添加了相同地接社
		var max_div = $("#div-supplier");
		var all_divs = $(max_div).children();
		var pks = new Array();
		for (var i = 0; i < all_divs.length; i++) {
			var current_div = all_divs[i];

			var supplier_pk = $(current_div).find('input[st="supplier-pk"]').val();
			pks.push(supplier_pk);
		}

		if (pks.isRepeat()) {
			fail_msg("不能添加相同的供应商！")
			return false;
		}

		// 检查金额是否合账
		var max_div = $("#div-supplier");
		var all_divs = $(max_div).children();
		for (var i = 0; i < all_divs.length; i++) {
			var current_div = all_divs[i];
			var supplier_cost = $(current_div).find('input[st="supplier-cost"]').val() - 0;
			let all_txt_prices = $(current_div).find("input[st='price']");
			let sum_money = 0;
			all_txt_prices.each(function(index,txt){
				sum_money += ($(txt).val()-0);
			});
			if (supplier_cost != sum_money) {
				fail_msg("地接操作" + (i + 1) + "的团款总计：￥" + supplier_cost + "与单价合计：￥" + sum_money + "不符！");
				return false;
			}
		}
		return result;
	}

	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param, function(data) {
			self.supplierEmployees(data.employees);

			self.totalCount(Math.round(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	self.searchSupplierEmployee = function() {
		self.refreshSupplier();
	};
	self.pickSupplierEmployee = function(name, pk) {
		$(currentSupplier).val(name);
		$(currentSupplier).next().val(pk);
		layer.close(supplierEmployeeLayer);
	};

	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(team_number) {
		self.passengers.removeAll();

		var url = "order/selectSaleOrderNameListByTeamNumber";

		$.getJSON(self.apiurl + url, {
			team_number : team_number
		}, function(data) {
			self.passengers(data.passengers);
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['游客信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#passengers-check'
				},
				end : function() {
				}
			});
		});
	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 10;
	self.pageNums = ko.observableArray();
	self.totalCount = ko.observable(1);
	self.startIndex = ko.computed(function() {
		return (self.currentPage() - 1) * self.perPage;
	});

	self.resetPage = function() {
		self.currentPage(1);
	};

	self.previousPage = function() {
		if (self.currentPage() > 1) {
			self.currentPage(self.currentPage() - 1);
			self.refreshPage();
		}
	};

	self.nextPage = function() {
		if (self.currentPage() < self.pageNums().length) {
			self.currentPage(self.currentPage() + 1);
			self.refreshPage();
		}
	};

	self.turnPage = function(pageIndex) {
		self.currentPage(pageIndex);
		self.refreshPage();
	};

	self.setPageNums = function(curPage) {
		var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self.totalCount();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.searchSupplierEmployee();
	};
	// end pagination
};

var ctx = new ProductContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
var currentSupplier;
var supplierEmployeeLayer;
function choseSupplierEmployee(event) {
	supplierEmployeeLayer = $.layer({
		type : 1,
		title : ['选择供应商操作', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['600px', '650px'],
		offset : ['50px', ''],
		scrollbar : true,
		page : {
			dom : '#supplier-pick'
		},
		end : function() {
			console.log("Done");
		}
	});

	currentSupplier = event.target;
	$(currentSupplier).blur();
}
function addRow(btn) {
	var tr_current = $(btn).parent().parent();
	let tbody = $(tr_current).parent();
	let length = tbody.find("[st='radio-jie-0']").length;
	if(length==3){
		fail_msg("最多三组接送！");
		return;
	}
	
	var timestamp = (new Date()).getTime();
	let tr_min = $(`<tr>
						<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
						<td class="r">接：</td>
						<td><input name="radio-jie-${timestamp}" checked st="radio-jie-0" type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
						<td><input type="text" maxlength="1" st="txt-jie-type-0" oninput="fillFlight()" /></td>
						<td><input name="radio-jie-${timestamp}" st="radio-jie-1" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
						<td><input type="text" maxlength="10" st="txt-jie-type-1" disabled/></td>
						<td><input class="required" type="number" maxlength="2" st="day"/></td>
						<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
						<td><input class="required" type="text" maxlength="15" st="time" /></td>
						<td><input class="required" type="text" maxlength="15" st="city" /></td>
						<td><input class="required" type="text" maxlength="30" st="place" /></td>
					</tr>`);
	let tr_add =$(`<tr>
						<td><input type="button" value="+" onclick="addRow(this)" /></td>
						<td class="r">送：</td>
						<td><input name="radio-song-${timestamp}" checked st="radio-song-0" type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
						<td><input type="text" maxlength="1" st="txt-song-type-0" oninput="fillFlight()" /></td>
						<td><input st="radio-song-1" name="radio-song-${timestamp}" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
						<td><input type="text" maxlength="10" st="txt-song-type-1" disabled/></td>
						<td><input class="required" type="number" maxlength="2" st="day" /></td>
						<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
						<td><input class="required" type="text" maxlength="15" st="time" /></td>
						<td><input class="required" type="text" maxlength="15" st="city" /></td>
						<td><input class="required" type="text" maxlength="30" st="place" /></td>
					</tr>`);
	var tr_line = $('<tr>'
			+ '<td colspan="11"><hr style="width: 100%; text-align: center; vertical-align: middle" /></td>' + '</tr>');
	tr_current.after(tr_line);
	tr_line.after(tr_min);
	tr_min.after(tr_add);
}

function deleteRow(btn) {
	$.layer({
		area : ['auto', 'auto'],
		dialog : {
			msg : "确认要删除当前接送组吗？",
			btns : 2,
			type : 4,
			btn : ['确认', '取消'],
			yes : function(index) {
				layer.close(index);
				var tr_min = $(btn).parent().parent();
				var tr_add = tr_min.next();
				var tr_line = tr_min.prev();
				var tbody = tr_min.parent();
				var index = $(tbody).children().length;
				if (index > 2) {
					tr_min.remove();
					tr_add.remove();
					tr_line.remove();
				}else{
					fail_msg("至少保留一组接送组！");
				}
			}
		}
	});
}

function addSupplier() {
	if($("#div-supplier").children().length==5){
		fail_msg("最多有5个地接操作！");
		return;
	}
	var timestamp = (new Date()).getTime();
	
	var div_supplier = $(`<div>
							<h3>地接信息</h3>
							<div class="input-row clearfloat">
						    <div class="col-md-3 required">
						        <label class="l">地接操作</label>
						        <div class="fix-width1">
						            <input type="text" class="ip- required" st="supplier-name" onclick="choseSupplierEmployee(event)" />
						            <input type="text" class="need" st="supplier-pk" style="display: none" />
						        </div>
						    </div>
						    <div class="col-md-3 required">
						        <label class="l">产品名称</label>
						        <div class="fix-width1">
						            <input type="text" class="ip- required" st="supplier-product-name" />
						        </div>
						    </div>
						</div>
						<div class="input-row clearfloat">
						    <div class="col-md-3 required">
						        <label class="l">接团日期</label>
						        <div class="fix-width1">
						            <input type="text" class="ip- required date-picker" st="pick-date" />
						        </div>
						    </div>
						    <div class="col-md-3 required">
						        <label class="l">天数</label>
						        <div class="ip" style="width: 50% !important">
						            <input type="number" class="ip- required" st="supplier-product-days" />
						        </div>
						    </div>
						    <div class="col-md-3 required">
						        <label class="l">团款总计</label>
						        <div class="ip" style="width: 50% !important">
						            <input type="number" class="ip- required" st="supplier-cost" />
						        </div>
						    </div>
						</div>
						<div class="input-row clearfloat">
						    <div class="col-md-4">
						        <label class="l">接待说明</label>
						        <div class="ip">
						            <textarea type="text" class="ip-default" rows="5" st="treat-comment" maxlength="200" placeholder="接待说明"></textarea>
						        </div>
						    </div>
						    <div class="col-md-4">
						        <label class="l">团款说明</label>
						        <div class="ip">
						            <textarea type="text" class="ip-default" rows="5" st="payable-comment" maxlength="200" placeholder="团款说明"></textarea>
						        </div>
						    </div>
						</div>
						<div style="margin-top: 20px; padding-left: 70px">
						    <table style="width: 90%" class="table-supplier">
						        <thead>
						            <tr class="required">
						                <th style="width: 5%"></th>
						                <th style="width: 5%"></th>
						                <th style="width: 5%"></th>
						                <th style="width: 10%"></th>
						                <th style="width: 5%"></th>
						                <th style="width: 10%"></th>
						                <th class="r" style="width: 10%">天次</th>
						                <th class="r" style="width: 10%">交通工具</th>
						                <th class="r" style="width: 10%">抵离时间</th>
						                <th class="r" style="width: 10%">抵离城市</th>
						                <th class="r" style="width: 10%">抵离地点</th>
						            </tr>
						        </thead>
						        <tbody st="t-body">
						            <tr>
										<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
										<td class="r">接：</td>
										<td><input name="radio-jie-${timestamp}" checked st="radio-jie-0" type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
										<td><input type="text" maxlength="1" st="txt-jie-type-0" oninput="fillFlight()" /></td>
										<td><input name="radio-jie-${timestamp}" st="radio-jie-1" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-jie-type-1" disabled/></td>
										<td><input class="required" type="number" maxlength="2" st="day"/></td>
										<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
										<td><input class="required" type="text" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" maxlength="30" st="place" /></td>
									</tr>
						            <tr>
										<td><input type="button" value="+" onclick="addRow(this)" /></td>
										<td class="r">送：</td>
										<td><input name="radio-song-${timestamp}" checked st="radio-song-0" type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
										<td><input type="text" maxlength="1" st="txt-song-type-0" oninput="fillFlight()" /></td>
										<td><input st="radio-song-1" name="radio-song-${timestamp}" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-song-type-1" disabled/></td>
										<td><input class="required" type="number" maxlength="2" st="day" /></td>
										<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
										<td><input class="required" type="text" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" maxlength="30" st="place" /></td>
									</tr>
						        </tbody>
						    </table>
						</div>
						<div class="input-row clearfloat">
						    <div class="col-md-6">
						        <label class="l" style="width: 70px !important">游客信息：</label>
						        <div class="ip">
						            <div style="padding-top: 4px;">
						                <em class="small-box">
						                    <input type="checkbox" checked name="chk_tourist" value="name" /><label>姓名</label>
						                    <input type="checkbox" checked name="chk_tourist" value="sex" /><label>性别</label>
						                    <input type="checkbox" name="chk_tourist" value="age" /><label>年龄</label>
						                    <input type="checkbox" name="chk_tourist" value="id" /><label>身份证号码</label>
						                    <input type="checkbox" checked name="chk_tourist" value="tel" /><label>联系方式</label>
						                    <input type="checkbox" name="chk_tourist" value="room_group" /><label>分房组</label>
						                </em>
						            </div>
						        </div>
						    </div>
						    <div class="col-md-3">
						        <div class="ip">
						            <a href="javascript:;" class="a-upload">上传确认件<input type="file" onchange="changeFile(this)"/></a>
						            <input type="hidden" st="confirm-file-templet"/>
						            <span style="color: blue">默认模板</span>
						        </div>
						    </div>
						</div>
						<hr />
						<div class="input-row clearfloat table-order-here"></div>
						<hr class="hr-big-dash" />
						</div>`);
	
	
	
	let table = $("#div-table-order").children().clone(true);
	$(div_supplier).find(".table-order-here").append(table);
	$('#div-supplier').append(div_supplier);
	initializeDatePicker();
	adjustTextareaHeight();
}
function deleteSupplier() {
	$.layer({
		area : ['auto', 'auto'],
		dialog : {
			msg : "确认要删除最后一个地接操作吗？",
			btns : 2,
			type : 4,
			btn : ['确认', '取消'],
			yes : function(index) {
				layer.close(index);
				var div_suppliers = $("#div-supplier");
				var children = div_suppliers.children();
				var index = children.length;
				if (index > 1) {
					children[index - 1].remove();
				}
			}
		}
	});
}
function changeJieSongType(ra) {
	var v = $(ra).val();
	var tr = $(ra).parent().parent();
	var txt0 = $(tr).find(':input[st^="txt-"][st$="type-0"]');
	var txt1 = $(tr).find(':input[st^="txt-"][st$="type-1"]');
	if (v == "0") {
		$(txt0).attr("disabled", false);
		$(txt0).addClass("required");
		$(txt1).attr("disabled", true);
		$(txt1).removeClass("required");
	} else {
		$(txt1).attr("disabled", false);
		$(txt1).addClass("required");
		$(txt0).attr("disabled", true);
		$(txt0).removeClass("required");
	}
}
/**
 * 计算供应商成本合计
 */
function caculateOtherCost() {
	var tbody = $("#table-supplier tbody");
	var trs = $(tbody).children();
	var sum = 0;
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var supplierCost = $(tr).find("[st='supplier-cost']").val();
		sum += supplierCost - 0;
	}

	$("#local-adult-cost").val(sum);
	caculateGrossProfit();
}
let ticketLayer;
let currentFill;
function fillFlight() {
	let txt = event.target;
	currentFill = txt;
	let txt_value = $(txt).val().trim().toUpperCase();
	if (isLetter(txt_value)) {
		$(txt).val(txt_value);
	} else {
		$(txt).val(txt_value.slice(0, -1));
	}
	
	$(txt).blur();
	let flight_info;
	let air_info;

	for (const info of ctx.airTickets()) {
		if (txt_value === alphabetMap[info.ticket_index]) {
			flight_info = info;
			flight_info.leg = txt_value;
		}
	}
	for (let i = 0;i<ctx.ticket_infos().length;i++) {
		if (txt_value === alphabetMap[i+1]) {
			air_info = ctx.ticket_infos()[i];
			air_info.leg = alphabetMap[i+1];
		}
	}
	
	if(flight_info&&air_info){
		const departure_date = ctx.product_order().departure_date;
		air_info.day_index = dateDiff(new Date(departure_date),new Date(air_info.ticket_date)).replace("天","")-0+1;
		flight_info.ticket_date = new Date(departure_date).addDate(flight_info.start_day-1).Format('yyyy-MM-dd');
		showTicketLayer(flight_info,air_info);
	}else{
		if(flight_info){
			let a, b, c = '';
			if (flight_info) {
				a = flight_info.start_day
				b = '飞机';
				c = flight_info.start_city + '--' + flight_info.end_city;
			}
			let tr = $(txt).parent().parent();
			$(tr).find(':input[st="day"]').val(a);
			$(tr).find(':input[st="traffic-tool"]').val(b);
			$(tr).find(':input[st="city"]').val(c);
		}else if(air_info){
			const departure_date = ctx.product_order().departure_date;
			air_info.day_index = dateDiff(new Date(departure_date),new Date(air_info.ticket_date)).replace("天","")-0+1;
			let tr = $(txt).parent().parent();
			$(tr).find(':input[st="day"]').val(air_info.day_index);
			$(tr).find(':input[st="traffic-tool"]').val("飞机");
			$(tr).find(':input[st="time"]').val(air_info.from_to_time);
			$(tr).find(':input[st="city"]').val(air_info.from_to_city);
			$(tr).find(':input[st="place"]').val(air_info.from_airport+"--"+air_info.to_airport);
		}
	}
}
let showTicketLayer = function(flight_info,air_info){
	$("#ticket-pick").html('');
let table_html = `<table style="width: 100%" class="table table-striped table-hover">
				<thead>
				</thead>
				<tbody style="cursor:pointer">
				</tbody>
			</table>`;
let table = $(table_html);
let th = `<tr>
				<th>来源</th>
				<th>航段</th>
				<th>日期</th>
				<th>城市对</th>
				<th>航班号</th>
				<th>航班时刻</th>
				<th>起飞机场</th>
				<th>降落机场</th>
			</tr>`;

let thead = table.find("thead");
thead.append(th);

let tbody = table.find("tbody");
let tr1 = $(`<tr ondblclick="fillFlightInfo()">
		<input type="hidden" value="${air_info.day_index};${air_info.leg};${air_info.ticket_date};${air_info.from_to_city};${air_info.ticket_number};${air_info.from_to_time};${air_info.from_airport};${air_info.to_airport}" />
		<td>票务</td>
		<td>${air_info.leg}</td>
		<td>${air_info.ticket_date}</td>
		<td>${air_info.from_to_city}</td>
		<td>${air_info.ticket_number}</td>
		<td>${air_info.from_to_time}</td>
		<td>${air_info.from_airport}</td>
		<td>${air_info.to_airport}</td>
	</tr>`);
let tr2 = $(`<tr ondblclick="fillFlightInfo()">
						<input type="hidden" value="${flight_info.start_day};${flight_info.leg};${flight_info.ticket_date};${flight_info.start_city + '--' + flight_info.end_city};${flight_info.flight_number==null?"":flight_info.flight_number};;;" />
						<td>维护</td>
						<td>${flight_info.leg}</td>
						<td>${flight_info.ticket_date}</td>
						<td>${flight_info.start_city + '--' + flight_info.end_city}</td>
						<td>${flight_info.flight_number==null?"":flight_info.flight_number}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>`);

tbody.append(tr1);
tbody.append(tr2);

$("#ticket-pick").append(table);
ticketLayer = $.layer({
		type : 1,
		title : ['双击选择机票信息', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['1000px', '400px'],
		offset : ['', ''],
		scrollbar : true,
		page : {
			dom : '#ticket-pick'
		},
		end : function() {
			console.log("Done");
		}
	});
}
let fillFlightInfo = function(){
	let tr = $(event.target).parent();
	let txt = tr.find("input").val();
	let data = txt.split(";");
	let fill_tr = $(currentFill).parent().parent();
	$(fill_tr).find(':input[st="day"]').val(data[0]);
	$(fill_tr).find(':input[st="traffic-tool"]').val("飞机");
	$(fill_tr).find(':input[st="time"]').val(data[5]);
	$(fill_tr).find(':input[st="city"]').val(data[3]);
	$(fill_tr).find(':input[st="place"]').val(data[6]+"--"+data[7]);
	layer.close(ticketLayer);
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
	let table = $(txt).parent().parent().parent().parent();
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
}