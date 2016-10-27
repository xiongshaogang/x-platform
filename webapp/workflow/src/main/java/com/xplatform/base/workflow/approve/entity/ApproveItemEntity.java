package com.xplatform.base.workflow.approve.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :审批常用语
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月22日 下午5:59:26
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月22日 下午5:59:26
 *
 */
@Entity
@Table(name="t_flow_approve_item")
public class ApproveItemEntity extends OperationEntity {
	
	public static final String global = "Y";
	public static final String notGlobal = "N";
	  
	private String setId;
	private String nodeId;
	private String isGlobal;
	private String actDefId;
	private String content;
	
	@Column(name = "set_id", nullable = true, length = 32)
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}
	
	@Column(name = "node_id", nullable = true, length = 32)
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	@Column(name = "is_global" ,columnDefinition="char", nullable = true, length = 1)
	public String getIsGlobal() {
		return isGlobal;
	}
	public void setIsGlobal(String isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	@Column(name = "act_id", nullable = true, length = 32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name = "content", nullable = true, length = 1000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
