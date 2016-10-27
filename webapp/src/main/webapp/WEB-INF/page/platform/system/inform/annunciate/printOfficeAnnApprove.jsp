<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<html>
<head>
<script type="text/javascript" src="plug-in/office/OfficeContorlFunctions.js"></script>
<script type="text/javascript">
function loadOffice(){
	intializePage('${webRoot}'+"/ntko/templateFile/annApprove.doc");
}
function OnComplete3(){
	    replaceBookMarkSingle("title",'${map.title}');
	    replaceBookMarkSingle("reference",'${map.reference}');
	    replaceBookMarkSingle("now_time",'${map.now_time}');
	    replaceBookMarkSingle("orgName",'${map.orgName}');
	    replaceBookMarkSingle("userName",'${map.userName}');
	    replaceBookMarkSingle("userName1",'${map.userName}');
	    replaceBookMarkSingle("deptName",'${map.deptName}');
	    if('${map.emergency}' == '0'){
	    	replaceBookMarkSingle("emergency1",'■');
	    }
	    if('${map.emergency}' == '1'){
	    	replaceBookMarkSingle("emergency2",'■');
	    }
	    if('${map.emergency}' == '2'){
	    	replaceBookMarkSingle("emergency3",'■');
	    }
	    replaceBookMarkSingle("pmopinion",'${map.pm }');
	    replaceBookMarkSingle("pmdate",'${fn:substring(map.pmt,0,10) }');
	    replaceBookMarkSingle("gmopinion",'${map.gm }');
	    replaceBookMarkSingle("gmdate",'${fn:substring(map.gmt,0,10) }');
	    replaceBookMarkSingle("remark",'■');
	    setFileNew(false);
	    setFileSaveAs(false);
	    //setIsNoCopy(true);
	    OFFICE_CONTROL_OBJ.FileOpen=false;
	    OFFICE_CONTROL_OBJ.FileClose=false;
	    OFFICE_CONTROL_OBJ.FileSave=false;
	    //OFFICE_CONTROL_OBJ.SetReadOnly(true);
}
function showAll(){
	OFFICE_CONTROL_OBJ.FullScreenMode=true;
}

function printOffice(){
	OFFICE_CONTROL_OBJ.PrintOut(true);
}
</script>
</head>
 <body onload="loadOffice();" style="position: relative;z-index: -1;">
<!-- <input type="button" name="aa" onclick="doProtect()" value="禁用"/> -->
<input type="hidden" id="busId" name="busId" value="">
<input type="hidden" id="officeFile" name="officeFile" value="upLoadFile"/><!-- 文件域id值，用于saveFileToUrl第二个参数 -->
<input type="hidden" id="htmlFile" name="htmlFile" value="uploadHtml"/><!-- 文件域id值，用于saveFileAsHtmlToUrl第二个参数 -->
<div style="width: 100%;height: 500px;">
<script type="text/javascript" src="plug-in/office/ntkoofficecontrol.js"></script>
<script language="JScript" for="TANGER_OCX" event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
	replaceBookMarkSingle("title",'${map.title}');
	replaceBookMarkSingle("reference",'${map.reference}');
	replaceBookMarkSingle("now_time",'${map.now_time}');
	replaceBookMarkSingle("orgName",'${map.orgName}');
	replaceBookMarkSingle("userName",'${map.userName}');
	replaceBookMarkSingle("userName1",'${map.userName}');
	replaceBookMarkSingle("deptName",'${map.deptName}');
	if('${map.emergency}' == '0'){
		replaceBookMarkSingle("emergency1",'■');
	}
	if('${map.emergency}' == '1'){
		replaceBookMarkSingle("emergency2",'■');
	}
	if('${map.emergency}' == '2'){
		replaceBookMarkSingle("emergency3",'■');
	}
	replaceBookMarkSingle("pmopinion",'${map.pm }');
    replaceBookMarkSingle("pmdate",'${fn:substring(map.pmt,0,10) }');
    replaceBookMarkSingle("gmopinion",'${map.gm }');
    replaceBookMarkSingle("gmdate",'${fn:substring(map.gmt,0,10) }');
    replaceBookMarkSingle("remark",'■');
	setFileNew(false);
	setFileSaveAs(false);
	//setIsNoCopy(true);
	 OFFICE_CONTROL_OBJ.FileOpen=false;
	 OFFICE_CONTROL_OBJ.FileClose=false;
	 OFFICE_CONTROL_OBJ.FileSave=false;
	//OFFICE_CONTROL_OBJ.SetReadOnly(true);
</script>
</div>
 </body>
 </html>