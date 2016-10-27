package com.xplatform.base.work.schedule.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.work.schedule.entity.OftenScheduleEntity;

public interface OftenScheduleService {

	/**
	 * 保存常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 * @return
	 * @throws BusinessException
	 */
	public String save(OftenScheduleEntity oftenSchedule) throws BusinessException;
	
	/**
	 * 更新常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 * @throws BusinessException
	 */
	public void update(OftenScheduleEntity oftenSchedule) throws BusinessException;
	
	/**
	 * 删除常用日程数据
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除常用日程数据
	 * @param ids
	 * @throws BusinessException
	 */
	public void batchDelete(String ids) throws BusinessException;
	
	/**
	 * 根据编码获取常用日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public OftenScheduleEntity getOftenScheduleEntity(String id);
	
	/**
	 * 查询常用日程列表信息
	 * @return
	 * @throws BusinessException
	 */
	public List<OftenScheduleEntity> queryList(Map<String, String> map);
	
	/**
	 * 分页获取常用日程列表信息
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException;
}
