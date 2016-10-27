<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:datagrid name="mailConfigList"   checkbox="true" fitColumns="true" title="邮件配置" actionUrl="messageController.do?datagrid" idField="id" fit="true"  queryMode="separate"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="账号名称"  field="userName"   width="100"  query="true"></t:dgCol>
   <t:dgCol title="邮件地址"  field="mailAddress"   width="150"  ></t:dgCol>
   <t:dgCol title="是否默认"  field="isdefault"   replace="否_0,是_1" width="400"  ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgOpenOpt preinstallWidth="2" height="450" url="messageController.do?mailConfigEdit&id={id}" exParams="{optFlag:'update'}" title="编辑" ></t:dgOpenOpt>
   <t:dgOpenOpt preinstallWidth="2" height="450" url="messageController.do?mailConfigEdit&id={id}&optFlag=detail" exParams="{optFlag:'detail'}" title="查看" ></t:dgOpenOpt>
   <t:dgFunOpt funname="setDefault(id)" title="设为默认邮件" icon="fa fa-star"></t:dgFunOpt>
   <t:dgFunOpt funname="testConnect(id)" title="测试链接" icon="fa fa-link"></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="messageController.do?deleteMailCofig&id={id}" callback="reflash" />
   <t:dgToolBar title="新增" icon="awsm-icon-plus" url="messageController.do?mailConfigEdit" funname="add" buttonType="GridAdd"  preinstallWidth="2"  height="460" ></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="messageController.do?batchDeleteMailCofig" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash" ></t:dgToolBar>
  </t:datagrid>
 <script type="text/javascript">
 function reflash(){
	  $("#mailConfigList").datagrid("reload");
  }
  function setDefault(id){
	  doAction("messageController.do?setDefault", "mailConfigList", "","",id)
  }
  
  function testConnect(id){
	  doAction("messageController.do?testConnect", "", "","",id)
  }
 
   $(function(){
	 redrawEasyUI($("#page_content"));
 });
 </script>