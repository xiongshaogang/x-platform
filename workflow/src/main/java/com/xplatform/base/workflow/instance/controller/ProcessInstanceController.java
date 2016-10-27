package com.xplatform.base.workflow.instance.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.bpm.graph.ShapeMeta;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.mybatis.vo.InstanceVo;
import com.xplatform.base.workflow.instance.service.ProcessInstHistoryService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

/**
 * 
 * description : 流程实例管理controller
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:32:17
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:32:17
 *
 */
@Controller
@RequestMapping("/processInstanceController")
public class ProcessInstanceController extends BaseController {
	
	@Resource
	private ProcessInstHistoryService processInstHistoryService;
	@Resource
	private ProcessInstanceService processInstanceService;
	@Resource
	private DefinitionService definitionService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private HistoryService historyService;
	@Resource
	private FlowService flowService;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private TaskMessageService taskMessageService;
	
	private Page<InstanceVo> page = new Page<InstanceVo>(PAGESIZE);
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 流程实例管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "processInstance")
	public ModelAndView processInstance(HttpServletRequest request) {
		String defId=request.getParameter("defId");
		if(StringUtil.isNotEmpty(defId)){
			request.setAttribute("defId", defId);
		}
		return new ModelAndView("workflow/processInstance/processInstanceList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param ProcessInstance
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ProcessInstanceEntity processInstance,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ProcessInstanceEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, processInstance, request.getParameterMap());
		try{
			
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.processInstanceService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 流程实例删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param ProcessInstance
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(ProcessInstanceEntity processInstance, HttpServletRequest request) {
		message = "流程实例删除成功";
		try{
			processInstanceService.delete(processInstance.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids=request.getParameter("ids");
		try {
			processInstanceService.batchDelete(ids);
			message="删除成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "processInstanceEdit")
	public ModelAndView processInstanceEdit(ProcessInstanceEntity processInstance, Model model,
			HttpServletRequest request) {
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			processInstance = processInstanceService.get(id);
			if (processInstance == null) {
				model.addAttribute("processInstance", processInstHistoryService.getByActInstanceId(id));
			} else {
				model.addAttribute("processInstance", processInstance);
			}

		}
		return new ModelAndView("workflow/processInstance/processInstanceEdit");
	}
	
	/**
	 * 新增或修改流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param ProcessInstance
	 * @return
	 */
	@RequestMapping(params = "saveProcessInstance")
	@ResponseBody
	public AjaxJson saveProcessInstance(ProcessInstanceEntity ProcessInstance) {
		try {
			if (StringUtil.isNotEmpty(ProcessInstance.getId())) {
				message = "流程实例更新成功";
				processInstanceService.update(ProcessInstance);
			} else {
				message = "流程实例新增成功";
				processInstanceService.save(ProcessInstance);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping( params = "processImage" )
	public ModelAndView processImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv=new ModelAndView("workflow/processInstance/processInstanceImage");
		String action = request.getParameter("action");
		String actInstanceId = request.getParameter("actInstId");
		ProcessInstanceEntity	processRun = this.processInstanceService.getByActInstanceId(actInstanceId);
		String actDefId=null;
		if (processRun == null) {
			ProcessInstHistoryEntity hisProcessRun = this.processInstHistoryService.getByActInstanceId(actInstanceId);
			if (hisProcessRun != null) {
				actDefId = hisProcessRun.getActDefId();
			}
		}else{
			actDefId=processRun.getActDefId();
		}
		String defXml = this.flowService.getDefXmlByProcessDefinitionId(actDefId);
		ExecutionEntity executionEntity = this.flowService.getExecution(actInstanceId);

		if ((executionEntity != null)&& (executionEntity.getSuperExecutionId() != null)) {
			ExecutionEntity superExecutionEntity = this.flowService.getExecution(executionEntity.getSuperExecutionId());
			mv.addObject("superInstanceId", superExecutionEntity.getProcessInstanceId());
		}
		ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);
		return mv.addObject("notShowTopBar",request.getParameter("notShowTopBar"))
				.addObject("defXml",defXml).addObject("actInstId", actInstanceId)
				.addObject("shapeMeta", shapeMeta).addObject("processRun",processRun)
				.addObject("action", action);
	}
	
	@RequestMapping(params = "subFlowImage")
	public ModelAndView subFlowImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subProcessDefinitionId = null;
		List subProcessInstanceId = new ArrayList();
		String subDefXml = null;
		String actInstanceId = request.getParameter("processInstanceId");
		String processDefinitionId = request.getParameter("processDefinitionId");
		String nodeId = request.getParameter("nodeId");

		int subProcessRun = 0;

		String subFlowKey = null;
		String actDefId = null;
		if (StringUtil.isNotEmpty(actInstanceId))
			actDefId = this.processInstanceService.getByActInstanceId(actInstanceId).getActDefId();
		else if (StringUtil.isNotEmpty(processDefinitionId)) {
			actDefId = processDefinitionId;
		}

		Map<String,FlowNode> flowNodeMap = NodeCache.getByActDefId(actDefId);
		for(Map.Entry<String, FlowNode> entry:flowNodeMap.entrySet()){
			String flowNodeId =  entry.getKey();
			if (flowNodeId.equals(nodeId)) {
				FlowNode flowNode = entry.getValue();
				subFlowKey = flowNode.getAttribute("subFlowKey");
				break;
			}
		}

		DefinitionEntity subBpmDefinition = this.definitionService.getMainDefByActDefKey(subFlowKey);
		if (subBpmDefinition.getActDeployId() != null)
			subDefXml = this.flowService.getDefXmlByDeployId(subBpmDefinition.getActDeployId().toString());
		else {
			subDefXml = BpmUtil
					.transform(subBpmDefinition.getActKey(), subBpmDefinition.getName(), subBpmDefinition.getDefXml());
		}

		List<HistoricProcessInstance> historicProcessInstances = this.historyService
				.createHistoricProcessInstanceQuery().superProcessInstanceId(actInstanceId).list();
		if (BeanUtils.isNotEmpty(historicProcessInstances)) {
			for (HistoricProcessInstance instance : historicProcessInstances) {
				String procDefId = instance.getProcessDefinitionId();
				DefinitionEntity bpmDef = this.definitionService
						.getByActDefId(procDefId);
				if (bpmDef.getActKey().equals(subFlowKey)) {
					subProcessInstanceId.add(instance.getId());
					subProcessRun = 1;
				}
			}
		}
		if (subProcessRun == 0) {
			subProcessDefinitionId = subBpmDefinition.getActId();
		}

		ShapeMeta subShapeMeta = BpmUtil.transGraph(subDefXml);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("defXml", subDefXml);
		modelAndView.addObject("subProcessRun", Integer.valueOf(subProcessRun));
		if (subProcessRun == 0)
			modelAndView.addObject("processDefinitionId",
					subProcessDefinitionId);
		else {
			modelAndView.addObject("processInstanceIds", subProcessInstanceId);
		}
		modelAndView.addObject("shapeMeta", subShapeMeta);
		return modelAndView;
	}
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年11月10日 下午6:26:46
	 * @Decription 进入追回与撤销页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params="redoOrRecoverTask")
	public ModelAndView redoOrRecoverTask(HttpServletRequest request, HttpServletResponse response) {
		String actInstId = request.getParameter("actInstId");
		String backToStart = request.getParameter("backToStart");
		request.setAttribute("actInstId", actInstId);
		request.setAttribute("backToStart", backToStart);
		return new ModelAndView("workflow/task/redoOrRecoverTask");
	}
	
	/**
	 * 流程追回与撤销
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="recover")
	@ResponseBody
	public AjaxJson recover(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		String actInstId = request.getParameter("actInstId");
		String informType = request.getParameter("informType");
		String memo = request.getParameter("result");
		String backToStart = request.getParameter("backToStart");
		try {
			if (StringUtil.equals(backToStart, "1")) {//撤销，撤销就是返回到流程发起人
				this.processInstanceService.executeRecover(actInstId, informType, memo);
				result.setMsg("撤销成功");
			} else {//任务追回
				this.processInstanceService.executeRedo(actInstId, informType, memo);
				result.setMsg("追回成功");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			if (StringUtil.equals(backToStart, "1")){
				result.setMsg("撤销操作出错,错误信息:"+e.getMessage());
			}else{
				result.setMsg("追回操作出错,错误信息:"+e.getMessage());
			}
			
		}
		return result;
	}
	
	
	
	/**
	 * 是否允许转发
	 * @author xiehs
	 * @createtime 2014年10月9日 上午11:56:44
	 * @Decription
	 *
	 * @param bpmDefinition
	 * @param processRun
	 * @return
	 */
	private boolean isFinishedDivert(DefinitionEntity bpmDefinition,String status) {
		if (DefinitionEntity.STATUS_DISABLED.equals(bpmDefinition.getStatus()))
			return false;
		if (BeanUtils.isNotEmpty(bpmDefinition.getAllowFinishedDivert())) {
			return (StringUtil.equals(bpmDefinition.getAllowFinishedDivert(), "1")
					&& StringUtil.equals(status, ProcessInstanceEntity.STATUS_FINISH.toString()));
		}
		return false;
	}

	
	@RequestMapping(params = "requestInstance")
	public ModelAndView requestInstance(HttpServletRequest request) {
		return new ModelAndView("workflow/processInstance/requestInstanceList");
	}

	/**
	 * 我的请求
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param job
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "requestInstanceDatagrid")
	@ResponseBody
	public AjaxJson requestInstanceDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		//自动封装查询条件
		this.buildFilter(page, request); 
		//手动添加查询条件
		Map<String,String> param=new HashMap<String,String>();
		param.put("userId", user.getId());
		page.setParameter(param);
		//添加排序字段
		this.buildOrder(page,request,"createTime",Page.DESC);
		page =this.processInstanceService.queryRequestInstanceList(page);
		
		if(StringUtil.equals("1",request.getParameter("isApp") )){
			result.setObj(page.getResult());
			result.setMsg("获取成功");
			return result;
		}else{
			dataGrid.setResults(page.getResult());
			dataGrid.setTotal((int)page.getTotalCount());
			TagUtil.datagrid(response, dataGrid);
			return null;
		}
	}

	
	@RequestMapping(params = "completeInstance")
	public ModelAndView completeInstance(HttpServletRequest request) {
		return new ModelAndView("workflow/processInstance/completeInstanceList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param job
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "completeInstanceDatagrid")
	public void completeInstanceDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		//自动封装查询条件
		this.buildFilter(page, request); 
		//手动添加查询条件
		Map<String,String> param=new HashMap<String,String>();
		param.put("userId", user.getId());
		page.setParameter(param);
		//添加排序字段
		this.buildOrder(page,request,"create_time",Page.ASC);
		page =this.processInstanceService.queryCompleteInstanceList(page);
		dataGrid.setResults(page.getResult());
		dataGrid.setTotal((int)page.getTotalCount());
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 流程实例的信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "info")
	public ModelAndView info(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String curUserId = ClientUtil.getUserId();
		String actInstId = request.getParameter("instId");
		String taskId = request.getParameter("id");
		/*String nodeId = request.getParameter("nodeId");*/
		String defId = null;
		String status = null;
		boolean isFirst = false;
		boolean isCanRecover = false;//是否可以撤销，流程实例发起人并且流程没有结束才能撤销
		boolean isCanRedo = false;//是否可以追回
		ProcessInstanceEntity processRun = this.processInstanceService.getByActInstanceId(actInstId);
		String businessKey="";
		if (BeanUtils.isEmpty(processRun)) {
			ProcessInstHistoryEntity historyInst = processInstHistoryService.getByActInstanceId(actInstId);
			defId = historyInst.getDefId();
			status = historyInst.getStatus();
			isFirst = false;
			isCanRecover = false;
			isCanRedo = false;
			businessKey=historyInst.getBusinessKey();
		} else {
			defId = processRun.getDefId();
			status = processRun.getStatus();
			isFirst = this.isFirst(processRun);
			isCanRecover = isCanRecover(processRun, isFirst, curUserId);
			isCanRedo = isCanRedo(processRun, isFirst, curUserId);
			businessKey=processRun.getBusinessKey();
		}

		DefinitionEntity bpmDefinition = this.definitionService.get(defId);

		boolean isFinishedDivert = isFinishedDivert(bpmDefinition, status);//是否允许转发

		NodeSetEntity nodeSet = null;
		TaskOpinionEntity taskOpinion=null;
		if(StringUtil.equals(request.getParameter("isCompleteTask"), "1")){//是已办任务和办结任务
			taskOpinion=this.taskOpinionService.getByTaskId(taskId);
			NodeSetEntity bpmNodeSet = this.nodeSetService.getByActDefIdNodeId(bpmDefinition.getActId(), taskOpinion.getTaskKey());
			/*taskOpinion=this.taskOpinionService.getLatestTaskOpinion(actInstId, nodeId);*/
			//节点表单设置为空，取全局的表单设置
			if (StringUtil.isEmpty(bpmNodeSet.getFormId())&&StringUtil.isEmpty(bpmNodeSet.getFormUrl())) {
				NodeSetEntity globalNodeSet = this.nodeSetService.getGlobalByActDefId(bpmDefinition.getActId());
				nodeSet = globalNodeSet;
			}else{
				nodeSet = bpmNodeSet;
			}
		}else{
			NodeSetEntity bpmNodeSet = this.nodeSetService.getBpmFormByActDefId(bpmDefinition.getActId());
			//若业务综合表单设置了,则优先取业务综合表单
			if (BeanUtils.isNotEmpty(bpmNodeSet)) {
				nodeSet = bpmNodeSet;
			} else {
				NodeSetEntity globalNodeSet = this.nodeSetService.getGlobalByActDefId(bpmDefinition.getActId());
				if (BeanUtils.isNotEmpty(globalNodeSet)) {
					nodeSet = globalNodeSet;
				}
			}
		}
		ModelAndView mv = new ModelAndView();
		if (BeanUtils.isNotEmpty(nodeSet)) {
			//节点表单设置为空，取全局的表单设置
			String fromId = nodeSet.getFormId();
			FlowFormEntity flowForm = flowFormService.get(fromId);
			String formUrl = "";
			if (StringUtil.equals(nodeSet.getFormType(), NodeSetEntity.FORM_TYPE_URL)) {
				formUrl = nodeSet.getFormUrl();
			} else {
				formUrl = flowForm.getUrl();
			}
			mv.addObject("optFlag", "detail");
			mv.setViewName(formUrl);
			if(StringUtil.indexOf(formUrl, ".do")>0){
				mv.setViewName("redirect:"+formUrl+"&formId="+fromId+"&businessKey="+businessKey+"&formCode="+flowForm.getCode()+"&viewType=viewProcess");
			}
		}
		
		return mv.addObject("definition", bpmDefinition).addObject("processInstance", processRun).addObject("businessKey", businessKey)
				.addObject("isFirst", Boolean.valueOf(isFirst)).addObject("isCanRedo", Boolean.valueOf(isCanRedo))
				.addObject("isCanRecover", Boolean.valueOf(isCanRecover))
				.addObject("isFinishedDivert", Boolean.valueOf(isFinishedDivert)).addObject("isWorkFlow", true)
				.addObject("taskId", taskId).addObject("actInstId", actInstId).addObject("taskOpinion",taskOpinion);
	}
	
	/**
	 * 是否是第一个结点
	 * @author xiehs
	 * @createtime 2014年10月9日 上午11:56:54
	 * @Decription
	 *
	 * @param processRun
	 * @return
	 * @throws Exception
	 */
	private boolean isFirst(ProcessInstanceEntity processRun) {
		boolean isFirst = false;
		if (BeanUtils.isEmpty(processRun))
			return isFirst;
		String instId = processRun.getActInstId();
		String actDefId = processRun.getActDefId();
		List<TaskOpinionEntity> taskOpinionList = this.taskOpinionService.getCheckOpinionByInstId(instId);
		FlowNode flowNode=null;
		try {
			flowNode = NodeCache.getFirstNodeId(actDefId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (flowNode == null)
			return isFirst;
		String nodeId = flowNode.getNodeId();
		for (TaskOpinionEntity taskOpinion : taskOpinionList) {
			isFirst = nodeId.equals(taskOpinion.getTaskKey());
			if (isFirst)
				break;
		}
		return isFirst;
	}
		
	/**
	 * 是否可以撤销
	 * @param processRun
	 * @param isFirst
	 * @param curUserId
	 * @return
	 */
	private boolean isCanRecover(ProcessInstanceEntity processRun, boolean isFirst,String curUserId) {
		String actDefId = processRun.getActDefId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		if (DefinitionEntity.STATUS_DISABLED.equals(bpmDefinition.getStatus())){
			return false;
		}
		return (!isFirst)&& (curUserId.equals(processRun.getCreateUserId()))&& (StringUtil.equals(processRun.getStatus(), ProcessInstanceEntity.STATUS_RUNNING.toString()));
	}

	/**
	 * 是否可以追回,
	 * @param processRun
	 * @param isFirst
	 * @param curUserId
	 * @return
	 */
	private boolean isCanRedo(ProcessInstanceEntity processRun, boolean isFirst,String curUserId) {
		if ((!processRun.getStatus().equals(ProcessInstanceEntity.STATUS_RUNNING.toString()))|| (isFirst)){
			return false;
		}
		String actDefId = processRun.getActDefId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		if (DefinitionEntity.STATUS_DISABLED.equals(bpmDefinition.getStatus())){
			return false;
		}
		String instanceId = processRun.getActInstId();
		//登陆用户在流程实例最近的审批结果
		TaskOpinionEntity taskOpinion = this.taskOpinionService.getLatestUserOpinion(instanceId, curUserId);
		if (taskOpinion != null) {
			Integer checkStatus = taskOpinion.getCheckStatus();
			if (TaskOpinionEntity.STATUS_AGREE.intValue()==checkStatus) {
				String taskKey = taskOpinion.getTaskKey();
				FlowNode flowNode = NodeCache.getNodeByActNodeId(processRun.getActDefId(), taskKey);
				if (flowNode != null) {
					List<FlowNode> nextNodes = flowNode.getNextFlowNodes();
					List<String> nodeKeys = new ArrayList<String>();
					for (FlowNode node : nextNodes) {
						nodeKeys.add(node.getNodeId());
					}
					List<ProcessTask> tasks = this.flowService.getTasks(instanceId);
					if (tasks.size() != 1){
						return false;
					}
					if (nodeKeys.contains((tasks.get(0)).getTaskDefinitionKey())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	@RequestMapping(params="getFormByParentBusinessKey")
	@ResponseBody
	public AjaxJson getFormByParentBusinessKey(HttpServletRequest request, HttpServletResponse response){
		String businessKey=request.getParameter("businessKey");
		String formId="";
		String formCode=request.getParameter("formCode");

		try { 
			if(StringUtil.isNotEmpty(formCode)){
				FlowFormEntity flowFormEntity = this.flowFormService.getFlowFormByCode(formCode);
				formId = flowFormEntity.getId();
			}
			if(StringUtil.isNotEmpty(formId)){
				List<FlowFormEntity> flowFormList=flowFormService.queryRelaFlFoList(formId,formCode);
				if(flowFormList!=null && flowFormList.size()>0){
					if(StringUtil.isNotEmpty(businessKey)){
						 List<ProcessInstanceEntity> processList =this.processInstanceService.getProcessByParentBusinessKey(businessKey);
						 if(processList!=null && processList.size()>0){
							 for(FlowFormEntity flowForm:flowFormList){
								 for(ProcessInstanceEntity process:processList){
									 if(StringUtil.equals(flowForm.getCode(),process.getFormCode())){
										 flowForm.setStatus(3);
										 break;
									 }
								 }
							 }
						 }
					}
				}
				result.setObj(flowFormList);
				result.setMsg("获取成功");
			}else{
				result.setObj(null);
				result.setMsg("参数有误");
			}
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setMsg("获取失败");
		}
		
		return result;
		
	}

	/**
	 * 发送传阅消息
	 * @param request
	 * @param response
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params="sendNotifyMessage")
    @ResponseBody
    public AjaxJson sendNotifyMessage(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Map<String, Object> vars = new HashMap<String, Object>();
        String finalValue = request.getParameter("finalValue");
        String businessKey = request.getParameter("businessKey");
        String formCode = request.getParameter("formCode");
        vars.put("businessKey", businessKey);
        vars.put("formCode", formCode);
        try {
            vars.put("msgTemplateCode", MsgTemplateEntity.USE_TYPE_CIRCULATE);
            vars.put("businessKey", businessKey);
            vars.put("formCode", formCode);
            vars.put("sourceType", BusinessConst.SourceType_CODE_circulate);
            vars.put("sourceBusinessType", formCode);

            List<String> users=sysUserService.getDistinctMulUserIds(finalValue);
            taskMessageService.sendMessage(StringUtil.asString(users, ","), vars);
            j.setMsg("传阅成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("传阅失败");
        }
        return j;
    }
	
}
