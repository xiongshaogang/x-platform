<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center"  style="padding:0px;border:0px;">
  <t:datagrid name="meetingList"   checkbox="false" fitColumns="true" title="会议通知" actionUrl="meetingController.do?datagrid" idField="id" fit="true"  queryMode="group"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
    <t:dgCol title="status"  field="status" hidden="false" ></t:dgCol>
    <t:dgCol title="发自"  field="name"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="needReply"  field="needReply" hidden="false" ></t:dgCol>
   <t:dgCol title="主题"  field="title"   width="200"  ></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime"   width="200"  formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <%-- 保存编辑 --%>
   <t:dgOpenOpt width="1000" height="460" title="编辑" icon="awsm-icon-edit blue" url="meetingController.do?meetingEdit&id={id}" exParams="{optFlag:'send'}" exp="status#eq#0"></t:dgOpenOpt>
   <t:dgOpenOpt width="1000" height="460"  icon="awsm-icon-zoom-in green" url="meetingController.do?meetingEdit&id={id}&optFlag=detail" exParams="{optFlag:'detail'}" title="查看" ></t:dgOpenOpt>
   <t:dgFunOpt exp="needReply#eq#1" funname="replyView(id)" title="查看回复" icon="awsm-icon-reply green"></t:dgFunOpt>
   <t:dgFunOpt funname="printFile(id,@@meetingTemplate.doc@@)" title="打印" icon="awsm-icon-print green"></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="meetingController.do?delete&id={id}" callback="reflashMeeting" />
   <t:dgToolBar title="新增" icon="awsm-icon-plus" url="meetingController.do?meetingEdit" funname="send" buttonType="GridAdd"  width="1000" height="460" ></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 function reflashMeeting(){
	  $("#meetingList").datagrid("reload");
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
 </script>