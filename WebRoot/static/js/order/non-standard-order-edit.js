var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.employee = ko.observable({});
	self.order_pk = $("#key").val();
	self.passengers = ko.observableArray([]);
	self.independent_msg = ko.observable();

	var d = new Date();
	var year_now = d.getFullYear();

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

		if (self.order().independent_flg == 'Y') {
			self.independent_msg("（独立团）");
		}

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
		const url = self.apiurl + 'order/updateBudgetNonStandardOrder';
		var data = $("form").serialize();

		// 名单json
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		var json = '[';
		for (var i = 0; i < trs.length; i++) {
			if (i != 0)
				json += ',';
			var tr = trs[i];
			var teamChairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val();
			var sex = $(tr).find("[st='sex']").val();

			var cellphone_A = $(tr).find("[st='cellphone_A']").val();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val();
			var id = $(tr).find("[st='id']").val();

			json += '{"chairman":"' + teamChairman + '","index":"' + index + '","name":"' + name + '","sex":"' + sex
					+ '","cellphone_A":"' + cellphone_A + '","cellphone_B":"' + cellphone_B + '","id":"' + id + '"}';
		}
		json += ']';
		data += "&json=" + json;

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : url,
			data : data
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/order/tbc-order.jsp";
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
});