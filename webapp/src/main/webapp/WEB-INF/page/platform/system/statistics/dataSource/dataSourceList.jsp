<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:datagrid name="dataSourceList"   checkbox="true" fitColumns="false" title="数据源" actionUrl="dataSourceController.do?datagrid" idField="id" fit="true"  queryMode="separate"    pagination="true" >
   <t:dgCol title="主键"  field="id" hidden="false"   ></t:dgCol>
   <t:dgCol title="名称"  field="name"  query="true" width="80"  ></t:dgCol>
   <t:dgCol title="编码"  field="code"   width="80"  ></t:dgCol>
   <t:dgCol title="类型"  field="type"   width="80"  ></t:dgCol>
   <t:dgCol title="数据值"  field="value"   width="300"  ></t:dgCol>
   <t:dgCol title="描述"  field="description"   width="250"  ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgOpenOpt preinstallWidth="2" height="450" icon="awsm-icon-edit blue" url="dataSourceController.do?dataSourceEdit&id={id}" exParams="{optFlag:'add'}" title="编辑" operationCode="dataSourceManager_dataSourceEdit_edit"></t:dgOpenOpt>
   <t:dgOpenOpt preinstallWidth="2" height="450" icon="awsm-icon-zoom-in green" url="dataSourceController.do?dataSourceEdit&id={id}" exParams="{optFlag:'detail'}" title="查看" operationCode="dataSourceManager_dataSourceView_view"></t:dgOpenOpt>
   <t:dgDelOpt title="删除" url="dataSourceController.do?delete&id={id}" callback="reflash" operationCode="dataSourceManager_dataSourceDelete_delete"/>
   <t:dgToolBar title="录入" icon="awsm-icon-plus" url="dataSourceController.do?dataSourceEdit" funname="add" buttonType="GridAdd"  preinstallWidth="1" height="460" operationCode="dataSourceManager_dataSourceAdd_add"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="dataSourceController.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash" operationCode="dataSourceManager_sourceBatchDelete_batchDelete"></t:dgToolBar>
  </t:datagrid>
 <script type="text/javascript">
 function reflash(){
	  $("#dataSourceList").datagrid("reload");
  }
   $(function(){
	 redrawEasyUI($("#page_content"));
 });
 </script>