<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:formvalid action="attachController.do?doRename" gridId="dataVoList">
	<input name="id" type="hidden" value="${id}">
	<input name="parentTypeId" type="hidden" value="${parentTypeId}">
	<input name="fileFlag" type="hidden" value="${fileFlag}">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span>名称:
			</label></td>
			<td class="value"><input name="name" datatype="*1-50" type="text" class="inputxt" value='${name}'></td>
		</tr>
	</table>
</t:formvalid>
