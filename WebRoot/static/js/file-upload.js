function changeFile(param) {
	var K = {
		size : 1024,
		width : 600,
		type : "image",
		required : "no",
		accept:"image/jpeg,image/png"
	};

	var thisx = param.input;

	var maxSize = param.size == null ? K.size : param.size;
	var initWidth = param.width == null ? K.width : param.width;
	var type = param.type == null ? K.type : param.type;
	
	if(type=="image"){ 
		param.accept = K.accept;
	}
	var typeMapping = {
		"image" : "图片"
	};
	var file = thisx.files[0];
	var	name_check = file.name;
	var file_size = file.size;
	var fil_type = file.type;
	if (fil_type.indexOf(type) < 0) {
		fail_msg("请上传" + typeMapping[type] + "!");
		return;
	}
	if (Math.round(file_size / 1024 * 100) / 100 > maxSize) {
		fail_msg("文件不能大于" + maxSize + "KB!");
		return;
	}
	var imgContainer = $(thisx).parent().parent().parent().next();
	var fileNameInput = $(thisx).parent().next();
	var progress = $("<progress value='0'> </progress>");
	$(thisx).parent().parent().next().append(progress);
	var formData = new FormData();
	formData.append("file", file);

	var url = ctx.apiurl + 'file/fileUpload';
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, true);
	xhr.responseType = "blob";
	xhr.onprogress = function() {
		updateProgress(event, progress);
	};
	xhr.upload.onprogress = function() {
		updateProgress(event, progress);
	};
	xhr.onload = function() {
		if (this.status == 200) {
			var fileName = this.getResponseHeader("Content-Disposition").split(";")[1].split("=")[1];
			var blob = this.response;
			var deleteButton = $("<div class='delete'>删除</div>");

			var img = document.createElement("img");
			$("body").append(deleteButton);
			deleteButton.hide();
			deleteButton.click(function() {
				deleteImage(this, thisx, img, fileNameInput, fileName, param);
			});
			deleteButton.mouseenter(function() {
				$(this).show();
			});
			img.onload = function(e) {
				window.URL.revokeObjectURL(img.src);
				if (img.width > initWidth) {
					img.height = initWidth * (img.height / img.width);
					img.width = initWidth;
				}

				$(img).mouseenter(function() {
					deleteButton.css("top", $(img).offset().top + img.height / 2 - 25);
					deleteButton.css("left", $(img).offset().left + img.width / 2 - 50);
					deleteButton.show();
				});
				$(img).mouseout(function() {
					deleteButton.hide();
				});
			};
			img.src = window.URL.createObjectURL(blob);
			imgContainer.html(img);
			fileNameInput.val(fileName);

			progress.delay(2000).fadeIn(function() {
				progress.remove();
			});
		}
	};
	xhr.send(formData);
}
function updateProgress(e, progress) {
	if (e.lengthComputable) {
		$(progress).attr({
			value : e.loaded,
			max : e.total
		});
	}
}
function deleteImage(deleteButton, inputFile, img, fileNameInput, fileName, param) {
	$(deleteButton).remove();
	$(img).remove();
	var inputName = inputFile.name;
	var required = param.required == "yes" ? "required='required'" : "";
	var accept = param.accept;
	var newInputFile = $("<input type='file' " + required + " name='" + inputName + "' accept='"+accept+"'/>");
	newInputFile.change(function() {
		changeFile({
			input : this,
			size : param.size,
			width : param.width,
			type : param.type,
			required : param.required
		});
	});
	$(inputFile).parent().append(newInputFile);
	$(inputFile).remove();

	$(fileNameInput).val($(fileNameInput).val().replace(fileName, ""));
}
function remove(div) {
	$(div).parent().remove();
}