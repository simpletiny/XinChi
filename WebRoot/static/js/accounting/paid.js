var AgencyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.wfpPk = $("#wfp_pk").val();
	self.wfp = ko.observable({});
	self.supplier = ko.observable({});
	self.genders = [];

	// 项目映射
	self.itemMapping = {
		'D' : '地接款',
		'X' : '销售费用',
		'B' : '办公费用',
		'C' : '产品费用',
		'P' : '票务费用',
		'J' : '交通垫付',
		'G' : '工资费用',
		'Q' : '其他支出',
		'M' : '多付返款',
		'F' : 'FLY'
	};
	var now = new Date();
	self.current_min = now.Format("yyyy-MM-dd hh:mm");
	self.chosenAccount = ko.observable();
	// 获取所有账户
	self.accounts = ko.observableArray([]);

	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
			self.chosenAccount("交行倒账9363");
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
	self.defaultMoney = ko.observable();
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'accounting/searchOneWFP', {
		wfp_pk : self.wfpPk
	}, function(data) {
		if (data.wfp) {
			self.wfp(data.wfp);
			if (data.supplier != null) {
				self.supplier(data.supplier);
			} else {
				self.supplier(new Object());
			}
			self.defaultMoney(self.wfp().money);
		} else {
			fail_msg("待支付信息不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
	self.count = 1;
	self.add = function() {
		self.count += 1;
		$("#div_add").before($("#div_mod").html());
		if ($('.datetime-picker').datetimepicker != null) {
			$('.datetime-picker').datetimepicker({
				format : 'Y-m-d H:i',
				timepicker : true,
				scrollInput : false,
				defaultDate : new Date(),
				lang : 'zh'
			});
		}
		var prev = $("#div_add").prev();
		$(prev).find("[name='account']").attr("name", "account" + self.count);
		$(prev).find("[name^='account']").val("交行倒账9363");
		$(prev).find("[name='time']").attr("name", "time" + self.count);
		$(prev).find("[name^='time']").val(self.current_min);
		$(prev).find("[name='receiver']").attr("name", "receiver" + self.count);
		$(prev).find("[name^='receiver']").val(
				self.supplier().personal_account_name);
		$(prev).find("[name='money']").attr("name", "money" + self.count);
		$(prev).find("[name='file']").attr("name", "file" + self.count);
		$(prev).find("[name='voucherFile']").attr("name",
				"voucherFile" + self.count);
		$(':file').change(function() {
			changeFile(this);
		});
	};
	self.finish = function() {

		if (!$("form").valid()) {
			return;
		}
		if (!self.caculateSum()) {
			fail_msg("支付金额合计和应支付总金额不符！");
			return;
		}

		var paidJson = '[';
		var allAccount = $("form").find("select[name^='account']");

		for (var i = 0; i < allAccount.length; i++) {
			var current = $(allAccount[i]).parent().parent().parent().parent();
			var account = $(allAccount[i]).val();
			var time = $(current).find("[name^='time']").val();
			var receiver = $(current).find("[name^='receiver']").val();
			var money = $(current).find("[name^='money']").val();
			var voucherFile = $(current).find("[name^='voucherFile']").val();
			if (voucherFile == "") {
				fail_msg("请上传凭证");
				return;
			}
			paidJson += '{"account":+"' + account + '","time":"' + time
					+ '","receiver":"' + receiver + '","money":"' + money
					+ '","voucherFile":"' + voucherFile + '"';

			if (i == allAccount.length - 1) {
				paidJson += '}';
			} else {
				paidJson += '},';
			}
		}

		paidJson += ']';

		startLoadingSimpleIndicator("保存中");
		$.ajax(
				{
					type : "POST",
					url : self.apiurl + 'accounting/pay',
					data : "json=" + paidJson + "&voucher_number="
							+ self.wfp().pay_number
				}).success(
				function(str) {
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/accounting/waiting-for-paid.jsp";
					} else if (str == "time") {
						fail_msg("同一账户在同一时间下已存在支出！");
						endLoadingIndicator();
					}
				});
	};

	self.caculateSum = function() {
		var allMoney = $("form").find("[name^='money']");
		var sum = 0;
		for (var i = 0; i < allMoney.length; i++) {
			sum += ($(allMoney[i]).val() - 0);
		}
		if (sum == self.wfp().money) {
			return true;
		} else {
			return false;
		}
	};
};

var ctx = new AgencyContext();

var initWidth = 600;
$(document).ready(function() {
	ko.applyBindings(ctx);

	$(':file').change(function() {
		changeFile(this);

	});
});
function changeFile(thisx) {
	var file = thisx.files[0];
	name_check = file.name;
	size = file.size;
	type = file.type;
	if (type.indexOf("image") < 0) {
		fail_msg("请上传图片");
		return;
	}
	if (size > 512000) {
		fail_msg("文件大于500KB");
		return;
	}
	var imgContainer = $(thisx).parent().parent().parent().next();
	var fileNameInput = $(thisx).parent().next();
	var progress = $("<progress value='0'> </progress>");
	$(thisx).parent().parent().next().append(progress);
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/fileUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.responseType = "blob";
	xhr.onprogress = function() {
		updateProgress(event, progress);
	};
	xhr.upload.onprogress = function() {
		updateProgress(event, progress);
	};
	xhr.onload = function() {
		if (this.status == 200) {
			var fileName = this.getResponseHeader("Content-Disposition").split(
					";")[1].split("=")[1];
			var blob = this.response;
			var deleteButton = $("<div class='delete'>删除</div>");

			var img = document.createElement("img");
			$("body").append(deleteButton);
			deleteButton.hide();
			deleteButton.click(function() {
				deleteImage(this, thisx, img, fileNameInput, fileName);
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

				$(img).mouseenter(
						function() {
							deleteButton.css("top", $(img).offset().top
									+ img.height / 2 - 25);
							deleteButton.css("left", $(img).offset().left
									+ img.width / 2 - 50);
							deleteButton.show();
						});
				$(img).mouseout(function() {
					deleteButton.hide();
				});
			};
			img.src = window.URL.createObjectURL(blob);
			imgContainer.html(img);
			fileNameInput.val(fileName);

			progress.delay(2000).fadeIn(function() {
				progress.remove();
			});
		}
	};
	xhr.send(formData);
}
function updateProgress(e, progress) {
	if (e.lengthComputable) {
		$(progress).attr({
			value : e.loaded,
			max : e.total
		});
	}
}
function deleteImage(deleteButton, inputFile, img, fileNameInput, fileName) {
	$(deleteButton).remove();
	$(img).remove();
	var inputName = inputFile.name;
	var newInputFile = $("<input type='file' name='" + inputName + "'/>");
	newInputFile.change(function() {
		changeFile(this);
	});
	$(inputFile).parent().append(newInputFile);
	$(inputFile).remove();

	$(fileNameInput).val($(fileNameInput).val().replace(fileName, ""));
}
function remove(div) {
	$(div).parent().remove();
}