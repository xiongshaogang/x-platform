<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
	  <t:datagrid name="msgTemplateList" checkbox="true" fitColumns="true" title="消息任务列表" actionUrl="msgTemplateController.do?datagrid" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务名称"  field="name" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务标题"  field="title"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
<%-- 	   <t:dgCol title="任务用途" sql="select t.name code,t.value name from t_sys_dict_value t where t.type_code='msg_template' "  field="useType"  hidden="true"  queryMode="single"  width="120"></t:dgCol> --%>
	   <t:dgCol title="是否默认" replace="是_Y,否_N" field="isDefault"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	  
	   <t:dgOpenOpt title="编辑" preinstallWidth="2" url="msgTemplateController.do?msgTemplateEdit&id={id}" exParams="{optFlag:'update'}" height="500" ></t:dgOpenOpt>
   	   <t:dgOpenOpt title="查看" preinstallWidth="2" url="msgTemplateController.do?msgTemplateEdit&id={id}" exParams="{optFlag:'detail'}" height="500" ></t:dgOpenOpt>
	   <t:dgDelOpt title="删除" operationCode="msgTemplateManager_deletemsgTemplate_delete" url="msgTemplateController.do?delete&id={id}" />
	   
	   <t:dgToolBar title="添加" preinstallWidth="2" operationCode="msgTemplateManager_addmsgTemplate_add" height="500" url="msgTemplateController.do?msgTemplateEdit" funname="add"></t:dgToolBar>
	   <t:dgToolBar title="批量删除"  operationCode="msgTemplateManager_deletemsgTemplate_batchDelete" url="msgTemplateController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
	  </t:datagrid>
  </div>
</div>
<script>
$(document).ready(function(){
	redrawEasyUI($(".easyui-layout"));
});
</script>
