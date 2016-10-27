<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>


<form id="taskExeAssigneeForm">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>任务接收人:
				</label>
			</td>
			<td class="value">
			   <t:empSelect displayName="exeUserName" multiples="false" hiddenName="exeUserId"></t:empSelect>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>通知方式:
				</label>
			</td>
			<td class="value">
			   <t:comboBox name="informType" id="informType" multiple="true" value="${definition.informType }" data='[{"id":"innerMessage","text":"站内信"},{"id":"email","text":"邮件"},{"id":"sms","text":"短信"}]'></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					转办原因:
				</label>
			</td>
			<td class="value">
				<textarea id="description" name="description" style="width: 97%;" rows="5"></textarea>
			</td>
		</tr>
	</table>
</form>