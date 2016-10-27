package com.xplatform.base.workflow.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
@Entity
@Table(name="t_flow_task_log")
public class TaskLogEntity extends OperationEntity {
	private String name;//日志名称
	private String content;//日志内容
	private String defId;//流程定义Id
	private String defName;//流程定义名称
	private String instId;//流程实例扩展Id
	private String nodeId;//节点id
	private String nodeName;//节点名称
	private String operatorType;//审批类型
	
	@Column(name="name",nullable=true,length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="content",nullable=true,length=1000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name="def_id",nullable=true,length=32)
	public String getDefId() {
		return defId;
	}
	public void setDefId(String defId) {
		this.defId = defId;
	}
	
	@Column(name="def_name",nullable=true,length=100)
	public String getDefName() {
		return defName;
	}
	public void setDefName(String defName) {
		this.defName = defName;
	}
	
	@Column(name="inst_id",nullable=true,length=32)
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
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
	
	@Column(name="0perator_type",nullable=true,length=10)
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	
}
