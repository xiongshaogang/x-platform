package com.xplatform.base.workflow.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程任务分发汇总
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:12:57
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:12:57
 *
 */
@Entity
@Table(name="t_flow_task_fork")
public class TaskForkEntity extends OperationEntity {
	
	public static String TAKEN_PRE = "T";
	public static String TAKEN_VAR_NAME = "_token_";
	
	private String actInstId;//流程实例id
	private String forkSn;//分发序号
	private Integer forkCount;//分发个数
	private Integer finishCount;//完成个数
	
	private String forkTaskName;//分发任务名称
	private String forkTaskKey;//分发任务key
	private String joinTaskName;//汇总任务名称
	private String joinTaskKey;//汇总任务key
	
	private String forkTokens;//分发令牌号  格式如：T_1_1,T_1_2,T_1_3,或 T_1,T_2,T_3
	private String forkTokenPre;//上个任务令牌号 格式为T_或格式T_1 或T_1_2等
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="fork_sn",nullable=true,length=32)
	public String getForkSn() {
		return forkSn;
	}
	public void setForkSn(String forkSn) {
		this.forkSn = forkSn;
	}
	
	@Column(name="form_count",nullable=true,length=20)
	public Integer getForkCount() {
		return forkCount;
	}
	public void setForkCount(Integer forkCount) {
		this.forkCount = forkCount;
	}
	
	@Column(name="finish_count",nullable=true,length=20)
	public Integer getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	
	@Column(name="fork_task_name",nullable=true,length=100)
	public String getForkTaskName() {
		return forkTaskName;
	}
	public void setForkTaskName(String forkTaskName) {
		this.forkTaskName = forkTaskName;
	}
	
	@Column(name="fork_task_key",nullable=true,length=32)
	public String getForkTaskKey() {
		return forkTaskKey;
	}
	public void setForkTaskKey(String forkTaskKey) {
		this.forkTaskKey = forkTaskKey;
	}
	
	@Column(name="join_task_name",nullable=true,length=100)
	public String getJoinTaskName() {
		return joinTaskName;
	}
	public void setJoinTaskName(String joinTaskName) {
		this.joinTaskName = joinTaskName;
	}
	
	@Column(name="join_task_key",nullable=true,length=32)
	public String getJoinTaskKey() {
		return joinTaskKey;
	}
	public void setJoinTaskKey(String joinTaskKey) {
		this.joinTaskKey = joinTaskKey;
	}
	
	@Column(name="fork_takens",nullable=true,length=300)
	public String getForkTokens() {
		return forkTokens;
	}
	public void setForkTokens(String forkTokens) {
		this.forkTokens = forkTokens;
	}
	
	@Column(name="fork_taken_pre",nullable=true,length=300)
	public String getForkTokenPre() {
		return forkTokenPre;
	}
	public void setForkTokenPre(String forkTokenPre) {
		this.forkTokenPre = forkTokenPre;
	}
	
}
