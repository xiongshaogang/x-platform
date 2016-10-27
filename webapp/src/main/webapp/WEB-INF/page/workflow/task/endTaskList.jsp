<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px;">
		<t:datagrid name="endTaskList" checkbox="true" fitColumns="true" title="任务列表"
			actionUrl="taskController.do?endTaskDatagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="任务Id" field="id" hidden="true"></t:dgCol>
			<t:dgCol title="流程实例id" field="processInstanceId" hidden="false"></t:dgCol>
			<t:dgCol title="任务名称" field="name" query="true" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="实例标题" field="subject" query="true" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="所属流程" field="processName" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="实例创建人" field="creator" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="任务创建时间" field="createTime" formatter="yyyy-MM-dd HH:mm:ss" hidden="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="任务执行时间" field="duration" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
			<t:dgOpenOpt width="1000" height="700" exParams="{noheader:true}"
				url="processInstanceController.do?info&instId={processInstanceId}&id={id}" title="详细"></t:dgOpenOpt>
			<t:dgFunOpt title="转发" icon="awsm-icon-zoom-in green"
				funname="divertTask(processInstanceId)" />
			<t:dgOpenOpt title="详细信息" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processInstanceEdit&id={processInstanceId}" exParams="{optFlag:'detail'}"
				preinstallWidth="1" height="400" />
			<t:dgOpenOpt title="流程图" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processImage&actInstId={processInstanceId}" width="840" height="500" />
			<t:dgOpenOpt title="审批历史" icon="awsm-icon-zoom-in green"
				url="definitionController.do?showTaskOpinions&actInstId={processInstanceId}" width="700" height="500" />
			<t:dgOpenOpt title="抄送人" icon="awsm-icon-zoom-in green"
				url="processInsCptoController.do?showCptoList&id={id}&type=taskId" width="700" height="400" />
		</t:datagrid>
	</div>
</div>
<script>
	$(document).ready(function() {
		redrawEasyUI($(".easyui-layout"));
	});
</script>
