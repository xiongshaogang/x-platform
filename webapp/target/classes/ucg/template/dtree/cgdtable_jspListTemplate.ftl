<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	function clickTree(node){
		$("#nodeid").val(node.id);
    	$('#${entityName?uncap_first}_tree').tree('select', node.target);
    	$("#${entityName?uncap_first}List").datagrid("reload",{${cgformConfig.baseFormEntity.connect}:node.id});
    	$("#${entityName?uncap_first}List").datagrid("getPanel").panel("setTitle",node.text+"列表");
	}
	function reflashTree(){
		$("#${entityName?uncap_first}_tree").tree('reload');
	}
</script>

<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="模块树" style="width:200px;">
    <t:tree id="${entityName?uncap_first}_tree" <#if subG.baseFormEntity.enableEdit?if_exists?html=='Y'> showOptMenu="true"</#if> gridTreeFilter="${cgformConfig.baseFormEntity.connect}"
			url="${subG.entityName?uncap_first}Controller.do?tree" gridId="${entityName?uncap_first}List" <#if subG.baseFormEntity.selectType?if_exists?html=='Y'> checkbox="true"</#if>
			clickPreFun="clickTree(node)">
			<t:treeOpt funname="appendNode()" icon="awsm-icon-plus blue" title="新增" />
			<t:treeOpt funname="removeNode()" icon="awsm-icon-trash red" title="删除" />
			<t:treeOpt funname="updateNode()" icon="awsm-icon-edit blue" title="修改" />
			<t:treeOpt funname="view()" icon="awsm-icon-zoom-in green" title="查看" />
	</t:tree>
    </div>  
  <div region="center"  style="border:0px">
  <t:datagrid name="${entityName?uncap_first}List"  autoLoadData="false" <#if cgformConfig.baseFormEntity.selectType?if_exists?html=='Y'> checkbox="true"</#if> fitColumns="false" title="${ftl_description}" actionUrl="${entityName?uncap_first}Controller.do?datagrid" idField="id" fit="true" <#if cgformConfig.baseFormEntity.queryType?if_exists?html=='queryType_single'> queryMode="single" <#else> queryMode="group" </#if>  <#if cgformConfig.baseFormEntity.isPagenation?if_exists?html=='N'> pagination="false" <#else> pagination="true" </#if>>
  <#list columns as po>
  <#if fieldMeta[po.fieldName] != cgformConfig.baseFormEntity.connect?upper_case>
   <t:dgCol title="${po.fieldLabel}"  field="${po.fieldName}" <#if po.listShow?if_exists?html =='N'>hidden="false"<#else>hidden="true"</#if> <#if po.queryShow =='Y'>query="true"</#if> width="120" <#if po.gridStyle?if_exists?html !=''>style="${po.gridStyle?if_exists?html}"</#if><#if po.alignDict?if_exists?html !=''> align="${po.alignDict?if_exists?html}" </#if><#if po.isOrder?if_exists?html =='N'> sortable="false" </#if><#if po.sQueryMode?if_exists?html =='queryType_single'> queryMode="single" </#if><#if po.sQueryMode?if_exists?html =='queryType_group'> queryMode="group" </#if><#if po.sQueryInputType?if_exists?html !=''> queryInputType="${po.sQueryInputType}" </#if><#if po.sDictCode?if_exists?html !=''> dictCode="${po.sDictCode}" </#if><#if po.sData?if_exists?html !=''> data="${po.sData}" </#if><#if po.extend?if_exists?html !=''> extend="${po.extend}" </#if> ></t:dgCol>
  </#if>
  </#list>
   <t:dgCol title="操作" width="180" field="opt"></t:dgCol>
   <t:dgOpenOpt preinstallWidth="1" icon="awsm-icon-edit blue" height="450" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit&id={id}" exParams="{optFlag:'add'}" title="编辑"></t:dgOpenOpt>
   <t:dgOpenOpt preinstallWidth="1" icon="awsm-icon-zoom-in green" height="450" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit&id={id}&optCode=detail" exParams="{optFlag:'detail'}" title="查看"></t:dgOpenOpt>
   <t:dgDelOpt  title="删除" url="${entityName?uncap_first}Controller.do?delete&id={id}" icon="awsm-icon-trash red" callback="refreshTable"/>
   <t:dgToolBar preinstallWidth="1" height="450" title="新增"  icon="awsm-icon-plus" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit" funname="add" buttonType="GridAdd"></t:dgToolBar>
   <t:dgToolBar title="删除" icon="awsm-icon-remove" callback="refreshTable" url="${entityName?uncap_first}Controller.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <input type="hidden" id="nodeid">
<script type="text/javascript">
     $(function(){
	 redrawEasyUI($(".easyui-layout"));
     });
     function refreshTable() {
		$("#${entityName?uncap_first}List").datagrid('reload');
	}
	function appendNode() {
		var node = $('#${entityName?uncap_first}_tree').tree('getSelected');
		$("#nodeid").val(node.id);
		createwindow("新增机构", "${subG.entityName?uncap_first}Controller.do?${subG.entityName?uncap_first}Edit", 400, 500, null, {
			optFlag : 'add'
		});
	}

	function removeNode() {
		var node = $('#${entityName?uncap_first}_tree').tree('getSelected');
		$.messager.confirm("提示信息", "确定删除所有所选数据?", function(r) {
			if (r) {
				$.ajax({
					url : '${subG.entityName?uncap_first}Controller.do?delete',
					type : 'post',
					data : {
						ids : node.id
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							$.messager.show({
								title : '提示信息',
								msg : msg,
								timeout : 1000 * 6
							});
							$("#${entityName?uncap_first}_tree").tree('reload');
						}
					}
				});
			}
		});
	}

	function updateNode() {
		var node = $('#${entityName?uncap_first}_tree').tree('getSelected');
		parentId = node.id;
		parentName = node.text;
		$("#parentId").val(parentId);
		$("#parentName").val(parentName);
		createwindow("修改机构", "${subG.entityName?uncap_first}Controller.do?${subG.entityName?uncap_first}Edit&id=" + node.id, 400,
				500, null, {
					optFlag : 'update'
				});
	}

	function view() {
		var node = $('#${entityName?uncap_first}_tree').tree('getSelected');
		parentId = node.id;
		parentName = node.text;
		$("#parentId").val(parentId);
		$("#parentName").val(parentName);
		createwindow("查看", "${subG.entityName?uncap_first}Controller.do?${subG.entityName?uncap_first}Edit&id=" + node.id, 400,
				500, null, {
					optFlag : 'detail'
				});
	}

</script>

