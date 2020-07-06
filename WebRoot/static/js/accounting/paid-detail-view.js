var voucherCheckLayer;
var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.ps = ko.observable({});
	var pay_number = $("#pay-number").val();

	self.details = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		var param = "voucher_number=" + pay_number;
		$.getJSON(self.apiurl + 'accounting/searchPaidDetailByPayNumber',
				param, function(data) {
					self.details(data.details);
					self.ps(data.ps);
					$(".rmb").formatCurrency();
				});
	};
	self.checkVoucherPic = function(fileName, accountId) {
		$("#img-pic").attr("src", "");
		voucherCheckLayer = $.layer({
			type : 1,
			title : ['查看凭证', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});

		$("#img-pic").attr(
				"src",
				self.apiurl + 'file/getFileStream?fileFileName=' + fileName
						+ "&fileType=VOUCHER&subFolder=" + accountId);
	};
	// 新标签页显示大图片
	$("#img-pic").on(
			'click',
			function() {
				window.open(self.apiurl
						+ "templates/common/check-picture-big.jsp?src="
						+ encodeURIComponent($(this).attr("src")));
			});
	self.search = function() {
		self.refresh();
	};
};

var ctx = new DetailContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});