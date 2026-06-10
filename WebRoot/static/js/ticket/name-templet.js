var templetLayer;
var templetContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.templets = ko.observable({
		total: 0,
		items: []
	});

	self.refresh = function() {
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'ticket/searchTicketNameTempletByPage', param, function(data) {

			self.templets(data.templets);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	/**
	 * 新建名单模板
	 */
	self.create = function() {
		contentClear("div-templet");
		if ($("#a-view")) {
			$("#a-view").remove();
		}
		templetLayer = $.layer({
			type: 1,
			title: ['新建名单模板', ''],
			maxmin: false,
			closeBtn: [1, true],
			shadeClose: false,
			area: ['600px', '255px'],
			offset: ['', ''],
			scrollbar: true,
			page: {
				dom: '#div-templet'
			},
			end: function() {
				contentClear('div-templet');
			}
		});
	}

	self.doTemplet = function() {
		if ($("#templet-name").val().trim() === "") {
			fail_msg("请填写模板名称！");
			return;
		}

		const templet_pk = $("#templet-pk").val();
		let url = "";
		if (templet_pk === "") {
			if ($("#office-file").val() === "") {
				fail_msg("请上传模板！");
				return;
			}
			url = self.apiurl + 'ticket/createTicketNameTemplet';
		} else {
			url = self.apiurl + 'ticket/updateTicketNameTemplet';
		}

		startLoadingSimpleIndicator("保存中...");
		var data = $("#form-templet").serialize();
		
		$.ajax({
			type: "POST",
			url: url,
			data: data,
			success: function(str) {
				if (str == "success") {
					self.refresh();
					contentClear('div-templet');
					layer.close(templetLayer);
				} else if (str == "existname") {
					fail_msg("存在同名模板！");
				} else {
					fail_msg(str);
				}
				endLoadingIndicator();
			}
		});
	}
	self.cancelTemplet = function() {
		layer.close(templetLayer);
	}
	self.templet = ko.observable({});
	self.editTemplet = function(data) {
		self.templet(data);
		let temp_href = `<a id="a-view" href="javascript:void(0)" onclick = "viewExcel('` + data.file_name + `','TICKET_NAME_TEMPLET')">预览</a></span>`;
		if ($("#a-view")) {
			$("#a-view").remove();
		}
		$("#file-templet").parent().after(temp_href);
		templetLayer = $.layer({
			type: 1,
			title: ['更新名单模板', ''],
			maxmin: false,
			closeBtn: [1, true],
			shadeClose: false,
			area: ['600px', '255px'],
			offset: ['', ''],
			scrollbar: true,
			page: {
				dom: '#div-templet'
			},
			end: function() {
				contentClear('div-templet');
			}
		});
	}

	self.deleteTemplet = function(data) {
		$.layer({
			area: ['auto', 'auto'],
			dialog: {
				msg: "确认要删除此模板吗！",
				btns: 2,
				type: 4,
				btn: ['确认', '取消'],
				yes: function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("删除中");
					var param = "templet_pk=" + data.pk;
					$.ajax({
						type: "POST",
						url: self.apiurl + 'ticket/deleteTicketNameTemplet',
						data: param
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
						} else {
							fail_msg("删除失败，联系管理员！");
						}
					});
				}
			}
		});
	}
	self.viewTemplet = function(data) {
		viewExcel(data.file_name, "TICKET_NAME_TEMPLET");
	}

	self.download = function(data) {
		window.location.href = self.apiurl + 'file/getFileStream?fileFileName=' + data.file_name
			+ "&fileType=TICKET_NAME_TEMPLET";
	}
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

var ctx = new templetContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	$('#file-templet').change(function() {
		uploadOffice({
			input: this,
			type: 'xlsx',
			required: "yes"
		}).then(fileName => { templetUploadStatus(fileName) }).catch(error => { console.error(error) });
	});
});

let templetUploadStatus = function(fileName) {
	let temp_href = `<a id="a-view" href="javascript:void(0)" onclick = "viewExcel('` + fileName + `','TEMP')">预览</a></span>`;
	if ($("#a-view")) {
		$("#a-view").remove();
	}
	$("#file-templet").parent().after(temp_href);
}
var contentClear = function(id) {
	$("#" + id + " input,textarea").val('');
	$("#" + id + " img").remove();
}

var replacer = function(key, value) {
	if (typeof value === "number") {
		return value + "";
	}
	return value;
}