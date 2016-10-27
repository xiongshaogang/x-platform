<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
  select {
    margin-bottom: 5px;
  }
</style>
<div class="pull-left all-person-box">
	<div class="top">
		<span class="top-left-span">选择成员：</span>
		<span class="top-left-btn">
			<!-- <a class="select-all">全选</a> -->
		</span>
		<div class="search">
			<input type="text" id="searchUser" place-holder="搜索成员"> <i class="glyphicon glyphicon-search"></i>
		</div>
	</div>
	<div class="person-box">
		<div class="slim-div">
			<div class="pb-item" style="display:none" id="roleArea">
				<div class="header">工作组</div>
				<div class="content person-list">
					<ul class="list-style-none" id="roleUL">
						<!-- <li id="1">
							<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
							<div class="name"><p class="text-overflow" title="18555665566">11111</p></div>
						</li>-->
					</ul>
				</div>
			</div>
			<div class="pb-item" style="display:none" id="orgArea">
				<div class="header">团队</div>
				<div class="content" id="ztreeDiv">
					<ul id="orgUL" class="ztree">
						<!-- <li id="1">
							<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
							<div class="name"><p class="text-overflow" title="18555665566">11111</p></div>
						</li> -->
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="pull-right selected-box">
	<div class="top">
		<span class="top-left-span">新添成员：</span>
		<span class="top-left-btn">
			<a class="remove-all">全部移除</a>
		</span>
		<span class="pull-right">已选<span class="selected-count" id="selectedCount">0</span>人
		</span>
	</div>
	<div class="person-box">
		<div class="slim-div">
			<div class="person-list approval-flow">
				<ul class="list-style-none-h" id="selectedUL">
					<!-- 
					<li id="1">
						<div class="item-person">
							<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
							<div class="name"><p class="text-overflow" title="18555665566">1111</p></div>
						</div>
						<i class="glyphicon glyphicon-remove"></i>
						<i class="glyphicon glyphicon-arrow-right"></i>
					</li>
					 -->
				</ul>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var zTree; 
var zNodes;
var selectedUL=$("#selectedUL");
var roleUL=$("#roleUL");
var selectedCount=$("#selectedCount");
var type = "click";
var options=${options};
var selected=options.selected||[];

function filter(treeId, parentNode, childNodes) {
	/* if (!childNodes) return null;
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;  */
}

var selectMul = {
	addToSelected:function(item){
		if(!options.multiple&&$("li",selectedUL).length>=1){
   			alert("只能选择一项");
   			return;
   		}
		if(!options.repeatSelect&&$("li#"+item.id,selectedUL)[0]){
			alert("已选择["+item.name+"]");
			return;
		}
		var _this=this;
		var portrait=null;
		if(item.type=="role"){
			portrait=item.portrait?item.portrait:selectMulType.getKindIcon(item.type);
		}else if(item.type=="org"){
			portrait=item.portrait?item.portrait:selectMulType.getKindIcon(item.type);
		}else if(item.type=="user"){
			portrait=item.portrait?item.portrait:defaultImg('${attachForeRequest}',item.portrait);
		}
		var $thisLi = $("<li id='"+item.id+"'>"+
    			"<div class='item-person'>"+
    			"<div class='avatar'><img src='"+portrait+"'></div><div class='name'><p title='"+item.name+"' class='text-overflow'>"+item.name+"</p></div>"+
    			"</div>"+
    			"<i class='glyphicon glyphicon-remove'></i>"+
    			(options.needArrow?"<i class='glyphicon glyphicon-arrow-right'></i>":"")+
    			"</li>");
		$thisLi.click(function(){
			$(this).closest("li").remove();
			selectedCount.html($("li",selectedUL).length);
		});
		$thisLi.data(item);
		selectedUL.append($thisLi);
		selectedCount.text($("li",selectedUL).length);
	},
	initRoleUL:function(result){
		var _this=this;
		$.each(result, function(i, item) {
			var info={
				id : item.id,
				name : item.name,
				type:"role",
				portrait : selectMulType.getKindIcon("role")
	    	};
				
			var templi = $("<li id='"+info.id+"'>"
					+"<div class='avatar'><img src='"+info.portrait+"'></div>"
					+"<div class='name'><p class='text-overflow' title='"+info.name+"'>"+info.name+"</p></div>"
					+"</li>").off().on("click",function(){
				   		_this.addToSelected(info);
				    });
			templi.data(info);		
			roleUL.append(templi);
		});
	},
    //表单权限、表单数据权限，审批人权限的用户列表
	getOrgTree : function(){
		ajax("orgnaizationController.do?getTree",function(result){
			zNodes = result.obj;
			//给ztree传递数据
			zTree = $.fn.zTree.init($("#orgUL"), setting, zNodes);
		});
	},
	//获取所有角色（role）
	getRoleList: function(){
		var _this=this;
	    ajax('roleController.do?queryMyRole',function(result){
	    	roleUL.empty();
	    	_this.initRoleUL(result.obj);
	    });
	},
	//人员列表json转化成HTML结构
    dataToLi: function(dataObj, container, addType){
    	$.each(dataObj, function(i, n){
    		var avatarUrl = "";
    		if(n.type == "user"){
    			avatarUrl = ("${attachForeRequest }" + n.portrait) || "basic/img/avatars/avatar_80.png"
    		}else{
    			avatarUrl = selectMulType.getKindIcon(n.type);
    		}
    		container.append($("<li id='"+n.id+"' data-type='"+n.type+"' data-portrait='"+n.portrait+"'>"+
        			"<div class='item-person'>"+
        			"<div class='avatar'><img src='"+avatarUrl+"'></div>"+
					"<div class='name'><p class='text-overflow' title='"+n.name+"'>"+n.name+"</p></div>"+
        			"</div>"+
        			"<i class='glyphicon glyphicon-remove'></i>"+
        			"<i class='glyphicon glyphicon-arrow-right'></i>"+
        			"</li>").click(function(){
        				$(this).closest("li").remove();
        				$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
        			}));
    	});
    	if(addType == "prepend"){
    		container.children(".add-item").appendTo(container);
    	}
    },
    //人员列表HTML转化成json
    liToData: function(liObj, dataObj){
    	liObj.each(function(index, ele){
     		console.log(ele);
    		dataObj.push({
    			"id": ele.id, 
    			"portrait": $(ele).attr("data-portrait"), 
    			"name": $(ele).find(".name").children("p").text(),
    			"type": $(ele).attr("data-type"),
    			"orderby": index + 1
    		});
    	});
    	
    	return dataObj;
    },
};

var setting = {
		view: {
			showLine: false
		},
		async: {
			enable: true,
			url:"orgnaizationController.do?getTreeData",
			dataType:"json",
			autoParam:["id=id"],
			dataFilter: filter //异步返回后经过Filter 
		},
		callback: {
			onClick: function(event,treeId, treeNode) {
				var _this=selectMul;
				var nodeType = (treeNode.isParent ? "org" : "user");
				var info={
					id:treeNode.id,
					name:treeNode.name,
					type:nodeType,
					portrait:treeNode.icon
				};
				if(nodeType=="org"){
					if(options.selectOrg){
						_this.addToSelected(info);
					}else{
						alert("无法选择团队");
					}
				}else if(nodeType=="user"){
					if(options.showOrgPerson){
						_this.addToSelected(info);
					}else{
						alert("无法选择用户");
					}
				}
			},
			onAsyncSuccess: function(event, treeId, treeNode, childNodes) {
				 var parentZNode = zTree.getNodeByParam("id", treeNode.id, null); //获取父节点  
				 parentZNode =  zTree.addNodes(parentZNode, childNodes.obj, true); 
			}
		}
	};
var timer;
 
$(function(){
	//调用获取用户列表方法
	if(options.showType.indexOf("org")!=-1){
		selectMul.getOrgTree();
		$("#orgArea").show();
	}
	if(options.showType.indexOf("role")!=-1){
		selectMul.getRoleList();
		$("#roleArea").show();
	}
	
	//反显已有数据
	$.each(selected,function(i,item){
		selectMul.addToSelected(item);
	});
	//初始化滚动条	
	home.loadSlimScroll([ {
		obj : $('.person-box .slim-div'),
		width: "100%"
	}]);
	
	
	/****** 各按钮绑定事件 ******/
	$(".remove-all").on("click", function(){
    	$("#selectedUser").empty();
    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
    });
	/****** 各按钮绑定事件 ******/
});
</script>

