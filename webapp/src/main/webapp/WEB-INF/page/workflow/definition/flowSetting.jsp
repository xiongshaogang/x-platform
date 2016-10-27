<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="easyui-panel" data-options="fit:true,border:false">
		<t:tabs id="flowSetting"  border="false" hBorderBottom="false" hBorderLeft="false" hBorderRight="false" hBorderTop="false"
			leftDiv="true" leftDivWidth="70" leftDivTitle="流程配置" rightDiv="true" closeBtn="true">
			<t:tab title="流程明细" id="detail" href="definitionController.do?detail&id=${defId}"></t:tab>
			<t:tab title="节点配置" id="nodeSet" href="definitionController.do?nodeSet&id=${defId}"></t:tab>
			<t:tab title="人员选择" id="user" href="nodeUserController.do?nodeUser&defId=${defId}"></t:tab>
			<t:tab title="表单选择" id="form" href="nodeSetController.do?nodeForm&id=${defId}"></t:tab>
			<%-- <t:tab title="历史版本" id="version" href="definitionController.do?definition&isMain=0&id=${defId}"></t:tab> --%>
			<t:tab title="实例配置" id="params" href="definitionController.do?otherParam&id=${defId}"></t:tab>
		</t:tabs>
	</div>

	<script type="text/javascript">
		$(function() {
			var close_btn = $("#flowSetting .tabs-header .close-btn");
			close_btn.click(function() {
				closeD(parent.getD(parent.$("#flowSettingDialog")));
			});
		});
	</script>
</body>
</html>