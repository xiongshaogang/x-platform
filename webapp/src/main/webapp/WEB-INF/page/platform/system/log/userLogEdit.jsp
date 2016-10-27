<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid usePlugin="password" layout="table" action="userLogController.do?saveLogUser" refresh="true"  gridId="userLogList">
		<input id="id" name="id" type="hidden" value="${userLog.id }">
		<input id="createUserId" name="createUserId" type="hidden" value="${userLog.createUserId }">
		<input id="createUserName" name="createUserName" type="hidden" value="${userLog.createUserName }">
		<input id="createTime" name="createTime" type="hidden" value="${userLog.createTime }">
		<input id="updateUserId" name="updateUserId" type="hidden" value="${userLog.updateUserId }">
		<input id="updateUserName" name="updateUserName" type="hidden" value="${userLog.updateUserName }">
		<input id="updateTime" name="updateTime" type="hidden" value="${userLog.updateTime }">
		<input id="userName" name="userName" type="hidden" value="${userLog.userName }">
		<input id="time" name="time" type="hidden" value="${userLog.time }">
		<input id="content" name="content" type="hidden" value="${userLog.content }">
		<input id="status" name="status" type="hidden" value="${userLog.status }">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
		</table>
   </t:formvalid>
 </body>
