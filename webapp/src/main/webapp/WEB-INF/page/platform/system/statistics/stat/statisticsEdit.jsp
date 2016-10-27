<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>模块信息</title>
 </head>
 <body>
  <t:formvalid  usePlugin="password" layout="table" action="statisticsController.do?saveStatistics" refresh="true" gridId="statisticsList">
		<input id="id" name="id" type="hidden" value="${statistics.id }">
		<input id="type" name="type.id" type="hidden" value="${statistics.type.id }">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
		 <tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>统计名称:</label></td>
			<td class="value" colspan="1" >
				<input id="name" name="name" type="text" class="inputxt"   datatype="*"  value="${statistics.name}" />
			</td>
		 <tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>统计code:</label></td>
			<td class="value" colspan="1" >
				<input id="code" name="code" type="text" class="inputxt"   datatype="*"  value="${statistics.code}" />
			</td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>数据源类型:</label></td>
		    <td class="value" colspan="1">
		        <t:comboBox   textField="name" valueField="id" url="dataSourceController.do?getList" name="datasourceId" id="datasourceId" value="${statistics.datasourceId}"></t:comboBox>
		    </td>
		 <tr>
			<td class="td_title"><label class="Validform_label">默认显示图标类型:</label></td>
		    <td class="value" colspan="1">
		        <t:comboBox   dictCode="chartType"  name="showType" id="showType" value="${statistics.showType}"></t:comboBox>
		    </td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label">权限类型:</label></td>
		    <td class="value" colspan="1">
		        <t:comboBox   multiple="false"  dictCode="" url="" name="authorityType" id="authorityType" value="${statistics.authorityType}"></t:comboBox>
		    </td>
		</tr>
		</table>
   </t:formvalid>
   <script type="text/javascript">
	  $(function(){
		   if(!$("#type").val()){
			   $("#type").val($("#nodeid").val());
		   }
	  });
  </script>
 </body>
