<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$(function() {

	});
</script>
<t:formvalid formid="form_addSign" action="taskController.do?addSign" >
	<input type="hidden" name="taskId" value="${taskId}" />
	<table cellpadding="0" cellspacing="1" class="formtable" style="width: 100%;">
		<tr>
			<td class="td_title"><label class="Validform_label">加签人:</label></td>
			<td class="value"><t:empSelect empOrUser="user" displayName="signUserUserName" hiddenName="signUserIds" /></td>
		</tr>
	</table>
</t:formvalid>
