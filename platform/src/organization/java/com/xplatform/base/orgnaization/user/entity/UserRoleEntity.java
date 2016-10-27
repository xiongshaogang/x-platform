package com.xplatform.base.orgnaization.user.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;

@Entity
@Table(name = "t_org_user_role")
public class UserRoleEntity extends OperationEntity {

	private UserEntity user;
	private RoleEntity role;
	private String orgId;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@ForeignKey(name = "null")
	@JoinColumn(name = "userId", nullable = true)
	// @NotFound(action=NotFoundAction.IGNORE)
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@ForeignKey(name = "null")
	@JoinColumn(name = "roleId", nullable = true)
	// @NotFound(action=NotFoundAction.IGNORE)
	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	@Column(name = "orgId", nullable = true, length = 32)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
