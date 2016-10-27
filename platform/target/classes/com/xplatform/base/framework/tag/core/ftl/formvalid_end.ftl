	<script type="text/javascript">
		$(function() {
			$('[datatype]',$('#${formid}')).yitip();
			$('#${formid}').Validform({
				<#if tiptype=="5">
				tiptype:function(msg, o, cssctl) {
					if(!$(o.obj).is("form")){
						var api = $(o.obj).yitip("api");
						api.setContent(msg);
						var validType=o.type;
						if(validType==2){
							api.hide(0);
						}else if(validType==3){
							api.show(0);
						}
					}
				},
				<#else>
				tiptype:${tiptype},
				</#if>
				btnSubmit:'#${btnsub}',
				btnReset:'#${btnreset}',
				ajaxPost:${ajaxPost?string("true","false")},
				showAllError:true,
				tipSweep:false,
				<#if beforeSubmit??>
				beforeSubmit:function(curform){
					var tag=false;
					<#if !beforeSubmit?contains(")")>
					return ${beforeSubmit+"()"};
					<#else>
					return ${beforeSubmit};
					</#if>
				},
				</#if>
				<#if usePlugin??>
				usePlugin : {
					<#if usePlugin?contains("password") >
					passwordstrength : {
						minLen : 6,
						maxLen : 18,
						trigger : function(obj, error) {
							if (error) {
								obj.parent().next().find(".Validform_checktip").show();
								obj.find(".passwordStrength").hide();
							} else {
								$(".passwordStrength").show();
								obj.parent().next().find(".Validform_checktip").hide();
							}
						}
					}
					</#if>
					<#if (usePlugin?contains("password"))&&(usePlugin?contains("jqtransform")) >
					// 如果同时引入了2个插件,中间要产生逗号(本解决方法有缺陷)
					,
					</#if>
					<#if usePlugin?contains("jqtransform") >
					jqtransform :{
						selector:'${jqtransformSelector}'
					}
					</#if>
				},
				</#if>
				callback : function(data) {
					<#if callback?? >
						<#if callback?contains(")")>
							${callback};
						<#else>
							${callback}(data);
						</#if>
					</#if>
					if (data.success == true) {
						if(data.msg&&data.msg!=""){
							tip(data.msg,'success','fa-check');
						} 
					} else {
						ajaxError(data);
						return false;
					}
					<#if dialog&&refresh&&(gridId??) >
					<#switch gridType>
						<#case "datagrid">
							$("#${gridId}").datagrid('reload');
							<#break>
						<#case "treegrid">
							$("#${gridId}").treegrid('reload');
							<#break>
						<#case "tree">
							$("#${gridId}").tree('reload');
							<#break>
						<#default>
					</#switch>
					</#if>
					<#if dialog&&afterSaveClose >
					closeD($('#${formid}').closest(".window-body"));
					</#if>
				}
			});
			
			if($('#${formid}').closest('.window-body')[0]){
				$('#${formid}').closest('.window-body').on('scroll',function(e){
					resetYitipPosition(e.currentTarget);
				});
			}
			
			<#if request.getAttribute("optFlag")??>
			var pageOptFlag="${request.getAttribute("optFlag")}";
			<#else>
			var pageOptFlag="";
			</#if>
			
			if("detail"==pageOptFlag){
				setTimeout(function (){
					pageReadonly($('#${formid}'));
				},0);
			}else{
				if(getD($('#${formid}'))[0]){
				    var dialogOptFlag=getD($('#${formid}')).dialog('options').optFlag;
				    if("detail"==dialogOptFlag){
				    	pageReadonly($('#${formid}'));
				    }
				}
			}
		});
	</script>
</div>
</form>
<#if layout=="div" >
</div>
<#if tabtitle?? >
	<div id="navigation" style="display: none;" >
	<ul>
	<#assign tabtitlesArray=(tabtitle?split(",")) >
	<#list tabtitlesArray as titles>
		<li><a href="#">${titles}</a></li>
	</#list>
	</ul>
	</div>
</#if>
</div>
</div>
</#if>