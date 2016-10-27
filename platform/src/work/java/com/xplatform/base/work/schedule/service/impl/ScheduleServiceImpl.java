package com.xplatform.base.work.schedule.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.work.schedule.dao.ScheduleDao;
import com.xplatform.base.work.schedule.entity.ScheduleEntity;
import com.xplatform.base.work.schedule.service.ScheduleService;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	private static final Logger logger = Logger.getLogger(ScheduleServiceImpl.class);
	
	@Resource
	private ScheduleDao scheduleDao;
	
	/**
	 * 保存日程数据
	 * @author luoheng
	 * @param schedule
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Action(moduleCode="ScheduleManagement",description="日程新增",detail="日程${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ScheduleEntity schedule) throws BusinessException {
		String pk = "";
		try {
			pk = scheduleDao.saveSchedule(schedule);
			logger.error("日程保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("日程保存失败");
			throw new BusinessException("日程保存失败");
		}
		return pk;
	}
	
	/**
	 * 更新日程数据
	 * @author luoheng
	 * @param schedule
	 * @throws BusinessException
	 */
	@Override
	@Action(moduleCode="ScheduleManagement",description="日程修改",detail="日程${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ScheduleEntity schedule) throws BusinessException {
		try {
			ScheduleEntity oldEntity = this.getScheduleEntity(schedule.getId());
			MyBeanUtils.copyBeanNotNull2Bean(schedule, oldEntity);
			scheduleDao.updateSchedule(oldEntity);
			logger.error("日程更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("日程更新失败");
			throw new BusinessException("日程更新失败");
		}
	}
	
	/**
	 * 删除日程数据
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	@Override
	@Action(moduleCode="ScheduleManagement",description="日程删除",detail="日程${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			scheduleDao.deleteSchedule(id);
			logger.error("日程删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("日程删除失败");
			throw new BusinessException("日程删除失败");
		}
	}
	
	/**
	 * 批量删除日程数据
	 * @param ids
	 * @throws BusinessException
	 */
	@Override
	@Action(moduleCode="ScheduleManagement",description="日程批量删除",detail="日程${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws BusinessException {
		try {
			if(StringUtil.isNotBlank(ids)){
				for(String id : ids.split(",")){
					this.delete(id);
				}
			}
			logger.error("日程批量删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("日程批量删除失败");
			throw new BusinessException("日程批量删除失败");
		}
	}
	
	/**
	 * 根据编码获取日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public ScheduleEntity getScheduleEntity(String id){
		ScheduleEntity schedule = null;
		schedule = this.scheduleDao.getScheduleEntity(id);
		return schedule;
	}
	
	/**
	 * 查询日程列表信息
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<ScheduleEntity> queryList(String userId) {
		List<ScheduleEntity> scheduleList=new ArrayList<ScheduleEntity>();
		scheduleList=this.scheduleDao.queryScheduleList(userId);
		return scheduleList;
	}
	
	/**
	 * 根据日期查询日程信息
	 * @param date
	 * @return
	 */
	@Override
	public List<ScheduleEntity> queryScheduleListByDate(Date date) throws BusinessException {
		List<ScheduleEntity> scheduleList=new ArrayList<ScheduleEntity>();
		try {
			scheduleList=this.scheduleDao.queryScheduleListByDate(date);
			logger.info("日程获取列表成功");
		} catch (Exception e) {
			logger.error("日程获取列表失败");
			throw new BusinessException("日程获取列表失败");
		}
		return scheduleList;
	}
	
	/**
	 * 分页获取日程列表信息
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.scheduleDao.getDataGridReturn(cq, true);
			logger.info("日程获取分页列表成功");
		} catch (Exception e) {
			logger.error("日程获取分页列表失败");
			throw new BusinessException("日程获取分页列表失败");
		}
	}

	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}
	
}
