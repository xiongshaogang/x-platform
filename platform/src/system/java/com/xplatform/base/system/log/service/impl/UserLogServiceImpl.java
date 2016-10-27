package com.xplatform.base.system.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.io.Serializable;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.log.dao.UserLogDao;
import com.xplatform.base.system.log.entity.UserLogEntity;
import com.xplatform.base.system.log.service.UserLogService;



@Service("userLogService")
public class UserLogServiceImpl implements UserLogService {

    private static final Logger logger = Logger.getLogger(UserLogServiceImpl.class);

    @Resource
	private UserLogDao userLogDao;
	
	@Resource
	private BaseService baseService;

	public void setUserLogDao(UserLogDao userLogDao) {
		this.userLogDao = userLogDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
 	@Override
	public String save(UserLogEntity userLog) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.userLogDao.addUserLog(userLog);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("用户日志保存失败");
		}
		logger.info("用户日志保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.userLogDao.deleteUserLog(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("用户日志删除失败");
		}
		logger.info("用户日志删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("用户日志批量删除成功");
	}

	@Override
	public void update(UserLogEntity userLog) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.userLogDao.updateUserLog(userLog);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("用户日志更新失败");
		}
		logger.info("用户日志更新成功");
	}

	@Override
	public UserLogEntity get(String id){
		// TODO Auto-generated method stub
		UserLogEntity userLog=null;
		try {
			userLog=this.userLogDao.getUserLog(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("用户日志获取失败");
		}
		logger.info("用户日志获取成功");
		return userLog;
	}

	@Override
	public List<UserLogEntity> queryList(){
		// TODO Auto-generated method stub
		List<UserLogEntity> userLogList=new ArrayList<UserLogEntity>();
		try {
			userLogList=this.userLogDao.queryUserLogList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("用户日志获取列表失败");
		}
		logger.info("用户日志获取列表成功");
		return userLogList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.userLogDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("用户日志获取分页列表失败");
		}
		logger.info("用户日志获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(UserLogEntity.class, param, propertyName);
	}
}