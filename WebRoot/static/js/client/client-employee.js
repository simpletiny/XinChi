var financialLayer;
var jobHoppingLayer;
var salesLayer;
var reviewLayer;
var checkFinancialLayer;
var commentLayer;
var headCheckLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenEmployees = ko.observableArray([]);
	self.employeeArea = ['哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化', '呼伦贝尔', '伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭'];

	// 销售信息
	self.sales = ko.observableArray([]);
	self.chosenSales = ko.observableArray([]);
	self.status = ['N', 'Y'];
	self.deleteMapping = {
		'N' : "正常",
		'Y' : "已停用"
	}
	self.relationMapping = {
		'朋友级' : "强",
		'商务级' : "强",
		'市场级' : "中",
		'新增级' : "弱",
		'排斥级' : "差",
		'' : '未知'
	}
	self.chosenStatus = ko.observableArray([]);
	self.chosenStatus.push("N");
	$.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
		self.sales(data.users);
		var pub = new Object();
		pub.user_name = "公开";
		pub.pk = "public";
		self.sales.push(pub);
	});

	self.createClientEmployee = function() {
		window.location.href = self.apiurl + "templates/client/employee-creation.jsp";
	};

	self.employees = ko.observable({
		total : 0,
		items : []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");

		var param = $("#form-search").serialize();
		if ($("#chk_public").is(":checked")) {
			param += "&employee.public_flgs=Y";
		} else {
			param += "&employee.public_flgs=N";
		}

		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(data) {
			self.employees(data.employees);
			console.log(data.employees);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			self.loadFiles();
			endLoadingIndicator();
		});
	};
	self.rld = ko.observable({
		sum_cnt : '',
		strong_cnt : '',
		middle_cnt : '',
		weak_cnt : '',
		bad_cnt : '',
		unknow_cnt : ''

	});
	self.refreshSumCnt = function() {
		var param = $("#form-search").serialize();
		$.getJSON(self.apiurl + 'client/searchSumCntData', param, function(data) {
			self.rld(data.rld);
		});
	}

	self.chosenUser = ko.observableArray([]);
	/**
	 * 调整客户所属销售
	 */
	self.changeSales = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择客户");
			return;
		} else {
			$.getJSON(self.apiurl + 'client/searchOneEmployee', {
				employee_pk : self.chosenEmployees()[0]
			}, function(data) {
				if (data.employee) {
					if (data.employee.review_flg == 'N') {
						fail_msg("客户未审核！！");
					} else {
						salesLayer = $.layer({
							type : 1,
							title : ['修改客户销售', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['600px', '400px'],
							offset : ['', ''],
							scrollbar : true,
							page : {
								dom : '#edit-sale'
							},
							end : function() {

							}
						});
					}
				} else {
					fail_msg("员工不存在！");
				}

			}).fail(function(reason) {
				fail_msg(reason.responseText);
			});
		}
	};
	self.checkSale = function() {
		if (self.chosenUser().contains("public")) {
			self.chosenUser.removeAll();
			self.chosenUser.push("public");
		}
		return true;
	}
	self.doChangeSale = function() {
		if (self.chosenUser().length == 0) {
			fail_msg("请选择销售！");
			return;
		}

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : '确认将选中的客户移至新销售名下吗?',
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					var data = {
						employee_pks : self.chosenEmployees(),
						sale_pks : self.chosenUser()
					};

					startLoadingSimpleIndicator("转移中...");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'client/changeEmployeeSales',
						traditional : true,
						data : data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(salesLayer);
							self.refresh();
							self.chosenEmployees.removeAll();
							self.chosenUser.removeAll();
						} else {
							fail_msg("转移失败，请联系管理员！");
						}
					});
				}
			}
		});
	};

	self.doCancelChangeSale = function() {
		layer.close(salesLayer);
	};

	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenEmployees().length == 1) {
			window.location.href = self.apiurl + "templates/client/employee-edit.jsp?key=" + self.chosenEmployees()[0];
		}
	};

	self.stopEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("只能选择一个员工");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认停用该客户吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("停用中");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'client/deleteClientEmployee',
							data : "employee_pks=" + self.chosenEmployees()
						}).success(function(str) {
							if (str == "success") {
								self.refresh();
								self.chosenEmployees.removeAll();
								endLoadingIndicator();
							} else {
								fail_msg("停用失败，请联系管理员！");
							}
						});
					}
				}
			});

		}

	};
	self.failMsg = ["exist_visit", "exist_accurate", "exist_order"];
	self.failMapping = {
		"exist_visit" : "有效拜访",
		"exist_accurate" : "有效沟通",
		"exist_order" : "订单"
	}
	/**
	 * 删除客户员工(物理删除)
	 */
	self.deleteEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("只能选择一个员工");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认删除该客户吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("删除中...");
						String
						data = {
							employee_pk : self.chosenEmployees()[0]
						};
						$.ajax({
							type : "POST",
							url : self.apiurl + 'client/deleteClientEmployeeReally',
							traditional : true,
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenEmployees.removeAll();

							} else if (self.failMsg.contains(str)) {
								fail_msg("该员工下存在{0}相关数据，不允许删除！".format(self.failMapping[str]));
							} else if (str == "alreadyreview") {
								fail_msg("客户资料已审核，不允许删除！")
							} else {
								fail_msg("停用失败，请联系管理员！");
							}
						});
					}
				}
			});

		}
	};
	self.employee = ko.observable({
		financial_body_name : ""
	});
	/**
	 * 跳槽
	 */
	self.jobHopping = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("只能选择一个员工");
			return;
		} else {
			$.getJSON(self.apiurl + 'client/searchOneEmployee', {
				employee_pk : self.chosenEmployees()[0]
			}, function(data) {
				if (data.employee) {
					self.employee(data.employee);
					if (self.employee().review_flg == "Y") {
						jobHoppingLayer = $.layer({
							type : 1,
							title : ['跳槽', ''],
							maxmin : false,
							closeBtn : [1, true],
							shadeClose : false,
							area : ['350px', '300px'],
							offset : ['', ''],
							scrollbar : true,
							page : {
								dom : '#job-hopping'
							},
							end : function() {
								console.log("Done");
							}
						});
					} else {
						fail_msg("员工还未审核！")
					}
				} else {
					fail_msg("员工不存在！");
				}
			}).fail(function(reason) {
				fail_msg(reason.responseText);
			});

		}
	};

	self.doJobHopping = function() {
		if (!$("#form-hopping").valid()) {
			return;
		}
		var data = $("#form-hopping").serialize();
		startLoadingIndicator("保存中...");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/jobHopping',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.clearFinancial();
				layer.close(jobHoppingLayer);
				self.refresh();
				self.chosenEmployees.removeAll();
			} else {
				fail_msg(str);
			}
		});
	};

	self.cancelJobHopping = function() {
		layer.close(jobHoppingLayer);
		self.clearFinancial();
	};
	/**
	 * 将员工标记为离职状态
	 */
	self.dimission = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("只能选择一个员工");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确定将员工标记为离职状态吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						var data = "employee.pk=" + self.chosenEmployees()[0] + "&employee.dimission_flg=Y";
						$.ajax({
							type : "POST",
							url : self.apiurl + 'client/swapDimission',
							traditional : true,
							data : data
						}).success(function(str) {
							layer.close(index);
							if (str == "success") {
								self.refresh();
								self.chosenEmployees.removeAll();
								endLoadingIndicator();
							} else {
								fail_msg("标记失败，请联系管理员！");
							}
						});
					}
				}
			})
		}

	}
	/**
	 * 合并客户员工，以第一个选中的为主
	 */
	self.combineEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length != 2) {
			fail_msg("合并功能只能而且必须选择两个员工");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '合并会将员工资料合并至第一个选择的员工名下，确定这么做吗？',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("合并中...");
						var data = {
							employee_pks : self.chosenEmployees()
						};
						$.ajax({
							type : "POST",
							url : self.apiurl + 'client/combineClientEmployee',
							traditional : true,
							data : data
						}).success(function(str) {
							if (str == "success") {
								self.refresh();
								self.chosenEmployees.removeAll();
								endLoadingIndicator();
							} else {
								fail_msg("合并失败，请联系管理员！");
							}
						});
					}
				}
			});
		}
	}
	self.publicEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认公开该客户吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("保存中...");
						var data = {
							employee_pks : self.chosenEmployees()
						};
						$.ajax({
							type : "POST",
							url : self.apiurl + 'client/publicClientEmployee',
							traditional : true,
							data : data
						}).success(function(str) {
							if (str == "success") {
								self.refresh();
								self.chosenEmployees.removeAll();
								endLoadingIndicator();
							} else {
								fail_msg("公开失败，请联系管理员！");
							}
						});
					}
				}
			});

		}

	};
	/**
	 * 审核客户员工
	 */
	self.reviewEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择员工");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("只能选择一个员工");
			return;
		} else {
			// 检测手机号和微信号
			$.ajax({
				type : "POST",
				url : self.apiurl + 'client/checkTelInfo',
				data : {
					employee_pk : self.chosenEmployees()[0]
				}
			}).success(function(data) {
				console.log(data);
				if (data.resultStr == "success") {
					$.getJSON(self.apiurl + 'client/searchOneEmployee', {
						employee_pk : self.chosenEmployees()[0]
					}, function(data) {
						if (data.employee) {
							self.employee(data.employee);
							if (self.employee().review_flg == "N") {
								reviewLayer = $.layer({
									type : 1,
									title : ['审核客户员工', ''],
									maxmin : false,
									closeBtn : [1, true],
									shadeClose : false,
									area : ['400px', '200px'],
									offset : ['', ''],
									scrollbar : true,
									page : {
										dom : '#div-review'
									},
									end : function() {
										console.log("Done");
									}
								});
							} else {
								success_msg("已经审核！");
							}
						} else {
							fail_msg("员工不存在！");
						}
					}).fail(function(reason) {
						fail_msg(reason.responseText);
					});
				} else {
					fail_msg("存在相同的电话号码或者微信号:" + data.employee.name);
				}
			});

		}
	};

	self.doReview = function() {
		if (!$("#form-review").valid()) {
			return;
		}
		var data = $("#form-review").serialize();
		startLoadingIndicator("保存中...");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/reviewEmployee',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.clearFinancial();
				layer.close(reviewLayer);
				self.refresh();
				self.chosenEmployees.removeAll();
			} else if (str == "exist") {
				fail_msg("已经存在同名客户员工！");
			} else {
				fail_msg(str);
			}
		});
	};
	self.cancelReview = function() {
		layer.close(reviewLayer);
		self.clearFinancial();
	};
	self.choseFinancial = function() {
		financialLayer = $.layer({
			type : 1,
			title : ['选择新的财务主体', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#financial_pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.clients = ko.observableArray([]);
	self.refreshClient = function() {
		var param = "client.client_short_name=" + $("#client_name").val() + "&client.statuses=N";
		param += "&page.start=" + self.startIndex1() + "&page.count=" + self.perPage1;

		$.getJSON(self.apiurl + 'client/searchCompanyByPage', param, function(data) {
			self.clients(data.clients);
			self.totalCount1(Math.ceil(data.page.total / self.perPage));
			self.setPageNums1(self.currentPage());
		});
	};
	self.searchFinancial = function() {
		self.refreshClient();

	};
	self.pickFinancial = function(name, pk) {
		$("#financial_body_name").val(name);
		$("#financial_body_pk").val(pk);
		$("#financial_body_name1").val(name);
		$("#financial_body_pk1").val(pk);
		layer.close(financialLayer);
	};

	self.clearFinancial = function() {
		$("#financial_body_name").val("");
		$("#financial_body_pk").val("");
		$("#financial_body_name1").val("");
		$("#financial_body_pk1").val("");
	}
	self.financial = ko.observable({});
	/**
	 * 查看财务主体详细
	 */
	self.checkFinancialBody = function(client_pk) {
		if (null != client_pk) {
			$.getJSON(self.apiurl + 'client/searchOneCompany', {
				client_pk : client_pk
			}, function(data) {
				if (data.client) {
					self.financial(data.client);
					console.log(self.financial());
					checkFinancialLayer = $.layer({
						type : 1,
						title : ['财务主体详情', ''],
						maxmin : false,
						closeBtn : [1, true],
						shadeClose : false,
						area : ['600px', '400px'],
						offset : ['', ''],
						scrollbar : true,
						page : {
							dom : '#check-financial'
						},
						end : function() {
							console.log("Done");
						}
					});
				} else {
					fail_msg("公司不存在！");
				}
			}).fail(function(reason) {
				fail_msg(reason.responseText);
			});

		} else {
			fail_msg("客户未审核！");
		}

	}

	self.clientEmployee = ko.observable({});
	// 添加/修改备注
	self.editComment = function(employee_pk) {
		$.getJSON(self.apiurl + 'client/searchOneEmployee', {
			employee_pk : employee_pk
		}, function(data) {
			self.clientEmployee(data.employee);
			commentLayer = $.layer({
				type : 1,
				title : ['备注', ''],
				maxmin : false,
				closeBtn : [1, true],
				shadeClose : false,
				area : ['500px', '300px'],
				offset : ['', ''],
				scrollbar : true,
				page : {
					dom : '#comment-edit'
				},
				end : function() {
					console.log("Done");
				}
			});

		});
	};

	self.cancelEditComment = function() {
		layer.close(commentLayer);
		$("#txt-comment").val('');
	};

	self.updateComment = function() {
		var employee_pk = self.clientEmployee().pk;
		var comment = $("#txt-comment").val();
		var data = "employee.pk=" + employee_pk + "&" + "employee.comment=" + comment;

		startLoadingIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + "client/updateEmployeeSimply",
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
				layer.close(commentLayer);
				$("#txt-comment").val('');
			}
		});
	};

	// 查看头像
	self.checkHeadPhoto = function(fileName) {
		$("#img-pic").attr("src", "");
		headCheckLayer = $.layer({
			type : 1,
			title : ['头像', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['320px', '355px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});
		if (fileName == "img") {
			$("#img-pic").attr("src", self.apiurl + "static/img/head.jpg");
		} else {
			$("#img-pic").attr("src",
					self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=CLIENT_EMPLOYEE_HEAD");
		}

	};

	// 加载头像
	self.loadFiles = function() {
		$("[st='st-file-name']").each(function(idx, stFileName) {
			var fileName = $(stFileName).val();
			if (fileName != "img") {
				self.downFile(stFileName, fileName);
			}
		});
	};

	self.downFile = function(stFileName, fileName) {
		var imgContainer = $(stFileName).prev();

		var formData = new FormData();
		formData.append("fileFileName", fileName);
		formData.append("fileType", "CLIENT_EMPLOYEE_MIN_HEAD");

		var url = ctx.apiurl + 'file/getFileStream';
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.responseType = "blob";
		xhr.onload = function() {
			if (this.status == 200) {
				var blob = this.response;
				imgContainer.attr("src", window.URL.createObjectURL(blob));
			}
		};
		xhr.send(formData);
	};

	// start pagination client
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

	self.turnPage1 = function(pageIndex1) {
		self.currentPage1(pageIndex1);
		self.refreshPage1();
	};

	self.setPageNums1 = function(curPage1) {
		var startPage1 = curPage1 - 4 > 0 ? curPage1 - 4 : 1;
		var endPage1 = curPage1 + 4 <= self.totalCount1() ? curPage1 + 4 : self.totalCount1();
		var pageNums1 = [];
		for (var i = startPage1; i <= endPage1; i++) {
			pageNums1.push(i);
		}
		self.pageNums1(pageNums1);
	};

	self.refreshPage1 = function() {
		self.refreshClient();
	};
	// end pagination client

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 100;
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

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
	ctx.refreshSumCnt();
	$("#img-pic").css({
		"width" : "320px",
		"height" : "320px"
	});

});
