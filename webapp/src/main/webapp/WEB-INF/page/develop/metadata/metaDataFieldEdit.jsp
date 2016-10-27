<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
<script type="text/javascript" src="plug-in/tools/curdtools.js"></script>
 
 </head>
 <body>
  <t:formvalid formid="fieldedit"  usePlugin="password" layout="table" action="cgFormFieldController.do?saveFormField" refresh="true" gridId="cgFormFieldList">
					<input id="oldFieldName" name="oldFieldName" type="hidden" value="${formField.oldFieldName }">
					<input id="id" name="id" type="hidden" value="${formField.id }">
					<input id="table.id" name="table.id" type="hidden" value="${param.tableid}">
					<input id="orderNum" name="orderNum" type="hidden" value="${formField.orderNum }">
					<input id="createUserId" name="createUserId" type="hidden" value="${formField.createUserId }">
					<input id="createUserName" name="createUserName" type="hidden" value="${formField.createUserName }">
					<input id="createTime" name="createTime" type="hidden" value="${formField.createTime }">
					<input id="updateUserId" name="updateUserId" type="hidden" value="${formField.updateUserId }">
					<input id="updateUserName" name="updateUserName" type="hidden" value="${formField.updateUserName }">
					<input id="updateTime" name="updateTime" type="hidden" value="${formField.updateTime }">
		<table  cellpadding="0" cellspacing="1" class="formtable">
		<tr>
		<td class="td_title"><label class="Validform_label"> 字段名称</label></td>
		<td class="value" ><input type="text" class="inputxt" id="fieldName" name="fieldName" value="${formField.fieldName }" datatype="*" > </td>
		<td class="td_title"><label class="Validform_label"> 字段描述</label></td>
		<td class="value" ><input type="text" class="inputxt" id="content" name="content" value="${formField.content }" datatype="*"> </td>
		</tr>
		<tr>
		<td class="td_title"><label class="Validform_label"> 字段长度</label></td>
		<td class="value" ><input type="text" class="inputxt" id="length" name="length" value="${formField.length }" datatype="n" > </td>
		<td class="td_title"><label class="Validform_label"> 小数点长度</label></td>
		<td class="value" ><input type="text" class="inputxt" id="pointLength" name="pointLength" value="${formField.pointLength }" datatype="n"> </td>
		</tr>
		<tr>
		<td class="td_title"><label class="Validform_label"> 默认值</label></td>
		<td class="value" ><input type="text" class="inputxt" id="fieldDefault" name="fieldDefault" value="${formField.fieldDefault }"  > </td>
		<td class="td_title"><label class="Validform_label">字段类型</label></td>
		<td class="value" >
			<t:comboBox data='[{"id":"string","text":"String"},{"id":"int","text":"Integer"},{"id":"double","text":"Double"},{"id":"Date","text":"Date"},{"id":"BigDecimal","text":"BigDecimal"},{"id":"Text","text":"Text"},{"id":"Blob","text":"Blob"}]' value="${formField.type}" name="type" id="type" ></t:comboBox>
		</td>
		</tr>
		<tr>
		<td class="td_title"><label class="Validform_label">是否主键</label></td>
		<td class="value" >
		<c:choose>
		<c:when test="${empty formField.isKey}"><t:comboBox dictCode="YNType" value="N" name="isKey" id="isKey" ></t:comboBox></c:when>
		<c:otherwise><t:comboBox dictCode="YNType" value="${formField.isKey}" name="isKey" id="isKey" ></t:comboBox></c:otherwise>
		</c:choose>
		</td>
		<td class="td_title"><label class="Validform_label">是否可为空</label></td>
		<td class="value" >
		<c:choose>
		<c:when test="${empty formField.isNull}"><t:comboBox dictCode="YNType" value="Y" name="isNull" id="isNull" ></t:comboBox></c:when>
		<c:otherwise><t:comboBox dictCode="YNType" value="${formField.isNull}" name="isNull" id="isNull" ></t:comboBox></c:otherwise>
		</c:choose>
		</td>
		</tr>
		<tr style="display:none;">
		<td class="td_title"><label class="Validform_label"> 外键ID</label></td>
		<td class="value" ><input type="text" class="inputxt" id="mainField" name="mainField" value="${formField.mainField }"  > </td>
		<td class="td_title"><label class="Validform_label"> 外键table</label></td>
		<td class="value" ><input type="text" class="inputxt" id="mainTable" name="mainTable" value="${formField.mainTable }" > </td>
		</tr>
		</table>
		</t:formvalid>
 </body>