package com.xplatform.base.system.im.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;

/**
 * 即时聊天消息备份表
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2015年6月3日 下午5:09:29
 * 
 *
 */

@Entity
@Table(name = "t_sys_chatmessage")
public class ChatMessageEntity extends AssignedOperationEntity {
	private String mainType; // 消息类型(目前只有chatmessage)
	private Date created; // 消息在环信服务器创建时间
	private Date modified; // 消息修改时间
	private Date sendTimestamp; // 消息在客户端创建时间
	private String fromId; // 发送方userId
	private String msgId;// 消息关键Id
	private String toId; // 接收方userId(群聊则和groupId相同)
	private String groupId; // 群聊Id
	private String chatType; // 聊天类型(1.单聊chat;2.群聊groupchat;)
	private String bodies; // 消息体(json)
	private String ext; // 消息扩展体(json)

	private String type; // img、file、video、audio、loc、txt 几种取值
	private String msg; // 文本消息
	private String url; // 源文件url
	private String fileAttachId; // 源文件关联系统附件Id
	private String filename; // 文件/视频/音频 名称
	private Integer length; // 音频/视频时长(秒)
	private String secret; // 文件/图片/视频/音频获取密匙
	private String thumb; // 缩略图url
	private String thumbAttachId; // 缩略图关联系统附件Id
	private Long fileLength; // 文件大小(byte)
	private String thumbSecret; // 缩略图获取密匙
	private Integer width; // 图片宽度
	private Integer height; // 图片高度
	private String addr; // 地址
	private Double lat; // 纬度
	private Double lng; // 经度

	@Column(name = "mainType", length = 20)
	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "modified")
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	@Column(name = "sendTimestamp")
	public Date getSendTimestamp() {
		return sendTimestamp;
	}

	public void setSendTimestamp(Date sendTimestamp) {
		this.sendTimestamp = sendTimestamp;
	}

	@Column(name = "fromId", length = 32)
	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	@Column(name = "msgId", length = 100)
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Column(name = "toId", length = 32)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	@Column(name = "groupId", length = 32)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "chatType", length = 50)
	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	@Column(name = "bodies", columnDefinition = "TEXT")
	public String getBodies() {
		return bodies;
	}

	public void setBodies(String bodies) {
		this.bodies = bodies;
	}

	@Column(name = "ext", columnDefinition = "TEXT")
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Column(name = "type", length = 50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "msg", columnDefinition = "TEXT")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Column(name = "url", length = 500)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "filename", length = 100)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "length", length = 5)
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Column(name = "secret", length = 50)
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name = "thumb", length = 500)
	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	@Column(name = "fileLength")
	public Long getFileLength() {
		return fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

	@Column(name = "thumbSecret", length = 50)
	public String getThumbSecret() {
		return thumbSecret;
	}

	public void setThumbSecret(String thumbSecret) {
		this.thumbSecret = thumbSecret;
	}

	@Column(name = "width", length = 4)
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = "height", length = 4)
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "addr", length = 100)
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Column(name = "lat", precision = 8, scale = 6)
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Column(name = "lng", precision = 8, scale = 6)
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Column(name = "fileAttachId", length = 32)
	public String getFileAttachId() {
		return fileAttachId;
	}

	public void setFileAttachId(String fileAttachId) {
		this.fileAttachId = fileAttachId;
	}

	@Column(name = "thumbAttachId", length = 32)
	public String getThumbAttachId() {
		return thumbAttachId;
	}

	public void setThumbAttachId(String thumbAttachId) {
		this.thumbAttachId = thumbAttachId;
	}

}
