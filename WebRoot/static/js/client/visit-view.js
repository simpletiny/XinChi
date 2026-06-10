let visitImgCheckLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenVisits = ko.observableArray([]);

	self.visits = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchVisitByPage', param, function(data) {
			self.visits(data.visits);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			endLoadingIndicator();
		});
	};

	self.search = function() {

	};

	self.resetPage = function() {

	};

	// self.editEmployee = function() {
	// if (self.chosenEmployees().length == 0) {
	// fail_msg("请选择员工");
	// return;
	// } else if (self.chosenEmployees().length > 1) {
	// fail_msg("编辑只能选中一个");
	// return;
	// } else if (self.chosenEmployees().length == 1) {
	// window.location.href = self.apiurl +
	// "templates/client/employee-edit.jsp?key=" + self.chosenEmployees()[0];
	// }
	// };
	self.checkVisitImg = function(fileName, connect_date) {
		$("#img-pic").attr("src", "");
		visitImgCheckLayer = $.layer({
			type : 1,
			title : ['会见图片', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['320px', '355px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});

		let sub_folder = connect_date.substring(0, 4) + "/" + connect_date.substring(5, 7);
		console.log(self.apiurl + 'file/getFileStream?fileFileName=' + fileName
				+ "&fileType=CLIENT_VISIT_IMG&subFolder" + sub_folder);
		$("#img-pic").attr(
				"src",
				self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=CLIENT_VISIT_IMG&subFolder="
						+ sub_folder);
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
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.refresh();
	};
	// end pagination
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	$("#img-pic").css({
		"width" : "320px",
		"height" : "320px"
	});
});
