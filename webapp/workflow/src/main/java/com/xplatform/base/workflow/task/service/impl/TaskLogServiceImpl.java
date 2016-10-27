package com.xplatform.base.workflow.task.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.workflow.task.dao.TaskLogDao;
import com.xplatform.base.workflow.task.entity.TaskLogEntity;
import com.xplatform.base.workflow.task.service.TaskLogService;
/**
 * 
 * description :任务日志service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("taskLogService")
public class TaskLogServiceImpl implements TaskLogService {
	private static final Logger logger = Logger.getLogger(TaskLogServiceImpl.class);
	@Resource
	private TaskLogDao taskLogDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="TaskLogManager",description="任务日志新增",detail="任务日志${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskLogEntity TaskLog) throws BusinessException {
		String pk="";
		try {
			pk=this.taskLogDao.addTaskLog(TaskLog);
		} catch (Exception e) {
			logger.error("任务日志保存失败");
			throw new BusinessException("任务日志保存失败");
		}
		logger.info("任务日志保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="TaskLogManager",description="任务日志删除",detail="任务日志${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskLogDao.deleteTaskLog(id);
		} catch (Exception e) {
			logger.error("任务日志删除失败");
			throw new BusinessException("任务日志删除失败");
		}
		logger.info("任务日志删除成功");
	}

	@Override
	@Action(moduleCode="TaskLogManager",description="任务日志批量删除",detail="任务日志${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("任务日志批量删除成功");
	}

	@Override
	@Action(moduleCode="TaskLogManager",description="任务日志修改",detail="任务日志${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskLogEntity TaskLog) throws BusinessException {
		try {
			TaskLogEntity oldEntity = get(TaskLog.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskLog, oldEntity);
			this.taskLogDao.updateTaskLog(oldEntity);
		} catch (Exception e) {
			logger.error("任务日志更新失败");
			throw new BusinessException("任务日志更新失败");
		}
		logger.info("任务日志更新成功");
	}

	@Override
	public TaskLogEntity get(String id){
		TaskLogEntity TaskLog=null;
		try {
			TaskLog=this.taskLogDao.getTaskLog(id);
		} catch (Exception e) {
			logger.error("任务日志获取失败");
			//throw new BusinessException("任务日志获取失败");
		}
		logger.info("任务日志获取成功");
		return TaskLog;
	}

	@Override
	public List<TaskLogEntity> queryList() throws BusinessException {
		List<TaskLogEntity> TaskLogList=new ArrayList<TaskLogEntity>();
		try {
			TaskLogList=this.taskLogDao.queryTaskLogList();
		} catch (Exception e) {
			logger.error("任务日志获取列表失败");
			throw new BusinessException("任务日志获取列表失败");
		}
		logger.info("任务日志获取列表成功");
		return TaskLogList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskLogDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务日志获取分页列表失败");
			throw new BusinessException("任务日志获取分页列表失败");
		}
		logger.info("任务日志获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskLogEntity.class, param, propertyName);
	}
	
	public void setTaskLogDao(TaskLogDao taskLogDao) {
		this.taskLogDao = taskLogDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
