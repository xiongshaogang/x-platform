<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
	#roleDataDirPanell .grid_toolbar_div{
		height:0px;
	}
	#roleDataDirPanel .datagrid .panel-body{
		border:none;
	}
	#roleDataDirPanel .datagrid-view .datagrid-footer{
		height: 0px;
	}
</style>

<div class="easyui-layout" style="width:99%;height:99%;margin:4px auto 0px auto">
   <div region="center" style="padding: 1px;border:0px;">
     <div class="easyui-panel" id="roleDataDirPanel" style="padding: 1px" fit="true" border="true"
        data-options="
			title :'资料目录列表',
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					updateRoleDataDirPrivillage();
				}
			},{
				iconCls : 'icon-reload',
				handler : function() {
					roleDataDirResource.reload();
				}
			},{
				iconCls : 'icon-tip'
			} ]">
       <t:datagrid name="dataDirByRoleTreegrid" checkbox="false" treegrid="true" autoLoadData="true" idField="id" 
                          onLoadSuccess="roleDataDirResource.expandAll" pagination="false" fit="true" fitColumns="true"
                                   actionUrl="typeController.do?getRoleDataDirTreegrid&roleId=${roleId}"  >
          <t:dgCol field="id" treefield="id" hidden="false" title=""></t:dgCol>
          <t:dgCol field="isLeaf" treefield="isLeaf" hidden="false" title=""></t:dgCol>
          <t:dgCol field="name" treefield="text"  hidden="true" title="资料目录"></t:dgCol>
          <t:dgCol field="resourceIds" treefield="resourceIds" hidden="true" title="操作权限"></t:dgCol>
       </t:datagrid>
     </div>
   </div>
</div>
<input type="hidden" name="roleId" value="${roleId }">

<script type="text/javascript">	
	var roleDataDirResource = {
			expandAll: function(){
				$("#dataDirByRoleTreegrid").treegrid("expandAll");
			},
			reload: function(){
				$("#dataDirByRoleTreegrid").treegrid("reload");
			}
	}

   $(function(){
	   
	   $.parser.onComplete=function (){
			$("#dataDirByRoleTreegrid").treegrid("options").onExpand = function (){
				//资源全选
				 $("input:checkbox[id^='selectAll_']").off().click(function(e){
					var dataDir = this.id.split("_")[1];//id="selectAll_dataDir" 全选框id
					if($(this).attr("checked")){
						//id="dataDir_resourceId" 资源框id
						$("input:checkbox[id^='resource_"+dataDir+"']").attr("checked","true");
					}else{
						$("input:checkbox[id^='resource_"+dataDir+"']").removeAttr("checked");
					}
					
				}); 
				
				//资料目录级联选择
				 $("input:checkbox[id^='dataDir_']").off().click(function(e){
					var selectedId = this.id.split("_")[1]; //id="module_{moduleId}" 模块选择框
					var children = $("#dataDirByRoleTreegrid").treegrid("getChildren", selectedId);
					if(children){
						if($(this).attr("checked")){
							for(var i=0;i<children.length;i++){
								$("input:checkbox[id='dataDir_" + children[i].id + "']").attr("checked","true");
							}
						}
					}
				 });
			};
			
			var api=$(".icon-tip").yitip({
				"showEvent":"mouseover",
				"hideEvent":"mouseout",
				"position": "topMiddle",
				"color":"black",
				"content":"勾选资料目录,便拥有看到该目录下文件的权限"
			});
			$.parser.onComplete = mainComplete;
		}
   });

   function updateRoleDataDirPrivillage(){
	   var dataDirs = $("input:checkbox[id^='dataDir_']:checked");
	   var dataDirIds = "";  //资料文件夹权限 
	   var unSelParentIds = ""; //未勾选的父节点
	   var pNode = "";
	   for(var i=0; i<dataDirs.length; i++){
		   var nodeId = dataDirs[i].id.substr(dataDirs[i].id.indexOf("_")+1);
		   dataDirIds += nodeId + ",";
		   pNode = $("#dataDirByRoleTreegrid").treegrid("getParent", nodeId);
		   while(pNode !=null && unSelParentIds.indexOf(pNode.id) == -1 && dataDirIds.indexOf(pNode.id) == -1){
			   unSelParentIds += pNode.id + ",";
			   pNode = $("#dataDirByRoleTreegrid").treegrid("getParent", pNode.id);
		   }
	   }
	   dataDirIds = dataDirIds.substring(0, dataDirIds.lastIndexOf(","));
	   unSelParentIds = unSelParentIds.substring(0, unSelParentIds.lastIndexOf(","));
	   //有勾选资源操作权限的
	   var dataDirResource = $("input:checkbox[id^='resource_']:checked"); //resource_{dataDirId}_{resourceId}
	   var dataDirResourceIds = "";
	   for(var i=0; i<dataDirResource.length; i++){
		   var dataDirId = dataDirResource[i].id.split("_")[1];
		   if(dataDirIds.indexOf(dataDirId) == -1){
			   continue;  //勾选了操作按钮权限，没有勾选资料文件夹权限 。不保存操作按钮权限 ； 删除资料文件夹权限的同时会删除操作按钮权限
		   }
		   dataDirResourceIds += dataDirResource[i].id.substr(dataDirResource[i].id.indexOf("_")+1) + ",";
	   }
	   
	   //没有勾选资源操作权限的
	   var allDataDir = $("input:checkbox[id^='selectAll_']");//id="selectAll_dataDirId" 全选框id
	   for(var i=0; i<allDataDir.length; i++){
		   var dataDirId = allDataDir[i].id.split("_")[1];
		   if($("input:checkbox[id^='resource_"+dataDirId+"']:checked").length==0){
			  //1.模块有资源权限，页面没勾选，删除用户资源权限记录   2模块没有资源权限，用户资源权限表中有记录需删除 . 
			  dataDirResourceIds += dataDirId + "_,";
		   }
	   }
	   dataDirResourceIds = dataDirResourceIds.substring(0, dataDirResourceIds.lastIndexOf(","));
	   
	   $.ajax({
		   url: "typeController.do?updateRoleDataDirAndResource",
		   data: "dataDirIds=" + dataDirIds + "&unSelParentIds=" + unSelParentIds + 
		                            "&dataDirResourceIds=" + dataDirResourceIds+"&roleId=${roleId }",
		   method: "post",
		   cache: "false",
		   success: function(data){
			   tip($.parseJSON(data).msg);
		   }
	   });
   }
</script>

<!-- 
<input type="hidden" name="roleId" value="${roleId}" id="roleId">
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 1px;">
		<div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="typeListPanel"
			data-options="
			title :'资料目录列表',
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					typeSubmit();
				}
			},{
				iconCls : 'icon-tip'
			} ]">
			<a id="selectAllBtn" onclick="selectDataDirByRoleTreeAllBtn();">全选</a> <a id="resetBtn"
				onclick="resetDataDirByRoleTree();">重置</a>
			<t:tree id="dataDirByRoleTree" url="typeController.do?queryRoleFileTypeCheckedTree&roleId=${roleId}"
				cascadeCheck="false" checkbox="true" clickPreFun="treeClick(node)" onLoadSuccess="treeLoadSuccess()"></t:tree>
		</div>
	</div>
	<div region="east" style="width: 150px; overflow: hidden;" split="true">
		<div class="easyui-panel" style="padding: 1px; text-align: center" fit="true" border="false" id="resourceListPanel"
			data-options="
			title :'目录权限列表',
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					resourceSubmit();
				}
			} ]
		"></div>
	</div>
</div>


<script type="text/javascript">
	/* 树点击事件 */
	function treeClick(node) {
		var roleId = $("#roleId").val();
		if (node.checked) {
			$('#resourceListPanel').panel("refresh",
					"roleController.do?resourceSelectByRole&typeId=" + node.id + "&roleId=" + roleId);
		}else{
			tip('您未分配当前目录的权限,无法分配目录下的操作权限,请先保存目录权限')
		}
	}
	/* 树加载事件,用于展开加载所有选中的树节点 */
	function treeLoadSuccess() {
		var node = $("#dataDirByRoleTree").tree("getChecked");
		//如果有选中的则加载选中的项
		if (node[0]) {
			for (var i = 0; i < node.length; i++) {
				$("#dataDirByRoleTree").tree("expandAll", node[i].target);
			}
		} else { //否则加载全部
			$("#dataDirByRoleTree").tree("expandAll");
		}

	}

	function typeSubmit() {
		var roleId = $("#roleId").val();
		var s = GetCheckedNode();
		var checkedTypeIds = s.split("#")[0];
		var pIds = s.split("#")[1];
		doSubmit("typeController.do?updateRoleFileType&checkedTypeIds=" + checkedTypeIds + "&pIds=" + pIds + "&roleId="
				+ roleId);
	}

	/* 获得选中项,及其未选中的父节点 */
	function GetCheckedNode() {
		var node = $('#dataDirByRoleTree').tree('getChecked');
		var cnodes = '';
		var pnodes = '';
		var pnode = null; //保存上一步所选父节点
		for (var i = 0; i < node.length; i++) {
			var nodeId = node[i].id;
			cnodes += nodeId + ',';
			//如果在迭代过的Id中没出现过的话,才去查找父Id(避免重复寻找父Id)
			pnode = $('#dataDirByRoleTree').tree('getParent', node[i].target); //获取当前节点的父节点
			while (pnode != null && cnodes.indexOf(pnode.id) == -1 && pnodes.indexOf(pnode.id) == -1) {//构造出未选中,但是要出现的父节点
				pnodes += pnode.id + ',';
				pnode = $('#dataDirByRoleTree').tree('getParent', pnode.target);
			}
		}
		//去掉最后一个逗号
		if (cnodes.indexOf(",") != -1) {
			cnodes = cnodes.substring(0, cnodes.length - 1);
		}
		if (pnodes.indexOf(",") != -1) {
			pnodes = pnodes.substring(0, pnodes.length - 1);
		}

		return cnodes + "#" + pnodes;
	};

	function selectDataDirByRoleTreeAllBtn() {
		var node = $('#dataDirByRoleTree').tree('getRoots');
		for (var i = 0; i < node.length; i++) {
			$('#dataDirByRoleTree').tree('check', node[i].target);
			var childrenNode = $('#dataDirByRoleTree').tree('getChildren', node[i].target);
			for (var j = 0; j < childrenNode.length; j++) {
				$('#dataDirByRoleTree').tree("check", childrenNode[j].target);
			}
		}
	}
	function resetDataDirByRoleTree() {
		$('#dataDirByRoleTree').tree('reload');
	}

	$('#selectAllBtn').linkbutton({});
	$('#resetBtn').linkbutton({});
	$.parser.onComplete = function() {
		var api = $(".icon-tip").yitip({
			"showEvent" : "mouseover",
			"hideEvent" : "mouseout",
			"position" : "topMiddle",
			"color" : "black",
			"content" : "勾选资料目录,便拥有看到该目录下文件的权限"
		});
	}
</script>
 -->