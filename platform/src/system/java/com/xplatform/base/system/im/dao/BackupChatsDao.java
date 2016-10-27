package com.xplatform.base.system.im.dao;

import com.xplatform.base.framework.core.common.dao.BaseDao;
import com.xplatform.base.system.im.entity.BackupChatsEntity;

public interface BackupChatsDao extends BaseDao<BackupChatsEntity> {
	public BackupChatsEntity findClosestTask();
}
