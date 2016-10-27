<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <script type="text/javascript">
  //编写自定义JS代码
  function setFieldData(){
    <#list subList as sub>
	<#if sub.baseFormEntity.enableEdit?if_exists?html=='Y'>
	  if(!getEditData("${sub.entityName?uncap_first}List")){
		  return false;
	  }
	  $("#insertedRows${sub.entityName}").val(getEditData("${sub.entityName?uncap_first}List").insertedRows);
	  $("#updatedRows${sub.entityName}").val(getEditData("${sub.entityName?uncap_first}List").updatedRows);
	  $("#deletedRows${sub.entityName}").val(getEditData("${sub.entityName?uncap_first}List").deletedRows);
	</#if>
	</#list>
  }
  $(function(){
   $.parser.onComplete = function() {
	 <#list subList as sub>
	 var queryParams = $("#${sub.entityName?uncap_first}List").datagrid('options').queryParams;
	 queryParams["${mainG.entityName?uncap_first}_id"] = '${'$'}{${entityName?uncap_first}.id}';
	 $("#${sub.entityName?uncap_first}List").datagrid("reload");
	 </#list>
     $.parser.onComplete = mainComplete;
   };
  });	
  function reflash(data){
	  $("#${entityName?uncap_first}List").datagrid("reload");
	  //获得列表的查询参数
	  <#list subList as sub>
	  var queryParams=$("#${sub.entityName?uncap_first}List").datagrid('options').queryParams;
	  queryParams["${mainG.entityName?uncap_first}_id"] = data.obj;
	  $("#id").val(data.obj);
	  $("#${sub.entityName?uncap_first}List").datagrid("reload");
	  </#list>
  }
  
  <#list subList as sub>
  function reflash${sub.entityName}(){
	  var queryParams = $("#${sub.entityName?uncap_first}List").datagrid('options').queryParams;
	  queryParams["${mainG.entityName?uncap_first}_id"] = $("#id").val();
	  $("#${sub.entityName?uncap_first}List").datagrid("reload");
  }
  </#list>
  function checkMain(){
	 if($("#id").val() == ""){
		 tip('请先保存主表信息');
		 return false;
	 }else{
		 return true;
	 } 
  }
  
  <#list subList as sub>
  <#if sub.baseFormEntity.enableEdit?if_exists?html!='Y'>
  function add${sub.entityName}(title, addurl, gridID, width, height, preinstallWidth, exParams) {
	    if(!checkMain()){
	    	return ;
	    }
		var defaultOptions = {
			optFlag : 'add'
		};
		addurl += '&optFlag=add&${entityName?uncap_first}Id='+$("#id").val();
		var options = $.extend({}, defaultOptions, exParams);
		createwindow(title, addurl, width, height, preinstallWidth, options);
  }
  </#if>
  </#list>
  </script>
 <div class="easyui-layout" fit="true">
<div region="center" style="padding: 1px;width:100%;height:100%;border:0px;overflow: hidden;">
  <t:formvalid formid="${entityName?uncap_first}Formobj"  action="${entityName?uncap_first}Controller.do?save${entityName}"  gridId="${entityName?uncap_first}List" callback="reflash"  refresh="false" afterSaveClose="false" beforeSubmit="setFieldData">
	<#list subList as sub>
	<#if sub.baseFormEntity.enableEdit?if_exists?html=='Y'>
	<input type="hidden" id="insertedRows${sub.entityName}" name="insertedRows${sub.entityName}" value=""/>
	<input type="hidden" id="deletedRows${sub.entityName}" name="deletedRows${sub.entityName}" value=""/>
	<input type="hidden" id="updatedRows${sub.entityName}" name="updatedRows${sub.entityName}" value=""/>
	</#if>
	</#list>
	<#list columns as po>
		<#if po.editShow == 'N'>
		<input id="${po.fieldName}" name="${po.fieldName}" type="hidden" value="${'$'}{${entityName?uncap_first}.${po.fieldName} }">
		</#if>
	</#list>		
	   <table  cellpadding="0" cellspacing="1" class="formtable">
			<#list pageColumns as po>
			<#if po_index%2==0>
		 <tr>
			</#if>
			<td class="td_title"><label class="Validform_label">${po.fieldLabel?if_exists?html}:</label></td>
			<#if po.showtypeDict?if_exists?html=='InputType_str'>
			<td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if> >
				<input id="${po.fieldName}" name="${po.fieldName}" type="text" class="inputxt"  <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else> </#if><#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if><#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if><#if po.pageStyle?if_exists?html !=''> style="${po.pageStyle?if_exists?html}" </#if><#if po.isReadonly?if_exists?html =='Y'> readonly="readonly"</#if> value="${'$'}{${entityName?uncap_first}.${po.fieldName}}"   <#if po.isUnique?if_exists?html =='Y'>uniquemsg='${po.fieldLabel?if_exists?html}已存在' oldValue="${'$'}{${entityName?uncap_first}.${po.fieldName}}" entityName="${bussiPackage}.${entityPackage}.entity.${entityName}Entity" ajaxurl="commonController.do?checkUnique"</#if>/>
			</td>
		    <#elseif po.showtypeDict?if_exists?html=='InputType_text'>
			<td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
				<textarea id="${po.fieldName}" name="${po.fieldName}" class="input_area" <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else> </#if><#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if><#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if><#if po.pageStyle?if_exists?html !=''> style="${po.pageStyle?if_exists?html}" </#if><#if po.isReadonly?if_exists?html =='Y'> readonly="readonly"</#if>>${'$'}{${entityName?uncap_first}.${po.fieldName}}</textarea>
			</td>
		    <#elseif po.showtypeDict?if_exists?html=='InputType_select' || po.showtypeDict?if_exists?html=='radio' || po.showtypeDict?if_exists?html=='select' || po.showtypeDict?if_exists?html=='checkbox'>	 
		    <td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
		        <t:comboBox  <#if po.showtypeDict =='checkbox'>multiple="true" <#else> multiple="false" </#if> dictCode="${po.dictCode?if_exists?html}" url="${po.dataUrl?if_exists?html}" name="${po.fieldName}" id="${po.fieldName}" value="${'$'}{${entityName?uncap_first}.${po.fieldName}}" <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else> </#if><#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if><#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if>  <#if po.isUnique?if_exists?html =='Y'>uniquemsg='${po.fieldLabel?if_exists?html}已存在' oldValue="${'$'}{${entityName?uncap_first}.${po.fieldName}}" entityName="${bussiPackage}.${entityPackage}.entity.${entityName}Entity" ajaxurl="commonController.do?checkUnique"</#if>></t:comboBox>
		    </td>
		    <#elseif po.showtypeDict?if_exists?html=='InputType_date' >	 
		    <!-- 日期格式 -->
		    <td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
		    <t:datetimebox name="${po.fieldName}" id="${po.fieldName}" type="datetime" value="${'$'}{${entityName?uncap_first}.${po.fieldName}}" <#if po.isReadonly?if_exists?html =='Y'> disabled="true"</#if> <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else> </#if><#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if><#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if> />
		    </td>
		    <#else>
		    <td class="value" <#if po.pageRowspan?if_exists?html != ''>colspan="${po.pageRowspan}"</#if>>
				<input id="${po.fieldName}" name="${po.fieldName}" type="text" class="inputxt"  <#if po.isNullable?if_exists?html == 'N' && po.reCheckRule?if_exists?html != ''> datatype="${po.reCheckRule?if_exists?html} ${po.andOrRule?if_exists?html} ${po.formRuleEntity?if_exists?html},*" <#elseif po.isNullable?if_exists?html == 'N'>datatype="*" <#else> </#if><#if po.errorMsg?if_exists?html != '' > errormsg="${po.errorMsg?if_exists?html}" </#if><#if po.nullMsg?if_exists?html != '' > nullMsg="${po.nullMsg?if_exists?html}" </#if><#if po.pageStyle?if_exists?html !=''> style="${po.pageStyle?if_exists?html}" </#if><#if po.isReadonly?if_exists?html =='Y'> readonly="readonly"</#if> value="${'$'}{${entityName?uncap_first}.${po.fieldName}}" <#if po.isUnique?if_exists?html =='Y'>uniquemsg='${po.fieldLabel?if_exists?html}已存在' oldValue="${'$'}{${entityName?uncap_first}.${po.fieldName}}" entityName="${bussiPackage}.${entityPackage}.entity.${entityName}Entity" ajaxurl="commonController.do?checkUnique"</#if>/>
			</td>
		    </#if>
			<#if (po_index%2==0)&&(!po_has_next)>
			<td class="td_title">
				<label class="Validform_label"></label>
			</td>
			<td class="value"></td>
			</#if>
			<#if (po_index%2!=0)||(!po_has_next)>
		</tr>
			</#if>
			</#list>
	  </table>
   </t:formvalid>
   </div>
   <div region="south" style="padding: 1px;width:100%;height:300px;padding:4px 4px 0px 4px;border:0px;overflow: hidden;">
   <#if subNum?if_exists == 1>
     <#list subList as sub>
        <t:datagrid name="${sub.entityName?uncap_first}List"  <#if sub.baseFormEntity.selectType?if_exists?html=='Y'> checkbox="true"</#if> fitColumns="true" title="${ftl_description}" actionUrl="${sub.entityName?uncap_first}Controller.do?datagrid" idField="id" fit="true" <#if sub.baseFormEntity.queryType?if_exists?html=='queryType_single'> queryMode="single" <#else> queryMode="group" </#if>  <#if sub.baseFormEntity.isPagenation?if_exists?html=='N'> pagination="false" <#else> pagination="true" </#if>  <#if sub.baseFormEntity.enableEdit?if_exists?html=='Y'>editable="true" defaultOpt="true"</#if>>
	   <#list sub.columns as po>
	   <t:dgCol title="${po.fieldLabel}"  field="${po.fieldName}" <#if po.listShow?if_exists?html =='N'>hidden="false"<#else>hidden="true"</#if> <#if po.queryShow =='Y'>query="true"</#if> width="120" <#if po.gridStyle?if_exists?html !=''>style="${po.gridStyle?if_exists?html}"</#if><#if po.alignDict?if_exists?html !=''> align="${po.alignDict?if_exists?html}" </#if><#if po.isOrder?if_exists?html =='N'> sortable="false" </#if><#if po.sQueryMode?if_exists?html =='queryType_single'> queryMode="single" </#if><#if po.sQueryMode?if_exists?html =='queryType_group'> queryMode="group" </#if><#if po.sQueryInputType?if_exists?html !=''> queryInputType="${po.sQueryInputType}" </#if><#if po.sDictCode?if_exists?html !=''> dictCode="${po.sDictCode}" </#if><#if po.sData?if_exists?html !=''> data="${po.sData}" </#if><#if po.extend?if_exists?html !=''> extend="${po.extend}" </#if>  <#if sub.baseFormEntity.enableEdit?if_exists?html=='Y'><#if po.showtypeDict?if_exists?html=='InputType_date'> editType="datetimebox"</#if> <#if po.showtypeDict?if_exists?html=='InputType_select'> editType="combobox"</#if> <#if po.showtypeDict?if_exists?html=='InputType_text'> editType="textarea"</#if><#if po.isNullable?if_exists?html == 'N'>editorParams='{required:true}'</#if></#if> <#if po.fieldType=='Date'>formatter="yyyy-MM-dd HH:mm:ss"</#if>    <#if po.sDictCode?if_exists?html =='' && po.dictCode?if_exists?html !=''> dictCode="${po.dictCode}" </#if>></t:dgCol>
	   </#list>
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	   <t:dgDelOpt title="删除" url="${sub.entityName?uncap_first}Controller.do?delete&id={id}" callback="reflash${sub.entityName}"/>
	   <#if sub.baseFormEntity.enableEdit?if_exists?html!='Y'>
	   <t:dgToolBar title="录入" icon="awsm-icon-plus" url="${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit" funname="add" buttonType="GridAdd" width="400" height="400" exParams="{formId:'${sub.entityName?uncap_first}Formobj'}" onclick="add${sub.entityName}('录入','${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit','${sub.entityName?uncap_first}List',400,400,null,{formId:'${sub.entityName?uncap_first}Formobj'})"></t:dgToolBar>
	   <t:dgToolBar title="编辑" icon="awsm-icon-edit" url="${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit" funname="update" buttonType="GridUpdate" width="400" height="400" exParams="{formId:'${sub.entityName?uncap_first}Formobj'}"></t:dgToolBar>
	   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="${sub.entityName?uncap_first}Controller.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash${sub.entityName}"></t:dgToolBar>
	   </#if>
	   <t:dgToolBar title="查看" icon="awsm-icon-search" url="${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit" funname="detail" buttonType="GridDetail" width="400" height="400" exParams="{formId:'${sub.entityName?uncap_first}Formobj'}"></t:dgToolBar>
	  </t:datagrid>
     </#list>
    <#else>
    <t:tabs id="onetomany"  border="true" hBorderBottom="false" hBorderLeft="false" hBorderRight="false" hBorderTop="false" leftDiv="true" leftDivWidth="70" leftDivTitle="子表" rightDiv="true" >
	<#list subList as sub>
	<div title="${sub.entityName}" style="overflow: hidden;" id="${sub.entityName?uncap_first}">
	   <t:datagrid name="${sub.entityName?uncap_first}List"  <#if sub.baseFormEntity.selectType?if_exists?html=='Y'> checkbox="true"</#if> fitColumns="true" title="${ftl_description}" actionUrl="${sub.entityName?uncap_first}Controller.do?datagrid" idField="id" fit="true" <#if sub.baseFormEntity.queryType?if_exists?html=='queryType_single'> queryMode="single" <#else> queryMode="group" </#if>  <#if sub.baseFormEntity.isPagenation?if_exists?html=='N'> pagination="false" <#else> pagination="true" </#if>  <#if sub.baseFormEntity.enableEdit?if_exists?html=='Y'>editable="true" defaultOpt="true"</#if>>
	   <#list sub.columns as po>
	   <t:dgCol title="${po.fieldLabel}"  field="${po.fieldName}" <#if po.listShow?if_exists?html =='N'>hidden="false"<#else>hidden="true"</#if> <#if po.queryShow =='Y'>query="true"</#if> width="120" <#if po.gridStyle?if_exists?html !=''>style="${po.gridStyle?if_exists?html}"</#if><#if po.alignDict?if_exists?html !=''> align="${po.alignDict?if_exists?html}" </#if><#if po.isOrder?if_exists?html =='N'> sortable="false" </#if><#if po.sQueryMode?if_exists?html =='queryType_single'> queryMode="single" </#if><#if po.sQueryMode?if_exists?html =='queryType_group'> queryMode="group" </#if><#if po.sQueryInputType?if_exists?html !=''> queryInputType="${po.sQueryInputType}" </#if><#if po.sDictCode?if_exists?html !=''> dictCode="${po.sDictCode}" </#if><#if po.sData?if_exists?html !=''> data="${po.sData}" </#if><#if po.extend?if_exists?html !=''> extend="${po.extend}" </#if>  <#if sub.baseFormEntity.enableEdit?if_exists?html=='Y'><#if po.showtypeDict?if_exists?html=='InputType_date'> editType="datetimebox"</#if> <#if po.showtypeDict?if_exists?html=='InputType_select'> editType="combobox"</#if> <#if po.showtypeDict?if_exists?html=='InputType_text'> editType="textarea"</#if><#if po.isNullable?if_exists?html == 'N'>editorParams='{required:true}'</#if></#if> <#if po.fieldType=='Date'>formatter="yyyy-MM-dd HH:mm:ss"</#if>    <#if po.sDictCode?if_exists?html =='' && po.dictCode?if_exists?html !=''> dictCode="${po.dictCode}" </#if>></t:dgCol>
	   </#list>
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	   <t:dgDelOpt title="删除" url="${sub.entityName?uncap_first}Controller.do?delete&id={id}" callback="reflash${sub.entityName}"/>
	   <#if sub.baseFormEntity.enableEdit?if_exists?html!='Y'>
	   <t:dgToolBar title="录入" icon="awsm-icon-plus" url="${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit" funname="add" buttonType="GridAdd" width="400" height="400" exParams="{formId:'${sub.entityName?uncap_first}Formobj'}" onclick="add${sub.entityName}('录入','${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit','${sub.entityName?uncap_first}List',400,400,null,{formId:'${sub.entityName?uncap_first}Formobj'})"></t:dgToolBar>
	   <t:dgToolBar title="编辑" icon="awsm-icon-edit" url="${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit" funname="update" buttonType="GridUpdate" width="400" height="400" exParams="{formId:'${sub.entityName?uncap_first}Formobj'}"></t:dgToolBar>
	   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="${sub.entityName?uncap_first}Controller.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash${sub.entityName}"></t:dgToolBar>
	   </#if>
	   <t:dgToolBar title="查看" icon="awsm-icon-search" url="${sub.entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}Edit" funname="detail" buttonType="GridDetail" width="400" height="400" exParams="{formId:'${sub.entityName?uncap_first}Formobj'}"></t:dgToolBar>
	  </t:datagrid>
	</div>
	</#list>
    </t:tabs>
   </#if>
   </div>
   </div>
