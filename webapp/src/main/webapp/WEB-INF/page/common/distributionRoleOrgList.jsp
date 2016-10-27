<!-- 角色分配机构界面 -->
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" style="width:99%;height:99%;margin:4px auto 0px auto">
  <div region="center" style="border:0px">
        <div class="easyui-panel" style="padding: 1px;" fit="true" border="true" id="roleOrgTreeSelPanel">
		   <t:tree id="roleOrgTreeSel" checkbox="true" cascadeCheck="false" showOptMenu="false" 
		                         url="orgnaizationController.do?roleOrgstree&roleId=${roleId }"></t:tree>
  	   </div>
  </div>
</div>
<input id="roleId" type="hidden" value="${roleId }" />
<script type="text/javascript">
	$(function() {
		$("#roleOrgTreeSel").tree("options").onLoadSuccess = function(node,data){
			roleOrgList.expandAll();
			if(node){
				nodes=$("#roleOrgTreeSel").tree("getChildren",node.target);
			}else{
				nodes=$("#roleOrgTreeSel").tree("getRoots");
			}
			$.each(nodes,function(i,e){
				if(e.attributes['available'] == 0){
				    disabledTreeNode($(e.target));
				}
			});
			$(".icon-tip").yitip({
				"showEvent":"mouseover",
				"hideEvent":"mouseout",
				"position": "topMiddle",
				"color":"black",
				"content":"设置机构权限，勾选机构，机构下所有人都拥有该角色."
			});
		};
		
 		$("#roleOrgTreeSelPanel").panel({
 			title :'组织机构',
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					roleOrgList.saveRoleOrg();
				}
			},{
				iconCls : 'icon-reload',
				handler : function() {
					roleOrgList.reload();
				}
			},{
				iconCls : 'icon-tip'
			} ]
		}); 
 		
	});
	
	var roleOrgList = {
   		expandAll: function() {
   			var node = $('#roleOrgTreeSel').tree('getSelected');
   			if (node) {
   				$('#roleOrgTreeSel').tree('expandAll', node.target);
   			} else {
   				$('#roleOrgTreeSel').tree('expandAll');
   			}
   		},
   		 	
   		reload: function(){
   			$("#roleOrgTreeSel").tree("reload");
   		},
   		
   		saveRoleOrg: function(){
   			var nodes = $('#roleOrgTreeSel').tree('getChecked');
   			var orgIds = "";
   			for(var i = 0; i < nodes.length; i++){
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