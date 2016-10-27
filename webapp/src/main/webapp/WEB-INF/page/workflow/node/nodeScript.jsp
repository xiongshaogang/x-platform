<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
#nodeScriptformobj .form_div {
    	margin: 0px auto;
    	width: 100%;
    }
</style>
<script type="text/javascript">
$(function(){
	setTimeout('InitMirror.initId("preScript")', InitMirror.options.initDelay);
	if('${param.type}' == 'userTask' ||  '${param.type}' == 'multiUserTask'){
		setTimeout('InitMirror.initId("rearScript")', InitMirror.options.initDelay);
		setTimeout('InitMirror.initId("allotScript")', InitMirror.options.initDelay);
	} 
	$(".ucg-dialog-titletab").parent(".form_div").addClass("width-100");
});

function close1(){
	closeD($("#formobj").closest(".window-body"));
}

function saveScript(){
	//InitMirror.save();
	$("#preScript").val(InitMirror.getValue("preScript"));
	if('${param.type}' == 'userTask' ||  '${param.type}' == 'multiUserTask'){
		$("#rearScript").val(InitMirror.getValue("rearScript"));
		$("#allotScript").val(InitMirror.getValue("allotScript"));
	} 
}
</script>
<t:formvalid formid="nodeScriptformobj" action="nodeScriptController.do?saveNodeScript" beforeSubmit="saveScript">
		<input type="hidden" name="actDefId" value="${param.actDefId}" />
	    <input type="hidden" name="nodeId" value="${param.nodeId}" />
	    <input type="hidden" name="type" value="${param.type}" />
	    <div style="overflow: hidden;">
 			<t:tabs id="nodeScriptTab" hBorderLeft="false" hBorderTop="false" hBorderRight="false"  fit="false" hBorderBottom="true" leftDiv="true" leftDivWidth="90" leftDivTitle="脚本设置" rightDiv="true" closeBtn="true">
     	          <div  title="<c:choose>
						<c:when test="${param.type == 'userTask' ||  param.type == 'multiUserTask'}"> 前置脚本</c:when>
						<c:when test="${param.type == 'startEvent' }">开始脚本</c:when>
						<c:when test="${param.type == 'endEvent' }">结束脚本</c:when>
						<c:when test="${param.type == 'script' }">脚本节点</c:when>
						</c:choose>" fit="true" id="preScript1" style="height: 328px;">
 					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 100%">
					<tr>
						<td width="40px" align="right"><label class="Validform_label"> 脚本：</label></td>
					    <td class="value" ><div style="background: #F7F7F7;width: 90%">
					    <button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('preScript')">常用脚本</button>
						<textarea codemirror="true" mirrorheight="180px" id="preScript" name="preScript" class="input_area">${pre.script}</textarea>
					</div></td>
					</tr>
					
					</table>
 					</div> 
 					
 					<c:if test="${param.type == 'userTask' ||  param.type == 'multiUserTask'}">
 					<div  title="后置脚本" fit="true" id="rearScript1" >
 					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 100%">
					<tr>
						<td width="40px" align="right"><label class="Validform_label"> 脚本：</label></td>
					    <td class="value" ><div style="background: #F7F7F7;width: 90%">
					    <button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('rearScript')">常用脚本</button>
						<textarea codemirror="true" mirrorheight="180px" id="rearScript" name="rearScript" class="input_area">${rear.script}</textarea>
					</div></td>
					</tr>
					</table>
 					</div> 
 					<div  title="分配脚本" fit="true" id="allotScript1">
 					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 100%">
					<tr>
						<td width="40px" align="right"><label class="Validform_label"> 脚本：</label></td>
					    <td class="value" ><div style="background: #F7F7F7;width: 90%">
					    <button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('allotScript')">常用脚本</button>
						<textarea codemirror="true" mirrorheight="180px" id="allotScript" name="allotScript" class="input_area">${allot.script}</textarea>
					</div></td>
					</tr>
					</table> 
 					</div> 
				</c:if>
				</t:tabs>
				</div>
		</t:formvalid>
