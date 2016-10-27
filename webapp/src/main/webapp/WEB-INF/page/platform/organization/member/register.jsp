<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<!DOCTYPE html>
<html>
 <head>
  <title>会员信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
 <div >
  <c:if test="${not empty param.email}">
        <p style="color: red;font-size: 15px;">
        	恭喜您注册成功!<br>请尽快登录您的邮箱（${param.email}）查收系统发送的账号激活邮件进行激活，账号激活后可通过系统首页登录系统。<br>
			<font size="2pt">提示：如长时间（半个小时后）无法收到系统发送的激活邮件，请检查垃圾邮件或更换电子邮箱重新注册，如有疑问，请联系技术支持110。</font>
			<br>
        </p>
   </c:if>
    <c:if test="${empty param.email }">
        ${msg }
   </c:if>
      </div>
 </body>