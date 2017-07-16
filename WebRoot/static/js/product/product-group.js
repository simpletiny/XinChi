var supplierLayer;
var GroupContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	// 新建用户组
	self.create = function() {
		window.location.href = self.apiurl + "templates/product/group-creation.jsp";
	};
	self.chosenGroups = ko.observableArray([]);
	// 编辑用户组
	self.edit = function() {
		if (self.chosenGroups().length == 0) {
			fail_msg("请选择产品组！");
			return;
		} else if (self.chosenGroups().length > 1) {
			fail_msg("修改只能选择一个！");
			return;
		} else if (self.chosenGroups().length == 1) {
			window.location.href = self.apiurl + "templates/product/group-edit.jsp?key=" + self.chosenGroups();
		}
	};
	self.suppliers = ko.observableArray([]);
	// 查看包含供应商
	self.checkSuppliers = function(group_pk) {
		$.getJSON(self.apiurl + 'product/searchSuppliersByGroupPk', {
			group_pk : self.group_pk
		}, function(data) {
			self.suppliers(data.group_suppliers);
			supplierLayer = $.layer({
				type : 1,
				title : [ '查看包含供应商', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '700px', '300px' ],
				offset : [ '200px', '' ],
				scrollbar : true,
				page : {
					dom : '#supplier-panel'
				},
				end : function() {
					self.suppliers.removeAll();
				}
			});
		});
	};
	self.deleteGroup = function() {
		if (self.chosenGroups().length == 0) {
			fail_msg("请选择产品组！");
			return;
		} else if (self.chosenGroups().length > 1) {
			fail_msg("删除只能选择一个！");
			return;
		} else if (self.chosenGroups().length == 1) {
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '确认要删除此产品组吗?',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						var data = "group_pk=" + self.chosenGroups();
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/deleteGroup',
							data : data
						}).success(function(str) {
							if (str == "success") {
								self.refresh();
								self.chosenGroups.removeAll();
								layer.close(index);
							} else if (str == "exists") {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	};

	self.groups = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {

		var param = "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchGroupsByPage', param, function(data) {
			self.groups(data.groups);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.getUsers = function(group_pk) {

		return "暂未处理";
	};
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 10;
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

var ctx = new GroupContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
