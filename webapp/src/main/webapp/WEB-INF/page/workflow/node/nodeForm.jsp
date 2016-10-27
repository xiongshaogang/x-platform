<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style type="text/css">
	input.checkbox_vertical {
		vertical-align: -2px;
		margin-right: 5px;
	}
	ul li {list-style:inside circle;list-style-type:none ;font-weight: normal; } 
	td{
		border:0
	}
	
	
</style>
<div class="easyui-layout"   fit="true">
	<div region="center"  border="false" style="padding: 5px;">
	<div class="easyui-panel" style="padding:5px;" fit="true" id="nodeFormListPanel">
		<form action="nodeSetController.do?saveNodeSet" method="post" id="nodeSetForm">	
			<input type="hidden" id="defId" name="defId" value="${bpmDefinition.id}"/>
			<div style="width:99%;margin:0 auto;">
				<t:collapseTitle id="div1" title="全局表单" style="">
					<table  cellpadding="0" cellspacing="1" class="formtable">
						<tr>
							<td class="td_title">
								<label class="Validform_label">
									节点表单类型:
								</label>
							</td>
							<td class="value">
							    <t:comboBox onChange="changeGlobalFormType(newValue,oldValue)" name="globalFormType" id="globalFormType" value="${globalForm.formType}" data='[{"id":"-1","text":"请选择"},{"id":"0","text":"表单实体"},{"id":"1","text":"表单路径"}]'></t:comboBox>
							</td>
							<td class="td_title formKeyGlobal">
								<label class="Validform_label">
									表单实体:
								</label>
							</td>
							<td class="value formKeyGlobal">
							    <t:commonSelect multiples="false"  displayName="defaultFormName" displayValue="${globalForm.formName}" hiddenName="defaultFormId" hiddenValue="${globalForm.formId}" url="flowFormController.do?datagrid@@flag=zujian"  >
							    	<t:csField title="主键" width="20" field="id" hidden="false" backField="defaultFormId"></t:csField>
							    	<t:csField title="表单编码" width="20" field="code" backField="defaultFormKey"></t:csField>
							    	<t:csField title="表单名称" width="20" field="name" backField="defaultFormName">
							    	</t:csField><t:csField title="表单url" width="20" field="url"></t:csField>
							    </t:commonSelect>
								<input id="defaultFormKey" type="hidden" name="defaultFormKey">
							</td>
							<td class="td_title formUrlGlobal">
								<label class="Validform_label">
									表单路径:
								</label>
							</td>
							<td class="value formUrlGlobal">
							    <input type="text" id="formUrlGlobal" name="formUrlGlobal" value="${globalForm.formUrl }" class="inputxt"/>
							    <input type="hidden" name="formIdGlobal" value="${globalForm.id }"/>
							</td>
						</tr>
						<tr>
							<td class="td_title">
								<label class="Validform_label">
									实例表单类型:
								</label>
							</td>
							<td class="value">
							    <t:comboBox width="192" onChange="changeBpmFormType(newValue,oldValue)" name="bpmFormType" id="bpmFormType" value="${bpmForm.formType}" data='[{"id":"-1","text":"请选择"},{"id":"0","text":"表单实体"},{"id":"1","text":"表单路径"}]'></t:comboBox>
							</td>
							<td class="td_title formKeyBpm">
								<label class="Validform_label">
									表单实体:
								</label>
							</td>
							<td class="value formKeyBpm">
							    <t:commonSelect multiples="false"  displayName="bpmFormName" displayValue="${bpmForm.formName}" hiddenName="bpmFormId" hiddenValue="${bpmForm.formId}" url="flowFormController.do?datagrid@@flag=zujian"  >
							    	<t:csField title="主键" width="20" field="id" hidden="false" backField="bpmFormId"></t:csField>
							    	<t:csField title="表单编码" width="20" field="code" backField="bpmFormKey"></t:csField>
							    	<t:csField title="表单名称" width="20" field="name" backField="bpmFormName">
							    	</t:csField><t:csField title="表单url" width="20" field="url"></t:csField>
							    </t:commonSelect>
								<input id="bpmFormKey" type="hidden" name="bpmFormKey">
								<input type="hidden" name="formIdBpm" value="${bpmForm.id }"/>
							</td>
							<td class="td_title formUrlBpm">
								<label class="Validform_label">
									表单路径:
								</label>
							</td>
							<td class="value formUrlBpm">
							    <input type="text" id="bpmFormUrl" name="bpmFormUrl" value="${bpmForm.formUrl }" class="inputxt"/>
							</td>
						</tr>
					</table>
				</t:collapseTitle>
				<t:collapseTitle id="div3" title="节点表单" style="">
					<table class="datagrid-body" cellspacing="0" width="100%" cellpadding="0">
						<tr style="border-bottom: 1px solid #ddd; height="35px">
							<td class="datagrid-header" width="15%" height="30px" style="padding-left:10px">节点名</td>
							<td class="datagrid-header" width="15%" height="30px" style="padding-top:6px">
								<label><input type="checkbox" class="o_checkbox" id="o_checkbox">跳转类型</label>
							</td>
							<td class="datagrid-header" width="15%" height="30px" style="padding-top:6px">
								<label title="隐藏执行路径"><input type="checkbox" class="o_checkbox" id="ckHidenPath">回退规则</label>
							</td>
							<td class="datagrid-header" width="15%" height="30px" style="padding-top:6px">
								<label title="隐藏表单意见"><input type="checkbox" class="o_checkbox" id="ckHidenOption">隐藏意见</label>
							</td>
							<td class="datagrid-header" width="30%" height="30px" style="padding-top:2px">表单</td>
						</tr>
						<c:forEach items="${bpmNodeSetList}" var="item" varStatus="status">
							<tr  <c:if test="${status.index%2=='1' }">id="datagrid-row-r1-2-3" class="datagrid-row datagrid-row-alt"</c:if>>
								<td style="padding-left:10px;">
									${item.nodeName}
									<input name="nodeName_${item.nodeId}" type="hidden" value="${item.nodeName}"/>
								</td>
								<td nowrap="nowrap">
									<input type="checkbox" class="jumpType o_checkbox" name="jumpType_${item.nodeId}" value="1"  <c:if test="${fn:indexOf(item.jumpType,'1')!=-1}">checked="checked"</c:if> />正常跳转<br> 
									<input type="checkbox" class="jumpType o_checkbox" name="jumpType_${item.nodeId}" value="2" <c:if test="${fn:indexOf(item.jumpType,'2')!=-1}">checked="checked"</c:if> />选择路径跳转<br>  
									<input type="checkbox" class="jumpType o_checkbox" name="jumpType_${item.nodeId}" value="3" <c:if test="${fn:indexOf(item.jumpType,'3')!=-1}">checked="checked"</c:if> />自由回退
								</td>
								<td>
									<input type="checkbox" class="hidePath o_checkbox"  name="isHidePath_${item.nodeId}" value="1" <c:if test="${fn:indexOf(item.backType,'1')!=-1}">checked="checked"</c:if> />回退<br> 
									<input type="checkbox" class="hidePath o_checkbox"  name="isHidePath_${item.nodeId}" value="2" <c:if test="${fn:indexOf(item.backType,'2')!=-1}">checked="checked"</c:if> />反馈
								</td>
								<td>
									<input type="checkbox" class="hideOption o_checkbox" id="isHideOption_${item.nodeId}" name="isHideOption_${item.nodeId}" value="1" <c:if test="${item.isHideOption==1}">checked="checked"</c:if> />
								</td>
								<td>
									<table class="table-detail" width="100%">
										<tr>
											<td nowrap="nowrap" class="head">表单类型:</td>
											<td style="padding:4px 0 2px 0">
												<input name="nodeId" type ="hidden" value="${item.nodeId}"/>
												<t:comboBox onSelect="formTypeChange(record,self)" name="formType"  id="formType_${item.nodeId}" value="${item.formType}" data='[{"id":"-1","text":"请选择"},{"id":"0","text":"在线表单"},{"id":"1","text":"URL表单"}]'></t:comboBox>
											</td>
										</tr>
									</table>
									<div style="margin:2px 0 0 0" id="formBox_${item.nodeId}" name="nodeForm" <c:if test="${item.formType==-1}">style="display:none"</c:if>>
										<table class="table-detail table-noborder" >
											<tr class="form_${item.nodeId}" <c:if test="${item!=null && item.formType!=0 }">style="display: none" </c:if>>
												<td>表单实体:</td>
												<td style="padding-left:8px;">
													<t:commonSelect multiples="false" idOrName="id" displayId="formName_${item.nodeId}" displayName="formName" hiddenId="formKey_${item.nodeId}" hiddenName="formKey" url="flowFormController.do?datagrid@@flag=zujian" displayValue="${item.formName}" hiddenValue="${item.formKey}">
														<t:csField title="主键" width="20" field="id" hidden="false" backField="formId_${item.nodeId}"></t:csField>
														<t:csField title="表单编码" width="20" field="code" backField="formKey_${item.nodeId}"></t:csField>
														<t:csField title="表单名称" width="20" field="name" backField="formName_${item.nodeId}"></t:csField>
														<t:csField title="表单url" width="20" field="url"></t:csField>
													</t:commonSelect>
													<input id="formId_${item.nodeId}" type="hidden" name="formId" value="${item.formId}">
												</td>
											</tr>
										</table>
										<table class="table-detail table-noborder" >
											<tr <c:if test="${item!=null && item.formType!=1 }">style="display: none" </c:if> class="url_${item.nodeId}">
											<td>表单路径:</td>
												<td style="padding-left:8px;"><input type="text" id="formUrl_${item.nodeId}" name="formUrl" value="${item.formUrl}" class="inputxt" size="30"/></td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</c:forEach>
					</table>
				</t:collapseTitle>
			</div>
		</form>
	</div>
	</div>
</div>
<script type="text/javascript">
	var actDefId="${bpmDefinition.actId}";
	
	function changeBpmFormType(newValue,oldValue){
		if(newValue==-1){
			$(".formUrlBpm").hide();
			$(".formKeyBpm").hide();
		}else{
			if(newValue==0){
				$(".formUrlBpm").hide();
				$(".formKeyBpm").show();
			}
			else if(newValue==1){
				$(".formUrlBpm").show();
				$(".formKeyBpm").hide();
			}
		}
	}
	
	function formTypeChange(record,self){
		var formTypeId=$(self).attr("id");
		var nodeId=formTypeId.split("_")[1];
		var value=$(self).val();
		if(value==-1){
			$("#formBox_"+nodeId).hide();
		}else{
			$("#formBox_"+nodeId).show();
			if(value==0){
				$(".form_"+nodeId).show();
				$(".url_"+nodeId).hide();
			}
			else if(value==1){
				$(".form_"+nodeId).hide();
				$(".url_"+nodeId).show();
			}else{
				$(".form_"+nodeId).hide();
				$(".url_"+nodeId).hide();
			}
		}
	}
	
	function changeGlobalFormType(newValue,oldValue){
		if(newValue==-1){
			$(".globalHandler").hide();
			$(".formUrlGlobal").hide();
			$(".formKeyGlobal").hide();
		}else{
			if(newValue==0){
				$(".globalHandler").show();
				$(".formUrlGlobal").hide();
				$(".formKeyGlobal").show();
			}
			else if(newValue==1){
				$(".globalHandler").show();
				$(".formUrlGlobal").show();
				$(".formKeyGlobal").hide();
			}
		}
	}
	
	$(function(){
		$("#nodeFormListPanel").panel({
			title : "节点表单设置",
			tools : [ {
				iconCls : 'icon-save',
				handler : function() {
					save();
				}
			} ]
		});
		$('#ckJumpType').on('click',function(){
			$('.jumpType').attr('checked',this.checked);
		});
		
		$('#ckHidenOption').on('click',function(){
			var checked=this.checked;
			$('.hideOption').attr('checked',checked);
		});
		
		$('#ckHidenPath').on('click',function(){
			var checked=this.checked;
			$('.hidePath').attr('checked',checked);
		});
		
		
		//是否默认选中  隐藏执行路径
		var isNew = '${isNew}';
		if(isNew=='yes'){             //没有绑定表单时都要默认选中  隐藏执行路径
			$('#ckHidenPath').attr('checked',true);
			var checked=this.checked;
			$('.hidePath').attr('checked',checked);
		}
		
		//全局表单的初始化显示与隐藏
		if(${globalForm!=null}){//全局表单不为空
			if(${globalForm.formType==0}){//全局表单不为空且类型为表单实体
				$(".formUrlGlobal").hide();
			}else if(${globalForm.formType==1}){//全局表单不为空且类型为表单url
				$(".formKeyGlobal").hide();
			}
		}else{
			$(".formUrlGlobal").hide();
			/* $(".formKeyGlobal").hide(); */
		}
		
		//业务综合表单的初始化显示与隐藏
		if(${bpmForm!=null}){//全局表单不为空
			if(${bpmForm.formType==0}){//全局表单不为空且类型为表单实体
				$(".formUrlBpm").hide();
			}else if(${bpmForm.formType==1}){//全局表单不为空且类型为表单url
				$(".formKeyBpm").hide();
			}
		}else{
			$(".formUrlBpm").hide();
			/* $(".formKeyBpm").hide(); */
		}
		
	});
	
	function save(){
		var isEmpty=isEmptyForm();
		if(isEmpty){
			alert("表单设置为空！");
			return;
		}
		var formval = $("#nodeSetForm").serialize();
		$.ajax({
 			cache : true,
 			type : 'POST',
 			url : "nodeSetController.do?saveNodeSet",// 请求的action路径
 			data : $("#nodeSetForm").serialize(),
 			async : false,
 			success : function(data) {
 				var d = $.parseJSON(data);
 				if (d.success) {
 					var msg = d.msg;
 					tip(msg);
 				}
 			}
 		});
	}
	
	function isEmptyForm(){
		var isEmpty=true;
		var globalType=$('#globalFormType').combobox('getValue');
		var bpmType=$('#bpmFormType').combobox('getValue');
		if(globalType=="0"){
			isEmpty=false;
		}else if(globalType=="1"){
			isEmpty=false;
		}
		if(bpmType=="0"){
			isEmpty=false;
		}else if(bpmType=="1"){
			isEmpty=false;
		}
		$("div[name='nodeForm']").each(function(){
			var formKeyObj=$(this).find('input[name="formKey"]');
			var formKey=(formKeyObj!=undefined)?formKeyObj.val():0;
			var formUrlObj=$(this).find('input[name="formUrl"]');
			var formUrl=(formUrlObj!=undefined)?formUrlObj.val():"";
			if(formKey!=0||formUrl!=""){
				isEmpty=false;
			}
		});
		return isEmpty;
	}
</script>