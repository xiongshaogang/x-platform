<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<script type="text/javascript">
	$(document).ready(function(){
		redrawEasyUI($(".easyui-layout"));
	});
	function clickTree(node){
    	$('#flow_type_tree').tree('select', node.target);
    	$("#flowDefinitionList").datagrid("reload",{typeId:node.id});
    	$("#flowDefinitionList").datagrid("getPanel").panel("setTitle",node.text+"列表");
	}
</script>


<div class="easyui-layout" fit="true" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="流程分类树" style="width:200px;">
    	<t:tree id="flow_type_tree" gridTreeFilter="typeId"
			url="typeController.do?typeRoleTreeBySysTypeTree&sysType=workflow" gridId="flowDefinitionList"
			clickPreFun="clickTree(node)">
		</t:tree>
    </div>  
    <div region="center" style="border:0px">
    <t:datagrid name="flowDefinitionList" checkbox="true" fitColumns="true" title="流程列表" actionUrl="definitionController.do?datagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="编号" field="id" hidden="false" ></t:dgCol>
			<t:dgCol title="流程名称" width="20" field="name" query="true" ></t:dgCol>
			<t:dgCol title="流程编码" width="20" field="code" query="true"></t:dgCol>
			<t:dgCol title="所属类型"  width="15" field="type.name" ></t:dgCol>
			<t:dgCol title="版本号"  width="15" field="version" ></t:dgCol>
			<t:dgCol title="是否发布" replace="未发布_N,已发布_Y" width="15" field="published" ></t:dgCol>
			<t:dgCol title="状态" replace="启用_Y,禁用_N" width="15" field="status" ></t:dgCol>
			
			<t:dgCol title="操作" width="20" field="opt" ></t:dgCol>
			<t:dgOpenOpt title="流程设计" icon="glyphicon glyphicon-pencil icon-color"  height="99%" width="95%" url="definitionController.do?online&id={id}" exParams="{isIframe:true,optFlag:null}"></t:dgOpenOpt>
			<t:dgDelOpt title="删除" icon="glyphicon glyphicon-remove icon-color"  url="definitionController.do?delete&id={id}" />
			<t:dgFunOpt exp="published#eq#N" title="发布" icon="glyphicon glyphicon-forward icon-color" funname="doPublished(id)" />
			<t:dgFunOpt exp="published#eq#Y&&status#eq#N" title="启用" icon="glyphicon glyphicon-play icon-color" funname="doActive(id)" />
			<t:dgFunOpt exp="published#eq#Y&&status#eq#Y" title="禁用" icon="glyphicon glyphicon-stop icon-color" funname="doActive(id)" />
			<t:dgOpenOpt title="流程定义版本管理" icon="glyphicon glyphicon-th icon-color"  height="500" width="900" url="definitionController.do?definition&isMain=0&id={id}" exParams="{optFlag:null}"></t:dgOpenOpt>
			<t:dgOpenOpt exp="published#eq#Y" title="流程配置" exParams="{noheader:true,isIframe:false,dialogID:'flowSettingDialog'}" height="500" width="900" icon="glyphicon glyphicon-cog icon-color" url="definitionController.do?flowSetting&id={id}" />
			
			<t:dgToolBar title="流程设计" height="99%" width="95%" exParams="{isIframe:true,optFlag:null}" icon="glyphicon glyphicon-plus icon-color" url="definitionController.do?online" funname="add"></t:dgToolBar>
		    <t:dgToolBar title="删除"  icon="glyphicon glyphicon-trash icon-color" url="definitionController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
		    
		</t:datagrid>
    </div>  
</div>
<script type="text/javascript">
    function doPublished(id){
    	doSubmit("definitionController.do?published&id="+id,"flowDefinitionList");
    }
	function doActive(id){
		doSubmit("definitionController.do?doActive&id="+id,"flowDefinitionList");
	}
</script>
