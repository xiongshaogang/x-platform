package com.xplatform.base.workflow.instance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程实例节点栈，用来记录流程节点的过程，可用来做流程回退,自由流，分发汇总等
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月14日 上午10:04:45
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月14日 上午10:04:45
 *
 */
@Entity
@Table(name="t_flow_instance_execution_stack")
public class ExecutionStackEntity extends OperationEntity {
	public static final String MULTI_TASK = "1";//会签任务
	public static final String COMMON_TASK = "0";//普通任务
	
	private String actDefId;//acitivi定义id
	private String nodeId;//节点id
	private String nodeName;//节点名称
	private Date endTime;//结束时间
	private String assignees;//审批人
	private String isMutiTask=COMMON_TASK;//是不是多节点任务
	private String parentId;//父节点id
	private String actInsId;//流程实例id
	private String taskIds;//节点产生的任务id集合
	private String nodePath;//节点路径
	private Integer depth;//节点深度
	private String taskToken;//是针对分发任务时，携带的令牌，方便查找其父任务堆栈
	private String status;//状态（1.活动节点 0.完成节点），驳回或者自由流配置是否结束正在活动的节点
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
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
	
	
	@Column(name="end_time",nullable=true)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="assignees",nullable=true,length=320)
	public String getAssignees() {
		return assignees;
	}
	public void setAssignees(String assignees) {
		this.assignees = assignees;
	}
	
	@Column(name="is_mutitask",columnDefinition="char",nullable=true,length=1)
	public String getIsMutiTask() {
		return isMutiTask;
	}
	public void setIsMutiTask(String isMutiTask) {
		this.isMutiTask = isMutiTask;
	}
	
	@Column(name="parent_id",nullable=true,length=32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInsId() {
		return actInsId;
	}
	public void setActInsId(String actInsId) {
		this.actInsId = actInsId;
	}
	
	@Column(name="task_ids",nullable=true,length=300)
	public String getTaskIds() {
		return taskIds;
	}
	public void setTaskIds(String taskIds) {
		this.taskIds = taskIds;
	}
	
	@Column(name="node_path",nullable=true,length=2000)
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	
	@Column(name="depth",nullable=true,length=8)
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	
	@Column(name="task_token",nullable=true,length=300)
	public String getTaskToken() {
		return taskToken;
	}
	public void setTaskToken(String taskToken) {
		this.taskToken = taskToken;
	}
	
	@Column(name="status",nullable=true,length=10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
