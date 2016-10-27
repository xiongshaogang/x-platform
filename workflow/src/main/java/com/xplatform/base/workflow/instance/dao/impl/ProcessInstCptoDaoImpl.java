package com.xplatform.base.workflow.instance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.instance.dao.ProcessInstCptoDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstCptoEntity;
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
@Repository("processInstCptoDao")
public class ProcessInstCptoDaoImpl extends CommonDao implements ProcessInstCptoDao {

	@Override
	public String addProcessInsCpto(ProcessInstCptoEntity ProcessInsCpto) {
		// TODO Auto-generated method stub
		return (String) this.save(ProcessInsCpto);
	}

	@Override
	public void deleteProcessInsCpto(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ProcessInstCptoEntity.class, id);
	}

	@Override
	public void updateProcessInsCpto(ProcessInstCptoEntity ProcessInsCpto) {
		// TODO Auto-generated method stub
		this.updateEntitie(ProcessInsCpto);
	}

	@Override
	public ProcessInstCptoEntity getProcessInsCpto(String id) {
		// TODO Auto-generated method stub
		return (ProcessInstCptoEntity) this.get(ProcessInstCptoEntity.class, id);
	}

	@Override
	public List<ProcessInstCptoEntity> queryProcessInsCptoList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ProcessInsCptoEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
