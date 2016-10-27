<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
	  <t:datagrid name="timerLogList" sortName="createTime" sortOrder="desc" pageSize="1000"  
 extendParams="view:scrollview," checkbox="true" fitColumns="true" actionUrl="timerLogController.do?datagrid&jobName=${jobName}&triggerName=${triggerName}" idField="id" fit="true">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="定时任务名称"  field="jobName" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务计划名称"  field="triggerName" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="日志内容"  field="content"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="开始时间"  field="createTime" hidden="true"  queryMode="single"  width="140"></t:dgCol>
	   <t:dgCol title="结束时间"  field="endTime"  hidden="true"  queryMode="single"  width="140"></t:dgCol>
	   <t:dgCol title="持续时间"  field="runTime"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
	   <t:dgCol title="操作" field="opt" width="60"></t:dgCol>
	  
	   <t:dgDelOpt  title="删除" icon="awsm-icon-trash red" url="timerLogController.do?delete&id={id}" />
	   
	   <t:dgToolBar title="批量删除" icon="awsm-icon-remove" url="timerLogController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
	  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
	 redrawEasyUI($(".easyui-layout"));
 		//给时间要素加上样式
 });
 </script>