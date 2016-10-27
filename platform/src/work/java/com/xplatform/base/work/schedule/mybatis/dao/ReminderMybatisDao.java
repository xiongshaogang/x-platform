package com.xplatform.base.work.schedule.mybatis.dao;

import java.util.List;
import java.util.Map;

public interface ReminderMybatisDao {

	List<Map<String, Object>> queryDateReminderList(Map<String, Object> param);

	List<Map<String, Object>> queryMeetingInform(Map<String, Object> param);

	List<Map<String, Object>> queryTrainInform(Map<String, Object> param);

	List<Map<String, Object>> queryAnnApprove(Map<String, Object> param);

	List<Map<String, Object>> queryAnnunciateInform(Map<String, Object> param);

}
