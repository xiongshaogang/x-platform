<#if !customInput>
<input class="tag_empsel_input" type="text" readonly="readonly" id="${displayId}" name="${displayName}" value="${displayValue}" title="${displayValue}" <#if extendDisplay??>${extendDisplay}</#if> />
</#if>

<button class="btn btn-pageselect-normal" type="button" onclick="goUserSelectPage(this);" multiples="${multiples?string('true','false')}" 
displayId="${displayId}" displayName="${displayName}" displayValue="${displayValue}" 
hiddenId="${hiddenId}" hiddenName="${hiddenName}" hiddenValue="${hiddenValue}" 
treeUrl="${treeUrl}" gridUrl="${gridUrl}" aysn="${aysn?string('true','false')}" onlyAuthority="${onlyAuthority?string('true','false')}"
orgCode="${orgCode}" containSelf="${containSelf?string('true','false')}" expandAll="${expandAll?string('true','false')}"
needBtnSelected="${needBtnSelected?string('true','false')}" needBtnSave="${needBtnSave?string('true','false')}" afterSaveClose="${afterSaveClose?string('true','false')}" 
<#if saveUrl??>saveUrl="${saveUrl}"</#if> empOrUser="${empOrUser}" <#if callback??>callback="${callback}"</#if> <#if showOrgTypes??>showOrgTypes="${showOrgTypes}"</#if>
<#if extendButton??>${extendButton}</#if> width="${width}" height="${height}">
	<i class="${icon} icon-on-right"></i><#if text??>${text}</#if>
</button>

<#if !customInput>
<input <#if datatype??>datatype='${datatype}'</#if> <#if sucmsg??>sucmsg='${sucmsg}'</#if> <#if nullmsg??>nullmsg='${nullmsg}'</#if> <#if errormsg??>errormsg='${errormsg}'</#if> <#if ajaxurl??>ajaxurl='${ajaxurl}'</#if> <#if uniquemsg??>uniquemsg='${uniquemsg}'</#if> <#if entityName??>entityName='${entityName}'</#if> <#if oldValue??>oldValue='${oldValue}'</#if> <#if ignore??>ignore='${ignore}'</#if>
class="orgs" relateDisplay="${displayId}" type="hidden" id="${hiddenId}" name="${hiddenName}" value="${hiddenValue}" <#if extendHidden??>${extendHidden}</#if> />
</#if>

