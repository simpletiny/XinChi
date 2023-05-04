var ViewContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.view = ko.observable({});

	self.publish = function() {
		editor.sync();
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'system/createSystemGuide',
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
				window.editor = K.create('#content', {
					uploadJson : ctx.apiurl + 'kindeditor/keFileUpload',
					deleteJson : ctx.apiurl + 'kindeditor/keFileDelete',
					items : ['fullscreen', 'undo', 'redo', 'print', 'cut', 'copy', 'paste', 'plainpaste', 'wordpaste',
							'|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist',
							'insertunorderedlist', 'indent', 'outdent', 'subscript', 'superscript', '|', 'selectall',
							'-', 'title', 'fontname', 'fontsize', '|', 'textcolor', 'bgcolor', 'bold', 'italic',
							'underline', 'strikethrough', 'removeformat', '|', 'image', 'advtable', 'hr', 'emoticons',
							'link', 'unlink', '|', 'about']
				});
			});
		});
