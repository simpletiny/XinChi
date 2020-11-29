var sourceLayer;
var FlightContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.flight = ko.observable({});

	self.createFlight = function() {
		if (!$("form").valid()) {
			return;
		}

		var tbody = $("#table-ticket tbody");
		var rs = $(tbody).find("[required='required']");
		var check = true;
		$(rs).each(function(v, e) {
			if ($(e).val().trim() == "") {
				check = false;
				return;
			}
		});
		if (!check) {
			fail_msg("请填写非空项！")
			return;
		}

		startLoadingIndicator("保存中！");
		var data = $("form").serialize();
		var json = '[';
		var allTrs = tbody.children();
		for (var i = 0; i < allTrs.length; i++) {
			var current = allTrs[i];

			var ticket_index = i + 1;
			var add_day_flg = $(current).find("[st='is-add-day']").is(
					":checked") ? 'Y' : 'N';
			var index_leg = $(current).find("[st='index-leg']").val();
			var start_day = $(current).find("[st='start-day']").val();
			var ticket_number = $(current).find("[st='ticket-number']").val();
			var ticket_leg = $(current).find("[st='ticket-leg']").val();
			var start_time = $(current).find("[st='start-time']").val();
			var end_time = $(current).find("[st='end-time']").val();

			var start_place = $(current).find("[st='start-place']").val();
			var end_place = $(current).find("[st='end-place']").val();

			json += '{"ticket_index":"' + ticket_index + '","add_day_flg":"'
					+ add_day_flg + '","index_leg":"' + index_leg
					+ '","start_day":"' + start_day + '","ticket_number":"'
					+ ticket_number + '","ticket_leg":"' + ticket_leg
					+ '","start_time":"' + start_time + '","end_time":"'
					+ end_time + '","start_place":"' + start_place
					+ '","end_place":"' + end_place + '"'

			if (i == allTrs.length - 1) {
				json += '}';
			} else {
				json += '},';
			}
		}

		json += ']';
		data += "&json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'ticket/createSeasonTicket',
			data : data
		}).success(
				function(str) {
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/ticket/season-ticket.jsp";
					}
				});
	}

	// 供应商选择
	self.supplierEmployees = ko.observable({});
	self.refreshSupplier = function() {
		var param = "employee.type=A&employee.name="
				+ $("#supplier_name").val();
		param += "&page.start=" + self.startIndex1() + "&page.count="
				+ self.perPage1;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param,
				function(data) {
					self.supplierEmployees(data.employees);

					self
							.totalCount1(Math.ceil(data.page.total
									/ self.perPage1));
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
		var endPage1 = curPage + 4 <= self.totalCount1() ? curPage + 4 : self
				.totalCount1();
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

	// 查询票务航段模块
	self.airLegs = ko.observable({});
	self.refreshAirLeg = function() {
		var param = "leg.city=" + $("#city").val();
		param += "&page.start=" + self.startIndex2() + "&page.count="
				+ self.perPage2;
		$.getJSON(self.apiurl + 'ticket/searchAirLegsByPage', param, function(
				data) {
			self.airLegs(data.legs);

			self.totalCount2(Math.ceil(data.page.total / self.perPage2));
			self.setPageNums2(self.currentPage2());
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
	self.currentPage2 = ko.observable(1);
	self.perPage2 = 10;
	self.pageNums2 = ko.observableArray();
	self.totalCount2 = ko.observable(1);
	self.startIndex2 = ko.computed(function() {
		return (self.currentPage2() - 1) * self.perPage2;
	});
	self.resetPage2 = function() {
		self.currentPage2(1);
	};

	self.previousPage2 = function() {
		if (self.currentPage2() > 1) {
			self.currentPage2(self.currentPage2() - 1);
			self.refreshPage2();
		}
	};

	self.nextPage2 = function() {
		if (self.currentPage2() < self.pageNums2().length) {
			self.currentPage2(self.currentPage2() + 1);
			self.refreshPage2();
		}
	};

	self.turnPage2 = function(pageIndex2) {
		self.currentPage2(pageIndex2);
		self.refreshPage2();
	};

	self.setPageNums2 = function(curPage2) {
		var startPage2 = curPage2 - 4 > 0 ? curPage2 - 4 : 1;
		var endPage2 = curPage2 + 4 <= self.totalCount2() ? curPage2 + 4 : self
				.totalCount2();
		var pageNums2 = [];
		for (var i = startPage2; i <= endPage2; i++) {
			pageNums2.push(i);
		}
		self.pageNums2(pageNums2);
	};

	self.refreshPage2 = function() {
		self.refreshAirLeg();
	};
	// end pagination client
};

var ctx = new FlightContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});

function addRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length;
	var html = '<tr>'
			+ '<td><input type="text" st="index-leg" maxlength="10" required="required"/></td>'
			+ '<td><input st="start-day" type="number" maxlength="2" min="1" max="20" required="required" /></td>'
			+ '<td><input st="ticket-number" type="text" maxlength="10" required="required" /></td>'
			+ '<td><input st="ticket-leg" type="text" onclick="choseAirLeg(event)"  maxlength="20" required="required" /></td>'
			+ '<td><input st="start-time" type="text" maxlength="5" onkeyup="formatTime(this)" onblur="checkTime(this)" required="required" /></td>'
			+ '<td><input st="end-time" type="text" maxlength="5" onkeyup="formatTime(this)" onblur="checkTime(this)" required="required" /></td>'
			+ '<td style="float: left"><input st="is-add-day" type="checkbox" />+1</td>'
			+ '<td><input st="start-place" type="text" maxlength="20" /></td>'
			+ '<td><input st="end-place" type="text" maxlength="20" /></td>'
			+ '</tr>';

	var tr = $(html);

	$(tr).find("input[st='index-leg']").val(CHARACTER_ARRAY[index]);

	tbody.append(tr);
}

function deleteRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length - 1;
	if (index > 0) {
		$(tbody.children()[index]).remove();
	}
}

function designated() {
	var tbody = $("#table-ticket tbody");
	if ($("#chk-designated").is(":checked")) {
		tbody.find("input[st='flight-number']").attr("disabled", false);
	} else {
		tbody.find("input[st='flight-number']").attr("disabled", true);
	}
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

	currentSupplier = event.toElement;
	$(currentSupplier).blur();
}
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

	currentAirLeg = event.toElement;
	$(currentAirLeg).blur();
}
// 时间格式验算
function formatTime(txt) {
	var reg = /[^0-9,:]/ig;

	$(txt).val($(txt).val().replace(reg, ""))
	var current = $(txt).val();
	if (current.length == 1) {
		if (current - 0 > 2) {
			$(txt).val("");
		}
	} else if (current.length == 2) {
		if (current - 0 > 23) {
			$(txt).val(current.substring(0, 1));
		} else {
			$(txt).val(current + ":");
		}
	} else if (current.length == 4) {
		var x = current.split(":");
		if (x[1] - 0 > 5) {
			$(txt).val(current.substring(0, 3));
		}
	} else if (current.length == 5) {
		checkTime(txt);
	}
}
function checkTime(txt) {
	var current = $(txt).val();
	var time_reg = /([0-1]{1}[0-9]{1}|2[0-3]):[0-5]{1}[0-9]{1}/;
	if (!time_reg.test(current)) {
		$(txt).val("");
	}
}