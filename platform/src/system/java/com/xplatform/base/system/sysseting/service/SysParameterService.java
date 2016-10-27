package com.xplatform.base.system.sysseting.service;

import java.io.Serializable;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.sysseting.entity.SysParameterEntity;

public interface SysParameterService{
	
 	public <T> void delete(String id);
 	
 	public <T> Serializable save(T entity);
 	
 	public <T> void update(T entity);
 	
 	SysParameterEntity getEntity(Class<SysParameterEntity> class1, String id);
 	
 	void getDataGridReturn(CriteriaQuery cq, boolean b);
 	
 	/**
	 * @author luoheng
	 * 根据code查询数参数信息
	 * @param code
	 * @return
	 */
	public SysParameterEntity findByCode(String code);
 	
 	/**
	 * 初始化缓存
	 */
	public void initCache();
}
