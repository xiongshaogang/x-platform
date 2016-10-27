package com.xplatform.base.system.message.jms.model;

import java.io.Serializable;
import java.util.List;

import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;

public class SmsModel implements Serializable {
	private static final long serialVersionUID = -2258038196642596506L;
	private String funcType;// 消息功能来源(global-公告;group-分组消息(选择部门/角色/某个用户范围);private-个人消息(1对1/1对少量n))
	private String sendId;// 发送表Id
	private String smsContent;// 消息内容(站内信、推送等正文内容)
	private String[] mobile;// 手机号码集合

	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String[] getMobile() {
		return mobile;
	}

	public void setMobile(String[] mobile) {
		this.mobile = mobile;
	}

}
