package com.xplatform.base.workflow.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * description :流程节点实例状态
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月14日 上午10:23:20
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月14日 上午10:23:20
 *
 */
@Entity
@Table(name="t_flow_instance_status")
public class TaskNodeStatusEntity extends OperationEntity {
	private String defId;//流程定义ID
	private String actDefId;//ACT流程定义ID
	private String actInstId;//流程实例ID
	private String nodeId;//节点ID
	private String nodeName;//节点名称
	private String status;//状态
	
	@Column(name="def_id",nullable=true,length=32)
	public String getDefId() {
		return defId;
	}
	public void setDefId(String defId) {
		this.defId = defId;
	}
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="node_id",nullable=true,length=32)
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	@Column(name="node_name",nullable=true,length=100)
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	@Column(name="status",nullable=true,length=10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
