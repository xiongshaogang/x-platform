<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- Head -->
<head>
    <meta charset="utf-8" />
    <title>xplatform</title>

    <meta name="description" content=xplatform />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="shortcut icon" href="basic/img/favicon.png" type="image/x-icon">

    <!--Basic Styles-->
    <link href="basic/css/bootstrap.min.css" rel="stylesheet" />
    <link href="basic/css/font-awesome.min.css" rel="stylesheet" />
    <link href="basic/css/weather-icons.min.css" rel="stylesheet" />

    <!--Fonts-->
	<!-- <link href="http://fonts.useso.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,400,600,700,300" rel="stylesheet" type="text/css"> -->
    
    <!--Beyond styles-->
    <link href="basic/css/beyond.min.css" rel="stylesheet" type="text/css" />
    <link href="basic/css/beyond-rewrite.css" rel="stylesheet" type="text/css" />
    <link href="basic/css/typicons.min.css" rel="stylesheet" />
    <link href="basic/css/animate.min.css" rel="stylesheet" />
    
    <!-------------------------------------------------------- Easy UI -->
	<link rel="stylesheet" href="plug-in/easyui-1.4.2/themes/gray/easyui.css" />
	<link rel="stylesheet" href="plug-in/tools/css/easyui_rewrite.css" type="text/css"></link>
	<link rel="stylesheet" href="plug-in/easyui-1.4.2/themes/icon.css" />
	
	<!-------------------------------------------------------- jquery File Upload 上传组件 -->
	<link rel="stylesheet" href="plug-in/jquery-fileupload/css/jquery.fileupload-ui.css" />
	<link rel="stylesheet" href="plug-in/jquery-fileupload/css/jquery.fileupload.css" />
	
	<!-------------------------------------------------------- Tools -->
	<link rel="stylesheet" href="plug-in/tools/css/common.css" type="text/css"></link>
	<link rel="stylesheet" href="plug-in/tools/css/attachment.css" type="text/css"></link>
	<link rel="stylesheet" href="plug-in/workflow/css/opinionArea.css" type="text/css"></link>
	<link rel="stylesheet" href="plug-in/yitip/css/jquery.yitip.css" type="text/css"></link>
	<link rel="stylesheet" href="plug-in/Validform/css/style.css" type="text/css"/>
	<link rel="stylesheet" href="plug-in/Validform/css/tablefrom.css" type="text/css"/>
	<link rel="stylesheet" href="plug-in/Validform/plugin/jqtransform/jqtransform.css" type="text/css" />
	
	<!-------------------------------------------------------- 代码高亮  -->
	<link rel="stylesheet" href="plug-in/jquery/jquery-autocomplete/jquery.autocomplete.css" type="text/css" />
	
	<!-------------------------------------------------------- jcrop plugin 图片裁剪插件(头像上传) -->
	<link rel="stylesheet" href="plug-in/jcrop/css/jquery.Jcrop.css" type="text/css" />


    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
    <script src="basic/js/skins.min.js"></script>
</head>
<!-- /Head -->
<!-- Body -->
<body>
    <!-- Navbar  头部-->
    <div class="navbar">
        <div class="navbar-inner">
            <div class="navbar-container">
                <!-- Navbar Barnd -->
                <div class="navbar-header pull-left">
                    <a href="#" class="navbar-brand">
                        <small>
                            <img src="basic/img/logo.png" alt="xplatform" />
                        </small>
                    </a>
                </div>
                <!-- /Navbar Barnd -->

                <!-- Sidebar Collapse -->
                <div class="sidebar-collapse" id="sidebar-collapse" style="display: none;">
                    <i class="collapse-icon fa fa-bars"></i>
                </div>
                
                <!-- Fun List -->
                <div class="navbar-header navbar-funlist" id="navbar_funlist">
                    <div class="navbar-account">
                        <ul class="account-area">
                        	<!-- 
                            <li class="active">
                                <a class="dropdown-toggle" title="功能" href="#">
									<i class="icon fa fa-bars"></i>
									<span class="fun-name">功能1</span>
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-toggle" title="功能" href="#">
									<i class="icon fa fa-bars"></i>
									<span class="fun-name">功能2</span>
                                </a>
                            </li>
                        	 -->
                            <li class="others">
                                <a class=" dropdown-toggle" data-toggle="dropdown" title="Help" href="#">
                                    <i class="icon fa fa-warning"></i>
                                    <span class="fun-name">其他</span>
                                </a>
                                <ul class="pull-right dropdown-menu dropdown-arrow dropdown-notifications" id="others_dropdown">
                                	<!-- 
                                	<li>
                                		<div class="fun-icon"><i class="icon fa fa-user"></i></div>
                                		<div class="fun-name">功能1</div>
                                	</li>
                                	<li>
                                		<div class="fun-icon"><i class="icon fa fa-user"></i></div>
                                		<div class="fun-name">功能1</div>
                                	</li>
                                	<li>
                                		<div class="fun-icon"><i class="icon fa fa-user"></i></div>
                                		<div class="fun-name">功能1</div>
                                	</li>
                                	<li>
                                		<div class="fun-icon"><i class="icon fa fa-user"></i></div>
                                		<div class="fun-name">功能1</div>
                                	</li>
                                	 -->
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
                <!--/ Fun List -->
                
                <!-- /Sidebar Collapse -->
                <!-- Account Area and Settings --->
                <div class="navbar-header pull-right">
                    <div class="navbar-account">
                        <ul class="account-area">
                            <li>
                                <a class="wave in dropdown-toggle" data-toggle="dropdown" title="Help" href="#">
                                    <i class="icon fa fa-envelope"></i>
                                    <span class="badge">3</span>
                                </a>
                                <!--Messages Dropdown-->
                                <ul class="pull-right dropdown-menu dropdown-arrow dropdown-messages" id="myMessage">
                                <!--     <li>
                                        <a href="#">
                                            <img src="basic/img/avatars/divyia.jpg" class="message-avatar" alt="Divyia Austin">
                                            <div class="message">
                                                <span class="message-sender">
                                                    Divyia Austin
                                                </span>
                                                <span class="message-time">
                                                    2 minutes ago
                                                </span>
                                                <span class="message-subject">
                                                    Here's the recipe for apple pie
                                                </span>
                                                <span class="message-body">
                                                    to identify the sending application when the senders image is shown for the main icon
                                                </span>
                                            </div>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#">
                                            <img src="basic/img/avatars/bing.png" class="message-avatar" alt="Microsoft Bing">
                                            <div class="message">
                                                <span class="message-sender">
                                                    Bing.com
                                                </span>
                                                <span class="message-time">
                                                    Yesterday
                                                </span>
                                                <span class="message-subject">
                                                    Bing Newsletter: The January Issue‏
                                                </span>
                                                <span class="message-body">
                                                    Discover new music just in time for the Grammy® Awards.
                                                </span>
                                            </div>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#">
                                            <img src="basic/img/avatars/adam-jansen.jpg" class="message-avatar" alt="Divyia Austin">
                                            <div class="message">
                                                <span class="message-sender">
                                                    Nicolas
                                                </span>
                                                <span class="message-time">
                                                    Friday, September 22
                                                </span>
                                                <span class="message-subject">
                                                    New 4K Cameras
                                                </span>
                                                <span class="message-body">
                                                    The 4K revolution has come over the horizon and is reaching the general populous
                                                </span>
                                            </div>
                                        </a>
                                    </li> -->
                                </ul>
                                <!--/Messages Dropdown-->
                            </li>

                            <li>
                                <a class="dropdown-toggle" data-toggle="dropdown" title="Tasks" href="#">
                                    <i class="icon fa fa-tasks color-icon"></i>
                                    <span class="badge" id="headTaskCount"></span>
                                </a>
                                <!--Tasks Dropdown-->
                                <ul id="headTaskList" class="pull-right dropdown-menu dropdown-tasks dropdown-arrow ">
                                    <li class="dropdown-header bordered-themeprimary">
                                        <i class="fa fa-tasks icon-color"></i><span id="headTaskTitle"></span>
                                    </li>
                                </ul>
                                <!--/Tasks Dropdown-->
                            </li>
                            
                            <li>
                                <a class="login-area dropdown-toggle" data-toggle="dropdown">
                                    <div class="avatar" title="View your public profile">
                                        <img id="index-portrait30" src='<c:choose><c:when test="${portraitUrl==null or portraitUrl==''}">basic/img/avatars/avatar_80.png</c:when><c:otherwise>${portraitUrl}</c:otherwise></c:choose>'>
                                    </div>
                                    <section>
                                        <h2><span class="profile"><span style="width:84px">您好！<c:out value="${user.name}"></c:out></span></span></h2>
                                    </section>
                                </a>
                                <!--Login Area Dropdown-->
                                <ul class="pull-right dropdown-menu dropdown-arrow dropdown-login-area">
                                    <li class="username"><a><c:out value="${user.name}"></c:out></a></li>
                                    <li class="email"><a><c:out value="${user.email}"></c:out></a></li>
                                    <!--Avatar Area-->
                                    <li>
                                        <div class="avatar-area">
                                            <img id="index-portrait80" src='<c:choose><c:when test="${portraitUrl==null or portraitUrl==''}">basic/img/avatars/avatar_80.png</c:when><c:otherwise>${portraitUrl}</c:otherwise></c:choose>' class="avatar">
                                            <span onclick="main.portraitUpload()" class="caption">更换头像</span>
                                        </div>
                                    </li>
                                    <!--Theme Selector Area-->
                                    <li class="theme-area">
                                        <ul class="colorpicker" id="skin-changer">
                                            <li><a class="colorpick-btn" href="#" style="background-color:#5DB2FF;" rel="basic/css/skins/blue.min.css"></a></li>
                                            <li><a class="colorpick-btn" href="#" style="background-color:#2dc3e8;" rel="basic/css/skins/azure.min.css"></a></li>
                                            <li><a class="colorpick-btn" href="#" style="background-color:#03B3B2;" rel="basic/css/skins/teal.min.css"></a></li>
                                            <li><a class="colorpick-btn" href="#" style="background-color:#53a93f;" rel="basic/css/skins/green.min.css"></a></li>
                                            <li><a class="colorpick-btn" href="#" style="background-color:#FF8F32;" rel="basic/css/skins/orange.min.css"></a></li>
                                            <li><a class="colorpick-btn" href="#" style="background-color:#cc324b;" rel="basic/css/skins/pink.min.css"></a></li>
                                           <!--  <li><a class="colorpick-btn" href="#" style="background-color:#AC193D;" rel="basic/css/skins/darkred.min.css"></a></li> -->
                                            <li><a class="colorpick-btn" href="#" style="background-color:#8C0095;" rel="basic/css/skins/purple.min.css"></a></li>
                                            <li><a class="colorpick-btn" href="#" style="background-color:#0072C6;" rel="basic/css/skins/darkblue.min.css"></a></li>
                                            <li><a class="colorpick-btn" href="#" style="background-color:#585858;" rel="basic/css/skins/gray.min.css"></a></li>
                                           <!--  <li><a class="colorpick-btn" href="#" style="background-color:#474544;" rel="basic/css/skins/black.min.css"></a></li> -->
                                            <li><a class="colorpick-btn" href="#" style="background-color:#001940;" rel="basic/css/skins/deepblue.min.css"></a></li>
                                        </ul>
                                    </li>
                                    <!--/Theme Selector Area-->
                                    <li class="edit">
                                        <a href="javascritp:void(0);" id="modifyPass"  class="pull-left">密码</a>
                                        <a href="javascritp:void(0);" id="loginOut" class="pull-right">注销</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /Navbar -->
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
            <!-- Page Sidebar -->
            <div class="page-sidebar hide" id="sidebar">
                <!-- Page Sidebar Header-->
                <div class="sidebar-header-wrapper">
                    <input type="text" class="searchinput" id="searchinput_fun" placeholder="请输入搜索内容" />
                    <i class="searchicon fa fa-search"></i>
                    <div class="searchhelper" id="searchhelper_fun">
                    	<ul class="list-style-none">
                    	</ul>
                    	<!-- 
                    	<span id="no_content_notice">请输入搜索内容</span>
                    	 -->
                    	<span id="no_content_nodata" style="display: none; padding: 0 10px; line-height: 2.5em; text-align: center;"> - 无搜索结果 - </span>
                    </div>
                </div>
                <!-- /Page Sidebar Header -->
                <!-- Sidebar Menu -->
                <div id="slimscroll-container">
	                <ul class="nav sidebar-menu"></ul>
                </div>
                <!-- /Sidebar Menu -->
            </div>
            <!-- /Page Sidebar -->
            <!-- Page Content -->
            <div class="page-content">
                <!-- page-header -->
                <div class="page-breadcrumbs page-header position-relative" style="display: none;">
                    <ul id="breadcrumbnav" class="breadcrumb">
                        <li>
                            <i class="typcn typcn-home icon-color"></i>
                            <a id="indexBtn" href="#">首页</a>
                        </li>
                    </ul>
                    <div class="header-buttons">
                        <a class="sidebar-toggler" href="#">
                            <i class="fa fa-arrows-h"></i>
                        </a>
                        <a class="refresh" id="refresh-toggler" href="">
                            <i class="glyphicon glyphicon-refresh"></i>
                        </a>
                        <a class="fullscreen" id="fullscreen-toggler" href="#">
                            <i class="glyphicon glyphicon-fullscreen"></i>
                        </a>
                    </div>
                </div>
                <!-- page-body -->
                <div class="page-body">
					<div id="page_content" style="height:500px;overflow:visible;display:none">
						
					</div>
					<div id="page_index">
						<div class="row">
	                        <div class="col-lg-4 col-sm-6 col-xs-6">
	                            <div class="widget">
	                                <div class="widget-header bordered-bottom bordered-themeprimary">
	                                    <i class="widget-icon fa fa-tasks themeprimary"></i>
	                                    <span class="widget-caption themeprimary">任务列表</span>
	                                </div><!--Widget Header-->
	                                <div class="widget-body">
	                                    <div class="widget-main no-padding">
	                                        <div class="task-container" ><!-- style="height:530px" -->
	                                            <ul id="taskListBody" class="tasks-list">
	                                                <!-- <li class="task-item">
	                                                    <div class="task-state">  任务名称</div>
	                                                    <div class="task-time">指派时间</div>
	                                                    <div class="task-body">项目名称</div>
	                                                    <div class="task-creator">上一步执行人</div>
	                                                    <div class="task-assignedto">所属流程</div>
	                                                </li>
	                                                -->
	                                            </ul>
	                                        </div>
	                                    </div><!--Widget Main Container-->
	                                </div><!--Widget Body-->
	                            </div>
	                        </div>
						</div>
					</div><!-- page_index end -->
					<div class="page_formbuilder"></div>
                </div>
            </div>
            <!-- /Page Content -->
        </div>
        <!-- /Page Container -->
        <!-- Main Container -->
    </div>
    <!-- alert提示框 -->
    <div id="modal-danger" class="modal modal-message modal-danger fade" style="display: none;" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <i class="glyphicon glyphicon-fire"></i>
                </div>
                <div class="modal-title">提示信息</div>

                <div class="modal-body">操作出错!</div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">确定</button>
                </div>
            </div> <!-- / .modal-content -->
        </div> <!-- / .modal-dialog -->
    </div>

    <!--Basic Scripts-->
    <script src="basic/js/html5.js"></script>
    <script src="basic/js/jquery-2.0.3.min.js"></script>
    <script src="basic/js/jquery-migrate-1.1.0.js"></script>
    <script src="basic/js/bootstrap.min.js"></script>
    <script src="basic/js/toastr/toastr.js"></script>
    <script src="basic/js/beyond.min.js"></script>
    <!--[if lt IE 9]>
	  <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
	  <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
	
    <!-------------------------------------- 基础类型扩展、方法扩展  -->
	<script src="plug-in/tools/syUtil.js"></script>
	<!-------------------------------------- Easy UI -->
	<script src="plug-in/easyui-1.4.2/jquery.easyui.all.js"></script>
	<script src="plug-in/easyui-1.4.2/extends/easyuiextend.js"></script>
	<script src="plug-in/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
	<script src="plug-in/easyui-1.4.2/extends/datagrid-scrollview.js"></script>
	<script src="plug-in/easyui-1.4.2/extends/datagrid-groupview.js"></script>
	<script src="plug-in/easyui-1.4.2/extends/editTableExtends.js"></script>
	
	<!-------------------------------------- Tools工具脚本 Begin -->
	<script src="plug-in/jquery-plugs/form/jquery.form.js"></script>
	<script src="plug-in/tools/jquery.cookie.js"></script>
	<script src="plug-in/tools/jquery.json.js"></script>
	<!-- js产生uuid插件 -->
	<script src="plug-in/tools/uuid.js"></script>
	<!-- tip插件 yitip -->
	<script src="plug-in/yitip/js/jquery.yitip.js"></script>
	<!-- js复制插件 -->
	<script src="plug-in/clipboard/ZeroClipboard.js"></script>
	<!-- xml数据处理相关 -->
	<script src="plug-in/xml/js/ucgXmlDataCollect.js"></script>
	<!-- 实现js的Map结构 -->
	<script src="plug-in/tools/Map.js"></script>
	<!-- slimscroll虚拟滚动条插件 -->
	<script src="basic/js/jquery.slimscroll.min.js"></script>
	<!-------------------------------------- Tools工具脚本 End -->
	
    <!-------------------------------------- codemirror 代码高亮插件 -->
	<script src="plug-in/javacode/codemirror.js"></script>
	<script src="plug-in/javacode/InitMirror.js"></script>
	<!-------------------------------------- ckeditor -->
	<script type="text/javascript" src="plug-in/ckeditor/ckeditor.js"></script>
	
	<!-------------------------------------- 自动补全标签 -->
	<script src="plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js"></script>
    
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
    
    <!-------------------------------------- jcrop plugin 图片裁剪插件(头像上传) -->
	<script src="plug-in/jcrop/js/jquery.Jcrop.js"></script>
	
    <!-------------------------------------- 表单验证相关JS -->
	<script type="text/javascript" src="plug-in/Validform/js/form.js"></script>
	<script type="text/javascript" src="plug-in/Validform/js/Validform_v5.3.2.js"></script>
	<script type="text/javascript" src="plug-in/Validform/js/Validform_Datatype.js"></script>
	<script type="text/javascript" src="plug-in/Validform/js/datatype.js"></script>
	<script type="text/javascript" src="plug-in/Validform/plugin/jqtransform/jquery.jqtransform.js"></script>
	<script type="text/javascript" src="plug-in/Validform/plugin/passwordStrength/passwordStrength-min.js"></script>
    
	<!-------------------------------------- 功能相关脚本 Begin -->
	<script src="plug-in/tools/curdtools.js"></script>
	<!-- 首页js相关 -->
	<script src="plug-in/tools/main.js"></script>
	<!-- workflow工作流相关 -->
	<script src="plug-in/workflow/js/approveButton.js"></script>
	
   
   <script type="text/javascript">
		jQuery(function($) {
			//首页初始化方法
			main.initIndex();
			index.loadInnerMessage();
			$("#loginOut").on("click", function(e){
				e.stopPropagation();
				e.preventDefault();
		        $.messager.confirm("操作提示", "您确定要执行操作吗？", function (data) {
		            if (data) {
		                location.href ="loginController.do?logout";
		            }
		            else {

		            }
		        });
			});
			
			$("#modifyPass").on("click", function(e){
				e.stopPropagation();
				e.preventDefault();
				index.modofyPass();
			});
			
			//临时给LOGO加事件，点击打开表单编辑器
			$(".navbar-brand").on("click", function(){
				$(".page-body > div").hide();
				$(".page_formbuilder").show().append("<iframe id='formHomeIframe' src='appFormFieldController.do?appForm' width='100%' height='100%'></iframe>");
			});
		});
		
	var index = {
			modofyPass : function() {
				var url = "userController.do?changePass";
				//var optFlag = (id ? "update" : "add");
				createwindow("修改密码", url, 450,300, null, {
					//optFlag : optFlag,
					buttons : [ {
						text : "修改",
						iconCls : 'awsm-icon-external-link-sign',
						handler : function() {
							//$("#approveStatus",$("#orderEdit")).val("1");//提交到业务交易审核
							//var coupon = $("#orderEdit").data("coupon");
							if(1 == 1){
								saveObj();
							}else{
								alert("请解析正确的申购码");
							}
							
						}
					}, {
						text : "取消",
						iconCls : 'awsm-icon-remove',
						handler : function() {
							closeD($(this).closest(".window-body"));
						}
					} ]
				});
			},
			loadInnerMessage : function(){
				var imgURL = '${attachForeRequest}';
				$.ajax({
					url : 'messageController.do?loadInnerMessage&page=1&rows=5',
					type : 'post',
					data: '',
					dataType: "json",
					success : function(result) {
						if(result.success){
							$.each(result.obj, function(i, item) {
								if(item.title != null && item.title !=""){
									
								var content = "";
								var title = "";
								if(item.title.length > 12){
								   title = item.title.substr(0,11)+"...";
								}else{
								   title = item.title;
								}
								
								if(item.sourceType != "" && item.sourceType != null && item.sourceType == "flow"){
									var extra = JSON.parse(item.extra);
									$.each(extra.extraData, function(j, item1) {
										//只显示两行
										if(j<2){
											if(item1.value.length > 10){
												content = content+item1.name+":"+item1.value.substr(0,10)+"..."+"</br>";
											}else{
												content = content+item1.name+":"+item1.value+"</br>";
											}
											
										}
									});
									var templi = "<li>"
	                            		+"<a href='#'>"
	                        			+"<img src='"+imgURL+extra.img+"' class='message-avatar' >"
	                        			+"<div class='message'>"
	                            		+"<span class='message-sender'>"
	                                	+title
	                           			+"</span>"
	                            		+"<span class='message-time'>"
	                               		+extra.createTime
	                            		+"</span>"
	/*                             		+"<span class='message-subject'>"
	                               		+"Here's the recipe for apple pie"
	                            		+"</span>" */
	                            		+"<span class='message-body'>"
	                                	+content
	                           			+"</span>"
	                        			+"</div>"
	                    				+"</a>"
	                					+"</li>";
	                				$("#myMessage").append(templi);
								}
							}
						});
					}
				}
			});
		}

			
	}
	</script>


</body>
<!--  /Body -->
</html>
