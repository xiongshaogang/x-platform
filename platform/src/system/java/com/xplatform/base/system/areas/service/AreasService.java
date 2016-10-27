package com.xplatform.base.system.areas.service;

import java.util.List;

import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.areas.entity.AreasEntity;

public interface AreasService extends BaseService<AreasEntity> {
	/**
	 * 通过父区域Id查找下属的区域
	 * 
	 * @param parentId
	 * @return
	 */
	public List<AreasEntity> queryAreasByParent(String parentId);
}
