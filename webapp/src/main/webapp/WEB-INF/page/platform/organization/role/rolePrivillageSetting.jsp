<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:tabs id="userPrivillage"  border="true" closeBtn="true" rightDiv="true">
   <t:tab id="distributeOrg" title="分配机构" href="orgnaizationController.do?orgToRoleSelect&roleId=${roleId}"></t:tab>
   <%-- <t:tab id="distributeUser" title="分配用户" href="roleController.do?userToRoleSelect&roleId=${roleId}"></t:tab> --%>
   <t:tab id="moduleAndResourcePri" title="权限设置" href="roleController.do?moduleAndResourceSelect&roleId=${roleId }"></t:tab>
   <t:tab id="dataDirPri" title="资料文件夹操作权限设置" href="roleController.do?dataDirBtnByRoleSelect&roleId=${roleId }"></t:tab>
</t:tabs>
