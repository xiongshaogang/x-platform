package com.xplatform.base.workflow.task.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
/**
 * 
 * description :任务转办代理
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:12:13
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:12:13
 *
 */
@Entity
@Table(name="t_flow_task_exe")
public class TaskExeEntity extends OperationEntity {
	public static final String STATUS_INIT = "0";
	public static final String STATUS_COMPLETE = "1";
	public static final String STATUS_CANCEL = "2";
	public static final String STATUS_OTHER_COMPLETE = "3";
	public static final String STATUS_BACK = "4";
	
	public static final String TYPE_ASSIGNEE = "1";//代理
	public static final String TYPE_TRANSMIT = "2";//转办
	
	private String InstanceName;//实例名称
	private String actInstId;//acitiviti定义id
	private String instId;//流程实例扩展id
	private String taskName;//任务名称
	private String taskDefKey;//任务key
	private String taskId;//任务id
	private String assigneeId;//任务承接人id
	private String assigneeName;//任务承接人名称
	private String ownerId;//任务所有人id
	private String ownerName;//任务所有人姓名
	private Date exeTime;//执行时间
	private String exeUserId;//执行人
	private String exeUserName;//执行人名称
	private String type;//代理/转办
	private String status;//状态
	private String description;//备注原因
	private String informType;//通知类型
	private String subject;
	private String defName;
	private String businessName;
	
	@Column(name="inst_name",nullable=true,length=100)
	public String getInstanceName() {
		return InstanceName;
	}
	public void setInstanceName(String instanceName) {
		InstanceName = instanceName;
	}
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="inst_id",nullable=true,length=32)
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	@Column(name="task_name",nullable=true,length=100)
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@Column(name="task_def_key",nullable=true,length=50)
	public String getTaskDefKey() {
		return taskDefKey;
	}
	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	
	@Column(name="task_id",nullable=true,length=32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Column(name="assignee_id",nullable=true,length=32)
	public String getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}
	
	@Column(name="assignee_name",nullable=true,length=50)
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	
	@Column(name="owner_id",nullable=true,length=32)
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	@Column(name="owner_name",nullable=true,length=50)
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	@Column(name="exe_time",nullable=true)
	public Date getExeTime() {
		return exeTime;
	}
	public void setExeTime(Date exeTime) {
		this.exeTime = exeTime;
	}
	
	@Column(name="exe_user_id",nullable=true,length=32)
	public String getExeUserId() {
		return exeUserId;
	}
	public void setExeUserId(String exeUserId) {
		this.exeUserId = exeUserId;
	}
	
	@Column(name="exe_user_name",nullable=true,length=50)
	public String getExeUserName() {
		return exeUserName;
	}
	public void setExeUserName(String exeUserName) {
		this.exeUserName = exeUserName;
	}
	
	@Column(name="type",nullable=true,length=10)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="status",nullable=true,length=10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="description",nullable=true,length=2000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="inform_type",nullable=true,length=20)
	public String getInformType() {
		return informType;
	}
	public void setInformType(String informType) {
		this.informType = informType;
	}
	
	@Column(name="subject",nullable=true,length=1000)
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Column(name="def_name",nullable=true,length=300)
	public String getDefName() {
		return defName;
	}
	public void setDefName(String defName) {
		this.defName = defName;
	}
	
	@Column(name="business_name",nullable=true,length=300)
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
}
