package com.xplatform.base.framework.core.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.service.MailRunner;
import com.xplatform.base.framework.core.common.service.MailService;


public class MailServiceImpl implements MailService {

	private static final String ENCODING = "utf-8";

	/**
	 * 邮件发送器.
	 */
	private JavaMailSender mailSender;

	/**
	 * 任务管理器.
	 */
	private TaskExecutor texec;

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * 发送人地址.
	 */
	private String fromAddr;
	private boolean isRuning = false;

	private String[] excludedomain;
	

	/**
	 * 发送邮件.
	 */
	@Override
	public void sendMailOffLine(String toAddr, String sbuject, String content,
			List<Map<String, Object>> attachments, Integer isHtml) {
		MailRunner runner = new MailRunner(fromAddr, toAddr, sbuject, content, attachments, isHtml,mailSender);
		texec.execute(runner);
	}


	public TaskExecutor getTexec() {
		return texec;
	}

	public void setTexec(TaskExecutor texec) {
		this.texec = texec;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	public boolean isRuning() {
		return isRuning;
	}

	public void setRuning(boolean isRuning) {
		this.isRuning = isRuning;
	}

	public String[] getExcludedomain() {
		return excludedomain;
	}

	public void setExcludedomain(String[] excludedomain) {
		this.excludedomain = excludedomain;
	}


}
