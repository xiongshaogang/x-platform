package com.xplatform.base.system.inform.service;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.inform.entity.MeetingEntity;

public interface MeetingService {

	void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * 根据id获取实体
	 * */
	MeetingEntity getMeetingById(String id);


	void updateMeetingEntity(MeetingEntity meeting);

	void saveMeetingEntity(MeetingEntity meeting);

	void deleteByMeetingId(String parameter);

}
