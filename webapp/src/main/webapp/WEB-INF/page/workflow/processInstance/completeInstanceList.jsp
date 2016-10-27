<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>



<div class="easyui-layout" fit="true" style="width:100%;height:100%;border:0px">
  <div region="center" border="false" style="padding:5px;border:0px;">
		<t:datagrid name="completeInstanceList" title="流程实例管理" checkbox="true" fitColumns="true"
			actionUrl="processInstanceController.do?completeInstanceDatagrid" idField="actInstId" fit="true" queryMode="group">
			<t:dgCol title="流程实例id" field="actInstId" hidden="false"></t:dgCol>
			<t:dgCol title="实例名称" query="true" width="35" field="title"></t:dgCol>
			<t:dgCol title="所属流程" query="true" width="20" field="defName"></t:dgCol>
			<t:dgCol title="创建时间" formatter="yyyy-MM-dd HH:mm:ss" query="true" width="15" field="createTime"></t:dgCol>
			<t:dgCol title="结束时间" formatter="yyyy-MM-dd HH:mm:ss" query="true" width="15" field="endTime"></t:dgCol>
			<t:dgCol title="持续时间" width="15" field="duration"></t:dgCol>
			<t:dgCol title="启用状态" replace="完成_2,运行中_1,禁用_0" width="15" field="status"></t:dgCol>

			<t:dgCol title="操作" width="15" field="opt"></t:dgCol>
			<t:dgFunOpt title="转发" icon="awsm-icon-zoom-in green" funname="divertTask(actInstId)" />
			<t:dgOpenOpt title="详细信息" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processInstanceEdit&id={actInstId}" exParams="{optFlag:'detail'}"
				preinstallWidth="1" height="400" />
			<t:dgOpenOpt title="流程图" icon="awsm-icon-zoom-in green"
				url="processInstanceController.do?processImage&actInstId={actInstId}" width="840" height="500" />
			<t:dgOpenOpt title="审批历史" icon="awsm-icon-zoom-in green"
				url="definitionController.do?showTaskOpinions&actInstId={actInstId}" width="700" height="500" />
			<t:dgOpenOpt title="抄送人" icon="awsm-icon-zoom-in green"
				url="processInsCptoController.do?showCptoList&id={actInstId}&type=actInstId" width="700" height="300" />
		</t:datagrid>
	</div>
</div>
