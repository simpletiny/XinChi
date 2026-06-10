var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenViews = ko.observableArray([]);

	self.createView = function() {
		window.location.href = self.apiurl + "templates/culture/product-research-view-creation.jsp";
	};

	self.views = ko.observable({
		total : 0,
		items : []
	});
	self.chosenLabel = ko.observable();
	self.labels = ko.observableArray([]);
	self.searchLabels = function() {
		$.getJSON(self.apiurl + 'culture/searchAllLabels', {}, function(data) {
			self.labels(data.labels);
			if (self.labels != null && self.labels().length > 0) {
				self.chosenLabel(self.labels()[0].label_name);
				self.refresh();
			}
		});
	}
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中……");
		var param = "view.label=" + self.chosenLabel();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'culture/searchProductResearchViewByPage', param, function(data) {
			self.views(data.views);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			endLoadingIndicator();
		});
	};

	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.deleteView = function() {
		if (self.chosenViews().length == 0) {
			fail_msg("请选择要删除的文章");
			return;
		} else if (self.chosenViews().length > 1) {
			fail_msg("删除每次只能选中一个");
			return;
		} else if (self.chosenViews().length == 1) {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要删除这篇文章吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						$.ajax({
							type : "POST",
							url : self.apiurl + 'culture/deleteProductResearchView',
							data : "view_pk=" + self.chosenViews()
						}).success(function(str) {
							if (str == "OK") {
								self.refresh();
							} else if (str == "NORIGHT") {
								fail_msg("只能删除自己撰写的文章。")
							}
						});
					}
				}
			});
		}
	};

	self.labelManager = function() {

		headCheckLayer = $.layer({
			type : 1,
			title : ['标签（拖动排序）', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['500px', '400px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#label-manager'
			},
			end : function() {
				self.sortLabel();
			}
		});
	}

	self.createLabel = function() {
		var label = $("#txt-label").val().trim();
		if (label == "")
			return;

		for (var i = 0; i < self.labels().length; i++) {
			if (label == self.labels()[i].label_name) {
				$("#txt-label").val("");
				return;
			}
		}

		var data = "label.label_name=" + label + "&label.label_index=" + (self.labels().length + 1);
		$.ajax({
			type : "POST",
			url : self.apiurl + 'culture/createLabel',
			data : data

		}).success(function(str) {
			if (str == "success") {
				self.searchLabels();
				$("#txt-label").val("");
			}

		});
	}
	self.deleteLabel = function() {
		var label = $("#txt-label").val().trim();
		if (label == "")
			return;

		var data = "label_name=" + label;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'culture/deleteLabelByName',
			data : data

		}).success(function(str) {
			if (str == "success") {
				self.searchLabels();
				$("#txt-label").val("");
			}
		});
	}
	self.sortLabel = function() {
		var allDivs = $("#labelGrid").children();
		var json = "[";
		for (var i = 0; i < allDivs.length; i++) {
			var div = allDivs[i];
			var pk = $(div).find("input").val();
			json += '{"pk":"' + pk + '","label_index":"' + (i + 1) + '"}';
			if (i != allDivs.length - 1) {
				json += ',';
			}
		}
		json += ']';

		var data = "sort_json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'culture/sortLabels',
			data : data

		}).success(function(str) {
			if (str == "success") {
				self.searchLabels();
			}
		});
	}

	self.setLabelName = function(label_name) {
		$("#txt-label").val(label_name);
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

var ctx = new ViewContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.searchLabels();

	new Sortable(labelGrid, {
		animation : 150,
		ghostClass : 'blue-background-class'
	});
});
