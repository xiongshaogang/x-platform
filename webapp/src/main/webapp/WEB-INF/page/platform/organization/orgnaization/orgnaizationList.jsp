<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
function getEmployee(node) {
	//没有权限  
	if(node.attributes['available'] == 0){
		return false;
	}
	$("#porgId").val(node.id);
	$("#porgName").val(node.text);
	$("#userList").datagrid('options').queryParams.orgId=node.id;
	$('#userList').datagrid('reload');
	$("#userList").datagrid("getPanel").panel("setTitle", node.text+"用户列表");
}

function loadOrgSuccess(node,data){
	if(node){
		nodes=$("#orgnaization_tree").tree("getChildren",node.target);
	}else{
		nodes=$("#orgnaization_tree").tree("getRoots");
	}
	/* $.each(nodes,function(i,e){
		if(e.attributes['available'] == 0){
		    disabledTreeNode($(e.target));
		}
	}); */
};
</script>
<div class="easyui-layout" fit="true" style="width: 100%; height: 100%; border: 0px">
	<div region="west" split="true" title="机构树" style="width: 230px;" data-options="tools:[
				{
					iconCls:'glyphicon glyphicon-plus icon-color',
					handler:function(){addOrg();}
				}]">
		<t:tree id="orgnaization_tree" showOptMenu="false"  url="orgnaizationController.do?tree"
		   onLoadSuccess="loadOrgSuccess(node,data)" clickFirstNode="true" clickPreFun="getEmployee(node)"></t:tree>
	</div>
    <div id="orgMenu" class="easyui-menu" style="width:120px;display:none">
		<div onclick="addDept()" data-options="iconCls:'awsm-icon-plus bigger-140 green'">新增子部门</div>
		<div onclick="removeOrg()" data-options="iconCls:'awsm-icon-minus bigger-140 green'">解散团队</div>
		<div onclick="updateOrg()" data-options="iconCls:'awsm-icon-pencil bigger-140 green'">修改团队</div>
		<div onclick="viewOrg()" data-options="iconCls:'awsm-icon-zoom-in bigger-140 green'">查看团队</div>
		<div onclick="distributionUser()" data-options="iconCls:'awsm-icon-cogs bigger-140 green'">添加员工</div>
		<!-- <div onclick="distributionRole()" data-options="iconCls:'awsm-icon-cogs bigger-140 green'">分配角色</div> -->
	</div>
	<div id="deptMenu" class="easyui-menu" style="width:120px;display:none">
		<div onclick="addDept()" data-options="iconCls:'awsm-icon-plus bigger-140 green'">新增子部门</div>
		<div onclick="removeOrg()" data-options="iconCls:'awsm-icon-minus bigger-140 green'">删除部门</div>
		<div onclick="updateOrg()" data-options="iconCls:'awsm-icon-pencil bigger-140 green'">修改部门</div>
		<div onclick="viewOrg()" data-options="iconCls:'awsm-icon-zoom-in bigger-140 green'">查看部门</div>
		<div onclick="distributionUser()" data-options="iconCls:'awsm-icon-cogs bigger-140 green'">添加员工</div>
		<!-- <div onclick="distributionRole()" data-options="iconCls:'awsm-icon-cogs bigger-140 green'">分配角色</div> -->
	</div>
	
	<div region="center" style="border: 0px">
		<t:datagrid name="userList" autoLoadData="false" checkbox="true" queryMode="group" fitColumns="true" 
			                title="员工列表" actionUrl="userController.do?datagrid" idField="id" fit="true">
			<t:dgCol title="员工ID" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="员工状态" field="flag" hidden="false"></t:dgCol>
			<t:dgCol title="岗位ID" field="currentOrgId" hidden="false"></t:dgCol>
			<t:dgCol title="员工岗位ID" field="currentUserOrgId" hidden="false"></t:dgCol>
			<t:dgCol title="员工姓名" width="100" field="name" query="true"></t:dgCol>
			<t:dgCol title="员工编号" width="100" field="code" query="true"></t:dgCol>
			<t:dgCol title="性别"  width="80" dictCode="sex" field="sex"></t:dgCol>
			<t:dgCol title="手机号码" width="100" field="phone"></t:dgCol>
			<t:dgCol title="座机" width="80" field="telephone"></t:dgCol>
			<t:dgCol title="组织" width="100" field="orgNames"></t:dgCol>
			<t:dgCol title="操作" width="180" field="opt"></t:dgCol>
			<t:dgOpenOpt url="userController.do?userEdit&id={currentUserOrgId}" operationCode="organizationManager_updateEmp_edit"
				icon="glyphicon glyphicon-pencil icon-color" exParams="{optFlag:'update'}" preinstallWidth="2" height="520" title="编辑"></t:dgOpenOpt>
			<t:dgOpenOpt url="userController.do?userEdit&id={currentUserOrgId}" operationCode="organizationManager_detailEmp_view"
				icon="glyphicon glyphicon-search icon-color" exParams="{optFlag:'detail'}" preinstallWidth="2" height="520" title="查看"></t:dgOpenOpt>
			<t:dgDelOpt title="删除" callback="refreshOrgTree()" operationCode="organizationManager_deleteEmp_delete"
				icon="glyphicon glyphicon-remove icon-color" url="userController.do?delete&ids={currentUserOrgId }" />
			<t:dgFunOpt title="初始化密码" icon="glyphicon glyphicon-refresh icon-color" funname="initPwd(id)" />
			<t:dgFunOpt exp="flag#eq#1" title="锁定" icon="glyphicon glyphicon-pause icon-color" funname="doActive(id,{3})" />
			<t:dgFunOpt exp="flag#eq#2" title="激活" icon="glyphicon glyphicon-play icon-color" funname="doActive(id,{1})" />
			<t:dgFunOpt exp="flag#eq#3" title="解锁" icon="glyphicon glyphicon-forward icon-color" funname="doActive(id,{1})" />
			
			<t:dgToolBar title="添加" preinstallWidth="2"  onclick="addUser()" height="620" icon="glyphicon glyphicon-plus icon-color"
				operationCode="organizationManager_addEmp_add" url="userController.do?userEdit" funname="add"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="glyphicon glyphicon-remove icon-color" buttonType="GridDelMul" operationCode="organizationManager_batchDeleteEmp_batchDelete"
				url="userController.do?delete" funname="deleteALLSelect" rowId="currentUserOrgId"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
<input type="hidden" id="porgId">
<input type="hidden" id="porgName">
<script type="text/javascript">
    $(function(){
		setTimeout(function(){
			$("#orgnaization_tree").tree("options").onContextMenu = function(e, node){
				/* if(node.attributes['available'] == 0){
					return false;
				} */
				e.preventDefault();
				$('#orgnaization_tree').tree('select', node.target);
				var type = node.attributes['type'];
				// 显示菜单
				if(type =='dept'){
					$('#deptMenu').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				}else{
					$('#orgMenu').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
			 	}
			};
		}, 0);
	
    }); 
    
    
    
	function refreshOrgTree() {
		var node = $("#orgnaization_tree").tree("getSelected");
		var parent = $("#orgnaization_tree").tree("getParent",node.target);
		if(parent){
			$("#orgnaization_tree").tree('reload',parent.target);
		}else{
			$("#orgnaization_tree").tree('reload');
		}
	}
    

	function addUser(){
		var parentId = $("#porgId").val();
		if(parentId == ''){
			tip('请选择组织添加员工');
			return;
		}
		var node = $('#orgnaization_tree').tree('getSelected');
		var type = node.attributes['type'];
		createwindow("添加","userController.do?userEdit&orgId="+parentId , 700, 550, null, {optFlag : 'add'});
	}
	
	function addOrg() {
		createwindow("新增机构", "orgnaizationController.do?orgnaizationEdit&type=org&parent.id=-1" , 800, 500, null, {optFlag : 'add'});
	}
	function addDept() {
		var node = $('#orgnaization_tree').tree('getSelected');
		parentId = node.id;
		parentName = node.text;
		var type = node.attributes['type'];
		$("#orgType").val(type);
		$("#orgParentId").val(parentId);
		$("#orgParentName").val(parentName);
		createwindow("新增机构", "orgnaizationController.do?orgnaizationEdit&parent.id="+parentId , 800, 500, null, {optFlag : 'add'});
	}

	function removeOrg() {
		var node = $('#orgnaization_tree').tree('getSelected');
		if(!node){
			tip("请选择需要删除的机构");
			return false;
		}
		$.messager.confirm("提示信息", "确定删除所有所选数据?", function(r) {
			if (r) {
				$.ajax({
					url : 'orgnaizationController.do?delete',
					type : 'post',
					data : {
						ids : node.id
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							tip(d.msg);
							var parent = $("#orgnaization_tree").tree('getParent',node.target);
							$("#orgnaization_tree").tree('reload',parent.target);
						}
					}
				});
			}
		});
	}

	function updateOrg() {
		var node = $('#orgnaization_tree').tree('getSelected');
		parentId = node.id;
		parentName = node.text;
		$("#orgParentId").val(parentId);
		$("#orgParentName").val(parentName);
		createwindow("修改机构", "orgnaizationController.do?orgnaizationEdit&id=" + 
				                     node.id, 800, 500, null, { optFlag : 'update'});
	}

	function viewOrg() {
		var node = $('#orgnaization_tree').tree('getSelected');
		parentId = node.id;
		parentName = node.text;
		$("#orgParentId").val(parentId);
		$("#orgParentName").val(parentName);
		createwindow("查看机构", "orgnaizationController.do?orgnaizationEdit&id=" + node.id, 800,
				500, null, {
					optFlag : 'detail'
				});
	}

	function distributionRole() {
		var node = $('#orgnaization_tree').tree('getSelected');
		var url = "roleController.do?getRoleIds4Orgs&orgId=" + node.id;
		var saveUrl = "roleController.do?saveDistributionOrgRole&orgId=" + node.id;
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : url,// 请求的action路径
			success : function(data) {
				goRoleSavePage({
					ids : data,
					saveUrl : saveUrl
				})
			}
		});
	}

	
	function distributionUser() {
		var node = $('#orgnaization_tree').tree('getSelected');
		var url = "userOrgController.do?getUserIds4Orgs&orgId=" + node.id;
		var saveUrl = "userOrgController.do?saveUsersOrg&orgId=" + node.id;
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : url,// 请求的action路径
			success : function(data) {
				goUserSavePage({
					title : "岗位分配用户",
					ids : data,
					saveUrl : saveUrl,
					callback: "refreshGrid"
				})
			}
		});
	}
	function refreshGrid(data){
		$("#userList").datagrid("reload");
	}

	function initPwd(id) {
		doSubmit("userController.do?initPwd&id=" + id, "userList");
	}
	
	function doActive(id, flag) {
		doSubmit("userController.do?doActive&id=" + id + "&flag=" + flag, "userList");
	}
	
	$(document).ready(function(){
		redrawEasyUI($(".easyui-layout"));
	});
</script>