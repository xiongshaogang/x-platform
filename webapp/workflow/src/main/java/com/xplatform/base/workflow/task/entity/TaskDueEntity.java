package com.xplatform.base.workflow.task.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :任务节点催办时间设置
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:11:28
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:11:28
 *
 */
@Entity
@Table(name = "t_flow_task_due")
public class TaskDueEntity extends OperationEntity {
	private String name;//名称
	private String actDefId;//acitiviti定义id
	private String nodeId;//节点id
	private Integer reminderStart;//催办开始时间
	private Integer reminderEnd;//催办间隔
	private String innerContent;//站内信模板内容
	private String commonTitle;//邮件与站内信标题
	private String mailContent;//邮件模板内容
	private String smsContent;//短信模版
	private String action;//到期要处理的动作（0：无动作，1：同意，2：反对，3：驳回，4：驳回至发起人，5：交办，6：结束，7：调用指定方法）
	private Integer times;//提醒次数
	private String conditon;//条件表达式
	private String script;//制定执行动作脚本
	private String relativeNodeId;//相对节点
	private String relativeNodeType;//相对节点动作（0：创建/1：完成）
	private Integer completeTime;//相对完成时间
	private String relativeTimeType;//相对时间类型（0：工作日，1：日历日）
	private String assignerId;//任务到期后交办人id
	private String assignerName;//任务到期后交办人姓名
	private String sendType;//发送类型(短信、站内信、邮件)

	@Column(name = "name", nullable = true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "act_id", nullable = true, length = 32)
	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	@Column(name = "node_id", nullable = true, length = 32)
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "reminder_start", nullable = true, length = 50)
	public Integer getReminderStart() {
		return reminderStart;
	}

	public void setReminderStart(Integer reminderStart) {
		this.reminderStart = reminderStart;
	}

	@Column(name = "reminder_end", nullable = true, length = 50)
	public Integer getReminderEnd() {
		return reminderEnd;
	}

	public void setReminderEnd(Integer reminderEnd) {
		this.reminderEnd = reminderEnd;
	}

	@Column(name = "inner_content", nullable = true, length = 1000)
	public String getInnerContent() {
		return innerContent;
	}

	public void setInnerContent(String innerContent) {
		this.innerContent = innerContent;
	}

	@Column(name = "mail_content", nullable = true, length = 1000)
	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	@Column(name = "sms_content", nullable = true, length = 1000)
	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	@Column(name = "action", nullable = true, length = 50)
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "times", nullable = true, length = 50)
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	@Column(name = "con_exp", nullable = true, length = 1000)
	public String getConditon() {
		return conditon;
	}

	public void setConditon(String conditon) {
		this.conditon = conditon;
	}

	@Column(name = "script", nullable = true, length = 1000)
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Column(name = "relative_node_id", nullable = true, length = 32)
	public String getRelativeNodeId() {
		return relativeNodeId;
	}

	public void setRelativeNodeId(String relativeNodeId) {
		this.relativeNodeId = relativeNodeId;
	}

	@Column(name = "relative_node_type", nullable = true, length = 50)
	public String getRelativeNodeType() {
		return relativeNodeType;
	}

	public void setRelativeNodeType(String relativeNodeType) {
		this.relativeNodeType = relativeNodeType;
	}

	@Column(name = "complete_time", nullable = true, length = 50)
	public Integer getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Integer completeTime) {
		this.completeTime = completeTime;
	}

	@Column(name = "relative_time_type", nullable = true, length = 50)
	public String getRelativeTimeType() {
		return relativeTimeType;
	}

	public void setRelativeTimeType(String relativeTimeType) {
		this.relativeTimeType = relativeTimeType;
	}

	@Column(name = "assigner_id", nullable = true, length = 32)
	public String getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(String assignerId) {
		this.assignerId = assignerId;
	}

	@Column(name = "assigner_name", nullable = true, length = 50)
	public String getAssignerName() {
		return assignerName;
	}

	public void setAssignerName(String assignerName) {
		this.assignerName = assignerName;
	}

	@Column(name = "send_type", nullable = true, length = 50)
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	@Column(name = "common_title", nullable = true, length = 100)
	public String getCommonTitle() {
		return commonTitle;
	}

	public void setCommonTitle(String commonTitle) {
		this.commonTitle = commonTitle;
	}

	

}
