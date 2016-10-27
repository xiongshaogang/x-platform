<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	function clickTree(node){
		$("#${cgformConfig.baseFormEntity.connect}").val(node.id);
    	$('#${entityName?uncap_first}_tree').tree('select', node.target);
    	$("#${entityName?uncap_first}List").datagrid("reload",{${cgformConfig.baseFormEntity.connect}:node.id});
    	$("#${entityName?uncap_first}List").datagrid("getPanel").panel("setTitle",node.text);
	}
	function reflashTree(){
		$("#${entityName?uncap_first}_tree").tree('reload');
	}
</script>

<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="模块树" style="width:200px;">
    <t:tree id="${entityName?uncap_first}_tree"   gridTreeFilter="${cgformConfig.baseFormEntity.connect}"
			url="${cEntityName?uncap_first}Controller.do?tree" gridId="${entityName?uncap_first}List"
			clickPreFun="clickTree(node)">
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
   <t:dgDelOpt callback="reflashTree" title="删除" url="${entityName?uncap_first}Controller.do?delete&id={id}" />
   <t:dgToolBar preinstallWidth="1" height="450" title="新增"  icon="awsm-icon-plus" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit" funname="add"></t:dgToolBar>
   <t:dgToolBar title="删除" callback="reflashTree" icon="awsm-icon-remove" url="${entityName?uncap_first}Controller.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <input type="hidden" id="${cgformConfig.baseFormEntity.connect}">
