package com.xplatform.base.system.timer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.xplatform.base.framework.core.common.exception.BusinessException;

/**
 * 
 * description :定时器管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月17日 下午2:15:39
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月17日 下午2:15:39
 *
 */
public interface TimerService {

	
	public void addJob(String jobName, String className,String parameterJson, String description)throws BusinessException;

	public void addJob(String jobName, Class cls, Map parameterMap,String description) throws BusinessException ;

	public List<JobDetail> getJobList() throws BusinessException ;

	public List<Trigger> getTriggersByJob(String jobName) throws BusinessException ;

	public HashMap<String, Trigger.TriggerState> getTriggerStatus(List<Trigger> list) throws BusinessException;

	public void addTrigger(String jobName, String trigName, String planJson) throws BusinessException;
	
	public void addTrigger(String jobName, String trigName, int minute)throws BusinessException ;

	public void delJob(String jobName) throws BusinessException ;
    
	public boolean isJobExists(String name);
	
	public boolean isTriggerExists(String trigName);
	
	public Trigger getTrigger(String triggerName) throws BusinessException ;

	public void delTrigger(String triggerName) throws BusinessException ;

	public void updateTriggerRun(String triggerName) throws BusinessException;

	public void executeJob(String jobName) throws BusinessException ;
	

}