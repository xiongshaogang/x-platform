package com.xplatform.base.system.message.jms.model;

import java.io.Serializable;

import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;

public class PushModel implements Serializable {

	private static final long serialVersionUID = -1826070334131791150L;
	private String funcType;// 消息功能来源(global-公告;group-分组消息(选择部门/角色/某个用户范围);private-个人消息(1对1/1对少量n))
	private String sendId;// 发送表Id
	private String androidPushJson;// 安卓推送的json
	private String IOSPushJson;// ios推送的json

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

	public String getAndroidPushJson() {
		return androidPushJson;
	}

	public void setAndroidPushJson(String androidPushJson) {
		this.androidPushJson = androidPushJson;
	}

	public String getIOSPushJson() {
		return IOSPushJson;
	}

	public void setIOSPushJson(String iOSPushJson) {
		IOSPushJson = iOSPushJson;
	}

}
