var importBlackLayer;
var UsersContext = function() {
	var self = this;
	self.basePath = $("#hidden_apiurl").val();
	self.users = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function(){
		$.getJSON(self.basePath + 'user/search', {}, function(data) {
			self.users(data.users);
		});
	}


	self.checkIdPic = function(fileName) {
		importBlackLayer = $.layer({
			type : 1,
			title : [ '查看身份证图片', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#pic-check'
			},
			end : function() {
				console.log("Done");
			}
		});

		$("#img-pic").attr(
				"src",
				self.basePath + 'file/getFileStream?fileFileName=' + fileName
						+ "&fileType=USER_ID");
	}
	
	self.agreeUser = function(pk){
		layer.confirm('确定要同意用户吗？', {
			  btn: ['确认','取消'] //按钮
			}, function(){
				layer.msg('审批成功', {icon: 1});
			}, function(){
			  alert("取消")
			});
		self.refresh();
	}
	
	self.rejectUser = function(pk){
		self.refresh();
	}
	// 新标签页显示大图片
	$("#img-pic").on(
			'click',
			function() {
				window.open(self.basePath
						+ "templates/common/check-picture-big.jsp?src="
						+ encodeURIComponent($(this).attr("src")));
			});
};

var ctx = new UsersContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
