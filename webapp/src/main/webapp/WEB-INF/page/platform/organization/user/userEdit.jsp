<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	  $(function(){
		   if(!$("#orgId").val()){
			   $("#orgId").val($("#porgId").val());
			   $("#orgName").val($("#porgName").val());
		   }
	  });
  </script>
  <t:formvalid formid="formobj" gridId="userList" action="userController.do?saveOrUpdate" >
		<input id="id" name="id" type="hidden" value="${user.id }">
		<input id="orgId" name="orgId" type="hidden" value="${userOrg.org.id }">
		<input id="orgName" name="orgName" type="hidden" value="${userOrg.org.name }">
		<input id="userOrgId" name="userOrgId" type="hidden" value="${userOrg.id }">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>员工姓名:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name"  datatype="*1-50"  type="text" class="inputxt" value='${user.name}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>员工编码:
					</label>
				</td>
				<td class="value">
				    <input id="code" name="code" type="text" class="inputxt" datatype="*1-32" ajaxurl="commonController.do?checkUnique"
				     entityName="com.xplatform.base.orgnaization.user.entity.UserEntity" oldValue='${user.code}' value='${user.code}'>
				</td>
			</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>登陆名称:
				</label>
			</td>
			<td class="value">
			    <input datatype="s" name="userName" type="text" class="inputxt" uniquemsg='用户名已存在' oldValue='${user.userName}' entityName="com.xplatform.base.orgnaization.user.entity.UserEntity" ajaxurl="commonController.do?checkUnique" value='${user.userName}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>性别:
				</label>
			</td>
			<td class="value">
				<t:comboBox name="sex" dictCode="sex" multiple="false" id="sex" value="${user.sex}"></t:comboBox>
			</td>
			
		</tr>
		
		<tr>
			<td class="td_title">
					<label class="Validform_label">
						入职日期:
					</label>
				</td>
				<td class="value">
					<fmt:formatDate value="${user.enterDate}" pattern="yyyy-MM-dd" var="enterDate"/>
					<t:datetimebox id="enterDate" name="enterDate" type="date" value='${user.enterDate}'></t:datetimebox>
			    </td>
			<td class="td_title">
				<label class="Validform_label">
					出生日期:
				</label>
			</td>
			<td class="value">
				<fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd" var="birthday"/>
				<t:datetimebox id="birthday" name="birthday" type="date" value='${user.birthday}'></t:datetimebox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					证件类型:
				</label>
			</td>
			<td class="value">
				<t:comboBox name="cardType" dictCode="cardType" multiple="false" id="cardType" value="${user.cardType}"></t:comboBox>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					证件号码:
				</label>
			</td>
			<td class="value">
			    <input id="cardNo" name="cardNo" type="text" datatype="*1-18" ignore="ignore"  class="inputxt" value='${user.cardNo}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					QQ:
				</label>
			</td>
			<td class="value">
			    <input id="qq" name="qq" type="text" class="inputxt" value='${user.qq}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					邮箱:
				</label>
			</td>
			<td class="value">
			    <input id="email" name="email" datatype="e" ignore="ignore" type="text" class="inputxt" value='${user.email}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					电话:
				</label>
			</td>
			<td class="value">
			    <input id="telephone" name="telephone" type="text" class="inputxt" value='${user.telephone}'>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					手机号码:
				</label>
			</td>
			<td class="value">
			    <input id="phone" name="phone" datatype="m" ignore="ignore" type="text" class="inputxt" value='${user.phone}'
			     ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.orgnaization.user.entity.UserEntity" 
			     oldValue='${user.phone}' uniquemsg='该手机号码在系统中已存在'>
			</td>
		</tr>
		
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					政治面貌:
				</label>
			</td>
			<td class="value">
				<t:comboBox name="politicsStatus" dictCode="politicsStatus" multiple="false" id="politicsStatus"
				 value="${user.politicsStatus}"></t:comboBox>
			</td>
			<td class="td_title">
				<label class="Validform_label">
					婚姻状态:
				</label>
			</td>
			<td class="value">
				<t:comboBox name="maritalStatus" dictCode="maritalStatus" multiple="false" id="maritalStatus"
				 value="${user.maritalStatus}"></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					家庭地址:
				</label>
			</td>
			<td class="value" colspan="3">
			    <textarea id="address" name="address" type="text" class="inputxt">${user.address}</textarea>
			</td>
		</tr>
		<input name="password"   type="hidden" class="inputxt" value='123456'>
	</table>
</t:formvalid>
