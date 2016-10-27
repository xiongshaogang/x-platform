<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/ckeditor/ckeditor_sysTemp.js"></script>
<script type="text/javascript">
	$(function() {
		getD($("#nodeInfoType_table")).dialog("setTitle", "${bpmNodeSet.nodeName}节点通知类型");
		$("#nodeInfoType_div0").closest(".dialog-content").css("overflow-x","hidden");
		setTimeout(
				'$("#nodeInfoType_div3 .cke_contents,#nodeInfoType_div2 .cke_contents,#nodeInfoType_div6 .cke_contents,#nodeInfoType_div7 .cke_contents").css("height","103px")',
				1000);

	});
	if (CKEDITOR.instances['preMailContent']) {
		CKEDITOR.remove(CKEDITOR.instances['preMailContent']);
	}
	var preMailContent = ckeditor('preMailContent');

	if (CKEDITOR.instances['preInnerContent']) {
		CKEDITOR.remove(CKEDITOR.instances['preInnerContent']);
	}
	var preInnerContent = ckeditor('preInnerContent');

	if (CKEDITOR.instances['lastMailContent']) {
		CKEDITOR.remove(CKEDITOR.instances['lastMailContent']);
	}
	var lastMailContent = ckeditor('lastMailContent');

	if (CKEDITOR.instances['lastInnerContent']) {
		CKEDITOR.remove(CKEDITOR.instances['lastInnerContent']);
	}
	var lastInnerContent = ckeditor('lastInnerContent');

	var nodeInfoType = {
		saveContent : function() {
			$('#preMailContent').val(preMailContent.getData());
			$('#preInnerContent').val(preInnerContent.getData());
			$('#lastMailContent').val(lastMailContent.getData());
			$('#lastInnerContent').val(lastInnerContent.getData());
		}
	}
</script>
	<t:formvalid formid="formobj" action="nodeSetController.do?saveInfoType" beforeSubmit="nodeInfoType.saveContent">
		<input type="hidden" name="actDefId" value="${bpmNodeSet.actDefId}" />
		<input type="hidden" name="nodeId" value="${bpmNodeSet.nodeId}" />
		<div style="margin-top: 3px; width: 657px;">
			<t:collapseTitle id="nodeInfoType_div0" title="基本信息">
				<table cellpadding="0" cellspacing="1" class="formtable" id="nodeInfoType_table">
					<tr>
						<td class="td_title"><label class="Validform_label">通知方式 :</label></td>
						<td class="value"><t:comboBox id="nodeInfo_informType" name="informType" multiple="true"
								dictCode="noticeType" value="${bpmNodeSet.informType}"></t:comboBox></td>
						<%-- <td class="td_title"><label class="Validform_label">手机审批 : </label></td>
						<td class="value"><t:comboBox name="globalFormType" id="globalFormType" value="${bpmNodeSet.isAllowMobile}"
								data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'></t:comboBox></td> --%>
						<td class="td_title"><label class="Validform_label">执行人接收: </label></td>
						<td class="value"><t:comboBox name="isInform" id="isInform" value="${bpmNodeSet.isInform}"
								data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'></t:comboBox></td>		
					</tr>
				</table>
			</t:collapseTitle>
		</div>
		<div style="margin-top: 3px; height: 260px; width: 657px;">
			<t:tabs id="nodeInfoType_tab1" fit="true">
				<div title="前置抄送人" id="nodeInfoType_div1">
					<t:datagrid name="preNoticeUserList" pagination="false" checkbox="false"
						actionUrl="nodeUserController.do?datagrid&defId=${defId}&nodeId=${bpmNodeSet.nodeId}&funcType=preNoticeUser">
						<t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="是否设置用户" field="flag" hidden="false" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="分配类型" field="assignTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="分配名称" field="assignNames" hidden="true" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="计算类型" field="countTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="操作" field="opt" width="100"></t:dgCol>

						<t:dgOpenOpt title="编辑" icon="awsm-icon-edit blue"
							url="nodeUserController.do?nodeUserEdit&id={id}&gridId=preNoticeUserList"
							exParams="{optFlag:'add',formId:'nodeUser_form'}" preinstallWidth="1" height="400"></t:dgOpenOpt>
						<t:dgOpenOpt title="查看" icon="awsm-icon-zoom-in green"
							url="nodeUserController.do?nodeUserEdit&id={id}&optFlag=detail" exParams="{optFlag:'detail'}" preinstallWidth="1"
							height="400"></t:dgOpenOpt>
						<t:dgDelOpt title="删除" icon="awsm-icon-trash red" url="nodeUserController.do?delete&id={id}" />
						<t:dgToolBar title="添加" preinstallWidth="1" height="300" icon="awsm-icon-plus" exParams="{formId:'nodeUser_form'}"
							url="nodeUserController.do?nodeUserEdit&defId=${defId}&nodeId=${bpmNodeSet.nodeId}&funcType=preNoticeUser&gridId=preNoticeUserList"
							funname="add"></t:dgToolBar>
					</t:datagrid>
				</div>
				<div title="邮件内容" id="nodeInfoType_div2">
					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 90%">
						<tr>
							<td class="td_title"><label class="Validform_label"> 邮件标题: </label></td>
							<td class="value" style="width: 80%;"><input type="text" class="inputxt" name="preCommonTitle"
								id="preCommonTitle" value="${bpmNodeSet.preCommonTitle}" /></td>
						</tr>
						<tr>
							<td class="td_title"><label class="Validform_label"> 邮件内容: </label></td>
							<td class="value" style="width: 80%;"><textarea name="preMailContent" id="preMailContent">${bpmNodeSet.preMailContent}</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div title="站内消息内容" id="nodeInfoType_div3">
					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 90%">
						<tr>
							<td class="td_title"><label class="Validform_label"> 站内消息内容: </label></td>
							<td class="value" style="width: 80%"><textarea name="preInnerContent" id="preInnerContent">${bpmNodeSet.preInnerContent}</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div title="手机短信内容" id="nodeInfoType_div4">
					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 90%">
						<tr>
							<td class="td_title"><label class="Validform_label"> 短信内容: </label></td>
							<td class="value" style="width: 80%"><textarea name="preSmsContent" id="preSmsContent"
									style="height: 183px; width: 98%">${bpmNodeSet.preSmsContent}</textarea></td>
						</tr>
					</table>
				</div>
			</t:tabs>
		</div>
		<div style="margin-top: 3px; height: 260px; width: 657px;">
			<t:tabs id="nodeInfoType_tab2" fit="true" >
				<div title="后置抄送人" id="nodeInfoType_div5">
					<t:datagrid name="lastNoticeUserList" pagination="false" checkbox="false"
						actionUrl="nodeUserController.do?datagrid&defId=${defId}&nodeId=${bpmNodeSet.nodeId}&funcType=lastNoticeUser">
						<t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="是否设置用户" field="flag" hidden="false" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="分配类型" field="assignTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="分配名称" field="assignNames" hidden="true" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="计算类型" field="countTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
						<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
						<t:dgOpenOpt title="编辑" icon="awsm-icon-edit blue"
							url="nodeUserController.do?nodeUserEdit&id={id}&gridId=lastNoticeUserList"
							exParams="{optFlag:'add',formId:'nodeUser_form'}" preinstallWidth="1" height="400"></t:dgOpenOpt>
						<t:dgOpenOpt title="查看" icon="awsm-icon-zoom-in green"
							url="nodeUserController.do?nodeUserEdit&id={id}&optFlag=detail" exParams="{optFlag:'detail'}" preinstallWidth="1"
							height="400"></t:dgOpenOpt>
						<t:dgDelOpt title="删除" icon="awsm-icon-trash red" url="nodeUserController.do?delete&id={id}" />

						<t:dgToolBar title="添加" preinstallWidth="1" height="300" icon="awsm-icon-plus" exParams="{formId:'nodeUser_form'}"
							url="nodeUserController.do?nodeUserEdit&defId=${defId}&nodeId=${bpmNodeSet.nodeId}&funcType=lastNoticeUser&gridId=lastNoticeUserList"
							funname="add"></t:dgToolBar>
					</t:datagrid>
				</div>
				<div title="邮件内容" id="nodeInfoType_div6">
					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 90%">
						<tr>
							<td class="td_title"><label class="Validform_label"> 邮件标题: </label></td>
							<td class="value" style="width: 80%;"><input type="text" class="inputxt" name="lastCommonTitle"
								id="lastCommonTitle" value="${bpmNodeSet.lastCommonTitle}" /></td>
						</tr>
						<tr>
							<td class="td_title"><label class="Validform_label"> 邮件内容: </label></td>
							<td class="value" style="width: 80%;"><textarea name="lastMailContent" id="lastMailContent">${bpmNodeSet.lastMailContent}</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div title="站内消息内容" id="nodeInfoType_div7">
					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 90%">
						<tr>
							<td class="td_title"><label class="Validform_label"> 站内消息内容: </label></td>
							<td class="value" style="width: 80%"><textarea name="lastInnerContent" id="lastInnerContent">${bpmNodeSet.lastInnerContent}</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div title="手机短信内容" id="nodeInfoType_div8">
					<table cellpadding="0" cellspacing="1" class="formtable" style="width: 90%">
						<tr>
							<td class="td_title"><label class="Validform_label"> 短信内容: </label></td>
							<td class="value" style="width: 80%"><textarea name="lastSmsContent" id="lastSmsContent"
									style="height: 183px; width: 98%">${bpmNodeSet.lastSmsContent}</textarea></td>
						</tr>
					</table>
				</div>
			</t:tabs>
		</div>
	</t:formvalid>
