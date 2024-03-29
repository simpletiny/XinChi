var nameListLayer;
var clientEmployeeLayer;
var supplierEmployeeLayer;
var currentType;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.suppliers = ko.observable({});

	self.orderPk = $("#order_key").val();
	// 加载订单信息
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'sale/searchOneFinalOrder', {
		order_pk : self.orderPk
	}, function(data) {
		if (data.order) {
			self.order(data.order);

			// 获取订单包含的供应商
			$.getJSON(self.apiurl + 'sale/searchFinalOrderSupplier', {
				team_number : self.order().team_number
			}, function(data) {
				self.suppliers(data.budgetSuppliers);
			});

		} else {
			fail_msg("订单不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.clientEmployees = ko.observable({});
	self.supplierEmployees = ko.observable({});

	self.refreshClient = function() {
		var param = "employee.name=" + $("#client_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(
				data) {
			self.clientEmployees(data.employees);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};

	self.searchClientEmployee = function() {
		currentType = "client";
		self.refreshClient();
	};

	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'supplier/searchEmployeeByPage', param,
				function(data) {
					self.supplierEmployees(data.employees);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
				});
	};
	self.searchSupplierEmployee = function() {
		currentType = "supplier";
		self.refreshSupplier();
	};

	self.addSupplier = function(data, event) {
		$(event.target)
				.parent()
				.parent()
				.prev()
				.after(
						' <div class="input-row clearfloat" st="supplier">'
								+ '<div class="col-md-6">'
								+ '<label class="l">供应商</label>'
								+ '<div class="ip"><input type="text" class="ip-" onclick="choseSupplierEmployee(this,event)" placeholder="供应商" st="supplierEmployeeName"/></div>'
								+ '<input type="text" class="ip-" st="supplierEmployeePk" style="display:none" />'
								+ '</div>'
								+ '<div class="col-md-6">'
								+ '<label class="l">应付款</label>'
								+ '<div class="ip"><input type="number" st="payable" class="ip-" placeholder="应付款" /></div>'
								+ '</div>' + '</div>');
	};

	self.recordNameList = function() {
		nameListLayer = $.layer({
			type : 1,
			title : ['名单录入', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#name-list'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.choseClientEmployee = function() {
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

	self.pickSupplierEmployee = function(name, pk) {
		$(currentSupplier).val(name);
		$(currentSupplier).parent().next().val(pk);
		layer.close(supplierEmployeeLayer);
	};

	self.saveNameList = function() {
		layer.close(nameListLayer);

	};

	self.updateOrder = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		var nameList = $("#txt-name-list").val();
		nameList = $.trim(nameList.replace(new RegExp("；", "gm"), ";").replace(
				new RegExp("：", "gm"), ":"));
		var supplierArr = new Array();
		var allSupplierEmployees = $("[st='supplier']");
		var supplierJson = '[';
		for (var i = 0; i < allSupplierEmployees.length; i++) {
			var current = allSupplierEmployees[i];
			var supplierEmployeeName = $(current).find(
					"[st='supplierEmployeeName']").val();
			var supplierEmployeePk = $(current).find(
					"[st='supplierEmployeePk']").val();
			var payable = $(current).find("[st='payable']").val();

			if (supplierEmployeePk == "" || supplierEmployeeName == "")
				continue;
			supplierArr.push(supplierEmployeePk);
			supplierJson += '{"supplierEmployeeName":"' + supplierEmployeeName
					+ '",' + '"supplierEmployeePk":"' + supplierEmployeePk
					+ '",' + '"payable":"' + payable;
			if (i == allSupplierEmployees.length - 1) {
				supplierJson += '"}';
			} else {
				supplierJson += '"},';
			}
		}
		supplierJson += ']';
		if (supplierArr.isRepeat()) {
			fail_msg("不能有重复的供应商！");
			return;
		}
		var data = $("form").serialize() + "&nameList=" + nameList
				+ "&supplierJson=" + supplierJson;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/updateOrder',
			data : data
		}).success(
				function(str) {
					if (str == "OK") {
						window.location.href = self.apiurl
								+ "templates/sale/order.jsp";
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
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self
				.totalCount();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		if (currentType == "supplier") {
			self.searchSupplierEmployee();
		} else {
			self.searchClientEmployee();
		}

	};
	// end pagination
};

var ctx = new OrderContext();
var currentSupplier;
function choseSupplierEmployee(data, event) {
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
			// console.log("Done");
		}
	});

	currentSupplier = event.target;
}
$(document).ready(function() {
	ko.applyBindings(ctx);
});
