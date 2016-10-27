<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>角色信息</title>
 </head>
 <body>
  <t:formvalid formid="formobj" gridId="roleList" action="roleController.do?saveOrUpdateRole">
		<input id="id" name="id" type="hidden" value="${role.id }">
		<table  cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>角色名称:
					</label>
				</td>
				<td class="value">
				    <input datatype="zh" name="name" datatype="*1-32" type="text" class="inputxt" value='${role.name}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>角色编码:
					</label>
				</td>
				<td class="value">
				    <input name="code" type="text" datatype="*1-50" class="inputxt" value='${role.code}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>允许编辑:
					</label>
				</td>
				<td class="value">
					<t:comboBox name="allowEdit" id="allowEdit" data='[{"id":"Y","text":"是"},{"id":"N","text":"否"}]' datatype="*" value="${role.allowEdit}"></t:comboBox>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>角色类型:
					</label>
				</td>
				<td class="value">
					<t:comboBox name="definedFlag" id="definedFlag" data='[{"id":2,"text":"私人角色"},{"id":3,"text":"公共角色"}]' datatype="*" value="${role.definedFlag}"></t:comboBox>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>允许删除:
					</label>
				</td>
				<td class="value">
					<t:comboBox name="allowDelete" id="allowDelete" data='[{"id":"Y","text":"是"},{"id":"N","text":"否"}]' datatype="*" value="${role.allowDelete}" />
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						备注:
					</label>
				</td>
				<td class="value">
				    <textarea style="height:80px" id="description" name="description" type="text" class="input_area">${role.description}</textarea>
				</td>
			</tr>
		</table>
	</t:formvalid>
 </body>