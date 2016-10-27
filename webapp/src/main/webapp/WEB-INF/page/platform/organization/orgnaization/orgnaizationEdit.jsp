<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <t:formvalid callback="refreshOrgTree" action="orgnaizationController.do?saveOrUpdate" tiptype="5" >
	<input id="id" name="id" type="hidden" value="${org.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>机构名称:
				</label>
			</td>
			<td class="value">
			    <input id="name" name="name" type="text" datatype="*1-50" class="inputxt" value='${org.name}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>机构编码:
				</label>
			</td>
			<td class="value">
			    <input id="code" name="code" type="text" class="inputxt" datatype="*1-32" ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity" oldValue='${org.code}' value='${org.code}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>组织全称:
				</label>
			</td>
			<td class="value">
			    <input id="shortName" name="shortName" datatype="*1-20" type="text" class="inputxt" value='${org.shortName}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					英文名:
				</label>
			</td>
			<td class="value">
			    <input id="enName" name="enName" type="text" datatype="letter" ignore="ignore" class="inputxt" value='${org.enName}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>机构类型:
				</label>
			</td>
			<td class="value">
			    <c:choose>
			    	<c:when test="${orgType eq 'dept'}">
			          <t:comboBox onSelect="disablePositionSel()" name="type" id="type" value="${org.type}" data='[{"id":"org","text":"机构"}]' ></t:comboBox>
			       </c:when>
			       <c:when test="${orgType eq 'org'}">
			          <t:comboBox onSelect="disablePositionSel()" name="type" id="type" value="${org.type}"
			           data='[{"id":"dept","text":"部门"}]'></t:comboBox>
			       </c:when>
			       <c:when test="${orgType eq 'dept'}">
			          <t:comboBox onSelect="disablePositionSel()" name="type" id="type" value="${org.type}" data='[{"id":"dept","text":"部门"}]' ></t:comboBox>
			       </c:when>
			       <%-- <c:otherwise>
			           <t:comboBox onSelect="disablePositionSel()" name="type" id="type" value="${org.type}" 
			                                        data='[{"id":"job","text":"岗位"}]' ></t:comboBox>
			       </c:otherwise> --%>
			    </c:choose>
				
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>所属机构、部门:
				</label>
			</td>
			<td class="value">
			    <input id="parent.id" name="parent.id" type="hidden" class="inputxt" value='${org.parent.id}'>
			    <input id="parent.name" name="parent.name" datatype="*"  type="text" readonly="readonly" class="inputxt" value='${org.parent.name}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					电话:
				</label>
			</td>
			<td class="value">
			    <input id="telephone" name="telephone" type="text" class="inputxt" value='${org.telephone}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					邮箱:
				</label>
			</td>
			<td class="value">
			    <input id="email" name="email" datatype="e" ignore="ignore" type="text" class="inputxt" value='${org.email}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					传真:
				</label>
			</td>
			<td class="value">
			    <input id="fax" name="fax" type="text" class="inputxt" value='${org.fax}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					邮编:
				</label>
			</td>
			<td class="value">
			    <input id="post" name="post" datatype="p" ignore="ignore" type="text" class="inputxt" value='${org.post}'>
			</td>
		</tr>
		<tr>
	       <td class="td_title">
				<label class="Validform_label">
					地址:
				</label>
			</td>
			<td class="value">
			    <input id="address" name="address" type="text" style="width: 100%;" class="inputxt" value='${org.address}'>
			</td>
	
			<td class="td_title">
				<label class="Validform_label">
					负责人:
				</label>
			</td>
			<td class="value">
			    <t:userSelect hiddenName="managerUserId" hiddenValue="${org.managerUserId}" displayName="managerUserName" displayValue="${org.managerUserName}" ></t:userSelect>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					备注:
				</label>
			</td>
			<td class="value" colspan="3">
				<textarea id="description" name="description" rows="2" style="width: 98%;">${org.description}</textarea>
			</td>
		</tr>
		<c:if test="${orgType eq 'dept'}">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					分管领导:
				</label>
			</td>
			<td class="value" colspan="3">
				<t:userSelect hiddenName="leaderUserId" hiddenValue="${org.leaderUserId}" displayName="leaderUserName" displayValue="${org.leaderUserName}" ></t:userSelect>
			</td>
		</tr>
		</c:if>
		
				
	</table>
</t:formvalid>
 <script type="text/javascript">
  	$(function(){
  		var type = '${parentOrg.type}';
  		if(!type){
  			type = '${org.type}'; 
  		}
  		setTimeout(function(){
			$('#type').combobox('select', type);
			//部门经理职位不能修改
			if($("#positionName").val()=='部门经理'){
		    	$("#positionName").closest("td").find("button").attr("disabled",true);
		    }
		},100);
  		
	    if("${parentOrg.id}" != ""){
		    document.getElementById("parent.id").value = "${parentOrg.id}";
		    document.getElementById("parent.name").value = "${parentOrg.name}";
	    }
  	});
  	
  	function refreshTree() {
  		window.close();
		$("#orgnaization_tree").tree('reload');
	}
  	
  	//只有岗位才能选择职位
  	function disablePositionSel(){
  		var org = $("input[name='type']").val();
  		if(org=='job'){
	  		$("#positionName").closest("td").find("button").removeAttr("disabled");
  		}else{
  			$("#positionName").closest("td").find("button").attr("disabled",true);
  		}
  	}
