var airTickeChecktLayer;
var ProductBoxContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	// self.locations = [ "云南", "华东", "桂林", "张家界", "四川", "其他" ];
	self.locations = ko.observableArray();

	$.getJSON(self.apiurl + 'system/searchByType', {
		type : "LINE"
	}, function(data) {
		self.locations(data.datas);
	});
	self.saleMapping = {
		'N' : "未上架",
		'Y' : "已上架"
	};
	// 创建标准订单
	self.createStandard = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			window.location.href = self.apiurl + "templates/order/standard-order-creation.jsp?independent_flg=N&key="
					+ self.chosenProducts();
		}
	};
	// 创建非标订单
	self.createNonStandard = function() {
		window.location.href = self.apiurl + "templates/order/non-standard-order-creation.jsp?independent_flg=N";
	};

	// 创建独立团
	self.createIndependent = function() {
		if (self.chosenProducts().length == 0) {
			window.location.href = self.apiurl + "templates/order/non-standard-order-creation.jsp?independent_flg=Y";
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			window.location.href = self.apiurl + "templates/order/standard-order-creation.jsp?independent_flg=Y&key="
					+ self.chosenProducts();;
		}
	};

	// 创建单机票订单
	self.createTicket = function() {
		window.location.href = self.apiurl + "templates/order/only-ticket-order-creation.jsp";
	}

	self.chosenProducts = ko.observableArray([]);

	// 查看机票信息
	self.product = ko.observable({});
	self.airTickets = ko.observableArray([]);
	self.chargeMapping = {
		'PRODUCT' : '产品包票',
		'SALE' : '销售包票',
		'NONE' : '无机票'
	};
	self.checkAirTicket = function(product_pk) {

		$.getJSON(self.apiurl + 'product/searchProductAirTicketInfoByProductPk', {
			product_pk : product_pk
		}, function(data) {
			self.product(data.product);

			self.airTickets(data.air_tickets);
			airTicketCheckLayer = $.layer({
				type : 1,
				title : ['机票信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#air-ticket-check'
				},
				end : function() {
				}
			});
		});
	};

	self.products = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var param = $("form").serialize() + "&product.sale_flg=Y";
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchOnSaleProducts', param, function(data) {
			self.products(data.products);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			endLoadingIndicator();
		});
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

var ctx = new ProductBoxContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
