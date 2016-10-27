package com.xplatform.base.workflow.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :任务催办执行情况
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:16:16
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:16:16
 *
 */
@Entity
@Table(name="t_flow_task_due_state")
public class TaskDueStateEntity extends OperationEntity {
	//注意这里createUserId代替了之前的userId,关联某用户某任务的催办消息
	private String actInstId;//activiti实例id
	private String actDefId;//acitiviti定义id
	private String taskId;//任务id
	private String taskDueId;//任务催办设置id(表示本次记录归属哪个催办设置产生的)
	private String type;//催办类型（1.消息提醒,2.完成流程办结的处理动作）
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name="task_id",nullable=true,length=32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Column(name="type",nullable=true,length=32)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="task_due_id",nullable=true,length=32)
	public String getTaskDueId() {
		return taskDueId;
	}
	public void setTaskDueId(String taskDueId) {
		this.taskDueId = taskDueId;
	}
	
	
}
