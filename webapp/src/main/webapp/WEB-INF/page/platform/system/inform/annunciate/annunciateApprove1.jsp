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
$(function(){
	$(".panel").css("overflow","hidden");
	$(".panel-body").css("overflow","hidden");
});
function setType(self){
	var val = $(self).val();
	if(val == 0){
		$("#conclusionApprove").val("2");
	}else{
		$("#conclusionApprove").val("1");
	}
}
function showOffice(){
	if (typeof(window.frames['office_iframe'].contentWindow) == "undefined") { 
		window.frames["office_iframe"].showAll();
	}else{
		window.frames["office_iframe"].contentWindow.showAll();
	}  
}
function printOffice(){
	if (typeof(window.frames['office_iframe'].contentWindow) == "undefined") { 
		window.frames["office_iframe"].printOffice();
	}else{
		window.frames["office_iframe"].contentWindow.printOffice();
	}  
}
</script>
<div>
	<t:tabs id="taskApproveTabs" fit="false" height="550" border="true" hBorderBottom="true" hBorderLeft="false"
		hBorderRight="false" hBorderTop="false" leftDiv="true" leftDivWidth="70" leftDivTitle="审批" rightDiv="true"
		rightDivWidth="80" >
		<div title="审批事项">
			<form id="approveForm">
			<input type="hidden" id="conclusionApprove" name="conclusionApprove" value="">
			<%-- <iframe src="messageController.do?printFile&id=${businessKey }&name=annunciateTemplate.doc"  style="width: 100%;height: 240px;padding:0px;border:0px;" id="office_iframe">
			
			</iframe> --%>
			<div id="approveContentDiv" class="approveContentDiv">
				<table cellpadding="0" cellspacing="1" class="approveContentTable">
				    <tr >
						<td style="width: 100%;height: 30px;">&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showOffice()">查看正文</a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="printOffice()">打印</a>
						<iframe src="messageController.do?printFile&id=${businessKey }&name=annunciateTemplate.doc"  style="width: 0;height: 0;padding:0px;border:0px;" id="office_iframe" name="office_iframe">
			            </iframe>
						</td>
					</tr>
					<tr>
						<td class="approve_table_title" style="width: 100%">意见</td>
					</tr>
					<tr>
						<td style="height: 350px;vertical-align: top;"><textarea class="approve_table_opnion" id="voteContent" name="voteContent">${opinion}</textarea></td>
					</tr>
					<tr>
						<td colspan="2" class="approve_table_pt">
							<span class="approve_conclusion"> 结论: <t:comboBox id="v_S_gmStatus" name="v_S_gmStatus" data='[{"id":"2","text":"同意"},{"id":"1","text":"同意但须修改"},{"id":"0","text":"不同意"}]' onChange="setType(self)"></t:comboBox></span> 
							<span class="approve_person" style="margin-left:430px;">审核/批人:${curUser.userTypeName}</span> <span class="approve_time"> 时间: <fmt:formatDate value="${nowDate}" pattern="yyyy-MM-dd HH:mm" />
						</span>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</t:tabs>
</div>
