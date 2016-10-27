<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="模块树" style="width:200px;">
    	<t:tree id="resource_tree" gridTreeFilter="moduleId" onlyLeafClick="true"
			url="moduleController.do?tree" gridId="resourceList"
			clickPreFun="clickTree(node)">
		</t:tree>
    </div>  
    <div region="center" style="border:0px">
    <t:datagrid name="resourceList" checkbox="true" fitColumns="true" title="资源列表" actionUrl="resourceController.do?datagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="编号" field="id" hidden="false" ></t:dgCol>
			<t:dgCol title="资源名称" width="20" field="name" query="true" ></t:dgCol>
			<t:dgCol title="资源编码" width="20" field="code" query="true"></t:dgCol>
			<t:dgCol title="操作类型" dictCode="optType" width="15" field="optType" ></t:dgCol>
			<t:dgCol title="资源类型" replace="模块_module,公共_common" width="15" field="filterType" ></t:dgCol>
			<t:dgCol title="是否拦截" width="15" field="isInterceptor"  replace="是_Y,否_N"></t:dgCol>
			<t:dgCol title="操作url" width="30" field="url" ></t:dgCol>
			
			<t:dgCol title="操作" width="20" field="opt" ></t:dgCol>
			
			<t:dgOpenOpt title="编辑" icon="glyphicon glyphicon-pencil icon-color" preinstallWidth="1" height="450" url="resourceController.do?resourceEdit&id={id}" exParams="{optFlag:'add'}"></t:dgOpenOpt>
   			<t:dgOpenOpt title="查看" icon="glyphicon glyphicon-search icon-color" preinstallWidth="1" height="450" url="resourceController.do?resourceEdit&id={id}&optCode=detail" exParams="{optFlag:'detail'}" ></t:dgOpenOpt>
			<t:dgDelOpt title="删除" icon="glyphicon glyphicon-remove icon-color"  url="resourceController.do?delete&id={id}" />
			
			<t:dgToolBar preinstallWidth="1" height="450" title="新增"  icon="glyphicon glyphicon-plus icon-color" url="resourceController.do?resourceEdit" funname="add"></t:dgToolBar>
		    <t:dgToolBar title="删除"  icon="glyphicon glyphicon-trash icon-color" url="resourceController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
		</t:datagrid>
    </div>  
</div>
<input type="hidden" id="pmoduleId">
<input type="hidden" id="pmoduleCode">
<script type="text/javascript">
	function clickTree(node){
		$("#pmoduleId").val(node.id);
		$("#pmoduleCode").val(node.attributes.code);
    	$('#module_tree').tree('select', node.target);
    	$("#resourceList").datagrid("reload",{moduleId:node.id});
    	$("#resourceList").datagrid("getPanel").panel("setTitle",node.text+" 模块资源列表");
	}
	$(document).ready(function(){
		 redrawEasyUI($("#page_content"));
	 });
</script>