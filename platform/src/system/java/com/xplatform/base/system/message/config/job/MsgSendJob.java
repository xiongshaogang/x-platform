package com.xplatform.base.system.message.config.job;

import java.util.List;

import org.quartz.JobExecutionContext;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.config.util.MsgUtil;
import com.xplatform.base.system.timer.job.BaseJob;

public class MsgSendJob extends BaseJob {
	private MessageService messageService = ApplicationContextUtil.getBean("messageService");

	@Override
	public void executeJob(JobExecutionContext paramJobExecutionContext) throws Exception {
		// TODO Auto-generated method stub
		List<MessageGroupSendEntity> msgSendList = this.messageService.queryMsgSendList();

		for (MessageGroupSendEntity msgSend : msgSendList) {
			MsgUtil.SendMulMeassage(msgSend,null);
		}
	}

}
