package com.xplatform.base.framework.core.common.service;

import java.util.List;
import java.util.Map;


public interface MailService {

	void sendMailOffLine(String toAddr, String sbuject, String content,
			List<Map<String, Object>> attachments, Integer isHtml);

}
