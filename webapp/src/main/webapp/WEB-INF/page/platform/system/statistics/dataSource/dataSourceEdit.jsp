<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

  <t:formvalid formid="formobj" action="dataSourceController.do?savedataSource"   gridId="dataSourceList" callback="saveEnd(data)" afterSaveClose="false">
		<input id="id" name="id" type="hidden" value="${dataSource.id }">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
		 <tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>名称:</label></td>
			<td class="value"  >
				<input id="name" name="name" type="text" class="inputxt"   datatype="*"  value="${dataSource.name}" />
			</td>
		</tr>
		<tr>
		<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>编码:</label></td>
			<td class="value" >
				<input id="code" name="code" type="text" class="inputxt"   datatype="*"  value="${dataSource.code}" />
			</td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>类型:</label></td>
		    <td class="value" >
		        <t:comboBox   multiple="false"  dictCode="dataSourceType" url="" name="type" id="type" value="${dataSource.type}"></t:comboBox>
		    </td>
		 </tr>
		 <tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>数据值:</label></td>
			<td class="value" >
				<textarea id="value" name="value" class="input_area"  datatype="*" rows="4">${dataSource.value}</textarea>
			</td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label">描述:</label></td>
			<td class="value" >
				<textarea id="description" name="description" class="input_area" rows="2">${dataSource.description}</textarea>
			</td>
		</tr>
		</table>
   </t:formvalid>
   
<script type="text/javascript">
   function saveEnd(data){
	   if(data.obj != 'error'){
		   tip(data.msg);
		   $("#dataSourceList").datagrid('reload');
		   closeD($('#formobj').closest(".window-body"));
	   }
   }
</script>
