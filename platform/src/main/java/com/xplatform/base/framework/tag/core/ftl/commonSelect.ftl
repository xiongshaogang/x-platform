<#if !customInput>
<input <#if datatype??>datatype='${datatype}'</#if> <#if sucmsg??>sucmsg='${sucmsg}'</#if> <#if nullmsg??>nullmsg='${nullmsg}'</#if> <#if errormsg??>errormsg='${errormsg}'</#if> <#if ajaxurl??>ajaxurl='${ajaxurl}'</#if>
class="tag_jobsel_input" type="text" readonly="readonly" id="${displayId}" name="${displayName}" value="${displayValue}" />
</#if>

<button class="btn ${buttonColorClass} btn-sm btn-pageselect-normal" type="button" onclick="goCommonSelectPage(this);"
displayId="${displayId}" displayName="${displayName}" displayValue="${displayValue}" 
hiddenId="${hiddenId}" hiddenName="${hiddenName}" hiddenValue="${hiddenValue}"  
multiples="${multiples?string('true','false')}" <#if url??>url='${url}'</#if>
<#if gridFieldsJson??>gridFieldsJson='${gridFieldsJson}'</#if>
<#if width??>width='${width}'</#if> <#if height??>height='${height}'</#if>
hasTree='${hasTree?string('true','false')}' expandAll='${expandAll?string('true','false')}'
<#if treeUrl??>treeUrl='${treeUrl}'</#if> <#if gridTreeFilter??>gridTreeFilter='${gridTreeFilter}'</#if>
<#if title??>title='${title}'</#if> <#if callback??>callback="${callback}"</#if> idOrName="${idOrName}"
>
	<i class="${icon} icon-on-right bigger-110"></i><#if text??>${text}</#if>
</button>

<#if !customInput>
<input type="hidden" id="${hiddenId}" name="${hiddenName}" value="${hiddenValue}" />
</#if>