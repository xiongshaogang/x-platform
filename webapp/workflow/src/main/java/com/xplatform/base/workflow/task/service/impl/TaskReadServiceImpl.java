package com.xplatform.base.workflow.task.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.xplatform.base.workflow.task.dao.TaskReadDao;
import com.xplatform.base.workflow.task.entity.TaskReadEntity;
import com.xplatform.base.workflow.task.service.TaskReadService;
/**
 * 
 * description :任务已读service实现
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
@Service("taskReadService")
public class TaskReadServiceImpl implements TaskReadService {
	private static final Logger logger = Logger.getLogger(TaskReadServiceImpl.class);
	@Resource
	private TaskReadDao taskReadDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="TaskReadManager",description="任务已读新增",detail="任务已读${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskReadEntity TaskRead) throws BusinessException {
		String pk="";
		try {
			pk=this.taskReadDao.addTaskRead(TaskRead);
		} catch (Exception e) {
			logger.error("任务已读保存失败");
			throw new BusinessException("任务已读保存失败");
		}
		logger.info("任务已读保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="TaskReadManager",description="任务已读删除",detail="任务已读${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskReadDao.deleteTaskRead(id);
		} catch (Exception e) {
			logger.error("任务已读删除失败");
			throw new BusinessException("任务已读删除失败");
		}
		logger.info("任务已读删除成功");
	}

	@Override
	@Action(moduleCode="TaskReadManager",description="任务已读批量删除",detail="任务已读${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("任务已读批量删除成功");
	}

	@Override
	@Action(moduleCode="TaskReadManager",description="任务已读修改",detail="任务已读${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskReadEntity TaskRead) throws BusinessException {
		try {
			TaskReadEntity oldEntity = get(TaskRead.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskRead, oldEntity);
			this.taskReadDao.updateTaskRead(oldEntity);
		} catch (Exception e) {
			logger.error("任务已读更新失败");
			throw new BusinessException("任务已读更新失败");
		}
		logger.info("任务已读更新成功");
	}

	@Override
	public TaskReadEntity get(String id){
		TaskReadEntity TaskRead=null;
		try {
			TaskRead=this.taskReadDao.getTaskRead(id);
		} catch (Exception e) {
			logger.error("任务已读获取失败");
			//throw new BusinessException("任务已读获取失败");
		}
		logger.info("任务已读获取成功");
		return TaskRead;
	}

	@Override
	public List<TaskReadEntity> queryList() throws BusinessException {
		List<TaskReadEntity> TaskReadList=new ArrayList<TaskReadEntity>();
		try {
			TaskReadList=this.taskReadDao.queryTaskReadList();
		} catch (Exception e) {
			logger.error("任务已读获取列表失败");
			throw new BusinessException("任务已读获取列表失败");
		}
		logger.info("任务已读获取列表成功");
		return TaskReadList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskReadDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务已读获取分页列表失败");
			throw new BusinessException("任务已读获取分页列表失败");
		}
		logger.info("任务已读获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskReadEntity.class, param, propertyName);
	}
	
	public void setTaskReadDao(TaskReadDao taskReadDao) {
		this.taskReadDao = taskReadDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public List<TaskReadEntity> getTaskRead(String instActId, String taskId) {
		// TODO Auto-generated method stub
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("actInstId", instActId);
		param.put("taskId", taskId);
		return this.taskReadDao.findByPropertys(TaskReadEntity.class, param);
	}
}
