/**
 * 上传office文件通用js
 */
function uploadOffice(param) {
	var K = {
		// 1MB
		size : 102400,
		type : "csv",
		required : "no",
		file_path : "file-path"
	};

	var thisx = param.input;

	var maxSize = param.size == null ? K.size : param.size;

	var type = param.type == null ? K.type : param.type;

	var typeMapping = {
		"csv" : "csv文件",
		"txt" : "txt文件"
	};

	var file = thisx.files[0];
	var file_name = file.name;
	var file_size = file.size;
	var file_type = file_name.split(".")[1];

	if (file_type.indexOf(type) < 0) {
		fail_msg("请上传" + typeMapping[type] + "!");
		return;
	}
	if (file_size > maxSize) {
		fail_msg("文件不能大于100KB!");
		return;
	}

	var fileNameInput = $(thisx).parent().next();
	var txtFilePath = param.file_path == null ? $("." + K.file_path) : $("#" + param.file_path);
	$(txtFilePath).val(file_name);
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/fileUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.responseType = "blob";

	xhr.onload = function() {
		if (this.status == 200) {
			var fileName = this.getResponseHeader("Content-Disposition").split(";")[1].split("=")[1];
			var blob = this.response;
			fileNameInput.val(fileName);
		} else {
			fail_msg("上传失败，请重试或联系管理员！");
		}
	};
	xhr.send(formData);
}