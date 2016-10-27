<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" style="width:100%;height:100%;border:0px">  
    <div region="west" split="true" title="统计分类树" style="width:200px;">
    <t:tree id="dataStatistics_tree"   
			url="statisticsController.do?tree&sysType=statistics"  onlyLeafClick="true"
			clickPreFun="clickTree(node)">
	</t:tree>
    </div>  
  <div region="center"  style="border:0px">
  <div id="sub_result" ></div>
  </div>
 </div>
 <script type="text/javascript">
  $(function(){
	 $('#sub_result').panel({   
		 title : '数据展示',   
         href : 'statisticsController.do?statisticsViewSample',
         fit : true
	});  
     }); 
  
    function clickTree(node){
    	if(node.attributes.sysType == 'stat'){
    		$('#sub_result').panel({   
      		    title : '数据展示',   
                href : 'statisticsController.do?statisticsView&datasourceId='+node.id+'&showType='+node.attributes.showType,
                fit : true
      	});  
    	}
    }
</script>
