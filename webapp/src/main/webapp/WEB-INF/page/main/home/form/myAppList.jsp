<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<style>
#myAppList .bootstrap-switch{
	top: -1px;
}
#myAppList .bootstrap-switch.bootstrap-switch-focused{
	border-color: #ccc;
	box-shadow: none;
}
</style>
<div class="app-list-container" id="appDiv">
	<div class="header">我的工作设置</div>
	<div class="sys-list">
		<ul id="myAppList" class="list-style-none sys-list-container">
	
		</ul>
	</div>
</div>

<script>
var myAppList = {
		queryAppList : function(){
			$.ajax({
				url : 'flowFormController.do?queryAPPAllList',
				type : 'post',
				data: '',
				dataType: "json",
				success : function(result) {
					if(result.success && result.obj.length >0){
						var templi = "";
						var status = "";
						$("#myAppList").empty();
						$.each(result.obj, function(i, item) {
							if(item.viewStatus == 1){
								status = "checked";
							}else{
								status = "";
							}
						 	templi = templi + "<li id='"+item.id+"' data-code='"+item.code+"' class='list-item'>"
						 		+"<div class='item-top'>"
								+"<span class='name'>"+item.name+"</span>"
								+"<span class='pull-right'><input type='checkbox' name='my-checkbox' "+status+"></span>"
								+"</div>"
								+"</li>";	
							
						});
						$("#myAppList").append(templi);
						
						$("[name='my-checkbox']").bootstrapSwitch({
							size: 'mini',
							onText: '开启',
							offText: '关闭',
							onColor: 'success'
						});
						
						$("[name='my-checkbox']").on("switchChange.bootstrapSwitch", function(){
							var $this=$(this);
							var id=$this.closest("li").attr("id");
							var code= $("#"+id).attr("data-code");
							myAppList.forbiddenOrStartApp(id,code);
						});
					}else{
						$("#myAppList").append("暂无数据");
					}
				}
			});
		},
		forbiddenOrStartApp : function(id,code){
			var myData = {"formId" : id,"formCode" : code};
			$.ajax({
				url : 'flowFormController.do?forbiddenOrStartApp',
				type : 'post',
				data: myData,
				dataType: "json",
				success : function(result) {
					if(result.success){
						var status = "";
						if(result.status == "1"){
							status = true;
						}else{
							status = false;
						}
						$("#"+id+" [type=checkbox]").bootstrapSwitch({state: status});
					}else{

					}
				}
			});
		}
};
$(function(){
	myAppList.queryAppList();
	
	home.loadSlimScroll([{
		obj : $("#myAppList").parent(".sys-list"),
		//width: "101%",
		height: "503px"
	}]);
});

</script>