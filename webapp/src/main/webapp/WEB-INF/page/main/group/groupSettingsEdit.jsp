<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<input type="hidden" id="gs-groupId" value="${group.id}" />
<div class="group-setting">
	<div class="group-setting-body-container">
		<div class="common-form">
			<div class="form-field-box form-usergroup clearfix">
				<div class="top clearfix">
					<label>工作组成员：</label>
					<span class="pull-right">
						<span  id="allGroupUserCounts">0</span>人
					</span>
				</div>
				<div class="content">
					<ul class="list-style-none-h user-group no-ligature">
						<li onclick="groupSettingsEdit.selectCandidate(this)" flag="addAssign">
							<div class="btn-add" >
								<i class="fa fa-plus"></i>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<div class="form-field-box seperator"></div>
			<div class="form-field-box clearfix">
				<label>工作组名称：</label>
				<span class="row-value group-name-edit"> 
					<input id="group-name" class="group-name" type="text"
							value="${group.name}" disabled> 
					<span id="groupname-edit" class="row-link btn-edit"> 
						<i class="glyphicon glyphicon-edit"></i> <span>编辑</span>
					</span>
					<button id="groupname-confirm" class="btn btn-orange btn-confirm">确定</button>
				</span>
			</div>
<!-- 			<div class="form-field-box clearfix"> -->
<!-- 				<span class="row-name">仅群主可加人</span> -->
<!-- 				<div class="row-value"> -->
<!-- 					<input type="checkbox" name="value-switch" checked data-on-color="success" data-off-color="danger" data-size="mini" data-on-text="是" data-off-text="否" /> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<div class="form-field-box clearfix">
				<label class="row-name">工作组创建者:</label>
				<div class="row-value">
					${ownername}
				</div>
			</div>
		</div>
	</div>
	<div class="group-setting-footer text-center">
		<c:choose> 
		<c:when test="${ownername eq loginName}">   
		    <button class="btn btn-orange" id="deleteGroup">解散工作组</button>  
		</c:when> 
		<c:otherwise>
		 	<button class="btn btn-orange" id="quitGroup">退出工作组</button>
		</c:otherwise>
		</c:choose> 
	</div>
</div>

<div class="modal fade" id="selectUserModal" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">选择工作组成员</h4>
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


<div class="modal fade" id="selectUserModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">添加新成员</h4>
			</div>
			<div class="modal-body clearfix">
				<div class="pull-left all-person-box">
					<div class="top">
						<span>选择成员：</span>
						<div class="search">
							<input type="text" place-holder="搜索成员"> <i class="glyphicon glyphicon-search"></i>
						</div>
					</div>
					<div class="person-box">
						<div class="person-list all">
							<ul class="list-style-none">
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
						<div class="person-list selected">
							<ul class="list-style-none">
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="userSelect-confirm" type="button" class="btn btn-orange" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>

<script>
	var existUserMap = new Map();
	var allCanSelectUserMap = new Map();
	var selectedUserMap = new Map();
	var selectUserModal = $("#selectUserModal");
	var allUserUL=$("#userList",selectUserModal);
	var selectedUserUl=$("#selectedUser ul",selectUserModal);
	var selected_count = $(".selected-count",selectUserModal);
	var groupNowUserUl = $(".user-group ul");
	var addAssignLi=$("[flag=addAssign]");
	var multiplePersonSelect=true;
	var timer=null;

	<c:forEach var="existUser" items="${groupUsers}">
	var existUser = {
		id : '${existUser.id}',
		name : '${existUser.name}',
		portrait : '${existUser.portrait}',
		sortKey : '${existUser.sortKey}',
		searchKey : '${existUser.searchKey}'
	};
	existUserMap.put(existUser.id, existUser);
	</c:forEach>

	
	var groupSettingsEdit={
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
						groupSettingsEdit.addUserToNowUl($(this).data());
					});
					//请求服务器维护成员
					ajaxTip("orgGroupController.do?addUser&groupId=" + groupId + "&userIds=" + userIds.removeDot(), function(data) {
						$("#allGroupUserCounts").html(existUserMap.size());
					});
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
			var li = $('<li onclick="groupSettingsEdit.removeCandidate(this)"><img class="avatar" src="'+portrait+'"><span class="name text-overflow">' + user.name + '</span></li>');
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
				if(confirm("是否移除该群成员?")){
					//请求服务器维护成员
					ajaxTip("orgGroupController.do?deleteUser&groupId=" + $("#gs-groupId").val() + "&userIds=" + id, function(data) {
						existUserMap.remove(id);
						$("#allGroupUserCounts").html(existUserMap.size());
						$(ele).remove();
					});
				}
			}
		}
	};

	$(function() {
		var groupId = $("#gs-groupId").val();
		//查询可选人
		ajax("orgGroupController.do?queryUsersByWork",function(result){
			if(result.success){
				groupSettingsEdit.initPersonUl(result.obj);
			}
		});
		//退出群组
		$("#quitGroup").on("click",function(){
			if(confirm("确定解散?")){
				ajaxTip('orgGroupController.do?exitGroup&groupId='+groupId,function(result){
					$("#homeSlidePop").toggleClass("active");
					$('#account_right').load('orgGroupController.do?workGroupList');
				});
			}
		});
		//解散群组
		$("#deleteGroup").on("click",function(){
			if(confirm("确定解散?")){
				ajaxTip('orgGroupController.do?delete&groupId='+groupId,function(result){
					$("#homeSlidePop").toggleClass("active");
					$('#account_right').load('orgGroupController.do?workGroupList');
				});
			}
		});
		
		//输入0.5秒后才进行查询
		$("#searchUser").on("input propertychange",function(){
			clearTimeout(timer);
			timer=setTimeout(function(){
				var key = encodeURI(encodeURI($("#searchUser").val()));
				ajax('orgGroupController.do?queryUsersByLikeKey&key='+key,function(result){
					allUserUL.empty();
					groupSettingsEdit.initPersonUl(result.obj);
				});
			},500);
	    });

		//修改群名称事件
		//$("#group-name").width($("#group-name").val().length + "em");
		$("#groupname-edit").on("click", function() {
			$(".group-name-edit").toggleClass("editing");
			$("#group-name").attr("disabled", false);//.width(165);
		});
		$("#groupname-confirm").on("click", function() {
			var groupName = $("#group-name").val();
			$(".group-name-edit").toggleClass("editing");
			//console.log($("#group-name").val().length);
			$("#group-name").attr("disabled", true);//.width($("#group-name").val().length + "em");
			ajaxTip("orgGroupController.do?update&name=" + groupName + "&id=" + groupId, function(data) {
				//修改列表中群名称
				$("#workGroupUl li#"+groupId+" span.name").html(groupName);
				//修改IM中群名称
				changeContactLiName(groupFlagMark + groupId, groupName);
			});
		});

		//渲染当前群成员
		$.each(existUserMap.values(), function(i, ele) {
			groupSettingsEdit.addUserToNowUl(ele);
		});
		$("#allGroupUserCounts").html(existUserMap.size())
	})
</script>