package com.xplatform.base.framework.core.cache.manager;

import com.xplatform.base.framework.core.cache.UcgCache;

public interface UcgCacheManager {
	/**
	 * <pre>
	 * 根据缓存的spring配置文件的名称获取缓存对象
	 * </pre>
	 * 
	 * @param beanName
	 * @return
	 */
	public UcgCache getCacheBean(String beanName);
	
	/**
	 * <pre>
	 * 获取数据字典缓存对象
	 * </pre>
	 * 
	 * @return
	 */
	public UcgCache getDictCacheBean();
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年11月20日 下午2:58:46
	 * @Decription 获取导出数据缓存对象
	 *
	 * @return
	 */
	public UcgCache getExportCacheBean();
}
