<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:formvalid formid="form_divertTask" action="processInsCptoController.do?finishDivert" callback="divertTaskPage.callback(data)">
	<input type="hidden" name="actInstId" value="${actInstId}" />
	<table class="formtable" style="width: 99.9%">
		<tr>
			<td class="td_title"><label class="Validform_label"> 提醒方式：</label></td>
			<td class="value"><t:comboBox name="informType" multiple="true" dictCode="noticeType"></t:comboBox></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 转发意见：</label></td>
			<td class="value"><textarea name="opinion" class="input_area"></textarea></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 接收人：</label></td>
			<td class="value"><t:empSelect empOrUser="emp" displayName="empNames" hiddenName="empIds"></t:empSelect></td>
		</tr>
	</table>
</t:formvalid>
<script type="text/javascript">
var divertTaskPage={
	callback:function (data){
		if (data.success == true) {
			//关闭所有弹出框
			var dialogs=$(".window-body");
			dialogs.dialog("close");
			refreshRelateGrid();
		}
		if(data.msg&&data.msg!=""){
			tip(data.msg);
		} 
	}
};
</script>