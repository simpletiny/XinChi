var operateLayer;
var finalLayser;
var passengerCheckLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});

	self.chosenOperations = ko.observableArray([]);

	self.status = ['N', 'I', 'Y'];

	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("N");

	self.statusMapping = {
		'N' : '未操作',
		'I' : '操作中',
		'Y' : '已操作',
		'F' : '已决算',
	};

	// 获取用户信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});

	self.operations = ko.observable({
		total : 0,
		items : []
	});

	self.totalPeopleCount = ko.observable();
	self.totalSupplierCost = ko.observable();
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var total_people_count = 0;
		var total_supplier_cost = 0;
		var param = $('form').serialize();
		param += "&operate_option.status=F";
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$
				.getJSON(
						self.apiurl
								+ 'product/searchProductOrderOperationByPage',
						param,
						function(data) {
							self.operations(data.operations);

							$(self.operations())
									.each(
											function(idx, data) {
												total_people_count += data.people_count - 0;
												total_supplier_cost += data.supplier_cost == null
														? 0
														: data.supplier_cost;
											});

							self.totalPeopleCount(total_people_count);
							self.totalSupplierCost(total_supplier_cost);
							$(".detail").showDetail();
							self.totalCount(Math.ceil(data.page.total
									/ self.perPage));
							self.setPageNums(self.currentPage());

							endLoadingIndicator();
						});
	};

	self.downloadSc = function(team_number, supplier_employee_pk) {
		window.location.href = self.apiurl
				+ "file/downloadProductFile?team_number=" + team_number
				+ "&supplier_employee_pk=" + supplier_employee_pk
				+ "&fileType=C";
	}

	self.productSuppliers = ko.observableArray([]);
	// 打回重新决算
	self.rollBackOperation = function() {
		if (self.chosenOperations().length == 0) {
			fail_msg("请选择产品订单！");
			return;
		} else if (self.chosenOperations().length > 1) {
			fail_msg("只能选择一个");
			return;
		} else if (self.chosenOperations().length == 1) {
			var current = self.chosenOperations()[0].split(";");
			var operate_pk = current[0];
			var data = "operate_pk=" + operate_pk;
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要打回到未决算状态吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("打回中...");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/rollBackOperation',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenOperations.removeAll();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});
		}
	};
	self.passengers = ko.observableArray([]);
	// 查看乘客信息
	self.checkPassengers = function(data, event) {
		self.passengers.removeAll();

		var team_number = data.team_number;
		var url = "order/selectSaleOrderNameListByTeamNumber";

		$.getJSON(self.apiurl + url, {
			team_number : team_number
		}, function(data) {
			self.passengers(data.passengers);
			passengerCheckLayer = $.layer({
				type : 1,
				title : ['游客信息', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['800px', '500px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#passengers-check'
				},
				end : function() {
				}
			});
		});
	};
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 20;
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
		self.refresh();
	};
	// end pagination

	self.supplierEmployees = ko.observable({});
	self.refreshSupplier = function() {
		var param = "employee.name=" + $("#supplier_name").val();
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
};

var ctx = new OrderContext();
$(document).ready(function() {

	ko.applyBindings(ctx);
	ctx.refresh();

});
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
function addRow() {
	var tbody = $("#table-supplier tbody");
	var index = tbody.children().length + 1;
	var tr = $('<tr><td st="index">1</td><td><input type="text" st="supplier-name" onclick="choseSupplierEmployee(event)" /><input type="text" st="supplier-pk" style="display: none" /></td><td><input st="supplier-product-name" maxlength="10" type="text" /></td><td><input st="supplier-cost" type="number" /></td><td><input st="land-day" type="number" /></td><td><input st="pick-type" maxlength="50" type="text" /></td><td><input st="picker" maxlength="10" type="text" /></td>	<td><input st="picker-cellphone" maxlength="15" type="number" /></td><td><input st="off-day" type="number" /></td><td><input st="send-type" maxlength="50" type="text" /></td><td><input type="button" value="-" onclick="deleteRow(this)" /></td></tr>');
	$(tr).find("td[st='index']").html(index);
	tbody.append(tr);
}

function deleteRow(btn) {
	var tbody = $("#table-supplier tbody");
	var index = tbody.children().length - 1;
	if (index > 0) {
		$(btn).parent().parent().remove();
		refreshIndex();
	}
}

function refreshIndex() {
	var tbody = $("#table-supplier tbody");
	var trs = $(tbody).children();
	for (var i = 0; i < trs.length; i++) {
		var tr = trs[i];
		$(tr).find("td[st='index']").html(i + 1);
	}
}