
function updateContent(k, e) {
	var c = $("<div class=\"nobr\" style=\"float:left;margin-right:-999em;\"></div>"), i = $("#flowVarsSelect").clone(true).removeAttr("id"), g = $("#paramKey").clone(true).removeAttr("id"), f = $("#paramCondition").clone(true).removeAttr("id"), b = $("#paramValue").clone(true).removeAttr("id"), d;
	if (!e) {
		d = "1";
	} else {
		d = e.ruleType;
	}
	if (d == "1") {
		labelSpan = $("<span class=\"label-span left\"></span>").attr("ruleType", d).text("普通规则");
		c.append(labelSpan).append(i).append($("<div class=\"judge left margin-set\"></div>"));
		$("div.nobr", k).remove();
		k.append(c);
		if (e) {
			i.val(e.flowvarKey).trigger("change");
			var a = $("div.judgeExp", k), j = $("div.judgeExp2", k);
			if (a) {
				setJudgeValue(a, e.judgeCon1, true);
				setJudgeValue(a, e.judgeVal1, false);
			}
			if (j) {
				setJudgeValue(j, e.judgeCon2, true);
				setJudgeValue(j, e.judgeVal2, false);
			}
		}
	} else {
		if (d == "2") {
			labelSpan = $("<span class=\"label-span left\"></span>").attr("ruleType", d).text("脚本规则");
			var h = $("<div class=\"judge left margin-set\"></div>").append($("<a name=\"script\" href=\"###\" onclick=\"editConditionScript(this)\">脚本</a>"));
			c.append(labelSpan).append(h);
			$("div.nobr", k).remove();
			k.append(c);
			if (e.script) {
				k.data("script", e.script);
			} else {
				if (typeof e.script === "undefined") {
					addConditionScript(k);
				}
			}
		} else {
			if (d == "3") {
				labelSpan = $("<span class=\"label-span left\"></span>").attr("ruleType", d).text("\xe7\x94\xa8\xe6\x88\xb7\xe5\xb1\x9e\xe6\x80\xa7");
			} else {
				labelSpan = $("<span class=\"label-span left\"></span>").attr("ruleType", d).text("\xe7\xbb\x84\xe7\xbb\x87\xe5\xb1\x9e\xe6\x80\xa7");
			}
			c.append(labelSpan).append(g).append(f).append(b);
			$("div.nobr", k).remove();
			k.append(c);
			if (e) {
				g.val(e.paramKey);
				f.val(e.paramCondition);
				b.val(e.paramValue);
			}
		}
	}
}
function addConditionScript(b) {
	var a = $("#defId").val();
	ConditionScriptEditDialog({data:{defId:a}, callback:function (c) {
		b.data("script", c.script);
	}});
}
function editConditionScript(c) {
	var e = $(c), a = $("#defId").val(), d = e.parent().parent().parent(), b = d.data("script");
	ConditionScriptEditDialog({data:{defId:a, script:b}, callback:function (f) {
		d.data("script", f.script);
	}});
}
function getFlowVarType(b) {
	var d = b.val(), c = b.attr("ctltype"), a = b.attr("ftype");
	if (c) {
		switch (c.toString()) {
		  case "4":
		  case "5":
		  case "6":
		  case "7":
		  case "8":
		  case "17":
		  case "18":
		  case "19":
			return 5;
		  case "3":
		  case "11":
		  case "13":
		  case "14":
			return 4;
		  case "15":
			return 3;
		}
	}
	if (d == "startUser") {
		return 5;
	}
	if (a) {
		switch (a.toLowerCase()) {
		  case "int":
		  case "number":
			return 1;
		  case "varchar":
		  case "string":
			return 2;
		  case "date":
			return 3;
		}
	}
	return 2;
}
function getDicControl(option) {
	var value = option.val(), ctltype = option.attr("ctltype"), chosenopt = option.attr("chosenopt"), opts = eval("(" + chosenopt + ")"), html = "", data = [], type = "";
	var tempHtml = $("#dic-radio-checkbox").val();
	if (ctltype.toString() == "13") {
		type = "checkbox";
	} else {
		if (ctltype.toString() == "14") {
			type = "radio";
		} else {
			tempHtml = $("#dic-select").val();
		}
	}
	for (var i = 0, c; c = opts[i++]; ) {
		data.push({type:type, name:value, option:c.key, memo:c.value});
	}
	html = easyTemplate(tempHtml, data).toString();
	return $(html);
}
function getSelector(b) {
	var a = b.attr("ctltype"), c = b.val(), e = "user-div";
	switch (a.toString()) {
	  case "4":
	  case "8":
		e = "user-div";
		break;
	  case "5":
	  case "17":
		e = "role-div";
		break;
	  case "6":
	  case "18":
		e = "org-div";
		break;
	  case "7":
	  case "19":
		e = "position-div";
		break;
	}
	var d = $("#" + e).clone(true, true).removeAttr("id");
	$("input[type='text']", d).attr("name", c);
	$("input[type='hidden']", d).attr("name", c + "ID");
	$("a.link", d).attr("name", c);
	return d;
}
function getJudgeExp(a, d) {
	var f = d.attr("datefmt"), c = $("<div class=\"judgeExp left\"></div>"), e = $("#judgeCon-" + a).clone(true).removeAttr("id"), b;
	if (!e || e.length == 0) {
		e = $("#judgeCon-1").clone(true).removeAttr("id");
	}
	c.attr("optType", a).append(e);
	switch (a) {
	  case 1:
	  case 2:
		b = $("#normal-input").clone(true).removeAttr("id");
		break;
	  case 3:
		b = $("#date-input").clone(true).removeAttr("id").attr("datefmt", f);
		break;
	  case 4:
		b = getDicControl(d);
		break;
	  case 5:
		b = getSelector(d).children();
		break;
	}
	c.append(b);
	return c;
}
function flowVarChange() {
	var e = $(this), b = e.parents("div.nobr"), c = e.find("option:selected");
	if (!c.val()) {
		return;
	}
	var d = getFlowVarType(c), f = c.attr("datefmt"), i = null;
	var h = $("div.judge", b).empty(), a = getJudgeExp(d, c);
	h.append(a);
	if (d == 1 || d == 3) {
		var g = a.clone(true).removeClass("judgeExp").addClass("judgeExp2");
		h.append(g);
	}
}
function getJudgeValue(a) {
	if (!a) {
		return false;
	}
	var b = [];
	a.find("input").each(function () {
		var d = $(this), c = d.attr("type");
		if (c == "checkbox" || c == "radio") {
			if (d.attr("checked")) {
				b.push(d.val());
			}
		} else {
			b.push(d.val());
		}
	});
	a.find("select").each(function () {
		var d = $(this), c = d.attr("name");
		if (c == "judgeCondition") {
			return true;
		}
		b.push(d.val());
	});
	return b.join("&&");
}
function getJudgeText(a) {
	if (!a) {
		return "";
	}
	var b = [];
	a.find("input:visible").each(function () {
		var d = $(this), c = d.attr("type");
		if (c == "checkbox" || c == "radio") {
			if (d.attr("checked")) {
				b.push(d.parent().text());
			}
		} else {
			b.push(d.val());
		}
	});
	a.find("select").each(function () {
		var d = $(this), c = d.attr("name");
		if (c == "judgeCondition") {
			return true;
		}
		b.push(d.find("option:selected").text());
	});
	return b.join("&&");
}
function setJudgeValue(b, c, a) {
	if (!b) {
		return;
	}
	if (!a) {
		b.find("input").each(function () {
			var e = $(this), g = e.val(), d = e.attr("type");
			if (d == "checkbox" || d == "radio") {
				if (c.indexOf(g) > -1) {
					e.attr("checked", "checked");
				}
			} else {
				if (/\&\&/.test(c)) {
					var f = c.split(/\&\&/);
					if (e.attr("type") == "hidden") {
						e.val(f[0]);
					} else {
						e.val(f[1]);
					}
				} else {
					e.val(c);
				}
			}
		});
	}
	b.find("select").each(function () {
		var e = $(this), d = e.attr("name");
		if ((d == "judgeCondition") == a) {
			e.val(c);
		}
	});
}
function rule2json(o) {
	var n = {}, g = $("span.label-span", o).attr("ruleType");
	if (g == "1") {
		var d = $("select[name='flowVars']", o).val(), j = $("select[name='flowVars']", o).find("option:selected").text(), b = $("div.judgeExp", o), m = $("div.judgeExp2", o), c = [];
		if (!b || b.length == 0) {
			return null;
		}
		c.push(j);
		var i = b.attr("optType");
		n.optType = i;
		n.flowvarKey = d;
		n.judgeCon1 = $("select[name='judgeCondition']", b).val();
		c.push($("select[name='judgeCondition']", b).find("option:selected").text());
		n.judgeVal1 = getJudgeValue(b);
		c.push(getJudgeText(b));
		if (m && m.length > 0) {
			n.judgeCon2 = $("select[name='judgeCondition']", m).val();
			c.push("\xe5\xb9\xb6\xe4\xb8\x94");
			c.push($("select[name='judgeCondition']", m).find("option:selected").text());
			n.judgeVal2 = getJudgeValue(m);
			c.push(getJudgeText(m));
		}
		n.conDesc = c.join(" ");
	} else {
		if (g == "2") {
			var k = o.data("script");
			n.script = k;
			n.conDesc = " \xe8\x84\x9a\xe6\x9c\xac ";
		} else {
			var c = [];
			var h = $("select[name='paramKey']", o).val(), l = $("select[name='paramKey']", o).find("option:selected").attr("title");
			var f = $("select[name='paramCondition']", o).val();
			var e = $("input[name='paramValue']", o).val();
			n.paramKey = h;
			n.paramCondition = f;
			n.paramValue = e;
			c.push(h);
			c.push(f);
			c.push(e);
			n.conDesc = c.join(" ");
			n.expression = h + f + e;
			n.dataType = l;
		}
	}
	n.ruleType = g;
	var a = $("select.computing-select", o).val();
	if (a) {
		n.compType = a;
	}
	return n;
}

