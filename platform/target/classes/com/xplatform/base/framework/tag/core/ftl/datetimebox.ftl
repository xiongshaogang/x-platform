<input <#if datatype??>datatype='${datatype}'</#if> <#if sucmsg??>sucmsg='${sucmsg}'</#if> <#if nullmsg??>nullmsg='${nullmsg}'</#if> <#if errormsg??>errormsg='${errormsg}'</#if> <#if ajaxurl??>ajaxurl='${ajaxurl}'</#if> <#if uniquemsg??>uniquemsg='${uniquemsg}'</#if> <#if entityName??>entityName='${entityName}'</#if> <#if oldValue??>oldValue='${oldValue}'</#if> <#if ignore??>ignore='${ignore}'</#if>
 type="text" name="${name}" id="${id}" class="<#if type=='date'>easyui-datebox<#elseif type=='datetime'>easyui-datetimebox</#if>" data-options='
 	parser:dateParser,
 	formatter:function(date){
    <#if type=='date'>
	    <#if format??>
	    return datetimeFormatters(date,"${format}");
	    <#else>
	    return datetimeFormatters(date,"yyyy-MM-dd");
	    </#if>
    <#elseif type=='datetime'>
	    <#if format??>
	    return datetimeFormatters(date,"${format}");
	    <#else>
	    return datetimeFormatters(date,"yyyy-MM-dd HH:mm:ss");
	    </#if>
    </#if>
    },
	showSeconds:${showSeconds?string('true','false')},
	disabled:${disabled?string('true','false')},
	editable:${editable?string('true','false')},
	<#if value??>
		value:"${value}",
	</#if>
	width:"${width}",
	height:"${height}",
	panelWidth:"${panelWidth}",
	<#if panelHeight??>
	panelHeight:"${panelHeight}"
	</#if>
	<#if onShowPanel??>
	onShowPanel:function(){
		var self=this;
		${onShowPanel};
	},
	</#if>
	<#if onHidePanel??>
	onHidePanel:function(){
		var self=this;
		${onHidePanel};
	},
	</#if>
	<#if onChange??>
	onChange:function(newValue, oldValue){
		var self=this;
		${onChange};
	},
	</#if>
	<#if onSelect??>
	onSelect:function(date){
		var self=this;
		${onSelect};
	},
	</#if>
'/>
