<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:formvalid gridId="${gridId}" formid="nodeUser_form" action="nodeUserController.do?saveNodeUser"
	beforeSubmit="nodeUserEdit.saveScript">
	<input type="hidden" id="defId" name="defId" value="${nodeUser.defId}" />
	<input type="hidden" id="funcType" name="funcType" value="${nodeUser.funcType}" />
	<input type="hidden" id="id" name="id" value="${nodeUser.id}" />
	<input type="hidden" id="assignIds" name="assignIds" value="${nodeUser.assignIds}" />
	<table cellpadding="0" cellspacing="1" class="formtable">
		<c:if test="${nodeUser.funcType=='nodeUser'}">
		<tr>
			<td class="td_title"><label class="Validform_label"> 任务节点: </label></td>
			<td class="value"><t:comboBox id="nodeId" name="nodeId" data='${nodeData}' value="${nodeUser.nodeId}"></t:comboBox>
			</td>
		</tr>
		</c:if>
		<c:if test="${nodeUser.funcType!='nodeUser'}">
		<input type="hidden" id="nodeId" name="nodeId" value="${nodeUser.nodeId}" />
		</c:if>	
		<tr>
			<td class="td_title"><label class="Validform_label"> 人员分配类型: </label></td>
			<td class="value"><t:comboBox id="assignType" name="assignType"
					onChange="nodeUserEdit.userTypeChange(newValue,oldValue,self)"
					data='[{"id":"user","text":"用户"},{"id":"role","text":"角色"},{"id":"org","text":"组织机构"},{"id":"startUser","text":"流程发起人"},{"id":"startUserDirectLeader","text":"流程发起人直接领导"},{"id":"taskExecutor","text":"节点任务执行人"},{"id":"manualExecutor","text":"自定义脚本执行人"}]'
					value="${nodeUser.assignType}"></t:comboBox></td>
		</tr>
		<tr>
			<!-- 人员 -->
			<td class="td_title user" style="display: none;"><label class="Validform_label"> 人员选择: </label></td>
			<td class="value user" style="display: none;"><t:userSelect width="800" hiddenName="userIds" displayName="userNames"></t:userSelect>
			</td>
			<!-- 角色 -->
			<td class="td_title role" style="display: none;"><label class="Validform_label"> 角色选择: </label></td>
			<td class="value role" style="display: none;"><t:roleSelect hiddenName="roleUserIds" displayName="roleUserNames"></t:roleSelect>
			</td>
			<!-- 机构 -->
			<td class="td_title org" style="display: none;"><label class="Validform_label"> 机构部门选择: </label></td>
			<td class="value org" style="display: none;"><t:orgSelect id="orgUserIds" name="orgUserIds" multiple="true"
					onlyLeafCheck="true"></t:orgSelect></td>
			<!-- 流程发起人 -->
			<td class="td_title startUser" style="display: none;"><label class="Validform_label"> 流程发起人: </label></td>
			<td class="value startUser" style="display: none;"><input type="text" class="inputxt" disabled="disabled"></td>
			<!-- 节点任务执行人 -->
			<td class="td_title taskExecutor" style="display: none;"><label class="Validform_label"> 节点任务执行人: </label></td>
			<td class="value taskExecutor" style="display: none;"><t:comboBox multiple="false" id="taskExecutor"
					name="taskExecutor" data='${nodeData}'></t:comboBox></td>
			<!--自定义脚本执行人 -->
			<td class="td_title manualExecutor" style="display: none;"><label class="Validform_label"> 自定义脚本执行人: </label></td>
			<td class="value manualExecutor" style="display: none;">
				<div style="background: #F7F7F7;">
					<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('manualExecutor')">常用脚本</button>
					<textarea mirrorheight="135px" id="manualExecutor" name="manualExecutor" class="input_area"></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 计算类型: </label></td>
			<td class="value"><t:comboBox multiple="false" id="countType" name="countType"
					data='[{"id":"and","text":"与"},{"id":"or","text":"或"},{"id":"not","text":"非"}]' value="${nodeUser.countType}"></t:comboBox>
			</td>
		</tr>
	</table>
</t:formvalid>

<script type="text/javascript">
	var nodeUserEdit = {
		saveScript : function() {
			$("#manualExecutor").val(InitMirror.getValue("manualExecutor"));
		},
		userTypeChange : function(newValue, oldValue, self) {
			if (oldValue == "") {
				$(".user").hide();
			} else {
				$("." + oldValue).hide();
			}
			$("." + newValue).show();
		}
	};
	$(function() {
		var assignNames = "${nodeUser.assignNames}";
		var assignIds = $("#assignIds").val();
		var userType = "${nodeUser.assignType}";
		$.parser.onComplete = function() {
			if (userType == "" || userType == null) {
				$(".user").show();
			} else {
				if (userType == "user") {
					$("#userIds").val(assignIds);
					$("#userNames").val(assignNames);
				} else if (userType == "role") {
					$("#roleUserIds").val(assignIds);
					$("#roleUserNames").val(assignNames);
				} else if (userType == "job") {
					$("#jobUserIds").val(assignIds);
					$("#jobUserNames").val(assignNames);
				} else if (userType == "dept") {
					$("#orgUserIds").combotree("setValues", assignIds.split(","));
				} else if (userType == "manualExecutor") {
					$("#manualExecutor").val(assignIds);
				} else {
					$("#" + userType).combobox("setValues", assignIds.split(","));
				}
				$("." + userType).show();
			}
			$.parser.onComplete = mainComplete;
		};
		setTimeout('InitMirror.initId("manualExecutor")', InitMirror.options.initDelay);
	})
</script>
