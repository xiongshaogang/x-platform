<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.projectDetail {
	width: 100%;
	height: 160px;
}

.project_title {
	padding-left: 10px;
	border: 1px solid #d3d3d3;
	height: 30px;
	line-height: 30px;
}

.project_left {
	border-right: 0.5px dashed #fff;
}

.project_left,.project_right {
	width: 49.9%;
	height: 130px;
	text-align: center;
	line-height: 130px;
	float: left;
	background-color: #33c0e0;
	color: #fff;
}
</style>
<script type="text/javascript">
function nextProcess(){
	saveObj();
}
$(function(){
	$(".panel").css("overflow","hidden");
	$(".panel-body").css("overflow","hidden");
});
</script>
<div>
	<t:tabs id="taskApproveTabs" fit="false" height="550" border="true" hBorderBottom="true" hBorderLeft="false"
		hBorderRight="false" hBorderTop="false" leftDiv="true" leftDivWidth="70" leftDivTitle="内容修改" rightDiv="true"
		rightDivWidth="80">
	<t:tab  title="内容修改" id="return" href="annunciateController.do?annunciateEdit&id=${businessKey }&optFlag=return" >
	<form id="approveForm">
	<input type="hidden" id="v_S_pmApprove" name="v_S_pmApprove" value="">
	<input type="hidden" id="conclusionApprove" name="conclusionApprove" value="1">
	<input type="hidden" id="taskId" name="taskId" value="${task.id }"/>
	</form>
	</t:tab>
	</t:tabs>
</div>
