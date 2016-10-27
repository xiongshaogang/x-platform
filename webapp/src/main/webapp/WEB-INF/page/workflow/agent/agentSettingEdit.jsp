<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
    function setUserType(){
  	  createwindow("员工选择", "userController.do?empSelect", 900,450);
    }
</script>
 <t:formvalid formid="formobj" gridId="agentSettingList" action="agentSettingController.do?saveAgentSetting" beforeSubmit="saveConExp">
	<input id="id" name="id" type="hidden" value="${agentSetting.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>代理名称:
				</label>
			</td>
			<td class="value">
			    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt" oldValue='${agentSetting.name}' value='${agentSetting.name}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>代理编码:
				</label>
			</td>
			<td class="value">
			    <input id="code" name="code" type="text" class="inputxt" datatype="*1-32" ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.workflow.agent.entity.AgentSettingEntity" oldValue='${agentSetting.code}' value='${agentSetting.code}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>授权人:
				</label>
			</td>
			<td class="value">
			    <t:userSelect onlyAuthority="true" hiddenName="authId" displayName="authName" displayValue="${agentSetting.authName}" hiddenValue="${agentSetting.authId}" multiples="false"></t:userSelect>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>代理人:
				</label>
			</td>
			<td class="value">
				<t:userSelect onlyAuthority="true" hiddenName="agentId" displayName="agentName" displayValue="${agentSetting.agentName}" hiddenValue="${agentSetting.agentId}" multiples="false"></t:userSelect>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>代理类型:
				</label>
			</td>
			<td class="value">
			    <t:comboBox datatype="*" value='${agentSetting.type}' name="type" id="type" data='[{"id":"0","text":"全部代理"},{"id":"1","text":"部分代理"},{"id":"2","text":"条件代理"}]'></t:comboBox>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>是否有效:
				</label>
			</td>
			<td class="value">
				<t:comboBox datatype="*" value='${agentSetting.status}' name="status" id="status" data='[{"id":"Y","text":"启用"},{"id":"N","text":"禁用"}]'></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>开始时间:
				</label>
			</td>
			<td class="value">
				<t:datetimebox value='${agentSetting.startTime}' name="startTime" id="startTime" type="datetime"></t:datetimebox>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>结束时间:
				</label>
			</td>
			<td class="value">
				<t:datetimebox value='${agentSetting.endTime}' name="endTime" id="endTime" type="datetime"></t:datetimebox>
			</td>
		</tr>
		
		
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					备注:
				</label>
			</td>
			<td class="value" colspan="3">
				<textarea id="description" name="description" style="width:98%;" rows="3">${agentSetting.description}</textarea>
			</td>
		</tr>
		<tr >
		    <td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>任务是否保留:
				</label>
			</td>
			<td class="value">
			 <t:comboBox dictCode="YNType" value="${agentSetting.selfReceive}" name="selfReceive" id="selfReceive" ></t:comboBox>
			</td>
			<td class="td_title flowDefinition" >
				<label class="Validform_label">
					<span style="color:red">*</span>代理流程:
				</label>
			</td>
			<td class="value flowDefinition" >
			<t:commonSelect displayName="flowName" hiddenName="flowId"  displayValue="${ flowName}" hiddenValue="${flowId }" gridTreeFilter="typeId" hasTree="true" multiples="true" treeUrl="typeController.do?typeRoleTreeBySysTypeTree@@sysType=workflow"
			url="definitionController.do?datagrid" height="500" width="800" >
			<t:csField title="主键" field="id" backField="flowId" hidden="false" ></t:csField>
			<t:csField title="流程名称" field="name" backField="flowName" hidden="true" ></t:csField>
			<t:csField title="流程编码" field="code" backField="flowCode" hidden="true" ></t:csField>
			<t:csField title="所属类型" field="type.name"  hidden="true" ></t:csField>
			<t:csField title="版本号" field="version"  hidden="true" ></t:csField>
			</t:commonSelect>
			    <%-- <t:comboBox datatype="*" value='${agentSetting.type}' name="type" id="type" data='[{"id":"1","text":"全部代理"},{"id":"2","text":"部分代理"},{"id":"3","text":"条件代理"}]'></t:comboBox> --%>
			</td>
			<%-- <td class="td_title condition">
				<label class="Validform_label">
					<span style="color:red">*</span>代理条件:
				</label>
			</td>
			<td class="value condition">
				<t:comboBox datatype="*" value='${agentSetting.status}' name="status" id="status" data='[{"id":"Y","text":"启用"},{"id":"N","text":"禁用"}]'></t:comboBox>
			</td> --%>
		</tr>
		<tr>
		    <td class="td_title condition">
				<label class="Validform_label">
					<span style="color:red">*</span>代理条件:
				</label>
			</td>
			<td class="value condition" style="width: 350px" colspan="3"><div style="background: #F7F7F7;">
			<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('conExp')">常用脚本</button>
					<textarea codemirror="true" mirrorheight="180px" id="conExp" name="conExp" class="input_area">${agentSetting.conExp}</textarea>
			</div></td>
		
		</tr>
	</table>
</t:formvalid>
<script type="text/javascript">
	
	$(document).ready(function(){
		var type="${agentSetting.type}";
		if(type=="0"){
			$(".flowDefinition").hide();
			$(".condition").hide();
		}else if(type=="1"){
			$(".flowDefinition").show();
			$(".condition").hide();
		}else if(type=="2"){
			$(".flowDefinition").show();
			$(".condition").show();
		}else{
			$(".flowDefinition").hide();
			$(".condition").hide();
		}
		setTimeout('InitMirror.initId("conExp")', InitMirror.options.initDelay);
	});
	
	$("#type").combobox({
		onChange: function(newValue,oldValue){
			if(newValue=="0"){
				$(".flowDefinition").hide();
				$("#flowId").val("");
				$("#flowName").val("");
				$("#conExp").val("");
				InitMirror.setValue("conExp",""); 
				$(".condition").hide();
			}else if(newValue=="1"){
				$(".flowDefinition").show();
				$("#conExp").val("");
				InitMirror.setValue("conExp",""); 
				$(".condition").hide();
			}else if(newValue=="2"){
				$(".flowDefinition").show();
				$(".condition").show();
			}
		}
	});
	
	function saveConExp(){
		$("#conExp").val(InitMirror.getValue("conExp"));
	}
</script>
