var AgencyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.wfpPk = $("#wfp_pk").val();
	self.wfp = ko.observable({});
	self.genders = [];

	// 项目映射
	self.itemMapping = {
		'D' : '地接款',
		'X' : '销售费用',
		'B' : '办公费用',
		'C' : '产品费用',
		'P' : '票务费用',
		'J' : '交通费用',
		'G' : '工资费用',
		'Q' : '其他费用'
	};

	// 获取所有账户
	self.accounts = ko.observableArray([]);
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
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
		$(prev).find("[name='time']").attr("name", "time" + self.count);
		$(prev).find("[name='receiver']").attr("name", "receiver" + self.count);
		$(prev).find("[name='money']").attr("name", "money" + self.count);
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

		for ( var i = 0; i < allAccount.length; i++) {
			var current = $(allAccount[i]).parent().parent().parent().parent();
			var account = $(allAccount[i]).val();
			var time = $(current).find("[name^='time']").val();
			var receiver = $(current).find("[name^='receiver']").val();
			var money = $(current).find("[name^='money']").val();
			paidJson += '{"account":+"' + account + '","time":"' + time + '","receiver":"' + receiver + '","money":"' + money + '"';
			if (i == allAccount.length - 1) {
				paidJson += '}';
			} else {
				paidJson += '},';
			}
		}

		paidJson += ']';

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'accounting/pay',
			data : "json=" + paidJson + "&voucher_number=" + self.wfp().pay_number
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/accounting/waiting-for-paid.jsp";
			}else if(str=="time"){
				fail_msg("同一账户在同一时间下已存在支出！");
				endLoadingIndicator();
			}
		});
	};

	self.caculateSum = function() {
		var allMoney = $("form").find("[name^='money']");
		var sum = 0;
		for ( var i = 0; i < allMoney.length; i++) {
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

function remove(div) {
	$(div).parent().remove();
}