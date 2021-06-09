var checkChangeLayer;
var changeLayer;
var PassengerContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenPassengers = ko.observableArray([]);
	self.colors = ['#ffff99', '#ccffff', '#9999ff', '#00ffcc'];
	self.passengers = ko.observable({
		total : 0,
		items : []
	});
	self.allRoles = ['ADMIN', 'MANAGER', 'SALES', 'PRODUCT', 'FINANCE',
			'TICKET'];

	self.roleMapping = {
		'MANAGER' : '经理',
		'ADMIN' : '管理员',
		'SALES' : '销售人员',
		'PRODUCT' : '产品',
		'FINANCE' : '财务',
		'TICKET' : '票务'
	};

	self.statusMapping = {
		'Y' : '正常出票',
		'C' : '航变'
	}

	self.refresh = function() {
		startLoadingIndicator("加载中...");

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage + "&passenger.status=Y";
		$.getJSON(self.apiurl + 'ticket/searchAirTicketDoneNameListByPage',
				param, function(data) {
					self.passengers(data.airTicketNameList);
					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());

					$(".rmb").formatCurrency();

					$("#chk-all").attr('checked', false);

					var trs = $("#div-table").find("tbody").find("tr");
					var current_no = "";
					var current_index = 0;
					for (var i = 0; i < trs.length; i++) {
						var tr = $(trs[i]);
						if (i == 0) {
							current_no = $(tr).find("td[st='order-number']")
									.text();
						}
						var order_number = $(tr).find("td[st='order-number']")
								.text();

						if (order_number != current_no) {
							current_no = order_number;
							current_index += 1;
						}
						tr.find("td").css(
								"cssText",
								"background:" + self.colors[current_index % 4]
										+ " !important");

					}

					endLoadingIndicator();
				});
	};

	self.changeNames = ko.observableArray([]);
	self.changeTicketSources = ko.observableArray([]);

	// 航变处理
	self.flightChange = function() {
		if (self.chosenPassengers().length == 0) {
			fail_msg("请选择乘客！");
			return;
		} else {
			var same_team_number = "";
			var passenger_pks = "";
			for (var i = 0; i < ctx.chosenPassengers().length; i++) {
				var data = self.chosenPassengers()[i].split(":");
				var team_number = data[3];
				var status = data[6];
				if (status == "C") {
					fail_msg("不能选择已经有航变的乘客！");
					return;
				}
				if (i == 0) {
					same_team_number = team_number;
				} else {
					if (same_team_number != team_number) {
						fail_msg("只能选择同一团号下的乘客进行航变操作！");
						return;
					}
				}
				passenger_pks += "," + data[0];
			}
			passenger_pks = passenger_pks.LTrim(",");
			startLoadingIndicator("加载中...");
			var param = "passenger_pks_str=" + passenger_pks;
			$.getJSON(self.apiurl
					+ 'ticket/searchFlightChangeDataByPassengerPks', param,
					function(data) {
						endLoadingIndicator();
						self.changeNames(data.airTicketNameList);
						self.changeTicketSources(data.ticket_sources);

						changeLayer = $.layer({
							type : 1,
							title : ['乘客航变', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['800px', '700px'],
							offset : ['', ''],
							scrollbar : true,
							page : {
								dom : '#div-flight-change'
							},
							end : function() {
							}
						});
					});

		}
	}

	self.doFlightChange = function() {
		if (!$("#form-change").valid()) {
			return;
		}

		var json = '{';
		var team_number = self.chosenPassengers()[0].split(":")[3];
		var passenger_pks = '';
		for (var i = 0; i < self.changeNames().length; i++) {
			passenger_pks += ',' + self.changeNames()[i].pk;
		}

		passenger_pks = passenger_pks.LTrim(",");

		var change_reason = $("input[st='change-reason']").val();
		var cost = $("input[st='change-cost']").val() - 0;
		var comment = $("textarea[st='comment']").val();

		json += '"team_number":"' + team_number + '","passenger_pks":"'
				+ passenger_pks + '","change_reason":"' + change_reason
				+ '","cost":"' + cost + '","comment":"' + comment
				+ '","allot":[';

		var divs = $("#div-allot").children();

		var allotJson = '';
		var sumMoney = 0;
		for (var i = 0; i < divs.length; i++) {

			var current = divs[i];
			var ticket_source_pk = $(current).find(
					"input[st='ticket-source-pk']").val();

			var money = $(current).find("input[st='money']").val() - 0;
			sumMoney += money;
			allotJson += ',{"ticket_source_pk":"' + ticket_source_pk
					+ '","money":"' + money + '"}';
		}
		if (cost != sumMoney) {
			fail_msg("航变成本与分配总额不符！");
			return;

		}

		allotJson = allotJson.LTrim(",");

		json += allotJson + ']}';

		console.log(json);

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : "确认提交航变信息吗",
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {

					$.ajax({
						type : "POST",
						url : self.apiurl + 'ticket/changeFlight',
						data : "json=" + json
					}).success(function(str) {
						layer.close(index);
						if (str == "success") {
							layer.close(changeLayer);
							self.refresh();
						}
					});

				}
			}
		});

	}

	self.changeLog = ko.observable({});
	/**
	 * 查看航变信息
	 */
	self.checkChange = function(passenger_pk) {
		var param = "passenger_pk=" + passenger_pk;
		$.getJSON(self.apiurl + 'ticket/searchFlightChangeLogByPassengerPk',
				param, function(data) {
					self.changeLog(data.changeLog);

					checkChangeLayer = $.layer({
						type : 1,
						title : ['航变详情', ''],
						maxmin : false,
						closeBtn : [1, true],
						shadeClose : false,
						area : ['800px', '300px'],
						offset : ['', ''],
						scrollbar : true,
						page : {
							dom : '#div-check-change'
						},
						end : function() {
						}
					});
				});
	}

	self.cancelChange = function() {
		layer.close(changeLayer);
	}
	/**
	 * 将已出票名单打回到出票状态
	 */
	self.rollBack = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择需要打回的名单！");
		} else {
			$
					.layer({
						area : ['auto', 'auto'],
						dialog : {
							msg : "确认要将这些名单打回至待出票状态吗？",
							btns : 2,
							type : 4,
							btn : ['确认', '取消'],
							yes : function(index) {
								layer.close(index);
								startLoadingIndicator("打回中...");
								var param = "";
								for (var i = 0; i < self.chosenPassengers().length; i++) {
									var data = self.chosenPassengers()[i]
											.split(":");

									var status = data[6];
									console.log(status);
									if (status == "C") {
										layer.close(index);
										fail_msg("不能选择已经有航变的乘客！");
										return;
									}
									var pk = data[0];
									param += "passenger_pks=" + pk + "&";
								}

								$
										.ajax(
												{
													type : "POST",
													url : self.apiurl
															+ 'ticket/rollBackNameDone',
													data : param
												}).success(function(str) {
											endLoadingIndicator();
											if (str == "success") {
												self.refresh();
											} else {
												fail_msg("打回失败，请联系管理员！");
											}
										});

							}
						}
					});
		}
	}
	self.search = function() {

	};

	self.resetPage = function() {

	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 50;
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

var ctx = new PassengerContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	$('body').click(function(event) {
		var target = event.target;
		if (!$(target).hasClass("passenger-name")) {
			$('.deletePassenger').remove();
		}
	});

	var clipboard = new Clipboard('#copy', {
		text : function() {
			var text = "";
			if (ctx.chosenPassengers().length > 0) {
				for (var i = 0; i < ctx.chosenPassengers().length; i++) {
					var id = ctx.chosenPassengers()[i].split(":")[1];
					var name = ctx.chosenPassengers()[i].split(":")[2];
					text += id + "：" + name + "；\n";
				}
			}
			return text;
		}
	});

	clipboard.on('success', function(e) {
		success_msg("已经成功复制到剪切板！")
	});

	clipboard.on('error', function(e) {
		console.log('error');
	});
});
/**
 * 
 * @param tr
 */
function showDetail(tr) {
	console.log(tr);
	var style = $("<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td></tr><tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td></tr>");
	$(tr).after(style);
}

function checkAll(chk) {
	if ($(chk).is(":checked")) {
		for (var i = 0; i < ctx.passengers().length; i++) {
			var passenger = ctx.passengers()[i];
			ctx.chosenPassengers.push(passenger.pk + ":" + passenger.name + ":"
					+ passenger.id + ":" + passenger.team_number + ":"
					+ passenger.order_number + ":"
					+ passenger.name_confirm_status + ":" + passenger.status)
		}
	} else {
		for (var i = 0; i < ctx.passengers().length; i++) {
			var passenger = ctx.passengers()[i];
			ctx.chosenPassengers.remove(passenger.pk + ":" + passenger.name
					+ ":" + passenger.id + ":" + passenger.team_number + ":"
					+ passenger.order_number + ":"
					+ passenger.name_confirm_status + ":" + passenger.status)
		}
	}
}
function checkSameOrderNumber(tr) {
	var order_number = $(tr).find("td[st='order-number']").text();
	for (var i = 0; i < ctx.passengers().length; i++) {
		var passenger = ctx.passengers()[i];

		if (passenger.order_number == order_number) {
			var xxstr = passenger.pk + ":" + passenger.name + ":"
					+ passenger.id + ":" + passenger.team_number + ":"
					+ passenger.order_number + ":"
					+ passenger.name_confirm_status + ":" + passenger.status;

			if (ctx.chosenPassengers().contains(xxstr)) {
				ctx.chosenPassengers.remove(xxstr);
			} else {
				ctx.chosenPassengers.push(xxstr);
			}

		}

	}

}