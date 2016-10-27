package com.xplatform.base.system.areas.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.system.areas.dao.AreasDao;
import com.xplatform.base.system.areas.entity.AreasEntity;
import com.xplatform.base.system.areas.service.AreasService;

@Service("areasService")
public class AreasServiceImpl extends BaseServiceImpl<AreasEntity> implements AreasService {
	@Resource
	private AreasDao areasDao;

	@Resource
	public void setBaseDao(AreasDao areasDao) {
		super.setBaseDao(areasDao);
	}

	public List<AreasEntity> queryAreasByParent(String parentId) {
		return areasDao.findByProperty("parentId", parentId);
	}

}
