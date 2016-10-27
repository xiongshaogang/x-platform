<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:formvalid action="generateFieldController.do?doSaveOrUpdate" gridId="formFieldEntityList">
	<input type="hidden" name="id" value="${formFieldEntity.id}" />
	<t:collapseTitle id="div1" title="常规设置">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title"><label class="Validform_label">字段名称</label></td>
				<td class="value"><input name="fieldName" type="text" class="inputxt" datatype="*" value="${formFieldEntity.fieldName}"></td>
				<td class="td_title"><label class="Validform_label">字段显示名</label></td>
				<td class="value"><input name="fieldLabel" type="text" class="inputxt" datatype="*" value="${formFieldEntity.fieldLabel}"></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">字段长度</label></td>
				<td class="value"><input name="fieldLength" type="text" class="inputxt" datatype="n" value="${formFieldEntity.fieldLength}"></td>
				<td class="td_title"><label class="Validform_label">字段精度</label></td>
				<td class="value"><input name="fieldPersion" type="text" class="inputxt" datatype="n" value="${formFieldEntity.fieldPersion}"></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">字段类型</label></td>
				<td class="value"><input name="fieldType" type="text" class="inputxt" value="${formFieldEntity.fieldType}"></td>
				<td class="td_title"><label class="Validform_label">显示类型</label></td>
				<td class="value"><t:comboBox dictCode="InputType" name="showtypeDict" id="showtypeDict" value="${formFieldEntity.showtypeDict}"></t:comboBox></td>
			</tr>
			<%-- <tr>
				<td class="td_title"><label class="Validform_label">字段描述</label></td>
				<td colspan="3" class="value"><textarea name="description"
						class="input_area">${formFieldEntity.description}</textarea></td>
			</tr> --%>
		</table>
	</t:collapseTitle>
	<t:collapseTitle id="div2" title="查询设置">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title"><label class="Validform_label">是否查询</label></td>
				<td class="value"><t:comboBox dictCode="YNType" name="queryShow" id="queryShow" value="${formFieldEntity.queryShow}"></t:comboBox></td>
				<td class="td_title"><label class="Validform_label">查询模式</label></td>
				<td class="value"><t:comboBox dictCode="queryType" value="${formFieldEntity.sQueryMode}" name="sQueryMode" id="sQueryMode"></t:comboBox></td>
				<%-- <td class="td_title"><label class="Validform_label">查询显示</label></td>
				<td class="value"><t:comboBox dictCode="YNType"
						name="isShowInQuery" id="isShowInQuery"
						value="${formFieldEntity.isShowInQuery}"></t:comboBox></td> --%>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">查询框类型</label></td>
				<td class="value"><t:comboBox
						data='[{"id":"text","text":"text"},{"id":"datetimebox","text":"datetimebox"},{"id":"datebox","text":"datebox"},{"id":"combobox","text":"combobox"},{"id":"combotree","text":"combotree"},{"id":"combogrid","text":"combogrid"},{"id":"numberbox","text":"numberbox"}]'
						value="${formFieldEntity.sQueryInputType}" name="sQueryInputType" id="sQueryInputType"></t:comboBox></td>
				<td class="td_title"><label class="Validform_label">字典code</label></td>
				<td class="value"><input name="sDictCode" type="text" class="inputxt" value="${formFieldEntity.sDictCode}"></td>
				<%-- <td class="td_title"><label class="Validform_label">查询条件类型</label></td>
				<td class="value"><t:comboBox dictCode="QueryCon"
						name="queryConDict" id="queryConDict"
						value="${formFieldEntity.queryConDict}"></t:comboBox></td>
				<td class="td_title"></td>
				<td class="value"></td> --%>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">临时数据</label></td>
				<td class="value"><input name="sData" type="text" class="inputxt" value="${formFieldEntity.sData}"></td>
				<td class="td_title"><label class="Validform_label">扩展属性</label></td>
				<td class="value"><input name="extend" type="text" class="inputxt" value="${formFieldEntity.extend}"></td>
			</tr>
		</table>
	</t:collapseTitle>
	<t:collapseTitle id="div3" title="列表设置">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title"><label class="Validform_label">列表显示</label></td>
				<td class="value"><t:comboBox dictCode="YNType" name="listShow" id="listShow" value="${formFieldEntity.listShow}"></t:comboBox></td>
				<td class="td_title"><label class="Validform_label">字段顺序</label></td>
				<td class="value"><input name="gridIndex" type="text" class="inputxt" value="${formFieldEntity.gridIndex}"></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">列显示位置</label></td>
				<td class="value"><t:comboBox dictCode="GridAlign" name="alignDict" id="alignDict"
						data='[{"id":"left","text":"居左"},{"id":"center","text":"居中"},{"id":"right","text":"居右"}]' value="${formFieldEntity.alignDict}"></t:comboBox></td>
				<td class="td_title"><label class="Validform_label">是否排序</label></td>
				<td class="value"><t:comboBox value="${formFieldEntity.isOrder}" dictCode="YNType" name="isOrder" id="isOrder"></t:comboBox></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">字段样式(列表)</label></td>
				<td class="value"><input name="gridStyle" type="text" class="inputxt" value="${formFieldEntity.gridStyle}"></td>
			</tr>
		</table>
	</t:collapseTitle>
	<t:collapseTitle id="div4" title="编辑页设置">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title"><label class="Validform_label">编辑页显示</label></td>
				<td class="value"><t:comboBox value="${formFieldEntity.editShow}" dictCode="YNType" name="editShow" id="editShow"></t:comboBox></td>
				<td class="td_title"><label class="Validform_label">是否只读</label></td>
				<td class="value"><t:comboBox value="${formFieldEntity.isReadonly}" dictCode="YNType" name="isReadonly" id="isReadonly"></t:comboBox></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">占用列数</label></td>
				<td class="value"><input name="pageRowspan" type="text" class="inputxt" value="${formFieldEntity.pageRowspan}"></td>
				<td class="td_title"><label class="Validform_label">字段样式</label></td>
				<td class="value"><input name="pageStyle" type="text" class="inputxt" value="${formFieldEntity.pageStyle}"></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">字典code</label></td>
				<td class="value"><input name="dictCode" type="text" class="inputxt" value="${formFieldEntity.dictCode}"></td>
				<td class="td_title"><label class="Validform_label">调用url</label></td>
				<td class="value"><input name="dataUrl" type="text" class="inputxt" value="${formFieldEntity.dataUrl}"></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">预设验证</label></td>
				<td class="value"><t:comboBox url="generateFieldController.do?getFormRuleEntityList" textField="name" valueField="regulation"
						value="${formFieldEntity.formRuleEntity}" name="formRuleEntity" id="formRuleEntity"></t:comboBox></td>
				<td class="td_title"><label class="Validform_label">验证与、或关系</label></td>
				<td class="value"><t:comboBox data='[{"id":",","text":"与"},{"id":"|","text":"或"}]' value="${formFieldEntity.andOrRule}" name="andOrRule" id="andOrRule"></t:comboBox></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">验证为空提示</label></td>
				<td class="value"><input name="nullMsg" type="text" onclick="ab()" class="inputxt" value="${formFieldEntity.nullMsg}"></td>
				<td class="td_title"><label class="Validform_label">验证错误提示</label></td>
				<td class="value"><input name="errorMsg" type="text" class="inputxt" value="${formFieldEntity.errorMsg}"></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">是否可为空</label></td>
				<td class="value"><t:comboBox value="${formFieldEntity.isNullable}" dictCode="YNType" name="isNullable" id="isNullable"></t:comboBox></td>
				<td class="td_title"><label class="Validform_label">是否唯一</label></td>
				<td class="value"><t:comboBox value="${formFieldEntity.isUnique}" dictCode="YNType" name="isUnique" id="isUnique"></t:comboBox></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label">自定义规则</label></td>
				<td class="value"><input name="reCheckRule" type="text" id="reCheckRule" class="inputxt" value="${formFieldEntity.reCheckRule}"></td>

			</tr>
		</table>
	</t:collapseTitle>
</t:formvalid>
