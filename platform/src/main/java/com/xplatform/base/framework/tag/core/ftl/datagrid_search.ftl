<#if queryMode=="single"&&hasQueryColum>
<span class="searchbox_span">
<input id="${name}searchbox" class="easyui-searchbox"
  data-options="searcher:${name}searchbox,prompt:'请输入',menu:'#${name}mm'"></input>
<div id="${name}mm" class="searchbox">
${divs_str}
</div>
</span>
</#if>
</div>
