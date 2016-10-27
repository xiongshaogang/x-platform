<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <meta charset="utf-8" />
	    <title>首页</title>
	    <meta name="description" content=xplatform />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    <link rel="shortcut icon" href="basic/img/favicon.png" type="image/x-icon">
	
	    <!--Basic Styles-->
	    <link href="basic/css/bootstrap.min.css" rel="stylesheet" />
	    <link href="basic/css/font-awesome.min.css" rel="stylesheet" />
	    <link href="basic/css/weather-icons.min.css" rel="stylesheet" />
	    <link href="basic/css/bootstrap-switch.min.css" rel="stylesheet" />
	    
	    <!-------------------------------------------------------- jquery File Upload 上传组件 -->
		<link rel="stylesheet" href="plug-in/jquery-fileupload/css/jquery.fileupload-ui.css" />
		<link rel="stylesheet" href="plug-in/jquery-fileupload/css/jquery.fileupload.css" />
	
		<!-------------------------------------------------------- uploadifive 上传组件 -->
		<link rel="stylesheet" type="text/css" href="plug-in/uploadifive-v1.2.2-standard/uploadifive.css">
	    <!-------------------------------------------------------- 系统通用样式 -->
		<link rel="stylesheet" href="basic/css/common.css"></link>
		<link rel="stylesheet/less" href="basic/css/index.less"></link>
	    
	    <!-- home样式文件 -->
	    <link href="basic/css/home.common.less" rel="stylesheet/less" />
	    <link href="basic/css/commonForm.less" rel="stylesheet/less" />
	    <link href="basic/css/home.less" rel="stylesheet/less" />
	    
	    <!-- LESS -->
	    <script src="basic/js/less.min.js"></script>
	</head>
	<body style="background: url(basic/img/background_home.jpg) no-repeat fixed top; background-size: 100%">
	    <div class="layout-container">
	    	<div class="layout-main">
	    		<div class="header">
					<div id="userInfoDiv" class="h-left pull-left">
						<ul class="list-style-none-h">
							<li class="avatar"><img src="<c:choose><c:when test='${empty user.portrait}'>basic/img/avatars/avatar_80.png</c:when><c:otherwise>${attachForeRequest}${user.portrait}</c:otherwise></c:choose>" alt="头像" /> <span class="username">${user.name}</span></li>
						</ul>
					</div>
					<div class="h-right pull-right">
						<!-- 
	    				<div class="search">
    						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
	    					<input type="text" placeholder="搜索联系人、讨论组、群、工作">
	    				</div>
	    				<div class="logofont-style" >bangbang<span class="orange">bang</span></div>
						 -->
						 <div class="logo">
						 	<img src="basic/img/logo_SAIFAMC.png" alt="logo">
						 </div>
		    		</div>
	    		</div>
	    		<div class="body">
	    			<div class="menu-pannel">
	    				<ul class="list-style-none text-center main-menus">
	    					<li id="sysHome" class="active" title="消息" data-url="messageController.do?sysMessageHome">
	    						<img src="basic/img/left_bang_4.png" alt="首页">
	    						<!-- 
	    						<div class="logofont-style btn-home">bang</div>
	    						 -->
	    					</li>
	    					<li id="sysApp" data-url="flowFormController.do?flowFormApp">
	    						<!-- 
	    						<span class="glyphicon glyphicon-th-large"></span>
	    						 -->
	    						 <img src="basic/img/left_app.png" alt="应用">
	    					</li>
	    					<li id="sysPlus" data-url="appFormFieldController.do?appForm">
	    						 <img src="basic/img/left_add.png" alt="添加">
	    						<!-- 
	    						<span class="glyphicon glyphicon-plus"></span>
	    						 -->
	    					</li>
	    					<!-- 
	    					<li id="sysHome" class="active" title="消息" data-url="messageController.do?home">
	    						<div class="logofont-style btn-home">bang</div>
	    					</li>
	    					<li id="sysConfig" data-url="loginController.do?appSetting"><span class="glyphicon glyphicon-cog"></span></li>
	    					<li id="sysChat" ><span class="glyphicon glyphicon-cog"></span></li>
	    					 -->
	    				</ul>
	    				<!-- 
	    				<ul class="list-style-none text-center extra-options">
	    					<li id="sysConfig"><span class="glyphicon glyphicon-cog"></span></li>
	    				</ul>
	    				 -->
	    			</div>
	    			<div class="menu-pannel-body">
	    				<iframe id="formHomeIframe" width="100%" height="100%">
						</iframe>
	    			</div>
	    			
	    		</div>
		    	<div class="menu-pannel-pop home" id="homeSlidePop">
	   				<div class="header text-center">
	   					<span class="pop-title">标题</span>
	   					<span class="pull-right light-grey close-btn">×</span>
	   				</div>
	   				<div class="body">
	   					<div class="body-content" id="myFrame">
	   						<!-- <iframe id="myFrame" width="100%" height="500px" src=""></iframe> -->
	   					</div>
	   				</div>
	   			</div>
	    	</div>
	    </div>
	    
	<!-- 聊天弹框 -->
	<div class="modal fade chat-modal" id="ImModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content" style="height:550px">
<!-- 				<div class="modal-header"> -->
<!-- 					<button type="button" class="close" data-dismiss="modal" aria-label="Close"> -->
<!-- 						<span aria-hidden="true">&times;</span> -->
<!-- 					</button> -->
<!-- 					<h4 class="modal-title">企信 -->
<!-- 					</h4> -->
<!-- 				</div> -->
				<div class="modal-body clearfix">

					<div id="im-recent-div" class="sub-menu-pannel" >
						<div class="text-center list-title">企信</div>
						<ul id="im-recent-ul" class="list-style-none msg-list">
						</ul>
					</div>
					<div class="content-pannel" >
						<div class="chat-top-bar">
							<div class="tool-bar pull-right">
								<button id="group-settings" class="glyphicon glyphicon-cog"></button>
							</div>
						</div>
						<div class="chat-box-container">
							<div id="chat-content" class="chat-box">
								<div class="chat-main">
									<div id="null-nouser" class="sys-notice">暂时没有聊天信息</div>
								</div>
							</div>
						</div>
						<div id="chat-send" class="chat-input-container">
							<div class="chat-input-toolbar">
								<ul class="list-style-none-h tool-list">
									<li>
										<button id="btn_emotion" class="glyphicon glyphicon-heart"></button>
										<div class="pop-expression">
											<div class="pop-expression-container">
												<ul id="emotionUL" class="list-style-none-h expression-list">
												</ul>
											</div>
										</div>
									</li>
									<li>
										<button id="btn_allFiles" class="glyphicon glyphicon-paperclip" alt="图片、音视频、文件"></button> <input type='file' id="fileInput"
										style="opacity: 0; position: absolute; left: 0; width: 18px; display: inline" onchange="sendFile()" />
									</li>
								</ul>
							</div>
							<div class="chat-input-text">
								<textarea id="talkInputId" autofocus></textarea>
							</div>
							<div class="chat-input-btnbar">
								<button class="pull-right" id="sendMyMsg" onclick="sendText()">发送</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="menu-pannel-pop" id="groupSettingPop">
   				<div class="header text-center">
   					<span class="pop-title">标题</span>
   					<span class="pull-right light-grey close-btn">×</span>
   				</div>
   				<div class="body">
   					<div class="body-content">
   					
   					</div>
   				</div>
   			</div>
		</div>
	</div>
</body>

    <!--Basic Scripts-->
    <script src="basic/js/html5.js"></script>
    <script src="basic/js/jquery-2.0.3.min.js"></script>
    <script src="basic/js/jquery-migrate-1.1.0.js"></script>
    <script src="basic/js/bootstrap.min.js"></script>
    <script src="basic/js/toastr/toastr.js"></script>
    <script src="basic/js/jquery.slimscroll.min.js"></script>
    <script src="basic/js/bootstrap-switch.min.js"></script>
    
    <!-- tmpl模板插件(jquery File Upload使用) -->
	<script src="plug-in/jquery-fileupload/tmpl.min.js"></script>
	<script src="plug-in/jquery-fileupload/load-image.min.js"></script>
	<script src="plug-in/jquery-fileupload/canvas-to-blob.min.js"></script>
	<script src="plug-in/jquery-fileupload/vendor/jquery.ui.widget.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.iframe-transport.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload-process.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload-image.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload-audio.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload-video.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload-validate.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload-ui.js"></script>
	<script src="plug-in/jquery-fileupload/jquery.fileupload-custom.js"></script>
	<script type="text/javascript" src="basic/js/jquery.twbsPagination.min.js"></script>
	<!-- h5验证文件 -->
	<script type="text/javascript" src="basic/js/html5Validate/jquery-html5Validate.js"></script>
    <!-------------------------------------- 基础类型扩展、方法扩展  -->
    <script src="plug-in/tools/uuid.js"></script>
    <script src="plug-in/tools/Map.js"></script>
	<script src="plug-in/tools/syUtil.js"></script>
	<script src="plug-in/tools/curdtools.js"></script>
	<script src="plug-in/tools/native-support.js"></script>
	
	<script src="plug-in/easemob/sdk/strophe.js"></script>
	<script src="plug-in/easemob/sdk/json2.js"></script>
	<script src="plug-in/easemob/sdk/easemob.im-1.0.7.js"></script>
	<script src="plug-in/uploadifive-v1.2.2-standard/jquery.uploadifive.js"></script>
	
	<!-------------------------------------------------------- iscroll 脚本库-->
	<script src="basic/js/iscroll/iscroll-probe.js"></script>
	<script src="basic/js/iscroll/iscrollAssist.js"></script>
	
    <!--[if lt IE 9]>
	  <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
	  <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
	  <script src="plug-in/easemob/sdk/jplayer/jquery.jplayer.min.js"></script>
	  <script src="plug-in/easemob/sdk/swfupload/swfupload.js"></script>
	<![endif]-->
    <script src="basic/js/home.js"></script>
    
    
	<script type="text/javascript">
		/***************** 环信IM方法相关 begin *****************/
		Easemob.im.config = {
			xmppURL : 'https://im-api.easemob.com/http-bind/',
			apiURL : '',
			appkey : "bangbangbang#saifu",
			https : true
		};

		var curUserId = null; //当前登录用户Id
		var curChatUserId = null;
		var conn = null;//环信连接对象
		var curRoomId = null;//当前聊天室Id
		var msgCardDivId = "chat-content";
		var talkToDivId = "talkTo";
		var talkInputId = "talkInputId";
		var fileInputId = "fileInput";//选择文件的模态框
		var bothRoster = []; //双方互为好友+我申请加对方(对方没加我)的集合
		var toRoster = [];//对方申请加我(我没加对方)的集合
		var maxWidth = 200;
		var groupFlagMark = "group--";
		var groupQuering = false;
		var textSending = false; //上一次输入正在发送的标识,防止连续发送
		var time = 0;

		var swfupload = null;
		var flashFilename = '';
		var audioDom = [];

		var recentUl = $("#im-recent-ul");
		var recentDiv = $("#im-recent-div");
		var contactCache = new Map();

		var pictype = {
			"jpg" : true,
			"gif" : true,
			"png" : true,
			"bmp" : true
		};

		var audtype = {
			"mp3" : true,
			"wma" : true,
			"wav" : true,
			"amr" : true,
			"avi" : true
		};
		var login = function(userId, password) {
			conn.open({
				apiUrl : Easemob.im.config.apiURL,
				user : userId,
				pwd : password,
				//连接时提供appkey
				appKey : Easemob.im.config.appkey
			});
		};

		//处理不支持异步上传的浏览器,使用swfupload作为解决方案
		var uploadType = null;
		var uploadShim = function(fileInputId) {
			var pageTitle = document.title;
			var uploadBtn = $('#' + fileInputId);
			if (typeof SWFUpload === 'undefined' || uploadBtn.length < 1)
				return;

			return new SWFUpload({
				file_post_name : 'file',
				flash_url : "sdk/swfupload/swfupload.swf",
				button_placeholder_id : fileInputId,
				button_width : uploadBtn.width() || 120,
				button_height : uploadBtn.height() || 30,
				button_cursor : SWFUpload.CURSOR.HAND,
				button_text : '点击上传',
				button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
				file_size_limit : 10485760,
				file_upload_limit : 0,
				file_queued_handler : function(file) {
					if (this.getStats().files_queued > 1) {
						this.cancelUpload();
					}
					var checkType = uploadType === 'audio' ? audtype : pictype;
					if (!checkType[file.type.slice(1)]) {
						conn.onError({
							type : EASEMOB_IM_UPLOADFILE_ERROR,
							msg : '不支持此文件类型' + file.type
						});
						this.setButtonText('点击上传');
						this.cancelUpload();
					} else if (10485760 < file.size) {
						conn.onError({
							type : EASEMOB_IM_UPLOADFILE_ERROR,
							msg : '文件大小超过限制！请上传大小不超过10M的文件'
						});
						this.setButtonText('点击上传');
						this.cancelUpload();
					} else {
						this.setButtonText(file.name);
						flashFilename = file.name;
					}
				},
				file_dialog_start_handler : function() {
					if (Easemob.im.Helper.getIEVersion() < 10) {
						document.title = pageTitle;
					}
				},
				upload_error_handler : function(file, code, msg) {
					if (code != SWFUpload.UPLOAD_ERROR.FILE_CANCELLED && code != SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED
							&& code != SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED) {
						this.uploadOptions.onFileUploadError && this.uploadOptions.onFileUploadError({
							type : EASEMOB_IM_UPLOADFILE_ERROR,
							msg : msg
						});
					}
				},
				upload_complete_handler : function() {
					this.setButtonText('点击上传');
				},
				upload_success_handler : function(file, response) {
					//处理上传成功的回调
					try {
						var res = Easemob.im.Helper.parseUploadResponse(response);
						res = $.parseJSON(res);
						res.filename = file.name;
						this.uploadOptions.onFileUploadComplete && this.uploadOptions.onFileUploadComplete(res);
					} catch (e) {
						conn.onError({
							type : EASEMOB_IM_UPLOADFILE_ERROR,
							msg : '上传图片发生错误'
						});
					}
				}
			});
		};
		//处理浏览器版本与https
		var handleConfig = function() {
			if (Easemob.im.Helper.getIEVersion() < 10) {
				Easemob.im.config.https = location.protocol == 'https:' ? true : false;
				if (!Easemob.im.config.https) {
					if (Easemob.im.config.xmppURL.indexOf('https') == 0) {
						Easemob.im.config.xmppURL = Easemob.im.config.xmppURL.replace(/^https/, 'http');
					}
					if (Easemob.im.config.apiURL.indexOf('https') == 0) {
						Easemob.im.config.apiURL = Easemob.im.config.apiURL.replace(/^https/, 'http');
					}
				} else {
					if (Easemob.im.config.xmppURL.indexOf('https') != 0) {
						Easemob.im.config.xmppURL = Easemob.im.config.xmppURL.replace(/^http/, 'https');
					}
					if (Easemob.im.config.apiURL.indexOf('https') != 0) {
						Easemob.im.config.apiURL = Easemob.im.config.apiURL.replace(/^http/, 'https');
					}
				}
			}
		};
		//处理连接时函数,主要是登录成功后对页面元素做处理
		var handleOpen = function(conn) {
			//从连接中获取到当前的登录人注册帐号名
			curUserId = conn.context.userId;
			//将登陆人的信息也加入本地缓存(发送时要用到)
			var obj = new Object();
			obj.id = curUserId;
			obj.portrait = user_portrait;
			obj.name = user_name;
			contactCache.put(curUserId, obj);
			//获取当前登录人的联系人列表
			conn.getRoster({
				success : function(roster) {
					// 页面处理
					//             hiddenWaitLoginedUI();
					var curroster;
					//处理获得所有类型的联系人
					for ( var i in roster) {
						var ros = roster[i];
						//both为双方互为好友，要显示的联系人,from我是对方的单向好友
						if (ros.subscription == 'both' || ros.subscription == 'from') {
							bothRoster.push(ros);
						} else if (ros.subscription == 'to') {
							//to表明了联系人是我的单向好友
							toRoster.push(ros);
						}
					}
					if (bothRoster.length > 0) {
						curroster = bothRoster[0];
						var ids = "";
						$.each(bothRoster, function(i, e) {
							ids += e.name + ",";
							ids.removeDot();
						});
						ajax("userController.do?getUsersInfo&ids=" + ids, function(data) {
							$.each(data.obj, function(i, e) {
								var obj = new Object();
								obj.id = e.id;
								obj.phone = e.phone;
								obj.portrait = e.portrait;
								obj.name = e.name;
								contactCache.put(e.id, obj);
								for (var i = 0; i < bothRoster.length; i++) {
									if (bothRoster[i].name == e.id) {
										bothRoster[i].phone = e.phone;
										bothRoster[i].portrait = e.portrait;
										bothRoster[i].username = e.name;
									}
								}
							});
						});
						buildContactDiv(bothRoster);//联系人列表页面处理
						if (curroster)
							setCurrentContact(curroster.name);//页面处理将第一个联系人作为当前聊天div
					}
					//获取当前登录人的群组列表
					conn.listRooms({
						success : function(rooms) {
							if (rooms && rooms.length > 0) {
								//buildListRoomDiv(rooms);//群组列表页面处理
								//if (curChatUserId == null) {
								//	setCurrentContact(groupFlagMark + rooms[0].roomId);
								//}
							}
							conn.setPresence();//设置用户上线状态，必须调用
						},
						error : function(e) {
						}
					});
				}
			});
			//启动心跳
			if (conn.isOpened()) {
				conn.heartBeat(conn);
			}
		};
		//连接中断时的处理，主要是对页面进行处理
		var handleClosed = function() {
			curUserId = null;
			curChatUserId = null;
			curRoomId = null;
			bothRoster = [];
			toRoster = [];
			hiddenChatUI();
			for (var i = 0, l = audioDom.length; i < l; i++) {
				if (audioDom[i].jPlayer)
					audioDom[i].jPlayer('destroy');
			}
			clearContactUI("contactlistUL", "contactgrouplistUL", "momogrouplistUL", msgCardDivId);
			//showLoginUI();
			groupQuering = false;
			textSending = false;
		};

		//收到不在列表消息时,追加联系人项
		var createContactLi = function(to, message, chattype) {
			var lielem;
			message=nulls(message);
			if (chattype && chattype == 'groupchat') {
				var roomId = to;
				var roomsName;
				var portraits = [];
				//处理头像和名称问题,若本地没缓存才请求服务器获取
				if (contactCache.containsKey(to)) {
					roomsName = contactCache.get(to).name;
					portraits = contactCache.get(to).portrait;
				} else {
					ajax("orgGroupController.do?queryGroupPortrait&groupId=" + roomId, function(data) {
						roomsName = data.obj.name;
						var obj = new Object();
						obj.id = roomId;
						obj.name = roomsName;
						$.each(data.obj.portraits, function(i, e) {
							portraits[i] = e.portrait;
						});
						obj.portrait = portraits;
						contactCache.put(roomId, obj);
					});
				}
				//创建群聊联系人项
				var lielem = $('<li><div class="avatar without-bubble group" ></div>' + '<div class="info"><span class="text-overflow username">' + roomsName
						+ '</span> <span class="pull-right light-grey rec-time">' + getLocalTimeString() + '</span></div>'
						+ '<div class="info"><span class="light-grey text-overflow detail">' + message
						+ '</span> <span class="pull-right light-grey type"></span></div>' + '</li>');

				for (var i = 0; i < portraits.length; i++) {
					var img;
					//var portrait = (portraits[i]=null) ? '${attachForeRequest}' + portraits[i] : 'basic/img/avatars/avatar_80.png';
					var portrait = portraits[i] ? '${attachForeRequest}' + portraits[i] : 'basic/img/avatars/avatar_80.png';
					img = $('<img>').attr('src', portrait);
					$('.avatar', lielem).prepend(img);
				}
				if (portraits.length == 3) {
					$('.avatar', lielem).addClass("three");
				}
				
				lielem.attr({
					'id' : groupFlagMark + roomId,
					'class' : 'offline',
					'className' : 'offline',
					'type' : 'groupchat',
					'displayName' : roomsName,
					'roomId' : roomId,
					'joined' : 'false'
				}).click(function() {
					chooseContactDivClick($(this));
				});
			} else {
				var username;
				var portrait;
				//处理头像和名称问题,若本地没缓存才请求服务器获取
				if (contactCache.containsKey(to)) {
					username = contactCache.get(to).name;
					portrait = contactCache.get(to).portrait;
				} else {
					ajax("userController.do?getUserInfo&id=" + to, function(data) {
						var obj = new Object();
						obj.id = data.obj.id;
						obj.phone = data.obj.phone;
						obj.portrait = data.obj.portrait;
						obj.name = data.obj.name;
						contactCache.put(e.id, obj);

						username = data.obj.name;
						portrait = data.obj.portrait;
					});
				}

				//创建单聊联系人项
				lielem = $('<li><div class="avatar without-bubble" ><img src="'+portrait+'" ></div>' + '<div class="info"><span class="text-overflow username">'
						+ username + '</span> <span class="pull-right light-grey rec-time">' + getLocalTimeString() + '</span></div>'
						+ '<div class="info"><span class="light-grey text-overflow detail">' + message
						+ '</span> <span class="pull-right light-grey type"></span></div>' + '</li>');

				lielem.attr({
					'id' : to,
					'class' : 'offline',
					'className' : 'offline',
					'type' : 'chat',
					'displayName' : username
				}).click(function() {
					chooseContactDivClick($(this));
				});
			}
			recentUl.prepend(lielem);
		}

		//构造联系人列表
		var buildContactDiv = function(roster) {
			//清空之前的会话
			recentUl.empty();
			var cache = {};
			for (i = 0; i < roster.length; i++) {
				//jid还不是环信Id,需要处理获得userName
				var jid = roster[i].jid;
				var username = roster[i].username;
				var portrait = roster[i].portrait ? '${attachForeRequest}' + roster[i].portrait : 'basic/img/avatars/avatar_80.png';
				var userId = jid.substring(jid.indexOf("_") + 1).split("@")[0];
				if (userId in cache) {
					continue;
				}
				cache[userId] = true;
				var lielem = $('<li><div class="avatar without-bubble" ><img src="'+portrait+'" ></div>'
						+ '<div class="info"><span class="text-overflow username">' + username
						+ '</span> <span class="pull-right light-grey rec-time"></span></div>'
						+ '<div class="info"><span class="light-grey text-overflow detail"></span> <span class="pull-right light-grey type"></span></div>'
						+ '</li>');

				lielem.attr({
					'id' : userId,
					'class' : 'offline',
					'className' : 'offline',
					'type' : 'chat',
					'displayName' : username
				}).click(function() {
					chooseContactDivClick($(this));
				});
				recentUl.append(lielem);
			}
		};

		//构造群组列表
		var buildListRoomDiv = function(rooms) {
			var cache = {};
			for (var i = 0; i < rooms.length; i++) {
				var portraits = [];
				var roomsName = rooms[i].name;
				var roomId = rooms[i].roomId;
				ajax("orgGroupController.do?queryGroupPortrait&groupId=" + roomId, function(data) {
					var obj = new Object();
					obj.id = roomId;
					obj.name = roomsName;
					$.each(data.obj.portraits, function(i, e) {
						portraits[i] = e.portrait;
					});
					obj.portrait = portraits;
					contactCache.put(roomId, obj);
				});
				if (roomId in cache) {
					continue;
				}
				cache[roomId] = true;
				var lielem = $('<li><div class="avatar without-bubble group" ></div><div class="info"><span class="text-overflow username">' + roomsName
						+ '</span> <span class="pull-right light-grey rec-time"></span></div>'
						+ '<div class="info"><span class="light-grey text-overflow detail"></span> <span class="pull-right light-grey type"></span></div>'
						+ '</li>');

				for (var j = 0; j < portraits.length; j++) {
					var img;
					//var portrait = (portraits[j]=null) ? '${attachForeRequest}' + portraits[j] : 'basic/img/avatars/avatar_80.png';
					var portrait = portraits[j] ? '${attachForeRequest}' + portraits[j] : 'basic/img/avatars/avatar_80.png';
					img = $('<img>').attr('src', portrait);
					$('.avatar', lielem).prepend(img);
				}

				if (portraits.length == 3) {
					$('.avatar', lielem).addClass("three");
				}
				lielem.attr({
					'id' : groupFlagMark + roomId,
					'class' : 'offline',
					'className' : 'offline',
					'type' : 'groupchat',
					'displayName' : roomsName,
					'roomId' : roomId,
					'joined' : 'false'
				}).click(function() {
					chooseContactDivClick($(this));
				});
				recentUl.append(lielem);
			}
		};

		//设置当前显示的聊天窗口div，如果有联系人则默认选中联系人中的第一个联系人，如没有联系人则当前div为null-nouser
		var setCurrentContact = function(defaultUserId) {
			var contactLi = getContactLi(defaultUserId);
			if (!contactLi[0]) {
				if(defaultUserId.indexOf(groupFlagMark)!=-1){
					//群组
					var groupId=defaultUserId.split(groupFlagMark)[1];
					//创建联系
					createContactLi(groupId,null,"groupchat");
				}else{
					//单聊
				}
			}
			showContactChatDiv(defaultUserId);
			if (curChatUserId != null) {
				hiddenContactChatDiv(curChatUserId);
			} else {
				$('#null-nouser').hide();
			}
			curChatUserId = defaultUserId;
		};

		//显示当前选中联系人的聊天窗口div，并将该联系人在联系人列表中背景色置为蓝色
		var showContactChatDiv = function(chatUserId) {
			var contentDiv = getContactChatDiv(chatUserId);
			if (!contentDiv[0]) {
				contentDiv = createContactChatDiv(chatUserId);
				$("#" + msgCardDivId).append(contentDiv);
			}
			contentDiv.css("display", "block");
			var contactLi = getContactLi(chatUserId);
			if (!contactLi[0]) {
				return;
			}
			contactLi.css("background-color", "#efefef");
			var dispalyTitle = null;//聊天窗口显示当前对话人名称
			if (chatUserId.indexOf(groupFlagMark) >= 0) {
				//如果是群组聊天
				curRoomId = $(contactLi).attr('roomid');
			} else {
				//如果是单聊
			}
		};

		//对上一个联系人的聊天窗口div做隐藏处理，并将联系人列表中选择的联系人背景色置为未选择
		var hiddenContactChatDiv = function(chatUserId) {
			var contactLi = $("#" + chatUserId);
			if (contactLi) {
				contactLi.css("background-color", "");
			}
			var contentDiv = getContactChatDiv(chatUserId);
			if (contentDiv) {
				contentDiv.hide();
			}
		};

		//获得当前聊天记录的窗口div
		var getContactChatDiv = function(chatUserId) {
			return $("#" + curUserId + "-" + chatUserId);
		};

		//如果当前没有某一个联系人的聊天窗口div就新建一个
		var createContactChatDiv = function(chatUserId) {
			var msgContentDivId = curUserId + "-" + chatUserId;
			var newContent = $("<div></div>");
			newContent.attr({
				"id" : msgContentDivId,
				"class" : "chat-main",
				"className" : "chat-main",
				"style" : "display:none"
			});
			return newContent;
		};

		//点击切换联系人聊天窗口div
		var chooseContactDivClick = function(li) {
			var chatUserId = li.attr("id");
			//? 若不在群组里,则加入群组
			if (li.attr("type") == 'groupchat' && ('true' != $(li).attr("joined"))) {
				conn.join({
					roomId : li.attr("roomId")
				});
				li.attr("joined", "true");
			}
			if (chatUserId != curChatUserId) {
				if (curChatUserId == null) {
					showContactChatDiv(chatUserId);
				} else {
					showContactChatDiv(chatUserId);
					hiddenContactChatDiv(curChatUserId);
				}
				curChatUserId = chatUserId;
			}
			//对默认的null-nouser div进行处理,走的这里说明联系人列表肯定不为空所以对默认的聊天div进行处理
			$('#null-nouser').css({
				"display" : "none"
			});
			//点击后,隐藏掉badge,并重置读数
			var badge = $(li).children(".avatar");
			badge.removeAttr("data-num");
			badge.addClass("without-bubble");
		};

		//获取联系人列表的某项
		var getContactLi = function(chatUserId) {
			return $("#" + chatUserId, recentUl);
		};

		//判断要操作的联系人和当前联系人列表的关系
		var contains = function(roster, contact) {
			var i = roster.length;
			while (i--) {
				if (roster[i].name === contact.name) {
					return true;
				}
			}
			return false;
		};

		//将文件对象处理为本地url
		var getObjectURL = function(file) {
			var url = null;
			if (window.createObjectURL != undefined) { // basic
				url = window.createObjectURL(file);
			} else if (window.URL != undefined) { // mozilla(firefox)
				url = window.URL.createObjectURL(file);
			} else if (window.webkitURL != undefined) { // webkit or chrome
				url = window.webkitURL.createObjectURL(file);
			}
			return url;
		};

		//修改会话列表名称(适用于改名情况)
		var changeContactLiName = function(id, groupName) {
			var li = $("#" + id);
			if (li[0]) {
				$(".username", li).html(groupName);
			}
		};

		//追加聊天信息到聊天界面
		var appendMsg = function(from, contact, message, chattype) {
			var contactDivId = contact;
			if (chattype && chattype == 'groupchat') {
				contactDivId = groupFlagMark + contact;
			}

			var contactLi = getContactLi(contactDivId);
			if (!contactLi[0]) {
				//若当前联系人还不存在
				createContactLi(contact, message, chattype);
			} else {
				//若存在,则显示最近一条消息
				contactLi.find(".detail").html(message);
				//修改到达时间
				contactLi.find(".rec-time").html(getLocalTimeString());
				//并且置顶
				contactLi.prependTo(recentUl);
			}
			// 消息体 {isemotion:true;body:[{type:txt,msg:ssss}{type:emotion,msg:imgdata}]}
			var localMsg = null;
			if (typeof message == 'string') {
				localMsg = Easemob.im.Helper.parseTextMessage(message);
				localMsg = localMsg.body;
			} else {
				localMsg = message.data;
			}

			var messageContent = localMsg;
			var lineDiv = $('<div class="chat-item"></div>');
			var username,portrait;
			if (contactCache.containsKey(from)) {
				//如果当前人存在的话
				username = contactCache.get(from).name;
				portrait = contactCache.get(from).portrait;
			} else {
				//如果当前人不存在的话
				ajax("userController.do?getUserInfo&id=" + from, function(data) {
					var obj = new Object();
					obj.id = data.obj.id;
					obj.phone = data.obj.phone;
					obj.portrait = data.obj.portrait;
					obj.name = data.obj.name;
					contactCache.put(from, obj);

					username = data.obj.name;
					portrait = data.obj.portrait;
				});
			}
			
			portrait = portrait ? '${attachForeRequest}' + portrait : 'basic/img/avatars/avatar_80.png';
			var nameTimeDiv = $('<div class="chat-profile clearfix"><span class="name">' + name + '</span><span class="chat-time">' + getLocalTimeString()
					+ '</span></div>');
			var clearfixDiv = $('<div class="clearfix"></div>');
			var imgDiv = $('<div class="avatar"><img src="'+portrait+'" ></div>');
			var bubbleDiv = $('<div class="msg-bubble-box"><pre></pre></div>');
			clearfixDiv.append(imgDiv);
			for (var i = 0; i < messageContent.length; i++) {
				var msg = messageContent[i];
				var type = msg.type;
				var data = msg.data;

				if (type == "emotion") {
					var ele = $('<img src="' + data + '"/>');
					bubbleDiv.append(ele);
					clearfixDiv.append(bubbleDiv);
				} else if (type == "pic" || type == 'audio' || type == 'video') {
					//TODO 文件类型的支持后,要显示文件名,图片和音视频不需要显示
					var filename = msg.filename;
					bubbleDiv.append(data);
					data.nodeType && clearfixDiv.append(bubbleDiv);
					if (type == 'audio' && msg.audioShim) {
						var d = $(lineDiv), t = new Date().getTime();
						d
								.append($('<div class="'+t+'"></div>\
	                    <button class="play'+t+'">播放</button><button style="display:none" class="play'+t+'">暂停</button>'));
					}
				} else {
					//文本消息
					var ele = data;
					bubbleDiv.append(ele);
					clearfixDiv.append(bubbleDiv);
				}
			}
			if (curUserId == from) {
				lineDiv.addClass("me");
			} else {
				lineDiv.addClass("not-me");
			}
			lineDiv.append(nameTimeDiv).append(clearfixDiv);
			//若当前没有聊天页面,则打开最近接收的这条
			if (curChatUserId == null && chattype == null) {
				setCurrentContact(contact);
			}
			//到达消息的对象非当前聊天对象时
			if (curChatUserId && curChatUserId.indexOf(contact) < 0) {
				//处理未读badge
				var contactLi = getContactLi(contactDivId);
				var badge = contactLi.children(".avatar");
				badge.removeClass("without-bubble");
				var count = nullsReplace(badge.attr("data-num"), 0);
				var myNum = new Number(count);
				myNum++;
				badge.attr("data-num", myNum);
			}

			//处理与此人/群的聊天框
			var contentDiv = getContactChatDiv(contactDivId);
			if (!contentDiv[0]) {
				contentDiv = createContactChatDiv(contactDivId);
				$("#" + msgCardDivId).append(contentDiv);
			}
			//加入本次消息
			contentDiv.append(lineDiv);
			//处理音频
			if (type == 'audio' && msg.audioShim) {
				setTimeout(function() {
					playAudioShim(d.find('.' + t), data.currentSrc, t);
				}, 0);
			}
			//将聊天框滚动到最下方
			contentDiv.scrollTop = contentDiv.scrollHeight;
			return lineDiv;
		};

		//上传触发接口
		var flashUpload = function(url, options) {
			if (swfupload.settings.button_text == '点击上传') {
				conn.onError({
					type : EASEMOB_IM_UPLOADFILE_ERROR,
					msg : '请选择文件'
				});
				return;
			}
			swfupload.setUploadURL(url);
			swfupload.uploadOptions = options;
			swfupload.startUpload();
		}

		//发送各文件类型时调用方法
		var sendFile = function() {
			var to = curChatUserId;
			if (to == null) {
				return;
			}
			// Easemob.im.Helper.getFileUrl为easemobwebim-sdk获取发送文件对象的方法，fileInputId为 input 标签的id值
			var fileObj = Easemob.im.Helper.getFileUrl(fileInputId);
			if (Easemob.im.Helper.isCanUploadFileAsync && (fileObj.url == null || fileObj.url == '')) {
				return;
			}
			var filetype = fileObj.filetype;
			var filename = fileObj.filename;
			if (filetype in pictype) {
				//若上传的是图片类型
				var opt = {
					type : 'chat',
					fileInputId : fileInputId,
					filename : flashFilename || '',
					to : to,
					apiUrl : Easemob.im.config.apiURL,
					onFileUploadError : function(error) {
						var messageContent = (error.msg || '') + ",发送图片文件失败:" + (filename || flashFilename);
						appendMsg(curUserId, to, messageContent);
					},
					onFileUploadComplete : function(data) {
						var file = document.getElementById(fileInputId);
						if (file && file.files) {
							var objUrl = getObjectURL(file.files[0]);
							if (objUrl) {
								var img = document.createElement("img");
								img.src = objUrl;
								img.width = maxWidth;
							}
						} else {
							//若因为某些原因 ,没有上传的控件了(type="file"),那么就取上传成功返回的数据来追加页面
							filename = data.filename || '';
							var img = document.createElement("img");
							img.src = data.uri + '/' + data.entities[0].uuid + '?token=';
							img.width = maxWidth;
						}
						appendMsg(curUserId, to, {
							data : [ {
								type : 'pic',
								filename : (filename || flashFilename),
								data : img
							} ]
						});
					},
					flashUpload : flashUpload
				};
				if (curChatUserId.indexOf(groupFlagMark) >= 0) {
					opt.type = 'groupchat';
					opt.to = curRoomId;
				}
				conn.sendPicture(opt);
				return;
			} else if (filetype in audtype) {
				//若上传的是音视频类型
				var opt = {
					type : "chat",
					fileInputId : fileInputId,
					filename : flashFilename || '',
					to : to,//发给谁
					apiUrl : Easemob.im.config.apiURL,
					onFileUploadError : function(error) {
						var messageContent = (error.msg || '') + ",发送音频失败:" + (filename || flashFilename);
						appendMsg(curUserId, to, messageContent);
					},
					onFileUploadComplete : function(data) {
						var file = document.getElementById(fileInputId);
						var objectURL = getObjectURL(file.files[0]);
						if (objectURL) {
							var audio = document.createElement("audio");
							if (Easemob.im.Helper.getIEVersion() != 9 && ("src" in audio) && ("controls" in audio)) {
								audio.onload = function() {
									audio.onload = null;
									//加载成功了就要释放资源
									window.URL && window.URL.revokeObjectURL && window.URL.revokeObjectURL(audio.src);
								};
								audio.onerror = function() {
									audio.onerror = null;
									appendMsg(from, contactDivId, "当前浏览器不支持播放此音频:" + (filename || ''));
								};
								audio.controls = "controls";
								audio.src = objectURL;
								appendMsg(curUserId, to, {
									data : [ {
										type : 'audio',
										filename : filename || '',
										data : audio
									} ]
								});
							} else {
								//低版本浏览器
								appendMsg(curUserId, to, {
									data : [ {
										type : 'audio',
										filename : filename || '',
										data : {
											currentSrc : objectURL
										},
										audioShim : true
									} ]
								});
							}
						}

						// 					var messageContent = "发送音频" + (filename || flashFilename);
						// 					appendMsg(curUserId, to, messageContent);
					},
					flashUpload : flashUpload
				};
				//构造完opt对象后调用easemobwebim-sdk中发送音频的方法
				if (curChatUserId.indexOf(groupFlagMark) >= 0) {
					opt.type = 'groupchat';
					opt.to = curRoomId;
				}
				conn.sendAudio(opt);
				return;
			} else {
				alert("不支持的文件类型");
			}
		};

		var sendText = function() {
			//上一次输入正在发送的标识,防止连续发送
			if (textSending) {
				return;
			}
			textSending = true;
			var msgInput = $("#" + talkInputId);
			var msg = msgInput.val();
			//防止没输入内容
			if (msg == null || msg.length == 0) {
				return;
			}
			var to = curChatUserId;
			//发给当前聊天框的对象
			if (to == null) {
				return;
			}
			var options = {
				to : to,
				msg : msg,
				type : "chat"
			};
			// 发送群组消息
			if (curChatUserId.indexOf(groupFlagMark) >= 0) {
				//需要加多参数
				options.type = 'groupchat';
				options.to = curRoomId;
			}
			conn.sendTextMessage(options);
			//当前登录人发送的信息在聊天窗口中原样显示(处理回车等情况)
			var msgtext = msg.replace(/\n/g, '<br>');
			appendMsg(curUserId, to, msgtext);
			//发送时关闭表情框
			//     turnoffFaces_box();
			msgInput.val("");
			msgInput.focus();
			//控制发送消息的频率
			setTimeout(function() {
				textSending = false;
			}, 1000);
		};

		//获得本地当前时间
		var getLocalTimeString = function() {
			var date = new Date();
			var time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
			return time;
		}

		//初始化表情
		var initEmotion = function() {
			var sjson = Easemob.im.Helper.EmotionPicData;
			for ( var key in sjson) {
				var emotions = $('<img>').attr({
					"id" : key,
					"src" : sjson[key],
					"style" : "cursor:pointer;"
				}).click(function() {
					var txt = document.getElementById(talkInputId);
					txt.value = txt.value + this.id;
					txt.focus();
				});
				$('<li>').append(emotions).appendTo($('#emotionUL'));
			}
		}

		//收到文本消息的回调方法的实现
		var handleTextMessage = function(message) {
			var from = message.from;//消息的发送者
			var mestype = message.type;//消息发送的类型是群组消息还是个人消息
			var messageContent = message.data;//文本消息体
			//TODO  根据消息体的to值去定位那个群组的聊天记录
			var room = message.to;
			if (mestype == 'groupchat') {
				appendMsg(message.from, message.to, messageContent, mestype);
			} else {
				//为何是双from,因为接收到A的消息也要放到A的聊天界面里
				appendMsg(from, from, messageContent);
			}
		};

		//收到表情消息的回调方法的实现，message为表情符号和文本的消息对象，文本和表情符号sdk中做了统一的处理，不需要用户自己区别字符是文本还是表情符号。
		var handleEmotion = function(message) {
			var from = message.from;
			var room = message.to;
			var mestype = message.type;//消息发送的类型是群组消息还是个人消息
			if (mestype == 'groupchat') {
				appendMsg(message.from, message.to, message, mestype);
			} else {
				appendMsg(from, from, message);
			}
		};

		//收到图片消息的回调方法的实现
		var handlePictureMessage = function(message) {
			var filename = message.filename;//文件名称，带文件扩展名
			var from = message.from;//文件的发送者
			var mestype = message.type;//消息发送的类型是群组消息还是个人消息
			var contactDivId = from;
			if (mestype == 'groupchat') {
				contactDivId = groupFlagMark + message.to;
			}
			var options = message;
			// 图片消息下载成功后的处理逻辑
			options.onFileDownloadComplete = function(response, xhr) {
				var objectURL = Easemob.im.Helper.parseDownloadResponse.call(this, response);
				img = document.createElement("img");
				img.onload = function(e) {
					img.onload = null;
					window.URL && window.URL.revokeObjectURL && window.URL.revokeObjectURL(img.src);
				};
				img.onerror = function() {
					img.onerror = null;
					if (typeof FileReader == 'undefined') {
						img.alter = "当前浏览器不支持blob方式";
						return;
					}
					img.onerror = function() {
						img.alter = "当前浏览器不支持blob方式";
					};
					var reader = new FileReader();
					reader.onload = function(event) {
						img.src = this.result;
					};
					reader.readAsDataURL(response);
				}
				img.src = objectURL;
				var pic_real_width = options.width;
				if (!pic_real_width || pic_real_width == 0) {
					$("<img/>").attr("src", objectURL).load(function() {
						pic_real_width = this.width;
						if (pic_real_width > maxWidth) {
							img.width = maxWidth;
						} else {
							img.width = pic_real_width;
						}
						appendMsg(from, contactDivId, {
							data : [ {
								type : 'pic',
								filename : filename || '',
								data : img
							} ]
						});
					});
				} else {
					if (pic_real_width > maxWidth) {
						img.width = maxWidth;
					} else {
						img.width = pic_real_width;
					}
					appendMsg(from, contactDivId, {
						data : [ {
							type : 'pic',
							filename : filename || '',
							data : img
						} ]
					});
				}
			};

			var redownLoadFileNum = 0;
			options.onFileDownloadError = function(e) {
				//下载失败时只重新下载一次
				if (redownLoadFileNum < 1) {
					redownLoadFileNum++;
					options.accessToken = options_c;
					Easemob.im.Helper.download(options);

				} else {
					appendMsg(from, contactDivId, e.msg + ",下载图片" + filename + "失败");
					redownLoadFileNum = 0;
				}

			};
			//下载文件对象的统一处理方法。
			Easemob.im.Helper.download(options);
		};

		//收到音频消息回调方法的实现
		var handleAudioMessage = function(message) {
			var filename = message.filename;
			var filetype = message.filetype;
			var from = message.from;
			var mestype = message.type;//消息发送的类型是群组消息还是个人消息
			var contactDivId = from;
			if (mestype == 'groupchat') {
				contactDivId = groupFlagMark + message.to;
			}
			var options = message;
			options.onFileDownloadComplete = function(response, xhr) {
				var objectURL = Easemob.im.Helper.parseDownloadResponse.call(this, response);
				var audio = document.createElement("audio");
				if (Easemob.im.Helper.getIEVersion() != 9 && ("src" in audio) && ("controls" in audio)) {
					audio.onload = function() {
						audio.onload = null;
						//加载成功了就要释放资源
						window.URL && window.URL.revokeObjectURL && window.URL.revokeObjectURL(audio.src);
					};
					audio.onerror = function() {
						audio.onerror = null;
						appendMsg(from, contactDivId, "当前浏览器不支持播放此音频:" + (filename || ''));
					};
					audio.controls = "controls";
					audio.src = objectURL;
					appendMsg(from, contactDivId, {
						data : [ {
							type : 'audio',
							filename : filename || '',
							data : audio
						} ]
					});
					//audio.play();
					return;
				} else {
					//低版本浏览器
					appendMsg(from, contactDivId, {
						data : [ {
							type : 'audio',
							filename : filename || '',
							data : {
								currentSrc : objectURL
							},
							audioShim : true
						} ]
					});
				}
			};
			options.onFileDownloadError = function(e) {
				appendMsg(from, contactDivId, e.msg + ",下载音频" + filename + "失败");
			};
			options.headers = {
				"Accept" : "audio/mp3"
			};
			Easemob.im.Helper.download(options);
		};

		// //处理出席状态操作
		// var handleRoster = function(rosterMsg) {
		//     for (var i = 0; i < rosterMsg.length; i++) {
		//         var contact = rosterMsg[i];
		//         if (contact.ask && contact.ask == 'subscribe') {
		//             continue;
		//         }
		//         if (contact.subscription == 'to') {
		//             toRoster.push({
		//                 name : contact.name,
		//                 jid : contact.jid,
		//                 subscription : "to"
		//             });
		//         }
		//         //app端删除好友后web端要同时判断状态from做删除对方的操作
		//         if (contact.subscription == 'from') {
		//             toRoster.push({
		//                 name : contact.name,
		//                 jid : contact.jid,
		//                 subscription : "from"
		//             });
		//         }
		//         if (contact.subscription == 'both') {
		//             var isexist = contains(bothRoster, contact);
		//             if (!isexist) {
		//                 var lielem = $('<li>').attr({
		//                     "id" : contact.name,
		//                     "class" : "offline",
		//                     "className" : "offline"
		//                 }).click(function() {
		//                     chooseContactDivClick(this);
		//                 });
		//                 $('<img>').attr({
		//                     "src" : "img/head/contact_normal.png"
		//                 }).appendTo(lielem);
		//                 $('<span>').html(contact.name).appendTo(lielem);
		//                 $('#contactlistUL').append(lielem);
		//                 bothRoster.push(contact);
		//             }
		//         }
		//         if (contact.subscription == 'remove') {
		//             var isexist = contains(bothRoster, contact);
		//             if (isexist) {
		//                 removeFriendDomElement(contact.name);
		//             }
		//         }
		//     }
		// };

		//收到联系人订阅请求的处理方法，具体的type值所对应的值请参考xmpp协议规范
		var handlePresence = function(e) {
			//（发送者希望订阅接收者的出席信息），即别人申请加你为好友
			if (e.type == 'subscribe') {
				if (e.status) {
					if (e.status.indexOf('resp:true') > -1) {
						agreeAddFriend(e.from);
						return;
					}
				}
				var subscribeMessage = e.from + "请求加你为好友。\n验证消息：" + e.status;
				showNewNotice(subscribeMessage);
				$('#confirm-block-footer-confirmButton').click(function() {
					//同意好友请求
					agreeAddFriend(e.from);//e.from用户名
					//反向添加对方好友
					conn.subscribe({
						to : e.from,
						message : "[resp:true]"
					});
					$('#confirm-block-div-modal').modal('hide');
				});
				$('#confirm-block-footer-cancelButton').click(function() {
					rejectAddFriend(e.from);//拒绝加为好友
					$('#confirm-block-div-modal').modal('hide');
				});
				return;
			}
			//(发送者允许接收者接收他们的出席信息)，即别人同意你加他为好友
			if (e.type == 'subscribed') {
				toRoster.push({
					name : e.from,
					jid : e.fromJid,
					subscription : "to"
				});
				return;
			}
			//（发送者取消订阅另一个实体的出席信息）,即删除现有好友
			if (e.type == 'unsubscribe') {
				//单向删除自己的好友信息，具体使用时请结合具体业务进行处理
				delFriend(e.from);
				return;
			}
			//（订阅者的请求被拒绝或以前的订阅被取消），即对方单向的删除了好友
			if (e.type == 'unsubscribed') {
				delFriend(e.from);
				return;
			}
		};
		//异常情况下的处理方法
		var handleError = function(e) {
			e && e.upload && $('#fileModal').modal('hide');
			if (curUserId == null) {
				//         hiddenWaitLoginedUI();
				alert(e.msg + ",请重新登录");
				//showLoginUI();
			} else {
				var msg = e.msg;
				if (e.type == EASEMOB_IM_CONNCTION_SERVER_CLOSE_ERROR) {
					if (msg == "" || msg == 'unknown') {
						alert("服务器断开连接,可能是因为在别处登录");
					} else {
						alert("服务器断开连接");
					}
				} else if (e.type === EASEMOB_IM_CONNCTION_SERVER_ERROR) {
					if (msg.toLowerCase().indexOf("user removed") != -1) {
						alert("用户已经在管理后台删除");
					}
				} else {
					alert(msg);
				}
			}
			conn.stopHeartBeat(conn);
		};
		var handleLocationMessage = function(e) {
			console.info("handleLocationMessage");
		}
		var handleFileMessage = function(e) {
			console.info("handleFileMessage");
		}
		var handleVideoMessage = function(e) {
			console.info("handleVideoMessage");
		}
		var handleInviteMessage = function(e) {
			console.info("handleInviteMessage");
		}
		var handleRoster = function(e) {
			console.info("handleRoster");
		}
		
		var showIM=function(){
			$("#ImModal").modal("show");
		}
		/***************** 环信IM方法相关 end *****************/
		
		
		/***************** 页面初始化区域 *****************/
		$(function(){
			user_name='${user.name}';
			user_id='${user.id}';
			user_portrait='${user.portrait}';
			user_password='${user.password}';
			$(".main-menus li").on("click", function(){
				$(this).siblings("li").removeClass("active");
				$(this).addClass("active");
				if(this.id == "sysPlus"){
					$(".menu-pannel-body").empty().append("<iframe id='formHomeIframe' src='"+$(this).attr("data-url")+"' width='100%' height='100%'></iframe>");
				}else{
					$(".menu-pannel-body").load($(this).attr("data-url"));
				}
				
			});
			$("#sysChat").on("click", function(e){
				e.stopPropagation();
				$("#ImModal").modal("show");
			});
/* 			$("#sysConfig").on("click", function(e){
				e.stopPropagation();
				$(".menu-pannel-pop").toggleClass("active");
				if($(".menu-pannel-pop").hasClass("active")){
					$(".pop-title").text("系统设置");
					$(".menu-pannel-pop .body-content").empty();
				}
			}); */
			$(".menu-pannel-pop .close-btn").on("click", function(e){
				e.stopPropagation();
				$(".menu-pannel-pop").removeClass("active");
			});
			$(document).on("click", function(){
				$(".menu-pannel-pop").removeClass("active");
			});
			$(".menu-pannel-pop").on("click", function(e){
				e.stopPropagation();
			});
			$(".layout-main > .header .h-left .avatar").on("click", function(){
				$(".menu-pannel-body").load("loginController.do?appSetting");
			});
			
			
 			$("#sysHome").trigger("click");
			
			/***************** 环信IM初始化相关 Begin *****************/
			if (!Easemob.im.Helper.isCanUploadFileAsync && typeof uploadShim === 'function') {
				swfupload = uploadShim('fileInput');
			}
			handleConfig();
			conn = new Easemob.im.Connection();
			//初始化连接
			conn.init({
				https : Easemob.im.config.https,
				url : Easemob.im.config.xmppURL,
				//当连接成功时的回调方法
				onOpened : function() {
					handleOpen(conn);
				},
				//当连接关闭时的回调方法
				onClosed : function() {
					handleClosed();
				},
				//收到透传消息时的回调方法
				onCmdMessage : function(message) {
					console.log(message);
					sysMessageHome.getLidata(message.ext.data,"0");
				},
				//收到文本消息时的回调方法
				onTextMessage : function(message) {
					handleTextMessage(message);
				},
				//收到表情消息时的回调方法
				onEmotionMessage : function(message) {
					handleEmotion(message);
				},
				//收到图片消息时的回调方法
				onPictureMessage : function(message) {
					handlePictureMessage(message);
				},
				//收到音频消息的回调方法
				onAudioMessage : function(message) {
					handleAudioMessage(message);
				},
				//收到位置消息的回调方法
				onLocationMessage : function(message) {
					handleLocationMessage(message);
				},
				//收到文件消息的回调方法
				onFileMessage : function(message) {
					handleFileMessage(message);
				},
				//收到视频消息的回调方法
				onVideoMessage : function(message) {
					handleVideoMessage(message);
				},
				//收到联系人订阅请求的回调方法
				onPresence : function(message) {
					handlePresence(message);
				},
				//收到联系人信息的回调方法
				onRoster : function(message) {
					handleRoster(message);
				},
				//收到群组邀请时的回调方法
				onInviteMessage : function(message) {
					handleInviteMessage(message);
				},
				//异常时的回调方法
				onError : function(message) {
					handleError(message);
				}
			});

			var loginInfo = {
				isLogin : false
			};

			//获取自己的登录信息
			if (loginInfo.isLogin) {
				//           showWaitLoginedUI();
				//TODO 未登录显示登录中
			} else {
				//没登陆则进行登陆
				login(user_id, user_password);
			}

			//监听enter和ctrl+enter的事件
			$("#" + talkInputId).keydown(function(event) {
				if (event.altKey && event.keyCode == 13) {
					e = $(this).val();
					$(this).val(e + '\n');
				} else if (event.ctrlKey && event.keyCode == 13) {
					e = $(this).val();
					$(this).val(e + '\n');
				} else if (event.keyCode == 13) {
					event.returnValue = false;
					sendText();
					return false;
				}
			});

			$(window).bind('beforeunload', function() {
				if (conn) {
					//             conn.close();
// 					if (navigator.userAgent.indexOf("Firefox") > 0)
// 						return ' ';
// 					else
// 						return '';
				}
			});

			home.loadSlimScroll([ {
				obj : $('.msg-list'),
				height : "503px"
			}, {
				obj : $('.chat-box'),
				start : "bottom"
			}, {
				obj : $(".chat-input-text textarea"),
				width : "100%",
				alwaysVisible : true,
				disableFadeOut : true
			}, {
				obj : $('.expression-list')
			} ]);

			//初始化表情
			initEmotion();

			//按钮事件绑定区域
			$(".chat-audio").on("click", function() {
				var $this = $(this);
				if ($this.hasClass("playing")) {
					$this.removeClass("playing");
					$this.children(".glyphicon").addClass("glyphicon-play");
					$this.children(".glyphicon").removeClass("glyphicon-pause");
				} else {
					$this.addClass("playing");
					$this.children(".glyphicon").addClass("glyphicon-pause");
					$this.children(".glyphicon").removeClass("glyphicon-play");
				}
			});
			$("#group-settings").on("click", function(e) {
				e.stopPropagation();
				$("#groupSettingPop").toggleClass("active");
				if ($("#groupSettingPop").hasClass("active")) {
					$("#groupSettingPop .pop-title").text("群设置");
					$("#groupSettingPop .body-content").empty().load("orgGroupController.do?groupSettingsEdit&groupId=" + curRoomId);
				}
			});

			$(document).on("click", function() {
				$(".pop-expression").removeClass("open");
			});
			$("#btn_emotion").on("click", function(e) {
				e.stopPropagation();
				$(".pop-expression").toggleClass("open");
			});
			$(".pop-expression").on("click", function(e) {
				e.stopPropagation();
			});
			/***************** 环信IM初始化相关 end *****************/
		})
	</script>
</html>
