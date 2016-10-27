<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<input type="hidden" id="gs-groupId" value="${group.id}" />
<div class="group-setting">
	<div class="group-setting-body-container">
		<div class="common-form">
			<div class="form-field-box form-usergroup clearfix">
				<div class="top clearfix">
					<label>群成员：</label>
					<span class="pull-right">
						<span  id="allGroupUserCounts">0</span>人
					</span>
				</div>
				<div class="content">
					<ul class="list-style-none-h user-group no-ligature">
						<li onclick="workGroupAdd.selectCandidate(this)" flag="addAssign">
							<div class="btn-add" >
								<i class="fa fa-plus"></i>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<div class="form-field-box seperator"></div>
			<div class="form-field-box clearfix">
				<label>群名称：</label> <input id="group-name" class="group-name" placeholder="请输入名称" type="text" value="${group.name}" >
			</div>
		</div>
	</div>
	<div class="group-setting-footer text-center">
		<button class="btn btn-orange" id="workGroupAddSubmit">提交建群</button>
	</div>
</div>

<div class="modal fade" id="selectUserModal" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">选择工作组成员(点击头像删除)</h4>
			</div>
			<div class="modal-body clearfix">
				<div class="pull-left all-person-box">
					<div class="top">
						<span>选择成员：</span>
						<div class="search">
							<input type="text" id="searchUser" place-holder="搜索成员"> <i class="glyphicon glyphicon-search"></i>
						</div>
					</div>
					<div class="person-box">
						<div class="person-list all">
							<ul class="list-style-none" id="userList">
							</ul>
						</div>
					</div>
				</div>
				<div class="pull-right selected-box">
					<div class="top">
						<span>新添成员：</span> <span class="pull-right">已选<span class="selected-count">0</span>人
						</span>
					</div>
					<div class="person-box">
						<div class="person-list approval-flow" id="selectedUser">
							<ul class="list-style-none-h">
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="selectTranConfirm" type="button" class="btn btn-orange" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>

<script>
	var allUserMap = new Map();
	var existUserMap = new Map();
	var allCanSelectUserMap = new Map();
	var selectedUserMap = new Map();
	var selectUserModal = $("#selectUserModal");
	var allUserUL=$("#userList",selectUserModal);
	var selectedUserUl=$("#selectedUser ul",selectUserModal);
	var selected_count = $(".selected-count",selectUserModal);
	var groupNowUserUl = $(".person-now ul");
	var addAssignLi=$("[flag=addAssign]");
	var multiplePersonSelect=true;
	var timer=null;
	
	var existUser = {
		id : user_id,
		name : user_name,
		portrait : user_portrait
	};
	existUserMap.put(existUser.id, existUser);
	
	var workGroupAdd={
		selectCandidate:function(){
			var groupId=$("#gs-groupId").val();
			home.loadSlimScroll([ {
				obj : $('.group-setting-body')
			},{
				obj : $('.person-list'),
			}]);
			selectUserModal.modal("show");
			selectedUserUl.empty();
			//选人确定完成选择事件
			$("#selectTranConfirm",selectUserModal).off().on("click", function() {
				var selectingUserLis=$("li",selectedUserUl);
				if(selectingUserLis.length>0){
					var userIds="";
					$.each(selectingUserLis,function(){
						userIds+=$(this).attr("id")+",";
						existUserMap.put($(this).attr("id"),$(this).data());
						workGroupAdd.addUserToNowUl($(this).data());
					});
					$("#allGroupUserCounts").html(existUserMap.size());
				}
			});
		},
		initPersonUl:function(result){
			$.each(result, function(i, item) {
				var userInfo={
					id : item.id,
					name : item.name,
					portrait : item.portrait,
					sortKey : item.sortKey,
					searchKey : item.searchKey
		    	};
				var portrait=item.portraits ? '${attachForeRequest}' + item.portrait : 'basic/img/avatars/avatar_80.png';
				var templi = $("<li id='"+item.id+"'>"
							+"<div class='avatar'><img src='"+portrait+"'></div>"
							+"<div class='name'><p class='text-overflow' title='"+item.name+"'>"+item.name+"</p></div>"
							+"</li>");
				templi.data(userInfo);
				allUserUL.append(templi);
			});
			
		    //选人弹框中，左侧所有人列表项点击添加到右侧
		    $(".person-list.all li").off().on("click",function(){
		   		if(!multiplePersonSelect&&$("li",selectedUserUl).length>=1){
		   			alert("只能选择一人");
		   		}else{
		   			if($("li#"+this.id,selectedUserUl)[0]){
		   				alert("已选择["+$(this).data("name")+"]");
		   				return;
		   			}
		   			if(existUserMap.containsKey(this.id)){
		   				alert("["+$(this).data("name")+"]已在群组中,请勿重复添加");
		   				return;
		   			}
		   			var $thisLi = $("<li id='"+this.id+"'>"+
			    			"<div class='item-person'>"+
			    			$(this).html()+
			    			"</div>"+
			    			"<i class='glyphicon glyphicon-remove'></i>"+
			    			"</li>").click(function(){
			    				selected_count.html($("li",selectedUserUl).length);
			    				$(this).closest("li").remove();
			    			});
		   			$thisLi.data($(this).data());
		    		selectedUserUl.append($thisLi);
		    		selected_count.html($("li",selectedUserUl).length);
		   		}
		    });
		    $("li .glyphicon-remove",selectedUserUl).on("click", function(){
		    	$(this).closest("li").remove();
		    	selected_count.html($("li",selectedUserUl).length);
		    });
		},
		//添加用户到现有群成员区域
		addUserToNowUl:function(user) {
			var portrait = defaultImg('${attachForeRequest}',user.portrait);
			var li = $('<li onclick="workGroupAdd.removeCandidate(this)"><img class="avatar" src="'+portrait+'"><span class="name text-overflow">' + user.name + '</p></li>');
			li.data(user);
			addAssignLi.before(li);
		},
		//点击移除候选人
		removeCandidate:function(ele){
			$this=$(ele);
			var id=$this.data("id");
			if(user_id&&user_id==id){
				alert("无法移除自己,您可以选择解散/退出该群");
			}else{
				existUserMap.remove(id);
				$("#allGroupUserCounts").html(existUserMap.size());
				$(ele).remove();
			}
		}
	};

	$(function() {
		//查询可选人
		ajax("orgGroupController.do?queryUsersByWork",function(result){
			if(result.success){
				workGroupAdd.initPersonUl(result.obj);
			}
		});
		//默认增加自己为群成员
		$.each(existUserMap.values(),function(){
			workGroupAdd.addUserToNowUl(this);
		});
		$("#allGroupUserCounts").html(existUserMap.size());
		
		
		//输入0.5秒后才进行查询
		$("#searchUser").on("input propertychange",function(){
			clearTimeout(timer);
			timer=setTimeout(function(){
				var key = encodeURI(encodeURI($("#searchUser").val()));
				ajax('orgGroupController.do?queryUsersByLikeKey&key='+key,function(result){
					allUserUL.empty();
					workGroupAdd.initPersonUl(result.obj);
				});
			},500);
	    });
		
		//提交事件
		$("#workGroupAddSubmit").on("click",function(){
			var userIds="";
			var groupName=nulls($("#group-name").val());
			$.each(existUserMap.values(),function(){
				userIds+=this.id+",";
			});
			if(userIds&&userIds.length==0){
				alert("您还未选择成员");		
				return;
			}
			if(groupName.length==0){
				if(!confirm("您未指定群名称,是否确认创建(默认将群成员名称拼接作为群名称)")){
					return ;
				}
			}
			//请求服务器创建群
			ajaxTip("orgGroupController.do?save&workGroup=1&ids=" + userIds.removeDot()+"&name="+groupName, function(data) {
				$("#homeSlidePop").toggleClass("active");
				$('#account_right').load('orgGroupController.do?workGroupList');
			});
		});
	})
</script>