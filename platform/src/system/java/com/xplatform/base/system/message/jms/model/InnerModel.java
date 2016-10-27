package com.xplatform.base.system.message.jms.model;

import java.io.Serializable;

import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;

public class InnerModel implements Serializable {

	private static final long serialVersionUID = -1826070334131791150L;
	private String funcType;// 消息功能来源(global-公告;group-分组消息(选择部门/角色/某个用户范围);private-个人消息(1对1/1对少量n))
	private String sendId;// 发送表Id
	private String receive;// 消息接收综合选择结果集合(","分割)
	private String cc;// 消息抄送接收综合选择结果集合(","分割)
	private String bcc;// 消息暗送接收综合选择结果集合(","分割)
	private String title;// 消息标题
	private String sourceType;// 业务功能来源
	private String sourceBusinessType;// 业务功能来源
	private String sourceId;// 业务功能Id
	private String extra;// 额外参数,json格式(可以配合通知到达后,打开App,打开URL,打开Activity使用,友盟、环信都可使用)(冗余)

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceBusinessType() {
		return sourceBusinessType;
	}

	public void setSourceBusinessType(String sourceBusinessType) {
		this.sourceBusinessType = sourceBusinessType;
	}

	// private String needReply;//是否需要回复

}
