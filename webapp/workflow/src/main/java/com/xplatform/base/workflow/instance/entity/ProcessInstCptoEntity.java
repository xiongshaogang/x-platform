package com.xplatform.base.workflow.instance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程抄送/转发
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月14日 上午10:05:14
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月14日 上午10:05:14
 *
 */
@Entity
@Table(name="t_flow_instance_cpto")
public class ProcessInstCptoEntity extends OperationEntity {
	public static String CPTYPE_COPY = "1";//转发
	public static String CPTYPE_SEND = "2";//抄送
	private String actDefId;//activit定义Id
	private String defId;//流程定义id
	private String defName;//流程定义名称
	private String actInsId;//acitivi流程实例id
	private String insId;//流程实例扩展id
	private String taskId;
	private String nodeId;//节点id
	private String nodeName;//节点名称
	private String receiveId;//接收人id
	private String receiveName;//接收人名称
	private String type;//copt(抄送)，trans(转发)
	private String isRead;//是否已读
	private Date readTime;//读取时间
	private String opionion;//填写意见
	private String Subject;
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
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
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInsId() {
		return actInsId;
	}
	public void setActInsId(String actInsId) {
		this.actInsId = actInsId;
	}
	
	@Column(name="inst_id",nullable=true,length=32)
	public String getInsId() {
		return insId;
	}
	public void setInsId(String insId) {
		this.insId = insId;
	}
	
	@Column(name="node_id",nullable=true,length=32)
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	@Column(name="task_id",nullable=true,length=32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name="node_name",nullable=true,length=100)
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	@Column(name="receive_id",nullable=true,length=32)
	public String getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}
	
	@Column(name="receive_name",nullable=true,length=300)
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	@Column(name="type",nullable=true,length=10)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="is_read",columnDefinition="char",nullable=true,length=1)
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	
	@Column(name="read_time",nullable=true)
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	
	@Column(name="opinion",nullable=true,length=1000)
	public String getOpionion() {
		return opionion;
	}
	public void setOpionion(String opionion) {
		this.opionion = opionion;
	}
	
	@Column(name="subject",nullable=true,length=2000)
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String subject) {
		Subject = subject;
	}
	
}
