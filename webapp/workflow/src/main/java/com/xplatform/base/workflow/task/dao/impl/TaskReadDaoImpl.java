package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskReadDao;
import com.xplatform.base.workflow.task.entity.TaskReadEntity;
/**
 * 
 * description :任务已读dao实现
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
@Repository("taskReadDao")
public class TaskReadDaoImpl extends CommonDao implements TaskReadDao {

	@Override
	public String addTaskRead(TaskReadEntity TaskRead) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskRead);
	}

	@Override
	public void deleteTaskRead(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskReadEntity.class, id);
	}

	@Override
	public void updateTaskRead(TaskReadEntity TaskRead) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskRead);
	}

	@Override
	public TaskReadEntity getTaskRead(String id) {
		// TODO Auto-generated method stub
		return (TaskReadEntity) this.get(TaskReadEntity.class, id);
	}

	@Override
	public List<TaskReadEntity> queryTaskReadList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskReadEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
