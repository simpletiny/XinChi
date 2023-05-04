var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.order_pk = $("#key").val();
	self.product = ko.observable({});
	self.employee = ko.observable({});
	self.confirm_date = ko.observable();
	self.independent_msg = ko.observable();

	self.passengers = ko.observableArray([]);

	var x = new Date();
	var year_now = x.getFullYear();

	self.confirm_date(x.Format('yyyy-MM-dd'));
	$.getJSON(self.apiurl + 'order/searchTbcBsOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.bsOrder);

		$(data.passengers).each(function(idx, passenger) {
			passenger.age = ko.observable();
			var birthYear = passenger.id.substring(6, 10);
			passenger.age(year_now - birthYear);
		});
		self.passengers(data.passengers);

		if (self.order().independent_flg == 'Y') {
			self.independent_msg("（独立团）");
		}

		$.getJSON(self.apiurl + 'product/searchProductByPk', {
			product_pk : self.order().product_pk
		}, function(data) {
			self.product(data.product);
		});
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

	self.confirmOrder = function() {
		if (!$("form").valid()) {
			return;
		}

		var x = new Date();
		var maxDate = new Date(x.Format("yyyy-MM-dd"));
		var minDate = new Date(x.addDate(-2).Format("yyyy-MM-dd"));
		var confirm_date = new Date($(".date-picker-confirm-date").val());
		if (confirm_date - maxDate > 0 || confirm_date - minDate < 0) {
			fail_msg("请选择允许的时间范围:" + minDate + "至" + maxDate);
			return;
		}

		/* 判断名单是否有误 */
		var tbody = $("#name-table").find("tbody");
		var ids = $(tbody).find("[st='id']");
		var errors = new Array();
		for (var i = 0; i < ids.length; i++) {
			var id = $(ids[i]).val();
			if (id.trim().length < 18) {
				errors.push(i + 1);
			}
		}

		if (errors.length > 0) {
			var msg = "序号为{0}的名单信息有误！";
			var indexs = "";
			for (var i = 0; i < errors.length; i++) {
				indexs += errors[i] + ",";
			}
			indexs = indexs.substring(0, indexs.length - 1);
			fail_msg(msg.format(indexs));
			return;
		}

		var confirm_file = $("#txt-confirm-file").val();
		if (confirm_file == "") {
			fail_msg("请上传确认件！");
			return;
		}

		var data = $("form").serialize();

		// 名单json
		// 判断是否有名单
		var hasNames = false;
		var hasChairman = false;
		var trs = $(tbody).children();
		var json = '[';
		for (var i = 0; i < trs.length; i++) {

			var tr = trs[i];
			var teamChairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val().trim();
			var sex = $(tr).find("[st='sex']").val();

			var cellphone_A = $(tr).find("[st='cellphone_A']").val();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val();
			var id = $(tr).find("[st='id']").val().trim();
			var price = $(tr).find("[st='price']").val().trim();

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

			json += '{"chairman":"' + teamChairman + '","index":"' + index + '","name":"' + name + '","sex":"' + sex
					+ '","cellphone_A":"' + cellphone_A + '","cellphone_B":"' + cellphone_B + '","id":"' + id
					+ '","price":"' + price + '"},';
		}

		if (!hasNames) {
			fail_msg("没有名单，不能确认！");
			return;
		}

		if (!hasChairman) {
			fail_msg("请指定团长！");
			return;
		}
		json = json.RTrim(',') + ']';
		data += "&json=" + json;

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/confirmBudgetStandardOrder',
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
};

var ctx = new OrderContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		changeFile(this);
	});
	var x = new Date();
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
