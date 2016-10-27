package com.xplatform.base.system.message.jms.handler.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.sms.JXTSmsClient;
import com.xplatform.base.framework.core.util.sms.SmsUtil;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.system.message.config.entity.MessageFromEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.job.MessageEngine;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.jms.handler.JmsHandler;
import com.xplatform.base.system.message.jms.model.SmsModel;

public class SmsHandler implements JmsHandler {
	private final Log logger = LogFactory.getLog(SmsHandler.class);

	@Resource
	private MessageService messageService;

	public void handMessage(Object model) {
		logger.info("进入了SmsHandler方法");
		SmsModel smsModel = (SmsModel) model;
		try {
			String[] mobiles = smsModel.getMobile();
			for (String mobile : mobiles) {
				SmsUtil.send(mobile, smsModel.getSmsContent());
			}

			messageService.updateSendStatus(smsModel.getFuncType(), BusinessConst.sendChannel_CODE_sms, smsModel.getSendId(), 3, null);
		} catch (Exception e) {
			// 更新发送表,发送失败
			String error = ExceptionUtil.getExceptionMessage(e);
			try {
				messageService.updateSendStatus(smsModel.getFuncType(), BusinessConst.sendChannel_CODE_sms, smsModel.getSendId(), 4, error);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}