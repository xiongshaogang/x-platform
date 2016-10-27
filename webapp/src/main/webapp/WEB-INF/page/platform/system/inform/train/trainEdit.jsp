<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/office/OfficeContorlFunctions.js"></script>
<script type="text/javascript">
var flag = "";
$(function(){
		var orgIds = '${train.orgIds}';
		$.parser.onComplete = function(){
			setType();
	    	userTypeChange(null,$("#allUsers"));
			if(orgIds !=''){
	    		$("#orgIds").combotree('setValues','${train.orgIds}'.split(","));
	    	}
		   $.parser.onComplete=mainComplete;				
		} 
		if('${param.optFlag}' != 'detail'){
		var myButton = [{
			id : 'saveMsg',
			text : '保存',
			iconCls : 'awsm-icon-save',
			operationCode : "messageManager_testConnect_other",
			handler : function() {
				var obj = new Object();
			    obj.messageType = "train";
			    obj.mailConfigId=$("#mailConfigId").val();
			    obj.allUsers=$("#allUsers").val();
			    var sendType = ""
			    $("input[name='sendType']:checked").each(function(){
			    	sendType += $(this).val()+","; 
			    });
			    if(sendType !=""){
			    	sendType = sendType.substring(0,sendType.length-1);
			    }
			    obj.sendType=sendType;
			    obj.empIds =$("#empIds").val();
			    obj.empNames=$("#empNames").val();
			    obj.jobIds=$("#jobIds").val();
			    obj.jobNames=$("#jobNames").val();
			    obj.orgIds=$("#orgIds").val();
			    obj.copiedIds=$("#copiedIds").val();
			    obj.copiedNames=$("#copiedNames").val();
			    obj.needReply=$('input[name="needReply"]:checked').val();
			    obj.name=$("#name").val();
			    obj.title=$("#title").val();
			    obj.trainTime=$("#trainTime").val();
			    obj.trainPerson=$("#trainPerson").val();
			    obj.trainPlace=$("#trainPlace").val();
			    obj.trainObj=$("#trainObj").val();
			    obj.company=$("#company").val();
			    obj.content=$("#content").val();
			    obj.id=$("#id").val();
			    $.ajax({
			    	url : "trainController.do?save",
					type : 'post',
					data : {
						data : $.toJSON(obj),
						flag : "save"
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							if(msg&&msg!=""){
								$("#trainList").datagrid('reload');
								tip(msg);
							}
							saveWord();//保存文件
						}
					}
			    });
			}
		}];
	
	addButton(getD($("#formobj")),myButton,1);
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
function setType(){
	if($("#email").is(":checked")){
		$(".email").show();
	}else{
		$(".email").hide();
		$("#mailConfigId").combobox("setValue","");
	}
}

//保存word文件
function saveWord(obj){
	flag = obj;
	if (typeof(window.frames['office_iframe'].contentWindow) == "undefined") { 
		window.frames["office_iframe"].saveFileToUrl();
	}else{
		window.frames["office_iframe"].contentWindow.saveFileToUrl();
	}  
}

//替换word书签值
function replaceBookMark(){
	 $.ajax({
	    	url : "trainController.do?getData",
			type : 'post',
			data : {
				id : $("#id").val()
			},
			async:false,
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					OFFICE_CONTROL_OBJ.SetReadOnly(false);
					replaceBookMarkSingle("from",d.attributes.from);
					replaceBookMarkSingle("name",d.attributes.name);
					replaceBookMarkSingle("now_time",d.attributes.now_time);
					replaceBookMarkSingle("company",d.attributes.company);
					replaceBookMarkSingle("mdate",d.attributes.mdate);
					replaceBookMarkSingle("mtime",d.attributes.mtime);
					replaceBookMarkSingle("place",d.attributes.place);
					replaceBookMarkSingle("person",d.attributes.person);
					replaceBookMarkSingle("title",d.attributes.title);
					replaceBookMarkSingle("obj",d.attributes.obj);
					OFFICE_CONTROL_OBJ.SetReadOnly(true);
				}
			}
	    });
}

//把word中html内容保存至表中
function saveContent(){
	console.info("saveContent");
	$.ajax({
    	url : "trainController.do?saveContent",
		type : 'post',
		data : {
			id : $("#id").val()
		},
		async:false,
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				if(flag != null){
					console.info("发送");
					sendMsg();
					closeD(getD($("#trainType")));
				}
			}
		}
    });
}

//发送消息
function sendMsg(){
	$.ajax({
    	url : "trainController.do?send",
		type : 'post',
		data : {
			id : $("#id").val()
		},
		async:false,
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				console.info(d.msg);
			}
		}
    });
}

function bfsub(){
	if($("#allUsers").val() == '0' && $("#empIds").val() == '' && $("#jobIds").val() == '' && $("#orgIds").val() == ''){
		$.messager.alert("提示信息", "发致员工、岗位、机构至少填写一个");
		return false;
	}
    var sendType = ""
    $("input[name='sendType']:checked").each(function(){
    	sendType += $(this).val()+","; 
    });
    if(sendType !=""){
    	sendType = sendType.substring(0,sendType.length-1);
    }
    if(sendType == ""){
    	$.messager.alert("提示信息", "请选择发送方式");
		return false;
    }
    if(sendType.indexOf("email") != -1 && $("#mailConfigId").val() == ''){
    	$.messager.alert("提示信息", "请选择发送邮件账号");
		return false;
    }
}
</script>

  <t:formvalid formid="formobj" gridId="trainList" action="trainController.do?save" callback="saveWord('111');" afterSaveClose="false" beforeSubmit="bfsub();">
         <input id="trainType" type="hidden"/>
        <input id="optFlag" type="hidden" name="optFlag" value="${param.optFlag}"/>
		<input id="id" name="id" type="hidden" value="${train.id }">
		<input id="messageType" name="messageType" type="hidden" value="train">
		<input id="moudelFile" name="moudelFile" type="hidden" value="trainTemplate.doc"/>
		<table cellpadding="0" cellspacing="1" class="formtable" style="margin-right: 35px;">
			<tr>
			    <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>培训发自:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt"  value='${train.name}'>
				</td>
				<td class="td_title" nowrap>
					<label class="Validform_label">
						<span style="color:red">*</span>发致全体员工:
					</label>
				</td>
				<td class="value">
				    <t:comboBox name="allUsers" onSelect="userTypeChange(record,self)"  multiple="false" id="allUsers" data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]' value="${train.allUsers }" datatype="*"></t:comboBox>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>发送方式:
					</label>
				</td>
				<td class="value">
					<input id="innerMessage" type="checkbox" name="sendType" value="innerMessage" <c:if test="${fn:contains(train.sendType,'innerMessage')}">checked="checked"</c:if>>&nbsp;站内信
					<input id="sms" type="checkbox" name="sendType" value="sms" <c:if test="${fn:contains(train.sendType,'sms')}">checked="checked"</c:if>>&nbsp;短信
					<input id="email" type="checkbox" name="sendType" value="email" onclick="setType()" <c:if test="${fn:contains(train.sendType,'email')}">checked="checked"</c:if>>&nbsp;邮件
				    <%-- <t:comboBox name="sendType"  multiple="true" id="sendType" data='[{"id":"innerMessage","text":"站内信"},{"id":"sms","text":"短信"},{"id":"email","text":"邮件"}]' value="${train.sendType }" onChange="setType(self)" datatype="*"></t:comboBox> --%>
				</td>
			</tr>
			<tr class="selectUser">
				<td class="td_title">
					<label class="Validform_label">
						发致员工:
					</label>
				</td>
				<td class="value">
				    <t:userSelect hiddenName="empIds"  hiddenValue="${train.empIds }" displayName="empNames" displayValue="${train.empNames }"></t:userSelect>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						发致岗位:
					</label>
				</td>
				<td class="value">
				    <t:jobSelect hiddenName="jobIds" displayName="jobNames" hiddenValue="${train.jobIds }" displayValue="${train.jobNames }"></t:jobSelect>
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
				    <t:userSelect hiddenName="copiedIds" displayName="copiedNames" hiddenValue="${train.copiedIds }" displayValue="${train.copiedNames }"></t:userSelect>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>培训主题:
					</label>
				</td>
				<td class="value">
				    <input id="title" name="title" type="text" class="inputxt" datatype="*1-50" value='${train.title }'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>培训时间:
					</label>
				</td>
				<td class="value">
				    <t:datetimebox name="trainTime" id="trainTime" type="datetime" value="${train.trainTime }" datatype="*"></t:datetimebox>
				</td>
			</tr>
			<tr>
			    <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>培训地点:
					</label>
				</td>
				<td class="value">
				    <input id="trainPlace" width="100%" name="trainPlace" datatype="*1-50" type="text" class="inputxt" value='${train.trainPlace}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>培训对象:
					</label>
				</td>
				<td class="value">
				    <input id="trainObj" width="100%" name="trainObj" datatype="*1-50" type="text" class="inputxt" value='${train.trainObj}'>
				</td>
			    <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>培训人:
					</label>
				</td>
				<td class="value">
				    <input id="trainPerson" name="trainPerson" datatype="*1-50" type="text" class="inputxt" value='${train.trainPerson}'>
				</td>
			</tr>
			<tr>
			    <td class="td_title" >
					<label class="Validform_label">
						<span style="color:red">*</span>公司名称:
					</label>
				</td>
				<td class="value">
				    <t:comboBox name="company"  id="company"  dictCode="companyType" value="${train.companyId }" datatype="*"></t:comboBox>
				</td>
			    <td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>是否需回复:
					</label>
				</td>
				<td class="value">
				   <label class="Validform_label"> <input type="radio" name="needReply" value="1" <c:if test="${train.needReply == '1' }">checked="checked"</c:if>/></label>是
				   <label class="Validform_label"> <input type="radio" name="needReply" value="0" <c:if test="${train.needReply == '0' || empty train.needReply}">checked="checked"</c:if>/></label>否
 				</td>
			    <td class="td_title email" style="display: none;">
					<label class="Validform_label">
						<span style="color:red">*</span>发送邮件账号:
					</label>
				</td>
				<td class="value email" style="display: none;">
				    <t:comboBox name="mailConfigId"  id="mailConfigId"  url="messageController.do?getEmail&value=${train.mailConfigId }" value="${train.mailConfigId }" ></t:comboBox>
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
			&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showAll()">查看正文</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<iframe src="webpage/office/officeLoad.jsp"  style="width: 0;height: 0;padding:0px;border:0px;" id="office_iframe" name="office_iframe" >
			</iframe>
			</div>
			</td>
			</tr>
		</table>
	</t:formvalid>
