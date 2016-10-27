package com.xplatform.base.form.dao.impl;

import org.springframework.stereotype.Repository;

import com.xplatform.base.form.dao.AppForbiddenDao;
import com.xplatform.base.form.entity.AppForbiddenEntity;
import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;

@Repository("appForbiddenDao")
public class AppForbiddenDaoImpl extends BaseDaoImpl<AppForbiddenEntity> implements AppForbiddenDao{

}
