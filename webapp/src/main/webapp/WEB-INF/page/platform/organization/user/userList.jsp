<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px;">
		<t:datagrid name="userList" checkbox="true" fitColumns="true" title="用户列表" actionUrl="userController.do?datagrid"
			idField="userId" fit="true" queryMode="group">
			<t:dgExportParams title="系统用户列表" sheetName="系统用户列表" entityClass="com.xplatform.base.orgnaization.user.mybatis.vo.UserVo"
				exportServer="com.xplatform.base.orgnaization.user.export.UserExcelExport"
				dataHanlder="com.xplatform.base.orgnaization.user.export.UserHandler" needHandlerFields="userName,locked" />
			<t:dgImportParams titleRows="2" sheetNum="2" startCell="0" endCell="2" entityClass="com.xplatform.base.orgnaization.user.mybatis.vo.UserVo"
			name="userResult" templateCode="userList" submitUrl="userController.do?userImportSave&a='d'" />
			<t:dgCol title="主键" field="userId" hidden="false"></t:dgCol>
			<t:dgCol title="是否 删除" field="flag" hidden="false"></t:dgCol>
			<t:dgCol title="是否锁定" field="locked" hidden="false"></t:dgCol>
			<t:dgCol title="是否激活" field="active" hidden="false"></t:dgCol>
			<t:dgCol title="员工姓名" field="empName" query="true" hidden="true" width="50" queryMode="single"></t:dgCol>
			<t:dgCol title="登陆名称" field="userName" query="true" hidden="true" width="50" queryMode="single"></t:dgCol>
			<t:dgCol title="用户编码" field="userCode" hidden="true" width="50" queryMode="single"></t:dgCol>
			<t:dgCol title="用户类型" field="userType" replace="员工_employee,会员_member" hidden="true" width="50" queryMode="single"></t:dgCol>
			<t:dgCol title="电子邮箱" field="email" hidden="true" width="80" queryMode="single"></t:dgCol>
			<t:dgCol title="主岗位" field="jobName" hidden="true" width="50" queryMode="single"></t:dgCol>
			<t:dgCol title="主部门" field="deptName" hidden="true" width="50" queryMode="single"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="150"></t:dgCol>

			<t:dgDelOpt exp="flag#eq#N" title="删除" icon="awsm-icon-trash red" url="userController.do?delete&id={userId}" />
			<t:dgFunOpt title="初始化密码" icon="awsm-icon-undo blue" funname="initPwd(userId)" />
			<t:dgFunOpt exp="active#eq#N" title="激活" icon="awsm-icon-eye-open green" funname="doActive(userId,active)" />
			<t:dgFunOpt exp="active#eq#Y" title="冻结" icon="awsm-icon-eye-close grey" funname="doActive(userId,active)" />
			<t:dgFunOpt exp="locked#eq#N" title="锁定" icon="awsm-icon-lock grey" funname="doLocked(userId,locked)" />
			<t:dgFunOpt exp="locked#eq#Y" title="解锁" icon="awsm-icon-unlock green" funname="doLocked(userId,locked)" />
			<%-- <t:dgOpenOpt title="权限设置" exParams="{noheader:true}" height="500" width="980" icon="awsm-icon-cogs black"
				url="userController.do?userPrivillageSetting&userId={userId}"></t:dgOpenOpt> --%>

			<t:dgToolBar icon="awsm-icon-plus" preinstallWidth="1" height="450" title="新增"
				url="userController.do?userEdit&parentId=" funname="add"></t:dgToolBar>
			<t:dgToolBar icon="awsm-icon-remove" title="删除" url="userController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="awsm-icon-edit" preinstallWidth="1" height="450" url="userController.do?userEdit"
				funname="update" />
			<t:dgToolBar title="查看" icon="awsm-icon-search" preinstallWidth="1" height="450" url="userController.do?userEdit"
				funname="detail" />
			<t:dgToolBar title="导出" buttonType="GridExcelExport" />
			<t:dgToolBar title="导入" buttonType="GridExcelImport" />
		</t:datagrid>
	</div>
	<!-- 
  <div region="east" style="width: 400px;" split="true">
	  <div class="easyui-panel" title="权限设置" style="padding: 10px;" fit="true" border="false" id="user-module-panel"></div>
  </div>
   -->
</div>
<script type="text/javascript">
	$(document).ready(function() {
		redrawEasyUI($(".easyui-layout"));
	});
	function setfunbyuser(id, userName) {
		$("#user-module-panel").panel({
			title : userName + "用户:当前权限",
			href : "userController.do?moduleSelect&userId=" + id
		});
		$('#user-module-panel').panel("refresh");
	}
	function typebyuser(id, userName) {
		$("#user-module-panel").panel({
			title : userName + "用户:当前系统类型权限",
			href : "userController.do?sysTypeSelect&userId=" + id
		});
		$('#user-module-panel').panel("refresh");
	}
	function doLocked(id, locked) {
		doSubmit("userController.do?doLocked&id=" + id + "&locked=" + locked, "userList");
	}
	function doActive(id, active) {
		doSubmit("userController.do?doActive&id=" + id + "&active=" + active, "userList");
	}
	function initPwd(id) {
		doSubmit("userController.do?initPwd&id=" + id, "userList");
	}

	//进入资料文件夹操作权限选择页
	function dataDirBtnByUserSelect(id, userName) {
		$("#user-module-panel").panel({
			title : userName + "用户:当前资料文件夹操作权限",
			href : "userController.do?dataDirBtnByUserSelect&userId=" + id
		});
	}
</script>
