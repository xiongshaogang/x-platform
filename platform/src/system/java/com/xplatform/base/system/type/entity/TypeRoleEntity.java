package com.xplatform.base.system.type.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;

@Entity
@Table(name="t_sys_type_role")
public class TypeRoleEntity extends OperationEntity {

	private RoleEntity role;
	private TypeEntity type;
	
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable=true)
	@ForeignKey(name="null")
	public RoleEntity getRole() {
		return role;
	}
	public void setRole(RoleEntity role) {
		this.role = role;
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
}
