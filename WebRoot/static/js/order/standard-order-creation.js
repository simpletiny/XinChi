var clientEmployeeLayer;
let productLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.product_pk = $("#key").val();
	self.product = ko.observable({});
	self.independent_flg = ko.observable();
	self.independent_flg($("#independent_flg").val());
	self.independent_msg = ko.observable();
	self.passengers = ko.observableArray([]);
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
		self.order({as_adult_flg:data.product.as_adult_flg});
		addName();
// self.passengers({name_index:1,chairman : 'Y',price:data.product.adult_price -
// data.product.business_profit_substract });
		writeNameFromSession();
	});
	self.createOrder = function() {
		if (!$("form").valid()) {
			return;
		}

		if (($("#other-cost").val() - 0) != 0 && $("#receivable-comment").val().trim() == "") {
			fail_msg("请填写团款备注")
			return;
		}
		startLoadingSimpleIndicator("保存中");
		// 名单json
		var tbody = $("#name-table").find("tbody");
		var trs = $(tbody).children();
		let people = new Array();
		let not_ok_names = new Array();
		for (var i = 0; i < trs.length; i++) {
			var tr = trs[i];
			var chairman = $(tr).find("[name='team_chairman']").is(":checked") ? "Y" : "N";
			var index = i + 1;
			var name = $(tr).find("[st='name']").val().trim();
			var sex = $(tr).find("[st='sex']").val();
			var age = $(tr).find("[st='age']").val();
			var cellphone_A = $(tr).find("[st='cellphone_A']").val();
			var cellphone_B = $(tr).find("[st='cellphone_B']").val();
			var id_type = $(tr).find("[st='type']").val();
			var id = $(tr).find("[st='id']").val().trim();
			var price = $(tr).find("[st='price']").val();
			let is_ok = $(tr).find("[st='is_ok']").val();
			
			if(is_ok!='Y'){
				not_ok_names.push(name);
			}
			
			if (name.trim() == "" || id.trim() == "") {
				continue;
			}
			let txt_as_adult = $(tr).find("[st='as-adult']");
			let as_adult = 'N';
			if(txt_as_adult){
				if($(txt_as_adult).is(":checked")){
					as_adult='Y';
				}
			}

			let person = {chairman,index,name,sex,age,cellphone_A,cellphone_B,id,price,id_type,as_adult};
			people.push(person);
		}
		
		if(not_ok_names.length>0){
			fail_msg(not_ok_names.join(",")+"未通过验证！");
			endLoadingIndicator();
			return;
		}
	
		let json = JSON.stringify(people);
		var data = $("form").serialize() + "&bsOrder.independent_flg=" + self.independent_flg() + "&json=" + json;
	
		$.ajax({
			type : "POST",
			url : self.apiurl + 'order/createBudgetStandardOrder',
			data : data
		}).success(function(str) {
			if (str == "success") {
				sessionStorage.removeItem("ok_names");
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

	self.clientEmployees = ko.observable({});
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

	self.pickClientEmployee = function(data) {
		$("#txt-client-employee-name").val(data.name);
		$("#txt-client-employee-pk").val(data.pk);
		$("#txt-financial-body-name").text(data.financial_body_name);
		layer.close(clientEmployeeLayer);
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
	self.products = ko.observableArray([]);
	self.refreshProduct = function() {
		startLoadingSimpleIndicator("加载中");
		const product_name = $("#product-name").val().trim();
		const product_model = $("#product-model").val().trim();
		let param = "product.name="+product_name+"&product.product_model="+product_model+"&product.sale_flg=Y";
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;
		$.getJSON(self.apiurl + 'product/searchOnSaleProducts', param, function(data) {
			self.products(data.products);
			self.totalCount1(Math.ceil(data.page.total / self.perPage1));
			self.setPageNums1(self.currentPage1());
			endLoadingIndicator();
		});
	};
	
	self.searchProduct = function(){
		self.refreshProduct();
	}
	
	self.choseProduct = function() {
		$("#txt-client-employee-name").blur();
		productLayer = $.layer({
			type : 1,
			title : ['选择产品', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['1200px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#product-pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.pickProduct = function(data) {
		$("#txt-product-name").val(data.name);
		$("#txt-product-pk").val(data.pk);
		$("#p-product-model").text(data.product_model);
		$("#txt-days").val(data.days);
		$("#p-days").text(data.days);
		layer.close(productLayer);
	};
	// start pagination1
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
		self.searchProduct();
	};
	// end pagination1
};
var ctx = new OrderContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		changeFile(this);
	});
	let a_btn = $(`<a type="submit"
		class="btn btn-green btn-r" onclick="checkName()">名单校验</a>`);
	$("#div-btn-area").prepend(a_btn);
});



