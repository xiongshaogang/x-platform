package com.xplatform.base.work.schedule.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.work.schedule.dao.OftenScheduleDao;
import com.xplatform.base.work.schedule.entity.OftenScheduleEntity;
import com.xplatform.base.work.schedule.service.OftenScheduleService;

@Service("oftenScheduleService")
public class OftenScheduleServiceImpl implements OftenScheduleService {
	
	private static final Logger logger = Logger.getLogger(OftenScheduleServiceImpl.class);

	@Resource
	private OftenScheduleDao oftenScheduleDao;
	
	/**
	 * 保存常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String save(OftenScheduleEntity oftenSchedule) throws BusinessException {
		String pk = "";
		try {
			pk = oftenScheduleDao.saveOftenSchedule(oftenSchedule);
			logger.error("常用日程保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("常用日程保存失败");
			throw new BusinessException("常用日程保存失败");
		}
		return pk;
	}
	
	/**
	 * 更新常用日程数据
	 * @author luoheng
	 * @param oftenSchedule
	 * @throws BusinessException
	 */
	@Override
	public void update(OftenScheduleEntity oftenSchedule) throws BusinessException {
		try {
			OftenScheduleEntity oldEntity = this.getOftenScheduleEntity(oftenSchedule.getId());
			MyBeanUtils.copyBeanNotNull2Bean(oftenSchedule, oldEntity);
			oftenScheduleDao.updateOftenSchedule(oldEntity);
			logger.error("常用日程更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("常用日程更新失败");
			throw new BusinessException("常用日程更新失败");
		}
	}
	
	/**
	 * 删除常用日程数据
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	@Override
	public void delete(String id) throws BusinessException {
		try {
			oftenScheduleDao.deleteOftenSchedule(id);
			logger.error("常用日程删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("常用日程删除失败");
			throw new BusinessException("常用日程删除失败");
		}
	}
	
	/**
	 * 批量删除常用日程数据
	 * @param ids
	 * @throws BusinessException
	 */
	@Override
	public void batchDelete(String ids) throws BusinessException {
		try {
			if(StringUtil.isNotBlank(ids)){
				for(String id : ids.split(",")){
					this.delete(id);
				}
			}
			logger.error("常用日程批量删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("常用日程批量删除失败");
			throw new BusinessException("常用日程批量删除失败");
		}
	}
	
	/**
	 * 根据编码获取常用日程信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public OftenScheduleEntity getOftenScheduleEntity(String id){
		OftenScheduleEntity oftenSchedule = null;
		oftenSchedule = this.oftenScheduleDao.getOftenScheduleEntity(id);
		return oftenSchedule;
	}
	
	/**
	 * 查询常用日程列表信息
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<OftenScheduleEntity> queryList(Map<String, String> map) {
		List<OftenScheduleEntity> oftenScheduleList=new ArrayList<OftenScheduleEntity>();
		oftenScheduleList=this.oftenScheduleDao.queryOftenScheduleList(map);
		return oftenScheduleList;
	}
	
	/**
	 * 分页获取常用日程列表信息
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.oftenScheduleDao.getDataGridReturn(cq, true);
			logger.info("常用日程获取分页列表成功");
		} catch (Exception e) {
			logger.error("常用日程获取分页列表失败");
			throw new BusinessException("常用日程获取分页列表失败");
		}
	}

	public void setOftenScheduleDao(OftenScheduleDao oftenScheduleDao) {
		this.oftenScheduleDao = oftenScheduleDao;
	}
}
