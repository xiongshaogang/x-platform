package com.xplatform.base.work.schedule.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.work.schedule.dao.ScheduleDao;
import com.xplatform.base.work.schedule.entity.ScheduleEntity;

@Repository("scheduleDao")
public class ScheduleDaoImpl extends CommonDao implements ScheduleDao {

	/**
	 * 保存日程数据
	 * @author luoheng
	 * @param schedule
	 * @return
	 */
	@Override
	public String saveSchedule(ScheduleEntity schedule){
		return (String) this.save(schedule);
	}
	
	/**
	 * 修改日程数据
	 * @author luoheng
	 * @param schedule
	 */
	@Override
	public void updateSchedule(ScheduleEntity schedule){
		this.merge(schedule);
	}
	
	/**
	 * 删除日程数据
	 * @author luoheng
	 * @param id
	 */
	@Override
	public void deleteSchedule(String id){
		this.deleteEntityById(ScheduleEntity.class, id);
	}
	
	/**
	 * 根据编码获取日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public ScheduleEntity getScheduleEntity(String id){
		return (ScheduleEntity) this.get(ScheduleEntity.class, id);
	}
	
	/**
	 * 查询日程列表信息
	 * @author luoheng
	 * @return
	 */
	@Override
	public List<ScheduleEntity> queryScheduleList(String userId) {
		String sql = "from  ScheduleEntity where user_id = ?";
		return this.findHql(sql, new Object[]{userId});
	}
	
	/**
	 * 根据日期查询日程信息
	 * @param date
	 * @return
	 */
	@Override
	public List<ScheduleEntity> queryScheduleListByDate(Date date){
		String sql = "from  ScheduleEntity where start_date <= ? and end_date >= ? and state = '0'";
		return this.findHql(sql, new Object[]{date,date});
	}
	
}
