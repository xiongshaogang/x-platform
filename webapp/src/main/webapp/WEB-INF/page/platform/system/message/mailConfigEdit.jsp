<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
$(function(){
		var myButton = {
				id : 'add',
				text : '测试连接',
				iconCls : 'awsm-icon-plus',
				operationCode : "messageManager_testConnect_other",
				handler : function() {
					var obj = new Object();
				    obj.userName = $("#userName").val();
				    obj.mailType = $("#mailType").val();
				    obj.mailAddress = $("#mailAddress").val();
				    obj.passWord = $("#passWord").val();
				    obj.smtpHost = $("#smtpHost").val();
				    obj.smtpPort = $("#smtpPort").val();
				    obj.popHost =$("#popHost").val();
				    obj.popPort =$("#popPort").val();
				    obj.imapHost = $("#imapHost").val();
				    obj.imapPort =$("#imapPort").val();
				    console.info($.toJSON(obj));
					doAction("messageController.do?testConnect&data="+$.toJSON(obj), "", "","",$("#id").val());
				}
			};
		addButton(getD($("#formobj")),myButton,1);
	});
</script>
 <t:formvalid formid="formobj" action="messageController.do?saveMailConfig" gridId="mailConfigList" refresh="true" >
	<input id="id" name="id" type="hidden" value="${mailConfig.id }">
	<input id="userId" name="userId" type="hidden" value="${user.id }">
	<table  cellpadding="0" cellspacing="1" class="formtable" style="margin-right: 33px;">
		<tr>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>账号名称:</label></td>
			<td class="value">
			    <input datatype="*" id="userName" name="userName" type="text" class="inputxt" value='${mailConfig.userName}'>
			</td>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>邮箱类型: </label></td>
			<td class="value">
			    <%-- <select name="mailType" id="mailType">
					<option value="pop3" <c:if test="${mailConfig.mailType=='pop3'}">selected="selected"</c:if> >pop3类型</option>
					<option value="imap" <c:if test="${mailConfig.mailType=='imap' }">selected="selected"</c:if>>imap类型</option>
				</select> --%>
				<t:comboBox onSelect="mailChange(record,self)" name="mailType"  id="mailType" value="${mailConfig.mailType}" data='[{"id":"pop3","text":"pop3类型"},{"id":"imap","text":"imap类型"}]'></t:comboBox>
			</td>
		</tr>
		<tr>
		    <td class="td_title"><label class="Validform_label"><span class="span_required">*</span>邮箱地址:</label></td>
			<td class="value">
			    <input datatype="*" id="mailAddress" name="mailAddress" type="text" class="inputxt" value='${mailConfig.mailAddress}'>
			</td>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>邮箱密码:</label></td>
			<td class="value">
			    <input datatype="*" id="passWord" name="passWord" type="password" class="inputxt" value='${mailConfig.passWord}'>
			</td>
		</tr>
		<tr>
		    <td class="td_title"><label class="Validform_label"><span class="span_required">*</span>smtp主机:</label></td>
			<td class="value">
			    <input datatype="*" id="smtpHost" name="smtpHost" type="text" class="inputxt" value='${mailConfig.smtpHost}'>
			</td>
			<td class="td_title"><label class="Validform_label"><span class="span_required">*</span>smtp端口:</label></td>
			<td class="value">
			    <input datatype="*" id="smtpPort" name="smtpPort" type="text" class="inputxt" value='${mailConfig.smtpPort}'>
			</td>
		</tr>
		<tr <c:if test="${mailConfig.mailType=='imap' }">style="display: none;"</c:if> id="pop">
		    <td class="td_title"><label class="Validform_label">pop主机:</label></td>
			<td class="value">
			    <input  id="popHost" name="popHost" type="text" class="inputxt" value='${mailConfig.popHost}'>
			</td>
			<td class="td_title"><label class="Validform_label">pop端口: </label></td>
			<td class="value">
			    <input  id="popPort" name="popPort" type="text" class="inputxt" value='${mailConfig.popPort}'>
			</td>
		</tr>
		<tr <c:if test="${mailConfig.mailType=='pop3' || empty mailConfig.mailType}">style="display: none;"</c:if> id="imap">
		    <td class="td_title"><label class="Validform_label">imap主机:</label></td>
			<td class="value">
			    <input  id="imapHost" name="imapHost" type="text" class="inputxt" value='${mailConfig.imapHost}'>
			</td>
			<td class="td_title"><label class="Validform_label">imap端口:</label></td>
			<td class="value">
			    <input  id="imapPort" name="imapPort" type="text" class="inputxt" value='${mailConfig.imapPort}'>
			</td>
		</tr>
	</table>
</t:formvalid>
 <script type="text/javascript">
     $("#mailType").change(function(){
		var type=$(this).val();
		if(type=='pop3'){
			$("#imap").hide();
			$("#pop").show();
		}else{
			$("#pop").hide();
			$("#imap").show();
		}
	});
     
     function mailChange(record,self){
    	var type=$(self).val();
  		if(type=='pop3'){
  			$("#imap").hide();
  			$("#pop").show();
  		}else{
  			$("#pop").hide();
  			$("#imap").show();
  		}
  		$("#mailAddress").blur();
     }
     
     $("#mailAddress").blur(function(){
			var address=$("#mailAddress").val();
			if(address!=''){
				address=address.trim();
				var type=$("#mailType").val();
				var s=address.substring(address.indexOf('@')+1,address.length+1).trim().toLowerCase();
				$("#smtpHost").val('smtp.'+s.replace("ucgsoft.com",'qq.com'));
				$("#popHost").val('pop'+'.'+s.replace("ucgsoft.com",'qq.com'));
				if(type=='pop3'){
					if(s=='gmail.com'||s=='msn.com'||s=='live.cn'||s=='live.com'||s=='hotmail.com'){
						$("#popPort").val('995');
						$("#smtpPort").val('587');
					}else if(s == 'qq.com' || s == 'ucgsoft.com'){
						$("#popPort").val('995');
						$("#smtpPort").val('465');
					}else{
						$("#popPort").val('110');
						$("#smtpPort").val('25');
					}
				}else{
					if(s=='gmail.com'||s=='msn.com'||s=='live.cn'||s=='live.com'||s=='hotmail.com'){
						$("#imapPort").val('993');
						$("#smtpPort").val('587');
					}else if(s == 'qq.com' || s == 'ucgsoft.com'){
						$("#imapPort").val('995');
						$("#smtpPort").val('465');
					}else{
						$("#imapPort").val('143');
						$("#smtpPort").val('25');
					}
					$("#imapHost").val('imap'+'.'+s);
				}
			}
		});
     
 </script>