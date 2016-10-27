<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div id="formFieldEntityList_div" class="easyui-layout" style="width: 100%; height: 100%">
	<div region="west" split="true" title="页面实体选择" style="width: 200px">
		<t:tree id="formEntityTree" gridTreeFilter="formEntityID" clickFirstNode="true" url="generateConfigController.do?getFormEntityTree&formTypeID=${formTypeID}"
			gridId="formFieldEntityList">
		</t:tree>
	</div>
	<div region="center" style="padding: 0px; border: 0px;" split="true">
		<t:datagrid pageSize="20" name="formFieldEntityList" title="字段详细配置列表" fitColumns="true" autoLoadData="false" idField="id" fit="true"
			actionUrl="generateFieldController.do?datagrid" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="字段名称" field="fieldName" width="120" query="true"></t:dgCol>
			<t:dgCol title="字段显示名" field="fieldLabel" width="120"></t:dgCol>
			<t:dgCol title="字段类型" field="fieldType" width="120"></t:dgCol>
			<t:dgCol title="字段长度" field="fieldLength" width="120"></t:dgCol>
			<t:dgCol title="字段精度" field="fieldPersion" width="120"></t:dgCol>
			<%-- <t:dgCol title="字段描述" field="description" width="120"></t:dgCol> --%>
			<t:dgCol title="表格显示" field="listShow" width="120" replace="是_Y,否_N"></t:dgCol>
			<t:dgCol title="查询显示" field="queryShow" width="120" replace="是_Y,否_N"></t:dgCol>
			<t:dgCol title="编辑显示" field="editShow" width="120" replace="是_Y,否_N"></t:dgCol>
			<t:dgCol title="是否唯一" field="isUnique" width="120" replace="是_Y,否_N"></t:dgCol>
			<t:dgCol title="是否只读" field="isReadonly" width="120" replace="是_Y,否_N"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="80"></t:dgCol>
			<t:dgFunOpt funname="editRow(id)" title="编辑" operationCode="templateManager_formFieldEntitySave_other"></t:dgFunOpt>
		</t:datagrid>
	</div>
</div>
<script type="text/javascript">
	
	var field = 'id,fieldName,fieldEntityId,fieldLabel,' + 'fieldType,fieldLength,fieldPersion,description,listShow,'
			+ 'queryShow,isReadonly,editShow,isUnique';
	function saveDatagrid() {
		var jsonArray = getEditData();
		$.ajax({
			url : 'generateFieldController.do?doAddOrUpdate',
			data : {
				formFieldVOList : jsonArray,
				field : field
			},
			cache : false,
			success : function(result) {
				//tip(result.msg);
			}
		});
	}

	function editRow(id) {
		var url = 'generateFieldController.do?goFormFieldUpdate&formFieldId=' + id;
		createwindow('字段配置', url, null, 400, 2, {
			optFlag : 'update'
		});
	}
</script>