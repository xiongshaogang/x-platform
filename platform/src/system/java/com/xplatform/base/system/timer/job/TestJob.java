package com.xplatform.base.system.timer.job;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJob extends BaseJob {
	protected Logger logger = LoggerFactory.getLogger(TestJob.class);
	@Override
	public void executeJob(JobExecutionContext context)
			throws Exception {
		// TODO Auto-generated method stub
		logger.info("开始执行测试任务");
		
	}

}
