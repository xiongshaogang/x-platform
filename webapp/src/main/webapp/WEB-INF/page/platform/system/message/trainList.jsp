<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center"  style="padding:0px;border:0px;">
  <t:datagrid name="trainList"   checkbox="true" fitColumns="true" title="会议通知" actionUrl="messageController.do?msgDatagrid&type=train" idField="id" fit="true"  queryMode="group"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="发自"  field="name"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="主题"  field="title"   width="200"  ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <%-- <t:dgOpenOpt preinstallWidth="2" height="450" icon="awsm-icon-edit blue" url="messageController.do?msgEdit&id={id}&type=train" exParams="{optFlag:'add'}" title="编辑" ></t:dgOpenOpt> --%>
   <t:dgOpenOpt width="1000" height="460"  icon="awsm-icon-zoom-in green" url="messageController.do?msgEdit&id={id}&type=train" exParams="{optFlag:'detail'}" title="查看" ></t:dgOpenOpt>
   <t:dgDelOpt title="删除" url="messageController.do?deleteMailCofig&id={id}" callback="reflash" />
   <t:dgToolBar title="新增" icon="awsm-icon-plus" url="messageController.do?msgEdit&type=train" funname="add" buttonType="GridAdd"  width="1000" height="460" ></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="messageController.do?batchDeleteMailCofig" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash" ></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 function reflash(){
	  $("#trainList").datagrid("reload");
  }
   $(function(){
	 redrawEasyUI($(".easyui-layout"));
 });
 </script>