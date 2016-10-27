package com.xplatform.base.work.schedule.dao;

import java.util.Date;
import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.work.schedule.entity.ScheduleEntity;

public interface ScheduleDao extends ICommonDao {

	/**
	 * 保存日程数据
	 * @author luoheng
	 * @param schedule
	 * @return
	 */
	public String saveSchedule(ScheduleEntity schedule);
	
	/**
	 * 修改日程数据
	 * @author luoheng
	 * @param schedule
	 */
	public void updateSchedule(ScheduleEntity schedule);
	
	/**
	 * 删除日程数据
	 * @author luoheng
	 * @param id
	 */
	public void deleteSchedule(String id);
	
	/**
	 * 根据编码获取日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public ScheduleEntity getScheduleEntity(String id);
	
	/**
	 * 查询日程列表信息
	 * @author luoheng
	 * @return
	 */
	public List< ScheduleEntity> queryScheduleList(String userId);
	
	/**
	 * 根据日期查询日程信息
	 * @param date
	 * @return
	 */
	public List<ScheduleEntity> queryScheduleListByDate(Date date);
}
