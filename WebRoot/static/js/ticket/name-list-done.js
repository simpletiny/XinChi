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
	self.allRoles = ['ADMIN', 'MANAGER', 'SALES', 'PRODUCT', 'FINANCE', 'TICKET'];

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

	self.deleteMapping = {
		"N" : "否",
		"Y" : "是"
	}

	self.lockMapping = {
		"N" : "未锁定",
		"Y" : "已锁定"
	}

	// 解锁名单
	self.unlockName = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择乘客！");
			return;
		} else {
			let passenger_pks = "";
			for (let i = 0; i < self.chosenPassengers().length; i++) {
				let data = self.chosenPassengers()[i].split(":");
				passenger_pks += data[0] + ",";
			}
			passenger_pks.RTrim(",");
			let msg = "解锁名单意味着，销售可以对解锁的名单进行编辑或删除操作。确定解锁这些名单吗？"
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						toggleLockName(passenger_pks, 'N');
					}
				}
			});
		}
	}
	// 锁定名单
	self.lockName = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择乘客！");
			return;
		} else {
			let passenger_pks = "";
			for (let i = 0; i < self.chosenPassengers().length; i++) {
				let data = self.chosenPassengers()[i].split(":");
				passenger_pks += data[0] + ",";
			}
			passenger_pks.RTrim(",");
			let msg = "锁定名单，销售将不再允许对这些名单进行操作。确认锁定这些名单吗？"
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						toggleLockName(passenger_pks, 'Y');
					}
				}
			});
		}
	}

	self.refresh = function() {
		startLoadingIndicator("加载中...");

		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage + "&passenger.status=Y";
		$.getJSON(self.apiurl + 'ticket/searchAirTicketDoneNameListByPage', param, function(data) {
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
					current_no = $(tr).find("td[st='order-number']").text();
				}
				var order_number = $(tr).find("td[st='order-number']").text();

				if (order_number != current_no) {
					current_no = order_number;
					current_index += 1;
				}
				tr.find("td").css("cssText", "background:" + self.colors[current_index % 4] + " !important");

			}

			$("span:contains('未锁定')").css("color", "red");
			$("span:contains('已锁定')").css("color", "green");
			$("span:contains('是')").css("color", "red");
			$("span:contains('否')").css("color", "green");

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
			$.getJSON(self.apiurl + 'ticket/searchFlightChangeDataByPassengerPks', param, function(data) {
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

		var change_reason = $("input[st='change-reason']").val();
		var cost = $("input[st='change-cost']").val() - 0;
		var comment = $("textarea[st='comment']").val();

		json += '"team_number":"' + team_number + '","change_reason":"' + change_reason + '","cost":"' + cost
				+ '","comment":"' + comment.replace("\n", " ") + '","allot":[';

		var divs = $("#div-allot").children();

		var allotJson = '';
		var sumMoney = 0;
		for (var i = 0; i < divs.length; i++) {

			var current = divs[i];
			var ticket_source_pk = $(current).find("input[st='ticket-source-pk']").val();

			var money = $(current).find("input[st='money']").val() - 0;
			sumMoney += money;
			allotJson += ',{"ticket_source_pk":"' + ticket_source_pk + '","money":"' + money + '"}';
		}

		if (cost.toFixed(2) != sumMoney.toFixed(2)) {
			fail_msg("航变成本与分配总额不符！");
			return;
		}

		allotJson = allotJson.LTrim(",");

		json += allotJson + ']';

		var nameAllotJson = '';

		var trs = $("#change-name-table tbody tr");
		var sumPersonMoney = 0;
		for (let i = 0; i < trs.length; i++) {
			let tr = $(trs[i]);

			let name_pk = tr.find("input[st='name-pk']").val();
			let change_cost = tr.find("input[st='change-cost-person']").val() - 0;
			sumPersonMoney += change_cost;

			nameAllotJson += ',{"name_pk":"' + name_pk + '","change_cost":"' + change_cost + '"}';
		}

		nameAllotJson = nameAllotJson.LTrim(",");

		json += ',"nameAllot":[' + nameAllotJson + ']}';

		if (cost.toFixed(2) != sumPersonMoney.toFixed(2)) {
			fail_msg("航变成本与航变名单分配总额不符！");
			return;
		}

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : "确认提交航变信息吗",
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("提交中……")
					$.ajax({
						type : "POST",
						url : self.apiurl + 'ticket/changeFlight',
						data : "json=" + json
					}).success(function(str) {
						endLoadingIndicator();
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
		$.getJSON(self.apiurl + 'ticket/searchFlightChangeLogByPassengerPk', param, function(data) {
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
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "打回操作会将同批次的名单(含航变,并删除航变信息)，全部打回至待出票状态！确认要将这些名单打回至待出票状态吗？",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						var param = "";
						for (var i = 0; i < self.chosenPassengers().length; i++) {
							var data = self.chosenPassengers()[i].split(":");
							var pk = data[0];
							param += "passenger_pks=" + pk + "&";
						}
						startLoadingIndicator("打回中...");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'ticket/rollBackNameDone',
							data : param
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
							} else {
								fail_msg(str);
							}
							self.chosenPassengers.removeAll();
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
					var arr = ctx.chosenPassengers()[i].split(":");
					var name = arr[1];
					var id = arr[2];
					var cellphone = arr[7];
					var sex = id.charAt(id.length - 2) % 2 == 1 ? "男" : "女";
					var birthday = id.substr(6, 4) + "/" + id.substr(10, 2) + "/" + id.substr(12, 2);
					text += name + "；" + id + "；" + cellphone + "；" + sex + "；" + birthday + "；\n";
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
			ctx.chosenPassengers.push(passenger.pk + ":" + passenger.name + ":" + passenger.id + ":"
					+ passenger.team_number + ":" + passenger.order_number + ":" + passenger.name_confirm_status + ":"
					+ passenger.status)
		}
	} else {
		for (var i = 0; i < ctx.passengers().length; i++) {
			var passenger = ctx.passengers()[i];
			ctx.chosenPassengers.remove(passenger.pk + ":" + passenger.name + ":" + passenger.id + ":"
					+ passenger.team_number + ":" + passenger.order_number + ":" + passenger.name_confirm_status + ":"
					+ passenger.status)
		}
	}
}
function checkSameOrderNumber(tr) {
	var order_number = $(tr).find("td[st='order-number']").text();
	for (var i = 0; i < ctx.passengers().length; i++) {
		var passenger = ctx.passengers()[i];

		if (passenger.order_number == order_number) {
			var xxstr = passenger.pk + ":" + passenger.name + ":" + passenger.id + ":" + passenger.team_number + ":"
					+ passenger.order_number + ":" + passenger.name_confirm_status + ":" + passenger.status + ":"
					+ passenger.cellphone_A;

			if (ctx.chosenPassengers().contains(xxstr)) {
				ctx.chosenPassengers.remove(xxstr);
			} else {
				ctx.chosenPassengers.push(xxstr);
			}

		}

	}

}

var toggleLockOrder = function(team_number, lock_flg) {
	startLoadingSimpleIndicator("执行中……");
	const param = "team_number=" + team_number + "&lock_flg=" + lock_flg;
	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'ticket/toggleLockOrder',
		async : false,
		data : param
	}).success(function(str) {
		endLoadingIndicator();
		if (str == "success") {
			ctx.refresh();
			ctx.chosenPassengers.removeAll();
		} else {
			fail_msg(str);
		}
	});

}

var toggleLockName = function(passenger_pks, lock_flg) {
	if (passenger_pks.length < 1) {
		fail_msg("没有乘客id");
		return;
	}

	startLoadingSimpleIndicator("执行中……");
	let param = "lock_flg=" + lock_flg;
	const data = passenger_pks.split(",");

	for (let i = 0; i < data.length; i++) {
		param += "&passenger_pks=" + data[i];
	}

	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'ticket/toggleLockName',
		async : false,
		data : param
	}).success(function(str) {
		endLoadingIndicator();
		if (str == "success") {
			ctx.refresh();
			ctx.chosenPassengers.removeAll();
		} else {
			fail_msg(str);
		}
	});
}

var calSum = function() {

	let current_value = $(event.target).val()

	var trs = $("#change-name-table tbody tr");
	var sumPersonMoney = 0;
	for (let i = 0; i < trs.length; i++) {
		let tr = $(trs[i]);
		if ($("#change-all").is(":checked")) {
			tr.find("input[st='change-cost-person']").val(current_value);
		}

		let change_cost = tr.find("input[st='change-cost-person']").val() - 0;
		sumPersonMoney += change_cost;
	}
	$("input[st='change-cost']").val(sumPersonMoney);
}