package com.xplatform.base.orgnaization.member.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.xplatform.base.framework.core.util.CertUtil;
import com.xplatform.base.framework.core.util.PropertiesUtil;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.jms.model.MailModel;

public class EmailUtil {

	/**
	 * 获取邮箱session
	 * 
	 * @param mail_host
	 * @param mail_from
	 * @param mail_password
	 * @param mail_port
	 * @return
	 */
	public static Session createEmailConnection(String mail_host, String mail_username, String mail_password, String mail_port) {
		final String temp_mail_username = mail_username;
		final String temp_mail_password = mail_password;

		String smtpAuth = "true";
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", mail_host);
		props.setProperty("mail.smtp.port", mail_port);
		props.put("mail.smtp.auth", smtpAuth);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", mail_port);
		File cert = CertUtil.get(mail_host, Integer.parseInt(mail_port));
		if (cert != null) {
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			System.setProperty("javax.net.ssl.trustStore", cert.getAbsolutePath());
			props.put("javax.net.ssl.trustStore", cert.getAbsolutePath());
		} else {
			String TLS_FACTORY = "javax.net.SocketFactory";
			// gmail和live邮箱要特殊处理
			int gmail = mail_host.trim().indexOf("gmail");
			int live = mail_host.trim().indexOf("live");
			if ((gmail != -1) || (live != -1)) {
				props.put("mail.smtp.starttls.enable", "true");
			}
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
		}
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(temp_mail_username, temp_mail_password);
			}
		});
		return session;
	}
}
