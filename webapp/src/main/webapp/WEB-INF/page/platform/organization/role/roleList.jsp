<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:datagrid name="roleList" checkbox="true" fitColumns="true" title="角色列表" actionUrl="roleController.do?datagrid" idField="id" fit="true" queryMode="separate">
	<t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="角色名称" field="name" query="true" hidden="true" width="80" queryMode="single"></t:dgCol>
	<t:dgCol title="角色编码" field="code" query="true" hidden="true" width="80" queryMode="single"></t:dgCol>
	<t:dgCol title="角色类型" field="definedFlag" replace="系统默认角色_1,私人角色_2,公共角色_3" query="true" hidden="true" width="150" queryMode="single"></t:dgCol>
	<t:dgCol title="允许删除" field="allowDelete" replace="是_Y,否_N" hidden="true" queryMode="single"></t:dgCol>
	<t:dgCol title="允许编辑" field="allowEdit" replace="是_Y,否_N" hidden="true" queryMode="single"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="150"></t:dgCol>

	<t:dgOpenOpt title="编辑" icon="glyphicon glyphicon-pencil icon-color" exp="definedFlag#ne#3" preinstallWidth="1" height="450" url="roleController.do?roleEdit&id={id}"
		exParams="{optFlag:'add'}"></t:dgOpenOpt>
	<t:dgOpenOpt title="查看" icon="glyphicon glyphicon-search icon-color" preinstallWidth="1" height="450" url="roleController.do?roleEdit&id={id}&optCode=detail"
		exParams="{optFlag:'detail'}"></t:dgOpenOpt>
	<t:dgDelOpt title="删除" icon="glyphicon glyphicon-remove icon-color" exp="definedFlag#ne#3" url="roleController.do?delete&id={id}" />

	<t:dgOpenOpt title="权限设置" exParams="{noheader:true}" height="500" width="980" icon="glyphicon glyphicon-cog icon-color"
		url="roleController.do?rolePrivillageSetting&roleId={id}"></t:dgOpenOpt>

	<t:dgToolBar preinstallWidth="1" height="450" title="新增" icon="glyphicon glyphicon-plus icon-color" url="roleController.do?roleEdit&parentId=" funname="add"></t:dgToolBar>

</t:datagrid>
<script type="text/javascript">
	function setfunbyrole(id, roleName) {
		$("#role-module-panel").panel({
			title : roleName + "角色:当前权限",
			href : "roleController.do?moduleSelect&roleId=" + id
		});
		$('#role-module-panel').panel("refresh");
	}
	//进入资料文件夹操作权限选择页
	function dataDirBtnByRoleSelect(id, userName) {
		$("#role-module-panel").panel({
			title : userName + "角色:当前资料文件夹操作权限",
			href : "roleController.do?dataDirBtnByRoleSelect&roleId=" + id
		});
	}
	$(function(){
		 redrawEasyUI($("#page_content"));
	});
</script>
