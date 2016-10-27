<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#user_module_tree').tree({
			checkbox : true,
			url : 'userController.do?setModuleAuthority&userId=${userId}',
			onLoadSuccess : function(node) {
				expandAll();
			},
			onClick : function(node) {
				var checkNodes = $('#user_module_tree').tree('getChecked');
				for(var i = 0; i < checkNodes.length; i++){
					//if(checkNodes[i].id == node.id){
						var isRoot = $('#user_module_tree').tree('getChildren',node.target);
						if (isRoot == '') {
							var userId = $("#rid").val();
							$('#user_resourceListpanel').panel(
								"refresh",
								"userController.do?resourceSelect&moduleId="+ node.id + "&userId=" + userId
							);
						}
					//}else{
					//	$('#resourceListpanel').panel("refresh","userController.do?resourceSelect&moduleId="+ "aaaaa" + "&userId=" + "cc111skjo");
					//}
				}
			}
		});
		$("#user_moduleListPanel").panel({
			title : "菜单列表",
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					mysubmit();
				}
			} ]
		});
		$("#user_resourceListpanel").panel({
			title : "操作按钮列表",
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					submitresource();
				}
			} ]
		});
	});
	function mysubmit() {
		var userId = $("#rid").val();
		var s = GetNode();
		doSubmit("userController.do?updateModuleAuthority&userModuleIds=" + s
				+ "&userId=" + userId);
	}
	function GetNode() {
		var node = $('#user_module_tree').tree('getChecked');
		var cnodes = '';
		var pnodes = '';
		var pnode = null; //保存上一步所选父节点
		for (var i = 0; i < node.length; i++) {
			if ($('#user_module_tree').tree('isLeaf', node[i].target)) {
				cnodes += node[i].id + ',';
				pnode = $('#user_module_tree').tree('getParent', node[i].target); //获取当前节点的父节点
				while (pnode != null) {//添加全部父节点
					pnodes += pnode.id + ',';
					pnode = $('#user_module_tree').tree('getParent', pnode.target);
				}
			}
		}
		cnodes = cnodes.substring(0, cnodes.length - 1);
		pnodes = pnodes.substring(0, pnodes.length - 1);
		return cnodes + "," + pnodes;
	};

	function expandAll() {
		var node = $('#user_module_tree').tree('getSelected');
		if (node) {
			$('#user_module_tree').tree('expandAll', node.target);
		} else {
			$('#user_module_tree').tree('expandAll');
		}
	}
	function selecrAll() {
		var node = $('#user_module_tree').tree('getRoots');
		for (var i = 0; i < node.length; i++) {
			var childrenNode = $('#user_module_tree').tree('getChildren',
					node[i].target);
			for (var j = 0; j < childrenNode.length; j++) {
				$('#user_module_tree').tree("check", childrenNode[j].target);
			}
		}
	}
	function reset() {
		$('#user_module_tree').tree('reload');
	}

	$('#user_selecrAllBtn').linkbutton({});
	$('#user_resetBtn').linkbutton({});
</script>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 1px;">
		<div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="user_moduleListPanel">
			<input type="hidden" name="userId" value="${userId}" id="rid">
			<a id="user_selecrAllBtn" onclick="selecrAll();">全选</a> 
			<a id="user_resetBtn" onclick="reset();">重置</a>
			<ul id="user_module_tree"></ul>
		</div>
	</div>
	<div region="east" style="width: 150px; overflow: hidden;" split="true">
		<div class="easyui-panel" style="padding: 1px;" fit="true"
			border="false" id="user_resourceListpanel"></div>
	</div>
</div>
