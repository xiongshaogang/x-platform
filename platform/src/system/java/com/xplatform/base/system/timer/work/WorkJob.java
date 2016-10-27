package com.xplatform.base.system.timer.work;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.PropertiesUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.timer.job.BaseJob;
import com.xplatform.base.work.schedule.entity.ScheduleEntity;
import com.xplatform.base.work.schedule.service.ScheduleService;

public class WorkJob extends BaseJob {
	protected Logger logger = LoggerFactory.getLogger(WorkJob.class);
	
	@Override
	public void executeJob(JobExecutionContext paramJobExecutionContext) throws Exception {
		try {
			
			ScheduleService scheduleService = ApplicationContextUtil.getBean("scheduleService");
			
			MessageService messageService = ApplicationContextUtil.getBean("messageService");
			
			UserService userService = ApplicationContextUtil.getBean("userService");
			
			List<ScheduleEntity> scheduleList = scheduleService.queryScheduleListByDate(new Date());
			
			for (ScheduleEntity schedule : scheduleList) {
				String a = null;
				Map<String, String> map = new HashMap<String, String>();
				map.put("typeId", schedule.getId());
				map.put("messageType", "schedule");
				Date sendDate = messageService.queryMsgSendMaxDate(map);
				if (sendDate == null || DateUtils.betweenDay(sendDate, new Date()) > 1) {// 当日程管理大于一天时，只有当上次日程提醒隔当前时间大于一天时才再次发送
					UserEntity user = userService.get(schedule.getUserId());
					MessageGroupSendEntity messageSend = new MessageGroupSendEntity();
					// messageSend.setId(UUIDGenerator.generate());
					// messageSend.setMessageType("schedule");
					// messageSend.setSendType(schedule.getScheduleType());
					// /*messageSend.setMailConfigId(mailConfig.getId());*/
					// messageSend.setTitle(schedule.getTitle());
					// messageSend.setReceiveIds(user.getId());
					// messageSend.setTypeId(schedule.getId());
					// if(schedule.getScheduleType() != null &&
					// schedule.getScheduleType().indexOf("innerMessage") !=
					// -1){
					// messageSend.setInnerStatus("0");
					// messageSend.setInnerContent(schedule.getContext());
					// }
					// if(schedule.getScheduleType() != null &&
					// schedule.getScheduleType().indexOf("sms") != -1){
					// messageSend.setSmsStatus("0");
					// messageSend.setSmsContent(schedule.getContext());
					// }
					// if(schedule.getScheduleType() != null &&
					// schedule.getScheduleType().indexOf("email") != -1){
					// messageSend.setMailStatus("0");
					// messageSend.setMailContent(schedule.getContext());
					// }
					// if(DateUtils.betweenDay(new Date(),
					// schedule.getEndDate()) < 1){//如果日程结束时间
					// 间隔现在时间小于一天即设置为该日程提醒已完成（默认一天提醒一次）；
					// schedule.setState("1");
					// scheduleService.update(schedule);
					// }
					// messageService.saveMsgSend(messageSend);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
