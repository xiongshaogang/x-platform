<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>表单校验规则</title>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="formRuleController.do?saveOrUpdate" tiptype="1" gridId="formRuleList" refresh="true">
					<input id="id" name="id" type="hidden" value="${formRulePage.id }">
		<table style="width: 100%;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								名称:
							</label>
						</td>
						<td class="value">
						    <input id="name" name="name" type="text" style="width: 150px" class="inputxt" value='${formRulePage.name}'>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								规则:
							</label>
						</td>
						<td class="value">
						    <input id="regulation" name="regulation" type="text" style="width: 150px" class="inputxt" value='${formRulePage.regulation}'>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								提示信息:
							</label>
						</td>
						<td class="value">
						    <input id="tipInfo" name="tipInfo" type="text" style="width: 150px" class="inputxt" value='${formRulePage.tipInfo}'>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								备注:
							</label>
						</td>
						<td class="value">
						    <input id="memo" name="memo" type="text" style="width: 150px" class="inputxt" value='${formRulePage.memo}'>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
