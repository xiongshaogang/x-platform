package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskLogDao;
import com.xplatform.base.workflow.task.entity.TaskLogEntity;
/**
 * 
 * description :任务日志dao实现
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
@Repository("taskLogDao")
public class TaskLogDaoImpl extends CommonDao implements TaskLogDao {

	@Override
	public String addTaskLog(TaskLogEntity TaskLog) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskLog);
	}

	@Override
	public void deleteTaskLog(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskLogEntity.class, id);
	}

	@Override
	public void updateTaskLog(TaskLogEntity TaskLog) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskLog);
	}

	@Override
	public TaskLogEntity getTaskLog(String id) {
		// TODO Auto-generated method stub
		return (TaskLogEntity) this.get(TaskLogEntity.class, id);
	}

	@Override
	public List<TaskLogEntity> queryTaskLogList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskLogEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
