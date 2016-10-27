package com.xplatform.base.system.im.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.system.im.dao.BackupChatsDao;
import com.xplatform.base.system.im.entity.BackupChatsEntity;
import com.xplatform.base.system.im.service.BackupChatsService;

@Service("backupChatsService")
public class BackupChatsServiceImpl extends BaseServiceImpl<BackupChatsEntity> implements BackupChatsService {
	@Resource
	private BackupChatsDao backupChatsDao;

	@Resource
	public void setBaseDao(BackupChatsDao backupChatsDao) {
		super.setBaseDao(backupChatsDao);
	}

	public BackupChatsEntity findClosestTask() {
		return backupChatsDao.findClosestTask();
	}

}
