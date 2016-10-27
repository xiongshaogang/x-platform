<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!-------------------------------------- codemirror 代码高亮插件 -->
<script src="plug-in/javacode/codemirror.js"></script>
<script src="plug-in/javacode/InitMirror.js"></script>
<!-------------------------------------- 自动补全标签 -->
<script src="plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js"></script>
<script type="text/javascript">
	//页面加载时切换视图
	var sourceDictValue = "${sysScript.sourceDict}";
	//初始化代码编辑器
	$(function() {
		//初始化代码高亮插件
		setTimeout('InitMirror.initId("scriptContent")', InitMirror.options.initDelay);
		initPage();
	});
	function initPage() {
		//说明是编辑已有记录
		if ($("#id").val() != "") {
			setTimeout(function() {
				var sourceDict = $("#sourceDict", getD($("#sysscript_button_div"))).combobox("select", sourceDictValue);
				if (sourceDictValue == "pieceScript") {
					selectClass({
						id : '${sysScript.className}'
					});
					var onloadArgument = '${sysScript.argument}';
					var table = createParamTable({
						para : $.parseJSON(onloadArgument)
					});
					var paraInfoTd = $("#paraInfo").empty().append(table);
				}
			},0);
		} else {
			$("#sysscript_button_div").hide();
			$("tr[flag=mergeScript]").hide();
			$("tr[flag=pieceScript]").hide();
		}
	}

	//保存前将代码编辑器的内容保存到textarea
	function saveEditor() {
		InitMirror.save();
		var json = bulidParaJson();
		$("input#argument").val(json);
	}

	//选择脚本来源下拉框触发方法
	function changeSource(record, self) {
		//手动拼接产生脚本
		if (record.id == "mergeScript") {
			$("#sysscript_button_div").show();
			$("tr[flag=mergeScript]").show();
			$("tr[flag=pieceScript]").hide();
		} else if (record.id == "pieceScript") { //从类中选取产生脚本
			//如果还未加载数据则改变url加载数据
			$("#sysscript_button_div").hide();
			$("tr[flag=pieceScript]").show();
			$("tr[flag=mergeScript]").hide();
		}
	}

	//选择来源类触发方法
	function selectClass(record) {
		//返回对应类的spring注入的类实例名
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : "sysScriptController.do?getClassInsName",
			data : "className=" + record.id,
			success : function(data) {
				if (data) {
					var result = $.parseJSON(data);
					$("#classInsName").val(result);
				}
			}
		});

		if ($("#methodName").attr("autocomplete") == null) {
			$("#methodName").autocomplete("sysScriptController.do?getMethods", {
				minChars : 2,
				delay : 500,
				max : 15,
				width : 200,
				dataType : "json",
				matchContains : true,
				cacheLength : 0,
				extraParams : {
					className : function() {
						return $("#className").combobox("getValue");
					}
				},
				parse : function(data) {
					return $.map(data, function(row) {
						return {
							data : row,
							value : row.methodName,
							result : row.methodName
						}
					});
				},
				formatItem : function(row, i, max) {//格式化列表中的条目
					return row.methodName;
				},
				formatMatch : function(row, i, max) {//匹配的值
					return row.methodName;
				}
			}).result(function(event, data, formatted) {
				$("#returnType").val(data.returnType);
				var table = createParamTable(data);
				var paraInfoTd = $("#paraInfo").empty().append(table);
			});
		}

	}

	//构造参数table
	function createParamTable(data) {
		var table = $("#paramTableDiv #paramTableTpl").clone();
		var trTpl = $("tbody tr", table);
		var tbody = $("tbody", table).empty();
		for (var i = 0; i < data.para.length; i++) {
			var para = data.para[i];
			var tr = createParamTr(para);
			tbody.append(tr);
		}
		return table;
	}

	//构造参数table的tr
	function createParamTr(para) {
		//复制tr
		var tr = $("#paramTableDiv #paramTableTpl tbody tr").clone();
		//给tr的td内容赋值
		var paraName = $("[flag=paraName]", tr).text(para.paraName).attr("title", para.paraName);
		var paraType = $("[flag=paraType]", tr).text(para.paraType).attr("title", para.paraType);
		var paraDesc = $("[flag=paraDesc]", tr).val(para.paraDesc);
		return tr;
	}

	//读取表格中td内容,构造json
	function bulidParaJson() {
		var paraArray = [];
		var paraJson;
		$("#paraInfo table tbody tr").each(function() {
			var tr = $(this);
			var paraName = tr.find("span[flag=paraName]").text();
			var paraType = tr.find("span[flag=paraType]").text();
			var paraDesc = tr.find("input[flag=paraDesc]").val();

			//构造单行js对象
			var paraObj = {};
			paraObj.paraName = paraName;
			paraObj.paraType = paraType;
			paraObj.paraDesc = paraDesc;

			paraArray.push(paraObj);
		});
		//数组转json
		paraJson = JSON.stringify(paraArray);
		return paraJson;
	}
</script>
<style type="text/css">
#paramTableTpl {
	color: #858585;
	border-left: 1px solid #d5d5d5;
	border-bottom: 1px solid #d5d5d5;
	border-left: 1px solid #d5d5d5;
}

#paramTableTpl th {
	text-align: center;
}

#paramTableTpl td,th {
	height: 30px;
	border-right: 1px solid #d5d5d5;
	border-top: 1px solid #d5d5d5;
	border-right: 1px solid #d5d5d5;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

#paramTableTpl input.paraDescInput {
	border: 1px solid #4570a3;
	height: 100%;
}

#sysscript_button_div {
	margin-top: 5px;
	width: 100%;
	height: 30px;
}

#sysscript_button_div button {
	margin-left: 5px;
}
</style>
<div id="sysscript_button_div">
	<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('scriptContent')">脚本选择</button>
</div>
<t:formvalid beforeSubmit="saveEditor" gridId="sysScriptList" action="sysScriptController.do?save">
	<input id="id" name="id" type="hidden" value="${sysScript.id}">
	<input id="argument" name="argument" type="hidden" value='${sysScript.argument}'>

	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>脚本名称: </label></td>
			<td class="value"><input id="name" name="name" datatype="*1-100" type="text" class="inputxt"
				value='${sysScript.name}'></td>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>脚本类型: </label></td>
			<td class="value"><t:comboBox id="typeDict" name="typeDict" datatype="*" dictCode="scriptType"
					value="${sysScript.typeDict}"></t:comboBox></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>脚本来源: </label></td>
			<td class="value"><t:comboBox onSelect="changeSource(record,self)" id="sourceDict" name="sourceDict"
					datatype="*" dictCode="scriptSource"></t:comboBox></td>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>是否启用: </label></td>
			<td class="value"><t:comboBox id="enableDict" name="enableDict" datatype="*" dictCode="YNType"
					value="${sysScript.enableDict}"></t:comboBox></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">脚本描述:</label></td>
			<td class="value" style="width: 350px" colspan="3"><textarea class="input_area" id="remark" name="remark"
					style="width: 100%">${sysScript.remark}</textarea></td>
		</tr>
		<tr flag="pieceScript">
			<td class="td_title"><label class="Validform_label">来源类名:</label></td>
			<td class="value"><t:comboBox panelWidth="480" onSelect="selectClass(record)" id="className" name="className"
					url="sysScriptController.do?getClass" value="${sysScript.className}"></t:comboBox></td>
			<td class="td_title"><label class="Validform_label">类实例名:</label></td>
			<td class="value"><input type="text" class="inputxt" readonly="readonly" id="classInsName" name="classInsName"
				value="${sysScript.classInsName}" /></td>
		</tr>
		<tr flag="pieceScript">
			<td class="td_title"><label class="Validform_label">来源方法:</label></td>
			<td class="value"><input class="inputxt" type="text" id="methodName" name="methodName"
				value="${sysScript.methodName}" /></td>
			<td class="td_title"><label class="Validform_label">返回值类型:</label></td>
			<td class="value"><input type="text" class="inputxt" readonly="readonly" id="returnType" name="returnType"
				value="${sysScript.returnType}" /></td>
		</tr>
		<tr flag="pieceScript">
			<td class="td_title"><label class="Validform_label">参数信息:</label></td>
			<td class="value" style="width: 350px" colspan="3" id="paraInfo"></td>
		</tr>
		<tr flag="mergeScript">
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>脚本内容: </label></td>
			<td class="value" style="width: 350px" colspan="3"><div style="background: #F7F7F7;">
					<textarea mirrorheight="200px" id="scriptContent" name="scriptContent" class="input_area">${sysScript.scriptContent}</textarea>
				</div></td>
		</tr>
	</table>

	<div id="paramTableDiv" style="display: none">
		<table id="paramTableTpl" class="formtable" style="width: 100%; table-layout: fixed; border: 1px solid #d5d5d5"
			cellpadding="0" cellspacing="1">
			<thead>
				<tr>
					<th style="width: 12%">参数名</th>
					<th style="width: 68%">参数类型</th>
					<th>参数说明</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><span flag="paraName"></span></td>
					<td><span flag="paraType"></span></td>
					<td style="text-align: center;"><input class="paraDescInput" type="text" flag="paraDesc"
						style="width: 94%; padding: 2px; margin: 0 auto" /></td>
				</tr>
			</tbody>
		</table>
	</div>
</t:formvalid>

