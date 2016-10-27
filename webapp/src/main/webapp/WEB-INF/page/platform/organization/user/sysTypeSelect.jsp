<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" style="width:99%;height:99%;margin:4px auto 0px auto">
	<input id="selecrAllFlag" type="hidden" value="false">
	<div region="center" style="padding: 1px;border: 0px;">
		<div class="easyui-panel" style="padding: 1px;" fit="true" border="true" id="sysTypeListPanel">
			<input type="hidden" name="userId" value="${userId}" id="rid">
			<a id="selecrAllBtn" onclick="selecrAll();">全选</a> 
			<a id="resetBtn" onclick="reset();">重置</a>
			<t:tree onLoadSuccess="sysTypeSel.expandAll()" id="sysType_tree" checkbox="true" showOptMenu="false" 
			                               url="typeController.do?queryUserTypeTree&userId=${userId}"></t:tree>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$("#sysTypeListPanel").panel({
			title : "菜单列表",
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					mysubmit();
				}
			} ]
		});
	});
	function mysubmit() {
		var userId = $("#rid").val();
		var nodeStr = getNodes().split("#");
		var checkNodes = nodeStr[0];
		var pNodes = nodeStr[1];
		doSubmit("typeController.do?updateUserType&checkedNodes=" + checkNodes + "&pNodes=" + pNodes + "&userId=" + userId);
	}
	function getNodes() {
		var nodes = $("#sysType_tree").tree("getChecked");
		var cNodes = "";
		var pNodes = "";
		var pNode = "";
		for(var i=0; i<nodes.length; i++){
			var nodeId = nodes[i].id;
			cNodes += nodeId + ",",
			pNode = $("#sysType_tree").tree("getParent", nodes[i].target);
			while(pNode != null && pNodes.indexOf(pNode.id) == -1 && cNodes.indexOf(pNode.id) == -1){
				pNodes += pNode.id + ",";
				pNode = $("#sysType_tree").tree("getParent", pNode.target);
			}
		}
		cNodes = cNodes.substring(0, cNodes.lastIndexOf(","));
		pNodes = pNodes.substring(0, pNodes.lastIndexOf(","));
		return cNodes + "#" + pNodes;
		/* var node = $('#sysType_tree').tree('getChecked');
		var typeIds = "";
		if(node.length == 1 && $('#sysType_tree').tree('getParent', node[0].target) == null){
			typeIds = typeIds + node[0].id + "|1" + ",";
		}else{
			for(var i = 0; i < node.length; i++){
				
				var pnode = $('#sysType_tree').tree('getParent', node[i].target);
				if(typeIds.indexOf(node[i].id) == -1){
					if(pnode != null){
						var pnodeSel = $('#sysType_tree').tree('getSelected', node[i]);
						if(pnodeSel != null){
							if(typeIds.indexOf(pnode.id) == -1){
								typeIds = typeIds + pnode.id + "|1" + ",";
							}
						}else{
							if(typeIds.indexOf(pnode.id) == -1){
								typeIds = typeIds + pnode.id + "|0" + ",";
							}
						}
					}
					typeIds = typeIds + node[i].id + "|1" + ",";
					var obj={};
					obj.typeIds=typeIds;
					findPid(node[i], obj);
					typeIds = obj.typeIds;
				}
			}
		}
		typeIds = typeIds.substring(0, typeIds.length - 1);
		return typeIds; */
	};
	
	/* function findPid(node, obj){
		var pnode = $('#sysType_tree').tree('getParent', node.target);
		var pnode1 = $('#sysType_tree').tree('getParent', pnode.target);
		var typeIds=obj.typeIds;
		if(pnode1 != null){
			if(typeIds.indexOf(pnode.id) == -1){
				var pnodeSel = $('#sysType_tree').tree('getSelected', pnode);
				if(pnodeSel != null){
					obj.typeIds = obj.typeIds + pnode.id + "|1" + ",";
				}else{
					obj.typeIds = obj.typeIds + pnode.id + "|0" + ",";
				}
			}
			findPid(pnode, obj);
		}else{
			if(typeIds.indexOf(pnode.id) == -1){
				obj.typeIds = obj.typeIds + pnode.id + "|0" + ",";
			}
		}
	} */


	function selecrAll() {
		var selecrAllFlag = $("#selecrAllFlag").val();
		if(selecrAllFlag == 'false'){
			var node = $('#sysType_tree').tree('getRoots');
			for (var i = 0; i < node.length; i++) {
				var childrenNode = $('#sysType_tree').tree('getChildren', node[i].target);
				$('#sysType_tree').tree("check", node[i].target);
				for (var j = 0; j < childrenNode.length; j++) {
					$('#sysType_tree').tree("check", childrenNode[j].target);
				}
			}
			$("#selecrAllFlag").val("true");
		}else{
			var node = $('#sysType_tree').tree('getRoots');
			for (var i = 0; i < node.length; i++) {
				var childrenNode = $('#sysType_tree').tree('getChildren', node[i].target);
				$('#sysType_tree').tree("uncheck", node[i].target);
				for (var j = 0; j < childrenNode.length; j++) {
					$('#sysType_tree').tree("uncheck", childrenNode[j].target);
				}
			}
			$("#selecrAllFlag").val("false");
		}
	}
	function reset() {
		$('#sysType_tree').tree('reload');
	}

	$('#selecrAllBtn').linkbutton({});
	$('#resetBtn').linkbutton({});
	
	var sysTypeSel = {
			expandAll: function () {
				var node = $('#sysType_tree').tree('getSelected');
				if (node) {
					$('#sysType_tree').tree('expandAll', node.target);
				} else {
					$('#sysType_tree').tree('expandAll');
				}
			}
	}
</script>