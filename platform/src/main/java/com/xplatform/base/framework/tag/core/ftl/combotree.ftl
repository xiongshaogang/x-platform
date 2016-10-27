
<input <#if textname??>textname='${textname}'</#if> <#if datatype??>datatype='${datatype}'</#if> <#if sucmsg??>sucmsg='${sucmsg}'</#if> <#if nullmsg??>nullmsg='${nullmsg}'</#if> <#if errormsg??>errormsg='${errormsg}'</#if> <#if ajaxurl??>ajaxurl='${ajaxurl}'</#if> <#if uniquemsg??>uniquemsg='${uniquemsg}'</#if> <#if entityName??>entityName='${entityName}'</#if> <#if oldValue??>oldValue='${oldValue}'</#if> <#if ignore??>ignore='${ignore}'</#if> 
name='${name}' id='${id}' class='easyui-combotree' data-options='
	<#if data??>
	data:${data},
	</#if>
	<#if url??>
	url:"${url}",
	</#if>
	editable:${editable?string("true","false")},
	multiple:${multiple?string("true","false")},
	<#if disabled??>
	disabled:${disabled?string("true","false")},
	</#if>
	<#if onClick??>
	onClick:function(node){
		var self=this;
		${onClick};
	},
	</#if>
	<#if onChange??>
	onChange:function(newValue, oldValue){
		var self=this;
		${onChange};
	},
	</#if>
	<#if onCheck??>
	onCheck:function(node, checked){
		var self=this;
		${onCheck};
	},
	</#if>
	<#if onSelect??>
	onSelect:function(node){
		var self=this;
		${onSelect};
	},
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
	animate : ${animate?string('true','false')},
	cascadeCheck : ${cascadeCheck?string('true','false')},
	onlyLeafCheck : ${onlyLeafCheck?string('true','false')},
	lines:${lines?string('true','false')},
	<#if onLoadSuccess??>
		onLoadSuccess:function(node, data){
			${onLoadSuccess};
			<#if (value??)&&(value!="")>
			$("#${id}").combotree("tree").tree("expandAll");
			$("#${id}").combotree("setValues","${value}".split(","));
			</#if>
		},
	<#else>
		onLoadSuccess:function(node, data){
			<#if (autoExpand??) && autoExpand>
			$("#${id}").combotree("tree").tree("expandAll");
			</#if>
			<#if (value??)&&(value!="")>
			$("#${id}").combotree("tree").tree("expandAll");
			$("#${id}").combotree("setValues","${value}".split(","));
			</#if>
		},
	</#if>
	
	<#if fit>
		<#if width=="198">
		width:parentWidth("${id}")-2,
		<#else>
		width:${width},
		</#if>
		<#if panelWidth=="200">
		panelWidth:parentWidth("${id}"),
		<#else>
		panelWidth:${panelWidth},
		</#if>
		<#if height=="28">
		height:parentHeight("${id}"),
		<#else>
		height:${height},
		</#if>
	<#else>
		width:${width},
		height:${height},
		panelWidth:${panelWidth},
	</#if>
	<#if panelHeight??>
	panelHeight:${panelHeight}
	<#else>
	panelHeight:"auto"
	</#if>
'/>