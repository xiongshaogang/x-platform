<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center"  style="padding:0px;border:0px;">
  <t:datagrid name="annunciateList"  checkbox="false" fitColumns="true" title="内部通告" actionUrl="annunciateController.do?datagrid" idField="id" fit="true"  queryMode="group"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="needReply"  field="needReply" hidden="false" ></t:dgCol>
   <%-- <t:dgCol title="status"  field="status" hidden="false" ></t:dgCol> --%>
   <t:dgCol title="发自"  field="name"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="主题"  field="title"   width="100"  ></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime"   width="100"  formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
   <t:dgCol title="是否需要主管审核"  field="pmApprove"  width="100"  replace="是_1,否_0"></t:dgCol>
   <t:dgCol title="状态"  field="status"  width="100" replace="保存_0,审核中_1,已审核_3"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="120"></t:dgCol>
   <%-- 保存编辑 --%>
   <t:dgOpenOpt width="1000" height="400" title="编辑" icon="awsm-icon-edit blue" url="annunciateController.do?annunciateEdit&id={id}&optFlag=update" exParams="{optFlag:'send'}" exp="status#eq#0"></t:dgOpenOpt>
   <t:dgOpenOpt width="1000" height="400"  icon="awsm-icon-zoom-in green" url="annunciateController.do?annunciateEdit&id={id}&optFlag=detail" exParams="{optFlag:'detail'}" title="查看" ></t:dgOpenOpt>
   <t:dgFunOpt exp="needReply#eq#1" funname="replyView(id)" title="查看回复" icon="awsm-icon-reply green"></t:dgFunOpt>
   <t:dgFunOpt funname="printFile(id,@@annunciateTemplate.doc@@)" title="打印" icon="awsm-icon-print green"></t:dgFunOpt>
   <t:dgFunOpt funname="printApprove(id)" title="审批表" icon="awsm-icon-download green" exp="status#eq#3"></t:dgFunOpt>
   <%-- <t:dgFunOpt funname="downloadPdf(id,@@annApprove@@)" title="审批表pdf" icon="awsm-icon-download green" exp="status#eq#3"></t:dgFunOpt> --%>
   <t:dgDelOpt title="删除" url="annunciateController.do?delete&id={id}" callback="reflashAnnunciate" />
   <t:dgToolBar title="新增" icon="awsm-icon-plus" url="annunciateController.do?annunciateEdit" funname="send" buttonType="GridAdd"  width="1000" height="400" ></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
   function reflashAnnunciate(){
	  $("#annunciateList").datagrid("reload");
  }
   
   function replyView(id){
	   createwindow("查看回复列表","messageController.do?replyView2&id="+id , 600, 400, null, {optFlag:'detail'});
   }
   
   function send(title, addurl, gridID, width, height, preinstallWidth, exParams) {
		var defaultOptions = {
			optFlag : 'send'
		};
		var options = $.extend({}, defaultOptions, exParams);
		createwindow(title, addurl, width, height, preinstallWidth, options);
	}
   
 //office word查看、打印
   function printApprove(id){
   	createwindow("打印", "annunciateController.do?annApproveOffice&id="+id, 900, 550, null, {optFlag:'detail',isIframe:true,dialogID:'officePrintDialog'});
   }
 </script>