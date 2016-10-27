<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/office/OfficeContorlFunctions.js"></script>
<script type="text/javascript">
$(function(){
	var orgIds = '${annunciate.orgIds}';
	$.parser.onComplete = function(){
		setType();
    	userTypeChange(null,$("#allUsers"));
		if(orgIds !=''){
    		$("#orgIds").combotree('setValues','${annunciate.orgIds}'.split(","));
    	}
	   $.parser.onComplete=mainComplete;				
	}
	
	setTask();
});

function setTask(){
	$("#v_S_pmApprove").val($('input[name="pmApprove"]:checked').val());
}
function uploadAnnunciate() {
	commonPageUpload({
		businessKey : "${annunciate.id }",
		businessType : "annunciate",
		isNeedToType : false, 
		//finishUploadCallback:"annUpload(allFiles)"
	});
}
/* function annUpload(result){
	for(var i=0;i<result.length;i++){
		var html = '<span id="'+result[i].id+'"><a onclick="common_downloadFile(\''+result[i].id+'\')" href="#">'+result[i].name+'</a>&nbsp;&nbsp;<a onclick="deleteFile(\''+result[i].id+'\')" href="#">删除</a><br/></span>'
		$("#annfileList").append(html);
	}
} */


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
function setType(){
	if($("#email").is(":checked")){
		$(".email").show();
	}else{
		$(".email").hide();
		$("#mailConfigId").combobox("setValue","");
	}
}

function intializePage(fileUrl)
{
	console.info(fileUrl);
	if (typeof(window.frames['office_iframe'].contentWindow) == "undefined") { 
		OFFICE_CONTROL_OBJ = window.frames['office_iframe'].document.getElementById("TANGER_OCX");
	}else{
		OFFICE_CONTROL_OBJ = window.frames['office_iframe'].contentWindow.document.getElementById("TANGER_OCX");
	}  
	NTKO_OCX_OpenDoc(fileUrl);
}

//保存word文件
function saveWord(){
	if (typeof(window.frames['office_iframe'].contentWindow) == "undefined") { 
		window.frames["office_iframe"].saveFileToUrl();
	}else{
		window.frames["office_iframe"].contentWindow.saveFileToUrl();
	}  
}

//替换word书签值
function replaceBookMark(){
	
	 $.ajax({
	    	url : "annunciateController.do?getData",
			type : 'post',
			data : {
				id : $("#id").val()
			},
			async:false,
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					replaceBookMarkSingle("from",d.attributes.from);
					replaceBookMarkSingle("name",d.attributes.name);
					replaceBookMarkSingle("company",d.attributes.company);
					replaceBookMarkSingle("title",d.attributes.title);
					replaceBookMarkSingle("wenhao",d.attributes.reference);
					replaceBookMarkSingle("attach",d.attributes.attach); 
					console.info("替换书签");
				}
			}
	    });
}

//把word中html内容保存至表中
function saveContent(){
	$.ajax({
     	url : "annunciateController.do?saveContent",
		type : 'post',
		data : {
			id : $("#id").val()
		},
		async:false,
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				console.info("saveContent");
				nextProcessOnly($("#taskId").val());
			}
		}
    });
}
function showAll(){
	OFFICE_CONTROL_OBJ.FullScreenMode=true;
}
</script>
  <t:formvalid formid="formobj" gridId="annunciateList" action="annunciateController.do?save" callback="saveWord();" afterSaveClose="false">
  		<input id="annunciateType" type="hidden"/>
        <input id="optFlag" type="hidden" name="optFlag" value="${param.optFlag}"/>
		<input id="id" name="id" type="hidden" value="${annunciate.id }">
		<input id="messageType" name="messageType" type="hidden" value="annunciate">
		<input id="moudelFile" name="moudelFile" type="hidden" value="annunciateTemplate.doc"/>
		
		<table cellpadding="0" cellspacing="1" class="formtable" >
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>通告发自:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt"  value='${annunciate.name}'>
				</td>
				<td class="td_title" nowrap>
					<label class="Validform_label">
						<span style="color:red">*</span>发致全体员工:
					</label>
				</td>
				<td class="value">
				    <t:comboBox name="allUsers" onSelect="userTypeChange(record,self)"  multiple="false" id="allUsers" data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]' value="${annunciate.allUsers }" datatype="*"></t:comboBox>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>发送方式:
					</label>
				</td>
				<td class="value">
					<input id="innerMessage" type="checkbox" name="sendType" value="innerMessage" <c:if test="${fn:contains(annunciate.sendType,'innerMessage')}">checked="checked"</c:if>>&nbsp;站内信
					<input id="sms" type="checkbox" name="sendType" value="sms" <c:if test="${fn:contains(annunciate.sendType,'sms')}">checked="checked"</c:if>>&nbsp;短信
					<input id="email" type="checkbox" name="sendType" value="email" onclick="setType()" <c:if test="${fn:contains(annunciate.sendType,'email')}">checked="checked"</c:if>>&nbsp;邮件
				</td>
			</tr>
			<tr class="selectUser">
				<td class="td_title">
					<label class="Validform_label">
						发致员工:
					</label>
				</td>
				<td class="value">
				    <t:empSelect hiddenName="empIds"  hiddenValue="${annunciate.empIds }" displayName="empNames" displayValue="${annunciate.empNames }"></t:empSelect>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						发致岗位:
					</label>
				</td>
				<td class="value">
				    <t:jobSelect hiddenName="jobIds" displayName="jobNames" hiddenValue="${annunciate.jobIds }" displayValue="${annunciate.jobNames }"></t:jobSelect>
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
						抄送人(邮件):
					</label>
				</td>
				<td class="value">
				    <t:empSelect hiddenName="copiedIds" displayName="copiedNames" hiddenValue="${annunciate.copiedIds }" displayValue="${annunciate.copiedNames }"></t:empSelect>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>主题:
					</label>
				</td>
				<td class="value">
				    <input id="title" name="title" type="text" class="inputxt" datatype="*" value='${annunciate.title }'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>公司名称:
					</label>
				</td>
				<td class="value">
				    <t:comboBox name="company"  id="company"  dictCode="companyType" value="${annunciate.companyId }" datatype="*"></t:comboBox>
				</td>
			</tr>
			<tr>
			     <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>是否需回复:
					</label>
				</td>
				<td class="value">
				   <label class="Validform_label"> <input type="radio" id="needReply" name="needReply" value="1" <c:if test="${annunciate.needReply == '1' }">checked="checked"</c:if>/></label>是
				   <label class="Validform_label"> <input type="radio" id="needReply" name="needReply" value="0" <c:if test="${annunciate.needReply == '0' || empty annunciate.needReply}">checked="checked"</c:if>/></label>否
 				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>是否需主管审核:
					</label>
				</td>
				<td class="value">
				   <label class="Validform_label"> <input type="radio" id="pmApprove" name="pmApprove" value="1" onclick="setTask()" <c:if test="${annunciate.pmApprove == '1'  }">checked="checked"</c:if>/></label>是
				   <label class="Validform_label"> <input type="radio" id="pmApprove" name="pmApprove" value="0" onclick="setTask()" <c:if test="${annunciate.pmApprove == '0' || empty  annunciate.pmApprove}">checked="checked"</c:if>/></label>否
 				</td>
 				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>紧急程度:
					</label>
				</td>
				<td class="value">
				   <label class="Validform_label"> <input type="radio" id="emergency" name="emergency" value="0" <c:if test="${annunciate.emergency == '0' || empty  annunciate.emergency}">checked="checked"</c:if>/></label>一般
				   <label class="Validform_label"> <input type="radio" id="emergency" name="emergency" value="1" <c:if test="${annunciate.emergency == '1'  }">checked="checked"</c:if>/></label>紧急
				   <label class="Validform_label"> <input type="radio" id="emergency" name="emergency" value="2" <c:if test="${annunciate.emergency == '2'  }">checked="checked"</c:if>/></label>非常紧急
 				</td>
			</tr>
			<tr>
			<td class="td_title email" style="display: none;">
				<label class="Validform_label">
					<span style="color:red">*</span>邮件发件人:
				</label>
			</td>
			<td class="value email" style="display: none;" colspan="5">
			    <t:comboBox name="mailConfigId"  id="mailConfigId"  url="messageController.do?getEmail&value=${annunciate.mailConfigId }" value="${annunciate.mailConfigId }" ></t:comboBox>
			</td>
			</tr>
			<tr>
			    <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>附件:
					</label>
				</td>
				<td class="value" colspan="5">
				   <%-- <div id="annfileList">
				   <c:forEach var="data" items="${dataVoList }" begin="0" end="1">
				   <span id="${data.id}"><a onclick="common_downloadFile('${data.id}')" href="#">${data.name }</a>&nbsp;&nbsp;<a onclick="deleteFile('${data.id}')" href="#" <c:if test="${optFlag =='detail'}">style="display:none"</c:if>>删除</a><br/></span>
				   </c:forEach>
				   </div><c:if test="${optFlag !='detail'}"><a href="#" onclick="uploadAnnunciate()" class="awsm-icon-cloud-upload" style="color:blue;">添加附件</a></c:if> --%>
				   <a href="#" onclick="uploadAnnunciate()" class="awsm-icon-cloud-upload" style="color:blue;"><c:if test="${optFlag !='detail'}">添加附件</c:if><c:if test="${optFlag =='detail'}">查看附件</c:if></a>
 				</td>
			</tr>
			<tr>
			<td class="td_title">
            <label class="Validform_label">
			    <span style="color:red">*</span>内容:
			</label>
            </td>
			<td colspan="5" >
			<div>
			&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showAll()">填写正文</a>
			<iframe src="webpage/office/officeLoad.jsp"  style="width: 0;height: 0;padding:0px;border:0px;" id="office_iframe" name="office_iframe" >
			</iframe>
			</div>
			</td>
			</tr>
		</table>
	</t:formvalid>
