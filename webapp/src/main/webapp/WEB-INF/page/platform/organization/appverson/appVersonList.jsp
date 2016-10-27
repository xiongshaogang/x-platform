<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">

		var appVerson = {
				uploadApp : function() {
					var id = $("#aid").val();
					commonPageUpload({
						businessKey : id,
						businessType : "AppVersonEntity",
						businessExtra : "versionNumber",
						isNeedToType : false,
						finishUploadCallback : "appVerson.uploadApp1(allFiles)"

					});
				},
				 uploadApp1 : function(result) {
					//if (result.length > 0) {
						var id = $("#id").val();
						var attachMentId = result[0].id;
						$("#attachMentId").val(attachMentId);
						
/* 					} else {
						alert("请先上传新版软件");
					} */
				},
				 appupdateBefore : function(){
					/* var aid = $("#attachMentId").val();
					if(aid.length<1){
						alert("请先上传新版本软件");
						return false;
					} */
				},
					
					 appupdateCallback : function(){
						
						$("#versionName").val("");
						$("#versionNumber").val("");
						$("#versionDescrition").val("");
						$("#attachMentId").val("");

					}
			};
	
	</script>
		<t:datagrid name="appVersonList" title="App版本列表" actionUrl="appVersonController.do?datagrid">
		<t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="100"></t:dgCol>	
		<t:dgCol title="软件名称" query="true" field="versionName" hidden="true" queryMode="single" width="200" align="center"></t:dgCol>
		<t:dgCol title="软件版本号" field="versionNumber" hidden="true" queryMode="single" width="180" align="center"></t:dgCol>
		<t:dgCol title="是否强制更新" field="isForcedUpdate" hidden="true" queryMode="single" replace="是_1,否_0" width="50" align="center"></t:dgCol>
		<t:dgCol title="软件类型" field="type" hidden="true" queryMode="single" replace="ios_1,android_0" width="50" align="center"></t:dgCol>
		<t:dgCol title="软件更新提示" field="versionDescrition" hidden="true" queryMode="single" width="200" align="center"></t:dgCol>
		<t:dgCol title="软件更新日期" field="createTime" hidden="true" queryMode="single" width="200" align="center"></t:dgCol>
		<t:dgCol title="下载链接" field="url" hidden="true" queryMode="single" width="200" align="center"></t:dgCol>
		<t:dgCol title="操作" field="opt" hidden="true"  width="50"></t:dgCol>
						
			<t:dgOpenOpt title="查看" url="appVersonController.do?editPage&id={id}" exParams="{optFlag:'detail'}" width="500" height="500"></t:dgOpenOpt>

			<t:dgToolBar title="新增" funname="add" url="appVersonController.do?editPage" width="500"  height="500"></t:dgToolBar>

		</t:datagrid>
		 <script type="text/javascript">
	 $(function(){
		 redrawEasyUI($("#page_content"));
	 });
 </script>
