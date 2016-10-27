<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var orgMulArray = [];
	var orgMul;
	var orgMultipleSelect = ${multiples};
	var empExpandAll = ${expandAll};
	var needOrg=${needOrg};
	var needRole=${needRole};
	var needJob=${needJob};
	var afterSaveClose=${afterSaveClose};
	var empOrUser="${empOrUser}";
	var orgMulArray=[];
	if('${hiddenValue}'){
		orgMulArray=parseJSON('${hiddenValue}');
	}
	
	$(function() {
		setTimeout(function() {
			$.each($("#orgTypesAccordion").accordion("panels"), function() {
				var title = $(this).panel("options").title;
				var ck;
				if (title == "按组织") {
					ck = "<input class=\"o_checkbox\" type=\"checkbox\" id=\"orgType\" name=\"orgType\" style=\"margin:0 2px 0 8px;\"/>选组织"
				} else if (title == "按角色") {
					ck = "<input class=\"o_checkbox\" type=\"checkbox\" id=\"roleType\" name=\"roleType\" style=\"margin:0 2px 0 8px;\"/><span>选角色</span>"
				} else if (title == "按岗位") {
					ck = "<input class=\"o_checkbox\" type=\"checkbox\" id=\"jobType\" name=\"jobType\" style=\"margin:0 2px 0 8px;\"/><span>选岗位</span>"
				}
				$(this).panel("setTitle", title + ck);
			});
			$("#orgTypesAccordion input[type=checkbox]").filter("#orgType,#roleType,#jobType").on("click", function(e) {
				checkType(e, this);
			});
		},0);
		//加载已选项
		$.each(orgMulArray, function(i, ele) {
			addSelectedHtml(ele.id, ele.name, ele.type);
		});
		
		//给各按钮,选项绑定默认事件
		if(needOrg){
			$("#allOrgMul").on("click", function() {
				var queryParams = $("#orgMulGrid").datagrid("options").queryParams;
				$("#orgMulGridtb").find(":input").val("");
				$("#orgMulGrid").datagrid("load", {});
			});
		}
		
		if(needRole){
			//加载角色数据
			queryRoleList();
			$("#roleSearch").on("click", function() {
				var name = $("#roleInput").val();
				queryRoleList(name);
			});
			$("#roleHome").on("click", function() {
				queryRoleList();
				$("#roleInput").val("");
			});
			$(".roleListItem").off().on("click", function() {
				$("#orgMulGridtb").find(":input").val("");
				$("#orgMulGrid").datagrid("reload", {
					roleId : $(this).attr("id")
				});
			});
		}
		
		if(needJob){
			//加载岗位数据
			queryJobList();
			$("#jobSearch").on("click", function() {
				var name = $("#jobInput").val();
				queryJobList(name);
			});
			$("#jobHome").on("click", function() {
				queryJobList();
				$("#jobInput").val("");
			});
			$(".jobListItem").off().on("click", function() {
				$("#orgMulGridtb").find(":input").val("");
				$("#orgMulGrid").datagrid("reload", {
					jobId : $(this).attr("id")
				});
			});
		}
	});

	//checkbox选中后的逻辑
	function checkType(e, ele) {
		//阻止事件冒泡,否则无法勾选中
		e.stopPropagation();
		var index;
		var checked = false;
		checked = ele.checked;
		if (ele.id == "orgType") {
			index = 0;
			if (checked) {
				$("#OrgSelect4OrgMulSelect").tree("options").onClick = function(node) {
					orgMul_add(node.id, node.text, "org");
				};
			} else {
				$("#OrgSelect4OrgMulSelect").tree("options").onClick = function(node) {
					$("#orgMulGridtb").find(":input").val("");
					$("#orgMulGrid").datagrid("load", {
						orgId : node.id
					});
				};
			}
		} else if (ele.id == "roleType") {
			index = 1;
			if (checked) {
				$(".roleListItem").off().on("click", function() {
					orgMul_add($(this).attr("id"), $(this).attr("name"), "role");
				});
			} else {
				$(".roleListItem").off().on("click", function() {
					$("#orgMulGridtb").find(":input").val("");
					$("#orgMulGrid").datagrid("load", {
						roleId : $(this).attr("id")
					});
				});
			}
		} else if (ele.id == "jobType") {
			index = 2;
			if (checked) {
				$(".jobListItem").off().on("click", function() {
					orgMul_add($(this).attr("id"), $(this).attr("name"), "pos");
				});
			} else {
				$(".jobListItem").off().on("click", function() {
					$("#orgMulGridtb").find(":input").val("");
					$("#orgMulGrid").datagrid("load", {
						jobId : $(this).attr("id")
					});
				});
			}
		}
		//当前panel选中状态
		var selected = !($("#orgTypesAccordion").accordion("getPanel", index).panel("options").collapsed);
		//如果panel已选中展开,则收回
		if (selected) {
			// 			$("#orgTypesAccordion").accordion("getPanel", index).panel("collapse",true);
		} else { //如果panel未展开,则选中展开
			$("#orgTypesAccordion").accordion("getPanel", index).panel("expand", true);
		}
	}

	//查询角色列表
	function queryRoleList(name) {
		//加载角色数据
		var roleListUrl = "${roleListUrl}" +"&name=" + encodeURI(encodeURI(nulls(name)));
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : roleListUrl,// 请求的action路径
			success : function(data) {
				var d = $.parseJSON(data);
				if (d) {
					initRoleList(d);
				}
			}
		});
	}

	//查询岗位列表
	function queryJobList(name) {
		//加载角色数据
		var jobListUrl = "${jobListUrl}"+"&name=" + nulls(name);
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : jobListUrl,// 请求的action路径
			success : function(data) {
				var d = $.parseJSON(data);
				if (d) {
					initJobList(d);
				}
			}
		});
	}

	//渲染加载出角色列表
	function initRoleList(list) {
		var spanContent = "";
		$.each(list,function() {
			spanContent += '<span class="viewdelete_span roleListItem" name="'+this.name+'" id="' + this.id
			+ '"><i class="fa fa-suitcase green" style="margin-right:5px"></i><label style="width:80%;">'
			+ this.name + '</label></span>';
		});
		$("#asRoleDiv").html(spanContent);
	}

	//渲染加载出岗位列表
	function initJobList(list) {
		var spanContent = "";
		$.each(list,function() {
			spanContent += '<span class="viewdelete_span jobListItem" name="'+this.name+'" id="' + this.id
			+ '"><i class="awsm-icon-group blue" style="margin-right:5px"></i><label style="width:80%;">'
			+ this.name + '</label></span>';
		});
		$("#asJobDiv").html(spanContent);
	}

	//部门、角色、岗位添加方法
	function orgMul_add(id, name, type) {
		if ((!orgMultipleSelect) && orgMulArray.length > 0) {
			tip("只能选择一项");
		} else {
			var isContained = false;
			//如果还没有被选中,则加入到右侧已选
			$.each(orgMulArray, function(i, ele) {
				if (ele.id == id) {
					isContained = true;
				}
			});

			if (!isContained) {
				addSelectedHtml(id, name, type);
				addSelectedData(id, name, type);
			}
		}
	}

	//添加已选Html
	function addSelectedHtml(id, name, type) {
		var icon;
		if (type == 'role') {
			icon = '<i title="角色" class="fa fa-suitcase green"></i>';
		} else if (type == 'pos') {
			//TODO 
			icon = '<i title="岗位" class="awsm-icon-group blue"></i>';
		} else if (type == 'org') {
			icon = '<i title="部门" class="fa fa-sitemap orange"></i>';
		} else if (type == 'user') {
			icon = '<i title="员工" class="fa fa-comments purple"></i>';
		}
		var spanContent = '<span class="viewdelete_span" id="'+ id +'">' + icon + '<label style="width:72%;cursor: default;" title="'
				+ name + '">' + name + '</label><a title="删除" href="#1" style="float: right;" onclick="removeOrgMul(\''
				+ id + '\');"><i class="fa fa-remove red"></i></a></span>';
		$("#orgMulListSel").append(spanContent);
		
	}
	
	//添加已选数据(至数组里)
	function addSelectedData(id, name, type){
		var add = {};
		add.id = id;
		add.name = name;
		add.type = type;
		orgMulArray.push(add);
	}

	//从列表添加员工类型
	function orgMul_gridAdd() {
		var rows = $("#orgMulGrid").datagrid('getSelections');
		if ((!orgMultipleSelect) && (rows.length > 1 || orgMulArray.length > 0)) {
			tip("只能选择一项");
		} else {
			for (var i = 0; i < rows.length; i++) {
				var id="";
				if(empOrUser=="user"){
					id=rows[i].id;
				}else if(empOrUser=="emp"){
					id=rows[i].id;
				}
				var name = rows[i].name;
				var isContained = false;
				//如果还没有被选中,则加入到右侧已选
				$.each(orgMulArray, function(i, ele) {
					if (ele.id == id) {
						isContained = true;
					}
				});

				if (!isContained) {
					addSelectedHtml(id, name, "user");
					addSelectedData(id, name, "user");
				}
			}
		}
	}

	function finishSelectOrgMul(ele) {
		if ((!orgMultipleSelect) && orgMulArray.length > 1) {
			tip("只能选择一项");
		} else {
			var finalValue = "";
			var name = "";
			$.each(orgMulArray, function(i, ele) {
				finalValue += ele.id + "^^" + ele.name + "^^" + ele.type + ",";
				name += ele.name + ",";
			});
			finalValue = finalValue.removeDot();
			name = name.removeDot();
			$("#${hiddenId}").val(finalValue);
			$("#${displayId}").val(name);
			$("#${hiddenId}").blur();
			${callback};
			closeD(getD($(ele)));
		}
	}

	//删除触发方法
	function removeOrgMul(id) {
		orgMulArray = $.grep(orgMulArray, function(ele, i) {
			return ele.id != id;
		});
		$("#orgMulListSel span").filter("#" + id).remove();
	}

	//打开页面时是否异步加载出所有节点
	function treeOnLoadSuccess() {
		if (empExpandAll) {
			$("#OrgSelect4OrgMulSelect").tree("expandAll");
		}
	}
	
	//多类型直接保存方法
	function saveOrgMul(ele){
		if ((!orgMultipleSelect) && orgMulArray.length > 1) {
			tip("只能选择一项");
		} else {
			var finalValue = [];
			$.each(orgMulArray, function(i, ele) {
				var mulResult=new Object();
				mulResult.id=ele.id;
				mulResult.name=ele.name;
				mulResult.type=ele.type;
				finalValue[i]=mulResult;
			});
			var subUrl="${saveUrl}"+"&finalValue="+JSON.stringify(finalValue);
			var callback="${callback}";
			if(callback&&callback!=""){
				var backFun = new Function('${callback}');
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
<style>
#orgMulListSel i:first-child {
	margin-right: 3px;
}

.viewdelete_span label {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	cursor: pointer;
}
</style>
<div class="easyui-layout" style="width: 100%; height: 100%; margin: 0 auto">
	<div region="west" data-options="split:true" style="width: 150px" title="选择类型">
		<div id="orgTypesAccordion" class="easyui-accordion" style="width: 150px;" data-options="fit:true,border:false">
			<c:if test="${needOrg}">
				<div title="按组织" data-options="">
				<span id="allOrgMul" style="cursor: pointer; display: inline-block; margin: 5px 0; text-align: center; width: 100%;" class="icon-color" title="查询全体员工"><i
					class="glyphicon glyphicon-home icon-color" style="margin-right:5px"></i>全体员工</span>
				<t:tree id="OrgSelect4OrgMulSelect" url="${treeUrl}" gridId="orgMulGrid" gridTreeFilter="orgId"
					onLoadSuccess="treeOnLoadSuccess()"></t:tree>
			</div>
			</c:if>
			<c:if test="${needRole}">
			<div title="按角色" data-options="">
				<div>
					<span style="display: inline-block; margin-left: 5px; margin-top: 3px;"><input id="roleInput" type="text"
						style="width: 95px;" /><i id="roleSearch" title="搜索角色" class="glyphicon glyphicon-search icon-color"
						style="cursor: pointer; margin-left: 5px;"></i><i id="roleHome" title="全部角色"
						class="glyphicon glyphicon-home icon-color" style="cursor: pointer; margin-left: 5px;"></i></span>
					<div id="asRoleDiv" style="text-align: center;"></div>
				</div>
			</div>
			</c:if>
			<c:if test="${needJob}">
			<div title="按岗位" data-options="">
				<div>
					<span style="display: inline-block; margin-left: 5px; margin-top: 3px;"><input id="jobInput" type="text"
						style="width: 95px;" /><i id="jobSearch" title="搜索岗位" class="awsm-icon-search bigger-120 blue"
						style="cursor: pointer; margin-left: 5px;"><i id="jobHome" title="全部岗位" class="awsm-icon-home bigger-120 blue"
							style="cursor: pointer; margin-left: 5px;"></i></i></span>
					<div id="asJobDiv" style="text-align: center;"></div>
				</div>
			</div>
			</c:if>
		</div>
	</div>

	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="orgMulGrid" title="用户列表" actionUrl="${gridUrl}">
			<t:dgCol title="用户主键" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="昵称" field="name" query="true" width="100"></t:dgCol>
			<t:dgCol title="编码" field="code" query="true" width="100"></t:dgCol>
			<t:dgCol title="手机号" field="phone" query="true" width="100"></t:dgCol>
			<t:dgToolBar id="orgMul_gridAdd" title="加入已选" icon="glyphicon glyphicon-ok icon-color" onclick="orgMul_gridAdd()"></t:dgToolBar>
			<c:if test="${needBtnSelected}">
			<t:dgToolBar id="finishSelectOrgMul" title="确定" icon="glyphicon glyphicon-plus icon-color" onclick="finishSelectOrgMul(this)"></t:dgToolBar>
			</c:if>
			<c:if test="${needBtnSave}">
			<t:dgToolBar id="saveOrgMul" title="保存" icon="fa fa-save icon-color" onclick="saveOrgMul(this)"></t:dgToolBar>
			</c:if>
		</t:datagrid>
	</div>
	<div id="orgMulListSel" title="已选类型" data-options="split:true" region="east" style="width: 150px; text-align: center;">
	</div>
</div>
