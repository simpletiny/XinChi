var financialLayer;
var SupplierEmployeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.supplier = ko.observable({});
	self.genders = [ '男', '女' ];
	// self.employeeArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆' ];
	// self.provices = [ '北京市', '天津市', '上海市', '重庆市', '河北省', '山西省', '辽宁省', '吉林省',
	// '黑龙江省', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省',
	// '湖南省', '广东省', '海南省', '四川省', '贵州省', '云南省', '陕西省', '甘肃省', '青海省',
	// '台湾省', '内蒙古自治区', '广西壮族自治区', '西藏自治区', '宁夏回族自治区', '新疆维吾尔自治区',
	// '香港特别行政区', '澳门特别行政区' ];
	


	// self.supplierType = [ '注册', '挂靠', '独立旅游人', '夫妻店', '其他' ];
	self.sales = ko.observableArray([]);
	self.employee = ko.observable({});
	self.suppliers = ko.observable({
		total : 0,
		items : []
	});
	

	self.choosenSales = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
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
		$.getJSON(self.apiurl + 'supplier/searchSupplier', {}, function(data) {
			self.suppliers(data.suppliers);
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

	self.createEmployee = function() {

		if (!$("form").valid()) {
			return;
		}

		$.ajax(
				{
					type : "POST",
					url : self.apiurl + 'supplier/createEmployee',
					data : $("form").serialize() + "&employee.sales="
							+ self.choosenSales()
				}).success(
				function(str) {
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/supplier/supplier-employee.jsp";
					}
				});
	};
};

var ctx = new SupplierEmployeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
