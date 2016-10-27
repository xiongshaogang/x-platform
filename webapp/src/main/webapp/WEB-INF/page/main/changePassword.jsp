<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.formtable {
	width: 100%; 
	/* background-color: #B8CCE2; */
	margin:5px auto;
}

.formtable .td_title{
	width:90px;
	height:30px;
	text-align:right;
	/*padding-left:5px;*/
}

.formtable .value {
	/*background-color: #FFFFFF;*/
	width:200px;
	height:30px;
	padding:4px;
}
</style>
<script type="text/javascript">
$(function(){
	//增加密码输入框事件监听，校验密码强度
	$("#new_psd_val").on("keyup blur",function(event){
		//checkPsdStrong(event.target, event.target.value);
	});
});

function checkPwd(){
	var message = '';
	var flag = true;
	$.ajax({
		url : 'loginController.do?findSysParameterCode&code=passwordStrength',
		type : 'post',
		async : false,
		cache : false,
		success : function(data) {
			var d = $.parseJSON(data);
			console.info(d);
			if (d.success) {
				isVerificationCode = d.msg;
				if(d.msg == 'low'){
					if($(".pst-strong").data("psd_level")<1){
						message = "密码安全级别必须达到低以上";
						flag = false;
					}
				}else if(d.msg == 'medium'){
					if($(".pst-strong").data("psd_level")<2){
						message = "密码安全级别必须达到中以上";
						flag = false;
					}
				}else if(d.msg == 'high'){
					if($(".pst-strong").data("psd_level")<3){
						message = "密码安全级别必须达到高以上";
						flag = false;
					}
				}else{
					if($(".pst-strong").data("psd_level")<1){
						message = "密码安全级别必须达到低以上";
						flag = false;
					}
				}
			}else{
				if($(".pst-strong").data("psd_level")<1){
					message = "密码安全级别必须达到低以上";
					flag = false;
				}
			}
		}
	});
	console.info(message);
	if(message != ''){
		tip(message);
	}
	return flag;
}
function initPsd(obj){
	$("#"+obj.id).val($.trim(obj.value));
}
</script>
   <t:formvalid formid="formobj" action="userController.do?changePassword" >
   		<input type="hidden" id="userName" name="userName" class="inputxt"  value="${userName }"/>
		<table cellpadding="0" cellspacing="1" class="formtable" >
			<tr>
			<td class="td_title"><label class="Validform_label"><span style="color:red">*</span>原密码:</label></td>
			<td class="value" colspan="2"><input type="password"  id="old_psd_val" name="old_psd_val" class="inputxt"  value="" ajaxurl="userController.do?checkPassword&userName=${userName }" datatype="*6-16"/></td>
			</tr>
			<tr>
			<td class="td_title"><label class="Validform_label"><span style="color:red">*</span>新密码:</label></td>
			<td class="value"><input type="password" id="new_psd_val"  name="new_psd_val" class="inputxt"  value="" onpaste="return false"  onkeyup="initPsd(this)"  datatype="*6-16" ajaxurl="userController.do?checkPasswordStrength&userName=${userName }" oldValue=""/></td>
			<td class="value"></td>
			</tr>
			<tr>
			<td class="td_title"><label class="Validform_label"><span style="color:red">*</span>确认密码:</label></td>
			<td class="value" colspan="2"><input type="password"  id="cf_n_psd" recheck="new_psd_val" name="cf_n_psd"  onpaste="return false"  onkeyup="initPsd(this)" class="inputxt"  value="" datatype="*6-16" errormsg="新密码与确认密码不一致"/></td>
			</tr>
		</table>
	</t:formvalid>
 
