var SupplierContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.supplierPk = $("#supplier_key").val();
	self.supplier = ko.observable({
		sales : []
	});
	self.supplierFiles = ko.observable({
		total : 0,
		items : []
	});
	self.isMapping = {
		'Y' : '合作',
		'N' : '终止合作',
	};

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'supplier/searchOneSupplier', {
		supplier_pk : self.supplierPk
	}, function(data) {
		if (data.supplier) {
			self.supplier(data.supplier);
		} else {
			fail_msg("公司不存在！");
		}

		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	// 获取该供应商所有的文件信息
	$.getJSON(self.apiurl + 'supplier/searchSupplierFiles', {
		supplier_pk : self.supplierPk
	}, function(data) {
		self.supplierFiles(data.supplierFiles);
		$(self.supplierFiles()).each(function(idx, file) {
			switch (file.file_type) {
			case "LICENCE":
				$("#txt-licence").val(file.file_name);
				break;
			case "PERMIT":
				$("#txt-permit").val(file.file_name);
				break;
			case "INSURANCE":
				$("#txt-insurance").val(file.file_name);
				break;
			case "CORPORATE":
				$("#txt-corporate").val(file.file_name);
				break;
			case "CHIEF":
				$("#txt-chief").val(file.file_name);
				break;
			case "OTHER":
				$("#txt-other").val(file.file_name + ";;;" + $("#txt-other").val());
				break;
			default:
				// do nothing
			}
		});
		self.loadFiles();
	});
	var initWidth = 600;
	self.loadFiles = function() {
		$("[st='st-file-name']").each(function(idx, stFileName) {
			var fileName = $(stFileName).val();
			if (!fileName.isEmpty()) {
				if (fileName.indexOf(";;;") > -1) {
					var fileNames = fileName.split(";;;");
					$(fileNames).each(function(idx, name) {
						if (!name.isEmpty())
							self.downFile(stFileName, name);
					});
				} else {
					self.downFile(stFileName, fileName);
				}
			}
		});
	};

	self.downFile = function(stFileName, fileName) {
		var inputName = stFileName.name;
		var imgContainer = $(stFileName).parent();

		var formData = new FormData();
		formData.append("fileFileName", fileName);
		formData.append("fileType", "SUPPLIER_FILE");
		formData.append("subFolder", self.supplierPk);

		var url = ctx.apiurl + 'file/getFileStream';
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.responseType = "blob";
		xhr.onload = function() {
			if (this.status == 200) {
				var blob = this.response;
				var img = document.createElement("img");
				img.onload = function(e) {
					window.URL.revokeObjectURL(img.src);
					if (img.width > initWidth) {
						img.height = initWidth * (img.height / img.width);
						img.width = initWidth;
					}
				};
				img.src = window.URL.createObjectURL(blob);

				if (inputName != "file6") {
					imgContainer.html(img);
				} else {
					imgContainer.append(img);
				}
			}
		};
		xhr.send(formData);
	};
};

var ctx = new SupplierContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});
