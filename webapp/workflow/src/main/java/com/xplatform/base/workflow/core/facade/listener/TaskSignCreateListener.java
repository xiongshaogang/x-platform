package com.xplatform.base.workflow.core.facade.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.agent.service.AgentSettingService;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstCptoService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.entity.TaskSignDataEntity;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.task.service.TaskSignDataService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;
import com.xplatform.base.workflow.threadlocal.TaskUserAssignService;

public class TaskSignCreateListener extends BaseTaskListener {
	
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(TaskSignCreateListener.class);

	private TaskSignDataService taskSignDataService = ApplicationContextUtil.getBean("taskSignDataService");
	private TaskOpinionService taskOpinionService = ApplicationContextUtil.getBean("taskOpinionService");
	private TaskUserAssignService taskUserAssignService = ApplicationContextUtil.getBean("taskUserAssignService");
	private TaskNodeStatusService taskNodeStatusService = ApplicationContextUtil.getBean("taskNodeStatusService");
	private AgentSettingService agentSettingService = ApplicationContextUtil.getBean("agentSettingService");
	private SysUserService sysUserService = ApplicationContextUtil.getBean("sysUserService");
	private ProcessInstanceService processInstanceService = ApplicationContextUtil.getBean("processInstanceService");
	private DefinitionService definitionService = ApplicationContextUtil.getBean("definitionService");
	private ProcessInstCptoService processInstCptoService = ApplicationContextUtil.getBean("processInstCptoService");
	private TaskActService taskActService = ApplicationContextUtil.getBean("taskActService");
	
	protected void execute(DelegateTask delegateTask, String actDefId, String nodeId) {
		delegateTask.setDescription(TaskOpinionEntity.STATUS_CHECKING.toString());
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("id", delegateTask.getId());
		params.put("userId", ClientUtil.getUserEntity().getId());
		params.put("userName", ClientUtil.getUserEntity().getName());
		taskActService.setTaskUser(params);

		//新增审批信息
		TaskOpinionEntity taskOpinion = new TaskOpinionEntity(delegateTask);
		try {
			this.taskOpinionService.save(taskOpinion);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//为会签分配人员
		TaskExecutor taskExecutor = (TaskExecutor) delegateTask.getVariable("assignee");
		assignTaskExecutor(delegateTask, taskExecutor);

		TaskThreadService.addTask((TaskEntity) delegateTask);

		String processInstanceId = delegateTask.getProcessInstanceId();

		this.logger.debug("enter the signuser listener notify method, taskId:"+ delegateTask.getId() + " assignee:"+ delegateTask.getAssignee());

		Integer instanceOfNumbers = (Integer) delegateTask.getVariable("nrOfInstances");
		Integer loopCounter = (Integer) delegateTask.getVariable("loopCounter");

		if (loopCounter == null) loopCounter = Integer.valueOf(0);

		this.logger.debug("instance of numbers:" + instanceOfNumbers+ " loopCounters:" + loopCounter);

		//如果是第一次，那么添加会签的信息，不管是串行还是并行，每个人任务都会进入这个listener,但是只能执行一次，生成多条会签投票数数据
		if (loopCounter.intValue() == 0) {
			addSignData(delegateTask, nodeId, processInstanceId,instanceOfNumbers);
		}

		this.taskNodeStatusService.saveOrUpdte(actDefId,processInstanceId, nodeId,TaskOpinionEntity.STATUS_CHECKING.toString());
		//更新会签的信息,在记录上添加任务id
		updTaskSignData(processInstanceId, nodeId, taskExecutor, delegateTask.getId());
		
		copyMessage(nodeId, delegateTask);
	}

	private void updTaskSignData(String processInstanceId, String nodeId,
			TaskExecutor taskExecutor, String taskId) {
		String executorId = taskExecutor.getExecuteId();
		TaskSignDataEntity taskSignData = this.taskSignDataService.getUserTaskSign(processInstanceId, nodeId, executorId);
		if (taskSignData == null)
			return;
		taskSignData.setTaskId(taskId);
		try {
			this.taskSignDataService.update(taskSignData);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addSignData(DelegateTask delegateTask, String nodeId,
			String processInstanceId, Integer instanceOfNumbers) {
		List<TaskExecutor> signUserList =this.taskUserAssignService.getNodeUserMap().get(nodeId);
		if (signUserList == null)
			return;

		int batch = this.taskSignDataService.getMaxBatch(processInstanceId,nodeId) + 1;

		for (int i = 0; i < instanceOfNumbers.intValue(); i++) {
			int sn = i + 1;

			TaskSignDataEntity signData = new TaskSignDataEntity();
			signData.setActInsId(processInstanceId);
			signData.setNodeName(delegateTask.getName());
			signData.setNodeId(nodeId);
			signData.setSignNums(Integer.valueOf(sn));
			signData.setActDefId(delegateTask.getProcessDefinitionId());
			signData.setTaskId(delegateTask.getId());
			signData.setIsCompleted(TaskSignDataEntity.NOT_COMPLETED.toString());

			TaskExecutor signUser = (TaskExecutor) signUserList.get(i);
			if (signUser != null) {
				signData.setVoteUserId(signUser.getExecuteId());
				signData.setVoteUserName(signUser.getExecutor());
			}
			signData.setBatch(batch);

			try {
				this.taskSignDataService.save(signData);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setAssignUser(DelegateTask delegateTask,
			TaskExecutor taskExecutor, String sysUserId) {
		
		
		UserEntity sysUser = null;
		String selfReceive="";
		if (isAllowAgent()) {
			Map<String,Object> agent= this.agentSettingService.getAgent(delegateTask,sysUserId);
			if(agent!=null){
				sysUser=(UserEntity)agent.get("sysUser");
				selfReceive=(String)agent.get("selfReceive");
			}
		}

		if (sysUser != null && !StringUtil.equals("Y", selfReceive)) {//代理自己不接收任务
			delegateTask.setAssignee(sysUser.getId());
			delegateTask.setDescription(TaskOpinionEntity.STATUS_AGENT.toString());
			delegateTask.setOwner(taskExecutor.getExecuteId());
		} else if(sysUser != null && StringUtil.equals("Y", selfReceive)){//代理自己接收任务
			//delegateTask.setAssignee(sysUser.getId());
			delegateTask.setDescription(TaskOpinionEntity.STATUS_AGENT.toString());
			delegateTask.setOwner(taskExecutor.getExecuteId());
			delegateTask.addCandidateUser(sysUserId);
			delegateTask.addCandidateUser(taskExecutor.getExecuteId());
		}else {//非代理
			delegateTask.setAssignee(taskExecutor.getExecuteId());
		}
		
		
		TaskOpinionEntity taskOpinion = this.taskOpinionService.getByTaskId(delegateTask.getId());
		UserEntity exeUser = this.sysUserService.getUserById(sysUserId);
		taskOpinion.setExeUserId(exeUser.getId());
		taskOpinion.setExeUserName(exeUser.getUserName());
		try {
			this.taskOpinionService.update(taskOpinion);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void assignTaskExecutor(DelegateTask delegateTask,
			TaskExecutor taskExecutor) {
		if ("user".equals(taskExecutor.getType())) {
			delegateTask.setOwner(taskExecutor.getExecuteId());
			String sysUserId = taskExecutor.getExecuteId();
			setAssignUser(delegateTask, taskExecutor, sysUserId);
		} else {
			delegateTask.setAssignee("0");
			delegateTask.setOwner("0");

			/*if (2 == taskExecutor.getExactType()) {
				List<SysUser> userList = BpmNodeUserUtil.getUserListByExecutor(taskExecutor);
				if (BeanUtils.isEmpty(userList)) {
					String msg = "[" + taskExecutor.getExecutor()
							+ "],没有设置人员,请先设置人员。";
				}
				for (UserEntity sysUser : userList) {
					delegateTask.addCandidateUser(sysUser.getUserId().toString());
				}

			} else if (taskExecutor.getExactType() == 0) {*/
				delegateTask.addGroupIdentityLink(taskExecutor.getExecuteId(),taskExecutor.getType());
			/*} else if (3 == taskExecutor.getExactType()) {
				String[] aryExecutor = taskExecutor.getExecuteId().split(",");
				if (aryExecutor.length == 1) {
					delegateTask.setOwner(taskExecutor.getExecuteId());
					Long sysUserId = Long.valueOf(taskExecutor.getExecuteId());
					setAssignUser(delegateTask, taskExecutor, sysUserId);
				} else {
					for (String executorId : aryExecutor){
						delegateTask.addCandidateUser(executorId);
					}
				}
			}*/
		}
	}
	
	public void copyMessage(String nodeId, DelegateTask delegateTask){
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		Map<String,Object> var=delegateTask.getVariables();
		var.put("nodeId", delegateTask.getTaskDefinitionKey());
		var.put("nodeName", delegateTask.getName());
		var.put("taskId", delegateTask.getId());
		var.put("exeType", "create");
		ProcessInstanceEntity processRun=processInstanceService.getByActInstanceId(delegateTask.getProcessInstanceId());
		DefinitionEntity definition=definitionService.getByActDefId(delegateTask.getProcessDefinitionId());
		try {
			this.processInstCptoService.handlerCopyTask(processRun, var, cmd.getCurrentUserId().toString(), definition,NodeUserEntity.FUNC_PRE_NOTICE_USER);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getScriptType() {
		return BpmConst.PreScript;
	}
}