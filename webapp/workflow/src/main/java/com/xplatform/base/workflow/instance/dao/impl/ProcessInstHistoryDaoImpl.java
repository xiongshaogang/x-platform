package com.xplatform.base.workflow.instance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.instance.dao.ProcessInstHistoryDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
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
@Repository("processInsHistoryDao")
public class ProcessInstHistoryDaoImpl extends CommonDao implements ProcessInstHistoryDao {

	@Override
	public String addProcessInsHistory(ProcessInstHistoryEntity ProcessInsHistory) {
		// TODO Auto-generated method stub
		return (String) this.save(ProcessInsHistory);
	}

	@Override
	public void deleteProcessInsHistory(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ProcessInstHistoryEntity.class, id);
	}

	@Override
	public void updateProcessInsHistory(ProcessInstHistoryEntity ProcessInsHistory) {
		// TODO Auto-generated method stub
		this.updateEntitie(ProcessInsHistory);
	}

	@Override
	public ProcessInstHistoryEntity getProcessInsHistory(String id) {
		// TODO Auto-generated method stub
		return (ProcessInstHistoryEntity) this.get(ProcessInstHistoryEntity.class, id);
	}

	@Override
	public List<ProcessInstHistoryEntity> queryProcessInsHistoryList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ProcessInsHistoryEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
