package com.xplatform.base.system.timer.job;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.timer.entity.TimerLogEntity;
import com.xplatform.base.system.timer.service.TimerLogService;

/**
 * 
 * description :所有的任务类都要继承这个basejob
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月18日 上午9:37:42
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月18日 上午9:37:42
 *
 */
@DisallowConcurrentExecution
public abstract class BaseJob implements Job {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public abstract void executeJob(JobExecutionContext paramJobExecutionContext)
			throws Exception;

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String jobName = context.getJobDetail().getKey().getName();

		String trigName = "directExec";
		Trigger trig = context.getTrigger();
		if (trig != null)
			trigName = trig.getKey().getName();
		Date strStartTime = new Date();
		long startTime = System.currentTimeMillis();
		try {
			executeJob(context);
			long endTime = System.currentTimeMillis();
			Date strEndTime = new Date();

			long runTime = (endTime - startTime) / 1000L;
			addLog(jobName, trigName, strStartTime, strEndTime, runTime,
					"任务执行成功!", 1);
		} catch (Exception ex) {
			long endTime = System.currentTimeMillis();
			Date strEndTime = new Date();
			long runTime = (endTime - startTime) / 1000L;
			try {
				addLog(jobName, trigName, strStartTime, strEndTime, runTime, "执行任务出错", 0);
			} catch (Exception e) {
				e.printStackTrace();
				this.log.error("执行任务出错:" + e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年6月18日 上午9:38:11
	 * @Decription
	 * 添加日志
	 * @param jobName
	 * @param trigName
	 * @param strStartTime
	 * @param strEndTime
	 * @param runTime
	 * @param content
	 * @param state
	 * @throws Exception
	 */
	private void addLog(String jobName, String trigName, Date strStartTime,
			Date strEndTime, long runTime, String content, int state)
			throws Exception {
		TimerLogService timerLogService=ApplicationContextUtil.getBean("timerLogService");
		TimerLogEntity model = new TimerLogEntity();
		model.setJobName(jobName);
		model.setTriggerName(trigName);
		model.setCreateTime(strStartTime);
		model.setEndTime(strEndTime);
		model.setContent(content);
		model.setState(state);
		model.setRunTime(Long.valueOf(runTime));
		timerLogService.save(model);
	}
}