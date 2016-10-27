<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center"  style="padding:0px;border:0px;">
  <t:datagrid name="${entityName?uncap_first}List"  <#if cgformConfig.baseFormEntity.selectType?if_exists?html=='Y'> checkbox="true"</#if> fitColumns="true" title="${ftl_description}" actionUrl="${entityName?uncap_first}Controller.do?datagrid" idField="id" fit="true" <#if cgformConfig.baseFormEntity.queryType?if_exists?html=='queryType_single'> queryMode="single" <#else> queryMode="group" </#if>  <#if cgformConfig.baseFormEntity.isPagenation?if_exists?html=='N'> pagination="false" <#else> pagination="true" </#if>  <#if cgformConfig.baseFormEntity.enableEdit?if_exists?html=='Y'>editable="true"</#if>>
  <#list columns as po>
   <t:dgCol title="${po.fieldLabel}"  field="${po.fieldName}" <#if po.listShow?if_exists?html =='N'>hidden="false"<#else>hidden="true"</#if> <#if po.queryShow =='Y'>query="true"</#if> width="120" <#if po.gridStyle?if_exists?html !=''>style="${po.gridStyle?if_exists?html}"</#if><#if po.alignDict?if_exists?html !=''> align="${po.alignDict?if_exists?html}" </#if><#if po.isOrder?if_exists?html =='N'> sortable="false" </#if><#if po.sQueryMode?if_exists?html =='queryType_single'> queryMode="single" </#if><#if po.sQueryMode?if_exists?html =='queryType_group'> queryMode="group" </#if><#if po.sQueryInputType?if_exists?html !=''> queryInputType="${po.sQueryInputType}" </#if><#if po.sDictCode?if_exists?html !=''> dictCode="${po.sDictCode}" </#if><#if po.sData?if_exists?html !=''> data="${po.sData}" </#if><#if po.extend?if_exists?html !=''> extend="${po.extend}" </#if> ></t:dgCol>
  </#list>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="${entityName?uncap_first}Controller.do?delete&id={id}" callback="reflash"/>
   <t:dgToolBar title="录入" icon="awsm-icon-plus" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit" funname="add" buttonType="GridAdd" width="400" height="400" exParams="{formId:'${entityName?uncap_first}Formobj'}"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="awsm-icon-edit" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit" funname="update" buttonType="GridUpdate" width="400" height="400" exParams="{formId:'${entityName?uncap_first}Formobj'}"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="${entityName?uncap_first}Controller.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="awsm-icon-search" url="${entityName?uncap_first}Controller.do?${entityName?uncap_first}Edit" funname="detail" buttonType="GridDetail" width="400" height="400" exParams="{formId:'${entityName?uncap_first}Formobj'}"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 function reflash(){
	  $("#${entityName?uncap_first}List").datagrid("reload");
  }
 </script>