package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name="t_sys_message_to",schema="")
public class MessageToEntity extends OperationEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8871710858867104691L;
	private String receiveId;//接收人ID
	private String receiveName;//接收人name
	private String isRead;//是否已读
	private String needReply;//是否回复  0:不需要回复，1：需要回复 ，2已回复
	private String status;//发送状态 0:待发送，1：发送成功,2:发送失败  ，3：删除
	private String errorMsg;//发送失败错误信息
	private String fromId;//发送表ID
	private String replyContent;//回复信息
	
	@Column(name ="receive_id",nullable=true,length=32)
	public String getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}
	
	@Column(name ="receive_name",nullable=true,length=50)
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	@Column(name ="is_read",nullable=true,length=5)
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	@Column(name ="need_reply",nullable=true,length=5)
	public String getNeedReply() {
		return needReply;
	}
	public void setNeedReply(String needReply) {
		this.needReply = needReply;
	}
	
	@Column(name ="from_id",nullable=true,length=32)
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	
	@Column(name ="status",nullable=true,length=5)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name ="errorMsg",nullable=true,length=2000)
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	@Column(name ="reply_content",nullable=true,length=200)
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
}
