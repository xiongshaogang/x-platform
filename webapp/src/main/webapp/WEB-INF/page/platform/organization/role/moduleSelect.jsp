<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#role_module_tree').tree({
			checkbox : true,
			url : 'roleController.do?setModuleAuthority&roleId=${roleId}',
			onLoadSuccess : function(node) {
				expandAll();
			},
			onClick : function(node) {
				var isRoot = $('#role_module_tree').tree('getChildren',node.target);
				if (isRoot == '') {
					var roleId = $("#rid").val();
					$('#resourceListpanel').panel(
						"refresh",
						"roleController.do?resourceSelect&moduleId="+ node.id + "&roleId=" + roleId
					);
				}
			}
		});
		$("#moduleListPanel").panel({
			title : "菜单列表",
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					mysubmit();
				}
			} ]
		});
		$("#resourceListpanel").panel({
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
		var roleId = $("#rid").val();
		var s = GetNode();
		doSubmit("roleController.do?updateModuleAuthority&roleModules=" + s
				+ "&roleId=" + roleId);
	}
	function GetNode() {
		var node = $('#role_module_tree').tree('getChecked');
		var cnodes = '';
		var pnodes = '';
		var pnode = null; //保存上一步所选父节点
		for (var i = 0; i < node.length; i++) {
			if ($('#role_module_tree').tree('isLeaf', node[i].target)) {
				cnodes += node[i].id + ',';
				pnode = $('#role_module_tree').tree('getParent', node[i].target); //获取当前节点的父节点
				while (pnode != null) {//添加全部父节点
					pnodes += pnode.id + ',';
					pnode = $('#role_module_tree').tree('getParent', pnode.target);
				}
			}
		}
		cnodes = cnodes.substring(0, cnodes.length - 1);
		pnodes = pnodes.substring(0, pnodes.length - 1);
		return cnodes + "," + pnodes;
	};

	function expandAll() {
		var node = $('#role_module_tree').tree('getSelected');
		if (node) {
			$('#role_module_tree').tree('expandAll', node.target);
		} else {
			$('#role_module_tree').tree('expandAll');
		}
	}
	function selecrAll() {
		var node = $('#role_module_tree').tree('getRoots');
		for (var i = 0; i < node.length; i++) {
			var childrenNode = $('#role_module_tree').tree('getChildren',
					node[i].target);
			for (var j = 0; j < childrenNode.length; j++) {
				$('#role_module_tree').tree("check", childrenNode[j].target);
			}
		}
	}
	function reset() {
		$('#role_module_tree').tree('reload');
	}

	$('#selecrAllBtn').linkbutton({});
	$('#resetBtn').linkbutton({});
</script>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 1px;">
		<div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="moduleListPanel">
			<input type="hidden" name="roleId" value="${roleId}" id="rid">
			<a id="selecrAllBtn" onclick="selecrAll();">全选</a> 
			<a id="resetBtn" onclick="reset();">重置</a>
			<ul id="role_module_tree"></ul>
		</div>
	</div>
	<div region="east" style="width: 150px; overflow: hidden;" split="true">
		<div class="easyui-panel" style="padding: 1px;" fit="true"
			border="false" id="resourceListpanel"></div>
	</div>
</div>
