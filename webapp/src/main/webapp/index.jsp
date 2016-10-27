<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="cn">
<head>
	<meta charset="utf-8" />
	<title>重新登录页面</title>
</head>
<body>
	<script type="text/javascript">
	    //判断如果当前页面不为主框架，则将主框架进行跳转
	  	var tagert_URL = "<%=request.getContextPath()%>";
	    if(self==top){
	    	window.location.href = tagert_URL+"/loginController.do?login";
	    }else{
	    	top.location.href = tagert_URL+"/loginController.do?login";
	    }
	 </script>
</body>
</html>