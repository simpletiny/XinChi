var nanobar;
var changeValueByPath = function(obj, path, value) {
	var ps = path.split('.'), co = obj;
	for ( var i = 0; i < ps.length - 1; i++) {
		co = (co[ps[i]]) ? co[ps[i]] : co[ps[i]] = {};
	}
	co[ps[ps.length - 1]] = value;
};

// to use this function pls impl changeRichText function.
ko.bindingHandlers.richText = {
	init : function(element, valueAccessor) {

		var modelValue = valueAccessor();
		var value = ko.unwrap(valueAccessor());
		var element$ = $(element);

		// Set initial value and create the CKEditor
		element$.html(value);
		var editor = element$.ckeditor().editor;

		// bind to change events and link it to the observable
		editor.on('change', function(e) {
			var self = this;
			if (ko.isWriteableObservable(self)) {
				self($(e.listenerData).val());
			} else {
				changeRichText(e.listenerData.name, $(e.listenerData).val());
			}
		}, modelValue, element);

		/*
		 * Handle disposal if KO removes an editor through template binding
		 */
		ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
			editor.updateElement();
			editor.destroy();
		});
	},

	/*
	 * Hook and handle the binding updating so we write back to the observable
	 */
	update : function(element, valueAccessor) {
		var element$ = $(element);
		var newValue = ko.unwrap(valueAccessor());
		if (element$.ckeditorGet().getData() != newValue) {
			element$.ckeditorGet().setData(newValue);
		}
	}
};

// change based on: https://gist.github.com/tyrsius/4157832
ko.bindingHandlers.inlineSelect = {
	init : function(element, valueAccessor, allBindingsAccessor, data) {
		var span = $(element);
		var select = $('<select></select>', {
			'style' : 'display: none',
			'class' : 'form-control'
		});
		var changeFunc = allBindingsAccessor().changeFunc;
		span.after(select);

		ko.applyBindingsToNode(select.get(0), {
			value : valueAccessor(),
			options : allBindingsAccessor().inlineOptions
		});
		ko.applyBindingsToNode(span.get(0), {
			text : valueAccessor()
		});

		span.click(function() {
			span.hide();
			select.show();
			select.focus();
		});

		select.blur(function() {
			span.show();
			select.hide();
			changeFunc(data);
		});
	}
};

ko.bindingHandlers.inlineDate = {
	init : function(element, valueAccessor, allBindingsAccessor, data) {
		inlineTextFunc(element, valueAccessor, allBindingsAccessor, data, 'ip- date-picker');
	}
};

ko.bindingHandlers.inline = {
	init : function(element, valueAccessor, allBindingsAccessor, data) {
		inlineTextFunc(element, valueAccessor, allBindingsAccessor, data, 'ip-');
	}
};

var inlineTextFunc = function(element, valueAccessor, allBindingsAccessor, data, clazz) {
	var span = $(element);
	var input = $('<input />', {
		'type' : 'text',
		'style' : 'display:none',
		'class' : clazz
	});
	var changeFunc = allBindingsAccessor().changeFunc;
	span.after(input);

	ko.applyBindingsToNode(input.get(0), {
		value : valueAccessor()
	});
	ko.applyBindingsToNode(span.get(0), {
		text : valueAccessor()
	});

	span.click(function() {
		input.width(span.width());
		span.hide();
		input.show();
		input.focus();
	});

	input.blur(function() {
		span.show();
		input.hide();
		changeFunc(data);
	});

	input.keypress(function(e) {
		if (e.keyCode == 13) {
			span.show();
			input.hide();
		}
	});
};
var msg= function(txt){
	$.layer({
		type : 0,
		closeBtn : [ 1, true ],
		area : [ '300px', 'auto' ],
		dialog : {
			type : -1,
			msg : txt
		}
	});
};
var success_msg = function(msg) {
	$.layer({
		type : 0,
		closeBtn : [ 1, true ],
		area : [ '300px', 'auto' ],
		dialog : {
			type : 9,
			msg : msg
		}
	});
};

var fail_msg = function(msg) {
	$.layer({
		type : 0,
		closeBtn : [ 1, true ],
		area : [ '300px', 'auto' ],
		dialog : {
			type : 8,
			msg : msg
		}
	});
};

var date2iso = function(date) {
	if (date == null || date.trim() == "") {
		return null;
	} else {
		return new Date(date.replace(/-/g, "/")).toISOString();
	}
};

function startLoadingIndicator(msg) {
	var apiurl = $("#hidden_apiurl").val();
	if (apiurl == null) {
		apiurl = "../../";
	}
	if ($('.large-format-loader').length == 0) {
		var uiCode = '<div class="large-format-loader is-not-loading">\
                <div id="loader-inside-infi" class="loader-inside-format">\
                    <p style="padding-top: 30px"><img height="50" width="50" src="'
				+ apiurl
				+ 'static/img/loading.gif"/></p>\
                    <p id="loading-message" style="padding-top: 10px;"></p>\
                </div>\
                <div id="loader-inside-process" class="loader-inside-format" hidden>\
                    <p id="loading-message" style="padding-top: 30px;"></p>\
                    <div id="loader-inside-bar-container" style="width: 75%;height: 40px;margin:0 auto"></div>\
                    <p id="loader-inside-value"></p>\
                </div>\
            </div>';

		$('body').prepend(uiCode);
		var options = {
			bg : '#ff5722',
			target : document.getElementById('loader-inside-bar-container'),
			id : 'loader-inside-bar'
		};
		nanobar = new Nanobar(options);
	}
	$('#loader-inside-infi').show();
	$('#loader-inside-process').hide();
	$('.large-format-loader #loading-message').html(msg + '...');
	$('.large-format-loader').removeClass('is-not-loading');
}

function startLoadingSimpleIndicator(msg) {
	var apiurl = $("#hidden_apiurl").val();
	if (apiurl == null) {
		apiurl = "../../";
	}
	if ($('.large-format-loader').length == 0) {
		var uiCode = '<div class="large-format-loader-simple is-not-loading">\
                <div id="loader-inside-infi" class="loader-inside-format">\
                    <p style="padding-top: 30px"><img height="50" width="50" src="'
				+ apiurl
				+ 'static/img/loading.gif"/></p>\
                    <p id="loading-message" style="padding-top: 10px;"></p>\
                </div>\
                <div id="loader-inside-process" class="loader-inside-format" hidden>\
                    <p id="loading-message" style="padding-top: 30px;"></p>\
                    <div id="loader-inside-bar-container" style="width: 75%;height: 40px;margin:0 auto"></div>\
                    <p id="loader-inside-value"></p>\
                </div>\
            </div>';

		$('body').prepend(uiCode);
		var options = {
			bg : '#ff5722',
			target : document.getElementById('loader-inside-bar-container'),
			id : 'loader-inside-bar'
		};
		nanobar = new Nanobar(options);
	}
	$('#loader-inside-infi').show();
	$('#loader-inside-process').hide();
	$('.large-format-loader-simple #loading-message').html(msg + '...');
	$('.large-format-loader-simple').removeClass('is-not-loading');
}

function endLoadingIndicator() {
	$('.large-format-loader').addClass('is-not-loading');
	$('.large-format-loader-simple').addClass('is-not-loading');
}

var addTotalCountLabel = function(idLabel, total) {
	$("#" + idLabel).html('<label style="background-color: #6495ED; font-size: smaller; padding-right: 10px;padding-left: 10px;" class="label">共&nbsp;' + total + '&nbsp;条结果</label>');
};

Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};
String.prototype.isEmpty = function() {
	return null == this || this.trim() == "";
};
