<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:datagrid name="formRuleList" checkbox="true" fitColumns="false" title="表单校验规则" actionUrl="formRuleController.do?datagrid" idField="id" fit="true" queryMode="separate">
   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="规则名称"  field="name" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="规则"  field="regulation"  hidden="true"  queryMode="single"  width="300"></t:dgCol>
   <t:dgCol title="提示信息"  field="tipInfo"  hidden="true"  queryMode="single"  width="250"></t:dgCol>
   <t:dgCol title="备注"  field="memo"  hidden="true"  queryMode="single"  width="200"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="140"></t:dgCol>
   
   <t:dgOpenOpt  preinstallWidth="1" url="formRuleController.do?editPage&id={id}" icon="glyphicon glyphicon-pencil icon-color" exParams="{optFlag:'add'}"  height="430" title="编辑"></t:dgOpenOpt>
   <t:dgOpenOpt  preinstallWidth="1" url="formRuleController.do?editPage&id={id}" icon="glyphicon glyphicon-search icon-color" exParams="{optFlag:'detail'}" height="430" title="查看"></t:dgOpenOpt>
   <t:dgDelOpt   title="删除" icon="glyphicon glyphicon-remove icon-color" url="formRuleController.do?delete&ids={id}" />
 
   <t:dgToolBar operationCode="dictManager_addDictType_add" title="添加" preinstallWidth="1" height="430" icon="glyphicon glyphicon-plus icon-color" url="formRuleController.do?editPage" funname="add"></t:dgToolBar>
   <t:dgToolBar operationCode="dictManager_batchDeleteDictType_batchDelete" title="批量删除" icon="glyphicon glyphicon-trash icon-color" url="formRuleController.do?delete" funname="deleteALLSelect"></t:dgToolBar>
 
</t:datagrid>
 <script type="text/javascript">
	 $(function(){
		 redrawEasyUI($("#page_content"));
	 });
 </script>