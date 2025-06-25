var PreciseEmployeeContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.client = ko.observable({});
	self.genders = [{ key: "M", value: "男" }, { key: "F", value: "女" }];
	self.employeeType = ['未知', '员工', '老板', '包桌'];
	self.employee = ko.observable({});

	self.createEmployee = function() {
		if (!$("form").valid()) {
			return;
		}
		if ($("#financial_body_pk").val() === "") {
			fail_msg("请选择财务主体！");
			return;
		}
		startLoadingIndicator("保存中");
		$.ajax({
			type: "POST",
			url: self.apiurl + 'client/createPreciseEmployee',
			data: $("form").serialize()
		}).success(function(str) {
			endLoadingIndicator();
			switch (str) {
				case "success":
					window.history.go(-1);
					break;
				case "existcellphone":
					fail_msg("存在相同的手机号！");
					break;
				case "existwechat":
					fail_msg("存在相同的微信号！");
					break;
				case "existsamenameid":
					fail_msg("存在相同姓名身份证号的客户！");
					break;
				case "illegal":
					fail_msg("身份证号不合法！");
					break;
				case "mismatch":
					fail_msg("姓名和身份证号不匹配！");
					break;
				case "no_record":
					fail_msg("实名认证无记录，请联系管理员！");
					break;
				case "fail":
					fail_msg("实名认证失败！");
					break;
				default:
			}
		});
	}
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
			area: ['600px', '750px'],
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

		var param = "client.client_short_name=" + $("#client_name").val() + "&client.public_flgs=N&client.statuses=N";
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

var ctx = new PreciseEmployeeContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	cropHeadPhoto();
	$('.file-img').change(function() {
		changeFile({
			input: this,
			size: 400,
			width: 400,
			required: "yes"
		});
	});
});
let cropHeadPhoto = function() {
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

	document.getElementById('crop').addEventListener('click', function() {
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
}
let genGender = function(txt) {
	const id = $(txt).val();
	if (id.length < 18) {
		$('select[name="employee.gender"]').val("");
		return;
	}
	const gender_num = determineGender(id);
	if (gender_num != -1) {
		$('select[name="employee.gender"]').val(gender_num === 0 ? "F" : "M");
	} else {
		$('select[name="employee.gender"]').val("");
	}
}
