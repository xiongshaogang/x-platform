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
    <title>Login Page</title>

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
    <div class="login-container animated fadeInDown">
    	<form name="formLogin" id="formLogin" check="loginController.do?checkuser" method="post">
	        <div class="loginbox bg-white">
	            <div class="loginbox-title">请输入登录信息 </div>
	            
	            <div class="loginbox-textbox">
	                <span class="input-icon icon-left">
						<input id="userName" name="userName" value="" class="form-control" type="text" placeholder="手机号">
						<i class="glyphicon glyphicon-user"></i>
					</span>
	            </div>
	            <div class="loginbox-textbox">
	                <span class="input-icon icon-left">
						<input id="password" name="password" value="" type="password" class="form-control" placeholder="密码">
						<i class="fa fa-lock"></i>
					</span>
	            </div>
	            <div id="jy-valid" class="loginbox-textbox">
	            </div>
	            <div class="loginbox-submit">
	                <input id="but_login" type="button" class="btn btn-primary btn-block" value="登录">
	            </div>
	            <div class="loginbox-forgot">
	                <a href="">忘记密码</a>
	            </div>
	            <div class="loginbox-signup">
	                <a href="register.html">注册</a>
	            </div>
	            <div class="loginbox-or">
	                <div class="or-line"></div>
	                <div class="or">APP扫描下载</div>
	            </div>
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
	                    <img width="200" height="200" src="images/appdownload/downapp.png"/>
	                </div>
	            </div>
	        </div>
        </form>
    </div>

    <!--Basic Scripts-->
    <script src="basic/js/jquery-2.0.3.min.js"></script>
    <script src="basic/js/bootstrap.min.js"></script>
    
    <script src="plug-in/tools/syUtil.js"></script>
    <script src="plug-in/tools/curdtools.js"></script>
    <script type="text/javascript" src="plug-in/login/js/login.js"></script>
</body>
<!--Body Ends-->
</html>
