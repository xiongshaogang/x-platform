<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	function clickTree(node){
		$('#user_dept_org_tree').tree('select', node.target);
		$("#userEmployeeList").datagrid('options').queryParams.orgId=node.id;
		$("#userEmployeeList").datagrid("getPanel").panel("setTitle",node.text);
	}
	function addSelectEmp(){
		var rows = $("#userEmployeeList").datagrid('getSelections');
		if(rows.length>0){
			var row=rows[0];
			var empId=row["employee.id"];
			var empName=row["employee.name"];
			$('#userTypeName').val(empName);
			$('#userTypeId').val(empId);
			closeD(getD($('#user_dept_org_tree')));
		}else{
			alert("请选择人员");
		}
	}
</script>

	<div class="easyui-layout" fit="true">  
	    <div region="west" split="true" title="机构树" style="width:200px;">
	    	<t:tree id="user_dept_org_tree" gridTreeFilter="deptId" showOptMenu="false" url="deptController.do?tree" gridId="userEmployeeList" clickPreFun="clickTree(node)"></t:tree>
	    </div>  
	    <div region="center" style="border:0px">
	    	<t:datagrid name="userEmployeeList" autoLoadData="false" checkbox="false"  fitColumns="true" title="员工列表" actionUrl="employeeController.do?datagrid" idField="id" fit="true" queryMode="group">
				<t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"></t:dgCol>
			   <t:dgCol title="员工主键"  field="employee.id"  hidden="false"  queryMode="single"></t:dgCol>
			   <t:dgCol title="员工代码"  field="employee.code" query="true" hidden="true"  queryMode="single"  width="100"></t:dgCol>
			   <t:dgCol title="员工姓名"  field="employee.name" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
			   <t:dgCol title="性别"  field="employee.sex" dictCode="sex"  hidden="true"  queryMode="single"  width="100"></t:dgCol>
			   <t:dgCol title="手机号码"  field="employee.phone"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
			   <t:dgCol title="工作邮箱"  field="employee.jobPost"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
			   <t:dgCol title="转正日期"  field="employee.regularDate"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
			   <t:dgCol title="出生日期"  field="employee.birthday"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
			
				<t:dgToolBar title="选定" icon="awsm-icon-plus" funname="addSelectEmp"></t:dgToolBar>
			</t:datagrid>
	    </div>  
	   
	</div>
