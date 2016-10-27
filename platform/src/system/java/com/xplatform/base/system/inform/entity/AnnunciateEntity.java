package com.xplatform.base.system.inform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;

@Entity
@Table(name="t_sys_message_annunciate",schema="")
public class AnnunciateEntity extends AssignedOperationEntity {
	
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
	private String companyId;//公司id
	private String companyName;//公司name
	private String reference;//文号
	private String emergency;//紧急程度 0:一般,1:紧急,2:非常紧急
	private String content;//内容
	private String needReply;//是否需要回复 1：是，0：否
	private String pmApprove;//是否需要经理审批 1：是，0：否
	private String status;//整体状态 0:保存,1:审核中,2：已删除,3:已发送
	
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
	
	@Column(name ="content",nullable=true,length=2000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name ="need_reply",nullable=true,length=5)
	public String getNeedReply() {
		return needReply;
	}
	public void setNeedReply(String needReply) {
		this.needReply = needReply;
	}
	
	@Column(name ="status",nullable=true,length=5)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name ="title",nullable=true,length=50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name ="company_id",nullable=true,length=50)
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@Column(name ="company_name",nullable=true,length=100)
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Column(name ="reference",nullable=true,length=100)
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	@Column(name ="emergency",nullable=true,length=5)
	public String getEmergency() {
		return emergency;
	}
	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}
	
	@Column(name ="pm_approve",nullable=true,length=5)
	public String getPmApprove() {
		return pmApprove;
	}
	public void setPmApprove(String pmApprove) {
		this.pmApprove = pmApprove;
	}
}
