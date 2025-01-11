var clientEmployeeLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.clientEmployees = ko.observable({});
	self.order_pk = $("#key").val();

	self.product = ko.observable({});
	self.employee = ko.observable({});
	self.independent_msg = ko.observable();
	self.passengers = ko.observableArray([]);
	var d = new Date();
	var year_now = d.getFullYear();

	$.getJSON(self.apiurl + 'order/searchOrderByPk', {
		order_pk : self.order_pk
	}, function(data) {
		self.order(data.order);
		if (data.passengers.length > 0) {
			$(data.passengers).each(function(index,data){
				data.as_adult = data.as_adult=='Y'?true:false;
			})
			
			self.passengers(data.passengers);
		}

		if (self.order().independent_flg == 'Y') {
			self.independent_msg("（独立团）");
		}
		
		autoPersonInfo();
		
		if(self.order().product_pk){
			$.getJSON(self.apiurl + 'product/searchProductByPk', {
				product_pk : self.order().product_pk
			}, function(data) {
				self.product(data.product);
				if(self.passengers().length==0){
					self.passengers({name_index:1,price:data.product.adult_price - data.product.business_profit_substract })
				}
				
			});
		}

		if(self.order().client_employee_pk){
			$.getJSON(self.apiurl + 'client/searchOneEmployee', {
				employee_pk : self.order().client_employee_pk
			}, function(data) {
				if (data.employee) {
					self.employee(data.employee);
				} else {
					fail_msg("员工不存在！");
				}
			}).fail(function(reason) {
				fail_msg(reason.responseText);
			});
		}
		self.loadFiles();

	});
	/**
	 * 更新标准订单
	 */
	self.updateOrder = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		const url = self.apiurl + 'order/updateBudgetStandardOrder';
		var data = $("form").serialize();

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
		data += "&json=" + json;
		$.ajax({
			type : "POST",
			url : url,
			data : data
		}).success(function(str) {
			if (str == "success") {
				window.history.go(-1);
			} else if (str == "conflict") {
				fail_msg("订单名单人数与票务不符，请联系票务!");
			}
		});
	};
	self.loadFiles = function() {
		var stFileName = $("#txt-confirm-file");
		var fileName = $("#txt-confirm-file").val();
		if (!fileName.isEmpty()) {
			self.downFile(stFileName, fileName);
		}
	};

	self.downFile = function(stFileName, fileName) {
		var inputFile = $(stFileName).prev().find("input");
		var inputName = inputFile.attr("name");
		var imgContainer = $(inputFile).parent().parent().parent().next();
		var fileNameInput = stFileName;

		var formData = new FormData();
		formData.append("fileFileName", fileName);
		formData.append("fileType", "CLIENT_CONFIRM");
		formData.append("subFolder", self.order().create_user);
		console.log(self.order().create_user);
		var url = ctx.apiurl + 'file/getFileStream';
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.responseType = "blob";
		xhr.onload = function() {
			if (this.status == 200) {
				var blob = this.response;
				var deleteButton = $("<div class='delete'>删除</div>");

				var img = document.createElement("img");
				$("body").append(deleteButton);
				deleteButton.hide();
				deleteButton.click(function() {
					deleteImage(this, inputFile, img, fileNameInput, fileName);
				});
				deleteButton.mouseenter(function() {
					$(this).show();
				});
				img.onload = function(e) {
					window.URL.revokeObjectURL(img.src);
					if (img.width > initWidth) {
						img.height = initWidth * (img.height / img.width);
						img.width = initWidth;
					}

					$(img).mouseenter(function() {
						deleteButton.css("top", $(img).offset().top + img.height / 2 - 25);
						deleteButton.css("left", $(img).offset().left + img.width / 2 - 50);
						deleteButton.show();
					});
					$(img).mouseout(function() {
						deleteButton.hide();
					});
				};
				img.src = window.URL.createObjectURL(blob);

				if (inputName != "file6") {
					imgContainer.html(img);
				} else {
					imgContainer.append(img);
				}
			}
		};
		xhr.send(formData);
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
	// changeAutoType("Y");
	let a_btn = $(`<a type="submit"
	class="btn btn-green btn-r" onclick="checkName()">名单校验</a>`);
	$("#div-btn-area").prepend(a_btn);

});

// function changeAutoType(v) {
// if (v == "Y") {
// $(".auto-2").attr("disabled", "disabled")
// $(".auto-1").attr("disabled", false)
// } else if (v == "N") {
// $(".auto-1").attr("disabled", "disabled")
// $(".auto-2").attr("disabled", false)
// }
// }
