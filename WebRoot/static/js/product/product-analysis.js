var costLayer;
var valueLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCharge = ko.observable();
	// self.locations = [ "云南", "华东", "桂林", "张家界", "四川", "其他" ];
	self.locations = ko.observableArray();

	$.getJSON(self.apiurl + 'system/searchByType', {
		type : "LINE"
	}, function(data) {
		self.locations(data.datas);
	});
	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});

	self.product = ko.observable({});

	self.status = ['N', 'Y', 'D'];
	self.saleMapping = {
		'N' : "架下",
		'Y' : "架上",
		'D' : "废弃"
	};
	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("N");
	self.chosenStatuses.push("Y");

	self.chosenProducts = ko.observableArray([]);
	// 成本编辑
	self.costEdit = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("维护只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			$.getJSON(self.apiurl + 'product/searchProductByPk', {
				product_pk : self.chosenProducts()[0]
			}, function(data) {
				self.product(data.product);

				if (self.product().cash_flow_air_flg == "Y")
					$("#chk-air-ticket").attr("checked", true);

				if (self.product().cash_flow_local_flg == "Y")
					$("#chk-local").attr("checked", true);

				if (self.product().cash_flow_other_flg == "Y")
					$("#chk-other").attr("checked", true);
				caculateGrossProfit();
				costLayer = $.layer({
					type : 1,
					title : ['成本编辑', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['800px', '500px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#cost-update'
					},
					end : function() {
					}
				});
			});

		}
	};

	self.valueEdit = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("维护只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			$.getJSON(self.apiurl + 'product/searchProductByPk', {
				product_pk : self.chosenProducts()[0]
			}, function(data) {
				self.product(data.product);
				valueLayer = $.layer({
					type : 1,
					title : ['分值编辑', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['800px', '600px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#value-update'
					},
					end : function() {
					}
				});
			});

		}
	}

	self.products = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;

		$.getJSON(self.apiurl + 'product/searchProductsByPage', param,
				function(data) {
					self.products(data.products);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
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
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self
				.totalCount();
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

var ctx = new ProductContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

function caculateGrossProfit() {
	var adult_price = $("#adult-price").val() - 0;
	var child_price = $("#child-price").val() - 0;
	var business_profit_substract = $("#business-profit-substract").val() - 0;
	var max_profit_substract = $("#max-profit-substract").val() - 0;

	// 产品价格
	var product_price = adult_price - business_profit_substract
			- max_profit_substract;

	var product_child_price = child_price - business_profit_substract
			- max_profit_substract;

	var local_adult_cost = $("#local-adult-cost").val() - 0;
	var air_ticket_cost = $("#air-ticket-cost").val() - 0;
	var other_cost = $("#other-cost").val() - 0;

	var local_child_cost = $("#local-child-cost").val() - 0;
	var air_ticket_child_cost = $("#air-ticket-child-cost").val() - 0;
	var other_child_cost = $("#other-child-cost").val() - 0;

	// 毛利
	var gross_profit = product_price - local_adult_cost - air_ticket_cost
			- other_cost;
	var gross_child_profit = product_child_price - local_child_cost
			- air_ticket_child_cost - other_child_cost;

	$("#gross-profit").text(gross_profit);
	$("#txt-gross-profit").val(gross_profit);

	$("#gross-child-profit").text(gross_child_profit);
	$("#txt-gross-child-profit").val(gross_child_profit);

	// 毛利率
	var gross_profit_rate = 0;
	if (adult_price - business_profit_substract != 0) {
		gross_profit_rate = parseFloat((gross_profit / (adult_price - business_profit_substract))
				.toFixed(2));
		gross_profit_rate = Math.round(gross_profit_rate * 100);
	}

	var gross_child_profit_rate = 0;
	if (child_price - business_profit_substract != 0) {
		gross_child_profit_rate = parseFloat((gross_child_profit / (child_price - business_profit_substract))
				.toFixed(2));
		gross_child_profit_rate = Math.round(gross_child_profit_rate * 100);
	}

	$("#gross-profit-rate").text(gross_profit_rate + "%");
	$("#txt-gross-profit-rate").val(gross_profit_rate);

	$("#gross-child-profit-rate").text(gross_child_profit_rate + "%");
	$("#txt-gross-child-profit-rate").val(gross_child_profit_rate);

	// 现付资金
	var spot_cash = 0;
	var spot_child_cash = 0;
	if ($("#chk-air-ticket").is(":checked")) {
		spot_cash += air_ticket_cost;
		spot_child_cash += air_ticket_child_cost;
	}

	if ($("#chk-local").is(":checked")) {
		spot_cash += local_adult_cost;
		spot_child_cash += local_child_cost;
	}

	if ($("#chk-other").is(":checked")) {
		spot_cash += other_cost;
		spot_child_cash += other_child_cost;
	}

	$("#spot-cash").text(spot_cash)
	$("#txt-spot-cash").val(spot_cash);

	$("#spot-child-cash").text(spot_child_cash)
	$("#txt-spot-child-cash").val(spot_child_cash);

	// 现金流
	var cash_flow = product_price - spot_cash;
	var cash_child_flow = product_child_price - spot_child_cash;

	$("#cash-flow").text(cash_flow);
	$("#txt-cash-flow").val(cash_flow);

	$("#cash-child-flow").text(cash_child_flow);
	$("#txt-cash-child-flow").val(cash_child_flow);
}

function updateCost() {
	if (!$("#form-cost").valid())
		return;
	layer.close(costLayer);
	startLoadingIndicator("保存中！");
	var data = $("#form-cost").serialize();
	data += "&product.analysis_flg=Y";
	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'product/updateProductDirectly',
		data : data
	}).success(function(str) {
		endLoadingIndicator();
		if (str == "success") {
			ctx.refresh();
			ctx.chosenProducts.removeAll();
		} else {
			fail_msg(str);
		}
	});
}
function cancelUpdateCost() {
	layer.close(costLayer);
}

function updateValue() {
	if (ctx.product().sale_flg == "Y") {
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '系统检测到此为架上产品，更新分值将于次日凌晨生效！',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					doUpdateValue();
				}
			}
		});
	} else {
		doUpdateValue();
	}
}

function doUpdateValue() {
	if (!$("#form-value").valid())
		return;
	layer.close(valueLayer);
	startLoadingIndicator("保存中！");
	var data = $("#form-value").serialize();
	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'product/updateProductValue',
		data : data
	}).success(function(str) {
		endLoadingIndicator();
		if (str == "success") {
			ctx.refresh();
			ctx.chosenProducts.removeAll();
		} else if (str == "more_update") {
			fail_msg("一天之内只能更新三次！")
		} else {
			fail_msg(str);
		}
	});
}
function cancelUpdateValue() {
	layer.close(valueLayer);
}
