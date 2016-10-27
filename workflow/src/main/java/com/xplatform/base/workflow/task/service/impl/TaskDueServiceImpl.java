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
import com.xplatform.base.workflow.task.dao.TaskDueDao;
import com.xplatform.base.workflow.task.entity.TaskDueEntity;
import com.xplatform.base.workflow.task.service.TaskDueService;
/**
 * 
 * description :任务催办service实现
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
@Service("taskDueService")
public class TaskDueServiceImpl implements TaskDueService {
	private static final Logger logger = Logger.getLogger(TaskDueServiceImpl.class);
	@Resource
	private TaskDueDao taskDueDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="TaskDueManager",description="任务催办新增",detail="任务催办${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskDueEntity TaskDue) throws BusinessException {
		String pk="";
		try {
			pk=this.taskDueDao.addTaskDue(TaskDue);
		} catch (Exception e) {
			logger.error("任务催办保存失败");
			throw new BusinessException("任务催办保存失败");
		}
		logger.info("任务催办保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="TaskDueManager",description="任务催办删除",detail="任务催办${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskDueDao.deleteTaskDue(id);
		} catch (Exception e) {
			logger.error("任务催办删除失败");
			throw new BusinessException("任务催办删除失败");
		}
		logger.info("任务催办删除成功");
	}

	@Override
	@Action(moduleCode="TaskDueManager",description="任务催办批量删除",detail="任务催办${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("任务催办批量删除成功");
	}

	@Override
	@Action(moduleCode="TaskDueManager",description="任务催办修改",detail="任务催办${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskDueEntity TaskDue) throws BusinessException {
		try {
			TaskDueEntity oldEntity = get(TaskDue.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskDue, oldEntity);
			this.taskDueDao.updateTaskDue(oldEntity);
		} catch (Exception e) {
			logger.error("任务催办更新失败");
			throw new BusinessException("任务催办更新失败");
		}
		logger.info("任务催办更新成功");
	}

	@Override
	public TaskDueEntity get(String id){
		TaskDueEntity TaskDue=null;
		try {
			TaskDue=this.taskDueDao.getTaskDue(id);
		} catch (Exception e) {
			logger.error("任务催办获取失败");
			//throw new BusinessException("任务催办获取失败");
		}
		logger.info("任务催办获取成功");
		return TaskDue;
	}

	@Override
	public List<TaskDueEntity> queryList() throws BusinessException {
		List<TaskDueEntity> TaskDueList=new ArrayList<TaskDueEntity>();
		try {
			TaskDueList=this.taskDueDao.queryTaskDueList();
		} catch (Exception e) {
			logger.error("任务催办获取列表失败");
			throw new BusinessException("任务催办获取列表失败");
		}
		logger.info("任务催办获取列表成功");
		return TaskDueList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskDueDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务催办获取分页列表失败");
			throw new BusinessException("任务催办获取分页列表失败");
		}
		logger.info("任务催办获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskDueEntity.class, param, propertyName);
	}
	
	public List<TaskDueEntity> getByActDefAndNodeId(String actDefId,String nodeId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("actDefId", actDefId);
		param.put("nodeId", nodeId);
		return this.taskDueDao.findByPropertys(TaskDueEntity.class, param);
	}
	
	public void settaskDueDao(TaskDueDao taskDueDao) {
		this.taskDueDao = taskDueDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public List<TaskDueEntity> getByActDefId(String actDefId) {
		// TODO Auto-generated method stub
		return this.taskDueDao.findByProperty(TaskDueEntity.class, "actDefId", actDefId);
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.taskDueDao.executeHql("delete from TaskDueEntity t where t.actDefId='"+actDefId+"'");
	}
}
