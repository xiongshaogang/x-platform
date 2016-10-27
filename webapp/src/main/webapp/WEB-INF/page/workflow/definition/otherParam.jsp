<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/ckeditor/ckeditor_rule.js"></script>
<div class="easyui-layout" fit="true">
	<div region="center" border="false" style="padding: 5px;">
		<div class="easyui-panel" style="padding: 5px;" fit="true" id="otherParamPanel">
			<t:collapseTitle id="otherParam_div1" title="参数设置">
				<form action="#" method="post" id="otherParamForm">
					<input type="hidden" id="id" name="id" value="${definition.id}" />
					<table style="width: 88%" cellpadding="0" cellspacing="1" class="formtable">
						<tr>
							<td class="td_title" ><label class="Validform_label"> 是否允许转办:</label></td>
							<td class="value" ><t:comboBox name="allowDivert" id="allowDivert"
									value="${definition.allowDivert}" data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'></t:comboBox></td>
							<td class="td_title" ><label class="Validform_label"> 是否允许我的办结转发:</label></td>
							<td class="value" ><t:comboBox name="allowFinishedDivert"
									id="allowFinishedDivert" value="${definition.allowFinishedDivert}"
									data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'></t:comboBox></td>
						</tr>
						<tr>
							<td class="td_title"><label class="Validform_label"> 归档时发送消息给发起人: </label></td>
							<td class="value"><t:comboBox name="informStart" id="informStart" multiple="true"
									value="${definition.informStart}"
									data='[{"id":"innerMessage","text":"站内信"},{"id":"email","text":"邮件"},{"id":"sms","text":"短信"}]'></t:comboBox></td>
							<td class="td_title"><label class="Validform_label"> 是否允许办结抄送: </label></td>
							<td class="value"><t:comboBox name="allowFinishedCc" id="allowFinishedCc"
									value="${definition.allowFinishedCc}" data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'></t:comboBox></td>
						</tr>
						<tr>
							<td class="td_title"><label class="Validform_label"> 是否跳过第一个结点: </label></td>
							<td class="value"><t:comboBox name="skipFirstNode" id="skipFirstNode" value="${definition.skipFirstNode}"
									data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'></t:comboBox></td>
							<td class="td_title"><label class="Validform_label"> 全局消息类型: </label></td>
							<td class="value"><t:comboBox name="informType" id="informType" multiple="true"
									value="${definition.informType }"
									data='[{"id":"innerMessage","text":"站内信"},{"id":"email","text":"邮件"},{"id":"sms","text":"短信"}]'></t:comboBox></td>
						</tr>
						<%-- <tr>
							<td class="td_title"><label class="Validform_label"> 节点任务名称规则: </label></td>
							<td class="value formUrlGlobal" colspan="3">
								<textarea id="taskNameRule" row="2" name="taskNameRule">${definition.taskNameRule }</textarea>
							</td>
						</tr> --%>
					</table>
				</form>
			</t:collapseTitle>
			<div style="margin-top:3px;margin-bottom:3px;">
				<t:collapseTitle id="otherParam_div2" title="办结抄送人列表">
					<div style="height:200px;">
						<t:datagrid border="false" name="endCCUserList" pagination="false" checkbox="false"
							actionUrl="nodeUserController.do?datagrid&defId=${defId}&funcType=endCCUser">
							<t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="120"></t:dgCol>
							<t:dgCol title="是否设置用户" field="flag" hidden="false" queryMode="single" width="120"></t:dgCol>
							<t:dgCol title="分配类型" field="assignTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
							<t:dgCol title="分配名称" field="assignNames" hidden="true" queryMode="single" width="120"></t:dgCol>
							<t:dgCol title="计算类型" field="countTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
							<t:dgCol title="操作" field="opt" width="100"></t:dgCol>

							<t:dgOpenOpt exp="flag#eq#true" title="编辑" icon="glyphicon glyphicon-pencil icon-color"
								url="nodeUserController.do?nodeUserEdit&id={id}&gridId=endCCUserList" exParams="{optFlag:'add',formId:'nodeUser_form'}"
								preinstallWidth="1" height="400"></t:dgOpenOpt>
							<t:dgOpenOpt exp="flag#eq#true" title="查看" icon="glyphicon glyphicon-search icon-color"
								url="nodeUserController.do?nodeUserEdit&id={id}&optFlag=detail" exParams="{optFlag:'detail'}"
								preinstallWidth="1" height="400"></t:dgOpenOpt>
							<t:dgDelOpt exp="flag#eq#true" title="删除" icon="glyphicon glyphicon-remove icon-color" url="nodeUserController.do?delete&id={id}" />

							<t:dgToolBar title="添加" preinstallWidth="1" height="300" icon="glyphicon glyphicon-plus icon-color"
								exParams="{formId:'nodeUser_form'}"
								url="nodeUserController.do?nodeUserEdit&defId=${defId}&funcType=endCCUser&gridId=endCCUserList" funname="add"></t:dgToolBar>
						</t:datagrid>
					</div>
				</t:collapseTitle>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	/* if (CKEDITOR.instances['taskNameRule']) {
		CKEDITOR.remove(CKEDITOR.instances['taskNameRule']);
	}
	var taskNameRuleEditor = ckeditor('taskNameRule'); */

	$(function() {
		$("#otherParamPanel").panel({
			title : "流程实例设置",
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					/* $('#taskNameRule').val(taskNameRuleEditor.getData()); */
					$.ajax({
						cache : true,
						type : 'POST',
						url : "definitionController.do?saveOtherParam",// 请求的action路径
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