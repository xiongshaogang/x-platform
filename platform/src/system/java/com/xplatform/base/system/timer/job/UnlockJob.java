package com.xplatform.base.system.timer.job;

import java.util.List;

import org.quartz.JobExecutionContext;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserService;

public class UnlockJob extends BaseJob{

	private UserService userService = ApplicationContextUtil.getBean("userService");
	@Override
	public void executeJob(JobExecutionContext paramJobExecutionContext)
			throws Exception {
		// TODO 这里需要修改
//		List<UserEntity> list = this.userService.findByPropertys("locked", "Y");
		List<UserEntity> list = null;
		for(UserEntity user : list){
			user.setLoginErrorTimes(0);
			user.setFlag("1");
			this.userService.update(user);
		}
	}

}
