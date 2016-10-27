<input <#if datatype??>datatype='${datatype}'</#if> <#if sucmsg??>sucmsg='${sucmsg}'</#if> <#if nullmsg??>nullmsg='${nullmsg}'</#if> <#if errormsg??>errormsg='${errormsg}'</#if> <#if ajaxurl??>ajaxurl='${ajaxurl}'</#if> <#if uniquemsg??>uniquemsg='${uniquemsg}'</#if> <#if entityName??>entityName='${entityName}'</#if> <#if oldValue??>oldValue='${oldValue}'</#if> <#if ignore??>ignore='${ignore}'</#if> 
type="text" name="${name}" id="${id}" class="inputxt <#if spinner>easyui-numberspinner<#else>easyui-numberbox</#if>"
<#if onfocus??>onfocus="${onfocus}"</#if> <#if onblur??>onblur="${onblur}"</#if> <#if onclick??>onclick="${onclick}"</#if>
<#if extend??>${extend}</#if>
data-options='
	<#if min??>
	min:${min},
	</#if>
	<#if max??>
	max:${max},
	</#if>
	<#if precision??>
	precision:${precision},
	</#if>
	<#if groupSeparator??>
	groupSeparator:"${groupSeparator}",
	</#if>
	<#if decimalSeparator??>
	decimalSeparator:"${decimalSeparator}",
	</#if>
	<#if suffix??>
	suffix:"${suffix}",
	</#if>
	<#if prefix??>
	prefix:"${prefix}",
	</#if>
	editable:${editable?string('true','false')},
	height:${height},
	width:${width},
	<#if value??>
	value:"${value}",
	</#if>
	<#if onChange??>
	onChange:function(newValue,oldValue){
		var self=this;
		${onChange};
	},
	</#if>
	disabled:${disabled?string('true','false')}
'/>