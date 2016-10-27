<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<
<script type="text/javascript">
	var userMap = new Map();

	<c:forEach var="user" items="${users}">
	var user = {
		id : '${user.id}',
		name : '${user.name}',
		portrait : '${user.portrait}',
		sortKey : '${user.sortKey}',
		searchKey : '${user.searchKey}'
	};
	userMap.put(user.id, user);
	</c:forEach>

	$(function() {
		//渲染可选择用户
		$.each(userMap.values(), function(i, ele) {
			addUserToAllArea(ele);
		});
	});

	var addUserToAllArea = function(user) {

	}
</script>

<div class="modal fade" id="addPersonModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
								<li id="zhangsan1">
									<div class="avatar">
										<img src="basic/img/avatars/Sergey-Azovskiy.jpg">
									</div>
									<div class="name">
										<p>张三1</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i> <i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="zhangsan2">
									<div class="avatar">
										<img src="basic/img/avatars/Sergey-Azovskiy.jpg">
									</div>
									<div class="name">
										<p>张三2</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i> <i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="zhangsan3">
									<div class="avatar">
										<img src="basic/img/avatars/Sergey-Azovskiy.jpg">
									</div>
									<div class="name">
										<p>张三3</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i> <i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="pull-right selected-box">
					<div class="top">
						<span>新添成员：</span> <span class="pull-right">已选<span>2</span>人
						</span>
					</div>
					<div class="person-box">
						<div class="selected-item"></div>
						<div class="person-list selected">
							<ul class="list-style-none">
								<li id="zhangsan4">
									<div class="avatar">
										<img src="basic/img/avatars/Sergey-Azovskiy.jpg">
									</div>
									<div class="name">
										<p>张三4</p>
									</div>
									<div class="status remove">
										<i class="glyphicon glyphicon-ok"></i> <i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="zhangsan5">
									<div class="avatar">
										<img src="basic/img/avatars/Sergey-Azovskiy.jpg">
									</div>
									<div class="name">
										<p>张三5</p>
									</div>
									<div class="status remove">
										<i class="glyphicon glyphicon-ok"></i> <i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="zhangsan6">
									<div class="avatar">
										<img src="basic/img/avatars/Sergey-Azovskiy.jpg">
									</div>
									<div class="name">
										<p>张三6</p>
									</div>
									<div class="status remove">
										<i class="glyphicon glyphicon-ok"></i> <i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-orange" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>