<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>

			<t:datagrid name="backupChatsList" checkbox="true" fitColumns="false" title="聊天记录" actionUrl="backupChatsController.do?datagrid" idField="id" fit="true" queryMode="group">
									<t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
									<t:dgCol title="备份完成时间"  field="finishTime" query="true" hidden="true"  queryMode="single"  width="180" align="center" formatter="yyyy-MM-dd HH:mm:ss" ></t:dgCol>
									<t:dgCol title="备份的聊天记录开始时间"  field="startTimestamp"  hidden="true"  queryMode="single"  width="180" align="center" formatter="yyyy-MM-dd HH:mm:ss" ></t:dgCol>
									<t:dgCol title="备份的聊天记录结束时间"  field="endTimestamp"  hidden="true"  queryMode="single"  width="180" align="center" formatter="yyyy-MM-dd HH:mm:ss" ></t:dgCol>
									<t:dgCol title="备份数据量"  field="counts"  hidden="true"  queryMode="single"  width="100"></t:dgCol>
									<t:dgCol title="执行状态"  field="status"  hidden="true"  queryMode="single"  width="100" replace="正常完成_1,中途出错_2" data='[{"id":"1","text":"正常完成"},{"id":"2","text":"中途出错"}]' ></t:dgCol>
									<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
									<t:dgOpenOpt operationCode="sysParameterManager_editSysParameter_edit" url="sysParameterController.do?editPage&id={id}" preinstallWidth="1" icon="awsm-icon-edit blue" exParams="{optFlag:'add'}" height="500" title="编辑"></t:dgOpenOpt>
									<t:dgOpenOpt operationCode="sysParameterManager_viewSysParameter_view" url="sysParameterController.do?optFlag=detail&editPage&id={id}" preinstallWidth="1" icon="awsm-icon-img-test green" exParams="{optFlag:'detail'}" height="500" title="查看"></t:dgOpenOpt>
									<t:dgDelOpt operationCode="sysParameterManager_deleteSysParameter_delete" title="删除" icon="awsm-icon-trash red" url="sysParameterController.do?delete&ids={id}" />
									
									<t:dgToolBar title="添加"  operationCode="sysParameterManager_addSysParameter_add" preinstallWidth="1" height="500" icon="awsm-icon-img-test" buttonType="GridAdd" url="sysParameterController.do?editPage" funname="add"></t:dgToolBar>
									<t:dgToolBar title="批量删除" operationCode="sysParameterManager_deleteBatchSysParameter_batchDelete" icon="awsm-icon-remove" buttonType="GridDelMul" url="sysParameterController.do?delete" funname="deleteALLSelect"></t:dgToolBar>
								</t:datagrid>
	

<script type="text/javascript">
	
</script>