var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.order_pk = $("#key").val();
	self.product = ko.observable({});
	self.employee = ko.observable({});
	self.independent_msg = ko.observable();

	self.passengers = ko.observableArray([]);

	var x = new Date();
	var year_now = x.getFullYear();
	self.do_confirm_date = ko.observable();
	
	$.getJSON(self.apiurl + 'order/searchTbcBsOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.bsOrder);
		if (data.passengers.length>0) {
			$(data.passengers).each(function(index,data){
				data.as_adult = data.as_adult=='Y'?true:false;
			})
			self.passengers(data.passengers);
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

		if (self.order().independent_flg == 'Y') {
			self.independent_msg("（独立团）");
		}
		autoPersonInfo();
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

	self.updateOrder = function() {
		if (!$("form").valid()) {
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
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		let people = new Array();
		for (var i = 0; i < trs.length; i++) {

			var tr = trs[i];
			var chairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val().trim();
			var sex = $(tr).find("[st='sex']").val();
			var pk = $(tr).find("[st='name-pk']").val();
			var lock_flg = $(tr).find("[st='name-lock']").val();
			var cellphone_A = $(tr).find("[st='cellphone_A']").val();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val();
			var id = $(tr).find("[st='id']").val().trim();
			var price = $(tr).find("[st='price']").val().trim();
			var age = $(tr).find("[st='age']").val().trim();
			var id_type = $(tr).find("[st='type']").val();

			
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
			let txt_as_adult = $(tr).find("[st='as-adult']");
			let as_adult = 'N';
			if(txt_as_adult){
				if($(txt_as_adult).is(":checked")){
					as_adult='Y';
				}
			}
			let person = {pk,lock_flg,chairman,index,name,sex,age,cellphone_A,cellphone_B,id,price,id_type,as_adult};
			people.push(person);
		}

		if (!hasNames) {
			fail_msg("没有名单，不能确认！");
			return;
		}

		if (!chairman) {
			fail_msg("请指定团长！");
			return;
		}
		let json = JSON.stringify(people);
		data += "&json=" + json;

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/updateConfirmedStandardOrder',
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
