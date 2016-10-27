<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();

	String basePath = request.getScheme()
			+ "://"
			+ request.getServerName()
			+ (request.getServerPort() == 80 ? "" : ":"
					+ request.getServerPort()) + path;
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<%=basePath%>/">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
</head>
<script type="text/javascript" src="plug-in/mobile-detect/mobile-detect.js"></script>
<script type="text/javascript">
	//获取浏览器种类和版本
	var getBrowserInfo = function() {
		var Sys = {}, ua = navigator.userAgent.toLowerCase(), s, scan;

		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1]
				: (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : (s = ua.match(/micromessenger\/([\d.]+)/)) ? Sys.weixin = s[1] : (s = ua
						.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

		if (Sys.ie) {
			return {
				type : "ie",
				version : Sys.ie
			};
		}
		if (Sys.firefox) {
			return {
				type : "firefox",
				version : Sys.firefox
			};
		}
		if (Sys.chrome) {
			return {
				type : "chrome",
				version : Sys.chrome
			};
		}
		if (Sys.weixin) {
			return {
				type : "weixin",
				version : Sys.weixin
			};
		}
		if (Sys.opera) {
			return {
				type : "opera",
				version : Sys.opera
			};
		}
		if (Sys.safari) {
			return {
				type : "safari",
				version : Sys.safari
			};
		}
	}

	window.mobileCheck = function() {
		var check = false;
		(function(a) {
			if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i
					.test(a)
					|| /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i
							.test(a.substr(0, 4)))
				check = true;
		})(navigator.userAgent || navigator.vendor || window.opera);

		return check;
	};

	window.onload = function() {
		var md = new MobileDetect(window.navigator.userAgent);
		if (getBrowserInfo() && getBrowserInfo().type == "weixin") {
			//window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.huike.bang";
			alert("微信下载申请中...")
		} else {
			if(md.os()){
				if (md.os().toLowerCase().indexOf("android") > -1) {
					window.location.href = "appVersonController.do?downloadAppFile";
				} else if (md.os().toLowerCase().indexOf("ios") > -1) {
					window.location.href = "itms-services://?action=download-manifest&url=https://coding.net/u/yujiansirius/p/TDEngine/git/raw/master/download.plist";
				}
			}else{
				//网页端
				window.location.href = "appVersonController.do?downloadAppFile";
			}
		}
	};
</script>
</html>
