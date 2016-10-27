<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
$(function(){
	//removeButton(getD($("#trainFormobj")),1);
	var needReply = "${te.needReply}";
	var replyContent = "${mr.replyContent}";
	if(needReply == '1' && replyContent == ''){
		var myButton = [{
				id : 'reply',
				text : '回复',
				iconCls : 'awsm-icon-reply',
				operationCode : "messageManager_testConnect_other",
				handler : function() {
					if($("#content").val().trim() == ''){
						$.messager.alert("提示信息", "回复信息不能为空");
						return false;
					}
					$("#btn_type").val("reply");
					saveObj("btn_sub","trainFormobj");
				}
	    },{
		id : 'transpond',
		text : '转发',
		iconCls : 'awsm-icon-plus',
		operationCode : "messageManager_testConnect_other",
		handler : function() {
			if($("#empIds").val() == '' && $("#jobIds").val() == '' && $("#orgIds").val() == ''){
				$.messager.alert("提示信息", "转发至员工、岗位、机构至少填写一个");
				return false;
			}
			var str="";
			$("input[name='sendType']:checked").each(function(){
				str +=$(this).val();
			});
			if(str == ''){
				$.messager.alert("提示信息", "转发方式不能为空");
				return false;
			}
			if($("#email").is(":checked") && $("#mailConfigId").val() == ''){
				$.messager.alert("提示信息", "发送邮件账号不能为空");
				return false;
            }
			$("#btn_type").val("transpond");
			saveObj("btn_sub","trainFormobj");
		}
	}];
		addButton(getD($("#trainFormobj")),myButton,1);
	}else{
		var myButton = [{
		id : 'transpond',
		text : '转发',
		iconCls : 'awsm-icon-plus',
		operationCode : "messageManager_testConnect_other",
		handler : function() {
			if($("#empIds").val() == '' && $("#jobIds").val() == '' && $("#orgIds").val() == ''){
				$.messager.alert("提示信息", "转发至员工、岗位、机构至少填写一个");
				return false;
			}
			var str="";
			$("input[name='sendType']:checked").each(function(){
				str +=$(this).val();
			});
			if(str == ''){
				$.messager.alert("提示信息", "转发方式不能为空");
				return false;
			}
			if($("#email").is(":checked") && $("#mailConfigId").val() == ''){
				$.messager.alert("提示信息", "发送邮件账号不能为空");
				return false;
            }
			$("#btn_type").val("transpond");
			saveObj("btn_sub","trainFormobj");
		}
	}];
	addButton(getD($("#trainFormobj")),myButton,1);
	}
});
function setType(){
	if($("#email").is(":checked")){
		$(".email").show();
	}else{
		$(".email").hide();
		$("#mailConfigId").combobox("setValue","");
	}
}
function showOffice(){
	if (typeof(window.frames['office_iframe'].contentWindow) == "undefined") { 
		window.frames["office_iframe"].showAll();
	}else{
		window.frames["office_iframe"].contentWindow.showAll();
	}  
}

</script>
  <t:formvalid formid="trainFormobj" gridId="trainInformList" action="trainController.do?saveReply" refresh="true">
		<input id="id" name="id" type="hidden" value="${te.id }">
		<input id="receiveId" name="receiveId" type="hidden" value="${mr.id }">
		<input id="btn_type" name="btn_type" type="hidden" value="">
		<table cellpadding="0" cellspacing="1" class="formtable" style="margin-right: 100px;">
		    <tr >
			<td class="td_title">&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showOffice()" style="color:blue;">查看正文</a>&nbsp;&nbsp;&nbsp;
			<iframe src="messageController.do?printFile&id=${te.id }&name=trainTemplate.doc"  style="width: 0;height: 0;padding:0px;border:0px;" id="office_iframe" name="office_iframe"></iframe>
			</td>
			</tr>
		   <tr>
		    <td class="td_title"><label class="Validform_label">转发至员工:</label></td>
			<td class="value"><t:empSelect hiddenName="empIds"  displayName="empNames" ></t:empSelect></td>
			<td class="td_title"><label class="Validform_label">转发至岗位:</label></td>
			<td class="value"><t:jobSelect hiddenName="jobIds" displayName="jobNames" ></t:jobSelect></td>
		   </tr>
		   <tr>
			<td class="td_title"><label class="Validform_label">转发至机构:</label></td>
			<td class="value"><t:orgSelect id="orgIds" name="orgIds" value="" multiple="true" onlyLeafCheck="true" ></t:orgSelect></td>
			<td class="td_title"><label class="Validform_label"><span style="color:red">*</span>转发方式:</label></td>
			<td class="value">
			<input id="innerMessage" type="checkbox" name="sendType" value="innerMessage" >&nbsp;站内信
			<input id="sms" type="checkbox" name="sendType" value="sms">&nbsp;短信
			<input id="email" type="checkbox" name="sendType" value="email" onclick="setType()">&nbsp;邮件
			</td>
		   </tr>
		   <tr>
		   <td class="td_title email" style="display: none;"><label class="Validform_label"><span style="color:red">*</span>发送邮件账号:</label></td>
		   <td class="value email" style="display: none;"><t:comboBox name="mailConfigId"  id="mailConfigId"   url="messageController.do?getEmail" value="" ></t:comboBox></td>
		   </tr>
			<c:if test="${te.needReply =='1'}">
			<tr>
				<td class="td_title"><span class="Validform_label">回复内容：</span></td>
				<td colspan="3" class="value">
					<textarea id="content" name="content" style="width:510px;height:100px;border: 1px solid #D5D5D5;"  <c:if test="${not empty mr.replyContent}">disabled="disabled"</c:if> >${mr.replyContent }</textarea>
				</td>
			</tr>
			</c:if>
		</table>
	</t:formvalid>
