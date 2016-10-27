<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:formvalid formid="formobj" gridId="flowFormList" action="flowFormController.do?saveFlowForm" >
	<input id="id" name="id" type="hidden" value="${flowForm.id }">
	<input id="url" name="url" type="hidden" value="${flowForm.url }">
	<textarea codemirror="true" mirrorheight="400px" id="description" name="description" rows="10" cols="80">${content}</textarea>
</t:formvalid>

<script type="text/javascript">
$(function(){
	setTimeout(InitMirror.init,InitMirror.options.initDelay);
});
</script>