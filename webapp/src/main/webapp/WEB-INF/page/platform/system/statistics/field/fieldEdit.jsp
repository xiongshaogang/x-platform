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
  <t:formvalid usePlugin="password" layout="table" action="fieldController.do?saveField" refresh="true"  gridId="fieldList">
		<input id="id" name="id" type="hidden" value="${field.id }">
		<input id="createUserId" name="createUserId" type="hidden" value="${field.createUserId }">
		<input id="createUserName" name="createUserName" type="hidden" value="${field.createUserName }">
		<input id="createTime" name="createTime" type="hidden" value="${field.createTime }">
		<input id="updateUserId" name="updateUserId" type="hidden" value="${field.updateUserId }">
		<input id="updateUserName" name="updateUserName" type="hidden" value="${field.updateUserName }">
		<input id="updateTime" name="updateTime" type="hidden" value="${field.updateTime }">
		<input id="name" name="name" type="hidden" value="${field.name }">
		<input id="showName" name="showName" type="hidden" value="${field.showName }">
		<input id="type" name="type" type="hidden" value="${field.type }">
		<input id="num" name="num" type="hidden" value="${field.num }">
		<input id="description" name="description" type="hidden" value="${field.description }">
		<input id="isshow" name="isshow" type="hidden" value="${field.isshow }">
		<input id="issum" name="issum" type="hidden" value="${field.issum }">
		<input id="issearch" name="issearch" type="hidden" value="${field.issearch }">
		<input id="searchActivex" name="searchActivex" type="hidden" value="${field.searchActivex }">
		<input id="searchCondition" name="searchCondition" type="hidden" value="${field.searchCondition }">
		<input id="isx" name="isx" type="hidden" value="${field.isx }">
		<input id="isy" name="isy" type="hidden" value="${field.isy }">
		<input id="datasourceId" name="datasourceId" type="hidden" value="${field.datasourceId }">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
		</table>
   </t:formvalid>
 </body>
