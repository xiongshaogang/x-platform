package com.xplatform.base.workflow.task.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :任务沟通接收人
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:26:58
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:26:58
 *
 */
@Entity
@Table(name="t_flow_task_comu_reveiver")
public class TaskComuReceiverEntity extends OperationEntity {
	private String opinionId;//意见id
	private String taskId;//任务id
	private String receiverId;//接收人id
	private String receiverName;//接收人姓名
	private String status;//状态 (0 未读,1,已阅,2,已反馈）
	private Date feedBackTime;//反馈时间
	
	@Column(name="opinion_id",nullable=true,length=32)
	public String getOpinionId() {
		return opinionId;
	}
	public void setOpinionId(String opinionId) {
		this.opinionId = opinionId;
	}
	
	@Column(name="task_id",nullable=true,length=32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Column(name="receiver_id",nullable=true,length=32)
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	@Column(name="receiver_name",nullable=true,length=50)
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	@Column(name="status",nullable=true,length=10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="feed_back_time",nullable=true)
	public Date getFeedBackTime() {
		return feedBackTime;
	}
	public void setFeedBackTime(Date feedBackTime) {
		this.feedBackTime = feedBackTime;
	}
}
