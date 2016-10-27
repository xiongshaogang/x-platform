<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
	  <t:datagrid pagination="false" name="timerList" checkbox="true" fitColumns="true" title="定时器任务列表" actionUrl="timerController.do?datagrid" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务名称"  field="name" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务类"  field="className" hidden="true"  queryMode="single"  width="150"></t:dgCol>
	   <t:dgCol title="描述"  field="description"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="操作" field="opt" width="120"></t:dgCol>
	  
	   <t:dgDelOpt  title="删除" icon="glyphicon glyphicon-remove icon-color" url="timerController.do?delete&jobName={name}" />
	   <t:dgDelOpt  title="执行" icon="glyphicon glyphicon-pencil icon-color" url="timerController.do?executeJob&jobName={name}" />
	   <t:dgOpenOpt title="定时器任务日志" icon="awsm-icon-list blue" url="timerLogController.do?timerLog&jobName={name}" width="1000" height="500"   />
	   <t:dgOpenOpt title="任务计划" icon="glyphicon glyphicon-pencil icon-color" url="triggerController.do?trigger&jobName={name}" width="1000" height="500"   />
	   
	   
	  
	   <t:dgToolBar title="添加" preinstallWidth="1" height="430" icon="awsm-icon-plus" url="timerController.do?timerEdit" funname="add"></t:dgToolBar>
	  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
	 redrawEasyUI($(".easyui-layout"));
 		//给时间要素加上样式
 });
 </script>