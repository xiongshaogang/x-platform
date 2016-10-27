<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<!--
Beyond Admin - Responsive Admin Dashboard Template build with Twitter Bootstrap 3
Version: 1.0.0
Purchase: http://wrapbootstrap.com
-->

<html xmlns="http://www.w3.org/1999/xhtml">
<!--Head-->
<head>
    <meta charset="utf-8" />
    <title>Rigister Page</title>

    <meta name="description" content="login page" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <!--Basic Styles-->
    <link href="basic/css/bootstrap.min.css" rel="stylesheet" />
    <link href="basic/css/font-awesome.min.css" rel="stylesheet" />


    <!--Beyond styles-->
    <link href="basic/css/beyond.min.css" rel="stylesheet" />
    <link href="basic/css/beyond-rewrite.css" rel="stylesheet" />
    <link href="basic/css/animate.min.css" rel="stylesheet" />

    <script src="basic/js/skins.min.js"></script>
    <link rel="stylesheet" href="plug-in/jquery-realperson/jquery.realperson.css" />
</head>
<!--Head Ends-->
<!--Body-->
<body>
    <div class="login-container animated fadeInDown" id="registerInput">
	        <div class="loginbox bg-white">
	            <div class="loginbox-title">请输入注册信息 </div>
	            
	            <div class="loginbox-textbox">
	                <span class="input-icon icon-left">
						<input id="userName" name="userName" value="" class="form-control" type="text" placeholder="手机号">
						<i class="glyphicon glyphicon-user"></i>
						<button class="bfs-rightbtn" id="verifyCodeBtn" onclick="register.sendVerifyCode(this)">获取验证码</button>
					</span>
	            </div>
	            <div class="loginbox-textbox">
	                <span class="input-icon icon-left">
						<input id="verifyCode" name="verifyCode" value="" type="text" class="form-control" placeholder="验证码">
						<i class="fa fa-lock"></i>
					</span>
	            </div>
	            <div class="loginbox-textbox">
	                <span class="input-icon icon-left">
						<input id="password" name="password" value="" type="password" class="form-control" placeholder="密码">
						<i class="fa fa-lock"></i>
					</span>
	            </div>
	            <div class="loginbox-textbox">
	                <span class="input-icon icon-left">
						<input id="surePass" name="surePass" value="" type="password" class="form-control" placeholder="确认密码">
						<i class="fa fa-lock"></i>
					</span>
	            </div>
	            <div id="jy-valid" class="loginbox-textbox">
	            </div>
	            <div class="loginbox-submit">
	                <input id="register" type="button" class="btn btn-primary btn-block" value="注册" onclick="register.register()">
	            </div>

<!-- 	            <div class="loginbox-signup">
	                <a href="register.html">注册</a>
	            </div>
	            <div class="loginbox-or">
	                <div class="or-line"></div>
	                <div class="or">APP扫描下载</div>
	            </div> -->
	            <div class="loginbox-social">
	                <!-- <div class="social-title ">APP下载</div> -->
	                <div class="social-buttons">
	                    <!-- <a href="" class="button-facebook">
	                        <i class="social-icon fa fa-facebook"></i>
	                    </a>
	                    <a href="" class="button-twitter">
	                        <i class="social-icon fa fa-twitter"></i>
	                    </a>
	                    <a href="" class="button-google">
	                        <i class="social-icon fa fa-google-plus"></i>
	                    </a> -->
	                    <!-- <img width="200" height="200" src="images/appdownload/downapp.png"/> -->
	                </div>
	            </div>
	        </div>
    </div>

    <!--Basic Scripts-->
    <script src="basic/js/jquery-2.0.3.min.js"></script>
    <script src="basic/js/bootstrap.min.js"></script>
    
    <script src="plug-in/tools/syUtil.js"></script>
    <script src="plug-in/tools/curdtools.js"></script>
    <script type="text/javascript" src="plug-in/login/js/login.js"></script>
    <!-- h5验证文件 -->
	<script type="text/javascript" src="basic/js/html5Validate/jquery-html5Validate.js"></script>
	<script type="text/javascript" src="plug-in/tools/syUtil.js"></script>
</body>
<!--Body Ends-->
<script type="text/javascript">
	var register = {
		 sendVerifyCode : function(ele) {
			var reg_username = $("#userName").val();
			var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/; 
			if (reg_username == "") {
				$("#userName").testRemind("请输入手机号");
				return false;
			} else if (!reg.test(reg_username)) { //判断是否为手机号
				$("#userName").testRemind("请输入有效的手机号");
				return false;
			} else {
				//var url = "loginController.do?verifyCode";
				var data = {
					"phone" : $("#userName").val(),
					"moduleFlag" : "register"
				};
				$.ajax({
					url : 'loginController.do?getVerifyCode',
					type : 'post',
					data: data,
					dataType: "json",
					success : function(result) {
						if(result.success){
							$("#userName").testRemind(result.msg);
						}
					}
					
				});
			}
		},
		register : function(){
			var password = $("#password").val();
			var surePass = $("#surePass").val();
			var reg_username = $("#userName").val();
			var verifyCode = $("#verifyCode").val();
			var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/; 
			if (reg_username == "") {
				$("#userName").testRemind("请输入手机号");
				return false;
			}else if (!reg.test(reg_username)) { //判断是否为手机号
				$("#userName").testRemind("请输入有效的手机号");
				return false;
			}else if (verifyCode == "") { 
				$("#verifyCode").testRemind("请输入验证码");
				return false;
			}else if((password.length < 6) || (password.length > 20)){
				$("#password").testRemind("密码长度应在6-20位之间");
				return false;
			}else if(password != surePass){
				$("#surePass").testRemind("两次密码不一致");
				return false;
			}
			var data = {
				"phone" : $("#userName").val(),
				"password" : $("#password").val(),
				"verifyCode" : $("#verifyCode").val()
			};
 			$.ajax({
				url : 'loginController.do?register',
				type : 'post',
				data: data,
				dataType: "json",
				success : function(result) {
					if(result.success){
						$.ajax({
							url : 'loginController.do?checkuser',
							type : 'post',
							data: data,
							dataType: "json",
							success : function(result) {
								if(result.success){
									location.href = "loginController.do?home";
								}else{
									$("#userName").testRemind(result.msg);
								}
							}
							
						}); 
						location.href = "loginController.do?home";
					}else{
						$("#userName").testRemind(result.msg);
					}
				}
				
			}); 
		
		}
	};
	$(function(){
// 		$("#navbtn_index").closest("ul").find("a").removeClass("active");
	});
</script>
</html>