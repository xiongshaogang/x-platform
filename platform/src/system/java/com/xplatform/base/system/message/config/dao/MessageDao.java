package com.xplatform.base.system.message.config.dao;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;

public interface MessageDao extends ICommonDao {

	void updateMailConfig(MailConfigEntity old);

	void connectSmtp(MailConfigEntity mail) throws Exception;

	void connectReciever(MailConfigEntity mail) throws Exception;

}
