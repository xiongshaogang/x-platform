package com.xplatform.base.workflow.core.facade.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.slf4j.Logger;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

public class TaskAssignListener extends BaseTaskListener {
	private static final long serialVersionUID = 1L;
	private TaskOpinionService taskOpinionService =  ApplicationContextUtil.getBean("taskOpinionService");
	//执行任务执行人分配
	protected void execute(DelegateTask delegateTask, String actDefId,String nodeId) {
		String userId = delegateTask.getAssignee();
		this.logger.debug("任务ID:" + delegateTask.getId());
		//修改审批意见的执行人
		TaskOpinionEntity taskOpinion = this.taskOpinionService.getByTaskId(delegateTask.getId());
		if (taskOpinion != null) {
			this.logger.debug("update taskopinion exe userId" + userId);
			taskOpinion.setExeUserId(userId);
			try {
				this.taskOpinionService.update(taskOpinion);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//设置任务的
		//delegateTask.setOwner(userId);
	}

	protected String getScriptType() {
		return BpmConst.AllotScript;
	}
}