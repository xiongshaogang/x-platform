<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:datagrid name="userLogList"   checkbox="true" fitColumns="false" title="用户日志" actionUrl="userLogController.do?datagrid" idField="id" fit="true"  queryMode="separate"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="登录名"  field="userName"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="时间"  field="time"   width="150" align="center" formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
   <t:dgCol title="日志内容"  field="content"   width="400" align="center" ></t:dgCol>
    <t:dgCol title="ip地址"  field="ip"   width="100" align="center" ></t:dgCol>
   <t:dgCol title="状态"  field="status"   width="100"  replace="登录_0,退出_1" align="center"  query="true"  queryInputType="combobox" data='[{"id":"0","text":"登录"},{"id":"1","text":"退出"}]'></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="userLogController.do?delete&id={id}" callback="reflash" operationCode="userLogManager_userLogDelete_delete"/>
   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="userLogController.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash" operationCode="userLogManager_userLogBatchDelete_batchDelete"></t:dgToolBar>
  </t:datagrid>
 <script type="text/javascript">
 /* function reflash(){
	  $("#userLogList").datagrid("reload");
  } */
   $(function(){
	 redrawEasyUI($("#page_content"));
 });
 </script>