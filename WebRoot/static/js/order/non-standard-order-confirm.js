var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.employee = ko.observable({});
	self.order_pk = $("#key").val();
	self.current_date = $("#hidden-server-date").val();

	self.confirm_date = ko.observable();
	self.independent_msg = ko.observable();
	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.users(data.users);
	});

	self.passengers = ko.observableArray([{
		name_index : 1,
		chairman : 'Y'
	}]);

	var x = new Date(self.current_date);
	var year_now = x.getFullYear();

	self.confirm_date(self.current_date);
	$.getJSON(self.apiurl + 'order/searchTbcBnsOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.bnsOrder);

		if (data.passengers.length > 0) {
			self.passengers(data.passengers);
		}

		if (self.order().name_list_lock == '1')
			$("#txt-name-list").disabled();

		if (self.order().independent_flg == 'Y') {
			self.independent_msg("（独立团）");
		}
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

	self.updateOrder = function() {
		if (!$("form").valid()) {
			return;
		}

		var maxDate = new Date(x.Format("yyyy-MM-dd"));
		var minDate = new Date(x.addDate(-2).Format("yyyy-MM-dd"));
		var confirm_date = new Date($(".date-picker-confirm-date").val());
		if (confirm_date - maxDate > 0 || confirm_date - minDate < 0) {
			fail_msg("请选择允许的时间范围！");
			return;
		}

		var confirm_file = $("#txt-confirm-file").val();
		if (confirm_file == "") {
			fail_msg("请上传确认件！");
			return;
		}

		var data = $("form").serialize();

		// 名单json
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		// 判断是否有名单
		var hasNames = false;
		var hasChairman = false;
		let people = new Array();
		let not_ok_names = new Array();
		for (var i = 0; i < trs.length; i++) {
			var tr = trs[i];
			var chairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val().trim();
			var sex = $(tr).find("[st='sex']").val();

			var cellphone_A = $(tr).find("[st='cellphone_A']").val().trim();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val().trim();
			var id = $(tr).find("[st='id']").val().trim();

			const age = $(tr).find("[st='age']").val().trim();
			const id_type = $(tr).find("[st='type']").val();
			let is_ok = $(tr).find("[st='is_ok']").val();
			
			if(is_ok!='Y'){
				not_ok_names.push(name);
			}
			if (name == "" && id == "") {
				continue;
			}

			if ((name != "" && id == "") || (name == "" && id != "")) {
				fail_msg("请正确填写第" + index + "个名单!");
				return;
			}

			if (name != "" && id != "" && !hasNames) {
				hasNames = true;
			}

			if (chairman == "Y") {
				hasChairman = true;
			}

			let person = {chairman,index,name,sex,age,cellphone_A,cellphone_B,id_type,id};
			people.push(person);
		}

		if (!hasNames) {
			fail_msg("没有名单，不能确认！");
			return;
		}
		if (!hasChairman) {
			fail_msg("请指定团长！");
			return;
		}
		if(not_ok_names.length>0){
			fail_msg(not_ok_names.join(",")+"未通过验证！");
			endLoadingIndicator();
			return;
		}
		const json = JSON.stringify(people);
		data += "&json=" + json;

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/confirmBudgetNonStandardOrder',
			data : data
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/order/tbc-order.jsp";
			} else if (str == "noenoughcredit") {
				endLoadingIndicator();
				fail_msg("信用额度不足，不能确认订单！");
			}
		});
	};
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

	const current_date = new Date(ctx.current_date);

	var maxDate = new Date(current_date.Format("yyyy-MM-dd"));
	var minDate = new Date(current_date.addDate(-2).Format("yyyy-MM-dd"));
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
