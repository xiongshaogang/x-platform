<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="easyui-layout" style="width: 100%; height: 100%; border: 0px">
	<div region="west" split="true" title="代码生成类型" style="width: 200px;">
		<t:tree id="modelTypeTree" gridTreeFilter="typeID" onlyLeafClick="true" url="generateTypeController.do?getAsynTree" gridId="formTypeEntityList"
			clickPreFun="clickTree(node)">
		</t:tree>
	</div>

	<div region="center" style="border: 0px">
		<input type="hidden" id="typeID" name="typeID" />
		<t:datagrid name="formTypeEntityList" autoLoadData="false" checkbox="true" fitColumns="true" title="代码生成器列表" actionUrl="generateController.do?datagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="表单模板名称" field="name" query="true"></t:dgCol>
			<t:dgCol title="表单Code" field="code" width="120" query="true"></t:dgCol>
			<t:dgCol title="所属模型类型" field="typeName" width="120"></t:dgCol>
			<t:dgCol title="所属模型类型ID" field="typeID" hidden="false" width="120"></t:dgCol>
			<t:dgCol title="创建人" field="createUserName" width="120"></t:dgCol>
			<t:dgCol title="描述" field="description" hidden="true" width="100"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="200"></t:dgCol>


			<t:dgFunOpt title="模板实体配置" icon="glyphicon glyphicon-cog icon-color" funname="addBaseFormEntity(id,typeID)"
				operationCode="templateManager_formentityAdd_other" />
			<t:dgOpenOpt title="页面字段配置" icon="glyphicon glyphicon-align-justify icon-color" url="generateFieldController.do?goFieldList&formTypeID={id}" width="1000"
				height="500" />
			<t:dgFunOpt title="代码生成" icon="glyphicon glyphicon-forward icon-color" funname="generate(id,typeID)" operationCode="templateManager_generateCode_other" />

			<t:dgToolBar title="新增" icon="glyphicon glyphicon-plus icon-color" width="450" height="380" buttonType="GridAdd" url="generateController.do?goAddOrUpdate"
				onclick="addWithTreeID()" operationCode="templateManager_formtypeAdd_add" />
			<t:dgToolBar title="编辑" icon="glyphicon glyphicon-pencil icon-color" width="450" height="380" buttonType="GridUpdate"
				url="generateController.do?goAddOrUpdate" funname="update" operationCode="templateManager_formtypeEdit_edit" />
			<t:dgToolBar title="批量删除" icon="glyphicon glyphicon-remove icon-color" url="generateController.do?doBatchDel" funname="deleteALLSelect"
				operationCode="templateManager_formtypeBatchDel_batchDel" />
			<t:dgToolBar title="查看" icon="glyphicon glyphicon-search icon-color" width="460" height="410" buttonType="GridDetail"
				url="generateController.do?goAddOrUpdate" funname="detail" operationCode="templateManager_formtypeView_view" />
		</t:datagrid>
	</div>

</div>

<script type="text/javascript">
	$(document).ready(function() {
		redrawEasyUI($("#page_content"));
	});

	function clickTree(node) {
		//如果是子节点才进行过滤
		if (node.state != null) {
			//获得右列表的查询参数
			queryParams = $("#formTypeEntityList").datagrid('options').queryParams;
			//添加上左树的ID进行过滤
			queryParams.typeID = node.id;
			$("#typeID").val(node.id);
			$("#formTypeEntityListtb").find(":input").val("");
			$('#formTypeEntityList').datagrid('reload');
		}
	}

	function addWithTreeID() {
		var url = 'generateController.do?goAddOrUpdate';
		var typeID = $("#typeID").val();
		if ($("#typeID").val()) {
			url += '&typeID=' + typeID;
			createwindow('新增', url, 460, 410, 1, {
				optFlag : 'add'
			});
		} else {
			tip('您还未选择左侧的模型类型');
		}

	}
	function addBaseFormEntity(type_id, model_id) {
		createwindow("模板表单实体配置", "generateController.do?deployEntity&model_id=" + model_id + "&type_id=" + type_id, 900, 500, null, {
			optFlag : 'add'
		});
	}
	// type_id 具体模板名称ID  model_id 模型类型ID
	function generate(type_id, model_id) {
		$.messager.confirm("确认信息", "确定生成代码？", function(flag) {
			if (flag) {
				$.ajax({
					url : "newGenerateController.do?dogenerate",
					type : "post",
					dataType : "json",
					data : {
						"type_id" : type_id,
						"model_id" : model_id
					},
					success : function(data) {
						if (data.success) {
							$.messager.show({
								title : "提示信息",
								msg : data.msg,
								timeout : 3000
							});
						}
					},
					error : function(data) {
						if (data.success) {
							$.messager.show({
								title : "提示信息",
								msg : data.msg,
								timeout : 3000
							});
						}
					}
				});
			}
		});

	}
</script>