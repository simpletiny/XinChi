var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.allTypes = [ 'NONE', 'IN', 'OUT' ];
	self.typeMapping = {
		'NONE' : '无汇兑',
		'IN' : '转入汇兑',
		'OUT' : '转出汇兑'
	};

	self.chosenTypes = ko.observableArray([]);

	self.chosenDetails = ko.observableArray([]);
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

	self.createDetail = function(type) {
		window.location.href = self.apiurl + "templates/finance/" + type + "-detail-creation.jsp";
	};

	self.details = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'finance/searchInnerTransferByPage', param, function(data) {
			self.details(data.inners);
			$(".rmb").formatCurrency();

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	self.search = function() {
		self.refresh();
	};

	self.resetPage = function() {

	};
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 20;
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
		for ( var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.refresh();
	};
	// end pagination
};

var ctx = new DetailContext();

$(document).ready(function() {
	// $('.month-picker-st').AlertSelf();
	ko.applyBindings(ctx);
	ctx.refresh();
	$('.month-picker-st').MonthPicker({
		Button : false,
		MonthFormat : 'yy-mm'
	});
	$(':file').change(function() {
		changeFile(this);

	});
});

function changeFile(thisx) {
	var file = thisx.files[0];
	name = file.name;
	size = file.size;
	type = file.type;

	if (type.indexOf("excel") < 0) {
		fail_msg("请上传Excel");
		return;
	}
	if (size > 4194304) {
		fail_msg("文件大于4MB");
		return;
	}
	startLoadingSimpleIndicator("导入中");
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/detailExcelUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.onload = function() {
		if (this.status == 200) {

			var blob = this.response;
			if (blob == "OK") {
				success_msg("导入成功");
			} else if (blob == "BEFORE") {
				fail_msg("导入数据的交易时间不能早于系统已存在明细的时间");
			}
			endLoadingIndicator();
		}
	};
	xhr.send(formData);
}
// (function($){
// $.fn.AlertSelf = function(){
// this.click(function(){alert($(this).val())});
// }
// })(jQuery)
