package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskDueDao;
import com.xplatform.base.workflow.task.entity.TaskDueEntity;
/**
 * 
 * description :任务催办dao实现
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
@Repository("taskDueDao")
public class TaskDueDaoImpl extends CommonDao implements TaskDueDao {

	@Override
	public String addTaskDue(TaskDueEntity TaskDue) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskDue);
	}

	@Override
	public void deleteTaskDue(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskDueEntity.class, id);
	}

	@Override
	public void updateTaskDue(TaskDueEntity TaskDue) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskDue);
	}

	@Override
	public TaskDueEntity getTaskDue(String id) {
		// TODO Auto-generated method stub
		return (TaskDueEntity) this.get(TaskDueEntity.class, id);
	}

	@Override
	public List<TaskDueEntity> queryTaskDueList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskDueEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
