package com.xplatform.base.system.message.jms.handler.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.utils.HXUtils;
import com.xplatform.base.system.message.config.push.PushUtils;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.jms.handler.JmsHandler;
import com.xplatform.base.system.message.jms.model.IMModel;
import com.xplatform.base.system.message.jms.model.PushModel;

public class PushHandler implements JmsHandler {
	private final Log logger = LogFactory.getLog(PushHandler.class);

	@Resource
	private MessageService messageService;

	public void handMessage(Object model) {
		logger.info("进入了PushHandler方法");
		PushModel pushModel = (PushModel) model;
		try {
			AjaxJson androidResult = PushUtils.sendByAndroidJson(pushModel.getAndroidPushJson());
			AjaxJson iosResult = PushUtils.sendByIOSJson(pushModel.getIOSPushJson());

			messageService.updateSendStatus(pushModel.getFuncType(), BusinessConst.sendChannel_CODE_push_android, pushModel.getSendId(), 3,
					androidResult.getInfo());
			messageService.updateSendStatus(pushModel.getFuncType(), BusinessConst.sendChannel_CODE_push_ios, pushModel.getSendId(), 3, iosResult.getInfo());
		} catch (Exception e) {
			// 更新发送表,发送失败
			String error = ExceptionUtil.getExceptionMessage(e);
			try {
				messageService.updateSendStatus(pushModel.getFuncType(), BusinessConst.sendChannel_CODE_push_android, pushModel.getSendId(), 4, error);
				messageService.updateSendStatus(pushModel.getFuncType(), BusinessConst.sendChannel_CODE_push_ios, pushModel.getSendId(), 4, error);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}