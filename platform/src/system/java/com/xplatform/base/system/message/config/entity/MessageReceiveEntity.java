package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_sys_message_receive", schema = "")
public class MessageReceiveEntity extends OperationEntity implements Serializable {

	private static final long serialVersionUID = -784033750555857186L;
	private String receiveId;// 接收人ID
	private Date readTime;// 阅读时间
	// private String replyContent;// 回复内容
	private Integer status;// 接收状态 (0-未读,1-已读,2-删除)
	private String funcType;// 发送表类型(group-分组类型,main-其他类型)
	private String sendId;// 发送表ID
	private String title;// send表消息标题,冗余以提高性能
	private String sourceId;// 消息功能来源Id
	private String sourceType;// send表业务功能来源大分类,冗余以提高性能
	private String sourceBusinessType;// send表业务功能来源细分(比如code,比如和附件表一样的businessType),冗余以提高性能
	private String extra;// 额外参数,json格式(可以配合通知到达后,打开App,打开URL,打开Activity使用,友盟、环信都可使用)(冗余)

	@Column(name = "receiveId", nullable = true, length = 32)
	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	@Column(name = "readTime")
	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	@Column(name = "sendId", length = 32)
	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "title", length = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "sourceType", length = 32)
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "extra", columnDefinition = "text")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Column(name = "funcType", length = 32)
	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

	@Column(name = "sourceId", length = 32)
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	@Column(name = "sourceBusinessType", length = 32)
	public String getSourceBusinessType() {
		return sourceBusinessType;
	}

	public void setSourceBusinessType(String sourceBusinessType) {
		this.sourceBusinessType = sourceBusinessType;
	}

	
}
