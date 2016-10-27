<div id="${id}" class="easyui-panel" <#if style?? >style="${style}"</#if>  data-options="
	cls:'collapse-title-wrapdiv',
	<#if title??>
	title:'${title}',
	</#if>
	collapsible:${collapsible?string('true','false')},
	collapsed:${collapsed?string('true','false')},
	<#if width??>
	width:${width},
	</#if>
	<#if height??>
	height:${height},
	</#if>
	<#if href??>
	href:'${href}',
	</#if>
	cache:${cache?string('true','false')},
	<#if loadingMessage??>
	loadingMessage:'${loadingMessage}',
	</#if>
	<#if openAnimation??>
	openAnimation:'${openAnimation}',
	</#if>
	<#if openDuration??>
	openDuration:${openDuration},
	</#if>
	<#if closeAnimation??>
	closeAnimation:'${closeAnimation}',
	</#if>
	<#if closeDuration??>
	closeDuration:${closeDuration},
	</#if>
	<#if method??>
	method:'${method}',
	</#if>
	<#if queryParams??>
	queryParams:${queryParams},
	</#if>
	fit:${fit?string('true','false')},
	doSize:${doSize?string('true','false')},
	noheader:${noheader?string('true','false')},
	border:${border?string('true','false')}
" >