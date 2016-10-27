package com.xplatform.base.workflow.support.msgtemplate.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name="t_flow_msg_template")
public class MsgTemplateEntity extends OperationEntity {
	
	public static String USE_TYPE_URGE = "remind";//催办提醒
	
	public static String USE_TYPE_REVOKED = "recover";//审批提醒
	public static String USE_TYPE_CANCLE_DELEGATE = "cancelDivert";//取消转办
	public static String USE_TYPE_COMMUNICATION = "comunication";//沟通提醒
	public static String USE_TYPE_NOTIFY_STARTUSER = "startUser";//归档提醒
	
	public static String USE_TYPE_REJECT = "back";//退回提醒
	public static String USE_TYPE_FEEDBACK = "coumSubmit";//被沟通提交
	public static String USE_TYPE_CANCLE_AGENT = "cancelDelegate";//取消代理
	public static String USE_TYPE_COPYTO = "copy";//抄送提醒
	public static String USE_TYPE_NOBODY = "nodeNoUser";//流程节点无人员
	public static String USE_TYPE_OVERDUE = "delegateNotice";//逾期提醒
	public static String USE_TYPE_AGENT = "deleteNotice";//代理提醒
	public static String USE_TYPE_FORWARD = "divertNotice";//转发提醒
	public static String USE_TYPE_RESTARTTASK = String.valueOf(24);
	public static String USE_TYPE_NOTIFYOWNER_AGENT = "taskAssign(Delegate)";//通知任务所属人(代理)
	public static String USE_TYPE_TRANSTO = "addSignNotice";//加签提醒
	public static String USE_TYPE_TRANSTO_FEEDBACK = "addSignFeedback";//加签反馈
	public static String USE_TYPE_PRE_NOTICE_USER = "preNoticeUser";// 节点任务开始抄送
	public static String USE_TYPE_NOTIFY = "approve";//审批提醒
	
	public static String USE_TYPE_DELEGATE = "divert";//转办提醒
	public static String USE_TYPE_STARTFLOW = "startFlow";//启动提醒
	public static String USE_TYPE_CIRCULATE = "circulate";//传阅提醒 
	public static String USE_TYPE_TERMINATION = "end";//终止提醒
	public static String USE_TYPE_LAST_NOTICE_USER = "lastNoticeUser";// 审批完成信息抄送
	
	public static String TEMPLATE_TYPE_INNER = "inner";
    public static String TEMPLATE_TYPE_MAIL = "email";
    public static String TEMPLATE_TYPE_SMS = "sms";
    public static String TEMPLATE_TITLE = "title";
    public static String TEMPLATE_TITLE_MAIL = "mailtitle";
    public static String TEMPLATE_TITLE_INNER = "innertitle";
	
	private String name;//模版名称
	private String innerContent;//站内信模版内容
	private String mailContent;//邮件模版内容
	private String smsContent;//短信模版内容
	private String title;//标题
	private String isDefault;//是否默认
	private String useType;//使用类型
	
	@Column(name="name",nullable=true,length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="inner_content",nullable=true,length=1000)
	public String getInnerContent() {
		return innerContent;
	}
	public void setInnerContent(String innerContent) {
		this.innerContent = innerContent;
	}
	
	@Column(name="mail_content",nullable=true,length=1000)
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	
	@Column(name="sms_content",nullable=true,length=1000)
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
	@Column(name="title",nullable=true,length=100)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name="is_default",columnDefinition="char",nullable=true,length=1)
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@Column(name="use_type",nullable=true,length=50)
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	
	
}
