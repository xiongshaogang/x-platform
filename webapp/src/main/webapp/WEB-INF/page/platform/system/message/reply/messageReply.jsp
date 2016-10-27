<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.formtable {
	width: 100%; 
	/* background-color: #B8CCE2; */
	margin:5px auto;
}

.formtable .td_title{
	width:20%;
	height:30px;
	text-align:right;
	/*padding-left:5px;*/
}

.formtable .value {
	/*background-color: #FFFFFF;*/
	height:30px;
	padding:4px;
}
</style>
<script type="text/javascript">
$(function(){
	removeButton(getD($("#formobj")),1);
	var need_reply ='${param.need_reply}';
	if(need_reply == '1'){
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
					saveObj();
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
			if($("#sendType").val() == ''){
				$.messager.alert("提示信息", "转发方式不能为空");
				return false;
			}
			if($("#sendType").val().indexOf('email') != -1 && $("#mailConfigId").val() == ''){
				$.messager.alert("提示信息", "邮件发送人不能为空");
				return false;
            }
			$("#btn_type").val("transpond");
			saveObj();
		}
	},{
		id : 'print',
		text : '打印',
		iconCls : 'awsm-icon-reply',
		disabled :true,
		operationCode : "messageManager_testConnect_other",
		handler : function() {
			winPrint('${param.id}');
		},
	}];
		addButton(getD($("#formobj")),myButton,1);
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
			if($("#sendType").val() == ''){
				$.messager.alert("提示信息", "转发方式不能为空");
				return false;
			}
			if($("#sendType").val().indexOf('email') != -1 && $("#mailConfigId").val() == ''){
				$.messager.alert("提示信息", "邮件发送人不能为空");
				return false;
            }
			$("#btn_type").val("transpond");
			saveObj();
		}
	},{
		id : 'print',
		text : '打印',
		iconCls : 'awsm-icon-reply',
		disabled :true,
		operationCode : "messageManager_testConnect_other",
		handler : function() {
			winPrint('${param.id}');
		},
	}];
	addButton(getD($("#formobj")),myButton,1);
	}
});

function setTypeReply(self){
	var val = $(self).val();
	if(val.indexOf("email") != -1){
		$(".email").show();
	}else{
		$(".email").hide();
		$("#mailConfigId").combobox("setValue","");
	}
}
</script>
<div class="easyui-layout" fit="true" style="border: 0;">
  <div region="center"  style="padding:0px;border: 0;height:330px;" href="messageController.do?innerMsgView&id=${param.id }&type=view" border="false" split="true">
  </div>
  <div region="south" split="true" style="padding:0px;border: 0;height:220px;">
  <t:formvalid formid="formobj" gridId="innerList" action="messageController.do?saveReply" refresh="true">
		<input id="id" name="id" type="hidden" value="${msgTo.id }">
		<input id="btn_type" name="btn_type" type="hidden" value="">
		<table cellpadding="0" cellspacing="1" class="formtable">
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
			<td class="value"><t:comboBox name="sendType"  multiple="true" id="sendType" data='[{"id":"innerMessage","text":"站内信"},{"id":"sms","text":"短信"},{"id":"email","text":"邮件"}]' value="" onChange="setTypeReply(self)" ></t:comboBox></td>
		   </tr>
		   <tr>
		   <td class="td_title email" style="display: none;"><label class="Validform_label"><span style="color:red">*</span>邮件发件人:</label></td>
		   <td class="value email" style="display: none;"><t:comboBox name="mailConfigId"  id="mailConfigId"   url="messageController.do?getEmail" value="" ></t:comboBox></td>
		   </tr>
			<c:if test="${param.need_reply =='1' || param.need_reply =='2'}">
			<tr>
				<td class="td_title"><span class="Validform_label">回复内容：</span></td>
				<td colspan="3" class="value">
					<textarea id="content" name="content" style="width:540px;height:100px;border: 1px solid #D5D5D5;"  <c:if test="${param.need_reply =='2' }">disabled="disabled"</c:if> >${msgTo.replyContent }</textarea>
				</td>
			</tr>
			</c:if>
		</table>
	</t:formvalid>
  </div>  
</div>
