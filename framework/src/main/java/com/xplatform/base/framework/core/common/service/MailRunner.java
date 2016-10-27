package com.xplatform.base.framework.core.common.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import jodd.util.StringUtil;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * 
 * 邮件发送过程模块.
 * 
 * @author binyong
 * 
 */
public class MailRunner implements Runnable {

	private static final String ENCODING = "utf-8";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 邮件发送器.
	 */
	private JavaMailSender mailSender;

	/**
	 * 发送邮件地址.
	 */
	private String fromaddr;
	/**
	 * 收件地址.
	 */
	private String toAddr;
	/**
	 * 标题.
	 */
	private String subject;
	/**
	 * 邮件内容.
	 */
	private String content;

	/**
	 * 邮件所有附件. key:fileName value:fileData
	 */
	private List<Map<String, Object>> attachments;

	/**
	 * 是否以html格式发送.
	 */
	private Integer isHtml = 1;

	public MailRunner(String fromaddr, String toAddr, String subject, String content,
			List<Map<String, Object>> attachments, Integer isHtml,JavaMailSender mailSender) {
		super();
		this.fromaddr = fromaddr;
		this.toAddr = toAddr;
		this.subject = subject;
		this.content = content;
		this.attachments = attachments;
		this.isHtml = isHtml;
		this.mailSender =mailSender;
	}

	@Override
	public void run() {

		try {
			sendEmailByJavaMailSender();
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("邮件发送失败");
		}

	}

	
	/*private void sendEmailBySimple() throws Exception {
		SimpleEmail email = new SimpleEmail();
		email.setTLS(true);
		email.setHostName(host);
		email.setAuthenticator(new DefaultAuthenticator(username, password)); // 用户名和密码
		email.setSSL(true);
		email.setSslSmtpPort(port);

		try {
			email.addTo(toAddr); // 接收方
			email.setFrom(fromaddr); // 发送方
			email.setSubject(subject); // 标题
			email.setMsg(content); // 内容
			email.send();

		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

	 private void sendEmailByMailSender() throws Exception {
	 MimeMessagePreparator preparator = new MimeMessagePreparator() {  
	            public void prepare(MimeMessage mimeMessage) throws Exception {  
	                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));//toXXX邮箱  
	                mimeMessage.setFrom(new InternetAddress(fromaddr)); //from XXX邮箱  
	                mimeMessage.setSubject(subject);      //设置主题  
	                mimeMessage.setText(content);          //设置内容  
	            }  
	        };  
	        try {  
	            this.mailSender.send(preparator);  
	        } catch (MailException e) {  
	            e.printStackTrace();  
	        }  
	 }*/
	 
	 /**
	   * 发送Mime邮件，携带附件.
	   */
	 private boolean sendEmailByJavaMailSender() throws Exception {
		 MimeMessage msg = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(msg, true, ENCODING);
				// 设置参数	
				helper.setTo(toAddr);
				helper.setFrom(fromaddr);
				/*
				 * 暂时不需要回复 try { helper.setReplyTo("support@scholarmate.com", "scholarmate support"); } catch
				 * (UnsupportedEncodingException e) { logger.warn("构造邮件地址失败", e); }
				 */
				helper.setSubject(subject);
				buildContent(helper, content);
				buildAttachment(helper, attachments);
				// 发送邮件
				mailSender.send(msg);
				//if (this.mailCode != null && mailLogService != null) {
					// 发送完成，设置邮件标志位为正确发送成功
				//	mailLogService.sendMailSucceed(this.mailCode);
				//}
				return true;
			} catch (Exception e) {
				logger.error("邮件发送失败:收件人=" + toAddr + "subject= " + subject, e);
				// 发送错误，设置邮件错误原因。
				// 设置邮件发送失败.
				String errorMsg = e.getMessage();
				if (errorMsg != null && errorMsg.length() > 2000) {
					errorMsg = errorMsg.substring(0, 1500);
				}
				//if (this.mailCode != null && mailLogService != null) {
				//	mailLogService.sendMailFail(this.mailCode, errorMsg);
				//}
				return false;
			}
		 
	}
	 
	 

	/**
	 * 生成html格式内容.
	 */
	private void buildContent(MimeMessageHelper helper, String content2)
			throws Exception {
		if (isHtml == 1) {
			helper.setText(replaceToHtml(content2), true);
		} else {
			helper.setText(replaceToHtml(content2), false);
		}

	}

	public static String replaceToHtml(String source) {
		if (StringUtil.isEmpty(source)) {
			return "";
		}
		return source.replace("\r\n", "<br>").replace("\n", "<br>");
	}

	/**
	 * 添加附件.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public void buildAttachment(MimeMessageHelper helper,
			List<Map<String, Object>> attachs) throws Exception {

		if (attachs == null) {
			return;
		}

		for (Map<String, Object> map : attachs) {

			if (map.get("fileName") == null) {
				continue;
			}
			String fileName = map.get("fileName").toString();
			// String fileName = new
			// String(map.get("fileName").toString().getBytes("ISO-8859-1"),
			// "gb2312");
			// 取出附件的文件名和文件数据

			helper.addAttachment(MimeUtility.encodeWord(fileName),
					(File) map.get("fileData"));

		}
	}

}
