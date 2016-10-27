package com.xplatform.base.orgnaization.orggroup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :群组人员
 *
 * @version 1.0
 * @author lixt
 * @createtime : 2015年10月13日 上午10:51:44
 * 
 */
@Entity
@Table(name = "t_org_group_member", schema = "")
public class OrgGroupMemberEntity extends OperationEntity {

	private String userId; // 用户id
	private String groupId; // 群组id
	private Integer isOwner;  //是否群组(0否，1是)

	@Column(name = "userId", nullable = true, length = 32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "groupId", nullable = true, length = 32)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Column(name = "isOwner", nullable = true, length = 5)
	public Integer getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(Integer isOwner) {
		this.isOwner = isOwner;
	}
}
