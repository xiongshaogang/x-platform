$(document).ready(function(){
var frms = window.frames;
$("iframe[name$='printIFrame']").css("display","none");
var url = $("form[name*='_turnPageForm']").attr("action");
if(!url)return;
var report_code = url.substring(url.indexOf('?')+1);
var old_action=location.href;
var urls = old_action.split("?");
if(urls.length<2){
	$("form[name*='_turnPageForm']").attr("action",old_action);
	return ;
};
var paramArray = urls[1].split("&");
var lastParam= paramArray[paramArray.length-1];
var tmp=old_action;
if(lastParam.indexOf("=")==-1){
	if(old_action.indexOf("&")==-1)
		tmp = old_action.substring(0, old_action.lastIndexOf("?"));
	else
		tmp = old_action.substring(0, old_action.lastIndexOf("&"));
}
var concatStr="&";
if(tmp.indexOf("?")==-1)
	concatStr="?";
var new_action = tmp+concatStr+report_code;
//alert(new_action);
$("form[name*='_turnPageForm']").attr("action",new_action);
});