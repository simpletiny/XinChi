var editor;
var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.viewPk = $("#view_key").val();
	self.view = ko.observable({
		sales : []
	});

	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");
		$.getJSON(self.apiurl + 'system/searchOneSystemGuide', {
			view_pk : self.viewPk
		}, function(data) {
			if (data.view) {
				self.view(data.view);
				editor.html(self.view().content);
			} else {
				fail_msg("文章不存在！");
			}

			endLoadingIndicator();
		}).fail(function(reason) {
			fail_msg(reason.responseText);
		});
	}

	self.publish = function() {
		editor.sync();
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'system/updateSystemGuide',
			data : $("form").serialize()
		}).success(function(str) {
			if (str == "OK") {
				window.location.href = self.apiurl + "templates/system/system-guide.jsp";
			}
		});
	};
};
var ctx = new ViewContext();
$(document).ready(
		function() {
			ko.applyBindings(ctx);
			KindEditor.ready(function(K) {
				editor = K.create('#content', {
					uploadJson : ctx.apiurl + 'kindeditor/keFileUpload',
					deleteJson : ctx.apiurl + 'kindeditor/keFileDelete',
					items : ['source', '|', 'fullscreen', 'undo', 'redo', 'print', 'cut', 'copy', 'paste',
							'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
							'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent',
							'subscript', 'superscript', '|', 'selectall', '-', 'title', 'fontname', 'fontsize', '|',
							'textcolor', 'bgcolor', 'bold', 'italic', 'underline', 'strikethrough', 'removeformat',
							'|', 'image', 'advtable', 'hr', 'emoticons', 'link', 'unlink', '|', 'about']
				});
				ctx.refresh();
			});
		});
