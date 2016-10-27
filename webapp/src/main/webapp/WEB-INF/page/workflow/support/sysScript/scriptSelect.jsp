<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var calTools = [ {
		exp : 'like',
		title : 'like',
		msg : '类似'
	}, {
		exp : 'not',
		title : 'not',
		msg : '不是'
	}, {
		exp : '||',
		title : 'or',
		msg : '或'
	}, {
		exp : '&&',
		title : 'and',
		msg : '并'
	}, {
		exp : '!=',
		title : '!=',
		msg : '不等于'
	}, {
		exp : '=',
		title : '=',
		msg : '等于'
	}, {
		exp : '<',title:'&lt;',msg:'小于'},{exp:'>',
		title : '>',
		msg : '大于'
	}, {
		exp : '(',
		title : '(',
		msg : '左括号'
	}, {
		exp : ')',
		title : ')',
		msg : '右括号'
	}, {
		exp : '/',
		title : '÷',
		msg : '除'
	}, {
		exp : '*',
		title : '×',
		msg : '乘'
	}, {
		exp : '-',
		title : '-',
		msg : '减'
	}, {
		exp : '+',
		title : '+',
		msg : '加'
	} ], scriptTree, scriptTypes = [];
	//初始化代码编辑器
	$(function() {
		//初始化代码高亮插件
		setTimeout('InitMirror.initId("script_result")', InitMirror.options.initDelay);
		initTools();
	});
	function initTools() {
		$.each(calTools, function(i, tool) {
			var calDiv = $("<div></div>");
			calDiv.attr("title", tool.msg);
			calDiv.html(tool.title);
			calDiv.addClass("calTool");
			calDiv.attr("title", tool.msg);
			calDiv.attr("exp", tool.exp);
			calDiv.on("click", tool.clickHandler || clickHandler);
			$("#tools_comment").append(calDiv);
		});
		$(".calTool").on("mouseenter mouseleave", function() {
			$(this).toggleClass("tool-hover");
		});
	}

	//表达式操作符点击事件
	function clickHandler() {
		var exp = $(this).attr("exp");
		InitMirror.insertValue("script_result", exp);
	}

	//脚本树双击选择对应脚本
	function scriptTreeClick(node) {
		InitMirror.insertValue("script_result", node.attributes.scriptContent);
	}

	//验证表达式
	function validExpression() {
		var scriptContent = InitMirror.getValue("script_result");
		var url = "sysScriptController.do?validExpression";
		$.ajax({
			async : false,
			cache : false,
			data : {
				scriptContent : scriptContent
			},
			type : 'POST',
			url : url,// 请求的action路径
			error : function() {// 请求失败处理函数
			},
			success : function(data) {
				var d = $.parseJSON(data);
				var html = "<table cellpadding='0' cellspacing='1'  class='resule_table'>" + "<tr><th>执行结果</th><td>" + d.success + "</td></tr>"
						+ "<tr><th>执行信息</th><td>" + d.msg + "</td></tr>" + "<tr><th>返回值类型</th><td>" + (d.obj ? d.obj.resultType : "")
						+ "</td></tr>" + "<tr><th>返回值</th><td>" + (d.obj ? d.obj.result : "") + "</td></tr>" + "</table>";
				$.parser.onComplete = mainComplete;
				createwindow("验证结果", null, 500, 173, null, {
					content : html
				});
			}
		});
	}
</script>
<style type="text/css">
.calTool {
	margin: 3px;
	float: left;
	background: url(webpage/workflow/support/sysScript/calTools.png) 0 0 no-repeat;
	width: 40px;
	height: 40px;
	font-size: 13px;
	font-weight: bold;
	text-align: center;
	line-height: 38px;
	cursor: pointer;
}

.tool-hover {
	background-position: 0 -40px;
}

#scripttree_div {
	width: 402px;
	height: 260px;
	float: left;
}

#opt_div {
	width: 140px; height : 260px;
	float: right;
	height: 260px;
}

#result_area {
	width: 99.9%;
	height: 205px;
	padding-top:5px;
	clear: both;
}

#tools_comment {
	text-align: center;
	margin: 0 auto;
}

.resule_table {
	width: 95%;
	margin: 5px auto 0px auto;
	border: 1px solid #4570a3;
}

.resule_table td,th {
	height: 30px;
	border-right: 1px solid #4570a3;
	border-top: 1px solid #4570a3;
	border-right: 1px solid #4570a3;
}

.resule_table th {
	width: 20%;
}

.resule_table td {
	width: 80%;
}
</style>
<div style="width: 98%; height: 98%; margin: 5px auto 0 auto;">
	<div id="scripttree_div">
		<div class="easyui-panel" fit="true" title="预设脚本">
			<t:tree id="scriptTree" url="sysScriptController.do?getScriptTree" onDblClick="scriptTreeClick(node)" onlyLeafClick="true" />
		</div>
	</div>
	<div id="opt_div">
		<div class="easyui-panel" fit="true" title="表达式运算符">
			<div id="tools_comment"></div>
		</div>
	</div>
	<div id="result_area">
		<div class="easyui-panel" fit="true" title="脚本内容">
			<div style="background: #F7F7F7;">
				<textarea mirrorheight="170px" id="script_result" class="input_area"></textarea>
			</div>
		</div>
	</div>
</div>