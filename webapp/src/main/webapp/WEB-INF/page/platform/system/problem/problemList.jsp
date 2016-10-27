<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border: 0px;">
  <t:datagrid name="problemList" checkbox="false" fitColumns="false" title="问题反馈列表" actionUrl="problemController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"    field="id"  hidden="false"  queryMode="single"></t:dgCol>
   <t:dgCol title="问题类型"  field="problemType" replace="bug问题_bug,体验问题_experience" queryInputType="combobox" data='[{id:"bug",text:"bug问题"},{id:"experience",text:"体验问题"}]' query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="反馈内容"  field="content" query="false" hidden="true"  queryMode="single"  width="400"></t:dgCol>
   <t:dgCol title="联系方式"  field="contactInformation" query="false"  hidden="true" queryMode="single"  width="200"></t:dgCol>
   <t:dgCol title="反馈时间"  field="createTime" queryInputType="datebox" query="true"  hidden="true" queryMode="group"  width="160"></t:dgCol>
   <t:dgCol title="状态" myFormatter="return formatState(value,rec,index)" field="problemState" queryInputType="combobox" data='[{id:"0",text:"未读"},{id:"1",text:"已读"},{id:"2",text:"已解决"}]' query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
   
   <t:dgCol title="操作" field="opt" width="120"></t:dgCol>
   <t:dgOpenOpt  url="problemController.do?detail&id={id}" preinstallWidth="1" icon="awsm-icon-zoom-in green" exParams="{optFlag:'detail'}" height="390" title="查看"></t:dgOpenOpt>
   <t:dgOpenOpt  url="problemController.do?resolveProblemPage&id={id}" preinstallWidth="1" icon="awsm-icon-edit green" exParams="{optFlag:'update'}" height="260" title="解决方案"></t:dgOpenOpt>
   
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
	 redrawEasyUI($(".easyui-layout"));
 		//给时间要素加上样式
 });
 
 function formatState(value,rec,index){
	 if(rec.problemState=="0"){
		 return '<span style="color:red">未读</span>';
	 }else if(rec.problemState=="1"){
		 return '已读';
	 }else if(rec.problemState=="2"){
		 return '<span style="color:blue">已解决</span>';
	 }else{
		 return value;
	 }
 }
 </script>