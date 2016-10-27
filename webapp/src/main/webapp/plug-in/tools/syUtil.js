//控制某个div下错误tip显示与否
function validTipChange(jq, type) {
	if (type) {
		var validObj = $("[datatype]", jq);
		validObj.each(function(i, ele) {
			var api = $(ele).yitip("api");
			if (api && api.$yitip) {
				if (type == "show") {
					if (api.$content && api.$content.text().length > 0) {
						api.$yitip.show();
					}
				} else if (type == "hide") {
					api.$yitip.hide();
				}
			}
		});
	}
}
/**
 * @author 孙宇
 * 
 * @requires jQuery
 * 
 * 将form表单元素的值序列化成对象
 * 
 * @returns object
 */
serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};

/**
 * @author 孙宇
 * 
 * 增加formatString功能
 * 
 * 使用方法：formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * 
 * @returns 格式化后的字符串
 */
formatString = function(str) {
	for (var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

/**
 * @author 孙宇
 * 
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * 
 * @returns list
 */
stringToList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for (var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

/**
 * @author 孙宇
 * 
 * @requires jQuery
 * 
 * 改变jQuery的AJAX默认属性和方法
 */
$.ajaxSetup({
	type : 'POST',
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		$.messager.progress('close');
		$.messager.alert('错误', XMLHttpRequest.responseText);
	}
});

//时间格式化
Date.prototype.format = function (format,value) {
	/*
	 * eg:format="yyyy-MM-dd HH:mm:ss";
	 */
	if (!format) {
		format = "yyyy-MM-dd HH:mm:ss";
	}
	var date;
	if(value){
		//将2014-6-27 12:22:33.0转换为2014/6/27 12:22:33
		var strdata=value.replace(/-/g,"/");
		var index=strdata.indexOf(".");
		if(index>0)
		{
			strdata=strdata.substr(0,index);
		}
		//如果传回来的时间字符串是"Sun Mar 31 12:00:00 CST 2013"的形式,要转成GMT+0800的时区格式,否则会多14个小时
		if(strdata.indexOf("CST")>0){
			strdata=strdata.replace("CST","GMT+0800");
		}
		date= new Date(Date.parse(strdata));
	}else{
		date=this;
	}
	
	var o = {
		"M+" : date.getMonth() + 1, // month
		"d+" : date.getDate(), // day
		"H+" : date.getHours(), // hour
		"m+" : date.getMinutes(), // minute
		"s+" : date.getSeconds(), // second
		"q+" : Math.floor((date.getMonth() + 3) / 3), // quarter
		"S" : date.getMilliseconds()
		// millisecond
	};
	
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4-RegExp.$1.length));
	}
	
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	
	return format;
};

// 把时间字符串转化为时间(给easyui的datebox/datetimebox的parse类使用)
// 支持如下格式
// 2006
// 2006-01
// 2006-01-01
// 2006-01-01 12
// 2006-01-01 12:12
// 2006-01-01 12:12:12
function dateParser(s) {
	var date = new Array();
	var finalDate;
	if (s) {
		var regexDT = /(\d{4})-?(\d{2})?-?(\d{2})?\s?(\d{2})?:?(\d{2})?:?(\d{2})?/g;
		var matchs = regexDT.exec(s);

		for (var i = 1; i < matchs.length; i++) {
			if (matchs[i] != undefined) {
				date[i] = matchs[i];
			} else {
				if (i <= 3) {
					date[i] = '01';
				} else {
					date[i] = '00';
				}
			}
		}
	} else {
		return new Date();
	}
	finalDate = new Date(date[1], date[2] - 1, date[3], date[4], date[5], date[6]);
	return finalDate;
}

// 把时间格式化为指定的字符串形式,如"yyyy-MM-dd
// HH:mm:ss"(给easyui的datebox/datetimebox的formatter类使用)
function datetimeFormatters(date, format) {
	if(!format){
		format="yyyy-MM-dd HH:mm";
	}
	return date.format(format);
}

/* 删除数组中指定下标元素 */
Array.prototype.indexDel = function(index) {
	if (index < 0) {
		return this;
	} else {
		return this.splice(index, 1);
	}
}

/* 删除数组中指定值元素 */
Array.prototype.valueDel = function(value) {
	var index = $.inArray(value, this);
	this.indexDel(index);
}

/* 删除字符串中最后一个逗号 */
String.prototype.removeDot = function() {
	return (this.indexOf(",") != -1) ? this.substring(0, this.length - 1) : this;
}

/* 字符串为空则替换 */
nullsReplace = function(str, replaceStr) {
	return (typeof (str) == "undefined" || (typeof (str) == "string" && (str.toLowerCase == "null" || str == ""))) ? replaceStr : str;
}

/* 处理js中字符串的undefined和null值,返回"" */
nulls = function(str) {
	if (typeof (str) == "undefined" || (typeof (str) == "string" && str.toLowerCase == "null")||str==null) {
		return "";
	}
	return str;
}
/* 获取文件后缀名 */
getExt = function(fileName) {
	var pointIndex = fileName.lastIndexOf(".");
	return fileName.substr(pointIndex);
}
/* js对象转化为url后面跟的参数 */
obj2url = function(param, key) {
	var paramStr = "";
	if (param instanceof String || param instanceof Number || param instanceof Boolean) {
		paramStr += "&" + key + "=" + encodeURIComponent(param);
	} else {
		$.each(param, function(i) {
			var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
			paramStr += '&' + obj2url(this, k);
		});
	}
	return paramStr;
};
/* 判断是否为json,'{a:1}'这样的字符串不算 */
isJson = function(obj) {
	var isjson = typeof (obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
	return isjson;
}

/* 将字符串转化为json,若本来就是json对象则直接返回 */
parseJSON = function(obj) {
	var d = isJson(obj) ? obj : $.parseJSON(obj);
	return d;
}

// 当ajax请求运行的方法发生异常后,页面操作
function ajaxError(data) {
	var error;
	if (data.responseText == '' || data.responseText == undefined) {
		if (data.msg && data.msg != "") {
			error = data.msg;
		} else {
			// 遇到无法解析的错误提示信息
			return;
		}
	} else {
		try {
			error = data.responseText.substring(data.responseText.indexOf('错误描述'), data.responseText.indexOf('错误信息'));
		} catch (ex) {
			error = data.responseText;
		}
	}
	
	if (error && error != "") {
		tip(error, 'danger', 'fa-bolt');
	}
// $.Hidemsg();
}

// 系统公用ajax方法
function ajax(url, callbackFun, jsonparam) {
	var defaultOptions = {
		async : false,
		cache : false,
		type : 'POST',
		dataType : 'json',
		url : url,// 请求的action路径
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		},
		success : function(data) {
			var d = parseJSON(data);
			if (d.success) {
				if (callbackFun) {
					if (typeof (callbackFun) == 'function') {
						callbackFun(d);
					}
				}
			} else {
				ajaxError(d);
			}
		}
	};
	var options = $.extend({}, defaultOptions, jsonparam);
	$.ajax(options);
}
//系统公用ajax方法(自动根据返回msg弹tip)
function ajaxTip(url, callbackFun, jsonparam) {
	var defaultOptions = {
		async : false,
		cache : false,
		type : 'POST',
		dataType : 'json',
		url : url,// 请求的action路径
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		},
		success : function(data) {
			var d = parseJSON(data);
			if (d.success) {
				var msg = d.msg;
				if (msg && msg != "") {
					tip(msg);
				}
				if (callbackFun) {
					if (typeof (callbackFun) == 'function') {
						callbackFun(d);
					}
				}
			} else {
				ajaxError(d);
			}
		}
	};
	var options = $.extend({}, defaultOptions, jsonparam);
	$.ajax(options);
}

// 是否存在指定函数
function isExitsFunction(funcName) {
	try {
		if (typeof (eval(funcName)) == "function") {
			return true;
		}
	} catch (e) {
	}
	return false;
}
// 是否存在指定变量
function isExitsVariable(variableName) {
	try {
		if (typeof (variableName) == "undefined") {
			// alert("value is undefined");
			return false;
		} else {
			// alert("value is true");
			return true;
		}
	} catch (e) {
	}
	return false;
}

// 获得useragent
function getUseragent() {
	if (navigator.userAgent.toLowerCase().indexOf('iphone') > 0) {
		return "iphone";
	} else if (navigator.userAgent.toLowerCase().indexOf('android') > 0) {
		return "android";
	} else {
		return "pcweb";
	}
}

function escapeJquery(srcString) {
	// 转义之后的结果
	var escapseResult = srcString;
	// javascript正则表达式中的特殊字符
	var jsSpecialChars = [ "//", "^", "$", "*", "?", ".", "+", "(", ")", "[", "]", "|", "{", "}" ];
	// jquery中的特殊字符,不是正则表达式中的特殊字符
	var jquerySpecialChars = [ "~", "`", "@", "#", "%", "&", "=", "'", "/", ":", ";", "<", ">", ",", "/" ];
	for (var i = 0; i < jsSpecialChars.length; i++) {
		escapseResult = escapseResult.replace(new RegExp("\\" + jsSpecialChars[i], "g"), "\\" + jsSpecialChars[i]);
	}
	for (var i = 0; i < jquerySpecialChars.length; i++) {
		escapseResult = escapseResult.replace(new RegExp(jquerySpecialChars[i], "g"), "\\" + jquerySpecialChars[i]);
	}
	return escapseResult;
}

//检查数字输入框
var checkNumbers = function(obj, num) {
	if(num==0){
		//先把非数字的都替换掉
		obj.value = obj.value.replace(/[^\d]/g, "");
	}else{
		//先把非数字的都替换掉，除了数字和.
		obj.value = obj.value.replace(/[^\d.]/g, "");
		//必须保证第一个为数字而不是.
		obj.value = obj.value.replace(/^\./g, "");
		//保证只有出现一个.而没有多个.
		obj.value = obj.value.replace(/\.{2,}/g, ".");
		//保证.只出现一次，而不能出现两次以上
		obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
		var index = obj.value.indexOf(".");
		if (index >= 0 && (obj.value.length - 1) - index > num) {
			obj.value = obj.value.substring(0, obj.value.length - 1);
		}
	}
}

//获取浏览器种类和版本
var getBrowserInfo = function(){
	var Sys = {},
		ua = navigator.userAgent.toLowerCase(),
		s, scan;
	
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
	(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
	(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	(s = ua.match(/micromessenger\/([\d.]+)/)) ? Sys.weixin = s[1] :
	(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
	(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

	if(Sys.ie){
	    return {type: "ie", version: Sys.ie};
	}
	if(Sys.firefox){    
		return {type: "firefox", version: Sys.firefox};
	}
	if(Sys.chrome){
		return {type: "chrome", version: Sys.chrome};
	}
	if(Sys.weixin){
		return {type: "weixin", version: Sys.weixin};
	}
	if(Sys.opera){
		return {type: "opera", version: Sys.opera};
	}
	if(Sys.safari){
		return {type: "safari", version: Sys.safari};
	}
}

//获得当前点击事件
var getEvent = function() {
	if (window.event) {
		return window.event;
	} else {
		var aFunction = function(dArguments, dLevel) {
			if (!dArguments)
				return null;
			if (!dLevel)
				return null;
			for (var i = 0; i < dArguments.length; i++) {
				if (dArguments[i] && dArguments[i].altKey !== undefined) {
					return dArguments[i];
				}
			}
			if (dArguments && dArguments.callee && dArguments.callee.caller && dArguments.callee.caller.arguments) {
				return aFunction(dArguments.callee.caller.arguments, 5 - 1);
			}
		}
		return aFunction(arguments, 5);
	}
}

//动态调用方法
callbackFunction = function(fn, args) {
	if (typeof fn == "function") {
		return fn.call(this, args);
	} else {
		var fun = eval(fn);
		return fun.call(this, args);
	}
}

var defaultImg=function(preUrl,src){
	if(src.indexOf("http")!=-1){
		return src;
	}
	return nulls(src) ? preUrl + src : 'basic/img/avatars/avatar_80.png';
}

//对json中的换行符等进行处理
filterSymbol = function(src){
	var dest=src.replace(/\n/g, "\\n")
	.replace(/\&/g, "\\&")
	.replace(/\r/g, "\\r")
	.replace(/\t/g, "\\t")
	.replace(/\f/g, "\\f");
	return dest;
}

//textarea高度自适应
$.fn.autoHeight = function(){
	function autoHeight(elem){
		elem.style.height = 'auto';
		elem.scrollTop = 0; //防抖动
		elem.style.height = elem.scrollHeight + 'px';
	}
	
	this.each(function(){
		autoHeight(this);
		$(this).on('keyup', function(){
			autoHeight(this);
		});
	});
}