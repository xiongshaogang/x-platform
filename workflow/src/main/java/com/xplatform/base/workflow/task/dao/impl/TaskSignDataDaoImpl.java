package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskSignDataDao;
import com.xplatform.base.workflow.task.entity.TaskSignDataEntity;
/**
 * 
 * description :任务会签投票dao实现
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
@Repository("taskSignDataDao")
public class TaskSignDataDaoImpl extends CommonDao implements TaskSignDataDao {

	@Override
	public String addTaskSignData(TaskSignDataEntity TaskSignData) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskSignData);
	}

	@Override
	public void deleteTaskSignData(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskSignDataEntity.class, id);
	}

	@Override
	public void updateTaskSignData(TaskSignDataEntity TaskSignData) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskSignData);
	}

	@Override
	public TaskSignDataEntity getTaskSignData(String id) {
		// TODO Auto-generated method stub
		return (TaskSignDataEntity) this.get(TaskSignDataEntity.class, id);
	}

	@Override
	public List<TaskSignDataEntity> queryTaskSignDataList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskSignDataEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
