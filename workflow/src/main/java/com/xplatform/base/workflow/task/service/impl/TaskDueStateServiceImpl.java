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
import com.xplatform.base.workflow.task.dao.TaskDueStateDao;
import com.xplatform.base.workflow.task.entity.TaskDueStateEntity;
import com.xplatform.base.workflow.task.service.TaskDueStateService;

/**
 * 
 * description :任务催办状态service实现
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
@Service("taskDueStateService")
public class TaskDueStateServiceImpl implements TaskDueStateService {
	private static final Logger logger = Logger.getLogger(TaskDueStateServiceImpl.class);
	@Resource
	private TaskDueStateDao taskDueStateDao;

	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode = "TaskDueStateManager", description = "任务催办状态新增", detail = "任务催办状态${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskDueStateEntity TaskDueState) throws BusinessException {
		String pk = "";
		try {
			pk = this.taskDueStateDao.addTaskDueState(TaskDueState);
		} catch (Exception e) {
			logger.error("任务催办状态保存失败");
			throw new BusinessException("任务催办状态保存失败");
		}
		logger.info("任务催办状态保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode = "TaskDueStateManager", description = "任务催办状态删除", detail = "任务催办状态${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskDueStateDao.deleteTaskDueState(id);
		} catch (Exception e) {
			logger.error("任务催办状态删除失败");
			throw new BusinessException("任务催办状态删除失败");
		}
		logger.info("任务催办状态删除成功");
	}

	@Override
	@Action(moduleCode = "TaskDueStateManager", description = "任务催办状态批量删除", detail = "任务催办状态${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if (StringUtil.isNotBlank(ids)) {
			String[] idArr = StringUtil.split(ids, ",");
			for (String id : idArr) {
				this.delete(id);
			}
		}
		logger.info("任务催办状态批量删除成功");
	}

	@Override
	@Action(moduleCode = "TaskDueStateManager", description = "任务催办状态修改", detail = "任务催办状态${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskDueStateEntity TaskDueState) throws BusinessException {
		try {
			TaskDueStateEntity oldEntity = get(TaskDueState.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskDueState, oldEntity);
			this.taskDueStateDao.updateTaskDueState(oldEntity);
		} catch (Exception e) {
			logger.error("任务催办状态更新失败");
			throw new BusinessException("任务催办状态更新失败");
		}
		logger.info("任务催办状态更新成功");
	}

	@Override
	public TaskDueStateEntity get(String id) {
		TaskDueStateEntity TaskDueState = null;
		try {
			TaskDueState = this.taskDueStateDao.getTaskDueState(id);
		} catch (Exception e) {
			logger.error("任务催办状态获取失败");
			//throw new BusinessException("任务催办状态获取失败");
		}
		logger.info("任务催办状态获取成功");
		return TaskDueState;
	}

	@Override
	public List<TaskDueStateEntity> queryList() throws BusinessException {
		List<TaskDueStateEntity> TaskDueStateList = new ArrayList<TaskDueStateEntity>();
		try {
			TaskDueStateList = this.taskDueStateDao.queryTaskDueStateList();
		} catch (Exception e) {
			logger.error("任务催办状态获取列表失败");
			throw new BusinessException("任务催办状态获取列表失败");
		}
		logger.info("任务催办状态获取列表成功");
		return TaskDueStateList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskDueStateDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务催办状态获取分页列表失败");
			throw new BusinessException("任务催办状态获取分页列表失败");
		}
		logger.info("任务催办状态获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param, String propertyName) {
		logger.info(propertyName + "字段唯一校验");
		return this.baseService.isUnique(TaskDueStateEntity.class, param, propertyName);
	}

	public List<TaskDueStateEntity> getByActDefAndNodeId(String actDefId, String nodeId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("actDefId", actDefId);
		param.put("nodeId", nodeId);
		return this.taskDueStateDao.findByPropertys(TaskDueStateEntity.class, param);
	}

	public void setTaskDueStateDao(TaskDueStateDao taskDueStateDao) {
		this.taskDueStateDao = taskDueStateDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.taskDueStateDao.executeHql("delete from TaskDueStateEntity t where t.actDefId='" + actDefId + "'");
	}

	@Override
	public Integer getAmountByUserTaskId(String taskId, String userId, String type, String taskDueId)
			throws BusinessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("taskId", taskId);
		param.put("createUserId", userId);
		param.put("type", type);
		param.put("taskDueId", taskDueId);
		List<TaskDueStateEntity> list = this.taskDueStateDao.findByPropertys(TaskDueStateEntity.class, param);
		return list.size();
	}

	@Override
	public Integer getAmountByTaskId(String taskId, String type, String taskDueId) throws BusinessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("taskId", taskId);
		param.put("type", type);
		param.put("taskDueId", taskDueId);
		List<TaskDueStateEntity> list = this.taskDueStateDao.findByPropertys(TaskDueStateEntity.class, param);
		return list.size();
	}

}
