<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/ckeditor/ckeditor_sysTemp.js"></script>
 <t:formvalid beforeSubmit="getValues" formid="formobj" gridId="msgTemplateList" action="msgTemplateController.do?saveMsgTemplate">
	<input id="id" name="id" type="hidden" value="${msgTemplate.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>任务名称:
				</label>
			</td>
			<td class="value">
			    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt" oldValue='${msgTemplate.name}' value='${msgTemplate.name}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>任务标题:
				</label>
			</td>
			<td class="value">
			    <input id="title" name="title" datatype="*1-50" type="text" class="inputxt" value='${msgTemplate.title}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>任务用途:
				</label>
			</td>
			<td class="value">
				<t:comboBox id="useType" name="useType" dictCode="msg_template" value='${msgTemplate.useType}'></t:comboBox>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>是否默认:
				</label>
			</td>
			<td class="value">
				<t:comboBox id="isDefault" name="isDefault" data='[{"text":"是","id":"Y"},{"text":"否","id":"N"}]' value='${msgTemplate.isDefault}'></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					站内消息:
				</label>
			</td>
			<td class="value" colspan="3">
				<textarea id="innerContent" name="innerContent" style="width:100%;" rows="4">${fn:escapeXml(msgTemplate.innerContent)}</textarea>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					邮件消息:
				</label>
			</td>
			<td class="value" colspan="3">
				<textarea id="mailContent" name="mailContent" style="width:100%;" rows="4">${fn:escapeXml(msgTemplate.mailContent)}</textarea>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					短信消息:
				</label>
			</td>
			<td class="value" colspan="3" width="80%">
				<textarea id="smsContent" name="smsContent" style="width: 98%;" rows="4">${msgTemplate.smsContent}</textarea>
			</td>
		</tr>
	</table>
</t:formvalid>
<script type="text/javascript">
	if( CKEDITOR.instances['innerContent'] ){
	    CKEDITOR.remove(CKEDITOR.instances['innerContent']);
	}
	if( CKEDITOR.instances['mailContent'] ){
	    CKEDITOR.remove(CKEDITOR.instances['mailContent']);
	}
	var innerEditor = ckeditor('innerContent');
	var mailEditor = ckeditor('mailContent');
	function getValues(){
		$('#innerContent').val(innerEditor.getData());
		$('#mailContent').val(mailEditor.getData());
	}
</script>