<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>


<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
		<t:datagrid name="requestInstanceList" title="流程实例管理" actionUrl="processInstanceController.do?requestInstanceDatagrid">
			<t:dgCol title="实例名称" query="true" width="35" field="title"></t:dgCol>
			<t:dgCol title="acitiviti定义id" query="true" width="35" hidden="false" field="actInstId"></t:dgCol>
			<t:dgCol title="id" query="true" width="35" hidden="false" field="id"></t:dgCol>
			<t:dgCol title="所属流程" query="true" width="20" field="defName"></t:dgCol>
			<t:dgCol title="创建时间" formatter="yyyy-MM-dd HH:mm:ss" query="true" width="15" field="createTime"></t:dgCol>
			<t:dgCol title="结束时间" formatter="yyyy-MM-dd HH:mm:ss" query="true" width="15" field="endTime"></t:dgCol>
			<t:dgCol title="持续时间" width="15" field="duration"></t:dgCol>
			<t:dgCol title="启用状态" replace="完成_2,运行中_1,禁用_0" width="15" field="status"></t:dgCol>

			<t:dgCol title="操作" width="15" field="opt"></t:dgCol>
			<t:dgOpenOpt title="详细信息" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processInstanceEdit&id={actInstId}" exParams="{optFlag:'detail'}"
				preinstallWidth="1" height="400" />
			<t:dgOpenOpt title="流程图" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processImage&actInstId={actInstId}" width="840" height="500" />
			<t:dgOpenOpt title="审批历史" icon="awsm-icon-zoom-in green"
				url="definitionController.do?showTaskOpinions&actInstId={actInstId}" width="700" height="500" />
			<t:dgFunOpt title="撤销" icon="awsm-icon-trash red" funname="recoverTask(actInstId)" />
			<t:dgDelOpt title="删除" icon="awsm-icon-trash red" url="processInstanceController.do?delete&id={id}" />
			<t:dgToolBar title="批量删除" icon="awsm-icon-remove" url="processInstanceController.do?batchDelete"
				funname="deleteALLSelect"></t:dgToolBar>

		</t:datagrid>
	</div>
</div>
