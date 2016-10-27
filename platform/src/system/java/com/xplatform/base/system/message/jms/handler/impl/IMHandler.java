package com.xplatform.base.system.message.jms.handler.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.utils.HXUtils;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.jms.handler.JmsHandler;
import com.xplatform.base.system.message.jms.model.IMModel;

public class IMHandler implements JmsHandler {
	private final Log logger = LogFactory.getLog(IMHandler.class);

	@Resource
	private MessageService messageService;

	public void handMessage(Object model) {
		logger.info("进入了IMHandler方法");
		IMModel IMModel = (IMModel) model;
		ObjectNode result = null;
		try {
			ObjectNode IMMsg = JSONHelper.toObjectNode(IMModel.getIMMsg());
			ObjectNode extra = null;
			if (StringUtil.isNotEmpty(IMModel.getExtra())) {
				extra = JSONHelper.toObjectNode(IMModel.getExtra());
			}
			ObjectNode finalData = JsonNodeFactory.instance.objectNode();
			finalData.put("data", extra);
			result = HXUtils.sendUserMessages(IMModel.getReceive(), IMMsg, IMModel.getFromId(), finalData);
			// TODO 这里存在有部分用户发送IM不成功的可能性,现在处理是依旧算成功,不过记录错误信息
			messageService.updateSendStatus(IMModel.getFuncType(), BusinessConst.sendChannel_CODE_sms, IMModel.getSendId(), 3, result.asText());
		} catch (Exception e) {
			// 更新发送表,发送失败
			String error = ExceptionUtil.getExceptionMessage(e);
			try {
				messageService.updateSendStatus(IMModel.getFuncType(), BusinessConst.sendChannel_CODE_sms, IMModel.getSendId(), 4, error);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}