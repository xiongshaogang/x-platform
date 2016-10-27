<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
</style>
<div style="height: 100%; width: 98%; margin: 0 auto;">
	<t:datagrid name="cptoPersonList" title="抄送人列表" checkbox="false" view="scrollview" pagination="false"
		actionUrl="processInsCptoController.do?cptoDatagrid&id=${id}&type=${type}" idField="actInstId">
		<t:dgCol title="流程实例id" field="actInstId" hidden="false"></t:dgCol>
		<t:dgCol title="实例名称" width="15" field="subject"></t:dgCol>
		<t:dgCol title="节点名称" width="10" field="nodeName"></t:dgCol>
		<t:dgCol title="发送人名称" width="5" field="createUserName"></t:dgCol>
		<t:dgCol title="接收人名称" width="5" field="receiveName"></t:dgCol>
		<t:dgCol title="启用状态" replace="已读_1,未读_0" width="5" field="isRead"></t:dgCol>
		<t:dgCol title="发送时间" formatter="yyyy-MM-dd HH:mm:ss" width="10" field="createTime"></t:dgCol>
	</t:datagrid>
</div>
<script>
	
</script>
