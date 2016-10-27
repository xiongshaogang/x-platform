<input <#if textname??>textname='${textname}'</#if> <#if datatype??>datatype='${datatype}'</#if> <#if sucmsg??>sucmsg='${sucmsg}'</#if> <#if nullmsg??>nullmsg='${nullmsg}'</#if> <#if errormsg??>errormsg='${errormsg}'</#if> <#if ajaxurl??>ajaxurl='${ajaxurl}'</#if> <#if uniquemsg??>uniquemsg='${uniquemsg}'</#if> <#if entityName??>entityName='${entityName}'</#if> <#if oldValue??>oldValue='${oldValue}'</#if> <#if ignore??>ignore='${ignore}'</#if>
<#if groupName??>groupName='${groupName}'</#if> <#if index??>index=${index}</#if> name='${name}' id='${id}' class='easyui-combobox' data-options='
	<#if data??>
	data:${data},
	</#if>
	<#if url??>
	url:"${url}",
	</#if>
	valueField:"${valueField}",
	textField:"${textField}",
	editable:${editable?string("true","false")},
	multiple:${multiple?string("true","false")},
	<#if value??>
	value:"${value}",
	</#if>
	<#if disabled??>
	disabled:${disabled?string("true","false")},
	</#if>

	<#if (groupName??)&&(index??)>
		onSelect:function(record){
			var ownEle=$("#${id}[groupName=${groupName}]");
			$("input[groupName=${groupName}]").each(function(i,ele){
				var eleIndex=parseInt($(ele).attr("index"));
				var ownIndex=parseInt(ownEle.attr("index"));
				//清空子孙值
				if(eleIndex>ownIndex){
					$(ele).combobox("clear"); //清空后续输入框的值
				}
				//启用子控件,刷新子控件
				if(eleIndex==ownIndex+1){
					$(ele).combobox("enable"); //清空后续输入框的值
					$(ele).combobox("reload");
				}
				//禁用孙控件
				if(eleIndex>ownIndex+1){
					$(ele).combobox("disable"); //将下一个节点之后的所有节点设置成不可选择
				} 
			});
			<#if onSelect??>
				var self=this;
				${onSelect};
			</#if>
		},
		onBeforeLoad:function(param){
			var ownEle=$("#${id}[groupName=${groupName}]");
			var ownIndex=parseInt(ownEle.attr("index"));
			var lastIndex=ownIndex-1;
			var parentEle=$("input[groupName=${groupName}]").filter("[index="+lastIndex+"]");
			if(parentEle[0]){
				var parentValue=parentEle.combobox("getValue");
				if(parentValue==""){
					ownEle.combobox("clear");
					ownEle.combobox("disable");
				}else{
					param["${valueField}"]=parentValue;
				}
			}
			<#if onBeforeLoad??>
				${onBeforeLoad};
			</#if>
		},
	<#else>
		<#if onSelect??>
			onSelect:function(record){
				var self=this;
				${onSelect};
			},
		</#if>
		<#if onBeforeLoad??>
			onBeforeLoad:function(param){
				${onBeforeLoad};
			},
		</#if>
	</#if>
	<#if onLoadSuccess??>
		onLoadSuccess:function(){
			${onLoadSuccess};
			<#if multiple&&(value??)&&(value!="")>
			$("#${id}").combobox("setValues","${value}".split(","));
			</#if>
		},
	<#else>
		<#if multiple&&(value??)&&(value!="")>
		onLoadSuccess:function(){
			$("#${id}").combobox("setValues","${value}".split(","));
		},
		</#if>
	</#if>
	<#if onLoadError??>
		onLoadError:function(){
			${onLoadError};
		},
	</#if>
	<#if onUnselect??>
		onUnselect:function(record){
			var self=this;
			${onUnselect};
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
	<#if onChange??>
		onChange:function(newValue,oldValue){
			var self=this;
			${onChange};
		},
	</#if>
	width:"${width}",
	height:"${height}",
	panelWidth:"${panelWidth}",
	<#if panelHeight??>
	panelHeight:"${panelHeight}"
	</#if>
'/>
<#if (groupName??)&&(index??)>
	<#if index==1>
	</#if>
</#if>
