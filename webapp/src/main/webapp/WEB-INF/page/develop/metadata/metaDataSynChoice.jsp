<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type=text/javascript src="plug-in/clipboard/ZeroClipboard.js"></script>
<div style="width: 100%; height: 100%; line-height: 36px;">
	<div style="width: 181px; margin: 0 auto; height: 38px;">
		<label>
			<input type='radio' name='synMethod' value='normal' checked><span class="text">普通同步(保留表数据)</span>
		</label>
		<label>	
			<input type='radio' name='synMethod' value='force'><span class="text">强制同步(删除表,重新生成)</span>
		</label>
	</div>
	<SCRIPT type="text/javascript">
		function getSynChoice() {
			var synchoice;
			$("[name='synMethod']").each(function() {
				if ($(this).attr("checked")) {
					synchoice = $(this).val();
				}
			});
			return synchoice;
		}
	</SCRIPT>