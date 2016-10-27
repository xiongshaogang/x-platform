<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/ckeditor/ckeditor_sysTemp.js"></script>
<style>
#taskDueformobj .form_div {
	margin: 0px auto;
	width: 100%;
}
</style>
<script type="text/javascript">
	$(function() {
		curTime = '${taskReminder.times}';
		//是否发送催办信息的checkbox
		$("#needSendMsg").change(function() {
			var me = $(this), sendMsg = me.attr("checked");
			if (sendMsg) {
				$(".send-msg-tr").show();
			} else {
				$("#sendType").combobox("clear");
				$("#reminderEndDay").val(0);
				$("#reminderEndHour").combobox("setValue", 0);
				$("#reminderEndMinute").combobox("setValue", 0);
				$("#times").combobox("setValue", 0);
				$(".send-msg-tr").hide();
			}
		});

		$.parser.onComplete = function() {
			if (curTime > 0) {
				$("#needSendMsg").attr("checked", true).trigger("change");
			} else {
				$("#needSendMsg").attr("checked", false).trigger("change");
			}
			change();
			$.parser.onComplete = mainComplete;
		};

		$("#assignerId").val('${taskReminder.assignerId}');
		$("#assignerName").val('${taskReminder.assignerName}');

		var myButton = {
			id : 'add',
			text : '新增',
			iconCls : 'awsm-icon-plus',
			operationCode : "definitionManager_taskDue_other",
			handler : function() {
				var t_commonTitle = $("#t_commonTitle").val();
				var t_smsContent = $("#t_smsContent").val();
				var t_innerContent = $("#t_innerContent").val();
				var t_mailContent = $("#t_mailContent").val();
				$("#taskDueformobj").find("input[name!='actDefId'][name!='nodeId'][name],textarea").not(":disabled")
						.each(function() {
							var id = $(this).get(0).id;
							$(this).val("");
							if (id == "script" || id == "conditon") {
								InitMirror.setValue(id, "");
							}
							if (id == "innerContent") {
								innerContent.setData(t_innerContent);
							}
							if (id == "mailContent") {
								mailContent.setData(t_mailContent);
							}
						});
				$("#commonTitle").val(t_commonTitle);
				$("#smsContent").val(t_smsContent);
				$("#reminderEndDay").val(0);
				$("#reminderEndHour").combobox("setValue", 0);
				$("#reminderEndMinute").combobox("setValue", 0);

				$("#completeTimeDay").val(0);
				$("#completeTimeHour").combobox("setValue", 0);
				$("#completeTimeMinute").combobox("setValue", 0);

				$("#times").combobox("setValue", 0);
				$("#action").combobox("setValue", 0);

				$("#relativeNodeId").combobox("clear");
				$("#relativeNodeType").combobox("clear");
				$("#relativeTimeType").combobox("clear");
				$("#sendType").combobox("clear");
				//修改新增标识
				$("#ruleStatus").val("1");
				//是否发送催办信息重置为未勾选
				$("#needSendMsg").attr("checked", false).trigger("change");
				//执行动作切换触发方法
				change();
			}
		};
// 		addButton(getD($("#taskDueDiv")), myButton, 1);

		setTimeout('InitMirror.initId("conditon")', InitMirror.options.initDelay);
		setTimeout('InitMirror.initId("script")', InitMirror.options.initDelay);
		setTimeout('$(".cke_contents").css("height","260px")', 1000);

	});

	if (CKEDITOR.instances['mailContent']) {
		CKEDITOR.remove(CKEDITOR.instances['mailContent']);
	}
	var mailContent = ckeditor('mailContent');

	if (CKEDITOR.instances['innerContent']) {
		CKEDITOR.remove(CKEDITOR.instances['innerContent']);
	}

	var innerContent = ckeditor('innerContent');
	// if( CKEDITOR.instances['smsContent'] ){
	//     CKEDITOR.remove(CKEDITOR.instances['smsContent']);
	// }
	// var smsContent = ckeditor('smsContent');

	function saveContent() {
		$('#mailContent').val(mailContent.getData());
		$('#innerContent').val(innerContent.getData());
		// 	$('#smsContent').val(smsContent.getData());
	}

	//执行动作修改触发方法
	function change() {
		var s = $("#action").combobox("getValue");
		$(".sub").hide();
		$(".choose-assigner").hide();
		if (s == 7) {//选择执行脚本
			$(".sub").show();
		}
		if (s == 5) {//选择交办
			$(".choose-assigner").show();
		}
	}

	function addTaskDue(data) {
		if ($("#ruleStatus").val() == 1) {
			var spanContent = '<span style="margin-left: 10px;" id="'+ data.obj.TaskDue.id +'">'
					+ '<a class="taskDueAdd" href="#" onclick="checkTaskDue(\'' + data.obj.TaskDue.id + '\');">'
					+ data.obj.TaskDue.name + '</a>'
					+ '<a class="taskDueRemove" href="#1" style="float: right;" onclick="removeTaskDue(\''
					+ data.obj.TaskDue.id
					+ '\');"><i style="margin-right: 10px;" class="awsm-icon-remove red"></i></a></span>';
			$("#taskDueList").append(spanContent);
		} else {
			$("#taskDueList span").filter("#" + data.obj.TaskDue.id).find("a").eq("0").html(
					data.obj.TaskDue.name);
		}
		$("#ruleStatus").val("0");
		$("#id").val(data.obj.TaskDue.id);
	}
	//删除触发方法
	function removeTaskDue(taskDueId) {
		$.messager.confirm('提示信息', '确认删除该记录?', function(r) {
			if (r) {
				$.ajax({
					url : "taskDueController.do?delete",
					type : 'post',
					data : {
						id : taskDueId
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						var msg = d.msg;
						tip(msg);
						$("#taskDueList span").filter("#" + taskDueId).remove();
						$("#taskDueList span:first-child a.taskDueAdd").click();
					}
				});

			}
		});
	}

	function checkTaskDue(id) {
		$.messager.progress({
			text : '数据加载中....',
			interval : 200
		});
		$.ajax({
			url : "taskDueController.do?getTaskDue",
			type : 'post',
			data : {
				id : id
			},
			cache : false,
			success : function(data) {
				var data = $.parseJSON(data);
				$("#ruleStatus").val("0");
				$("#id").val(data.obj.taskDue.id);
				$("#name").val(data.obj.taskDue.name);
				$("#relativeNodeId").combobox("setValue", data.obj.taskDue.relativeNodeId);
				$("#relativeNodeType").combobox("setValue", data.obj.taskDue.relativeNodeType);

				$("#completeTimeDay").val(data.obj.completeTimeDay);
				$("#completeTimeHour").combobox("setValue", data.obj.completeTimeHour);
				$("#completeTimeMinute").combobox("setValue", data.obj.completeTimeMinute);

				$("#reminderEndDay").val(data.obj.reminderEndDay);
				$("#reminderEndHour").combobox("setValue", data.obj.reminderEndHour);
				$("#reminderEndMinute").combobox("setValue", data.obj.reminderEndMinute);

				$("#relativeTimeType").combobox("setValue", data.obj.taskDue.relativeTimeType);
				$("#times").combobox("setValue", data.obj.taskDue.times);
				$("#conditon").val(data.obj.taskDue.conditon);
				InitMirror.setValue("conditon", data.obj.taskDue.conditon);
				$("#action").combobox("setValue", data.obj.taskDue.action);
				var sendType = data.obj.taskDue.sendType;
				var sendTypeArray = [];
				if (sendType) {
					sendTypeArray = sendType.split(",");
				}
				$("#sendType").combobox("setValues", sendTypeArray);

				$("#script").val(data.obj.taskDue.script);
				InitMirror.setValue("script", data.obj.taskDue.script);
				$("#assignerId").val(data.obj.taskDue.assignerId);
				$("#assignerName").val(data.obj.taskDue.assignerName);
				$("#commonTitle").val(data.obj.taskDue.commonTitle);
				$("#mailContent").val(data.obj.taskDue.mailContent);
				mailContent.setData(data.obj.taskDue.mailContent);
				$("#smsContent").val(data.obj.taskDue.smsContent);
				$("#innerContent").val(data.obj.taskDue.innerContent);
				innerContent.setData(data.obj.taskDue.innerContent);
				curTime = data.obj.taskDue.times;
				if (curTime > 0) {
					$("#needSendMsg").attr("checked", true).trigger("change");
				} else {
					$("#needSendMsg").attr("checked", false).trigger("change");
				}
				change();
			}
		});
		$.messager.progress('close');
	}
	function saveTaskDueScript() {
		$("#conditon").val(InitMirror.getValue("conditon"));
		$("#script").val(InitMirror.getValue("script"));
		saveContent();
	}
</script>
<div id="taskDueDiv" class="easyui-layout" style="width: 98%; height: 98.5%; margin: 5px auto 0px auto">
	<div region="east" split="true" title="任务列表" style="width: 140px;" id="taskDueList">
		<c:forEach var="taskDue" items="${taskReminders}">
			<span id="${taskDue.id}" style="margin-left: 10px;"><a class="taskDueAdd" href="#"
				onclick="checkTaskDue('${taskDue.id }')">${taskDue.name}</a> <a class="taskDueRemove" style="float: right;"
				href="#1" onclick="removeTaskDue('${taskDue.id}');"><i style="margin-right: 10px;" class="awsm-icon-remove red"></i></a></span>
			<br />
		</c:forEach>
	</div>
	<div region="center" split="true" style="overflow: hidden">
		<t:formvalid formid="taskDueformobj" action="taskDueController.do?saveTaskDue" callback="addTaskDue"
			afterSaveClose="false" beforeSubmit="saveTaskDueScript">
			<input type="hidden" id="t_innerContent" value="${innerContent}" />
			<input type="hidden" id="t_smsContent" value="${smsContent}" />
			<input type="hidden" id="t_mailContent" value="${mailContent}" />
			<input type="hidden" id="t_commonTitle" value="${commonTitle}" />
			<input type="hidden" name="actDefId" value="${param.actDefId}" />
			<input type="hidden" name="nodeId" value="${param.nodeId}" />
			<input type="hidden" id="id" name="id" value="${taskReminder.id}" />
			<input type="hidden" name="ruleStatus" value="0" id="ruleStatus" />
			<t:tabs id="taskDueTab" fit="false" hBorderBottom="true" hBorderLeft="false" hBorderRight="false" hBorderTop="false"
				rightDiv="false" closeBtn="true" height="390">
				<div title="基本信息" id="basicPanel" style="overflow-x: hidden; overflow-y: auto">
					<t:collapseTitle width="690" id="a" title="到期条件设置" border="false" collapsible="true">
						<table cellpadding="0" cellspacing="1" class="formtable">
							<tr>
								<td class="td_title"><label class="Validform_label"> 名称：</label></td>
								<td class="value"><input type="text" class="inputxt" name="name" id="name" value="${taskReminder.name }" />
								</td>
								<td class="td_title"><label class="Validform_label"> 当前节点：</label></td>
								<td class="value"><input type="text" class="inputxt" disabled="disabled" value="${nodeId}" /></td>
							</tr>
							<tr>
								<td class="td_title"><label class="Validform_label"> 相对节点：</label></td>
								<td class="value"><t:comboBox data="${jsonNodes}" id="relativeNodeId" name="relativeNodeId"
										value="${taskReminder.relativeNodeId}" /></td>
								<td class="td_title"><label class="Validform_label"> 相对动作：</label></td>
								<td class="value"><t:comboBox id="relativeNodeType" name="relativeNodeType" dictCode="relativeNodeType"
										value="${taskReminder.relativeNodeType}" /></td>
							</tr>
							<tr>
								<td class="td_title"><label class="Validform_label"> 相对时间：</label></td>
								<td id="completeTr" class="value"><input type="text" id="completeTimeDay" name="completeTimeDay"
									value="${completeTimeDay}" style="width: 50px; height: 28px; padding: 0 !important;" /> <span>天</span> <t:comboBox
										panelWidth="60" panelHeight="150" width="60" height="28" data="${jsonHours}" id="completeTimeHour"
										name="completeTimeHour" value="${completeTimeHour}" /> <t:comboBox panelWidth="60" panelHeight="150"
										width="60" height="28" data="${jsonMinutes}" id="completeTimeMinute" name="completeTimeMinute"
										value="${completeTimeMinute}" /></td>
								<td class="td_title"><label class="Validform_label"> 相对时间类型:</label></td>
								<td class="value"><t:comboBox id="relativeTimeType" name="relativeTimeType" dictCode="relativeTimeType"
										value="${taskReminder.relativeTimeType}" /></td>
							</tr>
							<tr>
								<td class="td_title"><label class="Validform_label"> 条件表达式:</label></td>
								<td class="value" colspan="3">
									<div style="background: #F7F7F7; width: 96.5%">
										<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('conditon')">常用脚本</button>
										<textarea mirrorheight="90px" id="conditon" name="conditon" class="input_area">${taskReminder.conditon}</textarea>
									</div>
								</td>
							</tr>
						</table>
					</t:collapseTitle>
					<t:collapseTitle width="690" id="b" title="到期动作设置" border="false" collapsible="true">
						<table cellpadding="0" cellspacing="1" class="formtable" style="width: 650px">
							<tr>
								<td class="td_title"><label class="Validform_label"> 执行动作: </label></td>
								<td colspan="3" class="value"><t:comboBox onChange="change()" id="action" name="action"
										dictCode="executeAction" value="${taskReminder.action}" /></td>
							</tr>
							<tr class="sub">
								<td class="td_title"><label class="Validform_label"> 执行脚本: </label></td>
								<td class="value" colspan="3">
									<div style="background: #F7F7F7; width: 96.5%">
										<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('script')">常用脚本</button>
										<textarea mirrorheight="90px" id="script" name="script" class="input_area">${taskReminder.script}</textarea>
									</div>
								</td>
							</tr>
							<tr class="choose-assigner">
								<td class="td_title" nowrap><label class="Validform_label"> 指定交办人员:</label></td>
								<td colspan="3" class="value">
									<%-- <input type="hidden" name="assignerId" value="${taskReminder.assignerId}"/>
							<input type="text" name="assignerName" readonly="readonly" value="${taskReminder.assignerName}"/> --%> <t:userSelect
										hiddenName="assignerId" displayName="assignerName" multiples="false"></t:userSelect>
								</td>
							</tr>
						</table>
					</t:collapseTitle>
					<t:collapseTitle width="690" id="c" title="发送催办消息设置" border="false" collapsible="true">
						<table cellpadding="0" cellspacing="1" class="formtable" style="width: 650px;">
							<tr>
								<td class="td_title" nowrap><label class="Validform_label"> 发送催办信息:</label></td>
								<td colspan="3" class="value"><label><input style="vertical-align: -2px; margin-right: 5px;"
										type="checkbox" id="needSendMsg" />发送</label></td>
							</tr>
							<!-- 							<tr class="send-msg-tr"> -->
							<!-- 								<td class="td_title"><label class="Validform_label"> 开始发送时间:</label></td> -->
							<!-- 								<td id="startTr" colspan="3" class="value"><input style="width: 50px; height: 28px; padding: 0 !important;" -->
							<%-- 									type="text" id="reminderStartDay" name="reminderStartDay" value="${reminderStartDay}" /> <span>天</span> <select --%>
							<!-- 									id="reminderStartHour" style="width: 60px; height: 28px; padding: 0 !important;" name="reminderStartHour"> -->
							<%-- 										<c:forEach var="i" begin="0" end="23" step="1"> --%>
							<%-- 											<option value="${i}" <c:if test="${reminderStartHour==i}">selected="selected"</c:if>>${i}小时</option> --%>
							<%-- 										</c:forEach> --%>
							<!-- 								</select> <select id="reminderStartMinute" style="width: 60px; height: 28px; padding: 0 !important;" -->
							<!-- 									name="reminderStartMinute"> -->
							<%-- 										<c:forEach var="i" begin="0" end="4" step="1"> --%>
							<%-- 											<option value="${i}" <c:if test="${reminderStartMinute==i}">selected="selected"</c:if>>${i}分钟</option> --%>
							<%-- 										</c:forEach> --%>
							<%-- 										<c:forEach var="i" begin="5" end="59" step="5"> --%>
							<%-- 											<option value="${i}" <c:if test="${reminderStartMinute==i}">selected="selected"</c:if>>${i}分钟</option> --%>
							<%-- 										</c:forEach> --%>
							<!-- 								</select></td> -->
							<!-- 							</tr> -->
							<tr class="send-msg-tr">
								<td class="td_title"><label class="Validform_label">消息类型:</label></td>
								<td id="startTr" colspan="3" class="value"><t:comboBox dictCode="noticeType" id="sendType" name="sendType"
										multiple="true" value="${taskReminder.sendType}"></t:comboBox></td>
							</tr>
							<tr class="send-msg-tr">
								<td class="td_title"><label class="Validform_label"> 发送的间隔:</label></td>
								<td id="endTr" class="value"><input style="width: 50px; height: 28px; padding: 0 !important;" type="text"
									id="reminderEndDay" name="reminderEndDay" value="${reminderEndDay}" /> <span>天</span> <t:comboBox
										panelWidth="60" panelHeight="150" width="60" height="28" data="${jsonHours}" id="reminderEndHour"
										name="reminderEndHour" value="${reminderEndHour}" /> <t:comboBox panelWidth="60" panelHeight="150" width="60"
										height="28" data="${jsonMinutes}" id="reminderEndMinute" name="reminderEndMinute" value="${reminderEndMinute}" /></td>
								<td class="td_title"><label class="Validform_label"> 发送信息次数: </label></td>
								<td class="value"><t:comboBox data="${jsonNums}" id="times" name="times" value="${taskReminder.times}" /></td>
							</tr>
						</table>
					</t:collapseTitle>
				</div>
				<div title="邮件内容" fit="true" id="emailPanel">
					<table cellpadding="0" cellspacing="1" class="formtable">
						<tr>
							<td class="td_title"><label class="Validform_label"> 邮件标题: </label></td>
							<td class="value" style="width: 85%;"><input type="text" class="inputxt" name="commonTitle" id="commonTitle"
								value="${taskReminder.commonTitle}" /></td>
						</tr>
						<tr>
							<td class="td_title"><label class="Validform_label"> 邮件内容: </label></td>
							<td class="value" style="width: 85%;"><textarea name="mailContent" id="mailContent">${taskReminder.mailContent}</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div title="站内消息内容" fit="true" id="messagePanel">
					<table cellpadding="0" cellspacing="1" class="formtable">
						<tr>
							<td class="td_title"><label class="Validform_label"> 站内消息内容: </label></td>
							<td class="value" style="width: 85%"><textarea name="innerContent" id="innerContent">${taskReminder.innerContent}</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div title="手机短信内容" fit="true" id="smsPanel">
					<table cellpadding="0" cellspacing="1" class="formtable">
						<tr>
							<td class="td_title"><label class="Validform_label"> 短信内容: </label></td>
							<td class="value" style="width: 85%">
								<%-- <div style="background: #F7F7F7;">
						<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('smsContent')">常用脚本</button>
						<textarea codemirror="true" mirrorheight="180px" id="smsContent" name="smsContent" class="input_area">${taskReminder.smsContent}</textarea>
						</div> --%> <textarea name="smsContent" id="smsContent" style="height: 310px; width: 98%">${taskReminder.smsContent}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</t:tabs>
		</t:formvalid>
	</div>
</div>

