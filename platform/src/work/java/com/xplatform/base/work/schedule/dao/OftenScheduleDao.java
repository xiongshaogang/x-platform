package com.xplatform.base.work.schedule.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.work.schedule.entity.OftenScheduleEntity;

public interface OftenScheduleDao extends ICommonDao {

	/**
	 * 保存常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 * @return
	 */
	public String saveOftenSchedule(OftenScheduleEntity oftenSchedule);
	
	/**
	 * 修改常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 */
	public void updateOftenSchedule(OftenScheduleEntity oftenSchedule);
	
	/**
	 * 删除常用日程数据
	 * @author luoheng
	 * @param id
	 */
	public void deleteOftenSchedule(String id);
	
	/**
	 * 根据编码获取常用日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public OftenScheduleEntity getOftenScheduleEntity(String id);
	
	/**
	 * 查询常用日程列表信息
	 * @author luoheng
	 * @return
	 */
	public List<OftenScheduleEntity> queryOftenScheduleList(Map<String, String> map);
}
