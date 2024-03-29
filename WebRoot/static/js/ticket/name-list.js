var sourceLayer;
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

	self.confirmStatusMapping = {
		"1" : "未确认",
		"2" : "待确认",
		"3" : "产品确认",
		"4" : "待确认",
		"5" : "已确认",
	}

	self.deleteMapping = {
		"N" : "否",
		"Y" : "是"
	}

	self.lockMapping = {
		"N" : "未锁定",
		"Y" : "已锁定"
	}

	self.roleMapping = {
		'MANAGER' : '经理',
		'ADMIN' : '管理员',
		'SALES' : '销售人员',
		'PRODUCT' : '产品',
		'FINANCE' : '财务',
		'TICKET' : '票务'
	};
	self.existsSources = ko.observableArray([]);
	self.operate = function() {

		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择乘客！");
			return;
		} else {
			let has_unlock = false;
			$("td :checkbox:checked").each(function() {

				var no = $($(this).parents()[1]).find("span:contains('未锁定')");
				if (no.length > 0) {
					has_unlock = true;
					return;
				}
			});

			if (has_unlock) {
				fail_msg("名单包含未锁定状态！")
				return;
			}

			// 判断名单是否确认
			for (var i = 0; i < self.chosenPassengers().length; i++) {
				var d = self.chosenPassengers()[i];
				var cd = d.split(":");
				var s = cd[5];
				if (s != "5") {
					fail_msg("请选择销售已确认的名单！");
					return;
				}
			}

			var sources = $(".txt-ticket-source");
			self.existsSources.removeAll();
			// 先填写票源
			if (sources.length > 0) {
				for (var i = 0; i < sources.length; i++) {
					var ss = $(sources[i]).val().trim();
					$(sources[i]).val(ss);
					if (ss == "") {
						fail_msg("请先填写第" + (i + 1) + "个票源");
						return;
					}
					var obj = new Object();
					obj.name = ss;
					obj.index = i;
					self.existsSources.push(obj);
				}
				sourceLayer = $.layer({
					type : 1,
					title : ['选择票源', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['400px', '150px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#source-pick'
					},
					end : function() {
					}
				});
			} else {

				var param = "";
				for (var i = 0; i < self.chosenPassengers().length; i++) {
					var passenger_pk = self.chosenPassengers()[i].split(":")[0];
					param += "passenger_pks=" + passenger_pk + "&";
				}

				if (self.checkHasSameLeg(param)) {
					if ($("#div-table").hasClass("already")) {
						self.addTicketSource();
					} else {
						$("#div-table").animate({
							width : '70%'
						}, "slow", function() {
							// $(this).css("float", "left");
							$(this).addClass("already");
							$(".right-div").css("display", "block");
							self.addTicketSource();
						});
					}
				} else {
					fail_msg("所选乘客不存在相同航段！");
				}

			}
			// } else {
			// fail_msg(str + "团号下的名单未确认，请销售确认后再分配！");
			// }
			// });

		}
	};
	// 判断所选乘客是否有相同票务航段
	self.checkHasSameLeg = function(param) {
		var result = false;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'ticket/checkSameAirLeg',
			async : false,
			data : param
		}).success(function(str) {
			if (str == "success") {
				result = true;
			} else if (str == "fail") {
				result = false;
			}
		});

		return result;
	}
	// 将名单信息添加到剪切板
	self.doCopy = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("未选择！");
		} else {
			var txt = "";
			for (var i = 0; i < self.chosenPassengers().length; i++) {
				var data = self.chosenPassengers()[i].split(":");
				txt += data[1] + "：" + data[2] + "；/n";
			}
			window.clipboardData.setData("text", txt);
			success_msg("已成功复制！");
		}
	}
	// 解锁订单
	self.unlockOrder = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择乘客！");
			return;
		} else if (self.chosenPassengers().length > 1) {
			fail_msg("解锁订单只能选中一个乘客！");
			return;
		} else {
			let data = self.chosenPassengers()[0].split(":");
			let team_number = data[3];
			let msg = "解锁订单意味着销售可以新增乘客。确认要解锁订单" + team_number + "吗？"
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						toggleLockOrder(team_number, 'N');

					}
				}
			});
		}
	}
	// 锁定订单
	self.lockOrder = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择乘客！");
			return;
		} else if (self.chosenPassengers().length > 1) {
			fail_msg("锁定订单只能选中一个乘客！");
			return;
		} else {
			let data = self.chosenPassengers()[0].split(":");
			let team_number = data[3];
			let msg = "锁定订单，销售将不再能增加乘客。确认要锁定订单" + team_number + "吗？"
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						toggleLockOrder(team_number, 'Y');
					}
				}
			});
		}
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

	// 删除名单
	self.deleteName = function() {
		if (self.chosenPassengers().length < 1) {
			fail_msg("请选择乘客！");
			return;
		} else {
			let passenger_pks = "";
			for (let i = 0; i < self.chosenPassengers().length; i++) {
				let data = self.chosenPassengers()[i].split(":");
				passenger_pks += "&passenger_pks=" + data[0];
			}
			passenger_pks.LTrim("&");
			let msg = "确认要删除这些名单吗？"
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("删除中……")
						const param = "passenger_pks=" + passenger_pks;
						$.ajax({
							type : "POST",
							url : ctx.apiurl + 'ticket/deletePassengerByPassengerPks',
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
				}
			});
		}
	}

	// 选择票源
	self.pickTicketSource = function() {

		var source = $("#select-ticket-source").val();
		if (source == "") {
			var param = "";
			for (var i = 0; i < self.chosenPassengers().length; i++) {
				var passenger_pk = self.chosenPassengers()[i].split(":")[0];
				param += "passenger_pks=" + passenger_pk + "&";
			}
			if (self.checkHasSameLeg(param)) {
				self.addTicketSource();
			} else {
				fail_msg("所选乘客不存在相同航段！");
			}

		} else {
			var sources = $(".name-box");
			var target = sources[source];
			var param = "";
			var alreadyPks = $(target).find(".passenger-pk");
			for (var i = 0; i < alreadyPks.length; i++) {
				var passenger_pk = $(alreadyPks[i]).val();
				param += "passenger_pks=" + passenger_pk + "&";
			}

			for (var i = 0; i < self.chosenPassengers().length; i++) {
				var passenger_pk = self.chosenPassengers()[i].split(":")[0];
				param += "passenger_pks=" + passenger_pk + "&";
			}
			if (!self.checkHasSameLeg(param)) {
				fail_msg("所选乘客不存在相同航段！");
				return;
			}

			outer : for (var i = 0; i < self.chosenPassengers().length; i++) {
				var data = self.chosenPassengers()[i].split(":");
				for (var j = 0; j < alreadyPks.length; j++) {
					var passenger_pk = $(alreadyPks[j]).val();
					if (data[0] == passenger_pk) {
						continue outer;
					}
				}

				var padiv = $('<em class="small-box"></em>');
				var label = $('<a href="#" style="margin-left:5px;cursor:pointer" class="passenger-name"></a>');
				var hiddenPk = $('<input type="hidden" class="passenger-pk"></input>');
				$(hiddenPk).val(data[0]);
				$(label).text(data[1]);
				$(label).click(function() {
					self.addDeletePassenger(this);
				});
				$(padiv).append(label);
				$(padiv).append(hiddenPk);
				$(target).append(padiv);
			}
			self.chosenPassengers.removeAll();
		}
		layer.close(sourceLayer);
	};
	// 添加票源
	self.addTicketSource = function() {
		var sourceDiv = $('<div style="display:block;border: solid 1px black;width:100%;margin-top:10px" class="source-div"></div>');
		var deleteDiv = $('<div class="deleteDiv"></div>');
		$(deleteDiv).click(function() {
			self.deleteSource(this);
		});
		var passengerDiv = $('<div class="input-row clearfloat" style="padding: 20px 10px 0 0px"></div>');
		var passengerBox = $('<div class="col-md-12"></div>');
		var passengerBoxChild = $('<div class="ip" style="width: 80%"></div>');
		var passengerBoxGrandson = $('<div style="padding-top: 4px;" class="name-box"></div>');

		$(passengerBox).append('<label class="l" style="width: 20%">名单</label>');

		for (var i = 0; i < self.chosenPassengers().length; i++) {
			var data = self.chosenPassengers()[i].split(":");
			var padiv = $('<em class="small-box"></em>');
			var label = $('<a href="javascript:void(0);" style="margin-left:5px;cursor:pointer" class="passenger-name"></a>');
			var hiddenPk = $('<input type="hidden" class="passenger-pk"></input>');
			$(hiddenPk).val(data[0]);
			$(label).text(data[1]);
			$(label).click(function() {
				self.addDeletePassenger(this);
			});
			$(padiv).append(label);
			$(padiv).append(hiddenPk);
			$(passengerBoxGrandson).append(padiv);
		}
		$(passengerBoxChild).append(passengerBoxGrandson);
		$(passengerBox).append(passengerBoxChild);
		$(passengerDiv).append(passengerBox);
		$(sourceDiv).append(deleteDiv);
		$(sourceDiv)
				.append(
						'<div class="input-row clearfloat" style="padding: 20px 10px 0 0px">'
								+ '<div class="col-md-12">'
								+ '<label class="l" style="width: 20%">票源</label>'
								+ '<div class="ip" style="width: 80%">'
								+ '<input  st="supplier-name" type="text" onclick="choseSupplierEmployee(event)"  class="ip- txt-ticket-source" placeholder="票源" maxlength="20"'
								+ ' /><input type="text" st="supplier-pk" style="display: none" />' + '</div>'
								+ '</div>');

		$(sourceDiv).append(passengerDiv);
		$(".right-div").append(sourceDiv);
		self.chosenPassengers.removeAll();
	};
	self.deleteSource = function(div) {
		$(div).parent().remove();
		var all = $('.source-div');
		if (all.length < 1) {
			$(".right-div").css("display", "none");
			$("#div-table").animate({
				width : '100%'
			}, "slow", function() {
				$(this).removeClass("already");
			});
		}
	};

	self.addDeletePassenger = function(label) {
		$('.deletePassenger').remove();
		var X = $(label).offset().top;
		var Y = $(label).offset().left;
		var div = $('<div></div>');
		var a_name = $('<a href="javascript:void(0);" style="cursor:pointer">删除</a>');
		$(div).append(a_name);

		$('.right-div').append(div);
		$(div).addClass("deletePassenger");

		$(div).offset({
			top : X + 20,
			left : Y
		})

		$(a_name).click({
			label : label
		}, function(event) {
			self.deleteRightPassenger(event.data.label);
		});
	};

	self.deleteRightPassenger = function(label) {
		$(label).parent().remove();
	};
	self.refresh = function() {
		startLoadingIndicator("加载中...");
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage + "&passenger.status=I";
		$.getJSON(self.apiurl + 'ticket/searchAirTicketNameListByPage', param, function(data) {
			self.passengers(data.airTicketNameList);
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

			$("span:contains('未锁定')").css("color", "red");
			$("span:contains('已锁定')").css("color", "green");
			$("span:contains('是')").css("color", "red");
			$("span:contains('否')").css("color", "green");

			endLoadingIndicator();
		});
	};

	// 完成选择票源
	self.finishChosen = function() {
		var sources = $(".txt-ticket-source");
		if (sources.length > 0) {
			for (var i = 0; i < sources.length; i++) {
				var ss = $(sources[i]).val().trim();
				$(sources[i]).val(ss);
				if (ss == "") {
					fail_msg("请先填写第" + (i + 1) + "个票源");
					return;
				}
			}
			var json = '[';
			var all = $('.source-div');
			for (var i = 0; i < all.length; i++) {
				var current = all[i];
				var sourceName = $(current).find("input.txt-ticket-source").val();
				var sourcePk = $(current).find("[st='supplier-pk']").val();
				var pks = $(current).find("input.passenger-pk");
				var passengerPks = "";
				for (var j = 0; j < pks.length; j++) {
					passengerPks += $(pks[j]).val() + ",";
				}
				passengerPks = passengerPks.substr(0, passengerPks.length - 1);
				json += '{"sourceName":"' + sourceName + '","sourcePk":"' + sourcePk + '","passengerPks":"'
						+ passengerPks + '"},';
			}
			json = json.substr(0, json.length - 1);
			json += ']';
			$("#json-data").val(json);

			$("#data-form").submit();
		} else {
			fail_msg("什么也没有！");
			retrurn;
		}

	};

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

	// 供应商选择
	self.supplierEmployees = ko.observable({});
	function choseSupplierEmployee(event) {
		supplierEmployeeLayer = $.layer({
			type : 1,
			title : ['选择供应商操作', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#supplier-pick'
			},
			end : function() {
				console.log("Done");
			}
		});

		currentSupplier = event.target;
		$(currentSupplier).blur();
	}

	self.refreshSupplier = function() {
		var param = "employee.type=A&employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param, function(data) {
			self.supplierEmployees(data.employees);

			self.totalCount1(Math.ceil(data.page.total / self.perPage1));
			self.setPageNums1(self.currentPage1());
		});
	};

	self.searchSupplierEmployee = function() {
		self.refreshSupplier();
	};
	self.pickSupplierEmployee = function(name, pk) {
		$(currentSupplier).val(name);
		$(currentSupplier).next().val(pk);
		layer.close(supplierEmployeeLayer);
	};

	// start pagination
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

	self.turnPage1 = function(pageIndex) {
		self.currentPage1(pageIndex);
		self.refreshPage1();
	};

	self.setPageNums1 = function(curPage) {
		var startPage1 = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage1 = curPage + 4 <= self.totalCount1() ? curPage + 4 : self.totalCount1();
		var pageNums1 = [];
		for (var i = startPage1; i <= endPage1; i++) {
			pageNums1.push(i);
		}
		self.pageNums1(pageNums1);
	};

	self.refreshPage1 = function() {
		self.searchSupplierEmployee();
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
					var sex = id.charAt(id.length - 2) % 2 == 1 ? "男" : "女";
					var birthday = id.substr(6, 4) + "/" + id.substr(10, 2) + "/" + id.substr(12, 2);
					var cellphone = arr[6];
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

var currentSupplier;
var supplierEmployeeLayer;
function choseSupplierEmployee(event) {
	supplierEmployeeLayer = $.layer({
		type : 1,
		title : ['选择供应商操作', ''],
		maxmin : false,
		closeBtn : [1, true],
		shadeClose : false,
		area : ['600px', '650px'],
		offset : ['50px', ''],
		scrollbar : true,
		page : {
			dom : '#supplier-pick'
		},
		end : function() {
			console.log("Done");
		}
	});

	currentSupplier = event.target;
	$(currentSupplier).blur();
}

function checkAll(chk) {
	if ($(chk).is(":checked")) {
		for (var i = 0; i < ctx.passengers().length; i++) {
			var passenger = ctx.passengers()[i];
			ctx.chosenPassengers.push(passenger.pk + ":" + passenger.name + ":" + passenger.id + ":"
					+ passenger.team_number + ":" + passenger.order_number + ":" + passenger.name_confirm_status + ":"
					+ passenger.cellphone_A)
		}
	} else {
		for (var i = 0; i < ctx.passengers().length; i++) {
			var passenger = ctx.passengers()[i];
			ctx.chosenPassengers.remove(passenger.pk + ":" + passenger.name + ":" + passenger.id + ":"
					+ passenger.team_number + ":" + passenger.order_number + ":" + passenger.name_confirm_status + ":"
					+ passenger.cellphone_A)
		}
	}
}

function checkSameOrderNumber(tr) {
	var order_number = $(tr).find("td[st='order-number']").text();
	for (var i = 0; i < ctx.passengers().length; i++) {
		var passenger = ctx.passengers()[i];

		if (passenger.order_number == order_number) {
			var xxstr = passenger.pk + ":" + passenger.name + ":" + passenger.id + ":" + passenger.team_number + ":"
					+ passenger.order_number + ":" + passenger.name_confirm_status + ":" + passenger.cellphone_A;

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
