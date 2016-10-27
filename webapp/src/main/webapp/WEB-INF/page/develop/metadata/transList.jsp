<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:datagrid name="transList" fitColumns="false" pagination="false" checkbox="true" fit="true" queryMode="group"
	actionUrl="cgformTransController.do?datagrid" idField="id" sortName="id">
	<t:dgCol title="表名" field="id" query="true" width="300"></t:dgCol>
	<t:dgToolBar title="生成表单" icon="glyphicon glyphicon-forward icon-color" url="metaDataController.do?transEditor" funname="dataEditor" operationCode="formHeadManager_transEditor_other" buttonType="GridDetail"></t:dgToolBar>
</t:datagrid>
<script type="text/javascript">
	function dataEditor(title, url, id) {
		var ids = [];
		var rows = $("#transList").datagrid('getSelections');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++){
				ids.push(rows[i].id);
			}
			$.ajax({
				url : url + "&id=" + ids,
				type : 'post',
				cache : false,
				success : function(data) {
					var c = $.parseJSON(data);
					var d = "";
					var e = "";
					$.each(c.obj, function(key, value) {
						if (key == "no")
							d = value;
						else
							e = value;
					});
					alertTip("生成成功:" + e);
					$("#metadataList").datagrid('reload');
					closeD(getD($("#transList")));
				}
			});
		} else
			alertTip("请选择要生成表单的项!");
	}
</script>