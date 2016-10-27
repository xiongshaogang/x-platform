package com.xplatform.base.workflow.core.facade.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.script.IScript;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.model.NodeTranUser;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.model.TaskAmount;
import com.xplatform.base.workflow.core.bpm.model.TaskNodeInfo;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.dao.FlowDao;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.execution.mybatis.vo.ProcessExecution;
import com.xplatform.base.workflow.execution.service.ExecutionExtService;
import com.xplatform.base.workflow.history.mybatis.vo.ProcessTaskHistory;
import com.xplatform.base.workflow.history.service.HistoryTaskInstanceService;
import com.xplatform.base.workflow.history.service.HistoryActivitiInstanceService;
import com.xplatform.base.workflow.history.service.HistoryProcessInstanceService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.model.NodeUserMap;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeUserService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.mybatis.vo.TaskUser;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskForkService;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.task.service.TaskUserService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

@Service("flowService")
public class FlowServiceImpl implements FlowService,IScript{
	

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Resource
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Resource
	private ProcessEngineConfiguration processEngineConfiguration;
	
	@Resource
	private MsgTemplateService msgTemplateService;
	@Resource
	private DefinitionService definitionService;
	
	@Resource
	private ProcessInstanceService processInstanceService;
	
	@Resource
	private TaskOpinionService taskOpinionService;
	
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private NodeUserService nodeUserService;
	
	@Resource
	private TaskForkService taskForkService;
	
	@Resource
	private FlowDao flowDao;
	
	@Resource
	private TaskActService taskActService;
	
	@Resource
	private ExecutionExtService executionExtService;
	
	@Resource
	private TaskMessageService taskMessageService;
	
	@Resource
	private HistoryActivitiInstanceService historyActivitiInstanceService;
	
	@Resource
	private HistoryTaskInstanceService historicTaskInstanceService;
	
	@Resource
	private HistoryProcessInstanceService historyProcessInstanceService;
	
	@Resource
	private TaskUserService taskUserService;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private TaskNodeStatusService taskNodeStatusService;
	
	@Resource
	private IdGenerator idGenerator;
	
	public ProcessInstance startFlowById(String proessDefId,
			Map<String, Object> variables) {
		ProcessInstance instance = this.runtimeService
				.startProcessInstanceById(proessDefId, variables);
		return instance;
	}

	public ProcessInstance startFlowById(String porcessDefId,
			String businessKey, Map<String, Object> variables) {
		ProcessInstance processInstance = this.runtimeService
				.startProcessInstanceById(porcessDefId, businessKey, variables);

		return processInstance;
	}

	/**
	 * 根据流程key开始生成流程实例
	 * @param processDefKey
	 * @param businessKey
	 * @param variables
	 * @return
	 */
	public ProcessInstance startFlowByKey(String processDefKey,
			String businessKey, Map<String, Object> variables) {
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(processDefKey, businessKey,variables);
		return processInstance;
	}
	
	/**
	 * 激活流程
	 * @author xiehs
	 * @createtime 2014年10月2日 上午10:42:13
	 * @Decription
	 *
	 * @param actDefId
	 * @throws BusinessException
	 */
	public void activeProcessDefinition(String actDefId) throws BusinessException{
		try{
			this.repositoryService.activateProcessDefinitionById(actDefId);
		}catch(Exception e){
			throw new BusinessException("激活失败");
		}
	}
	
	/**
	 * 终止流程
	 * @author xiehs
	 * @createtime 2014年10月2日 上午10:42:22
	 * @Decription
	 *
	 * @param actDefId
	 * @throws BusinessException
	 */
	public void stopProcessDefinition(String actDefId) throws BusinessException{
		try{
			this.repositoryService.suspendProcessDefinitionById(actDefId);
		}catch(Exception e){
			throw new BusinessException("冻结失败");
		}
	}

	/**
	 * 发布流程
	 * @param name
	 * @param xml
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Deployment deploy(String name, String xml)
			throws UnsupportedEncodingException {
		InputStream stream = new ByteArrayInputStream(xml.getBytes("utf-8"));
		DeploymentBuilder deploymentBuilder = this.repositoryService
				.createDeployment();
		deploymentBuilder.name(name);
		deploymentBuilder.addInputStream("bpmn20.xml", stream);
		Deployment deploy = deploymentBuilder.deploy();

		return deploy;
	}
	
	public void transTo(String taskId, String toNode)throws BusinessException {
		TaskEntity task = getTask(taskId);

		ProcessDefinitionEntity processDefinition = getProcessDefinitionEntity(task.getProcessDefinitionId());

		ActivityImpl curActi = processDefinition.findActivity(task.getTaskDefinitionKey());

		ActivityImpl destAct = null;
		String[] toNodes=null;
		if (StringUtil.isNotEmpty(toNode)){
			toNodes=toNode.split(",");
			for(String nodeId:toNodes){
				//目标节点为空
				destAct = processDefinition.findActivity(nodeId);
				//若目标节点为空，则代表没有进行自由跳转，同时后续的节点也不为汇总节点
				if (destAct == null) {
					this.taskService.complete(task.getId());
					return;
				}
				//清空当前流向
				List<PvmTransition> nowPvmTransitionList = clearTransition(curActi);
				//创建新的流向
				TransitionImpl transitionImpl = curActi.createOutgoingTransition();
				transitionImpl.setDestination(destAct);
				//完成流转
				this.taskService.complete(task.getId());
				// 删除目标节点新流入
				curActi.getOutgoingTransitions().remove(transitionImpl);
				// 还原以前流向
				restoreTransition(curActi, nowPvmTransitionList);
			}
		}else{
			//若目标节点为空，则代表没有进行自由跳转，同时后续的节点也不为汇总节点
			if (destAct == null) {
				this.taskService.complete(task.getId());
				return;
			}
		}
		
		
	}
	
	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl,
			List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	public Map<String, String> genCommunicationTask(TaskEntity ent, String[] users,
			UserEntity sysUser) {
		if (BeanUtils.isEmpty(users))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		String parentId = ent.getId();
		for (String userId : users) {
			if (userId.equals(sysUser.getId()))
				continue;
			String taskId = this.idGenerator.getNextId();
			TaskEntity task = (TaskEntity) this.taskService.newTask(taskId);
			task.setAssignee(userId);
			task.setOwner(userId);
			task.setCreateTime(new Date());
			task.setName(ent.getName());

			task.setParentTaskId(parentId);
			task.setTaskDefinitionKey(ent.getTaskDefinitionKey());
			task.setProcessInstanceId(ent.getProcessInstanceId());
			task.setDescription(TaskOpinionEntity.STATUS_COMMUNICATION.toString());
			task.setProcessDefinitionId(ent.getProcessDefinitionId());
			this.taskService.saveTask(task);
			map.put(userId, taskId);
		}
		return map;
	}
	/*
	public Map<Long, Long> genTransToTask(TaskEntity ent, String[] users,
			SysUser sysUser) {
		if (BeanUtils.isEmpty(users))
			return null;
		Map map = new HashMap();
		String parentId = ent.getId();
		for (String userId : users) {
			if (userId.equals(sysUser.getUserId().toString()))
				continue;
			String taskId = String.valueOf(UniqueIdUtil.genId());
			TaskEntity task = (TaskEntity) this.taskService.newTask(taskId);
			task.setAssignee(userId);
			task.setOwner(userId);
			task.setCreateTime(new Date());
			task.setName(ent.getName());

			task.setParentTaskId(parentId);
			task.setTaskDefinitionKey(ent.getTaskDefinitionKey());
			task.setProcessInstanceId(ent.getProcessInstanceId());
			task.setDescription(TaskOpinionEntity.STATUS_TRANSTO.toString());
			task.setProcessDefinitionId(ent.getProcessDefinitionId());
			this.taskService.saveTask(task);
			map.put(Long.valueOf(userId), Long.valueOf(taskId));
		}
		return map;
	}*/

	public void onlyCompleteTask(String taskId) {
		TaskEntity task = getTask(taskId);

		ProcessDefinitionEntity processDefinition = getProcessDefinitionEntity(task.getProcessDefinitionId());

		ActivityImpl curActi = processDefinition.findActivity(task.getTaskDefinitionKey());

		List backTransList = new ArrayList();
		backTransList.addAll(curActi.getOutgoingTransitions());

		curActi.getOutgoingTransitions().clear();
		this.taskService.complete(task.getId());
	}

	public ProcessDefinitionEntity getProcessDefinitionEntity(
			String processDefinitionId) {
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		return processDefinition;
	}
	
	public ProcessDefinitionEntity getProcessDefinitionByDeployId(
			String deployId) {
		ProcessDefinition proDefinition = (ProcessDefinition) this.repositoryService
				.createProcessDefinitionQuery().deploymentId(deployId)
				.singleResult();
		if (proDefinition == null)
			return null;
		return getProcessDefinitionByDefId(proDefinition.getId());
	}

	public ProcessDefinitionEntity getProcessDefinitionByDefId(String actDefId) {
		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
				.getDeployedProcessDefinition(actDefId);
		return ent;
	}
	
	/**
	 * 通过流程实例获取流程定义
	 * @author xiehs
	 * @createtime 2014年8月11日 下午6:24:59
	 * @Decription
	 *
	 * @param processInstanceId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionByProcessInanceId(String processInstanceId) {
		String processDefinitionId = null;
		//获取流程实例
		ProcessInstance processInstance = (ProcessInstance) this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			//历史流程是例
			HistoricProcessInstance hisProInstance = (HistoricProcessInstance) this.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			processDefinitionId = hisProInstance.getProcessDefinitionId();
		} else {
			processDefinitionId = processInstance.getProcessDefinitionId();
		}
		//通过流程定义id获取流程定义
		return getProcessDefinitionByDefId(processDefinitionId);
	}

	
	
	public ProcessDefinitionEntity getProcessDefinitionByTaskId(String taskId) {
		TaskEntity taskEntity = (TaskEntity) this.taskService.createTaskQuery()
				.taskId(taskId).singleResult();
		return getProcessDefinitionByDefId(taskEntity.getProcessDefinitionId());
	}

	public List<ProcessDefinition> getProcessDefinitionByKey(String key) {
		List list = this.repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(key).list();
		return list;
	}

	

	public List<String> getActiveTasks(String taskId) {
		List acts = new ArrayList();
		TaskEntity taskEntity = (TaskEntity) this.taskService.createTaskQuery()
				.taskId(taskId).singleResult();

		List<ProcessTask> tasks = this.taskActService.getByInstanceId(taskEntity.getProcessInstanceId());

		for (ProcessTask task : tasks) {
			acts.add(task.getName());
		}

		return acts;
	}

	public Map<String, String> getOutNodesByTaskId(String taskId) {
		Map map = new HashMap();
		Task task = getTask(taskId);
		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());

		ActivityImpl curActi = ent.findActivity(task.getTaskDefinitionKey());

		List<PvmTransition> outs = curActi.getOutgoingTransitions();
		for (PvmTransition tran : outs) {
			ActivityImpl destNode = (ActivityImpl) tran.getDestination();
			map.put(destNode.getId(), (String) destNode.getProperty("name"));
		}
		return map;
	}

	public List<String> getActiveActIdsByTaskId(String taskId) {
		TaskEntity taskEntity = (TaskEntity) this.taskService.createTaskQuery()
				.taskId(taskId).singleResult();
		return getActiveActIdsByProcessInstanceId(taskEntity
				.getProcessInstanceId());
	}

	public List<String> getActiveActIdsByProcessInstanceId(
			String processInstanceId) {
		List acts = new ArrayList();
		List<ProcessTask> taskList = getTasks(processInstanceId);

		for (ProcessTask entity : taskList) {
			acts.add(entity.getTaskDefinitionKey());
		}

		return acts;
	}
	
	/**
	 * 通过流程发布id获取流程xml
	 * @param deployId
	 * @return
	 */
	public String getDefXmlByDeployId(String deployId) {
		return this.flowDao.getDefXmlByDeployId(deployId);
	}

	public String getDefXmlByProcessDefinitionId(String processDefinitionId) {
		ProcessDefinitionEntity entity = getProcessDefinitionByDefId(processDefinitionId);
		if (entity == null) {
			return null;
		}
		String defXml = getDefXmlByDeployId(entity.getDeploymentId());
		return defXml;
	}
	
	//根据任务id的到流程定义的xml
	public String getDefXmlByProcessTaskId(String taskId) {
		ProcessDefinitionEntity entity = getProcessDefinitionByTaskId(taskId);
		if (entity == null) {
			return null;
		}
		String defXml = getDefXmlByDeployId(entity.getDeploymentId());
		return defXml;
	}
	
	public String getDefXmlByProcessProcessInanceId(String processInstanceId) {
		ProcessDefinitionEntity entity = getProcessDefinitionByProcessInanceId(processInstanceId);
		if (entity == null) {
			return null;
		}
		String defXml = getDefXmlByDeployId(entity.getDeploymentId());
		return defXml;
	}
	
	public void wirteDefXml(String deployId, String defXml) {
		this.flowDao.wirteDefXml(deployId, defXml);
		//ProcessDefinitionEntity ent = getProcessDefinitionByDeployId(deployId);
		((ProcessEngineConfigurationImpl) this.processEngineConfiguration).getDeploymentManager().getProcessDefinitionCache().clear();
		//((ProcessEngineConfigurationImpl) this.processEngineConfiguration).getDeploymentCache();
	}

	//获取所有的流程结点
	public List<ActivityImpl> getActivityNodes(String actDefId) {
		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
				.getDeployedProcessDefinition(actDefId);
		return ent.getActivities();
	}
	
	//判断是否会签任务
	public boolean isSignTask(String actDefId, String nodeId) {
		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
				.getDeployedProcessDefinition(actDefId);
		List<ActivityImpl> list = ent.getActivities();
		for (ActivityImpl actImpl : list) {
			if (actImpl.getId().equals(nodeId)) {
				String multiInstance = (String) actImpl
						.getProperty("multiInstance");
				if (multiInstance != null)
					return true;
			}
		}
		return false;
	}
	
	//根据任务id获取任务实体
	public TaskEntity getTask(String taskId) {
		TaskEntity taskEntity = (TaskEntity) this.taskService.createTaskQuery().taskId(taskId).singleResult();
		return taskEntity;
	}
	
	public Map<String, Map<String, String>> getJumpNodes(String taskId) {
		List<String> actIds = getActiveActIdsByTaskId(taskId);
		TaskEntity taskEntity = (TaskEntity) this.taskService.createTaskQuery().taskId(taskId).singleResult();
		String defXml = getDefXmlByProcessDefinitionId(taskEntity.getProcessDefinitionId());
		Map<String, Map<String, String>> map = BpmUtil.getTranstoActivitys(defXml, actIds, Boolean.valueOf(false));
		return map;
	}
	
	public Map<String, String> getTaskNodes(String actDefId, String nodeId) {
		Map nodeMaps = getExecuteNodesMap(actDefId, true);

		if (nodeMaps.containsKey(nodeId)) {
			nodeMaps.remove(nodeId);
		}
		return nodeMaps;
	}
	
	protected Map<String, String> getExecuteNodes(ActivityImpl actImpl) {
		Map nodeMap = new HashMap();
		List<ActivityImpl> acts = actImpl.getActivities();
		if (acts.size() == 0)
			return nodeMap;
		for (ActivityImpl act : acts) {
			String nodeType = (String) act.getProperties().get("type");

			if (nodeType.indexOf("Task") != -1) {
				String name = (String) act.getProperties().get("name");
				nodeMap.put(act.getId(), name);
			} else {
				if (!"subProcess".equals(nodeType))
					continue;
				nodeMap.putAll(getExecuteNodes(act));
			}
		}
		return nodeMap;
	}
	
	
	public Map<String, String> getExecuteNodesMap(String actDefId,
			boolean includeSubProcess) {
		Map<String,String> nodeMap = new HashMap<String,String>();

		List<ActivityImpl> acts = getActivityNodes(actDefId);
		for (ActivityImpl actImpl : acts) {
			String nodeType = (String) actImpl.getProperties().get("type");

			if (nodeType.indexOf("Task") != -1) {
				String name = (String) actImpl.getProperties().get("name");
				nodeMap.put(actImpl.getId(), name);
			} else if ((includeSubProcess) && ("subProcess".equals(nodeType))) {
				nodeMap.putAll(getExecuteNodes(actImpl));
			}
		}
		return nodeMap;
	}

	public Page<ProcessTask>  getTasks(Page<ProcessTask> page) {
		return this.taskActService.getAll(page);
	}

	public Page<ProcessTask> getMyTasks(Page<ProcessTask> page) {
		return this.taskActService.getMyTasks(page);
	}
	
	
	/*public List<TaskEntity> getMyMobileTasks(QueryFilter filter) {
		return this.taskActService.getMyMobileTasks(filter);
	}*/

	public boolean isEndProcess(String processInstanceId) {
		HistoricProcessInstance his = (HistoricProcessInstance) this.historyService
				.createHistoricProcessInstanceQuery().processInstanceId(
						processInstanceId).singleResult();

		return (his != null) && (his.getEndTime() != null);
	}
	
	public boolean isSignTask(TaskEntity taskEntity) {
		RepositoryServiceImpl impl = (RepositoryServiceImpl) this.repositoryService;

		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) impl
				.getDeployedProcessDefinition(taskEntity
						.getProcessDefinitionId());

		ActivityImpl taskAct = ent.findActivity(taskEntity
				.getTaskDefinitionKey());

		String multiInstance = (String) taskAct.getProperty("multiInstance");
		if(StringUtil.isEmpty(multiInstance)){
			return false;
		}
		return multiInstance != null;
	}
	
	public HistoricTaskInstanceEntity getHistoricTaskInstanceEntity(
			String taskId) {
		return (HistoricTaskInstanceEntity) this.historyService
				.createHistoricTaskInstanceQuery().taskId(taskId)
				.singleResult();
	}

	public void assignTask(String taskId, String userId) {
		this.taskService.setOwner(taskId, userId);
		this.taskService.setAssignee(taskId, userId);
	}

	public void setDueDate(String taskId, Date dueDate) {
		this.taskActService.setDueDate(taskId, dueDate);
	}

	public ExecutionEntity getExecution(String executionId) {
		Execution execution = (Execution) this.runtimeService
				.createExecutionQuery().executionId(executionId).singleResult();
		return (ExecutionEntity) execution;
	}

	public ExecutionEntity getExecutionByTaskId(String taskId) {
		TaskEntity taskEntity = getTask(taskId);
		if (taskEntity.getExecutionId() == null)
			return null;
		return getExecution(taskEntity.getExecutionId());
	}

	public Map<String, Object> getVarsByTaskId(String taskId) {
		return this.taskService.getVariables(taskId);
	}

	public void setExecutionVariable(String executionId, String variableName,
			Object varVal) {
		this.runtimeService.setVariable(executionId, variableName, varVal);
	}

	
	public ProcessTask newTask(String orgTaskId, String assignee) {
		return newTask(orgTaskId, assignee, null, null);
	}

	public ProcessTask newTask(String orgTaskId, String assignee,
			String newNodeId, String newNodeName) {
		String newExecutionId = this.idGenerator.getNextId();
		String newTaskId = this.idGenerator.getNextId();

		TaskEntity taskEntity = getTask(orgTaskId);
		ExecutionEntity executionEntity = null;
		executionEntity = getExecution(taskEntity.getExecutionId());

		ProcessExecution newExecution = new ProcessExecution(executionEntity);
		newExecution.setId(newExecutionId);

		ProcessTask newTask = new ProcessTask();
		BeanUtils.copyProperties(newTask, taskEntity);
		newTask.setId(newTaskId);
		newTask.setExecutionId(newExecutionId);
		newTask.setCreateTime(new Date());

		newTask.setAssignee(assignee);
		newTask.setOwner(assignee);

		ProcessTaskHistory newTaskHistory = new ProcessTaskHistory(taskEntity);
		newTaskHistory.setAssignee(assignee);
		newTaskHistory.setStartTime(new Date());
		newTaskHistory.setId(newTaskId);
		newTaskHistory.setOwner(assignee);

		if (newNodeId != null) {
			newExecution.setActivityId(newNodeId);
			newTask.setTaskDefinitionKey(newNodeId);
			newTask.setName(newNodeName);
			newTaskHistory.setTaskDefinitionKey(newNodeId);
			newTaskHistory.setName(newNodeName);
		}

		this.executionExtService.insert(newExecution);
		this.taskActService.insertTask(newTask);
		this.historicTaskInstanceService.save(newTaskHistory);

		return newTask;
	}
	
	public List<ProcessTask> getTasks(String processInstanceId) {
		List<ProcessTask> taskList = this.taskActService.getByInstanceId(processInstanceId);
		return taskList;
	}

	/*public void newForkTasks(TaskEntity taskEntity, Set<TaskExecutor> uIds) {
		String token = (String) taskEntity.getVariableLocal(TaskFork.TAKEN_VAR_NAME);
		if (token == null)
			token = TaskFork.TAKEN_PRE;
		Iterator uIt = uIds.iterator();
		int i = 0;
		while (uIt.hasNext())
			if (i++ == 0) {
				assignTask(taskEntity, (TaskExecutor) uIt.next());

				taskEntity.setVariableLocal(TaskFork.TAKEN_VAR_NAME, token
						+ "_" + i);

				changeTaskExecution(taskEntity);
			} else {
				ProcessTask processTask = newTask(taskEntity,(TaskExecutor) uIt.next());

				TaskEntity newTask = getTask(processTask.getId());

				TaskThreadService.addTask(newTask);

				this.taskService.setVariableLocal(processTask.getId(),TaskFork.TAKEN_VAR_NAME, token + "_" + i);

				TaskOpinionEntity taskOpinion = new TaskOpinionEntity(processTask);
				taskOpinion.setOpinionId(Long.valueOf(UniqueIdUtil.genId()));
				taskOpinion.setTaskToken(token);
				this.taskOpinionService.save(taskOpinion);
			}
	}

	public void newNotifyTasks(TaskEntity taskEntity, List<String> uIds) {
		for (String userId : uIds) {
			String taskId = this.idGenerator.getNextId();

			TaskEntity task = (TaskEntity) this.taskService.newTask(taskId);
			task.setAssignee(userId);
			task.setOwner(userId);
			task.setName(taskEntity.getName());

			task.setCreateTime(new Date());
			task.setDescription("通知任务");
			task.setParentTaskId(taskEntity.getId());
			task.setTaskDefinitionKey(taskEntity.getTaskDefinitionKey());
			this.taskService.saveTask(task);
		}
	}

	public void newForkTasks(TaskEntity taskEntity, List<TaskExecutor> uIdList) {
		Set uIdSet = new HashSet();
		uIdSet.addAll(uIdList);
		newForkTasks(taskEntity, uIdSet);
	}

	public void assignTask(TaskEntity taskEntity, TaskExecutor taskExecutor) {
		if ("user".equals(taskExecutor.getType())) {
			taskEntity.setAssignee(taskExecutor.getExecuteId());
			taskEntity.setOwner(taskExecutor.getExecuteId());
		} else {
			taskEntity.addGroupIdentityLink(taskExecutor.getExecuteId(),
					taskExecutor.getType());
		}
	}

	protected void changeTaskExecution(TaskEntity taskEntity) {
		String newExecutionId = this.idGenerator.getNextId();
		ProcessExecution newExecution = new ProcessExecution(taskEntity
				.getExecution());

		newExecution.setId(newExecutionId);

		this.executionExtService.insert(newExecution);

		taskEntity.setExecutionId(newExecutionId);
	}*/

	protected ProcessTask newTask(TaskEntity taskEntity,
			TaskExecutor taskExecutor) {
		String newExecutionId = this.idGenerator.getNextId();
		String newTaskId = this.idGenerator.getNextId();

		ProcessExecution newExecution = new ProcessExecution(taskEntity.getExecution());
		newExecution.setId(newExecutionId);

		ProcessTask newTask = new ProcessTask();
		BeanUtils.copyProperties(newTask, taskEntity);
		newTask.setId(newTaskId);
		newTask.setExecutionId(newExecutionId);
		newTask.setCreateTime(new Date());
		ProcessTaskHistory newTaskHistory = new ProcessTaskHistory(taskEntity);

		TaskUser taskUser = null;

		String executorId = taskExecutor.getExecuteId();

		if ("user".equals(taskExecutor.getType())) {
			newTask.setAssignee(executorId);
			newTask.setOwner(executorId);
			newTaskHistory.setAssignee(executorId);
			newTaskHistory.setOwner(executorId);
		} else {
			taskUser = new TaskUser();
			taskUser.setId(this.idGenerator.getNextId());
			taskUser.setGroupId(executorId);
			taskUser.setType(taskExecutor.getType());
			taskUser.setReversion(Integer.valueOf(1));
			taskUser.setTaskId(newTaskId);
		}

		newTaskHistory.setStartTime(new Date());
		newTaskHistory.setId(newTaskId);

		this.executionExtService.insert(newExecution);//1.保存execution
		this.taskActService.insertTask(newTask);//2.保存任务
		this.historicTaskInstanceService.save(newTaskHistory);//3.保存任务历史
		if (taskUser != null) {
			this.taskUserService.saveTaskUser(taskUser);//4.保存任务执行人
		}
		return newTask;
	}

	
	/*
	public List<TaskUser> getCandidateUsers(String taskId) {
		return this.taskUserDao.getByTaskId(taskId);
	}
	
	@SuppressWarnings("unchecked")
	public void saveCondition(String defId, String forkNode,Map<String, String> map, String canChoicePathNodeId)throws IOException {
		DefinitionEntity bpmDefinition = this.definitionService.get(defId);
		//选择了
		if (StringUtil.isNotEmpty(canChoicePathNodeId)) {bpmDefinition.getCanChoicePathNodeMap().put(forkNode,canChoicePathNodeId);
			bpmDefinition.updateCanChoicePath();
		//没有选择
		} else {
			Object o = bpmDefinition.getCanChoicePathNodeMap().get(forkNode);
			if (o != null)
				bpmDefinition.getCanChoicePathNodeMap().remove(forkNode);
			bpmDefinition.updateCanChoicePath();
		}
		String deployId = bpmDefinition.getActDeployId().toString();
		String actDefId = bpmDefinition.getActId();
		String defXml = this.flowDao.getDefXmlByDeployId(deployId);
		String graphXml = bpmDefinition.getDefXml();
		defXml = BpmUtil.setCondition(forkNode, map, defXml);
		graphXml = BpmUtil.setGraphXml(forkNode, map, graphXml);
		bpmDefinition.setDefXml(graphXml);
		//更新acitiviti定义的xml
		this.flowDao.wirteDefXml(deployId, defXml);
		//更新扩展流程定义
		this.definitionService.update(bpmDefinition);
		//清楚缓存
		DeploymentCache.clearProcessDefinitionEntity();

		((RepositoryServiceImpl) this.repositoryService).getDeployedProcessDefinition(actDefId);
	}
	
	public void delLoopAssigneeVars(String executionId) {
		this.executionExtService.delLoopAssigneeVars(executionId);
	}

	/*public List<TaskEntity> getTaskByUserId(Long agentuserid, QueryFilter filter) {
		return this.taskActService.getMyTasks(agentuserid, filter);
	}

	public String getMyEvents(Map map) {
		List list = this.taskActService.getMyEvents(map);

		String mode = (String) map.get("mode");
		StringBuffer sb = new StringBuffer();
		sb.append("[");

		for (int idx = 0; idx < list.size(); idx++) {
			Object obj = list.get(idx);
			ProcessTask task = (ProcessTask) obj;

			sb.append("{\"id\":\"").append(task.getId()).append("\",");

			Date startTime = task.getCreateTime();
			if (startTime == null) {
				Calendar curCal = Calendar.getInstance();
				startTime = curCal.getTime();
			}

			Date endTime = task.getDueDate();
			if ((endTime == null) && ("month".equals(mode))) {
				endTime = startTime;
			}

			String sTime = DataUtils.formatEnDate(startTime);
			String eTime = endTime == null ? "" : DataUtils.formatEnDate(endTime);

			String eTime0 = "";
			if (("month".equals(mode))
					&& (sTime.substring(0, 10).equals(eTime.substring(0, 10)))) {
				String[] dateArr = sTime.substring(0, 10).split("/");
				eTime0 = DataUtils.addOneDay(new StringBuilder(String
						.valueOf(dateArr[2])).append("-").append(dateArr[0])
						.append("-").append(dateArr[1]).toString())
						+ " 00:00:00 AM";
			}

			if (!"month".equals(mode)) {
				String[] dateArr = sTime.substring(0, 10).split("/");
				eTime0 = DataUtils.addOneHour(dateArr[2] + "-" + dateArr[0]
						+ "-" + dateArr[1]
						+ sTime.substring(10, sTime.length()));
			}

			sb.append("\"type\":\"").append("2").append("\",");
			sb.append("\"startTime\":\"");

			if ("month".equals(mode))
				sb.append(sTime.substring(0, 10) + " 00:00:00 AM")
						.append("\",");
			else {
				sb.append(sTime).append("\",");
			}

			if (!eTime0.equals(""))
				sb.append("\"endTime\":\"").append(eTime0).append("\",");
			else {
				sb.append("\"endTime\":\"").append(eTime).append("\",");
			}

			sb.append("\"title\":\"").append(task.getSubject()).append("\",");
			sb.append("\"description\":\"").append(task.getProcessName())
					.append("\",");
			sb.append("\"status\":\"").append("0").append("\"");
			sb.append("},");
		}

		if (list.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");

		return sb.toString();
	}*/

	public boolean getCanChoicePath(String actDefId, String taskId) {
		if (StringUtil.isEmpty(taskId)) {
			return false;
		}
		TaskEntity taskEntity = getTask(taskId);
		String taskKey = taskEntity.getTaskDefinitionKey();
		if (StringUtil.isEmpty(actDefId))
			actDefId = taskEntity.getProcessDefinitionId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		String canChoicePath = bpmDefinition.getCanChoicePath();
		if (StringUtil.isNotEmpty(canChoicePath)) {
			return canChoicePath.contains(taskKey);
		}
		return false;
	}

	public List<NodeTranUser> getNodeTaskUserMap(String taskId, String preUserId,
			boolean canChoicePath) {
		TaskEntity taskEntity = getTask(taskId);

		String instanceId = taskEntity.getProcessInstanceId();

		String actDefId = taskEntity.getProcessDefinitionId();

		FlowNode flowNode = (FlowNode) NodeCache.getByActDefId(actDefId).get(
				taskEntity.getTaskDefinitionKey());

		ProcessInstanceEntity ProcessInstanceEntity = this.processInstanceService.getByActInstanceId(instanceId);

		String curUserId = null;

		if (ProcessInstanceEntity != null) {
			curUserId = ProcessInstanceEntity.getCreateUserId();
		}

		Map<String,Object> vars = this.taskService.getVariables(taskId);

		return getNodeUserMap(actDefId, instanceId,flowNode.getNextFlowNodes(), curUserId, preUserId,canChoicePath, vars);
	}

	public List<NodeTranUser> getStartNodeUserMap(String actDefId,String startUserId, Map<String, Object> vars) throws Exception {
		//DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);

		//boolean jumpOverFirstNode = bpmDefinition.getToFirstNode().shortValue() == 1;

		FlowNode startNode = NodeCache.getStartNode(actDefId);

		List<FlowNode> nextNodes = startNode.getNextFlowNodes();

		if (nextNodes.size() != 1)
			throw new Exception("开始节点后没有连接的节点!");

		/*if (jumpOverFirstNode) {
			FlowNode flowNode = (FlowNode) nextNodes.get(0);
			if ((!"userTask".equals(flowNode.getNodeType()))
					&& (nextNodes.size() != 1))
				throw new Exception("第一个节点必须为任务节点!");

			nextNodes = flowNode.getNextFlowNodes();
		}*/

		return getNodeUserMap(actDefId, null, nextNodes, startUserId,startUserId, false, vars);
	}

	public List<NodeTranUser> getNodeUserMap(String processDefinitionId,
			String instanceId, List<FlowNode> nextFlowNodes, String curUserId,
			String preUserId, boolean canChoicePath, Map<String, Object> vars) {
		List nodeList = new ArrayList();
		for (FlowNode flowNode : nextFlowNodes) {
			Set nodeUserMapSet = new LinkedHashSet();
			NodeTranUser nodeTranUser = new NodeTranUser(flowNode.getNodeId(),
					flowNode.getNodeName(), nodeUserMapSet);

			if (flowNode.getNodeType().equals("userTask")) {
				Set taskExecutors = getNodeHandlerUsers(processDefinitionId,
						instanceId, flowNode.getNodeId(), curUserId, preUserId,
						vars);
				nodeUserMapSet.add(new NodeUserMap(flowNode.getNodeId(),
						flowNode.getNodeName(), taskExecutors, flowNode
								.getIsMultiInstance().booleanValue()));
			} else if (flowNode.getNodeType().indexOf("Gateway") != -1) {
				if ((canChoicePath)
						&& ("inclusiveGateway".equals(flowNode.getNodeType()))) {
					Map nextPathMap = new HashMap();
					List<FlowNode> gatewayPathList = flowNode
							.getNextFlowNodes();
					for (FlowNode gatewayFlowNode : gatewayPathList) {
						nextPathMap.put(gatewayFlowNode.getNodeId(),
								gatewayFlowNode.getNodeName());

						String nodeType = gatewayFlowNode.getNodeType();
						if (nodeType.equals("callActivity")) {
							Map subGatewayChildNodes = gatewayFlowNode
									.getSubProcessNodes();
							String subFlowKey = gatewayFlowNode
									.getAttribute("subFlowKey");
							FlowNode startNode = NodeCache
									.getStartNode(subGatewayChildNodes);

							DefinitionEntity bpmDefinition = this.definitionService.getMainDefByActDefKey(subFlowKey);
							String subProcessDefinitionId = bpmDefinition.getActId();
							genUserMap(subProcessDefinitionId, instanceId,
									startNode.getNextFlowNodes(),
									nodeUserMapSet, curUserId, preUserId, vars);
						}
					}
					nodeTranUser.setNextPathMap(nextPathMap);
				}
				genUserMap(processDefinitionId, instanceId, flowNode
						.getNextFlowNodes(), nodeUserMapSet, curUserId,
						preUserId, vars);
			} else if ((flowNode.getNodeType().equals("subProcess"))
					&& (flowNode.getSubFirstNode() != null)) {
				genUserMap(processDefinitionId, instanceId, flowNode
						.getSubFirstNode().getNextFlowNodes(), nodeUserMapSet,
						curUserId, preUserId, vars);
			} else if (flowNode.getNodeType().equals("callActivity")) {
				Map subChildNodes = flowNode.getSubProcessNodes();
				String subFlowKey = flowNode.getAttribute("subFlowKey");
				FlowNode startNode = NodeCache.getStartNode(subChildNodes);

				DefinitionEntity bpmDefinition = this.definitionService.getMainDefByActDefKey(subFlowKey);
				String subProcessDefinitionId = bpmDefinition.getActId();
				genUserMap(subProcessDefinitionId, instanceId, startNode
						.getNextFlowNodes(), nodeUserMapSet, curUserId,
						preUserId, vars);
			}
			nodeList.add(nodeTranUser);
		}
		return nodeList;
	}

	private void genUserMap(String processDefinitionId, String instanceId,
			List<FlowNode> nextFlowNodes, Set<NodeUserMap> nodeUserMapSet,
			String curUserId, String preUserId, Map<String, Object> vars) {
		for (FlowNode flowNode : nextFlowNodes)
			if (flowNode.getNodeType().indexOf("Gateway") != -1) {
				genUserMap(processDefinitionId, instanceId, flowNode
						.getNextFlowNodes(), nodeUserMapSet, curUserId,
						preUserId, vars);
			} else if ("userTask".equals(flowNode.getNodeType())) {
				Set taskExecutors = getNodeHandlerUsers(processDefinitionId,
						instanceId, flowNode.getNodeId(), curUserId, preUserId,
						vars);
				NodeUserMap nodeUserMap = new NodeUserMap(flowNode.getNodeId(),
						flowNode.getNodeName(), taskExecutors, flowNode
								.getIsMultiInstance().booleanValue());
				nodeUserMapSet.add(nodeUserMap);
			}
	}

	public Set<TaskExecutor> getNodeHandlerUsers(String actInstanceId,
			String nodeId, Map<String, Object> vars) {
		Set uSet = new HashSet();
		ProcessInstanceEntity processInstance= this.processInstanceService.getByActInstanceId(actInstanceId);
		String actDefId = processInstance.getActDefId();
		String startUserId = processInstance.getCreateUserId();
		List taskExecutorList = this.nodeUserService.getExecutors(actDefId, actInstanceId, nodeId, startUserId, vars,NodeUserEntity.FUNC_NODE_USER);

		if (BeanUtils.isEmpty(taskExecutorList)) {
			return uSet;
		}
		uSet.addAll(taskExecutorList);

		return uSet;
	}

	public Set<TaskExecutor> getNodeHandlerUsers(String actDefId,
			String instanceId, String nodeId, String startUserId, String preUserId,
			Map<String, Object> vars) {
		Set uSet = new HashSet();
		List<TaskExecutor> taskExecutorList = this.nodeUserService.getExecutors(actDefId, instanceId, nodeId, startUserId, vars,NodeUserEntity.FUNC_NODE_USER);

		if (taskExecutorList == null) {
			return uSet;
		}
		for (TaskExecutor taskExecutor : taskExecutorList) {
			uSet.add(taskExecutor);
		}
		return uSet;
	}

	public void deleteTask(String taskId) {
		TaskEntity taskEntity = getTask(taskId);
		this.taskService.deleteTask(taskId);
		this.executionExtService.delExecutionById(taskEntity.getExecutionId());
	}

	public void deleteTasks(String[] taskIds) {
		for (String taskId : taskIds)
			deleteTask(taskId);
	}

	public void updateTaskAssignee(String taskId, String userId) {
		this.taskActService.updateTaskAssignee(taskId, userId);
	}

	public void updateTaskAssigneeNull(String taskId) {
		this.taskActService.updateTaskAssigneeNull(taskId);
	}

	public void updateTaskOwner(String taskId, String userId) {
		this.taskActService.updateTaskOwner(taskId, userId);
	}

	public ProcessInstance getProcessInstance(String actInstId) {
		ProcessInstance processInstance = (ProcessInstance) this.runtimeService
				.createProcessInstanceQuery().processInstanceId(actInstId)
				.singleResult();
		return processInstance;
	}

	public ProcessInstanceEntity endProcessByInstanceId(String instanceId, String nodeId,
			String memo,String userId,String taskId) throws BusinessException {

		try {
			ProcessInstanceEntity processInstance = this.processInstanceService.getByActInstanceId(instanceId);
			processInstance.setStatus(ProcessInstanceEntity.STATUS_MANUAL_FINISH.toString());
			String startUserId = processInstance.getCreateUserId();
			UserEntity sysUser=this.sysUserService.getUserById(userId);
			UserEntity startUser = this.sysUserService.getUserById(startUserId);

			DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(processInstance.getActDefId());
			
			Date endDate = new Date();
			Date startDate = processInstance.getCreateTime();
			String duration =DateUtils.getTime(DateUtils.getTime(startDate, endDate));
			processInstance.setEndTime(endDate);
			processInstance.setDuration(duration);
			
			this.processInstanceService.update(processInstance);

			this.taskActService.delCandidateByInstanceId(instanceId);

			this.taskActService.delByInstanceId(instanceId);

			//this.bpmFormRunDao.delByInstanceId(String.valueOf(instanceId));

			updHistoryActInst(instanceId, nodeId, userId);

			HistoricProcessInstanceEntity processInstanceHistory = (HistoricProcessInstanceEntity) this.historyService
					.createHistoricProcessInstanceQuery().processInstanceId(
							instanceId.toString()).singleResult();
			processInstanceHistory.setEndTime(new Date());
			processInstanceHistory.setDurationInMillis(Long.valueOf(System
					.currentTimeMillis()
					- processInstanceHistory.getStartTime().getTime()));
			processInstanceHistory.setEndActivityId(nodeId);
			this.historyProcessInstanceService.update(processInstanceHistory);

			this.executionExtService.delVariableByProcInstId(instanceId);

			this.executionExtService.delExecutionByProcInstId(instanceId);

			this.taskNodeStatusService.saveOrUpdte(null,instanceId, nodeId,TaskOpinionEntity.STATUS_ENDPROCESS.toString());
			//this.bpmProTransToDao.delByActInstId(instanceId);
			updateTaskOpinion(taskId, memo, sysUser);
			String informStart = bpmDefinition.getInformStart();
			List<UserEntity> receiverUserList = new ArrayList<UserEntity>();
			receiverUserList.add(startUser);
			Map<String,String> msgTempMap = this.msgTemplateService.getTempByFun(MsgTemplateEntity.USE_TYPE_TERMINATION);

			if (StringUtil.isNotEmpty(informStart)) {
				this.taskMessageService.sendMessage(sysUser, receiverUserList,
						informStart, msgTempMap, processInstance.getTitle(), memo,
						null, processInstance.getId(),null);
			}
			return processInstance;
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("流程结束失败");
		}

		
	}
	/*
	private void updHistoryActInst(List<HistoricActivityInstance> list) {
		for (HistoricActivityInstance instance : list) {
			HistoricActivityInstanceEntity ent = (HistoricActivityInstanceEntity) instance;
			ent.setEndTime(new Date());
			long duration = System.currentTimeMillis()
					- ent.getStartTime().getTime();
			ent.setDurationInMillis(Long.valueOf(duration));
			this.historyActivitiInstanceService.update(ent);
		}
	}*/
	
	
	
	public List<Map<String,Object>> getHasCandidateExecutor(String taskIds) {
		return this.taskActService.getHasCandidateExecutor(taskIds);
	}

	public void setInnerVariable(DelegateTask task, String varName, Object obj) {
		Map map = (Map) task.getVariable("innerPassVars");
		map.put(varName, obj);
		task.setVariable("innerPassVars", map);
	}

	public Object getOutVariable(DelegateTask task, String varName) {
		Map vars = (Map) task.getVariable("outPassVars");
		return vars.get(varName);
	}

	public Object getOutVariable(DelegateExecution excution, String varName) {
		Map vars = (Map) excution.getVariable("outPassVars");
		return vars.get(varName);
	}

	public void setObject(Object obj) {
		TaskThreadService.setObject(obj);
	}

	public Object getObject() {
		return TaskThreadService.getObject();
	}

	public ProcessCmd getProcessCmd() {
		return TaskThreadService.getProcessCmd();
	}

	public List<TaskAmount> getMyTasksCount(String userId) {
		return null;
		//return (List<TaskAmount>) this.taskActService.getMyTasksCount(userId);
	}

	public boolean getIsAllowBackByTask(TaskEntity task) {
		return getIsAllowBackByTask(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
	}

	public boolean getIsAllowBackByTask(ProcessTask task) {
		return getIsAllowBackByTask(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
	}
	
	/**
	 * 判断节点是否具有驳回权限
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public boolean getIsAllowBackByTask(String actDefId, String nodeId) {
		boolean rtn = NodeCache.isFirstNode(actDefId, nodeId);//开始节点不能驳回
		if (rtn) {
			return false;
		}
		Map<String,FlowNode> map = NodeCache.getByActDefId(actDefId);
		FlowNode flowNode = (FlowNode) map.get(nodeId);
		if(flowNode.getIsMultiInstance().booleanValue()){//当前结点是多实例结点，不允许驳回
			return false;
		}
		List<FlowNode> preFlowNodeList = flowNode.getPreFlowNodes();
		for (FlowNode preNode : preFlowNodeList) {
			if ((preNode.getIsMultiInstance().booleanValue())&& (!"userTask".equals(preNode.getNodeType()))){//上一个节点是多实例节点但不是用户节点不能驳回
				return false;
			}
			if ((!"exclusiveGateway".equals(preNode.getNodeType()))&& (!"userTask".equals(preNode.getNodeType()))){//上一个节点是网关节点不能驳回
				return false;
			}
		}
		return true;
	}
	
	public ProcessTask getFirstNodeTask(String actInstanceId) {
		List<ProcessTask> tasks = this.taskActService.getTaskByInstId(actInstanceId);
		if (BeanUtils.isEmpty(tasks))
			return null;
		return (ProcessTask) tasks.get(0);
	}
	
	private void updateTaskOpinion(String taskId, String memo, UserEntity user) {
		TaskOpinionEntity taskOpion=this.taskOpinionService.getByTaskId(taskId);
		taskOpion.setCheckStatus(TaskOpinionEntity.STATUS_ENDPROCESS.intValue());
		taskOpion.setExeUserId(user.getId());;
		taskOpion.setExeUserName(user.getUserName());
		taskOpion.setOpinion(memo);
		
		Date endDate = new Date();
		Date startDate = taskOpion.getCreateTime();
		String duration =DateUtils.getTime(DateUtils.getTime(startDate, endDate));
		taskOpion.setEndTime(endDate);
		taskOpion.setDurTime(duration);
		try {
			this.taskOpinionService.update(taskOpion);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ProcessInstanceEntity delProcessByInstanceId(String instanceId, String memo,String userId) {
		UserEntity sysUser=this.sysUserService.getUserById(userId);
		List list = getTasks(instanceId);
		String nodeId = "";
		if (BeanUtils.isNotEmpty(list)) {
			ProcessTask task = (ProcessTask) list.get(0);
			nodeId = task.getTaskDefinitionKey();
		}

		ProcessInstanceEntity processInstance = this.processInstanceService.getByActInstanceId(instanceId);
		processInstance.setStatus(ProcessInstanceEntity.STATUS_DELETE.toString());

		Date endDate = new Date();
		Date startDate = processInstance.getCreateTime();
		String duration =DateUtils.getTime(DateUtils.getTime(startDate, endDate));
		processInstance.setEndTime(endDate);
		processInstance.setDuration(duration);
		
		try {
			this.processInstanceService.update(processInstance);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.taskActService.delCandidateByInstanceId(instanceId);

		this.taskActService.delByInstanceId(instanceId);

		//this.bpmFormRunDao.delByInstanceId(String.valueOf(instanceId));

		updHistoryActInst(instanceId, nodeId, userId);


		HistoricProcessInstanceEntity processInstanceHistory = (HistoricProcessInstanceEntity) this.historyService
				.createHistoricProcessInstanceQuery().processInstanceId(
						instanceId.toString()).singleResult();
		processInstanceHistory.setEndTime(new Date());

		processInstanceHistory.setDurationInMillis(Long.valueOf(System
				.currentTimeMillis()
				- processInstanceHistory.getStartTime().getTime()));
		processInstanceHistory.setEndActivityId(nodeId);



		
		this.historyProcessInstanceService.update(processInstanceHistory);

		this.executionExtService.delVariableByProcInstId(instanceId);

		this.executionExtService.delExecutionByProcInstId(instanceId);

		this.taskNodeStatusService.saveOrUpdte(null,instanceId, nodeId,TaskOpinionEntity.STATUS_ENDPROCESS.toString());

		//updateTaskOpinion(instanceId, memo, sysUser);

		return processInstance;
	}

	public void updHistoryActInst(String actInstId, String nodeId, String assignee) {
		List<HistoricActivityInstanceEntity> hisList = this.historyActivitiInstanceService.getByInstanceId(actInstId, nodeId);
		for (HistoricActivityInstanceEntity hisActInst : hisList) {
			Date endTime = new Date();

			hisActInst.setEndTime(endTime);
			hisActInst.setDurationInMillis(Long.valueOf(System
					.currentTimeMillis()
					- hisActInst.getStartTime().getTime()));

			if ((StringUtil.isEmpty(hisActInst.getAssignee()))
					|| (!hisActInst.getAssignee().equals(assignee))) {
				hisActInst.setAssignee(assignee);
			}
			this.historyActivitiInstanceService.update(hisActInst);
		}
	}
	
	public List<TaskNodeInfo> getNodeCheckStatusInfo(String actInstId) {
		List<TaskNodeInfo> TaskNodeInfoList = new ArrayList<TaskNodeInfo>();
		List<TaskOpinionEntity> taskOpinionList = this.taskOpinionService.getByActInstId(actInstId);

		Map<TaskNodeInfo,TaskNodeInfo> map = new HashMap<TaskNodeInfo,TaskNodeInfo>();

		for (TaskOpinionEntity taskOpinion : taskOpinionList) {
			TaskNodeInfo taskNodeInfo = new TaskNodeInfo();
			taskNodeInfo.setActInstId(taskOpinion.getActInstId());
			taskNodeInfo.setTaskKey(taskOpinion.getTaskKey());
			if (map.containsKey(taskNodeInfo)) {
				TaskNodeInfo tmp = (TaskNodeInfo) map.get(taskNodeInfo);
				tmp.getTaskOpinionList().add(taskOpinion);
			} else {
				taskNodeInfo.getTaskOpinionList().add(taskOpinion);
				map.put(taskNodeInfo, taskNodeInfo);
			}
		}
		TaskNodeInfoList.addAll(map.values());
		return TaskNodeInfoList;
	}
	
	public TaskNodeInfo getNodeCheckStatusInfo(String actInstId, String nodeId) {
		TaskNodeInfo taskNodeInfo = new TaskNodeInfo();
		List<TaskOpinionEntity> taskOpinionList = this.taskOpinionService.getByActInstIdTaskKey(actInstId, nodeId);
		if (BeanUtils.isNotEmpty(taskOpinionList)) {
			
			Collections.sort(taskOpinionList, new Comparator<TaskOpinionEntity>() { 
				@Override
				public int compare(TaskOpinionEntity o1, TaskOpinionEntity o2) {
					// TODO Auto-generated method stub
					return o1.getCreateTime().compareTo(o2.getCreateTime());
				} });
			Collections.reverse(taskOpinionList);
			taskNodeInfo.setActInstId(actInstId);
			taskNodeInfo.setTaskKey(nodeId);
			taskNodeInfo.setTaskOpinionList(taskOpinionList);

			TaskOpinionEntity opinion = (TaskOpinionEntity) taskOpinionList.get(taskOpinionList.size() - 1);
			taskNodeInfo.setLastCheckStatus(opinion.getCheckStatus());
		} else {
			Map<String,Object> vars = this.runtimeService.getVariables(actInstId);
			List<TaskExecutor> taskExecutorList = this.nodeUserService.getExecutors(
					this.processInstanceService.getByActInstanceId(actInstId).getActDefId(), 
					actInstId, 
					nodeId, 
					(String) vars.get("startUser"),vars,NodeUserEntity.FUNC_NODE_USER);
			taskNodeInfo.setTaskExecutorList(taskExecutorList);
		}
		return taskNodeInfo;
	}
	
	public List<ProcessTask> getByInstanceIdTaskDefKey(String instanceId, String taskDefKey) {
		return this.taskActService.getByInstanceIdTaskDefKey(instanceId, taskDefKey);
	}
	
	
	public void setTaskVariable(String taskId, String variableName,Object varVal) {
		this.taskService.setVariable(taskId, variableName, varVal);
	}
	

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setProcessEngineConfiguration(
			ProcessEngineConfiguration processEngineConfiguration) {
		this.processEngineConfiguration = processEngineConfiguration;
	}

	public void setFlowDao(FlowDao flowDao) {
		this.flowDao = flowDao;
	}

	public void setDefinitionService(DefinitionService definitionService) {
		this.definitionService = definitionService;
	}

	public void setTaskActService(TaskActService taskActService) {
		this.taskActService = taskActService;
	}

	@Override
	public void saveCondition(String defId, String forkNode,
			Map<String, String> map, String canChoicePathNodeId)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delLoopAssigneeVars(String executionId) {
		// TODO Auto-generated method stub
		
	}

}
