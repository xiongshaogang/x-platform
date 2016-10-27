package com.xplatform.base.workflow.core.facade.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.apache.commons.lang.StringUtils;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.constant.AppConstant;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.execution.service.ExecutionExtService;
import com.xplatform.base.workflow.history.service.HistoryActivitiInstanceService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstCptoService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.task.entity.TaskForkEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

public class TaskCompleteListener extends BaseTaskListener {
	private static final long serialVersionUID = 1L;
	
	private TaskOpinionService taskOpinionService =ApplicationContextUtil.getBean("taskOpinionService");
	private TaskNodeStatusService taskNodeStatusService = ApplicationContextUtil.getBean("taskNodeStatusService");
	private ProcessInstCptoService processInstCptoService = ApplicationContextUtil.getBean("processInstCptoService");
	private TaskActService taskDao = ApplicationContextUtil.getBean("taskActService");
	private HistoryActivitiInstanceService historyActivitiInstanceService = ApplicationContextUtil.getBean("historyActivitiInstanceService");
	private ProcessInstanceService processInstanceService = ApplicationContextUtil.getBean("processInstanceService");
	private DefinitionService definitionService = ApplicationContextUtil.getBean("definitionService");
	private TaskMessageService taskMessageService = ApplicationContextUtil.getBean("taskMessageService");
	
	protected void execute(DelegateTask delegateTask, String actDefId,String nodeId) {
		String token = (String) delegateTask.getVariableLocal(TaskForkEntity.TAKEN_VAR_NAME);
		if (token != null) {
			TaskThreadService.setToken(token);
		}

		ProcessCmd processCmd = TaskThreadService.getProcessCmd();
		if ((processCmd != null)
				&& ((processCmd.isBack().intValue() > 0) || (StringUtils
						.isNotEmpty(processCmd.getDestTask())))) {
			this.taskDao.updateNewTaskDefKeyByInstIdNodeId(delegateTask
					.getTaskDefinitionKey()
					+ "_1", delegateTask.getTaskDefinitionKey(), delegateTask
					.getProcessInstanceId());
		}
		//更新审批后的节点的关系
		//updateExecutionStack(delegateTask.getProcessInstanceId(), delegateTask.getTaskDefinitionKey(), token);

		//保存审批的意见
		updOpinion(delegateTask);
		//修改节点状态
		updNodeStatus(nodeId, delegateTask);
		//保存历史审批人
		setActHisAssignee(delegateTask);
		//抄送信息
		copyMessage(nodeId,delegateTask);
		System.out.println("*******************************************待办完成发给发起人***********************************");
		//TODO 最后一步审批提交,就不发给发起人了,而是走流程结束事件
		try {
			Map<String, Object> vars = delegateTask.getVariables();
			vars.put("msgTemplateCode", MsgTemplateEntity.USE_TYPE_LAST_NOTICE_USER);
			vars.put("sourceType",BusinessConst.SourceType_CODE_flowNotice);
			vars.put("url", "appFormTableController.do?commonFormEdit&viewType=detail&formCode=" + vars.get("formCode").toString() + "&businessKey="
					+ vars.get("businessKey").toString());
			taskMessageService.sendMessage(vars.get("startUser").toString(), vars);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取审批的结果
	private Short getStatus(ProcessCmd cmd) {
		Short status = TaskOpinionEntity.STATUS_AGREE;

		int isBack = cmd.isBack().intValue();
		boolean isRevover = cmd.isRecover();

		int vote = cmd.getVoteAgree().shortValue();
		switch (isBack) {
		case 0:
			switch (vote) {
			case 0:
				status = TaskOpinionEntity.STATUS_ABANDON;
				break;
			case 1:
				status = TaskOpinionEntity.STATUS_AGREE;
				break;
			case 2:
				status = TaskOpinionEntity.STATUS_REFUSE;
				break;
			case 5:
				status = TaskOpinionEntity.STATUS_PASSED;
				break;
			case 6:
				status = TaskOpinionEntity.STATUS_NOT_PASSED;
				break;
			case 33:
				status = TaskOpinionEntity.STATUS_SUBMIT;
				break;
			case 34:
				status = TaskOpinionEntity.STATUS_RESUBMIT;
				break;
			case 40:
				status = TaskOpinionEntity.STATUS_REPLACE_SUBMIT;
			}
			break;
		case 1:
			if (isRevover) {
				status = TaskOpinionEntity.STATUS_RECOVER;
			} else {
				status = TaskOpinionEntity.STATUS_REJECT;
			}
			break;
		case 2:
			if (isRevover) {
				status = TaskOpinionEntity.STATUS_RECOVER_TOSTART;
			} else {
				status = TaskOpinionEntity.STATUS_REJECT_TOSTART;
			}
		}

		return status;
	}

	@Override
	protected String getScriptType() {
		// TODO Auto-generated method stub
		return BpmConst.RearScript;
	}
	
	/**
	 * 更新审批后的节点的关系
	 * @param instanceId
	 * @param nodeId
	 * @param token
	 */
	/*private void updateExecutionStack(String instanceId, String nodeId,
			String token) {
	//	ExecutionStackEntity executionStack = this.executionStackService.getLastestStack(instanceId, nodeId, token);
		if (executionStack != null) {
			UserEntity curUser = ClientUtil.getSessionUserName();
			String userId = "";
			if (curUser != null) {
				userId = curUser.getId();
			} else {
				userId = AppConstant.SYSTEMUSERID;
			}
			executionStack.setAssignees(userId);//设置节点审批过的人
			executionStack.setEndTime(new Date());//设置节点审批的时间
			this.executionStackService.update(executionStack);
		}
	}*/
	
	//更新节点状态
	private void updNodeStatus(String nodeId, DelegateTask delegateTask) {
		boolean isMuliti = BpmUtil.isMultiTask(delegateTask);
		if (!isMuliti) {
			String actInstanceId = delegateTask.getProcessInstanceId();
			Short approvalStatus = (Short) delegateTask.getVariable("approvalStatus_"+ delegateTask.getTaskDefinitionKey());
			this.taskNodeStatusService.saveOrUpdte(null, actInstanceId, nodeId, approvalStatus.toString());
		}
	}
	
	/**
	 * 更新审批意见
	 * @param delegateTask
	 * @return
	 */
	private void updOpinion(DelegateTask delegateTask) {
		TaskOpinionEntity taskOpinion = this.taskOpinionService.getByTaskId(delegateTask.getId());

		if (taskOpinion == null) return;
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		UserEntity sysUser = null;
		if(StringUtil.isNotEmpty(cmd.getCurrentUserId())){
			SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
			sysUser = userService.getUserById(cmd.getCurrentUserId());
		}
		String userId = AppConstant.SYSTEMUSERID;
		String userName = "系统";
		if (sysUser != null) {
			userId = sysUser.getId();
			userName = sysUser.getName();
		}

		taskOpinion.setExeUserId(userId);
		taskOpinion.setExeUserName(userName);
		String approvalContent = cmd.getVoteContent();

		//获取流程审批意见
		Short status = getStatus(cmd);
		taskOpinion.setCheckStatus(status.intValue());
		taskOpinion.setOpinion(approvalContent);
		taskOpinion.setEndTime(new Date());
		String duration =DateUtils.getTime(DateUtils.getTime(taskOpinion.getCreateTime(), new Date()));
		taskOpinion.setDurTime(duration);
		try {
			this.taskOpinionService.update(taskOpinion);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setActHisAssignee(DelegateTask delegateTask) {
		ExecutionExtService executionExtService = ApplicationContextUtil.getBean("executionExtService");
		DelegateExecution delegateExecution = delegateTask.getExecution();
		String parentId = delegateExecution.getParentId();

		String nodeId = delegateTask.getTaskDefinitionKey();

		List<HistoricActivityInstanceEntity> hisList = null;
		DelegateExecution execution = delegateExecution;
		while (execution != null) {
			hisList = this.historyActivitiInstanceService.getByExecutionId(execution.getId(), nodeId);
			if (BeanUtils.isNotEmpty(hisList)) {
				break;
			}
			parentId = execution.getParentId();
			if (StringUtil.isEmpty(parentId))
				execution = null;
			else {
				execution = (DelegateExecution) executionExtService.get(parentId);
			}
		}

		if (BeanUtils.isEmpty(hisList)) {
			return;
		}

		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		UserEntity user = null;
		if(StringUtil.isNotEmpty(cmd.getCurrentUserId())){
			SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
			user = userService.getUserById(cmd.getCurrentUserId());
		}
		if (user == null) {
			return;
		}
		String assignee = user.getId();
		for (HistoricActivityInstanceEntity hisActInst : hisList) {
			if (TaskOpinionEntity.STATUS_COMMON_TRANSTO.toString().equals(
					delegateTask.getDescription())) {
				hisActInst.setAssignee(delegateTask.getAssignee());
			} else if ((StringUtil.isEmpty(hisActInst.getAssignee()))
					|| (!hisActInst.getAssignee().equals(assignee))) {
				hisActInst.setAssignee(assignee);
			}
			this.historyActivitiInstanceService.update(hisActInst);
		}
	}
	
	public void copyMessage(String nodeId, DelegateTask delegateTask){
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		Map<String,Object> var=delegateTask.getVariables();
		var.put("nodeId", delegateTask.getTaskDefinitionKey());
		var.put("nodeName", delegateTask.getName());
		var.put("taskId", delegateTask.getId());
		var.put("exeType", "complete");
		ProcessInstanceEntity processRun=processInstanceService.getByActInstanceId(delegateTask.getProcessInstanceId());
		DefinitionEntity definition=definitionService.getByActDefId(delegateTask.getProcessDefinitionId());
		try {
			this.processInstCptoService.handlerCopyTask(processRun, var, cmd.getCurrentUserId().toString(), definition,NodeUserEntity.FUNC_LAST_NOTICE_USER);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}