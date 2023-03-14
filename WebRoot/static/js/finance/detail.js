var DetailContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.type = ['收入', '支出', '内转'];
	self.chosenDetails = ko.observableArray([]);
	self.accounts = ko.observableArray([]);
	$.getJSON(self.apiurl + 'finance/searchAllAccounts', {}, function(data) {
		if (data.accounts) {
			self.accounts(data.accounts);
		} else {
			fail_msg("不存在账户，无法建立明细账！");
		}
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.createDetail = function(type) {
		window.location.href = self.apiurl + "templates/finance/" + type + "-detail-creation.jsp";
	};

	self.details = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		startLoadingIndicator("加载中...");
		var param = $("form").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'finance/searchDetailByPage', param, function(data) {
			self.details(data.details);
			$(".rmb").formatCurrency();

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());

			endLoadingIndicator();
		});
	};

	self.modify = function() {
		var len = self.chosenDetails().length;
		if (len < 1) {
			fail_msg("请选择明细");
		} else if (len > 1) {
			fail_msg("只能选择一条明细");
		} else if (len == 1) {
			var detailId = self.chosenDetails()[0];
			$.getJSON(
					self.apiurl + 'finance/searchDetailByPk',
					"detailId=" + detailId,
					function(data) {
						if (data.detail) {
							var detail = data.detail;
							if (detail.finance_flg == "Y") {
								if (detail.type == "收入") {
									window.location.href = self.apiurl
											+ "templates/finance/receive-detail-edit.jsp?key="
											+ self.chosenDetails()[0];
								} else {
									window.location.href = self.apiurl + "templates/finance/pay-detail-edit.jsp?key="
											+ self.chosenDetails()[0];
								}
							} else if (detail.finance_flg == "N") {
								if (detail.type == "收入") {
									window.location.href = self.apiurl
											+ "templates/finance/normal-receive-detail-edit.jsp?key="
											+ self.chosenDetails()[0];
								} else {
									window.location.href = self.apiurl + "templates/finance/pay-detail-edit.jsp?key="
											+ self.chosenDetails()[0];
								}
							}
						} else {
							fail_msg("不存的明细");
						}
					}).fail(function(reason) {
				fail_msg(reason.responseText);
			});

		}
	};

	self.modify_temp = function() {
		var len = self.chosenDetails().length;
		if (len < 1) {
			fail_msg("请选择明细");
		} else if (len > 1) {
			fail_msg("只能选择一条明细");
		} else if (len == 1) {
			var detailId = self.chosenDetails()[0];
			$.getJSON(
					self.apiurl + 'finance/searchDetailByPk',
					"detailId=" + detailId,
					function(data) {
						if (data.detail) {
							var detail = data.detail;
							if (detail.finance_flg == "Y") {
								if (detail.type == "收入") {
									window.location.href = self.apiurl
											+ "templates/finance/receive-detail-edit.jsp?key="
											+ self.chosenDetails()[0];
								} else {
									window.location.href = self.apiurl + "templates/finance/pay-detail-edit.jsp?key="
											+ self.chosenDetails()[0];
								}
							} else if (detail.finance_flg == "N") {
								if (detail.type == "收入") {
									var match_flg = detail.match_flg;
									if (match_flg == 'Y') {
										fail_msg("收入已匹配，不能修改！");
										return;
									}
									var create_time = new Date(detail.create_time - 0)
									var diff = dateDiff(create_time, new Date());
									var diff_day = diff.substring(0, diff.indexOf("天")) - 0;
									if (diff_day >= 3) {
										fail_msg("只能修改三天以内记录的收入！")
										return;
									}

									window.location.href = self.apiurl
											+ "templates/finance/normal-receive-detail-edit.jsp?key="
											+ self.chosenDetails()[0];
								} else {
									fail_msg("支出不能修改！");
								}
							}
						} else {
							fail_msg("不存的明细");
						}
					}).fail(function(reason) {
				fail_msg(reason.responseText);
			});

		}
	}

	self.deleteDetail_temp = function() {
		var len = self.chosenDetails().length;
		if (len < 1) {
			fail_msg("请选择明细");
		} else if (len > 1) {
			fail_msg("只能选择一条明细");
		} else if (len == 1) {
			var detailId = self.chosenDetails()[0];

			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要删除此条明细吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						$.getJSON(self.apiurl + 'finance/searchDetailByPk', "detailId=" + detailId, function(data) {
							if (data.detail) {
								var detail = data.detail;
								if (detail.finance_flg == "Y") {
									startLoadingSimpleIndicator("删除中");
									$.ajax({
										type : "POST",
										url : self.apiurl + 'finance/deleteDetail',
										data : "detailId=" + detailId
									}).success(function(str) {

										if (str == "success") {
											success_msg("删除成功！");
											self.chosenDetails.removeAll();
										} else if (str == "forbidden") {
											fail_msg("只能删除财务收支！");
										} else {
											fail_msg("删除失败，请联系管理员");
										}
										self.search();
										endLoadingIndicator();
									});
								} else if (detail.finance_flg == "N") {
									if (detail.type == "收入") {
										var match_flg = detail.match_flg;
										if (match_flg == 'Y') {
											fail_msg("收入已匹配，不能删除！");
											return;
										}
										var create_time = new Date(detail.create_time - 0)
										var diff = dateDiff(create_time, new Date());
										var diff_day = diff.substring(0, diff.indexOf("天")) - 0;
										if (diff_day >= 3) {
											fail_msg("只能删除三天以内记录的收入！")
											return;
										}
										startLoadingSimpleIndicator("删除中");
										$.ajax({
											type : "POST",
											url : self.apiurl + 'finance/deleteDetail',
											data : "detailId=" + detailId
										}).success(function(str) {

											if (str == "success") {
												success_msg("删除成功！");
												self.chosenDetails.removeAll();
											} else if (str == "forbidden") {
												fail_msg("只能删除财务收支！");
											} else {
												fail_msg("删除失败，请联系管理员");
											}
											self.search();
											endLoadingIndicator();
										});

									} else {
										fail_msg("支出不能删除！");
									}
								}
							} else {
								fail_msg("不存的明细");
							}
						}).fail(function(reason) {
							fail_msg(reason.responseText);
						});

					}
				}
			});
		}
	}

	self.deleteDetail = function() {
		var len = self.chosenDetails().length;
		if (len < 1) {
			fail_msg("请选择明细");
		} else if (len > 1) {
			fail_msg("只能选择一条明细");
		} else if (len == 1) {
			var detailId = self.chosenDetails()[0];
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : '确认要删除此条明细吗?',
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						startLoadingSimpleIndicator("删除中");
						$.ajax({
							type : "POST",
							url : self.apiurl + 'finance/deleteDetail',
							data : "detailId=" + detailId
						}).success(function(str) {

							if (str == "success") {
								success_msg("删除成功！");
								self.chosenDetails.removeAll();
							} else if (str == "forbidden") {
								fail_msg("只能删除财务收支！");
							} else if (str == "matched") {
								fail_msg("收入已匹配，不能删除！");
							} else {
								fail_msg("删除失败，请联系管理员");
							}
							self.search();
							endLoadingIndicator();
						});
						layer.close(index);
					}
				}
			});
		}
	};
	self.account = ko.observable();
	self.bat_details = ko.observableArray([]);
	// 批量上传
	self.upload = function() {
		$(".file-path").val("");
		$("#file-upload").val("");
		self.account("微信9544");
		uploadLayer = $.layer({
			type : 1,
			title : ['选择文件', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['550px', '250px'],
			offset : ['', ''],
			scrollbar : true,
			page : {
				dom : '#div-upload'
			},
			end : function() {

			}
		});
	}
	// 对上传的文件进行操作
	self.doUpload = function() {
		if ($("#file-upload").val().trim() == "") {
			fail_msg("请选择要上传的文件！");
			return;
		}
		layer.close(uploadLayer);
		startLoadingSimpleIndicator("处理中...");
		var data = "file_name=" + $("#csv-file").val();
		$.ajax({
			type : "POST",
			url : self.apiurl + 'finance/batUploadReceived',
			data : data,
			success : function(data) {
				self.bat_details(data.details);
				$(".rmb").formatCurrency();
				endLoadingIndicator();

				$(self.bat_details()).each(function(idx, data) {
					data.old_time = data.time;
				});

				uploadConfirmLayer = $.layer({
					type : 1,
					title : ['上传确认', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['1200px', '750px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#div-upload-confirm'
					},
					end : function() {
					}
				});
			}
		});
	}

	// 取消上传
	self.cancelUpload = function() {
		layer.close(uploadLayer);
	}

	self.doSaveBat = function() {
		var confirmed = new Array();
		var isLegal = true;
		$(self.bat_details()).each(function(idx, data) {
			if (!data.second || data.second.trim().length != 2) {
				isLegal = false;
				return;
			}
		});

		if (!isLegal) {
			fail_msg("请准确填写支出秒！")
			return;
		}

		$(self.bat_details()).each(function(idx, data) {
			data.time = data.old_time + ":" + data.second;
			confirmed.push(data);
		});

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : "确认要上传这些收入记录吗？",
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("保存中...");
					var data = "account=" + self.account() + "&json=" + JSON.stringify(confirmed, replacer);
					$.ajax({
						type : "POST",
						url : self.apiurl + 'finance/batSaveReceived',
						data : data,
						success : function(str) {
							layer.close(uploadConfirmLayer);
							endLoadingIndicator();
							if (str == "success") {
								success_msg("上传成功！");
								self.refresh();
							} else if (str.indexOf("time") >= 0) {
								fail_msg(str.split("&")[1] + "时间下存在收入！");
							} else if (str == "repeat") {
								fail_msg("上传的数据中有重复的时间！")
							} else {
								fail_msg(str);
							}
						}
					});
				}
			}
		});

	}

	self.cancelSaveBat = function() {
		layer.close(uploadConfirmLayer);
		self.bat_details.removeAll();
	}
	self.test = function() {
		alert("test")
	}

	self.search = function() {
		self.refresh();
	};

	self.resetPage = function() {

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

var ctx = new DetailContext();

$(document).ready(function() {
	// $('.month-picker-st').AlertSelf();
	ko.applyBindings(ctx);
	ctx.refresh();
	$('.month-picker-st').MonthPicker({
		Button : false,
		MonthFormat : 'yy-mm'
	});
	$('.file-csv').change(function() {
		uploadOffice({
			input : this,
			type : 'csv',
			required : "yes"
		});
	});
});

// function changeFile(thisx) {
// var file = thisx.files[0];
// name = file.name;
// size = file.size;
// type = file.type;
//
// if (type.indexOf("csv") < 0) {
// fail_msg("请上传csv文件！");
// return;
// }
// if (size > 102400) {
// fail_msg("文件不能大于100KB!");
// return;
// }
// startLoadingSimpleIndicator("导入中");
// var formData = new FormData();
// formData.append("file", file);
//
// var url = ctx.apiurl + 'file/detailExcelUpload';
// var xhr = new XMLHttpRequest();
// xhr.open('POST', url, true);
// xhr.onload = function() {
// if (this.status == 200) {
//
// var blob = this.response;
// if (blob == "OK") {
// success_msg("导入成功");
// } else if (blob == "BEFORE") {
// fail_msg("导入数据的交易时间不能早于系统已存在明细的时间");
// }
// endLoadingIndicator();
// }
// };
// xhr.send(formData);
// }

function lessthan60(event, data) {
	var txt = event.target;
	var seconds = $(txt).val();

	if (seconds.length == 1) {
		if (seconds - 0 > 5) {
			$(txt).val("");
			return;
		}
	} else if (seconds.length == 2) {
		if (seconds - 0 > 59) {
			$(txt).val($(txt).val().charAt(0));
			return;
		}
	} else {
		$(txt).val($(txt).val().substr(0, 2));
	}
	seconds = $(txt).val();
	var time = $(txt).next().val();
	$(txt).parent().prev().html(time + ":" + seconds);
}
var replacer = function(key, value) {
	if (typeof value === "number") {
		return value + "";
	}

	// if (typeof value === "string") {
	// if (value.indexOf(":") > -1) {
	// return value.replaceAll(":", "\\:");
	// }
	// }

	return value;
}