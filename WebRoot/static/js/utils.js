var nanobar;
var CHARACTER_ARRAY = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
var changeValueByPath = function(obj, path, value) {
	var ps = path.split('.'), co = obj;
	for ( var i = 0; i < ps.length - 1; i++) {
		co = (co[ps[i]]) ? co[ps[i]] : co[ps[i]] = {};
	}
	co[ps[ps.length - 1]] = value;
};

// $(document).ready(function() {
// if($("input[type='number']")!=null){
// $("input[type='number']").mousewheel(function() {
// return false;
// });
// }
// });

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
var msg = function(txt) {
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
	if ($('.large-format-loader-simple').length == 0) {
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

Date.prototype.before = function(date, format){
	  if(!format) 
		  return date.getTime() > this.getTime()
	  let d1 = new Date(this.Format(format)),
	      d2 = new Date(date.Format(format));
	  return d2.getTime() > d1.getTime()
};

Date.prototype.after = function(date, format){
	   return date.before(this, format)
	};

Date.prototype.equal = function(date, format){
	  if(!format)
		  return date.getTime() === this.getTime()

	  let d1 = new Date(this.Format(format)),
	      d2 = new Date(date.Format(format));
	  return d2.getTime() === d1.getTime()
	};
/**
 * 字符串占位符替换{0}{1}{2}
 */
String.prototype.format = function(){
	if(arguments.length==0)
		return this;
	for(var s=this,i=0;i<arguments.length;i++){
		s=s.replace(new RegExp("\\{"+i+"\\}","g"),arguments[i]);
	}
	return s;
}
Date.prototype.addDate = function(days) {
	var x = this.valueOf();
	x = x + days * 24 * 60 * 60 * 1000;
	x = new Date(x);
	return x;
};

Date.prototype.addMonth = function(months) {
	var current = this.getMonth();
	var x = new Date(this);
	x.setMonth(current+months);
	return x;
};
var dateDiff = function(date1,date2){
	var date3 =Math.abs(date2.getTime()-date1.getTime());
	var days=Math.floor(date3/(24*3600*1000));
	
	var leave1=date3%(24*3600*1000);   
	var hours=Math.floor(leave1/(3600*1000));

	var leave2=leave1%(3600*1000);        
	var minutes=Math.floor(leave2/(60*1000));

	var leave3=leave2%(60*1000); 
	var seconds=Math.round(leave3/1000);
	
	return (days==0?"":days+"天")+(hours==0?"":hours+"小时")+(minutes==0?"":minutes+"分钟")+(seconds==0?"":seconds+"秒");
};
String.prototype.isEmpty = function() {
	return null == this || this.trim() == "";
  };

String.prototype.LTrim = function(str) {
	var pattern = new RegExp("(^\s*)", 'g');
	if (arguments.length != 0) {
		pattern = new RegExp("(^" + str + "*)", 'g');
	}
	return this.replace(pattern, "");
};

String.prototype.RTrim = function(str) {
	var pattern = new RegExp("(\s*$)", 'g');
	if (arguments.length != 0) {
		pattern = new RegExp("(" + str + "*$)", 'g');
	}
	return this.replace(pattern, "");
};

Array.prototype.isRepeat = function() {
	var hash = {};
	for ( var i in this) {
		if (hash[this[i]]) {
			return true;
		}
		hash[this[i]] = true;
	}
	return false;
};
Array.prototype.contains = function(obj) {
	for ( var i = 0; i < this.length; i++) {
		if (this[i] == obj) {
			return true;
		}
	}
	return false;
};
function getServerTime() {
	 return fetch($("#hidden_apiurl").val() + 'simpletiny/currentDate') 
	    .then(response => {
	      if (!response.ok) {
	        throw new Error(`获取服务器时间失败，状态码：${response.status}`);
	      }
	      return response.json();
	    })
	    .then(data => {
	    	console.log(data);
	      return new Date(data.current_date);
	    })
	    .catch(error => {
	      fail_msg(error);
	      return new Date();
	    });
	 
	}

var isChild = function(birthday){
	birthday = birthday.replace(/\-/gm,"");
	var birthYear = birthday.substring(0,4)-0;
	var birthMonth = birthday.substring(4,6)-0;
	var birthDate = birthday.substring(6,9)-0;
	var year12Date = new Date();
	year12Date.setYear(birthYear+12);
	year12Date.setMonth(birthMonth-1);
	year12Date.setDate(birthDate);
	var year12 = year12Date.Format("yyyyMMdd")-0;
	var x = new Date();
	var now = x.Format("yyyyMMdd")-0;
	if(year12>now){
		return true;
	}
	return false;
}

// jq 扩展
jQuery.fn.extend({
	disabled : function() {
		this.addClass("disabled");
		this.focus(function() {
			this.blur();
		});
	},
	enable:function(){
		this.removeClass("disabled");
		this.unbind("focus");
	},
	showDetail : function() {
		this.click(function() {
			msg($(this).html());
		});
	}
});
String.prototype.format = function(){    
    var args = arguments;    
    return this.replace(/\{(\d+)\}/g,                    
        function(m,i){    
            return args[i];    
        });   
}
String.prototype.addDate = function(days){
	var str = this.replace(/-/g, '/');
	var date = new Date(str);
	date.setDate(date.getDate() + days);
	
	return date.Format("yyyy-MM-dd");
}

// document.onkeydown=function(event){
// var e = event || window.event || arguments.callee.caller.arguments[0];
// if(e && e.keyCode==13){ // enter 键
// $("[st='btn-search']").click();
// }
// };

$(document).ready(function() {
	var current_url = window.location.href;
	var apiurl = $("#hidden_apiurl").val();
	$.ajax({
		url : apiurl + "user/updateUserCurrentPage",
		type : "post",
		data : "current_url=" + current_url,
		success : function(data) {
			
		},
		error : function(data) {
			console.log(eval(data));
		}
	});
});

function dataURLtoFile(dataurl, filename) {
	var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(
			n);
	while (n--) {
		u8arr[n] = bstr.charCodeAt(n);
	}
	return new File([ u8arr ], filename, {
		type : mime
	});
}
(function($) {
	$.fn.tableSum = function(options) {
		var default_options ={
				title:"合计",
				title_index:1,
				accept:[],
				except:[],
				style:[],
		}
		
		
		if(options){
			default_options.title = options.title||default_options.title;
			default_options.title_index = options.title_index||default_options.title_index;
			default_options.accept = options.accept||default_options.accept;
			default_options.except = options.except ||default_options.except;
			default_options.style = options.style ||default_options.style;
		}
		
		default_options.except.push(default_options.title_index);
		
		$this = $(this);
		var len = $this.find("tbody:first tr:first td").length;
		var sum = new Array(len).fill(0);
		
		if(default_options.accept.length==0 && default_options.except.length==0){
			default_options.except = [1];
		}else{
			if(default_options.accept.length!=0){
				for(var i =0;i<len;i++){
					if(default_options.accept.contains(i+1))
						continue;
					default_options.except.push(i+1);
				}
			}
		}
		
		var tbody =$($this.find("tbody.st-sum")[0]|| $("<tbody class='st-sum'></tbody>"));
		tbody.html("");
		var tr = $("<tr></tr>");
		tbody.append(tr);
		
		$this.find("tbody:eq(0) tr").each(function(){
			var tds = $(this).children();
			for(var i =0;i<tds.length;i++){
				
				if(default_options.except.contains(i+1))
					continue;
				var td = $(tds[i]);
				if(typeof +td.text() === 'number' && !isNaN(td.text()))
					sum[i] +=+td.text();
			}
		});
		
		for(var i = 0;i<len;i++){
			var td = $("<td></td>");
			tr.append(td);
			if(default_options.except.contains(i+1)){
				if(i+1==default_options.title_index){
					td.text(default_options.title);
					continue;
				}else{
					continue;
				}
			}
			if((''+sum[i]).indexOf(".")>-1){
				td.text(sum[i].toFixed(2));
			}else{
				td.text(sum[i]);
			}
		}
		 $this.find("tbody:eq(0)").after(tbody);
	}
	
})(jQuery);

function debounce(func, duration = 500) {
    let timer_id;
    return function (...args) {
        clearTimeout(timer_id);
        timer_id = setTimeout(() => {
            func.apply(this, args);
        }, duration);
    }
}

