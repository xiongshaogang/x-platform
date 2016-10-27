<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
</style>
<script type="text/javascript">
	$(function() {
		//设置提示语句
		$("#confirmImport", getD($("#form_excelImport"))).linkbutton("disable");
		var excelImport_tip = "<ul class='excelImport_tip_ul'>"
				+ "<li>本功能支持[microsoft office 2003~2013]及[金山WPS]两款软件的excel文件导入</li>"
				+ "<li>请先下载导入的模板,然后在模板中构造导入的数据,请务必严格按照模板各列的单元格格式构造数据</li>" + "</ul>";
		var excelImport_api = yitip($("#importHelp"), excelImport_tip);
		$('#form_excelImport').fileupload(
				{
					uploadTemplateId : null,
					downloadTemplateId : null,
					change : function(e, data) {
						if (data.fileInput) {
							$("#selectFileName").val(data.fileInput.val());
							$("#selectFileName").attr("title", data.fileInput.val());
							//启用解析按钮
							// 							$("#uploadBtn").attr({
							// 								"disabled" : false
							// 							});
							$("#confirmImport", getD($("#form_excelImport"))).linkbutton("enable");
						}
					},
					add : function(e, data) {
						data.submit();
					},
					send : function(e, data) {
					},
					done : function(e, data) {
						if (data.textStatus == "success") {
							var result = data.result;
							if (result) {
								$("#errorSheetTab").show();
								$("#correctSheetTab").show();
								$("#form_excelImport .div_tip").remove();
								$('#form_excelImport .tabs-inner span').each(function(i, n) {
									var t = $(n).text();
									$('#correctSheetTab').tabs('close', t);
									$('#errorSheetTab').tabs('close', t);
								});
								// 								$("#errorSheetTab").tabs({});
								//获得正确与错误数据
								var correctResult = result.correctResult;
								var errorResult = result.errorResult;
								//构造handsontable默认参数
								var defaultOptions = {
									fixedRowsTop : 1, //最上方固定的行数
									readOnly : true, //禁止编辑
									fillHandle : false,//禁止下拉新增行/列
									rowHeaders : true, //行首，A,B,C,D...
									colHeaders : true, //列首，1,2,3,4...
									// 							colWidths : 120,
									currentRowClassName : 'currentRow', //选中行时，整行的样式class名
									currentColClassName : 'currentCol', //选中列时，整列的样式class名
									cells : function(row, col, prop) { //设置单元格格式
										var cellProperties = {};
										if (row === 0) {
											cellProperties.className = "htCenter htMiddle";
										}
										if (row === 0) {
											cellProperties.className = cellProperties.className + " ucg-cell-title";
										}
										return cellProperties;
									}
								};
								//增加正确sheet的tab,并填充实例化handsontable内容
								for (var i = 0; i < correctResult.length; i++) {
									var title = "Sheet" + (i + 1);
									var tableId = "correctResultDiv" + i;
									var correctContent = '<div id=' + tableId
											+ ' style="height:100%;width:100%;overflow:auto;"></div>';
									var d = correctResult[i];
									$("#correctSheetTab").tabs('add', {
										title : title,
										content : correctContent
									});
									var $correctResultDiv = $("#" + tableId);
									var options = $.extend({}, defaultOptions, {
										data : d
									});
									$correctResultDiv.handsontable(options);
								}
								//增加错误sheet的tab,并填充实例化handsontable内容
								for (var i = 0; i < errorResult.length; i++) {
									var title = "Sheet" + (i + 1);
									var tableId = "errorResultDiv" + i;
									var errorContent = '<div id=' + tableId
											+ ' style="height:100%;width:100%;overflow:auto;"></div>';
									var d = errorResult[i];
									$("#errorSheetTab").tabs('add', {
										title : title,
										content : errorContent
									});
									var $errorResultDiv = $("#" + tableId);
									var options = $.extend({}, defaultOptions, {
										data : d
									});
									$errorResultDiv.handsontable(options);
								}
								$("#changeResultLabel").html(
										"导入统计: 总共[" + result.allCount + "]条,正确数据[" + result.correctCount + "]条,错误数据["
												+ result.errorCount + "]条");
								$(".changeResultDiv").show();
								//解析完后,默认显示错误数据,若无错误数据,显示正确数据相关
								if (result.errorCount > 0) {
									$("#downloadError").show();
									$("#errorSheetTab").show();
									$("#correctSheetTab").hide();
									$("#changeResult").combobox("setValue", 1);
								} else {
									$("#downloadError").hide();
									$("#errorSheetTab").hide();
									$("#correctSheetTab").show();
									$("#changeResult").combobox("setValue", 0);
								}
								//默认选择第一个tab页
								$("#correctSheetTab").tabs("select", 0);
								$("#errorSheetTab").tabs("select", 0);
								$("#errorCount").val(result.errorCount);
								//禁用解析按钮
								// 								$("#uploadBtn").attr({
								// 									"disabled" : "disabled"
								// 								});
							}
						} else {
							tip("解析失败,请联系管理员!");
						}
					}
				});
	});
	var excelImport = {
		//正确、错误数据切换方法
		changeResult : function(record) {
			if (record.id == "1") {
				$("#errorSheetTab").show();
				$("#correctSheetTab").hide();
			} else if (record.id == "0") {
				$("#errorSheetTab").hide();
				$("#correctSheetTab").show();
			}
		},
		downloadFile : function(templateCode) {
			var url = "commonController.do?downloadTemplate&templateCode=" + templateCode;
			var elemIF = $("<iframe class='downloadIframe'/>");
			elemIF.attr("src", url);
			elemIF.appendTo(document.body);
			elemIF.hide();
			setTimeout("excelImport.removeIframe", 500);
		},
		removeIframe : function(url) {
			$(".downloadIframe").remove();
		},
		downloadError : function() {
			var name = $("#form_excelImport #name").val();
			var tempName = $("#form_excelImport #downTempName").val();
			var url = "commonController.do?downloadError&name=" + name + "&tempName=" + tempName;
			var elemIF = $("<iframe class='downloadIframe'/>");
			elemIF.attr("src", url);
			elemIF.appendTo(document.body);
			elemIF.hide();
			// 			setTimeout("excelImport.removeIframe", 1000);
		}
	};
</script>
<style>
#form_excelImport .mainDiv {
	width: 95%;
	margin: 0 auto;
}

#form_excelImport .fileinput-button {
	margin-bottom: 3px;
}

#form_excelImport .rowDiv {
	margin-top: 10px;
	width: 100%;
	height: 26px;
}

#form_excelImport .importViewDiv {
	height: 400px;
}

#form_excelImport .inputxt {
	margin-left: 5px;
	width: 150px;
	height: 24px;
	width: 150px;
}

#form_excelImport .div_tip {
	display: block;
	font-size: 65px;
	color: #ddd;
	text-align: center;
	line-height: 340px;
	-moz-user-select: none;
	-khtml-user-select: none;
	user-select: none;
	-moz-user-select: none;
	cursor: default;
}

#form_excelImport .changeResultDiv {
	display: none;
}

.excelImport_tip_ul {
	margin: 0 0 0 10px;
	text-align: left;
	font-size: 15px;
}
</style>
<form id="form_excelImport" method="post" action="commonController.do?importExcel" enctype="multipart/form-data">
	<input type="hidden" id="errorCount" value="0" /> <input type="hidden" name="titleRows" value="${titleRows}" /> <input
		type="hidden" name="headRows" value="${headRows}" /> <input type="hidden" name="startRows" value="${startRows}" /> <input
		type="hidden" name="startCell" value="${startCell}" /> <input type="hidden" name="endCell" value="${endCell}" /> <input
		type="hidden" name="keyIndex" value="${keyIndex}" /> <input type="hidden" name="sheetNum" value="${sheetNum}" /> <input
		type="hidden" name="needSave" value="${needSave}" /> <input type="hidden" name="dataHanlder" value="${dataHanlder}" />
	<input type="hidden" name="needHandlerFields" value="${needHandlerFields}" /> <input type="hidden" name="templateCode"
		value="${templateCode}" /> <input type="hidden" id="name" name="name" value="${name}" /> <input type="hidden"
		name="entityClass" value="${entityClass}" />
	<div class="mainDiv">
		<div class="downloadTemplateDiv rowDiv">
			<label>模板名:</label><input type="text" class="inputxt" id="downTempName" readonly="readonly" value="${fileName}" /> <span
				id="downTempSpan" class="btn btn-xs btn-success fileinput-button" style="width: 63px;"
				onclick="excelImport.downloadFile('${templateCode}')"> <i class="awsm-icon-download bigger-110"></i>模板下载
			</span> <span id="importHelp" style="color: #333399; font-size: 15px;"><i class="awsm-icon-question-sign bigger-110"
				style="margin: 0 3px;"></i>帮助</span>
		</div>
		<div class="uploadDiv rowDiv">
			<label>文件名:</label><input type="text" class="inputxt" id="selectFileName" readonly="readonly" /> <span
				id="selectImgSpan" class="btn btn-xs btn-success fileinput-button" style="width: 63px;"> <i
				class="awsm-icon-search bigger-110 "></i>选择Excel<input id="selectFile" type="file" name="file[]" multiple />
			</span>
			<!-- 			<span id="uploadBtn" class="btn btn-xs btn-success fileinput-button" style="width: 68px;"> <i -->
			<!-- 				class="awsm-icon-retweet bigger-110"></i>解析文件 -->
			<!-- 			</span>  -->

		</div>
		<div class="changeResultDiv rowDiv">
			<label id="changeResultLabel" style="margin-top: 3px">导入统计: 总共[]条,正确数据[]条,错误数据[]条</label>
			<t:comboBox name="changeResult" onSelect="excelImport.changeResult(record)" width="90" panelWidth="90" height="24"
				data='[{id:"0",text:"正确数据"},{id:"1",text:"错误数据"}]' />

			<span id="downloadError" class="btn btn-xs btn-success" style="width: 100px;"
				onclick="excelImport.downloadError()"> <i class="awsm-icon-download bigger-110 "></i>下载错误Excel
			</span>
		</div>
		<div class="importViewDiv rowDiv">
			<div id="correctSheetTab" class="sheetTab easyui-tabs" fit="true" data-options="tabPosition:'bottom'">
				<span class="div_tip" unselectable="on" onselectstart="return false;">导入数据展示区</span>
			</div>
			<div id="errorSheetTab" class="sheetTab easyui-tabs" fit="true" data-options="tabPosition:'bottom'">
				<span class="div_tip" unselectable="on" onselectstart="return false;">导入数据展示区</span>
			</div>
		</div>
	</div>
</form>

