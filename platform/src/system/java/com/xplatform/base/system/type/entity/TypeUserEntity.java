package com.xplatform.base.system.type.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

@Entity
@Table(name="t_sys_type_user")
public class TypeUserEntity extends OperationEntity {

	private UserEntity user;
	private TypeEntity type;
	private String isManage;//是否可管理
	
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable=true)
	@ForeignKey(name="null")
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", nullable=true)
	@ForeignKey(name="null")
	public TypeEntity getType() {
		return type;
	}
	public void setType(TypeEntity type) {
		this.type = type;
	}
	
	@Column(name="isManage",nullable=false,length=64)
	public String getIsManage() {
		return isManage;
	}
	public void setIsManage(String isManage) {
		this.isManage = isManage;
	}
}
