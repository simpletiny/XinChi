var DataContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

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

		var data = "baseData.type=LINE&baseData.order_index=" + order_index
				+ "&baseData.name=" + name;
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
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : "确定要删除“" + old_name + "”产品线吗？",
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("删除中！");
						var pk = $("#txt-pk").val();
						var data = "baseData.type=LINE&baseData.pk=" + pk
								+ "&baseData.name=" + old_name;

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
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : "更新会将系统中当前产品线下的产品信息一并修改。",
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						layer.close(index);
						startLoadingIndicator("更新中！");
						var pk = $("#txt-pk").val();
						var name = $("#txt-line").val();
						var data = "baseData.type=LINE&baseData.name=" + name
								+ "&baseData.pk=" + pk;

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
});
