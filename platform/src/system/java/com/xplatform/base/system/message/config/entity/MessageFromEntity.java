package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;

@Entity
@Table(name="t_sys_message_from",schema="")
public class MessageFromEntity extends AssignedOperationEntity implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 6462072688090643908L;
	private String name;//发自
	private String mailConfigId;//发件来源  主要是记录邮箱配置关联Id
	private String allUsers;//是否发至全体员工
	private String empIds;//发送至员工
	private String empNames;//发送至员工
	private String jobIds;//发送至岗位
	private String jobNames;//发送至岗位
	private String orgIds;//发送至机构
	private String orgNames;//发送至机构
	private String copiedIds;//抄送人
	private String copiedNames;//抄送人
	private String sendType;//发送方式
	private String messageType;//信息类型
	private String title;//主题
	private String content;//内容
	private String attach;//附件
	private String needReply;//是否需要回复 1：是，0：否
	private String pmApprove;//是否需要经理审批 1：是，0：否
	private String pmStatus;//经理审批状态   0；不通过，1：通过
	private String gmStatus;//总经理审批状态 0；不通过，1：通过
	private String smsStatus;//短信状态0：待发送，1；发送中，2；发送完成,3:发送失败
	private String smsErrorLog;
	private String emailStatus;//邮件状态0：待发送，1；发送中，2；发送完成,3:发送失败
	private String emailErrorLog;
	private String innerStatus; // 0:未保存  1： 以保存
	private String status;//整体状态 0:保存,3:发送, 1：已删除,2:转发,
	@Column(name ="name",nullable=true,length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name ="mail_config_id",nullable=true,length=32)
	public String getMailConfigId() {
		return mailConfigId;
	}
	public void setMailConfigId(String mailConfigId) {
		this.mailConfigId = mailConfigId;
	}
	
	
	@Column(name ="all_user",nullable=true,length=5)
	public String getAllUsers() {
		return allUsers;
	}
	
	public void setAllUsers(String allUsers) {
		this.allUsers = allUsers;
	}
	
	@Column(name ="emp_ids",nullable=true,length=2000)
	public String getEmpIds() {
		return empIds;
	}
	public void setEmpIds(String empIds) {
		this.empIds = empIds;
	}
	
	@Column(name ="emp_names",nullable=true,length=2000)
	public String getEmpNames() {
		return empNames;
	}
	public void setEmpNames(String empNames) {
		this.empNames = empNames;
	}
	
	@Column(name ="job_ids",nullable=true,length=2000)
	public String getJobIds() {
		return jobIds;
	}
	public void setJobIds(String jobIds) {
		this.jobIds = jobIds;
	}
	
	@Column(name ="job_names",nullable=true,length=2000)
	public String getJobNames() {
		return jobNames;
	}
	public void setJobNames(String jobNames) {
		this.jobNames = jobNames;
	}
	
	@Column(name ="org_ids",nullable=true,length=2000)
	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	
	@Column(name ="org_names",nullable=true,length=2000)
	public String getOrgNames() {
		return orgNames;
	}
	public void setOrgNames(String orgNames) {
		this.orgNames = orgNames;
	}

	@Column(name ="copied_ids",nullable=true,length=2000)
	public String getCopiedIds() {
		return copiedIds;
	}
	public void setCopiedIds(String copiedIds) {
		this.copiedIds = copiedIds;
	}
	
	@Column(name ="copied_names",nullable=true,length=2000)
	public String getCopiedNames() {
		return copiedNames;
	}
	public void setCopiedNames(String copiedNames) {
		this.copiedNames = copiedNames;
	}
	
	
	@Column(name ="send_type",nullable=true,length=50)
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	@Column(name ="message_type",nullable=true,length=20)
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	@Column(name ="title",nullable=true,length=200)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name ="content",nullable=true,length=2000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name ="attach",nullable=true,length=2000)
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	@Column(name ="need_reply",nullable=true,length=5)
	public String getNeedReply() {
		return needReply;
	}
	public void setNeedReply(String needReply) {
		this.needReply = needReply;
	}
	@Column(name ="smsStatus",nullable=true,length=5)
	public String getSmsStatus() {
		return smsStatus;
	}
	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}
	
	@Column(name ="smsErrorLog",nullable=true,length=2000)
	public String getSmsErrorLog() {
		return smsErrorLog;
	}
	public void setSmsErrorLog(String smsErrorLog) {
		this.smsErrorLog = smsErrorLog;
	}
	
	@Column(name ="emailStatus",nullable=true,length=5)
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	
	@Column(name ="emailErrorLog",nullable=true,length=2000)
	public String getEmailErrorLog() {
		return emailErrorLog;
	}
	public void setEmailErrorLog(String emailErrorLog) {
		this.emailErrorLog = emailErrorLog;
	}
	@Column(name ="innerStatus",nullable=true,length=5)
	public String getInnerStatus() {
		return innerStatus;
	}
	public void setInnerStatus(String innerStatus) {
		this.innerStatus = innerStatus;
	}
	
	@Column(name ="status",nullable=true,length=5)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name ="pm_approve",nullable=true,length=5)
	public String getPmApprove() {
		return pmApprove;
	}
	public void setPmApprove(String pmApprove) {
		this.pmApprove = pmApprove;
	}
	
	@Column(name ="pm_status",nullable=true,length=5)
	public String getPmStatus() {
		return pmStatus;
	}
	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}
	@Column(name ="gm_status",nullable=true,length=5)
	public String getGmStatus() {
		return gmStatus;
	}
	public void setGmStatus(String gmStatus) {
		this.gmStatus = gmStatus;
	}
	
	
}
