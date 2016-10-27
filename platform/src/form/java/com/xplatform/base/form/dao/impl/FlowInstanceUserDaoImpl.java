package com.xplatform.base.form.dao.impl;


import org.springframework.stereotype.Repository;

import com.xplatform.base.form.dao.FlowInstanceUserDao;
import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;

@Repository("flowInstanceUserDao")
public class FlowInstanceUserDaoImpl extends BaseDaoImpl<FlowInstanceUserEntity> implements FlowInstanceUserDao {
	
}
