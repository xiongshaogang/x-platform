package com.xplatform.base.workflow.core.facade.listener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.service.NodeScriptService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

public abstract class BaseTaskListener implements TaskListener {
	private static final long serialVersionUID = 1L;
	protected Logger logger = LoggerFactory.getLogger(BaseTaskListener.class);

	public void notify(DelegateTask delegateTask) {
		this.logger.debug("enter the baseTaskListener notify method...");
		//任务实体
		TaskEntity taskEnt = (TaskEntity) delegateTask;
		String nodeId = taskEnt.getExecution().getActivityId();
		String actDefId = taskEnt.getProcessDefinitionId();
		//执行
		execute(delegateTask, actDefId, nodeId);
		//获取脚本
		String scriptType = getScriptType();
		//执行脚本
		exeEventScript(delegateTask, scriptType, actDefId, nodeId);
	}

	protected abstract void execute(DelegateTask paramDelegateTask,String paramString1, String paramString2);

	protected abstract String getScriptType();

	private void exeEventScript(DelegateTask delegateTask, String scriptType,
			String actDefId, String nodeId) {
		this.logger.debug("enter the baseTaskListener exeEventScript method...");
		NodeScriptService nodeScriptService = ApplicationContextUtil.getBean("nodeScriptService");

		NodeScriptEntity model = nodeScriptService.getScriptByType(nodeId,actDefId,scriptType);
		if (model == null){
			return;
		}
		String script = model.getScript();
		if (StringUtil.isEmpty(script)){
			return;
		}
		String instId = delegateTask.getProcessInstanceId();

		TaskThreadService.setTempLocal(instId);

		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) ApplicationContextUtil.getBean("scriptEngine");
		Map<String,Object> vars = delegateTask.getVariables();

		vars.put("task", delegateTask);
		scriptEngine.execute(script, vars);

		TaskThreadService.resetTempLocal(instId);
	}

	protected Set<TaskExecutor> getByTaskExecutors(List<TaskExecutor> list) {
		Set exSet = new LinkedHashSet();
		for (TaskExecutor ex : list) {
			List tmp = getByTaskExecutor(ex);

			exSet.addAll(tmp);
		}
		return exSet;
	}

	protected List<TaskExecutor> getByTaskExecutor(TaskExecutor taskExecutor) {
		List list = new ArrayList();
		list.add(taskExecutor);
		/*if (taskExecutor.getExactType() == 0) {//非分组
			list.add(taskExecutor);
		} else {//分组
			List<User> userList = BpmNodeUserUtil.getUserListByExecutor(taskExecutor);
			for (SysUser sysUser : userList) {
				list.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getUsername()));
			}
		}*/
		return list;
	}

	protected boolean isAllowAgent() {
		ProcessCmd processCmd = TaskThreadService.getProcessCmd();
		if ((BpmConst.TASK_BACK_TOSTART.equals(processCmd.isBack()))
				|| (BpmConst.TASK_BACK.equals(processCmd.isBack()))) {
			return false;
		}

		Short toFirstNode = (Short) TaskThreadService.getToFirstNode();
		TaskThreadService.removeToFirstNode();

		return (toFirstNode == null) || (toFirstNode.shortValue() != 1);
	}
	
}