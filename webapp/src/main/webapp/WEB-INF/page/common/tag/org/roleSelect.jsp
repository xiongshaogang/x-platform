<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var roleArray = [];
	var role;
	var roleMultipleSelect = ${multiples};
	var afterSaveClose=${afterSaveClose};
	<c:forEach var="role" items="${roleList}">
	role = {
		id : '${role.id}',
		name : '${role.name}'
	};
	roleArray.push(role);
	</c:forEach>
	$(function(){
		//加载已选项
		$.each(roleArray, function(i, ele) {
			addSelectedHtml(ele.id, ele.name);
		});
	});
	
	//添加已选Html
	function addSelectedHtml(id, name) {
		var spanContent = '<span class="viewdelete_span" id="'+ id +'"><label style="width:72%;cursor: default;" title="'
				+ name + '">' + name + '</label><a title="删除" href="#1" style="float: right;" onclick="removeRole(\''
				+ id + '\');"><i class="fa fa-times icon-color"></i></a></span>';
		$("#empRoleListSel").append(spanContent);
	}
	
	//添加已选数据(至数组里)
	function addSelectedData(id, name){
		var adds = {};
		adds.id = id;
		adds.name = name;
		roleArray.push(adds);
	}
	
	function addRole() {
		var rows = $("#roleListSel").datagrid('getSelections');
		if ((!roleMultipleSelect) && (rows.length > 1 || roleArray.length > 0)) {
			tip("只能选择一个角色");
		} else {
			for (var i = 0; i < rows.length; i++) {
				var roleId = rows[i].id;
				var roleName = rows[i].name;
				var isContained = false;
				//如果还没有被选中,则加入到右侧已选
				$.each(roleArray, function(i, ele) {
					if (ele.id == roleId) {
						isContained = true;
					}
				});

				if (!isContained) {
					addSelectedHtml(roleId,roleName);
					addSelectedData(roleId,roleName);
				}
			}
		}
	}

	function finishSelectRole(ele) {
		if ((!roleMultipleSelect) && roleArray.length > 1) {
			tip("只能选择一个角色");
		} else {
			var ids = "";
			var names = "";
			$.each(roleArray, function(i, ele) {
				ids += ele.id + ",";
				names += ele.name + ",";
			});
			ids = ids.removeDot();
			names = names.removeDot();
			$("#${hiddenId}").val(ids);
			$("#${displayId}").val(names);
			$("#${displayId}").attr("title",names);
			$("#${hiddenId}").blur();
			closeD(getD($(ele)));
		}
	}

	//删除触发方法
	function removeRole(roleId) {
		roleArray = $.grep(roleArray, function(ele, i) {
			return ele.id != roleId;
		});
		$("#empRoleListSel span").filter("#" + roleId).remove();
	}
	
	//直接保存方法
	function saveRole(ele){
		if ((!roleMultipleSelect) && roleArray.length > 1) {
			tip("只能选择一项");
		} else {
			var ids = "";
			var names = "";
			$.each(roleArray, function(i, ele) {
				ids += ele.id + ",";
				names += ele.name + ",";
			});
			ids = ids.removeDot();
			names = names.removeDot();
			var subUrl="${saveUrl}"+"&ids="+ids+"&names="+names;
			doSubmit(subUrl);
			if(afterSaveClose){
				closeD(getD($(ele)));
			}
		}
	}
</script>
<div class="easyui-layout" style="width: 100%; height: 100%; margin: 0 auto">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="roleListSel" checkbox="true" fit="true" fitColumns="true" title="角色列表"
			actionUrl="roleController.do?datagrid">
			<t:dgCol title="主键" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="角色名称" field="name" query="true" width="100"></t:dgCol>
			<t:dgCol title="角色编码" field="code" query="true" width="100"></t:dgCol>
			<t:dgToolBar id="role_gridAdd" title="加入已选" icon="glyphicon glyphicon-ok icon-color" onclick="addRole()"></t:dgToolBar>
			<c:if test="${needBtnSelected}">
			<t:dgToolBar id="finishSelectRole" title="确定" icon="glyphicon glyphicon-plus icon-color" onclick="finishSelectRole(this)"></t:dgToolBar>
			</c:if>
			<c:if test="${needBtnSave}">
			<t:dgToolBar id="saveRole" title="保存" icon="fa fa-save icon-color" onclick="saveRole(this)"></t:dgToolBar>
			</c:if>
		</t:datagrid>
	</div>
	<div id="empRoleListSel" title="已选角色" region="east" data-options="split:true" style="width:150px; text-align: center;">
	</div>
</div>
