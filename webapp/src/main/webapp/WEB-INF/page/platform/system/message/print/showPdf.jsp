<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="plug-in/pdf/pdfobject.js"></script>
<script type="text/javascript">
window.onload = function (){
	var name = '${name}';
	var u = "userfiles/pdf/" + name;
	document.getElementById("ad").href = u;
	if ((navigator.userAgent.indexOf('MSIE') >= 0 || navigator.userAgent.indexOf('Trident') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){//ie
		document.getElementById("ad").click();
	}else if (navigator.userAgent.indexOf('Firefox') >= 0 || navigator.userAgent.indexOf('Chrome') >= 0){
		var myPDF = new PDFObject({url: u }).embed();
	}
};
</script>

</head>

<body>

<p>没有Adobe Reader或PDF支持web浏览器。 <a href="userfiles/pdf/sample.pdf" id="ad">点击这里下载PDF</a></p>

</body>
</html>