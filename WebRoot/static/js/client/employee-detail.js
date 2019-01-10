var employeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.employeePk = $("#employee_key").val();
	self.employee = ko.observable({
		sales : []
	});
	self.recovery = function() {
		$.layer({
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '确认恢复该客户吗?',
				btns : 2,
				type : 4,
				btn : [ '确认', '取消' ],
				yes : function(index) {
					layer.close(index);
					startLoadingSimpleIndicator("恢复中");
					$.ajax({
						type : "POST",
						url : self.apiurl + 'client/recoveryClientEmployee',
						data : "employee_pks=" + self.employeePk
					}).success(function(str) {
						if (str == "success") {
							location.reload();
							endLoadingIndicator();
						} else {
							fail_msg("恢复失败，请联系管理员！");
						}
					});
				}
			}
		});

	};

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'client/searchOneEmployee', {
		employee_pk : self.employeePk
	}, function(data) {
		if (data.employee) {
			self.employee(data.employee);
			self.loadFiles();
		} else {
			fail_msg("员工不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
	// 加载头像
	self.loadFiles = function() {
		var fileName = $("#head").val();
		if (fileName != "img") {
			self.downFile(fileName);
		}
	};

	self.downFile = function(fileName) {
		var imgContainer = $("#avatar");

		var formData = new FormData();
		formData.append("fileFileName", fileName);
		formData.append("fileType", "CLIENT_EMPLOYEE_HEAD");

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
	//查看拜访记录
	self.visitRecord = function(){
		window.location.href = self.apiurl + "templates/client/visit-view.jsp?key="+self.employeePk;
	};
	//查看精推
	self.accurateSaleRecord = function(){
		window.location.href = self.apiurl + "templates/client/accurate-sale-view.jsp?key="+self.employeePk;
	};
};

var ctx = new employeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
