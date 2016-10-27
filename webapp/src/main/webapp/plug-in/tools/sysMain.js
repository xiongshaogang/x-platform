var sysMain = {
	// 页面初始化
	initIndex : function() {
		sysMain.initSysFuns();
		sysMain.initEvents();
	},
	initEvents : function() {
		$("#li_logout").click(function() {
			createconfirm(null, "亲,确定退出系统吗?", function(r) {
				if (r)
					window.location = "loginController.do?logout";
			})
		});
	},
	// 加载功能菜单
	initSysFuns : function() {
		$.ajax({
			async : true,
			cache : false,
			type : 'POST',
			url : "loginController.do?left", // 请求的action路径
			data : "",
			dataType : "text",
			error : function() { // 请求失败处理函数
				bootbox.alert("请求失败！");
			},
			success : function(data) {
				// 产生功能菜单
				sysMain.loadSysFuns(data);
				$("#user_concerned").children("a").trigger("click");
			}
		});
	},
	// 产生功能菜单
	loadSysFuns : function(d) {
		var mdJson_d = $.parseJSON(d),
			// $pageContent = $("#page_content").empty(),
			$pageContent = $("#page_content"),
			// $funTypeList = $("#fun-list > ul.nav-tabs").empty(),
			$funTypeList = $("#fun-list > ul.nav-tabs"), 
			$funsList = $("#fun-list > div.tab-content"), 
			$closeEle = $(".funlist-close");

		for (i in mdJson_d) {
			// 无子节点，有子系统
			if (mdJson_d[i].url != "" && (mdJson_d[i].url + "").length > 1 && mdJson_d[i].subsystem != "" && mdJson_d[i].subsystem != undefined) {
				// 判断是否已经存在该子系统的tab-pane
				if ($funsList.children("#" + mdJson_d[i].subsystem).length < 1) {
					$funTypeList.append("<li role='presentation'><a href='#" + mdJson_d[i].subsystem + "' aria-controls='" + mdJson_d[i].subsystem
							+ "' role='tab' data-toggle='tab'>" + (mdJson_d[i].subSystemName == "" ? mdJson_d[i].subsystem : mdJson_d[i].subSystemName)
							+ "</a></li>");
					$funsList
							.append("<div role='tabpanel' class='tab-pane' id='"
									+ mdJson_d[i].subsystem
									+ "'><div id='carousel_"
									+ mdJson_d[i].subsystem
									+ "' class='carousel slide qgz-list-carousel' data-ride='carousel' data-interval='false'><div class='carousel-inner' role='listbox'></div><a class='left carousel-control' href='#carousel_"
									+ mdJson_d[i].subsystem
									+ "' role='button' data-slide='prev'><span class='glyphicon glyphicon-chevron-left' aria-hidden='true'></span><span class='sr-only'>Previous</span></a><a class='right carousel-control' href='#carousel_"
									+ mdJson_d[i].subsystem
									+ "' role='button' data-slide='next'><span class='glyphicon glyphicon-chevron-right' aria-hidden='true'></span><span class='sr-only'>Next</span></a></div></div>");
				}

				var $thisTabpane = $funsList.children("#" + mdJson_d[i].subsystem), $thisCaInner = $thisTabpane.find(".carousel-inner"), $thisCaInnerLi = $thisCaInner
						.children("li");

				$thisCaInner.append("<li class='text-overflow' data-url='" + mdJson_d[i].url + "'>" + mdJson_d[i].name + "</li>");
				var ssFunNum = $thisCaInnerLi.length;
				// console.log(mdJson_d[i].subsystem);
				// if(mdJson_d[i].subsystem == "log"){
				// console.log(mdJson_d[i].name);
				// console.log(mdJson_d[i].subsystem);
				// console.log($thisCaInner.html());
				// }
				if (ssFunNum % 11 == 0 && ssFunNum > 0) {
					$thisCaInnerLi.wrapAll("<div class='item'></div>").wrapAll("<ul class='list-style-none-h'></ul>");
				}
			}
		}
		
		$funsList.find(".carousel-inner").each(function() {
			$(this).children("li").wrapAll("<div class='item'></div>").wrapAll("<ul class='list-style-none-h'></ul>");
			$(this).children(".item:first").addClass("active");
			if ($(this).children(".item").length < 2) {
				$(this).parent(".carousel").children("a").remove();
			}
		});

		$funsList.find("li").each(function() {
			$(this).on("click", function(event) {
				// $pageContent.load($(this).attr("data-url"));
				$pageContent.panel('resize',{height:$(window).height()-155});
				$pageContent.panel('open').panel('refresh', $(this).attr("data-url"));
				_disControl(false);
			});
		});
		
		//给各个功能绑定点击事件
		$funTypeList.children("li").each(function() {
			$(this).on("click", function() {
				if($(this).attr("id") == "user_concerned"){
					_disControl(false);
					//绑定数据，供onLoad事件判断是否为首页
					$pageContent.data("isConcerned", true);
					$pageContent.panel('open').panel('refresh', "concernPageController.do?concernPage");
				}else{
					_disControl(true);
				}
			});
		});
		
		//特殊处理首页（关注）按钮的点击事件及页面高度
		$pageContent.panel({
			onLoad: function(){
				if($pageContent.data("isConcerned")){
					var pageCtHeight = 0,
						pchCounter = 0;
					
					$pageContent.children("div").each(function(){
						pchCounter++;
						if(pchCounter <= $pageContent.children("div").length) pageCtHeight += $(this).outerHeight();
						$pageContent.panel('resize',{height:pageCtHeight});
					});
					$pageContent.data("isConcerned", false);
				}
			}
		});

		$closeEle.on("click", function() {
			_disControl(false);
		});

		// 控制菜单显示/隐藏的内部方法
		var _disControl = function(st) {
			if (st) {
				$funsList.show();
				$closeEle.show();
			} else {
				$funsList.hide();
				$closeEle.hide();
			}
		}
	},
	//显示tooltip气泡提示
	showTooltip : function(opt){
//		console.log("showTooltip");
//		debugger;
		if(opt.clean == null || opt.clean)
			$(".tooltip").remove();
		
		var obj = opt.obj;
		if(!(obj instanceof jQuery))
			obj = $(obj);
		  
		obj.attr({
			"data-toggle": 'tooltip',
			"data-title": opt.msg || "",
			"data-original-title": opt.msg || "",
			"data-placement": opt.placement || "",
			"data-container": opt.container || "body",
//			"data-container": "div.datagrid-view",  //test
			"data-trigger": opt.trg || "manual",  //manual: 手动
			"data-html": opt.html || "",
			"data-template": opt.tpl
		});
		
		obj.tooltip();
		if(opt.show || opt.show == null)
			obj.tooltip("show");
		
		if(opt.destroy || opt.destroy == null)
			setTimeout(function(){obj.tooltip("destroy");},20000);
//			setTimeout(function(){$(".tooltip").remove();},2000);
	}
}
