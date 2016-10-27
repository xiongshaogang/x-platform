<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var empArray = [];
	var emp;
	var empMultipleSelect = ${multiples};
	var empExpandAll = ${expandAll};
	var afterSaveClose=${afterSaveClose};
	var empOrUser="${empOrUser}";
	var callback="${callback}";
	<c:forEach var="emp" items="${userList}">
	emp = {
		id : '${emp.id}',
		name : '${emp.name}'
	};
	empArray.push(emp);
	</c:forEach>
	
	$(function(){
		//加载已选项
		$.each(empArray, function(i, ele) {
			addSelectedHtml(ele.id, ele.name);
		});
		//给各按钮,选项绑定默认事件
		$("#allEmp").on("click", function() {
			var queryParams = $("#empGrid").datagrid("options").queryParams;
			$("#empGridtb").find(":input").val("");
			$("#empGrid").datagrid("load", {});
		});
	});
	function addEmp() {
		var rows = $("#empGrid").datagrid('getSelections');
		if ((!empMultipleSelect) && (rows.length > 1 || empArray.length > 0)) {
			tip("只能选择一个员工");
		} else {
			for (var i = 0; i < rows.length; i++) {
				var id="";
				if(empOrUser=="emp"){
					id=rows[i].empId;
				}else {
					id=rows[i].id;
				}
				var empName = rows[i].name;
				var isContained = false;
				//如果还没有被选中,则加入到右侧已选
				$.each(empArray, function(i, ele) {
					if (ele.id == id) {
						isContained = true;
					}
				});

				if (!isContained) {
					addSelectedHtml(id,empName);
					addSelectedData(id,empName);
				}
			}
		}
	}
	
	//添加已选Html
	function addSelectedHtml(id, name) {
		var spanContent = '<span class="viewdelete_span" id="'+ id +'"><label style="width:72%;cursor: default;" title="'
				+ name + '">' + name + '</label><a title="删除" href="#1" style="float: right;" onclick="removeEmp(\''
				+ id + '\');"><i class="fa fa-times icon-color"></i></a></span>';
		$("#empEmpListSel").append(spanContent);
	}
	
	//添加已选数据(至数组里)
	function addSelectedData(id, name){
		var addsEmp = {};
		addsEmp.id = id;
		addsEmp.name = name;
		empArray.push(addsEmp);
	}

	function finishSelectEmp(ele) {
		if ((!empMultipleSelect) && empArray.length > 1) {
			tip("只能选择一个员工");
		} else {
			var ids = "";
			var names = "";
			$.each(empArray, function(i, ele) {
				ids += ele.id + ",";
				names += ele.name + ",";
			});
			ids = ids.removeDot();
			names = names.removeDot();
			$("#${hiddenId}").val(ids);
			$("#${displayId}").val(names);
			$("#${displayId}").attr("title",names);
			$("#${hiddenId}").blur();
			${callback};
			closeD(getD($(ele)));
		}
	}

	//删除触发方法
	function removeEmp(empId) {
		empArray = $.grep(empArray, function(ele, i) {
			return ele.id != empId;
		});
		$("#empEmpListSel span").filter("#" + empId).remove();
	}
	
	//打开页面时是否异步加载出所有节点
	function treeOnLoadSuccess(node,data){
		var param;
		param="${showOrgTypes}";
		onlyAuthority=${onlyAuthority};
		if(param!=""){
			var nodes;
			if(node){
				nodes=$("#OrgSelect4EmpSelect").tree("getChildren",node.target);
				
			}else{
				nodes=$("#OrgSelect4EmpSelect").tree("getRoots");
			}
			$.each(nodes,function(i,e){
				var isDisabled=chargeShowTypeDisabled(e,param);
				if(isDisabled){
					disabledTreeNode($(e.target));
				}
			});
		}
		
		if(onlyAuthority){
			if(node){
				nodes=$("#OrgSelect4EmpSelect").tree("getChildren",node.target);
				
			}else{
				nodes=$("#OrgSelect4EmpSelect").tree("getRoots");
			}
			$.each(nodes,function(i,e){
				var isDisabled=chargeAuthorityDisabled(e);
				if(isDisabled){
					disabledTreeNode($(e.target));
				}
			});
		}
		
		if(empExpandAll){
			$("#OrgSelect4EmpSelect").tree("expandAll");
		}
	}
	
	//直接保存方法
	function saveEmp(ele){
		if ((!empMultipleSelect) && empArray.length > 1) {
			tip("只能选择一项");
		} else {
			var ids = "";
			var names = "";
			$.each(empArray, function(i, ele) {
				ids += ele.id + ",";
				names += ele.name + ",";
			});
			ids = ids.removeDot();
			names = names.removeDot();
			var subUrl="${saveUrl}"+"&ids="+ids+"&names="+names;
			if(callback&&callback!=""){
				var backFun = new Object(${callback});
				doSubmit(subUrl,null,backFun);
			}else{
				doSubmit(subUrl);
			}
			if(afterSaveClose){
				closeD(getD($(ele)));
			}
		}
	}
</script>
<div class="easyui-layout" style="width: 100%; height: 100%; margin: 0 auto">
	<div region="west" data-options="split:true" style="width: 150px" title="组织机构">
		<span id="allEmp" style="cursor: pointer; display: inline-block; margin: 5px 0; text-align: center; width: 100%;" class="icon-color" title="查询全体员工"><i
			class="glyphicon glyphicon-home icon-color" style="margin-right:5px"></i>全体员工</span>
		<t:tree id="OrgSelect4EmpSelect" url="${treeUrl}" gridId="empGrid" gridTreeFilter="orgId" onLoadSuccess="treeOnLoadSuccess(node,data)"></t:tree>
	</div>
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="empGrid" title="员工列表" actionUrl="${gridUrl}">
			<t:dgCol title="用户主键" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="用户姓名" field="name" query="true" width="100"></t:dgCol>
			<t:dgCol title="用户编码" field="code" query="true" width="100"></t:dgCol>
			<t:dgToolBar id="emp_gridAdd" title="加入已选" icon="glyphicon glyphicon-ok icon-color" onclick="addEmp()"></t:dgToolBar>
			<c:if test="${needBtnSelected}">
				<t:dgToolBar id="finishSelectEmp" title="确定" onclick="finishSelectEmp(this)" icon="glyphicon glyphicon-plus icon-color"></t:dgToolBar>
			</c:if>
			<c:if test="${needBtnSave}">
				<t:dgToolBar id="saveEmp" title="保存" onclick="saveEmp(this)" icon="fa fa-save icon-color"></t:dgToolBar>
			</c:if>
		</t:datagrid>
	</div>
	<div id="empEmpListSel" title="已选" data-options="split:true" region="east" style="width: 150px; text-align: center;"></div>
</div>
