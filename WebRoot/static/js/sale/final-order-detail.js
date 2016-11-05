var receivableLayer;
var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.orderPk = $("#order_key").val();
	self.order = ko.observable({
		sales : []
	});
	self.suppliers = ko.observable({});
	self.receivableDetails = ko.observable({
		total : 0,
		items : []
	});
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'sale/searchOneFinalOrder', {
		order_pk : self.orderPk
	}, function(data) {
		if (data.order) {
			self.order(data.order);
			
			//获取订单包含的供应商
			$.getJSON(self.apiurl + 'sale/searchFinalOrderSupplier', {
				team_number: self.order().team_number
			}, function(data) {
				self.suppliers(data.finalOrderSuppliers);
			});
			
//			$.getJSON(self.apiurl + 'sale/searchReceivableDetails', {
//				team_number : self.order().team_number
//			}, function(data) {
//				if (data.receivableDetails) {
//					self.receivableDetails(data.receivableDetails);
//				}
//
//			}).fail(function(reason) {
//				fail_msg(reason.responseText);
//			});

		} else {
			fail_msg("订单不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.addReceivable = function() {
		receivableLayer = $.layer({
			type : 1,
			title : [ '收款记录', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '200px' ],
			offset : [ 'auto', '' ],
			scrollbar : true,
			page : {
				dom : '#receivable-add'
			},
			end : function() {
				console.log("Done");
			}
		});
	};
	
	//保存明细
	self.saveReceivable = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/saveReceivableDetail',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "OK") {
				self.refreshDetail();
				$.getJSON(self.apiurl + 'sale/searchOneOrder', {
					order_pk : self.orderPk
				}, function(data) {
					if (data.order) {
						self.order(data.order);
					}
				}).fail(function(reason) {
					fail_msg(reason.responseText);
				});
				layer.close(receivableLayer);
			}
		});
	};
	//刷新明细
	self.refreshDetail = function() {
		var team_number = $("#txt-team-number").val();
		$.getJSON(self.apiurl + 'sale/searchReceivableDetails', {
			team_number : team_number
		}, function(data) {
			if (data.receivableDetails) {
				self.receivableDetails(data.receivableDetails);
			}

		}).fail(function(reason) {
			fail_msg(reason.responseText);
		});
	};
	// 删除一条明细
	self.deleteReceivableDetail = function(detail_pk) {
		$.ajax({
			type : "POST",
			url : self.apiurl + 'sale/deleteReceivableDetail',
			data : "detail_pk="+detail_pk
		}).success(function(str) {
			if (str == "OK") {
				self.refreshDetail();
				
				$.getJSON(self.apiurl + 'sale/searchOneOrder', {
					order_pk : self.orderPk
				}, function(data) {
					if (data.order) {
						self.order(data.order);
					}
				}).fail(function(reason) {
					fail_msg(reason.responseText);
				});
			}
		});
	};

};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
