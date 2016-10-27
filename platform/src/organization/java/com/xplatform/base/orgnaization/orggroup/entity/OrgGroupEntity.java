package com.xplatform.base.orgnaization.orggroup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :群组
 *
 * @version 1.0
 * @author lixt
 * @createtime : 2015年10月13日 上午10:51:44
 * 
 */
@Entity
@Table(name = "t_org_group", schema = "")
public class OrgGroupEntity extends AssignedOperationEntity {

	private String name; // 群组名称
	private String description; // 描述
	private Integer allowinvites; // 是否允许群成员邀请别人加入此群(值为0,1)
	private Integer approval; // 是否需审核加入
	//private String emGroupId; // 环信群组id
	private Integer ispublic; // 是否公开群
	private Integer maxusers; // 群成员上限
	private String owner; // 群主id
	private String type;  //群类型（2为角色类型群组）
	private Integer workGroup; // 工作组类型 (1为手动创建的工作组，0位自动创建的群组)
/*	private String orgId; //机构id
	private String roleId; //角色id
*/
	@Column(name = "name", nullable = true, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", columnDefinition = "varchar(500)")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "allowinvites", nullable = true, length = 6)
	public Integer getAllowinvites() {
		return allowinvites;
	}

	public void setAllowinvites(Integer allowinvites) {
		this.allowinvites = allowinvites;
	}

/*	@Column(name = "emGroupId", nullable = true, length = 32)
	public String getEmGroupId() {
		return emGroupId;
	}

	public void setEmGroupId(String emGroupId) {
		this.emGroupId = emGroupId;
	}*/

	@Column(name = "ispublic", nullable = true, length = 6)
	public Integer getIspublic() {
		return ispublic;
	}

	public void setIspublic(Integer ispublic) {
		this.ispublic = ispublic;
	}

	@Column(name = "maxusers", nullable = true, length = 6)
	public Integer getMaxusers() {
		return maxusers;
	}

	public void setMaxusers(Integer maxusers) {
		this.maxusers = maxusers;
	}

	@Column(name = "owner", nullable = true, length = 32)
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "approval")
	public Integer getApproval() {
		return approval;
	}

	public void setApproval(Integer approval) {
		this.approval = approval;
	}

	@Column(name = "type", nullable = true, length = 32)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "workGroup", nullable = true, length = 6)
	public Integer getWorkGroup() {
		return workGroup;
	}

	public void setWorkGroup(Integer workGroup) {
		this.workGroup = workGroup;
	}

/*	@Column(name = "orgId", nullable = true, length = 32)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name = "roleId", nullable = true, length = 32)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}*/
	

}
