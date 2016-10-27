/*
加载左侧树列表
 */
var loadLeftNav = function(menudata) {
	//a标签里的图标，暂时删除<i class="awsm-icon-dashboard"></i>
	$(".nav-list").append('<li class="active"><a id="indexBtn" href="#"><i class="awsm-icon-home"></i><span class="ace-menu-text">首页 </span></a></li>');

	var $sidebar = $("#sidebar"),
		$navlist = $(".nav-list"),
		$indexBtn = $("#indexBtn"),  //首页链接按钮
		$pageIndexDiv = $("#page_index"),  //首页内容区域
		$pageDiv = $("#page_div"),  //其他内容区域
		$divIE = $("#div_ie"),
		mdJson_d = $.parseJSON(menudata).data,
		$tabbar = $("#tabbar"),
		i,
		btmFunListsData = {"data":[]},
		funType = "";
	
	for (i in mdJson_d) {
		if (mdJson_d[i].funPid == "" || mdJson_d[i].funPid == "-1") {
			$navlist.append(_json2Dom(mdJson_d[i]));
			funType = "fun_root";
		} else {
			if(!$("#" + mdJson_d[i].funPid + " > ul").length) 
				$("#" + mdJson_d[i].funPid).append("<ul class=\"submenu\"></ul>");
			$("#" + mdJson_d[i].funPid + ">ul").append(_json2Dom(mdJson_d[i]));
			if(mdJson_d[i].funCsize < 1) funType = "fun_leaf";
			else funType = "";
		}
		
		if(funType == "fun_leaf")
			btmFunListsData.data.push({"funId":mdJson_d[i].funId, "funPid":mdJson_d[i].funPid, "funCsize":mdJson_d[i].funCsize, "funType":funType, "funSubsystem":mdJson_d[i].funSubsystem, "funStyle":"btn-light", "funSize":"btn-xs", "funIcon":mdJson_d[i].funIcon, "funIconSize":"bigger-120", "funName":mdJson_d[i].funName});
		if(mdJson_d[i].funUrl =="messageController.do?innerMsgList"){
			$("#innerMsg").attr("data-funId", mdJson_d[i].funId);
		}
		
		if(mdJson_d[i].funUrl =="messageController.do?message&type=annunciate"){
			$("#annReturn").attr("data-funId", mdJson_d[i].funId);
		}
		
		if(mdJson_d[i].funUrl =="messageController.do?annunciateApprove"){
			$("#annApprove").attr("data-funId", mdJson_d[i].funId);
		}
		if(mdJson_d[i].funCsize < 1)
			$("#" + mdJson_d[i].funId + " > a").on("click",mdJson_d[i], function(e){
//				debugger;
				var thdurl = $(this).attr("data-url");
				
				if (thdurl) {
//					$tabbar.height(window.screen.availHeight - $("#navbar").height() - $("#navbar").height()-120);
					$pageIndexDiv.hide();
					$tabbar.show();
					var data=e.data;
					if(isIE(8)){
						$divIE.show().load(data.funUrl);
					}else{
						addTab(data.funName,data.funUrl,data.funIcon,data.funSubsystem,data.funIframe);
					}
					$navlist.find("li[data-subsys='"+data.funSubsystem+"']").show();
					$("#indexBtn").parent("li").show();
					//					$tabbar.height($(window).height()-130);
//					var tabbarheight = $(window).height();
//					debugger;
//					$tabbar.tabs("resize");
//					$tabbar.tabs({onLoad:function(panel){
//						$tabbar.tabs("select", data.funName)
//						panel.panel('resize');
//					}})
					if(!isIE(8)){
						if(thdurl.indexOf("scheduleManagement") > -1){
							$tabbar.tabs('getSelected').height(800);
							//$.getScript("plug-in/assets/js/jquery-ui-1.10.4.dg.dp.rs.min.js");
						}else{
							$tabbar.tabs('getSelected').height($(window).height()-117);
//						$.getScript("plug-in/easyui/jquery.easyui.min.1.3.2.js");
						}
					}
					//					$pageDiv.empty().load(thdurl).show();
//					$pageDiv.height(window.screen.availHeight - $("#sidebar-shortcuts").height() - $("#navbar").height()-115);
					handleBreadcrumbs($(this), "func");
//					if(!$("#ncl-my").hasClass("active"))
//						$("#ncl-my").trigger("click");
					
//					$("#navbar-container > .navbar-header:first > a:first").addClass("active");
					//点击功能菜单时，触发LOGO的点击事件，以显示左侧快捷按钮和触发其他相关操作
					var $logo = $("#navbar-container > .navbar-header:first");
					if(!$logo.children("a:first").hasClass("active"))
						$logo.trigger("click");
				}
//				$tabbar.tabs('getSelected').height($(window).height()-130);
				
				if($sidebar.hasClass("menu-none")){
					$sidebar.removeClass("menu-none");
					$sidebar.find("ul.nav-list").removeClass("menu-none");
					$sidebar.find("div.sidebar-collapse").removeClass("menu-none");
				}
				
				if(!isIE(8))
					$tabbar.tabs('resize');
			});
	}
	
	handleSlimScrollBar($("#sidebar"), "show");
	
	$(window).data("btmFunListsData",btmFunListsData);

	$indexBtn.on("click", function() {
		$pageIndexDiv.show();
		$tabbar.hide();
		$divIE.hide();
		$("#navbar-container > .navbar-header:first").children("a:first").removeClass("active");
		$(".page-content").css("background-color","#E7E7E7");
		
		$sidebar.addClass("menu-none");
		$sidebar.find("ul.nav-list").addClass("menu-none");
		$sidebar.find("div.sidebar-collapse").addClass("menu-none");
		handleBreadcrumbs();
//		$tabbar.tabs('resize');
		resizeTabSize();
		
	});
	
	
	
	
	$("#innerMsg").on("click",function(){
//		alert("trigger");
		var datafunId = $(this).attr("data-funId");
		$("#"+datafunId+">a").trigger("click");
	})
	
	$("#annReturn").on("click",function(){
//		alert("trigger");
		var datafunId = $(this).attr("data-funId");
		$("#"+datafunId+">a").trigger("click");
	})
	
	$("#annApprove").on("click",function(){
//		alert("trigger");
		var datafunId = $(this).attr("data-funId");
		$("#"+datafunId+">a").trigger("click");
	})
	
	//json元素转换成DOM元素
	function _json2Dom(fdata) {
		var rtn = "<li id=\"" + fdata.funId + "\" data-subsys=\""+fdata.funSubsystem+"\">";
		rtn += "<a href=\"#\" "
			+ (fdata.funUrl == "" ? "" : ("data-url=\"" + fdata.funUrl + "\"")) 
			+ " class=\"" 
			+ (fdata.funCsize > 0 ? "dropdown-toggle" : "") 
			+ "\">";
		
		if (fdata.funPid == "" || fdata.funPid == "-1") {
			//处理根节点
			rtn += "<i class=\"" + fdata.funIcon + "\"></i>";
			rtn += "<span class=\"ace-menu-text\">" + fdata.funName + "</span>";
		} else {
			//处理根节点之外的所有节点
			rtn += "<i class=\"awsm-icon-double-angle-right\"></i>" + fdata.funName;
		}
		
		//处理有子元素的节点
		if (fdata.funCsize > 0)
			rtn += "<b class=\"arrow awsm-icon-angle-down\"></b>";
		
		rtn += "</a></li>";

		return rtn;
	}
}

//处理datagrid中“操作”列中的按钮tip
var handleTooltip = function(obj, type, title){
	var $yitipObj = $(obj).closest("a");
	$yitipObj.yitip({
		"position": "topMiddle",
		"offest": {"left":-2, "top":-3},
		"showEvent": "mouseover", 
		"hideEvent": "mouseout",
		"color": "black",
		"content": title,
		"hideDestroy":true,
		"effect": {show:{"speed":10},hide:{"speed":10}}
	});
}

//处理系统左侧功能列表固定时的滚动条
var handleSlimScrollBar = function(obj, type){
	var $navlist = $(obj).find(".nav-list"),
		fixedHeight = $(window).height() - $("#sidebar-shortcuts").height() - $("#navbar").height() - 28; //28为底部收缩箭头的高度（包括边框）
	if(type == "show"){
		$navlist.wrap("<div class='navlist-slimbar'></div>");
		$navlist.height(fixedHeight);
		$(".navlist-slimbar").slimScroll({
			height: fixedHeight,
			color: '#c2c2c2',
			size: '8px',
			alwaysVisible: false,
			wheelStep: 1
		});
	}else {
		$navlist.css({"height":"auto"});
		if($navlist.closest(".slimScrollDiv").length){
			$navlist.unwrap().unwrap().parent().find("div[class^='slimScroll']").remove();
		}
	}
}

//渲染EaryUI样式
var redrawEasyUI = function(obj){
	$.parser.parse(obj.parent());
}

//构造面包屑导航
var handleBreadcrumbs = function(obj, type){
	var $breadcrumb = $("ul.breadcrumb");
//	$breadcrumb.empty();
	
	var _handleBC = function(_obj){
//		debugger;
		var crtLi = "<li><a href='#'><span>"+_obj.text()+"</span></a></li>";
		if(isIE(8) && $breadcrumb.children("li").length > 1){
			$breadcrumb.children("li:last").remove();
		}
		$breadcrumb.append(crtLi);
		$breadcrumb.find("li").children("a").css("color","#4c8fbd");
		$breadcrumb.find("li:last").children("a").css("color","#555")
		$breadcrumb.find("li:last").on("click",function(){
//			console.log("click in");
			_obj.trigger("click");
			$breadcrumb.find("li").children("a").css("color","#4c8fbd");
			$(this).children("a").css("color","#555");
		});
//		if(_obj.closest("ul").attr("class").indexOf("nav-list") < 0){
//			_handleBC(_obj.closest("ul").parent("li").find("a:first"));
//		}
	}
	
	if(type == "func"){
		if($breadcrumb.find("a").text().indexOf(obj.text()) < 0)
			_handleBC(obj);
		//显示导航左侧的图标
//		var bcIcon = obj.parents("li.open:last").find("a > i").attr("class");
//		$breadcrumb.prepend("<i class='" + bcIcon + " home-icon'></i>");
		//最后一级内容去a标签，字体颜色显示为黑色
//		$breadcrumb.find("li:last span").unwrap();
	}else{
//		$breadcrumb.empty().prepend("<i class='awsm-icon-home home-icon'></i><li><a href='#'>首页</a></li>");
	}
	
}

function scheduleView(){
//	createwindow("日程管理","scheduleController.do?scheduleManagement" , 1200, 600, null, {isIframe:'true'});
	$("#sidebar").find("a[data-url*='scheduleManagement']").trigger("click");
}

function editSchedulePage(date){
	createwindow("添加日程","scheduleController.do?scheduleEdit&startDate="+date+"&endDate="+date , 400,420, null, {optFlag : 'add'});
}

var handleMyEventsList = function(obj, data){
	//处理数据库读取的事件列表
	$.ajax({
		async: false,
		url : "oftenScheduleController.do?oftenScheduleList",
		type : 'post',
		cache : false,
		success : function(doc) {
			data_i = doc.data;
			for(var i = 0; i < data_i.length; i++){
				var colorStyle = data_i[i].eventColor;
				obj.append("<div id=\""+ data_i[i].id +"\" class=\"external-event "+colorStyle+"\" data-class=\""
						+ colorStyle+"\"><i class=\"awsm-icon-move\"></i><span>"+data_i[i].eventName+"</span>" 
						+ "<div class=\"pull-right\">"
						+ "<span href=\"#\" onclick=\"editOftenSchedule('"+ data_i[i].id +"');\" class=\""+colorStyle+"\"><i class=\"awsm-icon-pencil bigger-110\"></i></span>" 
						+ "<span href=\"#\" onclick=\"removeOftenSchedule('"+ data_i[i].id +"');\" class=\""+colorStyle+"\"><i class=\"awsm-icon-remove bigger-110\"></i></span>"
						+ "</div></div>");
			}
		}
	});
	//obj.append("<label id=\"removeLab\"><input type=\"checkbox\" class=\"ace ace-checkbox\" id=\"drop-remove\" /><span class=\"lbl\">拖拽后删除</span></label>");
	
	//绑定新增事件处理函数
	$(".external-events-title a").on("click",function(){
		$("#oftenTitle").val('');
    	$("#oftenContext").val('');
    	//$("#oftenColorStyle").ColorPickerSetColor("#a0a0a0");
    	//$("#oftenColorStyle").val('');
    	//$("#oftenColorStyle option[text='#a0a0a0']").attr("selected",true);
		$("#oftenScheduleEdit").dialog({
		    bgiframe: true,
		    resizable: false,
		    width:340,
		    height:360,
		    position: ["auto",40],
		    modal: true,
		    dialogClass: "ucg-ui-dialog",
		    overlay: {
		        backgroundColor: '#000',
		        opacity: 0.5
		    },
		    buttons: [{
		    	html: "<i class='awsm-icon-save bigger-110'></i>&nbsp;保存",
				"class" : "btn btn-info btn-xs",
		    	click: function() {
		        	var title = $("#oftenTitle").val();
					var context = $("#oftenContext").val();
					var classStyle = excgColor("", $("#oftenColorStyle").val());
					var url = "oftenScheduleController.do?saveOrUpdate&title="+title+"&context="+context+"&colorStyle="+classStyle;
					$.ajax({
						url : encodeURI(encodeURI(url)),
						type : 'post',
						data : {
							id : ''
						},
						cache : false,
						success : function(data) {
							if($("#external-events div:first-child")[0]){
								$("#external-events div:first-child").clone(false,false).prependTo($("#external-events"));
								var obj = $("#external-events div:first-child");
								
								obj.removeClass(obj.attr("data-class")).addClass(data.colorStyle);
								obj.find(".pull-right span").removeClass(obj.attr("data-class")).addClass(data.colorStyle).removeAttr("onclick");
								obj.find("span:first").text(data.title);
								obj.attr("data-class", data.colorStyle);
								obj.attr("id", data.id);
								obj.find(".pull-right span:first").on("click", function(){
									editOftenSchedule(data.id);
								});
								obj.find(".pull-right span:last").on("click", function(){
									removeOftenSchedule(data.id);
								});
								obj.draggable({
									zIndex : 999,
									revert : true, 
									revertDuration : 0
								});
							}else{
								var colorStyle = data.colorStyle;
								$("<div id=\""+ data.id +"\" class=\"external-event "+colorStyle+"\" data-class=\""
										+ colorStyle+"\"><i class=\"awsm-icon-move\"></i><span>"+data.title+"</span>" 
										+ "<div class=\"pull-right\">"
										+ "<span href=\"#\" onclick=\"editOftenSchedule('"+ data.id +"');\" class=\""+colorStyle+"\"><i class=\"awsm-icon-pencil bigger-110\"></i></span>" 
										+ "<span href=\"#\" onclick=\"removeOftenSchedule('"+ data.id +"');\" class=\""+colorStyle+"\"><i class=\"awsm-icon-remove bigger-110\"></i></span>"
										+ "</div></div>").prependTo($("#external-events"));
								$('#external-events div.external-event').each(function() {
									var eventObject = {
										title : $.trim($(this).text())
									};
									$(this).data('eventObject', eventObject);
									$(this).draggable({
										zIndex : 999,
										revert : true,
										revertDuration : 0
									});
								});
							}
						}
					});
		            $(this).dialog('close');
		            $(this).dialog('destroy');
		        }
		    },{
		    	html: "<i class='awsm-icon-remove bigger-110'></i>&nbsp;取消",
				"class" : "btn btn-info btn-xs",
		    	click: function() {
		    		$(this).dialog('close');
		    		$(this).dialog('destroy');
		    	}
		    }]
		});
	});
}

function editSchedule(id){
	createwindow("添加日程","scheduleController.do?scheduleEdit&id="+id, 400,420, null, {optFlag : 'add'});
}

function removeSchedule(id){
	bootbox.confirm("确定删除吗？", function(rst){
		if(rst){
			$.ajax({
				url : 'scheduleController.do?delete',
				type : 'post',
				data : {
					ids : id
				},
				cache : false,
				success : function(data) {
					var d = $.parseJSON(data);
					if (d.success) {
						scheduleMainTabs();
					}
				}
			});
		}
	});
}	

function editOftenSchedule(id){
	$.ajax({
		url : 'oftenScheduleController.do?findById&id='+id,
		type : 'post',
		data : {
			id : id
		},
		cache : false,
		success : function(data) {
			$("#oftenTitle").val(data.title);
        	$("#oftenContext").val(data.context);
        	//debugger;
        	var classStyle = excgColor(data.colorStyle, "");
			//$("#oftenColorStyle").val(classStyle);
			$("#oftenColorStyle option[text='"+data.colorStyle+"']").attr("selected",true);
			$("#oftenScheduleEdit").dialog({
			    bgiframe: true,
			    resizable: false,
			    width:340,
			    height:360,
			    position: ["auto",40],
			    modal: true,
			    dialogClass: "ucg-ui-dialog",
			    overlay: {
			        backgroundColor: '#000',
			        opacity: 0.5
			    },
			    buttons: [{
			    	html: "<i class='awsm-icon-save bigger-110'></i>&nbsp;保存",
					"class" : "btn btn-info btn-xs",
			    	click: function() {
			    		document.scheduleDialogForm.reset();
			    		var title = $("#oftenTitle").val();
			    		var context = $("#oftenContext").val();
			    		var classStyle = excgColor("", $("#oftenColorStyle").val());
			    		var url = "oftenScheduleController.do?saveOrUpdate&id="+id+"&title="+title+"&context="+context+"&colorStyle="+classStyle;
			    		$.ajax({
			    			url : encodeURI(encodeURI(url)),
			    			type : 'post',
			    			data : {
			    				id : id
			    			},
			    			cache : false,
			    			success : function(data) {
			    				$("#"+id).removeClass($("#"+id).attr("data-class")).addClass(data.colorStyle);
			    				$("#"+id).find(".pull-right a").removeClass($("#"+id).attr("data-class")).addClass(data.colorStyle);
			    				$("#"+id).find("span:first").text(data.title);
			    				$("#"+id).attr("data-class", data.colorStyle);
			    				//scheduleMainTabs();
			    			}
			    		});
			    		$(this).dialog('close');
			    		$(this).dialog('destroy');
			    	}
			    },{
			    	html: "<i class='awsm-icon-remove bigger-110'></i>&nbsp;取消",
					"class" : "btn btn-info btn-xs",
			    	click: function() {
			    		$(this).dialog('close');
			    		$(this).dialog('destroy');
			    	}
			    }]
			});
		}
	});
	//createwindow("修改事件", "oftenScheduleController.do?oftenScheduleEdit&id="+id, 400,400, null, {optFlag : 'update'});
}
function removeOftenSchedule(id){
	bootbox.confirm("确定删除吗？", function(rst){
		if(rst){
			$.ajax({
				url : 'oftenScheduleController.do?delete',
				type : 'post',
				data : {
					ids : id
				},
				cache : false,
				success : function(data) {
					var d = $.parseJSON(data);
					if (d.success) {
						$("#"+id).remove();
					}
				}
			});
		}
	});
	
}	

var createEventsDiolog = function(elements){
//	elements = "{\"data\":["
//		+"{\"t_lable\":\"标题\", \"i_id\":\"title\", \"i_name\":\"title\", \"i_type\":\"text\", \"i_value\":\"test2\"},"
//		+"{\"t_lable\":\"开始时间\", \"i_id\":\"startDate\", \"i_name\":\"startDate\", \"i_type\":\"text\", \"i_value\":\"test3\"},"
//		+"{\"t_lable\":\"结束时间\", \"i_id\":\"endDate\", \"i_name\":\"endDate\", \"i_type\":\"text\", \"i_value\":\"test4\"},"
//		+"{\"t_lable\":\"内容\", \"i_id\":\"context\", \"i_name\":\"context\", \"i_type\":\"text\", \"i_value\":\"test5\"}"
//		+"]}";
	var data = elements.data;
	var ctn = "<form class='form-inline formtable'>";
	ctn += "<table id='scheduleTab'>";
	for(var i = 0; i < data.length; i++){
		ctn += "<tr>";
		ctn += "<td class='td_title'><label class='Validform_label'>"+data[i].t_lable+"：</label></td>";
		ctn += "<td class='value'>";
		if(data[i].i_type == "select"){
			ctn += "<select id='colorStyle' name='colorStyle' class='hide'>";
			var colorStyle = excgColor(data[i].selectValue, "");
			for(j in data[i].i_value){
				if(data[i].i_value[j].value == colorStyle){
					ctn += "<option data-class='"+data[i].i_value[j].dataclass+"' selected='selected' value='"+data[i].i_value[j].value+"'>"+data[i].i_value[j].value+"</option>";
				}else{
					ctn += "<option data-class='"+data[i].i_value[j].dataclass+"' value='"+data[i].i_value[j].value+"'>"+data[i].i_value[j].value+"</option>";
				}
			}
			ctn += "</select>"
		}else if(data[i].i_type == "textarea"){
			ctn += "<textarea row='5' style='width:100%' id='"+data[i].i_id+"' name='"+data[i].i_name+"' value=''>"+data[i].i_value+"</textarea>";
		}else if(data[i].i_id == "n_psd"){
			ctn += "<span><input  class='inputxt' id='"+data[i].i_id+"' name='"+data[i].i_name+"' type="+data[i].i_type+" value='" + data[i].i_value + "' /></span>";
		}else{
			ctn += "<input  class='inputxt' id='"+data[i].i_id+"' name='"+data[i].i_name+"' type="+data[i].i_type+" value='" + data[i].i_value + "' />";
		}
		ctn += "</tr>";
	}
	ctn += "</td>";
	ctn += "</table>";
	ctn += "</form>";
	
	return ctn;
	
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

function repeat(id,str){
	$("#"+id).addClass('has-error');
	$("#"+id).attr({
		"data-toggle": 'tooltip',
		"data-title": str,
		"data-original-title":str,
		"data-placement": "right",
		"data-container": "body",
		"data-trigger": "manual"
	});
	$("#"+id).tooltip("show");
	$("#"+id).on('keydown', function(event) {
		// event.preventDefault();	//取消事件绑定的默认行为，比如输入内容（按键后无反应）
		$("#"+id).removeClass('has-error');
	});
	setTimeout(function(){
		$("#"+id).tooltip("destroy");
	},2000);
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

//显示tooltip气泡提示
function showTooltip(opt){
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
//		"data-container": "div.datagrid-view",  //test
		"data-trigger": opt.trg || "manual",  //manual: 手动
		"data-html": opt.html || "",
		"data-template": opt.tpl
	});
	
	obj.tooltip();
	if(opt.show || opt.show == null)
		obj.tooltip("show");
	
	if(opt.destroy || opt.destroy == null)
		setTimeout(function(){obj.tooltip("destroy");},20000);
//		setTimeout(function(){$(".tooltip").remove();},2000);
}

//检测密码强度
var checkPsdStrong = function(obj, psd){
	function _CharMode(iN){  
		if (iN>=48 && iN <=57) //数字  
			return 1;  
		if (iN>=65 && iN <=90) //大写  
			return 2;  
		if (iN>=97 && iN <=122) //小写  
			return 4;  
		else  
		return 8;   
	}  
	//_bitTotal函数  
	//计算密码模式  
	function _bitTotal(num){
		modes=0;
		for (i=0;i<4;i++){
			if (num & 1) modes++;
				num>>>=1;
		}
		return modes;  
	}
	//返回强度级别  
	function _checkStrong(sPW){  
		if (sPW.length<=5)  
			return 0; //密码太短  
		Modes=0;  
		for (i=0;i<sPW.length;i++){  
			//密码模式  
			Modes|=_CharMode(sPW.charCodeAt(i));  
		}  
		return _bitTotal(Modes);  
	}  
	  
	//返回  
	function _pwStrength(pwd){
		var rtn = {},
			O_color="#fff", 
			L_color="#d15b47",
			M_color="#e59729",
			H_color="#629b58";
		if (pwd==null||pwd==''){  
			rtn.color =  O_color;
			rtn.text =  "弱";
			rtn.level =  0;
		}else{  
			var S_level=_checkStrong(pwd); 
			rtn.level =  S_level;
			$("#new_psd_val").attr("oldValue",S_level);
			switch(S_level) {  
				case 0:  
					rtn.color =  O_color;
					rtn.text =  "";
				case 1:  
					rtn.color =  L_color;  
					rtn.text =  "弱";
					break;  
				case 2:  
					rtn.color =  M_color; 
					rtn.text =  "中";
					break;  
				default:  
					rtn.color =  H_color;  
					rtn.text =  "强";
			}  
		}  
		return rtn;  
	}
	var pst_strong = "<div class='pst-strong'>"
		+ "<span>密码强度</span><span><i class='awsm-icon-question-sign blue'/>：</span><div><div>弱</div><div>中</div><div>强</div></div>"
		+ "</div>";
	
	var psd_info = _pwStrength(psd);
	$(".pst-strong").data("psd_level",psd_info.level);
	if(!$(".pst-strong").length) 
		//$(obj).parent().after(pst_strong);
	$(obj).parent().parent().find("td").eq("2").html(pst_strong)
	if(obj.value != ""){
		if(psd_info.level == 0) psd_info.level = 1;
		$(".pst-strong>div>div").hide();
		$(".pst-strong>div>div:lt("+psd_info.level+")").css({background: psd_info.color, display:"inline-block"});
		$(".pst-strong").css({display: "inline-block"});
	}else{
		$(".pst-strong").hide("fast");
	}
	showTooltip({obj:$(".pst-strong .awsm-icon-question-sign").parent(), msg:"密码可包含字母、数字、符号；请尽量设置复杂的密码组合，以保障您的帐号安全。", placement:"right", trg:"hover focus click", show:false, destroy:false});
}

//通过文件后缀名，返回文件图标对应的class
var getFileIcon = function(ext){
	var rtn;
	switch(ext){
		case ".txt":
			rtn = "awsm-icon-file-text-alt grey";
			break;
		case ".pdf":
			rtn = "awsm-icon-file-pdf-o red";
			break;
		case ".doc":case ".docx":
			rtn = "awsm-icon-file-word-o blue";
			break;
		case ".xlsx":case ".xls":case ".csv":
			rtn = "awsm-icon-file-excel-o green";
			break;
		case ".ppt":case ".pptx":
			rtn = "awsm-icon-file-powerpoint-o red";
			break;
		case ".jpg":case ".gif":case ".png":case ".bmp":case ".pic":
			rtn = "awsm-icon-file-image-o purple";
			break;
		case ".zip":case ".rar":
			rtn = "awsm-icon-file-zip-o blue";
			break;
		case ".wav":case ".mp3":case ".ram":
			rtn = "awsm-icon-file-audio-o orange";
			break;
		case ".avi":case ".mp4":case ".rmvb":case ".rm":
			rtn = "awsm-icon-file-video-o light-orange";
			break;
		case ".htm":case ".html":case ".js":case ".css":case ".java":case ".class":
			rtn = "awsm-icon-file-code-o grey";
			break;
		default:
			rtn = "awsm-icon-file blue";
			break;
	}
	
	return rtn;
}

var loadBottomFunList = function(){
	var data = $(window).data("btmFunListsData");
	
	var winWidth = $("#navbar").width(),
		funListWidth = winWidth-24,
		funListCtnWidth = funListWidth*0.9,
		funIconWidth = 64+2*7+1,
		funLists = data.data,
		maxFunNum = funLists.length,
		maxRowIconNum = parseInt(funListCtnWidth/funIconWidth),
		maxRowNum = Math.ceil(maxFunNum/maxRowIconNum),
		$carouselInner = $("#carousel-funlist .carousel-inner"),
		rootFuns = [],
		tempRoots = "",
		subSystemInfo = [],
		subSystemIcon = "",
		subSystemName = "";
	
	//查询子系统图标及中文名
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : "moduleController.do?querySubsystemInfo",
		error : function() {
			bootbox.alert("请求失败！"); // 请求失败处理函数
		},
		success : function(result) {
			if(result){
				subSystemInfo=$.parseJSON(result);
			}
		}
	});
//	console.log(subSystemInfo);
	$carouselInner.empty();
	//遍历生成root节点
	for(var i = 0; i < maxFunNum; i++){
		//由于主要的功能节点funSubsystem字段都没有值，所以暂时给funSubsystem字段为空的节点指定固定子系统名，后期完善后，可取消此设置
		if(funLists[i].funSubsystem.length < 1)
			funLists[i].funSubsystem = "sysFuns";
		
		if(tempRoots.indexOf(funLists[i].funSubsystem) < 0){
			if(!subSystemInfo[funLists[i].funSubsystem] || !subSystemInfo[funLists[i].funSubsystem][0]) 
				subSystemIcon = "awsm-icon-info";
			else 
				subSystemIcon = subSystemInfo[funLists[i].funSubsystem][0];
			
			if(!subSystemInfo[funLists[i].funSubsystem] || !subSystemInfo[funLists[i].funSubsystem][1]) 
				subSystemName = "首页";
			else 
				subSystemName = subSystemInfo[funLists[i].funSubsystem][1];
//			debugger;
//			console.log(subSystemIcon);
			tempRoots += (funLists[i].funSubsystem + ",");
			rootFuns.push({"funCode":funLists[i].funSubsystem, "funIcon":subSystemIcon, "funIconSize":"bigger-120", "funName":subSystemName});
		}
	}
	$(window).data("subSystems",rootFuns);
	for(var i = 0; i < rootFuns.length; i++){
		$carouselInner.append(
				"<li class='fun-icon' title='"+rootFuns[i].funName+"' data-funName='"+rootFuns[i].funName+"'>"
				+ "<div class='fun-icon-inner'>"
				+ "<i class='" + rootFuns[i].funIcon + " " + rootFuns[i].funIconSize + "'></i>"
				+ "</div>"
				+ "</li>"
		);
//		console.log("root:"+rootFuns[i].funName);
		//生成具体功能leaf节点
		for(var j = 0; j < maxFunNum; j++){
			if(funLists[j].funSubsystem == rootFuns[i].funCode){
				var currentWidth = 0,
					currentHtml = "<a data-funId='"+funLists[j].funId+"' href='#' data-subsys='"+funLists[j].funSubsystem+"' data-temp='"+i+","+j+"'>"+funLists[j].funName+"</a>",
					$currentLastLi = $carouselInner.children("li:last");
				if($currentLastLi.children("a").length == 1){
					$currentLastLi.append(currentHtml);
				}else{
//					debugger;
					if($currentLastLi.length)
						$currentLastLi.after("<li class='fun-link'>" + currentHtml + "</li>");
					else
						$carouselInner.append("<li class='fun-link'>" + currentHtml + "</li>");
				}
//				console.log("leaf:"+funLists[j].funName);
				
//				var $currentEle = $("a[data-funId='"+funLists[j].funId+"']").parent(".fun-link");
				var $currentEle = $carouselInner.children("li:last");
				$currentEle.prevAll("li").each(function(){
					//计算当前产生的所有li元素总宽度，14号字体每个字宽度为14px，每个li边距5px，之外还有默认边距5px
					if($(this).hasClass("fun-link")){
						currentWidth += (Math.max($(this).children("a:first").text().length, $(this).children("a:last").text().length)*14+15+14);
					}else{
						//每个icon元素宽度85px
						currentWidth += (85+5+5);
					}
				});
				if((currentWidth+80) > funListCtnWidth){
//					var $innerLi = $carouselInner.children("li[class='fun-icon']:last").prevAll();
					var $innerLi = $carouselInner.children("li");
					var funiconNum = $carouselInner.children("li.fun-icon").length;
					if(funiconNum > 1) 
						$innerLi = $carouselInner.children("li").not($carouselInner.children("li.fun-icon:last").nextAll("li").andSelf());
					
					$innerLi.wrapAll("<div class='item'>")
						.wrapAll("<div class='carousel-content'></div>")
						.wrapAll("<ul class='carousel-content-list'></ul>");
				}
			}
		}
	}
	$carouselInner.children("li")
		.wrapAll("<div class='item'>")
		.wrapAll("<div class='carousel-content'></div>")
		.wrapAll("<ul class='carousel-content-list'></ul>");
		
	$carouselInner.find(".item:first").addClass("active");
	
	//如果一个子系统只有一个功能，则显示为垂直居中
	$carouselInner.find("li.fun-icon").each(function(){
		var innerLi = $(this).nextUntil("li.fun-icon");
		if(innerLi.length == 1 && $(innerLi[0]).children().length == 1)
			$(innerLi[0]).css("vertical-align","baseline");
	});
	
//	$carouselInner.find("li.fun-link").each(function(){
//		if($(this).children("a").length == 1){
//			$(this).css("vertical-align","baseline");
//		}
//	});
	
	$carouselInner.find("a").on("click",function(){
		var datafunId = $(this).attr("data-funId");
		$("#"+datafunId+">a").trigger("click");
	})
	
}
function getLocalTime(nS) {
    return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");      
 }

/** 修改个人资料 */
function userInfo(){
	createwindow("个人资料","employeeController.do?userInfoEdit", null, 480, 2, {optFlag:'add',formId:'form_userInfoEdit'});
}
/** 问题反馈**/
$("#ace-feedback-btn").on("click",function(){
	var winWidth = $(window).width();
    var winHeight = $(window).height();
    var x = winWidth - 330;
    var y = winHeight - 390;
	createwindow("问题反馈", "problemController.do?editPage", 330, 390,"",{optFlag:'send', left:x, top:y}); 
});

var loadTimeline = function(obj, data){
	obj.empty();
	//下面data1是写死的,没用处了,只是展示本控件需要什么样的数据,传入的data才是真实渲染的数据
	data1 = {"data":[{
		"t_lable":"今天",  //每一天的时间轴项，最上方显示的lable
		"t_lable_type":"label-primary",
		"t_items":[{
			"t_item_info_img":"plug-in/assets/avatars/user.jpg",  //每一项内容左侧头像图片
			"t_item_info_time":"16:52", //左侧头像下方时间
			"t_item_content_header_color":"",  //某一项事件显示的颜色，如 header-color-red2，暂未启用
			"t_item_content_header_user":"张三",  //事件标题栏，最左侧的用户名
			"t_item_content_header_task":"总经理调查审批",  //标题栏，用户名右侧的事件名称
			"t_item_content_header_cost":"2小时",  //标题栏，用户名右侧的事件名称
			"t_item_content_header_time":"2014-12-13 16:22",  //标题栏最右侧的时间
			"t_item_content_header_toolbar":[{
				"tool_action":"collapse",
				"tool_icon":"awsm-icon-chevron-up",
				"tool_link":"#"
			}],
			"t_item_content_body_content":"该用户在X年X月X日审批了这个流程。",  //事件内容区域的文本
			"t_item_content_body_toolbar":[{  //内容区域的工具栏
				"tool_position_class":"pull_left",  //左侧工具栏
				"tool_tools":[{
					"tool_icon":"awsm-icon-flag-checkered",
					"tool_color":"grey",  //按钮颜色
					"tool_text":"否定"  //按钮图标右侧文本
				}]
			}]
		},{
			"t_item_info_img":"plug-in/assets/avatars/avatar1.png",
			"t_item_info_time":"16:22",
			"t_item_content_header_color":"header-color-red2",  
			"t_item_content_header_title":"审批一个流程",
			"t_item_content_header_time":"16:22",
			"t_item_content_body_content":"该用户在X年X月X日审批了这个流程。",
			"t_item_content_body_toolbar":[{
				"tool_position_class":"pull_left",
				"tool_tools":[{
					"tool_icon":"awsm-icon-hand-right",
					"tool_color":"grey",
					"tool_text":"点击查看详情&hellip;"
				}]
			},{
				"tool_position_class":"pull_right",
				"tool_tools":[{
					"tool_icon":"awsm-icon-ok",
					"tool_color":"green",
					"tool_size":"bigger-130"
				},{
					"tool_icon":"awsm-icon-pencil",
					"tool_color":"blue",
					"tool_size":"bigger-125"
				},{
					"tool_icon":"awsm-icon-remove",
					"tool_color":"red",
					"tool_size":"bigger-125"
				}]
			}]
		}]
	},{
		"t_lable":"昨天",
		"t_lable_type":"label-success",
		"t_items":[{
			"t_item_info_img":"plug-in/assets/avatars/avatar2.png",
			"t_item_info_time":"16:22",
			"t_item_content_header_color":"", 
			"t_item_content_header_user":"张三",
			"t_item_content_header_title":"审批一个流程",
			"t_item_content_header_time":"16:22",
			"t_item_content_header_toolbar":[{
				"tool_action":"reload",
				"tool_icon":"awsm-icon-refresh",
				"tool_link":"#"
			},{
				"tool_action":"collapse",
				"tool_icon":"awsm-icon-chevron-up",
				"tool_link":"#"
			}],
			"t_item_content_body_content":"该用户在X年X月X日审批了这个流程。",
			"t_item_content_body_toolbar":[{
				"tool_position_class":"pull_right",
				"tool_tools":[{
					"tool_icon":"awsm-icon-ok",
					"tool_color":"green",
					"tool_size":"bigger-130"
				},{
					"tool_icon":"awsm-icon-pencil",
					"tool_color":"blue",
					"tool_size":"bigger-125"
				}]
			}]
		},{
			"t_item_info_img":"plug-in/assets/avatars/avatar3.png",
			"t_item_info_time":"16:22",
			"t_item_content_header_color":"", 
			"t_item_content_header_user":"张三",
			"t_item_content_header_title":"审批一个流程",
			"t_item_content_header_time":"16:22",
			"t_item_content_header_toolbar":[{
				"tool_action":"reload",
				"tool_icon":"awsm-icon-refresh",
				"tool_link":"#"
			},{
				"tool_action":"collapse",
				"tool_icon":"awsm-icon-chevron-up",
				"tool_link":"#"
			}],
			"t_item_content_body_content":"该用户在X年X月X日审批了这个流程。",
			"t_item_content_body_toolbar":[{
				"tool_position_class":"pull_left",
				"tool_tools":[{
					"tool_icon":"awsm-icon-hand-right",
					"tool_color":"grey",
					"tool_text":"点击查看详情&hellip;"
				}]
			}]
		}]
	}]};
//	data = $.parseJSON(data);
	
	
	var timelineHtml = "";
	for(var i = 0; i < data.data.length; i++){
		var timelineData = data.data[i];
		timelineHtml += "<div class='timeline-container'>"
			+"<div class='timeline-label'>"
			+"<span class='label "+timelineData.t_lable_type+" arrowed-in-right label-lg'>" 
			+"<b>"+timelineData.t_lable+"</b>"
			+"</span>"
			+"</div>"
			+"<div class='timeline-items'>"; 
			
		for(var j = 0; j < timelineData.t_items.length; j++){
			var itemData = timelineData.t_items[j];
			timelineHtml += "<div class='timeline-item clearfix'>"
				+"<div class='timeline-info'>"
				+"<img alt='图片加载失败' src='"+(itemData.t_item_info_img==null?"plug-in/assets/avatars/avatar_80.png":itemData.t_item_info_img)+"'/>"
				+"<span class='label label-info label-sm'>"+itemData.t_item_info_time+"</span>"
				+"</div>"
				+"<div class='widget-box transparent'>"  
				+"<div class='widget-header "+itemData.t_item_content_header_color+" widget-header-small'>"
				+"<h5 class='smaller'>"
				+"<a href='#' class='blue'>"+(itemData.t_item_content_header_user == null ? "「暂无」" : itemData.t_item_content_header_user)+"</a>"
				+"<span class='grey'>&nbsp;任务:</span>"
				+"<span class='blue'>"+(itemData.t_item_content_header_task== null ? "「暂无」" : itemData.t_item_content_header_task)+"</span>"
				+"<span class='grey'>&nbsp;用时:</span>"
				+"<span class='blue'>"+(itemData.t_item_content_header_cost== null ? "「暂无」" : itemData.t_item_content_header_cost)+"</span>"
				+"</h5>"
			if(itemData.t_item_content_header_toolbar){
				timelineHtml += "<span class='widget-toolbar' >";
				for(var k = 0; k < itemData.t_item_content_header_toolbar.length; k++){
					var toolsData = itemData.t_item_content_header_toolbar[k];
					timelineHtml += "<a href='"+toolsData.tool_link+"' data-action='"+toolsData.tool_action+"'><i class='"+toolsData.tool_icon+"'></i></a>";
				}
				timelineHtml +="</span>";
			}
			timelineHtml +="<span class='widget-toolbar no-border'><i class='icon-time bigger-110'></i>"+itemData.t_item_content_header_time+"</span>";
			timelineHtml += "</div>"  //+"<div class=''></div>"
				+"<div class='widget-body'>"
				+"<div class='widget-main'>"
				+(itemData.t_item_content_body_content==null?"":itemData.t_item_content_body_content)
				+"<div class='space-6'></div>"
				+"<div class='widget-toolbox clearfix'>";
			if(itemData.t_item_content_body_toolbar){
				for(var m = 0; m < itemData.t_item_content_body_toolbar.length; m++){
					var bodyToolbarData = itemData.t_item_content_body_toolbar[m];
					if(bodyToolbarData.tool_position_class == "pull_left"){
						timelineHtml += "<div class='pull-left'>"
							+ "<i class='"+bodyToolbarData.tool_tools[0].tool_icon+" "+bodyToolbarData.tool_tools[0].tool_color+" bigger-125'></i>&nbsp;<a href='#' class='blue bigger-120' style='cursor:default'>"+(bodyToolbarData.tool_tools[0].tool_text==null?"":bodyToolbarData.tool_tools[0].tool_text)+"</a>"
							+"</div>";
					}else{
						timelineHtml += "<div class='pull-right action-buttons'>";
						for(var n = 0; n < bodyToolbarData.tool_tools.length; n++){
							var rightToolsData = bodyToolbarData.tool_tools[n];
							timelineHtml += "<a href='#'><i class='"+rightToolsData.tool_icon+" "+rightToolsData.tool_color+" "+rightToolsData.tool_size+"'></i></a>";
						}
						timelineHtml += "</div>";
					}
				}
				
			}
				
			timelineHtml += "</div>"
				+"</div>"
				+"</div>"
				+"</div>"
				+"</div>";
		}
		timelineHtml += "</div>"
			+"</div>";
	}
	obj.append("<div class='row'><div class='col-xs-12 col-sm-10 col-sm-offset-1 timeline-content'></div></div>");
	var $timelineContent = obj.children(".row").children(".timeline-content");
	$timelineContent.html(timelineHtml);
}

//处理tab panel右上角的下拉列表
var handleTabpanelDropdown = function(obj){
	var $d = $(".ucg-panelinner-dropdown"),
		$d_li = $(".ucg-panelinner-dropdown > ul > li"),
		$d_div = $(".ucg-panelinner-dropdown > div");
	
	$d_li.each(function(){
		if($(this).width() > $d_div.width())
			$d_div.width($(this).width());
	});
	
	$d_li.on("click", function(){
		$d_div.text($(this).text());
		$d_li.removeClass("active");
		$(this).addClass("active");
	});
//	debugger;
	$d.detach();
	obj.after($d);
//	alert(obj.html());
}

var main = {
	/******************** 首页初始化方法 ********************/
	initIndex:function(){
		//加载系统树列表
		main.loadSysFunc();
		//加载常用功能
		//加载站内信(消息)数据(每5秒执行1次)
//		main.queryInnerMsgData();
		//加载待办任务
//		main.loadMyTask();
		//setInterval("main.queryInnerMsgData();", 500000);
		//处理右上角按钮触发显示的功能菜单
		var $ucgFunlist = $(".ucg-funlist"),
			$navbarFunlistDropdown = $(".navbar-funlist-dropdown");
		$(".navbar-funlist-dropdown").click(function(){
			$(this).toggleClass("open");
			if($ucgFunlist.css("display") == "none"){
				$ucgFunlist.stop().width($("#navbar").width()).slideDown(100);
//				$ucgFunlist.height(69);
			}else{
				$ucgFunlist.stop().slideUp(100);
			}
		});
		$("div.main-container").on("click",function(){
			$navbarFunlistDropdown.removeClass("open");
			$ucgFunlist.stop().slideUp(100);
		});
		var timeoutId;
		$ucgFunlist.on("mouseleave",function(){
			timeoutId = setTimeout(function(){
				$navbarFunlistDropdown.removeClass("open");
				$ucgFunlist.stop().slideUp(100);
			},1000);
		});
		$ucgFunlist.on("mouseenter",function(){
			clearTimeout(timeoutId);
		});
		
		//改变窗口大小时，重新加载底部菜单
		$(window).resize(function(){
			loadBottomFunList();
		});
		
		//点击系统LOGO的操作链接  navbar-brand
		$("#navbar-container > .navbar-header:first").on("click", function(){
//			return;
			var $sidebar = $(".sidebar"),
				$ucgSidebar = $(".ucg-sidebar"),
				$thisChildrenA = $(this).children("a:first"),
				$pageContent = $(".page-content");
			
			if($thisChildrenA.hasClass("active")){
//				$("#indexBtn").trigger("click");
//				$sidebar.addClass("menu-none");
//				$ucgSidebar.addClass("menu-none");
//				$ucgSidebar.hide();
				$thisChildrenA.removeClass("active");
				$pageContent.css("background-color","#E7E7E7");
			}else{ //ncl-index
				if(!$("#tabbar .tabs > li").length){
//					bootbox.alert("请先通过系统上方功能菜单选择相应功能！");
				}else{
//					$("#page_index").hide();
//					$("#tabbar").show();
//					$sidebar.removeClass("menu-none");
//					$ucgSidebar.removeClass("menu-none");
//					$ucgSidebar.show();
					$thisChildrenA.addClass("active");
					//$pageContent.css("background-color","#FFF");
				}
			}
		});
		
		//针对第二种boot tab样式，因为所有tab panel一次性加载，除第一个panel外，其他panel内容区域初始化没有宽度，导致加载easyui表格无法显示，所以需要延迟触发重新渲染一下
		$(".ucg-sysTask-titletab .nav-tabs > li > a").on("click", function(){
			setTimeout(function(){
				redrawEasyUI($("div[data-flag='completeTaskListPage-panel']"));
			},100);
		});
		
		main.scheduleMainTabs();
		
		//由于页面元素加载顺序不同，导致首页下方的“系统任务”，“系统消息”两个模块先于页面滚动条出现，致使模块内部panel元素宽度自动计算有误差
		//所以首页初始化最后，把panel宽度重置
		$(".panel-resize-flag").panel("resize",{width:"auto"});
		
		//点击面包屑导航上的“首页”链接时，触发树菜单上的“首页”按钮
		$("#breadcrumb_indexBtn").on("click", function(){
			$("#indexBtn").trigger("click");
		});
		
		//“我的日程”功能上的“我的日历”按钮，点击触发相应操作
		$("#scheduleMainTab_right").css("cursor","pointer").on("click", function(){
			scheduleView();
		});
	},
	/******************** 系统消息相关 ********************/
	//消息弹出框
	showMsg : function(url) {
		createwindow("系统消息", url, 500, 350);
	},
	//进入消息列表
	showMsgList : function() {
		$pageIndexDiv = $("#page_index"), // 首页内容区域
		$tabbar = $("#tabbar");
		$pageIndexDiv.hide();
		$tabbar.show();

		var url = "messageController.do?sysMsgList";
		addTab("系统消息", url, null, null, false);
		if(!isIE(8)){
			$tabbar.tabs('getSelected').height($(window).height() - 130);
			$tabbar.tabs('resize');
		}
		var $logo = $("#navbar-container > .navbar-header:first");
		if (!$logo.children("a:first").hasClass("active"))
			$logo.trigger("click");

	},
	// 加载站内信(消息)数据
	queryInnerMsgData : function() {
		$.ajax({
			async : true,
			cache : false,
			type : 'POST',
			url : "messageController.do?queryInnerMsgData",
			error : function() {
				bootbox.alert("请求失败！"); // 请求失败处理函数
			},
			success : function(resule) {
				data = $.parseJSON(resule);
				main.parseInnerMsgData(data);
			}
		});
	},
	//通过加载的数据,渲染站内信(消息)界面
	parseInnerMsgData : function(data) {
		var main = $("#innerMessageArea"),
			msgsNum = data.category.length;
		var content = '<a data-toggle="dropdown" class="dropdown-toggle" href="#">'
			+ '<i class="awsm-icon-envelope awsm-icon-animated-vertical"></i>'
			+ (data.total > 0 ? '<span class="badge badge-success">'+ data.total +'</span>' : '')
			+ '</a>'
			+ '<ul class="pull-right dropdown-navbar navbar-grey dropdown-menu dropdown-caret dropdown-close" id="innerMessageUl">'
			+ '<li class="dropdown-header" ><i class="awsm-icon-envelope-alt"></i>共' + data.total + '条消息</li>';
		for (var i = 0; i < msgsNum; i++) {
			var category = data.category[i];
			if (category.msgs && category.msgs.length > 0) {
				content += '<li class="dropdown-hover"><a href="#"><div class="clearfix"><span class="pull-left">'
						+ category.title + '</span><span class="pull-right badge badge-info">' + category.count
						+ '</span></div></a>' + '<ul class="dropdown-menu secItem" >';
				for (var j = 0; j < category.msgs.length; j++) {
					var msg = category.msgs[j];
					content += '<li><a href="#" onclick="main.showMsg(\'' + msg.url + '\')"><span style="float:left">' + msg.title + '</span><span style="float:right">'+msg.receiveTime+'</span></a></li>';
				}
				content += '</ul>';
			} else {
				content += '<li><a href="#"><div class="clearfix"><span class="pull-left">' + category.title
						+ '</span><span class="pull-right badge badge-info">' + category.count + '</span></div></a>';
			}
			content += '</li>';
		}
		content += '<li><a href="#" onclick="main.showMsgList()">查看所有消息<i class="awsm-icon-arrow-right"></i></a></li>';
		content += '</ul>';
		main.html(content);
	},
	//菜单栏中待办任务
	/*var content = '<li><a href="#"><div class="clearfix"><span class="pull-left">'
    + '<i class="btn btn-xs no-hover btn-success awsm-icon-shopping-cart"></i>内部通告退回'
    + '</span></div></a></li>';*/
	/******************** 待办任务相关 ********************/
	loadMyTask: function(){
		var content = "";
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : "taskController.do?myTask", 
			error : function() {
				alert("请求失败！"); 
			},
			success : function(data) {
				var datas = $.parseJSON(data),
					msgsNum = datas.length;
				content = '<a data-toggle="dropdown" class="dropdown-toggle" href="#">'
						+ '<span class="dropdown-title">任务</span>'
						+ (msgsNum > 0 ? '<span class="dropdown-badge"></span>' : '')
						+ '</a>'
				        + "<ul  class='pull-right dropdown-navbar navbar-grey dropdown-menu dropdown-caret dropdown-close'>" 
				        + "<li class='dropdown-header' ><i class='awsm-icon-ok'></i>共"+msgsNum+"条待办事宜</li>";
				for(var i=0; i<msgsNum; i++){
					var day = new Date(datas[i].createTime).format("MM.dd");
					var taskName = datas[i].processName + "-" + datas[i].name;
					var title = taskName + "-" + new Date(datas[i].createTime).format("yyyy-MM-dd-HH:mm:ss");  
					if(taskName.length>20){
						taskName = taskName.substr(0,20)+"...";
					}
					taskName = taskName + "-" + day;
					content += '<li><a href="#" onclick="main.completeTask('+datas[i].id+')" title='+title+'>'
							+'<div class="clearfix"><span class="pull-left">'
							+ '<i class="btn btn-xs no-hover btn-success awsm-icon-shopping-cart"></i>'
							+ taskName + '</span></div></a></li>';
				}
			}
		});
		content+='<li><a href="#" onclick="main.showTaskList()">'
			+'<div class="clearfix">查看所有待办事宜<i class="awsm-icon-arrow-right"></i></div></a></li>';
		$("#myTaskListArea").html(content);
	},
	//点击待办事宜
	completeTask: function(id){
		createwindow("", "taskController.do?toStart&taskId="+id+"", 1000, 600, null, null);
	},
	//点击所有待办事宜
	showTaskList: function(){
		$pageIndexDiv = $("#page_index"), // 首页内容区域
		$tabbar = $("#tabbar");
		$pageIndexDiv.hide();
		$tabbar.show();
		
		var url = "taskController.do?myTaskList";
		addTab("待办事宜", url, null, null, false);
		if(!isIE(8)){
			$tabbar.tabs('getSelected').height($(window).height() - 130);
			$tabbar.tabs('resize');
		}
		var $logo = $("#navbar-container > .navbar-header:first");
		if (!$logo.children("a:first").hasClass("active"))
			$logo.trigger("click");
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
				bootbox.alert("请求失败！");
			},
			success : function(data) {
				// 加载左树
				loadLeftNav(data);
				// 加载功能菜单
				loadBottomFunList();
				//加载左侧树上部的子系统按钮区域
				main.loadSubsysBtn();
			}
		});
	},
	
	/******************** 首页日程管理功能 ********************/
	scheduleMainTabs : function(){
		$.ajax({
			async: false,
			url : "scheduleController.do?scheduleMainTabs",
			type : 'post',
			cache : false,
			success : function(doc) {
				var editTodayDate = $("#editTodayDate").val();
				var editTomorrowDate = $("#editTomorrowDate").val();
				var editDayAfterTomorrowDate = $("#editDayAfterTomorrowDate").val();
				var editThrdate = $("#editThrdate").val();
				var editFordate = $("#editFordate").val();
				var todayStr = doc.today;
				var todayHtml = "";
				for(var i = 0; i < todayStr.length; i++){
					todayHtml += '<div class="schedulebox pull-left"><div class="schedulebox-data center width-100">';
					todayHtml += '<div class="schedulebox-data-date">'+todayStr[i].dateTime+'</div>';
					if(todayStr[i].allDay == 'false'){
						todayHtml += '<div class="schedulebox-data-time">'+todayStr[i].startTime+' ~ '+todayStr[i].endTime+'</div>';
					}
					todayHtml += '<div class="schedulebox-data-title blue">'+todayStr[i].title+'</div>' 
								+ '<div class="schedulebox-data-tools">'
								+ '<div><i class="awsm-icon-edit bigger-130" onclick="editSchedule(\''+ todayStr[i].id +'\')"></i></div>'
								+ '<div><i class="awsm-icon-remove bigger-130 light-red" onclick="removeSchedule(\''+ todayStr[i].id +'\')" ></i></div>'
								+ '</div></div></div>';
				}
				todayHtml += '<div class="schedulebox">' 
							+ '<div class="schedulebox-data center width-100">'
							+ '<div class="schedulebox-data-add bigger-120">'
							+ '<i class="awsm-icon-plus"></i>'
							+ '<span onclick="editSchedulePage(\''+editTodayDate+'\')">新的提醒</span>'
							+ '</div></div></div>';
				$("#todayShedule").html(todayHtml);
				
				var tomorrowStr = doc.tomorrow;
				var tomorrowHtml = "";
				for(var i = 0; i < tomorrowStr.length; i++){
					tomorrowHtml += '<div class="schedulebox pull-left"><div class="schedulebox-data center width-100">';
					tomorrowHtml += '<div class="schedulebox-data-date">'+tomorrowStr[i].dateTime+'</div>';
					if(tomorrowStr[i].allDay == 'false'){
						tomorrowHtml += '<div class="schedulebox-data-time">'+tomorrowStr[i].startTime+' ~ '+tomorrowStr[i].endTime+'</div>';
					}
					tomorrowHtml += '<div class="schedulebox-data-title blue">'+tomorrowStr[i].title+'</div>' 
								+ '<div class="schedulebox-data-tools">'
								+ '<div><i class="awsm-icon-edit bigger-130" onclick="editSchedule(\''+ tomorrowStr[i].id +'\')"></i></div>'
								+ '<div><i class="awsm-icon-remove bigger-130 light-red" onclick="removeSchedule(\''+ tomorrowStr[i].id +'\')"></i></div>'
								+ '</div></div></div>';
				}
				tomorrowHtml += '<div class="schedulebox">' 
					+ '<div class="schedulebox-data center width-100">'
					+ '<div class="schedulebox-data-add bigger-120">'
					+ '<i class="awsm-icon-plus"></i>'
					+ '<span onclick="editSchedulePage(\''+editTomorrowDate+'\')">新的提醒</span>'
					+ '</div></div></div>';
				$("#tomorrowShedule").html(tomorrowHtml);
				
				var dayAfterTomorrowStr = doc.dayAfterTomorrow;
				var dayAfterTomorrowHtml = "";
				for(var i = 0; i < dayAfterTomorrowStr.length; i++){
					dayAfterTomorrowHtml += '<div class="schedulebox pull-left"><div class="schedulebox-data center width-100">';
					dayAfterTomorrowHtml += '<div class="schedulebox-data-date">'+dayAfterTomorrowStr[i].dateTime+'</div>';
					if(dayAfterTomorrowStr[i].allDay == 'false'){
						dayAfterTomorrowHtml += '<div class="schedulebox-data-time">'+dayAfterTomorrowStr[i].startTime+' ~ '+dayAfterTomorrowStr[i].endTime+'</div>';
					}
					dayAfterTomorrowHtml += '<div class="schedulebox-data-title blue">'+dayAfterTomorrowStr[i].title+'</div>' 
								+ '<div class="schedulebox-data-tools">'
								+ '<div><i class="awsm-icon-edit bigger-130" onclick="editSchedule(\''+ dayAfterTomorrowStr[i].id +'\')"></i></div>'
								+ '<div><i class="awsm-icon-remove bigger-130 light-red" onclick="removeSchedule(\''+ dayAfterTomorrowStr[i].id +'\')"></i></div>'
								+ '</div></div></div>';
				}
				dayAfterTomorrowHtml += '<div class="schedulebox">' 
					+ '<div class="schedulebox-data center width-100">'
					+ '<div class="schedulebox-data-add bigger-120">'
					+ '<i class="awsm-icon-plus"></i>'
					+ '<span onclick="editSchedulePage(\''+editDayAfterTomorrowDate+'\')">新的提醒</span>'
					+ '</div></div></div>';
				$("#dayAfterTomorrowShedule").html(dayAfterTomorrowHtml);
				
				var dayAfterThrStr = doc.dayAfterThr;
				var dayAfterThrHtml = "";
				for(var i = 0; i < dayAfterThrStr.length; i++){
					dayAfterThrHtml += '<div class="schedulebox pull-left"><div class="schedulebox-data center width-100">';
					dayAfterThrHtml += '<div class="schedulebox-data-date">'+dayAfterThrStr[i].dateTime+'</div>';
					if(dayAfterThrStr[i].allDay == 'false'){
						dayAfterThrHtml += '<div class="schedulebox-data-time">'+dayAfterThrStr[i].startTime+' ~ '+dayAfterThrStr[i].endTime+'</div>';
					}
					dayAfterThrHtml += '<div class="schedulebox-data-title blue">'+dayAfterThrStr[i].title+'</div>' 
								+ '<div class="schedulebox-data-tools">'
								+ '<div><i class="awsm-icon-edit bigger-130" onclick="editSchedule(\''+ dayAfterThrStr[i].id +'\')"></i></div>'
								+ '<div><i class="awsm-icon-remove bigger-130 light-red" onclick="removeSchedule(\''+ dayAfterThrStr[i].id +'\')"></i></div>'
								+ '</div></div></div>';
				}
				dayAfterThrHtml += '<div class="schedulebox">' 
					+ '<div class="schedulebox-data center width-100">'
					+ '<div class="schedulebox-data-add bigger-120">'
					+ '<i class="awsm-icon-plus"></i>'
					+ '<span onclick="editSchedulePage(\''+editThrdate+'\')">新的提醒</span>'
					+ '</div></div></div>';
				$("#dayAfterThrShedule").html(dayAfterThrHtml);
				
				var dayAfterForStr = doc.dayAfterFor;
				var dayAfterForHtml = "";
				for(var i = 0; i < dayAfterForStr.length; i++){
					dayAfterForHtml += '<div class="schedulebox pull-left"><div class="schedulebox-data center width-100">';
					dayAfterForHtml += '<div class="schedulebox-data-date">'+dayAfterForStr[i].dateTime+'</div>';
					if(dayAfterForStr[i].allDay == 'false'){
						dayAfterForHtml += '<div class="schedulebox-data-time">'+dayAfterForStr[i].startTime+' ~ '+dayAfterForStr[i].endTime+'</div>';
					}
					dayAfterForHtml += '<div class="schedulebox-data-title blue">'+dayAfterForStr[i].title+'</div>' 
								+ '<div class="schedulebox-data-tools">'
								+ '<div><i class="awsm-icon-edit bigger-130" onclick="editSchedule(\''+ dayAfterForStr[i].id +'\')"></i></div>'
								+ '<div><i class="awsm-icon-remove bigger-130 light-red" onclick="removeSchedule(\''+ dayAfterForStr[i].id +'\')"></i></div>'
								+ '</div></div></div>';
				}
				dayAfterForHtml += '<div class="schedulebox">' 
					+ '<div class="schedulebox-data center width-100">'
					+ '<div class="schedulebox-data-add bigger-120">'
					+ '<i class="awsm-icon-plus"></i>'
					+ '<span onclick="editSchedulePage(\''+editFordate+'\')">新的提醒</span>'
					+ '</div></div></div>';
				$("#dayAfterForShedule").html(dayAfterForHtml);
			}
		});
	},
	/******************** 首页待办任务双击事件 ********************/
	taskApprovePage : function(rowIndex, rowData) {
		var url = "taskController.do?toStart&taskId=" + rowData.id;
		createwindow('审批', url, 1000, 600, null, {
			noheader : true,
			optFlag : 'close'
		});
	},
	/******************** 首页已办任务双击事件 ********************/
	completeTaskPage : function(rowIndex, rowData) {
		var url = "processInstanceController.do?info&instId=" + rowData.processInstanceId + "&id=" + rowData.id
				+ "&nodeId=" + rowData.taskDefinitionKey + "&isCompleteTask=1";
		createwindow('详细', url, 1000, 600, null, {
			noheader : true
		});
	},
	sysMsgViewPage : function(rowIndex, rowData) {
		var url = "messageController.do?sysMsgView&id=" + rowData.id;
		createwindow('消息详细', url, 650, 400, null, null);
	},
	/******************** 加载系统左侧树上部子系统图标按钮区域 ********************/
	loadSubsysBtn: function(){
		//rootFuns.push({"funCode":funLists[i].funSubsystem, "funIcon":subSystemIcon, "funIconSize":"bigger-120", "funName":subSystemName});
		//$(window).data("subSystems",rootFuns);
		//处理系统左侧树上部子系统图标Hover事件
		
		var $sidebar = $(".sidebar"),
			$sidebarShortcuts = $(".sidebar-shortcuts"),
			$sidebarShortcutsLarge = $sidebarShortcuts.children(".sidebar-shortcuts-large"),
			buttonNum, //47px
			subSystems = $(window).data("subSystems"),
			subSystemBtnCtn = "";
		for(var i = 0; i < subSystems.length; i++){
			var subSystem = subSystems[i];
			subSystemBtnCtn += "<button title='"+subSystem.funName+"' data-subsyscode='"+subSystem.funCode+"' class='btn btn-success'><i class='"+subSystem.funIcon+"'></i></button>"
		}
		$sidebarShortcutsLarge.html(subSystemBtnCtn);
		
		buttonNum = $sidebarShortcutsLarge.children("button").length;
		
		$sidebarShortcuts.hover(
			function(){
				var buttonNum_clac = $sidebar.hasClass("menu-min") ? buttonNum+1 : buttonNum;
				$(this).width(buttonNum_clac * 47).css({"z-index":"20", position:"relative", "border-right":"1px solid #ccc"});
				$sidebarShortcutsLarge.width(buttonNum_clac * 47);
			},
			function(){
				$(this).width("auto").css({"z-index":"0", position:"static", "border-right":"0"});
				$sidebarShortcutsLarge.width("auto");
			}
		)
		
		$sidebarShortcutsLarge.children("button").on("click",function(){
			var subsyscode = $(this).attr("data-subsyscode"),
				$treeUl = $sidebar.find("ul.nav-list"),
				$treeUlFirstSubLi = $treeUl.find("li[data-subsys = "+subsyscode+"]:first");
			
			$treeUl.find("li").show();
			$treeUl.find("li[data-subsys != "+subsyscode+"]").hide();
			$treeUl.find("#indexBtn").parent("li").show();
			
			$sidebar.removeClass("menu-none");
			$sidebar.find("ul.nav-list").removeClass("menu-none");
			$sidebar.find("div.sidebar-collapse").removeClass("menu-none");
			if(!$treeUlFirstSubLi.hasClass("open"))
				$treeUlFirstSubLi.children("a").trigger("click");
			$treeUlFirstSubLi.children("ul").children("li[data-subsys = "+subsyscode+"]").filter(function(index){
				return $("ul", this).length == 0;
			}).first().children("a").trigger("click");
			
			resizeTabSize();
		});
	}
}
function redrawTabSize(id){
	//setTimeout('$("#'+id+'").datagrid("resize");',0);
	setTimeout(function(){
		redrawEasyUI($("#"+id+""));
	},100);
}

function resizeTabSize(){
//	$(".panel-resize-flag").panel("resize",{width:"auto",height:"229"});
	if(!isIE(8))
		$("#tabbar").tabs('resize');
}

//判断IE版本
var isIE = function(ver){
    var b = document.createElement('b')
    b.innerHTML = '<!--[if IE ' + ver + ']><i></i><![endif]-->'
    return b.getElementsByTagName('i').length === 1
}

