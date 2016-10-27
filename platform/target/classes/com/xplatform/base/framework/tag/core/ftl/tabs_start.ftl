<div id='${id}' class='easyui-tabs <#if tabPosition=="top">${classStyle}</#if>' data-options='
	<#if width??>
	width:${width},
	<#else>
	width:"auto",
	</#if>
	<#if height??>
	height:${height},
	<#else>
	height:"auto",
	</#if>
	plain:${plain?string("true","false")},
	fit:${fit?string("true","false")},	
	border:${border?string("true","false")},	
	scrollIncrement:${scrollIncrement},
	scrollDuration:${scrollDuration},
	toolPosition:"${toolPosition}",
	tabPosition:"${tabPosition}",
	leftDiv:${leftDiv?string("true","false")},
	<#if leftDivWidth??>
	leftDivWidth:${leftDivWidth},
	</#if>
	<#if leftDivTitle??>
	leftDivTitle:"${leftDivTitle}",
	</#if>
	rightDiv:${rightDiv?string("true","false")},
	<#if rightDivWidth??>
	rightDivWidth:${rightDivWidth},
	</#if>
	<#if rightDivTitle??>
	rightDivTitle:"${rightDivTitle}",
	</#if>
	<#if tabsAlign??>
	tabsAlign:"${tabsAlign}",
	</#if>
	closeBtn:${closeBtn?string("true","false")},
	hBorderLeft:${hBorderLeft?string("true","false")},
	hBorderRight:${hBorderRight?string("true","false")},
	hBorderTop:${hBorderTop?string("true","false")},
	hBorderBottom:${hBorderBottom?string("true","false")},
	leftTabHeight:${leftTabHeight},
	<#if flowButton??>
	flowButton:"${flowButton}",
	</#if>
	headerWidth:${headerWidth},
	<#if onLoad??>
	onLoad:function(panel){
		${onLoad};
	},
	</#if>
	<#if onBeforeClose??>
	onBeforeClose:function(title,index){
		${onBeforeClose};
	},
	</#if>
	<#if onClose??>
	onClose:function(title,index){
		${onClose};
	},
	</#if>
	<#if onAdd??>
	onAdd:function(title,index){
		${onAdd};
	},
	</#if>
	<#if onUpdate??>
	onUpdate:function(title,index){
		${onUpdate};
	},
	</#if>
	<#if onContextMenu??>
	onContextMenu:function(e, title,index){
		${onContextMenu};
	},
	</#if>
	onSelect : function(title,index) {
		var p = $(this).tabs("getTab", title);
		<#if onSelect??>
		${onSelect};
		</#if>
		<#if (tabList?size>0) >
			<#list tabList as tab1>
				<#if tab1.aysn>
					if(title=="${tab1.title}"){
						p.find("iframe").attr("src","${tab1.href}");
					}
				</#if>
			</#list>
		</#if>
	}
' >
