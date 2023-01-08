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
				K.each({
					'plug-align' : {
						name : '对齐方式',
						method : {
							'justifyleft' : '左对齐',
							'justifycenter' : '居中对齐',
							'justifyright' : '右对齐'
						}
					},
					'plug-order' : {
						name : '编号',
						method : {
							'insertorderedlist' : '数字编号',
							'insertunorderedlist' : '项目编号'
						}
					},
					'plug-indent' : {
						name : '缩进',
						method : {
							'indent' : '向右缩进',
							'outdent' : '向左缩进'
						}
					}
				}, function(pluginName, pluginData) {
					var lang = {};
					lang[pluginName] = pluginData.name;
					KindEditor.lang(lang);
					KindEditor.plugin(pluginName, function(K) {
						var self = this;
						self.clickToolbar(pluginName, function() {
							var menu = self.createMenu({
								name : pluginName,
								width : pluginData.width || 100
							});
							K.each(pluginData.method, function(i, v) {
								menu.addItem({
									title : v,
									checked : false,
									iconClass : pluginName + '-' + i,
									click : function() {
										self.exec(i).hideMenu();
									}
								});
							});
						});
					});
				});
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
