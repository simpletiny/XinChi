var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.client = ko.observable({});
	self.genders = ['男', '女'];
	self.employeeType = ['未知', '员工', '老板', '包桌'];
	self.employee = ko.observable({});

	self.choosenSales = ko.observableArray([]);
	self.publicFlg = ko.observable("N");

	self.createEmployee = function() {
		if (!$("form").valid()) {
			return;
		}

		$.ajax({
			type: "POST",
			url: self.apiurl + 'client/createEmployee',
			data: $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.history.go(-1);
			} else if (str == "existcellphone") {
				fail_msg("存在相同的手机号！");
			} else if (str == "existwechat") {
				fail_msg("存在相同的微信号！");
			}
		});
	};
	self.clients = ko.observable({
		total: 0,
		items: []
	});
	self.choseFinancial = function() {
		financialLayer = $.layer({
			type: 1,
			title: ['选择财务主体', ''],
			maxmin: false,
			closeBtn: [1, true],
			shadeClose: false,
			area: ['600px', '650px'],
			offset: ['50px', ''],
			scrollbar: true,
			page: {
				dom: '#financial_pick'
			},
			end: function() {
				console.log("Done");
			}
		});
	};

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");
		
		var param = "client.client_short_name=" + $("#client_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'client/searchCompanyByPage', param, function(data) {
			endLoadingIndicator();
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

var ctx = new CompanyContext();

$(document)
	.ready(
		function() {
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
					aspectRatio: 1,
					viewMode: 3,
				});
			}).on('hidden.bs.modal', function() {
				cropper.destroy();
				cropper = null;
			});

			document
				.getElementById('crop')
				.addEventListener(
					'click',
					function() {
						var initialAvatarURL;
						var canvas;

						$modal.modal('hide');

						if (cropper) {
							canvas = cropper.getCroppedCanvas({
								width: 100,
								height: 100,
							});
							initialAvatarURL = avatar.src;
							avatar.src = canvas.toDataURL();
							var formData = new FormData();
							formData.append('file',
								dataURLtoFile(canvas
									.toDataURL(),
									"test.png"));
							var url = ctx.apiurl
								+ 'file/fileUpload';
							var xhr = new XMLHttpRequest();
							xhr.open('POST', url, true);
							xhr.onload = function() {
								if (this.status == 200) {
									var fileName = this
										.getResponseHeader(
											"Content-Disposition")
										.split(";")[1]
										.split("=")[1];
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

		var data = "employee.cellphone=" + cellphone;
		$.ajax({
			type: "POST",
			url: ctx.apiurl + 'client/checkEmployeeCellphone',
			data: data
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

		var data = "employee.wechat=" + wechat;
		$.ajax({
			type: "POST",
			url: ctx.apiurl + 'client/checkEmployeeWechat',
			data: data
		}).success(function(str) {
			if (str == "success") {
				fail_msg("已存在相同微信号！");
			}
		});

	}
}
window.addEventListener('DOMContentLoaded', function() {

});
