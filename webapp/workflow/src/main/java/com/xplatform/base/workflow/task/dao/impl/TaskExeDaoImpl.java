package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskExeDao;
import com.xplatform.base.workflow.task.entity.TaskExeEntity;
/**
 * 
 * description :任务转办代理dao实现
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
@Repository("taskExeDao")
public class TaskExeDaoImpl extends CommonDao implements TaskExeDao {

	@Override
	public String addTaskExe(TaskExeEntity TaskExe) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskExe);
	}

	@Override
	public void deleteTaskExe(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskExeEntity.class, id);
	}

	@Override
	public void updateTaskExe(TaskExeEntity TaskExe) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskExe);
	}

	@Override
	public TaskExeEntity getTaskExe(String id) {
		// TODO Auto-generated method stub
		return (TaskExeEntity) this.get(TaskExeEntity.class, id);
	}

	@Override
	public List<TaskExeEntity> queryTaskExeList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskExeEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
