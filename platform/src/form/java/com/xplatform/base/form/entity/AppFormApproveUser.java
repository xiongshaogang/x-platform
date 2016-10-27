package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
/**
 * 哪些人可以看到模版
 * @author sony
 */
@Entity
@Table(name = "t_app_form_approve_user", schema = "")
public class AppFormApproveUser extends OperationEntity{ 
	private String formId;//表单模版id
	private String userId;//审批人，
	private String userName;//审批人姓名
	private Integer orderby;//环节排序
	private String taskId;
	private String type; //(user个人，org机构或部门，role角色)
	
	@Column(name = "formId", length = 32)
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	@Column(name = "userId", length = 32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "userName", length = 100)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name = "orderby", length = 10)
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}
	
	@Transient
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Column(name = "type", length = 10)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
