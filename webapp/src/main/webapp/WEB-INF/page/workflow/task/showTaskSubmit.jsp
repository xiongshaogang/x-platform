<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.taskSubmit_mainView {
	width: 98%;
	margin: 5px auto;
}

.taskSubmit_jumpView {
	height: 30px;
	line-height: 30px;
	background: #f8f9fa;
	border: 1px solid #d3d3d3;
}

.taskSubmit_radioSpan {
	margin-left: 5px;
}

.taskSubmit_pathView {
	width: 100%;
	height: 250px;
	margin-top: 5px;
}

.subChoice {
	margin-right: 5px !important;
	vertical-align: -1px;
}
</style>
<div class="taskSubmit_mainView">
	<div class="taskSubmit_jumpView">
		<c:if test="${fn:indexOf(jumpType,'1')!=-1}">
			<span class="taskSubmit_radioSpan">
				<input class="subChoice" type="radio" name="sublujingChoice" value="1"onclick="chooseJumpType(this)" />正常跳转
			</span>
		</c:if>
		<c:if test="${fn:indexOf(jumpType,'2')!=-1}">
			<span class="taskSubmit_radioSpan">
				<input class="subChoice" type="radio" name="sublujingChoice" value="2" onclick="chooseJumpType(this)" />选择路径跳转
			</span> 
		</c:if>
		<c:if test="${fn:indexOf(jumpType,'3')!=-1}">
			<span class="taskSubmit_radioSpan">
				<input class="subChoice" type="radio" name="sublujingChoice" value="3" onclick="chooseJumpType(this)" />自由回退
			</span>
		</c:if>
	</div>
	
				
	<div class="taskSubmit_pathView">
		<c:choose>
			<c:when test="${fn:indexOf(jumpType,'1')!=-1}">
				<div id="taskSubmit_pathPanel" class="easyui-panel" data-options="href:'taskController.do?tranTaskUserMap&taskId=${taskId}&selectPath=0',border:false,fit:true"></div>
			</c:when>
			<c:when test="${fn:indexOf(jumpType,'2')!=-1}">
				<div id="taskSubmit_pathPanel" class="easyui-panel" data-options="href:'taskController.do?tranTaskUserMap&taskId=${taskId}&selectPath=1',border:false,fit:true"></div>
			</c:when>
			<c:otherwise>
				<div id="taskSubmit_pathPanel" class="easyui-panel" data-options="href:'taskController.do?freeJump&taskId=${taskId}',border:false,fit:true"></div>
			</c:otherwise>
		</c:choose>
		
	</div>
</div>
<script>
	function chooseJumpType(ele) {
		//选择的流转方式值
		var val = ele.value;
		var pathView = $("#taskSubmit_pathPanel");
		var url = "";
		if (val == 1) {
			url = "taskController.do?tranTaskUserMap&taskId=${taskId}&selectPath=0";
		} else if (val == 2) {
			url = "taskController.do?tranTaskUserMap&taskId=${taskId}&selectPath=1";
		} else if (val == 3) {
			url = "taskController.do?freeJump&taskId=${taskId}";
		}
		pathView.panel("open").panel("refresh", url);
	}

	$(function() {
		if(${fn:indexOf(jumpType,'1')!=-1}){
			$("input[name=sublujingChoice][value=1]").attr("checked",true);
		}else if(${fn:indexOf(jumpType,'2')!=-1}){
			$("input[name=sublujingChoice][value=2]").attr("checked",true);
		}else if(${fn:indexOf(jumpType,'3')!=-1}){
			$("input[name=sublujingChoice][value=3]").attr("checked",true);
		}
	});
</script>
