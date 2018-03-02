var confirmCheckLayer;
var commentLayer;
var passengerCheckLayer;
var finalCheckLayer;
var ProductBoxContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.locations = [ "云南", "华东", "桂林", "张家界", "四川", "其他" ];
	self.chosenOrders = ko.observableArray([]);
	self.statusMapping = {
		"I" : "决算中",
		"N" : "被驳回",
		"Y" : "正常",
		"C" : "取消",
		"O" : "投诉",
		"H" : "变更"
	};
	// 销售信息
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});

	self.order = ko.observable({});
	// 添加/修改备注
	self.editComment = function(order_pk, standard_flg) {
		var url = "";
		if (standard_flg == "Y") {
			url = "order/searchTbcBsOrderByPk";
		} else {
			url = "order/searchTbcBnsOrderByPk";
		}

		$.getJSON(self.apiurl + url, {
			order_pk : order_pk
		}, function(data) {
			if (standard_flg == "Y") {
				self.order(data.bsOrder);
			} else {
				self.order(data.bnsOrder);
			}

			commentLayer = $.layer({
				type : 1,
				title : [ '备注', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '500px', '300px' ],
				offset : [ '50px', '' ],
				scrollbar : true,
				page : {
					dom : '#comment-edit'
				},
				end : function() {
					console.log("Done");
				}
			});

		});
	};

	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(data, event) {
		self.passengers.removeAll();
		var team_number = data.team_number;
		var url = "order/selectSaleOrderNameListByTeamNumber";

		$.getJSON(self.apiurl + url, {
			team_number : team_number
		}, function(data) {
			self.passengers(data.passengers);
			passengerCheckLayer = $.layer({
				type : 1,
				title : [ '游客信息', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '800px', '500px' ],
				offset : [ '', '' ],
				scrollbar : true,
				page : {
					dom : '#passengers-check'
				},
				end : function() {
				}
			});
		});
	};

	self.cancelEditComment = function() {
		layer.close(commentLayer);

		$("#txt-order-pk").val('');
		$("#txt-standard-flg").val('');
		$("#txt-comment").val('');
	};

	self.updateComment = function() {
		var order_pk = $("#txt-order-pk").val();
		var standard_flg = $("#txt-standard-flg").val();
		var comment = $("#txt-comment").val();
		var param = "";
		var data = "";
		if (standard_flg == "Y") {
			param = "bsOrder";
		} else {
			param = "bnsOrder";
		}

		data = param + ".pk=" + order_pk + "&" + param + ".comment=" + comment + "&standard_flg=" + standard_flg;
		startLoadingIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + "order/updateComment",
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
				layer.close(commentLayer);
				$("#txt-order-pk").val('');
				$("#txt-standard-flg").val('');
				$("#txt-comment").val('');
			}
		});
	};

	self.orders = ko.observable({});

	self.refresh = function() {

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		startLoadingSimpleIndicator("加载中...");
		$.getJSON(self.apiurl + 'order/searchFOrdersByPage', param, function(data) {
			self.orders(data.tbcOrders);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			endLoadingIndicator();
		});
	};
	/**
	 * 打回重报
	 */
	self.rollBack = function() {
		if (self.chosenOrders().length == 0) {
			fail_msg("请选择订单！");
			return;
		} else if (self.chosenOrders().length > 1) {
			fail_msg("只能选择一个订单！");
			return;
		} else if (self.chosenOrders().length == 1) {

			var current = self.chosenOrders()[0];
			var order_pk = current.pk;
			var standard_flg = current.standard_flg;
			var data = "order_pk=" + order_pk + "&standard_flg=" + standard_flg;
			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '确认要打回重报吗？',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						startLoadingSimpleIndicator("打回中...");
						$.ajax({
							type : "POST",
							url : self.apiurl + "order/rollBackFinalOrder",
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								layer.close(index);
								self.chosenOrders.removeAll();
							}
						});
					}
				}
			});
		}
	};

	// 查看决算单
	self.checkConfirmPic = function(fileName, team_number) {
		$("#img-pic").attr("src", "");
		confirmCheckLayer = $.layer({
			type : 1,
			title : [ '查看决算单', '' ],
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

		$("#img-pic").attr("src", self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=CLIENT_FINAL&subFolder=" + team_number);
	};
	// 查看凭证
	self.checkVoucherPic = function(fileName, team_number) {
		$("#img-pic").attr("src", "");
		confirmCheckLayer = $.layer({
			type : 1,
			title : [ '查看凭证', '' ],
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

		$("#img-pic").attr("src", self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=CLIENT_FINAL_VOUCHER&subFolder=" + team_number);
	};

	// 新标签页显示大图片
	$("#img-pic").on('click', function() {
		window.open(self.apiurl + "templates/common/check-picture-big.jsp?src=" + encodeURIComponent($(this).attr("src")));
	});
	self.detailMsg = ko.observable();
	// 查看决算详情
	self.checkFinalDetail = function(data) {
		self.order(data);
		switch (data.final_type) {
		case "1":
			self.detailMsg("无变化");
			$("#div-1").show();
			$("#div-2").hide();
			$("#div-3").hide();
			break;
		case "2":
			$("#div-1").hide();
			$("#div-2").show();
			$("#div-3").hide();
			break;
		case "3":
			$("#div-1").hide();
			$("#div-2").hide();
			$("#div-3").show();
			break;
		case "4":
			self.detailMsg("订单取消");
			$("#div-1").show();
			$("#div-2").hide();
			$("#div-3").hide();
			break;
		default:
			self.detailMsg("更新前数据");
			$("#div-1").show();
			$("#div-2").hide();
			$("#div-3").hide();
			break;
		}
		finalCheckLayer = $.layer({
			type : 1,
			title : [ '查看决算详情', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '450px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#final-check'
			},
			end : function() {
				console.log("Done");
			}
		});
	};
	// 下载相关文件
	self.downloadFile = function(data, event) {
		$('.download-panel').remove();
		var label = event.target;
		var X = $(label).offset().top;
		var Y = $(label).offset().left;
		var div = $('<div></div>');
		var departure_notice = $('<a href="#" style="cursor:pointer;margin-right:10px">出团通知</a>');
		var supplier_confirm = $('<a href="#" style="cursor:pointer">地接确认</a>');

		$(div).append(departure_notice);
		$(div).append(supplier_confirm);

		$(div).addClass("download-panel");
		$(div).css({
			'top' : X + 20,
			'left' : Y - 70
		});
		$('body').append(div);

		$(departure_notice).click({
			label : data
		}, function(label) {

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

	$('body').click(function(event) {
		var target = event.target;
		if (!$(target).hasClass("download")) {
			$('.download-panel').remove();
		}
	});
});
var check = function(radio) {
	var r = $(radio).val();
	if (r == "1") {
		$("input[st='st-date-1']").attr("disabled", false);
		$("input[st='st-date-2']").val("");
		$("input[st='st-date-2']").attr("disabled", true);
	} else {
		$("input[st='st-date-1']").attr("disabled", true);
		$("input[st='st-date-2']").attr("disabled", false);
		$("input[st='st-date-1']").val("");
	}
};
