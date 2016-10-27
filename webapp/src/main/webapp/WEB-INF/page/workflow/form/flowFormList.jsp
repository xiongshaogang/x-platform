<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<script type="text/javascript">
	$(document).ready(function(){
		redrawEasyUI($(".easyui-layout"));
	});
	function clickTree(node){
		$("#ptypeId").val(node.id);
		$("#ptypeCode").val(node.attributes.code);
    	$('#type_tree').tree('select', node.target);
    	$("#flowFormList").datagrid("reload",{typeId:node.id});
    	$("#flowFormList").datagrid("getPanel").panel("setTitle",node.text+" 模块表单列表");
	}
	function refreshTree() {
		$("#dept_org_tree").tree('reload');
	}
</script>


<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="模块树" style="width:200px;">
    	<!-- <ul id="flowForm_tree"></ul> -->
    	<t:tree id="flowForm_tree" gridTreeFilter="typeId"
			url="typeController.do?typeRoleTreeBySysTypeTree&sysType=flowform" gridId="flowFormList"
			clickPreFun="clickTree(node)">
		</t:tree>
    </div>
    <div region="center" style="border:0px">
    <t:datagrid name="flowFormList" checkbox="true" fitColumns="true" title="表单列表" actionUrl="flowFormController.do?datagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="编号" field="id" hidden="false" ></t:dgCol>
			<t:dgCol title="表单名称" width="20" field="name" query="true" ></t:dgCol>
			<t:dgCol title="表单编码" width="20" field="code" query="true"></t:dgCol>
			<t:dgCol title="表单url" width="30" field="url" ></t:dgCol>
			<t:dgCol title="备注" width="30" field="description" ></t:dgCol>
			
			<t:dgCol title="操作" width="20" field="opt" ></t:dgCol>
			
			<t:dgOpenOpt title="编辑" icon="glyphicon glyphicon-pencil icon-color" preinstallWidth="1" height="400" url="flowFormController.do?flowFormEdit&id={id}" exParams="{optFlag:'add'}"></t:dgOpenOpt>
   			<t:dgOpenOpt title="查看" icon="glyphicon glyphicon-search icon-color" preinstallWidth="1" height="400" url="flowFormController.do?flowFormEdit&id={id}&optCode=detail" exParams="{optFlag:'detail'}" ></t:dgOpenOpt>
			<t:dgDelOpt title="删除" icon="glyphicon glyphicon-remove icon-color"  url="flowFormController.do?delete&ids={id}" />
			<t:dgOpenOpt title="表单内容" icon="glyphicon glyphicon-pencil icon-color" preinstallWidth="2" height="500" url="flowFormController.do?flowFormContent&id={id}" exParams="{optFlag:'add'}"></t:dgOpenOpt>
			
			<t:dgOpenOpt title="表单任务" icon="glyphicon glyphicon-pencil icon-color" width="1000" height="550" url="flowFormController.do?templateEdit&id={id}" exParams="{isIframe:true}"></t:dgOpenOpt>
			
			
			<t:dgToolBar preinstallWidth="1" height="400" title="新增"  icon="glyphicon glyphicon-plus icon-color" url="flowFormController.do?flowFormEdit" funname="add"></t:dgToolBar>
		    <t:dgToolBar title="批量删除"  icon="glyphicon glyphicon-trash icon-color" url="flowFormController.do?delete" funname="deleteALLSelect"></t:dgToolBar>
		</t:datagrid>
    </div>  
</div>
<input type="hidden" id="ptypeId">
<input type="hidden" id="ptypeCode">