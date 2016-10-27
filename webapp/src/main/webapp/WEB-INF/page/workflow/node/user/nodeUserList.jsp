<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 4px; border: 0px;">
		<t:datagrid pagination="false" view="groupview" groupField="nodeId" groupFormatter="groupFormatter(value,rows)"
			name="nodeUserList" checkbox="false" fitColumns="true"
			actionUrl="nodeUserController.do?datagrid&defId=${defId}&funcType=nodeUser" idField="id" fit="true" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="是否设置用户" field="flag" hidden="false" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="节点ID" field="nodeId" hidden="false" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="节点名称" field="nodeName" hidden="false" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="分配类型" field="assignTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="分配名称" field="assignNames" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="计算类型" field="countTypeName" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>

			<t:dgOpenOpt exp="flag#eq#true" title="编辑" icon="glyphicon glyphicon-pencil icon-color"
				url="nodeUserController.do?nodeUserEdit&id={id}&gridId=nodeUserList"
				exParams="{optFlag:'add',formId:'nodeUser_form'}" preinstallWidth="1" height="400"></t:dgOpenOpt>
			<t:dgOpenOpt exp="flag#eq#true" title="查看" icon="glyphicon glyphicon-search icon-color"
				url="nodeUserController.do?nodeUserEdit&id={id}" exParams="{optFlag:'detail'}" preinstallWidth="1" height="400"></t:dgOpenOpt>
			<t:dgDelOpt exp="flag#eq#true" title="删除" icon="glyphicon glyphicon-remove icon-color" url="nodeUserController.do?delete&id={id}" />

			<t:dgToolBar title="添加" preinstallWidth="1" height="300" icon="glyphicon glyphicon-plus icon-color" exParams="{formId:'nodeUser_form'}"
				url="nodeUserController.do?nodeUserEdit&defId=${defId}&funcType=nodeUser&gridId=nodeUserList" funname="add"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
<script type="text/javascript">
	function groupFormatter(value, rows) {
		return rows[0].nodeName;
	}
</script>