<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div id="attachView_layout" class="easyui-layout" fit="true">
	<input id="common_attachType" type="hidden" /> <input id="common_viewFlag" type="hidden" /> <input id="businessKey"
		type="hidden" value="${businessKey}" /> <input id="businessType" type="hidden" value="${businessType}" /> <input
		id="businessExtra" type="hidden" value="${businessExtra}" /> <input id="otherKey" type="hidden" value="${otherKey}" />
	<input id="otherKeyType" type="hidden" value="${otherKeyType}" />
	<div region="center" style="padding: 0px;">
		<div class="common-dataViewDiv-top">
			<div class="common-dataViewDiv-filetype">
				<a href="#" onclick="common_findDataByType('');" class="list-item-infeed active"><i
					class="awsm-icon-file-alt bigger-160"></i><span>全部</span></a> <a href="#" onclick="common_findDataByType('img');"
					class="list-item-infeed"><i class="awsm-icon-picture bigger-160"></i><span>图片</span></a> <a href="#"
					onclick="common_findDataByType('doc');" class="list-item-infeed"><i class="awsm-icon-file-text-alt bigger-160"></i><span>文档</span></a>
				<a href="#" onclick="common_findDataByType('video');" class="list-item-infeed"><i
					class="awsm-icon-facetime-video bigger-160"></i><span>音频/视频</span></a> <a href="#"
					onclick="common_findDataByType('other');" class="list-item-infeed"><i
					class="awsm-icon-ellipsis-horizontal bigger-160"></i><span>其他</span></a>
			</div>
			<div class="common-dataViewDiv-viewtype">
				<button id="data_datagrid_btn" class="btn btn-xs btn-white data_datagrid_btn changeView" data-view="datagridView">
					<i class="awsm-icon-list bigger-130"></i>
				</button>
				<button id="data_thumbnail_btn" class="btn btn-xs btn-white data_thumbnail_btn changeView" data-view="thumbnailView">
					<i class="awsm-icon-th-large bigger-130"></i>
				</button>
			</div>
		</div>
		<div id="common_dataViewDiv" style="border: 0px" class="easyui-panel dataViewDiv"
			data-options="border:false,cache:false,fit:true,noheader:true"></div>
	</div>
</div>
<script type="text/javascript">
	var attachView_layout = $("#attachView_layout");
	var businessKey = $("#businessKey", attachView_layout).val();
	var businessType = $("#businessType", attachView_layout).val();
	var businessExtra = $("#businessExtra", attachView_layout).val();
	var otherKey = $("#otherKey", attachView_layout).val();
	var otherKeyType = $("#otherKeyType", attachView_layout).val();
	var uParam = "&businessKey=" + businessKey + "&businessType=" + businessType + "&businessExtra=" + businessExtra
			+ "&otherKey=" + otherKey + "&otherKeyType=" + otherKeyType;
	$(function() {
		$(".common-dataViewDiv-top a.list-item-infeed", attachView_layout).on("click", function() {
			$(".common-dataViewDiv-top a.list-item-infeed").removeClass("active");
			$(this).addClass("active");
		});
		$(".common-dataViewDiv-top button.changeView", attachView_layout).on(
				"click",
				function() {
					var viewFlag = $(this).attr("data-view");
					var attachType = $("#common_attachType").val();
					var requestFun;
					if (viewFlag == "datagridView") {
						requestFun = "datagridView";
					} else if (viewFlag == "thumbnailView") {
						requestFun = "thumbnailView";
					}
					$("#common_viewFlag", attachView_layout).val(viewFlag);

					$("#common_dataViewDiv", attachView_layout).panel("refresh",
							"attachController.do?" + requestFun + "&attachType=" + attachType + uParam);
				});
		setTimeout('$("#data_datagrid_btn", attachView_layout).click()', 0);
	});

	//按类型查找
	function common_findDataByType(attachType) {
		$("#common_attachType", attachView_layout).val(attachType);
		var viewFlag = $("#common_viewFlag", attachView_layout).val();
		if (viewFlag == 'datagridView') {
			$("#common_dataViewDiv", attachView_layout).panel("refresh",
					"attachController.do?datagridView" + "&attachType=" + attachType + uParam);
		} else {
			$("#common_dataViewDiv", attachView_layout).panel("refresh",
					"attachController.do?thumbnailView" + "&attachType=" + attachType + uParam);
		}
	}
</script>