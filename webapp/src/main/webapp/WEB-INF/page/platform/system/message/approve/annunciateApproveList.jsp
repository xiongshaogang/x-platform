<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true" id="abcdefg">
  <div region="center"   style="padding:0px;border:0px;">
  <t:datagrid name="annunciateApproveList"   checkbox="true" fitColumns="true" title="内部通告审核列表" actionUrl="messageController.do?annunciateApproveDatagrid&roleCode=${roleCode }" idField="id" fit="true"  queryMode="group"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="needReply"  field="needReply" hidden="false" ></t:dgCol>
   <t:dgCol title="发自"  field="name"   width="100"  ></t:dgCol>
   <t:dgCol title="主题"  field="title"   width="200"  ></t:dgCol>
   <t:dgCol title="发送时间"  field="create_time"   width="200" formatter="yyyy-MM-dd HH:mm:ss" ></t:dgCol>
   <c:if test="${roleCode =='pm' }">
   <t:dgCol title="审核状态"  field="pmStatus"   width="200" replace="未审核_3,审核通过_1,审核不通过_0" ></t:dgCol>
    <t:dgCol title="审核主管"  field="pmName"   width="200" ></t:dgCol>
   </c:if>
   <c:if test="${roleCode =='gm' }">
   <t:dgCol title="审核状态"  field="gmStatus"   width="200" replace="未审核_3,审核通过_1,审核不通过_0" ></t:dgCol>
    <t:dgCol title="审核总经理"  field="gmName"   width="200" ></t:dgCol>
   </c:if>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <%-- <t:dgOpenOpt width="800" height="550"  icon="awsm-icon-zoom-in green" url="messageController.do?innerMsgView&id={id}&type=approve" exParams="{optFlag:'detail'}" title="查看" ></t:dgOpenOpt> --%>
   <c:if test="${roleCode =='pm' }">
   <t:dgFunOpt exp="pmStatus#eq#3" funname="arrpove(id)" title="审核" icon="awsm-icon-reply green" ></t:dgFunOpt>
   <t:dgFunOpt exp="pmStatus#ne#3" funname="approveView(id)" title="查看审核结果" icon="awsm-icon-zoom-in green" ></t:dgFunOpt>
   </c:if>
   <c:if test="${roleCode =='gm' }">
   <t:dgFunOpt exp="gmStatus#eq#3" funname="arrpove(id)" title="审核" icon="awsm-icon-reply green" ></t:dgFunOpt>
   <t:dgFunOpt exp="gmStatus#ne#3" funname="approveView(id)" title="查看审核结果" icon="awsm-icon-zoom-in green" ></t:dgFunOpt>
   </c:if>
   <t:dgFunOpt funname="downloadPdf(id,@@annApprove@@)" title="下载审批表pdf" icon="awsm-icon-download green"></t:dgFunOpt>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 function reflash(){
	  $("#annunciateApproveList").datagrid("reload");
  }
 function arrpove(id){
	 createwindow("内部通告审核","messageController.do?annApprove&id="+id+"&roleCode="+'${roleCode}'+"&type=approve" , 800, 550, null, {optFlag:'add'});
 }
 
 function approveView(id){
	 createwindow("内部通告审核查看","messageController.do?annApprove&id="+id+"&roleCode="+'${roleCode}'+"&type=view" , 800, 550, null, {optFlag:'detail'});
 }
/*  function downloadApprovePdf(id){
	   location.href = "pdfController.do?genPdf&type=annApprove&id="+id;
 } */
 </script>