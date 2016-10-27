<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="west" split="true" title="系统类型树" style="width: 200px;">
  	<t:tree id="sys_type_tree" showOptMenu="false" clickPreFun="parentType(node)" url="typeController.do?tree">
  		
  	</t:tree>
  </div>
  <div region="center" style="padding:0px;border: 0px;">
  <t:datagrid name="sysTypeList" checkbox="true" fitColumns="true" title="系统类型" actionUrl="typeController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="160"></t:dgCol>
   <t:dgCol title="代码"  field="code" query="true" hidden="true"  queryMode="single"  width="160"></t:dgCol>
   <t:dgCol title="名称"  field="name" query="true" hidden="true"  queryMode="single"  width="160"></t:dgCol>
   <t:dgCol title="类型"  field="sysType"  hidden="true" dictCode="systemType" queryMode="single"  width="160"></t:dgCol>
   <t:dgCol title="描述"  field="remark"  hidden="true"  queryMode="single"  width="180"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="200"></t:dgCol>
   <t:dgOpenOpt operationCode="typeManager_editType_edit" url="typeController.do?typeEdit&id={id}" preinstallWidth="1" icon="glyphicon glyphicon-pencil icon-color" exParams="{optFlag:'add'}" height="400" title="编辑"></t:dgOpenOpt>
   <t:dgOpenOpt operationCode="typeManager_viewType_view" url="typeController.do?typeEdit&id={id}" preinstallWidth="1" icon="glyphicon glyphicon-search icon-color" exParams="{optFlag:'detail'}" height="400" title="查看"></t:dgOpenOpt>
   <t:dgDelOpt operationCode="typeManager_deleteType_delete" callback="refreshTypeTree" title="删除" icon="glyphicon glyphicon-remove icon-color" url="typeController.do?delete&id={id}" />
   
   <t:dgToolBar title="添加" operationCode="typeManager_addType_add" preinstallWidth="1" height="400" icon="glyphicon glyphicon-plus icon-color" buttonType="GridAdd" url="typeController.do?typeEdit" funname="add"></t:dgToolBar>
   <t:dgToolBar title="批量删除" callback="refreshTypeTree" operationCode="typeManager_batchDeleteTyper_batchDelete" icon="glyphicon glyphicon-trash icon-color" buttonType="GridDelMul" url="typeController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgOpenOpt title="分配角色" icon="glyphicon glyphicon-pencil icon-color" operationCode="organizationManager_distributionRoleDept_other" url="roleController.do?distributionRole&id={id}&distributionFlag=typeDistribution"  width="1100" height="500" ></t:dgOpenOpt>
  </t:datagrid>
  </div>
<input type="hidden" id="typeParentId">
<input type="hidden" id="typeParentName">
<input id="typeParentType" type="hidden" />
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
	 redrawEasyUI($(".easyui-layout"));
 		//给时间要素加上样式
 });
 function parentType(node){
	 $("#sysTypeList").datagrid("reload",{parentId:node.id});
	 //$('#typeList').datagrid('reload');
	 $("#typeParentId").val(node.id);
	 $("#typeParentName").val(node.text);
	 var sysType = node.attributes['sysType'];
	 $("#typeParentType").val(sysType);
 }
 function refreshTypeTree(){
	$("#sys_type_tree").tree('reload');
}
 </script>