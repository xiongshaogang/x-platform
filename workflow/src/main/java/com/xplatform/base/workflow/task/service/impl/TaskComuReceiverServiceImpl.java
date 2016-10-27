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
import com.xplatform.base.workflow.task.dao.TaskComuReceiverDao;
import com.xplatform.base.workflow.task.entity.TaskComuReceiverEntity;
import com.xplatform.base.workflow.task.service.TaskComuReceiverService;
/**
 * 
 * description :任务沟通接收service实现
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
@Service("taskComuReceiverService")
public class TaskComuReceiverServiceImpl implements TaskComuReceiverService {
	private static final Logger logger = Logger.getLogger(TaskComuReceiverServiceImpl.class);
	@Resource
	private TaskComuReceiverDao taskComuReceiverDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="TaskComuReceiverManager",description="任务沟通接收新增",detail="任务沟通接收${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskComuReceiverEntity TaskComuReceiver) throws BusinessException {
		String pk="";
		try {
			pk=this.taskComuReceiverDao.addTaskComuReceiver(TaskComuReceiver);
		} catch (Exception e) {
			logger.error("任务沟通接收保存失败");
			throw new BusinessException("任务沟通接收保存失败");
		}
		logger.info("任务沟通接收保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="TaskComuReceiverManager",description="任务沟通接收删除",detail="任务沟通接收${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskComuReceiverDao.deleteTaskComuReceiver(id);
		} catch (Exception e) {
			logger.error("任务沟通接收删除失败");
			throw new BusinessException("任务沟通接收删除失败");
		}
		logger.info("任务沟通接收删除成功");
	}

	@Override
	@Action(moduleCode="TaskComuReceiverManager",description="任务沟通接收批量删除",detail="任务沟通接收${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("任务沟通接收批量删除成功");
	}

	@Override
	@Action(moduleCode="TaskComuReceiverManager",description="任务沟通接收修改",detail="任务沟通接收${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskComuReceiverEntity TaskComuReceiver) throws BusinessException {
		try {
			TaskComuReceiverEntity oldEntity = get(TaskComuReceiver.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskComuReceiver, oldEntity);
			this.taskComuReceiverDao.updateTaskComuReceiver(oldEntity);
		} catch (Exception e) {
			logger.error("任务沟通接收更新失败");
			throw new BusinessException("任务沟通接收更新失败");
		}
		logger.info("任务沟通接收更新成功");
	}

	@Override
	public TaskComuReceiverEntity get(String id) {
		TaskComuReceiverEntity TaskComuReceiver=null;
		try {
			TaskComuReceiver=this.taskComuReceiverDao.getTaskComuReceiver(id);
		} catch (Exception e) {
			logger.error("任务沟通接收获取失败");
		//	throw new BusinessException("任务沟通接收获取失败");
		}
		logger.info("任务沟通接收获取成功");
		return TaskComuReceiver;
	}

	@Override
	public List<TaskComuReceiverEntity> queryList() throws BusinessException {
		List<TaskComuReceiverEntity> TaskComuReceiverList=new ArrayList<TaskComuReceiverEntity>();
		try {
			TaskComuReceiverList=this.taskComuReceiverDao.queryTaskComuReceiverList();
		} catch (Exception e) {
			logger.error("任务沟通接收获取列表失败");
			throw new BusinessException("任务沟通接收获取列表失败");
		}
		logger.info("任务沟通接收获取列表成功");
		return TaskComuReceiverList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskComuReceiverDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务沟通接收获取分页列表失败");
			throw new BusinessException("任务沟通接收获取分页列表失败");
		}
		logger.info("任务沟通接收获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskComuReceiverEntity.class, param, propertyName);
	}
	
	
	public void setTaskComuReceiverDao(TaskComuReceiverDao taskComuReceiverDao) {
		this.taskComuReceiverDao = taskComuReceiverDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
