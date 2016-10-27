package com.xplatform.base.workflow.core.bpm.model;

import java.util.ArrayList;
import java.util.List;

import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;

/**
 * 任务节点信息,一个节点可能有多个任务，每个任务肯能有多个人审核意见和多个审核人
 * description :
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年10月8日 上午11:22:45
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年10月8日 上午11:22:45
 *
 */
public class TaskNodeInfo {
	private String actInstId;
	private String taskKey;
	private Integer lastCheckStatus = TaskOpinionEntity.STATUS_INIT.intValue();

	private List<TaskOpinionEntity> taskOpinionList = new ArrayList<TaskOpinionEntity>();

	private List<TaskExecutor> taskExecutorList = new ArrayList<TaskExecutor>();

	public void setLastCheckStatus(Integer lastCheckStatus) {
		this.lastCheckStatus = lastCheckStatus;
	}

	public String getActInstId() {
		return this.actInstId;
	}

	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}

	public String getTaskKey() {
		return this.taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public List<TaskOpinionEntity> getTaskOpinionList() {
		return this.taskOpinionList;
	}

	public void setTaskOpinionList(List<TaskOpinionEntity> taskOpinionList) {
		this.taskOpinionList = taskOpinionList;
	}

	public Integer getLastCheckStatus() {
		return this.lastCheckStatus;
	}

	public List<TaskExecutor> getTaskExecutorList() {
		return this.taskExecutorList;
	}

	public void setTaskExecutorList(List<TaskExecutor> taskExecutorList) {
		this.taskExecutorList = taskExecutorList;
	}
}
