package com.xplatform.base.system.im.dao.impl;

import org.springframework.stereotype.Repository;
import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.system.im.dao.BackupChatsDao;
import com.xplatform.base.system.im.entity.BackupChatsEntity;


/**
 * 
 * description :组织维护dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 *
 */
@Repository("backupChatsDao")
public class BackupChatsDaoImpl extends BaseDaoImpl<BackupChatsEntity> implements BackupChatsDao {
	
	@Override
	public BackupChatsEntity findClosestTask() {
		String hql = "FROM BackupChatsEntity WHERE endTimestamp=(SELECT MAX(endTimestamp) FROM BackupChatsEntity)";
		return findUniqueByHql(hql);
	}


}
