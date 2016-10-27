package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 推荐填写的字段 title,content,sourceType,sourceId,sendChannel,receive,extra
 * @author Administrator
 *
 */
@MappedSuperclass
public abstract class MessageSend extends OperationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;// 消息标题
	private String messageSummary; // 消息概述
	private String content;// 消息内容(站内信、推送等正文内容)
	private String mailContent;// 邮件内容
	private String smsContent;// 短信内容

	private String funcType;// 消息功能来源(global-公告;group-分组消息(选择部门/角色/某个用户范围);private-个人消息(1对1/1对少量n))
	private String sourceType;// 业务功能来源大分类
	private String sourceBusinessType;// 业务功能来源细分(比如code,比如和附件表一样的businessType)
	private String sourceId;// 消息功能来源Id
	private String sendChannel;// 发送途径(msg-消息;push-推送;sms-短信;email-邮箱)
	private Date sendTime;// 消息发送时间,为空时及时发送
	private Date deadline;// 过期时间,为null 则表示用不过期
	private String mailConfigId;// email发送， 主要是记录邮箱配置关联Id
	private String receive;// 消息接收综合选择结果集合,以,号隔开(形如:)
	private String mailCC;// 邮件抄送综合选择结果集合,以,号隔开
	private String mailBCC;// 邮件暗送综合选择结果集合,以,号隔开
	private String fromId;// 发送人(暂时环信的模仿2人发消息才用)

	/********* 数据推送相关 *********/
	private String IMMsg;// 环信发送时的msg参数,json格式(保存了消息类型,消息内容等信息)
	private String pushType;// 推送平台(umeng-友盟)
	private String openType; // 消息类型(text-文本消息;url-打开URL;view-打开应用指定页面)
	private String castType; // 广播类型(广播-broadcast;单播-unicast;列播-listcast;组播-groupcast;文件播-filecast)
	private Integer pushAndroidStatus;// 安卓推送标识(1-未推送;2-发送中;3-发送成功;4-发送失败)
	private Integer pushIOSStatus;// IOS推送标识(1-未推送;2-发送中;3-发送成功;4-发送失败)
	private Integer msgStatus; // 消息状态(0-未发布;1-已发布待发送;2-发送中(用于jms发送消息时超过定时器轮询时间导致的重复发送问题);3-发布成功)
	private Integer smsStatus;// 短信状态(1-待发送;2-发送中;3-发送完成;4-发送失败)
	private Integer mailStatus;// 邮件状态(1-待发送;2-发送中;3-发送完成;4-发送失败)
	private Integer IMStatus;// 即时通讯发送状态(1-待发送;2-发送中;3-发送完成;4-发送失败)
	private String IMLog;// 即时通讯发送错误日志
	private String smsLog;// 短信错误日志
	private String mailLog;// 邮件错误日志

	private String url;// url方式打开时的url
	private String activity;// android视图名称(冗余)
	private String extra;// 额外参数,json格式(可以配合通知到达后,打开App,打开URL,打开Activity使用,环信使用)(冗余)
	
	private String androidPushJson;//安卓推送的json
	private String IOSPushJson;//ios推送的json
	private String androidPushLog;// 安卓推送日志
	private String IOSPushLog;// ios推送日志
	private String pushExtra;//推送使用的额外数据
	
	private String iViewName;// ios视图名称(临时字段)

	// private String receiverIds;// 给开发人员调用简单消息发送,可直接指定多个userId(临时字段)
	// private String receiverCCIds;// 给开发人员调用简单消息发送,可直接指定多个抄送userId(临时字段)
	// private String receiverBCCIds;// 给开发人员调用简单消息发送,可直接指定多个暗送userId(临时字段)

	// private String innerStatus; // 0:未保存 1：
	// 已保存，2:发送中（用于jms发送消息时超过定时器轮询时间导致的重复发送问题。）

	@Column(name = "title", length = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "mailConfigId", length = 32)
	public String getMailConfigId() {
		return mailConfigId;
	}

	public void setMailConfigId(String mailConfigId) {
		this.mailConfigId = mailConfigId;
	}

	@Column(name = "mailCC", columnDefinition = "varchar(2000)")
	public String getMailCC() {
		return mailCC;
	}

	public void setMailCC(String mailCC) {
		this.mailCC = mailCC;
	}

	@Column(name = "mailBCC", columnDefinition = "varchar(2000)")
	public String getMailBCC() {
		return mailBCC;
	}

	public void setMailBCC(String mailBCC) {
		this.mailBCC = mailBCC;
	}

	@Column(name = "mailContent", columnDefinition = "text")
	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	@Column(name = "smsContent", columnDefinition = "varchar(1000)")
	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	@Column(name = "sendTime")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "messageSummary", length = 1000)
	public String getMessageSummary() {
		return messageSummary;
	}

	public void setMessageSummary(String messageSummary) {
		this.messageSummary = messageSummary;
	}

	@Column(name = "content", columnDefinition = "text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "sourceType", length = 32)
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	@Column(name = "sourceBusinessType", length = 32)
	public String getSourceBusinessType() {
		return sourceBusinessType;
	}

	public void setSourceBusinessType(String sourceBusinessType) {
		this.sourceBusinessType = sourceBusinessType;
	}

	@Column(name = "castType", length = 32)
	public String getCastType() {
		return castType;
	}

	public void setCastType(String castType) {
		this.castType = castType;
	}

	@Column(name = "pushType", length = 32)
	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	@Column(name = "openType", length = 32)
	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	@Column(name = "sendChannel", length = 32)
	public String getSendChannel() {
		return sendChannel;
	}

	public void setSendChannel(String sendChannel) {
		this.sendChannel = sendChannel;
	}

	@Column(name = "sourceId", length = 32)
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "smsLog", columnDefinition = "text")
	public String getSmsLog() {
		return smsLog;
	}

	public void setSmsLog(String smsLog) {
		this.smsLog = smsLog;
	}

	@Column(name = "mailLog", columnDefinition = "text")
	public String getMailLog() {
		return mailLog;
	}

	public void setMailLog(String mailLog) {
		this.mailLog = mailLog;
	}

	@Transient
	public String getiViewName() {
		return iViewName;
	}

	public void setiViewName(String iViewName) {
		this.iViewName = iViewName;
	}

	@Column(name = "extra", length = 4000)
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Column(name = "url", length = 500)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "activity", length = 100)
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Column(name = "pushAndroidStatus")
	public Integer getPushAndroidStatus() {
		return pushAndroidStatus;
	}

	public void setPushAndroidStatus(Integer pushAndroidStatus) {
		this.pushAndroidStatus = pushAndroidStatus;
	}

	@Column(name = "pushIOSStatus")
	public Integer getPushIOSStatus() {
		return pushIOSStatus;
	}

	public void setPushIOSStatus(Integer pushIOSStatus) {
		this.pushIOSStatus = pushIOSStatus;
	}

	@Column(name = "msgStatus")
	public Integer getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(Integer msgStatus) {
		this.msgStatus = msgStatus;
	}

	@Column(name = "smsStatus")
	public Integer getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(Integer smsStatus) {
		this.smsStatus = smsStatus;
	}

	@Column(name = "mailStatus")
	public Integer getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(Integer mailStatus) {
		this.mailStatus = mailStatus;
	}

	@Column(name = "funcType", length = 32)
	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

	@Column(name = "receive", columnDefinition = "varchar(2000)")
	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	// @Transient
	// public String getReceiverIds() {
	// return receiverIds;
	// }
	//
	// public void setReceiverIds(String receiverIds) {
	// this.receiverIds = receiverIds;
	// }
	//
	// @Transient
	// public String getReceiverCCIds() {
	// return receiverCCIds;
	// }
	//
	// public void setReceiverCCIds(String receiverCCIds) {
	// this.receiverCCIds = receiverCCIds;
	// }
	//
	// @Transient
	// public String getReceiverBCCIds() {
	// return receiverBCCIds;
	// }
	//
	// public void setReceiverBCCIds(String receiverBCCIds) {
	// this.receiverBCCIds = receiverBCCIds;
	// }

	@Column(name = "deadline")
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Column(name = "IMStatus")
	public Integer getIMStatus() {
		return IMStatus;
	}

	public void setIMStatus(Integer iMStatus) {
		IMStatus = iMStatus;
	}

	@Column(name = "IMLog", columnDefinition = "text")
	public String getIMLog() {
		return IMLog;
	}

	public void setIMLog(String iMLog) {
		IMLog = iMLog;
	}

	@Column(name = "IMMsg", columnDefinition = "text")
	public String getIMMsg() {
		return IMMsg;
	}

	public void setIMMsg(String iMMsg) {
		IMMsg = iMMsg;
	}

	@Column(name = "fromId", length = 32)
	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	@Column(name = "androidPushJson", columnDefinition = "text")
	public String getAndroidPushJson() {
		return androidPushJson;
	}

	public void setAndroidPushJson(String androidPushJson) {
		this.androidPushJson = androidPushJson;
	}

	@Column(name = "IOSPushJson", columnDefinition = "text")
	public String getIOSPushJson() {
		return IOSPushJson;
	}

	public void setIOSPushJson(String iOSPushJson) {
		IOSPushJson = iOSPushJson;
	}

	@Column(name = "androidPushLog", columnDefinition = "text")
	public String getAndroidPushLog() {
		return androidPushLog;
	}

	public void setAndroidPushLog(String androidPushLog) {
		this.androidPushLog = androidPushLog;
	}

	@Column(name = "IOSPushLog", columnDefinition = "text")
	public String getIOSPushLog() {
		return IOSPushLog;
	}

	public void setIOSPushLog(String iOSPushLog) {
		IOSPushLog = iOSPushLog;
	}

	@Column(name = "pushExtra", columnDefinition = "text")
	public String getPushExtra() {
		return pushExtra;
	}

	public void setPushExtra(String pushExtra) {
		this.pushExtra = pushExtra;
	}

	
}
