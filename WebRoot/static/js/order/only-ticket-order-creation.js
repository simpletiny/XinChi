var clientEmployeeLayer;
var passengerBatLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.passengers = ko.observableArray([{
		name_index : 1,
		chairman : 'Y'
	}]);
	self.current_date = $("#hidden-server-date").val();

	var x = new Date(self.current_date);
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
			}
		});
	}

	self.pickClientEmployee = function(name, pk) {
		$("#txt-client-employee-name").val(name);
		$("#txt-client-employee-pk").val(pk);
		layer.close(clientEmployeeLayer);
	}

	self.createOrder = function() {
		if (!$("form").valid()) {
			return;
		}

		// 航班信息
		var info_table = $("#table-ticket tbody");
		var infos = $(info_table).children();
		let legs = new Array();
		for (let i = 0; i < infos.length; i++) {
			const tr = $(infos[i]);
			const index = tr.find("input[st='flight-index']").val().trim();
			const date = tr.find("input[st='date']").val().trim();
			const from_city = tr.find("input[st='from-city']").val().trim();
			const to_city = tr.find("input[st='to-city']").val().trim();
			
			let leg = {index,date,from_city,to_city};
			legs.push(leg);
		}
		
		var air_comment = $("#air-comment").val().trim();

		// 名单json
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		let people = new Array();
		for (var i = 0; i < trs.length; i++) {
			const tr = trs[i];
			const chairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			const index = i + 1;
			const name = $(tr).find("[st='name']").val().trim();
			const sex = $(tr).find("[st='sex']").val();
			const age = $(tr).find("[st='age']").val().trim();
			const id_type = $(tr).find("[st='type']").val();

			const cellphone_A = $(tr).find("[st='cellphone_A']").val();
			const cellphone_B = $(tr).find("[st='cellphone_B']").val();
			const id = $(tr).find("[st='id']").val().trim();

			if (name == "" || id == "") {
				continue;
			}
			
			let person = {chairman,index,name,sex,age,cellphone_A,cellphone_B,id_type,id};
			people.push(person);
		}
		
		const info = {ticket_json:legs,name_json:people,air_comment:air_comment};
		const json = JSON.stringify(info);
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
