package com.xplatform.base.framework.core.cache.impl;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.xplatform.base.framework.core.cache.UcgCache;

public class UcgCacheImpl implements UcgCache {
	private Ehcache ehcache;
	
	public UcgCacheImpl(Ehcache ehcache) {
		this.setEhcache(ehcache);
	}
	
	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return ehcache.get(key) == null ? null
				: ehcache.get(key).getValue() == null ? null : ehcache.get(key)
						.getValue();
	}

	@Override
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		ehcache.put(new Element(key, value));
	}

	@Override
	public void put(Object key, Object value, boolean eternal) {
		// TODO Auto-generated method stub
		ehcache.put(new Element(key, value, eternal,0,0));
	}

	@Override
	public void put(Object key, Object value, Integer timeToIdleSeconds,
			Integer timeToLiveSeconds) {
		// TODO Auto-generated method stub
		ehcache.put(new Element(key, value, false, timeToIdleSeconds,
				timeToLiveSeconds));
	}

	@Override
	public void remove(Object key) {
		// TODO Auto-generated method stub
		ehcache.remove(key);
	}

	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		ehcache.removeAll();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}

	public Ehcache getEhcache() {
		return ehcache;
	}
}
