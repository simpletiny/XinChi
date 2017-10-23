var financialLayer;
var EmployeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.employeePk = $("#employee_key").val();
	self.employee = ko.observable({});
	self.genders = [ '男', '女' ];
	self.suppliers = ko.observable({
		total : 0,
		items : []
	});
	
	startLoadingSimpleIndicator("加载中");
	
	$.getJSON(self.apiurl + 'supplier/searchOneEmployee', {
		employee_pk : self.employeePk
	}, function(data) {
		if (data.employee) {
			self.employee(data.employee);
		} else {
			fail_msg("员工不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
	
	self.choseFinancial = function() {
		financialLayer = $.layer({
			type : 1,
			title : [ '选择财务主体', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#financial_pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.refresh = function() {
		var param = "supplier.supplier_short_name="+$("#supplier_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count="
		+ self.perPage;
		$.getJSON(self.apiurl + 'supplier/searchSupplierByPage', param, function(data) {
			self.suppliers(data.suppliers);
			
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.searchFinancial = function() {
		self.refresh();

	};

	self.pickFinancial = function(name, pk) {
		$("#financial_body_name").val(name);
		$("#financial_body_pk").val(pk);
		layer.close(financialLayer);
	};

	
	self.saveEmployee = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'supplier/updateEmployee',
			data : $("form").serialize()
		}).success(function(str) {
			if(str=="success"){
				window.location.href=self.apiurl+"templates/supplier/supplier-employee.jsp";
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
		for ( var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.searchFinancial();

	};
	// end pagination
};

var ctx = new EmployeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
