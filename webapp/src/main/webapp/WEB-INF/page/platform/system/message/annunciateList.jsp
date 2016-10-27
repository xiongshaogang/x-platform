<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center"  style="padding:0px;border:0px;">
   <c:if test="${param.type =='meeting' }">
   <c:set var="title">会议通知</c:set>
   </c:if>
   <c:if test="${param.type =='annunciate' }">
   <c:set var="title">内部通告</c:set>
   </c:if>
   <c:if test="${param.type =='train' }">
   <c:set var="title">培训通知</c:set>
   </c:if>
  
  <t:datagrid name="${param.type }List"   checkbox="true" fitColumns="true" title="${title }" actionUrl="messageController.do?msgDatagrid&type=${param.type }" idField="id" fit="true"  queryMode="group"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="needReply"  field="needReply" hidden="false" ></t:dgCol>
    <t:dgCol title="status"  field="status" hidden="false" ></t:dgCol>
   <t:dgCol title="发自"  field="name"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="主题"  field="title"   width="100"  ></t:dgCol>
   <t:dgCol title="发送时间"  field="createTime"   width="100"  formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
   <t:dgCol title="是否需要主管审核"  field="pmApprove"  width="100"  replace="是_1,否_0"></t:dgCol>
   <t:dgCol title="主管领导审核状态"  field="pmStatus"  width="100"  replace="审核通过_1,审核不通过_0"></t:dgCol>
   <t:dgCol title="总经理审核状态"  field="gmStatus"   width="100"  replace="审核通过_1,审核不通过_0"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="120"></t:dgCol>
   <%-- 退回编辑 --%>
   <t:dgOpenOpt width="1000" height="460" title="编辑" icon="awsm-icon-edit blue" url="messageController.do?msgEdit&id={id}&type=${param.type }&optFlag=return" exParams="{optFlag:'send'}" exp="pmStatus#eq#0"></t:dgOpenOpt>
   <t:dgOpenOpt width="1000" height="460" title="编辑" icon="awsm-icon-edit blue" url="messageController.do?msgEdit&id={id}&type=${param.type }&optFlag=return" exParams="{optFlag:'send'}" exp="gmStatus#eq#0"></t:dgOpenOpt>
   <%-- 保存编辑 --%>
   <t:dgOpenOpt width="1000" height="460" title="编辑" icon="awsm-icon-edit blue" url="messageController.do?msgEdit&id={id}&type=${param.type }&optFlag=update" exParams="{optFlag:'send'}" exp="status#eq#0"></t:dgOpenOpt>
   <t:dgOpenOpt width="1000" height="460"  icon="awsm-icon-zoom-in green" url="messageController.do?msgEdit&id={id}&type=${param.type }&optFlag=detail" exParams="{optFlag:'detail'}" title="查看" ></t:dgOpenOpt>
   <t:dgFunOpt exp="needReply#eq#1&&status#ne#0" funname="replyView(id)" title="查看回复" icon="awsm-icon-reply green" ></t:dgFunOpt>
   <t:dgFunOpt exp="status#ne#0" funname="approveView(id)" title="查看审核结果" icon="awsm-icon-comment green"></t:dgFunOpt>
   <%-- <t:dgFunOpt funname="winPrint(id)" title="打印" icon="awsm-icon-print green"></t:dgFunOpt> --%>
   <t:dgFunOpt funname="pageShowPdf(id,@@${param.type}@@)" title="下载pdf" icon="awsm-icon-download green"></t:dgFunOpt>
  <%--  <t:dgFunOpt exp="pmStatus#empty#false" funname="approvePmView(id)" title="查看主管领导审核结果" icon="awsm-icon-zoom-in green" ></t:dgFunOpt>
   <t:dgFunOpt exp="gmStatus#empty#false" funname="approveGmView(id)" title="查看总经理审核结果" icon="awsm-icon-zoom-in green" ></t:dgFunOpt> --%>
   <t:dgDelOpt title="删除" url="messageController.do?delete&id={id}" callback="reflash" />
   <t:dgToolBar title="新增" icon="awsm-icon-plus" url="messageController.do?msgEdit&type=${param.type }&optFlag=add" funname="add" buttonType="GridAdd"  width="1000" height="460"  ></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 function reflash(){
	  $("#"+${param.type }+"List").datagrid("reload");
  }
   $(function(){
	 redrawEasyUI($(".easyui-layout"));
 });
   function replyView(id){
	   createwindow("查看回复列表","messageController.do?replyView&id="+id , 600, 400, null, {optFlag:'detail'});
   }
   
   function approveView(id){
	   createwindow("查看审核结果","messageController.do?approveView&id="+id , 600, 400, null, {optFlag:'detail'});
   }
   
   function approvePmView(id){
		 createwindow("主管领导审核结果查看","messageController.do?annApprove&id="+id+"&roleCode=pm" , 600, 400, null, {optFlag:'detail'});
	 }
   function approveGmView(id){
		 createwindow("总经理审核结果查看","messageController.do?annApprove&id="+id+"&roleCode=gm" , 600, 400, null, {optFlag:'detail'});
	 }
 </script>