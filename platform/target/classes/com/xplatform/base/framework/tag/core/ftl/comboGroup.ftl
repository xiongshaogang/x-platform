<!--处理级联-->
<script type="text/javascript"> 
	$(function(){
		<#list users as user>
		$('#${user}').combobox({
			onSelect:function(){
				var pid = $('#${user}').combobox('getValue');
				<#list users as user>
					$('#sel_2').combobox('setValue' , '');
					$('#sel_2').combobox('reload' , '${url}&pid='+pid);
				</#list>
			}
		});
		</#list>
	});
</script>
