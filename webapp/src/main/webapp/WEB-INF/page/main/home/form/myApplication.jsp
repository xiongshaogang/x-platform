<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="tableName" value="t_auto_mxcs" />
<input type="hidden" id="formId" value="402881b35104cf450151053b83e70073" />
<script type="text/javascript" src="basic/js/jquery.twbsPagination.min.js"></script>
<!-- <span id="402881b35104cf450151053b83e70564">id:402881b35104cf450151053b83e70564 </span> -->
<!-- <div class="text-center header">
</div> -->
<div id="shenpi">
	<div class="sys-list" id="myApplicationDiv">
		<ul class="list-style-none sys-list-container no-padding" id="myApplicationListUl">
		</ul>
		
	
	</div>
	<!-- 	<div id="coupon_pagination">
		分页区域
		<ul class="bbb-pagination list-style-none-h text-center hidden-num" id="coupon_paginationUl"></ul>
	</div> -->
</div>

<script type="text/javascript">

var commonFormEdit=function(id){
	var tableName=$("#tableName").val();
	var formId=$("#formId").val();
	window.location.href="appFormTableController.do?commonFormEdit&formId="+formId+"&id="+id;
}

var myApplication_scroll;
var myApplication_content=$("#myApplicationDiv");
var myApplication = {
	getMyApplication : function(){
		var imgURL = '${attachForeRequest}';
		var pagenumber = myApplication_content.data('pagenumber');
		if(pagenumber == "" || pagenumber == null || typeof(pagenumber) == "undefined"){
			pagenumber = 1;
		}
		$.ajax({
			url : 'processInstanceController.do?requestInstanceDatagrid&isApp=1&rows=10&page='+pagenumber,
			type : 'post',
			data: '',
			dataType: "json",
		    success : function(result) {
				
				if(result.success && result.obj.length > 0){
					if(pagenumber == 1){
						$("#myApplicationListUl").empty();
					}
					//myApplication.loadPage();
					$.each(result.obj, function(i, item) {
						//var myExtras = item.extras;
						if(item.extra != "" && item.extra != null){
							extra = parseJSON(item.extra);
							var date = new Date(extra.createTime);
							var content = "";
							var title = "";
							var portrait = "";
							var moduleName = "";
							if(extra.moduleName != null && extra.moduleName != "" && typeof(extra.moduleName) != "undefined"){
								moduleName = extra.moduleName;
							}
							$.each(extra.extraData, function(i, myExtras) {
									content += "<div class='content-item'>"
										+ "<span class='label'>"+myExtras.name+":</span>"
										+ "<span class='text'>"+myExtras.value+"</span>"
										+ "</div>";
							});
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
							if(extra.titleContent != null && extra.titleContent != "" && typeof(extra.titleContent) != "undefined"){
								title = "<div class='content-item'>"
									+ "<span class='label'>"+extra.titleContent.name+"：</span>"
									+ "<span class='text'>"+extra.titleContent.value+"</span>"
									+ "</div>";
							}
								if(content != ""){
									content = "<div class='item-content'>"
									+ "<div class='content-inner'>"
									+title
									+ content
									+ "</div>"
									+ "</div>";
								}
								if(content != ""){
									var templi = "<li id='"+extra.id+"' data-code='"+item.formCode+"' class='list-item' data-title='"+moduleName+"'>"
										+ "<div class='item-top'>"
										+ "<span class='item-title'>"+moduleName+"</span>"
										+ "<span class='item-ctime'>"+extra.createTime+"</span>"
										//+ "<i class='fa fa-close' ></i>"
										+ "</div>"
										+ content
										+portrait
										+ "</li>";
									
									$("#myApplicationListUl").append(templi);
								}
						}
					});
					

					
					$("#myApplicationListUl li").off().on("click", function(e){
						var formCode = $("#"+this.id).attr("data-code");
						id = this.id;
						//左上角出现后腿样式
						$("#leftName").html("<i class='fa fa-angle-left'></i>");
						$("#commonFormEdit").load("appFormTableController.do?commonFormEdit&formCode="+formCode+"&businessKey="+id+"&viewType=detail");
						//修改顶上名称为title
						$("#appName").text($("#"+id).attr("data-title"));
						
					});
					
					if (myApplication_scroll) {
						if(myApplication_content.data("hasInit")){
							//页面已加载过数据的情况
							myApplication_scroll.refresh();
							iscrollAssist.pullActionCallback(myApplication_scroll);
							return ;
						}
					}
					
					//第一次进入(scroll未实例化)/二次进入,但是是首次加载并有数据(需要重新实例化scroll,即使之前已存在)
					setTimeout(function(){
						myApplication_content.nodata.hide();
						myApplication_content.wrapper.show();
						myApplication_scroll = iscrollAssist.initScroll(myApplication_content, myApplication.pullDownAction, myApplication.pullUpAction,{
							autoLoadMargin:800
						});
						myApplication_content.data("hasInit",true);
					},0);
					
				/* 	$("#myApplicationListUl li .fa-close").on("click", function(e){
						if(confirm("确认删除该数据?")){
							var $this=$(this);
							var tableName=$("#tableName").val();
							var id=$this.closest("li").attr("id");
							if(tableName&&id){
								ajaxTip("appFormTableController.do?deleteData&tableName="+tableName+"&id="+id,function(){
									$this.closest("li").remove();
								});
							}
						}
						e.stopPropagation();
					}); */
			 }else{
				 if(myApplication_scroll){
						if(myApplication_content.data("hasInit")){
							//页面已加载过数据的情况
							iscrollAssist.pullActionNoMore(myApplication_scroll);
							//本次加载没数据时,pagenumber保持不变
							myApplication_content.data('pagenumber', pagenumber - 1);
							return;
						}
					}
					
					//页面当前无数据(第一次进入就没数据/二次进入页面没数据)
					myApplication_content.nodata.show();
					myApplication_content.wrapper.hide();
				 $("#myApplicationListUl").append("暂无数据");
			 }
		    }
		});

	},
/* 	loadPage : function(){
		//debugger;
		$('.bbb-pagination').twbsPagination({
			totalPages: 7,
			visiblePages: 7,
			paginationClass: "bbb-pagination",
			first: "首页",
			prev: "上一页",
			next: "下一页",
			last: "尾页",
			onPageClick: function (event, page) {
				$("#myApplicationDiv").data("pagenumber",page);
				myApplication.getMyApplication();
			}
		});
	}, */
	/**
	* 上拉加载下一页
	*/
	pullUpAction : function() {
		var pagenumber = myApplication_content.data('pagenumber');
		if (pagenumber) {
			//页数+1
			var next_page = parseInt(pagenumber) + 1;
		} else {
			//如果绑定的data没值,下拉直接第二页
			var next_page = 2;
		}
		//修改data中的页数
		myApplication_content.data('pagenumber', next_page);
		//加载数据
		myApplication.getMyApplication();
	},
	/**
	* 下拉回到第一页
	*/
	pullDownAction : function() {
		myApplication_content.data('pagenumber', 1);
		myApplication.getMyApplication();
	}

};

$(function(){
	myApplication_content.data('pagenumber', 1);
	iscrollAssist.initDOM(myApplication_content, myApplication.pullDownAction, myApplication.pullUpAction);
	myApplication.getMyApplication();
})
</script>
