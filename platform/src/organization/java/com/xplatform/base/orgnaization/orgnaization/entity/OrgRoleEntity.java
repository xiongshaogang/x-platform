package com.xplatform.base.orgnaization.orgnaization.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;

/**
 * 
 * description : 部门/岗位    角色表
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月9日 上午11:47:20
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年12月9日 上午11:47:20
 *
 */
@Entity
@Table(name="t_org_role_org")
public class OrgRoleEntity extends OperationEntity {

	private OrgnaizationEntity org;
	private RoleEntity role;
	//private String orgType;   //类型:部门或岗位
	
	
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable=true)
	public OrgnaizationEntity getOrg() {
		return org;
	}
	public void setOrg(OrgnaizationEntity org) {
		this.org = org;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable=true)
	public RoleEntity getRole() {
		return role;
	}
	public void setRole(RoleEntity role) {
		this.role = role;
	}
	
/*	@Column(name="org_type",nullable=true,length=50)
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}*/
	
	
}
