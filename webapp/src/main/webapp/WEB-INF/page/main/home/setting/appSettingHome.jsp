<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
#portraitPage .modal-dialog {
	width: 415px;
}

.avatorInfo {
	margin: 20px 0;
	width: 100%;
	/* 	height: 235px; */
}

.editBox {
	border: 1px solid #f79263;
	/* 	box-shadow: 0 1px 0 #b9b9b9; */
	height: 230px;
	margin-left: 25px;
	margin-right: 22px;
	width: 230px;
	float: left;
}

.priviewDiv {
	float: left;
	height: 230px;
	position: relative;
}

.priviewBox {
	width: 80px;
	height: 80px;
	overflow: hidden;
	/* 	background: url("../images/avatar_default17ced3.png") no-repeat scroll center center rgba(0, 0, 0, 0); */
	border: 1px solid #f79263;
	/* 	box-shadow: 0 1px 0 #b9b9b9; */
}

.sourceImg {
	/* 	width: 230px; */
	/* 	height: 230px; */
	
}

.priviewImg {
	/* 	width: 80px !important; */
	/* 	height: 80px !important; */
	
}
</style>
<!-------------------------------------------------------- jcrop plugin 图片裁剪插件(头像上传) -->
<link rel="stylesheet" href="plug-in/jcrop/css/jquery.Jcrop.css" type="text/css" />
<div class="bbb-body">
	<!-- 
		<div class="forenoon account_home_msg_wrapper">
			<div class="account_home_msg clearfix">
			<input id="id" name="id" type="hidden" value="${member.ID}"/>
				<div style="display: none;">
				<input type="file" name="portrait" id="portrait" />
				</div> 
					<a  class="user_img"  onclick="myAccountInfo.uploadImg()">
					<span>更改头像</span>
					<img id="user_img" src="images/index/default_avatar.png">
				</a>
			    <div class="account_home_msg11">
					<ul class="list-style-none">
						<li class="gg-num3">
					    	<span class="user-name" id="myAccountInfo_phone">您还未绑定手机</span>
					 	</li>
					    <li>亲爱的用户，您好！每一天，努力让梦想更近一些！</li>
						<li class="gg_num1">
					    	<span>积分：<a href="#" id="creditBalance">0.00</a></span>
					    	<span>邦币：<a href="#" id="accountBalance">0.00</a></span>
						</li>
					</ul>
					<a href="#" class="main-inside-letter">
						您有 <span id="myAccountInfo_notReMes">0</span> 条未读消息
					</a>
				</div>
			</div>
		</div>
		 -->
	<div class="bbb-content">
		<div class="bbb-content-inner">
			<div class="account-container">
				<div class="account-left pull-left">
					<div class="acl-top">
						<div class="avatar avatars-area text-center">
							<img src="images/my.png" alt="头像" id="user_img" data-toggle="modal" data-target="#portraitPage"> <a id="userName"
								onClick="javascript:$('#account_right').load('member/personalData');">
								<p id="myName"></p>
							</a>
						</div>
						<ul class="list-style-table numeric-area">
							<li><a onClick="javascript:$('#account_right').load('member/integralRecord');">
									<p class="orange" id="creditBalance">0.00</p>
									<p>积分</p>
							</a></li>
							<li><a onClick="javascript:$('#account_right').load('member/fundRecord');">
									<p class="orange" id="accountBalance">0.00</p>
									<p>邦币</p>
							</a></li>
						</ul>
					</div>
					<ul class="list-style-none acl-fun-list" id="func_list">
						<!-- 
							<li class="acl-title">我的账户</li>
							 -->
						<!-- <li class="seperator"></li>
						<li><a onClick="javascript:$('#account_right').load('member/personalData');">个人资料</a></li> -->
						
						<li class="seperator"></li>
						<li><a id="appList" onClick="javascript:$('#account_right').load('flowFormController.do?appList');">我的工作设置</a></li>
						<li><a id="workGroupList" onClick="javascript:$('#account_right').load('orgGroupController.do?workGroupList');">我的工作组</a></li>
						<li><a id="flowFormList" onClick="javascript:$('#account_right').load('flowFormController.do?flowFormList');">我的任务</a></li>
			
						<li class="seperator"></li>
						<li><a id="changePass" onClick="javascript:$('#account_right').load('userController.do?changePass1');">修改密码</a></li>			
						<li id="loginOut" ><a >退出登录</a></li>
						<li class="seperator"></li>
					</ul>
				</div>
				<div class="account-right-container">
					<div id="account_right" class="account-right"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 选人弹框 -->
<div class="modal fade in-iframe" id="mapTestDiv" role="dialog">
	<div class="modal-dialog with-select-all" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">地图测试</h4>
			</div>
			<div class="modal-body clearfix">
				<div id="testMap" style="width:500px;height:500px;">
				<iframe id="myMap" src="flowFormController.do?mapTest" width="100%" height="100%">
				
				</iframe>
				</div>
			</div>
			<div class="modal-footer">
				<button id="mapConfirm" type="button" class="btn btn-orange" data-dismiss="modal">确定</button> 
			</div>
		</div>
	</div>
</div>


<div id="portraitPage" class="modal fade" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">更改头像</h4>
			</div>
			<div class="modal-body">
				<form id="form_portaitUpload" method="post" action="userController.do?uploadPortraitFile" enctype="multipart/form-data">
					<!-- 	<table cellpadding="0" cellspacing="1" class="formtable"> -->
					<!-- 		<tr> -->
					<!-- 			<td class="td_title"><label class="Validform_label">图片上传<font color='red'>*.gif,*.jpg,*.png,*.bmp</font></label></td> -->
					<!-- 			<td class="value"></td> -->
					<!-- 		</tr> -->
					<!-- 	</table> -->
					<input type="hidden" id="portaitUpload_x" name="portaitUpload_x" /> <input type="hidden" id="portaitUpload_y" name="portaitUpload_y" /> <input
						type="hidden" id="portaitUpload_w" name="portaitUpload_w" /> <input type="hidden" id="portaitUpload_h" name="portaitUpload_h" /> <input type="hidden"
						id="portaitUpload_w" name="portaitUpload_w" />
					<div class="avatorInfo clearfix">
						<div class="editBox">
							<!-- 			<img class="sourceImg" src="webpage/main/sago.jpg" id="sourceImg" onload="resizeImg(this,230,230);initJcrop()" /> -->
						</div>
						<div class="priviewDiv">
							<div class="priviewBox">
								<img class="priviewImg" id="preview" />
							</div>
							<span id="selectImgSpan" class="btn btn-xs btn-warn fileinput-button" style="width: 80px; position: absolute; bottom: 40px"> <i
								class="awsm-icon-plus-search"></i>选择文件<input id="selectImg" type="file" name="files[]" multiple />
							</span><br /> <span id="useImg" class="btn btn-xs btn-warn fileinput-button" style="width: 80px; position: absolute; bottom: 0"> <i
								class="awsm-icon-plus-search"></i>使用头像
							</span>
						</div>
					</div>
				</form>

			</div>
		</div>
		<!-- / .modal-content -->
	</div>
	<!-- / .modal-dialog -->
</div>


<!-------------------------------------- jcrop plugin 图片裁剪插件(头像上传) -->
<script src="plug-in/jcrop/js/jquery.Jcrop.js"></script>
<script type="text/javascript">
	var previewWidth = 80;
	var previewHeight = 80;
	var maxWidth = 230;
	var maxHeight = 230;
	var aspectRatio = 1;
	$(function() {
		
		var portrait = user_portrait ? '${attachForeRequest}' + user_portrait : 'basic/img/avatars/avatar_80.png';
		$("#user_img").attr("src", portrait);
		var userName = '${userName}';
		$("#myName").text(userName?userName:"未设置昵称");
		
		$("#loginOut").on("click", function(e){
			
			e.stopPropagation();
			e.preventDefault();
			 var a=confirm("您确定要执行该操作吗?");
			 if(a==true){
				 location.href ="loginController.do?logout";
			 }
		});
		
		$("#mapConfirm").on("click", function(e){
			e.stopPropagation();
			e.preventDefault();
			 var a=confirm("您确定要执行该操作吗?");
			 if(a==true){
				 alert("非常好");
			 }
		});
		
		//设置提示语句
		// 		yitip($("#useImg"), "建议上传容量小于100KB的头像文件");
		
		/*
		$('#form_portaitUpload').fileupload({
			uploadTemplateId : null,
			downloadTemplateId : null,
			add : function(e, data) {
				$("#useImg").off().on("click", function() {
					data.submit();
				});
			},
			send : function(e, data) {
			},
			done : function(e, data) {
				if (data.textStatus == "success") {
					var result = data.result;
					if (result && result.success) {
						//给对应预览图赋值
						$("#user_img").attr("src", result.obj);
						$("#userInfoDiv img").attr("src", result.obj);
						//关闭上传页
						$("#portraitPage").modal('hide');
					}
					tip(result.msg);
				} else {
					tip("头像上传失败,请重新上传!");
				}
			}
		});
		*/

		var jcrop_api, boundx, boundy;
		FileReader = window.FileReader;
		$("#selectImg").change(function() {
			var image = $("<img>");
			if (jcrop_api != null) {
				jcrop_api.destroy();
			}
			if (FileReader) {
				//Firefox
				var reader = new FileReader();
				var file = this.files[0];
				reader.readAsDataURL(file);
				reader.onload = function(e) {
					image.attr("src", this.result);
					image.attr("id", "sourceImg");
					image.hide();
					image.on("load", function() {
						//缩放尺寸
						resizeImg(this, maxWidth, maxHeight);
						image.show();
						//初始化裁剪工具
						initJcrop();
					});
					$("#preview").attr("src", this.result);
					$(".editBox").html(image);
				};
			} else {
				//IE
				var path = $(this).val();
				image.src = path;
				image.id = "sourceImg";
				$("#preview").attr("src", path);
				$(".editBox").html(image);
				$(image).Jcrop({
					onChange : updatePreview,
					onChange : updateCoords,
					onSelect : updateCoords,
					onSelect : updatePreview,
					onRelease : clearCoords
				}, function() {
					jcrop_api = this;
				});
				//设置长宽比
				width = image.width;
				height = image.height;
				while (width > MAXWIDTH || height > MAXHEIGHT) {
					var rat;
					if (width > MAXWIDTH) {
						rat = MAXWIDTH / width;
						width = MAXWIDTH;
						height = height * rat;
					}
					if (height > MAXHEIGHT) {
						rat = MAXHEIGHT / height;
						height = MAXHEIGHT;
						width = width * rat;
					}
				}
				$(image).css('width', width);
				$(image).css('height', height);
				$("#sourceImg").css({
					width : width,
					height : height
				});
			}
		});
		if('${load}'==1){
			$("#flowFormList").trigger("click");
		}
	});

	function goPortraitPage() {
		$("#portraitPage").modal('show');
	}

	function updateCoords(c) {
		$('#portaitUpload_x').val(Math.round(c.x));
		$('#portaitUpload_y').val(Math.round(c.y));
		$('#portaitUpload_w').val(Math.round(c.w));
		$('#portaitUpload_h').val(Math.round(c.h));
	}

	function initJcrop() {
		$('#sourceImg').Jcrop({
			//大小改变事件
			onChange : function(c) {
				showPreview(c);
				updateCoords(c);
			},
			//选择事件
			onSelect : function(c) {
				showPreview(c);
				updateCoords(c);
			},
			aspectRatio : 1,
			allowSelect : false,
			allowResize : true,
			addClass : 'jcrop-dark',
			bgOpacity : 0.5
		}, function() {
			jcrop_api = this;
			//jcrop_api.ui.selection.addClass('jcrop-selection');
			jcrop_api.setSelect([ 0, 0, 80, 80 ]);
			jcrop_api.setOptions({
				bgFade : true
			});
			jcrop_api.ui.holder.css("margin-top", $("#sourceImg").css("margin-top"));
			jcrop_api.ui.holder.css("margin-left", $("#sourceImg").css("margin-left"));
			$(".sourceImg").css("position", "static");
		});
	}

	function showPreview(coords) {
		var rx = previewWidth / (coords.w || 1);
		var ry = previewHeight / (coords.h || 1);
		var boundx = $("#sourceImg").width();
		var boundy = $("#sourceImg").height();
		$('#preview').css({
			width : Math.round(rx * boundx) + 'px',
			height : Math.round(ry * boundy) + 'px',
			marginLeft : '-' + Math.round(rx * coords.x) + 'px',
			marginTop : '-' + Math.round(ry * coords.y) + 'px'
		});
	}
	function resizeImg(ImgD, rWidth, rHeight) {
		var image = new Image();
		image.src = ImgD.src;
		if (image.width > 0 && image.height > 0) {
			if (image.width / image.height >= rWidth / rHeight) {
				if (image.width > rWidth) {
					ImgD.width = rWidth;
					ImgD.height = image.height * (rWidth / image.width);
					$(ImgD).css("margin-top", (rHeight - ImgD.height) / 2);
				} else {
					ImgD.width = image.width;
					ImgD.height = image.height;
					$(ImgD).css("margin-left", (rWidth - ImgD.width) / 2);
					$(ImgD).css("margin-top", (rHeight - ImgD.height) / 2);
				}
			} else {
				if (image.height > rHeight) {
					ImgD.height = rHeight;
					ImgD.width = image.width * (rHeight / image.height);
					$(ImgD).css("margin-left", (rWidth - ImgD.width) / 2);
				} else {
					ImgD.width = image.width;
					ImgD.height = image.height;
					$(ImgD).css("margin-top", (rHeight - ImgD.height) / 2);
					$(ImgD).css("margin-left", (rWidth - ImgD.width) / 2);
				}
			}
			ImgD.title = image.width + "×" + image.height;
		}
	}
</script>
</html>
