package com.xplatform.base.framework.core.cache.manager.impl;

import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.annotation.Autowired;

import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.impl.UcgCacheImpl;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;

public class UcgCacheManagerImpl implements UcgCacheManager {

	@Override
	public UcgCache getCacheBean(String beanName) {
		// TODO Auto-generated method stub
		UcgCache huilanCache = null;
		Ehcache ehcache = (Ehcache) ApplicationContextUtil.getBean(beanName);
		if (ehcache != null) {
			huilanCache = new UcgCacheImpl(ehcache);
		}
		return huilanCache;
	}

	@Override
	public UcgCache getDictCacheBean() {
		// TODO Auto-generated method stub
		return getCacheBean("dictCache");
	}
	
	@Override
	public UcgCache getExportCacheBean() {
		// TODO Auto-generated method stub
		return getCacheBean("exportCache");
	}

}
