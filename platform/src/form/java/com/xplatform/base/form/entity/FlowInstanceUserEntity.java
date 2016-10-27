package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 哪些人可以看到模版
 * @author sony
 *
 */
@Entity
@Table(name = "t_flow_instance_user", schema = "")
public class FlowInstanceUserEntity extends OperationEntity{

	private String businessKey ;//表单id
	private String userId;//用户id
	private String userName ;//用户名
	private String type;//类型(org,role,user)
	private Integer status;//状态
	private String taskNodeId;//任务节点id
	private Integer orderby;//环节排序
	
	@Column(name = "businessKey", length = 32)
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	@Column(name = "userId", length = 32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "userName", length = 50)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name = "status", length = 2)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "taskNodeId", length = 32)
	public String getTaskNodeId() {
		return taskNodeId;
	}
	public void setTaskNodeId(String taskNodeId) {
		this.taskNodeId = taskNodeId;
	}
	
	@Column(name = "orderby", length = 10)
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}
	
	@Column(name = "type", length = 32)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
