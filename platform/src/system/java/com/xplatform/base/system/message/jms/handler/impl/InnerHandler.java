package com.xplatform.base.system.message.jms.handler.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.system.message.config.entity.MessageReceiveEntity;
import com.xplatform.base.system.message.config.entity.SendUnionEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.jms.handler.JmsHandler;
import com.xplatform.base.system.message.jms.model.InnerModel;

public class InnerHandler implements JmsHandler {
	private final Log logger = LogFactory.getLog(InnerHandler.class);

	@Resource
	private SysUserService sysUserService;

	@Resource
	private MessageService messageService;

	public void handMessage(Object model) {
		logger.info("进入了InnerHandler方法");
		InnerModel innerModel = (InnerModel) model;
		String sendId = innerModel.getSendId();
		String funcType = innerModel.getFuncType();
		String title = innerModel.getTitle();
		String sourceType = innerModel.getSourceType();
		String sourceBusinessType = innerModel.getSourceBusinessType();
		String sourceId = innerModel.getSourceId();
		String extra = innerModel.getExtra();
		try {
			if (BusinessConst.FUNCTYPE_CODE_private.equals(funcType)) {
				// 如果是点对点消息
				List<UserEntity> userList = sysUserService.getUserByIds(innerModel.getReceive());
				for (UserEntity user : userList) {
					MessageReceiveEntity messageReceive = new MessageReceiveEntity();
					messageReceive.setReceiveId(user.getId());
					messageReceive.setSendId(sendId);
					messageReceive.setFuncType(funcType);
					messageReceive.setStatus(0);
					messageReceive.setTitle(title);
					messageReceive.setSourceType(sourceType);
					messageReceive.setSourceBusinessType(sourceBusinessType);
					messageReceive.setSourceId(sourceId);
					messageReceive.setExtra(extra);
					messageService.saveMessageReceive(messageReceive);
				}
			} else if (BusinessConst.FUNCTYPE_CODE_group.equals(funcType)) {
				// 如果是点对局部消息
				saveSendUnion(innerModel.getReceive(), BusinessConst.SendPart_CODE_main, title, sourceType);
				saveSendUnion(innerModel.getCc(), BusinessConst.SendPart_CODE_cc, title, sourceType);
				saveSendUnion(innerModel.getBcc(), BusinessConst.SendPart_CODE_bcc, title, sourceType);
			}

			messageService.updateSendStatus(funcType, BusinessConst.sendChannel_CODE_msg, sendId, 3, null);
		} catch (BusinessException e) {
			// 更新发送表,发送失败
			String error = ExceptionUtil.getExceptionMessage(e);
			try {
				messageService.updateSendStatus(funcType, BusinessConst.sendChannel_CODE_sms, sendId, 4, error);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void saveSendUnion(String receive, String sendPart, String title, String sourceType) {
		String[] receiveArray = receive.split(",");
		for (String receiveStr : receiveArray) {
			String[] mulType = receiveStr.split("^^");
			String id = mulType[0];
			String type = mulType[2];

			SendUnionEntity sendUnion = new SendUnionEntity();
			sendUnion.setRelationId(id);
			sendUnion.setRelationType(type);
			sendUnion.setReceiveType(sendPart);
			sendUnion.setTitle(title);
			sendUnion.setSourceType(sourceType);
			messageService.saveSendUnion(sendUnion);
		}
	}
}