package com.xplatform.base.framework.core.util;

import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;

/**
 * 
 * description :获取cache bean
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月19日 下午8:14:23
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月19日 下午8:14:23
 *
 */
public class CacheUtil {
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月19日 下午8:14:44
	 * @Decription 数据字典缓存
	 * @return
	 */
	public static UcgCache getDictCache(){
		UcgCacheManager UcgCacheManager=ApplicationContextUtil.getBean("ucgCacheManager");
		UcgCache cache=UcgCacheManager.getDictCacheBean();
		return cache;
	}
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月19日 下午8:15:12
	 * @Decription 系统配置缓存
	 * @return
	 */
	public static UcgCache getSystemSettingCache(){
		UcgCacheManager UcgCacheManager=ApplicationContextUtil.getBean("ucgCacheManager");
		UcgCache cache=UcgCacheManager.getDictCacheBean();
		return cache;
	}
}
