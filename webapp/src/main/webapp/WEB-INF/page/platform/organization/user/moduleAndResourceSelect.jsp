<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
	#userModuleAndResourcePanel .grid_toolbar_div{
		height:0px;
	}
	#userModuleAndResourcePanel .datagrid .panel-body{
		border:none;
	}
</style>
<div class="easyui-layout" style="width:99%;height:99%;margin:4px auto 0px auto">
   <div region="center" style="border:0px;">
      <div class="easyui-panel" style="padding: 1px;" fit="true" border="true" id="userModuleAndResourcePanel"
			data-options="
			title :'模块列表',
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					saveModuleAndResource();
				}
			},{
				iconCls : 'icon-reload',
				handler : function() {
					userModuleAndResource.reload();
				}
			},{
				iconCls : 'icon-tip'
			} ]">
			
		  <t:datagrid name="moduleAndResourceSelect" checkbox="false" autoLoadData="true" pagination="false" fit="true"
		            idField="id" fitColumns="true" actionUrl="userController.do?getUserModuleTreegrid&userId=${userId }" 
		             onLoadSuccess="userModuleAndResource.loadSucess(row,data)"  queryMode="group" treegrid="true" >
	         <t:dgCol field="id" title="" treefield="id" hidden="false"></t:dgCol> 
	         <t:dgCol field="isLeaf" title="" treefield="isLeaf" hidden="false"></t:dgCol>
	         <t:dgCol field="name" title="模块名称" treefield="text" hidden="true"></t:dgCol>
	         <t:dgCol field="resourceIds" title="资源权限" treefield="resourceIds" hidden="true"></t:dgCol>
	      </t:datagrid>
	  </div>
   </div> 
</div>
<input type="hidden" name="userId" value="${userId }">
<script type="text/javascript">
	var userModuleAndResource = {
			expandAll: function(){
				$("#moduleAndResourceSelect").treegrid("expandAll");
			},
			reload: function(){
				$("#moduleAndResourceSelect").treegrid("reload");
			},
			loadSucess: function(row,data){
				for(var i=0; i<data.length; i++){
			    	var id = "module_" + data[i].id;
			    	var checked=$("#"+id).attr("checked");
			    	if(checked=="checked"){
						$("#moduleAndResourceSelect").treegrid("expandAll", data[i].id);
					}
			    }  
			}
	}
	$(document).ready(function(){		
		$.parser.onComplete=function (){
			$("#moduleAndResourceSelect").treegrid("options").onExpand = function (){
				$("input:checkbox[id^='selectAll_']").off().click(function(e){
					var moduleId = this.id.split("_")[1];//id="selectAll_moduleId" 全选框id
					if($(this).attr("checked")){
						$("input:checkbox[id^='resource_"+moduleId+"']").attr("checked","true");//id="moduleId_resourceId" 资源框id
					}else{
						$("input:checkbox[id^='resource_"+moduleId+"']").removeAttr("checked");
					}
					
				});
				
				//模块级联选择
				$("input:checkbox[id^='module_']").off().click(function(e){
					var selectedId = this.id.split("_")[1]; //id="module_{moduleId}" 模块选择框
					var children = $("#moduleAndResourceSelect").treegrid("getChildren", selectedId);
					if(children){
						if($(this).attr("checked")){
							for(var i=0;i<children.length;i++){
								$("input:checkbox[id='module_" + children[i].id + "']").attr("checked","true");
							}
						}else{
							for(var i=0;i<children.length;i++){
								$("input:checkbox[id='module_" + children[i].id + "']").removeAttr("checked");
							}
						}
					}
					
					var parent = $("#moduleAndResourceSelect").treegrid("getParent", selectedId);
					while(parent){
						var node = $("input:checkbox[id='module_" + parent.id + "']");
						//勾选的节点为选中，父节点未选中，选中父节点。
						if($(this).attr("checked") && !node.attr("checked")){
							node.attr("checked","true");
						}
						parent = $("#moduleAndResourceSelect").treegrid("getParent", parent.id);
					}
				});
			};
			
			$(".icon-tip").yitip({
				"showEvent":"mouseover",
				"hideEvent":"mouseout",
				"position": "topMiddle",
				"color":"black",
				"content":"设置模块及资源权限，左边为模块权限，右边为资源权限"
			});
			$.parser.onComplete = mainComplete;
		}
		
	});
	
	function saveModuleAndResource(){
		//模块权限
		var modules = $("input:checkbox[id^='module_']:checked");//id="module_{moduleId}"  模块id
		var moduleIds = "";
		for(var i=0; i<modules.length; i++){
			moduleIds += modules[i].id.substr(modules[i].id.indexOf("_")+1) + ",";
		}
		moduleIds = moduleIds.substring(0, moduleIds.lastIndexOf(","));
		
		//资源权限：模块有资源权限，且有勾选资源权限
		var moduleResourceIds = "";
		var selectedResource = $("input:checkbox[id^='resource_']:checked");//id="resource_{moduleId}_{resourceId}" 资源id
		for(var i=0; i<selectedResource.length; i++){
			var moduleId = selectedResource[i].id.split("_")[1];
		    if(moduleIds.indexOf(moduleId) == -1){
			    continue;  //勾选了操作按钮权限，没有勾选资料文件夹权限 。不保存操作按钮权限 ； 删除模块权限的同时会删除操作按钮权限
		    }
			//截取模块资源id  {moduleId}_{resourceId}
			moduleResourceIds += selectedResource[i].id.substr(selectedResource[i].id.indexOf("_")+1) + ",";
		}
		//资源权限：没有勾选资源权限 或没有资源权限
		var allModule = $("input:checkbox[id^='selectAll_']"); //id="selectAll_moduleId" 全选框id
		for(var i=0; i<allModule.length; i++){
			var moduleId = allModule[i].id.split("_")[1];
			if($("input:checkbox[id^='resource_"+moduleId+"']:checked").length==0){
				//1.模块有资源权限，页面没勾选，删除用户资源权限记录   2模块没有资源权限，用户资源权限表中有记录需删除 . 
				moduleResourceIds += moduleId + "_,";
			}
		}
		moduleResourceIds = moduleResourceIds.substring(0, moduleResourceIds.lastIndexOf(","));
		
		$.ajax({
			url: "userController.do?updateUserModuleAndResource",
			method: "post",
			cache: "fasle",
			data: "moduleIds=" + moduleIds + "&moduleResourceIds=" + moduleResourceIds +"&userId=${userId}",
			success: function(data){
				tip($.parseJSON(data).msg);
			}
		});
	}
	
</script>

