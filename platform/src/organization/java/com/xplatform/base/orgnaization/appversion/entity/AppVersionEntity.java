/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.orgnaization.appversion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;


/**
 * description :
 *
 * @version 1.0
 * @author ccw
 * @createtime : 2015年2月4日 下午8:37:01
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * ccw        2015年2月4日 下午8:37:01 
 *
 */
@Entity
@Table(name="t_sys_appversions")
public class AppVersionEntity extends OperationEntity{
	
	private String versionNumber;
	private String versionName;
	private String versionDescrition;
	private String attachMentId;
	private Integer isForcedUpdate; //0不强制更新，1强制更新
	private Integer type; //0表示安卓，1表示ios
	private String url; //第三方url
	
	@Column(name="versionNumber",length=32,nullable=true)
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	@Column(name="versionName",length=32,nullable=true)
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	@Column(name="versionDescrition",length=32,nullable=true)
	public String getVersionDescrition() {
		return versionDescrition;
	}
	public void setVersionDescrition(String versionDescrition) {
		this.versionDescrition = versionDescrition;
	}
	@Column(name="attachMentId",length=32,nullable=true)
	public String getAttachMentId() {
		return attachMentId;
	}
	public void setAttachMentId(String attachMentId) {
		this.attachMentId = attachMentId;
	}
	
	@Column(name="isForcedUpdate",length=2,nullable=true)
	public Integer getIsForcedUpdate() {
		return isForcedUpdate;
	}
	public void setIsForcedUpdate(Integer isForcedUpdate) {
		this.isForcedUpdate = isForcedUpdate;
	}
	
	@Column(name="type",length=2,nullable=true)
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name="url",length=200,nullable=true)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}

	