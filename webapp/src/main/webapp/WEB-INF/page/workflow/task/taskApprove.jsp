<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>

</style>
<script type="text/javascript">
	$(function() {

	});
</script>
<div>
	<t:tabs id="taskApproveTabs" fit="false" height="445" border="false" hBorderBottom="true" hBorderLeft="false"
		hBorderRight="false" hBorderTop="false" leftDiv="true" leftDivWidth="70" leftDivTitle="审批" rightDiv="true"
		rightDivWidth="200" closeBtn="true">
		<div title="审批事项">
			<form id="approveForm">
				<input type="hidden" name="v_I_date" value="7">
				<div id="approveContentDiv" class="approveContentDiv">
					<table cellpadding="0" cellspacing="1" class="approveContentTable">
						<tr>
							<td class="approve_table_title" style="width: 50%">重点审核/批事项</td>
							<td class="approve_table_title" style="width: 50%">意见</td>
						</tr>
						<tr>
							<td class="approve_table_content">
								<div class="approve_table_content_div">
									<c:forEach var="flowFormItem" items="${flowFormItemList}">
										<div class="matterDiv">
											<span class="matterSign red">■</span><span class="matterName">${flowFormItem.name}</span><span class="matterCheck"><input
												class="matterCheckbox" type="checkbox" /></span>
										</div>
									</c:forEach>
								</div>
							</td>
							<td class="approve_table_content"><textarea class="approve_table_opnion" id="voteContent" name="voteContent">${opinion}</textarea></td>
						</tr>
						<tr>
							<td colspan="2" class="approve_table_pt">
								<div>
									<span class="approve_conclusion"> 
										结论： <t:comboBox id="conclusionApprove" name="conclusionApprove" data="${conclusionData}"></t:comboBox>
									</span> <input name=v_S_a/>
								</div><!-- 删除空白，避免50%宽度换行
								--><div>
									<span class="approve_person">审核/批人：${curUser.name}</span> 
									<span class="approve_time pull-right"> 
										时间： <fmt:formatDate value="${nowDate}" pattern="yyyy-MM-dd HH:mm" />
									</span>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</t:tabs>
</div>
