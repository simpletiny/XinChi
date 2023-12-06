var financialLayer;
var EmployeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.employeePk = $("#employee_key").val();
	self.employee = ko.observable({});
	self.genders = ['男', '女'];
	self.employeeType = ['未知', '员工', '老板', '包桌'];
	// self.employeeArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆' ];
	// self.clientType = [ '注册', '挂靠', '独立旅游人', '夫妻店', '其他' ];
	// self.sales = ko.observableArray([]);
	self.clients = ko.observable({
		total : 0,
		items : []
	});
	// self.choosenSales = ko.observableArray([]);
	self.publicFlg = ko.observable();

	startLoadingSimpleIndicator("加载中");

	// $.getJSON(self.apiurl + 'user/searchAllSales', {}, function(data) {
	// self.sales(data.users);
	// });

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

	self.choseFinancial = function() {
		financialLayer = $.layer({
			type : 1,
			title : ['选择财务主体', ''],
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

	self.refresh = function() {
		var param = "client.client_short_name=" + $("#client_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'client/searchCompanyByPage', param, function(data) {
			self.clients(data.clients);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.searchFinancial = function() {
		self.refresh();

	};

	self.pickFinancial = function(name, pk) {
		$("#financial_body_name").val(name);
		$("#financial_body_pk").val(pk);
		layer.close(financialLayer);
	};

	self.saveEmployee = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/updateEmployee',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/client/client-employee.jsp";
			} else if (str == "existcellphone") {
				fail_msg("存在相同手机号！")
			} else if (str == "existwechat") {
				fail_msg("存在相同微信号！")
			} else if (str == "exist") {
				fail_msg("同一财务主体下存在同名客户!");
			}
		});
	};

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
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 10;
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
		self.searchFinancial();

	};
};

var ctx = new EmployeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);

	var avatar = document.getElementById('avatar');
	var image = document.getElementById('image');
	var input = document.getElementById('input');
	var $modal = $('#modal');
	var cropper;

	$('[data-toggle="tooltip"]').tooltip();

	input.addEventListener('change', function(e) {
		var files = e.target.files;
		var done = function(url) {
			input.value = '';
			image.src = url;
			$modal.modal('show');
		};
		var reader;
		var file;
		var url;

		if (files && files.length > 0) {
			file = files[0];

			if (URL) {
				done(URL.createObjectURL(file));
			} else if (FileReader) {
				reader = new FileReader();
				reader.onload = function(e) {
					done(reader.result);
				};
				reader.readAsDataURL(file);
			}
		}
	});

	$modal.on('shown.bs.modal', function() {
		cropper = new Cropper(image, {
			aspectRatio : 1,
			viewMode : 3,
		});
	}).on('hidden.bs.modal', function() {
		cropper.destroy();
		cropper = null;
	});

	document.getElementById('crop').addEventListener('click', function() {
		var initialAvatarURL;
		var canvas;

		$modal.modal('hide');

		if (cropper) {
			canvas = cropper.getCroppedCanvas({
				width : 100,
				height : 100,
			});
			initialAvatarURL = avatar.src;
			avatar.src = canvas.toDataURL();
			var formData = new FormData();
			formData.append('file', dataURLtoFile(canvas.toDataURL(), "test.png"));
			var url = ctx.apiurl + 'file/fileUpload';
			var xhr = new XMLHttpRequest();
			xhr.open('POST', url, true);
			xhr.onload = function() {
				if (this.status == 200) {
					var fileName = this.getResponseHeader("Content-Disposition").split(";")[1].split("=")[1];
					$("#head").val(fileName);
				}
			};
			xhr.send(formData);
		}
	});
});

checkCellphone = function(input) {
	var cellphone = $(input).val();
	if (cellphone.trim() != "") {

		var data = "employee.cellphone=" + cellphone + "&employee.pk=" + ctx.employee().pk;
		$.ajax({
			type : "POST",
			url : ctx.apiurl + 'client/checkEmployeeCellphone',
			data : data
		}).success(function(str) {
			if (str == "success") {
				fail_msg("已存在相同电话号码！");
			}
		});

	}
}
checkWechat = function(input) {
	var wechat = $(input).val();
	if (wechat.trim() != "") {

		var data = "employee.wechat=" + wechat + "&employee.pk=" + ctx.employee().pk;
		$.ajax({
			type : "POST",
			url : ctx.apiurl + 'client/checkEmployeeWechat',
			data : data
		}).success(function(str) {
			if (str == "success") {
				fail_msg("已存在相同微信号！");
			}
		});

	}
}