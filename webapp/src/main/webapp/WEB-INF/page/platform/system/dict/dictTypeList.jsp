<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <t:datagrid name="dictTypeList" checkbox="true" fitColumns="true" title="数据字典类型" actionUrl="dictTypeController.do?datagrid" idField="id" fit="true" queryMode="separate">
  <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
  <t:dgCol title="类型名称"  field="name" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
  <t:dgCol title="类型编码"  field="code" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
  <t:dgCol title="类型"  field="type" replace="用户_user,系统_system" hidden="true"  queryMode="single"  width="120"></t:dgCol>
  <t:dgCol title="值类型"  field="valueType" replace="下拉框_selected,树结构_tree,复选框_checkbox" hidden="true"  queryMode="single"  width="120"></t:dgCol>
  <t:dgCol title="描述"  field="description"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
  <t:dgCol title="操作" field="opt" width="120"></t:dgCol>
 
  <t:dgOpenOpt operationCode="dictManager_editDictType_edit" preinstallWidth="1" url="dictTypeController.do?dictTypeEdit&id={id}" icon="glyphicon glyphicon-pencil icon-color" exParams="{optFlag:'add'}"  height="430" title="编辑"></t:dgOpenOpt>
  <t:dgOpenOpt operationCode="dictManager_viewDictType_view" preinstallWidth="1" url="dictTypeController.do?dictTypeEdit&id={id}" icon="glyphicon glyphicon-search icon-color" exParams="{optFlag:'detail'}" height="430" title="查看"></t:dgOpenOpt>
  <t:dgDelOpt operationCode="dictManager_deleteDictType_delete"  title="删除" icon="glyphicon glyphicon-remove icon-color" url="dictTypeController.do?delete&ids={id}" />
  <t:dgOpenOpt operationCode="dictManager_dictValue_other" title="字典数据值" icon="glyphicon glyphicon-list icon-color" url="dictValueController.do?dictValue&typeId={id}" width="1000" height="500"   />
 
  <t:dgToolBar operationCode="dictManager_addDictType_add" title="添加" preinstallWidth="1" height="430" icon="glyphicon glyphicon-plus icon-color" url="dictTypeController.do?dictTypeEdit" funname="add"></t:dgToolBar>
  <t:dgToolBar operationCode="dictManager_batchDeleteDictType_batchDelete" title="批量删除" icon="glyphicon glyphicon-trash icon-color" url="dictTypeController.do?delete" funname="deleteALLSelect"></t:dgToolBar>
 </t:datagrid>
 <script type="text/javascript">
	 $(function(){
		 redrawEasyUI($("#page_content"));
	 });
 </script>