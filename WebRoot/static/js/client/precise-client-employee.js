var commentLayer;
var headCheckLayer;
let checkFinancialLayer;
let clientEmployeeLayer;
var PreciseEmployeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenEmployees = ko.observableArray([]);
	self.employeeArea = ['哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化', '呼伦贝尔', '伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭'];

	//跳转创建精准客户页面
	self.createPreciseEmployee = function() {
		window.location.href = self.apiurl + "templates/client/precise-employee-creation.jsp";
	};

	self.employees = ko.observable({
		total: 0,
		items: []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");

		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'client/searchPreciseEmployeeByPage', param, function(data) {
			self.employees(data.employees);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
			self.loadFiles();
			endLoadingIndicator();
		});
	};

	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editPreciseEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择精准客户！");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("编辑只能选中一个精准客户！");
			return;
		} else if (self.chosenEmployees().length == 1) {
			window.location.href = self.apiurl + "templates/client/precise-employee-edit.jsp?key=" + self.chosenEmployees()[0].pk;
		}
	};

	/**
	 * 删除客户员工(物理删除)
	 */
	self.deleteEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择精准客户！");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("只能选择一个精准客户！");
			return;
		} else {
			$.layer({
				area: ['auto', 'auto'],
				dialog: {
					msg: '删除会把关联关系一并删除，确认删除该精准客户吗?',
					btns: 2,
					type: 4,
					btn: ['确认', '取消'],
					yes: function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("删除中...");
						const data = {
							employee_pk: self.chosenEmployees()[0].pk
						};
						$.ajax({
							type: "POST",
							url: self.apiurl + 'client/deletePreciseEmployee',
							data: data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
								self.chosenEmployees.removeAll();
							} else {
								fail_msg("删除失败，请联系管理员！");
							}
						});
					}
				}
			});

		}
	};

	self.financial = ko.observable({});
	/**
	 * 查看财务主体详细
	 */
	self.checkFinancialBody = function(client_pk) {
		if (null != client_pk) {
			$.getJSON(self.apiurl + 'client/searchOneCompany', {
				client_pk: client_pk
			}, function(data) {
				if (data.client) {
					self.financial(data.client);
					checkFinancialLayer = $.layer({
						type: 1,
						title: ['财务主体详情', ''],
						maxmin: false,
						closeBtn: [1, true],
						shadeClose: false,
						area: ['600px', '400px'],
						offset: ['', ''],
						scrollbar: true,
						page: {
							dom: '#check-financial'
						},
						end: function() {
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


	// 查看收入凭证
	self.checkPhoto = function(fileName, type) {
		$("#img-pic").attr("src", "");
		voucherLayer = $.layer({
			type: 1,
			title: ['查看图片', ''],
			maxmin: false,
			closeBtn: [1, true],
			shadeClose: false,
			area: ['600px', '650px'],
			offset: ['50px', ''],
			scrollbar: true,
			page: {
				dom: '#pic-check'
			},
			end: function() {
				console.log("Done");
			}
		});

		const file_folder = type === "self" ? "PRECISE_SELF_PHOTO" : "PRECISE_OTHER_PHOTO";
		$("#img-pic").css({
			"width": "550px",
			"height": "600px"
		});
		$("#img-pic").attr(
			"src",
			self.apiurl + 'file/getFileStream?fileFileName=' + fileName
			+ "&fileType=" + file_folder);

	};
	self.clientEmployee = ko.observable({});
	// 添加/修改备注
	self.editComment = function(employee_pk) {
		$.getJSON(self.apiurl + 'client/searchPreciseEmployeeByPk', {
			employee_pk: employee_pk
		}, function(data) {
			self.clientEmployee(data.employee);
			commentLayer = $.layer({
				type: 1,
				title: ['备注', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['500px', '300px'],
				offset: ['', ''],
				scrollbar: true,
				page: {
					dom: '#comment-edit'
				},
				end: function() {
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
			type: "POST",
			url: self.apiurl + "client/updatePreciseEmployeeSimp",
			data: data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
				layer.close(commentLayer);
				$("#txt-comment").val('');
			}
		});
	};

	self.filters = [{
		name: "is_binding",
		key: "bindinged",
		value: "已关联"
	}, {
		name: "is_binding",
		key: "notbindinged",
		value: "未关联"
	}, {
		name: "same_cellphone",
		key: "same_cellphone",
		value: "同电话"
	}, {
		name: "same_name",
		key: "same_name",
		value: "同姓名"
	}, {
		name: "same_area",
		key: "same_area",
		value: "同城市"
	}, {
		name: "same_county",
		key: "same_county",
		value: "同区县"
	}];
	self.chosenFilters = ko.observableArray([]);
	self.chosenFilters.push("notbindinged");
	// 查看头像
	self.checkHeadPhoto = function(fileName) {
		$("#img-pic").attr("src", "");
		headCheckLayer = $.layer({
			type: 1,
			title: ['头像', ''],
			maxmin: false,
			closeBtn: [1, true],
			shadeClose: false,
			area: ['320px', '355px'],
			offset: ['', ''],
			scrollbar: true,
			page: {
				dom: '#pic-check'
			},
			end: function() {
				console.log("Done");
			}
		});
		$("#img-pic").css({
			"width": "320px",
			"height": "320px"
		});
		if (fileName == "img") {
			$("#img-pic").attr("src", self.apiurl + "static/img/head.jpg");
		} else {
			$("#img-pic").attr("src",
				self.apiurl + 'file/getFileStream?fileFileName=' + fileName + "&fileType=PRECISE_HEAD_FULL");
		}
	};
	//关联客户资料
	self.chosenClientEmployees = ko.observableArray([]);
	self.removeClientEmployee = function(data) {
		self.chosenClientEmployees.remove(data);
	}
	self.bindingEmployee = function() {
		if (self.chosenEmployees().length == 0) {
			fail_msg("请选择精准客户！");
			return;
		} else if (self.chosenEmployees().length > 1) {
			fail_msg("只能选择一个精准客户！");
			return;
		} else {
			clientEmployeeLayer = $.layer({
				type: 1,
				title: ['选择客户资料', ''],
				maxmin: false,
				closeBtn: [1, true],
				shadeClose: false,
				area: ['1250px', '850px'],
				offset: ['', ''],
				scrollbar: true,
				page: {
					dom: '#client-employee-pick'
				},
				end: function() {
					console.log("Done");
				}
			});
		}
	}
	self.doBinding = function() {
		$.layer({
			area: ['auto', 'auto'],
			dialog: {
				msg: '确认关联这些客户资料吗?',
				btns: 2,
				type: 4,
				btn: ['确认', '取消'],
				yes: function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("保存中");
					let binding_obj = {};
					binding_obj.precise_pk = self.chosenEmployees()[0].pk;
					binding_obj.employee_pks = self.chosenClientEmployees().map(item => item.pk);
					const data = "json=" + JSON.stringify(binding_obj);
					$.ajax({
						type: "POST",
						url: self.apiurl + 'client/bindingPreciseEmployees',
						data: data
					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(clientEmployeeLayer);
							self.refresh();
							self.chosenEmployees.removeAll();
							self.chosenClientEmployees.removeAll();
						} else {
							fail_msg("关联失败，请联系管理员！");
						}
					});
				}
			}
		});
	}
	self.cancelBinding = function() {
		self.chosenClientEmployees.removeAll();
		layer.close(clientEmployeeLayer);
	}

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
		formData.append("fileType", "PRECISE_HEAD_MIN");

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

	//search client employees start
	self.clientEmployees = ko.observable({});
	self.refreshClient = function() {
		startLoadingSimpleIndicator("加载中……");
		const precise_employee = self.chosenEmployees()[0];
		let params = $("#form-search-employee").serializeArray();
		self.chosenFilters().forEach(function(item, index) {
			switch (item) {
				case "bindinged":
					params.push({ name: "employee.binding_statuses", value: "Y" });
					break;
				case "notbindinged":
					params.push({ name: "employee.binding_statuses", value: "N" });
					break;
				case "same_cellphone":
					params.push({ name: "employee.cellphone", value: precise_employee.cellphone });
					break;
				case "same_name":
					params = params.filter(item => item.name !== "employee.name");
					params.push({ name: "employee.name", value: precise_employee.name });
					break;
				case "same_area":
					params = params.filter(item => item.name !== "employee.employee_area");
					params.push({ name: "employee.employee_area", value: precise_employee.employee_area });
					break;
				case "same_county":
					params.push({ name: "employee.employee_county", value: precise_employee.employee_county });
					break;
				default:
			}
		});
		params.push({ name: "page.start", value: self.startIndex1() });
		params.push({ name: "page.count", value: self.perPage1 });
		let param = $.param(params);
		let selected = self.chosenClientEmployees();
		$.getJSON(self.apiurl + 'client/searchEmployeeByPage', param, function(data) {
			data.employees.forEach(function(item, index) {
				let matched = selected.find(function(sel) {
					return sel.pk === item.pk;
				});
				if (matched) {
					data.employees[index] = matched;
				}
			});

			self.clientEmployees(data.employees);

			self.totalCount1(Math.ceil(data.page.total / self.perPage));
			self.setPageNums1(self.currentPage1());

			endLoadingIndicator();
		});
	};

	self.searchClientEmployee = function() {
		self.refreshClient();
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
		var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage = curPage + 4 <= self.totalCount1() ? curPage + 4 : self.totalCount1();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums1(pageNums);
	};

	self.refreshPage1 = function() {
		self.searchClientEmployee();
	};

	// end pagination
	//search client employees end
};

var ctx = new PreciseEmployeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

