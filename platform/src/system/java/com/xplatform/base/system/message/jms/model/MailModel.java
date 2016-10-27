package com.xplatform.base.system.message.jms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;

public class MailModel implements Serializable {

	private static final long serialVersionUID = 384572988348167127L;
	private String funcType;// 消息功能来源(global-公告;group-分组消息(选择部门/角色/某个用户范围);private-个人消息(1对1/1对少量n))
	private String sendId;// 发送表Id
	private String title;// 消息标题
	private String mailContent;// 邮件内容

	private MailConfigEntity mailConfig;// 邮件配置
	private String[] mailMain;// 主送邮箱地址(","分割)
	private String[] mailCC;// 抄送邮箱地址(","分割)
	private String[] mailBCC;// 暗送邮箱地址(","分割)
	private List<AttachVo> dataVoList;// 附件VO

	// private String mailTemplate;//邮件模板名称

	public String getSendId() {
		return sendId;
	}

	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
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

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public MailConfigEntity getMailConfig() {
		return mailConfig;
	}

	public void setMailConfig(MailConfigEntity mailConfig) {
		this.mailConfig = mailConfig;
	}

	public String[] getMailMain() {
		return mailMain;
	}

	public void setMailMain(String[] mailMain) {
		this.mailMain = mailMain;
	}

	public String[] getMailCC() {
		return mailCC;
	}

	public void setMailCC(String[] mailCC) {
		this.mailCC = mailCC;
	}

	public String[] getMailBCC() {
		return mailBCC;
	}

	public void setMailBCC(String[] mailBCC) {
		this.mailBCC = mailBCC;
	}

	public List<AttachVo> getDataVoList() {
		return dataVoList;
	}

	public void setDataVoList(List<AttachVo> dataVoList) {
		this.dataVoList = dataVoList;
	}

}
