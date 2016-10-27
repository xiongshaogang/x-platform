<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="sub-menu-pannel">
	<div class="text-center list-title">消息</div>
	<div class="sys-list" id="sysMeesageHome">
		<div class="sys-list-container">
			<ul id="sys-messageList" class="list-style-none">
			</ul>
		</div>
<!-- 	<div id="coupon_pagination">
	分页区域
		<ul class="bbb-pagination list-style-none-h text-center hidden-num" id="coupon_paginationUl"></ul>
	</div> -->
	</div>
	

</div>
<div class="content-pannel">
	<div class="header" id="appTitle">
		<span id="appName"></span>
		<div class="btn-right">
			<span class="dropdown-btn" id="rightName"></span>
		</div>
	</div>
	<div class="sys-right-panel" id="sysMessageEdit" style="margin: 1em;">
	
	</div>
</div>

<script>

var sysMeesageHome_scroll;
var sysMeesageHome_content=$("#sysMeesageHome");
var sysMessageHome = {
		loadInnerMessage : function(){
			var pagenumber = sysMeesageHome_content.data('pagenumber');
			if(pagenumber == null || pagenumber == "" || typeof(pagenumber) == "undefined"){
				pagenumber = 1;
			}
			if(pagenumber == 1){
		   		$("#sys-messageList").empty();
       		}
			$.ajax({
				url : 'messageController.do?loadInnerMessage&rows=10&page='+pagenumber,
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					if(result.success&& result.obj.length > 0){
						//sysMessageHome.loadPage();
						$.each(result.obj, function(i, item) {
							sysMessageHome.getLidata(item,"1");
							});
						
							//删除系统消息
							$("#sys-messageList li .fa-close").on("click", function(e){
								e.stopPropagation();
								if(confirm("确认删除该数据?")){
									var $this=$(this);
									var sendId=$this.closest("li").attr("data-sendId");
									if(sendId){
										ajaxTip("messageController.do?deleteMessage&sendId="+sendId,function(){
											$this.closest("li").remove();
										});
									}
								}
								
							});
							

							$(".item-bottom",$("#sys-messageList")).on("click",function(e){
								var groupId = $("#contentBottom").attr("data-groupId");
								$("#appName").text("");
								setCurrentContact(groupFlagMark+groupId);
								showIM();
								e.stopPropagation();
							});
							
							
							if (sysMeesageHome_scroll) {
								if(sysMeesageHome_content.data("hasInit")){
									//页面已加载过数据的情况
									sysMeesageHome_scroll.refresh();
									iscrollAssist.pullActionCallback(sysMeesageHome_scroll);
									return ;
								}
							}
							
							//第一次进入(scroll未实例化)/二次进入,但是是首次加载并有数据(需要重新实例化scroll,即使之前已存在)
							setTimeout(function(){
								sysMeesageHome_content.nodata.hide();
								sysMeesageHome_content.wrapper.show();
								sysMeesageHome_scroll = iscrollAssist.initScroll(sysMeesageHome_content, sysMessageHome.pullDownAction, sysMessageHome.pullUpAction,{
									autoLoadMargin:800
								});
								sysMeesageHome_content.data("hasInit",true);
							},0);
					}else{
						if(sysMeesageHome_scroll){
							if(sysMeesageHome_content.data("hasInit")){
								//页面已加载过数据的情况
								iscrollAssist.pullActionNoMore(sysMeesageHome_scroll);
								//本次加载没数据时,pagenumber保持不变
								sysMeesageHome_content.data('pagenumber', pagenumber - 1);
								return;
							}
						}
						
						//页面当前无数据(第一次进入就没数据/二次进入页面没数据)
						sysMeesageHome_content.nodata.show();
						sysMeesageHome_content.wrapper.hide();
						$("#chatMain").append("<div id='null-nouser' class='sys-notice'>暂时没有聊天信息</div>");
						
					}
				}
			});
		},
		/**
		* 上拉加载下一页
		*/
		pullUpAction : function() {
			var pagenumber = sysMeesageHome_content.data('pagenumber');
			if (pagenumber) {
				//页数+1
				var next_page = parseInt(pagenumber) + 1;
			} else {
				//如果绑定的data没值,下拉直接第二页
				var next_page = 2;
			}
			//修改data中的页数
			sysMeesageHome_content.data('pagenumber', next_page);
			//加载数据
			sysMessageHome.loadInnerMessage();
		},
		/**
		* 下拉回到第一页
		*/
		pullDownAction : function() {
			sysMeesageHome_content.data('pagenumber', 1);
			sysMessageHome.loadInnerMessage();
		},
		compare : function(startTime,endTime){
			var startArr = startTime.split(" ");
			//console.info(startTimeArr[0]+"-----------"+startTimeArr[1]);
			var startDateArr = startArr[0].split("-");
			var startTimeArr = startArr[1].split(":");
			var startMyTime = new Date(startDateArr[0], startDateArr[1], startDateArr[2]);
			var startMTtimes = startMyTime.getTime();
			
			var endArr = endTime.split(" ");
			//console.info(endTimeArr[0]+"-----------"+endTimeArr[1]);
			var endDateArr = endArr[0].split("-");
			var endTimeArr = endArr[1].split(":");
			var endMyTime = new Date(endDateArr[0], endDateArr[1], endDateArr[2]);
			var endMTtimes = endMyTime.getTime();
			
			var flag = 0;
		    if (startMTtimes > endMTtimes) {
		    	flag = 0;
		    }else if(startMTtimes == endMTtimes){
		    	flag = 1;
		    }else{
		    	flag = 2;
		    }
		    
		    var status = false;
			if(flag == 0){
				status = true;
			}else if(flag == 1){
				if(startTimeArr[0]>endTimeArr[0]){
					status = true;
				}else if(startTimeArr[0] == endTimeArr[0]){
					if(startTimeArr[1] > endTimeArr[1]){
						status = true;
					}else{
						status = false;
					}
				}else{
					status = false;
				}
			}else{
				status = false;
			}
			return status;
		},
		/* loadPage : function(){
			//var relTotalPages = $("#fieldDataDiv").data("totalPages");
			$('.bbb-pagination').twbsPagination({
				totalPages: 7,
				visiblePages: 7,
				paginationClass: "bbb-pagination",
				first: "首页",
				prev: "上一页",
				next: "下一页",
				last: "尾页",
				onPageClick: function (event, page) {
					$("#coupon_pagination").data("pagenumber",page);
					sysMessageHome.loadInnerMessage();
				}
			});
		}, */
		getLidata : function(item,flag){
			var imgURL = '${attachForeRequest}';
			var templi = "";
			var content = "";
			var title = "";
			var img = "";
			var portrait = "";
			var approveStatus = "";
			var header = "";
			var extraContent = "";
			var footer = "";
			var contentStr = "";
			var taskId = "";
			var moduleName = "";
			var userInfo = "";
			var extra = null;
			var sourceId = "";
			if(item.sourceType != "" && item.sourceType != null){
				if(flag == "1"){
					extra = parseJSON(item.extra);
				}else{
					extra = parseJSON(item);
					
				}
				if(item.sourceType == "flowNotice"){
					if(item.sourceId != "" && item.sourceId != null){
						sourceId = "id='"+item.sourceId+"'";
						if($("#"+item.sourceId).length > 0){
							var liTime = $("#"+item.sourceId).attr("date-time");
							var nowTime = extra.createTime;
							var flag = sysMessageHome.compare(nowTime,liTime);
							if(flag){
								$("#"+item.sourceId).closest("li").remove();
							}else{
								//return false;
								return;
							}
						}
					}
				}
				/* if(extra.approveStatus == 1){
					approveStatus = "审批中";
					approveStatus = "<p class='content-footer'>"+approveStatus+"</p>";
				}else if(extra.approveStatus == 2){
					approveStatus = "已同意";
					approveStatus = "<p class='content-footer'>"+approveStatus+"</p>";
				}else if(extra.approveStatus == 3){
					approveStatus = "已反对";
					approveStatus = "<p class='content-footer'>"+approveStatus+"</p>";
				}else if(extra.approveStatus == 4){
					approveStatus = "已转交";
					approveStatus = "<p class='content-footer'>"+approveStatus+"</p>";
				} */
				if(extra.img != null && extra.img!="" && typeof(extra.img) != "undefined"){
					img = "<div class='content-item img'><img src='"+imgURL+extra.img+"'></div>";
				}
				if(extra.header != null && extra.header != "" && typeof(extra.header) != "undefined"){
					header = extra.header;
					header = "<p class='content-header'>"+header+"</p>";
				}
				if(extra.content != null && extra.content != "" && typeof(extra.content) != "undefined"){
					extraContent =  "<div class='content-item'>"
					+ "<span class='text text-top'>"+extra.content+"</span>"
					+ "</div>";
				}
				if(extra.userInfo != null && extra.userInfo != "" && typeof(extra.userInfo) != "undefined"){
					userInfo = "<div class='content-item'>"
						+ "<span class='label'>"+extra.userInfo.name+"：</span>"
						+ "<span class='text'>"+extra.userInfo.value+"</span>"
						+ "</div>";
				}
				if(extra.moduleName != null && extra.moduleName != "" && typeof(extra.moduleName) != "undefined"){
					moduleName = extra.moduleName;
				}
				if(extra.titleContent != null && extra.titleContent != "" && typeof(extra.titleContent) != "undefined"){
					title = "<div class='content-item'>"
						+ "<span class='label'>"+extra.titleContent.name+"：</span>"
						+ "<span class='text'>"+extra.titleContent.value+"</span>"
						+ "</div>";
				}
				if(extra.footer != null && extra.footer != "" && typeof(extra.footer) != "undefined"){
					footer = extra.footer;
					footer = "<p class='content-footer'>"+footer+"</p>";
				}
				/* if(extra.title != null && extra.title!=""){
					if(typeof(extra.title) == "object"){
						 if(extra.title.time!= null && extra.title.time != "" && typeof(extra.title.time) != "undefined"){
							 var date = new Date(extra.title.time);
							 title = date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
						 }else{
							 title = extra.title;
						 }
					}else{
						if(extra.title.length > 15){
							title = extra.title.substr(0,14)+"...";
						}else{
						   title = extra.title;
						}
					}
				}else{
					title="";
				} */
				if(extra.extraData != null && extra.extraData != "" && typeof(extra.extraData) != "undefined"){
					$.each(extra.extraData, function(j, item1) {
						content += "<div class='content-item'>"
						+ "<span class='label'>"+item1.name+"：</span>"
						+ "<span class='text'>"+item1.value+"</span>"
						+ "</div>";
					});
				}
				if(extra.groupUserList != null && extra.groupUserList != "" && typeof(extra.groupUserList) != "undefined"){
					$.each(extra.groupUserList, function(j, item2) {
						if(item2.portrait != null && item2.portrait != "" && typeof(item2.portrait) != "undefined"){
							portrait += "<li><img src='"+imgURL+item2.portrait+"'></li>"
						}
						
					});
					if(portrait != ""){
						portrait =  "<div class='item-bottom' id='contentBottom' data-groupId='"+extra.groupId+"'>"
						+ "<ul class='list-style-none-h user-group'>"
					/* 	+ "<li class='active'><img src='basic/img/avatars/Javi-Jimenez.jpg'></li>"
						+ "<li><img src='basic/img/avatars/Javi-Jimenez.jpg'></li>"
						+ "<li><img src='basic/img/avatars/Javi-Jimenez.jpg'></li>" */
						+portrait
						+ "</ul>"
						+ "</div>";
					}
				}
				if(img !="" || extraContent!="" || content!="" || title!="" || userInfo !=""){
					contentStr = "<div class='content-inner'>"
							+ img
							+extraContent
							+userInfo
							+title
							+ content
							+ "</div>";
				}
				if(extra.taskId != null && extra.taskId!="" && typeof(extra.taskId) != "undefined"){
					taskId = extra.taskId;
				}
				   templi = "<li "+sourceId+" class='list-item' data-sendId='"+item.sendId+"' date-time='"+extra.createTime+"' data-taskId='"+taskId+"' data-url='"+extra.url+"' data-title='"+moduleName+"' >"
					+ "<div class='item-top' >"
					+ "<span class='item-title'>"+moduleName+"</span>"
					+ "<span class='item-ctime'>"+extra.createTime+"</span>"
					+"<i class='fa fa-close'></i>"
					+ "</div>"
					+ "<div class='item-content' id='contentHeader' >"
					+ header
					+ contentStr
					//+ approveStatus
					+ footer
					+ "</div>"
					+portrait
					+ "</li>";
           		if(flag == "1"){
				   $("#sys-messageList").append(templi);
           		}else{
           			$("#sys-messageList").prepend(templi);
           		}
			}
			 $("#sys-messageList li").off("click").on("click", function(){
				var url = $("#"+this.id).attr("data-url");
				//alert(url);
				
				$("#chatMain").load(url);
				
			}); 
			
			$("#sys-messageList li").off().on("click",function(e){
				var url = $(this).attr("data-url");
				var title = $(this).attr("data-title");
				var taskId = $(this).attr("data-taskId");
				$("#appName").text(title);
				//$("#rightName").text("审批历史");
				$("#rightName").attr("data-taskId",taskId);
				$("#rightName").attr("data-type","1");
				//e.stopPropagation();
				$("#sysMessageEdit").load(url);
			});
			//return templi;
		}
};

$(function(){
	sysMeesageHome_content.data('pagenumber', 1);
	iscrollAssist.initDOM(sysMeesageHome_content, sysMessageHome.pullDownAction, sysMessageHome.pullUpAction);
	sysMessageHome.loadInnerMessage();
	
	
/* 	$("#rightName").off().on("click",function(e){
		e.stopPropagation();
		var type = $("#rightName").attr("data-type");
		if(typeof(type) != "undefined" && type == "1"){
			var taskId = $("#rightName").attr("data-taskId");
			$("#homeSlidePop").toggleClass("active");
			if ($("#homeSlidePop").hasClass("active")) {
				$("#homeSlidePop .pop-title").text("审批历史");
				//$("#homeSlidePop .body-content").empty().load("");
			}
			var url = "definitionController.do?showTaskOpinions&taskId="+taskId;
			$("#myFrame").empty().append("<iframe id='myFrame' width='100%' height='500px' src='"+url+"'></iframe>");
		}
	}); */
// 	home.loadSlimScroll([ {
// 		obj : $('.sys-list'),
// 		height : "503px"
// 	}]);
});
</script>