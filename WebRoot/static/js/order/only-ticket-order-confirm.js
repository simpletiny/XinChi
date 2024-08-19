var clientEmployeeLayer;
let confirmLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.employee = ko.observable({});
	self.order_pk = $("#key").val();
	self.passengers = ko.observableArray([{
		name_index : 1,
		chairman : 'Y'
	}]);
	self.ticket_infos = ko.observableArray([]);
	self.air_comment = ko.observable();
	self.confirm_date = ko.observable();
	self.current_date = $("#hidden-server-date").val();
	
	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.users(data.users);
	});

	var d = new Date(self.current_date);
	self.confirm_date(d.Format('yyyy-MM-dd'));

	$.getJSON(self.apiurl + 'order/searchTbcBnsOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.bnsOrder);
		if (data.passengers.length > 0) {
			self.passengers(data.passengers);
		}

		self.ticket_infos(data.ticketInfos);
		self.air_comment(self.ticket_infos()[0].comment);

		if(self.order().client_employee_pk){
			$.getJSON(self.apiurl + 'client/searchOneEmployee', {
				employee_pk : self.order().client_employee_pk
			}, function(data) {
				if (data.employee) {
					self.employee(data.employee);
				} else {
					fail_msg("员工不存在！");
				}
			}).fail(function(reason) {
				fail_msg(reason.responseText);
			});
		}
		
		reloadDatePicker();
		self.loadFiles();

	});
	self.loadFiles = function() {
		var stFileName = $("#txt-confirm-file");
		var fileName = $("#txt-confirm-file").val();
		if (!fileName.isEmpty()) {
			self.downFile(stFileName, fileName);
		}
	};

	self.downFile = function(stFileName, fileName) {
		var inputFile = $(stFileName).prev().find("input");
		var inputName = inputFile.attr("name");
		var imgContainer = $(inputFile).parent().parent().parent().next();
		var fileNameInput = stFileName;

		var formData = new FormData();
		formData.append("fileFileName", fileName);
		formData.append("fileType", "CLIENT_CONFIRM");
		formData.append("subFolder", self.order().create_user);

		var url = ctx.apiurl + 'file/getFileStream';
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.responseType = "blob";
		xhr.onload = function() {
			if (this.status == 200) {
				var blob = this.response;
				var deleteButton = $("<div class='delete'>删除</div>");

				var img = document.createElement("img");
				$("body").append(deleteButton);
				deleteButton.hide();
				deleteButton.click(function() {
					deleteImage(this, inputFile, img, fileNameInput, fileName);
				});
				deleteButton.mouseenter(function() {
					$(this).show();
				});
				img.onload = function(e) {
					window.URL.revokeObjectURL(img.src);
					if (img.width > initWidth) {
						img.height = initWidth * (img.height / img.width);
						img.width = initWidth;
					}

					$(img).mouseenter(function() {
						deleteButton.css("top", $(img).offset().top + img.height / 2 - 25);
						deleteButton.css("left", $(img).offset().left + img.width / 2 - 50);
						deleteButton.show();
					});
					$(img).mouseout(function() {
						deleteButton.hide();
					});
				};
				img.src = window.URL.createObjectURL(blob);

				if (inputName != "file6") {
					imgContainer.html(img);
				} else {
					imgContainer.append(img);
				}
			}
		};
		xhr.send(formData);
	};

	self.confirm_order = ko.observable({});
	self.updateOrder = function() {
		let result = validateOrder();
		if(result[0]!="SUCCESS"){
			fail_msg(result.join("<br>"));
			return;
		}
		let departure_date = $('[name="bnsOrder.departure_date"]').val();
		let days = $("#txt-days").val();
		let back_date =  new Date(new Date(departure_date).addDate(days-1)).Format("yyyy-MM-dd");
		let product_name = "单机票";
		let product_manager = $("#sel-product-manager option:selected").text();
		let adult_count = $("#people-count").val()-0;
		let special_count = $("#special-count").val()-0;
		let sum_count = adult_count+special_count;
		let client_employee = $("#txt-client-employee-name").val();
		
		self.confirm_order({departure_date,back_date,days,product_name,product_manager,adult_count,special_count,sum_count,client_employee})
		
		confirmLayer = $.layer({
			type : 1,
			title : ['订单确认', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['900px', '300px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#div-confirm'
			},
			end : function() {
				console.log("Done");
			}
		});
	}
	self.doConfirmOrder = function(){
		startLoadingIndicator("保存中");
		// 航班信息
		var info_table = $("#table-ticket tbody");
		var infos = $(info_table).children();
		let legs = new Array();
		for (let i = 0; i < infos.length; i++) {
			const tr = $(infos[i]);
			const index = tr.find("input[st='flight-index']").val().trim();
			const date = tr.find("input[st='date']").val().trim();
			const from_city = tr.find("input[st='from-city']").val().trim();
			const to_city = tr.find("input[st='to-city']").val().trim();
			let leg = {index,date,from_city,to_city};
			legs.push(leg);
		}
		
		var air_comment = $("#air-comment").val().trim();

		// 名单
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		let people = new Array();
		for (var i = 0; i < trs.length; i++) {
			const tr = trs[i];
			const chairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			const index = i + 1;
			const name = $(tr).find("[st='name']").val().trim();
			const sex = $(tr).find("[st='sex']").val();
			const age = $(tr).find("[st='age']").val().trim();
			const id_type = $(tr).find("[st='type']").val();
		
			const cellphone_A = $(tr).find("[st='cellphone_A']").val();
			const cellphone_B = $(tr).find("[st='cellphone_B']").val();
			const id = $(tr).find("[st='id']").val().trim();
			let person = {chairman,index,name,sex,age,cellphone_A,cellphone_B,id_type,id};
			people.push(person);
		}
		const info = {ticket_json:legs,name_json:people,air_comment:air_comment};
		const json = JSON.stringify(info);

		var data = $("form").serialize() + "&json=" + json;

		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/confirmOnlyTicketOrder',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				window.location.href = self.apiurl + "templates/order/tbc-order.jsp";
			} else if (str == "noenoughcredit") {
				fail_msg("信用额度不足，不能确认订单！");
			} else {
				fail_msg(str);
			}
		});
	}
	self.cancelConfirm = function(){
		layer.close(confirmLayer);
	}
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
	
	self.clientEmployees = ko.observable({});
	self.refreshClient = function() {
		startLoadingSimpleIndicator("加载中……");
		var param = "employee.name=" + $("#client_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(data) {
			self.clientEmployees(data.employees);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			
			endLoadingIndicator();
		});
	};

	self.searchClientEmployee = function() {
		self.refreshClient();
	};

	self.choseClientEmployee = function() {
		$("#txt-client-employee-name").blur();
		clientEmployeeLayer = $.layer({
			type : 1,
			title : ['选择客户操作', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#client-pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.pickClientEmployee = function(data) {
		$("#txt-client-employee-name").val(data.name);
		$("#txt-client-employee-pk").val(data.pk);
		$("#txt-financial-body-name").text(data.financial_body_name);
		layer.close(clientEmployeeLayer);
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
		self.searchClientEmployee();
	};
	
	// end pagination

};

var ctx = new OrderContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		changeFile(this);
	});

	var x = new Date(ctx.current_date);
	var maxDate = x.Format("yyyy/MM/dd");
	var minDate = x.addDate(-2).Format("yyyy/MM/dd");
	$(".date-picker-confirm-date").datetimepicker({
		format : 'Y-m-d',
		timepicker : false,
		scrollInput : false,
		defaultDate : new Date(),
		lang : 'zh',

		minDate : minDate,
		maxDate : maxDate,
	})
	
	let a_btn = $(`<a type="submit"
	class="btn btn-green btn-r" onclick="checkName()">名单校验</a>`);
	$("#div-btn-area").prepend(a_btn);
});

function validateOrder(){
	startLoadingSimpleIndicator("校验订单");
	let result = new Array();
	if (!$("form").valid()) {
		result.push("请填写非空项！");
	}
	
	var other_cost = $(":input[name='bnsOrder.other_cost']").val().trim();
	var other_comment = $(":input[name='bnsOrder.other_cost_comment']").val().trim();
	if (other_cost != "" && other_comment == "") {
		result.push("有其他费用，必须填写费用说明！")
	}
	
	// 航班信息
	var info_table = $("#table-ticket tbody");
	var infos = $(info_table).children();
	let legs = new Array();
	for (let i = 0; i < infos.length; i++) {
		const tr = $(infos[i]);
		const date = tr.find("input[st='date']").val().trim();
		const from_city = tr.find("input[st='from-city']").val().trim();
		const to_city = tr.find("input[st='to-city']").val().trim();
		// 判断是否有航班信息
		if (date == '' || from_city == '' || to_city == '') {
			result.push("请填写航班信息！")
		}
	}


	var x = new Date($("#hidden-server-date").val());
	var maxDate = new Date(x.Format("yyyy-MM-dd"));
	var minDate = new Date(x.addDate(-2).Format("yyyy-MM-dd"));
	var confirm_date = new Date($(".date-picker-confirm-date").val());
	if (confirm_date - maxDate > 0 || confirm_date - minDate < 0) {
		result.push("请选择允许的时间范围:" + minDate + "至" + maxDate+"！");
	}
	var confirm_file = $("#txt-confirm-file").val();
	if (confirm_file == "") {
		result.push("请上传确认件！");
	}
	var hasNames = false;
	var hasChairman = false;
	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	let not_ok_names = new Array();
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var chairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
		var name = $(tr).find("[st='name']").val().trim();
		var id = $(tr).find("[st='id']").val().trim();
		let is_ok = $(tr).find("[st='is_ok']").val();
		if(is_ok!='Y'){
			not_ok_names.push(name);
		}
		if (name != "" && id != "" && !hasNames) {
			hasNames = true;
		}

		if (chairman == "Y") {
			hasChairman = true;
		}
	}

	if (!hasNames) {
		result.push("没有名单，不能确认！");
	}

	if (!hasChairman) {
		result.push("请指定团长！");
	}

	if(not_ok_names.length>0){
		result.push(not_ok_names.join(",")+"未通过验证！");
	}
	
	if(result.length==0)
		result.push("SUCCESS");
	endLoadingIndicator();
	return result;
}