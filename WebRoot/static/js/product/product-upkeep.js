var airTicketLayer;
// 组团确认模板上传layer
var ctLayer;
// 出团通知模板上传layer
var ontLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.allCharges = ["PRODUCT", "SALE", "NONE"];
	self.chosenCharge = ko.observable();
	self.locations = ko.observableArray();

	$.getJSON(self.apiurl + 'system/searchByType', {
		type : "LINE"
	}, function(data) {
		self.locations(data.datas);
	});

	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchAllUseUsers', {}, function(data) {
		self.users(data.users);
	});
	self.chosenProducts = ko.observableArray([]);

	self.chosenProduct = ko.observable();

	self.clientConfirmType = ko.observable("D");
	self.clientConfirmTemplet = ko.observable("");
	// 上传组团确认模板
	self.uploadClientConfirmTemplet = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			$.getJSON(self.apiurl + 'product/searchProductByPk', {
				product_pk : self.chosenProducts()[0]
			}, function(data) {
				self.chosenProduct(data.product);
				var t = self.chosenProduct().client_confirm_templet;
				self.clientConfirmTemplet(t);
				if (t != "no") {
					self.clientConfirmType("Y");
					$("#c-c-t-a").show();
				}
				ctLayer = $.layer({
					type : 1,
					title : ['上传组团确认模板', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['500px', '240px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#c-c-t'
					},
					end : function() {
						self.clientConfirmType("D");
						self.clientConfirmTemplet("");
					}
				});
			});
		}
	}
	// 取消组团确认
	self.cancelCCT = function() {
		layer.close(ctLayer);
		self.clientConfirmType("D");
		self.clientConfirmTemplet("");
		// 清除上传信息
	}
	// 保存组团确认
	self.saveCCT = function() {
		var v = $(':radio[name="cctradio"]:checked').val();
		var product_pk = self.chosenProducts()[0];
		if (v == "D") {
			var cct_file = "no";
		} else if (v == "Y") {
			var cct_file = $("#cct_file").val();
		}

		var data = "product.pk=" + product_pk
				+ "&product.client_confirm_templet=" + cct_file;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/uploadClientConfirmTemplet',
			data : data
		}).success(function(str) {
			if (str == "success") {
				layer.close(ctLayer);
				self.refresh();
				$(':input[name="cct"]').val("");
			} else {
				fail_msg(str);
			}
		});
	}
	self.outNoticeType = ko.observable("D");
	self.outNoticeTemplet = ko.observable("");
	// 上传出团通知模板
	self.uploadOutNoticeConfirmTemplet = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			$.getJSON(self.apiurl + 'product/searchProductByPk', {
				product_pk : self.chosenProducts()[0]
			}, function(data) {
				self.chosenProduct(data.product);
				var t = self.chosenProduct().out_notice_templet;
				self.outNoticeTemplet(t);
				if (t != "no") {
					self.outNoticeType("Y");
					$("#o-n-t-a").show();
				}

				ontLayer = $.layer({
					type : 1,
					title : ['上传出团通知模板', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['500px', '240px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#o-n-t'
					},
					end : function() {
						self.outNoticeType("D");
						self.outNoticeTemplet("");
					}
				});
			});

		}
	}
	// 取消出团通知上传
	self.cancelONT = function() {
		layer.close(ontLayer);
		// 清除上传信息
		self.outNoticeType("D");
		self.outNoticeTemplet("");
	}
	// 保存出团通知模板
	self.saveONT = function() {
		var v = $(':radio[name="ontradio"]:checked').val();
		var product_pk = self.chosenProducts()[0];
		if (v == "D") {
			var ont_file = "no";
		} else if (v == "Y") {
			var ont_file = $("#ont_file").val();
		}

		var data = "product.pk=" + product_pk + "&product.out_notice_templet="
				+ ont_file;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'product/uploadOutNoticeTemplet',
			data : data
		}).success(function(str) {
			if (str == "success") {
				layer.close(ontLayer);
				self.refresh();
				$(':input[name="ont"]').val("");
			} else {
				fail_msg(str);
			}
		});
	}

	// 地接维护
	self.supplierManagement = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {
			$
					.getJSON(
							self.apiurl + 'product/searchProductByPk',
							{
								product_pk : self.chosenProducts()[0]
							},
							function(data) {
								if (data.product.supplier_upkeep_flg == "Y") {
									window.location.href = self.apiurl
											+ 'templates/product/supplier-management-edit.jsp?key='
											+ self.chosenProducts()[0];
								} else {
									window.location.href = self.apiurl
											+ 'templates/product/supplier-management.jsp?key='
											+ self.chosenProducts()[0];
								}
							});
		}
	}

	// 本地维护
	self.localManagement = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {

			$
					.getJSON(
							self.apiurl + 'product/searchProductByPk',
							{
								product_pk : self.chosenProducts()[0]
							},
							function(data) {
								if (data.product.local_upkeep_flg == "Y") {
									window.location.href = self.apiurl
											+ 'templates/product/local-management-edit.jsp?key='
											+ self.chosenProducts()[0];
								} else {

									window.location.href = self.apiurl
											+ 'templates/product/local-management.jsp?key='
											+ self.chosenProducts()[0];
								}
							});

		}
	}

	self.product = ko.observable({});
	self.airTickets = ko.observableArray([]);
	// 绑定机票
	self.ticketManagement = function() {
		if (self.chosenProducts().length == 0) {
			fail_msg("请选择产品！");
			return;
		} else if (self.chosenProducts().length > 1) {
			fail_msg("只能选择一个产品！");
			return;
		} else if (self.chosenProducts().length == 1) {

			$.getJSON(self.apiurl
					+ 'product/searchProductAirTicketInfoByProductPk', {
				product_pk : self.chosenProducts()[0]
			}, function(data) {
				self.product(data.product);
				if (self.product().sale_flg == "Y") {
					fail_msg("请选择未上架产品！");
					return;
				}

				self.airTickets(data.air_tickets);
				airTicketLayer = $.layer({
					type : 1,
					title : ['添加机票信息', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['600px', '500px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#air-ticket'
					},
					end : function() {
					}
				});
			});
		}
	};

	self.products = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;

		$.getJSON(self.apiurl + 'product/searchProductsByPage', param,
				function(data) {
					self.products(data.products);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
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
};

var ctx = new ProductContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();

	$(':file').change(function() {
		changeFile(this);
	});
});

function changeCctRadio(rad) {
	var v = $(rad).val();
	if (v == "D") {
		$("#c-c-t-a").hide();
	} else if (v == "Y") {
		$("#c-c-t-a").show();
	}
}
function changeOntRadio(rad) {
	var v = $(rad).val();
	if (v == "D") {
		$("#o-n-t-a").hide();
	} else if (v == "Y") {
		$("#o-n-t-a").show();
	}
}
function addRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length + 1;
	var tr = $('<tr><td st="index">1</td><td><input st="start-day" type="text" /></td><td><input st="start-city" type="text" /></td><td><input st="end-city" type="text" /></td></tr>');
	$(tr).find("td[st='index']").html(index);
	tbody.append(tr);
}

function deleteRow() {
	var tbody = $("#table-ticket tbody");
	var index = tbody.children().length - 1;
	if (index > 0) {
		$(tbody.children()[index]).remove();
	}
}
function saveTicket() {
	var data = $("#form-ticket").serialize();
	var ticketJson = '[';
	var tbody = $("#table-ticket tbody");
	var allTickets = tbody.children();
	for (var i = 0; i < allTickets.length; i++) {
		var current = allTickets[i];
		var index = i + 1;
		var start_day = $(current).find("[st='start-day']").val();
		var start_city = $(current).find("[st='start-city']").val();
		var end_city = $(current).find("[st='end-city']").val();

		if (start_day.trim() == "" || start_city.trim() == ""
				|| end_city.trim() == "") {
			fail_msg("请填写第" + index + "行非空项目！");
			return;
		}

		ticketJson += '{"index":"' + index + '","start_day":"' + start_day
				+ '","start_city":"' + start_city + '","end_city":"' + end_city
				+ '"';

		if (i == allTickets.length - 1) {
			ticketJson += '}';
		} else {
			ticketJson += '},';
		}
	}

	ticketJson += ']';
	data += "&ticket_json=" + ticketJson;
	layer.close(airTicketLayer);
	startLoadingIndicator("保存中！");
	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'product/saveAirTicket',
		data : data
	}).success(function(str) {
		endLoadingIndicator();
		if (str == "success") {
			ctx.refresh();
			ctx.chosenProducts.removeAll();
		} else {
			fail_msg(str);
		}
	});
}
function cancelTicket() {
	layer.close(airTicketLayer);
}
