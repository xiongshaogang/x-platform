package com.xplatform.base.work.schedule.service;

import java.util.Date;
import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.work.schedule.entity.ScheduleEntity;

public interface ScheduleService {

	/**
	 * 保存日程数据
	 * @author luoheng
	 * @param schedule
	 * @return
	 * @throws BusinessException
	 */
	public String save(ScheduleEntity schedule) throws BusinessException;
	
	/**
	 * 更新日程数据
	 * @author luoheng
	 * @param schedule
	 * @throws BusinessException
	 */
	public void update(ScheduleEntity schedule) throws BusinessException;
	
	/**
	 * 删除日程数据
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除日程数据
	 * @param ids
	 * @throws BusinessException
	 */
	public void batchDelete(String ids) throws BusinessException;
	
	/**
	 * 根据编码获取日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public ScheduleEntity getScheduleEntity(String id);
	
	/**
	 * 查询日程列表信息
	 * @return
	 * @throws BusinessException
	 */
	public List<ScheduleEntity> queryList(String userId) ;
	
	/**
	 * 根据日期查询日程信息
	 * @param date
	 * @return
	 */
	public List<ScheduleEntity> queryScheduleListByDate(Date date) throws BusinessException;
	
	/**
	 * 分页获取日程列表信息
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException;
}
