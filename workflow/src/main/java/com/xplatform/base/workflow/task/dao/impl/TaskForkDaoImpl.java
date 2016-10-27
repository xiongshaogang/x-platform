package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskForkDao;
import com.xplatform.base.workflow.task.entity.TaskForkEntity;
/**
 * 
 * description :任务分发汇总dao实现
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
@Repository("taskForkDao")
public class TaskForkDaoImpl extends CommonDao implements TaskForkDao {

	@Override
	public String addTaskFork(TaskForkEntity TaskFork) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskFork);
	}

	@Override
	public void deleteTaskFork(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskForkEntity.class, id);
	}

	@Override
	public void updateTaskFork(TaskForkEntity TaskFork) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskFork);
	}

	@Override
	public TaskForkEntity getTaskFork(String id) {
		// TODO Auto-generated method stub
		return (TaskForkEntity) this.get(TaskForkEntity.class, id);
	}

	@Override
	public List<TaskForkEntity> queryTaskForkList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskForkEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
