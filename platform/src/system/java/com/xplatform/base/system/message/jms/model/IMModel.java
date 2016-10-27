package com.xplatform.base.system.message.jms.model;

import java.io.Serializable;

import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;

/**
 * 即时通讯类消息的模型
 * 
 * @author xiaqiang
 *
 */
public class IMModel implements Serializable {

	private static final long serialVersionUID = -1826070334131791150L;
	private String funcType;// 消息功能来源(global-公告;group-分组消息(选择部门/角色/某个用户范围);private-个人消息(1对1/1对少量n))
	private String sendId;// 发送表Id
	private String receive;// 需要IM接收消息的用户(","分割)
	private String content;// 文本内容
	private String fromId;// 发送人
	private String IMMsg;// 环信发送时的msg参数,json格式(保存了消息类型,消息内容等信息)
	private String extra;// 额外数据

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

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIMMsg() {
		return IMMsg;
	}

	public void setIMMsg(String iMMsg) {
		IMMsg = iMMsg;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

}
