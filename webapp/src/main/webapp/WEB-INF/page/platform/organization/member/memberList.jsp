<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:datagrid name="memberList" checkbox="true" fitColumns="false" title="会员信息" actionUrl="memberController.do?datagrid"
	idField="id" fit="true" queryMode="separate">
	<t:dgExportParams title="系统会员列表" sheetName="系统会员列表" entityClass="com.xplatform.base.orgnaization.member.entity.MemberEntity"/>
	<t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="姓名" query="true" field="name" hidden="true" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="电子邮箱" field="email" hidden="true" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="出生日期" field="birthday" formatter="yyyy-MM-dd" hidden="true" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="性别" field="sex" dictCode="sex" hidden="true" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="地址" field="address" hidden="true" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="手机号码" field="mobile" hidden="true" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="证件号码" field="certificateNum" hidden="true" queryMode="single" width="120"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="100"></t:dgCol>

	<t:dgOpenOpt title="编辑" operationCode="memberManager_editMember_edit" icon="awsm-icon-edit blue"
		url="memberController.do?editPage&id={id}" exParams="{optFlag:'add'}" width="700" height="520"></t:dgOpenOpt>
	<t:dgOpenOpt title="查看" operationCode="memberManager_viewMember_view" icon="awsm-icon-zoom-in green"
		url="memberController.do?editPage&id={id}" exParams="{optFlag:'detail'}" width="700" height="520"></t:dgOpenOpt>
	<t:dgDelOpt title="删除" operationCode="memberManager_deleteMember_delete" icon="awsm-icon-trash red"
		url="memberController.do?delete&id={id}" />

	<t:dgToolBar title="新增" operationCode="memberManager_addMember_add" icon="awsm-icon-plus" width="700" height="520"
		url="memberController.do?editPage" funname="add"></t:dgToolBar>
	<t:dgToolBar title="批量删除" operationCode="memberManager_deleteBatchMember_batchDelete" icon="awsm-icon-remove"
		url="memberController.do?deleteBatch" funname="deleteALLSelect"></t:dgToolBar>
	<t:dgToolBar title="导出" buttonType="GridExcelExport" />
</t:datagrid>
<script type="text/javascript">
	$(document).ready(function() {
		redrawEasyUI($("#page_content"));
	});
</script>