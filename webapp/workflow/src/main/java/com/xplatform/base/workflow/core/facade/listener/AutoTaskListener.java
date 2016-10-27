package com.xplatform.base.workflow.core.facade.listener;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.task.entity.TaskNodeStatusEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;

/**
 * 
 * description :自动任务监听
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月28日 下午4:21:41
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月28日 下午4:21:41
 *
 */
public class AutoTaskListener extends BaseNodeEventListener {
	private static final long serialVersionUID = 1L;

	protected void execute(DelegateExecution execution, String actDefId,String nodeId) {
		TaskNodeStatusService taskNodeStatusService =ApplicationContextUtil.getBean("taskNodeStatusService");
		String actInstanceId =execution.getProcessInstanceId();
		//保存或修改任务状态
		taskNodeStatusService.saveOrUpdte(actDefId, actInstanceId, nodeId, TaskOpinionEntity.STATUS_EXECUTED.toString());
	}

	protected String getScriptType() {
		return BpmConst.ScriptNodeScript;
	}
}