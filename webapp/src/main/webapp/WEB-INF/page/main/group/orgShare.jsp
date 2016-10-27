<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <meta charset="utf-8" />
	    <title>邀请您加入办公</title>
	    <meta name="description" content=xplatform />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    <link rel="shortcut icon" href="basic/img/favicon.png" type="image/x-icon">
	
	    <!--Basic Styles-->
	    <link href="basic/css/bootstrap.min.css" rel="stylesheet" />
	    <link href="basic/css/font-awesome.min.css" rel="stylesheet" />
	    <link href="basic/css/weather-icons.min.css" rel="stylesheet" />
	    
	     <!-- share样式文件 -->
	    <link href="basic/css/share.less" rel="stylesheet/less" />
	    
	    <!-- LESS -->
	    <script src="basic/js/less.min.js"></script>
	    
	    
	</head>
	<body class="share-page">
		<div class="body-container">
			<div class="top-color"></div>
			<div class="body-content">
				<input type="hidden" id="orgId" value="${orgId}">
				<div class="top-inner text-center">
					<div class="avatar">
						<img id="portrait" src="basic/img/avatars/avatar_80.png" alt="头像">
					</div>
					<p><span>${ownerName }</span>邀请您加入</p>
					<p class="group-name">${name}</p>
				</div>
				<div class="seperator"></div>
				<div class="bottom-inner">
					<div class="form-box">
						<div class="form-input">
							<input type="text" id="phone" placeholder="输入手机号码">
							
						</div>
						<div class="form-input">
							<input type="password" id="password" placeholder="请输入密码">
						</div>
						<div class="form-btn">
							<button class="btn btn-orange" onclick="orgShare.joinTeam()">加入团队</button>
						</div>
						<div class="loginbox-signup">
	                		<a href="loginController.do?toRegister">注册</a>
	            		</div>
					</div>
				</div>
			</div>
		</div>
	</body>

    <!--Basic Scripts-->
    <script src="basic/js/jquery-2.0.3.min.js"></script>
    <script src="basic/js/jquery-migrate-1.1.0.js"></script>
    <script src="basic/js/html5.js"></script>
    <script src="basic/js/bootstrap.min.js"></script>
    <!-- h5验证文件 -->
	<script type="text/javascript" src="basic/js/html5Validate/jquery-html5Validate.js"></script>
    
    <!--[if lt IE 9]>
	  <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
	  <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
	<script>
	var orgShare = {
			loadPortrait : function(){
				//var imgURL = '${attachForeRequest }';
				var portrait = '${portrait}';
				if(portrait != null && portrait != ""){
					var myportrait = '${attachForeRequest}' + portrait;
					$("#portrait").attr("src",myportrait);
				}
				//alert(myportrait);
			},
			//加入团队
			joinTeam : function(){
				var reg =/^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/; 
				var orgId = $("#orgId").val();
				var phone = $("#phone").val();
				var orgIds = '${orgIds}';
				var type = '${type}';
				if (!reg.test(phone)){
					//$("#phone").testRemind("请输入已注册的有效手机号码！");
					alert("请输入已注册的有效手机号码！");
					return false;
				}
				var password = $("#password").val();
				if (password == ""){
					//$("#password").testRemind("请输入验证密码");
					alert("请输入验证密码");
					return false;
				}
				var data = {"orgId" : orgId,"phone" : phone,"userName" : phone,"password" : password,"orgId" : orgId,"orgIds" : orgIds,"type" : type};
				
				var myurl = "userOrgController.do?addUserFromWeb";
				$.ajax({
					async : true,
					type : "get",
					url : myurl,
					data : data,
					dataType : "json",
					success : callback
				});

				function callback(result) {
					alert(result.msg);
					if(result.status == "3"){
						location.href = "loginController.do?toRegister";
					}

				}
			}
		};
		$(function(){
			orgShare.loadPortrait();
		});
	</script>
</html>
