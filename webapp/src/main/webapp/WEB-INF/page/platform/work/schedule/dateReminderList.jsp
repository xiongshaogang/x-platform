<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px;">
  <t:datagrid name="dateReminderList"   checkbox="true" fitColumns="true" title="日程提醒" actionUrl="scheduleController.do?dateReminderDatagrid" idField="id" fit="true"  queryMode="group"  pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="tid"  field="tid" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="主题"  field="title" width="120"></t:dgCol>
   <t:dgCol title="开始时间"  field="startDate" width="120" formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
   <t:dgCol title="结束时间"  field="endDate" width="120" formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
   <t:dgCol title="内容"  field="context" width="120"></t:dgCol>
   <t:dgCol title="接收时间"  field="createTime" formatter="yyyy-MM-dd HH:mm:ss" width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgOpenOpt url="scheduleController.do?scheduleEdit&id={tid}" icon="awsm-icon-zoom-in green" exParams="{optFlag:'detail'}" width="400" height="420" title="查看"></t:dgOpenOpt>
  </t:datagrid>
 </div>
  </div>
 <script type="text/javascript">
 function reflash(){
	  $("#dateReminderList").datagrid("reload");
  }
 </script>