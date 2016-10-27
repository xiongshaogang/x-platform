<!-- 角色分配机构界面 -->
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div style="width: 99%; height: 99%;margin:4px auto">
	<div class="easyui-panel" fit="true" id="roleUserPanel">
	</div>
</div>
<input id="roleId" type="hidden" value="${roleId }" />
<input id="userIds" type="hidden" />
<script type="text/javascript">
	$(function() {
		ajax("roleController.do?queryUsersByRole&roleId=${roleId}",function(data){
			if(data.success){
				$("#userIds").val(data.obj);
				var url = "userController.do?userSelect&userIds=" + $("#userIds").val() + "&needBtnSave=" + true + "&afterSaveClose=" + false + "&multiples="
				+ true + "&treeUrl=" + "orgnaizationController.do?orgSelectTagTree" + "&gridUrl=" + "userController.do?datagrid4OrgMulSelectTag" + "&orgCode=" + "rootOrg" + "&callback=" + null + "&saveUrl="
				+ encodeURIComponent("roleController.do?saveUserRole&roleId=${roleId }");
				setTimeout(function(){
					$("#roleUserPanel").panel("open").panel("refresh",url);
				},0);
			}
		});
		
// 		$("#roleUserPanel").panel({
// 			title : '组织机构',
// 			tools : [ {
// 				iconCls : 'icon-save',
// 				handler : function() {
// 					roleOrgList.saveRoleOrg();
// 				}
// 			}, {
// 				iconCls : 'icon-reload',
// 				handler : function() {
// 					roleOrgList.reload();
// 				}
// 			}, {
// 				iconCls : 'icon-tip'
// 			} ]
// 		});

	});

	var roleOrgList = {
		expandAll : function() {
			var node = $('#roleOrgTreeSel').tree('getSelected');
			if (node) {
				$('#roleOrgTreeSel').tree('expandAll', node.target);
			} else {
				$('#roleOrgTreeSel').tree('expandAll');
			}
		},

		reload : function() {
			$("#roleOrgTreeSel").tree("reload");
		},

		saveRoleOrg : function() {
			var nodes = $('#roleOrgTreeSel').tree('getChecked');
			var orgIds = "";
			for (var i = 0; i < nodes.length; i++) {
				orgIds = orgIds + nodes[i].id + ",";
			}
			var roleId = $("#roleId").val();
			$.ajax({
				url : "orgnaizationController.do?saveRoleOrg&orgIds=" + orgIds + "&roleId=" + roleId,
				type : 'post',
				cache : false,
				success : function(data) {
					tip($.parseJSON(data).msg);
				}
			});
		}
	}
</script>