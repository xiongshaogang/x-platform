package com.xplatform.base.framework.core.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class OperationDeptEntity extends IdEntity {
	/** 创建时间 **/
	private Date createTime;
	/** 创建人Id **/
	private String createUserId;
	/** 创建人名称 **/
	private String createUserName;
	/** 上一次更新时间 **/
	private Date updateTime;
	/** 上一次更新人Id **/
	private String updateUserId;
	/** 上一次更新人名称 **/
	private String updateUserName;
	private String createDeptId;
	private String createDeptName;
	private String updateDeptId;
	private String updateDeptName;

	@Column(name = "createTime", nullable = true)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "createUserId", nullable = true, length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "createUserName", nullable = true, length = 32)
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@Column(name = "updateTime", nullable = true)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "updateUserId", nullable = true, length = 32)
	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "updateUserName", nullable = true, length = 32)
	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	@Column(name = "createDeptId", nullable = true, length = 32)
	public String getCreateDeptId() {
		return createDeptId;
	}

	public void setCreateDeptId(String createDeptId) {
		this.createDeptId = createDeptId;
	}

	@Column(name = "createDeptName", nullable = true, length = 300)
	public String getCreateDeptName() {
		return createDeptName;
	}

	public void setCreateDeptName(String createDeptName) {
		this.createDeptName = createDeptName;
	}

	@Column(name = "updateDeptId", nullable = true, length = 32)
	public String getUpdateDeptId() {
		return updateDeptId;
	}

	public void setUpdateDeptId(String updateDeptId) {
		this.updateDeptId = updateDeptId;
	}

	@Column(name = "updateDeptName", nullable = true, length = 300)
	public String getUpdateDeptName() {
		return updateDeptName;
	}

	public void setUpdateDeptName(String updateDeptName) {
		this.updateDeptName = updateDeptName;
	}

}
