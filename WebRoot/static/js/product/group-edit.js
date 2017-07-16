var financialLayer;
var GroupContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.group = ko.observable({});
	// 取得用户
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});

	// 获取产品组基本信息
	self.group_pk = $("#key").val();
	$.getJSON(self.apiurl + 'product/searchGroupByPk', {
		group_pk : self.group_pk
	}, function(data) {
		self.group(data.group);
	});

	self.suppliers = ko.observableArray([]);
	$.getJSON(self.apiurl + 'product/searchSuppliersByGroupPk', {
		group_pk : self.group_pk
	}, function(data) {
		self.suppliers(data.group_suppliers);
		for ( var i = 0; i < self.suppliers().length; i++) {
			var current_supplier = self.suppliers()[i];
			var lable = $("<lable></lable>");
			var img = $('<img onclick="deleteSupplier(this)" class="deleteImg" src="../../static/img/mc-icon-cancel.png">');
			var input = $('<input type="hidden" name="group.supplier_pks">');
			var supplier = $('<div class="supplierDiv"></div>');
			$(lable).html(current_supplier.supplier_short_name);
			$(input).val(current_supplier.pk);
			supplier.append(lable);
			supplier.append(img);
			supplier.append(input);
			$("#div_chosen_supplier").append(supplier);
		}

	});

	self.updateGroup = function() {
		if (!$("form").valid()) {
			return;
		}
		var exists = new Array();
		for ( var i = 0; i < $("#div_chosen_supplier").children().length; i++) {
			var supplier = $("#div_chosen_supplier").children().eq(i);
			var pk = $(supplier).children("input").val();
			exists.push(pk);
		}
		if (exists.length < 1) {
			fail_msg("请选择供应商！");
			return;
		}
		var data = $("form").serialize();
		startLoadingIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/updateGroup',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				window.location.href = self.apiurl + "templates/product/product-group.jsp";
			} else if (str == "exists") {
				fail_msg("小组名已经存在");

			}
		});

	};

	self.suppliers = ko.observable({
		total : 0,
		items : []
	});
	self.chosenSuppliers = ko.observableArray([]);
	self.choseFinancial = function() {
		financialLayer = $.layer({
			type : 1,
			title : [ '选择财务主体', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '700px', '650px' ],
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
		var param = "supplier.supplier_short_name=" + $("#supplier_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'supplier/searchSupplierByPage', param, function(data) {
			self.suppliers(data.suppliers);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.searchFinancial = function() {
		self.refresh();

	};

	self.pickFinancial = function() {
		layer.close(financialLayer);
		var exists = new Array();
		for ( var i = 0; i < $("#div_chosen_supplier").children().length; i++) {
			var supplier = $("#div_chosen_supplier").children().eq(i);
			var pk = $(supplier).children("input").val();
			exists.push(pk);
		}
		for ( var i = 0; i < self.chosenSuppliers().length; i++) {
			var data = self.chosenSuppliers()[i].split(";");

			if (exists.contains(data[0]))
				continue;
			var lable = $("<lable></lable>");
			var img = $('<img onclick="deleteSupplier(this)" class="deleteImg" src="../../static/img/mc-icon-cancel.png">');
			var input = $('<input type="hidden" name="group.supplier_pks">');
			var supplier = $('<div class="supplierDiv"></div>');
			$(lable).html(data[1]);
			$(input).val(data[0]);
			supplier.append(lable);
			supplier.append(img);
			supplier.append(input);
			$("#div_chosen_supplier").append(supplier);
		}
		self.chosenSuppliers.removeAll();
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
		self.searchFinancial();

	};
	// end pagination
};

var ctx = new GroupContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
});

// 删除已选择供应商
var deleteSupplier = function(lab) {
	$(lab).parent().remove();
};