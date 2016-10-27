<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:datagrid name="operLogList"   checkbox="true" fitColumns="false" title="系统日志" actionUrl="operLogController.do?datagrid" idField="id" fit="true"  queryMode="separate"    pagination="true" sortName="time" sortOrder="desc">
   <t:dgCol title="id"  field="id" hidden="false"  width="120"></t:dgCol>
   <t:dgCol title="操作名"  field="opname"   width="120" ></t:dgCol>
   <t:dgCol title="主部门"  field="createDeptId" hidden="false"  width="120" query="true" queryInputType="combotree" comboUrl="deptController.do?orgSelectTagTree&orgCode=rootOrg" aysn="true"></t:dgCol>
   <t:dgCol title="用户名"  field="userName" hidden="true" query="true" width="120" queryInputType="text"  align="center"></t:dgCol>
   <t:dgCol title="执行时间"  field="time" hidden="true"  width="150" align="center" formatter="yyyy-MM-dd HH:mm:ss" queryInputType="datebox" queryMode="group" query="true"></t:dgCol>
   <t:dgCol title="ip地址"  field="ip" hidden="true"  width="120" align="center"></t:dgCol>
   <t:dgCol title="模块名"  field="moduleName"  query="true" width="120"   queryInputType="text" ></t:dgCol>
   <t:dgCol title="日志信息"  field="detail"   width="300" ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt  title="删除" url="operLogController.do?delete&id={id}" callback="reflash" operationCode="optLogManager_operLogDelete_delete"/>
   <t:dgToolBar  title="批量删除"  icon="awsm-icon-remove" url="operLogController.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash" operationCode="optLogManager_operLogBatchDelete_batchDelete"></t:dgToolBar>
  <t:dgToolBar title="查看" icon="awsm-icon-search" url="operLogController.do?operLogEdit" funname="detail" buttonType="GridDetail" width="700" height="400" operationCode="optLogManager_operLogView_view"></t:dgToolBar>
  </t:datagrid>
 <script type="text/javascript">
 /* function reflash(){
	  $("#operLogList").datagrid("reload");
  } */
  $(function(){
	 redrawEasyUI($("#page_content"));
 });
 </script>