<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
	span .target{
		padding:10px;
	}
	div .icon{
		height:0px;
		display: inline-block;
		padding:8px 2px 10px 16px;
		width:0px;
		margin-top:-5px;
		margin-bottom:-5px;
	}
</style>
<div style="padding:10px">
	<span class="target">
		<span class="icon" style="background:gray;"></span>
		<span>未执行</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#F89800;"></span>
		<span>提交</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#00FF00;"></span>
		<span>同意</span>
	</span>
	<span class="target">
		<span class="icon" style="background:orange;"></span>
		<span>弃权</span>
	</span>
	<span class="target">
		<span class="icon" style="background:red;"></span>
		<span>当前节点</span>
	</span>
	<span class="target">
		<span class="icon" style="background:blue;"></span>
		<span>反对</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#8A0902;"></span>
		<span>驳回</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#023B62;"></span>
		<span>撤销</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#338848;"></span>
		<span>会签通过</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#82B7D7;"></span>
		<span>会签不通过</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#FFE76E;"></span>
		<span>会签再议</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#EEAF97;"></span>
		<span>人工终止</span>
	</span>
	<span class="target">
		<span class="icon" style="background:#C33A1F;"></span>
		<span>完成</span>
	</span>
</div>

<div id="processInsatnceImageContainer" style="overflow: auto;height:410px" >
	<div id="showInstanceImage" style=" margin:20px auto 0;position: relative;width:${shapeMeta.width}px;height:${shapeMeta.height+100}px;">
			${shapeMeta.xml} 
	</div>
</div>
<script type="text/javascript">
$(function(){
	var bgUrl="url('bpmImage?processInstanceId=${actInstId}&time="+new Date()+"')  no-repeat";
	$("#showInstanceImage").css("background",bgUrl);
});
</script>