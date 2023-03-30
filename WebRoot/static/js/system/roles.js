var clearLayer;
var DataContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.user_roles = ko.observableArray([]);
	self.refreshUserRoles = function() {
		var param = "type=ROLE";
		$.getJSON(self.apiurl + 'system/searchByType', param, function(data) {
			self.user_roles(data.datas);
		});
	};
	self.sortUserRoles = function() {
		startLoadingIndicator("排序中！");
		var allDivs = $("#roleGrid").children();
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
			url : self.apiurl + 'system/sortData',
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
	new Sortable(roleGrid, {
		animation : 150,
		ghostClass : 'blue-background-class',
		onEnd : function(e) {
			ctx.sortUserRoles();
		}
	});
	ctx.refreshUserRoles();
});
