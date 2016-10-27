package com.xplatform.base.workflow.task.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.dao.TaskNodeStatusDao;
import com.xplatform.base.workflow.task.entity.TaskNodeStatusEntity;
/**
 * 
 * description :流程实例dao实现
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
@Repository("processInsStatusDao")
public class TaskNodeStatusDaoImpl extends CommonDao implements TaskNodeStatusDao {

	@Override
	public String addProcessInsStatus(TaskNodeStatusEntity ProcessInsStatus) {
		// TODO Auto-generated method stub
		return (String) this.save(ProcessInsStatus);
	}

	@Override
	public void deleteProcessInsStatus(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TaskNodeStatusEntity.class, id);
	}

	@Override
	public void updateProcessInsStatus(TaskNodeStatusEntity ProcessInsStatus) {
		// TODO Auto-generated method stub
		this.updateEntitie(ProcessInsStatus);
	}

	@Override
	public TaskNodeStatusEntity getProcessInsStatus(String id) {
		// TODO Auto-generated method stub
		return (TaskNodeStatusEntity) this.get(TaskNodeStatusEntity.class, id);
	}

	@Override
	public List<TaskNodeStatusEntity> queryProcessInsStatusList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ProcessInsStatusEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
