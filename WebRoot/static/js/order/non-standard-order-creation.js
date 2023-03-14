var clientEmployeeLayer;
var passengerBatLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.independent_flg = ko.observable();
	self.independent_flg($("#independent_flg").val());
	self.independent_msg = ko.observable();
	if (self.independent_flg() == 'Y') {
		self.independent_msg("（独立团）");
	} else {
		self.independent_flg("N");
	}
	var x = new Date();
	self.order().confirm_date = x.Format("yyyy-MM-dd");
	self.refreshClient = function() {
		var param = "employee.name=" + $("#client_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(data) {
			self.clientEmployees(data.employees);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	self.searchClientEmployee = function() {
		self.refreshClient();
	};

	self.choseClientEmployee = function() {
		$("#txt-client-employee-name").blur();
		clientEmployeeLayer = $.layer({
			type : 1,
			title : ['选择客户操作', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#client-pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.pickClientEmployee = function(name, pk) {
		$("#txt-client-employee-name").val(name);
		$("#txt-client-employee-pk").val(pk);
		layer.close(clientEmployeeLayer);
	};

	self.createOrder = function() {
		if (!$("form").valid()) {
			return;
		}

		// 名单json
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		var json = '[';
		for (var i = 0; i < trs.length; i++) {
			if (i != 0)
				json += ',';
			var tr = trs[i];
			var teamChairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val();
			var sex = $(tr).find("[st='sex']").val();

			var cellphone_A = $(tr).find("[st='cellphone_A']").val();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val();
			var id = $(tr).find("[st='id']").val();

			json += '{"chairman":"' + teamChairman + '","index":"' + index + '","name":"' + name + '","sex":"' + sex
					+ '","cellphone_A":"' + cellphone_A + '","cellphone_B":"' + cellphone_B + '","id":"' + id + '"}';
		}
		json += ']';

		var data = $("form").serialize() + "&bnsOrder.independent_flg=" + self.independent_flg() + "&json=" + json;

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/createBudgetNonStandardOrder',
			data : data
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/product/product-box.jsp";
			}
		});
	};
	// 批量导入
	self.batName = function() {
		passengerBatLayer = $.layer({
			type : 1,
			title : ['批量导入名单', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '300px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#bat-passenger'
			},
			end : function() {
				console.log("Done");
			}
		});
	};
	self.addName = function() {
		var tbody = $("#name-table").find("tbody");
		var count = $(tbody).children().length;
		var html = '<tr>'
				+ '<td><input type="radio" name="team_chairman" /></td>'
				+ '<td st="name-index">'
				+ (count + 1)
				+ '</td>'
				+ '<td><input type="text" style="width: 90%" st="name" /></td>'
				+ '<td><select class="form-control" style="height: 34px" st="sex">'
				+ '<option value="">选择</option>'
				+ '<option value="M">男</option>'
				+ '<option value="F">女</option>'
				+ '</select></td>'
				+ '<td><input type="text" style="width: 90%" st="age" /></td>'
				+ '<td><input type="text" style="width: 90%" st="cellphone_A" /></td>'
				+ '<td><input type="text" style="width: 90%" st="cellphone_B" /></td>'
				+ '<td><input type="text" style="width: 90%" st="id" /></td>'
				+ '<td><input type="text" style="width: 90%" value="分房组" /></td>'
				+ '<td><a href="javascript:;" class="a-upload">上传身份证<input type="file" name="file" /></a> <input'
				+ 'type="hidden"/></td>'
				+ '<td><a href="javascript:;" class="a-upload">上传护照<input type="file" name="file" /></a> <input'
				+ 'type="hidden" /></td>'
				+ '<td><input type="button" style="width: 50px" onclick= "removeName(this)" alt="删除名单" value="-" /></td>'
				+ '</tr>';
		tbody.append(html);
	};

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
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.searchClientEmployee();
	};
	// end pagination
};

var ctx = new OrderContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		changeFile(this);
	});
});
function cancelBat() {
	layer.close(passengerBatLayer);
}

function autoCaculate() {
	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	var adultCnt = 0;
	var childrenCnt = 0;

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		var td_price = $(tr).find("[st='price']");
		var td_sex = $(tr).find("[st='sex']");
		var id = $(td_id).val();

		if (id.length < 18)
			continue;

		var td_age = $(tr).find("[st='age']");
		var d = new Date();
		var year_now = d.getFullYear();
		var birthYear = id.substring(6, 10);
		$(td_age).val(year_now - birthYear);

		var lastSecond = id.charAt(16);
		$(td_sex).val(lastSecond % 2 == 0 ? "F" : "M");

		var birthday = id.substring(6, 14);
		if (isChild(birthday)) {
			childrenCnt++;
		} else {
			adultCnt++;
		}
	}
	if (adultCnt != 0) {
		$("#people-count").val(adultCnt);
	}

	if (childrenCnt != 0) {
		$("#special-count").val(childrenCnt);
	}
}

function formatNameList() {
	nameList = $("#txt-name-list").val();
	if (nameList.trim() == "")
		return;
	var illegal = /[^\u4e00-\u9fa5,0-9,X,x]|[\n\r]/gm;

	nameList = nameList.replace(illegal, "");
	var idPattern = /[0-9,X,x]+/gm;
	var ids = nameList.match(idPattern);

	var namePattern = /[\u4e00-\u9fa5]+/gm;
	var names = nameList.match(namePattern);

	if (null == ids) {
		fail_msg("请填写正确的名单！");
		return;
	}

	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
		nameList = nameList.replace(id, ":" + id + ";");
	}

	var nameObjs = new Array();
	var names = nameList.split(";");
	var newNameList = "";

	for (var i = 0; i < names.length; i++) {
		if (names[i] == "")
			continue;

		if (i % 2 == 1) {
			newNameList += names[i] + ";\n";
		} else {
			newNameList += names[i] + ";";
		}
		var nameObj = new Object();
		var passenger = names[i].split(":");
		if (passenger.length < 2)
			continue;
		nameObj.name = passenger[0];
		nameObj.id = passenger[1];
		nameObjs.push(nameObj);
	}

	for (var i = 0; i < nameObjs.length; i++) {
		var nameObj = nameObjs[i];
		if (isRepeatId(nameObj.id))
			continue;
		writeName(nameObj);
	}

	$("#txt-name-list").val(newNameList);
	layer.close(passengerBatLayer);
	autoCaculate();
}

var writeName = function(nameObj) {

	var tbody = $("#name-table").find("tbody");
	var trs = $(tbody).children();
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];

		if ($(tr).find("[st='name']").val().trim() == "" && $(tr).find("[st='id']").val().trim() == "") {

			var name = $(tr).find("[st='name']");
			var id = $(tr).find("[st='id']");

			$(name).val(nameObj.name);
			$(id).val(nameObj.id);

			return;
		}
	}

	ctx.addName();
	writeName(nameObj);

}

// 判断是否已经存在重复的id乘客
var isRepeatId = function(id) {
	var tbody = $("#name-table").find("tbody");

	var trs = $(tbody).children();

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_id = $(tr).find("[st='id']");
		var existId = $(td_id).val();
		if (id == existId)
			return true;
	}
	return false;
}
var removeName = function(btn) {
	var tbody = $("#name-table").find("tbody");
	var count = $(tbody).children().length;
	var target = $(btn).parent().parent();
	if (count > 1) {
		var td_radio = $(target).find("[name='team_chairman']");
		$(target).remove();
		refreshNameIndex();

		if ($(td_radio).is(":checked")) {
			var next_chair = $("input[name='team_chairman']:eq(0)");
			$(next_chair).prop("checked", true);
		}
	}
	autoCaculate();

};
var refreshNameIndex = function() {
	var tbody = $("#name-table").find("tbody");

	var trs = $(tbody).children();

	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		var td_index = $(tr).find("[st='name-index']");
		$(td_index).html(i + 1);
	}
}