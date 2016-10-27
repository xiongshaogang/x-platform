<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
	  <t:datagrid name="agentSettingList" checkbox="true" fitColumns="true" title="流程代理列表" actionUrl="agentSettingController.do?datagrid" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="代理名称"  field="name"  query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="代理编码"  field="code"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="授权人"  field="authName" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="代理类型"  field="type"  hidden="true"  queryMode="single"  width="120" replace="全部代理_1,部分代理_2,条件代理_3"></t:dgCol>
	   <t:dgCol title="代理人"  field="agentName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="状态"  field="status"  hidden="true"  queryMode="single"  width="120" replace="启用_Y,禁用_N"></t:dgCol>
	   <t:dgCol title="开始时间"  field="startTime"  hidden="true"  queryMode="single"  width="120" formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
	   <t:dgCol title="结束时间"  field="endTime"  hidden="true"  queryMode="single"  width="120" formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
	  
	  
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	   <t:dgOpenOpt title="编辑" icon="awsm-icon-edit blue" operationCode="agentSettingManager_editagentSetting_edit" url="agentSettingController.do?agentSettingEdit&id={id}" exParams="{optFlag:'add'}" preinstallWidth="2" height="420" ></t:dgOpenOpt>
   	   <t:dgOpenOpt title="查看" icon="awsm-icon-zoom-in green" operationCode="agentSettingManager_viewagentSetting_view" url="agentSettingController.do?agentSettingEdit&id={id}" exParams="{optFlag:'detail'}" preinstallWidth="2" height="420" ></t:dgOpenOpt>
	   <t:dgDelOpt title="删除" icon="awsm-icon-trash red" operationCode="agentSettingManager_deleteagentSetting_delete" url="agentSettingController.do?delete&id={id}" />

	   <t:dgToolBar title="添加" preinstallWidth="2" operationCode="agentSettingManager_addagentSetting_add" height="420" icon="awsm-icon-plus" url="agentSettingController.do?agentSettingEdit" funname="add"></t:dgToolBar>
	   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" operationCode="agentSettingManager_deleteagentSetting_batchDelete" url="agentSettingController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
	  </t:datagrid>
  </div>
</div>
<script>
$(document).ready(function(){
	redrawEasyUI($(".easyui-layout"));
});
</script>
