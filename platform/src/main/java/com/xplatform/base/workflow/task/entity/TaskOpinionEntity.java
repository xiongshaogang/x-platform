package com.xplatform.base.workflow.task.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程任务审批意见
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:13:44
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:13:44
 *
 */
@Entity
@Table(name="t_flow_task_opinion")
public class TaskOpinionEntity extends OperationEntity {
	//节点审批状态
	public static final Short STATUS_INIT = Short.valueOf((short) -2);// 初始化
	public static final Short STATUS_CHECKING = Short.valueOf((short) -1);// 审核中
	public static final Short STATUS_ABANDON = Short.valueOf((short) 0);// 放弃
	public static final Short STATUS_AGREE = Short.valueOf((short) 1);// 同意
	public static final Short STATUS_REFUSE = Short.valueOf((short) 2);// 不同意
	public static final Short STATUS_REJECT = Short.valueOf((short) 3);// 驳回
	public static final Short STATUS_RECOVER = Short.valueOf((short) 4);// 撤销（实例创建人驳回到底一个节点）
	public static final Short STATUS_PASSED = Short.valueOf((short) 5);// 会签通过
	public static final Short STATUS_NOT_PASSED = Short.valueOf((short) 6);// 会签不通过
	public static final Short STATUS_RE_PASSED = Short.valueOf((short) 7);// 会签再议
	public static final Short STATUS_CHANGEPATH = Short.valueOf((short) 8);//改变路径
	public static final Short STATUS_NOTIFY = Short.valueOf((short) 9);//通知
	public static final Short STATUS_ENDPROCESS = Short.valueOf((short) 14);//结束流程
	public static final Short STATUS_COMMUNICATION = Short.valueOf((short) 15);//沟通
	public static final Short STATUS_COMMUN_FEEDBACK = Short.valueOf((short) 20);//反馈
	public static final Short STATUS_FINISHDIVERT = Short.valueOf((short) 16);
	public static final Short STATUS_DELEGATE = Short.valueOf((short) 21);
	public static final Short STATUS_DELEGATE_CANCEL = Short.valueOf((short) 22);
	public static final Short STATUS_CHANGE_ASIGNEE = Short.valueOf((short) 23);
	public static final Short STATUS_REJECT_TOSTART = Short.valueOf((short) 24);//驳回到底一个节点
	public static final Short STATUS_RECOVER_TOSTART = Short.valueOf((short) 25);
	public static final Short STATUS_REVOKED = Short.valueOf((short) 17);
	public static final Short STATUS_DELETE = Short.valueOf((short) 18);
	public static final Short STATUS_NOTIFY_COPY = Short.valueOf((short) 19);
	public static final Short STATUS_AGENT = Short.valueOf((short) 26);//代理
	public static final Short STATUS_AGENT_CANCEL = Short.valueOf((short) 27);
	public static final Short STATUS_OPINION = Short.valueOf((short) 28);
	public static final Short STATUS_BACK_CANCEL = Short.valueOf((short) 29);
	public static final Short STATUS_REVOKED_CANCEL = Short.valueOf((short) 30);
	public static final Short STATUS_PASS_CANCEL = Short.valueOf((short) 31);
	public static final Short STATUS_REFUSE_CANCEL = Short.valueOf((short) 32);
	public static final Short STATUS_SUBMIT = Short.valueOf((short) 33);
	public static final Short STATUS_RESUBMIT = Short.valueOf((short) 34);
	public static final Short STATUS_INTERVENE = Short.valueOf((short) 35);
	public static final Short STATUS_RESTART_TASK = Short.valueOf((short) 36);
	public static final Short STATUS_EXECUTED = Short.valueOf((short) 37);
	public static final Short STATUS_TRANSTO = Short.valueOf((short) 38);
	public static final Short STATUS_TRANSTO_ING = Short.valueOf((short) 39);
	public static final Short STATUS_REPLACE_SUBMIT = Short.valueOf((short) 40);
	public static final Short STATUS_COMMON_TRANSTO = Short.valueOf((short) 41);

	public static final String TASKKEY_NOTIFY = "NotifyTask";
	public static final String TASKKEY_DIVERT = "DivertTask";
	
	public static final Short READ = Short.valueOf((short) 1);
	public static final Short NOT_READ = Short.valueOf((short) 0);
	//流程定义实例表单信息
	private String actDefId;//流程定义id
	private String formId;//表单id
	private String actInstId;//流程实例Id
	//节点信息
	private String taskName;//任务名称
	private String taskId;//任务id
	private String taskKey;//任务key
	private String taskToken;//任务执行分发汇总taken
	
	private String exeUserId;//执行人id
	private String exeUserName;//执行人姓名
	protected String superExecution;
	//审批信息
	private String voteStatus;
	private Date endTime;//结束审批时间
	private String durTime;//执行审批时间
	private String candidateUser = "";//候选人
	private String opinion;//意见
	private Integer checkStatus = Integer.valueOf(STATUS_CHECKING);//审批状态，默认审核中
	private String checkStatusStr="审核中"; //审批状态中文，默认审核中
	private String portrait80; //用户头像(80X80)
	//回退信息
	private String backType;//回退类型（1.重新开始，2.反馈）
	private String backTaskId;//回退的父任务id
	private String backNodeId;//反馈的父节点id
	private String parentNodeId;//上一个节点的id
	
	public TaskOpinionEntity(){}
	
	public TaskOpinionEntity(DelegateTask task) {
	    this.actDefId = task.getProcessDefinitionId();
	    this.actInstId = task.getProcessInstanceId();
	    this.taskId = task.getId();
	    this.taskKey = task.getTaskDefinitionKey();
	    this.taskName = task.getName();
	    this.checkStatus = STATUS_CHECKING.intValue();
	    ExecutionEntity superExecution = ((ExecutionEntity)task.getExecution()).getProcessInstance().getSuperExecution();
	    if (superExecution != null){
	      this.superExecution = superExecution.getProcessInstanceId();
	    }
	}
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name="form_id",nullable=true,length=32)
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	@Column(name="act_instance_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="task_name",nullable=true,length=100)
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@Column(name="task_id",nullable=true,length=32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Column(name="task_key",nullable=true,length=32)
	public String getTaskKey() {
		return taskKey;
	}
	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}
	
	@Column(name="act_token",nullable=true,length=320)
	public String getTaskToken() {
		return taskToken;
	}
	public void setTaskToken(String taskToken) {
		this.taskToken = taskToken;
	}
	
	
	@Column(name="end_time",nullable=true)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="dur_time",nullable=true,length=32)
	public String getDurTime() {
		return durTime;
	}
	public void setDurTime(String durTime) {
		this.durTime = durTime;
	}
	
	@Column(name="candidate_user",nullable=true,length=640)
	public String getCandidateUser() {
		return candidateUser;
	}
	public void setCandidateUser(String candidateUser) {
		this.candidateUser = candidateUser;
	}
	
	@Column(name="opinion",nullable=true,length=1000)
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	@Column(name="check_status",nullable=true,length=10)
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	@Column(name="exe_user_id",nullable=true,length=32)
	public String getExeUserId() {
		return exeUserId;
	}
	public void setExeUserId(String exeUserId) {
		this.exeUserId = exeUserId;
	}
	
	@Column(name="exe_user_name",nullable=true,length=100)
	public String getExeUserName() {
		return exeUserName;
	}
	public void setExeUserName(String exeUserName) {
		this.exeUserName = exeUserName;
	}

	@Column(name="super_execution",nullable=true,length=32)
	public String getSuperExecution() {
		return superExecution;
	}

	public void setSuperExecution(String superExecution) {
		this.superExecution = superExecution;
	}

	@Column(name="check_status_str",nullable=true,length=50)
	public String getCheckStatusStr() {
		return checkStatusStr;
	}

	public void setCheckStatusStr(String checkStatusStr) {
		this.checkStatusStr = checkStatusStr;
	}

	public String getPortrait80() {
		return portrait80;
	}

	public void setPortrait80(String portrait80) {
		this.portrait80 = portrait80;
	}

	public String getBackType() {
		return backType;
	}

	@Column(name="back_type",nullable=true,length=10)
	public void setBackType(String backType) {
		this.backType = backType;
	}

	@Column(name="back_task_id",nullable=true,length=100)
	public String getBackTaskId() {
		return backTaskId;
	}

	public void setBackTaskId(String backTaskId) {
		this.backTaskId = backTaskId;
	}

	@Column(name="back_node_id",nullable=true,length=100)
	public String getBackNodeId() {
		return backNodeId;
	}

	public void setBackNodeId(String backNodeId) {
		this.backNodeId = backNodeId;
	}

	@Column(name="parent_node_id",nullable=true,length=100)
	public String getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	@Column(name="vote_status",nullable=true,length=30)
	public String getVoteStatus() {
		return voteStatus;
	}

	public void setVoteStatus(String voteStatus) {
		this.voteStatus = voteStatus;
	}
	
	
}
