<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<script type="text/javascript" src="plug-in/qtip/jquery.qtip.js"></script>
<link rel="stylesheet" href="plug-in/qtip/jquery.qtip.css" type="text/css">
<link rel="stylesheet" href="plug-in/workflow/css/definition.node.css" type="text/css">


<style type="text/css"> 
    div.flowNode{cursor:pointer;}
</style>

<script type="text/javascript">
	var defId ="${definition.id}";
	var actDefId ="${definition.actId}";
	var deployId= "${definition.actDeployId}";
       var menu;
       //判断参数是否加载
       var isOtherParamFrm=false;
       var currentObj=null;
       $(function (){
       		setContainerHeightWidth();
            $("div.flowNode").each(function(){                	
           	$(this).bind({mouseenter:function(){
           		currentObj=$(this);
           	}});
           	currentObj=$(this);
           	var type=currentObj.attr("type"),title=currentObj.attr("title");
           	var items= getItems(type);
           	if(!items)return;
           	if(items.length==0)return;
           	var item,html=['<div class="edui-menu-body">'];
           	while(item=items.pop()){
           		if(item.id=='-'){
           			html.push('<div class="edui-menuitem edui-menuseparator"><div class="edui-menuseparator-inner"></div></div>');
           		}
           		else{
           			html.push('<div class="edui-menuitem edui-for-'+item.id+'" onmouseover="$(this).addClass(\'edui-state-hover\')" onmouseout="$(this).removeClass(\'edui-state-hover\')" onclick="clickHandler(this.id)" id="'+item.id+'"><div class="edui-box edui-icon"></div><div class="edui-box edui-label edui-menuitem-label">'+item.text+'</div></div>');
           		}
           	}
          html.push('</div>');
	      $(this).qtip({  
	        content:{
	        	text:html.join(''),
	        	title:{
	        		text:title
	        		}
	        	},  
	        position: {
	        	at:'center',
	        	target:'event',
	        	adjust: {
	        		x:-15,
	        		y:-15
   				},  
   				viewport: $(window)
	        },
	        show:{   			        	
		     	effect: function(offset) {
					$(this).slideDown(200);
				}
	        },   			     	
	        hide: {
	        	event:'click mouseleave',
	        	leave: false,
	        	fixed:true,
	        	delay:300
	        	},  
	        style: {
	       	  classes:'ui-tooltip-light ui-tooltip-shadow'
	        } 			    
	      });
	    }); 
       });
       
       function setContainerHeightWidth(){
	      var h=document.documentElement.clientHeight ;
	      var w=$(".panel-container").width();
	      $("#divContainer").height(h-45).width(w);
       }
       
       function getItems(type) {
       	 var items=[];
       	 switch(type){
       		case "startEvent":
       			items= [{id:'flowEvent', text: '事件设置'}];
       			break;
       		case "endEvent":
       			items= [{id:'flowEvent', text: '事件设置' }];
       			break;
       		case "parallelGateway":
       			items= [];
       			break;
       		case "inclusiveGateway":
       		case "exclusiveGateway":
       			items= [{id:'flowCodition', text: '设置分支条件' }];
       			break;
       		case "multiUserTask":
       			items= [{id:'-'},
       			        {id:'flowRule', text: '跳转规则设置' },
           		        {id:'flowVote', text: '会签投票规则设置' },
           		        {id:'-'},
           		        {id:'flowEvent', text: '事件设置' },
           		        {id:'flowDue', text: '任务催办设置' },
           		        {id:'-'},
           		        {id:'flowApproval', text: '常用语设置' },
           		        {id:'informType',text:'通知方式'}];
       			break;
       		case "userTask":
       			items= [{id:'-'},
           		        /* {id:'flowRule', text: '跳转规则设置' }, */
           		        {id:'flowEvent', text: '事件设置' },
           		        {id:'-'},
           		        {id:'flowDue', text: '任务催办设置' },
           		        /* {id:'flowForkJoin', text: '流程分发汇总' }, */
           		        {id:'-'},
           		        {id:'flowApproval', text: '常用语设置' },
           		        {id:'informType',text:'通知方式'}];
       			break;
       		case "webService":
       			items= [{id:'webServiceSet',text:'WebService设置'}];
       			break;
       		case "email":
       			items= [{id:'flowMessage', text: '消息参数' }];
       			break;
       		case "script":
       			items= [{id:'flowEvent', text: '设置脚本' }];
       			break;
       		case "callActivity":
       			items= [
       			        {id: 'viewSubFlow',text: '子流程示意图'},
       			        {id: 'flowSet', text: '设置子流程'}];
       			break;
       		case "subProcess":
       			items= [{id: 'flowEvent',text: '事件设置'}];
       			break;
       	 }
       	 return items;
       }

       var signRule;
       var flowRule;
       var forkCondition;

       function clickHandler(itemId){
       	//节点类型
       	var type=currentObj.attr("type");            	
       	//任务id
       	var activitiId=currentObj.attr("id");
       	var activityName=currentObj.attr("title");
       	
       	if(itemId=="flowVote" && type=="multiUserTask"){
       		createwindow("会签投票规则设置", "nodeSignController.do?nodeSign&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId, null, 400, 2,{optFlag:'add',noheader:'true',formId:'nodeSignFormValid'});
       	}
       	else if(itemId=="flowRule"){
       		//FlowRuleWindow({deployId:deployId,actDefId:actDefId,nodeId:activitiId,nodeName:activityName});
       		createwindow("跳转规则设置", "nodeRuleController.do?NodeRule&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId+"&deployId="+deployId+"&nodeName="+activityName, 650, 500, null,{optFlag:'add'});
       	}
       	else if(itemId=="webServiceSet"){
       		//alert("webservice设置");
           	FlowWebServiceWindow({actDefId:actDefId,nodeId:activitiId,defId:defId});
       	}
       	else if(itemId=="flowCodition"){
       		createwindow("分支条件设置", "definitionController.do?setCondition&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId+"&deployId="+deployId+"&nodeName="+activityName, null, 400, 1,{optFlag:'add'});
      	}
       	else if(itemId=="flowMessage"){
       		//FlowMessageWindow({actDefId:actDefId,nodeId:activitiId});	
       		createwindow("消息参数设置", "nodeMessageController.do?nodeMessage&nodeId="+activitiId+"&actDefId="+actDefId, null, 530, 2,{optFlag:'add',noheader:'true',formId:'nodeMessageformobj'});
       	}
       	else if(itemId=="flowEvent"){
       		createwindow("脚本设置", "nodeScriptController.do?nodeScript&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId+"&type="+type, 500, 350, null,{optFlag:'add',noheader:'true',formId:'nodeScriptformobj'});
       	}
       	else if(itemId=="flowDue"){
       		createwindow("任务催办设置", "taskDueController.do?taskDue&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId, 860, 500, 2,{optFlag:'add',formId:'taskDueformobj'});
       	}else if(itemId=="flowForkJoin"){
       		FlowForkJoinWindow({actDefId:actDefId,nodeId:activitiId,activityName:activityName});
       	}else if(itemId=="flowApproval"){ // 常用语设置
       		createwindow("常用语设置", "approveItemController.do?approveItem&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId, null, 300, 1,{optFlag:'add',formId:'taskApprovalItemsForm'});
       	}else if(itemId=="viewSubFlow"){
       		createwindow("常用语设置", "approveItemController.do?approveItem&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId, null, 300, 1,{optFlag:'add',formId:'taskApprovalItemsForm'});
       	}else if(itemId=="informType"){
       		createwindow("节点通知类型", "nodeSetController.do?infoType&nodeId="+activitiId+"&defId="+defId+"&actDefId="+actDefId, 700, 500, 2,{optFlag:'add'});
       	}else if(itemId=="flowSet"){
       		FlowSetWindow({nodeId:activitiId,defId:defId,actDefId:actDefId});
       	}
       	
    }
</script> 
<div class="easyui-layout" fit="true">
	<div region="center" border="false" style="padding: 5px;">
		<div id="divContainer">
			<div id="nodeSetFlowImage" style="margin:20px auto 0;position: relative;width:${shapeMeta.width}px;height:${shapeMeta.height}px;">
					${shapeMeta.xml} 
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	var bgUrl="url('bpmImage?deployId=${definition.actDeployId}&time="+new Date()+"')  no-repeat";
	$("#nodeSetFlowImage").css("background",bgUrl);
});
</script>
