/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.system.attachment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.system.attachment.dao.FTPAttachDao;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.attachment.service.FTPAttachService;
import com.xplatform.base.system.dict.dao.DictTypeDao;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.service.DictTypeService;

/**
 * description : FTP远程文件记录
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午6:08:57
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午6:08:57 
 *
*/
@Service("ftpAttachService")
public class FTPAttachServiceImpl extends BaseServiceImpl<FTPAttachEntity> implements FTPAttachService {
	@Resource
	private FTPAttachDao ftpAttachDao;

	@Resource
	public void setBaseDao(FTPAttachDao ftpAttachDao) {
		super.setBaseDao(ftpAttachDao);
	}
}
