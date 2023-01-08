var clearLayer;
var DataContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	var x = new Date();
	self.today = ko.observable();
	self.today(x.Format("yyyy-MM-dd"));

	self.locations = ko.observableArray();
	self.refreshLocations = function() {
		var param = "type=LINE";
		$.getJSON(self.apiurl + 'system/searchByType', param, function(data) {
			self.locations(data.datas);
		});
	};

	self.createLine = function() {
		var name = $("#txt-line").val();
		var order_index = $("#lineGrid").children().length + 1;

		var data = "baseData.type=LINE&baseData.order_index=" + order_index + "&baseData.name=" + name;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'system/createBaseData',
			data : data

		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refreshLocations();
			} else if (str == "exists") {
				fail_msg("已存在“" + name + "”产品线！");
			}
		});
	}
	self.deleteLine = function() {
		var can_edit = $("#txt-edit").val();
		var old_name = $("#txt-old-name").val();
		if (can_edit == "N") {
			fail_msg("“" + old_name + "”是系统自带产品线，不允许删除！");
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "确定要删除“" + old_name + "”产品线吗？",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("删除中！");
						var pk = $("#txt-pk").val();
						var data = "baseData.type=LINE&baseData.pk=" + pk + "&baseData.name=" + old_name;

						$.ajax({
							type : "POST",
							url : self.apiurl + 'system/deleteBaseData',
							data : data

						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.cancelUpdate();
								self.refreshLocations();
							} else if (str == "exists") {
								fail_msg("“" + old_name + "”产品线下已存在产品，不允许删除！");
							}
						});
					}
				}
			});

		}
	}
	self.updateLine = function() {
		var can_edit = $("#txt-edit").val();
		var old_name = $("#txt-old-name").val();
		if (can_edit == "N") {
			fail_msg("“" + old_name + "”是系统自带产品线，不允许修改！");
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "更新会将系统中当前产品线下的产品信息一并修改。",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("更新中！");
						var pk = $("#txt-pk").val();
						var name = $("#txt-line").val();
						var data = "baseData.type=LINE&baseData.name=" + name + "&baseData.pk=" + pk;

						$.ajax({
							type : "POST",
							url : self.apiurl + 'system/updateBaseData',
							data : data

						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refreshLocations();
							} else if (str == "exists") {
								fail_msg("已存在“" + name + "”产品线！");
							}
						});
					}
				}
			});

		}
	}

	self.cancelUpdate = function() {
		$("#txt-line").val("");
		$("#txt-pk").val("");
		$("#txt-edit").val("");
		$("#txt-old-name").val("");
		$(".update").hide();
		$(".create").show();
	}

	self.setLineName = function(name, pk, can_edit) {
		$("#txt-line").val(name);
		$("#txt-pk").val(pk);
		$("#txt-edit").val(can_edit);
		$("#txt-old-name").val(name);
		$(".update").show();
		$(".create").hide();
	}
	self.sortProductLine = function() {
		startLoadingIndicator("排序中！");
		var allDivs = $("#lineGrid").children();
		var json = "[";
		for (var i = 0; i < allDivs.length; i++) {
			var div = allDivs[i];
			var pk = $(div).find("input").val();
			json += '{"pk":"' + pk + '","order_index":"' + (i + 1) + '"}';
			if (i != allDivs.length - 1) {
				json += ',';
			}
		}
		json += ']';

		var data = "json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'system/sortProductLine',
			data : data

		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				// do nothing
			} else {
				fail_msg("排序失败，请联系管理员！");
			}
		});
	}

	// 呆账设置
	self.badDenominator = ko.observableArray();
	var txtArray = ['一分之', '十分之', '百分之', '千分之', '万分之', '十万分之'];
	for (var i = 5; i >= 0; i--) {
		var denominator = new Object();
		denominator.value = Math.pow(10, i);
		denominator.text = txtArray[i];
		self.badDenominator.push(denominator);
	}
	self.chosenDenominator = ko.observable();
	self.changeDenominator = function() {
		var denominator = self.chosenDenominator();
		var numerator = $("#txt-bad-numerator").val().trim();
		$("#l-bad-rate").text(numerator / denominator);
	}

	self.days = ko.observableArray();
	for (var i = 0; i < 28; i++) {
		var day = new Object();
		day.value = i + 1;
		day.text = (i + 1) + "日";
		self.days.push(day);
	}
	self.chosenDay = ko.observable();
	self.badConfig = ko.observable();
	self.badNumerator = ko.observable();
	self.isAuto = ko.observable();

	self.refreshBadConfig = function() {
		var param = "type=BAD";
		$.getJSON(self.apiurl + 'system/searchByType', param, function(data) {
			self.badConfig(data.datas[0]);
			self.chosenDay(self.badConfig().ext3);
			self.chosenDenominator(self.badConfig().ext1);
			self.badNumerator(self.badConfig().ext2);
			self.isAuto(self.badConfig().code == "AUTO" ? true : false);
			self.changeDenominator();
		});
	};

	self.cleanBadPunishment = function() {
		clearLayer = $.layer({
			type : 1,
			title : ['立即清除', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['400px', '240px'],
			offset : ['150px', ''],
			scrollbar : true,
			page : {
				dom : '#div-clear'
			},
			end : function() {
				console.log("Done");
			}
		});
	}
	self.confirmCleanBadInterest = function() {
		var date = $("#txt-clean-date").val();
		if (date.length != 10)
			return;
		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : "确认立即清除" + date + "(含)之前的呆账罚息累计数据吗？",
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {

					startLoadingIndicator("清除中...");
					var pk = 'pk_clean_bad';
					var ext1 = date;
					var data = "baseData.type=BAD&baseData.pk=" + pk + "&baseData.ext1=" + ext1;
					$.ajax({
						type : "POST",
						url : self.apiurl + 'system/updateBaseData',
						data : data

					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							layer.close(index);
							// self.refreshBadConfig();
							layer.close(clearLayer);
						}
					});
				}
			}
		});

	}
	self.cancelCleanBadInterest = function() {
		layer.close(clearLayer);
	};
	self.saveBadConfig = function() {
		var can_edit = self.badConfig().can_edit;
		if (can_edit == "N") {
			fail_msg("不允许修改！请联系管理员！");
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "更新后不会影响之前的呆账罚息！",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("更新中！");
						var pk = self.badConfig().pk;
						var code = $("#chk-isauto").is(':checked') ? "AUTO" : "HUM";
						var ext1 = self.chosenDenominator();
						var ext2 = $("#txt-bad-numerator").val().trim();
						var ext3 = self.chosenDay();

						var data = "baseData.type=BAD&baseData.pk=" + pk + "&baseData.ext1=" + ext1 + "&baseData.ext2="
								+ ext2 + "&baseData.ext3=" + ext3 + "&baseData.code=" + code;
						$.ajax({
							type : "POST",
							url : self.apiurl + 'system/updateBaseData',
							data : data

						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refreshBadConfig();
							}
						});
					}
				}
			});

		}
	}

	self.teamConfig = ko.observable();
	self.saleCost = ko.observable();
	self.sysCost = ko.observable();
	// 单团核算相关
	self.refreshTeamConfig = function() {
		var param = "type=TEAM";
		$.getJSON(self.apiurl + 'system/searchByType', param, function(data) {
			self.teamConfig(data.datas[0]);
			self.saleCost(self.teamConfig().ext1)
			self.sysCost(self.teamConfig().ext2)
		});
	};

	self.saveTeamConfig = function() {

		$.layer({
			area : ['auto', 'auto'],
			dialog : {
				msg : "更新不会影响今日之前的数据！",
				btns : 2,
				type : 4,
				btn : ['确认', '取消'],
				yes : function(index) {
					layer.close(index);
					startLoadingIndicator("更新中！");
					var pk = self.teamConfig().pk;
					var ext1 = $("#txt-sale-cost").val().trim();
					var ext2 = $("#txt-sys-cost").val().trim();

					var data = "baseData.type=TEAM&baseData.pk=" + pk + "&baseData.ext1=" + ext1 + "&baseData.ext2="
							+ ext2;
					$.ajax({
						type : "POST",
						url : self.apiurl + 'system/updateBaseData',
						data : data

					}).success(function(str) {
						endLoadingIndicator();
						if (str == "success") {
							self.refreshTeamConfig();
							success_msg("更新成功！")
						}
					});
				}
			}
		});

	}

	self.saleCreditConfig = ko.observable();
	self.saleCreditFlg = ko.observable();
	// 销售信用额度启用/停用
	self.refreshSaleCreditConfig = function() {
		var param = "type=SCREDIT";
		$.getJSON(self.apiurl + 'system/searchByType', param, function(data) {
			self.saleCreditConfig(data.datas[0]);
			self.saleCreditFlg(data.datas[0].ext1);
		});
	};
	self.changeSaleCredit = function(flg) {
		startLoadingIndicator("更新中……");
		var pk = self.saleCreditConfig().pk;
		console.log(pk);
		var data = "baseData.type=SCREDIT&baseData.pk=" + pk + "&baseData.ext1=" + flg;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'system/updateBaseData',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				self.refreshSaleCreditConfig();
				if (flg == "Y") {
					success_msg("销售信用额度已启用！")
				} else {
					success_msg("销售信用额度已停用！")
				}
			}
		});
	}
};

var ctx = new DataContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refreshLocations();

	new Sortable(lineGrid, {
		animation : 150,
		ghostClass : 'blue-background-class',
		onEnd : function(e) {
			ctx.sortProductLine();
		}
	});
	ctx.refreshBadConfig();
	ctx.refreshTeamConfig();
	ctx.refreshSaleCreditConfig();
});
