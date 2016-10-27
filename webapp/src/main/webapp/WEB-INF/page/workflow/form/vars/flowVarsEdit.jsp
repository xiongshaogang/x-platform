<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

  <t:formvalid formid="formobj" gridId="flowVarList" action="flowVarsController.do?saveFlowVars" >
	<input id="id" name="id" type="hidden" value="${flowVars.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>变量名称:
				</label>
			</td>
			<td class="value">
			    <input name="name" dataType="zh" type="text" class="inputxt" value='${flowVars.name}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>变量编码:
				</label>
			</td>
			<td class="value">
			    <input name="code" dataType="*" type="text" class="inputxt" value='${flowVars.code}' uniquemsg='表单编码已存在' oldValue='${flowVars.code}' entityName="com.xplatform.base.workflow.form.entity.FlowVarsEntity" ajaxurl="commonController.do?checkUnique">
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>数据类型:
				</label>
			</td>
			<td class="value">
			    <input name="dataType" dataType="*" type="text" class="inputxt" value='${flowVars.dataType}' oldValue='${flowVars.dataType}' />
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					默认值:
				</label>
			</td>
			<td class="value">
			    <input name="defValue" type="text" class="inputxt" value='${flowVars.defValue}' oldValue='${flowVars.defValue}' />
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>所属表单:
				</label>
			</td>
			<td class="value">
			    <input id="formId" name="form.id" type="hidden" class="inputxt" value='${flowVars.form.id}' oldValue='${flowVars.form.id}' />
			    <input id="formName" dataType="*" readonly="readonly" type="text" class="inputxt" value='${flowVars.form.name}' oldValue='${flowVars.form.name}' />
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					备注:
				</label>
			</td>
			<td class="value">
			    <textarea style="height:80px" id="description" name="description" type="text" class="input_area">${flowVars.description}</textarea>
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