package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskDueStateDao;
import com.xplatform.base.workflow.task.entity.TaskDueStateEntity;
/**
 * 
 * description :任务催办状态dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:23:11
 *
 */
@Repository("taskDueStateDao")
public class TaskDueStateDaoImpl extends CommonDao implements TaskDueStateDao {

	@Override
	public String addTaskDueState(TaskDueStateEntity TaskDueState) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskDueState);
	}

	@Override
	public void deleteTaskDueState(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskDueStateEntity.class, id);
	}

	@Override
	public void updateTaskDueState(TaskDueStateEntity TaskDueState) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskDueState);
	}

	@Override
	public TaskDueStateEntity getTaskDueState(String id) {
		// TODO Auto-generated method stub
		return (TaskDueStateEntity) this.get(TaskDueStateEntity.class, id);
	}

	@Override
	public List<TaskDueStateEntity> queryTaskDueStateList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskDueStateEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
