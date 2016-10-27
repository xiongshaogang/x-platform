package com.xplatform.base.system.sysseting.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.sysseting.dao.SysParameterDao;
import com.xplatform.base.system.sysseting.entity.SysParameterEntity;
import com.xplatform.base.system.sysseting.service.SysParameterService;


@Service("sysParameterService")
public class SysParameterServiceImpl implements SysParameterService {

    @Resource
	private SysParameterDao sysParameterDao;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private UcgCacheManager ucgCacheManager;
	
	private UcgCache ucgCache;

	public void setSysParameterDao(SysParameterDao sysParameterDao) {
		this.sysParameterDao = sysParameterDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public void setUcgCacheManager(UcgCacheManager ucgCacheManager) {
		this.ucgCacheManager = ucgCacheManager;
	}
	
	@Action(moduleCode="sysParameterManager",description="系统参数删除",detail="系统参数${id}删除成功", execOrder = ActionExecOrder.BEFORE)
 	public <T> void delete(String id) {
 		ucgCache=ucgCacheManager.getDictCacheBean();
 		SysParameterEntity parameter = this.getEntity(SysParameterEntity.class, id);
 		if(ucgCache.get("parameter_" + parameter.getCode()) !=null )
 			ucgCache.remove("parameter_" + parameter.getCode());
		sysParameterDao.delete(parameter);
 	}
 	
 	@Action(moduleCode="sysParameterManager",description="系统参数新增",detail="系统参数${name}添加成功", execOrder = ActionExecOrder.BEFORE)
 	public <T> Serializable save(T entity) {
 		ucgCache=ucgCacheManager.getDictCacheBean();
 		SysParameterEntity parameter = (SysParameterEntity) entity;
 		ucgCache.put("parameter_" + parameter.getCode(), parameter.getValue());
 		return sysParameterDao.save(entity);
 	}
 	
 	@Override
 	@Action(moduleCode="sysParameterManager",description="系统参数修改",detail="系统参数${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public <T> void update(T entity) {
		try {
			ucgCache=ucgCacheManager.getDictCacheBean();
	 		SysParameterEntity parameterEntity = (SysParameterEntity) entity;
	 		SysParameterEntity sysParameterEntity = this.getEntity(SysParameterEntity.class, parameterEntity.getId());
	 		ucgCache.remove("parameter_" + sysParameterEntity.getCode());
	 		ucgCache.put("parameter_" + parameterEntity.getCode(), parameterEntity.getValue());
	 		
	 		SysParameterEntity oldEntity = getEntity(SysParameterEntity.class, parameterEntity.getId());
			MyBeanUtils.copyBeanNotNull2Bean(parameterEntity, oldEntity);
			sysParameterDao.merge(oldEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 	
 	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
 		sysParameterDao.getDataGridReturn(cq, b);
	}

	@Override
	public SysParameterEntity getEntity(Class<SysParameterEntity> class1, String id) {
		return sysParameterDao.getEntity(class1, id);
	}
	
	/**
	 * @author luoheng
	 * 根据code查询数参数信息
	 * @param code
	 * @return
	 */
	public SysParameterEntity findByCode(String code){
		String sql = "from SysParameterEntity where code = ?";
		List<SysParameterEntity> parameterEntitieList = sysParameterDao.findHql(sql, new Object[]{code});
		SysParameterEntity parameterEntity = null;
		if (parameterEntitieList.size() > 0)
			parameterEntity = parameterEntitieList.get(0);
		return parameterEntity;
	}
	
	/**
	 * @author luoheng
	 * 查询所有参数信息
	 * @return
	 */
	public List<SysParameterEntity> findBySysParameterEntity(){
		String sql = " from SysParameterEntity";
		List<SysParameterEntity> parameterList = sysParameterDao.findHql(sql, new Object[]{});
		return parameterList;
	}
	
	/**
	 * 初始化缓存
	 */
	public void initCache(){
		ucgCache=ucgCacheManager.getDictCacheBean();
		List<SysParameterEntity> parameterList = this.findBySysParameterEntity();
		for (SysParameterEntity parameter : parameterList) {
			ucgCache.put("parameter_" + parameter.getCode(), parameter.getValue());
		}
	}
 	
}