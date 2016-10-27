package com.xplatform.base.system.im.service;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.im.entity.BackupChatsEntity;

public interface BackupChatsService extends BaseService<BackupChatsEntity> {
	/**
	 * 找到最近的一条备份时间记录
	 * 
	 * @author xiaqiang
	 * @createtime 2015年6月3日 下午11:25:01
	 *
	 * @return
	 */
	public BackupChatsEntity findClosestTask();
	
}
