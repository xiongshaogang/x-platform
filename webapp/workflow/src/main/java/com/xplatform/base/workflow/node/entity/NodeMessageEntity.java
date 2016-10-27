package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :节点消息
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月6日 上午9:22:34
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月6日 上午9:22:34
 *
 */
@Entity
@Table(name="t_flow_node_message")
public class NodeMessageEntity extends OperationEntity{
	private String nodeId;//节点id
	private String actDefId;//activiti id
	private String messageType;//消息类型（email,innerMessage,sms）
	private String subject;//主题
	private String template;//消息内容模版
	private String isSend;//是否发送
	
	@Column(name = "node_id", nullable = true, length = 50)
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
	
	@Column(name = "message_type", nullable = true, length = 50)
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	@Column(name = "subject", nullable = true, length = 300)
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Column(name = "template", nullable = true, length = 2000)
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Column(name = "is_send",columnDefinition="char", nullable = true, length = 1)
	public String getIsSend() {
		return isSend;
	}
	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	
	
	
	
}
