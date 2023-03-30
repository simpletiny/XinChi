var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.product_pk = $("#key").val();
	self.product = ko.observable({});
	self.independent_flg = ko.observable();
	self.independent_flg($("#independent_flg").val());
	self.independent_msg = ko.observable();
	if (self.independent_flg() == 'Y') {
		self.independent_msg("（独立团）");
	} else {
		self.independent_flg("N");
	}
	// 获取产品信息
	$.getJSON(self.apiurl + 'product/searchProductByPk', {
		product_pk : self.product_pk
	}, function(data) {
		self.product(data.product);
	});
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

	self.pickClientEmployee = function(data) {
		$("#txt-client-employee-name").val(data.name);
		$("#txt-client-employee-pk").val(data.pk);
		$("#txt-financial-body-name").text(data.financial_body_name);
		layer.close(clientEmployeeLayer);
	};

	self.createOrder = function() {
		if (!$("form").valid()) {
			return;
		}

		if (($("#other-cost").val() - 0) != 0 && $("#receivable-comment").val().trim() == "") {
			fail_msg("请填写团款备注")
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
			var price = $(tr).find("[st='price']").val();

			if (name.trim() == "" || id.trim() == "") {
				continue;
			}
			json += '{"chairman":"' + teamChairman + '","index":"' + index + '","name":"' + name + '","sex":"' + sex
					+ '","cellphone_A":"' + cellphone_A + '","cellphone_B":"' + cellphone_B + '","id":"' + id
					+ '","price":"' + price + '"}';
		}
		json += ']';

		var data = $("form").serialize() + "&bsOrder.independent_flg=" + self.independent_flg() + "&json=" + json;
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/createBudgetStandardOrder',
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
	// changeAutoType("Y");
});