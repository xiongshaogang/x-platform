<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
	  <t:datagrid name="triggerList" pagination="false" checkbox="true" fitColumns="true" actionUrl="triggerController.do?datagrid&jobName=${jobName}" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务名称"  field="jobName" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="计划名称"  field="trigName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="计划描述"  field="description"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="状态" replace="启用_NORMAL,禁用_PAUSED,执行出错_ERROR,未启动_NONE,完成_COMPLETE,正在执行_BLOCKED" field="status"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	   
	   <t:dgDelOpt title="删除" icon="awsm-icon-trash red" url="triggerController.do?delete&name={trigName}" />
	   <t:dgFunOpt exp="status#eq#PAUSED" title="启用" icon="awsm-icon-eye-open green" funname="doActive(trigName)" />
	   <t:dgFunOpt exp="status#eq#NORMAL" title="冻结" icon="awsm-icon-eye-close grey" funname="doActive(trigName)" />
	   <t:dgOpenOpt title="任务计划日志" icon="awsm-icon-list blue" url="timerLogController.do?timerLog&triggerName={trigName}" width="1000" height="500"   />
	  
	   <t:dgToolBar title="添加" preinstallWidth="2" height="500" icon="awsm-icon-plus" url="triggerController.do?triggerEdit" funname="add"></t:dgToolBar>
	  </t:datagrid>
  </div>
</div>
<input type="hidden" id="pjobName" value="${jobName}">
<script type="text/javascript">
	function doActive(trigName){
		doSubmit("triggerController.do?doActive&name="+trigName, "triggerList");
	}
</script>
