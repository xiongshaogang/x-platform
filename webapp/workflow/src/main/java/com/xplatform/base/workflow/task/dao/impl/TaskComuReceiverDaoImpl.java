package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskComuReceiverDao;
import com.xplatform.base.workflow.task.entity.TaskComuReceiverEntity;
/**
 * 
 * description :任务沟通接收dao实现
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
@Repository("taskComuReceiverDao")
public class TaskComuReceiverDaoImpl extends CommonDao implements TaskComuReceiverDao {

	@Override
	public String addTaskComuReceiver(TaskComuReceiverEntity TaskComuReceiver) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskComuReceiver);
	}

	@Override
	public void deleteTaskComuReceiver(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskComuReceiverEntity.class, id);
	}

	@Override
	public void updateTaskComuReceiver(TaskComuReceiverEntity TaskComuReceiver) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskComuReceiver);
	}

	@Override
	public TaskComuReceiverEntity getTaskComuReceiver(String id) {
		// TODO Auto-generated method stub
		return (TaskComuReceiverEntity) this.get(TaskComuReceiverEntity.class, id);
	}

	@Override
	public List<TaskComuReceiverEntity> queryTaskComuReceiverList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskComuReceiverEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
