<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
	.formtable .td_title{
		width:200px;
		height:30px;
		text-align:right;
		/*padding-left:5px;*/
	}
	.formtable .value {
		/*background-color: #FFFFFF;*/
		width:400px;
		height:30px;
		padding:5px;
	}
	.formtable td {
		border:0;
	}
	#nodePriviligeListPanel td{
		height:40px;
	}
	
	#nodeSignFormValid .form_div {
    	margin: 0px auto;
    	width: 100%;
    }
}
</style>
<script type="text/javascript">
$(function(){
	changeVoteType($("input[name='voteType']:checked").val());
	
	$.parser.onComplete=function(){
		var json=$("#nodeSignPri").val();
		var obj = $.parseJSON(json);
		for(var i=0;i<obj.length;i++){
			var num = obj[i].privilegeType;
			/* $("select[name='privilegeUserType"+num+"']").val(obj[i].privilegeUserType);
			$("select[name='privilegeUserType"+num+"']").find("option[value='"+obj[i].privilegeUserType+"']").attr("selected",true); */
			$("#privilegeUserType"+num).combobox("setValue",obj[i].privilegeUserType);
			/* showUserType($("select[name='privilegeUserType"+num+"']")); */
			showUserType($("#privilegeUserType"+num));
			if(obj[i].privilegeUserType == 'org'){
				$("#orgPrivilegeUserIds"+num).combotree('setValues',obj[i].privilegeUserIds.split(","));
			}else if(obj[i].privilegeUserType == 'emp'){
				$("#empPrivilegeUserIds"+num).val(obj[i].privilegeUserIds);
				$("#empPrivilegeUserNames"+num).val(obj[i].privilegeUserNames);
			}else if(obj[i].privilegeUserType == 'job'){
				$("#jobPrivilegeUserIds"+num).val(obj[i].privilegeUserIds);
				$("#jobPrivilegeUserNames"+num).val(obj[i].privilegeUserNames);
			}else if(obj[i].privilegeUserType == 'role'){
				$("#rolePrivilegeUserIds"+num).val(obj[i].privilegeUserIds);
				$("#rolePrivilegeUserNames"+num).val(obj[i].privilegeUserNames);
			}else if(obj[i].privilegeUserType == 'script'){
				
				$("#scriptPrivilegeUserIds"+num).val(obj[i].privilegeUserIds);
			}
		}
		$.parser.onComplete=mainComplete;
	}
	setTimeout('InitMirror.initId("scriptPrivilegeUserIds0")', InitMirror.options.initDelay);
	setTimeout('InitMirror.initId("scriptPrivilegeUserIds1")', InitMirror.options.initDelay);
	setTimeout('InitMirror.initId("scriptPrivilegeUserIds2")', InitMirror.options.initDelay);
	setTimeout('InitMirror.initId("scriptPrivilegeUserIds3")', InitMirror.options.initDelay);
});
function close1(){
	closeD($("#formobj").closest(".window-body"));
}
function showUserType(self){
	/* var val = $(obj).val();
	var num = $(obj).parent("td").find("input[name^='privilegeType']").val(); */
	var val = $(self).val();
	var num = $(self).parent("td").find("input[name^='privilegeType']").val();
	if(val == 'org'){
		$("#org"+num).css("display","");
		$("#emp"+num).css("display","none");
		$("#role"+num).css("display","none");
		$("#job"+num).css("display","none");
		$("#script"+num).css("display","none");
	}else if(val == 'emp'){
		$("#org"+num).css("display","none");
		$("#emp"+num).css("display","");
		$("#role"+num).css("display","none");
		$("#job"+num).css("display","none");
		$("#script"+num).css("display","none");
	}else if(val == 'role'){
		$("#org"+num).css("display","none");
		$("#emp"+num).css("display","none");
		$("#role"+num).css("display","");
		$("#job"+num).css("display","none");
		$("#script"+num).css("display","none");
	}else if(val == 'job'){
		$("#org"+num).css("display","none");
		$("#emp"+num).css("display","none");
		$("#role"+num).css("display","none");
		$("#job"+num).css("display","");
		$("#script"+num).css("display","none");
	}else if(val=='script'){
		$("#org"+num).css("display","none");
		$("#emp"+num).css("display","none");
		$("#role"+num).css("display","none");
		$("#job"+num).css("display","none");
		$("#script"+num).css("display","");
	}
}

function changeVoteType(val){
	if(val=="percent"){
		$(".voteSpan").css("display","");
	}else{
		$(".voteSpan").css("display","none");
	}
}

function saveScript(){
	$("#scriptPrivilegeUserIds0").val(InitMirror.getValue("scriptPrivilegeUserIds0"));
	$("#scriptPrivilegeUserIds1").val(InitMirror.getValue("scriptPrivilegeUserIds1"));
	$("#scriptPrivilegeUserIds2").val(InitMirror.getValue("scriptPrivilegeUserIds2"));
	$("#scriptPrivilegeUserIds3").val(InitMirror.getValue("scriptPrivilegeUserIds3"));
}
</script>
<t:formvalid formid="nodeSignFormValid" action="nodeSignController.do?saveNodeSign" beforeSubmit="saveScript">
	 <input type="hidden" name="actDefId" value="${param.actDefId}" />
     <input type="hidden" name="nodeId" value="${param.nodeId}" />
     <input type="hidden" name="id" value="${nodeSign.id}" />
     <input type="hidden" id="nodeSignPri" value='${nodeSignPri}' />
     <t:tabs id="nodeUserRule" height="347" fit="false" hBorderBottom="true" leftDiv="true" leftDivWidth="90" leftDivTitle="会签规则设置" rightDiv="true" closeBtn="true">
     	<div  title="投票规则" id="voteRuleListPanel" style="overflow-x: hidden; overflow-y: auto">
     		<table cellpadding="0" cellspacing="1" class="formtable">
     		    <!-- 
				<tr>
					<td class="td_title"><label class="Validform_label"> 决策方式：</label></td>
					<td class="value" >
					<input type="radio" name="decideType" value="pass"  <c:if test="${nodeSign.decideType == 'pass'  }">checked="checked"</c:if> />通过
					<input type="radio" name="decideType" value="reject" <c:if test="${nodeSign.decideType == 'reject'  }">checked="checked"</c:if>/>拒绝
					</td>
				</tr>
				 -->
				<tr>
					<td class="td_title"><label class="Validform_label"> 后续处理模式：</label></td>
					<td class="value" >
					<input type="radio" name="completeType" value="direct"  <c:if test="${nodeSign.completeType == 'direct'  }">checked="checked"</c:if> />直接处理
					<input type="radio" name="completeType" value="complete" <c:if test="${nodeSign.completeType == 'complete'  }">checked="checked"</c:if>/>等待所有人投票
					</td>
				</tr>
				<tr>
					<td class="td_title"><label class="Validform_label"> 投票类型：</label></td>
					<td class="value" >
					<input type="radio" name="voteType" value="absolute" onclick="changeVoteType(this.value)"  <c:if test="${nodeSign.voteType == 'absolute'  }">checked="checked"</c:if> />绝对票数
					<input type="radio" name="voteType" value="percent" onclick="changeVoteType(this.value)" <c:if test="${nodeSign.voteType == 'percent'  }">checked="checked"</c:if>/>百分比
					</td>
				</tr>
				<tr>
					<td class="td_title"><label class="Validform_label"> 同意票数：</label></td>
					<td class="value" >
					<input type="text" class="inputxt" id="voteAgreeAmount" name="voteAgreeAmount" value="${nodeSign.voteAgreeAmount }" /><span style="display: none;color: red;" class="voteSpan">例如：输入50则表示50%</span>
					</td>
				</tr>
				<tr>
					<td class="td_title"><label class="Validform_label"> 反对票数：</label></td>
					<td class="value" >
					<input type="text" class="inputxt" id="voteRefuseAmount" name="voteRefuseAmount" value="${nodeSign.voteRefuseAmount }" /><span style="display: none;color: red;" class="voteSpan">例如：输入50则表示50%</span>
					</td>
				</tr>
				<tr>
					<td class="td_title"><label class="Validform_label"> 再议票数：</label></td>
					<td class="value" >
					<input type="text" class="inputxt" id="voteReconsideAmount" name="voteReconsideAmount" value="${nodeSign.voteReconsideAmount }" /><span style="display: none;color: red;" class="voteSpan">例如：输入50则表示50%</span>
					</td>
				</tr>
			</table>
     	</div>
     	<div  title="特权功能" id="nodePriviligeListPanel">
     		<table cellpadding="0" cellspacing="0" style="width:100%" border="0">
					<tr align="center">
					<td width="20%" align="right">特权类型</td>
					<td width="40%">用户类型</td>
					<td width="40%">用户来自</td>
				</tr>
				<tr>
					<td align="right"><label class="Validform_label"> 所有特权</label></td>
					<td align="center">
					<input type="hidden" name="privilegeType0" value="0"/>
					<!-- <select name="privilegeUserType0" onchange="showUserType(this)" >
					<option value=""></option>
					<option value="emp">员工</option>
					<option value="role">角色</option>
					<option value="job">岗位</option>
					<option value="org">机构部门</option>
					</select> -->
					<t:comboBox id="privilegeUserType0" name="privilegeUserType0" onSelect="showUserType(self)" dictCode="userType2" multiple="false"></t:comboBox>
					
					</td>
					<td align="center">
					<div id="org0" style="display: none;"><t:orgSelect id="orgPrivilegeUserIds0" name="orgPrivilegeUserIds0" multiple="true" onlyLeafCheck="true" ></t:orgSelect></div>
				    <div id="job0" style="display: none;"><t:jobSelect hiddenName="jobPrivilegeUserIds0" displayName="jobPrivilegeUserNames0" ></t:jobSelect></div>
				    <div id="role0" style="display: none;"><t:roleSelect hiddenName="rolePrivilegeUserIds0" displayName="rolePrivilegeUserNames0"></t:roleSelect></div> 
					<div id="emp0" style="display: none;"><t:empSelect hiddenName="empPrivilegeUserIds0" displayName="empPrivilegeUserNames0"></t:empSelect></div>
					<div id="script0" style="display: none;">
						<div style="background: #F7F7F7;text-align:left;margin:3px;">
							<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('scriptPrivilegeUserIds0')">常用脚本</button>
							<textarea mirrorheight="60px" id="scriptPrivilegeUserIds0" name="scriptPrivilegeUserIds0" class="input_area"></textarea>
						</div>
					</div>
					</td>
				</tr>
				<tr>
					<td align="right"><label class="Validform_label"> 允许直接处理</label></td>
					<td align="center">
						<input type="hidden" name="privilegeType1" value="1"/>
						<!-- <select name="privilegeUserType1" onchange="showUserType(this)">
							<option value=""></option>
							<option value="emp">员工</option>
							<option value="role">角色</option>
							<option value="job">岗位</option>
							<option value="org">机构部门</option>
						</select> -->
						<t:comboBox id="privilegeUserType1" name="privilegeUserType1" onSelect="showUserType(self)" dictCode="userType2" multiple="false"></t:comboBox>
					</td>
					<td align="center">
					<div id="org1" style="display: none;"><t:orgSelect id="orgPrivilegeUserIds1" name="orgPrivilegeUserIds1" multiple="true" onlyLeafCheck="true" ></t:orgSelect></div>
				    <div id="job1" style="display: none;"><t:jobSelect hiddenName="jobPrivilegeUserIds1" displayName="jobPrivilegeUserNames1" ></t:jobSelect></div>
				    <div id="role1" style="display: none;"><t:roleSelect hiddenName="rolePrivilegeUserIds1" displayName="rolePrivilegeUserNames1"></t:roleSelect></div> 
					<div id="emp1" style="display: none;"><t:empSelect hiddenName="empPrivilegeUserIds1" displayName="empPrivilegeUserNames1"></t:empSelect></div>
					<div id="script1" style="display: none;">
						<div style="background: #F7F7F7;text-align:left;margin:3px;">
							<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('scriptPrivilegeUserIds1')">常用脚本</button>
							<textarea mirrorheight="60px" id="scriptPrivilegeUserIds1" name="scriptPrivilegeUserIds1" class="input_area"></textarea>
						</div>
					</div>
					</td>
				</tr>
				<tr>
					<td align="right"><label class="Validform_label"> 一票否决制</label></td>
					<td align="center">
					<input type="hidden" name="privilegeType2" value="2"/>
					<!-- <select name="privilegeUserType2" onchange="showUserType(this)">
						<option value=""></option>
						<option value="emp">员工</option>
						<option value="role">角色</option>
						<option value="job">岗位</option>
						<option value="org">机构部门</option>
					</select> -->
					<t:comboBox id="privilegeUserType2" name="privilegeUserType2" onSelect="showUserType(self)" dictCode="userType2" multiple="false"></t:comboBox>
					</td>
					<td align="center">
					<div id="org2" style="display: none;"><t:orgSelect id="orgPrivilegeUserIds2" name="orgPrivilegeUserIds2" multiple="true" onlyLeafCheck="true" ></t:orgSelect></div>
				    <div id="job2" style="display: none;"><t:jobSelect hiddenName="jobPrivilegeUserIds2" displayName="jobPrivilegeUserNames2" ></t:jobSelect></div>
				    <div id="role2" style="display: none;"><t:roleSelect hiddenName="rolePrivilegeUserIds2" displayName="rolePrivilegeUserNames2"></t:roleSelect></div> 
					<div id="emp2" style="display: none;"><t:empSelect hiddenName="empPrivilegeUserIds2" displayName="empPrivilegeUserNames2"></t:empSelect></div>
					<div id="script2" style="display: none;">
						<div style="background: #F7F7F7;text-align:left;margin:3px;">
							<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('scriptPrivilegeUserIds2')">常用脚本</button>
							<textarea mirrorheight="60px" id="scriptPrivilegeUserIds2" name="scriptPrivilegeUserIds2" class="input_area"></textarea>
						</div>
					</div>
					</td>
				</tr>
				<tr>
					<td align="right"><label class="Validform_label"> 允许补签</label></td>
					<td align="center">
						<input type="hidden" name="privilegeType3" value="3"/>
						<!-- <select name="privilegeUserType3" onchange="showUserType(this)">
							<option value=""></option>
							<option value="emp">员工</option>
							<option value="role">角色</option>
							<option value="job">岗位</option>
							<option value="org">机构部门</option>
						</select> -->
						<t:comboBox id="privilegeUserType3" name="privilegeUserType3" onSelect="showUserType(self)" dictCode="userType2" multiple="false"></t:comboBox>
					</td>
					<td align="center">
					<div id="org3" style="display: none;"><t:orgSelect id="orgPrivilegeUserIds3" name="orgPrivilegeUserIds3" multiple="true" onlyLeafCheck="true" ></t:orgSelect></div>
				    <div id="job3" style="display: none;"><t:jobSelect hiddenName="jobPrivilegeUserIds3" displayName="jobPrivilegeUserNames3" ></t:jobSelect></div>
				    <div id="role3" style="display: none;"><t:roleSelect hiddenName="rolePrivilegeUserIds3" displayName="rolePrivilegeUserNames3"></t:roleSelect></div> 
					<div id="emp3" style="display: none;"><t:empSelect hiddenName="empPrivilegeUserIds3" displayName="empPrivilegeUserNames3"></t:empSelect></div>
					<div id="script3" style="display: none;">
						<div style="background: #F7F7F7;text-align:left;margin:3px;">
							<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('scriptPrivilegeUserIds3')">常用脚本</button>
							<textarea mirrorheight="60px" id="scriptPrivilegeUserIds3" name="scriptPrivilegeUserIds3" class="input_area"></textarea>
						</div>
					</div>
					</td>
				</tr>
			</table>
     	</div>
     </t:tabs>
</t:formvalid>
