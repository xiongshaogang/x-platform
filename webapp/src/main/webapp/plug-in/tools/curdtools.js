//﻿var jq = jQuery.noConflict();
/**
 * 增删改工具栏
 */
// window.onerror = function() {
// return true;
// };
var iframe;// iframe操作对象
var win;// 窗口对象
var gridID = "";// 操作datagrid对象名称
var D; // 当前打开的dialog的div的jquery对象

/**
 * @author xiaqiang
 * 
 * title 窗口标题 addurl 打开的url width 自定义宽度 height 自定义高度 preinstallWidth
 * 预设宽度,有1,2两个值:表示1栏(2个td),2栏(4个td)
 * 
 * jsonparam 1.主要是用来改EasyUI的.dialog()的默认设置,详细可查阅EasyUI文档(panel、window、dialog)
 * 2.然后一些后续添加的参数,比如设置窗口挂靠Div的ID----dialogID这种非EasyUI的参数
 * 
 * 现在特殊解析的参数如下: dialogID 自行设置的窗口div的id,可以为空,如果为空,自动产生uuid类型的id optFlag
 * 控制窗口底部操作栏出现的按钮,有3个值'add','update','detail'.add和update显示保存、关闭;detail只显示关闭
 * 
 * 演示用法: createwindow('我是标题','systemController.do?update',null,300,2,{
 * optFlag:'add', dialogID:'testId' });
 */
function createwindow(title, addurl, myWidth, myHeight, preinstallWidth, jsonparam) {
	/** ******** 对宽高进行处理 ********* */
	var width;
	var height;
	// 如果有preinstallWidth预设宽度的话
	if (preinstallWidth) {
		if (preinstallWidth == 1) {
			width = 400;
		} else if (preinstallWidth == 2) {
			width = 670;
		}
	}
	// 如果没显式传入宽高则默认一个宽高
	width = myWidth ? myWidth : width;
	height = myHeight ? myHeight : height;

	// 如果传入的宽高为百分比,则通过当前body的可见大小offsetXX(包含滚动条宽度)来确认实际宽高
	if ($.type(width) === "string") {
		if (width.indexOf("%")) {
			var pWidth = parseFloat(width) / 100;
			width = ($(window).width()) * pWidth;
		}
	}
	if ($.type(height) === "string") {
		if (height.indexOf("%")) {
			var pHeight = parseFloat(height) / 100;
			height = ($(window).height()) * pHeight;
		}
	}

	/** ******** 动态构造需要的div和iframe ********* */
	var dialogWindow;// 声明用于构造dialog的div对象
	var divIframe;// 声明放在div内部的iframe对象
	var dialogID;// 声明放在dialog的id
	var iframeID;// 声明放在iframe的id
	var isIframe;// 是否创建iframe页面
	var buttonsTemplate;// 按钮栏按钮数组
	var formId;// 本弹出页要提交的form表单Id
	var btnsub;// 本弹出页的提交按钮Id
	var onloadWin;// iframe onload方法
	/** ******** 处理jsonparam ********* */
	if (jsonparam) {
		/** ①----自定义div的ID * */
		var jsonDialogID = jsonparam.dialogID;
		if (jsonDialogID) {
			// 如果jsonparam传入了diaglogID,则使用传入的diaglogID,如果没有显式传入,则用uuid去产生一个无重复的uuid
			dialogID = jsonDialogID;
		}

		/** ②----自定义弹出页Form的ID * */
		formId = jsonparam.formId ? jsonparam.formId : null;
		btnsub = jsonparam.btnsub ? jsonparam.btnsub : null;
		/***********************************************************************
		 * ③----为3种默认的窗口,添加操作栏,需要在第6个参数的json参数里,加入optFlag,比如{optFlag:'add'}
		 * 有3种预设值,'add','update','detail'分别对应'新增','编辑','查看'
		 **********************************************************************/
		var optFlag = jsonparam.optFlag;
		if (optFlag) {
			if ("add" == optFlag) {
				if(addurl){
					addurl = addurl + "&optFlag=add";
				}
				buttonsTemplate = [ {
					id : 'save',
					text : '保存',
					iconCls : 'fa fa-save',
					handler : function() {
						saveObj(btnsub, formId);
					}
				}, {
					id : 'close',
					text : '关闭',
					iconCls : 'glyphicon glyphicon-remove',
					handler : function() {
						closeD($(this).closest(".dialog-button").prev(".window-body"));
					}
				} ];
			}
			if ("update" == optFlag) {
				if(addurl){
					addurl = addurl + "&optFlag=update";
				}
				buttonsTemplate = [ {
					id : 'save',
					text : '保存',
					iconCls : 'fa fa-save',
					handler : function() {
						saveObj(btnsub, formId);
					}
				}, {
					id : 'close',
					text : '关闭',
					iconCls : 'glyphicon glyphicon-remove',
					handler : function() {
						closeD($(this).closest(".dialog-button").prev(".window-body"));
					}
				} ];
			} else if ("send" == optFlag) {
				buttonsTemplate = [ {
					id : "send",
					text : '发送',
					iconCls : 'glyphicon glyphicon-plus',
					handler : function() {
						saveObj(btnsub, formId);
					}
				}, {
					text : '关闭',
					iconCls : 'glyphicon glyphicon-remove',
					handler : function() {
						closeD($(this).closest(".dialog-button").prev(".window-body"));
					}
				} ];
			} else if ("detail" == optFlag) {
				if(addurl){
					addurl = addurl + "&optFlag=detail";
				}
				buttonsTemplate = [ {
					text : '关闭',
					iconCls : 'glyphicon glyphicon-remove',
					handler : function() {
						closeD($(this).closest(".dialog-button").prev(".window-body"));
					}
				} ];
			} else if ("blank" == optFlag) {
				buttonsTemplate = [];
			} else if ("close" == optFlag) {
				buttonsTemplate = [ {
					text : '关闭',
					iconCls : 'glyphicon glyphicon-remove',
					handler : function() {
						closeD($(this).closest(".dialog-button").prev(".window-body"));
					}
				} ];
			}
		}

		/** ④----自定义div的ID * */
		var isIframe = jsonparam.isIframe;
	}
	if(addurl){
//		addurl += "&new=" + Math.random();
	}
	// 默认的窗口属性
	var defaultOptions = {
		title : title,
		width : width,
		height : height,
		cache : false,
		collapsible : false,
		closable : true,
		shadow : false,
		modal : true,
		onClose : function() {
			// 关闭弹出框时,还要销毁本弹出框下的验证提示框
			var validObj = $("[datatype]", $(this));
			validObj.each(function(i, ele) {
				var api = $(ele).yitip("api");
				if (api && api.$yitip) {
					api.$yitip.remove();
				}
			});
			// 每次关闭后,销毁弹出框以及子iframe
			$(this).dialog('destroy');

		},
		onMove : function() {
			resetYitipPosition($(this));
		},
		onBeforeCollapse : function() {
			validTipChange($(this), "hide");
		},
		onBeforeExpand : function() {
			validTipChange($(this), "show");
		},
		buttons : buttonsTemplate
	};

	// 合并默认配置和用户自定义配置json,遇到相同配置,后者会覆盖前者
	var options = $.extend({}, defaultOptions, jsonparam);
	if (isIframe) { // 如果产生iframe页面
		dialogID = !dialogID ? Math.uuid() : dialogID;
		iframeID = dialogID + "_iframe"; // 构造div内部的iframe的ID和name,就是dialog所在div的ID加上'_iframe'
		// 动态构造div
		if ($('#' + dialogID)[0]) { // 为true说明已存在该div,不需要重复生成
			dialogWindow = $('#' + dialogID);
			divIframe = $('#' + iframeID);
			// 重新加载iframe的内容
			divIframe.attr('src', divIframe.attr('src'));
			dialogWindow.dialog('open');
		} else { // 为false说明该id的div还未生成,重新构造
			onloadWin = jsonparam.onloadWin ? jsonparam.onloadWin : "";
			dialogWindow = $("<div id='" + dialogID + "' ></div>");
			divIframe = $("<iframe name='" + iframeID + "' id='" + iframeID + "' src='" + addurl + "'" + " onload= '" + onloadWin + "'"
					+ "scrolling='auto' style='width:100%;height:99.47%;' frameborder='0'></iframe>");
			// 动态添加div到本页面的body上
			dialogWindow.appendTo(window.document.body);
			// 动态添加iframe到div上
			divIframe.appendTo(dialogWindow);
			// 通过最终的options json类型配置数据,产生dialog
			$("#" + dialogID).dialog(options);
		}
		// 将刚产生的div赋给全局变量
		D = $('#' + dialogID);
		// 将刚产生的iframe jquery对象转为js对象赋给全局变量
		iframe = divIframe[0];
	} else { // 如果产生非iframe页面
		dialogID = !dialogID ? Math.uuid() : dialogID;
		if ($('#' + dialogID)[0]) { // 为true说明已存在该div,不需要重复生成
			dialogWindow = $('#' + dialogID);
			dialogWindow.dialog('open');
		} else { // 为false说明该id的div还未生成,重新构造
			dialogWindow = $("<div id='" + dialogID + "' ></div>");
			// 动态添加div到本页面的body上
			dialogWindow.appendTo(window.document.body);
			if (addurl) {
				options.href = addurl;
			}
			// 通过最终的options json类型配置数据,产生dialog
			$("#" + dialogID).dialog(options);
		}
	}

}

// 设置iframe src中内容加载后的高度
function setIframeLoadedHeight(iframeObj) {
	if (iframeObj.src.length > 0) {
		if (!iframeObj.readyState || iframeObj.readyState == "complete") {
			var bHeight;
			if (iframeObj.contentDocument && iframeObj.contentDocument.body.offsetHeight)  
				bHeight = iframeObj.contentDocument.body.offsetHeight; //FF NS  
	        else if(iframeObj.Document && iframeObj.document.body.scrollHeight)  
	        	bHeight = iframeObj.document.body.scrollHeight;//IE  
			
//			var bHeight = iframeObj.contentWindow.document.body.scrollHeight;
			var dHeight = iframeObj.contentWindow.document.documentElement.scrollHeight;
			var height = Math.max(bHeight, dHeight);
			$(iframeObj).height(height);
		}
	}
}

function upload(curform) {
	upload();
}
/**
 * 添加事件打开窗口
 * 
 * @param title
 *            编辑框标题
 * @param addurl//目标页面地址
 */
function add(title, url, gridID, width, height, preinstallWidth, exParams) {
	var defaultOptions = {
		optFlag : 'add'
	};
	url += '&optFlag=add';
	var options = $.extend({}, defaultOptions, exParams);
	createwindow(title, url, width, height, preinstallWidth, options);
}

/**
 * 更新事件打开窗口
 * 
 * @param title
 *            编辑框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function update(title, url, gridID, width, height, preinstallWidth, exParams) {
	var rowsData = $('#' + gridID).datagrid('getSelections');
	if (!rowsData || rowsData.length == 0) {
		tip('请选择编辑项目');
		return;
	}
	if (rowsData.length > 1) {
		tip('请选择一条记录再编辑');
		return;
	}

	url += '&optFlag=update';
	if (exParams && exParams.rowId) {
		url += '&id=' + rowsData[0][exParams.rowId];
	} else {
		url += '&id=' + rowsData[0].id;
	}
	var defaultOptions = {
		optFlag : 'update'
	};
	var options = $.extend({}, defaultOptions, exParams);
	createwindow(title, url, width, height, preinstallWidth, options);
}

/**
 * 查看详细事件打开窗口
 * 
 * @param title
 *            查看框标题
 * @param addurl
 *            目标页面地址
 * @param gridID
 *            datagrid的ID
 */
function detail(title, url, gridID, width, height, preinstallWidth, exParams) {
	var rowsData = $('#' + gridID).datagrid('getSelections');
	if (!rowsData || rowsData.length == 0) {
		tip('请选择查看项目');
		return;
	}
	if (rowsData.length > 1) {
		tip('请选择一条记录再查看');
		return;
	}
	url += '&optFlag=detail';
	if (exParams && exParams.rowId) {
		url += '&id=' + rowsData[0][exParams.rowId];
	} else {
		url += '&id=' + rowsData[0].id;
	}
	var defaultOptions = {
		optFlag : 'detail'
	};
	var options = $.extend({}, defaultOptions, exParams);
	createwindow(title, url, width, height, preinstallWidth, options);
}

/**
 * 关闭指定div对象所产生的easyui dialog弹出框 dialogJQObj
 * dialog所属的div的jquery对象(注意是jquery对象,不是js对象)
 */

function closeD(dialogJQObj) {
	dialogJQObj.dialog('close');
	$.Hidemsg();
}

/**
 * 多记录刪除請求
 * 
 * @param title
 * @param url
 * @param gname
 * @return
 */
function deleteALLSelect(title, msg, url, gridID, callbackFun, rowId) {
	title = title ? title : '提示信息';
	msg = msg ? msg : '确定删除所有所选数据?';
	var ids = [];
	var rows = $("#" + gridID).datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm(title, msg, function(r) {
			if (r) {
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i][rowId]);
				}
				$.ajax({
					url : url,
					type : 'post',
					data : {
						ids : ids.join(',')
					},
					cache : false,
					success : function(data) {
						var d = parseJSON(data);
						if (d.success) {
							$("#" + gridID).datagrid('reload');
							var msg = d.msg;
							if (msg && msg != "") {
								tip(msg);
							}
							$("#" + gridID).datagrid('unselectAll');
							ids = '';
						}
						if (callbackFun) {
							if (typeof (callbackFun) == "function") {
								callbackFun(data);
							}
						}
					}
				});
			}
		});
	} else {
		tip("请选择需要删除的数据");
	}
}

// 删除调用函数
function delObj(url, gridID, callbackFun) {
	reqConfirm('删除确认', '确定删除该记录吗 ?', url, gridID, callbackFun);
}

// 上传附件时删除调用函数
function confuploadify(url, id) {
	$.dialog.confirm('确定删除吗', function() {
		deluploadify(url, id);
	}, function() {
	});
}
/**
 * 执行删除附件
 * 
 * @param url
 * @param index
 */
function deluploadify(url, id) {
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
		},
		success : function(data) {
			var d = parseJSON(data);
			if (d.success) {
				$("#" + id).remove();// 移除SPAN
				m.remove(id);// 移除MAP对象内字符串
			}

		}
	});
}
// 普通询问操作调用函数
function confirms(url, content, gridID) {
	reqConfirm(null, content, url, gridID);
}

/**
 * 提示信息
 */
function tip(msg, colorClass, iconClass) {
	// 若没有提示的样式,则显示用alert显示
	if (isExitsFunction('Notify')) {
		if (arguments.length == 1) {
			Notify(msg, 'top-right', '5000', 'info', 'fa-info', true);

		} else if (arguments.length == 2) {
			Notify(msg, 'top-right', '5000', colorClass, 'fa-info', true);
		} else if (arguments.length == 3) {
			Notify(msg, 'top-right', '5000', colorClass, iconClass, true);
		}
	} else {
		alert(msg);
	}
}

/**
 * 提示信息像alert一样
 */
function alertTip(msg, title) {
	// 指定标题
	$("#modal-danger .modal-title").html(title);
	// 指定提示信息
	$("#modal-danger .modal-body").html(msg);
	$('#modal-danger').modal('show');
}

/**
 * 创建上传页面窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function openuploadwin(title, url, gridID, width, height) {
	createwindow(title, url, width, height, null, {
		buttons : [ {
			text : '开始上传',
			handler : function() {
				iframeWin = iframe.contentWindow;
				iframeWin.upload();
				return false;
			}
		}, {
			text : '取消上传',
			handler : function() {
				iframeWin = iframe.contentWindow;
				iframeWin.cancel();
			}
		} ]
	});

}

/**
 * 创建确认窗口
 * 
 * @param title
 *            标题,可不传
 * @param content
 *            提示内容
 * @param callbackFun
 *            点击是之后的事件
 */
function createconfirm(title, content, callbackFun) {
	title = title ? title : '提示信息';
	$.messager.confirm(title, content, callbackFun);
}

/**
 * 创建询问窗口
 * 
 * @param title
 * @param content
 * @param url
 */
function reqConfirm(title, content, url, gridID, callbackFun) {
	title = title ? title : '提示信息';
	$.messager.confirm(title, content, function(r) {
		if (r) {
			doSubmit(url, gridID, callbackFun);
			rowid = '';
		}
	});
}

/**
 * 执行保存
 * 
 * @param url
 * @param gridID
 */
function saveObj(btnsub, formId) {
	var b = btnsub ? btnsub : 'btn_sub';
	var f = formId ? formId : 'formobj';
	// 在对应的form下点击查找对应的提交按钮,并点击
	$('#' + b, $('#' + f)).click();
}

/**
 * 执行操作
 * 
 * @param url
 * @param index
 */
function doSubmit(url, gridID, callbackFun, type) {
	$.ajax({
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
				if (gridID) {
					if (type == "tree") {
						$('#' + gridID).tree("reload");
					} else if (type == "treegrid") {
						$('#' + gridID).treegrid("reload");
					} else {
						$('#' + gridID).datagrid("reload");
					}
				}
				if (msg && msg != "") {
					tip(msg,'success','fa-check');
				}
				if (callbackFun) {
					if (typeof (callbackFun) == 'function') {
						callbackFun(data);
					}
				}
			} else {
				ajaxError(d);
			}
		}
	});
}

/**
 * ajax执行同步操作
 * 
 * @param url
 * @param index
 */
function doAction(url, gridID, callbackFun, type, parameters) {
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		data : {
			"parameters" : parameters
		},// 所传参数
		error : function() {// 请求失败处理函数
		},
		success : function(data) {
			var d = parseJSON(data);
			if (d.success) {
				var msg = d.msg;
				if (gridID) {
					if (type == "tree") {
						$('#' + gridID).tree("reload");
					} else if (type == "treegrid") {
						$('#' + gridID).treegrid("reload");
					} else {
						$('#' + gridID).datagrid("reload");
					}
				}
				if (msg && msg != "") {
					tip(msg);
				}
				if (callbackFun) {
					callbackFun(data);
				}
			}
		}
	});
}

// 添加标签
function addOneTab(subtitle, url, icon) {
	if (icon == '') {
		icon = 'icon folder';
	}
	window.top.$.messager.progress({
		text : '页面加载中....',
		interval : 300
	});
	window.top.$('#maintabs').tabs({
		onClose : function(subtitle, index) {
			window.top.$.messager.progress('close');
		}
	});
	if (window.top.$('#maintabs').tabs('exists', subtitle)) {
		window.top.$('#maintabs').tabs('select', subtitle);
		window.top.$('#maintabs').tabs('update', {
			tab : window.top.$('#maintabs').tabs('getSelected'),
			options : {
				title : subtitle,
				href : url,
				// content : '<iframe name="tabiframe" scrolling="no"
				// frameborder="0" src="' + url + '"
				// style="width:100%;height:99%;"></iframe>',
				closable : true,
				icon : icon
			}
		});
	} else {
		if (url.indexOf('isIframe') != -1) {
			window.top.$('#maintabs').tabs('add', {
				title : subtitle,
				content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
				closable : true,
				icon : icon
			});
		} else {
			window.top.$('#maintabs').tabs('add', {
				title : subtitle,
				href : url,
				closable : true,
				icon : icon
			});
		}
	}
}
// 关闭自身TAB刷新父TABgrid
function closetab(title) {
	// 暂时先不刷新
	// window.top.document.getElementById('tabiframe').contentWindow.reloadTable();
	// window.top.document.getElementById('maintabs').contentWindow.reloadTable();
	window.top.$('#maintabs').tabs('close', title);
	// tip("添加成功");
}

// popup
// object: this name:需要选择的列表的字段 code:动态报表的code
function inputClick(obj, name, code) {
	$.dialog.setting.zIndex = 2000;
	if (name == "" || code == "") {
		alert("popup参数配置不全");
		return;
	}
	if (typeof (windowapi) == 'undefined') {
		$.dialog({
			content : "url:cgReportController.do?popup&id=" + code,
			lock : true,
			title : "选择",
			width : 800,
			height : 400,
			cache : false,
			ok : function() {
				iframeWin = this.iframe.contentWindow;
				var selected = iframeWin.getSelectRows();
				if (selected == '' || selected == null) {
					alert("请选择");
					return false;
				} else {
					var str = "";
					$.each(selected, function(i, n) {
						if (i == 0)
							str += n[name];
						else
							str += "," + n[name];
					});
					$(obj).val("");
					// $('#myText').searchbox('setValue', str);
					$(obj).val(str);
					return true;
				}

			},
			cancelVal : '关闭',
			cancel : true
		/* 为true等价于function(){} */
		});
	} else {
		$.dialog({
			content : "url:cgReportController.do?popup&id=" + code,
			lock : true,
			title : "选择",
			width : 800,
			height : 400,
			parent : windowapi,
			cache : false,
			ok : function() {
				iframeWin = this.iframe.contentWindow;
				var selected = iframeWin.getSelectRows();
				if (selected == '' || selected == null) {
					alert("请选择");
					return false;
				} else {
					var str = "";
					$.each(selected, function(i, n) {
						if (i == 0)
							str += n[name];
						else
							str += "," + n[name];
					});
					$(obj).val("");
					// $('#myText').searchbox('setValue', str);
					$(obj).val(str);
					return true;
				}

			},
			cancelVal : '关闭',
			cancel : true
		/* 为true等价于function(){} */
		});
	}
}

/**
 * Jeecg Excel 导出 代入查询条件
 */
function JeecgExcelExport(url, datagridId) {
	var queryParams = $('#' + datagridId).datagrid('options').queryParams;
	$('#' + datagridId + 'tb').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	var params = '&';
	$.each(queryParams, function(key, val) {
		params += '&' + key + '=' + val;
	});
	var fields = '&field=';
	$.each($('#' + datagridId).datagrid('options').columns[0], function(i, val) {
		if (val.field != 'opt') {
			fields += val.field + ',';
		}
	});
	window.location.href = url + encodeURI(fields + params);
}
/**
 * 自动完成的解析函数
 * 
 * @param data
 * @returns {Array}
 */
function jeecgAutoParse(data) {
	var parsed = [];
	$.each(data.rows, function(index, row) {
		parsed.push({
			data : row,
			result : row,
			value : row.id
		});
	});
	return parsed;
}

/**
 * 为操作栏新增按钮操作,最终使用方法,不同参数个数分别调用不同的方法 xiaqiang 2014.6.6 divJq
 * 操作栏所在Div的jquery对象,使用了createwindow方法的就是parent.D myButtons
 * 新增的按钮,支持单个按钮的json对象,和多个按钮的json对象数组,示例: var myButton = { id : 'a', text :
 * '测试1', iconCls : 'awsm-icon-save', handler : function() { closeD(parent.D); } };
 * var myButton1 = [ { id : 'b', text : '测试2', iconCls : 'awsm-icon-save',
 * handler : function() { alert(1); } }, { text : '测试3', iconCls :
 * 'awsm-icon-save', handler : function() { } } ];
 * 
 * index 添加到操作栏的位置,从1开始代表放到第一位.2代表放到第二位...
 * 
 * 一般情况下,我们使用2个参数的方法,addButton(myButton, 1);
 */
function addButton() {
	var argLength = arguments.length;
	var mybuttonsArray = null;
	switch (argLength) {
	case 2:
		mybuttonsArray = filterButtons(arguments[1]);
		addButtonDefault(arguments[0], mybuttonsArray);
		break;
	case 3:
		mybuttonsArray = filterButtons(arguments[1]);
		addButtonWithDivJq(arguments[0], mybuttonsArray, arguments[2]);
		break;
	}
}

/**
 * 为操作栏新增按钮操作,最终使用方法,不同参数个数分别调用不同的方法 xiaqiang 2014.6.6 divJq
 * 操作栏所在Div的jquery对象,使用了createwindow方法的就是parent.D idOrNameOrIndex
 * 需要删除的按钮的位置(1代表第一个按钮...)或按钮id(对应添加时json的id)或按钮名称(对应添加时json的text)
 * 
 * 一般情况下,我们使用1个参数的方法,removeButton(1)/removeButton('保存')/removeButton('4eqeq3212qaaddq3231213')
 */
function removeButton() {
	var argLength = arguments.length;
	switch (argLength) {
	case 1:
		removeButtonDefault(arguments[0]);
		break;
	case 2:
		removeButtonWithDivJq(arguments[0], arguments[1]);
		break;
	}
}

/** 为操作栏新增按钮操作,使用默认弹出框div xiaqiang 2014.6.6 * */
function addButtonDefault(myButtons, index) {
	var divJq = parent.D;
	addButtonWithDivJq(divJq, myButtons, index);
}

/** 为操作栏移除按钮操作,使用默认弹出框div xiaqiang 2014.6.6 * */
function removeButtonDefault(idOrNameOrIndex) {
	var divJq = parent.D;
	removeButtonWithDivJq(divJq, idOrNameOrIndex);
}

/** 为操作栏新增按钮操作,使用自己指定的弹出框Div对象 xiaqiang 2014.6.6 * */
function addButtonWithDivJq(divJq, myButtons, index) {
	var oldButtons = divJq.dialog("options").buttons;
	// 若原先操作栏存在按钮
	if (oldButtons) {
		if ($.isPlainObject(myButtons)) { // 如果是单个按钮,则直接添加
			// 在指定位置追加按钮
			oldButtons.splice(index - 1, 0, myButtons);
		} else if ($.isArray(myButtons)) { // 如果是按钮数组,则分割开逐个添加
			$.each(myButtons, function(i, obj) {
				// 在指定位置追加按钮
				oldButtons.splice(index - 1 + i, 0, obj);
			});
		}
	} else { // 若原先操作栏不存在按钮 oldButtons=null,则直接把新按钮赋给oldButtons
		if ($.isPlainObject(myButtons)) { // 如果是单个按钮,则转化为数组再赋值(否则easyui再想添加按钮就会出错)
			var array = [];
			array.push(myButtons);
			oldButtons = array;
		} else if ($.isArray(myButtons)) { // 如果是按钮数组,则直接赋值
			oldButtons = myButtons;
		}
	}
	initButtonBar(divJq, oldButtons);
	// 重新将按钮赋值给dialog
	divJq.dialog("options").buttons = oldButtons;
}

/** 为操作栏新增按钮操作,使用自己指定的弹出框Div对象 xiaqiang 2014.6.6 * */
function removeButtonWithDivJq(divJq, idOrNameOrIndex) {
	var oldButtons = divJq.dialog("options").buttons;

	if ($.type(idOrNameOrIndex) == 'number') {// 如果是数字,则是指代删除指定位置的按钮
		oldButtons.splice(idOrNameOrIndex - 1, 1);
	} else if ($.type(idOrNameOrIndex) == 'string') {// 如果是字符,则是指代删除指定名称的按钮(若同名只删除第一个,不过同一操作栏同名按钮的情况基本不可能出现)
		for (var i = 0; i < oldButtons.length; i++) {
			if (oldButtons[i].text == idOrNameOrIndex || oldButtons[i].id == idOrNameOrIndex) {
				oldButtons.splice(i, 1);
				break;
			}
		}
	}

	initButtonBar(divJq, oldButtons);

	// 重新将按钮赋值给dialog
	divJq.dialog("options").buttons = oldButtons;
}

/* 找到某jquery页面对象(只能是HtmlDom元素)所在的div */
function getD(jqObj) {
	return $(jqObj.closest(".window-body")[0]||jqObj.closest(".dialog-button").prev(".window-body")[0]);
}

/* 通过传入的json初始化渲染底部操作栏 */
function initButtonBar(divJq, buttonsJson) {
	// 获取弹出页下的底部操作栏
	var buttonBarDiv = $(".dialog-button", divJq);
	// 如果操作栏还不存在的话
	if (!buttonBarDiv[0]) {
		buttonBarDiv = $("<div class=\"dialog-button\"></div>").appendTo(divJq);
	}
	// 清空操作栏按钮内容(其他内容不清除)
	buttonBarDiv.empty();
	// 重新生成按钮
	for (var i = 0; i < buttonsJson.length; i++) {
		var buttons = buttonsJson[i];
		var button = $("<a href=\"javascript:void(0)\"></a>").appendTo(buttonBarDiv);
		if (buttons.handler) {
			button[0].onclick = buttons.handler;
		}
		button.linkbutton(buttons);
	}
}

/* 重新设置验证提示yitip的位置(比如窗口移动,收缩等事件时) */
function resetYitipPosition(divJq) {
	// 窗口移动时,提示框要跟着移动

	var validObj = $("[datatype]", divJq);
	var contentDivTop = $(divJq).offset().top;
	validObj.each(function(i, ele) {
		if ($(ele).attr("id") == 'sex') {
			console.info($(ele));
		}

		var validObjTop = $(ele).offset().top;
		var diff = contentDivTop - validObjTop;

		var api = $(ele).yitip("api");
		if (api) {
			// 是否已经产生错误的tip
			var isErrorTip = (api.$content.text().length > 0);
			// 如果是有错误yitip的才去判断
			if (isErrorTip) {
				if ($(ele).attr("id") == 'sex') {
					console.info(diff);
				}
				if (diff > 0) {
					api.$yitip.hide();
				} else {
					if ($(ele).attr("id") == 'sex') {
						console.info(api);
					}
					api.$yitip.show();
					api.setPosition("leftMiddle");
				}
			}

		}
	});
}

/* 传入一个按钮数组或单个按钮对象,然后通过向后台查询权限对增加的按钮进行权限过滤 */
function filterButtons(mybuttons) {
	var mybuttonsArray = [];
	var operationCodeArray = [];
	var operationCodeStr = "";
	if ($.isPlainObject(mybuttons)) { // 如果是单个按钮,则直接添加
		mybuttonsArray.push(mybuttons);
	} else if ($.isArray(mybuttons)) { // 如果是按钮数组,则分割开逐个添加
		mybuttonsArray = mybuttons;
	}

	// 首次过滤,过滤掉没写operationCode的按钮,并且组合出operationCode的数组准备传到后台
	mybuttonsArray = $.grep(mybuttonsArray, function(obj, i) {
		var operationCode = obj.operationCode;
		if (operationCode) {
			operationCodeArray.push(operationCode);
			return true;
		} else {// 如果operationCode不存在,则直接过滤不显示
			return false;
		}
	});
	// 将按钮code格式化成","隔开形式
	operationCodeStr = operationCodeArray.join(",");

	operationCodeStr = ajaxForOperationCode(operationCodeStr);
	if (operationCodeStr) {
		operationCodeArray = operationCodeStr.split(",");
	}

	// 第二次过滤,通过返回的有权限的按钮字符串进行迭代对比
	mybuttonsArray = $.grep(mybuttonsArray, function(obj, i) {
		// 是否保留
		var isHold = false;
		var operationCode = obj.operationCode;
		for (var i = 0; i < operationCodeArray.length; i++) {
			if (operationCodeArray[i] == operationCode) {
				isHold = true;
			}
		}
		return isHold;
	});

	return mybuttonsArray;
}

/* 传入一些按钮的code字符串组合,向后台请求,过滤出其中有权限的按钮 */
function ajaxForOperationCode(operationCodeStr) {
	var url = "commonController.do?ajaxForOperationCode";
	var operationCodes;
	$.ajax({
		async : false,
		data : {
			operationCode : operationCodeStr
		},
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
		},
		success : function(data) {
			var d = parseJSON(data);
			if (d.success) {
				operationCodes = d.attributes.operationCodes;
			}
		}
	});
	return operationCodes;
}

/* 验证可编辑表格的数据 */
function validateDatagrid(dgId) {
	var grid = $("#" + dgId);
	var rows = grid.datagrid('getRows');
	for (var i = 0; i < rows.length; i++) {
		if (!grid.datagrid('validateRow', i)) {
			tip('第' + (i + 1) + '行数据填写不正确');
			editIndex = i;
			return false;
		} else {
			grid.datagrid('endEdit', i);
		}
	}
	return true;
}

/* 得到可编辑表格的增删改数据 */
function getEditData(dgId) {
	var grid = $("#" + dgId);
	if (validateDatagrid(dgId)) {
		var insertedRows = JSON.stringify(grid.datagrid('getChanges', 'inserted'));
		var deletedRows = JSON.stringify(grid.datagrid('getChanges', 'deleted'));
		var updatedRows = JSON.stringify(grid.datagrid('getChanges', 'updated'));

		var obj = {
			insertedRows : insertedRows,
			deletedRows : deletedRows,
			updatedRows : updatedRows
		};
		return obj;
	}
	return null;
}

/* 获得父元素的宽度 */
function parentWidth(id) {
	return $("#" + id).parent().width();
}

/* 获得父元素的高度 */
function parentHeight(id) {
	return $("#" + id).parent().height();
}

/* 岗位选择自定义标签使用,进入选择页的js方法 */
function goJobSelectPage(ele) {
	var displayId = $(ele).attr("displayId"); // 显示的input框的id
	var displayName = $(ele).attr("displayName");// 显示的input框的name(不写的话,则默认为和id一样)
	var hiddenId = $(ele).attr("hiddenId"); // 隐藏的input框的id
	var hiddenName = $(ele).attr("hiddenName");// 隐藏的input框的name
	var displayValue = nulls($("#" + displayId).val());
	var hiddenValue = nulls($("#" + hiddenId).val());
	var multiples = $(ele).attr("multiples");// 是否多选
	var needBtnSelected = $(ele).attr("needBtnSelected");
	var needBtnSave = $(ele).attr("needBtnSave");
	var afterSaveClose = $(ele).attr("afterSaveClose");
	var saveUrl = $(ele).attr("saveUrl");
	var width = $(ele).attr("width");// 弹出框宽度
	var height = $(ele).attr("height");// 弹出框高度
	// 隐藏的input框的name(不写的话,则默认为和id一样)
	var url = "jobController.do?jobSelect&jobIds=" + hiddenValue + "&displayId=" + displayId + "&displayName=" + displayName + "&hiddenId=" + hiddenId
			+ "&hiddenName=" + hiddenName + "&multiples=" + multiples + "&needBtnSelected=" + needBtnSelected + "&needBtnSave=" + needBtnSave
			+ "&afterSaveClose=" + afterSaveClose + "&saveUrl=" + encodeURIComponent(saveUrl);
	createwindow("岗位选择", url, parseInt(width), parseInt(height), null);
}

/* 角色选择自定义标签使用,进入选择页的js方法 */
function goRoleSelectPage(ele) {
	var displayId = $(ele).attr("displayId"); // 显示的input框的id
	var displayName = $(ele).attr("displayName");// 显示的input框的name(不写的话,则默认为和id一样)
	var hiddenId = $(ele).attr("hiddenId"); // 隐藏的input框的id
	var hiddenName = $(ele).attr("hiddenName");// 隐藏的input框的name
	var displayValue = nulls($("#" + displayId).val());
	var hiddenValue = nulls($("#" + hiddenId).val());
	var multiples = $(ele).attr("multiples");// 是否多选
	var needBtnSelected = $(ele).attr("needBtnSelected");
	var needBtnSave = $(ele).attr("needBtnSave");
	var afterSaveClose = $(ele).attr("afterSaveClose");
	var saveUrl = $(ele).attr("saveUrl");
	var width = $(ele).attr("width");// 弹出框宽度
	var height = $(ele).attr("height");// 弹出框高度
	// 隐藏的input框的name(不写的话,则默认为和id一样)
	var url = "roleController.do?roleSelect&roleIds=" + hiddenValue + "&displayId=" + displayId + "&displayName=" + displayName + "&hiddenId=" + hiddenId
			+ "&hiddenName=" + hiddenName + "&multiples=" + multiples + "&needBtnSelected=" + needBtnSelected + "&needBtnSave=" + needBtnSave
			+ "&afterSaveClose=" + afterSaveClose + "&saveUrl=" + encodeURIComponent(saveUrl);
	createwindow("角色选择", url, parseInt(width), parseInt(height), null);
}

/* 分配角色附带保存反显页面使用,进入选择页的js方法 */
function goRoleSavePage(jsonParams) {
	if (jsonParams) {
		var ids = nulls(jsonParams.ids);
		var title = jsonParams.title ? jsonParams.title : "角色选择";
		var needBtnSave = jsonParams.needBtnSave ? jsonParams.needBtnSave : true;
		var afterSaveClose = jsonParams.afterSaveClose ? jsonParams.afterSaveClose : false;
		var saveUrl = jsonParams.saveUrl;
		var width = jsonParams.width ? jsonParams.width : 790;
		var height = jsonParams.height ? jsonParams.height : 480;
		var multiples = jsonParams.multiples ? jsonParams.multiples : true;
		var url = "roleController.do?roleSelect&roleIds=" + ids + "&needBtnSave=" + needBtnSave + "&afterSaveClose=" + afterSaveClose + "&multiples="
				+ multiples + "&saveUrl=" + encodeURIComponent(saveUrl);
		createwindow(title, url, parseInt(width), parseInt(height), null);
	}
}

/* 员工选择自定义标签使用,进入选择页的js方法 */
function goUserSelectPage(ele) {
	var displayId = $(ele).attr("displayId"); // 显示的input框的id
	var displayName = $(ele).attr("displayName");// 显示的input框的name(不写的话,则默认为和id一样)
	var hiddenId = $(ele).attr("hiddenId"); // 隐藏的input框的id
	var hiddenName = $(ele).attr("hiddenName");// 隐藏的input框的name
	var displayValue = nulls($("#" + displayId).val());
	var hiddenValue = nulls($("#" + hiddenId).val());
	var multiples = $(ele).attr("multiples");// 是否多选
	var width = $(ele).attr("width");// 弹出框宽度
	var height = $(ele).attr("height");// 弹出框高度

	var treeUrl = $(ele).attr("treeUrl");
	var gridUrl = $(ele).attr("gridUrl");
	var aysn = $(ele).attr("aysn");
	var onlyAuthority = $(ele).attr("onlyAuthority");
	var orgCode = $(ele).attr("orgCode");
	var containSelf = $(ele).attr("containSelf");
	var expandAll = $(ele).attr("expandAll");
	var needBtnSelected = $(ele).attr("needBtnSelected");
	var needBtnSave = $(ele).attr("needBtnSave");
	var afterSaveClose = $(ele).attr("afterSaveClose");
	var saveUrl = $(ele).attr("saveUrl");
	var empOrUser = $(ele).attr("empOrUser");
	var callback = nulls($(ele).attr("callback"));

	var url = "userController.do?userSelect&userIds=" + hiddenValue + "&displayId=" + displayId + "&displayName=" + displayName + "&hiddenId=" + hiddenId
			+ "&hiddenName=" + hiddenName + "&multiples=" + multiples + "&treeUrl=" + treeUrl + "&gridUrl=" + gridUrl + "&aysn=" + aysn + "&onlyAuthority="
			+ onlyAuthority + "&orgCode=" + orgCode + "&containSelf=" + containSelf + "&expandAll=" + expandAll + "&needBtnSelected=" + needBtnSelected
			+ "&needBtnSave=" + needBtnSave + "&afterSaveClose=" + afterSaveClose + "&saveUrl=" + encodeURIComponent(saveUrl) + "&empOrUser=" + empOrUser
			+ "&callback=" + callback;
	createwindow("人员选择", url, parseInt(width), parseInt(height), null);
}

/* 分配人员附带保存反显页面使用,进入选择页的js方法 */
function goUserSavePage(jsonParams) {
	if (jsonParams) {
		var ids = jsonParams.ids;
		var title = jsonParams.title ? jsonParams.title : "人员选择";
		var needBtnSave = jsonParams.needBtnSave ? jsonParams.needBtnSave : true;
		var afterSaveClose = jsonParams.afterSaveClose ? jsonParams.afterSaveClose : true;
		var saveUrl = jsonParams.saveUrl;
		var width = jsonParams.width ? jsonParams.width : 960;
		var height = jsonParams.height ? jsonParams.height : 480;
		var multiples = jsonParams.multiples ? jsonParams.multiples : true;
		var treeUrl = jsonParams.treeUrl ? jsonParams.treeUrl : "orgnaizationController.do?orgSelectTagTree";
		var gridUrl = jsonParams.gridUrl ? jsonParams.gridUrl : "userController.do?datagrid4OrgMulSelectTag";
		var orgCode = jsonParams.orgCode ? jsonParams.orgCode : "rootOrg";
		var callback = jsonParams.callback;
		var url = "userController.do?userSelect&userIds=" + ids + "&needBtnSave=" + needBtnSave + "&afterSaveClose=" + afterSaveClose + "&multiples="
				+ multiples + "&treeUrl=" + treeUrl + "&gridUrl=" + gridUrl + "&orgCode=" + orgCode + "&callback=" + callback + "&saveUrl="
				+ encodeURIComponent(saveUrl);
		createwindow(title, url, parseInt(width), parseInt(height), null);
	}
}

/* 组织多类型选择自定义标签使用,进入选择页的js方法 */
function goOrgMulSelectPage(ele) {
	var displayId = $(ele).attr("displayId"); // 显示的input框的id
	var displayName = $(ele).attr("displayName");// 显示的input框的name(不写的话,则默认为和id一样)
	var hiddenId = $(ele).attr("hiddenId"); // 隐藏的input框的id
	var hiddenName = $(ele).attr("hiddenName");// 隐藏的input框的name
	var displayValue = nullsReplace($(ele).attr("displayValue"),nulls($("#" + displayId).val()));
	var hiddenValue = nullsReplace($(ele).attr("hiddenValue"),nulls($("#" + hiddenId).val()));
	var multiples = $(ele).attr("multiples");// 是否多选

	var treeUrl = $(ele).attr("treeUrl");
	var gridUrl = $(ele).attr("gridUrl");
	var aysn = $(ele).attr("aysn");
	var onlyAuthority = $(ele).attr("onlyAuthority");
	var orgCode = nulls($(ele).attr("orgCode"));
	var orgId = nulls($(ele).attr("orgId"));
	var containSelf = $(ele).attr("containSelf");
	var expandAll = $(ele).attr("expandAll");
	var needOrg = $(ele).attr("needOrg");
	var needRole = $(ele).attr("needRole");
	var needJob = $(ele).attr("needJob");
	var roleListUrl = $(ele).attr("roleListUrl");
	var jobListUrl = $(ele).attr("jobListUrl");
	var needBtnSelected = $(ele).attr("needBtnSelected");
	var needBtnSave = $(ele).attr("needBtnSave");
	var afterSaveClose = $(ele).attr("afterSaveClose");
	var saveUrl = $(ele).attr("saveUrl");
	var empOrUser = $(ele).attr("empOrUser");
	var callback = nulls($(ele).attr("callback"));
	var url = "userController.do?orgMulSelect&hiddenValue=" + hiddenValue + "&displayId=" + displayId + "&displayName=" + displayName + "&hiddenId=" + hiddenId
			+ "&hiddenName=" + hiddenName + "&multiples=" + multiples + "&treeUrl=" + treeUrl + "&gridUrl=" + gridUrl + "&aysn=" + aysn + "&onlyAuthority="
			+ onlyAuthority + "&orgId=" + orgId + "&orgCode=" + orgCode + "&containSelf=" + containSelf + "&expandAll=" + expandAll + "&needOrg=" + needOrg + "&needRole="
			+ needRole + "&needJob=" + needJob + "&roleListUrl=" + roleListUrl + "&jobListUrl=" + jobListUrl + "&needBtnSelected=" + needBtnSelected
			+ "&needBtnSave=" + needBtnSave + "&afterSaveClose=" + afterSaveClose + "&saveUrl=" + encodeURIComponent(saveUrl) + "&empOrUser=" + empOrUser
			+ "&callback=" + callback;
	createwindow("组织多类型选择", url, 980, 460, null);
}

/* 通用弹出选择自定义标签使用,进入选择页的js方法 */
function goCommonSelectPage(ele) {
	var displayId = $(ele).attr("displayId"); // 显示的input框的id
	var displayName = $(ele).attr("displayName");// 显示的input框的name(不写的话,则默认为和id一样)
	var hiddenId = $(ele).attr("hiddenId"); // 隐藏的input框的id
	var hiddenName = $(ele).attr("hiddenName");// 隐藏的input框的name
	var displayValue = nulls($("#" + displayId).val());
	var hiddenValue = nulls($("#" + hiddenId).val());
	var multiples = $(ele).attr("multiples");// 是否多选
	var title = nullsReplace($(ele).attr("title"), "弹出页选择");// 弹出框标题,使用了扩展的nullsReplace方法

	var url1 = $(ele).attr("url");
	var gridFieldsJson = nulls($(ele).attr("gridFieldsJson"));
	var idOrName = nulls($(ele).attr("idOrName"));
	var width = parseInt($(ele).attr("width"));
	var height = parseInt($(ele).attr("height"));

	var hasTree = $(ele).attr("hasTree");
	var expandAll = $(ele).attr("expandAll");
	var treeUrl = $(ele).attr("treeUrl");
	var gridTreeFilter = $(ele).attr("gridTreeFilter");
	var callback = nulls($(ele).attr("callback"));
	// 隐藏的input框的name(不写的话,则默认为和id一样)
	var url = "commonController.do?commonSelect&hiddenValue=" + hiddenValue + "&displayValue=" + displayValue + "&displayId=" + displayId + "&displayName="
			+ displayName + "&hiddenId=" + hiddenId + "&hiddenName=" + hiddenName + "&multiples=" + multiples + "&url=" + url1 + "&gridFieldsJson="
			+ gridFieldsJson + "&hasTree=" + hasTree + "&expandAll=" + expandAll + "&treeUrl=" + treeUrl + "&gridTreeFilter=" + gridTreeFilter + "&callback="
			+ callback + "&idOrName=" + idOrName;
	createwindow(title, encodeURI(encodeURI(url)), width, height, null);
}

/* 可编辑表格中通用弹出选择使用,进入选择页的js方法 */
function goEditorCommonSelectPage(ele) {
	var displayField = $(ele).attr("displayField"); // 显示的input框的id
	var hiddenField = $(ele).attr("hiddenField");// 隐藏的input框的name
	var multiples = $(ele).attr("multiples");// 是否多选
	var index = $(ele).attr("index");
	var gridId = $(ele).attr("gridId");
	var gridUrl = $(ele).attr("gridUrl");
	var gridFieldsJson = $(ele).attr("gridFieldsJson");
	var width = parseInt($(ele).attr("width"));
	var height = parseInt($(ele).attr("height"));

	var hasTree = $(ele).attr("hasTree");
	var expandAll = $(ele).attr("expandAll");
	var treeUrl = $(ele).attr("treeUrl");
	var gridTreeFilter = $(ele).attr("gridTreeFilter");
	var callback = nulls($(ele).attr("callback"));
	var displayValue = nulls($("#" + gridId).datagrid("getEditorFieldValue", {
		index : index,
		field : displayField
	}));
	var hiddenValue = nulls($("#" + gridId).datagrid("getEditorFieldValue", {
		index : index,
		field : hiddenField
	}));
	// 隐藏的input框的name(不写的话,则默认为和id一样)
	var url = "commonController.do?editorCommonSelect&hiddenValue=" + hiddenValue + "&displayValue=" + displayValue + "&displayField=" + displayField
			+ "&hiddenField=" + hiddenField + "&multiples=" + multiples + "&url=" + gridUrl + "&gridFieldsJson=" + gridFieldsJson + "&hasTree=" + hasTree
			+ "&expandAll=" + expandAll + "&treeUrl=" + treeUrl + "&gridTreeFilter=" + gridTreeFilter + "&gridId=" + gridId + "&index=" + index + "&callback="
			+ callback;
	;
	createwindow("弹出页选择", encodeURI(encodeURI(url)), width, height, null);
}

function commonPageUpload(jsonParam) {
	var fullUrl = "";
	var url = "";
	var urlParam = "";
	var title = "";
	var width;
	var height;
	var exParams;
	/** 处理上传参数 * */
	if (jsonParam) {
		if (!jsonParam.businessKey) {
			if (!jsonParam.noBusinessKey) {
				tip("请您保存记录后,再进行文件上传");
				return;
			}
		}

		if (jsonParam.title) {
			title = jsonParam.title;
		} else {
			title = "上传";
		}

		if (jsonParam.url) {
			url = jsonParam.url;
		} else {
			url = "attachController.do?commonAttachUpload";
		}

		if (jsonParam.width) {
			width = jsonParam.width;
		} else {
			width = 780;
		}

		if (jsonParam.height) {
			height = jsonParam.height;
		} else {
			height = 500;
		}
		if (jsonParam.exParams) {
			exParams = jsonParam.exParams;
			delete jsonParam.exParams;
			url += "&exParams=" + JSON.stringify(exParams);
		}
		urlParam = obj2url(jsonParam);
	}
	fullUrl = url + urlParam;
	createwindow(title, fullUrl, width, height, null, null);
}

function commonUploadAndView(jsonParam) {
	var fullUrl = "";
	var url = "";
	var urlParam = "";
	var title = "";
	var width;
	var height;
	var exParams;
	/** 处理上传参数 * */
	if (jsonParam) {
		if (!jsonParam.businessKey) {
			if (!jsonParam.noBusinessKey) {
				tip("请您保存记录后,再进行文件上传");
				return;
			}
		}

		if (jsonParam.title) {
			title = jsonParam.title;
		} else {
			title = "附件上传";
		}

		if (jsonParam.url) {
			url = jsonParam.url;
		} else {
			url = "attachController.do?commonUploadAndView";
		}

		if (jsonParam.width) {
			width = jsonParam.width;
		} else {
			width = 830;
		}

		if (jsonParam.height) {
			height = jsonParam.height;
		} else {
			height = 500;
		}
		if (jsonParam.exParams) {
			exParams = jsonParam.exParams;
			delete jsonParam.exParams;
			url += "&exParams=" + JSON.stringify(exParams);
		}
		urlParam = obj2url(jsonParam);
	}
	fullUrl = url + urlParam;
	createwindow(title, fullUrl, width, height, null, null);
}

// 脚本选择页弹出框
function openScriptDialog(id) {
	var url = "sysScriptController.do?scriptSelect";
	createwindow("脚本选择", url, 560, 553, null, {
		buttons : [ {
			text : '验证表达式',
			iconCls : 'awsm-icon-legal',
			handler : function() {
				validExpression();
			}
		}, {
			text : '选择脚本',
			iconCls : 'awsm-icon-ok-sign',
			handler : function() {
				var scriptContent = InitMirror.getValue("script_result");
				InitMirror.getEditor(id).focus();
				InitMirror.insertValue(id, scriptContent);
				closeD(getD($(this)));
			}
		} ]
	});
}

/* 下载方法(用附件id) */
function common_downloadFile(id) {
	var url = "attachController.do?downloadFile&aId=" + id;
	var elemIF = $("<iframe class='downloadIframe'/>");
	elemIF.attr("src", url);
	elemIF.hide();
	elemIF.appendTo(document.body);

	setTimeout("removeIframe()", 500);
}
/* 下载方法(用附件完整url) */
function common_downloadFileByUrl(url) {
	var elemIF = $("<iframe class='downloadIframe'/>");
	elemIF.attr("src", url);
	elemIF.hide();
	elemIF.appendTo(document.body);

	setTimeout("removeIframe()", 500);
}
function removeIframe() {
	$(".downloadIframe").remove();
}

// pdf下载方法
function downloadPdf(id, type) {
	location.href = "pdfController.do?genPdf&type=" + type + "&id=" + id;
}

// 在线显示PDF
function pageShowPdf(id, type) {
	/*
	 * window.open('pdfController.do?showPdf&id='+id+'&type='+type,'_blank','screenX=100,screenY=100,left=100,top=100
	 * ,toolbar=no,menubar=no,location=no, status=no,resizable=yes');
	 */
	var iWidth = 800; // 弹出窗口的宽度;
	var iHeight = 600; // 弹出窗口的高度;
	var iTop = (window.screen.availHeight - 30 - iHeight) / 2; // 获得窗口的垂直位置;
	var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; // 获得窗口的水平位置;
	window.open('pdfController.do?showPdf&id=' + id + '&type=' + type, '', "height=" + iHeight + ", width=" + iWidth + ", top=" + iTop + ", left=" + iLeft
			+ ",resizable=yes");
}

/* 消息打印 */
function winPrint(id) {
	createwindow("打印", "messageController.do?getPringHtml&id=" + id, 800, 550, null, {
		optFlag : 'detail',
		isIframe : true,
		dialogID : 'printDialog',
		onloadWin : "ifPrint()"
	});
}
function ifPrint() {
	// console.info(document.getElementById('printDialog_iframe').contentWindow.document.body);
	if ((navigator.userAgent.indexOf('MSIE') >= 0 || navigator.userAgent.indexOf('Trident') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {// ie
		window.frames['printDialog_iframe'].focus();
		window.frames['printDialog_iframe'].print();
	} else if (navigator.userAgent.indexOf('Firefox') >= 0 || navigator.userAgent.indexOf('Chrome') >= 0) {
		document.getElementById('printDialog_iframe').contentWindow.print();
	} else {
		$.messager.alert("提示信息", "该功能只支持IE,firefox,Chrome浏览器");
	}
}

// office word查看、打印
function printFile(id, name) {
	createwindow("打印", "messageController.do?printFile&id=" + id + "&name=" + name, 900, 550, null, {
		optFlag : 'detail',
		isIframe : true,
		dialogID : 'officePrintDialog'
	});
}
// 通用查看附件详细
function common_viewFile(id, ext) {
	createwindow("查看", "attachController.do?viewDataEdit&id=" + id + "&ext=" + ext, "100%", "100%", null, {
		optFlag : 'detail',
		isIframe : true,
		noheader : true
	});
}

// 通用附件修改
function common_updateFile(id) {
	createwindow("修改", "attachController.do?updateDataEdit&id=" + id, 400, 400, null, {
		optFlag : 'update'
	});
}

// 通用文件名格式化为 图标+文件名 的方法
function onlyFileNameFormatter(value, rec, index) {
	return "<span><i class='" + getFileIcon(rec.ext) + " bigger-120'></i>  " + value + "</span>";
}

// 删除文件方法
function deleteFile(id) {
	$.messager.confirm("确认信息", "您确定删除该文件？", function(r) {
		if (r) {
			$.ajax({
				url : "attachController.do?deleteFile",
				type : 'post',
				data : {
					aId : id
				},
				cache : false,
				success : function(data) {
					var d = parseJSON(data);
					if (d.success) {
						$("#" + id).remove();
						tip(d.msg);
					}
				}
			});
		}
	});
}

// 公共提示tip(默认在元素上移入移出提示)
function yitip(jqSelector, content, exParams) {
	var defaultOptions = {
		"showEvent" : "mouseenter",
		"hideEvent" : "mouseleave",
		"position" : "topMiddle",
		"color" : "black",
		"content" : content
	};
	// 合并默认配置和用户自定义配置json,遇到相同配置,后者会覆盖前者
	var options = $.extend({}, defaultOptions, exParams);
	var api = jqSelector.yitip(options);
	return api;
}

function commonImportExcel(submitUrl, importParams) {
	var url = "commonController.do?goImportExcel" + importParams;
	createwindow("Excel数据导入", url, 900, 625, null, {
		buttons : [ {
			id : "confirmImport",
			text : '确认导入',
			iconCls : 'awsm-icon-save',
			handler : function() {
				var errorCount = $("#errorCount").val();
				var msg = (errorCount == "0" ? "确认导入数据?" : "本次导入有" + errorCount + "条错误数据,确认导入?");
				$.messager.confirm("提示", msg, function(r) {
					if (r) {
						$.ajax({
							aysn : false,
							url : submitUrl,
							type : 'post',
							dataType : 'json',
							cache : false,
							error : function(data) {
								$("#confirmImport", getD($("#form_excelImport"))).linkbutton("disable");
								tip("导入数据过程中出错,请联系管理员!");
							},
							success : function(data) {
								$("#confirmImport", getD($("#form_excelImport"))).linkbutton("disable");
								if (data.success) {
									tip(data.msg);
								}
							}
						});
					}
				});
			}
		}, {
			text : '关闭',
			iconCls : 'awsm-icon-remove',
			handler : function() {
				closeD($(this).closest(".window-body"));
			}
		} ]
	});
}

// 页面只读方法
function pageReadonly(jq) {
	// $(":input",jq).not("input[type=text]").attr("disabled", "disabled");
	// $(":input",jq).filter("input[type=text]").attr("readonly", "readonly");
	$(":input", jq).not(":hidden").attr("disabled", "disabled");// 所有Html输入型元素
	$("span.combo-arrow", jq).off("click").off("mouseenter").off("mouseleave"); // easyui
	// combo相关禁用
	$("span.spinner-arrow", jq).children().off("click").off("mouseenter").off("mouseleave");// easyui
	// spinner相关禁用
}

//页面只读方法
function formPageReadonly(ele,cType) {
	$this=$(ele);
	if(cType){
		if(cType=="file"){
			//文件类型
			$("[flag=addAttachBtn],[type=file],.btn-del",$this).remove();
			$this.addClass("disabled");
		}else if(cType=="detail"){
			//明细类型
			
			//明细需要移除增加按钮、删除按钮
			$(".detail-bottom,.detail-top i.delete",$this).remove();
			$this.addClass("disabled");
		}else{
			if(cType=="date"||cType=="time"||cType=="radio"||cType=="checkboxes"){
				$("input,select", $this).mobiscroll("disable");
			}
			$(":input", $this).attr("disabled", "disabled").attr("placeholder","");
			//增加整体灰色样式
			$this.addClass("disabled");
			//去除多选、单选、日期类型的箭头
			$(".fa-angle-right",$this).remove()
		}
	}else{
		//明细类型
		
		//明细需要移除增加按钮、删除按钮
		$(".detail-bottom,.detail-top i.delete",$this).remove();
		$this.addClass("disabled");
	}
}

var selectMulType = {
	//打开选人弹框统一处理
	getSelectedData:function(){
		var finalValue = [];
		var selectingUserLis=$("li",$("#selectedUL"));
		if(selectingUserLis.length>0){
			$.each(selectingUserLis, function(i, ele) {
				var mulResult=$(ele).data();
				finalValue[i]=mulResult;
			});
		}
		return finalValue;
	},
    goSelectPage: function(options){
    	var defaultOptions={
    		multiple:true,
    		needArrow:false,
    		repeatSelect:false,
    		containsSelf:true,
    		selectOrg:false,
    		showOrgPerson:true
    	};
    	var options = $.extend({}, defaultOptions, options);
    	var _this = this;
    	var $modal = $("#selectMulTypeModal"),
			$modalTitle = $modal.find(".modal-title"),
			$modalBody = $modal.find(".modal-body"),
			$userList = $(".person-list.approval-flow > ul");
    		$selectedUlList = $("#selectedUser");
		
    	$modalBody.empty();
    	$modalBody.load("userController.do?goSelectMulType&options="+JSON.stringify(options));
    	$modal.modal("show");
    	$modalTitle.text(options.title);
    	
    	
    	if(options.callbackKey){
    		$("#selectMulTypeConfirm",$modal).off().on("click", function(){
    			var callbackResult=new Object();
    	    	callbackResult.selected=_this.getSelectedData();
    	    	
        		callbackFunction(options.callbackKey, callbackResult.selected);
            });
    	}
    },
    show:function(){
    	$("#selectMulTypeModal").modal("show");
    },
    hide:function(){
    	$("#selectMulTypeModal").modal("hide");
    },
    //根据不同的type返回不同的图标
    getKindIcon: function(type){
    	var rtn = "";
    	switch(type){
    		case "org":
    			return "images/organization/organization.png";
    			break;
    		case "role":
    			return "images/organization/iconfont-group.png";
    			break;
    		default:
    			return "";
    	}
    }
}
