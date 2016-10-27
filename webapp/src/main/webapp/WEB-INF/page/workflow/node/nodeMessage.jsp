<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/ckeditor/ckeditor_sysTemp.js"></script>
<script type="text/javascript">
function close1(){
	closeD($("#formobj").closest(".window-body"));
}
</script>
<style>
	.formtable .td_title{
		width:20%;
		height:30px;
		text-align:right;
		/*padding-left:5px;*/
	}
	.formtable .value {
		/*background-color: #FFFFFF;*/
		width:80%;
		height:30px;
		padding:5px;
	}
	.formtable td {
		border:0;
	}
	td{
		height:40px;
	}
	#nodeMessageformobj .form_div {
    	margin: 0px auto;
    	width: 100%;
    }
	</style>
<t:formvalid formid="nodeMessageformobj" action="nodeMessageController.do?saveNodeMessage" tiptype="5" beforeSubmit="getValues">
        <input type="hidden" name="actDefId" value="${param.actDefId}" />
	    <input type="hidden" name="nodeId" value="${param.nodeId}" />
 				<t:tabs id="nodeMessageTab"  fit="false" hBorderBottom="true" leftDiv="true" leftDivWidth="90" leftDivTitle="消息参数" rightDiv="true" closeBtn="true">
     	         <div  title="邮件信息" fit="true" id="email">
 					<table cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td class="td_title"><label class="Validform_label"> 发送：</label></td>
						<td class="value" >
						<input type="checkbox" name="emailSend" value="1" <c:if test="${email.isSend == '1' }">checked="checked"</c:if> />
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 邮件主题：</label></td>
						<td class="value" >
						<input type="text" class="inputxt" name="emailSubject" value="${email.subject }"  />
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 邮件接收人：</label></td>
						<td class="value" >
						<input type="text" class="inputxt" name="emailRecipient" value="" />
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 普通抄送：</label></td>
						<td class="value" >
						<input type="text" class="inputxt" name="commonCarbonCopy" value="" />
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 秘密抄送：</label></td>
						<td class="value" >
						<input type="text" class="inputxt" name="secretCarbonCopy" value="" />
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 选择邮件任务内容：</label></td>
						<td class="value" >
						<t:comboBox url="nodeMessageController.do?getTemplate"  id="email" name="email" onSelect="chooseTemplate(this.value,this.id)" textField="name" valueField="id"></t:comboBox>
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 邮件任务：</label></td>
						<td class="value" >
						<textarea id="emailTemplate" name="emailTemplate"  class="input_area" style="height:60px">${email.template }</textarea>
						</td>
					</tr>
					
					</table>
 					</div> 
 					 <div  title="消息信息" fit="true" id="message">
 					<table cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td class="td_title"><label class="Validform_label"> 发送：</label></td>
						<td class="value" >
						<input type="checkbox" name="messageSend" value="1" <c:if test="${message.isSend == '1' }">checked="checked"</c:if>/>
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label">消息主题：</label></td>
						<td class="value" >
						<input type="text" class="inputxt" name="messageSubject" value="${message.subject }" />
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 消息接收人：</label></td>
						<td class="value" >
						<input type="text" class="inputxt" name="messageRecipient" value="" />
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 选择消息任务内容：</label></td>
						<td class="value" >
						<t:comboBox url="nodeMessageController.do?getTemplate"  id="msg" name="msg" onSelect="chooseTemplate(this.value,this.id)" textField="name" valueField="id"></t:comboBox>
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 消息任务：</label></td>
						<td class="value" >
						<textarea id="messageTemplate" name="messageTemplate"  class="input_area" style="height:60px">${message.template }</textarea>
						</td>
					</tr>
					</table>
 					</div> 
 					<div  title="短信信息" fit="true" id="sms">
 					<table cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td class="td_title"><label class="Validform_label"> 发送：</label></td>
						<td class="value" >
						<input type="checkbox" name="smsSend" value="1" <c:if test="${sms.isSend == '1' }">checked="checked"</c:if>/>
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 短信接收人：</label></td>
						<td class="value" >
						<input type="text" class="inputxt" name="smsRecipient" value="" />
						</td>
					</tr>
					<tr>
						<td class="td_title" nowrap><label class="Validform_label"> 选择短信任务内容：</label></td>
						<td class="value" >
						<t:comboBox url="nodeMessageController.do?getTemplate"  id="sms" name="sms" onSelect="chooseTemplate(this.value,this.id)" textField="name" valueField="id"></t:comboBox>
						</td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 短信任务：</label></td>
						<td class="value" >
						<textarea name="smsTemplate" id="smsTemplate"  cols="55" rows="8">${sms.template }</textarea>
						</td>
					</tr>
					</table> 
 					</div> 
 					</t:tabs>
		</t:formvalid>
		
		<script type="text/javascript">
			if( CKEDITOR.instances['emailTemplate'] ){
			    CKEDITOR.remove(CKEDITOR.instances['emailTemplate']);
			}
			if( CKEDITOR.instances['messageTemplate'] ){
			    CKEDITOR.remove(CKEDITOR.instances['messageTemplate']);
			}
			var emailTemplate = ckeditor('emailTemplate');
			var messageTemplate = ckeditor('messageTemplate');
			function getValues(){
				$('#emailTemplate').val(emailTemplate.getData());
				$('#messageTemplate').val(messageTemplate.getData());
			}
			function chooseTemplate(val,type){
				$.ajax({
					url : 'nodeMessageController.do?getContent',
					type : 'post',
					data : {
						id : val,
						type : type
					},
					cache : false,
					success : function(data) {
						data = data.substring(1,data.length-1);
						if(type=='email'){
						$('#emailTemplate').val(data);
						   emailTemplate.setData(data);	
						}else if(type=='msg'){
							$('#messageTemplate').val(data);
							messageTemplate.setData(data);
						}else if(type=="sms"){
							$("#smsTemplate").val(data);
						}
						
					}
				});
			}
		</script>
