<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<link href="basic/css/bootstrap.min.css" rel="stylesheet" />
<link href="basic/css/beyond.min.css" rel="stylesheet" type="text/css" />
<link href="basic/css/beyond-rewrite.css" rel="stylesheet" type="text/css" />
<script src="basic/js/jquery-2.0.3.min.js"></script>
<script src="basic/js/jquery-migrate-1.1.0.js"></script>
<script src="basic/js/bootstrap.min.js"></script>
<script src="basic/js/skins.min.js"></script>
<script src="basic/js/beyond.min.js"></script>
<script type="text/javascript">
	$(function() {
		var data=${data};
		loadTimeline(data);
// 		var taskOpinionsIframe = $(window.parent.document).find("#taskOpinionsIframe");
// 		if(taskOpinionsIframe[0]){
// 			window.parent.setIframeLoadedHeight(taskOpinionsIframe[0]);
// 		}
	});
	
	function loadTimeline(data){
		var timelineHtml = "";
		for(var i = 0; i < data.data.length; i++){
			var timelineData = data.data[i];
			timelineHtml +="";/*  "<li class='timeline-node'>"
						        +"<a class='btn' style='border:0px'>"+timelineData.t_lable+"</a>"
						    +"</li>"; */
				
			for(var j = 0; j < timelineData.t_items.length; j++){
				var flag=j%2;
				var itemData = timelineData.t_items[j];
				if(flag==0){
					timelineHtml += "<li>";
				}else if(flag==1){
					timelineHtml += "<li class='timeline-inverted'>";
				}
				timelineHtml += "<div class='timeline-datetime'>"
						            /* +"<span class='timeline-time'>"+itemData.t_item_info_time+"</span>" */
						       +"</div>"
						       +"<div class='timeline-badge'>"
						       		+"<img style='border-radius:40px' width='40' height='40' src='"+(itemData.t_item_info_img==null?"basic/img/avatars/avatar_80.png":"${attachForeRequest}"+itemData.t_item_info_img)+"'/>"
						       +"</div>"
						       +"<div class='timeline-panel bordered-right-3 '>"
						            +"<div class='timeline-body'>"
							            +"<div class='clearfix'>"
							                +"<div class='' style='float:left;padding:5px 0 0 0'>"+itemData.t_item_content_header_task+"</div>"
							                +"<div class='' style='float:right;padding:5px 10px 0 0'>"+itemData.t_item_info_time+"</div>"
							            +"</div>"
							            +"<div class='clearfix'>"
							                +"<div class='' style='float:left;padding:5px 0 0 0'>"+((itemData.t_item_content_header_taskId!=null&&itemData.t_item_content_header_user!=null)?itemData.t_item_content_header_user:'')+"</div>"
							                +"<div class='' style='float:right;padding:5px 10px 0 0'>"+(itemData.t_item_content_header_cost!=null?itemData.t_item_content_header_cost:'')+"</div>"
							            +"</div>"
							            +"<div>"
							                +"<div class='timeline-approve' style='clear:both;padding:5px 10px 0 0'>"+(itemData.t_item_content_header_apporveStr==null?'':itemData.t_item_content_header_apporveStr)+"</div>"
							            +"</div>" 
						                +"<p style='clear:both;padding:0 10px 0 24px; word-break:break-all;'>"+(itemData.t_item_content_body_content==null?"":itemData.t_item_content_body_content)+"</p>"
						            +"</div>"
						        +"</div>"
							+"</li>";
							
							
							
			}
		}
		$(".timeline").html(timelineHtml);
	}
</script>

<div style="margin-left:0px 0 0 0;padding:30px 4px 0 0;height:100%;background:#f2f2f2 none repeat scroll 0 0">
	<ul class="timeline"></ul>
</div>


