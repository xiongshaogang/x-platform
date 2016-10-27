<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid usePlugin="password" layout="table" action="operLogController.do?saveOperLog" refresh="true"  gridId="operLogList">
		<input id="id" name="id" type="hidden" value="${operLog.id }">
		<input id="opname" name="opname" type="hidden" value="${operLog.opname }">
		<input id="userId" name="userId" type="hidden" value="${operLog.userId }">
		<input id="userName" name="userName" type="hidden" value="${operLog.userName }">
		<input id="method" name="method" type="hidden" value="${operLog.method }">
		<input id="requesturi" name="requesturi" type="hidden" value="${operLosg.requesturi }">
		<input id="params" name="params" type="hidden" value="${operLog.params }">
		<input id="moduleName" name="moduleName" type="hidden" value="${operLog.moduleName }">
		<input id="detail" name="detail" type="hidden" value="${operLog.detail }">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
	     <tr>
			<td class="td_title"><label class="Validform_label">操作名:</label></td>
			<td class="value" colspan="1" >
				<input id="opname" name="opname" type="text" class="inputxt"  value="${operLog.opname}" />
			</td>
			<td class="td_title"><label class="Validform_label">模块名:</label></td>
			<td class="value" colspan="1" >
				<input id="moduleName" name="moduleName" type="text" class="inputxt"  value="${operLog.moduleName}" />
			</td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label">用户名:</label></td>
			<td class="value" colspan="1" >
				<input id="userName" name="userName" type="text" class="inputxt"  value="${operLog.userName}" />
			</td>
			<td class="td_title"><label class="Validform_label">执行时间:</label></td>
		    <!-- 日期格式 -->
		    <td class="value" colspan="1">
		    <t:datetimebox name="time" id="time" type="datetime" value="${operLog.time}"/>
		    </td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label">ip地址:</label></td>
			<td class="value" colspan="1" >
				<input id="ip" name="ip" type="text" class="inputxt"   value="${operLog.ip}" />
			</td>
			<td class="td_title"><label class="Validform_label">方法名:</label></td>
			<td class="value" colspan="1" >
				<input id="method" name="method" type="text" class="inputxt"  value="${operLog.method}" />
			</td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label">参数:</label></td>
			<td class="value" colspan="1" >
				<input id="params" name="params" type="text" class="inputxt"   value="${operLog.params}" />
			</td>
			<td class="td_title"><label class="Validform_label">请求路径:</label></td>
			<td class="value" colspan="1" >
				<input id="requesturi" name="requesturi" type="text" class="inputxt"  value="${operLog.requesturi}" />
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">日志内容:</label></td>
			<td class="value" colspan="3" >
				<input id="detail" name="detail" type="text" class="inputxt"   value="${operLog.detail}" />
			</td>
		</tr>
		</table>
   </t:formvalid>
 </body>
