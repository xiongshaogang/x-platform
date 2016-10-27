package com.xplatform.base.platform.common.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

//import com.xplatform.base.develop.formrule.service.FormRuleService;
import com.xplatform.base.framework.core.util.PropertiesUtil;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.sysseting.service.SysParameterService;

public class CacheListener implements ApplicationListener {
	private static Logger logger = Logger.getLogger(CacheListener.class);
	private static boolean initFlag = false;

//注释by lxt 20150507	
//	@Resource
//	private FormRuleService formRuleService;
	@Resource
	private DictTypeService dictTypeService;
	@Resource
	private SysParameterService sysParameterService;
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		if (!initFlag) {
			try {
				PropertiesUtil props = new PropertiesUtil("sysConfig.properties");
				String initCacheFlag=props.readProperty("initCacheFlag");
				if("Y".equals(initCacheFlag)){
//注释by lxt 20150507
//					formRuleService.initCache();
					dictTypeService.initCache();
					sysParameterService.initCache();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("初始化缓存失败");
			}
			initFlag = true;
		}
	}
}
