package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :节点脚本
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:01:26
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:01:26
 *
 */
@Entity
@Table(name="t_flow_node_script")
public class NodeScriptEntity extends OperationEntity{
	private String nodeId;//节点id
	private String actDefId;//activiti id
	private String script;//脚本内容
	private String type;//类型（前置，后置，分配，节点脚本）
	private String description;//备注
	
	@Column(name = "script", nullable = true, length = 1000)
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Column(name = "node_id", nullable = true, length = 32)
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "act_id", nullable = true, length = 32)
	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	@Column(name = "type", nullable = true, length = 10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
