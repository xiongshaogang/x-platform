
var assignUseType = 0;
$(function () {
	$("a.moveup,a.movedown").click(move);
	var a = getInitData();
	$("#ruleDiv").linkdiv({data:a, updateContent:updateContent, rule2json:rule2json});
});
function addDiv(a) {
	$("#ruleDiv").linkdiv("addDiv", {ruleType:a});
}
function removeDiv() {
	$("#ruleDiv").linkdiv("removeDiv");
}
function assembleDiv() {
	$("#ruleDiv").linkdiv("assembleDiv");
}
function splitDiv() {
	$("#ruleDiv").linkdiv("splitDiv");
}
function getData() {
	var a = $("#ruleDiv").linkdiv("getData");
	return a;
}
function getInitData() {
	/*var data = $("#conditionTxt").val().trim();
	if (data == "") {
		return;
	}
	var json = eval("(" + data + ")");
	if (json.length == 0) {
		return;
	}
	return json;*/
}
function save() {
	var json = getData();
	var param = $("#conditionEntity").val();
	param = eval("(" + param + ")");
	assignUseType = param.conditionType;
	if (json.length > 0) {
		param.condition = JSON2.stringify(json);
	}
	var users = extractNodeUserDatas();
	if (!users) {
		$.ligerDialog.warn("\xe6\xb2\xa1\xe6\x9c\x89\xe8\xae\xbe\xe7\xbd\xae\xe7\x94\xa8\xe6\x88\xb7!", "\xe6\x8f\x90\xe7\xa4\xba\xe4\xbf\xa1\xe6\x81\xaf");
		return;
	}
	var conditionShow = getconditionShow();
	if (conditionShow) {
		param.conditionShow = conditionShow;
	}
	param.users = users;
	$.post(__ctx + "/platform/bpm/bpmUserCondition/save.ht", param, function (response) {
		var resultJson = eval("(" + response + ")");
		if (resultJson.result == 1) {
			$.ligerDialog.success("\xe4\xbf\x9d\xe5\x98\xe6\x88\x90\xe5\x8a\x9f!", "\xe6\x8f\x90\xe7\xa4\xba\xe4\xbf\xa1\xe6\x81\xaf", function (rtn) {
				window.returnValue = true;
				if (window.opener) {
					window.opener.location.href = window.opener.location.href.getNewUrl();
				}
				window.close();
			});
		} else {
			$.ligerDialog.warn(resultJson.message);
		}
	});
}
function move() {
	var c = $(this);
	var e = c.hasClass("moveup");
	var a = c.closest("tr");
	if (e) {
		var d = a.prev();
		if (d.length > 0) {
			a.insertBefore(d);
		}
	} else {
		var b = a.next();
		if (b.length > 0) {
			a.insertAfter(b);
		}
	}
}
function extractNodeUserDatas() {
	var a = new Array();
	$("#tbodyUserSet").find("tr").each(function () {
		var g = $(this);
		var j = g.find("[name='assignType']").val();
		var e = g.find("[name='nodeId']").val();
		var h = g.find("[name='cmpIds']").val();
		var b = g.find("[name='cmpNames']").val();
		var i = g.find("[name='nodeUserId']").val();
		var c = g.find("[name='compType']").val();
		var d = g.find("[name='extractUser']").val();
		var f = {nodeUserId:i, assignType:j, assignUseType:assignUseType, nodeId:e, cmpIds:h, cmpNames:b, compType:c, extractUser:d};
		a.push(f);
	});
	if (a.length == 0) {
		return false;
	}
	return JSON2.stringify(a);
}
function getconditionShow() {
	var a = [];
	$("#tbodyUserSet").find("tr").each(function () {
		var e = $(this), d = $("option:selected", $("select[name='assignType']", e)), b = "", g = $("textarea[name='cmpNames']", e).val(), c = $("option:selected", $("select[name='compType']", e)), f = "";
		if (d[0]) {
			b = d[0].text;
		}
		if (c[0]) {
			f = c[0].text;
		}
		if (!!!b) {
			b = $("input[name='assignType']", e).siblings("span").text();
		}
		if (a.length > 0) {
			a.push(" ");
			a.push(f);
			a.push(" ");
		}
		a.push(b);
		a.push(":");
		a.push(g);
	});
	return a.join("");
}
function getNodeUserDatas() {
	var b = new Array();
	$("#tbodyUserSet").find("tr").each(function () {
		var h = $(this);
		var g = h.find("input[name='nodeUserCk']:checked");
		var k = h.find("[name='assignType']").val();
		var e = h.find("[name='nodeId']").val();
		var i = h.find("[name='cmpIds']").val();
		var c = h.find("[name='cmpNames']").val();
		var j = h.find("[name='nodeUserId']").val();
		var d = h.find("[name='compType']").val();
		var f = {nodeUserCk:g.length > 0 ? true : false, nodeUserId:j, assignType:k, assignUseType:assignUseType, nodeId:e, cmpIds:i, cmpNames:c, compType:d};
		b.push(f);
	});
	if (b.length == 0) {
		return false;
	}
	var a = new Array();
	$.each(b, function (f, c) {
		if (c.nodeUserCk) {
			a.push(c);
		}
	});
	if (a.length > 0) {
		return JSON2.stringify(a);
	} else {
		return JSON2.stringify(b);
	}
}
function previewUserSetting(a) {
	var e = getNodeUserDatas();
	if (!e) {
		alert("\xe8\xbf\x98\xe6\xb2\xa1\xe6\x9c\x89\xe5\xae\x9a\xe4\xb9\x89\xe7\x94\xa8\xe6\x88\xb7\xe9\x85\x8d\xe7\xbd\xae!");
		return;
	}
	var b = "dialogWidth=600px;dialogHeight=400px;help=0;status=0;scroll=1;center=1";
	var c = __ctx + "/platform/bpm/bpmNodeUser/previewMockParams.ht?defId=" + a;
	c = c.getNewUrl();
	var d = window.showModalDialog(c, e, b);
}

