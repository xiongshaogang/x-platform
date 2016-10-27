<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="sub-menu-pannel">
	<div class="text-center dropdown">
		<div class="drop-header">
			<span id="orgName" data-toggle="modal" data-target="#orgTmpModal"></span>
			<i class="fa fa-chevron-down dropdown-btn"></i>
		</div>
		<div class="drop-list">
			<ul class="list-style-none" id="orgList">
				<!-- 
				<li class="text-overflow">广州汇客空间互联网金融信息服务有限公司</li>
				<li class="text-overflow">广州汇客汇客汇客汇客汇客汇客空间互联网金融信息服务有限公司</li>
				<li class="text-overflow">有限公司</li>
				 -->
			</ul>
		</div>
	</div>
	<div class="app-list-container">
			<table class="app-list" id="appList">
 			 <tr id="daibanTr">
				<td data-url="flowFormController.do?commonFormEdit" id="daiban">
					<div class="app-icon" ><img class='icon' src="basic/img/logo/app_task.png" /></div>
					<div class="app-name">我的审批</div>
				</td>
				<td data-url="flowFormController.do?application" id="myApplication">
					<div class="app-icon" ><img class='icon' src="basic/img/logo/app_apply.png" /></div>
					<div class="app-name">我的申请</div>
				</td>
			
			</tr>
			<!--<tr>
				<td>
					<div class="app-icon"><i class="glyphicon glyphicon-plane"></i></div>
					<div class="app-name">应用4</div>
				</td>
				<td></td>
			</tr> -->
			<!-- 	
				-->
		
 			 <!--<tr>
				<td data-url="flowFormController.do?commonFormEdit">
					<div class="app-icon" id="daiban"><i class="glyphicon glyphicon-plane"></i></div>
					<div class="app-name">代办</div>
				</td>
				<td>
					<div class="app-icon"><i class="glyphicon glyphicon-plane"></i></div>
					<div class="app-name">代办XX</div>
				</td>
				<td>
					<div class="app-icon"><i class="glyphicon glyphicon-plane"></i></div>
					<div class="app-name">代办XX</div>
				</td>
			
			</tr>
			<tr>
				<td>
					<div class="app-icon"><i class="glyphicon glyphicon-plane"></i></div>
					<div class="app-name">应用4</div>
				</td>
				<td></td>
			</tr> -->
			<!-- 	
				-->
		</table>
	</div>
</div>
<div class="content-pannel" id="formContentPanel">
	<div class="header" id="appTitle">
		<!-- <div class="btn-left">
			<span class="dropdown-btn" id="leftName"></span>
		</div> -->
		<span class="btn-left" id="leftName"></span>
		<span id="appName"></span>
		<div class="btn-right">
			<span class="dropdown-btn" id="rightName"></span>
			<ul class="list-style-none dropdown" id="relaFlFoUl">
				<!-- <li>关联任务1</li>
				<li>关联任务2</li>
				<li>关联任务3</li> -->
			</ul>
		</div>
	</div>
	<div class="sys-right-panel" id="commonFormEdit" style="margin: 1em;">
	
	</div>
</div>

<!-- 选任务弹框 -->
<div class="modal fade in-iframe" id="orgTmpModal" role="dialog">
	<div class="modal-dialog single-select" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">选择机构</h4>
			</div>
			<div class="modal-body clearfix">
				<div class="pull-left all-person-box">
					<div class="person-box">
						<div class="person-list all">
							<ul class="list-style-none">
								<!-- <li id="1">
									<div class="name">
										<p>任务1</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i>
										<i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="2">
									<div class="name">
										<p>任务2</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i>
										<i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="3">
									<div class="name">
										<p>任务3</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i>
										<i class="glyphicon glyphicon-remove"></i>
									</div>
								</li> -->
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="orgConfirm_t" type="button" class="btn btn-orange" data-dismiss="modal" disabled>确定</button>
			</div>
		</div>
	</div>
</div>

<script>

var formAppHome = {
		queryAPPList : function(){
			var imgURL = '${attachForeRequest}';
			$.ajax({
				url : 'flowFormController.do?queryAPPList',
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					//debugger;
					if(result.success){
						var templi = "";
						//$("#appList").empty();
						//console.log(result.obj);
						var tempNumber = 0;
						$.each(result.obj, function(i, item) {
							if(i<1){
								temptd = "<td id='"+item.id+"' data-isEdit='"+item.isEdit+"' data-flag='1' data-code='"+item.code+"' data-name='"+item.name+"' data-isFlow='"+item.isFlow+"'>"
								+"<div class='app-icon'><img class='icon' src='basic/img/logo/"+item.logo+".png' /></div>"
								+"<div class='app-name'>"+item.name+"</div>"
								+"</td>";
								$("#daibanTr").append(temptd);
							}else{
								tempNumber = i-1;
								if(tempNumber%3==0){
									if(i==1){
										templi = "<tr>";
									}else{
										templi = templi+"</tr><tr>";
									}
								}
								templi = templi + "<td id='"+item.id+"' data-isEdit='"+item.isEdit+"' data-flag='1' data-code='"+item.code+"' data-name='"+item.name+"'  data-isFlow='"+item.isFlow+"'>"
									+"<div class='app-icon'><img class='icon' src='basic/img/logo/"+item.logo+".png' /></div>"
									+"<div class='app-name'>"+item.name+"</div>"
									+"</td>";	
							}
												
							
						});
						templi = templi+"</tr>";
						$("#appList").append(templi);
						var $lastTr = $("#appList").find("tr:last");
						//console.log($lastTr);
						if($lastTr.children("td").length == 1){
							$lastTr.append("<td></td><td></td>");
						}else if($lastTr.children("td").length == 2){
							$lastTr.append("<td></td>");
						}
						$("#appList td").on("click", function(){
							$(".btn-right").show();
							if(this.id != null && this.id != "" && this.id !="undefined"){
								//获取isEdit参数和isFlow参数
								if(this.id == "daiban"){
									$("#leftName").html("");		
									$("#commonFormEdit").load("flowFormController.do?commission");
									$("#appList").attr("data-flag",1);
									$("#appName").text("我的审批");
								}else if(this.id == "myApplication"){
									$("#leftName").html("");
									$("#leftName").attr("data-url","flowFormController.do?application");
									$("#rightName").text("");
		            			   	$("#appName").text("我的申请");
									$("#commonFormEdit").load("flowFormController.do?application");
									$("#leftName").attr("data-daiban",0);
									$("#leftName").attr("data-type",4);
									$("#leftName").attr("data-appName","我的申请")
								}else{
									$("#leftName").html("");								
									var daiban = $("#leftName").attr("data-daiban");
									if(daiban == "1" || typeof(daiban) == "undefined"){
										$("#leftName").attr("data-daiban",0);
									}
									var isEdit = $("#"+this.id).attr("data-isEdit");
									var isFlow = $("#"+this.id).attr("data-isFlow");
									//绑定应用code和应用id
									var code = $("#"+this.id).attr("data-code");
									//标识状态码，用来判断右上角只能点击一次
									var flag = $("#"+this.id).attr("data-flag");
									$("#appList").attr("data-code",code);
									$("#appList").attr("data-id",this.id);
									$("#appList").attr("data-flag",flag);
									//应用名称修改
									$("#appName").text($("#"+this.id).attr("data-name"));
									
									//判断是否进入编辑页或者数据列表页
									id=this.id;
									if(isEdit == 1){
										var url = "appFormTableController.do?commonFormEdit&formCode="+code+"&businessKey="+id+"&viewType=add";
										$("#leftName").attr("data-url",url);
										$("#leftName").attr("data-type",1);
										$("#leftName").attr("data-appName",$("#"+this.id).attr("data-name"));
										$("#commonFormEdit").load("appFormTableController.do?commonFormEdit&formCode="+code+"&businessKey="+id+"&viewType=add");
										$("#rightName").text("历史");
										$("#appList").attr("data-type",1);
									}else{
										var url = "appFormTableController.do?fieldData&formCode="+code+"&formId="+id;
										$("#leftName").attr("data-url",url);
										$("#leftName").attr("data-type",2);
										$("#leftName").attr("data-appName",$("#"+this.id).attr("data-name"));
										$("#commonFormEdit").load("appFormTableController.do?fieldData&formCode="+code+"&formId="+id);
										$("#rightName").html("<i class='fa fa-plus'></i>");
										$("#appList").attr("data-type",2);
										//formAppHome.getRelaFlFoList(code);
									}
									
		/* 							if( isFlow == 1){
										
										$("#rightName").html("<i class='fa fa-ellipsis-v'></i>");
										$("#appList").attr("data-type",3);
									} */
								}
							}
						});
						
						//右上角的点击事件
						$("#rightName").off().on("click", function(e){
							e.stopPropagation();
							$("#leftName").html("<img src='basic/img/icon_back.png' alt='后退'>");
							var flag = $("#appList").attr("data-flag");
							var type = $("#appList").attr("data-type");
							var code = $("#appList").attr("data-code");
							var id = $("#appList").attr("data-id");
							if(flag == 1){
								if(type == 1){
									$("#commonFormEdit").load("appFormTableController.do?fieldData&formCode="+code+"&formId="+id);
									$("#rightName").html("<img src='basic/img/icon_add.png' alt='新增'>");
									$("#appList").attr("data-type",2);
									$("#appList").attr("data-flag",0);
									$("#rightName").html("");
								}else if(type == 2){
									$("#commonFormEdit").load("appFormTableController.do?commonFormEdit&formCode="+code+"&businessKey="+id+"&viewType=add");
									$("#rightName").text("历史");
									$("#appList").attr("data-type",1);
									$("#appList").attr("data-flag",0);
									$("#rightName").html("");
								}else if(type == 3){
									var code = $("#appList").attr("data-code")
									$("#appList").attr("data-type",3);
									$(this).next(".dropdown").toggleClass("open");
								}//屏蔽审批历史
								/* else if(type == 4){
									$("#homeSlidePop").toggleClass("active");
									if ($("#homeSlidePop").hasClass("active")) {
										$("#homeSlidePop .pop-title").text("审批历史");
										//$("#homeSlidePop .body-content").empty().load("");
									}
									var url = $("#appList").attr("data-daiBanUrl");
									//$("#myFrame").attr("src",url);
									$("#myFrame").empty().append("<iframe id='myFrame' width='100%' height='500px' src='"+url+"'></iframe>");
									
								} */
							}else{
								$("#rightName").html("");
							}
							
						});
						$("body").on("click", function(){
							$(".dropdown").removeClass("open");
						});
						$(".dropdown").on("click", function(){
							$(".dropdown").removeClass("open");
						})
						
					}
				}
			});
		},
		getRelaFlFoList : function(code,id,businessKey){
			$.ajax({
				//url : 'flowFormController.do?queryRelaFlFoList&formCode='+code,
				url : 'processInstanceController.do?getFormByParentBusinessKey&businessKey='+businessKey+'&formId='+id+'&formCode='+code,
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					if(result.success && result.obj.length > 0){
						$("#relaFlFoUl").empty();
						$.each(result.obj, function(i, item) {
							var templi = "";
							if(item.isFlow == 1){
								if(item.status == 3){
									//样式变灰色
									 templi = "<li id='"+item.id+"' data-isEdit='"+item.isEdit+"' data-code='"+item.code+"' data-name='"+item.name+"'  data-isFlow='"+item.isFlow+"'  data-status='"+item.status+"'>"+item.name+"</li>";
								}else{
									 templi = "<li class='not-start' id='"+item.id+"' data-isEdit='"+item.isEdit+"' data-code='"+item.code+"' data-name='"+item.name+"'  data-isFlow='"+item.isFlow+"'  data-status='"+item.status+"'>"+item.name+"</li>";
								}
								$("#relaFlFoUl").append(templi);
							}
							
						});
						$("#relaFlFoUl li").off().on("click", function(){
							//获取isEdit参数和isFlow参数
							var isEdit = $("#"+this.id).attr("data-isEdit");
							var isFlow = $("#"+this.id).attr("data-isFlow");
							var status = $("#"+this.id).attr("data-status");
							//绑定应用code和应用id
							var code = $("#"+this.id).attr("data-code");
							$("#appList").attr("data-code",code);
							$("#appList").attr("data-id",this.id);
							
							
							//判断是否进入编辑页或者数据列表页
							id=this.id;
							if(status == 3){
								alert("该流程已启动");
							}else{
								//应用名称修改
								$("#appName").text($("#"+this.id).attr("data-name"));
								//$("#rightName").text("");
								//$("#rightName").html("");
								//$("#appList").attr("data-type",0);
								//$("#relaFlFoUl").empty();
								$("#commonFormEdit").load("appFormTableController.do?commonFormEdit&viewType=add&formCode="+code+"&parentBusinessKey=" + businessKey);
								/* if(isEdit == 1){
									var url = "appFormTableController.do?commonFormEdit&formCode="+code+"&businessKey="+id+"&viewType=add";
									$("#leftName").attr("data-url",url);
									$("#leftName").attr("data-type",1);
									$("#leftName").attr("data-appName",$("#"+this.id).attr("data-name"));
									$("#commonFormEdit").load("appFormTableController.do?commonFormEdit&formCode="+code+"&businessKey="+id+"&viewType=add");
									$("#rightName").text("历史");
									$("#appList").attr("data-type",1);
								}else{
									var url = "appFormTableController.do?fieldData&formCode="+code+"&formId="+id;
									$("#leftName").attr("data-url",url);
									$("#leftName").attr("data-type",2);
									$("#leftName").attr("data-appName",$("#"+this.id).attr("data-name"));
									$("#commonFormEdit").load("appFormTableController.do?fieldData&formCode="+code+"&formId="+id);
									$("#rightName").html("<i class='fa fa-plus'></i>");
									$("#appList").attr("data-type",2);
								} */
							}
							/* if(isFlow == 1){
								
								$("#rightName").html("<i class='fa fa-ellipsis-v'></i>");
								$("#appList").attr("data-type",3);
							} */
						});
					}else{
						$("#rightName").text("");
						$("#rightName").html("");
						$("#appList").attr("data-type",0);
					}
				}
			});
		},
		getCommission : function(){
			//debugger;
			var imgURL = '${attachForeRequest}';
			$.ajax({
				url : 'taskController.do?myTask&page=1',
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					//debugger;
					if(result.success){
						var templi = "<div class='sys-list'><ul id='commissionUl' class='list-style-none sys-list-container'>";
						var title = "";
						$.each(result.obj, function(i, item) {
							var content = "";
							var img = "";
							var portrait = "";
							var approveStatus = "";
							if(item.extra != null && item.extra!="" && typeof(item.extra) != "undefined"){
								var extra = JSON.parse(item.extra);
								if(extra.title != null && extra.title!=""){
									if(extra.title.length > 30){
									   title = extra.title.substr(0,29)+"...";
									}else{
									   title = extra.title;
									}
								}else{
									title="";
								}
								if(extra.extraData){
									$.each(extra.extraData, function(j, item1) {
										content += "<div class='content-item'>"
										+ "<span class='label'>"+item1.name+"：</span>"
										+ "<span class='text'>"+item1.value+"</span>"
										+ "</div>";
									});
									if(content != ""){
										content = "<div class='item-content'>"
												+ "<div class='content-inner'>" 
												+ content
												+ "</div>"
												+ "</div>";
									}			
								}
								if(extra.groupUserList){
									$.each(extra.groupUserList, function(j, item2) {
										portrait += "<li><img src='"+imgURL+item2.portrait+"'></li>"
									});
									if(portrait != ""){
										portrait = "<div class='item-bottom'>"
												+ "<ul class='list-style-none-h user-group'>"
												+ portrait
												+ "</ul>"
												+ "</div>";
									}
								}
							}
							
							
							 templi += "<li class='list-item' id='"+item.id+"'>"
								+ "<div class='item-top'>"
								+ "<span class='item-title'>"+title+"</span>"
								+ "<span class='item-ctime'>"+extra.createTime+"</span>"
								+ "</div>"
							    + content
								+ portrait
								+ "</li>";
							
		               		
						});
		               		templi +="</ul></div>";
            			   	$("#commonFormEdit").empty().append(templi);
            			   	//清空title的所有绑定数据
            			   	$("#rightName").text("");
            			   	$("#appName").text("待办");
            			   	$("#appList").attr("data-type","");
							$("#appList").attr("data-code","");
							$("#leftName").attr("data-url","");
							$("#leftName").attr("data-type","");
							$("#leftName").attr("data-appName","");
							
            			   	$("#commissionUl li").off().on("click",function(){
								$("#leftName").attr("data-daiban",1);
            			   		$("#commonFormEdit").load("taskController.do?toStart&taskId="+this.id);
            			   	});
					}
				}
			});
		},
		queryHomeCompany : function(){
			$.ajax({
				//url : 'flowFormController.do?queryRelaFlFoList&formCode='+code,
				url : 'userController.do?queryHomeCompany',
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					if(result.success && result.obj.length > 0){
						$("#orgList").empty();
						$.each(result.obj, function(i, item) {
							if(item.isManage){
								$("#orgName").text(item.name);
							}
							var templi = "<li id='"+item.id+"' data-orgName='"+item.name+"' name='"+item.name+"'>" + (item.name == "" ? "未定义" : item.name) + "</li>";
							$("#orgList").append(templi);
							
							//选择关联任务点击事件
							$("#orgList li").off().on("click", function() {
								var orgName = $(this).attr("name"),
						    		orgId = $(this).attr("id");
								
								formAppHome.changeCompany(orgName,orgId);
							});
						}); 
					}else{
						
					}
				}
			});
		},
		changeCompany : function(orgName,orgId){
			var myData = {"orgName" : orgName,"orgId" : orgId};
			$.ajax({
				//url : 'flowFormController.do?queryRelaFlFoList&formCode='+code,
				url : 'userController.do?changeCompany',
				type : 'post',
				data: myData,
				dataType: "json",
				success : function(result) {
					if(result.success){
				/* 		formAppHome.queryHomeCompany();
						formAppHome.queryAPPList();
						$("#orgName").text(orgName); */
						$(".menu-pannel-body").load("flowFormController.do?flowFormApp");
					}else{
						
					}
				}
			});
		}
		
};

$(function(){
	
	formAppHome.queryHomeCompany();
	formAppHome.queryAPPList();
	
	/* $("#daiban").on("click",function(){
		//debugger;
	
	}); */
	
	$("#leftName").off().on("click",function(){
		var daiban = $("#leftName").attr("data-daiban");
		$("#leftName").html("");
		if(daiban == "0"){
			$("#appList").attr("data-flag",1);
			$("#appName").text($("#leftName").attr("data-appName"));
			var type = $("#leftName").attr("data-type");
			if(type == 1){
				$("#rightName").text("历史");
				$("#appList").attr("data-type",1);
			}else if(type == 2){
				$("#rightName").html("<i class='fa fa-plus'></i>");
				$("#appList").attr("data-type",2);
			}else if(type == 3){
				
			}else if(type == 4){
				//$("#appName").text("我的申请");
			}
			var url = $("#leftName").attr("data-url");
			if(url != "" && url != null){
				$("#commonFormEdit").load(url);
			}
		}else{
			$("#leftName").html("");		
			$("#commonFormEdit").load("flowFormController.do?commission");
			$("#appList").attr("data-flag",1);
			$("#appName").text("我的审批");
		} 
		
	});
	
	//绑定选择机构下拉菜单按钮事件
	$(".dropdown .drop-header").on("click", function(e){
		e.stopPropagation();
		if($(".dropdown .drop-list li").length > 1){
			$(".dropdown .drop-list").show();
		}
	});
	$(".dropdown .drop-list li").on("click", function(){
		$(".dropdown .drop-list").hide();
	})
	$(document).on("click", function(){
		$(".dropdown .drop-list").hide();
	})
	
	home.loadSlimScroll([{
		obj : $(".app-list-container"),
		//width: "101%",
		height: "503px"
	},{
		obj : $("#commonFormEdit"),
		//width: "101%",
		height: 503-13+"px"
	}]);
	
	
	/* $("#appList td").on("click", function(){
		$("#commonFormEdit").load($(this).attr("data-url"));
	}); */
});
</script>