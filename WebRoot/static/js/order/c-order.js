var confirmCheckLayer;
var commentLayer;
var passengerCheckLayer;
var firstAirLayer;
var ProductBoxContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.locations = [ "云南", "华东", "桂林", "张家界", "四川", "其他" ];
	self.chosenOrders = ko.observableArray([]);
	self.confirmDates = ([ {
		"en" : "today",
		"cn" : "今天"
	}, {
		"en" : "thisweek",
		"cn" : "本周"
	}, {
		"en" : "lastweek",
		"cn" : "上周"
	} ])

	self.statusMapping = {
		"no" : "未出团",
		"yes" : "出团中",
		"back" : "已回团"
	}
	// 销售信息
	self.sales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
	});
	// 删除订单
	self.finalOrder = function() {
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
				window.location.href = self.apiurl + "templates/order/standard-order-final-create.jsp?key=" + order_pk;
			} else if (standard_flg == "N") {
				window.location.href = self.apiurl + "templates/order/non-standard-order-final-create.jsp?key=" + order_pk;
			}
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
	// 打回订单到未确认状态
	self.rollBackOrder = function() {
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
					msg : '确认要打回此订单吗？',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("打回中！");
						var data = "order_pk=" + order_pk + "&standard_flg=" + standard_flg;
						$.ajax({
							type : "POST",
							url : self.apiurl + 'order/rollBackCOrder',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenOrders.removeAll();
							} else if (str == "product") {
								fail_msg("订单产品已操作，请联系产品经理！");
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	}

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
	self.writeFirstAir = function() {
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
				caculate_fly_time();
				firstAirLayer = $.layer({
					type : 1,
					title : [ '首航段信息', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '1300px', '200px' ],
					offset : [ '', '' ],
					scrollbar : true,
					page : {
						dom : '#first-air'
					},
					end : function() {
						console.log("Done");
					}
				});

			});
		}
	}
	self.cancelWrite = function() {
		layer.close(firstAirLayer);
		$("#txt-order-pk").val('');
		$("#txt-standard-flg").val('');
		$("#txt-comment").val('');
		$("#txt-ticket-number").val('');
		$("#txt-start-city").val('');
		$("#txt-start-airport").val('');
		$("#txt-end-city").val('');
		$("#txt-end-airport").val('')
		$("#txt-off-time").val('');
		$("#txt-land-time").val('');
	};
	self.confirmWrite = function() {
		var order_pk = $("#txt-order-pk").val();
		var standard_flg = $("#txt-standard-flg").val();

		var ticket_number = $("#txt-ticket-number").val();
		var start_city = $("#txt-start-city").val();
		var start_airport = $("#txt-start-airport").val();
		var end_city = $("#txt-end-city").val();
		var end_airport = $("#txt-end-airport").val()
		var off_time = $("#txt-off-time").val();
		var land_time = $("#txt-land-time").val();
		var next_day = 0;

		if ($("#chk-next-day").is(":checked")) {
			next_day = $("#txt-next-day").val();
			if (next_day.trim() == "") {
				fail_msg("请填写所隔天数！");
				return;
			}
		}
		var param = "";
		var data = "";
		if (standard_flg == "Y") {
			param = "bsOrder";
		} else {
			param = "bnsOrder";
		}

		data = param + ".pk=" + order_pk + "&" + param + ".ticket_number=" + ticket_number + "&" + param + ".start_city=" + start_city + "&" + param + ".start_airport=" + start_airport + "&" + param
				+ ".end_city=" + end_city + "&" + param + ".end_airport=" + end_airport + "&" + param + ".off_time=" + off_time + "&" + param + ".land_time=" + land_time + "&" + param + ".next_day="
				+ next_day + "&standard_flg=" + standard_flg;

		startLoadingIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + "order/updateComment",
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				layer.close(firstAirLayer);
				$("#txt-order-pk").val('');
				$("#txt-standard-flg").val('');
				$("#txt-comment").val('');
				$("#txt-ticket-number").val('');
				$("#txt-start-city").val('');
				$("#txt-start-airport").val('');
				$("#txt-end-city").val('');
				$("#txt-end-airport").val('')
				$("#txt-off-time").val('');
				$("#txt-land-time").val('');
			}
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
		$.getJSON(self.apiurl + 'order/searchCOrdersByPage', param, function(data) {
			self.orders(data.tbcOrders);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	// 查看确认件
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
		$("input[st='st-date-1']").prop("disabled", false);
		$("select[st='st-date-2']").val("today");
		$("select[st='st-date-2']").prop("disabled", true);
	} else {
		$("input[st='st-date-1']").prop("disabled", true);
		$("select[st='st-date-2']").prop("disabled", false);
		$("input[st='st-date-1']").val("");
	}
};
var nextDay = function() {
	if ($("#chk-next-day").is(":checked")) {
		$("#txt-next-day").attr("disabled", false);
	} else {
		$("#txt-next-day").attr("disabled", true);
	}
	caculate_fly_time();
}

var caculate_fly_time = function() {
	var off_time = $("#txt-off-time").val();
	var land_time = $("#txt-land-time").val();
	var next_day = $("#txt-next-day").val() - 0;
	if (off_time == "" || land_time == "" || off_time.length != 5 || land_time.length != 5)
		return;
	var off_time = "1988-03-22 " + off_time + ":00";
	var land_time = "1988-03-22 " + land_time + ":00";
	var date1 = new Date(off_time);
	var date2 = new Date(land_time);
	if (date2.getTime() <= date1.getTime()) {
		$("#chk-next-day").prop("checked", true);
		$("#txt-next-day").attr("disabled", false);
	}
	if ($("#chk-next-day").is(":checked")) {
		date2 = date2.addDate(next_day);
	}
	$("#txt-fly-time").text(dateDiff(date1, date2));
}