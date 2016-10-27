//当前登录人的信息
var user_name,user_id,user_portrait,user_password;
var home = {
	//加载滚动条插件
	loadSlimScroll: function(objList){
		$.each(objList, function(){
			this.obj.slimScroll({
				height: this.height || "100%",
				width: this.width || "auto",
				start: this.start || "top",
				size: "5px",
				color: "#888",
				alwaysVisible: this.alwaysVisible || false,
				disableFadeOut: this.disableFadeOut || false
			});
			//this.obj.slimScroll().bind('slimscroll', function(e, pos){
			//    console.log("Reached " + pos);
			//});
		});
	},
	updateCid: function(cid){
		//console.log(cid);
		var t;
		return t = 2;
	},
	handleLoading: function(flag, pos){
		if(!pos){
			pos = $("body");
		}
		
		if(flag){
			if($(".sys-loading").length > 0){
				$(".sys-loading").remove();
			}
			
			pos.append(
					"<div class='sys-loading'>"
					+ "<div class='masker'></div>"
					+ "<div class='sys-loading-content'><img src='basic/img/bang_loading_outer.png'></div>"
					+ "</div>"
			);
		}else{
			if(!pos){
				$(".sys-loading").remove();
			}else{
				pos.find(".sys-loading").remove();
			}
		}
	}
}
