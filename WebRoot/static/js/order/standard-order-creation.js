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
			title : [ '选择客户操作', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
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
		var data = $("form").serialize() + "&bsOrder.independent_flg=" + self.independent_flg();
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
		for ( var i = startPage; i <= endPage; i++) {
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

function formatNameList(txt) {
	nameList = $(txt).val();
	if (nameList.trim() == "")
		return;
	var illegal = /[^\u4e00-\u9fa5,0-9,X,x]|[\n\r]/gm;

	nameList = nameList.replace(illegal, "");
	var namePattern = /[\u4e00-\u9fa5]+/gm;
	var names = nameList.match(namePattern);

	if (null == names) {
		fail_msg("请填写正确的名单！");
		return;
	}

	for ( var i = 0; i < names.length; i++) {
		var name = names[i];
		nameList = nameList.replace(name, ";" + name + ":");
	}
	nameList = nameList.replace(";","")+";";
	
	var names = nameList.split(";");
	var newNameList = "";
	for ( var i = 0; i < names.length; i++) {
		if (names[i] == "")
			continue;
		if (i % 2 == 1) {
			newNameList += names[i] + ";\n";
		} else {
			newNameList += names[i] + ";";
		}
	}
	$(txt).val(newNameList);
}
