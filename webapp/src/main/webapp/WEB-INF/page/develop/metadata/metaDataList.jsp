<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		redrawEasyUI($(".easyui-layout"));
	});
	function clickTree(node){
		$("#ptypeId").val(node.id);
    	$('#metadata_type_tree').tree('select', node.target);
    	$("#metadataList").datagrid("reload",{typeId:node.id});
    	$("#metadataList").datagrid("getPanel").panel("setTitle",node.text+"列表");
	}
</script>
<div class="easyui-layout" fit="true" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="元数据分类树" style="width:200px;">
    	<t:tree id="metadata_type_tree" gridTreeFilter="typeId"
			url="typeController.do?typeRoleTreeBySysTypeTree&sysType=metadata" gridId="metadataList"
			clickPreFun="clickTree(node)">
		</t:tree>
    </div>  
    <div region="center" style="border:0px">
	    <t:datagrid name="metadataList" autoLoadData="false" title="元数据管理" actionUrl="metaDataController.do?datagrid">
			<t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="表类型" field="jformType" replace="单表_tableType_1,主表_tableType_2,附表_tableType_3" width="110"  query="false"></t:dgCol>
			<t:dgCol title="表名" field="tableName" query="true" width="140" align="left"></t:dgCol>
			<t:dgCol title="表描述" field="content" width="150"></t:dgCol>
			<t:dgCol title="版本" field="jformVersion" width="80"></t:dgCol>
			<t:dgCol title="同步数据库" field="isDbsynch" replace="已同步_Y,未同步_N" style="background:red;_N" query="true"  width="100"></t:dgCol>
			<t:dgCol title="创建人" field="createUserName" hidden="false"></t:dgCol>
			<t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd" width="150" ></t:dgCol>
			<t:dgCol title="修改人" field="updateUserName" hidden="false"></t:dgCol>
			<t:dgCol title="修改时间" field="updateTime" formatter="yyyy-MM-dd" width="150" ></t:dgCol>
			<t:dgCol title="操作" field="opt" width="150"></t:dgCol>
		
			<t:dgOpenOpt title="编辑" url="metaDataController.do?metaDataEdit&id={id}" width="900" height="600" exParams="{optFlag:'update'}"></t:dgOpenOpt>
			<t:dgOpenOpt title="查看" url="metaDataController.do?metaDataEdit&id={id}" icon="glyphicon glyphicon-search icon-color" width="900" height="600" exParams="{optFlag:'detail'}"></t:dgOpenOpt>
			<t:dgDelOpt title="逻辑删除" icon="glyphicon glyphicon-remove icon-color" callback="reflashTree" url="metaDataController.do?rem&id={id}" />
			<t:dgDelOpt title="物理删除" icon="glyphicon glyphicon-remove icon-color" callback="reflashTree" url="metaDataController.do?del&id={id}" />
			
			<t:dgFunOpt title="同步数据库" funname="doDbsynch(id,tableName)" icon="glyphicon glyphicon-repeat icon-color" />
			
			<t:dgToolBar title="新增" icon="glyphicon glyphicon-plus icon-color" width="900" height="600" url="metaDataController.do?metaDataEdit" funname="add"></t:dgToolBar>
			<t:dgToolBar title="生成表单" icon="glyphicon glyphicon-forward icon-color" url="metaDataController.do?trans" funname="addToData" buttonType="GridDetail"></t:dgToolBar>
		</t:datagrid>
    </div>  
</div>
<input type="hidden" id="ptypeId">
<script type="text/javascript">
	//数据库表生成表单
	function addToData(title, url, id, a, b) {
		createwindow(title, url, 400, 500, null, {
			optFlag : 'add'
		});
	}
	/**
	 * 以多种方式同步数据库
	 * @param id 表单id
	 */
	function doDbsynch(id, content) {
		var url = "metaDataController.do?metaDataSynChoice";
		createwindow('同步数据库[' + content + ']', url, 400, 180, null, {
			buttons : [ {
				id : "okBtn",
				text : "确定",
				handler : function() {
					var synchoice = getSynChoice();
					if (synchoice) {
						var submitUrl = 'metaDataController.do?doDbSynch&id=' + id + '&synMethod=' + synchoice;
						doSubmit(submitUrl, 'metadataList');
					} else {
						alert('请选择同步方式');
						return false;
					}
				}
			}, {
				text : "关闭",
				handler : function() {
					closeD(getD($(this)));
				}
			} ],
		});
	}
</script>
