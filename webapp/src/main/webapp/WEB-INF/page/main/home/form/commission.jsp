<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="sys-tab">
	<ul class="nav nav-tabs list-style-table" role="tablist">
		<li role="presentation" class="active"><a href="#cm_undone" aria-controls="cm_undone" role="tab" data-toggle="tab">未审批</a></li>
		<li role="presentation"><a href="#cm_done" aria-controls="cm_done" role="tab" data-toggle="tab">已审批</a></li>
	</ul>
	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active" id="cm_undone">
			<div id="undone">
				<div id="commission">
					
				</div>
				
				<!-- <div id="coupon_pagination">
					分页区域
					<ul class="bbb-pagination list-style-none-h text-center hidden-num" id="coupon_paginationUl"></ul>
				</div> -->
			</div>
		</div>
		<div role="tabpanel" class="tab-pane" id="cm_done">
			<div id="done">
				<div id="already">
					
				</div>
				
				<!-- <div id="coupon_pagination1">
					分页区域
					<ul class="bbb-pagination list-style-none-h text-center hidden-num" id="coupon_paginationUl1"></ul>
				</div> -->
			</div>
		</div>
	</div>
</div>


<script>
var unDone_scroll;
var unDone_content=$("#undone");
var done_scroll;
var done_content=$("#done");

var commission = {

		getCommission : function(){
			var imgURL = '${attachForeRequest}';
			var page = unDone_content.data('pagenumber');
			if(page == null || page == "" || typeof(page) == "undefined"){
				page = 1;
			}
			if(page == 1){
				$("#commission").empty();
			}

			$.ajax({
				url : 'taskController.do?myTask&page='+page,
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					//debugger;
					if(result.success && result.obj.length>0){
						//commission.loadPage();
						var templi = "<div class='sys-list'><ul id='commissionUl' class='list-style-none sys-list-container no-padding'>";
						
						$.each(result.obj, function(i, item) {
							var content = "";
							var contentStr = "";
							var img = "";
							var portrait = "";
							var approveStatus = "";
							var contentAndportrait = "";
							var title = "";
							var userInfo = "";
							var moduleName = "";
							
							var extra = JSON.parse(item.extra);
							
							/* if(extra.title != null && extra.title!=""){
								if(extra.title.length > 30){
								   title = extra.title.substr(0,29)+"...";
								}else{
								   title = extra.title;
								}
							}else{
								title="";
							} */
							
							if(extra.extraData){
								if(extra.moduleName != null && extra.moduleName != "" && typeof(extra.moduleName) != "undefined"){
									moduleName = extra.moduleName;
								}
								if(extra.titleContent != null && extra.titleContent != "" && typeof(extra.titleContent) != "undefined"){
									title = "<div class='content-item'>"
										+ "<span class='label'>"+extra.titleContent.name+"：</span>"
										+ "<span class='text'>"+extra.titleContent.value+"</span>"
										+ "</div>";
								}
								if(extra.userInfo != null && extra.userInfo != "" && typeof(extra.userInfo) != "undefined"){
									userInfo = "<div class='content-item'>"
										+ "<span class='label'>"+extra.userInfo.name+"：</span>"
										+ "<span class='text'>"+extra.userInfo.value+"</span>"
										+ "</div>";
								}
								$.each(extra.extraData, function(j, item1) {
									content += "<div class='content-item'>"
									+ "<span class='label'>"+item1.name+"：</span>"
									+ "<span class='text'>"+item1.value+"</span>"
									+ "</div>";
								});
								if(content != ""||userInfo !="" ||title!=""){
									contentStr = "<div class='item-content'>"
									+ "<div class='content-inner'>" 
									+userInfo
									+title
									+ content
									+ "</div>"
									+ "</div>";
								}
							}
							if(extra.groupUserList){
								$.each(extra.groupUserList, function(j, item2) {
									portrait += "<li><img src='"+imgURL+item2.portrait+"'></li>";
								});
								if(portrait != ""){
									portrait = "<div class='item-bottom'>"
									+ "<ul class='list-style-none-h user-group'>"
									+portrait
									+ "</ul>"
									+ "</div>";
								}
							}
							if(contentStr != "" || portrait != ""){
								contentAndportrait = contentStr + portrait;
							}
							
							 templi += "<li class='list-item' id='"+item.id+"' data-title='"+moduleName+"' data-actInstId ='"+item.processInstanceId+"'>"
								+ "<div class='item-top'>"
								+ "<span class='item-title'>"+moduleName+"</span>"
								+ "<span class='item-ctime'>"+extra.createTime+"</span>"
								+ "</div>"
							    + contentAndportrait
								+ "</li>";
							
		               		
						});
		               		templi +="</ul></div>";
            			   	$("#commission").append(templi);
            			   	//清空title的所有绑定数据
            			   	$("#rightName").text("");
            			   	$("#appName").text("我的审批");
            			   	$("#appList").attr("data-type","");
							$("#appList").attr("data-code","");
							$("#leftName").attr("data-url","");
							$("#leftName").attr("data-type","");
							$("#leftName").attr("data-appName","");
							
            			   	$("#commissionUl li").off().on("click",function(){
            			   		$("#leftName").html("<img src='basic/img/icon_back.png' alt='后退'>");
            			   		var actInstId = $("#"+this.id).attr("data-actInstId");
            			   		var appTitle = $("#"+this.id).attr("data-title");
								$("#leftName").attr("data-daiban",1);
            			   		$("#commonFormEdit").load("taskController.do?toStart&taskId="+this.id);
            			   		//$("#appList").attr("data-daiBanUrl","definitionController.do?showTaskOpinions&actInstId="+actInstId);
            			   		$("#appList").attr("data-type","4");
            			   		$("#appName").text(appTitle);
            			   		//$("#rightName").text("审批历史");
            			   		$("#leftName").attr("data-url","flowFormController.do?commission");
            			   		//$("#leftName").attr("data-appName","审批历史");
            			   	 	$("#leftName").attr("data-type","3");
            			   	});
            			   	
							if (unDone_scroll) {
								if(unDone_content.data("hasInit")){
									//页面已加载过数据的情况
									unDone_scroll.refresh();
									iscrollAssist.pullActionCallback(unDone_scroll);
									return ;
								}
							}
							
							//第一次进入(scroll未实例化)/二次进入,但是是首次加载并有数据(需要重新实例化scroll,即使之前已存在)
							setTimeout(function(){
								unDone_content.nodata.hide();
								unDone_content.wrapper.show();
								unDone_scroll = iscrollAssist.initScroll(unDone_content, commission.pullDownAction, commission.pullUpAction,{
									autoLoadMargin:800
								});
								unDone_content.data("hasInit",true);
							},0);
					}else{
						if(unDone_scroll){
							if(unDone_content.data("hasInit")){
								//页面已加载过数据的情况
								iscrollAssist.pullActionNoMore(unDone_scroll);
								//本次加载没数据时,pagenumber保持不变
								unDone_content.data('pagenumber', page - 1);
								return;
							}
						}
						
						//页面当前无数据(第一次进入就没数据/二次进入页面没数据)
						unDone_content.nodata.show();
						unDone_content.wrapper.hide();
						$("#commission").append("暂无数据");
						//alert("已经是最后一页");
						//$("#commission").append("无数据");
						//分页样式变灰，不可点击
					}
				}
			});
		},
		/**
		* 上拉加载下一页
		*/
		pullUpAction : function() {
			var pagenumber = unDone_content.data('pagenumber');
			if (pagenumber) {
				//页数+1
				var next_page = parseInt(pagenumber) + 1;
			} else {
				//如果绑定的data没值,下拉直接第二页
				var next_page = 2;
			}
			//修改data中的页数
			unDone_content.data('pagenumber', next_page);
			//加载数据
			commission.getCommission();
		},
		/**
		* 下拉回到第一页
		*/
		pullDownAction : function() {
			unDone_content.data('pagenumber', 1);
			commission.getCommission();
		},
		getAlready : function(){
			var imgURL = '${attachForeRequest}';
			var page = done_content.data('pagenumber');
			if(page == null || page == "" || typeof(page) == "undefined"){
				page = 1;
			}
			if(page == 1){
				$("#already").empty();
			}

			$.ajax({
				url : 'taskController.do?completeTaskDatagrid&isApp=1&page='+page,
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					//debugger;
					if(result.success && result.obj.length>0){
						//commission.loadPage1();
						var templi = "<div class='sys-list'><ul id='commissionUl' class='list-style-none sys-list-container no-padding'>";
						
						$.each(result.obj, function(i, item) {
							var content = "";
							var contentStr = "";
							var img = "";
							var portrait = "";
							var approveStatus = "";
							var contentAndportrait = "";
							var title = "";
							var userInfo = "";
							var moduleName = "";
							
							var extra = parseJSON(item.extra);

							/* if(extra.title != null && extra.title!=""){
								if(extra.title.length > 30){
								   title = extra.title.substr(0,29)+"...";
								}else{
								   title = extra.title;
								}
							}else{
								title="";
							} */
							
							if(extra.extraData){
								if(extra.moduleName != null && extra.moduleName != "" && typeof(extra.moduleName) != "undefined"){
									moduleName = extra.moduleName;
								}
								if(extra.titleContent != null && extra.titleContent != "" && typeof(extra.titleContent) != "undefined"){
									title = "<div class='content-item'>"
										+ "<span class='label'>"+extra.titleContent.name+"：</span>"
										+ "<span class='text'>"+extra.titleContent.value+"</span>"
										+ "</div>";
								}
								if(extra.userInfo != null && extra.userInfo != "" && typeof(extra.userInfo) != "undefined"){
									userInfo = "<div class='content-item'>"
										+ "<span class='label'>"+extra.userInfo.name+"：</span>"
										+ "<span class='text'>"+extra.userInfo.value+"</span>"
										+ "</div>";
								}
								$.each(extra.extraData, function(j, item1) {
									content += "<div class='content-item'>"
									+ "<span class='label'>"+item1.name+"：</span>"
									+ "<span class='text'>"+item1.value+"</span>"
									+ "</div>";
								});
								if(content != ""||userInfo !="" ||title!=""){
									contentStr = "<div class='item-content'>"
									+ "<div class='content-inner'>" 
									+userInfo
									+title
									+ content
									+ "</div>"
									+ "</div>";
								}
							}
							if(extra.groupUserList){
								$.each(extra.groupUserList, function(j, item2) {
									portrait += "<li><img src='"+imgURL+item2.portrait+"'></li>";
								});
								if(portrait != ""){
									portrait = "<div class='item-bottom'>"
									+ "<ul class='list-style-none-h user-group'>"
									+portrait
									+ "</ul>"
									+ "</div>";
								}
							}
							if(contentStr != "" || portrait != ""){
								contentAndportrait = contentStr + portrait;
							}
							
							 templi += "<li class='list-item' id='"+item.id+"' data-title='"+moduleName+"' data-actInstId ='"+item.processInstanceId+"'>"
								+ "<div class='item-top'>"
								+ "<span class='item-title'>"+moduleName+"</span>"
								+ "<span class='item-ctime'>"+extra.createTime+"</span>"
								+ "</div>"
							    + contentAndportrait
								+ "</li>";
							
		               		
						});
		               		templi +="</ul></div>";
            			   	$("#already").append(templi);
            			   	//清空title的所有绑定数据
            			   	$("#rightName").text("");
            			   	$("#appName").text("我的审批");
            			   	$("#appList").attr("data-type","");
							$("#appList").attr("data-code","");
							$("#leftName").attr("data-url","");
							$("#leftName").attr("data-type","");
							$("#leftName").attr("data-appName","");
							
            			   	$("#commissionUl li").off("click").on("click",function(){
            			   		$("#leftName").html("<img src='basic/img/icon_back.png' alt='后退'>");
            			   		var actInstId = $("#"+this.id).attr("data-actInstId");
            			   		var appTitle = $("#"+this.id).attr("data-title");
								$("#leftName").attr("data-daiban",1);
            			   		$("#commonFormEdit").load("taskController.do?toStart&taskId="+this.id);
            			   		//$("#appList").attr("data-daiBanUrl","definitionController.do?showTaskOpinions&actInstId="+actInstId);
            			   		//$("#appList").attr("data-type","4");
            			   		$("#appName").text(appTitle);
            			   		//$("#rightName").text("审批历史");
            			   		$("#leftName").attr("data-url","flowFormController.do?commission");
            			   		//$("#leftName").attr("data-appName","审批历史");
            			   	 	$("#leftName").attr("data-type","3");
            			   	});
            			   	
            			   	if (done_scroll) {
    							if(done_content.data("hasInit")){
    								//页面已加载过数据的情况
    								done_scroll.refresh();
    								iscrollAssist.pullActionCallback(done_scroll);
    								return ;
    							}
    						}
    						
    						//第一次进入(scroll未实例化)/二次进入,但是是首次加载并有数据(需要重新实例化scroll,即使之前已存在)
    						setTimeout(function(){
    							done_content.nodata.hide();
    							done_content.wrapper.show();
    							done_scroll = iscrollAssist.initScroll(done_content, commission.pullDownAction1, commission.pullUpAction1,{
    								autoLoadMargin:800
    							});
    							done_content.data("hasInit",true);
    						},0);
					}else{
						if(done_scroll){
							if(done_content.data("hasInit")){
								//页面已加载过数据的情况
								iscrollAssist.pullActionNoMore(done_scroll);
								//本次加载没数据时,pagenumber保持不变
								done_content.data('pagenumber', pagenumber - 1);
								return;
							}
						}
						
						//页面当前无数据(第一次进入就没数据/二次进入页面没数据)
						done_content.nodata.show();
						done_content.wrapper.hide();
						$("#already").append("暂无数据");
						//alert("已经是最后一页");
						//$("#commission").append("无数据");
						//分页样式变灰，不可点击
					}
				}
			});
		},	
		/**
		* 上拉加载下一页
		*/
		pullUpAction1 : function() {
			var pagenumber = done_content.data('pagenumber');
			if (pagenumber) {
				//页数+1
				var next_page = parseInt(pagenumber) + 1;
			} else {
				//如果绑定的data没值,下拉直接第二页
				var next_page = 2;
			}
			//修改data中的页数
			done_content.data('pagenumber', next_page);
			//加载数据
			commission.getAlready();
		},
		/**
		* 下拉回到第一页
		*/
		pullDownAction1 : function() {
			done_content.data('pagenumber', 1);
			commission.getAlready();
		}/* ,
		loadPage : function(){
			$('#coupon_paginationUl').twbsPagination({
				totalPages: 7,
				visiblePages: 7,
				paginationClass: "bbb-pagination",
				first: "首页",
				prev: "上一页",
				next: "下一页",
				last: "尾页",
				onPageClick: function (event, page) {
					$("#commission").data("pagenumber",page);
					commission.getCommission();
				}
			});
		},
		loadPage1 : function(){
			$('#coupon_paginationUl1').twbsPagination({
				totalPages: 7,
				visiblePages: 7,
				paginationClass: "bbb-pagination",
				first: "首页",
				prev: "上一页",
				next: "下一页",
				last: "尾页",
				onPageClick: function (event, page) {
					$("#already").data("pagenumber",page);
					commission.getAlready();
				}
			});
		} */
};

$(function(){
	unDone_content.data('pagenumber', 1);
	iscrollAssist.initDOM(unDone_content, commission.pullDownAction, commission.pullUpAction);
	commission.getCommission();
	done_content.data('pagenumber', 1);
	iscrollAssist.initDOM(done_content, commission.pullDownAction1, commission.pullUpAction1);
	commission.getAlready();


	
	
});
</script>