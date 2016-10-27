package com.xplatform.base.workflow.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :任务已读
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:14:31
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:14:31
 *
 */
@Entity
@Table(name="t_flow_task_read")
public class TaskReadEntity extends OperationEntity {
	private String actInstId;//activiti实例id
	private String taskId;//任务id
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="task_id",nullable=true,length=32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
