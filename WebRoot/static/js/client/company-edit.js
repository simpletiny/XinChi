var agencyLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.companyPk = $("#client_key").val();
	self.client = ko.observable({});
	self.genders = ['男', '女'];

	self.clientType = ['总公司', '分公司', '营业部', '包桌', '经纪人', '其他'];
	self.storeTypes = ['未知', '门店', '写字间', '其它 '];
	self.mainBusinesses = ['组团', '户外', '线上', '综合', '地接', '同业', '其它'];
	self.backLevels = ['未知', '立即', '及时', '拖拉', '费劲', '定期', '垃圾', '布莱'];
	self.marketLevels = ['未知', '主导级', '引领级', '普通级', '跟随级', '玩闹级'];
	self.talkLevels = ['核心', '主力', '市场', '排斥'];

	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'client/searchOneCompany', {
		client_pk : self.companyPk
	}, function(data) {
		if (data.client) {
			self.client(data.client);
			downloadImg(data.client);
		} else {
			fail_msg("公司不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		console.log(reason.responseText);
	});

	self.saveCompany = function() {
		if (!$("form").valid()) {
			return;
		}
		startLoadingIndicator("保存中");
		let data = $("form").serialize();

		let imgs = {};
		let outImgs = new Array();
		let inImgs = new Array();

		let out_img_files = $("#out-container").find("input");
		console.log(out_img_files);

		for (let i = 0; i < out_img_files.length; i++) {
			let img_file_name = $(out_img_files.get(i)).val();
			outImgs.push(img_file_name);
		}
		let in_img_files = $("#in-container").find("input");

		for (let i = 0; i < in_img_files.length; i++) {
			let img_file_name = $(in_img_files.get(i)).val();
			inImgs.push(img_file_name);
		}

		imgs.outImgs = outImgs;
		imgs.inImgs = inImgs;

		let json = JSON.stringify(imgs);
		data += "&json=" + json;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/updateCompany',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				window.location.href = self.apiurl + "templates/client/company.jsp";
			} else if (str == "exist") {
				fail_msg("存在同名财务主体！");
			}
		});
	};

	// 关联旅游公司相关
	self.agencies = ko.observable({
		total : 0,
		items : []
	});

	self.chooseAgency = function() {
		agencyLayer = $.layer({
			type : 1,
			title : ['选择旅游公司', ''],
			maxmin : false,
			closeBtn : [1, true],
			shadeClose : false,
			area : ['600px', '650px'],
			offset : ['50px', ''],
			scrollbar : true,
			page : {
				dom : '#agency_pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.refresh = function() {
		var param = "agency.agency_name=" + $("#agency_full_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'client/searchAgencyByPage', param, function(data) {
			self.agencies(data.agencys);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.searchAgency = function() {
		self.refresh();

	};
	self.pickAgency = function(name, pk) {
		$("#agency_name").val(name);
		$("#agency_pk").val(pk);
		layer.close(agencyLayer);
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
		self.searchAgency();

	};
	// end pagination
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	Dropzone.autoDiscover = false;
	let dropZoneOptions = {
		paramName : 'file',
		url : ctx.apiurl + "file/fileUpload",
		maxFilesize : 1, // MB
		maxFiles : 4,
		dictDefaultMessage : '',
		acceptedFiles : "image/*",
		previewTemplate : document.querySelector('#tpl').innerHTML,
		success : function(file, response) {
			var fileName = file.xhr.getResponseHeader("Content-Disposition").split(";")[1].split("=")[1];
			let img_container = $(this.element).prev();
			let img_group = $("<div></div>")
			let img = document.createElement("img");
			img.src = file.dataURL;
			img_group.append(img);

			let deleteButton = $("<div class='delete'>删除</div>");
			deleteButton.hide();
			deleteButton.click(function() {
				img_group.remove();
				$(this).remove();
			});
			deleteButton.mouseenter(function() {
				$(this).show();
			});

			img_group.append(deleteButton);
			img.onload = function(e) {
				$(img).mouseenter(function() {
					deleteButton.css("top", $(img).offset().top + img.height / 2 - 25);
					deleteButton.css("left", $(img).offset().left + img.width / 2 - 50);
					deleteButton.show();
				});
				$(img).mouseout(function() {
					deleteButton.hide();
				});
			};

			let txtFileName = $("<input type='hidden' />");
			txtFileName.val(fileName);
			img_group.append(txtFileName);
			img_container.append(img_group)
		},
		error : function(file, errorMessage, xhr) {
			if (xhr && xhr.status !== 200) {
				console.log("HTTP Status:", xhr.status);
				console.log("Response from server:", xhr.responseText);
			}
			// 也可以根据 errorMessage 或 xhr 的内容来判断错误类型
			if (typeof errorMessage === 'string' && errorMessage.includes("File is too big")) {
				fail_msg("文件不能大于1MB！");
			} else if (xhr && xhr.status === 404) {
				fail_msg("服务端接口未找到！");
			} else if (xhr && xhr.status === 500) {
				fail_msg("服务器内部错误！");
			}
		}
	};

	var myDropzone1 = new Dropzone("#dropzoneout", dropZoneOptions);
	var myDropzone1 = new Dropzone("#dropzonein", dropZoneOptions);

});

function downloadImg(client) {
	let outs = new Array();
	let ins = new Array();
	for(const e of client.client_inouts){
		if(e.img_type==="O"){
			outs.push(e);
		}else if(e.img_type=="I"){
			ins.push(e);
		}
	}
	
	for(const e of outs){
		let data = {
			fileFileName :e.img_name,
			fileType :"CLIENT_INOUT_IMG",
			subFolder:e.client_pk
		};
		const img_container_id = "out-container";
		showImg(data,img_container_id);
	}
	for(const e of ins){
		let data = {
			fileFileName :e.img_name,
			fileType :"CLIENT_INOUT_IMG",
			subFolder:e.client_pk
		};
		const img_container_id = "in-container";
		showImg(data,img_container_id);
	}
}

function showImg(data,img_container_id){
	let params = new URLSearchParams(data).toString();
	fetch(`${ctx.apiurl}file/getFileStream?${params}`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        let fileName = response.headers.get("Content-Disposition").split(";")[1].split("=")[1];
        return response.blob().then(blob => ({ blob, fileName }));
    })
    .then(({ blob, fileName }) => {
        // 创建一个 URL 对象
        var url = URL.createObjectURL(blob);
        
        let img_container = $("#"+img_container_id+" .preview-container");
		let img_group = $("<div></div>")
		let img = document.createElement("img");
		img.src = url;
		img_group.append(img);

		let deleteButton = $("<div class='delete'>删除</div>");
		deleteButton.hide();
		deleteButton.click(function() {
			img_group.remove();
			$(this).remove();
		});
		deleteButton.mouseenter(function() {
			$(this).show();
		});

		img_group.append(deleteButton);
		img.onload = function(e) {
			$(img).mouseenter(function() {
				deleteButton.css("top", $(img).offset().top + img.height / 2 - 25);
				deleteButton.css("left", $(img).offset().left + img.width / 2 - 50);
				deleteButton.show();
			});
			$(img).mouseout(function() {
				deleteButton.hide();
			});
		};

		let txtFileName = $("<input type='hidden' />");
		txtFileName.val(fileName);
		img_group.append(txtFileName);
		img_container.append(img_group)
    })
    .catch(error => console.error('Error fetching image:', error));

}

