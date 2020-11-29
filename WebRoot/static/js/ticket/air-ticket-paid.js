var count = 1;
var AgencyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.payable_pks = $("#payable_pks").val();
	self.payables = ko.observableArray([]);
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
		'Q' : '其他支出'
	};

	// 获取所有账户
	self.accounts = ko.observableArray([]);
	self.receiver = ko.observable();
	$.getJSON(self.apiurl + 'finance/searchCardsByPurpose', {
		purpose : 'TICKET'
	}, function(data) {
		if (data.cards) {
			self.accounts(data.cards);
		} else {
			fail_msg("不存在票务账户，无法支付！");
			setTimeout(function() {
				window.history.go(-1);
			}, 2000);
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	startLoadingSimpleIndicator("加载中");

	$.getJSON(self.apiurl + 'payable/searchAirTicketPayablesByPks', {
		payable_pks : self.payable_pks
	}, function(data) {
		if (data.payables) {
			self.payables(data.payables);
			self.receiver(self.payables()[0].financial_body_name);
			caculateSumMoney();
		} else {
			fail_msg("支付信息不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.add = function() {
		if (count >= 10) {
			fail_msg("最多只能有10个支付信息！");
			return;
		}
		count += 1;
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
		// var prev = $("#div_add").prev();
		// $(prev).find("[name='account']").attr("name", "account" + count);
		// $(prev).find("[name='time']").attr("name", "time" + count);
		// $(prev).find("[name='receiver']").attr("name", "receiver" + count);
		// $(prev).find("[name='money']").attr("name", "money" + count);
		// $(prev).find("[name='file']").attr("name", "file" + count);
		// $(prev).find("[name='voucherFile']")
		// .attr("name", "voucherFile" + count);

		$(':file').change(function() {
			changeFile(this);
		});

		console.log(count);
	};
	self.finish = function() {

		if (!$("form").valid()) {
			return;
		}
		if (!self.caculateSum()) {
			fail_msg("支付金额合计和应支付总金额不符！");
			return;
		}

		var payableJson = '[';
		var allPayables = $("#div-payable").children();
		for (var i = 0; i < allPayables.length; i++) {
			var current = allPayables[i];
			var payable_pk = $(current).find("[st^='payable-pk']").val();
			var this_paid = $(current).find("[st^='this-paid']").val();

			if (this_paid.trim() == "") {
				fail_msg("请填写此次付款金额！");
				retrun;
			}

			payableJson += '{"payable_pk":"' + payable_pk + '","this_paid":"'
					+ this_paid + '"';

			if (i == allPayables.length - 1) {
				payableJson += '}';
			} else {
				payableJson += '},';
			}
		}
		payableJson += ']';
		startLoadingIndicator("保存中");
		var paidJson = '[';
		var allAccount = $("form").find("select[name^='account']");

		for (var i = 0; i < allAccount.length; i++) {
			var current = $(allAccount[i]).parent().parent().parent().parent();
			var account = $(allAccount[i]).val();
			var time = $(current).find("[name^='time']").val();
			var receiver = $(current).find("[name^='receiver']").val();
			var money = $(current).find("[name^='money']").val();
			var voucherFile = $(current).find("[name^='voucherFile']").val();
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

		$.ajax(
				{
					type : "POST",
					url : self.apiurl + 'payable/payAirTicket',
					data : "paidJson=" + paidJson + "&payableJson="
							+ payableJson + "&allot_money=" + sumMoney
				}).success(function(str) {
			if (str == "success") {
				window.history.go(-1);
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
		if (sum == sumMoney) {
			return true;
		} else {
			return false;
		};
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
	count -= 1;
	console.log(count);
}
var sumMoney = 0;
/**
 * 计算此次付款总金额
 */
function caculateSumMoney() {
	sumMoney = 0;
	var payables = $("#div-payable").children();
	for (var i = 0; i < payables.length; i++) {
		var current = payables[i];
		sumMoney += $(current).find("[st^='this-paid']").val() - 0;
	}

	$("#sum-money").text(sumMoney);
}

function sleep(d) {
	var t = Date.now();
	console.log(d);
	while (Date.now() - t <= d) {
		console.log("dew");
	};
}
