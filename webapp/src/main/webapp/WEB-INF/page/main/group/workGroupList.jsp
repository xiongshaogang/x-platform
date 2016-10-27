<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="content-pannel" id="workGroupList">
	<div class="header">
		<span id="appName">我的工作组</span>
		<div class="btn-right">
			<span id="rightName" class="dropdown-btn"> <i class="fa fa-plus"></i>
			</span>
		</div>
	</div>
	<div class="work-group-content">
		<ul class="list-style-none msg-list width-auto" id="workGroupUl">
		</ul>
	</div>
</div>

<script>
var workGroupList = {
		queryWorkGroupList : function(){
			ajax('orgGroupController.do?queryUserGroups', function(result) {
				if(result.success){
					var templi = "";
					$("#workGroupUl").empty();
					$.each(result.obj, function(i, item) {
						templi = "<li onclick='workGroupList.goGroupSettingsEdit(this,event)' id='"+item.id+"'>"
							+"<span class='name'>"+item.name+"</span>"
							+"<span class='pull-right'>"+datetimeFormatters(new Date(item.createTime))+"</span>"
							+"</li>";
							
						templi = "<li onclick='workGroupList.goGroupSettingsEdit(this,event)' id='"+item.id+"'>"
							+"<div class='avatar without-bubble group'>"
							+"<img src='basic/img/avatars/adam-jansen.jpg' alt='头像'>"
							+"<img src='basic/img/avatars/adam-jansen.jpg' alt='头像'>"
							+"<img src='basic/img/avatars/adam-jansen.jpg' alt='头像'>"
							+"<img src='basic/img/avatars/adam-jansen.jpg' alt='头像'>"
							+"</div>"
							+"<div class='info'>"
							+"<span class='text-overflow username'>"+item.name+"</span> <span class='pull-right light-grey rec-time'>"+datetimeFormatters(new Date(item.createTime))+"</span>"
							+"</div>"
							+"<div class='info'>"
							+"<span class='light-grey text-overflow detail'>佛山市顺德区某公司</span> <span class='pull-right light-grey type'>受理审批</span>"
							+"</div>"
							+"</li>";
							
						$("#workGroupUl").append($(templi));
					});
				}else{
					$("#workGroupList").append("暂无工作组");
				}
			});
		},
		goGroupSettingsEdit:function(ele,event){
			event.stopPropagation();
			$this=$(ele);
			$("#homeSlidePop").toggleClass("active");
			if ($("#homeSlidePop").hasClass("active")) {
				$("#homeSlidePop .pop-title").text("修改工作组");
				$("#homeSlidePop .body-content").empty().load("orgGroupController.do?groupSettingsEdit&optFlag=update&groupId="+$this.attr("id"));
			}
		}
};

$(function(){
	workGroupList.queryWorkGroupList();
	
	$("#workGroupList #rightName").on("click",function(event){
		event.stopPropagation();
		$("#homeSlidePop").toggleClass("active");
		if ($("#homeSlidePop").hasClass("active")) {
			$("#homeSlidePop .pop-title").text("新增工作组");
			$("#homeSlidePop .body-content").empty().load("orgGroupController.do?groupSettingsEdit&optFlag=add");
		}
	});
	home.loadSlimScroll([{
		obj : $("#workGroupUl"),
		height: 503-26
	}]);
});

</script>