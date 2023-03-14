var airTicketCheckLayer;
var passengerCheckLayer;
var createLayer;
var seasonTicketLayer;
var NeedContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenNeeds = ko.observableArray([]);

	// 生成机票订单
	self.createOrder = function() {
		if (self.chosenNeeds().length == 0) {
			fail_msg("请选择");
			return;
		} else if (self.chosenNeeds().length > 1) {
			fail_msg("只能选择一个");
			return;
		} else if (self.chosenNeeds().length == 1) {
			self.airTickets.removeAll();
			startLoadingIndicator("加载中...");
			$.getJSON(self.apiurl + 'ticket/selectOrderAirInfoByProductOrderNumber', {
				product_order_number : self.chosenNeeds()[0].split(";")[1]
			}, function(data) {
				self.airTickets(data.order_air_infos);
				endLoadingIndicator();
				createLayer = $.layer({
					type : 1,
					title : ['生成订单', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['1200px', '600px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#order-create'
					},
					end : function() {
					}
				});
			});

		}
	};
	self.deleteAirInfo = function(data, event) {
		var a = event.target;
		var tr = $(a).parent().parent();
		tr.remove();
	}
	// 确认生成订单
	self.doCreateOrder = function() {

		if ($("#txt-ticket-price").val().trim() == "") {
			fail_msg("请填写成人单价！");
			return;
		}
		if ($("#txt-ticket-special-price").val().trim() == "") {
			fail_msg("请填写儿童单价！");
			return;
		}

		var allLegTxt = $(".ticket-air-leg");
		var isSame = true;
		for (var i = 0; i < allLegTxt.length; i++) {
			var v = $(allLegTxt[i]).val().trim();
			if (v == "") {
				fail_msg("请填写票务航段！");
				return;
			}

			var old_leg = $(allLegTxt[i]).parent().prev().prev().text().trim();

			if (old_leg != v)
				isSame = false;
		}

		var confirm_msg = "确认要生成订单吗";

		if (!isSame) {
			confirm_msg = "票务航段与需求城市对不符，确认继续生成订单吗？"
		}

		var hasLeg = true;

		var tbody = $("#leg-table tbody");
		var trs = tbody.children();
		var legJson = '[';
		if (trs.length < 1) {
			hasLeg = false;
			confirm_msg = "没有航段信息，生成的订单将直接归入已操作订单，并且机票费用为0！确认要生成订单吗？";
		} else {
			for (var i = 0; i < trs.length; i++) {
				var tr = $(trs[i]);

				var ticket_number = $(tr).find("input[st='ticket-number']").val();

				var leg_from = tr.find("input[st='leg-from-city']").val();
				var leg_to = tr.find(':input[st="leg-to-city"]').val();

				var start_time = tr.find("input[st='start-time']").val();
				var end_time = tr.find("input[st='end-time']").val();

				var add_day_flg = tr.find("input[st='is-add-day']").is(":checked") ? "Y" : "N";

				var start_place = tr.find("input[st='start-place']").val();
				var end_place = tr.find("input[st='end-place']").val();

				var leg_index = tr.find(':input[st="leg-index"]').val();
				var leg_date = tr.find(':input[st="leg-date"]').val();

				legJson += '{"leg_index":"' + leg_index + '","leg_date":"' + leg_date + '","leg_from":"' + leg_from
						+ '","leg_to":"' + leg_to + '","ticket_number":"' + ticket_number + '","start_time":"'
						+ start_time + '","end_time":"' + end_time + '","add_day_flg":"' + add_day_flg
						+ '","start_place":"' + start_place + '","end_place":"' + end_place + '"}';

				if (i != trs.length - 1) {
					legJson += ',';
				}

			}
		}
		legJson += ']';

		var need_pk = self.chosenNeeds()[0].split(";")[0];
		var ticket_price = $("#txt-ticket-price").val();
		var ticket_special_price = $("#txt-ticket-special-price").val();

		var json = '{"need_pk":"' + need_pk + '","ticket_price":"' + ticket_price + '","ticket_special_price":"'
				+ ticket_special_price + '","legJson":' + legJson + '}';

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : confirm_msg,
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("保存中...");
					layer.close(createLayer);

					var data = "json=" + json;
					$.ajax({
						type : "POST",
						url : self.apiurl + 'ticket/createTicketOrder',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.chosenNeeds.removeAll();
							self.refresh();
						} else {
							fail_msg(str);
							self.chosenNeeds.removeAll();
						}
					});
				}
			}
		});
	};
	self.onlyTicket = function() {

	};
	self.airTickets = ko.observableArray([]);
	self.airBase = ko.observable([]);
	// 查看航段信息
	self.checkTicketPart = function(order_number) {
		self.airTickets.removeAll();
		console.log(order_number);
		startLoadingIndicator("加载中...");
		$.getJSON(self.apiurl + 'ticket/selectOrderAirInfoByProductOrderNumber', {
			product_order_number : order_number
		}, function(data) {
			self.airTickets(data.order_air_infos);
			endLoadingIndicator();
			airTicketCheckLayer = $.layer({
				type : 1,
				title : ['航段信息', ''],
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
	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(need_pk) {
		self.passengers.removeAll();
		startLoadingIndicator("加载中...");
		var url = "order/selectSaleOrderNameListByAirNeedPk";

		$.getJSON(self.apiurl + url, {
			need_pk : need_pk
		}, function(data) {

			self.passengers(data.passengers);
			endLoadingIndicator();
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['乘客信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#passengers-check'
				},
				end : function() {
				}
			});
		});
	};
	self.needs = ko.observable({
		total : 0,
		items : []
	});
	self.totalPeople = ko.observable();
	self.refresh = function() {
		startLoadingIndicator("加载中...");
		var totalPeople = 0;
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		param += "&airTicketNeed.ordered=N"
		$.getJSON(self.apiurl + 'ticket/searchAirTicketNeedByPage', param, function(data) {

			self.needs(data.airTicketNeeds);

			$(self.needs()).each(function(idx, data) {
				totalPeople += data.people_count - 0;
			});
			self.totalPeople(totalPeople);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			endLoadingIndicator();
		});
	};

	self.search = function() {

	};

	self.resetPage = function() {

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

	// 查询票务航段模块
	self.airLegs = ko.observable({});
	self.refreshAirLeg = function() {
		var param = "leg.city=" + $("#city").val();
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
	self.pickAirLeg = function(from, to) {
		$(currentAirLeg).val(from + "--" + to);
		var tr = $(currentAirLeg).parent().parent();
		tr.find(":input[st='leg-from-city']").val(from);
		tr.find(":input[st='leg-to-city']").val(to);
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
	// end pagination client

	/**
	 * 选择套票
	 */
	self.chooseSeasonTicket = function() {
		self.refreshSeasonTicket();
		seasonTicketLayer = $.layer({
			type : 1,
			title : ['选择套票', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['800px', '650px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#season-ticket-pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	}

	self.seasonTickets = ko.observableArray([]);
	/**
	 * 搜索套票
	 */
	self.refreshSeasonTicket = function() {
		var param = "base.name=" + $("#ticket-name").val();
		param += "&base.model=" + $("#ticket-model").val();

		param += "&page.start=" + self.startIndexSeason() + "&page.count=" + self.perPageSeason;

		$.getJSON(self.apiurl + 'ticket/searchSeasonTicketByPage', param, function(data) {
			self.seasonTickets(data.bases);

			self.totalCountSeason(Math.ceil(data.page.total / self.perPageSeason));
			self.setPageNumsSeason(self.currentPageSeason());
		});

	}

	self.pickSeasonTicket = function(data) {
		var trs = $("#leg-table tbody").children();
		var infos = data.infos;

		if (trs.length != infos.length) {
			fail_msg("套票航段数量与需求航段数量不匹配。")
			return;
		}

		var start_day = infos[0].start_day;
		var start_date = $(trs[0]).find("input[st='leg-date']").val();
		for (var i = 1; i < trs.length; i++) {
			var tr = trs[i];
			var air_date = $(trs[i]).find("input[st='leg-date']").val();

			var current_day = infos[i].start_day;

			var current_date = start_date.addDate(current_day - start_day);
			if (current_date != air_date) {
				fail_msg("套票天次和需求航段天次不匹配！");
				return;
			}
		}

		$("#txt-ticket-price").val(data.price);
		$("#txt-ticket-special-price").val(data.special_price);
		for (var i = 0; i < trs.length; i++) {
			var tr = trs[i];
			var info = infos[i];

			$(tr).find("input[st='ticket-number']").val(info.ticket_number);
			$(tr).find("input[st='ticket-air-leg']").val(info.ticket_leg);

			var from_to = info.ticket_leg.split("--");

			$(tr).find("input[st='leg-from-city']").val(from_to[0]);
			$(tr).find("input[st='leg-to-city']").val(from_to[1]);

			$(tr).find("input[st='start-time']").val(info.start_time);
			$(tr).find("input[st='end-time']").val(info.end_time);

			$(tr).find("input[st='is-add-day']").attr("checked", info.add_day_flg == 'Y' ? true : false);

			$(tr).find("input[st='start-place']").val(info.start_place);
			$(tr).find("input[st='end-place']").val(info.end_place);

		}

		layer.close(seasonTicketLayer);
	}

	// start pagination season ticket
	self.currentPageSeason = ko.observable(1);
	self.perPageSeason = 10;
	self.pageNumsSeason = ko.observableArray();
	self.totalCountSeason = ko.observable(1);
	self.startIndexSeason = ko.computed(function() {
		return (self.currentPageSeason() - 1) * self.perPageSeason;
	});
	self.resetPageSeason = function() {
		self.currentPageSeason(1);
	};

	self.previousPageSeason = function() {
		if (self.currentPageSeason() > 1) {
			self.currentPageSeason(self.currentPageSeason() - 1);
			self.refreshPageSeason();
		}
	};

	self.nextPageSeason = function() {
		if (self.currentPageSeason() < self.pageNumsSeason().length) {
			self.currentPageSeason(self.currentPageSeason() + 1);
			self.refreshPageSeason();
		}
	};

	self.turnPageSeason = function(pageIndexSeason) {
		self.currentPageSeason(pageIndexSeason);
		self.refreshPageSeason();
	};

	self.setPageNumsSeason = function(curPageSeason) {
		var startPageSeason = curPageSeason - 4 > 0 ? curPageSeason - 4 : 1;
		var endPageSeason = curPageSeason + 4 <= self.totalCountSeason() ? curPageSeason + 4 : self.totalCountSeason();
		var pageNumsSeason = [];
		for (var i = startPageSeason; i <= endPageSeason; i++) {
			pageNumsSeason.push(i);
		}
		self.pageNumsSeason(pageNumsSeason);
	};

	self.refreshPageSeason = function() {
		self.refreshSeasonTicket();
	};
};

var ctx = new NeedContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

var currentAirLeg;
var airLegLayer;
function choseAirLeg(event) {
	ctx.searchAirLeg();
	airLegLayer = $.layer({
		type : 1,
		title : ['选择票务航段', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['600px', '650px'],
		offset : ['', ''],
		scrollbar : true,
		page : {
			dom : '#air-leg-pick'
		},
		end : function() {
			console.log("Done");
		}
	});

	currentAirLeg = event.target;
	$(currentAirLeg).blur();
}
