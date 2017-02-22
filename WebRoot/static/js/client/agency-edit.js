var AgencyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.agencyPk = $("#agency_key").val();
	self.agencyFiles = ko.observable({
		total : 0,
		items : []
	});
	self.agency = ko.observable({});
	self.genders = [ '男', '女' ];
	self.mainBus = [ '组团', '综合', '地接', '同业', '其他' ];
	self.agencyType = [ '总公司', '分公司', '营业部' ];

	self.provices = [ '北京市', '天津市', '上海市', '重庆市', '河北省', '山西省', '辽宁省', '吉林省', '黑龙江省', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '海南省', '四川省', '贵州省', '云南省', '陕西省', '甘肃省',
			'青海省', '台湾省', '内蒙古自治区', '广西壮族自治区', '西藏自治区', '宁夏回族自治区', '新疆维吾尔自治区', '香港特别行政区', '澳门特别行政区' ];
	self.cityMapping = {
		'北京市' : [ '北京市' ],
		'天津市' : [ '天津市' ],
		'上海市' : [ '上海市' ],
		'重庆市' : [ '重庆市' ],
		'河北省' : [ '石家庄市', '唐山市', '秦皇岛市', '邯郸市', '邢台市', '保定市', '张家口市', '承德市', '沧州市', '廊坊市', '衡水市' ],
		'山西省' : [ '太原市', '大同市', '阳泉市', '长治市', '晋城市', '朔州市', '忻州市', '吕梁市', '晋中市', '临汾市', '运城市' ],
		'内蒙古自治区' : [ '呼和浩特市', '包头市', '乌海市', '赤峰市', '呼伦贝尔市', '通辽市', '乌兰察布市', '鄂尔多斯市', '巴彦淖尔市' ],
		'辽宁省' : [ '沈阳市', '大连市', '鞍山市', '抚顺市', '本溪市', '丹东市', '锦州市', '营口市', '阜新市', '辽阳市', '盘锦市', '铁岭市', '朝阳市', '葫芦岛市' ],
		'吉林省' : [ '长春市', '吉林市', '四平市', '辽源市', '通化市', '白山市', '白城市', '松原市' ],
		'黑龙江省' : [ '哈尔滨市', '齐齐哈尔市', '牡丹江市', '佳木斯市', '大庆市', '伊春市', '鸡西市', '鹤岗市', '双鸭山市', '七台河市', '绥化市', '黑河市' ],
		'江苏省' : [ '南京市', '无锡市', '徐州市', '常州市', '苏州市', '南通市', '连云港市', '淮安市', '盐城市', '扬州市', '镇江市', '泰州市', '宿迁市' ],
		'浙江省' : [ '杭州市', '宁波市', '温州市', '绍兴市', '湖州市', '嘉兴市', '金华市', '衢州市', '台州市', '丽水市', '舟山市' ],
		'安徽省' : [ '合肥市', '芜湖市', '蚌埠市', '淮南市', '马鞍山市', '淮北市', '铜陵市', '安庆市', '黄山市', '阜阳市', '宿州市', '滁州市', '六安市', '宣城市', '池州市', '亳州市' ],
		'福建省' : [ '福州市', '莆田市', '泉州市', '厦门市', '漳州市', '龙岩市', '三明市', '南平市', '宁德市' ],
		'江西省' : [ '南昌市', '赣州市', '宜春市', '吉安市', '上饶市', '抚州市', '九江市', '景德镇市', '萍乡市', '新余市', '鹰潭市' ],
		'山东省' : [ '济南市', '青岛市', '淄博市', '枣庄市', '东营市', '烟台市', '潍坊市', '济宁市', '泰安市', '威海市', '日照市', '滨州市', '德州市', '聊城市', '临沂市', '菏泽市', '莱芜市' ],
		'河南省' : [ '郑州市', '开封市', '洛阳市', '平顶山市', '安阳市', '鹤壁市', '新乡市', '焦作市', '濮阳市', '许昌市', '漯河市', '三门峡市', '商丘市', '周口市', '驻马店市', '南阳市', '信阳市' ],
		'湖北省' : [ '武汉市', '黄石市', '十堰市', '荆州市', '宜昌市', '襄阳市', '鄂州市', '荆门市', '黄冈市', '孝感市', '咸宁市', '随州市' ],
		'湖南省' : [ '长沙市', '株洲市', '湘潭市', '衡阳市', '邵阳市', '岳阳市', '张家界市', '益阳市', '常德市', '娄底市', '郴州市', '永州市', '怀化市' ],
		'广东省' : [ '广州市', '深圳市', '珠海市', '汕头市', '佛山市', '韶关市', '湛江市', '肇庆市', '江门市', '茂名市', '惠州市', '梅州市', '汕尾市', '河源市', '阳江市', '清远市', '东莞市', '中山市', '潮州市', '揭阳市', '云浮市' ],
		'广西壮族自治区' : [ '南宁市', '柳州市', '桂林市', '梧州市', '北海市', '崇左市', '来宾市', '贺州市', '玉林市', '百色市', '河池市', '钦州市', '防城港市', '贵港市' ],
		'海南省' : [ '海口市', '三亚市', '儋州市', '三沙市' ],
		'四川省' : [ '成都市', '绵阳市', '自贡市', '攀枝花市', '泸州市', '德阳市', '广元市', '遂宁市', '内江市', '乐山市', '资阳市', '宜宾市', '南充市', '达州市', '雅安市', '广安市', '巴中市', '眉山市' ],
		'贵州省' : [ '贵阳市', '六盘水市', '遵义市', '铜仁市', '毕节市', '安顺市' ],
		'云南省' : [ '昆明市', '昭通市', '曲靖市', '玉溪市', '普洱市', '保山市', '丽江市', '临沧市' ],
		'西藏自治区' : [ '拉萨市', '昌都市', '山南市', '日喀则市', '林芝市' ],
		'陕西省' : [ '西安市', '铜川市', '宝鸡市', '咸阳市', '渭南市', '汉中市', '安康市', '商洛市', '延安市', '榆林市' ],
		'甘肃省' : [ '兰州市', '嘉峪关市', '金昌市', '白银市', '天水市', '酒泉市', '张掖市', '武威市', '定西市', '陇南市', '平凉市', '庆阳市' ],
		'青海省' : [ '西宁市', '海东市' ],
		'宁夏回族自治区' : [ '银川市', '石嘴山市', '吴忠市', '固原市', '中卫市' ],
		'新疆维吾尔自治区' : [ '乌鲁木齐市', '克拉玛依市', '吐鲁番市', '哈密市' ]
	};

	self.agency().agency_provice = ko.observable();
	self.agency().agency_city = ko.observable();

	self.ter = function() {
		$("#city").empty();
		$("#city").append("<option value>-- 市--</option>");
		for ( var i = 0; i < self.cityMapping[self.agency().agency_provice].length; i++) {
			var value = self.cityMapping[self.agency().agency_provice][i];
			$("#city").append("<option value='" + value + "'>" + value + "</option>");
		}
	};

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'client/searchOneAgency', {
		agency_pk : self.agencyPk
	}, function(data) {
		if (data.agency) {
			self.agency(data.agency);
			self.ter();
			$("#city").val(self.agency().agency_city);
		} else {
			fail_msg("旅行社不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});
	// 获取该供应商所有的文件信息
	$.getJSON(self.apiurl + 'client/searchAgencyFiles', {
		agency_pk : self.agencyPk
	}, function(data) {
		self.agencyFiles(data.agencyFiles);
		$(self.agencyFiles()).each(function(idx, file) {
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
		var inputFile = $(stFileName).prev().find("input");
		var inputName = inputFile.attr("name");
		var imgContainer = $(inputFile).parent().parent().parent().next();
		var fileNameInput = stFileName;

		var formData = new FormData();
		formData.append("fileFileName", fileName);
		formData.append("fileType", "AGENCY_FILE");
		formData.append("subFolder", self.agencyPk);

		var url = ctx.apiurl + 'file/getFileStream';
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.responseType = "blob";
		xhr.onload = function() {
			if (this.status == 200) {
				var blob = this.response;
				var deleteButton = $("<div class='delete'>删除</div>");

				var img = document.createElement("img");
				$("body").append(deleteButton);
				deleteButton.hide();
				deleteButton.click(function() {
					deleteImage(this, inputFile, img, fileNameInput, fileName);
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

				if (inputName != "file6") {
					imgContainer.html(img);
				} else {
					imgContainer.append(img);
				}
			}
		};
		xhr.send(formData);
	};

	self.saveAgency = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/updateAgency',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/client/agency.jsp";
			}
		});
	};
};

var ctx = new AgencyContext();

var initWidth = 600;
$(document).ready(function() {
	ko.applyBindings(ctx);

	$(':file').change(function() {
		changeFile(this);

	});
});

var beforeSendHandler = function(data, xhr) {
	console.log(xhr);
};
var completeHandler = function() {

};
var errorHandler = function() {

};
function changeFile(thisx) {
	var file = thisx.files[0];
	var inputName = thisx.name;
	name_check = file.name;
	size = file.size;
	type = file.type;
	if (type.indexOf("image") < 0) {
		fail_msg("请上传图片");
		return;
	}
	if (size > 4194304) {
		fail_msg("文件大于4MB");
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
				deleteImage(this, thisx, img, fileNameInput, fileName);
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
			if (inputName != "file6") {
				imgContainer.html(img);
				fileNameInput.val(fileName);
			} else {
				imgContainer.append(img);
				fileNameInput.val(fileName + ";;;" + fileNameInput.val());
			}

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
function deleteImage(deleteButton, inputFile, img, fileNameInput, fileName) {
	$(deleteButton).remove();
	$(img).remove();
	var inputName = inputFile.attr("name");
	var newInputFile = $("<input type='file' name='" + inputName + "'/>");
	newInputFile.change(function() {
		changeFile(this);
	});
	$(inputFile).parent().append(newInputFile);
	$(inputFile).remove();

	if (inputName != "file6") {
		$(fileNameInput).val($(fileNameInput).val().replace(fileName, ""));
	} else {
		$(fileNameInput).val(($(fileNameInput).val()).replace(fileName + ";;;", ""));
	}
	// 删除服务器数据
	$.ajax({
		type : "POST",
		url : ctx.apiurl + 'client/deleteAgencyFile',
		data : "file_name=" + fileName + "&agency_pk=" + ctx.agencyPk
	}).success(function(str) {
		if (str == "success") {
			// do nothing
		}
	});
}
var name_check = false;
var code_check = false;
function checkSame(txt, inputType) {
	var content = $(txt).val();
	if (content == ""){
		if (inputType == "NAME") {
			name_check = false;
		} else if (inputType == "CODE") {
			code_check = false;
		}
		if (name_check==false && code_check==false) {
			$("#btn_save").attr("disabled", false);
		}
		return;
	}
	var agency_pk = $("#agency_key").val();
	var title = "";
	if (inputType == "NAME") {
		title = "公司全称";
	} else if (inputType == "CODE") {
		title = "信用代码";
	}
	$.ajax({
		type : "POST",
		url : $("#hidden_apiurl").val() + 'client/checkAgencySame',
		data : "editType=edit&inputType=" + inputType + "&content=" + content + "&agency_pk=" + agency_pk
	}).success(function(str) {
		if (str == "EXIST") {
			fail_msg(title + "已经存在");
			$("#btn_save").attr("disabled", true);
			if (inputType == "NAME") {
				name_check = true;
			} else if (inputType == "CODE") {
				code_check = true;
			}
		} else {
			if (inputType == "NAME") {
				name_check = false;
			} else if (inputType == "CODE") {
				code_check = false;
			}
			console.log(name_check==false)
			if (name_check==false && code_check==false) {
				$("#btn_save").attr("disabled", false);
			}
		}
	});
}