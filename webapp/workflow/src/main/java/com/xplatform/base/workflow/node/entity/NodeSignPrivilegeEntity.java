package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :会签节点特权功能
 *
 * @version 1.0
 * @author binyong
 *
 */
@Entity
@Table(name="t_flow_node_sign_privilege")
public class NodeSignPrivilegeEntity extends OperationEntity {
	public static final String DEFAULT="0";//所有特权

	public static final String ALLOW_DIRECT="1";//允许直接处理

	public static final String ALLOW_ONE_VOTE="2";//允许一票否决

	public static final String ALLOW_RETROACTIVE="3";//允许补签

	
	private String nodeSignId; //会签id
	private String privilegeType;//0=拥有所有特权,1=允许直接处理,2=允许一票制,3=允许补签'
	private String privilegeUserType;//特权用户类型
	private String privilegeUserIds;//特权用户ids
	private String privilegeUserNames;//特权用户名集合
	
	@Column(name = "node_sign_id", nullable = false, length = 32)
	public String getNodeSignId() {
		return nodeSignId;
	}
	public void setNodeSignId(String nodeSignId) {
		this.nodeSignId = nodeSignId;
	}
	@Column(name = "privilege_type", nullable = true, length = 10)
	public String getPrivilegeType() {
		return privilegeType;
	}
	public void setPrivilegeType(String privilegeType) {
		this.privilegeType = privilegeType;
	}
	
	@Column(name = "privilege_user_type", nullable = true, length = 10)
	public String getPrivilegeUserType() {
		return privilegeUserType;
	}
	public void setPrivilegeUserType(String privilegeUserType) {
		this.privilegeUserType = privilegeUserType;
	}
	
	@Column(name = "privilege_user_ids", nullable = true, length = 1000)
	public String getPrivilegeUserIds() {
		return privilegeUserIds;
	}
	public void setPrivilegeUserIds(String privilegeUserIds) {
		this.privilegeUserIds = privilegeUserIds;
	}
	
	@Column(name = "privilege_user_names", nullable = true, length = 1000)
	public String getPrivilegeUserNames() {
		return privilegeUserNames;
	}
	public void setPrivilegeUserNames(String privilegeUserNames) {
		this.privilegeUserNames = privilegeUserNames;
	}
	
}
