package com.xplatform.base.system.message.jms.handler.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.orgnaization.member.tools.EmailUtil;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.entity.MessageFromEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.job.MessageEngine;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.jms.handler.JmsHandler;
import com.xplatform.base.system.message.jms.model.MailModel;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;

public class MailHandler implements JmsHandler {
	private final Log logger = LogFactory.getLog(MailHandler.class);

	@Resource
	private MessageService messageService;

	public void handMessage(Object model) {
		logger.info("进入了MailHandler方法");
		MailModel mailModel = (MailModel) model;
		MailConfigEntity mailConfig = mailModel.getMailConfig();

		String mail_host = null;
		String mail_from = null;
		String mail_username = null;
		String mail_password = null;
		String mail_port = null;
		if (mailConfig != null) {
			mail_host = mailConfig.getSmtpHost();
			mail_from = mailConfig.getMailAddress();
			mail_username = mailConfig.getUserName();
			mail_password = mailConfig.getPassWord();
			mail_port = mailConfig.getSmtpPort();
		} else {
			mail_host = ConfigConst.mail_host;
			mail_from = ConfigConst.mail_from;
			mail_username = ConfigConst.mail_username;
			mail_password = ConfigConst.mail_password;
			mail_port = ConfigConst.mail_port;
		}
		Session session = EmailUtil.createEmailConnection(mail_host, mail_from, mail_password, mail_port);

		try {
			// Instantiate a message
			Message msg = new MimeMessage(session);

			// Set message attributes
			msg.setFrom(new InternetAddress(mail_from));
			msg.setSubject(mailModel.getTitle());// 邮件主题
			msg.setSentDate(new Date());
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent(mailModel.getMailContent(), "text/html;charset=utf-8"); // 邮件正文内容
			Multipart container = new MimeMultipart();
			container.addBodyPart(textBodyPart);

			// 设置邮件附件
			if (mailModel.getDataVoList() != null) {
				for (AttachVo file : mailModel.getDataVoList()) {
					MimeBodyPart fileBodyPart = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(file.getAbsolutePath());// 要发送的附件地址
					fileBodyPart.setDataHandler(new DataHandler(fds));
					fileBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));// 设置附件的名称
					container.addBodyPart(fileBodyPart);
				}
			}

			Address[] addressArray = null;
			if (mailModel.getMailMain() != null) {
				addressArray = toAddress(mailModel.getMailMain());
				if (addressArray != null) {
					msg.setRecipients(Message.RecipientType.TO, addressArray);// 收件人
				}
			}
			Address[] ccAddressArray = null;
			if (mailModel.getMailCC() != null) {
				ccAddressArray = toAddress(mailModel.getMailCC());
				if (ccAddressArray != null) {
					msg.setRecipients(Message.RecipientType.CC, ccAddressArray); // 抄送
				}
			}
			Address[] bccAddressArray = null;
			if (mailModel.getMailBCC() != null) {
				bccAddressArray = toAddress(mailModel.getMailBCC());
				if (bccAddressArray != null) {
					msg.setRecipients(Message.RecipientType.BCC, bccAddressArray);// 暗送
				}
			}
			msg.setContent(container);
			/*
			 * Transport transport = session.getTransport("smtp");
			 * transport.connect(HOST, FROM, PWD); transport.send(msg);
			 * transport.close();
			 */
			Transport.send(msg);

			// 更新发送表,邮件发送成功
			messageService.updateSendStatus(mailModel.getFuncType(), BusinessConst.sendChannel_CODE_email, mailModel.getSendId(), 3, null);
		} catch (Exception e) {
			// 更新发送表,邮件发送失败
			String error = ExceptionUtil.getExceptionMessage(e);
			try {
				messageService.updateSendStatus(mailModel.getFuncType(), BusinessConst.sendChannel_CODE_email, mailModel.getSendId(), 4, error);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 将邮件地址转化为Address数组
	 * 
	 * @param emailAddress
	 * @return
	 * @throws AddressException
	 */
	private Address[] toAddress(String[] emailAddress) throws AddressException {
		if (emailAddress != null && emailAddress.length > 0) {
			Address[] addressArray = new InternetAddress[emailAddress.length];
			for (int i = 0; i < addressArray.length; i++) {
				addressArray[i] = new InternetAddress(emailAddress[i]);
			}
			return addressArray;
		} else {
			return null;
		}
	}
}
