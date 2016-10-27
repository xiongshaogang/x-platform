package com.xplatform.base.workflow.task.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskOpinionDao;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
/**
 * 
 * description :审批意见dao实现
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
@Repository("taskOpinionDao")
public class TaskOpinionDaoImpl extends CommonDao implements TaskOpinionDao {

	@Override
	public String addTaskOpinion(TaskOpinionEntity TaskOpinion) {
		// TODO Auto-generated method stub
		return (String) this.save(TaskOpinion);
	}

	@Override
	public void deleteTaskOpinion(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskOpinionEntity.class, id);
	}

	@Override
	public void updateTaskOpinion(TaskOpinionEntity TaskOpinion) {
		// TODO Auto-generated method stub
		this.updateEntitie(TaskOpinion);
	}

	@Override
	public TaskOpinionEntity getTaskOpinion(String id) {
		// TODO Auto-generated method stub
		return (TaskOpinionEntity) this.get(TaskOpinionEntity.class, id);
	}

	@Override
	public List<TaskOpinionEntity> queryTaskOpinionList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TaskOpinionEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
