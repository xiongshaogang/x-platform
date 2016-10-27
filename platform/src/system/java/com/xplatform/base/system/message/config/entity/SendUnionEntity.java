package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 多类型接收方表
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_sys_message_sendunion")
public class SendUnionEntity extends OperationEntity implements Serializable {
	private String relationId;// 关联Id
	private String relationType; // 关联类型(org-组织机构;user-用户)
	private String receiveType;// 接收方类型(本字段暂时用于邮件方式使用,主送-main;抄送-cc;暗送-bcc)
	private String sendId;// 发送表Id
	private String title;// 标题,冗余以提高性能
	private String sourceType;// 业务功能来源,冗余以提高性能

	@Column(name = "relationId", length = 32)
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	@Column(name = "relationType", length = 32)
	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	@Column(name = "receiveType", length = 32)
	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	@Column(name = "sendId", length = 32)
	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
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

}
