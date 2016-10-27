<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	function addRoleUser(){
		var rows = $("#roleUserListSel").datagrid('getSelections');
		for ( var i = 0; i < rows.length; i++) {
			var context = $("#userListSel").html();
			var userId = rows[i].id;
			var userName = rows[i].userName;
		    if(context.indexOf(userId) == -1){
			    context += '<span id="'+ userId +'">'+ userName + '<a href="#" style="float: right;" onclick="removeRoleUser(\''+ userId +'\');">删除</a></span><div style="height: 1px;"></div>';
			    $("#userListSel").html(context);
		    }
		}
	}
	function saveRoleUser(){
		var userIds = "";
		var distributionid = $("#distributionid").val();
		var distributionFlag = $("#distributionFlag").val();
		var len = $("#userListSel span").size();//获取span标签的个数
		var arr = [];
		for(var index = 0; index < len; index++){
			//创建一个数字数组
			arr[index] = index;
		}
		$.each(arr, function(i){//循环得到不同的id的值
			var idValue = $("#userListSel span").eq(i).attr("id");
			if(idValue != ''){
				userIds = userIds + idValue + ","; 
			}
		});
		userIds = userIds.substring(0, userIds.length - 1);
		var url = "roleController.do?saveDistributionUser&userIds=" + userIds + "&distributionid=" + distributionid + "&distributionFlag="+distributionFlag;
		$.ajax({
			url : url,
			type : 'post',
			data : {
				userIds : userIds
			},
			cache : false,
			success : function(data) {
				var d = $.parseJSON(data);
				var msg = d.msg;
				$.messager.show({
					title : '提示信息',
					msg : msg,
					timeout : 1000 * 6
				});
			}
		});
	}
	function removeRoleUser(userId){
		$("#"+userId).remove();
	}
</script>
<div class="easyui-layout" style="width:99%;height:99%;margin:4px auto 0px auto">
  <div region="center" style="padding:1px;border:0px">
  	  <t:datagrid name="roleUserListSel" checkbox="true" fitColumns="true" title="用户列表" actionUrl="userController.do?datagrid" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"></t:dgCol>
	   <t:dgCol title="员工姓名"  field="empName" hidden="true" width="50"  queryMode="single"></t:dgCol>
	   <t:dgCol title="登陆名称"  field="userName" query="true" hidden="true" width="50"  queryMode="single"></t:dgCol>
	   <t:dgCol title="用户编码"  field="code" query="true" hidden="true" width="50" queryMode="single"></t:dgCol>
	   <t:dgCol title="用户类型"  field="userType" replace="员工_employee,会员_member" hidden="true" width="50" queryMode="single"></t:dgCol>
	   <t:dgCol title="电子邮箱"  field="email"  hidden="true" width="80" queryMode="single"></t:dgCol>
	   <t:dgCol title="主岗位"  field="jobName" hidden="true" width="50"  queryMode="single"></t:dgCol>
	   <t:dgCol title="主部门"  field="deptName" hidden="true" width="50"  queryMode="single"></t:dgCol>
	   <t:dgToolBar title="添加" operationCode="organizationManager_addEmpJob_other" icon="awsm-icon-plus" onclick="addRoleUser()"></t:dgToolBar>
	   <t:dgToolBar title="保存" operationCode="organizationManager_saveEmpJob_add" icon="awsm-icon-save" onclick="saveRoleUser()"></t:dgToolBar>
	  </t:datagrid>
  </div>
  <div id="userListSel" title="用户设置" region="east" style="width:170px;" split="true">
   		<c:forEach var="user" items="${userList }">
    		<span id="${user.user.id }">${user.user.userTypeName}<a style="float: right;" href="#" onclick="removeRoleUser('${user.user.id}');"><i class="awsm-icon-remove red"></i></a></span><div style="height: 1px;"></div>
    	</c:forEach>
  </div>
</div>
<input id="distributionid" type="hidden" value="${distributionid }" />
<input id="distributionFlag" type="hidden" value="${distributionFlag }" />