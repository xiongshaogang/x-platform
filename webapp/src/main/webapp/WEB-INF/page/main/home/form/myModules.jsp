<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="app-list-container" id="modulesDiv">
	<div class="header">我的任务</div>
	<div class="sys-list">
		<ul id="modules" class="list-style-none sys-list-container">
		<!-- 	<li class="list-item">
				<div class="item-top">
					<span class="item-title">我的项目</span>
					<span class="item-ctime">2015-05-01</span>
				</div>
				<div class="item-content">
					<p class="content-header">
						张三的出差需要您的审批
					</p>
					<div class="content-inner">
						<div class="content-item img">
							<img src="basic/img/background_home.jpg" alt="图片">
						</div>
						<div class="content-item">
							<span class="text text-top">张三的出差需要您的审批</span>
						</div>
						<div class="content-item">
							<span class="label">评论意见：</span>
							<span class="text">已完成所有待审批任务，准备接受下一个审批任务。</span>
						</div>
						<div class="content-item">
							<span class="label">评论意见：</span>
							<span class="text">已完成所有待审批任务，准备接受下一个审批任务。</span>
						</div>
					</div>
					<p class="content-footer">
						帮帮帮团队，2015-06-25
					</p>
				</div>
				<div class="item-bottom">
					<ul class="list-style-none-h user-group">
						<li class="active"><img src="basic/img/avatars/Javi-Jimenez.jpg"></li>
						<li><img src="basic/img/avatars/Javi-Jimenez.jpg"></li>
						<li><img src="basic/img/avatars/Javi-Jimenez.jpg"></li>
					</ul>
				</div>
			</li> -->
<!-- 			<li class="list-item">
				<div class="item-top">
					<span class="item-title">我的项目</span>
					<span class="item-ctime">2015-05-01</span>
				</div>
				<div class="item-content">
					<p class="content-header">
						张三的出差需要您的审批
					</p>
					<div class="content-inner">
						<div class="content-item img">
							<img src="basic/img/background_home.jpg" alt="图片">
						</div>
						<div class="content-item">
							<span class="text text-top">张三的出差需要您的审批</span>
						</div>
						<div class="content-item">
							<span class="label">评论意见：</span>
							<span class="text">已完成所有待审批任务，准备接受下一个审批任务。</span>
						</div>
						<div class="content-item">
							<span class="label">评论意见：</span>
							<span class="text">已完成所有待审批任务，准备接受下一个审批任务。</span>
						</div>
					</div>
					<p class="content-footer">
						帮帮帮团队，2015-06-25
					</p>
				</div>
				<div class="item-bottom">
					<ul class="list-style-none-h user-group">
						<li class="active"><img src="basic/img/avatars/Javi-Jimenez.jpg"></li>
						<li><img src="basic/img/avatars/Javi-Jimenez.jpg"></li>
						<li><img src="basic/img/avatars/Javi-Jimenez.jpg"></li>
					</ul>
				</div>
			</li> -->
		</ul>
	</div>
</div>

<script>
var myMudules = {
		queryFlowFormList : function(){
			$.ajax({
				url : 'flowFormController.do?queryMyCreateFlowFormList',
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					if(result.success && result.obj.length >0){
						var templi = "";
						var status = "";
						$("#modules").empty();
						console.log(result);
						$.each(result.obj, function(i, item) {
							if(item.status == "1"){
								status = "已发布";
							}else{
								status = "未发布";
							}
							templi = templi + "<li  id='"+item.id+"' class='list-item'>"
											+"<div class='item-top'>"
											+"<span class='item-title'>"+item.name+"</span>"
											+"<span class='item-ctime'>"+datetimeFormatters(new Date(item.createTime))+"</span>"
											+"<i class='fa fa-close'></i>" 
											+"</div>"
											+"<div class='item-content'>"
											+"<div class='content-inner'>"
											+"<div class='content-item'>"
											+"<span class='label'>版本号：</span>"
											+"<span class='text'>"+item.version+"</span>"
											+"</div>"
											+"<div class='content-item'>"
											+"<span class='label'>发布状态：</span>"
											+"<span class='text'>"+status+"</span>"
											+"</div>"
											+"</div>"
											+"</div>"
											+"</li>";
						/* 	templi = templi + "<li id='"+item.id+"'>"
								+"<span class='name'>"+item.name+"</span>"
								+"<span class='pull-right'>"+(new Date(item.createTime)).toLocaleDateString()+"</span>"
								+"<i class='fa fa-close'></i>"
								+"</li>";	 */					
							
						});
						$("#modules").append(templi);
						$("#modules li").on("click", function(){
							$(".menu-pannel-body").empty().append("<iframe id='formHomeIframe' src='appFormFieldController.do?appForm&id="+this.id+"' width='100%' height='100%'></iframe>");
							//$(".menu-pannel-body").load("appFormFieldController.do?appForm&id="+this.id);
						});
						//删除任务
						$("#modules li .fa-close").on("click", function(e){
							e.stopPropagation();
							if(confirm("确认删除该数据?")){
								var $this=$(this);
								var id=$this.closest("li").attr("id");
								if(id){
									ajaxTip("flowFormController.do?deleteFlowForm&formId="+id,function(){
										$this.closest("li").remove();
									});
								}
							}
							
						});
					}else{
						$("#modulesDiv").append("暂无数据");
					}
				}
			});
		}	
};
$(function(){
	myMudules.queryFlowFormList();
	
	home.loadSlimScroll([{
		obj : $("#modules").parent(".sys-list"),
		//width: "101%",
		height: "503px"
	}]);
});

</script>