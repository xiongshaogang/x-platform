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
import com.xplatform.base.workflow.task.dao.TaskForkDao;
import com.xplatform.base.workflow.task.entity.TaskForkEntity;
import com.xplatform.base.workflow.task.service.TaskForkService;
/**
 * 
 * description :任务分发汇总service实现
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
@Service("taskForkService")
public class TaskForkServiceImpl implements TaskForkService {
	private static final Logger logger = Logger.getLogger(TaskForkServiceImpl.class);
	@Resource
	private TaskForkDao taskForkDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="TaskForkManager",description="任务分发汇总新增",detail="任务分发汇总${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskForkEntity TaskFork) throws BusinessException {
		String pk="";
		try {
			pk=this.taskForkDao.addTaskFork(TaskFork);
		} catch (Exception e) {
			logger.error("任务分发汇总保存失败");
			throw new BusinessException("任务分发汇总保存失败");
		}
		logger.info("任务分发汇总保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="TaskForkManager",description="任务分发汇总删除",detail="任务分发汇总${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskForkDao.deleteTaskFork(id);
		} catch (Exception e) {
			logger.error("任务分发汇总删除失败");
			throw new BusinessException("任务分发汇总删除失败");
		}
		logger.info("任务分发汇总删除成功");
	}

	@Override
	@Action(moduleCode="TaskForkManager",description="任务分发汇总批量删除",detail="任务分发汇总${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("任务分发汇总批量删除成功");
	}

	@Override
	@Action(moduleCode="TaskForkManager",description="任务分发汇总修改",detail="任务分发汇总${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskForkEntity TaskFork) throws BusinessException {
		try {
			TaskForkEntity oldEntity = get(TaskFork.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskFork, oldEntity);
			this.taskForkDao.updateTaskFork(oldEntity);
		} catch (Exception e) {
			logger.error("任务分发汇总更新失败");
			throw new BusinessException("任务分发汇总更新失败");
		}
		logger.info("任务分发汇总更新成功");
	}

	@Override
	public TaskForkEntity get(String id) {
		TaskForkEntity TaskFork=null;
		try {
			TaskFork=this.taskForkDao.getTaskFork(id);
		} catch (Exception e) {
			logger.error("任务分发汇总获取失败");
			//throw new BusinessException("任务分发汇总获取失败");
		}
		logger.info("任务分发汇总获取成功");
		return TaskFork;
	}

	@Override
	public List<TaskForkEntity> queryList() throws BusinessException {
		List<TaskForkEntity> TaskForkList=new ArrayList<TaskForkEntity>();
		try {
			TaskForkList=this.taskForkDao.queryTaskForkList();
		} catch (Exception e) {
			logger.error("任务分发汇总获取列表失败");
			throw new BusinessException("任务分发汇总获取列表失败");
		}
		logger.info("任务分发汇总获取列表成功");
		return TaskForkList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskForkDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务分发汇总获取分页列表失败");
			throw new BusinessException("任务分发汇总获取分页列表失败");
		}
		logger.info("任务分发汇总获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskForkEntity.class, param, propertyName);
	}
	
	public void setTaskForkDao(TaskForkDao taskForkDao) {
		this.taskForkDao = taskForkDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
