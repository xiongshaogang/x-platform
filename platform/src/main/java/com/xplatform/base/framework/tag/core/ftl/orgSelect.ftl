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
	width:"${width}",
	height:"${height}",
	panelWidth:"${panelWidth}",
	<#if panelHeight??>
	panelHeight:"${panelHeight}",
	</#if>
	
	<#if onClick??>
	onClick:function(node){
		${onClick};
	},
	</#if>
	onChange:function(newValue, oldValue){
		<#if needText>
		var text="";
		if(newValue){
			if($.type(newValue)=="array"){
				$.each(newValue,function(){
					var node=$("#${id}").combotree("tree").tree("find",this);
					if(node){
						text+=node.text+",";
					}					
				});
			}else if($.type(newValue)=="string"){
				var node=$("#${id}").combotree("tree").tree("find",newValue);
				if(node){
					text+=node.text+",";
				}
			}
		}
		$("#${textId}").val(text.removeDot());
		</#if>
		<#if onChange??>
		${onChange};
		</#if>
	},
	<#if onCheck??>
	onCheck:function(node, checked){
		${onCheck};
	},
	</#if>
	<#if onShowPanel??>
	onShowPanel:function(){
		${onShowPanel};
	},
	</#if>
	<#if onHidePanel??>
	onHidePanel:function(){
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
			<#if autoExpand>
			$("#${id}").combotree("tree").tree("expandAll");
			</#if>
			<#if (value??)&&(value!="")>
			$("#${id}").combotree("tree").tree("expandAll");
			$("#${id}").combotree("setValues","${value}".split(","));
			</#if>
		}
	<#else>
		onLoadSuccess:function(node, data){
			<#if autoExpand>
			$("#${id}").combotree("tree").tree("expandAll");
			</#if>
			<#if (value??)&&(value!="")>
			$("#${id}").combotree("tree").tree("expandAll");
			$("#${id}").combotree("setValues","${value}".split(","));
			</#if>
		}
	</#if>
'/>
<#if needText>
<input id="${textId}" name="${textName}" type="hidden" value="${textValue}"/>
</#if>