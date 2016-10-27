<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div id="treeAndTableLayout" class="easyui-layout" style="width: 100%; height: 100%;">
	<div region="west" split="true" href="generateController.do?getTreeData&model_id=${model_id}&type_id=${type_id}" title="生成模型选择"
		style="width: 150px; padding: 1px;" id="form_entity"></div>
	<div region="center" split="true" title="模型详细配置" style="overflow: auto;">
		<t:formvalid beforeSubmit="openDisabled(1)" action="generateConfigController.do?addorupdate" callback="retunMessage" refresh="false" afterSaveClose="false">
			<input id="id" name="id" type="hidden" value="${BaseFormEntity.id}">
			<input id="formTypeEntity.id" name="formTypeEntity.id" type="hidden" value="${type_id}">
			<input id="conBaseFormId" name="conBaseFormId" type="hidden" value="">
			<div class="easyui-panel" data-options="collapsible:true;border:0px;">
				<table cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td class="td_title"><label class="Validform_label"> 所属实体表</label></td>
						<td class="value"><t:comboBox url="metaDataController.do?metaDataEntityList" textField="tableName" valueField="id" value="${BaseFormEntity.entityId}"
								name="entityId" id="entityId" panelHeight="200"></t:comboBox></td>
						<td class="td_title"><label class="Validform_label">表类型</label></td>
						<td class="value"><t:comboBox dictCode="tableType" value="${BaseFormEntity.tableType}" name="tableType" id="tableType"></t:comboBox></td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 实体类名</label></td>
						<td class="value"><input type="text" class="inputxt" id="entityClass" name="entityClass" value="${BaseFormEntity.entityClass}" datatype="*"></td>
						<td class="td_title"><label class="Validform_label"> 包名</label></td>
						<td class="value"><input type="text" class="inputxt" id="pack" name="pack" value="${BaseFormEntity.pack}" datatype="*"></td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 模块Code<br />(用于日志)
						</label></td>
						<td class="value"><input type="text" class="inputxt" id="moduleCode" name="moduleCode" value="${BaseFormEntity.moduleCode}" datatype="*"></td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 文件类型</label></td>
						<td class="value" id="tbcheck"><t:comboBox dictCode="fileType" value="${BaseFormEntity.fileType}" name="fileType" id="fileType" multiple="true"
								panelHeight="150"></t:comboBox> <select id="showType" name="showType" disabled="disabled" style="display: none;">
								<option value="tree" <c:if test="${BaseFormEntity.showType eq 'tree'}"> selected='selected'</c:if>>tree</option>
								<option value="table" <c:if test="${BaseFormEntity.showType eq 'table'}"> selected="selected"</c:if>>table</option>
						</select></td>
						<td class="td_title"><label class="Validform_label"> 查询模式</label></td>
						<td class="value"><t:comboBox dictCode="queryType" value="${BaseFormEntity.queryType}" name="queryType" id="queryType"></t:comboBox></td>
					</tr>
					<tr id="for">
						<td class="td_title"><label class="Validform_label"> 外键(与树表id关联)</label></td>
						<td class="value"><input type="text" class="inputxt" id="connect" name="connect" value="${BaseFormEntity.connect}"></td>
						<td class="td_title"><label class="Validform_label"> 是否分页</label></td>
						<td class="value"><t:comboBox dictCode="YNType" value="${BaseFormEntity.isPagenation}" name="isPagenation" id="isPagenation"></t:comboBox></td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 显示复选框</label></td>
						<td class="value"><t:comboBox dictCode="YNType" value="${BaseFormEntity.selectType}" name="selectType" id="selectType"></t:comboBox></td>
						<td class="td_title"><label class="Validform_label"> 是否可编辑</label></td>
						<td class="value"><t:comboBox dictCode="YNType" value="${BaseFormEntity.enableEdit}" name="enableEdit" id="enableEdit"></t:comboBox></td>
					</tr>
					<tr>
						<td class="td_title"><label class="Validform_label"> 功能描述</label></td>
						<td class="value" colspan="3"><textarea name="description" id="description" class="input_area">${formFieldEntity.description}</textarea></td>
					</tr>
				</table>
			</div>
		</t:formvalid>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$.parser.onComplete = function() {
			selectTab('${con_id}', '${type_id}', '${type}', '${tableType}');
			$.parser.onComplete = mainComplete;
		};
	});
	function flagCheck() {
		var str = "";
		$(":checkbox[name='flag']").each(function() {
			if ($(this).is(":checked")) {
				str += $(this).val() + ",";
			}
		});
		$("#fileType").val(str.substring(0, str.length - 1));
	}
	function checkStatus() {
		var fileType = $("#fileType").val().split(",");
		$("#tbcheck").find("input[type='checkbox']").each(function() {
			var val = $(this).attr("value");
			$(this).attr("checked", false);
			if ($.inArray(val, fileType) != -1) {
				$(this).attr("checked", true);
			}
		});
	}

	function selectTab(con_Id, type_id, type, tableType) {

		$.ajax({
			url : "generateConfigController.do?getBaseFormEntity",
			type : "post",
			dataType : "json",
			async : false,
			data : {
				"con_Id" : con_Id,
				"type_id" : type_id
			},
			success : function(data) {
				var obj = $.parseJSON(data)[0];
				if (obj != null) {
					$("#formobj").find("input[type!='checkbox'],select,textarea").each(function() {
						if ($(this).attr("id") != 'btn_sub') {
							var boxid = $(this).attr("comboname");
							if (typeof (boxid) != 'undefined' && boxid != '') {
								$("#" + boxid).combobox({
									value : "" + obj[boxid] + ""
								});
							} else {
								var a = $(this).attr("id");
								$("#" + a).val(obj[a]);
							}
						}
					});
				} else {
					$("#formobj").find("input[type!='checkbox'],select").each(function() {
						if ($(this).attr("id") != 'btn_sub') {
							var boxid = $(this).attr("comboname");
							if (typeof (boxid) != 'undefined' && boxid != '') {
								$("#" + boxid).combobox({
									value : ""
								});
							} else {
								var a = $(this).attr("id");
								$("#" + a).val("");
							}
						}
					});
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert("提示信息", "数据加载失败");
			}
		});
		$("#tableType").combobox({
			value : tableType
		});
		$("#tableType").combobox("disable");
		$("#conBaseFormId").val(con_Id);
		$("#formTypeEntity.id").val(type_id);
		if (type.indexOf("tree") != -1) {
			$("#showType").val("tree");
			$("#for").css("display", "none");
		} else {
			$("#showType").val("table");
			$("#for").css("display", "");
		}
		return false;
	}
	//对form提交disabled属性无效进行处理，
	function openDisabled() {
		$("#tableType").combobox("enable");
		$("#showType").attr("disabled", false);
	}

	function retunMessage(data) {
		$("#id").val(data.obj);
		$("#tableType").combobox("disable");
		$("#showType").attr("disabled", true);
	}
</script>
