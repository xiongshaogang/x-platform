package com.xplatform.base.framework.mybatis.entity;

import java.util.Date;


/**
 * 统一定义扩展的entity基类.
 * 
 * 基类统一定义flag的属性过滤策略.
 * 应用于假删除的查询问题.
 * 
 * @author xiehs
 */
public abstract class ExtEntity extends IdEntity {
	
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	
	
}
