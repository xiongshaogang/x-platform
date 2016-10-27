
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
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;

/**
 * 
 * description : 员工岗位表    保存员工所在的岗位
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月9日 上午11:48:02
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年12月9日 上午11:48:02
 *
 */
@Entity
@Table(name="t_org_user_org")
public class UserOrgEntity extends OperationEntity {
	
	private UserEntity user; 
	private OrgnaizationEntity org;   //机构
	
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable=true)
	@ForeignKey(name = "null")
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "orgId", nullable=true)
	@ForeignKey(name = "null")
	public OrgnaizationEntity getOrg() {
		return org;
	}
	public void setOrg(OrgnaizationEntity org) {
		this.org = org;
	}
	
}
