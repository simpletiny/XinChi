var supplierLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.saleMapping = {
		'N' : "未上架",
		'Y' : "已上架"
	};
	// 新建产品
	self.create = function() {
		window.location.href = self.apiurl + "templates/product/product-creation.jsp";
	};
	self.chosenProducts = ko.observableArray([]);
	// 编辑产品
	self.edit = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("维护只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			window.location.href = self.apiurl + "templates/product/product-edit.jsp?key=" + self.chosenProducts();
		}
	};
	self.onSale = function(sale_flg) {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 0) {
			var msg = "确认要";
			if (sale_flg == "Y") {
				msg += "上架";
			} else {
				msg += "下架";
			}

			if (self.chosenProducts().length == 1) {
				msg += "此";

			} else {
				msg += "这些";
			}
			msg += "产品吗？";

			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("保存中！");
						var data = "sale_flg=" + sale_flg + "&product_pks=" + self.chosenProducts();
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/onSaleProduct',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenProducts.removeAll();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	};

	self.products = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {

		var param = "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductsByPage', param, function(data) {
			self.products(data.products);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
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

var ctx = new ProductContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
