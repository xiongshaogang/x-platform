<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:formvalid formid="form_redoOrRecoverTask" action="processInstanceController.do?recover" callback="redoOrRecoverTask.callback(data)" afterSaveClose="false">
	<input type="hidden" name="actInstId" value="${actInstId}" />
	<input type="hidden" name="backToStart" value="${backToStart}" />
	<table class="formtable" style="width: 99.9%">
		<tr>
			<td class="td_title"><label class="Validform_label"> 提醒方式：</label></td>
			<td class="value"><t:comboBox name="informType" multiple="true" dictCode="noticeType"></t:comboBox></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 原因：</label></td>
			<td class="value"><textarea name="result" class="input_area"></textarea></td>
		</tr>
	</table>
</t:formvalid>
<script type="text/javascript">
var redoOrRecoverTask={
		callback:function (data){
			if (data.success == true) {
				//关闭所有弹出框
				var dialogs=$(".window-body");
				dialogs.dialog("close");
				//刷新已办事宜列表
				if($("#completeTaskList")[0]){
					$("#completeTaskList").datagrid("load");
				}
				//刷新待办事宜列表
				if($("#myTaskList")[0]){
					$("#myTaskList").datagrid("load");
				}
				//刷新办结事宜列表
				if($("#endTaskList")[0]){
					$("#endTaskList").datagrid("load");
				}
				//刷新我的请求列表
				if($("#requestInstanceList")[0]){
					$("#requestInstanceList").datagrid("load");
				}
				//刷新我的办结列表
				if($("#completeInstanceList")[0]){
					$("#completeInstanceList").datagrid("load");
				}
			}
			if(data.msg&&data.msg!=""){
				tip(data.msg);
			} 
		}
	};
</script>