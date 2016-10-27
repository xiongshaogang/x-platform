<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<html>
<head>
<script type="text/javascript" src="plug-in/office/OfficeContorlFunctions.js"></script>
<script type="text/javascript" src="plug-in/office/ntkoofficecontrol.js"></script>
<script type="text/javascript">
	function loadOffice() {
		intializePage("${officeFileRequest}");
	}

	function OnComplete3() {
		setFilePrint(false);
		setFileNew(false);
		setFileSaveAs(false);
		setIsNoCopy(false);
		setFileSave(false);
		setFileClose(false);
		setFileOpen(false);
		OFFICE_CONTROL_OBJ.SetReadOnly(true,'bySummer');
	}
</script>
<script language="JScript" for="TANGER_OCX" event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
	
	setFilePrint(false);
	setFileNew(false);
	setFileSaveAs(false);
	setIsNoCopy(false);
	setFileSave(false);
	setFileClose(false);
	setFileOpen(false);
	OFFICE_CONTROL_OBJ.SetReadOnly(true);
</script>
</head>
<body onload="loadOffice();">


</body>