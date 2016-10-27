package com.xplatform.base.workflow.instance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.instance.dao.ExecutionStackDao;
import com.xplatform.base.workflow.instance.entity.ExecutionStackEntity;
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
@Repository("executionStackDao")
public class ExecutionStackDaoImpl extends CommonDao implements ExecutionStackDao {

	@Override
	public String addExecutionStack(ExecutionStackEntity ExecutionStack) {
		// TODO Auto-generated method stub
		return (String) this.save(ExecutionStack);
	}

	@Override
	public void deleteExecutionStack(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ExecutionStackEntity.class, id);
	}

	@Override
	public void updateExecutionStack(ExecutionStackEntity ExecutionStack) {
		// TODO Auto-generated method stub
		this.updateEntitie(ExecutionStack);
	}

	@Override
	public ExecutionStackEntity getExecutionStack(String id) {
		// TODO Auto-generated method stub
		return (ExecutionStackEntity) this.get(ExecutionStackEntity.class, id);
	}

	@Override
	public List<ExecutionStackEntity> queryExecutionStackList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ExecutionStackEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
