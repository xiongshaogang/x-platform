package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
/**
 * 
 * description :节点用户条件
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月31日 下午2:26:19
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月31日 下午2:26:19
 *
 */
@Entity
@Table(name="t_flow_node_user_condition")
public class NodeUserConditionEntity extends OperationEntity{
	private String setId;//节点设置id
	private String nodeId;//节点id
	private String actDefId;//activiti定义id
	private String name;//条件名称
	private String conditionExp;//条件表达式
	private Integer groupNo;//批次号
	private String userType;//（节点用户nodeuser，消息接收人messageuser）
	
	@Column(name="set_id",nullable=true,length=32)
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}
	
	@Column(name="node_id",nullable=true,length=32)
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name="name",nullable=true,length=1000)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="condition_exp",nullable=true,length=1000)
	public String getConditionExp() {
		return conditionExp;
	}
	public void setConditionExp(String conditionExp) {
		this.conditionExp = conditionExp;
	}
	
	@Column(name="group_no",nullable=true,length=5)
	public Integer getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}
	
	@Column(name="user_type",nullable=true,length=20)
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	
}
