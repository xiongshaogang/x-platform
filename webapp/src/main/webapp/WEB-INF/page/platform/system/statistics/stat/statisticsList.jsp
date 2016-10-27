<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="统计树" style="width:200px;">
    <t:tree id="statistics_tree"   gridTreeFilter="type_id"
			url="typeController.do?typeRoleTreeBySysTypeTree&sysType=statistics" gridId="statisticsList"
			clickPreFun="clickTree(node)">
	</t:tree>
    </div>  
  <div region="center"  style="border:0px">
  <t:datagrid name="statisticsList"  autoLoadData="false"  checkbox="true" fitColumns="true" title="统计列表" actionUrl="statisticsController.do?datagrid" idField="id" fit="true"  queryMode="group"    pagination="true" >
   <t:dgCol title="主键"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="数据源类型"  field="type" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="统计名称"  field="name" hidden="true" query="true" width="120"  ></t:dgCol>
   <t:dgCol title="统计code"  field="code" hidden="true" query="true" width="120"  ></t:dgCol>
   <t:dgCol title="数据源类型"  field="datasourceId" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="默认显示图标类型"  field="showType" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="权限类型"  field="authorityType" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="操作" width="180" field="opt"></t:dgCol>
   <t:dgOpenOpt preinstallWidth="1" height="400" icon="awsm-icon-edit blue" url="statisticsController.do?statisticsEdit&id={id}" exParams="{optFlag:'add'}" title="编辑" operationCode="statisticsManager_statisticsEdit_edit"></t:dgOpenOpt>
   <t:dgOpenOpt preinstallWidth="1" height="400" icon="awsm-icon-zoom-in green" url="statisticsController.do?statisticsEdit&id={id}&optCode=detail" exParams="{optFlag:'detail'}" title="查看" operationCode="statisticsManager_statisticsView_view"></t:dgOpenOpt>
   <t:dgDelOpt  title="删除" url="statisticsController.do?delete&id={id}" icon="awsm-icon-trash red" callback="refreshTable" operationCode="statisticsManager_statisticsDelete_delete"/>
   <t:dgOpenOpt exp="type#eq#sql" width="1000" height="500" icon="awsm-icon-cogs green" url="fieldController.do?field1&datasourceId={datasourceId }" exParams="{optFlag:'add'}" title="字段配置" operationCode="statisticsManager_statisticsField_other"></t:dgOpenOpt> 
   <t:dgToolBar preinstallWidth="1" height="400" title="新增"  icon="awsm-icon-plus" url="statisticsController.do?statisticsEdit" funname="addStatistics" buttonType="GridAdd" operationCode="statisticsManager_statisticsAdd_add"></t:dgToolBar>
   <t:dgToolBar title="批量删除" callback="refreshTable" icon="awsm-icon-remove" url="statisticsController.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" operationCode="statisticsManager_statisticsBatchDelete_batchDelete"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <input type="hidden" id="nodeid">
 <script type="text/javascript">
  $(function(){
	 redrawEasyUI($(".easyui-layout"));
     });
	function clickTree(node){
		$("#nodeid").val(node.id);
		/* 
    	$('#statistics_tree').tree('select', node.target);
    	$("#statisticsList").datagrid("reload",{type_id:node.id});
    	$("#statisticsList").datagrid("getPanel").panel("setTitle",node.text); */
	}
	function reflashTree(){
		$("#statistics_tree").tree('reload');
	}
	function refreshTable() {
		$("#statisticsList").datagrid('reload');
	}
	/**
	 * 添加事件打开窗口
	 * @param title 编辑框标题
	 * @param addurl//目标页面地址
	 */
	function addStatistics(title, addurl, gridID, width, height, preinstallWidth, exParams) {
		if($("#nodeid").val() == ''){
			tip('请选择新增统计的分类');
		}else{
		var defaultOptions = {
			optFlag : 'add'
		};
		var options = $.extend({}, defaultOptions, exParams);
		createwindow(title, addurl, width, height, preinstallWidth, options);
		}
	}
</script>
