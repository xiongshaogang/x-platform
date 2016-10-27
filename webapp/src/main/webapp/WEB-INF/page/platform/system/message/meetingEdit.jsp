<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
$(function(){
	var optFlag = '${param.optFlag}';
	if(optFlag == "update" || optFlag == "add"){
		var myButton = [{
			id : 'view',
			text : '预览',
			iconCls : 'awsm-icon-plus',
			operationCode : "messageManager_testConnect_other",
			handler : function() {
				var obj = new Object();
				obj.id=$("#id").val();
			    obj.messageType = "meeting";
			    obj.allUsers=$("#allUsers").val();
			    obj.name = $("#name").val();
			    obj.empIds =$("#empIds").val();
			    obj.empNames=$("#empNames").val();
			    obj.jobIds=$("#jobIds").val();
			    obj.jobNames=$("#jobNames").val();
			    obj.orgIds=$("#orgIds").val();
			    obj.title=$("#title").val();
			    obj.businesstime=$("#businesstime").val();
			    obj.businessPerson=$("#businessPerson").val();
			    obj.businessPlace=$("#businessPlace").val();
			    obj.content=innerEditor.getData();
			    obj.company=$("#company").val();
			    createwindow("消息预览","messageController.do?innerMsgView&data="+$.toJSON(obj) , 800, 550, null, {optFlag:'detail'});
			}
		},{
			id : 'saveMsg',
			text : '保存',
			iconCls : 'awsm-icon-save',
			operationCode : "messageManager_testConnect_other",
			handler : function() {
				var obj = new Object();
			    obj.messageType = "meeting";
			    obj.allUsers=$("#allUsers").val();
			    obj.sendType=$("#sendType").val();
			    obj.name = $("#name").val();
			    obj.empIds =$("#empIds").val();
			    obj.empNames=$("#empNames").val();
			    obj.jobIds=$("#jobIds").val();
			    obj.jobNames=$("#jobNames").val();
			    obj.orgIds=$("#orgIds").val();
			    obj.copiedIds=$("#copiedIds").val();
			    obj.copiedNames=$("#copiedNames").val();
			    obj.title=$("#title").val();
			    obj.businesstime=$("#businesstime").val();
			    obj.businessPerson=$("#businessPerson").val();
			    obj.businessPlace=$("#businessPlace").val();
			    obj.company=$("#company").val();
			    obj.needReply=$('input[name="needReply"]:checked').val();
			    obj.optFlag=$("#optFlag").val();
			    obj.id=$("#id").val();
			    getValues();
			    obj.content=$('#content').val();
			    $.ajax({
			    	url : "messageController.do?updateMsg",
					type : 'post',
					data : {
						data : $.toJSON(obj)
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							if(msg&&msg!=""){
								$("#meetingList").datagrid('reload');
								tip(msg);
							}
						}
					}
			    });
			}
		}];
	}else{
		var myButton = [{
				id : 'view',
				text : '预览',
				iconCls : 'awsm-icon-plus',
				operationCode : "messageManager_testConnect_other",
				handler : function() {
					var obj = new Object();
					obj.id=$("#id").val();
				    obj.messageType = "meeting";
				    obj.allUsers=$("#allUsers").val();
				    obj.name = $("#name").val();
				    obj.empIds =$("#empIds").val();
				    obj.empNames=$("#empNames").val();
				    obj.jobIds=$("#jobIds").val();
				    obj.jobNames=$("#jobNames").val();
				    obj.orgIds=$("#orgIds").val();
				    obj.title=$("#title").val();
				    obj.businesstime=$("#businesstime").val();
				    obj.businessPerson=$("#businessPerson").val();
				    obj.businessPlace=$("#businessPlace").val();
				    obj.content=innerEditor.getData();
				    obj.company=$("#company").val();
				    createwindow("消息预览","messageController.do?innerMsgView&data="+$.toJSON(obj) , 800, 550, null, {optFlag:'detail'});
				}
			}];
	}
	
	addButton(getD($("#formobj")),myButton,1);
});
</script>

<script type="text/javascript" src="plug-in/ckeditor/ckeditor_msg.js"></script>
  <t:formvalid formid="formobj" gridId="meetingList" action="messageController.do?sendMsg" beforeSubmit="getValues">
		<input id="id" name="id" type="hidden" value="${messageFrom.id }">
		<input id="messageType" name="messageType" type="hidden" value="meeting">
		<input id="optFlag" name="optFlag" type="hidden" value="${param.optFlag }">
		<table cellpadding="0" cellspacing="1" class="formtable" >
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>会议发自:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt"  value='${messageFrom.name}'>
				</td>
				<td class="td_title" nowrap>
					<label class="Validform_label">
						<span style="color:red">*</span>发致全体员工:
					</label>
				</td>
				<td class="value">
				    <t:comboBox name="allUsers" onSelect="userTypeChange(record,self)"  multiple="false" id="allUsers" data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]' value="${messageFrom.allUsers }" datatype="*"></t:comboBox>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>发送方式:
					</label>
				</td>
				<td class="value">
				    <t:comboBox name="sendType"  multiple="true" id="sendType" data='[{"id":"innerMessage","text":"站内信"},{"id":"sms","text":"短信"},{"id":"email","text":"邮件"}]' value="${messageFrom.sendType }" onChange="setType(self)" datatype="*"></t:comboBox>
				</td>
			</tr>
			<tr class="selectUser">
				<td class="td_title">
					<label class="Validform_label">
						发致员工:
					</label>
				</td>
				<td class="value">
				    <t:empSelect hiddenName="empIds"  hiddenValue="${messageFrom.empIds }" displayName="empNames" displayValue="${messageFrom.empNames }"></t:empSelect>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						发致岗位:
					</label>
				</td>
				<td class="value">
				    <t:jobSelect hiddenName="jobIds" displayName="jobNames" hiddenValue="${messageFrom.jobIds }" displayValue="${messageFrom.jobNames }"></t:jobSelect>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						发致机构:
					</label>
				</td>
				<td class="value">
				    <t:orgSelect id="orgIds" name="orgIds" value="" multiple="true" onlyLeafCheck="true" ></t:orgSelect>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>会议主题:
					</label>
				</td>
				<td class="value">
				    <input id="title" name="title" type="text" class="inputxt" datatype="*1-32" value='${messageFrom.title }'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>会议时间:
					</label>
				</td>
				<td class="value">
				    <t:datetimebox name="businesstime" id="businesstime" type="datetime" value="${business.businesstime }" datatype="*"></t:datetimebox>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						抄送人:
					</label>
				</td>
				<td class="value">
				    <t:empSelect hiddenName="copiedIds" displayName="copiedNames" hiddenValue="${messageFrom.copiedIds }" displayValue="${messageFrom.copiedNames }"></t:empSelect>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>与会人员:
					</label>
				</td>
				<td class="value">
				    <input id="businessPerson" name="businessPerson" datatype="*1-50" type="text" class="inputxt" value='${business.businessPerson}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>会议地点:
					</label>
				</td>
				<td class="value">
				    <input id="businessPlace" width="100%" name="businessPlace" datatype="*1-50" type="text" class="inputxt" value='${business.businessPlace}'>
				</td>
				<td class="td_title" >
					<label class="Validform_label">
						<span style="color:red">*</span>公司名称:
					</label>
				</td>
				<td class="value">
				    <%-- <c:choose>
				    <c:when test="${messageFrom.allUsers == '1' }"><t:comboBox name="company"  id="company"  dictCode="companyType" value="${business.companyId }" multiple="true"></t:comboBox></c:when>
				    <c:otherwise><t:comboBox name="company"  id="company"  dictCode="companyType" value="${business.companyId }" ></t:comboBox></c:otherwise>
				    </c:choose> --%>
				    <t:comboBox name="company"  id="company"  dictCode="companyType" value="${business.companyId }" datatype="*"></t:comboBox>
				</td>
			</tr>
			<tr>
			    <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>是否需回复:
					</label>
				</td>
				<td class="value">
				   <label class="Validform_label"> <input type="radio" name="needReply" value="1" <c:if test="${messageFrom.needReply == '1' }">checked="checked"</c:if>/></label>是
				   <label class="Validform_label"> <input type="radio" name="needReply" value="0" <c:if test="${messageFrom.needReply == '0' || empty messageFrom.needReply}">checked="checked"</c:if>/></label>否
 				</td>
 				<td class="td_title email" style="display: none;">
					<label class="Validform_label">
						<span style="color:red">*</span>邮件发件人:
					</label>
				</td>
				<td class="value email" style="display: none;">
				    <t:comboBox name="mailConfigId"  id="mailConfigId"  url="messageController.do?getEmail&value=${messageFrom.mailConfigId }" value="${messageFrom.mailConfigId }" ></t:comboBox>
				</td>
			</tr>
			<%-- <tr>
			    <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>附件:
					</label>
				</td>
				<td class="value" colspan="5">
				   <div id="meetfileList">
				   <c:forEach var="data" items="${dataVoList }" begin="0" end="1">
				   <span id="${data.id}"><a onclick="commonDownloadFile('${data.id}')" href="#">${data.name }</a>&nbsp;&nbsp;<a onclick="deleteFile('${data.id}')" href="#" <c:if test="${optFlag =='detail'}">style="display:none"</c:if>>删除</a><br/></span>
				   </c:forEach>
				   </div><c:if test="${optFlag !='detail'}"><a href="#" onclick="uploadMeeting()" class="awsm-icon-cloud-upload" style="color:blue;">添加附件</a></c:if>
 				</td>
			</tr> --%>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						会议内容:
					</label>
				</td>
				<td class="value" colspan="5">
					<textarea id="content" name="content" style="width:97%;" rows="4">${messageFrom.content }</textarea>
				</td>
			</tr>
		</table>
	</t:formvalid>
<script type="text/javascript">
    $(function(){
    	var orgIds = '${messageFrom.orgIds}';
    	$.parser.onComplete = function(){
    		if(orgIds !=''){
        		$("#orgIds").combotree('setValues','${messageFrom.orgIds}'.split(","));
        	}
        	if($("#sendType").val().indexOf("email") != -1){
        		$(".email").show();
        	}
    	   $.parser.onComplete=mainComplete;				
    	} 
    });

	function userTypeChange(record,self){
		var value=$(self).val();
		if(value=="1"){
			$(".selectUser").hide();
			/* $("#company").combobox("setValue",""); */
			$("#empIds").val("");
			$("#empNames").val("");
			$("#jobIds").val("");
			$("#jobNames").val("");
			$("#orgIds").val("");
		}else if(value=="0"){
			$(".selectUser").show();
		}
	}
	function setType(self){
		var val = $(self).val();
		if(val.indexOf("email") != -1){
			$(".email").show();
		}else{
			$(".email").hide();
			$("#mailConfigId").combobox("setValue","");
		}
	}
	if( CKEDITOR.instances['content'] ){
	    CKEDITOR.remove(CKEDITOR.instances['content']);
	}
	var innerEditor = ckeditor('content');
	
	function getValues(){
		$('#content').val(innerEditor.getData());
	}
	
	 function uploadMeeting() {
			commonPageUpload({
				businessKey : "${messageFrom.id }",
				businessType : "meeting",
				isNeedToType : false, 
				finishUploadCallback:"meetingUpload(allFiles)"
			});
		}
		function meetingUpload(result){
			for(var i=0;i<result.length;i++){
				var html = '<span id="'+result[i].id+'"><a onclick="commonDownloadFile(\''+result[i].id+'\')" href="#">'+result[i].name+'</a>&nbsp;&nbsp;<a onclick="deleteFile(\''+result[i].id+'\')" href="#">删除</a><br/></span>'
				$("#meetfileList").append(html);
			}
		}
</script>						