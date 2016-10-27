/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.orgnaization.appversion.dao.impl;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.orgnaization.appversion.dao.AppVersionDao;
import com.xplatform.base.orgnaization.appversion.entity.AppVersionEntity;




@Service("appVersionDao")
public class AppVersionDaoImpl extends BaseDaoImpl<AppVersionEntity> implements AppVersionDao{

}

	