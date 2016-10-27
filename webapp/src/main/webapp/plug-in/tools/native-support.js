var jsBridge;

var nativeControllerCenter = function(cmd, params) {
	if (getUseragent() == 'iphone') {
//		var src = "http://WEBVIEW_ENGINE:" + cmd + ":" + params;
//		var elemIF = $("<iframe class='downloadIframe'/>");
//		elemIF.attr("src", src);
//		elemIF.hide();
//		elemIF.appendTo(document.body);
//		setTimeout(function(){
//			$(".downloadIframe").remove();
//		}, 500);
		jsBridge.callHandler(cmd,params);
	} else if (getUseragent() == 'android') {
		eval('javascript:WEBVIEW_ENGINE' + '.' + cmd + "('" + params + "')");
	} else if (getUseragent() == 'pcweb') {
		alert("Can't use this feature");
	}
};

//IOS 交互框架WebViewJavascriptBridge
var connectWebViewJavascriptBridge = function(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge);
    } else {
        document.addEventListener('WebViewJavascriptBridgeReady', function() {
            callback(WebViewJavascriptBridge);
        }, false);
    }
};

connectWebViewJavascriptBridge(function(bridge) {

    /* Init your app here */

    bridge.init(function(message, responseCallback) {
        alert('Received message: ' + message)   ;
        if (responseCallback) {
            responseCallback("Right back atcha");
        }
    })
    jsBridge=bridge;
//    jsBridge.registerHandler("showAlert", function(data) { alert(data) });
//    jsBridge.send('Please respond to this', function responseCallback(responseData) {
//        console.log("Javascript got its response", responseData)
//    })
});

var attachment={
	"select":function(businessKey,businessType,businessExtra,otherKey,otherKeyType,fileULId){
		var params='{"businessKey":"'+businessKey+'","businessType":"'+businessType+'","businessExtra":"'+businessExtra+'","otherKey":"'+otherKey+'","otherKeyType":"'+otherKeyType+'","fileULId":"'+fileULId+'"}';
		nativeControllerCenter('selectAttachment',params);
	},
	"download":function(aId,name){
		var params='{"aId":"'+aId+'","name":"'+name+'"}';
		nativeControllerCenter('downloadAttachment',params);
	},
	"select_callback":function(params){
		var data=parseJSON(params);
		if(data){
			commonFormEdit.appendAttachItem(data.attachments,$("[fileul="+escapeJquery(data.fileULId)+"]"));
		}
	}
};

var map={
	"getGPS":function(params){
		nativeControllerCenter('getGPSInfo',params);
	},
	"goMapView":function(params){
		nativeControllerCenter('goMapView',params);
	},
	"select_callback":function(params){
		var data=parseJSON(params);
		if(data){
			//visible排除掉模板项
			var fieldBox=$("[source="+escapeJquery(data.source)+"]").closest(".form-field-box:visible");
			var mapaddress=$("input[flag=mapaddress]",fieldBox);
			var lonandlat=$("input[flag=lonandlat]",fieldBox);
			var p=$("p",fieldBox);
			p.html(data.mapAddress);
			mapaddress.val(data.mapAddress);
			lonandlat.val(data.lonAndLat);
			mapViewIframe.attr("src","");
		}
	}
};

var selectPerson={
	"select":function(params){
		nativeControllerCenter('selectPerson',params);
	},
	"select_callback":function(params){
		var data=parseJSON(params);
		if(data.callbackKey){
			callbackFunction(data.callbackKey,data.selected);
		}
	}
};

var cloudDisk={
	"selectFolder":function(params){
		nativeControllerCenter('selectFolder',params);
	},
	"selectFolder_callback":function(params){
		var data=parseJSON(params);
		if(data.callbackKey){
			callbackFunction(data.callbackKey,data.selected);
		}
	}
};

var phone={
	"call":function(params){
		nativeControllerCenter('callPhone',params);
	}
};

var inputControl={
	"viewMore":function(params){
		params=filterSymbol(params);
		nativeControllerCenter('viewMore',params);
	}
};

var simpleCMD={
	//回退操作
	"back":function(params){
		nativeControllerCenter('back',params);
	},
	//移动端弹出新页面Webview加载
	"loadUrl":function(params){
		nativeControllerCenter('loadUrl',params);
	},
	//弹提示框
	"tip":function(params){
		if (getUseragent()=='iphone') {
			nativeControllerCenter('tip',params);
		} else if (getUseragent()=='android') {
			nativeControllerCenter('tip',params);
		} else if (getUseragent()=='pcweb'){
			alert(params);
		}
	},
	//显示处理中
	"showProcessing":function(params){
		if (getUseragent()=='iphone') {
			nativeControllerCenter('showProcessing',params);
		} else if (getUseragent()=='android') {
			nativeControllerCenter('showProcessing',params);
		} else if (getUseragent()=='pcweb'){
			home.handleLoading(true);
		}
		
	},
	//隐藏处理中
	"hideProcessing":function(params){
		if (getUseragent()=='iphone') {
			nativeControllerCenter('hideProcessing',params);
		} else if (getUseragent()=='android') {
			nativeControllerCenter('hideProcessing',params);
		} else if (getUseragent()=='pcweb'){
			home.handleLoading(false);
		}
	},
	//刷新上一页面的Webview
	"lastPageRefresh":function(params){
		nativeControllerCenter('lastPageRefresh',params);
	},
	//刷新上一页面的list
	"listRefresh":function(params){
		nativeControllerCenter('listRefresh',params);
	},
	//刷新上一页面的右上角关联模板(特殊)
	"relationTemplateRefresh":function(params){
		nativeControllerCenter('relationTemplateRefresh',params);
	},
	//移动端打开历史列表页面
	"goDataHistory":function(params){
		nativeControllerCenter('goDataHistory',params);
	}
};
