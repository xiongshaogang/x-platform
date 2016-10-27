<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<script type="text/javascript">
  $(function(){
		   $("#jobName").val($("#pjobName").val());
  });
 </script>
 <style type="text/css">
 	#triggerForm select {
 		width:92px !important;
 		border: 1px solid #D5D5D5 !important;
		height:22px;
 	}
</style>
 <t:formvalid beforeSubmit="getPlan" formid="formobj" gridId="triggerList" action="triggerController.do?saveTrigger">
	<input id="jobName" name="jobName" type="hidden" value="${jobName}" />
	<table id="triggerForm" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title" w><label class="Validform_label">计划名称:</label></td>
			<td class="value">
				<input type="text" id="name" name="name" class="inputxt" size="40"/>
				<input id="planJson" name="planJson" type="hidden" />	
			</td>
			<td class="value">&nbsp;</td>
			<td class="value">&nbsp;</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"><input style="text-align:right" type="radio" value="1"  name="rdoTimeType" />一次:</label></td>
			<td class="value" colspan="3">
				<t:datetimebox name="once" id="once" type="datetime"></t:datetimebox>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"><input type="radio" checked="checked" value="2" name="rdoTimeType" />每隔:</label>  </td>
			<td class="value" colspan="3">
				<select id="selEveryDay">
					<option value="1">1分钟</option>
         			<option value="5">5分钟</option>
         			<option value="10">10分钟</option>
         			<option value="15">15分钟</option>
         			<option value="30">30分钟</option>
         			<option value="60">1小时</option>
         		</select>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"><input type="radio"  value="3" name="rdoTimeType" />每天:</label> </td>
			<td class="value" colspan="3">
				
				<select id="txtDayHour">
					<c:forEach begin="0" end="23" step="1" var="tmp">
						<option value="${tmp }">${tmp }时</option>
					</c:forEach>
				</select>
		
				<select id="txtDayMinute">
					<c:forEach begin="0" end="55" step="5" var="tmp">
								<option value="${tmp }">${tmp }分</option>
					</c:forEach>
					<option value="59">59分</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="td_title" valign="top"><label class="Validform_label"><input type="radio" value="4" name="rdoTimeType" />每周:</label> </td>
			<td class="value" colspan="3">
			     <input type="checkbox" name="chkWeek" value="MON"/>星期一
              		 <input type="checkbox" name="chkWeek" value="TUE"/>星期二
              		 <input type="checkbox" name="chkWeek" value="WED"/>星期三
              		 <input type="checkbox" name="chkWeek" value="THU"/>星期四
              		 <input type="checkbox" name="chkWeek" value="FRI"/>星期五
              		 <input type="checkbox" name="chkWeek" value="SAT"/>星期六
              		 <input type="checkbox" name="chkWeek" value="SUN"/>星期日	<br/>
            				 <select id="txtWeekHour">
              		 	<c:forEach begin="0" end="23" step="1" var="tmp">
						<option value="${tmp }">${tmp }时</option>
					</c:forEach>
				</select>
				<select id="txtWeekMinute">
					<c:forEach begin="0" end="55" step="5" var="tmp">
						<option value="${tmp }">${tmp }分</option>
					</c:forEach>
					<option value="59">59分</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="td_title" valign="top" ><label  class="Validform_label"><input type="radio" value="5" name="rdoTimeType" />每月:</label></td>
			<td class="value" colspan="3">
				<c:forEach begin="1" end="31" var="mon">
					 <input type="checkbox" name="chkMon" value="${mon}"/>${mon}
				</c:forEach>
				     
             			 <input type="checkbox" name="chkMon" value="L"/>最后一天<br/>
            		 
              		 <select id="txtMonHour">
              		 	<c:forEach begin="0" end="23" step="1" var="tmp">
						<option value="${tmp }">${tmp }时</option>
					</c:forEach>
				</select>
				<select id="txtMonMinute">
					<c:forEach begin="0" end="55" step="5" var="tmp">
						<option value="${tmp }">${tmp }分</option>
					</c:forEach>
					<option value="59">59分</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"><input type="radio" value="6" name="rdoTimeType" />Cron表达式:</label></td>
			<td class="value" colspan="3"><input type="text" id="txtCronExpression" class="inputxt" name="txtCronExpression" /></td>
		</tr>
	</table>
</t:formvalid>

<script type="text/javascript">
	function getPlan() {
		var c = $("input[name=rdoTimeType]:checked").val();
		var f = "";
		switch (c) {
		  case "1":
			f = $("once").datetimebox("getValue");
			f = "{\"type\":1,\"timeInterval\":\"" + f + "\"}";
			break;
		  case "2":
			f = $("#selEveryDay").val();
			f = "{\"type\":2,\"timeInterval\":\"" + f + "\"}";
			break;
		  case "3":
			var e = $("#txtDayHour").val();
			var a = $("#txtDayMinute").val();
			f = e + ":" + a;
			f = "{\"type\":3,\"timeInterval\":\"" + f + "\"}";
			break;
		  case "4":
			f = getChkValue("chkWeek");
			var e = $("#txtWeekHour").val();
			var a = $("#txtWeekMinute").val();
			f += "|" + e + ":" + a;
			f = "{\"type\":4,\"timeInterval\":\"" + f + "\"}";
			break;
		  case "5":
			f = getChkValue("chkMon");
			var e = $("#txtMonHour").val();
			var a = $("#txtMonMinute").val();
			f += "|" + e + ":" + a;
			f = "{\"type\":5,\"timeInterval\":\"" + f + "\"}";
			break;
		  case "6":
			f += $("#txtCronExpression").val();
			f = "{\"type\":6,\"timeInterval\":\"" + f + "\"}";
			break;
		}
		$("#planJson").val(f);
	}
	
	function getChkValue(a) {
		var b = "";
		$("input[type=\"checkbox\"][name=" + a + "]").each(function () {
			if ($(this).attr("checked")) {
				b += $(this).val() + ",";
			}
		});
		if (b != "") {
			b = b.substring(0, b.length - 1);
		}
		return b;
	}
</script>
