<!-- 角色分配界面 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	function addUserRole(){
		var rows = $("#roleDistributionList").datagrid('getSelections');
		for ( var i = 0; i < rows.length; i++) {
			var context = $("#userRoleContext").html();
			var roleId = rows[i].id;
			var roleName = rows[i].name;
		    if(context.indexOf(roleId) == -1){
			    context += '<span id="'+ roleId +'">'+ roleName + '<a href="#" style="float: right;" onclick="removeUserRole(\''+ roleId +'\');">删除</a></span><div style="height: 1px;"></div>';
			    $("#userRoleContext").html(context);
		    }
		}
	}
	
	function saveUserRole(){
		var roleIds = "";
		var distributionid = $("#distributionid").val();
		var distributionFlag = $("#distributionFlag").val();
		var len = $("#userRoleContext span").size();//获取span标签的个数
		var arr = [];
		for(var index = 0; index < len; index++){
			//创建一个数字数组
			arr[index] = index;
		}
		$.each(arr, function(i){//循环得到不同的id的值
			var idValue = $("#userRoleContext span").eq(i).attr("id");
			if(idValue != ''){
				roleIds = roleIds + idValue + ","; 
			}
		});
		roleIds = roleIds.substring(0, roleIds.length - 1);
		var url = "roleController.do?saveDistributionRole&roleIds=" + roleIds + "&distributionid=" + distributionid + "&distributionFlag=" + distributionFlag;
		$.ajax({
			url : url,
			type : 'post',
			data : {
				roleIds : roleIds
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
	
	function removeUserRole(roleId){
		$("#"+roleId).remove();
	}
</script>
<div class="easyui-layout" style="width:99%;height:99%;margin:4px auto 0px auto">
  <div region="center" style="padding:1px;border:0px">
	  <t:datagrid name="roleDistributionList" checkbox="true" fitColumns="true" title="角色列表" actionUrl="roleController.do?datagrid" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="角色名称"  field="name" query="true" hidden="true" width="80"  queryMode="single"></t:dgCol>
	   <t:dgCol title="角色编码"  field="code" query="true" hidden="true" width="80" queryMode="single"></t:dgCol>
	   <t:dgCol title="允许删除"  field="allowDelete"  hidden="true" replace="是_Y,否_N" queryMode="single"></t:dgCol>
	   <t:dgCol title="允许编辑"  field="allowEdit"  hidden="true" replace="是_Y,否_N" queryMode="single"></t:dgCol>
	   <t:dgToolBar title="添加" operationCode="organizationManager_addDistributionRole_other" icon="awsm-icon-plus" onclick="addUserRole()"></t:dgToolBar>
	   <t:dgToolBar title="保存" operationCode="organizationManager_saveDistributionRole_add" icon="awsm-icon-save" onclick="saveUserRole()"></t:dgToolBar>
	  </t:datagrid>
  </div>
  <div id="userRoleContext" title="权限设置" region="east" style="width:170px;" split="true">
  		<c:forEach var="role" items="${roleList }">
    		<span id="${role.role.id }">${role.role.name}
    			<a style="float: right;" href="#" onclick="removeUserRole('${role.role.id}');"><i class="awsm-icon-remove red"></i></a>
    		</span>
    		<div style="height: 1px;"></div>
    	</c:forEach>
  </div>
</div>
<input id=distributionFlag type="hidden" value="${distributionFlag }" />
<input id=distributionid type="hidden" value="${distributionid }" />
