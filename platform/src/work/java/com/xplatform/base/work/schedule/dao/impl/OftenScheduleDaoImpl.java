package com.xplatform.base.work.schedule.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.work.schedule.dao.OftenScheduleDao;
import com.xplatform.base.work.schedule.entity.OftenScheduleEntity;

@Repository("oftenScheduleDao")
public class OftenScheduleDaoImpl extends CommonDao implements OftenScheduleDao {

	/**
	 * 保存常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 * @return
	 */
	@Override
	public String saveOftenSchedule(OftenScheduleEntity oftenSchedule){
		return (String) this.save(oftenSchedule);
	}
	
	/**
	 * 修改常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 */
	@Override
	public void updateOftenSchedule(OftenScheduleEntity oftenSchedule){
		this.merge(oftenSchedule);
	}
	
	/**
	 * 删除常用日程数据
	 * @author luoheng
	 * @param id
	 */
	@Override
	public void deleteOftenSchedule(String id){
		this.deleteEntityById(OftenScheduleEntity.class, id);
	}
	
	/**
	 * 根据编码获取常用日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public OftenScheduleEntity getOftenScheduleEntity(String id){
		return (OftenScheduleEntity) this.get(OftenScheduleEntity.class, id);
	}
	
	/**
	 * 查询常用日程列表信息
	 * @author luoheng
	 * @return
	 */
	@Override
	public List<OftenScheduleEntity> queryOftenScheduleList(Map<String, String> map) {
		String userId = map.get("userId");
		String sql = "from  OftenScheduleEntity where user_id = ?";
		return this.findHql(sql, new Object[]{userId});
	}
}
