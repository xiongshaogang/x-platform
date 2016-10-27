<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <script src="plug-in/echarts/echarts.js"></script>
 <script type="text/javascript">
 var myChart = null;
 var showType = '${param.showType}';
 var parameter = "";
 var condition = "";
 require.config({
     paths: {
    	 echarts: 'plug-in/echarts'
     }
 });
 // 使用
 require(
     [
         'echarts',
         'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
         'echarts/chart/line', // 使用线形图就加载bar模块，按需加载
         'echarts/chart/funnel', // 使用饼图就加载bar模块，按需加载
         'echarts/chart/pie'
     ],
     function (ec) {
       // 基于准备好的dom，初始化echarts图表
       myChart = ec.init(document.getElementById('containerline')); 
     }
 );
 
 $(function(){
	 if( showType == 'linechart'){
		 $("#showType").val("line");
	}else if(showType == 'histogram'){
		 $("#showType").val("bar");
	}else{
		$("#showType").val("pie");
	} 
 });
 
 
	function setdefault(){
		var val = $("#yAxis").combobox("getData");
		if(val[0]){
		 $('#yAxis').combobox('setValue',val[0].name);
		}
    }
	
	function checkPic(str){
        if(str == 'pie'){
        	if($("#yAxis").val()==''){
        		$.messager.alert("提示信息","需查看饼图请先选择对应的Y轴","info");
	        	return false;
        	}
        }
        $("#showType").val(str);
		var chart;
		$.ajax({
			type : "POST",
			data :{
				reportType : str,
				datasourceId : '${datasourceId }',
				yAxis : $("#yAxis").val(),
				parameter : parameter,
			    condition : condition
			},
			url : "statisticsController.do?getDataECharts",
			success : function(data) {
				//data = eval(jsondata);
				data = $.parseJSON(data);
				var  option = "";
				if(str != 'pie'){
		        	option = {
								    title : {
								        text: data.name,
								    },
								    tooltip : {
								        trigger: 'axis'
								    },
								    legend: {
								        data:data.dataType
								    },
								    toolbox: {
								        show : true,
								        feature : {
								            //mark : {show: true},
								            //dataView : {show: true, readOnly: false},
								            magicType : {show: true, type: ['line', 'bar']},
								            restore : {show: true},
								            saveAsImage : {show: true}
								        }
								    },
								    calculable : true,
								    xAxis : [
								        {
								            type : 'category',
								            boundaryGap : false,
								            data : data.xAxis
								        }
								    ],
								    yAxis : [
								        {
								            type : 'value'
								        }
								    ],
								    series : data.data
								};
		        	if(str == "bar"){
		        		option.xAxis[0].boundaryGap = true;
		        	}
				}else{
					option = {
						    title : {
						    	text: data.name,
						        x:'center'
						    },
						    tooltip : {
						        trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    legend: {
						        orient : 'vertical',
						        x : 'left',
						        data:data.xAxis
						    },
						    toolbox: {
						        show : true,
						        feature : {
						            //mark : {show: true},
						           // dataView : {show: true, readOnly: false},
						          //  magicType : {
						           //     show: true, 
						          //      type: ['pie', 'funnel'],
						          //  },
						            restore : {show: true},
						            saveAsImage : {show: true}
						        }
						    },
						    calculable : true,
						   // series : data.data
						      series :[{
						    	name : data.data.name,
						    	type : data.data.type,
						    	radius : "70%",
						    	center : ['50%', '60%'],
						    	data : data.data.data
						    }]  
						};
				}
				myChart.clear();
				myChart.setOption(option); 
			}
		});
	}
	function changeECharts(data){
		parameter = data.exParam.parameter;
		condition = data.exParam.condition;
		checkPic($("#showType").val());
	}
	
  </script>
<div class="easyui-layout" style="width:100%;height:100%;padding:0px;border:0px;">  
  <div region="north" style="height:240px;padding:0px;border:0px;">
  <div id="containerline" style="float:left;width:100%; height:85%;overflow: hidden;"></div>
  <div align="center" >
  <span class="search_button_area">
  <a href="#" class="easyui-linkbutton" iconCls="awsm-icon-search" onclick="checkPic('line');">线性图</a>
  <a href="#" class="easyui-linkbutton" iconCls="awsm-icon-search" onclick="checkPic('bar');">柱状图</a>
  <a href="#" class="easyui-linkbutton" iconCls="awsm-icon-search" onclick="checkPic('pie');">饼状图</a>
  Y轴(饼图)：<t:comboBox url="statisticsController.do?getyAxis&datasourceId=${datasourceId }" textField="showName" valueField="name" name="yAxis" id="yAxis"  onLoadSuccess="setdefault()"></t:comboBox>
  <input type="hidden" name="showType" id="showType" value=""/>
  </span>
  </div>
  </div>
  <div region="center"  style="padding:0px;border:0px;">
  <t:datagrid name="dataStatisticsList"  checkbox="true"  fitColumns="true" onLoadSuccess="changeECharts" actionUrl="statisticsController.do?datagridView&datasourceId=${datasourceId }" idField="id" fit="true" statistics="true" queryMode="group"  pagination="false"    pageSize="1000" border="0">
  <c:forEach var="field" items="${list }" begin="0" step="1">
  <c:set var="show">true</c:set>
  <c:set var="query">false</c:set>
  <c:if test="${field.isshow =='N' }"><c:set var="show">false</c:set></c:if>
  
  <c:set var="searchCondition">${field.searchCondition }</c:set>
  <c:if test="${field.issearch =='Y' }">
  <c:set var="query">true</c:set>
  <c:set var="searchActivex">${field.searchActivex }</c:set>
  <c:if test="${field.searchCondition =='BT'}">
  <c:set var="Condition">group</c:set>
  </c:if>
  <c:if test="${field.searchCondition !='BT'}">
  <c:set var="Condition">single</c:set>
  </c:if>
  </c:if>
  <c:if test="${searchActivex ==''}">
  <c:set var="searchActivex">text</c:set>
  </c:if>
   <c:if test="${Condition ==''}">
  <c:set var="Condition">single</c:set>
  </c:if>
  
  <c:if test="${searchCondition ==''}">
    <c:set var="searchCondition">EQ</c:set>
  </c:if>
  <c:if test="${field.issearch =='N' }"><c:set var="searchActivex">text</c:set><c:set var="Condition">single</c:set><c:set var="searchCondition">EQ</c:set></c:if>
  <t:dgCol title="${field.showName }"  field="${field.name }"  hidden="${ show}"  width="120" query="${query }"  queryInputType="${searchActivex }" queryMode="${Condition }" dictCode="${field.dictCode }" compareSign="${searchCondition }" ></t:dgCol>
  </c:forEach>
  </t:datagrid>
  </div>
 </div>
 <input type="hidden" id="nodeid">
