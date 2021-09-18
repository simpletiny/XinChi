var supplierEmployeeLayer;
var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.detail = ko.observable({});
	self.cards = ko.observableArray([]);
	self.key = $("#key").val();

	$.getJSON(self.apiurl + 'finance/searchCardsByPurpose', {
		purpose : "TICKET"
	}, function(data) {
		if (data.cards) {
			self.cards(data.cards);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.createDetail = function() {
		if (!$("form").valid()) {
			return;
		}

		var data = $("form").serialize() + "&payment_detail.type=" + self.key;
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '提交后无法修改，是否确认提交?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					startLoadingSimpleIndicator("保存中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'payable/createPaymentDetail',
						data : data
					}).success(function(str) {
						if (str == "success") {
							window.location.href = self.apiurl + "templates/ticket/paid.jsp";
						} else if (str == "time") {
							endLoadingIndicator();
							fail_msg("同一时间同一账户存在明细账");
						}

					});
					layer.close(index);
				}
			}
		});
	};

	// 供应商选择

	self.supplierEmployees = ko.observable({});
	self.choseFinancial = function() {
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
	};

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
		$('#supplier-employee-name').val(name);
		$('#supplier-employee-pk').val(pk);
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

var ctx = new DetailContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	$(':file').change(function() {
		changeFile({
			input : this,
			size : 400,
			width : 400,
			required : "yes"
		});
	});
});
