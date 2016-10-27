package com.xplatform.base.workflow.core.facade.listener;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.execution.service.ExecutionExtService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstFormService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

public class EndEventListener extends BaseNodeEventListener {
	private static final long serialVersionUID = 1L;
	private TaskMessageService taskMessageService = ApplicationContextUtil.getBean("taskMessageService");
	
	protected void execute(DelegateExecution execution, String actDefId,
			String nodeId) {
		ExecutionEntity ent = (ExecutionEntity) execution;
		if (!ent.isEnded())
			return;
		System.out.println("*******************************************流程结束发给发起人***********************************");
		if (ent.getId().equals(ent.getProcessInstanceId()))
			handEnd(ent);
		try {
			Map<String, Object> vars = execution.getVariables();
			vars.put("msgTemplateCode", MsgTemplateEntity.USE_TYPE_TERMINATION);
			vars.put("sourceType",BusinessConst.SourceType_CODE_flowNotice);
			vars.put("url", "appFormTableController.do?commonFormEdit&viewType=detail&formCode=" + vars.get("formCode").toString() + "&businessKey="
					+ vars.get("businessKey").toString());
			taskMessageService.sendMessage(vars.get("startUser").toString(), vars);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handEnd(ExecutionEntity ent) {
		if (ent.getParentId() == null) {
			//修改流程实例状态
			updProcessRunStatus(ent);
			//删除执行任务栈
			//ExecutionStackService executionStackService = ApplicationContextUtil.getBean("executionStackService");
			//executionStackService.deleteByActDefId(ent.getProcessDefinitionId());
			//删除通知
			delNotifyTask(ent);
			
			ExecutionExtService executionDao = ApplicationContextUtil.getBean("executionExtService");
			//删除流程实例变量
			executionDao.delVariableByProcInstId(ent.getId());
			//删除子执行器
			executionDao.delSubExecutionByProcInstId(ent.getId());
			
		}
	}

	private void delNotifyTask(ExecutionEntity ent) {
		String instanceId = ent.getProcessInstanceId();
		 
		TaskActService taskActService =  ApplicationContextUtil.getBean("taskActService");

		taskActService.delCandidateByInstanceId(instanceId);

		taskActService.delByInstanceId(instanceId);

		ProcessInstFormService processInstFormService =  ApplicationContextUtil.getBean("processInstFormService");
		processInstFormService.delByInstanceId(instanceId);
	}

	private void updProcessRunStatus(ExecutionEntity ent) {
		//流程实例结束，将对象设置为空，往下流转的时候会根据这个变量判断是否更新流程实例状态和抄送
		TaskThreadService.setObject(ProcessInstanceEntity.STATUS_FINISH);

		ProcessInstanceService processInstanceService = ApplicationContextUtil.getBean("processInstanceService");
		ProcessInstanceEntity processRun = processInstanceService.getByActInstanceId(ent.getProcessInstanceId());
		if (BeanUtils.isEmpty(processRun)){
			return;
		}
		processRun.setStatus(ProcessInstanceEntity.STATUS_FINISH.toString());
		try {
			processInstanceService.update(processRun);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getScriptType() {
		return BpmConst.EndScript;
	}
}