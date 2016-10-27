package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name="t_sys_approve",schema="")
@SuppressWarnings("serial")
public class ApproveEntity extends OperationEntity implements Serializable{

	private String fromId;
	private String psnType;
	private String type;
	private String status;
	private String content;
	
	@Column(name ="from_id",nullable=true,length=32)
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	
	@Column(name ="type",nullable=true,length=20)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name ="content",nullable=true,length=2000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name ="status",nullable=true,length=5)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name ="psn_type",nullable=true,length=20)
	public String getPsnType() {
		return psnType;
	}
	public void setPsnType(String psnType) {
		this.psnType = psnType;
	}
	
	
}
