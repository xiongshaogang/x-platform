<#if !customInput>
<input class="tag_jobsel_input" type="text" readonly="readonly" id="${displayId}" name="${displayName}" value="${displayValue}" <#if extendDisplay??>${extendDisplay}</#if>/>
</#if>

<button class="btn btn-purple btn-sm btn-pageselect-normal" type="button" onclick="goJobSelectPage(this);" multiples="${multiples?string('true','false')}" 
displayId="${displayId}" displayName="${displayName}" displayValue="${displayValue}" 
hiddenId="${hiddenId}" hiddenName="${hiddenName}" hiddenValue="${hiddenValue}"
needBtnSelected="${needBtnSelected?string('true','false')}" needBtnSave="${needBtnSave?string('true','false')}" afterSaveClose="${afterSaveClose?string('true','false')}" 
<#if saveUrl??>saveUrl="${saveUrl}"</#if> 
<#if extendButton??>${extendButton}</#if> width="${width}" height="${height}">
	<i class="${icon} icon-on-right bigger-110"></i><#if text??>${text}</#if>
</button>

<#if !customInput>
<input <#if datatype??>datatype='${datatype}'</#if> <#if sucmsg??>sucmsg='${sucmsg}'</#if> <#if nullmsg??>nullmsg='${nullmsg}'</#if> <#if errormsg??>errormsg='${errormsg}'</#if> <#if ajaxurl??>ajaxurl='${ajaxurl}'</#if> <#if uniquemsg??>uniquemsg='${uniquemsg}'</#if> <#if entityName??>entityName='${entityName}'</#if> <#if oldValue??>oldValue='${oldValue}'</#if> <#if ignore??>ignore='${ignore}'</#if>
class="orgs" relateDisplay="${displayId}" type="hidden" id="${hiddenId}" name="${hiddenName}" value="${hiddenValue}" <#if extendHidden??>${extendHidden}</#if> />
</#if>
