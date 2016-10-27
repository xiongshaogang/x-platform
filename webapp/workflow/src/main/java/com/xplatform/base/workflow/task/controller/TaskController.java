package com.xplatform.base.workflow.task.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.approve.service.ApproveItemService;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.model.NodeTranUser;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.history.mybatis.vo.ProcessTaskHistory;
import com.xplatform.base.workflow.history.service.HistoryTaskInstanceService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.entity.NodeSignEntity;
import com.xplatform.base.workflow.node.entity.NodeSignPrivilegeEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeSignService;
import com.xplatform.base.workflow.node.service.NodeUserService;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.entity.TaskSignDataEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskExeService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.task.service.TaskReadService;
import com.xplatform.base.workflow.task.service.TaskSignDataService;
import com.xplatform.base.workflow.task.service.TaskUserService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

/**
 * 
 * description :流程任务管理controller
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月19日 上午11:43:27
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月19日 上午11:43:27
 *
 */
@Controller
@RequestMapping("/taskController")
@Scope("prototype")
public class TaskController extends BaseController {
	
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource 
	private FlowService flowService;
	@Resource
	private ProcessInstanceService processInstanceService;
	@Resource
	private DefinitionService definitionService;
	@Resource
	private TaskActService taskActService;
	@Resource
	private TaskReadService taskReadService;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private TaskService taskService;
	@Resource
	private TaskSignDataService taskSignDataService;
	@Resource
	private NodeSignService nodeSignService;
	@Resource
	private TaskExeService taskExeService;
	@Resource
	private ApproveItemService approveItemService;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private NodeUserService nodeUserService;
	@Resource
	private HistoryTaskInstanceService historyTaskInstanceService;
	@Resource
	private AppFormTableService appFormTableService;
	@Resource
	private OrgGroupService orgGroupService;
	
	
	
	private Page<ProcessTask> page = new Page<ProcessTask>(PAGESIZE);
	private AjaxJson result = new AjaxJson();
	
	private String message;
	
	private void handleSignTask(ModelAndView mv, String instanceId,
			String nodeId, String actDefId, String userId) {
		List<TaskSignDataEntity> signDataList = this.taskSignDataService.getByNodeAndInstanceId(instanceId, nodeId);//获取投票数据

		NodeSignEntity nodeSign = this.nodeSignService.getByDefIdAndNodeId(actDefId, nodeId);//获取会签节点设置

		mv.addObject("nodeSignDataList", signDataList);
		mv.addObject("nodeSign", nodeSign);
		
		//允许直接处理
		boolean isAllowDirectExecute = this.nodeSignService.checkNodeSignPrivilege(actDefId, nodeId,NodeSignPrivilegeEntity.ALLOW_DIRECT,userId,instanceId);
		//允许补签
		boolean isAllowRetoactive = this.nodeSignService.checkNodeSignPrivilege(actDefId,nodeId,NodeSignPrivilegeEntity.ALLOW_RETROACTIVE,userId,instanceId);
		//允许一票否决
		boolean isAllowOneVote = this.nodeSignService.checkNodeSignPrivilege(actDefId, nodeId,NodeSignPrivilegeEntity.ALLOW_ONE_VOTE,userId,instanceId);
		
		mv.addObject("isAllowDirectExecute",Boolean.valueOf(isAllowDirectExecute))
		  .addObject("isAllowRetoactive", Boolean.valueOf(isAllowRetoactive))
		  .addObject("isAllowOneVote", Boolean.valueOf(isAllowOneVote));
	}
	
	private boolean getIsHidePath(String isHidePath) {
		if (BeanUtils.isEmpty(isHidePath)) {
			return false;
		}
		return StringUtil.equals(NodeSetEntity.HIDE_PATH, isHidePath.toString());
	}

	@RequestMapping(params="startFlow")
	@ResponseBody
	public AjaxJson startFlow(HttpServletRequest request,HttpServletResponse response) throws Exception {
		message="流程启动成功!";
		ProcessCmd processCmd = BpmUtil.getProcessCmd(request);//获取processCmd对象
		UserEntity user=ClientUtil.getUserEntity();
		processCmd.setCurrentUserId(user.getId());//设置当前用户
		processCmd.setParentBusinessKey(request.getParameter("parentBusinessKey"));
		processCmd.getVariables().put("formCode",request.getParameter("formCode"));
		processCmd.getVariables().put("isStartAssign",request.getParameter("isStartAssign"));
		TaskThreadService.setProcessCmd(processCmd);
		try {
			this.processInstanceService.startProcess(processCmd);
		} catch (Exception e) {
			// TODO: handle exception
			result.setSuccess(false);
			message="流程启动失败";
		}
		result.setMsg(message);
		return result;


	}
	
	/**
	 * @Decription 任务完成跳转
	 * @author xiehs
	 * @createtime 2014年8月19日 下午3:12:01
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="complete")
	@ResponseBody
	public AjaxJson complete(HttpServletRequest request,HttpServletResponse response){
		//获取用户
		UserEntity curUser=ClientUtil.getUserEntity();
		//获取任务
		String taskId = request.getParameter("taskId");
		TaskEntity task = this.flowService.getTask(taskId);
		if (task == null) {
			result.setMsg("此任务已经执行了!");
			return result;
		}
		//获取流程定义
		String actDefId = task.getProcessDefinitionId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		if (DefinitionEntity.STATUS_DISABLED.equals(bpmDefinition.getStatus())) {
			result.setSuccess(false);
			result.setMsg("流程已经禁止，该任务不能办理！");
			return result;
		}
		String userId = curUser.getId();
		try {
			ProcessCmd taskCmd=null;
			try {
				
				taskCmd = BpmUtil.getProcessCmd(request);
				taskCmd.getVariables().put("actDefKey", bpmDefinition.getActKey());
				taskCmd.getVariables().put("isStartAssign",request.getParameter("isStartAssign"));
				taskCmd.setParentNodeId(task.getTaskDefinitionKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taskCmd.setCurrentUserId(userId.toString());

			boolean isAdmin = StringUtil.equals(taskCmd.getIsManage(), "Y");
			if (!isAdmin) {
				//List<String> orgList = ClientUtil.getOrgQuoteIds();
				//List<RoleEntity> roleEntityList = ClientUtil.getRoleList();
				/*List<String> roleList=new ArrayList<String>();
				for(RoleEntity role:roleEntityList){
					roleList.add("'"+role.getId()+"'");
				}*/
				boolean rtn = this.taskActService.getHasRightsByTask(taskId, userId);
				if (!rtn) {
					result.setMsg("对不起,你不是这个任务的执行人,不能处理此任务!");
					return result;
				}
			}
			this.processInstanceService.nextProcess(taskCmd);
			message="任务审批完成!";
		} catch (BusinessException ex) {
			// TODO: handle exception
			message=ex.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * @Decription 任务完成跳转
	 * @author xiehs
	 * @createtime 2014年8月19日 下午3:12:01
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="completeTask")
	@ResponseBody
	public AjaxJson completeTask(HttpServletRequest request,HttpServletResponse response){
		String userId = request.getParameter("userId");
		SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
		//获取用户
		UserEntity curUser=userService.getUserById(userId);
		if(curUser==null){
			result.setSuccess(false);
			result.setMsg("没有此用户");
			return result;
		}
		//获取任务
		String taskId = request.getParameter("taskId");
		TaskEntity task = this.flowService.getTask(taskId);
		if (task == null) {
			result.setMsg("此任务已经执行了!");
			return result;
		}
		//获取流程定义
		String actDefId = task.getProcessDefinitionId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		if (DefinitionEntity.STATUS_DISABLED.equals(bpmDefinition.getStatus())) {
			result.setSuccess(false);
			result.setMsg("流程已经禁止，该任务不能办理！");
			return result;
		}
		try {
			ProcessCmd taskCmd=null;
			try {
				taskCmd = BpmUtil.getProcessCmd(request);
				taskCmd.setParentNodeId(task.getTaskDefinitionKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taskCmd.setCurrentUserId(userId.toString());
			String assignee = task.getAssignee();

			boolean isAdmin = StringUtil.equals(taskCmd.getIsManage(), "Y");
			if (!isAdmin) {
				HttpSession session = ContextHolderUtils.getSession();
				
				boolean rtn = this.taskActService.getHasRightsByTask(taskId, userId);
				if (!rtn) {
					result.setMsg("对不起,你不是这个任务的执行人,不能处理此任务!");
					return result;
				}
			}
			this.processInstanceService.nextProcess(taskCmd);
			message="任务审批完成!";
		} catch (BusinessException ex) {
			// TODO: handle exception
			message=ex.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * @Decription 任务结束
	 * @author xiehs
	 * @createtime 2014年8月19日 下午3:12:01
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="end")
	@ResponseBody
	public AjaxJson end(HttpServletRequest request,HttpServletResponse response){
		//获取用户
		UserEntity curUser=ClientUtil.getUserEntity();
		//获取任务
		String taskId = request.getParameter("taskId");
		TaskEntity task = this.flowService.getTask(taskId);
		if (task == null) {
			result.setMsg("此任务已经执行了!");
			return result;
		}
		//获取流程定义
		String actDefId = task.getProcessDefinitionId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		if (DefinitionEntity.STATUS_DISABLED.equals(bpmDefinition.getStatus())) {
			result.setMsg("流程已经禁止，该任务不能办理！");
			return result;
		}
		String userId = curUser.getId();
		try {
			ProcessCmd taskCmd=null;
			try {
				
				taskCmd = BpmUtil.getProcessCmd(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taskCmd.setCurrentUserId(userId.toString());
			String assignee = task.getAssignee();

			boolean isAdmin = StringUtil.equals(taskCmd.getIsManage(), "Y");
			if (!isAdmin) {
				boolean rtn = this.taskActService.getHasRightsByTask(taskId, userId);
				if (!rtn) {
					result.setMsg("对不起,你不是这个任务的执行人,不能处理此任务!");
					return result;
				}
			}
			String voteContent = "由"+ ClientUtil.getUserEntity().getName() + "进行完成操作！";
			taskCmd.setTaskId(taskId);
			taskCmd.setVoteAgree(Short.valueOf((short) 0));
			taskCmd.setVoteContent(voteContent);
			taskCmd.setOnlyCompleteTask(true);
			
			this.processInstanceService.nextProcess(taskCmd);
			message="任务审批完成!";
		} catch (BusinessException ex) {
			// TODO: handle exception
			message=ex.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * @Decription 任务结束
	 * @author xiehs
	 * @createtime 2014年8月19日 下午3:12:01
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="endProcess")
	@ResponseBody
	public AjaxJson endProcess(HttpServletRequest request,HttpServletResponse response){
		String taskId = request.getParameter("taskId");
		String memo = request.getParameter("result");
		String curUserId = ClientUtil.getUserId();
		TaskEntity taskEnt = this.flowService.getTask(taskId);
		if (taskEnt == null) {
			result.setMsg("此任务已经完成");
			return result;
		}
		String instanceId = taskEnt.getProcessInstanceId();
		try {
			message="流程结束成功!";
			this.flowService.endProcessByInstanceId(instanceId, taskEnt.getTaskDefinitionKey(), memo,curUserId,taskId);
		} catch (BusinessException ex) {
			// TODO: handle exception
			message=ex.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "bacthCompleteEdit")
	public ModelAndView bacthCompleteEdit(HttpServletRequest request,Model model) {
		List<ComboBox> conclusionList = new ArrayList<ComboBox>();
		ComboBox comboBox = new ComboBox();
		comboBox.setId("1");
		comboBox.setText("同意");
		conclusionList.add(comboBox);
		comboBox = new ComboBox();
		comboBox.setId("2");
		comboBox.setText("反对");
		conclusionList.add(comboBox);
		comboBox = new ComboBox();
		comboBox.setId("0");
		comboBox.setText("放弃");
		conclusionList.add(comboBox);
		model.addAttribute("conclusionData", JSONHelper.toJSONString(conclusionList));
		return new ModelAndView("workflow/task/bacthCompleteEdit");
	}
	
	@RequestMapping(params="batchComplete")
	@ResponseBody
	public AjaxJson batchComplete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String taskIds = request.getParameter("taskIds");
			String opinion = request.getParameter("opinion");
			String voteAgree = request.getParameter("voteAgree");
			this.processInstanceService.batchComplte(taskIds, opinion, voteAgree);
			message="任务批量审批完成!";
		} catch (BusinessException ex) {
			// TODO: handle exception
			message=ex.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	@RequestMapping(params = "myTaskList")
	public ModelAndView myTaskList(HttpServletRequest request) {
		return new ModelAndView("workflow/task/myTaskList");
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
	@RequestMapping(params = "myTaskDatagrid")
	public void myTaskDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		//自动封装查询条件
		this.buildFilter(page, request); 
		//手动添加查询条件
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", user.getId());
		
		page.setParameter(param);
		//添加排序字段
		this.buildOrder(page,request,"id",Page.DESC);
		page =this.flowService.getMyTasks(page);
		dataGrid.setResults(page.getResult());
		dataGrid.setTotal((int)page.getTotalCount());
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "myTask")
	@ResponseBody
	public AjaxJson myTask(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		//自动封装查询条件
		this.buildFilter(page, request);
		//手动添加查询条件
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", user.getId());
		page.setPageSize(5);//查询五条
		page.setParameter(param);
		//添加排序字段
		this.buildOrder(page,request,"id",Page.ASC);
		try {
			page =this.flowService.getMyTasks(page);
			result.setObj(page.getResult());
			result.setMsg("获取任务成功");
		} catch (Exception e) {
			// TODO: handle exception
			result.setSuccess(false);
			result.setMsg("获取数据失败");
		}
		return result;
	}
	
	/**
	 * 管理员查看所有的流程任务
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params ="taskList")
	public ModelAndView taskList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("workflow/task/taskList");
	}
	
	@RequestMapping(params = "taskListDatagrid")
	public void taskListDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		//自动封装查询条件
		this.buildFilter(page, request);
		//添加排序字段
		this.buildOrder(page,request,"id",Page.ASC);
		page =this.flowService.getTasks(page);
		TaskUserService taskUserService = ApplicationContextUtil.getBean("taskUserService");
		List<ProcessTask> list=page.getResult();
		if(list!=null && list.size()>0){
			for(ProcessTask task:list){
				if(StringUtil.isEmpty(task.getOrgName())){
					Set<TaskExecutor> candidateUsers = null;
					try {
						candidateUsers = taskUserService.getCandidateExecutors(task.getId());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String orgName="";
					for (TaskExecutor te : candidateUsers) {
						if ("org".equals(te.getType())){
				            if(StringUtil.isEmpty(orgName)){orgName=te.getExecutor();}else{orgName=orgName+","+te.getExecutor();}
						}else if ("role".equals(te.getType())){
							if(StringUtil.isEmpty(orgName)){orgName=te.getExecutor();}else{orgName=orgName+","+te.getExecutor();}
						}else if ("user".equals(te.getType())) {
							if(StringUtil.isEmpty(orgName)){orgName=te.getExecutor();}else{orgName=orgName+","+te.getExecutor();}
						}
					}
					task.setOrgName(orgName);
				}
			}
		}
		dataGrid.setResults(page.getResult());
		dataGrid.setTotal((int)page.getTotalCount());
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 任务认领转到审批界面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "toStart")
	public ModelAndView toStart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv= new ModelAndView();
		String isManager=request.getParameter("isManager");
		if(StringUtil.equals("1", isManager)){
			return getToStartView(request, response, mv, 1);
		}else{
			return getToStartView(request, response, mv, 0);
		}
		
	}
	
	/**
	 * 管理员后台进行审批
	 * @author xiehs
	 * @createtime 2014年10月5日 下午3:08:16
	 * @Decription
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params ="doNext")
	public ModelAndView doNext(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv= new ModelAndView();
		return getToStartView(request, response, mv, 1);
	}
	
	private ModelAndView getToStartView(HttpServletRequest request,
			HttpServletResponse response, ModelAndView mv, int isManage)
			throws Exception {
		ProcessCmd taskCmd=null;
		try {
			
			taskCmd = BpmUtil.getProcessCmd(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserEntity sysUser = ClientUtil.getUserEntity();
		String taskId = request.getParameter("taskId");//获取任务id
		String instanceId = request.getParameter("instanceId");//获取流程实例id

		if ((StringUtil.isEmpty(taskId)) && (StringUtil.isEmpty(instanceId))) {
			//return ServiceUtil.getTipInfo("没有输入任务或实例ID!");
		}

		if (StringUtil.isNotEmpty(instanceId)) {
			List<ProcessTask> list = this.flowService.getTasks(instanceId);
			if (BeanUtils.isNotEmpty(list)) {
				taskId = ((ProcessTask) list.get(0)).getId();
			}
		}

		TaskEntity taskEntity = this.flowService.getTask(taskId);

		if (taskEntity == null) {//如果查询不到任务，那么可能 是已办任务
			ProcessTaskHistory taskHistory = this.historyTaskInstanceService.get(taskId);
			if ((taskHistory == null) && (StringUtil.isEmpty(taskId))&& (StringUtil.isEmpty(instanceId))) {
				return null;//异常处理
			}
			String actInstId = taskHistory.getProcessInstanceId();
			String url = request.getContextPath()+ "processInstanceController.do?info&taskId="+taskId+"&instId="+actInstId;
			response.sendRedirect(url);
			return null;
		}

		instanceId = taskEntity.getProcessInstanceId();

		if (isManage == 0) {
			boolean hasRights = this.processInstanceService.getHasRightsByTask(taskEntity.getId(), sysUser.getId());
			if (!hasRights) {
				//return ServiceUtil.getTipInfo("对不起,你不是这个任务的执行人,不能处理此任务!");
				ProcessTaskHistory taskHistory = this.historyTaskInstanceService.get(taskId);
				if ((taskHistory == null) && (StringUtil.isEmpty(taskId))&& (StringUtil.isEmpty(instanceId))) {
					return null;//异常处理
				}
				String actInstId = taskHistory.getProcessInstanceId();
				String url = request.getContextPath()+ "processInstanceController.do?info&taskId="+taskId+"&instId="+actInstId;
				response.sendRedirect(url);
				return null;
			}
		}
		
		
		/*List<TaskReadEntity> taskReadList= taskReadService.getTaskRead(instanceId, taskId);
		if(!(taskReadList!=null && taskReadList.size()>0)){
			//设置为已读
			TaskReadEntity taskRead=new TaskReadEntity();
			taskRead.setTaskId(taskId);
			taskRead.setActInstId(instanceId);
			this.taskReadService.save(taskRead);
		}*/
		

		String nodeId = taskEntity.getTaskDefinitionKey();
		String actDefId = taskEntity.getProcessDefinitionId();
		String userId = sysUser.getId();

		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		ProcessInstanceEntity processRun = this.processInstanceService.getByActInstanceId(instanceId);

		//NodeSetEntity bpmNodeSet = this.nodeSetService.getByActDefIdNodeId(actDefId, nodeId);

		

		//Map<String,Object> variables = this.taskService.getVariables(taskId);
		
		
		//节点表单设置为空，取全局的表单设置
		NodeSetEntity bpmNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(actDefId, nodeId);
		if(bpmNodeSet==null){
			bpmNodeSet= this.nodeSetService.getGlobalByActDefId(actDefId);
		}else{
			//节点表单设置为空，取全局的表单设置
			if (StringUtil.isEmpty(bpmNodeSet.getFormId())&&StringUtil.isEmpty(bpmNodeSet.getFormUrl())) {
				NodeSetEntity globalNodeSet = this.nodeSetService.getGlobalByActDefId(actDefId);
				bpmNodeSet.setFormId(globalNodeSet.getFormId());
				bpmNodeSet.setFormType(globalNodeSet.getFormType());
			}
		}
		String fromId=bpmNodeSet.getFormId();
		FlowFormEntity flowForm=flowFormService.get(fromId);
		String formUrl="";
		if(StringUtil.equals(bpmNodeSet.getFormType(),NodeSetEntity.FORM_TYPE_URL)){
			formUrl=bpmNodeSet.getFormUrl();
		}else{
			formUrl=flowForm.getUrl();
		}
		mv.setViewName(formUrl);
		Map<String,String> param=new HashMap<String,String>();
		param.put("form.id", fromId);
		
		taskCmd.getVariables().put("actDefKey", bpmDefinition.getActKey());
		taskCmd.getVariables().put("isStartAssign",flowForm.getIsStartAssign()==1?"1":"0");
		taskCmd.setBusinessKey(processRun.getBusinessKey());
		TaskThreadService.setProcessCmd(taskCmd);
		/*List<FlowFormItemEntity> flowFormItemList=flowFormItemService.findByPropertys(param);*/
		String toBackNodeId = NodeCache.getFirstNodeId(actDefId).getNodeId();
		boolean isSignTask = this.flowService.isSignTask(taskEntity);//是不是会签节点
		if (isSignTask) {
			handleSignTask(mv, instanceId, nodeId, actDefId, userId);//处理会签特权,封装变量
		}

		boolean isCanBack = this.flowService.getIsAllowBackByTask(taskEntity);//是否允许驳回

		boolean isCanAssignee = this.taskExeService.isAssigneeTask(taskEntity, bpmDefinition,userId);//是否允许转办

		boolean isHidePath = this.getIsHidePath(bpmNodeSet.getIsHidePath());//是否隐藏路径

		List<String> taskAppItems = this.approveItemService.getApprovalByActDefId(actDefId, nodeId);//审批常用语

		TaskOpinionEntity taskOpinion = this.taskOpinionService.getOpinionByTaskId(taskId, userId);//任务意见
		
		List<ComboBox> conclusionList = new ArrayList<ComboBox>();
		ComboBox comboBox = new ComboBox();
		comboBox.setId("1");
		comboBox.setText("同意");
		conclusionList.add(comboBox);
		comboBox = new ComboBox();
		comboBox.setId("2");
		comboBox.setText("反对");
		conclusionList.add(comboBox);
		if(isSignTask){
			comboBox = new ComboBox();
			comboBox.setId("0");
			comboBox.setText("再议");
			conclusionList.add(comboBox);
		}
		if (isManage == 0) {
			mv.addObject("jumpType", bpmNodeSet.getJumpType());
		}else{
			mv.addObject("jumpType", NodeSetEntity.JUMP_TYPE_NORMAL+","+NodeSetEntity.JUMP_TYPE_SELECT+","+NodeSetEntity.JUMP_TYPE_FREE);
		}
		if(StringUtil.indexOf(formUrl, ".do")>0){
			mv.setViewName("redirect:"+formUrl);
			mv.addObject("viewType", "nextProcess");
		}
		return mv.addObject("task", taskEntity).addObject("nodeSet",bpmNodeSet).addObject("actInstId", instanceId)
				 .addObject("processRun", processRun).addObject("definition", bpmDefinition)
				 .addObject("isSignTask",Boolean.valueOf(isSignTask)).addObject("taskId", taskEntity.getId())
				 .addObject("conclusionData",JSONHelper.toJSONString(conclusionList))
				 .addObject("isCanBack",Boolean.valueOf(isCanBack)).addObject("isCanAssignee",Boolean.valueOf(isCanAssignee))
				 .addObject("businessKey",processRun.getBusinessKey()).addObject("toBackNodeId",toBackNodeId)
				 .addObject("formId", flowForm.getId()).addObject("formCode", flowForm.getCode())
				 .addObject("taskAppItems", taskAppItems).addObject("curUser", ClientUtil.getUserEntity())
				 .addObject("isManage",Integer.valueOf(isManage)).addObject("isWorkFlow", "true")
				 .addObject("taskOpinion", taskOpinion).addObject("nowDate",new Date()).addObject("isTransfer",true);
	}
	
	
	
		/*//任务回退数据查询
		@RequestMapping( { "back" })
		public ModelAndView back(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");

			if (StringUtils.isEmpty(taskId)) {
				return getAutoView();
			}
			TaskEntity taskEntity = this.bpmService.getTask(taskId);
			String taskToken = (String) this.taskService.getVariableLocal(
					taskEntity.getId(), TaskFork.TAKEN_VAR_NAME);
			ExecutionStack executionStack = this.executionStackService
					.getLastestStack(taskEntity.getProcessInstanceId(), taskEntity
							.getTaskDefinitionKey(), taskToken);
			if ((executionStack != null) && (executionStack.getParentId() != null)
					&& (executionStack.getParentId().longValue() != 0L)) {
				ExecutionStack parentStack = (ExecutionStack) this.executionStackService.getById(executionStack.getParentId());
				String assigneeNames = "";
				if (StringUtils.isNotEmpty(parentStack.getAssignees())) {
					String[] uIds = parentStack.getAssignees().split("[,]");
					int i = 0;
					for (String uId : uIds) {
						SysUser sysUser = (SysUser) this.sysUserService
								.getById(new Long(uId));
						if (sysUser == null)
							continue;
						if (i++ > 0) {
							assigneeNames = assigneeNames + ",";
						}
						assigneeNames = assigneeNames + sysUser.getFullname();
					}
				}
				request.setAttribute("assigneeNames", assigneeNames);
				request.setAttribute("parentStack", parentStack);
			}

			request.setAttribute("taskId", taskId);

			return getAutoView();
		}

		//任务回退流转
		@RequestMapping( { "jumpBack" })
		public ModelAndView jumpBack(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
			processCmd.setCurrentUserId(ContextUtil.getCurrentUserId().toString());
			this.processRunService.nextProcess(processCmd);
			return new ModelAndView("redirect:list.ht");
		}
		
		
		*//**
		 * 加载会签数据
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
		@RequestMapping( params="toSign")
		public ModelAndView toSign(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");
			ModelAndView modelView = null;//getAutoView();

			if (StringUtil.isNotEmpty(taskId)) {
				TaskEntity taskEntity = this.flowService.getTask(taskId);
				String nodeId = this.flowService.getExecution(taskEntity.getExecutionId()).getActivityId();
				String actInstId = taskEntity.getProcessInstanceId();

				List<TaskSignDataEntity> signDataList = this.taskSignDataService.getByNodeAndInstanceId(actInstId, nodeId);

				NodeSignEntity nodeSign = this.nodeSignService.getByDefIdAndNodeId(taskEntity.getProcessDefinitionId(),nodeId);

				modelView.addObject("signDataList", signDataList);
				modelView.addObject("task", taskEntity);
				modelView.addObject("curUser", ClientUtil.getUserEntity());
				modelView.addObject("bpmNodeSign", nodeSign);
			}

			return modelView;
		}

		/**
		 * 加签,处理activiti加签逻辑
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params="addSign")
		@ResponseBody
		public AjaxJson addSign(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");
			String signUserIds = request.getParameter("signUserIds");
			message = "加签成功成功";
			try{
				if ((StringUtil.isNotEmpty(taskId))&& (StringUtil.isNotEmpty(signUserIds))) {
					this.taskSignDataService.addSign(signUserIds, taskId);
				}
			}catch(Exception e){
				message = e.getMessage();
			}
			result.setMsg(message);
			return result;	
		}
		
		@RequestMapping(params = "showTaskSubmit")
		public ModelAndView showTaskSubmit(HttpServletRequest request) {
			request.setAttribute("taskId", request.getParameter("taskId"));
			request.setAttribute("jumpType", request.getParameter("jumpType"));
			return new ModelAndView("workflow/task/showTaskSubmit");
		}
		
		//选择路径跳转的路径查询
		@RequestMapping(params="tranTaskUserMap")
		public ModelAndView tranTaskUserMap(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			//int isStart = Integer.parseInt(request.getParameter("isStart"));
			String taskId = request.getParameter("taskId");
			String actDefId = request.getParameter("actDefId");
			int selectPath = Integer.parseInt(request.getParameter("selectPath"));
			

			boolean canChoicePath = this.flowService.getCanChoicePath(actDefId,taskId);

			String startUserId = ClientUtil.getUserId();
			List<NodeTranUser> nodeTranUserList = null;
			/*if (isStart == 1) {
				Map<String,Object> vars = new HashMap<String,Object>();
				nodeTranUserList = this.flowService.getStartNodeUserMap(actDefId,startUserId, vars);
			} else {*/
			nodeTranUserList = this.flowService.getNodeTaskUserMap(taskId,startUserId, canChoicePath);
			/*for(NodeTranUser nodeTranUser:nodeTranUserList){
				Set<NodeUserMap> nodeUserMap=nodeTranUser.getNodeUserMapSet();
				for(NodeUserMap nodeUser:nodeUserMap){
					Set<TaskExecutor> taskExeCutorList=nodeUser.getTaskExecutors();
					for(TaskExecutor taskExecutor:taskExeCutorList){
						
					}
				}
			}*/
			//}
			ModelAndView mv=new ModelAndView("workflow/task/taskTranTaskUserMap");
			mv.addObject("taskId", taskId);
			return mv.addObject("nodeTranUserList", nodeTranUserList)//用户
					 .addObject("selectPath", Integer.valueOf(selectPath))//选择路径
					 .addObject("canChoicePath", Boolean.valueOf(canChoicePath));//是否能够选择路径
		}
		
		//自由流选择路径
		@RequestMapping(params="freeJump")
		public ModelAndView freeJump(HttpServletRequest request,
				HttpServletResponse response,Model model) throws Exception {
			String taskId = request.getParameter("taskId");
			model.addAttribute("taskId", taskId);
			//Map<String,Map<String,String>> jumpNodesMap = this.flowService.getJumpNodes(taskId);
			Map<String,Map<String,String>> jumpNodesMap =new HashMap<String,Map<String,String>>();
			
			
			TaskEntity task=this.flowService.getTask(taskId);
			
			FlowNode flowNode = (FlowNode) NodeCache.getByActDefId(task.getProcessDefinitionId()).get(task.getTaskDefinitionKey());
			List<FlowNode> nodeList=new ArrayList<FlowNode>();
			getUserNode(flowNode,nodeList);
			Map<String,String> node=new HashMap<String,String>();
			for(FlowNode preNode:nodeList){
				node.put(preNode.getNodeId(), preNode.getNodeName());
			}
			jumpNodesMap.put("任务节点", node);
			model.addAttribute("jumpNodeMap", jumpNodesMap);
			
			NodeSetEntity nodeSet=this.nodeSetService.getNodeSetByActDefIdNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
			model.addAttribute("nodeSet", nodeSet);
			return new ModelAndView("workflow/task/taskFreeJump");
		}
		
		private void getUserNode(FlowNode flowNode,List<FlowNode> nodeList){
			if (flowNode.getPreFlowNodes() == null) {
				return;
			} else {
				for (FlowNode childNode : flowNode.getPreFlowNodes()) {
					if(childNode.getNodeType().equals("userTask")){
						nodeList.add(childNode);
					}
					getUserNode(childNode, nodeList);
				}
			}
		}
		
		/**
		 * 沟通
		 * @param request
		 * @param response
		 * @throws Exception
		 *//*
		@RequestMapping( { "toStartCommunication" })
		public void toStartCommunication(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			PrintWriter out = response.getWriter();
			ResultMessage resultMessage = null;
			String cmpIds = request.getParameter("cmpIds");
			if (StringUtil.isEmpty(cmpIds)) {
				resultMessage = new ResultMessage(0, "请输入通知人!");
				out.print(resultMessage);
				return;
			}
			try {
				String taskId = request.getParameter("taskId");
				String opinion = request.getParameter("opinion");
				String informType = RequestUtil.getString(request, "informType");

				TaskEntity taskEntity = this.bpmService.getTask(taskId);
				ProcessRun processRun = this.processRunService
						.getByActInstanceId(new Long(taskEntity
								.getProcessInstanceId()));

				this.processRunService.saveCommuniCation(taskEntity, opinion,informType, cmpIds, processRun.getSubject());
				ProcessCmd taskCmd = BpmUtil.getProcessCmd(request);
				//处理表单数据
				handlerFormData(taskCmd, processRun, taskEntity.getTaskDefinitionKey());

				Long runId = processRun.getRunId();

				String memo = "在:【" + processRun.getSubject() + "】,节点【"
						+ taskEntity.getName() + "】,意见:" + opinion;
				this.bpmRunLogService.addRunLog(runId,
						BpmRunLog.OPERATOR_TYPE_ADDOPINION, memo);

				resultMessage = new ResultMessage(1, "成功完成了该任务!");
			} catch (Exception e) {
				resultMessage = new ResultMessage(0, "完成任务失败!");
			}
			out.print(resultMessage);
		}
		
		@RequestMapping( { "claim" })
		@Action(description = "锁定任务")
		public void claim(HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			String taskId = RequestUtil.getString(request, "taskId");
			int isAgent = RequestUtil.getInt(request, "isAgent");
			String preUrl = RequestUtil.getPrePage(request);
			String assignee = ContextUtil.getCurrentUserId().toString();
			try {
				TaskEntity taskEntity = this.bpmService.getTask(taskId);
				ProcessRun processRun = this.processRunService
						.getByActInstanceId(new Long(taskEntity
								.getProcessInstanceId()));
				Long runId = processRun.getRunId();
				this.taskService.claim(taskId, assignee);
				String memo = "流程:" + processRun.getSubject() + ",锁定任务，节点【"
						+ taskEntity.getName() + "】";
				this.bpmRunLogService.addRunLog(runId,
						BpmRunLog.OPERATOR_TYPE_LOCK, memo);
				saveSuccessResultMessage(request.getSession(), "成功锁定任务!");
			} catch (Exception ex) {
				saveSuccessResultMessage(request.getSession(), "任务已经完成或被其他用户锁定!");
			}
			response.sendRedirect(preUrl);
		}

		@RequestMapping( { "unlock" })
		@Action(description = "解锁任务")
		public ModelAndView unlock(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");

			if (StringUtils.isNotEmpty(taskId)) {
				TaskEntity taskEntity = this.bpmService.getTask(taskId);
				ProcessRun processRun = this.processRunService
						.getByActInstanceId(new Long(taskEntity
								.getProcessInstanceId()));
				Long runId = processRun.getRunId();
				this.bpmService.updateTaskAssigneeNull(taskId);
				String memo = "流程:" + processRun.getSubject() + ",解锁任务，节点【"
						+ taskEntity.getName() + "】";
				this.bpmRunLogService.addRunLog(runId,
						BpmRunLog.OPERATOR_TYPE_UNLOCK, memo);
				saveSuccessResultMessage(request.getSession(), "任务已经成功解锁!");
			}
			return new ModelAndView("redirect:forMe.ht");
		}

		*/
		
		
		//更改路径时查询任务的用户
		@RequestMapping(params="getTaskUsers")
		@ResponseBody
		public List<TaskExecutor> getTaskUsers(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");

			TaskEntity taskEntity = this.flowService.getTask(taskId);

			String nodeId = request.getParameter("nodeId");

			if (StringUtil.isEmpty(nodeId)) {
				nodeId = taskEntity.getTaskDefinitionKey();
			}

			String actDefId = taskEntity.getProcessDefinitionId();

			String actInstId = taskEntity.getProcessInstanceId();

			Map<String,Object> vars = this.runtimeService.getVariables(taskEntity.getExecutionId());

			String startUserId = vars.get("startUser").toString();

			List<TaskExecutor> taskExecutorList = this.nodeUserService.getExecutors(actDefId, actInstId, nodeId, startUserId, vars,NodeUserEntity.FUNC_NODE_USER);

			return taskExecutorList;
		}
		
		/*@RequestMapping( { "delegate" })
		@Action(description = "任务交办", detail = "<#if StringUtils.isNotEmpty(taskId) && StringUtil.isNotEmpty(userId)>交办流程【${SysAuditLinkService.getProcessRunLink(taskId)}】的任务【${taskName}】给用户【${SysAuditLinkService.getSysUserLink(Long.valueOf(userId))}】</#if>")
		public void delegate(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			PrintWriter writer = response.getWriter();
			String taskId = request.getParameter("taskId");
			String userId = request.getParameter("userId");
			String delegateDesc = request.getParameter("memo");
			ResultMessage message = null;

			if ((StringUtils.isNotEmpty(taskId)) && (StringUtil.isNotEmpty(userId))) {
				SysAuditThreadLocalHolder.putParamerter("taskName", this.bpmService
						.getTask(taskId).getName());

				message = new ResultMessage(1, "任务交办成功!");
			} else {
				message = new ResultMessage(0, "没有传入必要的参数");
			}
			writer.print(message);
		}

		*//**
		 * 查询当前任务执行人
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 *//*
		@RequestMapping( { "changeAssignee" })
		@Action(description = "更改任务执行人")
		public ModelAndView changeAssignee(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");
			TaskEntity taskEntity = this.bpmService.getTask(taskId);
			SysUser curUser = ContextUtil.getCurrentUser();
			return getAutoView().addObject("taskEntity", taskEntity).addObject(
					"curUser", curUser);
		}

		*//**
		 * 动态改变任务的指派人
		 * @param request
		 * @param response
		 * @throws Exception
		 *//*
		@RequestMapping( { "setAssignee" })
		@Action(description = "任务指派")
		public void setAssignee(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			PrintWriter writer = response.getWriter();
			String taskId = request.getParameter("taskId");
			String userId = request.getParameter("userId");

			String voteContent = RequestUtil.getString(request, "voteContent");
			String informType = RequestUtil.getString(request, "informType");
			ResultMessage message = null;
			try {
				message = this.processRunService.updateTaskAssignee(taskId, userId,
						voteContent, informType);
			} catch (Exception e) {
				message = new ResultMessage(0, e.getMessage());
			}
			writer.print(message);
		}

		@RequestMapping( { "delete" })
		@Action(description = "删除任务", execOrder = ActionExecOrder.BEFORE, detail = "<#if StringUtils.isNotEmpty(taskId)><#assign entity1=bpmService.getTask(taskId)/>用户删除了任务【${entity1.name}】,该任务属于流程【${SysAuditLinkService.getProcessRunLink(taskId)}】</#elseif StringUtils.isNotEmpty(id)><#list StringUtils.split(id,\",\") as item><#assign entity2=bpmService.getTask(item)/>用户删除了任务【${entity2.name}】,该任务属于流程【${SysAuditLinkService.getProcessRunLink(item)}】</#list></#if>")
		public ModelAndView delete(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			ResultMessage message = null;
			try {
				String taskId = request.getParameter("taskId");
				String[] taskIds = request.getParameterValues("id");
				if (StringUtils.isNotEmpty(taskId)) {
					this.bpmService.deleteTask(taskId);

					TaskEntity task = this.bpmService.getTask(taskId);
					ProcessRun processRun = this.processRunService
							.getByActInstanceId(new Long(task
									.getProcessInstanceId()));
					String memo = "用户删除了任务[" + task.getName() + "],该任务属于["
							+ processRun.getProcessName() + "]流程。";
					this.bpmRunLogService.addRunLog(processRun.getRunId(),
							BpmRunLog.OPERATOR_TYPE_DELETETASK, memo);
					this.taskService.deleteTask(taskId);
				} else if ((taskIds != null) && (taskIds.length != 0)) {
					this.bpmService.deleteTasks(taskIds);
					for (int i = 0; i < taskIds.length; i++) {
						String id = taskIds[i];
						TaskEntity task = this.bpmService.getTask(id);
						ProcessRun processRun = this.processRunService
								.getByActInstanceId(new Long(task
										.getProcessInstanceId()));
						String memo = "用户删除了任务[" + task.getName() + "],该任务属于["
								+ processRun.getProcessName() + "]流程。";
						this.bpmRunLogService.addRunLog(processRun.getRunId(),
								BpmRunLog.OPERATOR_TYPE_DELETETASK, memo);
						this.taskService.deleteTask(id);
					}
				}
				message = new ResultMessage(1, "删除任务成功");
			} catch (Exception e) {
				message = new ResultMessage(0, "删除任务失败");
			}
			addMessage(message, request);
			return new ModelAndView("redirect:list.ht");
		}

		
		
		@RequestMapping( { "changePath" })
		public ModelAndView changePath(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");
			TaskEntity taskEntity = this.bpmService.getTask(taskId);
			Map taskNodeMap = this.bpmService.getTaskNodes(taskEntity
					.getProcessDefinitionId(), taskEntity.getTaskDefinitionKey());
			return getAutoView().addObject("taskEntity", taskEntity).addObject(
					"taskNodeMap", taskNodeMap).addObject("curUser",
					ContextUtil.getCurrentUser());
		}

		@RequestMapping( { "saveChangePath" })
		@ResponseBody
		public String saveChangePath(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
			this.processRunService.nextProcess(processCmd);
			saveSuccessResultMessage(request.getSession(), "更改任务执行的路径!");
			return "{success:true}";
		}
		
		//中止流程
		@RequestMapping( { "end" })
		@Action(description = "结束流程任务", detail = "结束流程【${SysAuditLinkService.getProcessRunLink(taskId)}】的任务")
		public void end(HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			ResultMessage resultMessage = null;
			try {
				String taskId = request.getParameter("taskId");
				TaskEntity taskEntity = this.bpmService.getTask(taskId);
				ProcessRun processRun = this.processRunService
						.getByActInstanceId(new Long(taskEntity
								.getProcessInstanceId()));
				String voteContent = "由"
						+ ContextUtil.getCurrentUser().getFullname() + "进行完成操作！";
				ProcessCmd cmd = new ProcessCmd();
				cmd.setTaskId(taskId);
				cmd.setVoteAgree(Short.valueOf((short) 0));
				cmd.setVoteContent(voteContent);
				cmd.setOnlyCompleteTask(true);
				this.processRunService.nextProcess(cmd);
				Long runId = processRun.getRunId();
				String memo = "结束了:" + processRun.getSubject();
				this.bpmRunLogService.addRunLog(runId,
						BpmRunLog.OPERATOR_TYPE_ENDTASK, memo);
				resultMessage = new ResultMessage(1, "成功完成了该任务!");
			} catch (Exception ex) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					resultMessage = new ResultMessage(0, "完成任务失败:" + str);
				} else {
					String message = ExceptionUtil.getExceptionMessage(ex);
					resultMessage = new ResultMessage(0, message);
				}
			}
			response.getWriter().print(resultMessage);
		}

		*//**
		 * 结束流程实例
		 * @param request
		 * @param response
		 * @throws Exception
		 *//*
		@RequestMapping( { "endProcess" })
		@Action(description = "根据任务结束流程实例")
		public void endProcess(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			PrintWriter out = response.getWriter();

			Long taskId = Long.valueOf(RequestUtil.getLong(request, "taskId"));
			String memo = RequestUtil.getString(request, "memo");
			Long curUserId = ContextUtil.getCurrentUserId();
			TaskEntity taskEnt = this.bpmService.getTask(taskId.toString());
			if (taskEnt == null) {
				writeResultMessage(out, "此任务已经完成!", 0);
			}

			String instanceId = taskEnt.getProcessInstanceId();
			ResultMessage message = null;
			try {
				String nodeId = taskEnt.getTaskDefinitionKey();
				ProcessRun processRun = this.bpmService.endProcessByInstanceId(
						new Long(instanceId), nodeId, memo);
				memo = "结束流程:" + processRun.getSubject() + ",结束原因:" + memo;
				this.bpmRunLogService.addRunLog(processRun.getRunId(),
						BpmRunLog.OPERATOR_TYPE_ENDTASK, memo);
				message = new ResultMessage(1, "结束流程实例成功!");
				writeResultMessage(out, message);
			} catch (Exception ex) {
				ex.printStackTrace();
				message = new ResultMessage(0, ExceptionUtil
						.getExceptionMessage(ex));
				writeResultMessage(out, message);
			}
		}
		
		*//**
		 * 批量审批
		 * @param request
		 * @param response
		 * @throws Exception
		 *//*
		@RequestMapping( { "batComplte" })
		public void batComplte(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			PrintWriter out = response.getWriter();
			ResultMessage resultMessage = null;
			String taskIds = RequestUtil.getString(request, "taskIds");
			String opinion = RequestUtil.getString(request, "opinion");
			try {
				this.processRunService.nextProcessBat(taskIds, opinion);
				String message = MessageUtil.getMessage();
				resultMessage = new ResultMessage(1, message);
			} catch (Exception ex) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					resultMessage = new ResultMessage(0, str);
				} else {
					String message = ExceptionUtil.getExceptionMessage(ex);
					resultMessage = new ResultMessage(0, message);
				}
			}
			out.print(resultMessage);
		}

		*//**
		 * 检查任务是否存在
		 * @param request
		 * @param response
		 * @throws IOException
		 *//*
		@RequestMapping( { "isTaskExsit" })
		public void isTaskExsit(HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			Long taskId = Long.valueOf(RequestUtil.getLong(request, "taskId"));
			PrintWriter out = response.getWriter();
			TaskEntity taskEnt = this.bpmService.getTask(taskId.toString());
			if (taskEnt == null) {
				writeResultMessage(out, "此任务已经完成!", 0);
			} else
				writeResultMessage(out, "任务存在!", 1);
		}*/
		
	@RequestMapping(params = "completeTaskList")
	public ModelAndView completeTaskList(HttpServletRequest request) {
		return new ModelAndView("workflow/task/completeTaskList");
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
	@RequestMapping(params = "completeTaskDatagrid")
	@ResponseBody 
	public AjaxJson completeTaskDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		//自动封装查询条件
		this.buildFilter(page, request); 
		//手动添加查询条件
		Map<String,String> param=new HashMap<String,String>();
		param.put("userId", user.getId());
		page.setParameter(param);
		//添加排序字段
		this.buildOrder(page,request,"updateTime",Page.DESC);
		page =this.taskActService.getMyCompleteTaskList(page);
		List<ProcessTask> list=page.getResult();
		if(list!=null && list.size()>0){
			for(ProcessTask task:list){
				NodeSetEntity bpmNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
				if(bpmNodeSet==null){
					bpmNodeSet= this.nodeSetService.getGlobalByActDefId(task.getProcessDefinitionId());
				}else{
					//节点表单设置为空，取全局的表单设置
					if (StringUtil.isEmpty(bpmNodeSet.getFormId())&&StringUtil.isEmpty(bpmNodeSet.getFormUrl())) {
						NodeSetEntity globalNodeSet = this.nodeSetService.getGlobalByActDefId(task.getProcessDefinitionId());
						bpmNodeSet.setFormId(globalNodeSet.getFormId());
						bpmNodeSet.setFormType(globalNodeSet.getFormType());
					}
				}
				String fromId=bpmNodeSet.getFormId();
				FlowFormEntity flowForm=flowFormService.get(fromId);
				if(flowForm==null){
					continue;
				}
				try {
					Map<String, Object> extra= appFormTableService.getOneFieldData(flowForm.getCode(),task.getBusinessKey());
					if(extra==null){
						extra=new HashMap<String,Object>();
					}
					String groupId=task.getGroupId();
					if(StringUtil.isNotEmpty(groupId)){
						extra.put("groupId", groupId);
						Map<String,Object> groupUserList=orgGroupService.getUserOrg(groupId);
						extra.put("groupUserList", groupUserList.get("OrgGroupMember"));
					}
					extra.put("createTime", DateUtils.formatTime(task.getCreateTime()));
					task.setExtra(JSONHelper.toJSONString(extra));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//ProcessInstanceEntity processRun=this.processInstanceService.getByActInstanceId(task.getProcessInstanceId());
				/*boolean isFirst = isFirst(processRun);

				//是否可以撤销，流程实例发起人并且流程没有结束才能撤销
				boolean isCanRecover = isCanRecover(processRun, isFirst, user.getId());
				//是否可以追回
				boolean isCanRedo = isCanRedo(processRun, isFirst, user.getId(),task.getId());
				task.setIsCanRedo(isCanRedo);
				task.setIsCanrecover(isCanRecover);*/
			}
		}
		
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

	
	@RequestMapping(params = "endTaskList")
	public ModelAndView endTaskList(HttpServletRequest request) {
		return new ModelAndView("workflow/task/endTaskList");
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
	@RequestMapping(params = "endTaskDatagrid")
	public void endTaskDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		//自动封装查询条件
		this.buildFilter(page, request); 
		//手动添加查询条件
		Map<String,String> param=new HashMap<String,String>();
		param.put("userId", user.getId());
		page.setParameter(param);
		//添加排序字段
		this.buildOrder(page,request,"create_time",Page.ASC);
		page =this.taskActService.getMyEndTaskList(page);
		dataGrid.setResults(page.getResult());
		dataGrid.setTotal((int)page.getTotalCount());
		TagUtil.datagrid(response, dataGrid);
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
	private boolean isCanRedo(ProcessInstanceEntity processRun, boolean isFirst,String curUserId,String taskId) {
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
		TaskOpinionEntity taskOpinion = this.taskOpinionService.getOpinionByTaskId(taskId, curUserId);
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

}
