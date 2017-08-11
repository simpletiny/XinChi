var confirmCheckLayer;
var ProductBoxContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.locations = [ "云南", "华东", "桂林", "张家界", "四川", "其他" ];
	self.chosenOrders = ko.observableArray([]);
	// 销售信息
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});

	// 删除订单
	self.deleteOrder = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else if (self.chosenOrders().length == 1) {
			var data = self.chosenOrders()[0].split(";");
			var order_pk = data[0];
			var standard_flg = data[1];
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '确认要删除此订单吗？',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("删除中！");
						var data = "order_pk=" + order_pk + "&standard_flg=" + standard_flg;
						$.ajax({
							type : "POST",
							url : self.apiurl + 'order/deleteTbcOrder',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenOrders.removeAll();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	};
	// 编辑订单
	self.editOrder = function() {

		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else if (self.chosenOrders().length == 1) {
			var data = self.chosenOrders()[0].split(";");
			var order_pk = data[0];
			var standard_flg = data[1];
			if (standard_flg == "Y") {
				window.location.href = self.apiurl + "templates/order/standard-order-edit.jsp?key=" + order_pk;
			} else if (standard_flg == "N") {
				window.location.href = self.apiurl + "templates/order/non-standard-order-edit.jsp?key=" + order_pk;
			}
		}
	};

	// 确认订单
	self.confirmOrder = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenOrders().length == 1) {
			var data = self.chosenOrders()[0].split(";");
			var order_pk = data[0];
			var standard_flg = data[1];
			if (standard_flg == "Y") {
				window.location.href = self.apiurl + "templates/order/standard-order-confirm.jsp?key=" + order_pk;
			} else if (standard_flg == "N") {
				window.location.href = self.apiurl + "templates/order/non-standard-order-confirm.jsp?key=" + order_pk;
			}
		}
	};

	self.orders = ko.observable({});

	self.refresh = function() {

		var param = $("form").serialize();

		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'order/searchTbcOrdersByPage', param, function(data) {
			self.orders(data.tbcOrders);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	// 查看身份证图片
	self.checkIdPic = function(fileName, user_number) {
		$("#img-pic").attr("src", "");
		confirmCheckLayer = $.layer({
			type : 1,
			title : [ '查看确认件', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});

		$("#img-pic").attr("src", self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=CLIENT_CONFIRM&subFolder=" + user_number);
	};
	// 新标签页显示大图片
	$("#img-pic").on('click', function() {
		window.open(self.apiurl + "templates/common/check-picture-big.jsp?src=" + encodeURIComponent($(this).attr("src")));
	});
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

var ctx = new ProductBoxContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
