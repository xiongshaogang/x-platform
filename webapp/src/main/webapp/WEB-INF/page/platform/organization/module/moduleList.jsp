<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<script type="text/javascript">
	function clickTree(node) {
		$("#pparentId").val(node.id);
		$('#module_tree').tree('select', node.target);
		$("#moduleList").datagrid("reload", {
			parentId : node.id
		});
		$("#moduleList").datagrid("getPanel").panel("setTitle", node.text);
	}
	function reflashTree() {
		var node = $('#module_tree').tree('getSelected');
		if (node == null) {
			$('#module_tree').tree('reload');
		} else {
			$('#module_tree').tree('reload', node.target);
		}
	}

	function addModule() {
		var parentId = $("#pparentId").val();
		var url = "moduleController.do?moduleEdit&parentId=" + parentId;
		createwindow('新增', url, null, 480, 1, {
			optFlag : 'add'
		});
	}
	
	$(document).ready(function(){
		 redrawEasyUI($("#page_content"));
	 });
</script>

<div class="easyui-layout" fit="true">
	<div region="west" split="true" title="模块树" style="width: 200px;">
		<t:tree id="module_tree" gridTreeFilter="parentId" url="moduleController.do?tree" clickPreFun="clickTree(node)"></t:tree>
	</div>
	<div region="center" style="border: 0px">
		<t:datagrid name="moduleList" title="模块列表"  actionUrl="moduleController.do?datagrid">
			<t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="父id" field="parentId" hidden="false"></t:dgCol>
			<t:dgCol title="模块名称" field="name" query="true" width="15"></t:dgCol>
			<t:dgCol title="模块编码" field="code" query="true" width="15"></t:dgCol>
			<t:dgCol title="模块路径" field="url" width="15"></t:dgCol>
			<t:dgCol title="子系统" field="subSystemName" width="15"></t:dgCol>
			<t:dgCol title="是否iframe" replace="是_Y,否_N" field="isIframe" width="15"></t:dgCol>
			<t:dgCol title="模块图标" field="iconCls" width="15"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="15%"></t:dgCol>

			<t:dgOpenOpt title="编辑" icon="glyphicon glyphicon-pencil icon-color" preinstallWidth="1" height="480"
				url="moduleController.do?moduleEdit&id={id}&parentId={parent_id}" exParams="{optFlag:'add'}"></t:dgOpenOpt>
			<t:dgOpenOpt title="查看" icon="glyphicon glyphicon-search icon-color" preinstallWidth="1" height="450"
				url="moduleController.do?moduleEdit&id={id}&optCode=detail" exParams="{optFlag:'detail'}"></t:dgOpenOpt>
			<t:dgDelOpt title="删除" icon="glyphicon glyphicon-remove icon-color" callback="reflashTree"
				url="moduleController.do?delete&id={id}" />

			<t:dgToolBar title="新增" icon="glyphicon glyphicon-plus icon-color" onclick="addModule()"></t:dgToolBar>
			<%--  <t:dgToolBar operationCode="moduleMananger_moduleBatchDelete_batchDelete" title="删除" callback="reflashTree" icon="awsm-icon-remove" url="moduleController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar> --%>
		</t:datagrid>
	</div>
</div>
<input type="hidden" id="pparentId">