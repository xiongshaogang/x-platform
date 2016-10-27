<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

  <script type="text/javascript">
	  $(function(){
		   if(!$("#typeId").val()){
			   $("#typeId").val($("#ptypeId").val());
		   }
	  });
  </script>

  <t:formvalid formid="formobj" gridId="flowFormList" action="flowFormController.do?saveFlowForm" >
	<input id="id" name="id" type="hidden" value="${flowForm.id }">
	<input id="typeId" name="typeId" type="hidden" value="${flowForm.type.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>表单名称:
				</label>
			</td>
			<td class="value">
			    <input name="name" dataType="zh" type="text" class="inputxt" value='${flowForm.name}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>表单编码:
				</label>
			</td>
			<td class="value">
			    <input name="code" dataType="*" type="text" class="inputxt" value='${flowForm.code}' uniquemsg='表单编码已存在' oldValue='${flowForm.code}' entityName="com.xplatform.base.workflow.form.entity.FlowFormEntity" ajaxurl="commonController.do?checkUnique">
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>表单url:
				</label>
			</td>
			<td class="value">
			    <input name="url" dataType="*" type="text" class="inputxt" value='${flowForm.url}' oldValue='${flowForm.url}' />
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					备注:
				</label>
			</td>
			<td class="value">
			    <textarea style="height:80px" id="description" name="description" type="text" class="input_area">${flowForm.description}</textarea>
			</td>
		</tr>
	</table>
</t:formvalid>