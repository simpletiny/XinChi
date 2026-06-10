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

	self.checkDishonest = function() {
		window.location.href = self.apiurl + "templates/system/dishonest.jsp";
	}

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
let names;
let nameCheckLayer;
var ctx = new ProductBoxContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	names = JSON.parse(sessionStorage.getItem('ok_names'));
	if (names != null) {
		$("#btn-check-names").show();
	}
});

function checkNames() {
	let tbody = $("#tbody-name");
	tbody.html('');
	names.forEach(function(name) {
		let tr = $(`<tr>
				<td>${name.name}</td>
				<td>${name.id}</td>
			</tr>`);
		tbody.append(tr);
	})
	
	nameCheckLayer = $.layer({
		type : 1,
		title : ['已校验名单', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['600px', '550px'],
		offset : ['', ''],
		scrollbar : true,
		page : {
			dom : '#div-name-check'
		},
		end : function() {
		}
	});
}

function deleteNames(){
	$.layer({
		area : ['auto', 'auto'],
		dialog : {
			msg : "确定要删除已校验的名单记录吗？",
			btns : 2,
			type : 4,
			btn : ['确认', '取消'],
			yes : function(index) {
				layer.close(index);
				startLoadingIndicator("删除中");
				sessionStorage.removeItem("ok_names");
				layer.close(nameCheckLayer);
				$("#btn-check-names").hide();
				endLoadingIndicator();
				success_msg("删除成功！");
			}
		}
	});
}
