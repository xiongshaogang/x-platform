<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>

<t:datagrid name="sysParameterList" checkbox="true" fitColumns="true" title="系统参数" actionUrl="sysParameterController.do?datagrid" idField="id" fit="true" queryMode="separate">
	<t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	<t:dgCol title="参数名称"  field="name" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	<t:dgCol title="参数编码"  field="code"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	<t:dgCol title="参数值"  field="value"  hidden="true"  queryMode="single"  width="150"></t:dgCol>
	<t:dgCol title="是否可修改"  field="updateFlag"  hidden="true" replace="是_Y,否_N" queryMode="single"  width="100"></t:dgCol>
	<t:dgCol title="参数类型名称"  field="type" query="true" hidden="true"  queryInputType="combobox"  dictCode="settingType"  width="120"></t:dgCol>
	<t:dgCol title="描述"  field="description"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="160"></t:dgCol>
	<t:dgOpenOpt operationCode="sysParameterManager_editSysParameter_edit" url="sysParameterController.do?editPage&id={id}" preinstallWidth="1" icon="glyphicon glyphicon-pencil icon-color" exParams="{optFlag:'add'}" height="500" title="编辑"></t:dgOpenOpt>
	<t:dgOpenOpt operationCode="sysParameterManager_viewSysParameter_view" url="sysParameterController.do?optFlag=detail&editPage&id={id}" preinstallWidth="1" icon="glyphicon glyphicon-search icon-color" exParams="{optFlag:'detail'}" height="500" title="查看"></t:dgOpenOpt>
	<t:dgDelOpt operationCode="sysParameterManager_deleteSysParameter_delete" title="删除" icon="glyphicon glyphicon-remove icon-color" url="sysParameterController.do?delete&ids={id}" />
	
	<t:dgToolBar title="添加"  operationCode="sysParameterManager_addSysParameter_add" preinstallWidth="1" height="500" icon="glyphicon glyphicon-plus icon-color" buttonType="GridAdd" url="sysParameterController.do?editPage" funname="add"></t:dgToolBar>
	<t:dgToolBar title="批量删除" operationCode="sysParameterManager_deleteBatchSysParameter_batchDelete" icon="glyphicon glyphicon-trash icon-color" buttonType="GridDelMul" url="sysParameterController.do?delete" funname="deleteALLSelect"></t:dgToolBar>
</t:datagrid>


<script type="text/javascript">
	$(function(){
		 redrawEasyUI($("#page_content"));
	});
</script>