<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px;">
		<t:datagrid name="completeTaskList" checkbox="true" fitColumns="true" title="任务列表"
			actionUrl="taskController.do?completeTaskDatagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="任务Id" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="结点key" field="taskDefinitionKey" hidden="false"></t:dgCol>
			<t:dgCol title="流程实例id" field="processInstanceId" hidden="false"></t:dgCol>
			<t:dgCol title="回退" field="isCanRedo" hidden="false" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="撤销" field="isCanrecover" hidden="false" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="项目名称"  field="businessName" hidden="true" width="100"></t:dgCol>
			<t:dgCol title="任务名称" field="name" query="true" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="实例标题" field="subject" query="true" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="所属流程" field="processName" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="实例创建人" field="creator" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="任务创建时间" field="createTime" formatter="yyyy-MM-dd HH:mm:ss" hidden="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="任务执行时间" field="duration" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>

			<t:dgOpenOpt width="1000" height="600" exParams="{noheader:true,optFlag:'close'}" url="processInstanceController.do?info&instId={processInstanceId}&id={id}&nodeId={taskDefinitionKey}&isCompleteTask=1" title="详细"></t:dgOpenOpt>
<%-- 			<t:dgFunOpt exp="isCanRedo#eq#true" title="回退" icon="awsm-icon-lock grey" funname="doLocked(id,locked)" /> --%>
<%-- 			<t:dgFunOpt exp="isCanrecover#eq#true" title="撤销" icon="awsm-icon-lock grey" funname="doLocked(id,locked)" /> --%>
<%-- 			<t:dgFunOpt title="审批历史" icon="awsm-icon-lock grey" funname="doLocked(id,locked)" /> --%>

<%-- 			<t:dgToolBar title="批量审批" preinstallWidth="1" height="400" icon="awsm-icon-plus" url="jobController.do?jobEdit" --%>
<%-- 				funname="add"></t:dgToolBar> --%>
		</t:datagrid>
	</div>
</div>
<script>
	$(document).ready(function() {
		redrawEasyUI($(".easyui-layout"));
	});
</script>
