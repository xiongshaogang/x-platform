package com.xplatform.base.workflow.core.bpm.model;

/**
 * 
 * description :流程扩展节点
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月1日 上午11:29:47
 * 
 */
public class BpmNode {
	private String nodeId;//节点id
	private String nodeName;//节点名称
	private String nodeType;//节点类型
	private Boolean isMultiple = Boolean.valueOf(false);//是否多实例
	private String condition = "";//条件

	public BpmNode() {
	}

	public BpmNode(String nodeId, String nodeName, String nodeType,Boolean isMultiple) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.nodeType = nodeType;
		this.isMultiple = isMultiple;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Boolean getIsMultiple() {
		return this.isMultiple;
	}

	public void setIsMultiple(Boolean isMultiple) {
		this.isMultiple = isMultiple;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
