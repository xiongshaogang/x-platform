package com.xplatform.base.orgnaization.userrelation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_org_user_relation", schema = "")
public class UserRelationEntity  extends OperationEntity{
	private String apply;  //申请方
	private String receive; //接受方
	private String relationCode; //关系code
	
	@Column(name ="apply",nullable=true,length=32)
	public String getApply() {
		return apply;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	
	@Column(name ="receive",nullable=true,length=32)
	public String getReceive() {
		return receive;
	}
	public void setReceive(String receive) {
		this.receive = receive;
	}
	
	@Column(name ="relationCode",nullable=true,length=32)
	public String getRelationCode() {
		return relationCode;
	}
	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

}
