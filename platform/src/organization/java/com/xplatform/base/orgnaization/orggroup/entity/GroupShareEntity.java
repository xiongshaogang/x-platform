package com.xplatform.base.orgnaization.orggroup.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;

@Entity
@Table(name = "t_org_group_share", schema = "")
public class GroupShareEntity extends AssignedOperationEntity {
	
	private String orgId;//机构id
	private String randomCode;//随机码
	private Date expireTime; //时效时间
	
	@Column(name = "orgId", nullable = true, length = 32)
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Column(name = "randomCode", nullable = true, length = 32)
	public String getRandomCode() {
		return randomCode;
	}
	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}
	
	@Column(name = "expireTime", nullable = true)
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
}
