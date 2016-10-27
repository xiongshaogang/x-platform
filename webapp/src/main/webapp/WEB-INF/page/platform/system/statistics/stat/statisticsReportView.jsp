<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report" %>

<script type="text/javascript" src="plug-in/report/ucgReport.js"></script>
<link rel="stylesheet" href="plug-in/assets/css/report.css" type="text/css" />

<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
  <div region="center"  style="border:0px">
  <div align="center" >
  <a href="#" class="ico_pdf"  onclick="detailForm_saveAsPdf()" title="下载PDF"></a>
  <a href="#" class="ico_word"  onclick="detailForm_saveAsWord()" title="下载WORD"></a>
  <a href="#" class="ico_excel"  onclick="detailForm_saveAsExcel()" title="下载EXCEL"></a>
  <br/>
  <!--  params="v_prp_code=${v_prp_code};v_org_code=${v_org_code};v_role_id=${v_role_id}" -->					
	<report:html name="detailForm" reportFileName="${dse.value }"
			width="-1"
			height="-1"
			funcBarLocation="boTh"
			functionBarColor="#fff5ee"
			funcBarFontSize="9pt"
			funcBarFontColor="blue"
			separator="&nbsp;&nbsp;"
			needSaveAsExcel="no"
			needSaveAsPdf="no"
			needPrint="no"
			printLabel="打印"
			saveAsName="${dse.description }"
			params=""
			needPageMark="no"
			firstPageLabel="<span class='pageinfo'>[首 &nbsp;页]</span>" 
			prevPageLabel="<span class='pageinfo'>上一页</span>" 
			nextPageLabel="<span class='pageinfo'>下一页</span>" 
			lastPageLabel="<span class='pageinfo'>[最后页]</span>"
			pageMarkLabel="<span class='pageinfo'>第{currPage}页 共{totalPage}页</span>"					
			displayNoLinkPageMark="yes"
			needSaveAsWord="true"
		/>
		 </div>
  </div>
 </div>
<script type="text/javascript">
$(function(){
	 redrawEasyUI($(".easyui-layout"));
    });
</script>



