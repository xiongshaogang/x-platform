package com.xplatform.base.orgnaization.orggroup.dao.impl;


import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.orgnaization.orggroup.dao.GroupShareDao;
import com.xplatform.base.orgnaization.orggroup.entity.GroupShareEntity;

@Repository("groupShareDao")
public class GroupShareDaoImpl extends BaseDaoImpl<GroupShareEntity> implements GroupShareDao {

}
