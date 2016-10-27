<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>模块信息</title>
 </head>
 <body>
  <t:formvalid  usePlugin="password" layout="table" action="${entityName?uncap_first}Controller.do?save${entityName}" refresh="true" gridId="${entityName?uncap_first}List">
	<#list columns as po>
		<#if po.editShow == 'N' && fieldMeta[po.fieldName] != cgformConfig.baseFormEntity.connect?upper_case>
		<input id="${po.fieldName}" name="${po.fieldName}" type="hidden" value="${'$'}{${entityName?uncap_first}.${po.fieldName} }">
		</#if>
		<#if po.editShow == 'N' && fieldMeta[po.fieldName] == cgformConfig.baseFormEntity.connect?upper_case>
		<input id="${subG.entityName?lower_case}" name="${subG.entityName?lower_case}.id" type="hidden" value="${'$'}{${entityName?uncap_first}.${subG.entityName?lower_case}.id }">
		</#if>
	</#list>		
	   <table  cellpadding="0" cellspacing="1" class="formtable">
			<#list pageColumns as po>
			<#if (pageColumns?size>10)>
			<#if po_index%2==0>
		 <tr>
			</#if>
			<#else>
		 <tr>
			</#if>
			<td class="td_title"><label class="Validform_label">${po.fieldLabel?if_exists?html}:</label></td>
			<#if po.showtypeDict?if_exists?html=='InputType_str'>
			<td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if> >
				<input id="${po.fieldName}" name="${po.fieldName}" type="text" class="inputxt"  <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else>  </#if>  <#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if>  <#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if><#if po.pageStyle?if_exists?html !=''> style="${po.pageStyle?if_exists?html}" </#if><#if po.isReadonly?if_exists?html =='Y'> readonly="readonly"</#if> <#if po.isUnique?if_exists?html =='Y'> uniquemsg='${po.fieldLabel?if_exists?html}已存在' oldValue="${'$'}{${entityName?uncap_first}.${po.fieldName}}" entityName="${bussiPackage}.${entityPackage}.entity.${entityName}Entity" ajaxurl="commonController.do?checkUnique"</#if> value="${'$'}{${entityName?uncap_first}.${po.fieldName}}" />
			</td>
		    <#elseif po.showtypeDict?if_exists?html=='InputType_text'>
			<td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
				<textarea id="${po.fieldName}" name="${po.fieldName}" class="input_area" <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else>  </#if>  <#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if>  <#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if><#if po.pageStyle?if_exists?html !=''> style="${po.pageStyle?if_exists?html}" </#if><#if po.isReadonly?if_exists?html =='Y'> readonly="readonly"</#if>>${'$'}{${entityName?uncap_first}.${po.fieldName}}</textarea>
			</td>
		    <#elseif po.showtypeDict?if_exists?html=='InputType_select' || po.showtypeDict?if_exists?html=='radio' || po.showtypeDict?if_exists?html=='select' || po.showtypeDict?if_exists?html=='checkbox'>	 
		    <td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
		        <t:comboBox  <#if po.showtypeDict =='checkbox'>multiple="true" <#else> multiple="false" </#if> dictCode="${po.dictCode?if_exists?html}" url="${po.dataUrl?if_exists?html}" name="${po.fieldName}" id="${po.fieldName}" value="${'$'}{${entityName?uncap_first}.${po.fieldName}}"></t:comboBox>
		    </td>
		    <#elseif po.showtypeDict?if_exists?html=='InputType_date' >	 
		    <!-- 日期格式 -->
		    <td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
		    <t:datetimebox name="${po.fieldName}" id="${po.fieldName}" type="datetime" value="${'$'}{${entityName?uncap_first}.${po.fieldName}}"/>
		    </td>
		    <#else>
		    <td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
				<input id="${po.fieldName}" name="${po.fieldName}" type="text" class="inputxt"  <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else>  </#if>  <#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if>  <#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if><#if po.pageStyle?if_exists?html !=''> style="${po.pageStyle?if_exists?html}" </#if><#if po.isReadonly?if_exists?html =='Y'> readonly="readonly"</#if> <#if po.isUnique?if_exists?html =='Y'> uniquemsg='${po.fieldLabel?if_exists?html}已存在' oldValue="${'$'}{${entityName?uncap_first}.${po.fieldName}}" entityName="${bussiPackage}.${entityPackage}.entity.${entityName}Entity" ajaxurl="commonController.do?checkUnique"</#if> value="${'$'}{${entityName?uncap_first}.${po.fieldName}}" />
			</td>
		    </#if>
			<#if (pageColumns?size>10)>
				<#if (po_index%2==0)&&(!po_has_next)>
			<td class="td_title">
				<label class="Validform_label"></label>
			</td>
			<td class="value"></td>
			</#if>
			<#if (po_index%2!=0)||(!po_has_next)>
		</tr>
			</#if>
			<#else>
		</tr>
			</#if>
				</#list>
		</table>
   </t:formvalid>
   <script type="text/javascript">
    $(function(){
		   if(!$("#${subG.entityName?lower_case}").val()){
			   $("#${subG.entityName?lower_case}").val($("#nodeid").val());
		   }
	  });
	  </script>
 </body>
