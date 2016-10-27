<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:formvalid action="generateController.do?doAddOrUpdate" gridId="formTypeEntityList">
	<input id="id" name="id" type="hidden" value="${formTypeEntity.id}">
	<input id="typeID" name="typeID" type="hidden" value="${formTypeEntity.typeID}">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title"><label class="Validform_label">表单模板名称</label></td>
			<td class="value"><input id="name" name="name" type="text" class="inputxt" datatype="*" value="${formTypeEntity.name}"></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">表单Code</label></td>
			<td class="value"><input id="code" name="code" type="text" datatype="*" class="inputxt" value="${formTypeEntity.code}"></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">所属模型类型</label></td>
			<td class="value"><input id="typeName" name="typeName" readonly="readonly" type="text" class="inputxt" value="${formTypeEntity.typeName}"></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">描述</label></td>
			<td class="value"><textarea id="description" name="description" class="input_area">${formTypeEntity.description}</textarea></td>
		</tr>
	</table>
</t:formvalid>
