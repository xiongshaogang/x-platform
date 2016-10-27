package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :节点规则
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:00:58
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:00:58
 *
 */
@Entity
@Table(name="t_flow_node_rule")
public class NodeRuleEntity extends OperationEntity{
	private String name;//规则名称
	private String condition;//规则表达式
	private Integer priority;//优先级
	private String nodeId;//节点id
	private String actDefId;//activiti定义id
	private String targetNodeId;//目标节点id
	private String targetNodeName;//目标节点名称
	private String description;//规则描述
	
	@Column(name="name",nullable=true,length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="con_exp",nullable=true,length=1000)
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Column(name="priority",nullable=true,length=5)
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	@Column(name="node_id",nullable=true,length=50)
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
	
	@Column(name="target_node_id",nullable=true,length=32)
	public String getTargetNodeId() {
		return targetNodeId;
	}
	public void setTargetNodeId(String targetNodeId) {
		this.targetNodeId = targetNodeId;
	}
	
	@Column(name="target_node_name",nullable=true,length=100)
	public String getTargetNodeName() {
		return targetNodeName;
	}
	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
	}
	
	@Column(name="description",nullable=true,length=1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
