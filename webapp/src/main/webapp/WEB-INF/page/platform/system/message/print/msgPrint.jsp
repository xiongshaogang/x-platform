<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<html>
<head>
<script type="text/javascript">
function annprint(){
	window.print();
}
</script>
</head>
 <body onload="annprint();">
 ${content }
 </body>
 </html>