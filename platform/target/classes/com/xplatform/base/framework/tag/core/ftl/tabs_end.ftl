<#if !customChildDiv>
	<#list tabList as tab>
		<#if tab.iframe>
			<div title="${tab.title}" class="tab_div">
				<#if tab.aysn>
				<iframe id="${tab.id}" scrolling="no" frameborder="0" width="<#if tab.width??>${tab.width}<#else>100%</#if>" height="<#if tab.height??>${tab.height}<#else>99.5%</#if>" >
				</iframe>
				<#else>
				<iframe id="${tab.id}" src="${tab.href}" scrolling="no" frameborder="0" width="<#if tab.width??>${tab.width}<#else>100%</#if>" height="<#if tab.height??>${tab.height}<#else>99.5%</#if>" >
				</iframe>
				</#if>
			</div>
			<#else>
			<div title="${tab.title}" cache="${tab.cache?string("true","false")}" class="tab_div" href="${tab.href}"></div>
		</#if>
	</#list>
</#if>
</div>

