package com.xplatform.base.orgnaization.role.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;

@Entity
@Table(name = "t_org_role_resource")
public class RoleResourceEntity extends OperationEntity {

	private RoleEntity role;
	private ModuleEntity module;
	private ResourceEntity resource;

	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name = "null")
	@JoinColumn(name = "roleId")
	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name = "null")
	@JoinColumn(name = "moduleId")
	public ModuleEntity getModule() {
		return module;
	}

	public void setModule(ModuleEntity module) {
		this.module = module;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name = "null")
	@JoinColumn(name = "resourceId")
	public ResourceEntity getResource() {
		return resource;
	}

	public void setResource(ResourceEntity resource) {
		this.resource = resource;
	}

}
