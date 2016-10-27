<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px;">
		<t:datagrid name="sysScriptList" title="脚本列表" actionUrl="sysScriptController.do?datagrid">
			<t:dgCol title="主键" field="id" hidden="false" width="120"></t:dgCol>
			<t:dgCol title="脚本名称" field="name" query="true" hidden="true" width="120"></t:dgCol>
			<t:dgCol title="脚本来源" field="sourceDict" width="120" query="true" queryInputType="combobox" dictCode="scriptSource"></t:dgCol>
			<t:dgCol title="脚本类型" field="typeDict" width="120" query="true" queryInputType="combobox" dictCode="scriptType"></t:dgCol>
			<t:dgCol title="是否启用" field="enableDict" dictCode="YNType" width="120"></t:dgCol>

			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>

			<t:dgOpenOpt title="编辑" preinstallWidth="2" icon="awsm-icon-edit blue"
				operationCode="sysScriptManager_editSysScript_edit" url="sysScriptController.do?edit&id={id}"
				exParams="{optFlag:'add'}" height="520"></t:dgOpenOpt>
			<t:dgOpenOpt title="查看" preinstallWidth="2" icon="awsm-icon-zoom-in green"
				operationCode="sysScriptManager_viewSysScript_view" url="sysScriptController.do?edit&id={id}"
				exParams="{optFlag:'detail'}" height="520"></t:dgOpenOpt>
			<t:dgDelOpt title="删除" icon="awsm-icon-trash red" operationCode="sysScriptManager_deleteSysScript_delete"
				url="sysScriptController.do?delete&id={id}" />

			<t:dgToolBar title="添加" preinstallWidth="2" operationCode="sysScriptManager_addSysScript_add" height="520"
				icon="awsm-icon-plus" url="sysScriptController.do?edit" funname="add"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="awsm-icon-remove" operationCode="sysScriptManager_deleteSysScript_batchDelete"
				url="sysScriptController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
<script>
	$(document).ready(function() {
		redrawEasyUI($(".easyui-layout"));
	});
</script>
