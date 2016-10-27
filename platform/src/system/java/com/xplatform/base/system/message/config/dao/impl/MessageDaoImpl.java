package com.xplatform.base.system.message.config.dao.impl;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.util.CertUtil;
import com.xplatform.base.system.message.config.dao.MessageDao;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.service.impl.MessageServiceImpl;

@Repository("messageDao")
public class MessageDaoImpl extends CommonDao implements MessageDao{

	private static final Logger logger = Logger.getLogger(MessageDaoImpl.class);
	@Override
	public void updateMailConfig(MailConfigEntity old) {
		// TODO Auto-generated method stub
		this.merge(old);
	}

	@Override
	public void connectSmtp(MailConfigEntity mail) throws Exception {
		// TODO Auto-generated method stub
		Session session = emailConn(mail);

		Transport transport = null;
		try {
			transport = session.getTransport("smtp");
			transport.connect(mail.getSmtpHost(), mail.getMailAddress(),
					mail.getPassWord());
		} finally {
			transport.close();
		}
	}
	
	public Session emailConn(MailConfigEntity mail) {
		final String username = mail.getMailAddress();
		final String password = mail.getPassWord();
		String smtpHost = mail.getSmtpHost();
		String smtpPort = mail.getSmtpPort();

		String smtpAuth = "true";
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtpHost);
		props.setProperty("mail.smtp.port", smtpPort);
		props.put("mail.smtp.auth", smtpAuth);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		File cert = CertUtil.get(smtpHost, Integer.parseInt(smtpPort));
		if (cert != null) {
			logger.debug("ssl connection...");
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			props.setProperty("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			System.setProperty("javax.net.ssl.trustStore", cert
					.getAbsolutePath());
			props.put("javax.net.ssl.trustStore", cert.getAbsolutePath());
		} else {
			String TLS_FACTORY = "javax.net.SocketFactory";
			int gmail = smtpHost.trim().indexOf("gmail");
			int live = smtpHost.trim().indexOf("live");
			if ((gmail != -1) || (live != -1)) {
				props.put("mail.smtp.starttls.enable", "true");
			}
			props.setProperty("mail.smtp.socketFactory.class",
					"javax.net.SocketFactory");
			logger.debug("plaintext connection or tls connection...");
		}
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		return session;
	}

	@Override
	public void connectReciever(MailConfigEntity mail) throws Exception {
		// TODO Auto-generated method stub
		String mailType = mail.getMailType();
		String host = "";
		String port = "";
		if(StringUtils.equals(mailType, "pop3")){
			host = mail.getPopHost();
			port = mail.getPopPort();
		}else{
			host = mail.getImapHost();
			port = mail.getImapPort();
		}
		Properties props = getProperty(mailType, host, port);
		URLName urln = new URLName(mailType, host, Integer.parseInt(port),
				null, mail.getMailAddress(), mail.getPassWord());
		Session session = Session.getInstance(props, null);

		Store store = null;
		try {
			store = session.getStore(urln);
			store.connect();
		} finally {
			store.close();
		}
	}
	
	private Properties getProperty(String type, String host, String port) {
		Properties prop = new Properties();
		int gmail = host.trim().indexOf("gmail");
		int live = host.trim().indexOf("live");
		int qq = host.trim().indexOf("qq");
		if ("pop3".equals(type)) {
			prop.put("mail.pop3.socketFactory.fallback", "false");
			prop.put("mail.pop3.port", port);
			prop.put("mail.pop3.socketFactory.port", port);
			prop.put("mail.pop3.host", host);
			prop.put("mail.smtp.starttls.enable", "true");
			if ((gmail != -1) || (live != -1) || (qq != -1))
				prop.put("mail.pop3.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
		} else {
			prop.put("mail.imap.socketFactory.fallback", "false");
			prop.put("mail.imap.port", port);
			prop.put("mail.imap.socketFactory.port", port);
			prop.put("mail.imap.host", host);
			prop.put("mail.store.protocol", "imap");
			if ((gmail != -1) || (live != -1) || (qq != -1)){
				prop.put("mail.imap.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
			}
		}
		return prop;
	}

}
