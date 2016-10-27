<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="模块树" style="width:200px;">
    <t:tree id="${entityName?uncap_first}_tree" gridTreeFilter="parent_id"
			url="${entityName?uncap_first}Controller.do?tree" gridId="${entityName?uncap_first}List"
			clickPreFun="clickTree(node)">
	</t:tree>
    </div>  
  <div region="center"  style="padding:0px;border:0px;">
  <t:datagrid name="${entityName?uncap_first}List"  autoLoadData="false" <#if cgformConfig.baseFormEntity.selectType?if_exists?html=='Y'> checkbox="true"</#if> fitColumns="false" title="${ftl_description}" actionUrl="${entityName?uncap_first}Controller.do?datagrid" idField="id" fit="true" <#if cgformConfig.baseFormEntity.queryType?if_exists?html=='queryType_single'> queryMode="single" <#else> queryMode="group" </#if>  <#if cgformConfig.baseFormEntity.isPagenation?if_exists?html=='N'> pagination="false" <#else> pagination="true" </#if> <#if cgformConfig.baseFormEntity.enableEdit?if_exists?html=='Y'>editable="true"</#if>>
  <#list columns as po>
  <#if po.fieldName !='parentId' || po.fieldName !='parent_id'>
   <t:dgCol title="${po.fieldLabel}"  field="${po.fieldName}" <#if po.listShow?if_exists?html =='N'>hidden="false"<#else>hidden="true"</#if> <#if po.queryShow =='Y'>query="true"</#if> width="120" <#if po.gridStyle?if_exists?html !=''>style="${po.gridStyle?if_exists?html}"</#if><#if po.alignDict?if_exists?html !=''> align="${po.alignDict?if_exists?html}" </#if><#if po.isOrder?if_exists?html =='N'> sortable="false" </#if><#if po.sQueryMode?if_exists?html =='queryType_single'> queryMode="single" </#if><#if po.sQueryMode?if_exists?html =='queryType_group'> queryMode="group" </#if><#if po.sQueryInputType?if_exists?html !=''> queryInputType="${po.sQueryInputType}" </#if><#if po.sDictCode?if_exists?html !=''> dictCode="${po.sDictCode}" </#if><#if po.sData?if_exists?html !=''> data="${po.sData}" </#if><#if po.extend?if_exists?html !=''> extend="${po.extend}" </#if> ></t:dgCol>
  </#if>
  </#list>
  <t:dgCol title="操作" width="180" field="opt"></t:dgCol>
   <t:dgOpenOpt preinstallWidth="1" height="450" icon="awsm-icon-edit blue" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit&id={id}" exParams="{optFlag:'add',formId:'${entityName?uncap_first}Formobj'}" title="编辑"></t:dgOpenOpt>
   <t:dgOpenOpt preinstallWidth="1" height="450" icon="awsm-icon-zoom-in green" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit&id={id}&optFlag=detail" exParams="{optFlag:'detail',formId:'${entityName?uncap_first}Formobj'}" title="查看"></t:dgOpenOpt>
   <t:dgDelOpt callback="reflashTree" title="删除" icon="awsm-icon-trash red" url="${entityName?uncap_first}Controller.do?delete&id={id}" />
   <t:dgToolBar preinstallWidth="1" height="450" title="新增"  icon="awsm-icon-plus" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit" funname="add" buttonType="GridAdd" exParams="{formId:'${entityName?uncap_first}Formobj'}"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <input type="hidden" id="nodeid">
<script type="text/javascript">
	function reflashTree(){
		var node = $('#${entityName?uncap_first}_tree').tree('getSelected');
		if(node==null){
			$('#${entityName?uncap_first}_tree').tree('reload');
		}else{
		    var parent = $('#${entityName?uncap_first}_tree').tree('getParent',node.target);
		    if(parent == null){
				$('#${entityName?uncap_first}_tree').tree('reload',node.target);
			}else{
				$('#${entityName?uncap_first}_tree').tree('reload',parent.target);
			}
		}
	}
	function clickTree(node){
		$("#nodeid").val(node.id);
    	$('#${entityName?uncap_first}_tree').tree('select', node.target);
	}
</script>