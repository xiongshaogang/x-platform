package com.xplatform.base.system.timer.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.system.timer.bean.ParameterObj;
import com.xplatform.base.system.timer.bean.PlanObject;
import com.xplatform.base.system.timer.service.TimerService;
import com.xplatform.base.system.timer.util.TimeUtil;

/**
 * 
 * description :定时器管理service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月17日 下午1:55:17
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月17日 下午1:55:17
 *
 */
@Service
public class TimerServiceImpl implements TimerService{
	Scheduler scheduler;//在spring中已经注入了
	private static HashMap<String, String> mapWeek = new HashMap();
	private static final String schedGroup = "group1";
	private static final Logger logger = Logger.getLogger(TimerServiceImpl.class);

	//@Autowired
	public void setScheduler(Scheduler s) {
		this.scheduler = s;
	}

	public void addJob(String jobName, String className,
			String parameterJson, String description)
			throws BusinessException{
		logger.info("添加定时器开始");
		if (this.scheduler == null){
			throw new BusinessException("scheduler 没有配置!");
		}
		try {
			Class cls = Class.forName(className);
			JobBuilder jb = JobBuilder.newJob(cls);
			jb.withIdentity(jobName, "group1");
			if (StringUtil.isNotEmpty(parameterJson)) {
				setJobMap(parameterJson, jb);
			}
			jb.storeDurably();
			jb.withDescription(description);
			JobDetail jobDetail = jb.build();
			this.scheduler.addJob(jobDetail, true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("添加定时器失败");
			throw new BusinessException("添加定时器失败!");
		}
		logger.info("添加定时器成功");
	}

	

	public void addJob(String jobName, Class cls, Map parameterMap,
			String description) throws BusinessException {
		if (this.scheduler == null){
			throw new BusinessException("scheduler 没有配置!");
		}
		
		try {
			JobBuilder jb = JobBuilder.newJob(cls);
			jb.withIdentity(jobName, "group1");
			if (parameterMap != null) {
				JobDataMap map = new JobDataMap();
				map.putAll(parameterMap);
				jb.usingJobData(map);
			}
			jb.storeDurably();
			jb.withDescription(description);
			JobDetail jobDetail = jb.build();
			this.scheduler.addJob(jobDetail, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("添加任务失败!");
		}
	}

	public boolean isJobExists(String jobName) {
		if (this.scheduler == null)
			return false;
		JobKey key = new JobKey(jobName, "group1");
		try {
			return this.scheduler.checkExists(key);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public List<JobDetail> getJobList() throws BusinessException {
		if (this.scheduler == null) {
			throw new BusinessException("scheduler 没有配置!");
		}
		List list = new ArrayList();
		try {
			GroupMatcher matcher = GroupMatcher.groupEquals("group1");
			Set<JobKey> set = this.scheduler.getJobKeys(matcher);
			for (JobKey jobKey : set) {
				JobDetail detail = this.scheduler.getJobDetail(jobKey);
				list.add(detail);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("scheduler 没有配置!");
		}
		
		return list;
	}

	public List<Trigger> getTriggersByJob(String jobName) throws BusinessException {
		if (this.scheduler == null) {
			return new ArrayList<Trigger>();
		}
		JobKey key = new JobKey(jobName, "group1");
		List<Trigger> list=new ArrayList<Trigger>();
		try {
			list=(List<Trigger>) this.scheduler.getTriggersOfJob(key);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("通过jobName获取Triggers方法发生错误");
		}
		return list;
	}

	public HashMap<String, Trigger.TriggerState> getTriggerStatus(
			List<Trigger> list) throws BusinessException {
		if (this.scheduler == null) {
			return new HashMap();
		}
		HashMap map = new HashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Trigger trigger = (Trigger) it.next();
			TriggerKey key = trigger.getKey();
			Trigger.TriggerState state;
			try {
				state = this.scheduler.getTriggerState(key);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BusinessException("获取定时器状态失败");
			}
			map.put(key.getName(), state);
		}
		return map;
	}

	public boolean isTriggerExists(String trigName) {
		if (this.scheduler == null) {
			return false;
		}
		TriggerKey triggerKey = new TriggerKey(trigName, "group1");
		try {
			return this.scheduler.checkExists(triggerKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void addTrigger(String jobName, String trigName, String planJson)
			throws BusinessException {
		if (this.scheduler == null) {
			return;
		}
		JobKey jobKey = new JobKey(jobName, "group1");
		TriggerBuilder tb = TriggerBuilder.newTrigger();
		tb.withIdentity(trigName, "group1");

		setTrigBuilder(planJson, tb);
		tb.forJob(jobKey);
		Trigger trig = tb.build();
		try {
			this.scheduler.scheduleJob(trig);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BusinessException("添加定时器失败");
		}
		
	}

	public void addTrigger(String jobName, String trigName, int minute)
			throws BusinessException {
		if (this.scheduler == null) {
			return;
		}
		JobKey jobKey = new JobKey(jobName, "group1");
		TriggerBuilder tb = TriggerBuilder.newTrigger();
		tb.withIdentity(trigName, "group1");
		ScheduleBuilder sb = CalendarIntervalScheduleBuilder
				.calendarIntervalSchedule().withIntervalInMinutes(minute);
		tb.startNow();
		tb.withSchedule(sb);
		tb.withDescription("每:" + minute + "分钟执行!");

		tb.forJob(jobKey);
		Trigger trig = tb.build();
		try {
			this.scheduler.scheduleJob(trig);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("添加定时器失败");
		}
		
	}

	private void setTrigBuilder(String planJson, TriggerBuilder<Trigger> tb)
			throws BusinessException {
		JSONObject jsonObject = JSONObject.fromObject(planJson);

		PlanObject planObject = (PlanObject) JSONObject.toBean(jsonObject,
				PlanObject.class);
		int type = planObject.getType();
		String value = planObject.getTimeInterval();
		switch (type) {
		case 1:
			Date date = TimeUtil.convertString(value);
			tb.startAt(date);
			tb.withDescription("执行一次,执行时间:" + date.toLocaleString());
			break;
		case 2:
			int minute = Integer.parseInt(value);
			ScheduleBuilder sb = CalendarIntervalScheduleBuilder
					.calendarIntervalSchedule().withIntervalInMinutes(minute);

			tb.startNow();
			tb.withSchedule(sb);
			tb.withDescription("每:" + minute + "分钟执行!");
			break;
		case 3:
			String[] aryTime = value.split(":");
			int hour = Integer.parseInt(aryTime[0]);
			int m = Integer.parseInt(aryTime[1]);
			ScheduleBuilder sb1 = CronScheduleBuilder.dailyAtHourAndMinute(
					hour, m);
			tb.startNow();
			tb.withSchedule(sb1);
			tb.withDescription("每天：" + hour + ":" + m + "执行!");
			break;
		case 4:
			String[] aryExpression = value.split("[|]");
			String week = aryExpression[0];
			String[] aryTime1 = aryExpression[1].split(":");
			String h1 = aryTime1[0];
			String m1 = aryTime1[1];
			String cronExperssion = "0 " + m1 + " " + h1 + " ? * " + week;
			ScheduleBuilder sb4 = CronScheduleBuilder
					.cronSchedule(cronExperssion);
			tb.startNow();
			tb.withSchedule(sb4);
			String weekName = getWeek(week);
			tb.withDescription("每周：" + weekName + "," + h1 + ":" + m1 + "执行!");
			break;
		case 5:
			String[] aryExpression5 = value.split("[|]");
			String day = aryExpression5[0];
			String[] aryTime2 = aryExpression5[1].split(":");
			String h2 = aryTime2[0];
			String m2 = aryTime2[1];
			String cronExperssion1 = "0 " + m2 + " " + h2 + " " + day + " * ?";
			ScheduleBuilder sb5 = CronScheduleBuilder
					.cronSchedule(cronExperssion1);
			tb.startNow();
			tb.withSchedule(sb5);
			String dayName = getDay(day);
			tb.withDescription("每月:" + dayName + "," + h2 + ":" + m2 + "执行!");
			break;
		case 6:
			ScheduleBuilder sb6 = CronScheduleBuilder.cronSchedule(value);
			tb.startNow();
			tb.withSchedule(sb6);
			tb.withDescription("CronTrigger表达式:" + value);
		}
	}

	private String getDay(String day) {
		String[] aryDay = day.split(",");
		int len = aryDay.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			String tmp = aryDay[i];
			tmp = tmp.equals("L") ? "最后一天" : tmp;
			if (i < len - 1) {
				str = str + tmp + ",";
			} else {
				str = str + tmp;
			}
		}
		return str;
	}

	private String getWeek(String week) {
		String[] aryWeek = week.split(",");
		int len = aryWeek.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			if (i < len - 1)
				str = str + (String) mapWeek.get(aryWeek[i]) + ",";
			else
				str = str + (String) mapWeek.get(aryWeek[i]);
		}
		return str;
	}

	private void setJobMap(String json, JobBuilder jb) {
		JSONArray aryJson = JSONArray.fromObject(json);
		ParameterObj[] list = (ParameterObj[]) (ParameterObj[]) JSONArray
				.toArray(aryJson, ParameterObj.class);
		for (int i = 0; i < list.length; i++) {
			ParameterObj obj = list[0];
			String type = obj.getType();
			String name = obj.getName();
			String value = obj.getValue();
			if (type.equals("int")) {
				jb.usingJobData(name, Integer.valueOf(Integer.parseInt(value)));
			} else if (type.equals("long")) {
				jb.usingJobData(name, Long.valueOf(Long.parseLong(value)));
			} else if (type.equals("float")) {
				jb.usingJobData(name, Float.valueOf(Float.parseFloat(value)));
			} else if (type.equals("boolean")) {
				jb.usingJobData(name, Boolean.valueOf(Boolean
						.parseBoolean(value)));
			} else
				jb.usingJobData(name, value);
		}
	}

	public void delJob(String jobName) throws BusinessException {
		
		if (this.scheduler == null) {
			return;
		}
		JobKey key = new JobKey(jobName, "group1");
		try {
			this.scheduler.deleteJob(key);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("删除定时器失败");
		}
		
	}

	public Trigger getTrigger(String triggerName) throws BusinessException {
		if (this.scheduler == null) {
			return null;
		}
		TriggerKey key = new TriggerKey(triggerName, "group1");
		Trigger trigger = null;
		try {
			trigger = this.scheduler.getTrigger(key);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("计划任务获取失败");
		}
		return trigger;
	}

	public void delTrigger(String triggerName) throws BusinessException {
		if (this.scheduler == null) {
			return;
		}
		TriggerKey key = new TriggerKey(triggerName, "group1");
		try {
			this.scheduler.unscheduleJob(key);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("计划任务删除失败");
		}
		
	}

	public void updateTriggerRun(String triggerName) throws BusinessException {
		if (this.scheduler == null) {
			return;
		}
		TriggerKey key = new TriggerKey(triggerName, "group1");
		try {
			Trigger.TriggerState state = this.scheduler.getTriggerState(key);
			if (state == Trigger.TriggerState.PAUSED) {
				this.scheduler.resumeTrigger(key);
			} else if (state == Trigger.TriggerState.NORMAL){
				this.scheduler.pauseTrigger(key);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("任务强制执行失败");
		}
		
	}

	public void executeJob(String jobName) throws BusinessException {
		if (this.scheduler == null) {
			return;
		}
		JobKey key = new JobKey(jobName, "group1");
		try {
			this.scheduler.triggerJob(key);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("任务执行失败");
		}
		
	}

	static {
		mapWeek.put("MON", "星期一");
		mapWeek.put("TUE", "星期二");
		mapWeek.put("WED", "星期三");
		mapWeek.put("THU", "星期四");
		mapWeek.put("FRI", "星期五");
		mapWeek.put("SAT", "星期六");
		mapWeek.put("SUN", "星期日");
	}
}