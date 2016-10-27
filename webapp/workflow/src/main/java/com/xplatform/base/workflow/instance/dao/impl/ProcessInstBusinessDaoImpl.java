package com.xplatform.base.workflow.instance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.instance.dao.ProcessInstBusinessDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstBusinessEntity;
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
@Repository("processInstBusinessDao")
public class ProcessInstBusinessDaoImpl extends CommonDao implements ProcessInstBusinessDao {

	@Override
	public String addProcessInstBusiness(ProcessInstBusinessEntity processInstBusiness) {
		// TODO Auto-generated method stub
		return (String) this.save(processInstBusiness);
	}

	@Override
	public void deleteProcessInstBusiness(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ProcessInstBusinessEntity.class, id);
	}

	@Override
	public void updateProcessInstBusiness(ProcessInstBusinessEntity processInstBusiness) {
		// TODO Auto-generated method stub
		this.updateEntitie(processInstBusiness);
	}

	@Override
	public ProcessInstBusinessEntity getProcessInstBusiness(String id) {
		// TODO Auto-generated method stub
		return (ProcessInstBusinessEntity) this.get(ProcessInstBusinessEntity.class, id);
	}

	@Override
	public List<ProcessInstBusinessEntity> queryProcessInstBusinessList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ProcessInstBusinessEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
