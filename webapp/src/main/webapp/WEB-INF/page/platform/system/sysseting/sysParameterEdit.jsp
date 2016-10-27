<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
	

		<t:formvalid formid="formobj" gridId="sysParameterList" action="sysParameterController.do?saveOrUpdate" tiptype="5">
		<input id="id" name="id" type="hidden" value="${sysParameterPage.id }">
		<table id="table_m" cellpadding="0" cellspacing="1" class="formtable">
		
					   <tr id="id_title">
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red">*</span>参数名称:
								</label>
							</td>
							<td class="value">
							    <input id="name" name="name" datatype="*1-50"  type="text" class="inputxt" value='${sysParameterPage.name}'>
							</td>
						</tr>
						<tr id="id_code">
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red"></span>参数编码:
								</label>
							</td>
							<td class="value">
									<input id="code" name="code" type="text" class="inputxt"  ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.system.sysseting.entity.SysParameterEntity" oldValue='${sysParameterPage.code}' value='${sysParameterPage.code}'>
							</td>
						</tr>
						<tr >
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red">*</span>参数值:
								</label>
							</td>
							<td class="value">
							    <input id="value" name="value" datatype="*"  type="text" class="inputxt" value='${sysParameterPage.value}'>
							</td>
						</tr>
						<tr>
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red">*</span>是否可修改:
								</label>
							</td>
							<td class="value">
								<t:comboBox name="updateFlag" id="updateFlag" datatype="*" value="${sysParameterPage.updateFlag}" data='[{"id":"Y","text":"是"},{"id":"N","text":"否"}]'></t:comboBox>
							</td>
						</tr>
						<tr>
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red">*</span>参数类型:
								</label>
							</td>
							<td class="value">
								<t:comboBox name="type"  dictCode="settingType" textname="typename" datatype="*" multiple="false" id="type" value="${sysParameterPage.type}"></t:comboBox>
							</td>
						</tr>
						
						<tr >
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red"></span>扩展字段1:
								</label>
							</td>
							<td class="value">
							    <input id="reserve1" name="reserve1"  type="text" class="inputxt" value='${sysParameterPage.reserve1}'>
							</td>
						</tr>
						
						<tr >
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red"></span>扩展字段2:
								</label>
							</td>
							<td class="value">
							    <input id="reserve2" name="reserve2"  type="text" class="inputxt" value='${sysParameterPage.reserve2}'>
							</td>
						</tr>
						
						<tr>
							<td class="td_title">
								<label class="Validform_label">
									描述:
								</label>
							</td>
							<td class="value">
								<textarea id="description" name="description" style="width: 96%;" rows="5" >${sysParameterPage.description}</textarea>
							</td>
						</tr>
						
					</table>
					</t:formvalid>

		
		
		

<script>
$(function(){
// 	$(".ucg-dialog-titletab").parent(".form_div").addClass("width-100");
//	$(".tabs-div-right").append("<div class=\"close-btn\" onclick=\"closeD($(this).closest('.window-body'));\"><i class=\"awsm-icon-remove bigger-120\"></i></div>");



});
</script>

  
  