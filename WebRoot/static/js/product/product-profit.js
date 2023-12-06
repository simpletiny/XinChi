var reconciliationLayer;
var ReportContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.order = ko.observable({});
	self.current_date = $("#hidden-server-date").val();
	self.chosenReports = ko.observableArray([]);

	// 获取用户信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', 'role=PRODUCT', function(data) {
		self.users(data.users);
	});

	self.years = ko.observableArray();

	var x = new Date(self.current_date);
	var current_year = x.getFullYear();
	for (var i = current_year; i > 2017; i--) {
		var o = new Object();
		o.name = i + "年";
		o.key = i;
		self.years().push(o);
	}
	// 开会用
	self.isSpecificDate = ko.observable();
	let today = x.Format("yyyy-MM-dd");

	self.isSpecificDate(today == '2023-08-16');

	self.statusMapping = {
		'N' : '待审核',
		'Y' : '待终审'
	};

	self.reports = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中...");
		var param = $('#form-search').serialize();
		$.getJSON(self.apiurl + 'product/searchProductProfit', param, function(data) {
			self.reports(data.productProfits);
			$(".table").tableSum({
				title : '汇总',
				title_index : 3,
				except : [1, 2, 3]
			});
			$(".rmb").formatCurrency();
			endLoadingIndicator();
		});
	};
	let reconciliation_type = "";
	self.addReceive = function() {
		reconciliation_type = "收入";
		reconciliationLayer = $.layer({
			type : 1,
			title : ['添加收入', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['1000px', '350px'],
			offset : ['', ''],
			scrollbar : true,
			zIndex : 887,
			page : {
				dom : '#div-reconciliation'
			},
			end : function() {
				$("#div-reconciliation").clear();
			}
		});
	}
	self.addPay = function() {
		reconciliation_type = "支出";
		reconciliationLayer = $.layer({
			type : 1,
			title : ['添加支出', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['1000px', '350px'],
			offset : ['', ''],
			scrollbar : true,
			zIndex : 887,
			page : {
				dom : '#div-reconciliation'
			},
			end : function() {
				$("#div-reconciliation").clear();
			}
		});
	}
	self.doReconciliation = function() {
		if (!$("#form-reconciliation").valid()) {
			return;
		}

		let msg = "确认添加" + reconciliation_type + "吗？";

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : msg,
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("保存中");
					let data = $("#form-reconciliation").serialize();
					data += "&reconciliation.type=" + reconciliation_type;
					$.ajax({
						type : "POST",
						url : self.apiurl + 'product/addReconciliation',
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refresh();
							$("#div-reconciliation").clear();
							layer.close(reconciliationLayer);
						} else {
							fail_msg(str);
						}
					});
				}
			}
		});
	}
	self.cancelReconciliation = function() {
		layer.close(reconciliationLayer);
		$("#div-reconciliation").clear();
	}
	/**
	 * 扎账
	 */
	self.confirm = function() {
		if (self.chosenReports().length < 1) {
			fail_msg("请选择！")
			return;
		} else if (self.chosenReports().length > 1) {
			fail_msg("只能选择一个！");
			return;
		} else {
			const belong_month = self.chosenReports()[0].departure_month;
			const current_month = today.substring(0, 7);
			const date1 = new Date(belong_month + '-01');
			const date2 = new Date(current_month + '-01');

			if (!date2.after(date1)) {
				fail_msg("次月才能扎账！");
				return;
			}

			const status = self.chosenReports()[0].status;
			if (status === 'N') {
				fail_msg("只能选择待终审的账目！");
				return;
			}

			const msg = "确认要对" + belong_month + "进行扎账吗？";

			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : msg,
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("保存中");
						const product_manager_number = self.chosenReports()[0].user_number;
						const data = "product_manager_number=" + product_manager_number + "&belong_month="
								+ belong_month;
						$.ajax({
							type : "POST",
							url : self.apiurl + 'product/confirmProductAccounting',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
							} else {
								fail_msg(str);
							}
						});
					}
				}
			});

		}
	}
};

var ctx = new ReportContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
