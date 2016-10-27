<#if layout=="div">
<div id="content">
<div id="wrapper">
<div id="steps">
</#if>
<form id="${formid}" name="${formid}" action="${action}"  method="${method}" <#if enctype??>enctype="${enctype}"</#if> >
<div id="form_div" class="form_div">
<!-- 如果是窗口模式并且是没有修改的提交按钮id--"btn_sub",则自动产生隐藏的提交按钮 -->
	<#if dialog >
	<input type="hidden" id="${btnsub}" class="${btnsub}"/>
	</#if>
