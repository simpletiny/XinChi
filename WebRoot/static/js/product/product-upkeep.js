var airTicketLayer;
var airTickeChecktLayer;
// 组团确认模板上传layer
var ctLayer;
// 出团通知模板上传layer
var ontLayer;
var ProductContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.allCharges = [ "PRODUCT", "SALE", "NONE" ];
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
	self.chargeMapping = {
		'PRODUCT' : '产品包票',
		'SALE' : '销售包票',
		'NONE' : '无机票'
	};
	self.status = [ 'N', 'Y', 'D' ];
	self.saleMapping = {
		'N' : "架下",
		'Y' : "架上",
		'D' : "废弃"
	};
	self.chosenStatuses = ko.observableArray([]);
	self.chosenStatuses.push("N");
	self.chosenStatuses.push("Y");

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
				if (t != "no") {
					self.clientConfirmType("Y");
					self.clientConfirmTemplet(t);
					$("#c-c-t-a").show();
				}
				ctLayer = $.layer({
					type : 1,
					title : [ '上传组团确认模板', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '500px', '240px' ],
					offset : [ '', '' ],
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
				if (t != "no") {
					self.outNoticeType("Y");
					self.outNoticeTemplet(t);
					$("#o-n-t-a").show();
				}

				ontLayer = $.layer({
					type : 1,
					title : [ '上传出团通知模板', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '500px', '240px' ],
					offset : [ '', '' ],
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

	// 机票维护
	self.flightManagement = function() {
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
								if (data.product.air_ticket_upkeep_flg == "Y") {
									window.location.href = self.apiurl
											+ 'templates/product/flight-management-edit.jsp?key='
											+ self.chosenProducts()[0];
								} else {
									window.location.href = self.apiurl
											+ 'templates/product/flight-management.jsp?key='
											+ self.chosenProducts()[0];
								}
							});

		}
	}

	self.product = ko.observable({});
	self.airTickets = ko.observableArray([]);
	// 绑定机票
	self.bindingTicket = function() {
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
				var ticket_charge = self.product().air_ticket_charge;

				if (ticket_charge == "NO") {
					self.chosenCharge("PRODUCT");
				} else {
					self.chosenCharge(ticket_charge);
				}

				changeCharge(self.chosenCharge());

				self.airTickets(data.air_tickets);
				airTicketLayer = $.layer({
					type : 1,
					title : [ '添加机票信息', '' ],
					maxmin : false,
					closeBtn : [ 1, true ],
					shadeClose : false,
					area : [ '800px', '500px' ],
					offset : [ '', '' ],
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

	self.checkAirTicket = function(product_pk) {
		$.getJSON(
				self.apiurl + 'product/searchProductAirTicketInfoByProductPk',
				{
					product_pk : product_pk
				}, function(data) {
					self.product(data.product);

					self.airTickets(data.air_tickets);
					airTicketCheckLayer = $.layer({
						type : 1,
						title : [ '机票信息', '' ],
						maxmin : false,
						closeBtn : [ 1, true ],
						shadeClose : false,
						area : [ '800px', '500px' ],
						offset : [ '', '' ],
						scrollbar : true,
						page : {
							dom : '#air-ticket-check'
						},
						end : function() {
						}
					});
				});
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

function changeFile(thisx) {
	var file = thisx.files[0];
	var inputName = thisx.name;
	name_check = file.name;
	size = file.size;
	type = file.type;

	if (type.indexOf("msword") < 0) {
		fail_msg("请上传word文档");
		return;
	}
	if (size > 1048576) {
		fail_msg("文件大于1MB");
		return;
	}

	var fileNameInput = $(thisx).parent().next();
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/fileUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.responseType = "blob";

	xhr.onload = function() {
		if (this.status == 200) {
			var fileName = this.getResponseHeader("Content-Disposition").split(
					";")[1].split("=")[1];
			var blob = this.response;
			fileNameInput.val(fileName);
			success_msg("上传成功！请点击保存按钮以保存。");
		} else {
			fail_msg("上传失败，请重试或联系管理员！");
		}
	};
	xhr.send(formData);
}
function updateProgress(e, progress) {
	if (e.lengthComputable) {
		$(progress).attr({
			value : e.loaded,
			max : e.total
		});
	}
}

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