package com.xplatform.base.system.message.config.mybatis.vo;

import java.io.Serializable;
import java.util.Date;

import com.xplatform.base.orgnaization.user.entity.UserEntity;

public class InnerMessageVo implements Serializable {
	private String receiveId;// 接收表Id
	private String receiveUser;// 接收人ID
	private Date readTime;// 阅读时间
	private Integer status;// 发送状态 (0-未读,1-已读,2-删除)
	private String sendType;// 发送表类型(group-分组类型,main-其他类型)
	private String sendId;// 发送表ID
	private String title;// send表消息标题,冗余以提高性能
	private String sourceType;// send表业务功能来源,冗余以提高性能
	private String sourceId;// send表业务来源Id,冗余以提高性能
	private String extra;// 额外参数,json格式(可以配合通知到达后,打开App,打开URL,打开Activity使用,友盟、环信都可使用)(冗余)
	private Date sendTime;// 发送时间

	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

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

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

}
