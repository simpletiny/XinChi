var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.employee = ko.observable({});
	self.order_pk = $("#key").val();
	self.passengers = ko.observableArray([{
		name_index : 1,
		chairman : 'Y'
	}]);
	self.independent_msg = ko.observable();
	self.current_date = $("#hidden-server-date").val();

	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.users(data.users);
	});

	var d = new Date(self.current_date);
	var year_now = d.getFullYear();
	self.do_confirm_date = ko.observable();
	$.getJSON(self.apiurl + 'order/searchTbcBnsOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.bnsOrder);

		if (data.passengers.length > 0) {
			self.passengers(data.passengers);
		}

		if (self.order().independent_flg == 'Y') {
			self.independent_msg("（独立团）");
		}
		self.do_confirm_date(self.order().do_confirm_date);
		let do_date = new Date(self.do_confirm_date());

		let maxDate = new Date(do_date.Format("yyyy-MM-dd"));
		let minDate = new Date(do_date.addDate(-2).Format("yyyy-MM-dd"));
		$(".date-picker-confirm-date").datetimepicker({
			format : 'Y-m-d',
			timepicker : false,
			scrollInput : false,
			defaultDate : new Date(),
			lang : 'zh',

			minDate : minDate,
			maxDate : maxDate,
		})

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

		let do_date = new Date(self.do_confirm_date());
		var maxDate = new Date(do_date.Format("yyyy-MM-dd"));
		var minDate = new Date(do_date.addDate(-2).Format("yyyy-MM-dd"));
		var confirm_date = new Date($(".date-picker-confirm-date").val());
		if (confirm_date - maxDate > 0 || confirm_date - minDate < 0) {
			fail_msg("请选择允许的时间范围！");
			return;
		}

		const url = self.apiurl + 'order/updateConfirmedNonStandardOrder';
		var data = $("form").serialize();

		// 名单json
		// 判断是否有名单
		var hasNames = false;
		var hasChairman = false;
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		let people = new Array();
		for (var i = 0; i < trs.length; i++) {
			var tr = trs[i];
			var chairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val().trim();
			var sex = $(tr).find("[st='sex']").val();

			var cellphone_A = $(tr).find("[st='cellphone_A']").val().trim();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val().trim();
			var id = $(tr).find("[st='id']").val().trim();
			var pk = $(tr).find("[st='name-pk']").val();

			const age = $(tr).find("[st='age']").val().trim();
			const id_type = $(tr).find("[st='type']").val();
			const lock_flg = $(tr).find("[st='name-lock']").val();
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

			let person = {chairman,index,name,sex,age,cellphone_A,cellphone_B,id_type,id,pk,lock_flg};
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

		const json = JSON.stringify(people);
		startLoadingSimpleIndicator("保存中");
		data += "&json=" + json;

		$.ajax({
			type : "POST",
			url : url,
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				window.location.href = self.apiurl + "templates/order/c-order.jsp";
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

});