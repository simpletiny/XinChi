var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.employee = ko.observable({});
	self.order_pk = $("#key").val();
	self.passengers = ko.observableArray([]);
	self.ticket_infos = ko.observableArray([]);
	self.air_comment = ko.observable();
	self.confirm_date = ko.observable();
	self.current_date = $("#hidden-server-date").val();

	var d = new Date(self.current_date);
	var year_now = d.getFullYear();

	self.confirm_date(d.Format('yyyy-MM-dd'));

	$.getJSON(self.apiurl + 'order/searchTbcBnsOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.bnsOrder);
		$(data.passengers).each(function(idx, passenger) {
			passenger.age = ko.observable();
			var birthYear = passenger.id.substring(6, 10);
			passenger.age(year_now - birthYear);
		});
		self.passengers(data.passengers);

		self.ticket_infos(data.ticketInfos);
		self.air_comment(self.ticket_infos()[0].comment);

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

	self.updateOrder = function() {

		if (!$("form").valid()) {
			return;
		}

		// 如果是确认订单编辑状态
		var maxDate = new Date(d.Format("yyyy-MM-dd"));
		var minDate = new Date(d.addDate(-2).Format("yyyy-MM-dd"));
		var confirm_date = new Date($(".date-picker-confirm-date").val());
		if (confirm_date - maxDate > 0 || confirm_date - minDate < 0) {
			fail_msg("请选择允许的时间范围！" + minDate.Format("yyyy-MM-dd") + "至" + maxDate.Format("yyyy-MM-dd"));
			return;
		}

		var confirm_file = $("#txt-confirm-file").val();
		if (confirm_file == "") {
			fail_msg("请上传确认件！");
			return;
		}

		var other_cost = $(":input[name='bnsOrder.other_cost']").val().trim();
		var other_comment = $(":input[name='bnsOrder.other_cost_comment']").val().trim();
		if (other_cost != "" && other_comment == "") {
			fail_msg("有其他费用，必须填写费用说明！")
			return;
		}

		var json = '';
		// 航班信息json
		var ticket_json = '[';
		var info_table = $("#table-ticket tbody");
		var infos = $(info_table).children();

		for (var i = 0; i < infos.length; i++) {
			if (i != 0)
				ticket_json += ',';
			var tr = $(infos[i]);
			var index = tr.find("input[st='flight-index']").val().trim();
			var date = tr.find("input[st='date']").val().trim();
			var from_city = tr.find("input[st='from-city']").val().trim();
			var to_city = tr.find("input[st='to-city']").val().trim();
			// 判断是否有航班信息
			if (date == '' || from_city == '' || to_city == '') {
				fail_msg("请填写航班信息！")
				return;
			}

			ticket_json += '{"index":"' + index + '","date":"' + date + '","from_city":"' + from_city + '","to_city":"'
					+ to_city + '"}';

		}
		ticket_json += ']';

		var air_comment = $("#air-comment").val().trim();

		// 名单json
		var hasNames = false;
		var hasChairman = false;
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		var name_json = '[';
		for (var i = 0; i < trs.length; i++) {
			if (i != 0)
				name_json += ',';
			var tr = trs[i];
			var teamChairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val().trim();
			var sex = $(tr).find("[st='sex']").val();

			var cellphone_A = $(tr).find("[st='cellphone_A']").val().trim();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val().trim();
			var id = $(tr).find("[st='id']").val().trim();

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

			if (teamChairman == "Y") {
				hasChairman = true;
			}

			name_json += '{"chairman":"' + teamChairman + '","index":"' + index + '","name":"' + name + '","sex":"'
					+ sex + '","cellphone_A":"' + cellphone_A + '","cellphone_B":"' + cellphone_B + '","id":"' + id
					+ '"}';
		}
		name_json += ']';

		// 判断是否有名单
		if (!hasNames) {
			fail_msg("没有名单，不能确认！");
			return;
		}

		if (!hasChairman) {
			fail_msg("请指定团长！");
			return;
		}

		json = '{"ticket_json":' + ticket_json + ',"name_json":' + name_json + ',"air_comment":"' + air_comment + '"}';

		var data = $("form").serialize() + "&json=" + json;

		startLoadingIndicator("保存中……");
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
});