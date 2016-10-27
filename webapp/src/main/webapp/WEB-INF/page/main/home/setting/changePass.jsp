<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
$(function(){
	//增加密码输入框事件监听，校验密码强度
	$("#new_psd_val").on("keyup blur",function(event){
		//checkPsdStrong(event.target, event.target.value);
	});
});

var changePass = {
		changePasswprd : function(){
			var new_psd_val = $("#new_psd_val").val();
			var cf_n_psd = $("#cf_n_psd").val();
			var old_psd_val = $("#old_psd_val").val();
			if(new_psd_val != cf_n_psd){
				alert("两次密码不一致");
				return false;
			}
			if(new_psd_val.length<6 || new_psd_val.length> 20){
				alert("密码长度必须爱在6-20位之间");
				return false;
			}
			var myData = {"old_psd_val" : old_psd_val,"new_psd_val" : new_psd_val};
			$.ajax({
				url : 'userController.do?changePassword',
				type : 'post',
				data: myData,
				dataType: "json",
				success : function(result) {
					if(result.success){
						$(".menu-pannel-body").load("loginController.do?appSetting");
					}else{
						alert("修改失败");
						
					}
				}
			});
		}
}

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
<div class="padding-1em">
   <form id="formobj" action="userController.do?changePassword" >
   		<input type="hidden" id="userName" name="userName" class="inputxt"  value="${userName }"/>
		<div class="sys-edit-form">
			<div class="form-row type-input">
				<label class="Validform_label row-label">原密码</label>
				<input type="password"  id="old_psd_val" name="old_psd_val" class="inputxt row-input"  value="" placeholder="请输入您的旧密码" ajaxurl="userController.do?checkPassword&userName=${userName }" datatype="*6-16"/>
			</div>
			<div class="form-row type-input">
				<label class="Validform_label row-label">新密码</label>
				<input type="password" id="new_psd_val"  name="new_psd_val" class="inputxt row-input"  value="" placeholder="请输入您的新密码" onpaste="return false"  onkeyup="initPsd(this)"  datatype="*6-16" ajaxurl="userController.do?checkPasswordStrength&userName=${userName }" oldValue=""/>
			</div>
			<div class="form-row type-input">
				<label class="Validform_label row-label">新密码</label>
				<input type="password"  id="cf_n_psd" recheck="new_psd_val" name="cf_n_psd"  onpaste="return false" placeholder="请确认您的新密码" onkeyup="initPsd(this)" class="inputxt row-input"  value="" datatype="*6-16" errormsg="新密码与确认密码不一致"/>
			</div>
			<div class="form-row type-btn">
				<button class="row-btn" onclick="changePass.changePasswprd()">确认修改</button>
			</div>
		</div>
	</form>
</div>
 
