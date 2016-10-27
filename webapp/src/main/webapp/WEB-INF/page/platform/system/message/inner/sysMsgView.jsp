<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
$(function(){
	main.queryInnerMsgData();
	$("#main_flowMsgList").datagrid("reload");//刷新首页系统消息列表
	$("#main_innerMsgList").datagrid("reload");
});
</script>
<style>
#sysMsgView #innerContent {
	overflow-x: hidden;
	overflow-y: auto;
	width: 340px;
	word-wrap: break-word;
	
}
#innerContent a{
	color:#428bca;
	text-decoration: underline;
}
</style>
<input id="id" name="id" type="hidden" value="${message.id}">
<table id="sysMsgView" cellpadding="0" cellspacing="1" class="formtable" style="width: 100%">
	<tr>
		<td class="td_title"><label class="Validform_label"> 标题:</label></td>
		<td class="value"><div>${message.title}</div></td>
	</tr>
	<tr>
		<td class="td_title"><label class="Validform_label"> 发送人:</label></td>
		<td class="value"><div>${message.sendUserName}</div></td>
	</tr>
	<tr>
		<td class="td_title"><label class="Validform_label"> 接收时间:</label></td>
		<td class="value"><div>
				<fmt:formatDate value="${message.receiveTime}" pattern="yyyy-MM-dd HH:mm:ss" />
			</div></td>

	</tr>
	<tr>
		<td class="td_title"><label class="Validform_label"> 消息内容:</label></td>
		<td class="value"><div id="innerContent">${message.innerContent}</div></td>
	</tr>
</table>
