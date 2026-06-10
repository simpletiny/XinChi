var ticketLayer;
let checkAirNeedLayer;
var PassengerContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenPassengers = ko.observableArray([]);
	self.colors = ['#ffff99', '#ccffff', '#9999ff', '#00ffcc'];
	self.passengers = ko.observable({
		total: 0,
		items: []
	});

	self.flight = ko.observableArray([]);

	self.operate = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择乘客！");
			return;
		} else {
			// 验证是否是相同产品单号
			if (!allOrderNumbersSame(self.chosenPassengers())) {
				fail_msg("所选名单不属于同一产品订单！");
				return;
			}

			ticketLayer = $.layer({
				type: 1,
				title: ['票务信息', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['800px', 'auto'],
				offset: ['', ''],
				page: {
					dom: '#air-ticket-edit'
				},
				end: function() {
				}
			});
		}
	}
	// 发送票务需求
	self.doSendAir = function() {
		var msg = "确定提交票务需求吗?";
		var tbody = $("#table-ticket tbody");

		var allTrs = tbody.children();
		for (var i = 0; i < allTrs.length; i++) {
			var current = allTrs[i];
			var start_day = $(current).find("[st='start-day']").val().trim();
			var from_to_city = $(current).find("[st='from-to-city']").val().trim();

			if (start_day == "" || from_to_city == "") {
				fail_msg("请填写必须填写的项目！");
				return;
			}
		}
		let hasTicket = "YES";

		$.layer({
			area: ['auto', 'auto'],
			dialog: {
				msg: msg,
				btns: 2,
				type: 4,
				btn: ['确认', '取消'],
				yes: function(index) {
					layer.close(index);
					startLoadingIndicator("保存中...");
					let name_pks = new Array();
					self.chosenPassengers().every(item => name_pks.push(item.pk));
					let json_obj = {};
					json_obj.has_ticket = hasTicket;
					json_obj.name_pks = name_pks;
					if (hasTicket === "YES") {
						json_obj.air_comment = $(".air_comment").val().replace(/\n/g, ";");
						let datas = new Array();

						var allTrs = tbody.children();
						for (var i = 0; i < allTrs.length; i++) {
							var current = allTrs[i];
							let data = {};
							data.flight_index = i + 1;
							data.flight_number = $(current).find("[st='flight-number']").val().trim();
							data.start_day = $(current).find("[st='start-day']").val().trim();
							data.from_to_city = $(current).find("[st='from-to-city']").val().trim();
							datas.push(data);
						}
						json_obj.data = datas;
					}

					var param = "json=" + JSON.stringify(json_obj);

					$.ajax({
						type: "POST",
						url: self.apiurl + 'product/sendAirTicketNeed',
						data: param
					}).success(function(str) {
						layer.close(ticketLayer);
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
							self.chosenPassengers.removeAll();
						} else {
							fail_msg("提交失败，请联系管理员！");
						}

					});
				}
			}
		});

	}

	self.cancelSendAir = function() {
		layer.close(ticketLayer);
	}

	self.flight_segments = ko.observableArray();

	self.checkAirNeed = function(name_pk) {
		// 打印转换后的数组
		$.getJSON(self.apiurl + 'product/searchAirNeedByNamePk', { name_pk: name_pk }, function(data) {
			self.flight_segments(Object.keys(data.flight_segments).map(function(key) {
				return { key: key, value: data.flight_segments[key] };
			}));

			checkAirNeedLayer = $.layer({
				type: 1,
				title: ['已发票务需求', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['800px', 'auto'],
				offset: ['', ''],
				page: {
					dom: '#div-check-air-need'
				},
				end: function() {

				}
			});
		});
	}
	self.refresh = function() {
		// "&name_option.operate_status="
		startLoadingIndicator("加载中...");
		var param = $("form").serialize();
		param += "&name_option.operate_statuses=Y&name_option.operate_statuses=D"
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'product/searchProductOrderNameByPage', param, function(data) {
			self.passengers(data.name_list);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			$("#chk-all").attr('checked', false);

			var trs = $("#div-table").find("tbody").find("tr");
			var current_no = "";
			var current_index = 0;
			for (var i = 0; i < trs.length; i++) {
				var tr = $(trs[i]);
				if (i == 0) {
					current_no = $(tr).find("td[st='order-number']").text();
				}
				var order_number = $(tr).find("td[st='order-number']").text();

				if (order_number != current_no) {
					current_no = order_number;
					current_index += 1;
				}
				tr.find("td").css("cssText", "background:" + self.colors[current_index % 4] + " !important");
			}
			$("span:contains('是')").css("color", "red");
			$("span:contains('否')").css("color", "green");

			endLoadingIndicator();
		});
	};

	self.search = function() {

	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 80;
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
	// 查询票务航段模块
	self.airLegs = ko.observable({});
	self.refreshAirLeg = function() {
		var param = "leg.city=" + $("#city").val();
		param += "&leg.use_flgs=Y";
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;
		$.getJSON(self.apiurl + 'ticket/searchAirLegsByPage', param, function(data) {
			self.airLegs(data.legs);

			self.totalCount1(Math.ceil(data.page.total / self.perPage1));
			self.setPageNums1(self.currentPage1());
		});
	};

	self.searchAirLeg = function() {
		self.refreshAirLeg();
	};
	self.pickAirLeg = function(data) {
		$(currentAirLeg).val(data.from_city + "-" + data.to_city);
		layer.close(airLegLayer);
	};

	// start pagination air leg
	self.currentPage1 = ko.observable(1);
	self.perPage1 = 10;
	self.pageNums1 = ko.observableArray();
	self.totalCount1 = ko.observable(1);
	self.startIndex1 = ko.computed(function() {
		return (self.currentPage1() - 1) * self.perPage1;
	});
	self.resetPage1 = function() {
		self.currentPage1(1);
	};

	self.previousPage1 = function() {
		if (self.currentPage1() > 1) {
			self.currentPage1(self.currentPage1() - 1);
			self.refreshPage1();
		}
	};

	self.nextPage1 = function() {
		if (self.currentPage1() < self.pageNums1().length) {
			self.currentPage1(self.currentPage1() + 1);
			self.refreshPage1();
		}
	};

	self.turnPage1 = function(pageIndex1) {
		self.currentPage1(pageIndex1);
		self.refreshPage1();
	};

	self.setPageNums1 = function(curPage1) {
		var startPage1 = curPage1 - 4 > 0 ? curPage1 - 4 : 1;
		var endPage1 = curPage1 + 4 <= self.totalCount1() ? curPage1 + 4 : self.totalCount1();
		var pageNums1 = [];
		for (var i = startPage1; i <= endPage1; i++) {
			pageNums1.push(i);
		}
		self.pageNums1(pageNums1);
	};

	self.refreshPage1 = function() {
		self.refreshAirLeg();
	};
	// end pagination air leg
};

var ctx = new PassengerContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

function checkAll(chk) {
	if ($(chk).is(":checked")) {
		ctx.chosenPassengers.push(...ctx.passengers());
	} else {
		ctx.chosenPassengers.removeAll();
	}
}

function checkSameOrderNumber(tr) {
	var order_number = $(tr).find("td[st='order-number']").text();
	for (var i = 0; i < ctx.passengers().length; i++) {
		var passenger = ctx.passengers()[i];
		if (passenger.product_order_number == order_number) {
			ctx.chosenPassengers.push(passenger);
		}
	}
}
function addRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;
	if (index == 10) {
		fail_msg("最多添加10个航段！")
		return;
	}
	var tr = $('<tr><td st="index"></td><td><input st="start-day" type="number" min="1" maxlength="2" /></td><td><input st="flight-number" type="text"/></td><td><input st="from-to-city" type="text" onclick="choseAirLeg(event)"/></td></td><td><input type="button" value="-" onclick="deleteRow(this)"></input></td></tr>');

	$(tr).find("td[st='index']").text(alphabetMap[index + 1]);
	tbody.append(tr);
}

function deleteRow(txt) {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;
	if (index == 1)
		return;
	$(txt).parent().parent().remove();
	var ins = $(tbody).find("td[st='index']");
	for (var i = 0; i < ins.length; i++) {
		$(ins[i]).text(alphabetMap[i + 1]);
	}
}
/* 切换是否有机票信息 */
function hasTicket(chk) {
	if ($(chk).is(":checked")) {
		$("#air-ticket").hide();
		$("#air-comment").hide();
	} else {
		$("#air-ticket").show();
		$("#air-comment").show();
	}

}
function allOrderNumbersSame(array) {
	return array.every(item => item.product_order_number === array[0].product_order_number);
}

var airLegLayer;
function choseAirLeg(event) {
	ctx.searchAirLeg();
	airLegLayer = $.layer({
		type: 1,
		title: ['选择票务航段', ''],
		maxmin: false,
		closeBtn: [1, true],
		shadeClose: false,
		area: ['600px', '650px'],
		offset: ['', ''],
		scrollbar: true,
		page: {
			dom: '#air-leg-pick'
		},
		end: function() {
			console.log("Done");
		}
	});

	currentAirLeg = event.target;
	$(currentAirLeg).blur();
}

function deleteSendedAirNeed(data) {
	const need_count = ctx.flight_segments().length;
	const air_need_pk = data.value[0].need_pk;
	let msg = "";
	if (need_count === 1) {
		msg = "只有一个已发送票务需求，删除后名单将会打回至待发票务状态。";
	} else {
		msg = "确认要删除当前票务需求吗？";
	}
	$.layer({
		area: ['auto', 'auto'],
		dialog: {
			msg: msg,
			btns: 2,
			type: 4,
			btn: ['确认', '取消'],
			yes: function(index) {
				layer.close(index);
				startLoadingIndicator("删除中");
				var param = "air_need_pk=" + air_need_pk;
				$.ajax({
					type: "POST",
					url: ctx.apiurl + 'product/deleteSendedAirNeed',
					data: param
				}).success(function(str) {
					endLoadingIndicator();
					if (str == "success") {
						layer.close(checkAirNeedLayer);
						ctx.refresh();
					} else {
						fail_msg(str);
					}
				});
			}
		}
	});

}