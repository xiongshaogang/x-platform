/**
 * java代码脚本编辑器
 * <pre>
 * 在同一个页面可以渲染多个编辑器
 * 通过选择设置了codemirror="true"的textarea来渲染
 * 当前光标插入内容：InitMirror.editor.insertCode(str);
 * 设置所有内容：InitMirror.editor.setCode(str);
 * 保存编辑器内容到原来的textarea:InitMirror.save();
 * </pre>
 */
if (typeof InitMirror == 'undefined') {
	InitMirror = {};
}

InitMirror.editor = null;

InitMirror.options = {
	randomRange : 999,// key随机数范围
	editors : [], // 编辑器数组
	height : "150px",
	lock : false,
	initDelay : 0,// 渲染延迟(不设置延迟渲染可能会与ligerui的布局初始化冲突)
	isCurrent : true
};

// 初始化(请勿使用,请使用InitMirror.initId("aaaa"))
InitMirror.init = function() {
	$("textarea[codemirror='true']").each(
			function() {
				var mirrorHeight = $(this).attr("mirrorheight"), key = InitMirror.getKey(), editor = CodeMirror
						.fromTextArea(this, {
							height : mirrorHeight || InitMirror.options.height,
							parserfile : [ "tokenizejava.js", "parsejava.js" ],
							stylesheet : "plug-in/javacode/javacolors.css",
							tabMode : "shift",
							path : "plug-in/javacode/",
							onFocus : function() {
								InitMirror.toggleFocus(key);
							}
						});
				editor.targetId = $(this).attr("id");
				InitMirror.options.editors.push({
					key : key,
					editor : editor
				});
				if (InitMirror.options.isCurrent) {
					InitMirror.editor = editor;
				}
				InitMirror.options.isCurrent = false;
			});
};
//根据id将某个textarea初始化为编辑器
InitMirror.initId = function(id, jsonParams) {
	var editor;
	var mirrorHeight = $("#" + id).attr("mirrorheight");
	var defaultParams = {
		height : mirrorHeight || InitMirror.options.height,
		parserfile : [ "tokenizejava.js", "parsejava.js" ],
		stylesheet : "plug-in/javacode/javacolors.css",
		tabMode : "shift",
		path : "plug-in/javacode/",
		lineNumbers : true,
		mode : "text/x-groovy"
	};
	var key = InitMirror.getKey();
	var options = $.extend({}, defaultParams, jsonParams);
	if (InitMirror.getEditor(id) == null) {
		editor = CodeMirror.fromTextArea(id, options);
		editor.targetId = id;
		InitMirror.options.editors.push({
			key : key,
			editor : editor
		});
	} else {
		var index = InitMirror.indexOf(id);
		editor = CodeMirror.fromTextArea(id, options);
		editor.targetId = id;
		InitMirror.options.editors[index].editor = editor;
	}
	return editor;
};
//根据id获得某个编辑器在所有打开编辑器中的索引
InitMirror.indexOf = function(id) {
	for (var i = 0; i < InitMirror.options.editors.length; i++) {
		var c = InitMirror.options.editors[i];
		if (c.editor) {
			if (c.editor.targetId == id) {
				return i;
			}
		}
	}
	return -1;
}
//根据id获得某个编辑器的值
InitMirror.getValue = function(id) {
	var editor = InitMirror.getEditor(id);
	return editor.getCode();
}
//根据id将某个编辑器的值替换为传入的内容
InitMirror.setValue = function(id, value) {
	var editor = InitMirror.getEditor(id);
	return editor.setCode(value);
}
//根据id向某个编辑器追加值(光标焦点处)
InitMirror.insertValue = function(id, value) {
	var editor = InitMirror.getEditor(id);
	return editor.insertCode(value);
}
//根据textarea的id获取编辑器
InitMirror.getEditor = function(id) {
	for (var i = 0; i < InitMirror.options.editors.length; i++) {
		var c = InitMirror.options.editors[i];
		if (c) {
			if (c.editor.targetId == id) {
				return c.editor;
			}
		}
	}
	return null;
};

// 获取编辑器中不重复的key
InitMirror.getKey = function() {
	var key = Math.floor(Math.random() * InitMirror.options.randomRange);
	for (var i = 0, c; c = InitMirror.options.editors[i++];) {
		if (key == c.key)
			return InitMirror.getKey();
		else
			return key;
	}
	return key;
};
// 切换当前获取焦点的编辑器
InitMirror.toggleFocus = function(key) {
	// focus的事件注册以后会频繁触发，锁定切换操作避免反复调用本方法
	if (InitMirror.options.lock)
		return;
	InitMirror.options.lock = true;
	for (var i = 0, c; c = InitMirror.options.editors[i++];) {
		if (c.key == key) {
			InitMirror.editor = c.editor;
		}
	}
	setTimeout(function() {
		InitMirror.options.lock = false;
	}, 200);
};
// 保存某个编辑器内容
InitMirror.saveById = function(id) {
	var editor = InitMirror.getEditor(id);
	editor.save();
};
// 保存编辑器内容
InitMirror.save = function() {
	for (var i = 0, c; c = InitMirror.options.editors[i++];) {
		c.editor.save();
	}
};
// 遍历所有编辑器
InitMirror.each = function(func) {
	for (var i = 0, c; c = InitMirror.options.editors[i++];) {
		func(c.editor);
	}
};
