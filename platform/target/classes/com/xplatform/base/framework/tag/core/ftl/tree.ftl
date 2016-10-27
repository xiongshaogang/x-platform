<script type="text/javascript">
$(function(){
	$('#${id}').tree({   
		<#if url??>
	    url:'${url}',
	    </#if>
	    animate : ${animate?string('true','false')},
		checkbox : ${checkbox?string('true','false')},
		cascadeCheck : ${cascadeCheck?string('true','false')},
		onlyLeafCheck : ${onlyLeafCheck?string('true','false')},
		dnd : ${dnd?string('true','false')},
		lines:${lines?string('true','false')},
		<#if loadFilter??>
		loadFilter:function(data,parent){
			return ${loadFilter};
		},
		</#if>
	    onLoadSuccess:function(node,data){
	    	<#if clickFirstNode>
	    	if(node==null){
	    		var roots=$('#${id}').tree('getRoots');
	    		if(roots!=null&&roots.length>0){
	    			$(roots[0].target).click();
	    		}
		    } 
		    </#if>
		    <#if onLoadSuccess??>
		    	${onLoadSuccess};
		    </#if>
	    },
	    <#if showOptMenu>
	    onContextMenu: function(e, node){
	    	//阻止默认右键行为
			e.preventDefault();
			//将右键的节点作为选中的节点
			var node=$('#${id}').tree('select', node.target);
			$('#${id}_menu').menu('show', {
				left: e.pageX,
				top: e.pageY
			});
		},
		</#if>
		<#if onDblClick??>
		onDblClick:function(node){
			${onDblClick};
		},
		</#if>
		onBeforeLoad:function(node, param){
	    	// 请求前处理
	    	<#if onBeforeLoad??>
	    		return ${onBeforeLoad};
	    	</#if>
	    },
		onClick:function(node){
	    	// 是否只有子节点被点击才触发过滤方法
	    	<#if onlyLeafClick>
	    	var isLeaf=$(this).tree("isLeaf",node.target);
	    	if (isLeaf) {
	    	</#if>
	    	<#if clickPreFun??>
	    		${clickPreFun};
	    	</#if>
	    		<#if gridId??>
				//将页数设置回第一页
				//清空右列表的查询框
				$("#${gridId}tb").find(":input").val("");
				//添加上左树的ID进行过滤
		    	$("#${gridId}").datagrid("load",{${gridTreeFilter}:node.id,loadFlag:true});
		    	</#if>
	    	<#if onlyLeafClick>
	    	}
	    	</#if>
	    }
	});
});
</script>
<ul id="${id}"></ul>
<#if showOptMenu>
<div id="${id}_menu" class="easyui-menu" >
<#list urlList as treeUrl>
	<div onclick="${treeUrl.funname}" data-options="iconCls:'${treeUrl.icon}'">${treeUrl.title}</div>
</#list>
</div>
</#if>