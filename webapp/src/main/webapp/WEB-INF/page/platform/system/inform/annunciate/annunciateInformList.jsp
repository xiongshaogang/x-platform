<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true" id="abcdefg">
  <div region="center"   style="padding:0px;border:0px;">
  <t:datagrid name="annunciateInformList"   checkbox="false" fitColumns="true" title="内部通告提醒列表" actionUrl="annunciateController.do?informDatagrid" idField="id" fit="true"  queryMode="group"    pagination="true">
   <t:dgCol title="id"  field="id" hidden="false"  width="80"  ></t:dgCol>
   <t:dgCol title="sid"  field="sid" hidden="false"  width="80"  ></t:dgCol>
   <t:dgCol title="mid"  field="mid" hidden="false"  width="80"  ></t:dgCol>
   <t:dgCol title="发自"  field="name"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="主题"  field="title"   width="200" align="left" ></t:dgCol>
   <t:dgCol title="接收时间"  field="createTime"   width="200" align="left" formatter="yyyy-MM-dd HH:mm:ss"></t:dgCol>
   <t:dgCol title="是否需要回复"  field="needReply"  width="80" replace="是_1,否_0"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100" ></t:dgCol>
   <t:dgOpenOpt width="1000" height="460"  icon="awsm-icon-zoom-in green" url="messageController.do?printFile&id={mid}&name=annunciateTemplate.doc" exParams="{optFlag:'detail',isIframe:true,dialogID:'officePrintDialog'}" title="查看" ></t:dgOpenOpt>
   <t:dgFunOpt  funname="replyAnnunciate(id,mid)" title="操作" icon="awsm-icon-reply green" ></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="messageController.do?deleteReceive&id={id}" callback="ireflashAnnunciate" />
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 
 function ireflashAnnunciate(){
	  $("#annunciateInformList").datagrid("reload");
  }
 
 function replyAnnunciate(id,mid){
	 createwindow("操作","annunciateController.do?annunciateReply&id="+id+"&mid="+mid , 800, 550, null, {optFlag:'close',formId:'annunciateFormobj'});
 }
 </script>