<script type="text/javascript"> 
	$(function() { 
		$('#${id}').combogrid({
			panelWidth:${panelWidth},
			idField:'${id}',
			textField:'${textField}',
			url:'${url}',
			mode:'remote',
			columns:[[
				${columns}
			]]
		});
	});
</script>
<select id="${id}" name="${name}"  style="${style}"></select>