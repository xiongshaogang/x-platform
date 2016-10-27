package com.xplatform.base.workflow.task.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :任务会签投票数据
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:15:21
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:15:21
 *
 */
@Entity
@Table(name="t_flow_task_sign_data")
public class TaskSignDataEntity extends OperationEntity {
	
	public static Short AGREE = Short.valueOf((short)1);//同意
	public static Short REFUSE = Short.valueOf((short)2);//拒绝
	public static Short ABORT = Short.valueOf((short)0);//再议
	public static Short BACK = Short.valueOf((short)3);//返回
	public static Short COMPLETED = Short.valueOf((short)1);//完成
	public static Short NOT_COMPLETED = Short.valueOf((short)0);//没有完成
	
	private String actDefId;//acitiviti定义id
	private String actInsId;//acitiviti流程实例id
	private String nodeName;//节点名称
	private String nodeId;//节点id
	private String taskId;//回迁任务id
	private String voteUserId;//投票人id
	private String voteUserName;//头骗人名称
	private Date voteTime;//投票时间
	private String isAgree;//0.弃权 1.同意 2.拒绝
	private String content;//投票意见内容
	private Integer signNums;//投票数
	private String isCompleted;//是否结束
	private Integer batch;//批次
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInsId() {
		return actInsId;
	}
	public void setActInsId(String actInsId) {
		this.actInsId = actInsId;
	}
	
	@Column(name="node_name",nullable=true,length=100)
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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
	
	@Column(name="vote_user_id",nullable=true,length=32)
	public String getVoteUserId() {
		return voteUserId;
	}
	public void setVoteUserId(String voteUserId) {
		this.voteUserId = voteUserId;
	}
	
	@Column(name="vote_user_Name",nullable=true,length=100)
	public String getVoteUserName() {
		return voteUserName;
	}
	public void setVoteUserName(String voteUserName) {
		this.voteUserName = voteUserName;
	}
	
	@Column(name="vote_time",nullable=true)
	public Date getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(Date voteTime) {
		this.voteTime = voteTime;
	}
	
	@Column(name="is_agree",nullable=true,length=10)
	public String getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}
	
	@Column(name="content",nullable=true,length=1000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name="sign_nums",nullable=true,length=20)
	public Integer getSignNums() {
		return signNums;
	}
	public void setSignNums(Integer signNums) {
		this.signNums = signNums;
	}
	
	@Column(name="is_completed",columnDefinition="char",nullable=true)
	public String getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}
	
	@Column(name="batch",nullable=true,length=20)
	public Integer getBatch() {
		return batch;
	}
	public void setBatch(Integer batch) {
		this.batch = batch;
	}
	
}
