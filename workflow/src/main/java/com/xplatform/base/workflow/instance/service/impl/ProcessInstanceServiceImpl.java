package com.xplatform.base.workflow.instance.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.timer.util.TimeUtil;
import com.xplatform.base.workflow.agent.service.AgentSettingService;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.core.facade.service.ActService;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.execution.service.ExecutionExtService;
import com.xplatform.base.workflow.instance.dao.ProcessInstanceDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstCptoEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstFormEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.mybatis.dao.InstanceMybatisDao;
import com.xplatform.base.workflow.instance.mybatis.vo.InstanceVo;
import com.xplatform.base.workflow.instance.service.ProcessInstCptoService;
import com.xplatform.base.workflow.instance.service.ProcessInstFormService;
import com.xplatform.base.workflow.instance.service.ProcessInstHistoryService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.service.NodeRuleService;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;
import com.xplatform.base.workflow.task.entity.TaskExeEntity;
import com.xplatform.base.workflow.task.entity.TaskForkEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskExeService;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.task.service.TaskReadService;
import com.xplatform.base.workflow.task.service.TaskSignDataService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;
import com.xplatform.base.workflow.threadlocal.TaskUserAssignService;

/**
 * 
 * description :流程实例service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("processInstanceService")
public class ProcessInstanceServiceImpl implements ProcessInstanceService {
	private static final Logger logger = Logger.getLogger(ProcessInstanceServiceImpl.class);
	
	@Resource
	private ProcessInstHistoryService processInstHistoryService;
	@Resource
	private TaskService taskService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private TaskMessageService taskMessageService;
	@Resource
	private ProcessInstanceDao processInstanceDao;
	@Resource
	private FlowService flowService;
	@Resource
	private DefinitionService definitionService;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private ProcessInstFormService processInstFormService;
	@Resource
	private NodeRuleService nodeRuleService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private TaskExeService taskExeService;
	@Resource
	private TaskSignDataService taskSignDataService;
	@Autowired
	private TaskUserAssignService taskUserAssignService;
	@Resource
	private AppFormTableService appFormTableService;
	@Resource
	private TaskActService taskActService;
	@Resource
	private ExecutionExtService executionExtService;
	@Resource
	private TaskReadService taskReadService;
	@Resource
	private MsgTemplateService msgTemplateService;
	@Resource
	private ActService actService;
	@Autowired
	private InstanceMybatisDao instanceMybatisDao;
	@Resource
	private AgentSettingService agentSettingService;
	@Resource
	private ProcessInstCptoService processInstCptoService;
	@Resource
	private OrgGroupService orgGroupService;

	//获取任务实体
	private TaskEntity getTaskEntByCmd(ProcessCmd processCmd) {
		TaskEntity taskEntity = null;
		String taskId = processCmd.getTaskId();
		String runId = processCmd.getRunId();
		if (StringUtil.isNotEmpty(taskId)) {
			taskEntity = this.flowService.getTask(taskId);
		}

		if ((taskEntity == null) && StringUtil.isNotBlank(runId)) {
			ProcessInstanceEntity processInstance = this.get(runId);
			if (processInstance == null)
				return null;
			String instanceId = processInstance.getActInstId();
			ProcessTask processTask = this.flowService.getFirstNodeTask(instanceId);
			if (processTask == null){
				return null;
			}
			taskEntity = this.flowService.getTask(processTask.getId());
		}

		return taskEntity;
	}
	
	//获取流程定义实体
	public DefinitionEntity getBpmDefinitionProcessCmd(ProcessCmd processCmd) {
		DefinitionEntity bpmDefinition = null;
		if (processCmd.getActDefId() != null)
			bpmDefinition = this.definitionService.getByActDefId(processCmd.getActDefId());
		else {
			bpmDefinition = this.definitionService.getMainDefByActDefKey(processCmd.getFlowKey());
		}
		return bpmDefinition;
	}
	
	//设置任务的执行人
	private void setTaskUser(ProcessCmd processCmd) {
		//设置多个任务结点的任务执行人
		if (processCmd.getLastDestTaskUids()!=null &&processCmd.getLastDestTaskUids().length>0) {
			String[] nodeIds = processCmd.getLastDestTaskIds();
			String[] nodeUserIds = processCmd.getLastDestTaskUids();
			if ((nodeIds != null) && (nodeUserIds != null)) {
				this.taskUserAssignService.addNodeUser(nodeIds, nodeUserIds);
			}
		}
		//设置下一个任务的执行人
		if (processCmd.getTaskExecutors().size() > 0) {
			this.taskUserAssignService.setExecutors(processCmd.getTaskExecutors());
		}
		TaskThreadService.setProcessCmd(processCmd);
	}
	
	//清除线程
	private void clearThreadLocal() {
		TaskThreadService.clearAll();
		TaskUserAssignService.clearAll();
	}
	
	//获取流程定义的第一个节点id
	public String getFirstNodetByDefId(String actDefId) {
		String bpmnXml = this.flowService.getDefXmlByProcessDefinitionId(actDefId);
		String firstTaskNode = BpmUtil.getFirstTaskNode(bpmnXml);
		return firstTaskNode;
	}
	
	public NodeSetEntity getStartBpmNodeSet(String defId, String nodeId) {
		NodeSetEntity firstBpmNodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(defId, nodeId);
		NodeSetEntity bpmNodeSetGlobal = this.nodeSetService.getBySetType(defId, NodeSetEntity.SetType_GloabalForm);
		if ((firstBpmNodeSet != null)&& (StringUtil.equals(firstBpmNodeSet.getFormType(),"-1"))) {
			return firstBpmNodeSet;
		}
		return bpmNodeSetGlobal;
	}
	
	//处理器执行
	@SuppressWarnings("unused")
	private void invokeHandler(ProcessCmd processCmd, NodeSetEntity bpmNodeSet,boolean isBefore) throws Exception {
		if (bpmNodeSet == null)
			return;
		String handler = "";
		if (isBefore){//前置
			handler = bpmNodeSet.getBeforeHandler();
		}else {//后置
			handler = bpmNodeSet.getAfterHandler();
		}
		if (StringUtil.isEmpty(handler)) {
			return;
		}
		String[] aryHandler = handler.split("[.]");
		if (aryHandler != null) {
			String beanId = aryHandler[0];
			String method = aryHandler[1];
			//获取bean
			Object serviceBean = ApplicationContextUtil.getBean(beanId);
			//触发方法执行
			if (serviceBean != null) {
				Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[] { ProcessCmd.class });
				invokeMethod.invoke(serviceBean, new Object[] { processCmd });
			}
		}
	}
	
	private ProcessInstanceEntity initProcessRun(DefinitionEntity bpmDefinition) {
		ProcessInstanceEntity processRun = new ProcessInstanceEntity();
		NodeSetEntity bpmNodeSet = this.nodeSetService.getBySetType(bpmDefinition.getId(), NodeSetEntity.SetType_GloabalForm);
		/*if (BeanUtils.isNotEmpty(bpmNodeSet)) {
			if (BpmNodeSet.FORM_TYPE_ONLINE.equals(bpmNodeSet.getFormType())) {
				BpmFormDef bpmFormDef = this.bpmFormDefDao.getDefaultPublishedByFormKey(bpmNodeSet.getFormKey());
				if (bpmFormDef != null)
					processRun.setFormDefId(bpmFormDef.getFormDefId());
			} else {
				processRun.setBusinessUrl(bpmNodeSet.getFormUrl());
			}
		}*/
		//processRun.setBusinessKey(UUIDGenerator.generate());
		processRun.setActDefId(bpmDefinition.getActId());
		processRun.setDefId(bpmDefinition.getId());
		processRun.setDefName(bpmDefinition.getName());
		processRun.setDefKey(bpmDefinition.getCode());

		processRun.setStatus(ProcessInstanceEntity.STATUS_RUNNING.toString());

		return processRun;
	}
	
	private String getSubject(DefinitionEntity bpmDefinition, ProcessCmd processCmd) {
		if (StringUtil.isNotEmpty(processCmd.getSubject())) {
			return processCmd.getSubject();
		}
		String rule = bpmDefinition.getTaskNameRule();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("title", bpmDefinition.getName());
		map.put("startUser", ClientUtil.getUserEntity().getName());
		map.put("startDate", TimeUtil.getCurrentDate());
		map.put("startTime", TimeUtil.getCurrentTime());
		map.put("businessKey", processCmd.getBusinessKey());
		map.putAll(processCmd.getVariables());
		rule = BpmUtil.getTitleByRule(rule, map);

		return rule;
	}
	
	//跳过第一个结点
	private void handJumpOverFirstNode(String processInstanceId,
			ProcessCmd processCmd) throws Exception {
		this.taskUserAssignService.clearNodeUserMap();
		TaskThreadService.clearNewTasks();
		List<ProcessTask> taskList = this.flowService.getTasks(processInstanceId);
		ProcessTask taskEntity = (ProcessTask) taskList.get(0);
		String taskId = taskEntity.getId();
		String parentNodeId = taskEntity.getTaskDefinitionKey();

		processCmd.getVariables().put("approvalStatus_" + parentNodeId,TaskOpinionEntity.STATUS_SUBMIT);
		processCmd.getVariables().put("approvalContent_" + parentNodeId, "填写表单");
		processCmd.setVoteAgree(TaskOpinionEntity.STATUS_SUBMIT);
		setVariables(taskId, processCmd);
		processCmd.setParentNodeId(parentNodeId);
		TaskThreadService.setProcessCmd(processCmd);
		this.flowService.transTo(taskId, "");
		//this.executionStackService.addStack(taskEntity.getProcessInstanceId(),parentNodeId, "");
	}
	
	private void addSubmitOpinion(ProcessInstanceEntity processRun) throws BusinessException {
		TaskOpinionEntity opinion = new TaskOpinionEntity();
		opinion.setCheckStatus(Integer.valueOf(TaskOpinionEntity.STATUS_SUBMIT));
		opinion.setActInstId(processRun.getActInstId());
		opinion.setOpinion("发起申请");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, -10);
		opinion.setCreateTime(calendar.getTime());
		opinion.setUpdateTime(calendar.getTime());
		opinion.setExeUserId(processRun.getCreateUserId());
		opinion.setExeUserName(processRun.getCreateUserName());
		opinion.setTaskName(processRun.getCreateUserName());
		opinion.setEndTime(new Date());
		this.taskOpinionService.save(opinion);
	}
	
	private void updOption(Map<String, String> optionsMap, ProcessCmd cmd) {
		if (BeanUtils.isEmpty(optionsMap))
			return;

		Set<String> set = optionsMap.keySet();
		String key = (String) set.iterator().next();
		String value = (String) optionsMap.get(key);
		cmd.setVoteFieldName(key);
		cmd.setVoteContent(value);
	}
	
	private void setVariables(String taskId, ProcessCmd processCmd) {
		if (StringUtil.isEmpty(taskId))
			return;
		Map<String,Object> vars = processCmd.getVariables();
		if (BeanUtils.isNotEmpty(vars)) {
			for(Map.Entry<String,Object> entry : vars.entrySet()){
				this.taskService.setVariable(taskId, (String) entry.getKey(), entry.getValue());
			}
		}
		Map<String,Object> formVars = this.taskService.getVariables(taskId);
		formVars.put("isExtCall", Boolean
				.valueOf(processCmd.isInvokeExternal()));
		formVars.put("subject_", processCmd.getSubject());

		TaskThreadService.setVariables(formVars);
		TaskThreadService.getVariables().putAll(processCmd.getVariables());
	}
	
	private void handleAgentTaskExe(ProcessCmd cmd) throws Exception {
		List<Task> taskList = TaskThreadService.getNewTasks();
		if (BeanUtils.isEmpty(taskList))
			return;
		for (Task taskEntity : taskList) {//任务为代理 ，在任务创建的时候知道是否代理
			if (!TaskOpinionEntity.STATUS_AGENT.toString().equals(
					taskEntity.getDescription()))
				continue;
			String assigeeId = taskEntity.getAssignee();
			Map<String,Object> agentObj= this.agentSettingService.getAgent((DelegateTask)taskEntity,taskEntity.getOwner());
			UserEntity auth = this.sysUserService.getUserById(taskEntity.getOwner());
			UserEntity agent = (UserEntity)agentObj.get("sysUser");
			addAgentTaskExe(taskEntity, cmd, auth, agent);
		}
	}

	private void addAgentTaskExe(Task task, ProcessCmd cmd, UserEntity auth,
			UserEntity agent) throws Exception {
		ProcessInstanceEntity processRun = cmd.getProcessRun();
		if (processRun == null) {
			processRun = getByActInstanceId(task.getProcessInstanceId());
		}
		String informType = cmd.getInformType();

		String memo = "[" + auth.getName() + "]自动代理给["+ agent.getName() + "]";

		TaskExeEntity taskExe = new TaskExeEntity();
		taskExe.setTaskId(task.getId());
		taskExe.setAssigneeId(agent.getId());
		taskExe.setAssigneeName(agent.getName());
		taskExe.setOwnerId(auth.getId());
		taskExe.setOwnerName(auth.getName());
		taskExe.setStatus(taskExe.STATUS_INIT);
		taskExe.setDescription(memo);
		taskExe.setActInstId(task.getProcessInstanceId());
		taskExe.setTaskDefKey(task.getTaskDefinitionKey());
		taskExe.setTaskName(task.getName());
		taskExe.setType(taskExe.TYPE_ASSIGNEE);
		taskExe.setInstId(processRun.getId());
		taskExe.setSubject(processRun.getTitle());
		taskExe.setDefName(processRun.getDefName());
		taskExe.setBusinessName(processRun.getBusinessName());
		this.taskExeService.save(taskExe);
	}
	
	private void signUsersOrSignVoted(ProcessCmd processCmd,
			TaskEntity taskEntity) {
		String nodeId = taskEntity.getTaskDefinitionKey();
		String taskId = taskEntity.getId();

		boolean isSignTask = this.flowService.isSignTask(taskEntity);// 是不是会签任务

		if (isSignTask) {// 是会签任务，设置执行人
			Map executorMap = processCmd.getTaskExecutor();
			if ((executorMap != null) && (executorMap.containsKey(nodeId))) {
				List executorList = (List) executorMap.get(nodeId);
				this.taskUserAssignService.setExecutors(executorList);
			}
		}

		if (processCmd.getVoteAgree() != null) {//记录投票记录
			if (isSignTask) {
				try {
					this.taskSignDataService.signVoteTask(taskId, processCmd.getVoteContent(), processCmd.getVoteAgree().toString());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			processCmd.getVariables().put("approvalStatus_" + nodeId,processCmd.getVoteAgree());
		}
	}
	
	private List<String> getNodeIdByTaskList(List<ProcessTask> taskList) {
		List<String> list = new ArrayList<String>();
		for (ProcessTask task : taskList) {
			list.add(task.getTaskDefinitionKey());
		}
		return list;
	}
	
	private boolean getTaskAllowBack(List<ProcessTask> list) {
		for (ProcessTask task : list) {
			boolean allBack = this.flowService.getIsAllowBackByTask(task);
			if (allBack) {
				return true;
			}
		}
		return false;
	}
	
	private boolean getTaskHasRead(List<ProcessTask> list) {
		boolean rtn = false;
		for (ProcessTask task : list) {
			List readList = this.taskReadService.getTaskRead(task
					.getProcessInstanceId(), task.getId());
			if (BeanUtils.isNotEmpty(readList)) {
				rtn = true;
				break;
			}
		}
		return rtn;
	}
	
	private Map<String, String> getTempByUseType(String useType)
			throws BusinessException {
		MsgTemplateEntity temp = this.msgTemplateService.getDefaultByUseType(useType);
		if (temp == null) {
			throw new BusinessException("模板中未找到内部消息的默认模板或系统模板");
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("inner", temp.getInnerContent());
		map.put("mail", temp.getMailContent());
		map.put("shortmsg", temp.getSmsContent());
		map.put("title", temp.getTitle());
		return map;
	}
	
	private void backToStart(String memo, List<ProcessTask> taskList,
			String informType) throws Exception {
		List<Task> taskEntityList = new ArrayList<Task>();
		ProcessInstanceEntity processRun = getByActInstanceId(((ProcessTask) taskList.get(0)).getProcessInstanceId());
		for (ProcessTask task : taskList) {
			Task taskEntity = this.flowService.getTask(task.getId());
			taskEntityList.add(taskEntity);
		}

		MsgTemplateEntity msgTemplate = this.msgTemplateService.getDefaultByUseType(MsgTemplateEntity.USE_TYPE_REVOKED);
		Map<String,String> msgTempMap = getTempByUseType(MsgTemplateEntity.USE_TYPE_REVOKED);
		this.taskMessageService.notify(taskEntityList, informType, msgTemplate.getTitle(), msgTempMap, memo,null);

		for (int i = 0; i < taskList.size(); i++) {
			ProcessTask taskEntity = (ProcessTask) taskList.get(i);

			ProcessCmd processCmd = new ProcessCmd();
			processCmd.setTaskId(taskEntity.getId());

			processCmd.setRecover(true);
			processCmd.setBack(BpmConst.TASK_BACK_TOSTART);
			processCmd.setVoteAgree(TaskOpinionEntity.STATUS_RECOVER_TOSTART);
			processCmd.setVoteContent(memo);
			processCmd.setInformType(informType);

			if (i > 0) {
				processCmd.setOnlyCompleteTask(true);
			}

			nextProcess(processCmd);
			this.taskExeService.cancel(taskEntity.getId());
		}
	}

	
	private void completeTask(ProcessCmd processCmd, TaskEntity taskEntity) throws BusinessException {
		String taskId = taskEntity.getId();
		if (processCmd.isOnlyCompleteTask()) {
			this.flowService.onlyCompleteTask(taskId);
		} else if (StringUtil.isNotEmpty(processCmd.getDestTask())) {// 完成任务，包括汇总的管理
			this.flowService.transTo(taskId, processCmd.getDestTask());
		} else {
			ExecutionEntity execution = this.actService.getExecution(taskEntity.getExecutionId());
			/**
			 * 跳转规则
			 */
			String jumpTo = this.nodeRuleService.evaluate(execution);
			this.flowService.transTo(taskId, jumpTo);
		}
	}
	
	/*private void initExtSubProcessStack() {
		List<String> list = TaskThreadService.getExtSubProcess();
		if (BeanUtils.isEmpty(list))
			return;
		List taskList = TaskThreadService.getNewTasks();
		Map map = getMapByTaskList(taskList);
		for (String instanceId : list) {
			List tmpList = (List) map.get(instanceId);
			this.executionStackService.initStack(instanceId, tmpList);
		}
	}*/

	private Map<String, List<Task>> getMapByTaskList(List<Task> taskList) {
		Map map = new HashMap();
		for (Task task : taskList) {
			String instanceId = task.getProcessInstanceId();
			if (map.containsKey(instanceId)) {
				((List) map.get(instanceId)).add(task);
			} else {
				List list = new ArrayList();
				list.add(task);
				map.put(instanceId, list);
			}
		}
		return map;
	}
	
	/**
	 * 驳回或者驳回到发起人
	 * @param processCmd
	 * @param taskEntity
	 * @param taskToken
	 * @return
	 * @throws Exception
	 */
	private void backPrepare(ProcessCmd processCmd,
			TaskEntity taskEntity, String taskToken) throws Exception {
		String actInstId = taskEntity.getProcessInstanceId();
		String aceDefId = taskEntity.getProcessDefinitionId();
		String taskId = taskEntity.getId();
		//不是返回
		if (processCmd.isBack().intValue() == 0){
			return ;
		}
		//已经设置了目标结点
		if(StringUtil.isNotEmpty(processCmd.getDestTask())){
			return ;
		}
		//驳回
		if (processCmd.isBack().equals(BpmConst.TASK_BACK)) {// 驳回
			/*FlowNode flowNode = (FlowNode) NodeCache.getByActDefId(aceDefId).get(taskEntity.getTaskDefinitionKey());
			List<FlowNode> nodeList=flowNode.getPreFlowNodes();
			if(nodeList!=null && nodeList.size()>0){
				String taskKeys=null;
				for(FlowNode node:nodeList){
					if(node.getNodeType().equals("userTask")){
						if(taskKeys==null){
							taskKeys=node.getNodeId();
						}else{
							taskKeys+=","+taskKeys;
						}
					}else if(node.getNodeType().equals("exclusiveGateway")){//如果是分支，那么往上寻在上一及的结点
						List<FlowNode> preNodeList=node.getPreFlowNodes();
						for(FlowNode preNode:preNodeList){
							if(preNode.getNodeType().equals("userTask")){
								if(taskKeys==null){
									taskKeys=preNode.getNodeId();
								}else{
									taskKeys+=","+taskKeys;
								}
							}
						}
					}
				}
				processCmd.setDestTask(taskKeys);
			}*/
			TaskOpinionEntity opin=this.taskOpinionService.getByTaskId(taskId);
			if(opin!=null && StringUtil.isNotEmpty(opin.getTaskKey())){
				processCmd.setDestTask(opin.getParentNodeId());
			}
		//回到初始结点
		} else if (processCmd.isBack() == BpmConst.TASK_BACK_TOSTART) {// 驳回到初始节点
			String backToNodeId = NodeCache.getFirstNodeId(aceDefId).getNodeId();// 初始节点id
			processCmd.setDestTask(backToNodeId);
		}
	}
	
	//跟新流程实例状态
	private void updateStatus(ProcessCmd cmd, ProcessInstanceEntity processRun) throws BusinessException{
		boolean isRecover = cmd.isRecover();
		int isBack = cmd.isBack().intValue();
		Short status = ProcessInstanceEntity.STATUS_RUNNING;
		switch (isBack) {
		case 0:
			status = ProcessInstanceEntity.STATUS_RUNNING;
			break;
		case 1:
			if (isRecover)
				status = ProcessInstanceEntity.STATUS_RECOVER;
			else
				status = ProcessInstanceEntity.STATUS_REJECT;
			break;
		case 2:
			if (isRecover)
				status = ProcessInstanceEntity.STATUS_RECOVER;
			else {
				status = ProcessInstanceEntity.STATUS_REJECT;
			}
		}
		processRun.setStatus(status.toString());
		update(processRun);
	}
	
	private void handSendMsgToStartUser(ProcessInstanceEntity processRun,
			DefinitionEntity bpmDefinition) throws Exception {
		String informStart = bpmDefinition.getInformStart();
		if (StringUtil.isEmpty(informStart))
			return;
		if (BeanUtils.isEmpty(processRun))
			return;
		String subject = processRun.getTitle();
		String startUserId = processRun.getCreateUserId();
		UserEntity user = this.sysUserService.getUserById(startUserId);
		List<UserEntity> receiverUserList = new ArrayList<UserEntity>();
		receiverUserList.add(user);

		Map<String,String> msgTempMap = this.msgTemplateService.getTempByFun(MsgTemplateEntity.USE_TYPE_NOTIFY_STARTUSER);
		this.taskMessageService.sendMessage(null, receiverUserList,informStart, msgTempMap, subject, "", null, processRun.getId(),null);
	}
	
	@Override
	@Action(moduleCode="processInstanceManager",description="流程实例新增",detail="流程实例${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ProcessInstanceEntity processInstance) throws BusinessException {
		String pk="";
		try {
			pk=this.processInstanceDao.addProcessInstance(processInstance);
			ProcessInstHistoryEntity history=new ProcessInstHistoryEntity();
			MyBeanUtils.copyBean2Bean(history, processInstance);
			processInstance.setId(pk);
			this.processInstHistoryService.save(history);
		} catch (Exception e) {
			logger.error("流程实例保存失败");
			throw new BusinessException("流程实例保存失败");
		}
		logger.info("流程实例保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="processInstanceManager",description="流程实例删除",detail="流程实例${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			ProcessInstanceEntity processRun = this.get(id);
			String procStatus = processRun.getStatus();
			if (!StringUtil.equals(ProcessInstanceEntity.STATUS_FINISH.toString(), procStatus) && !StringUtil.equals(ProcessInstanceEntity.STATUS_FORM.toString(), procStatus)) {//没有完成且不是草稿状态
				deleteProcessInstance(processRun);
			} else {
				delById(id);
			}
		} catch (Exception e) {
			logger.error("流程实例删除失败");
			throw new BusinessException("流程实例删除失败");
		}
		logger.info("流程实例删除成功");
	}

	private void deleteProcessInstance(ProcessInstanceEntity processRun) {
		ProcessInstanceEntity rootProcessRun = getRootProcessRun(processRun);
		deleteProcessRunCasade(rootProcessRun);
	}
	
	private ProcessInstanceEntity getRootProcessRun(ProcessInstanceEntity processRun) {
		ProcessInstance parentProcessInstance = (ProcessInstance) this.runtimeService
				.createProcessInstanceQuery().subProcessInstanceId(
						processRun.getActInstId()).singleResult();
		if (parentProcessInstance != null) {
			ProcessInstanceEntity parentProcessRun = getByActInstanceId(parentProcessInstance.getProcessInstanceId());

			return getRootProcessRun(parentProcessRun);
		}
		return processRun;
	}
	
	private void deleteProcessRunCasade(ProcessInstanceEntity processRun) {
		List<ProcessInstance> childrenProcessInstance = this.runtimeService
				.createProcessInstanceQuery().superProcessInstanceId(
						processRun.getActInstId()).list();
		for (ProcessInstance instance : childrenProcessInstance) {
			ProcessInstanceEntity pr = getByActInstanceId(instance.getProcessInstanceId());
			if (pr != null) {
				deleteProcessRunCasade(pr);
			}
		}
		String instanceId = processRun.getActInstId();
		String procStatus = processRun.getStatus();
		if (!StringUtil.equals(ProcessInstanceEntity.STATUS_FINISH.toString(), procStatus)) {
			this.taskActService.delCandidateByInstanceId(instanceId);
			this.taskActService.delByInstanceId(instanceId);
			this.executionExtService.delVariableByProcInstId(instanceId);
			this.executionExtService.delExecutionByProcInstId(instanceId);
		}
		String actDefId = processRun.getActDefId();
		
		try {
			delById(processRun.getId());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void delById(String id) throws BusinessException {
		this.processInstanceDao.deleteProcessInstance(id);
		this.processInstHistoryService.delete(id);
	}

	@Override
	@Action(moduleCode="ProcessInstanceManager",description="流程实例批量删除",detail="流程实例${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("流程实例批量删除成功");
	}

	@Override
	@Action(moduleCode="ProcessInstanceManager",description="流程实例修改",detail="流程实例${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ProcessInstanceEntity processInstance) throws BusinessException {
		try {
			
			
			ProcessInstHistoryEntity history = this.processInstHistoryService.get(processInstance.getId());
			history.setStatus(processInstance.getStatus());
			if (StringUtil.equals(ProcessInstanceEntity.STATUS_MANUAL_FINISH.toString() , processInstance.getStatus())
					|| StringUtil.equals(ProcessInstanceEntity.STATUS_DELETE.toString() , processInstance.getStatus())
					|| StringUtil.equals(ProcessInstanceEntity.STATUS_FINISH.toString() , processInstance.getStatus())) {
				Date endDate = new Date();
				Date startDate = history.getCreateTime();
				String duration =DateUtils.getTime(DateUtils.getTime(startDate, endDate));
				history.setEndTime(endDate);
				history.setDuration(duration);
				this.processInstHistoryService.update(history);
				this.processInstanceDao.deleteProcessInstance(processInstance.getId());;
			} else {
				//修改历史
				this.processInstHistoryService.update(history);
				//修改流程实例
				ProcessInstanceEntity oldEntity = get(processInstance.getId());
				MyBeanUtils.copyBeanNotNull2Bean(processInstance, oldEntity);
				this.processInstanceDao.updateProcessInstance(oldEntity);
			}
		} catch (Exception e) {
			logger.error("流程实例更新失败");
			throw new BusinessException("流程实例更新失败");
		}
		logger.info("流程实例更新成功");
	}

	@Override
	public ProcessInstanceEntity get(String id) {
		ProcessInstanceEntity ProcessInstance=null;
		try {
			ProcessInstance=this.processInstanceDao.getProcessInstance(id);
		} catch (Exception e) {
			logger.error("流程实例获取失败");
			//throw new BusinessException("流程实例获取失败");
		}
		logger.info("流程实例获取成功");
		return ProcessInstance;
	}

	@Override
	public List<ProcessInstanceEntity> queryList() throws BusinessException {
		List<ProcessInstanceEntity> ProcessInstanceList=new ArrayList<ProcessInstanceEntity>();
		try {
			ProcessInstanceList=this.processInstanceDao.queryProcessInstanceList();
		} catch (Exception e) {
			logger.error("流程实例获取列表失败");
			throw new BusinessException("流程实例获取列表失败");
		}
		logger.info("流程实例获取列表成功");
		return ProcessInstanceList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.processInstanceDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程实例获取分页列表失败");
			throw new BusinessException("流程实例获取分页列表失败");
		}
		logger.info("流程实例获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return false;//this.baseService.isUnique(ProcessInstanceEntity.class, param, propertyName);
	}
	
	@Override
	public ProcessInstanceEntity getByActInstanceId(String actInstId){
		List<ProcessInstanceEntity> list=this.processInstanceDao.findByProperty(ProcessInstanceEntity.class, "actInstId", actInstId);
	    if(list!=null && list.size()>0){
	    	return list.get(0);
	    }
		return null;
	}
	
	/**
	 * @Decription 启动流程
	 * @author xiehs
	 * @createtime 2014年8月19日 下午12:58:31
	 * @param processCmd
	 * @return
	 * @throws Exception
	 */
	public void startProcess(ProcessCmd processCmd) throws BusinessException {
		DefinitionEntity bpmDefinition = getBpmDefinitionProcessCmd(processCmd);
		processCmd.getVariables().put("actDefKey", bpmDefinition.getActKey());
		if (bpmDefinition == null){
			throw new BusinessException("没有该流程的定义");
		}
		if (DefinitionEntity.STATUS_DISABLED.equals(bpmDefinition.getStatus())) {
			throw new BusinessException("该流程已经被禁用");
		}
		if (DefinitionEntity.PUBLISHED_NO.equals(bpmDefinition.getPublished())) {
			throw new BusinessException("该流程还未被发布");
		}
		String defId = bpmDefinition.getId();
		String skipFirstNode=bpmDefinition.getSkipFirstNode();
		String actDefId = bpmDefinition.getActId();
		//开始结点后第一个结点的id
		String nodeId = getFirstNodetByDefId(actDefId);
		//流程开始节点实体
		NodeSetEntity bpmNodeSet = getStartBpmNodeSet(defId, nodeId);

		ProcessInstanceEntity processRun = processCmd.getProcessRun();
		String businessKey = processCmd.getBusinessKey();
		try {
			Short toFirstNode=0;
			if(StringUtil.equals(skipFirstNode, "1")){
				toFirstNode=1;
			}
			TaskThreadService.setToFirstNode(toFirstNode);

			if (toFirstNode.shortValue() == 1) {
				List<TaskExecutor> excutorList = new ArrayList<TaskExecutor>();
				UserEntity user=ClientUtil.getUserEntity();
				excutorList.add(TaskExecutor.getTaskUser(user.getId().toString(), user.getName()));
				this.taskUserAssignService.addNodeUser(nodeId, excutorList);
			}
			setTaskUser(processCmd);
			if (BeanUtils.isEmpty(processRun)) {
				processRun = initProcessRun(bpmDefinition);
				processRun.setBusinessKey(processCmd.getBusinessKey());
				if(processCmd.getVariables().get("formCode")!=null){
					processRun.setFormCode((String)processCmd.getVariables().get("formCode"));
				}
				processRun.setParentId((processCmd.getParentBusinessKey()));
				processRun.setBusinessName(processCmd.getBusinessName());
			} 
			//处理表单数据
			if (!processCmd.isInvokeExternal()) {
				/*BpmFormData bpmFormData = handlerFormData(processCmd,processRun, "");
				if (bpmFormData != null) {
					businessKey = processCmd.getBusinessKey();
					processRun.setTableName(bpmFormData.getTableName());
					if (bpmFormData.getPkValue() != null) {
						processRun.setPkName(bpmFormData.getPkValue().getName());
						processRun.setDsAlias(bpmFormData.getDsAlias());
					}
				}*/
			}
			//前置处理器
			if (!processCmd.isSkipPreHandler()) {
				invokeHandler(processCmd, bpmNodeSet, true);
			}
			if (StringUtil.isEmpty(businessKey)) {
				businessKey = processCmd.getBusinessKey();
			}

			String subject = getSubject(bpmDefinition, processCmd);
			processCmd.addVariable("flowRunId", processRun.getId());
			processCmd.addVariable("subject_", subject);

			

			
			processRun.setBusinessKey(businessKey);

			processRun.setTitle(subject);

			
			processRun.setStatus(ProcessInstanceEntity.STATUS_RUNNING.toString());
			
			processCmd.setProcessRun(processRun);
			
			TaskThreadService.setProcessCmd(processCmd);
			
			ProcessInstance processInstance = startWorkFlow(processCmd);//开始转向activiti处理，保存流程实例，往后流转
			
			String processInstanceId = processInstance.getProcessInstanceId();

			//String pk=save(processRun);

			List<Task> taskList = TaskThreadService.getNewTasks();
			//跟踪记录节点的路径
			//this.executionStackService.initStack(processInstanceId, taskList);

			
			//后置处理器
			if (!processCmd.isSkipAfterHandler()) {
				invokeHandler(processCmd, bpmNodeSet, false);
			}

			// 是否跳过第一个结点
			if (toFirstNode.shortValue() == 1) {
				handJumpOverFirstNode(processInstanceId, processCmd);
			}
			
			if (!processCmd.isInvokeExternal()) {
				ProcessInstFormEntity processInstForm=new ProcessInstFormEntity();
				processInstForm.setActDefId(actDefId);
				processInstForm.setActInstId(processInstanceId);
				//processInstForm.setInstId(pk);
				//this.processInstFormService.save(processInstForm);
			}

			String informType = processCmd.getInformType();
			NodeSetEntity taskNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(actDefId, ((Task) taskList.get(0)).getTaskDefinitionKey());
			if (StringUtil.isEmpty(informType)) {
				if (StringUtil.isNotEmpty(taskNodeSet.getInformType()))
					informType = taskNodeSet.getInformType();
				else {
					informType = bpmDefinition.getInformType();
				}
				processCmd.setInformType(informType);
			}
			//发送消息
			this.taskMessageService.notify(TaskThreadService.getNewTasks(),informType, processRun.getTitle(), null, "",processCmd.getVariables());
			//添加日志
			//String memo = "启动流程:" + subject;
			//this.bpmRunLogService.addRunLog(processRun.getRunId(),BpmRunLog.OPERATOR_TYPE_START, memo);
			//添加提交意见
			addSubmitOpinion(processRun);
			//处理代理
			handleAgentTaskExe(processCmd);
		} catch (Exception ex) {
			new BusinessException("流程启动失败");
		} finally {
			clearThreadLocal();
		}

	}
	
	private ProcessInstance startWorkFlow(ProcessCmd processCmd) {
		String businessKey = processCmd.getBusinessKey();
		ProcessInstance processInstance = null;
		if (StringUtil.isNotEmpty(businessKey)) {
			processCmd.getVariables().put("businessKey", businessKey);
		} 
		Authentication.setAuthenticatedUserId(ClientUtil.getUserEntity().getId());
		if (processCmd.getActDefId() != null)
			processInstance = this.flowService.startFlowById(processCmd.getActDefId(), businessKey, processCmd.getVariables());
		else {
			processInstance = this.flowService.startFlowByKey(processCmd.getFlowKey(), businessKey, processCmd.getVariables());
		}
		Authentication.setAuthenticatedUserId(null);
		return processInstance;
	}
	
	/**
	 * 流程节点往下走
	 * 
	 * @param processCmd
	 * @return
	 * @throws Exception
	 */
	public ProcessInstanceEntity nextProcess(ProcessCmd processCmd) throws BusinessException {
		ProcessInstanceEntity processRun = null;
		String taskId = "";
		TaskEntity taskEntity = getTaskEntByCmd(processCmd);
		
		if (taskEntity == null)
			return null;
		
		taskId = taskEntity.getId();
		processCmd.setTaskId(taskId);
		processCmd.setNodeId(taskEntity.getTaskDefinitionKey());
		if ((taskEntity.getExecutionId() == null)&& (TaskOpinionEntity.STATUS_COMMUNICATION.toString().equals(taskEntity.getDescription()))) {
			return null;
		}

		//设置下一个路径的变量
		Object nextPathObj = processCmd.getFormDataMap().get("nextPathId");
		if (nextPathObj != null) {
			this.flowService.setTaskVariable(taskId, "NextPathId", nextPathObj.toString());
		}
		String parentNodeId = taskEntity.getTaskDefinitionKey();
		String actDefId = taskEntity.getProcessDefinitionId();
		String actInstId = taskEntity.getProcessInstanceId();
		String executionId = taskEntity.getExecutionId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		processRun = this.processInstanceDao.getgetByActInstanceId(actInstId);
		processCmd.getVariables().put("groupId", processRun.getGroupId());
		processCmd.setProcessRun(processRun);
		try {
			String taskToken = (String) this.taskService.getVariableLocal(taskId, TaskForkEntity.TAKEN_VAR_NAME);
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(actDefId, parentNodeId);
			//设置下一步执行人
			setTaskUser(processCmd);//动态选择的人员
			
			if (!processCmd.isInvokeExternal()) {//任务节点中所有的任务都结束
				/*//记录数据
				BpmFormData bpmFormData = handlerFormData(processCmd,processRun, parentNodeId);
				//更新审批意见
				if (bpmFormData != null) {
					Map optionsMap = bpmFormData.getOptions();
					updOption(optionsMap, processCmd);
				}*/
				//updOption(optionsMap, processCmd);
			}
			
			//前置处理器
			if (!processCmd.isSkipPreHandler()) {
				invokeHandler(processCmd, nodeSet, true);
			}

			// 回退数据组装
			backPrepare(processCmd, taskEntity,taskToken);// 组装驳回 1.驳回 2.驳回到初始

			/*if (parentStack != null) {
				parentNodeId = parentStack.getNodeId();
			}*/
			
			//设置投票人或者会签人员
			signUsersOrSignVoted(processCmd, taskEntity);

			processCmd.setSubject(processRun.getTitle());
			//设置流程变量
			setVariables(taskId, processCmd);

			if (NodeSetEntity.JUMP_TYPE_SELF.equals(processCmd.getJumpType())) {
				processCmd.setDestTask(taskEntity.getTaskDefinitionKey());
			}
			TaskOpinionEntity opinion=this.taskOpinionService.getByTaskId(taskId);
			if(opinion!=null && StringUtil.equals("2", opinion.getBackType())){//反馈
				processCmd.setDestTask(opinion.getBackNodeId());
			}
			//addInterVene(processCmd, taskEntity);

			//流程流转
			completeTask(processCmd, taskEntity);

			//后置处理器
			if (!processCmd.isSkipAfterHandler()) {
				invokeHandler(processCmd, nodeSet, false);
			}

			if ((StringUtil.isEmpty(processRun.getBusinessKey()))
					&& (StringUtil.isNotEmpty(processCmd.getBusinessKey()))) {
				processRun.setBusinessKey(processCmd.getBusinessKey());
				this.runtimeService.setVariable(executionId, "businessKey",processCmd.getBusinessKey());
			}

			//节点审批后的节点栈维护
			/*if ((processCmd.isBack().intValue() > 0) && (parentStack != null)) {// 如果是往后退，那么删除栈节点
				this.executionStackService.pop(parentStack, processCmd.isRecover(), processCmd.isBack());//完成的节点出栈
			} else {
				List map = TaskThreadService.getExtSubProcess();
				if (BeanUtils.isEmpty(map)) {
					this.executionStackService.addStack(actInstId,parentNodeId, taskToken);
				} else {//
					initExtSubProcessStack();
				}
			}*/

			if ((processCmd.isBack().intValue() > 0)|| (StringUtil.isNotEmpty(processCmd.getDestTask()))) {
				this.taskActService.updateOldTaskDefKeyByInstIdNodeId(taskEntity.getTaskDefinitionKey()+ "_1", taskEntity.getTaskDefinitionKey(), taskEntity.getProcessInstanceId());
			}
			//获取下一批任务
			List<Task> taskList = TaskThreadService.getNewTasks();// 获取所有的threadlocaldddd任务
			
			String informType = processCmd.getInformType();
			if(taskList!=null){
				if (StringUtil.isEmpty(informType)) {
					NodeSetEntity taskNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(actDefId, ((Task) taskList.get(0)).getTaskDefinitionKey());
					if (taskNodeSet==null)
						informType = bpmDefinition.getInformType();
					else if (StringUtil.isNotEmpty(taskNodeSet.getInformType()))
						informType = taskNodeSet.getInformType();
					else {
						informType = bpmDefinition.getInformType();
					}
					processCmd.setInformType(informType);
				}
			}
			processCmd.getVariables().put("businessKey", processCmd.getBusinessKey());
			this.taskMessageService.notify(taskList,informType, processRun.getTitle(), null, "",processCmd.getVariables());
			//设置下一个任务的转办代理
			handleAgentTaskExe(processCmd);
			//完成本任务的转办代理
			this.taskExeService.complete(taskId);

			if (TaskThreadService.getObject() == null) {//流程没有结束，只是改变状态
				updateStatus(processCmd, processRun);
			} else {//流程结束，抄送
				Map vars = TaskThreadService.getVariables();
				//抄送到指定任务人
				this.processInstCptoService.handlerCopyTask(processRun, vars, processCmd.getCurrentUserId().toString(), bpmDefinition,NodeUserEntity.FUNC_END_CC_USER);
				//抄送到流程发起人
				if (StringUtil.isNotEmpty(bpmDefinition.getInformStart())) {//是否抄送到发起人
					//handSendMsgToStartUser(processRun, bpmDefinition);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			clearThreadLocal();
		}
		return processRun;
	}
	
	public void setProcessInstanceDao(ProcessInstanceDao processInstanceDao) {
		this.processInstanceDao = processInstanceDao;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.processInstanceDao.executeHql("delete from ProcessInstanceEntity t where t.actDefId='"+actDefId+"'");
	}
	

	@Override
	public boolean getHasRightsByTask(String taskId, String userId) {
		// TODO Auto-generated method stub
		return this.taskActService.getHasRightsByTask(taskId, userId);
	}

	@Override
	public void executeRecover(String actInstId, String informType, String memo)
			throws BusinessException {
		// TODO Auto-generated method stub
		ProcessInstanceEntity processRun = this.getByActInstanceId(actInstId);

		String status = processRun.getStatus();
		if (StringUtil.equals(status, ProcessInstanceEntity.STATUS_FINISH.toString())) {
			throw new BusinessException("对不起,此流程实例已经结束!");
		}
		if (StringUtil.equals(status, ProcessInstanceEntity.STATUS_MANUAL_FINISH.toString())) {
			throw new BusinessException("对不起,此流程实例已经被删除!");
		}

		boolean isCreator = StringUtil.equals(processRun.getCreateUserId(), ClientUtil.getUserEntity().getId());
		if (!isCreator) {
			throw new BusinessException("对不起,非流程发起人不能撤销到开始节点!");
			
		}

		FlowNode flowNode=null;
		try {
			flowNode = NodeCache.getFirstNodeId(processRun.getActDefId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw new BusinessException("撤销失败!");
		}
		String nodeId = flowNode.getNodeId();

		List taskList = this.flowService.getTasks(processRun.getActInstId());
		List taskNodeIdList = getNodeIdByTaskList(taskList);

		if (taskNodeIdList.contains(nodeId)) {
			throw new BusinessException("当前已经是开始节点!");
		}

		boolean hasRead = getTaskHasRead(taskList);
		if ((hasRead) && ("".equals(memo))) {
			throw new BusinessException("对不起,请填写撤销的原因!");
		}

		boolean allowBack = getTaskAllowBack(taskList);
		if (!allowBack) {
			throw new BusinessException("对不起,当前流程实例不允许撤销!");
		}
		try {
			backToStart(memo, taskList, informType);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("撤销失败!");
		}
		
	}

	@Override
	public void executeRedo(String actInstId, String informType, String memo)throws BusinessException {
		// TODO Auto-generated method stub
		ProcessInstanceEntity processRun = this.getByActInstanceId(actInstId);

		String instanceId = processRun.getActInstId();
		List<ProcessTask> tasks = this.flowService.getTasks(instanceId);
		List<Task> taskList = new ArrayList<Task>();
		for (ProcessTask task : tasks) {
			taskList.add(this.flowService.getTask(task.getId()));
		}
		ProcessTask taskEntity = (ProcessTask) tasks.get(0);

		ProcessCmd processCmd = new ProcessCmd();
		processCmd.setTaskId(taskEntity.getId());

		processCmd.setRecover(true);
		processCmd.setBack(BpmConst.TASK_BACK);
		processCmd.setVoteAgree(TaskOpinionEntity.STATUS_RECOVER);
		processCmd.setVoteContent(memo);
		processCmd.setInformType(informType);

		MsgTemplateEntity msgTemplate = this.msgTemplateService.getDefaultByUseType(MsgTemplateEntity.USE_TYPE_REVOKED);
		Map<String,String> map = getTempByUseType(MsgTemplateEntity.USE_TYPE_REVOKED);
		this.taskMessageService.notify(taskList, informType, msgTemplate.getTitle(), map, memo,processCmd.getVariables());

		nextProcess(processCmd);
	}

	@Override
	public Page<InstanceVo> queryRequestInstanceList(Page<InstanceVo> page) {
		// TODO Auto-generated method stub
		List<InstanceVo> list=this.instanceMybatisDao.getRequstInstanceListByPage(page);
		if(list!=null && list.size()>0){
			for(InstanceVo instance:list){
				Map<String, Object> extra;
				try {
					if(StringUtil.isNotEmpty(instance.getFormCode())){
						extra = appFormTableService.getOneFieldData(instance.getFormCode(),instance.getBusinessKey());
						if(extra==null){
							extra=new HashMap<String,Object>();
							//extra.put("status", 0);
						}/*else{
							if(StringUtil.isNotEmpty(instance.getStatus())){
								extra.put("status",Integer.parseInt(instance.getStatus()));
							}else{
								extra.put("status", 0);
							}
						}*/
						String groupId=instance.getGroupId();
						if(StringUtil.isNotEmpty(groupId)){
							extra.put("groupId", groupId);
							Map<String,Object> groupUserList=orgGroupService.getUserOrg(groupId);
							extra.put("groupUserList", groupUserList.get("OrgGroupMember"));
						}
						instance.setExtra(JSONHelper.toJSONString(extra));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		page.setResult(list);
		return page;
	}

	@Override
	public Page<InstanceVo> queryCompleteInstanceList(Page<InstanceVo> page) {
		// TODO Auto-generated method stub
		page.setResult(this.instanceMybatisDao.getCompleteInstanceListByPage(page));
		return page;
	}

	@Override
	public void batchComplte(String taskIds, String opinion, String voteAgree) throws BusinessException{
		// TODO Auto-generated method stub
		try {
			String[] aryTaskId = taskIds.split(",");
			for (String taskId : aryTaskId) {
				TaskEntity taskEntity = this.flowService.getTask(taskId);
				ProcessInstanceEntity processRun =this.getByActInstanceId(taskEntity.getProcessInstanceId());
				String subject = processRun.getTitle();
				ProcessCmd processCmd = new ProcessCmd();
				processCmd.setSubject(subject);
				processCmd.setVoteAgree(Short.valueOf(voteAgree));
				processCmd.setVoteContent(opinion);
				processCmd.setTaskId(taskId);
				this.nextProcess(processCmd);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("批量审批失败");
		}
		
	}
	
	//转发
	@Override
	public void executeDivertProcess(ProcessInstHistoryEntity hisInst,
			List<String> targetEmpIds, UserEntity currUser, String informType,
			String suggestion) throws BusinessException {
		// TODO Auto-generated method stub
		String instanceId = hisInst.getActInstId();
		String actDefId = hisInst.getActDefId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		String currUid = currUser.getId();
		if (BeanUtils.isEmpty(bpmDefinition)) {
			throw new BusinessException("流程定义为空");
		}
		if (BeanUtils.isEmpty(targetEmpIds))
			throw new BusinessException("目标用户为空");
		
		List<UserEntity> userList = new ArrayList<UserEntity>();
		try {
			for (String empId : targetEmpIds) {
				UserEntity destUser = this.sysUserService.getUserById(empId);
				if(destUser==null){
					continue;
				}
				if (StringUtil.equals(destUser.getId(),currUid)) {
					continue;
				}
				ProcessInstCptoEntity bpmProCopyto = new ProcessInstCptoEntity();
				bpmProCopyto.setActInsId(instanceId);
				bpmProCopyto.setReceiveId(destUser.getId());
				bpmProCopyto.setReceiveName(destUser.getName());
				bpmProCopyto.setType(ProcessInstCptoEntity.CPTYPE_SEND);
				bpmProCopyto.setOpionion(suggestion);
				bpmProCopyto.setIsRead("0");
				bpmProCopyto.setInsId(hisInst.getId());
				bpmProCopyto.setSubject(hisInst.getTitle());
				this.processInstCptoService.save(bpmProCopyto);
				userList.add(destUser);
			}
			Map<String,String> msgTempMap = this.msgTemplateService.getTempByFun(MsgTemplateEntity.USE_TYPE_FORWARD);
			String subject = hisInst.getTitle();
			String runId = hisInst.getId();
			this.taskMessageService.sendMessage(currUser, userList, informType,
					msgTempMap, subject, suggestion, null, runId,null);
		} catch (Exception e) {
			throw new BusinessException("转发失败");
		}
	}
	
	public List<ProcessInstanceEntity> getProcessByParentBusinessKey(String businessKey){
		return this.processInstanceDao.findByProperty(ProcessInstanceEntity.class, "parentId", businessKey);
	}
	
	public ProcessInstanceEntity getProcessBusinessKey(String businessKey){
		List<ProcessInstanceEntity> list= this.processInstanceDao.findByProperty(ProcessInstanceEntity.class, "businessKey", businessKey);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
