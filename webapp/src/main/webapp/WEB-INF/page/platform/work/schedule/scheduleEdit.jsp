<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/jquery-plugs/colorpicker/js/colorpicker.js"></script>
<link rel="stylesheet" href="plug-in/jquery-plugs/colorpicker/css/colorpicker.css"/>

<t:formvalid formid="formobj" callback="reloadCalendar" beforeSubmit="checkScheduleType" action="scheduleController.do?saveOrUpdate">
	<input id="id" name="id" type="hidden" value="${schedule.id }">
	<table id="scheduleTab" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>标题:
				</label>
			</td>
			<td class="value">
			    <input id="title" name="title" datatype="*1-100" type="text" class="inputxt" value='${schedule.title}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>开始时间:
				</label>
			</td>
			<td class="value">
				<t:datetimebox id="startDate" name="startDate" type="datetime" format="yyyy-MM-dd HH:mm" showSeconds="false" value='${schedule.startDate}'></t:datetimebox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>结束时间:
				</label>
			</td>
			<td class="value">
				<t:datetimebox id="endDate" name="endDate" format="yyyy-MM-dd HH:mm" type="datetime" showSeconds="false" value='${schedule.endDate}'></t:datetimebox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>内容:
				</label>
			</td>
			<td class="value">
				<textarea id="context" name="context" type="text" datatype="*1-300" class="input_area">${schedule.context}</textarea>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					颜色选择:
				</label>
			</td>
			<td class="value">
				<select id="className" name="className" class="hide">
					<option data-class="label-grey" value="#a0a0a0" <c:if test="${schedule.className=='label-grey'}">selected=""</c:if>>#a0a0a0</option>
					<option data-class="label-success" value="#82af6f" <c:if test="${schedule.className=='label-success'}">selected=""</c:if>>#82af6f</option>
					<option data-class="label-danger" value="#d15b47" <c:if test="${schedule.className=='label-danger'}">selected=""</c:if>>#d15b47</option>
					<option data-class="label-purple" value="#9585bf" <c:if test="${schedule.className=='label-purple'}">selected=""</c:if>>#9585bf</option>
					<option data-class="label-yellow" value="#fee188" <c:if test="${schedule.className=='label-yellow'}">selected=""</c:if>>#fee188</option>
					<option data-class="label-pink" value="#d6487e" <c:if test="${schedule.className=='label-pink'}">selected=""</c:if>>#d6487e</option>
					<option data-class="label-info" value="#3a87ad" <c:if test="${schedule.className=='label-info'}">selected=""</c:if>>#3a87ad</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>提醒方式:
				</label>
			</td>
			<td class="value">
				<input id="innerMessage" type="checkbox" name="scheduleType" value="innerMessage">&nbsp;站内信
				<input id="sms" type="checkbox" name="scheduleType" value="sms">&nbsp;短信
				<input id="email" type="checkbox" name="scheduleType" value="email">&nbsp;邮件
			</td>
		</tr>
	</table>
</t:formvalid>
<script type="text/javascript" defer="defer">
	$(function(){
		$('#className').ColorPicker();
		var scheduleType = '${schedule.scheduleType}';
		var scheduleTypes = scheduleType.split(",");
		for(var i = 0; i < scheduleTypes.length; i++){
			if(scheduleTypes[i] != ''){
				$("#"+scheduleTypes[i]).attr("checked","checked");
			}
		}
	})
	
	function checkScheduleType(){
		var scheduleTypes = document.getElementsByName("scheduleType");
		var count = 0;
		for(var i = 0; i < scheduleTypes.length; i++){
			if(scheduleTypes[i].checked){
				count ++;
			}
		}
		if(count == 0){
			alert("请选择提醒方式");
			return false;
		}else{
			return true;
		}
	}
	function reloadCalendar(){
		scheduleMainTabs();
		$('#calendar').fullCalendar('refetchEvents'); //刷新 
	}
</script>