<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:tabs id="userPrivillage"  border="true" hBorderBottom="true" hBorderLeft="false" hBorderRight="false" 
            hBorderTop="false" leftDiv="true" leftDivWidth="90" leftDivTitle="用户权限设置" rightDiv="true" closeBtn="true">
  <t:tab id="distributeRole" title="角色分配" href="roleController.do?distributionRole&id=${userId }&distributionFlag=userDistribution"></t:tab>
  <t:tab id="distributePrivillage" title="权限分配" href="userController.do?moduleAndResourceSelect&userId=${userId }"></t:tab>
  <t:tab id="sysPrivillage" title="系统类型权限分配" href="userController.do?sysTypeSelect&userId=${userId }"></t:tab>
  <t:tab id="dataPrivillage" title="资料文件夹操作权限分配" href="userController.do?dataDirBtnByUserSelect&userId=${userId }"></t:tab>
</t:tabs>