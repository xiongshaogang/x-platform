package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_sys_mail_config", schema = "")
@SuppressWarnings("serial")
public class MailConfigEntity extends OperationEntity implements Serializable{
	
	private String userName;
	private String mailType;
	private String mailAddress;
	private String passWord;
	private String smtpHost;
	private String smtpPort;
	private String popHost;
	private String popPort;
	private String imapHost;
	private String imapPort;
	private String isdefault;
	private String userId;
	
	
	@Column(name ="USER_NAME",nullable=true,length=50)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name ="MAIL_TYPE",nullable=true,length=10)
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	
	@Column(name ="MAIL_ADDRESS",nullable=true,length=50)
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	@Column(name ="PASSWORD",nullable=true,length=50)
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	@Column(name ="SMTP_HOST",nullable=true,length=50)
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	
	@Column(name ="SMTP_PORT",nullable=true,length=20)
	public String getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	@Column(name ="POP_HOST",nullable=true,length=20)
	public String getPopHost() {
		return popHost;
	}
	public void setPopHost(String popHost) {
		this.popHost = popHost;
	}
	
	@Column(name ="POP_PORT",nullable=true,length=20)
	public String getPopPort() {
		return popPort;
	}
	public void setPopPort(String popPort) {
		this.popPort = popPort;
	}
	
	@Column(name ="IMAP_HOST",nullable=true,length=20)
	public String getImapHost() {
		return imapHost;
	}
	
	public void setImapHost(String imapHost) {
		this.imapHost = imapHost;
	}
	
	@Column(name ="IMAP_PORT",nullable=true,length=20)
	public String getImapPort() {
		return imapPort;
	}
	public void setImapPort(String imapPort) {
		this.imapPort = imapPort;
	}
	
	@Column(name ="ISDEFAULT",nullable=false,length=1,columnDefinition="varchar(5) default 0")
	public String getIsdefault() {
		return isdefault;
	}
	
	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	
	@Column(name ="USER_ID",nullable=true,length=64)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
