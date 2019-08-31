var airTicketCheckLayer;
var passengerCheckLayer;
var createLayer;
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
			$.getJSON(self.apiurl + 'ticket/searchOrderAirInfoByTeamNumber', {
				team_number : self.chosenNeeds()[0]
			}, function(data) {
				self.airTickets(data.order_air_infos);
				endLoadingIndicator();
				createLayer = $.layer({
					type : 1,
					title : [ '生成订单', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '800px', '500px' ],
					offset : [ '', '' ],
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

		var allLegTxt = $(".ticket-air-leg");
		for (var i = 0; i < allLegTxt.length; i++) {
			var v = $(allLegTxt[i]).val().trim();
			if (v == "") {
				fail_msg("请填写票务航段！");
				return;
			}
		}

		var confirm_msg = "确认要生成订单吗";
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
				var leg_index = tr.find(':input[st="leg-index"]').val();
				var leg_date = tr.find(':input[st="leg-date"]').val();
				var leg_from = tr.find(':input[st="leg-from-city"]').val();
				var leg_to = tr.find(':input[st="leg-to-city"]').val();
				legJson += '{"leg_index":"' + leg_index + '","leg_date":"'
						+ leg_date + '","leg_from":"' + leg_from
						+ '","leg_to":"' + leg_to + '"}';

				if (i != trs.length - 1) {
					legJson += ',';
				}

			}
		}
		legJson += ']';

		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : confirm_msg,
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("保存中...");

					var data = "team_number=" + self.chosenNeeds()[0]
							+ "&json=" + legJson;

					$.ajax({
						type : "POST",
						url : self.apiurl + 'ticket/createTicketOrder',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						layer.close(createLayer);
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
	self.checkTicketPart = function(team_number) {
		self.airTickets.removeAll();
		startLoadingIndicator("加载中...");
		$.getJSON(self.apiurl + 'ticket/searchOrderAirInfoByTeamNumber', {
			team_number : team_number
		}, function(data) {
			self.airTickets(data.order_air_infos);
			endLoadingIndicator();
			airTicketCheckLayer = $.layer({
				type : 1,
				title : [ '航段信息', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '800px', '500px' ],
				offset : [ '', '' ],
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
	self.checkPassengers = function(team_number) {
		self.passengers.removeAll();
		startLoadingIndicator("加载中...");
		var url = "order/selectSaleOrderNameListByTeamNumber";

		$.getJSON(self.apiurl + url, {
			team_number : team_number
		}, function(data) {

			self.passengers(data.passengers);
			endLoadingIndicator();
			passengerCheckLayer = $.layer({
				type : 1,
				title : [ '乘客信息', '' ],
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
	self.needs = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		startLoadingIndicator("加载中...");
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		param += "&airTicketNeed.ordered=N"
		$.getJSON(self.apiurl + 'ticket/searchAirTicketNeedByPage', param,
				function(data) {

					self.needs(data.airTicketNeeds);

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

	// 查询票务航段模块
	self.airLegs = ko.observable({});
	self.refreshAirLeg = function() {
		var param = "leg.city=" + $("#city").val();
		param += "&page.start=" + self.startIndex1() + "&page.count="
				+ self.perPage1;
		$.getJSON(self.apiurl + 'ticket/searchAirLegsByPage', param, function(
				data) {
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
		var endPage1 = curPage1 + 4 <= self.totalCount1() ? curPage1 + 4 : self
				.totalCount1();
		var pageNums1 = [];
		for (var i = startPage1; i <= endPage1; i++) {
			pageNums1.push(i);
		}
		self.pageNums1(pageNums1);
	};

	self.refreshPage1 = function() {
		self.refreshClient();
	};
	// end pagination client
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
		title : [ '选择票务航段', '' ],
		maxmin : false,
		closeBtn : [ 1, true ],
		shadeClose : false,
		area : [ '600px', '650px' ],
		offset : [ '', '' ],
		scrollbar : true,
		page : {
			dom : '#air-leg-pick'
		},
		end : function() {
			console.log("Done");
		}
	});

	currentAirLeg = event.toElement;
	$(currentAirLeg).blur();
}
