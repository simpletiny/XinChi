var clientEmployeeLayer;
var passengerBatLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});

	var x = new Date();
	self.order().confirm_date = x.Format("yyyy-MM-dd");
	self.refreshClient = function() {
		startLoadingSimpleIndicator("加载中……");
		var param = "employee.name=" + $("#client_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(data) {
			self.clientEmployees(data.employees);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			endLoadingIndicator();
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

		var json = '';
		// 航班信息json
		var ticket_json = '[';
		var info_table = $("#table-ticket tbody");
		var infos = $(info_table).children();

		for (var i = 0; i < infos.length; i++) {
			if (i != 0)
				ticket_json += ',';
			var tr = $(infos[i]);
			var index = tr.find("input[st='flight-index']").val().trim();
			var date = tr.find("input[st='date']").val().trim();
			var from_city = tr.find("input[st='from-city']").val().trim();
			var to_city = tr.find("input[st='to-city']").val().trim();

			ticket_json += '{"index":"' + index + '","date":"' + date + '","from_city":"' + from_city + '","to_city":"'
					+ to_city + '"}';

		}
		ticket_json += ']';

		var air_comment = $("#air-comment").val().trim();

		// 名单json
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		var name_json = '[';
		for (var i = 0; i < trs.length; i++) {
			if (i != 0)
				name_json += ',';
			var tr = trs[i];
			var teamChairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val();
			var sex = $(tr).find("[st='sex']").val();

			var cellphone_A = $(tr).find("[st='cellphone_A']").val();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val();
			var id = $(tr).find("[st='id']").val();

			name_json += '{"chairman":"' + teamChairman + '","index":"' + index + '","name":"' + name + '","sex":"'
					+ sex + '","cellphone_A":"' + cellphone_A + '","cellphone_B":"' + cellphone_B + '","id":"' + id
					+ '"}';
		}
		name_json += ']';

		json = '{"ticket_json":' + ticket_json + ',"name_json":' + name_json + ',"air_comment":"' + air_comment + '"}';

		var data = $("form").serialize() + "&bnsOrder.independent_flg=A" + "&json=" + json;

		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/createOnlyTicketOrder',
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
