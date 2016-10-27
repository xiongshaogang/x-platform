<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true" id="abcdefg">
  <div region="center"   style="padding:0px;border:0px;">
  <t:datagrid name="innerList"   checkbox="true" fitColumns="true" title="站内信列表" actionUrl="messageController.do?innerDatagrid" idField="id" fit="true"  queryMode="group"    pagination="true" onLoadSuccess="aaa">
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="是否需要回复"  field="need_reply" hidden="false"   ></t:dgCol>
   <t:dgCol title="信息类型"  field="message_type"   width="100"  query="true" replace="会议通知_meeting,内部通告_annunciate,培训通知_train"  queryInputType="combobox" data='[{"id":"meeting","text":"会议通知"},{"id":"annunciate","text":"内部通告"},{"id":"train","text":"培训通知"}]'></t:dgCol>
   <t:dgCol title="发自"  field="name"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="主题"  field="title"   width="200" align="left" ></t:dgCol>
   <t:dgCol title="时间"  field="create_time"   width="200" align="left" ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100" ></t:dgCol>
   <t:dgOpenOpt width="800" height="550"  icon="awsm-icon-zoom-in green" url="messageController.do?innerMsgView&id={id}&type=view" exParams="{optFlag:'detail'}" title="查看" ></t:dgOpenOpt>
   <t:dgFunOpt  funname="reply1(id,message_type,need_reply)" title="操作" icon="awsm-icon-reply green" ></t:dgFunOpt>
  <%--  <t:dgFunOpt exp="need_reply#eq#2" funname="reply2(id,message_type)" title="查看回复" icon="awsm-icon-reply grey"></t:dgFunOpt> --%>
   <t:dgDelOpt title="删除" url="messageController.do?deleteMsgTo&id={id}" callback="reflash" />
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
    function aaa(){
    	$("#abcdefg").find("td[field='opt']").each(function(){
    		$(this).find("a").eq("0").click(function(){
        		setTimeout("loadInnerMsg()",2000);
        	});
    	});
    }
		 
 
 function reflash(){
	  $("#innerList").datagrid("reload");
  }
 
 function reply1(id,message_type,need_reply){
	 createwindow("操作","messageController.do?reply&type="+message_type+"&id="+id+"&need_reply="+need_reply , 800, 550, null, {optFlag:'add'});
	 setTimeout("loadInnerMsg()",2000);
 }
 function reply2(id,message_type){
	 createwindow("查看回复","messageController.do?reply&type="+message_type+"&id="+id , 800, 550, null, {optFlag:'detail'});
 }
 </script>