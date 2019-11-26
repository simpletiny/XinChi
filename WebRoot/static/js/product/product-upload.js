function changeFile(thisx) {
	var file = thisx.files[0];
	var inputName = thisx.name;
	name_check = file.name;
	size = file.size;
	type = file.type;

	if (type.indexOf("msword") < 0) {
		fail_msg("请上传word文档");
		return;
	}
	if (size > 1048576) {
		fail_msg("文件大于1MB");
		return;
	}

	var fileNameInput = $(thisx).parent().next();
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/fileUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.responseType = "blob";

	xhr.onload = function() {
		if (this.status == 200) {
			var fileName = this.getResponseHeader("Content-Disposition").split(
					";")[1].split("=")[1];
			var blob = this.response;
			fileNameInput.val(fileName);
			var span = $(thisx).parent().parent().find("span").css({
				"color" : "green"
			});
			var html = '已上传&nbsp;&nbsp;<a href="javascript:void(0)" onclick="viewTemplet(\'temp\',\''
					+ fileName + '\')">预览</a>';
			$(span).html(html)
			success_msg("上传成功！请点击保存按钮以保存。");
		} else {
			fail_msg("上传失败，请重试或联系管理员！");
		}
	};
	xhr.send(formData);
}
var viewTemplet = function(type, file) {
	var data = "viewType=" + type + "&fileName=" + file;
	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'file/viewWord',
		data : data
	}).success(function(str) {
		window.open(ctx.apiurl + "templates/temp/viewword/" + str)
	});
}