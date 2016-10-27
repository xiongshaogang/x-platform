
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>

		<t:datagrid name="formLogoList" title="商品列表" actionUrl="formLogoController.do?datagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="false" width="150" align="center"></t:dgCol>
			<t:dgCol title="文件名称" field="name" query="true" width="120" align="center"></t:dgCol>
			<t:dgCol title="文件编码" field="code" query="true" width="120" align="center"></t:dgCol>
			
			<t:dgCol title="操作" field="opt" width="160"></t:dgCol>
			<t:dgOpenOpt operationCode="formLogoManager_editFormLogo_edit" url="formLogoController.do?formLogoEdit&id={id}" preinstallWidth="2" icon="glyphicon glyphicon-pencil icon-color" exParams="{optFlag:'update'}" height="500" title="编辑"></t:dgOpenOpt>
			<t:dgOpenOpt operationCode="formLogoManager_viewFormLogo_view" url="formLogoController.do?optFlag=detail&formLogoEdit&id={id}" preinstallWidth="2" icon="glyphicon glyphicon-search icon-color" exParams="{optFlag:'detail'}" height="500" title="查看"></t:dgOpenOpt>
			<t:dgDelOpt operationCode="formLogoManager_deleteFormLogo_delete" title="删除" icon="glyphicon glyphicon-remove icon-color" url="formLogoController.do?delete&id={id}" />
	
			<t:dgToolBar title="添加"  operationCode="formLogoManager_addFormLogo_add" preinstallWidth="2" height="500" icon="glyphicon glyphicon-plus icon-color" buttonType="GridAdd" url="formLogoController.do?formLogoEdit" funname="add"></t:dgToolBar>
			<t:dgToolBar title="批量删除" operationCode="formLogoManager_deleteBatchFormLogo_batchDelete" icon="glyphicon glyphicon-trash icon-color" buttonType="GridDelMul" url="formLogoController.do?delete" funname="deleteALLSelect"></t:dgToolBar>
			</t:datagrid>


<script type="text/javascript">
	$(function(){
		 redrawEasyUI($("#page_content"));
	});
</script>