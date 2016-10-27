<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

  <t:formvalid formid="formobj" gridId="flowFormItemList" action="flowFormItemController.do?saveFlowFormItem" >
	<input id="id" name="id" type="hidden" value="${flowFormItem.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>事项名称:
				</label>
			</td>
			<td class="value">
			    <input name="name" dataType="zh" type="text" class="inputxt" value='${flowFormItem.name}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>事项编码:
				</label>
			</td>
			<td class="value">
			    <input name="code" dataType="*" type="text" class="inputxt" value='${flowFormItem.code}' uniquemsg='表单编码已存在' oldValue='${flowFormItem.code}' entityName="com.xplatform.base.workflow.form.entity.FlowFormItemEntity" ajaxurl="commonController.do?checkUnique">
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>所属表单:
				</label>
			</td>
			<td class="value">
			    <input id="formId" name="form.id" type="hidden" class="inputxt" value='${flowFormItem.form.id}' oldValue='${flowFormItem.form.id}' />
			    <input id="formName" dataType="*" readonly="readonly" type="text" class="inputxt" value='${flowFormItem.form.name}' oldValue='${flowFormItem.form.name}' />
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					备注:
				</label>
			</td>
			<td class="value">
			    <textarea style="height:80px" id="description" name="description" type="text" class="input_area">${flowFormItem.description}</textarea>
			</td>
		</tr>
	</table>
</t:formvalid>
  <script type="text/javascript">
	  $(function(){
		   if(!document.getElementById("formId").value){
			   $("#formId").val($("#pformId").val());
			   $("#formName").val($("#pformName").val());
		   }
	  });
  </script>