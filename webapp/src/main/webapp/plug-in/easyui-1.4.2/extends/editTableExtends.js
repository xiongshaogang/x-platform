$.extend($.fn.datagrid.methods, {
	editCell : function(jq, param) {
		return jq.each(function() {
			if (param.field == "ck" || param.field == "opt") {
				return;
			}
			var opts = $(this).datagrid('options');
			var fields = $(this).datagrid('getColumnFields', true).concat($(this).datagrid('getColumnFields'));
			fields = $.grep(fields, function(obj, i) {
				if (obj == "ck") {
					return false;
				}
				return true;
			});
			for (var i = 0; i < fields.length; i++) {
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor1 = col.editor;
				// 先把不是当前点击的列的Editor置为空,那么其他的列就呈现只读状态
				if (fields[i] != param.field) {
					col.editor = null;
				}
			}

			$(this).datagrid('beginEdit', param.index);
			// 将其他编辑类型回复option中的类型
			for (var i = 0; i < fields.length; i++) {
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
		});
	},

	/**
	 * @fun:  返回某单元格数据
	 * @case: 调用方式 $("#abc").datagrid("getFieldValue",{index:1,field:"name"});
	*/
	getFieldValue : function(jq, param) {
		return jq.eq(0).datagrid("getRows")[param.index][param.field];
	},
	/**
	 * @fun:  返回某单元格正在编辑的数据(还没有关闭编辑状态)
	 * @case: 调用方式 $("#abc").datagrid("getEditorFieldValue",{index:1,field:"name"});
	*/
	getEditorFieldValue : function(jq, param) {
		var editor = jq.eq(0).datagrid("getEditor", {
			index : param.index,
			field : param.field
		});
		return editor.actions.getValue(editor.target);
	},
	/**
	 * 
	 * @fun:  设置某单元格数据
	 * @case: 调用方式 $("#abc").datagrid("setFieldValue",{index:1,field:"name",value:"单元格的值"});
	*/
	setFieldValue : function(jq, param) {
		jq.eq(0).datagrid("getRows")[param.index][param.field] = param.value;
	},
	/**
	 * 
	 * @fun:  设置编辑状态某单元格数据
	 * @case: 调用方式 $("#abc").datagrid("setEditorFieldValue",{index:1,field:"name",value:"单元格的值"});
	 */
	setEditorFieldValue : function(jq, param) {
		var editor = jq.eq(0).datagrid("getEditor", {
			index : param.index,
			field : param.field
		});
		editor.actions.setValue(editor.target, param.value);
	},
	/**
	 * 
	 * @fun:  清除编辑状态某单元格数据
	 * @case: 调用方式 $("#abc").datagrid("clearEditorFieldValue",{index:1,field:"name"});
	 */
	clearEditorFieldValue : function(jq, param) {
		var editor = jq.eq(0).datagrid("getEditor", {
			index : param.index,
			field : param.field
		});
		editor.actions.setValue(editor.target, "");
	},
	/**
	 * 
	 * @fun:  禁用单元格控件
	 * @case: 调用方式 $("#abc").datagrid("disabledEditor",{index:1,field:"name"});
	 */
	disabledEditor : function(jq, param) {
		var editor = jq.eq(0).datagrid("getEditor", {
			index : param.index,
			field : param.field
		});
		editor.target.combobox("disable");
	},
	/**
	 * 
	 * @fun:  启用单元格控件
	 * @case: 调用方式 $("#abc").datagrid("enableEditor",{index:1,field:"name"});
	 */
	enableEditor : function(jq, param) {
		var editor = jq.eq(0).datagrid("getEditor", {
			index : param.index,
			field : param.field
		});
		editor.target.combobox("enable");
	},
	/**
	 * 
	 * @fun:  改变某列的可编辑的属性(正在编辑的行,无法改变对应属性)
	 * @case: 调用方式 $("#fieldList").datagrid("changeEditorOption",{field:"isshow",attr:"url",value:"abc.do&aaa=123"});
	 */
	changeEditorOption : function(jq, param) {
		jq.eq(0).datagrid("getColumnOption", param.field).editor.options[param.attr] = param.value;
	},
	/**
	 * @fun:  返回某行数据
	 * @case: 调用方式 $("#abc").datagrid("getRow",1);
	*/
	getRow : function(jq, param) {
		return jq.eq(0).datagrid("getRows")[param];
	}
});

$.extend($.fn.datagrid.defaults.editors, {
	datetimebox : {
		init : function(container, options) {
			var box = $('<input />').appendTo(container);
			box.datetimebox(options);
			return box;
		},
		getValue : function(target) {
			return $(target).datetimebox('getValue');
		},
		setValue : function(target, value) {
			$(target).datetimebox('setValue', value);
		},
		resize : function(target, width) {
			var box = $(target);
			box.datetimebox('resize', width);
		},
		destroy : function(target) {
			$(target).datetimebox('destroy');
		}
	},
	commonselect : {
		init : function(container, options) {
			/** 获取属性 **/
			var displayField = nulls(options.displayField);
			var hiddenField = nulls(options.hiddenField);
			var multiples = options.multiples == undefined ? true : options.multiples;
			var gridUrl = nulls(options.gridUrl);
			var gridFieldsJson = nulls(JSON.stringify(options.gridFieldsJson));
			var width = options.width == undefined ? 700 : options.width;
			var height = options.height == undefined ? 460 : options.height;
			var buttonColorClass = options.buttonColorClass == undefined ? "btn-info" : options.buttonColorClass;
			var icon = options.icon == undefined ? "awsm-icon-indent-right" : options.icon;
			var hasTree = options.hasTree == undefined ? false : options.hasTree;
			var expandAll = options.expandAll == undefined ? true : options.expandAll;
			var treeUrl = nulls(options.treeUrl);
			var gridTreeFilter = nulls(options.gridTreeFilter);
			/** 获取操作对象 **/
			// 获取本行tr对象
			var tr = container.closest("tr[datagrid-row-index]");
			// 获取产生Datagird的table对象id
			var gridId = getTableIdByTarget(container);
			// 本行行号
			var index = getIndexByTarget(container);
			// 产生组件span
			var span = $('<span class="datagrid-editor-span datagrid-editor-span-' + index + '"></span>').appendTo(
					container);
			// 产生输入框
			var input = $('<input class="datagrid-editable-select-input" readonly="readonly"/>');
			// 产生按钮
			var button = $('<span><button type="button" class="btn btn-info btn-pageselect" index="' + index
					+ '" gridId="' + gridId + '" displayField="' + displayField + '" hiddenField="' + hiddenField
					+ '" multiples="' + multiples + '" gridUrl="' + gridUrl + '" gridFieldsJson=\'' + gridFieldsJson
					+ '\' width="' + width + '" height="' + height + '" hasTree="' + hasTree + '" expandAll="'
					+ expandAll + '" treeUrl="' + treeUrl + '" gridTreeFilter="' + gridTreeFilter
					+ '" onclick="goEditorCommonSelectPage(this)" ><i class="' + icon
					+ ' bigger-110 icon-only"></i></button></span>');

			span.wrapInner(input);
			input.after(button);
			return input;
		},
		getValue : function(target) {
			return $(target).val();
		},
		setValue : function(target, value) {
			$(target).val(value);
		},
		resize : function(target, width) {
			// 不要撑满td,和其他的input留出空隙
			width = width - 2;
			var input = $(target);
			var span = input.closest("span[class^=datagrid-editor-span]");
			var button = span.find("button.btn-pageselect");
			var btnWidth = button.outerWidth();
			var spanWidth;
			var inputWidth;
			if ($.boxModel == true) {
				spanWidth = width - (span.outerWidth() - span.width());
				inputWidth = spanWidth - btnWidth;
			} else {
				spanWidth = width;
				inputWidth = spanWidth - btnWidth;
			}
			input.width(inputWidth);
			span.width(spanWidth);
		},
		destroy : function(target) {
			var input = $(target);
			// 清空span的父td
			var td = input.closest("td");
			td.empty();
		}
	}
});

// 通过editor的target(或者说是输入的html控件)/或者是init中的container(输入控件所在td)获得所在行序列,返回的为整形
function getIndexByTarget(target) {
	return parseInt($(target).closest("tr[datagrid-row-index]").attr("datagrid-row-index"));
}

// 通过editor的target(或者说是输入的html控件)/或者是init中的container(输入控件所在td)获得所在Datagrid的id
function getTableIdByTarget(target) {
	var gridTable = $(target).closest("div.datagrid-view").children("table:hidden");
	var gridId = gridTable.attr("id");
	return gridId;
}

// 通过editor的target(或者说是真实输入的html控件)获得所在列名
function getFieldByTarget(target) {
	return $(target).closest("td[field]").attr("field");
}

// 通过gridId,行索引,字段名禁用某个单元格编辑框(限combobox类型)
function disabledEditor(gridId, index, field) {
	$("#" + gridId).datagrid("disabledEditor", {
		index : index,
		field : field
	});
}

// 行编辑级联使用方法,用于onBeforeLoad
function editorCascadeBeforeLoad(param, self, cascadeIndex, fieldStr, paramName) {
	var fieldArray = fieldStr.split(",");
	if (paramName == undefined) {
		paramName = "id";
	}
	var index = getIndexByTarget(self);
	var gridId = getTableIdByTarget(self);
	var field = getFieldByTarget(self);
	var finalValue = "";
	// 获得父级的值
	var editorFieldValue = $("#" + gridId).datagrid("getEditorFieldValue", {
		index : index,
		field : fieldArray[cascadeIndex - 2]
	});
	var fieldValue = $("#" + gridId).datagrid("getFieldValue", {
		index : index,
		field : fieldArray[cascadeIndex - 2]
	});
	// 筛选父级的值(假如父级有数据,如果刚开启行编辑,editor是没有值的,不过能通过取rows的数据去拿到数据;继续编辑的话,则得取editor中实时的值了
	if (editorFieldValue == "" || editorFieldValue == undefined) {
		finalValue = fieldValue;
	} else {
		finalValue = editorFieldValue;
	}

	if (finalValue == "") {// 如果父级没有值,则不请求,并且禁用自己
		var fun = "disabledEditor('" + gridId + "'," + index + ",'" + field + "')";
		// 禁用方法disabledEditor不能直接调用,因为在onBeforeLoad调用的阶段,combobox还没出现呢,所以需要setTimeout延迟去调用禁用方法,延迟0秒一样有效
		setTimeout(fun, 0);
		return false;
	} else {// 如果父级有值,则将父内容作为参数
		param[paramName] = finalValue;
	}
}
// 行编辑级联使用方法,用于onSelect
function editorCascadeSelect(record, self, cascadeIndex, fieldStr) {
	var fieldArray = fieldStr.split(",");
	var index = getIndexByTarget(self);
	var gridId = getTableIdByTarget(self);
	// 清空所有子孙的值
	for (var i = cascadeIndex; i < fieldArray.length; i++) {
		$("#" + gridId).datagrid("clearEditorFieldValue", {
			index : index,
			field : fieldArray[i]
		});
	}
	// 启用子控件
	$("#" + gridId).datagrid("enableEditor", {
		index : index,
		field : fieldArray[cascadeIndex]
	});
	// 禁用孙控件
	for (var i = cascadeIndex; i + 1 < fieldArray.length; i++) {
		$("#" + gridId).datagrid("disabledEditor", {
			index : index,
			field : fieldArray[i + 1]
		});
	}
	// 刷新子控件url,因为有onBeforeLoad方法使用了闭包,所以url也直接是动态的,直接reload
	$("#" + gridId).datagrid("getEditor", {
		index : index,
		field : fieldArray[cascadeIndex]
	}).target.combobox("reload");
}


$.extend($.fn.datagrid.methods, {
    statistics: function (jq) {
        var opt = $(jq).datagrid('options').columns;
        var rows = $(jq).datagrid("getRows");
         
        var footer = new Array();
        footer['sum'] = "";
        footer['avg'] = "";
        footer['max'] = "";
        footer['min'] = "";
        //处理需要统计列的数据
        sisFooterData(opt,footer);
 
        var frozenColumns = $(jq).datagrid('options').frozenColumns;
        var objfield = opt[0][0].field;
        if(frozenColumns.length != 0){
            objfield = frozenColumns[0][0].field;
            //处理冻结列中需要统计列的数据
            sisFooterData(opt,footer);
        }
        var footerObj = new Array();
         
        if(footer['sum'] != ""){
            var tmp = '{' + footer['sum'].substring(0,footer['sum'].length - 1) + "}";
            var obj = eval('(' + tmp + ')');
            if(obj[objfield] == undefined){
                footer['sum'] += '"' + objfield + '":"<b>合计:</b>"';
                obj = eval('({' + footer['sum'] + '})');
            }else{
                obj[objfield] = "<b>合计:</b>" + obj[objfield];
            }
            footerObj.push(obj);
        }
         
        if(footer['avg'] != ""){
            var tmp = '{' + footer['avg'].substring(0,footer['avg'].length - 1) + "}";
            var obj = eval('(' + tmp + ')');
            if(obj[objfield] == undefined){
                footer['avg'] += '"' + objfield + '":"<b>平均值:</b>"';
                obj = eval('({' + footer['avg'] + '})');
            }else{
                obj[objfield] = "<b>平均值:</b>" + obj[objfield];
            }
            footerObj.push(obj);
        }
         
        if(footer['max'] != ""){
            var tmp = '{' + footer['max'].substring(0,footer['max'].length - 1) + "}";
            var obj = eval('(' + tmp + ')');
             
            if(obj[objfield] == undefined){
                footer['max'] += '"' + objfield + '":"<b>最大值:</b>"';
                obj = eval('({' + footer['max'] + '})');
            }else{
                obj[objfield] = "<b>最大值:</b>" + obj[objfield];
            }
            footerObj.push(obj);
        }
         
        if(footer['min'] != ""){
            var tmp = '{' + footer['min'].substring(0,footer['min'].length - 1) + "}";
            var obj = eval('(' + tmp + ')');
             
            if(obj[objfield] == undefined){
                footer['min'] += '"' + objfield + '":"<b>最小值:</b>"';
                obj = eval('({' + footer['min'] + '})');
            }else{
                obj[objfield] = "<b>最小值:</b>" + obj[objfield];
            }
            footerObj.push(obj);
        }
         
        if(footerObj.length > 0){
            $(jq).datagrid('reloadFooter',footerObj); 
        }
         
        function sisFooterData(opt,footer){
            for (var j = 0; j < opt.length; j++) {
                for(var i=0; i<opt[j].length; i++){
                     
                    if(!opt[j][i].field) continue;
                     
                    if(opt[j][i].sum){
                        footer['sum'] = footer['sum'] + sum(opt[j][i].field)+ ',';
                    }
                    if(opt[j][i].avg){
                        footer['avg'] = footer['avg'] + avg(opt[j][i].field)+ ',';
                    }
                    if(opt[j][i].max){
                        footer['max'] = footer['max'] + max(opt[j][i].field)+ ',';
                    }
                    if(opt[j][i].min){
                        footer['min'] = footer['min'] + min(opt[j][i].field)+ ',';
                    }
                }
            }
            return footer;
        }
         
        function sum(filed){
            var sumNum = 0;
            for(var i=0;i<rows.length;i++){
                sumNum += Number(rows[i][filed]);
            }           
            return '"' + filed + '":' + sumNum;
        };
         
        function avg(filed){
            var sumNum = 0;
            for(var i=0;i<rows.length;i++){
                sumNum += Number(rows[i][filed]);
            }
             
            if(rows.length != 0)
                sumNum = sumNum/rows.length;
             
            return '"' + filed + '":'+ sumNum;
        }
 
        function max(filed){
            var max = 0;
            for(var i=0;i<rows.length;i++){
                if(i==0){
                    max = Number(rows[i][filed]);
                }else{
                    max = Math.max(max,Number(rows[i][filed]));
                }
            }
            return '"' + filed + '":'+ max;
        }
         
        function min(filed){
            var min = 0;
            for(var i=0;i<rows.length;i++){
                if(i==0){
                    min = Number(rows[i][filed]);
                }else{
                    min = Math.min(min,Number(rows[i][filed]));
                }
            }
            return '"' + filed + '":'+ min;
        }
    }
});