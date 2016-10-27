<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="tableName" value="t_auto_mxcs" />
<input type="hidden" id="formId" value="402881b35104cf450151053b83e70073" />
<script type="text/javascript" src="basic/js/jquery.twbsPagination.min.js"></script>
<!-- <span id="402881b35104cf450151053b83e70564">id:402881b35104cf450151053b83e70564 </span> -->
<!-- <div class="text-center header">
</div> -->
<div>
<div class="sys-list" id="fieldDataDiv">
	<ul class="list-style-none sys-list-container no-padding" id="fieldDataListUl">
	</ul>
	

</div>
<!-- 	<div id="coupon_pagination" style="margin-bottom: 2em;">
		分页区域
		<ul class="bbb-pagination list-style-none-h text-center" id="coupon_paginationUl"></ul>
	</div> -->
</div>

<script type="text/javascript">

var commonFormEdit=function(id){
	var tableName=$("#tableName").val();
	var formId=$("#formId").val();
	window.location.href="appFormTableController.do?commonFormEdit&formId="+formId+"&id="+id;
}

var fieldData_scroll;
var fieldData_content=$("#fieldDataDiv");
var fieldData = {
	getFieldData : function(){
		/* var fileDataStr = '${fieldData}';*/
		var formCode = '${formCode}'; 
		var formId = '${formId}';
		var isFlow = '${isFlow}';
		var viewType = '${viewType}';
		var notifyType = '${notifyType}';
		var pagenumber = fieldData_content.data('pagenumber');
		if(pagenumber == "" || pagenumber == null || typeof(pagenumber) == "undefined"){
			pagenumber = 1;
		}
		$.ajax({
			url : 'appFormTableController.do?getFieldData&rows=10&page='+pagenumber+"&formCode="+formCode,
			type : 'post',
			data: '',
			dataType: "json",
			success : function(result) {
				if(result.success ){
					var deleteStr = ""; 
					if(isFlow != 1 && notifyType != 1){
						deleteStr = "<i class='fa fa-close' ></i>";
					}
					//$("#fieldDataListUl").empty();
					//$("#tableName").val(result.obj.tableName);
					console.info(result.obj);
					if(result.obj.extras.length > 0){
						$.each(result.obj.extras, function(i, item) {
							var date = new Date(item.createTime);
							var content = "";
							var title = "";
							
							if(item.titleContent != null && item.titleContent != "" && typeof(item.titleContent) != "undefined"){
								title = item.titleContent.value;
							}
							$.each(item.extraData, function(k, item2) {
								content += "<div class='content-item'>"
									+ "<span class='label'>"+item2.name+"：</span>"
									+ "<span class='text'>"+item2.value+"</span>"
									+ "</div>";
							});
							if(content != ""){
								content = "<div class='item-content'>"
								+ "<div class='content-inner'>"
								+ content
								+ "</div>"
								+ "</div>";
							}
							
							var templi = "<li id='"+item.id+"' data-tableName='"+result.obj.tableName+"' class='list-item' data-title='"+title+"'>"
								+ "<div class='item-top'>"
								+ "<span class='item-title'>"+title+"</span>"
								+ "<span class='item-ctime'>"+item.createTime+"</span>"
								+ deleteStr
								+ "</div>"
								+ content
								+ "</li>";
							
							$("#fieldDataListUl").append(templi);
						});

						
						$("#fieldDataListUl li").on("click", function(e){
							id = this.id;
							//左上角出现后腿样式
							$("#leftName").html("<i class='fa fa-angle-left'></i>");
							$("#commonFormEdit").load("appFormTableController.do?commonFormEdit&formCode="+formCode+"&businessKey="+id+"&viewType="+viewType);
							//修改顶上名称为title
							$("#appName").text($("#"+id).attr("data-title"));
							
							$("#rightName").html("<i class='fa fa-ellipsis-v'></i>");
							$("#appList").attr("data-flag",1);
							$("#appList").attr("data-type",3);
							//获取关联任务
							formAppHome.getRelaFlFoList(formCode,formId,id);
						});
						
						$("#fieldDataListUl li .fa-close").on("click", function(e){
							if(confirm("确定删除该项？")){
								var $this=$(this);
								var id=$this.closest("li").attr("id");
								var tableName=$("#"+id).attr("data-tableName");
								if(tableName&&id){
									ajaxTip("appFormTableController.do?deleteData&tableName="+tableName+"&id="+id,function(){
										$this.closest("li").remove();
									});
								}
							}
							e.stopPropagation();
						});
						
						if (fieldData_scroll) {
							if(fieldData_content.data("hasInit")){
								//页面已加载过数据的情况
								fieldData_scroll.refresh();
								iscrollAssist.pullActionCallback(fieldData_scroll);
								return ;
							}
						}
						
						//第一次进入(scroll未实例化)/二次进入,但是是首次加载并有数据(需要重新实例化scroll,即使之前已存在)
						setTimeout(function(){
							fieldData_content.nodata.hide();
							fieldData_content.wrapper.show();
							fieldData_scroll = iscrollAssist.initScroll(fieldData_content, fieldData.pullDownAction, fieldData.pullUpAction,{
								autoLoadMargin:800
							});
							fieldData_content.data("hasInit",true);
						},0);
				 }else{
					if(fieldData_scroll){
						if(fieldData_content.data("hasInit")){
							//页面已加载过数据的情况
							iscrollAssist.pullActionNoMore(fieldData_scroll);
							//本次加载没数据时,pagenumber保持不变
							fieldData_content.data('pagenumber', pagenumber - 1);
							return;
						}
					}
					
					//页面当前无数据(第一次进入就没数据/二次进入页面没数据)
					fieldData_content.nodata.show();
					fieldData_content.wrapper.hide();
					//$("#chatMain").append("<div id='null-nouser' class='sys-notice'>暂时没有聊天信息</div>");
					 $("#fieldDataListUl").append("暂无数据");
				 }
				}else{

				}
			}
			
		});

	},
	/**
	* 上拉加载下一页
	*/
	pullUpAction : function() {
		var pagenumber = fieldData_content.data('pagenumber');
		if (pagenumber) {
			//页数+1
			var next_page = parseInt(pagenumber) + 1;
		} else {
			//如果绑定的data没值,下拉直接第二页
			var next_page = 2;
		}
		//修改data中的页数
		fieldData_content.data('pagenumber', next_page);
		//加载数据
		fieldData.getFieldData();
	},
	/**
	* 下拉回到第一页
	*/
	pullDownAction : function() {
		fieldData_content.data('pagenumber', 1);
		fieldData.getFieldData();
	},
	loadPage : function(){
		//debugger;
		var relTotalPages = $("#fieldDataDiv").data("totalPages");
		$('.bbb-pagination').twbsPagination({
			totalPages: relTotalPages,
			visiblePages: 7,
			paginationClass: "bbb-pagination",
			first: "首页",
			prev: "上一页",
			next: "下一页",
			last: "尾页",
			onPageClick: function (event, page) {
				$("#fieldDataDiv").data("pagenumber",page);
				fieldData.getFieldData();
			}
		});
	},
	getDataCount : function(){
		//appFormTableController.do?getTableDataSum&formCode=zmb1204
		var formCode = '${formCode}'; 
		$.ajax({
			url : 'appFormTableController.do?getTableDataSum&formCode='+formCode,
			type : 'post',
			data: '',
			dataType: "json",
			success : function(result) {
				if(result.success ){
					if(result.obj.pageCount != null && result.obj.pageCount != "" && typeof(result.obj.pageCount) != "undefined"){
						$("#fieldDataDiv").data("totalPages",result.obj.pageCount);
						fieldData.loadPage();
					}
				}else{
					
				}
			}
			
		});	
	}

};

$(function(){
	fieldData_content.data('pagenumber', 1);
	iscrollAssist.initDOM(fieldData_content, fieldData.pullDownAction, fieldData.pullUpAction);
	//fieldData.getDataCount();
	fieldData.getFieldData();
})
</script>
