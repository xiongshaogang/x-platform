package com.xplatform.base.orgnaization.orggroup.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.BaseDao;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;

public interface OrgGroupDao  extends BaseDao<OrgGroupEntity> {

	public List<OrgGroupEntity> queryOrgGroupList();
	
}
