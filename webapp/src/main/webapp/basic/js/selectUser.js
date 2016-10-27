var selUser = {
	//打开选人弹框统一处理
    selectUser: function(option, callback){
    	//type: tplt/apro; title: 模版权限/审批权限
    	/*
    	option = {
   			type: "tplt", 
   			title: "任务权限", 
   			selected: [{
   				id: "123", name: "张三", portrait: "basic/img/avatars/avatar_80.png", type: "user"
   			},{
   				id: "456", name: "李四", portrait: "basic/img/avatars/avatar_80.png", type: "user"
   			}]
    	};
    	*/
    	var _this = this;
    	var $modal = $("#templateModal"),
			$modalTitle = $modal.find(".modal-title"),
			$userList = $(".person-list.approval-flow > ul").empty();
		
		if(option.type == "tplt"){
	    	$modal.find(".modal-dialog").addClass("with-select-all");
		}else if(option.type == "apro"){
	    	$modal.find(".modal-dialog").removeClass("with-select-all");
		}
		
		//调用获取用户列表方法
		selUser.getUserList();
		selUser.getRoleList();
		
		$("#searchUser").val("");
    	$modalTitle.text(option.title);
    	$modal.data("modalType", option.type);
    	selUser.dataToLi(option.selected, $userList);
    	$(".selected-box .selected-count").text(option.selected.length);
    	
    	
    	$("#tpltConfirm").one("click", function(e){
	    	callback.call(_this, option.type);
        });
    },
    //表单权限、表单数据权限，审批人权限的用户列表
	getUserList : function(){
		$.ajax({
			url : 'orgnaizationController.do?getTree',
			type : 'post',
			data: '',
			dataType: "json",
			success : function(result) {
				if(result.success){
					//$("#searchUserUl").hide();
					$("#searchUserDiv").remove();
					$("#userList").show();
					zNodes = result.obj;
					zTree = $.fn.zTree.init($("#userList"), setting, zNodes);
				}else{
					alert("暂无数据");
				}
			}
			
		}); 
	},
	//获取所有角色（role）
	getRoleList: function(){
	    $.ajax({
			url : 'roleController.do?queryMyRole',
			type : 'post',
			data: '',
			dataType: "json",
			success : function(result) {
				if(result.success){
					$("#allRole").empty();
					$.each(result.obj, function(i, item) {
						//console.log(item);
						var templi = '<li id='+item.id+' data-type="role">'+
						'<div class="avatar"><img src="images/organization/iconfont-group.png"></div>'+
						'<div class="name"><p class="text-overflow" title="'+item.name+'">'+item.name+'</p></div>'+
						'</li>';
						
						$("#allRole").append(templi);
					});
					
					//选人弹框中，左侧所有人列表项点击添加到右侧
				    $("#allRole > li").on("click",function(){
				    	var $thisLi = $("<li id='"+this.id+"' data-type='role'>"+
				    			"<div class='item-person'>"+
				    			$(this).html()+
				    			"</div>"+
				    			"<i class='glyphicon glyphicon-remove'></i>"+
				    			"<i class='glyphicon glyphicon-arrow-right'></i>"+
				    			"</li>").click(function(){
				    				$(this).closest("li").remove();
				    				$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
				    			});
						
				    	$(".person-list.approval-flow ul").append($thisLi);
				    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
				    });
				    $(".person-list.approval-flow li .glyphicon-remove").on("click", function(){
				    	$(this).closest("li").remove();
				    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
				    });
				}
			}
		});
	},
	//人员列表json转化成HTML结构
    dataToLi: function(dataObj, container, addType){
    	$.each(dataObj, function(i, n){
    		var avatarUrl = "";
    		if(n.type == "user"){
    			avatarUrl = ("${attachForeRequest }" + n.portrait) || "basic/img/avatars/avatar_80.png"
    		}else{
    			avatarUrl = selUser.getKindIcon(n.type);
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
    //根据不同的type返回不同的图标
    getKindIcon: function(type){
    	var rtn = "";
    	
    	switch(type){
    		case "org":
    			return "images/organization/iconfont-org.png";
    			break;
    		case "role":
    			return "images/organization/iconfont-group.png";
    			break;
    		default:
    			return "";
    	}
    }
}