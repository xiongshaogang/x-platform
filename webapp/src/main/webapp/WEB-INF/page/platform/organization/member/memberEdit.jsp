<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:formvalid formid="formobj" gridId="memberList" action="memberController.do?saveOrUpdate" tiptype="5">
			<input id="id" name="id" type="hidden" value="${memberPage.id }">
<table style="width: 600px; " cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>编码:
					</label>
				</td>
				<td class="value">
				    <input id="code" name="code" type="text" class="inputxt" datatype="*1-32" ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.orgnaization.member.entity.MemberEntity" oldValue='${memberPage.code}' value='${memberPage.code}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>姓名:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt" value='${memberPage.name}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>用户类型:
					</label>
				</td>
				<td class="value">
				    <t:comboBox datatype="*" name="userType" dictCode="userType" multiple="false" id="userType" value="${memberPage.userType}"></t:comboBox>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						企业编码:
					</label>
				</td>
				<td class="value">
				    <input id="companyId" name="companyId" type="text" class="inputxt" value='${memberPage.companyId}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						电子邮箱:
					</label>
				</td>
				<td class="value">
				    <input id="email" name="email" datatype="e" ignore="ignore" type="text" class="inputxt" value='${memberPage.email}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						出生日期:
					</label>
				</td>
				<td class="value">
					<fmt:formatDate value="${memberPage.birthday}" pattern="yyyy-MM-dd" var="birthday"/>
				    <t:datetimebox id="birthday" name="birthday" type="date" value='${birthday}'></t:datetimebox>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>性别:
					</label>
				</td>
				<td class="value">
				    <t:comboBox datatype="*" name="sex" dictCode="sex" multiple="false" id="sex" value="${memberPage.sex}"></t:comboBox>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>地址:
					</label>
				</td>
				<td class="value">
				    <input id="address" name="address" datatype="*1-100" type="text" class="inputxt" value='${memberPage.address}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						邮编:
					</label>
				</td>
				<td class="value">
				    <input id="post" name="post" type="text" datatype="p" ignore="ignore" class="inputxt" value='${memberPage.post}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						电话号码:
					</label>
				</td>
				<td class="value">
				    <input id="telephone" name="telephone" type="text" class="inputxt" value='${memberPage.telephone}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						手机号码:
					</label>
				</td>
				<td class="value">
				    <input id="mobile" name="mobile" datatype="m" ignore="ignore" type="text" class="inputxt" value='${memberPage.mobile}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						QQ号码:
					</label>
				</td>
				<td class="value">
				    <input id="qq" name="qq" type="text" class="inputxt" value='${memberPage.qq}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						MSN:
					</label>
				</td>
				<td class="value">
				    <input id="msn" name="msn" type="text" class="inputxt" value='${memberPage.msn}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						国籍:
					</label>
				</td>
				<td class="value">
				    <input id="nationallity" name="nationallity" type="text" class="inputxt" value='${memberPage.nationallity}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>证件类型:
					</label>
				</td>
				<td class="value">
					<t:comboBox name="certificateType" datatype="*" dictCode="cardType" multiple="false" id="certificateType" value="${memberPage.certificateType}"></t:comboBox>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>证件号码:
					</label>
				</td>
				<td class="value">
				    <input datatype="*" id="certificateNum" name="certificateNum" type="text" class="inputxt" value='${memberPage.certificateNum}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						单位名称:
					</label>
				</td>
				<td class="value">
				    <input id="unitName" name="unitName" type="text" class="inputxt" value='${memberPage.unitName}'>
				</td>
				<td class="td_title">
					<label class="Validform_label">
						传真:
					</label>
				</td>
				<td class="value">
				    <input id="fax" name="fax" type="text" class="inputxt" value='${memberPage.fax}'>
				</td>
			</tr>
	</table>
</t:formvalid>
