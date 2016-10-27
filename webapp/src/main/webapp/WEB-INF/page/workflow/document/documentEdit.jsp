<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

	<div class="easyui-panel" style="padding:5px;" fit="true" id="otherParamPanel">
		<form action="#" method="post" id="otherParamForm">	
			<input type="hidden" id="actDefId" name="actDefId" value="bxcs:1:4"/>
			<input type="hidden" id="actDefId" name="businessKey" value="34dsrthgssffdsds"/>
			<input type="hidden" id="address" name="v_S_address" value="奉新县人民政府"/>
			<input type="hidden" id="actDefId" name="businessName" value="发文审批"/>
			<input type="hidden" id="actDefId" name="v_I_startAgree" value="1"/>
			<input type="hidden" name="v_S_projectName" value="咨询受理">
			<input name="v_S_a">
			<table style="width:88%" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td class="td_title">
						<label class="Validform_label">
							归档时发送消息给发起人:
						</label>
					</td>
					<td class="value" >
					    <t:comboBox name="informStart" id="informStart" multiple="true" value="${definition.informStart}" data='[{"id":"innerMessage","text":"站内信"},{"id":"email","text":"邮件"},{"id":"sms","text":"短信"}]'></t:comboBox>
					</td>
					<td class="td_title">
						<label class="Validform_label">
							是否允许办结抄送:
						</label>
					</td>
					<td class="value" >
					    <t:comboBox name="allowFinishedCc" id="allowFinishedCc" value="${definition.allowFinishedCc}" data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'></t:comboBox>
					</td>
				</tr>
				<tr>
					<td class="td_title">
						<label class="Validform_label">
							办结抄送人员:
						</label>
					</td>
					<td class="value" >
					    <t:userSelect onlyAuthority="true" hiddenName="finishedCcId" displayName="finishedCcName" hiddenValue="${definition.finishedCcId}" displayValue="${definition.finishedCcName}"></t:userSelect>
					</td>
					<td class="td_title">
						<label class="Validform_label">
							全局消息类型:
						</label>
					</td>
					<td class="value" >
						<t:comboBox name="informType" id="informType" multiple="true" value="${definition.informType }" data='[{"id":"innerMessage","text":"站内信"},{"id":"email","text":"邮件"},{"id":"sms","text":"短信"}]'></t:comboBox>
					</td>
				</tr>
				
			</table>
		</form>
	</div>

<script type="text/javascript">


	$(function(){
		$("#otherParamPanel").panel({
			title : "流程其他参数设置",
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					$.ajax({
			 			cache : true,
			 			type : 'POST',
			 			url : "taskController.do?startFlow",// 请求的action路径
			 			data : $("#otherParamForm").serialize(),
			 			async : false,
			 			success : function(data) {
			 				var d = $.parseJSON(data);
			 				if (d.success) {
			 					var msg = d.msg;
			 					tip(msg);
			 				}
			 			}
			 		});
				}
			} ]
		});
	});
	
</script>