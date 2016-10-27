package com.xplatform.base.system.im.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.system.im.dao.BackupChatsDao;
import com.xplatform.base.system.im.dao.ChatMessageDao;
import com.xplatform.base.system.im.entity.ChatMessageEntity;
import com.xplatform.base.system.im.service.ChatMessageService;

@Service("chatMessageService")
public class ChatMessageServiceImpl extends BaseServiceImpl<ChatMessageEntity> implements ChatMessageService {
	@Resource
	public void setBaseDao(ChatMessageDao chatMessageDao) {
		super.setBaseDao(chatMessageDao);
	}

	@Resource
	private ChatMessageDao chatMessageDao;
}
