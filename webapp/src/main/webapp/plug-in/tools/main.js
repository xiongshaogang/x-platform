/*
加载左侧树列表
 */
var $pageContent = $("#page_content");
var loadLeftNav = function(menudata) {
	//a标签里的图标，暂时删除<i class="awsm-icon-dashboard"></i>
	/*$(".sidebar-menu").append('<li class="active"><a id="indexBtn" href="#"><i class="menu-icon typcn typcn-home icon-color"></i><span class="menu-text"> 首页 </span></a></li>');*/

	var $sidebar = $("#sidebar"),
		$navlist = $(".sidebar-menu"),
		$indexBtn = $("#indexBtn"),  //首页链接按钮
		$pageIndexDiv = $("#page_index"),  //首页内容区域
		$pageDiv = $("#page_div"),  //其他内容区域
		mdJson_d = $.parseJSON(menudata).data,
		$tabbar = $("#tabbar"),
		$searchhelperFunList = $("#searchhelper_fun > ul"),
		$navbarFunlist = $("#navbar_funlist .account-area"),
		i,
		btmFunListsData = {"data":[]},
		funType = "",
		rootEleArr = [];
	for (i in mdJson_d) {
		if (mdJson_d[i].funPid == "" || mdJson_d[i].funPid == "-1") {//没有父节点
			funType = "fun_root";
			$navlist.append(_json2Dom(mdJson_d[i], "fun_root"));
			
			//存储根节点数据数组
			rootEleArr.push({"funId": mdJson_d[i].funId, "funName": mdJson_d[i].funName, "funIcon": mdJson_d[i].funIcon});
		} else {
			if(!$("#" + mdJson_d[i].funPid + " > ul").length) 
				$("#" + mdJson_d[i].funPid).append("<ul class=\"submenu\"></ul>");
			$("#" + mdJson_d[i].funPid + ">ul").append(_json2Dom(mdJson_d[i], ""));
			if(mdJson_d[i].funCsize < 1) funType = "fun_leaf";
			else funType = "";
		}
		if(funType == "fun_leaf"){
			//存储叶子节点
			btmFunListsData.data.push({"funId":mdJson_d[i].funId, "funPid":mdJson_d[i].funPid, "funCsize":mdJson_d[i].funCsize, "funType":funType, "funSubsystem":mdJson_d[i].funSubsystem, "funStyle":"btn-light", "funSize":"btn-xs", "funIcon":mdJson_d[i].funIcon, "funIconSize":"bigger-120", "funName":mdJson_d[i].funName});
			//加载功能搜索初始列表
			$searchhelperFunList.append("<li id='"+mdJson_d[i].funId+"' style='display: none'>"+mdJson_d[i].funName+"</li>");
			
			$(".sidebar-menu #" + mdJson_d[i].funId + " > a").on("click",mdJson_d[i], function(e){
				var thdurl = $(this).attr("data-url");
				
				if (thdurl) {
					$(this).closest("ul.nav").find("li").removeClass("active");
					$(this).parentsUntil("ul.nav", "li").addClass("active");
					
					$pageIndexDiv.hide();
					$pageContent.show();
					$("#sidebar-collapse").show();
					$(".page-breadcrumbs").show();
					var datanav=e.data;
					$.ajax({
						url : thdurl,
						type : 'post',
						cache : false,
						success : function(data) {
							var first="<li><i class='typcn typcn-home icon-color'></i><a id='indexBtn' href='#'>首页</a></li>";
							var last="<li class='active'>"+datanav.funName+"</li>";
							var dataObject= {
									dataNav : []
							}; 
							var center="";
							getParentNav(mdJson_d,datanav.funPid,dataObject);
							for (j=dataObject.dataNav.length;j>0;j--) {
								center+=dataObject.dataNav[j-1];
							}
							$("#breadcrumbnav").html(first+center+last); 
							$pageContent.html(data);
							$("#indexBtn").on("click", function(){
								$("#sidebar").addClass("hide");
								$(".sidebar-toggler").addClass("active");
								$(this).closest("ul.nav").find("li").removeClass("active");
								$(this).parentsUntil("ul.nav", "li").addClass("active");
								$pageContent.hide();
								$pageIndexDiv.show();
								$("#navbar_funlist .account-area > li").removeClass("open");
								$("#breadcrumbnav").html("<li><i class='typcn typcn-home icon-color'></i><a id='indexBtn' href='#'>首页</a></li>");
								$("#sidebar-collapse").hide();
								$(".page-breadcrumbs").hide();
							});
							//处理表格resize
							resizeDatagrid();
							
						}
					});
				}
			});
		}
		
		//if(mdJson_d[i].funCsize < 1)
	}
	
	/*handleSlimScrollBar($("#sidebar"), "show");*/
	//console.log($searchhelperFunList.children("li"));
	
	//搜索模块功能，点击事件
	$searchhelperFunList.children("li").on("click", function(){
		var $thisFunLi = $navlist.find("#"+this.id),
			$thisFunsRootLi = $thisFunLi.closest("li[data-ftype='fun_root']"),
			$thisFunsRootLi_id = $thisFunsRootLi.attr("id"),
			$thisNavbarFunLi = $navbarFunlist.find("li#"+$thisFunsRootLi_id);
		
		$thisFunLi.find("a[data-url]:first").trigger("click");
		$thisFunsRootLi.removeClass("hidden");
		$thisFunsRootLi.siblings("li").addClass("hidden");
		//$thisFunsRootLi.find("li").removeClass("open");
		$thisFunLi.parentsUntil(".sidebar-menu", "li").addClass("open");
		$thisNavbarFunLi.addClass("open");
		$thisNavbarFunLi.siblings("li").removeClass("open");
	});
	
	$(window).data("btmFunListsData",btmFunListsData);
	
	$("#navbar_funlist").data("rootEleArr", rootEleArr);
	//console.log(rootEleArr);
	/*
	$("#indexBtn").on("click", function(){
		debugger;
		$(this).closest("ul.nav").find("li").removeClass("active");
		$(this).parentsUntil("ul.nav", "li").addClass("active");
		$pageContent.hide();
		$pageIndexDiv.show();
	});
	 * */

	function getParentNav(mdJson_d,funPid,dataObject){
		for (i in mdJson_d) {
			if(mdJson_d[i].funId==funPid){
				dataObject.dataNav.push("<li class='active'>"+mdJson_d[i].funName+"</li>");
				getParentNav(mdJson_d,mdJson_d[i].funPid,dataObject);
			}
			
		}
	}
	
	//json元素转换成DOM元素
	function _json2Dom(fdata, ftype) {
		var rtn = "<li id=\"" + fdata.funId + "\" data-ftype=\""+ftype+"\" data-subsys=\""+fdata.funSubsystem+"\">";
		rtn += "<a href=\"#\" "
			+ (fdata.funUrl == "" ? "" : ("data-url=\"" + fdata.funUrl + "\"")) 
			+ " class=\""
			+ (fdata.funCsize > 0 ? "menu-dropdown" : "") 
			+ "\">";
		
		if (ftype == "fun_root") {
			//处理根节点
			rtn += "<i class=\"menu-icon icon-color "+fdata.funIcon+"\"></i>";
			rtn += "<span class=\"menu-text\">" + fdata.funName + "</span>";
		} else {
			//处理根节点之外的所有节点
			rtn += "<i class=\"awsm-icon-double-angle-right\"></i>" + fdata.funName;
		}
		
		//处理有子元素的节点
		if (fdata.funCsize > 0)
			rtn += "<i class=\"menu-expand\"></i>";
		
		rtn += "</a></li>";

		return rtn;
	}
}

//加载顶部菜单按钮
var loadTopNav = function(){
	var $navbarFun = $("#navbar_funlist"),
		$navbarFunlist = $("#navbar_funlist .account-area"),
		$navbarFunOthers = $("#navbar_funlist .account-area .others"),
		$sidebarMenuUl = $("#sidebar .sidebar-menu"),
		$othersDropdown = $("#others_dropdown"),
		rootEleArr = $("#navbar_funlist").data("rootEleArr");
		
		$navbarFunlist.empty();
	
		for(var i = 0; i < rootEleArr.length; i++){
			var handleTopProb = "",
				handleTopProb_d = "";
			if(rootEleArr[i].funIcon.indexOf("fa-")>-1){
				//handleTopProb = "style='top:-2px;'";
				//handleTopProb_d = "style='top:0;'";
			}
			if(i < 4){
				$navbarFunlist.append("<li "+handleTopProb+" id='"+rootEleArr[i].funId+"'><a class='dropdown-toggle' title='"+rootEleArr[i].funName+"' href='#'>" +
						"<i class='icon "+rootEleArr[i].funIcon+"' "+handleTopProb_d+"></i>" +
						"<span class='fun-name' "+handleTopProb_d+">"+rootEleArr[i].funName+"</span>" +
						"</a>" +
						"</li>");
			}else{
				$othersDropdown.append("<li id='"+rootEleArr[i].funId+"'>" +
						"<div class='fun-icon'><i class='icon "+rootEleArr[i].funIcon+"'></i></div>" +
						"<div class='fun-name'>"+rootEleArr[i].funName+"</div>" +
						"</li>");
			}
		}
		if(!$othersDropdown.children("li").length)
			$navbarFunOthers.hide();
		
		$navbarFunlist.append($navbarFunOthers);
		
		$navbarFunlist.children("li").on("click", function(){
			if($(this).hasClass("others")) return;
			
			$(this).siblings("li").removeClass("open");
			$(this).addClass("open");
			
			_handleFunClick(this);
		});
		$othersDropdown.children("li").on("click", function(){
			_handleFunClick(this);
		});
		
		var _handleFunClick = function(thisObj){
			var $thisFunLi = $sidebarMenuUl.children("li[id='"+thisObj.id+"']");
			
			$("#sidebar").removeClass("hide");
	        $(".sidebar-toggler").removeClass("active");
			
			$thisFunLi.siblings("li").addClass("hidden");
			$thisFunLi.removeClass("hidden");
			if(!$thisFunLi.hasClass("open")){
				$thisFunLi.find("a:first").trigger("click");
			}
			$thisFunLi.find("a[data-url]:first").trigger("click");
		}
}

//渲染EaryUI样式
var redrawEasyUI = function(obj){
	$.parser.parse(obj.parent());
}



var excgColor = function(c_style, c_code){
	var rtn;
	if(c_style){
		switch(c_style) {
			case "label-grey":
				rtn = "#a0a0a0";
				break;
			case "label-success":
				rtn = "#82af6f";
				break;
			case "label-danger":
				rtn = "#d15b47";
				break;
			case "label-purple":
				rtn = "#9585bf";
				break;
			case "label-yellow":
				rtn = "#fee188";
				break;
			case "label-pink":
				rtn = "#d6487e";
				break;
			case "label-info":
				rtn = "#3a87ad";
				break;
			default:
				rtn = "#a0a0a0";
				break;
		}
	}else{
		switch(c_code) {
		case "#a0a0a0":
			rtn = "label-grey";
			break;
		case "#82af6f":
			rtn = "label-success";
			break;
		case "#d15b47":
			rtn = "label-danger";
			break;
		case "#9585bf":
			rtn = "label-purple";
			break;
		case "#fee188":
			rtn = "label-yellow";
			break;
		case "#d6487e":
			rtn = "label-pink";
			break;
		case "#3a87ad":
			rtn = "label-info";
			break;
		default:
			rtn = "label-grey";
			break;
		}
	}
	return rtn;
}

function chgPsd(){
	createwindow("修改密码","userController.do?passwordEdit", 530, 260, null, {optFlag:'update'});
}

function checkPassword(password){
	var flag = true;
	$.ajax({
		url : "userController.do?checkPassword",
		data : {
			password : password,
		},
		async : false,
		success:function(data){
			var data = $.parseJSON(data);
			flag = data.success;
		},
		error:function(){
			flag =  false;
		}
	});
	
	return flag;
}



//通过文件后缀名，返回文件图标对应的class
var getFileIcon = function(iconType,path){
	var rtn;
//	switch(ext){
//		case ".txt":
//			rtn = "awsm-icon-file-text-alt grey";
//			break;
//		case ".pdf":
//			rtn = "awsm-icon-file-pdf-o red";
//			break;
//		case ".doc":case ".docx":
//			rtn = "awsm-icon-file-word-o blue";
//			break;
//		case ".xlsx":case ".xls":case ".csv":
//			rtn = "awsm-icon-file-excel-o green";
//			break;
//		case ".ppt":case ".pptx":
//			rtn = "awsm-icon-file-powerpoint-o red";
//			break;
//		case ".jpg":case ".gif":case ".png":case ".bmp":case ".pic":
//			rtn = "awsm-icon-file-image-o purple";
//			break;
//		case ".zip":case ".rar":
//			rtn = "awsm-icon-file-zip-o blue";
//			break;
//		case ".wav":case ".mp3":case ".ram":
//			rtn = "awsm-icon-file-audio-o orange";
//			break;
//		case ".avi":case ".mp4":case ".rmvb":case ".rm":
//			rtn = "awsm-icon-file-video-o light-orange";
//			break;
//		case ".htm":case ".html":case ".js":case ".css":case ".java":case ".class":
//			rtn = "awsm-icon-file-code-o grey";
//			break;
//		default:
//			rtn = "awsm-icon-file blue";
//			break;
//	}
	if(iconType!='img'){
		rtn = "basic/img/attachment/"+iconType+".png";
	}else{
		rtn = path;
	}
	return rtn;
}


function getLocalTime(nS) {
    return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");      
 }

/** 修改个人资料 */
function userInfo(){
	createwindow("个人资料","employeeController.do?userInfoEdit", null, 480, 2, {optFlag:'add',formId:'form_userInfoEdit'});
}

function handleSlimScrollBar(){
	$("#slimscroll-container").slimscroll({
		height: $(window).outerHeight()-90,
		size: "4px",
		color: "#999"
	});
}

//resize表格
function resizeDatagrid(){
	$("#page_content .datagrid").find("table:hidden").datagrid("resize", {width: "100%"});
	$("#page_content .easyui-layout").layout("resize", {width: "100%"});
}

function loadTaskList(data){
	var result=$.parseJSON(data)
	if(result==null){
		return;
	}
	var taskList=result.obj;
	var length=taskList.length;
	$("#headTaskCount").html(length);
	$("#headTaskTitle").html(length+"个待处理任务");
	for(var i=0;i<length;i++){
		var task=taskList[i];
		//首页头部任务审批
		var headTaskString="<li>"
	        +"<a id=\"task-top-"+task.id+"\" href=\"#\">"
	            +"<div class=\"clearfix\">"
	            	+"<span class=\"pull-left\">"+task.name+"</span>"
	            	+"<span class=\"pull-right\">"+task.progress+"</span>"
	            +"</div>"
	            +"<div class=\"progress progress-xs\">"
	            	+"<div style=\"width:"+task.progress+"\" class=\"progress-bar icon-color\"></div>"
	            +"</div>"
	        +"</a>"
	     +"</li>";
		$("#headTaskList").append(headTaskString);
		//首页内人任务审批
		var taskListBodyString="<li id=\"task-item-"+task.id+"\" class=\"task-item\">"
							        +"<div class=\"task-state\">"+task.name+"</div>"
							        +"<div class=\"task-time\">"+task.createTime+"</div>"
							        +"<div class=\"task-body\">"+task.businessName+"</div>"
							        +"<div class=\"task-creator\">"+task.creator+"</div>"
							        +"<div class=\"task-assignedto\">"+task.processName+"</div>"
							  +"</li>";
		$("#taskListBody").append(taskListBodyString);
		//添加事件
		$("#headTaskList #task-top-"+task.id+",#taskListBody #task-item-"+task.id+"").on("click", function(){
			createwindow("任务处理", "taskController.do?toStart&taskId="+task.id, 1000, 500, null,{noheader:true,optFlag:'close'});
		});
	}
	var headTaskMore="<li class=\"dropdown-footer\">"
						 +"<a href=\"taskController.do?MyTaskList\">"
								 +"查看所有任务"
						 +"</a>"
					 +"</li>";
	$("#headTaskList").append(headTaskMore);
}

var main = {
	/******************** 首页初始化方法 ********************/
	initIndex:function(){
		//加载系统树列表
		main.loadSysFunc();
		
		//处理窗口大小变化后，easyui表格的宽度
		$("#sidebar-collapse, .sidebar-toggler").on("click", function(){
			resizeDatagrid();
		});
		
		//左侧添加虚拟滚动条
		handleSlimScrollBar();
		$(window).on("resize", function(){
			console.log("window resize");
			handleSlimScrollBar();
		});
		
		$(".navbar-brand").on("click", function(){ 
			$("#indexBtn").trigger("click");
		});
		
		$("#searchinput_fun").on("keyup", function(e){
			//console.log(e.keyCode);
			var funList = $(window).data("btmFunListsData").data,
				$searchhelperFunList = $("#searchhelper_fun > ul");
			
			if(this.value.trim().length > 0){
				$searchhelperFunList.show();
				$("#no_content_notice").hide();
				$("#searchhelper_fun").show();
			}else{
				$searchhelperFunList.hide();
				$("#no_content_notice").show();
				$("#searchhelper_fun").hide();
			}
			$searchhelperFunList.children("li").hide();
			
			$($.grep($searchhelperFunList.children("li"), function(n, i){
				return $(n).text().indexOf($("#searchinput_fun").val()) > -1;
			})).show();
			
			if($searchhelperFunList.children("li:visible").length < 1){
				$("#no_content_nodata").show();
			}else{
				$("#no_content_nodata").hide();
			}
			
			if(e.keyCode == "13"){
				$searchhelperFunList.children("li:visible:first").trigger("click");
				$("#searchhelper_fun").hide();
			}
		});
		$("#searchinput_fun").on("focusout", function(e){
			//console.log("focusout");
			$("#searchhelper_fun").hide();
		});
		$("#searchinput_fun").on("focusin", function(e){
			//console.log("focusout");
			$("#searchhelper_fun").show();
		});
	},
	/******************** 加载系统树列表 ********************/
	loadSysFunc : function() {
		$.ajax({
			async : true,
			cache : false,
			type : 'POST',
			url : "loginController.do?left", // 请求的action路径
			data : "",
			dataType : "text",
			error : function() { // 请求失败处理函数
				alert("请求失败！");
			},
			success : function(data) {
				// 加载左树
				loadLeftNav(data);
				//加载顶部菜单按钮
				loadTopNav();
			}
		});
		$.ajax({
			async : true,
			cache : false,
			type : 'POST',
			url : "taskController.do?myTask", // 请求的action路径
			dataType : "text",
			error : function() { // 请求失败处理函数
				//alert("请求失败！");
			},
			success : function(data) {
				// 加载任务列表
				loadTaskList(data);
			}
		});
	},
	/******************** 气泡提醒 ********************/
	showTooltip : function(opt){
		if(opt.clean == null || opt.clean)
			$(".tooltip").remove();
		
		var obj = opt.obj;
		if(!(obj instanceof jQuery))
			obj = $(obj);
		  
		obj.attr({
			"data-toggle": 'tooltip',
			"data-title": opt.msg || "",
			"data-original-title": opt.msg || "",
			"data-placement": opt.placement || "",
			"data-container": opt.container || "body",
			"data-trigger": opt.trg || "manual",  //manual: 手动
			"data-html": opt.html || "",
			"data-template": opt.tpl
		});
		
		obj.tooltip();
		if(opt.show || opt.show == null)
			obj.tooltip("show");
		
		if(opt.destroy || opt.destroy == null)
			setTimeout(function(){obj.tooltip("destroy");},20000);
	},
	portraitUpload: function(){
		createwindow("头像上传", "userController.do?portraitUpload", 390, 330, null);
	}
}

