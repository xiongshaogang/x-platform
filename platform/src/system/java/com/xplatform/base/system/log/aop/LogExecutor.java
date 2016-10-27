package com.xplatform.base.system.log.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.system.log.entity.OperLogEntity;
import com.xplatform.base.system.log.service.OperLogService;

class LogExecutor implements Runnable {
	private Log logger = LogFactory.getLog(LogExecutor.class);
	private LogHolder logHolder;
	//private FreemarkerHelper freemarkerHelper;
	private OperLogService operLogService;

	public void setLogHolders(LogHolder logHolder) {
		this.logHolder = logHolder;
		this.operLogService = ((OperLogService) ApplicationContextUtil
				.getBean(OperLogService.class));
		//this.freemarkerHelper = ((FreemarkerHelper) ApplicationContextUtil
		//		.getBean(FreemarkerHelper.class));
	}

	private void doLog() throws Exception {
		OperLogEntity logSys = this.logHolder.getOperLogEntity();
		if (this.logHolder.isNeedParse()) {
			String detail = FreemarkerHelper.parseByStringTemplate(
					this.logHolder.getParseDataModel(), logSys.getDetail());
			logSys.setDetail(detail);
		}
		operLogService.save(logSys);
	}

	public void run() {
		try {
			doLog();
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}
}