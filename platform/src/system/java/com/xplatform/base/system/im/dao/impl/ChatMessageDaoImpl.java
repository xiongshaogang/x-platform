package com.xplatform.base.system.im.dao.impl;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.system.im.dao.ChatMessageDao;
import com.xplatform.base.system.im.entity.ChatMessageEntity;


/**
 * 
 * description :组织维护dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 *
 */
@Repository("chatMessageDao")
public class ChatMessageDaoImpl extends BaseDaoImpl<ChatMessageEntity> implements ChatMessageDao {

}
