<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="easyui-layout" style="width: 100%; height: 100%; border: 0px">
	<div region="center" border="false" style="padding: 5px;">
		<t:datagrid name="processInstHistoryList" title="流程实例管理" checkbox="true" fitColumns="true"
			actionUrl="processInstHistoryController.do?datagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="扩展id" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="实例id" field="actInstId" hidden="false"></t:dgCol>
			<t:dgCol title="实例名称" query="true" width="40" field="title"></t:dgCol>
			<t:dgCol title="创建人" query="true" width="10" field="createUserName"></t:dgCol>
			<t:dgCol title="创建时间" query="true" width="20" field="createTime"></t:dgCol>
			<t:dgCol title="结束时间" query="true" width="20" field="endTime"></t:dgCol>
			<t:dgCol title="持续时间" width="15" field="duration"></t:dgCol>
			<t:dgCol title="启用状态" replace="完成_2,运行中_1,禁用_0" width="15" field="status"></t:dgCol>

			<t:dgCol title="操作" width="20" field="opt"></t:dgCol>
			<t:dgOpenOpt title="详细信息" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processInstanceEdit&id={actInstId}" exParams="{optFlag:'detail'}"
				preinstallWidth="1" height="400"></t:dgOpenOpt>
			<t:dgOpenOpt title="流程图" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processImage&actInstId={actInstId}" width="840" height="500"></t:dgOpenOpt>
			<t:dgOpenOpt title="审批历史" icon="awsm-icon-zoom-in green"
				url="definitionController.do?showTaskOpinions&actInstId={actInstId}" width="700" height="500"></t:dgOpenOpt>
			<t:dgOpenOpt title="抄送人" icon="awsm-icon-zoom-in green"
				url="processInsCptoController.do?showCptoList&id={actInstId}&type=actInstId" width="700" height="300" />

		</t:datagrid>
	</div>
</div>
